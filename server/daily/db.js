const fs = require('fs');
const path = require('path');
const { reconcileDailyScore } = require('./utils/bundle');

let db = null;
// Allow tests / deployments to point at a different database file.
const dbPath = process.env.DAILY_DB || path.join(__dirname, 'daily.db');

// Complete column list used for ALTER TABLE compatibility with old databases.
const ALL_COLUMNS = [
  { name: 'hero_subclass', def: 'TEXT DEFAULT "NONE"' },
  { name: 'hero_level', def: 'INTEGER NOT NULL DEFAULT 1' },
  { name: 'armor_tier', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'duration', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'enemies_slain', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'gold_collected', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'food_eaten', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'items_crafted', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'challenges', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'str', def: 'INTEGER NOT NULL DEFAULT 10' },
  { name: 'version', def: 'TEXT DEFAULT ""' },
  { name: 'seed', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'custom_seed', def: 'TEXT DEFAULT ""' },
  { name: 'game_mode', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'difficulty', def: 'INTEGER NOT NULL DEFAULT 1' },
  { name: 'mode', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'challenge_tier', def: 'INTEGER NOT NULL DEFAULT 0' },
  { name: 'player_id', def: 'TEXT DEFAULT NULL' },
  { name: 'daily_replay', def: 'INTEGER NOT NULL DEFAULT 0' }
];

async function initDatabase() {
  const initSqlJs = require('sql.js');
  const SQL = await initSqlJs();

  if (fs.existsSync(dbPath)) {
    const fileBuffer = fs.readFileSync(dbPath);
    db = new SQL.Database(fileBuffer);
  } else {
    db = new SQL.Database();
  }

  db.run(`
    CREATE TABLE IF NOT EXISTS daily_scores (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      date TEXT NOT NULL,
      player_name TEXT NOT NULL,
      hero_class TEXT NOT NULL,
      score INTEGER NOT NULL,
      won INTEGER NOT NULL DEFAULT 0,
      depth INTEGER NOT NULL DEFAULT 0,
      game_data TEXT,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
  `);

  // Compatibility with old databases: add any missing columns, never drop data.
  try {
    const cols = db.exec('PRAGMA table_info(daily_scores)');
    if (cols.length > 0) {
      const existingCols = cols[0].values.map(r => r[1]);
      for (const col of ALL_COLUMNS) {
        if (!existingCols.includes(col.name)) {
          console.log(`[DB] 添加缺失字段: ${col.name}`);
          db.run(`ALTER TABLE daily_scores ADD COLUMN ${col.name} ${col.def}`);
        }
      }
    }
  } catch (e) {
    console.log('[DB] 字段兼容检查跳过:', e.message);
  }

  db.run('CREATE INDEX IF NOT EXISTS idx_daily_scores_date ON daily_scores(date)');
  db.run('CREATE INDEX IF NOT EXISTS idx_daily_scores_date_score ON daily_scores(date, score DESC)');
  db.run('CREATE INDEX IF NOT EXISTS idx_daily_scores_filter ON daily_scores(date, difficulty, mode, challenge_tier, score DESC)');
  // One submitted score per player per UTC day (NULL player_id rows are old data).
  db.run('CREATE UNIQUE INDEX IF NOT EXISTS idx_daily_scores_date_player ON daily_scores(date, player_id)');

  // 重玩榜：每名玩家每天只保留最高分的一条重玩记录。
  db.run(`
    CREATE TABLE IF NOT EXISTS replay_scores (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      date TEXT NOT NULL,
      player_id TEXT NOT NULL,
      player_name TEXT NOT NULL,
      hero_class TEXT NOT NULL,
      hero_subclass TEXT DEFAULT 'NONE',
      hero_level INTEGER NOT NULL DEFAULT 1,
      armor_tier INTEGER NOT NULL DEFAULT 0,
      score INTEGER NOT NULL,
      won INTEGER NOT NULL DEFAULT 0,
      depth INTEGER NOT NULL DEFAULT 0,
      duration INTEGER NOT NULL DEFAULT 0,
      enemies_slain INTEGER NOT NULL DEFAULT 0,
      gold_collected INTEGER NOT NULL DEFAULT 0,
      food_eaten INTEGER NOT NULL DEFAULT 0,
      items_crafted INTEGER NOT NULL DEFAULT 0,
      challenges INTEGER NOT NULL DEFAULT 0,
      str INTEGER NOT NULL DEFAULT 10,
      version TEXT DEFAULT '',
      seed INTEGER NOT NULL DEFAULT 0,
      custom_seed TEXT DEFAULT '',
      game_mode INTEGER NOT NULL DEFAULT 0,
      difficulty INTEGER NOT NULL DEFAULT 1,
      mode INTEGER NOT NULL DEFAULT 0,
      challenge_tier INTEGER NOT NULL DEFAULT 0,
      game_data TEXT,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
  `);
  db.run('CREATE UNIQUE INDEX IF NOT EXISTS idx_replay_scores_date_player ON replay_scores(date, player_id)');
  db.run('CREATE INDEX IF NOT EXISTS idx_replay_scores_date_score ON replay_scores(date, score DESC)');

  repairLegacyDailyScores();

  saveToDisk();
  console.log('[DB] 数据库初始化完成');
}

/**
 * One-time repair for daily/replay scores submitted by old clients.
 *
 * v1 only fixed rows that were exactly half of the expected score. v2 uses
 * the score components stored in the snapshot as the source of truth, so
 * every row with a complete component set is corrected to the authoritative
 * daily score (including rows the old client computed with the 0.5
 * custom-seed penalty). Guarded by a new meta marker so it runs once even on
 * databases where the v1 migration already ran.
 */
function repairLegacyDailyScores() {
  db.run('CREATE TABLE IF NOT EXISTS meta (key TEXT PRIMARY KEY, value TEXT)');

  const marker = db.prepare("SELECT value FROM meta WHERE key = 'daily_score_repair_v2' LIMIT 1");
  marker.step();
  const done = marker.getAsObject();
  marker.free();
  if (done && done.value) return;

  const dailyUpdates = repairScoreTable('daily_scores');
  const replayUpdates = repairScoreTable('replay_scores');

  db.run('INSERT OR REPLACE INTO meta (key, value) VALUES (?, ?)',
      ['daily_score_repair_v2', new Date().toISOString()]);

  const sample = dailyUpdates.concat(replayUpdates).slice(0, 5)
      .map(u => `${u.id}:${u.oldScore}->${u.newScore}`).join(', ');
  console.log(`[DB] 旧分数修复(v2): daily=${dailyUpdates.length} replay=${replayUpdates.length}`
      + (sample ? `，前几行: ${sample}` : ''));
}

/**
 * Recomputes every row of a score table from its snapshot components.
 * @param {string} table internal table name (daily_scores / replay_scores)
 * @returns {Array<{id:number, oldScore:number, newScore:number}>} applied updates
 */
function repairScoreTable(table) {
  const scan = db.prepare(`SELECT id, score, difficulty, game_data
                           FROM ${table}
                           WHERE game_data IS NOT NULL`);
  const updates = [];
  while (scan.step()) {
    const row = scan.getAsObject();
    const fixed = reconcileDailyScore(
        row.score, row.game_data, row.difficulty == null ? 1 : row.difficulty);
    if (fixed !== row.score) {
      updates.push({ id: row.id, oldScore: row.score, newScore: fixed });
    }
  }
  scan.free();

  if (updates.length > 0) {
    const update = db.prepare(`UPDATE ${table} SET score = ? WHERE id = ?`);
    for (const u of updates) {
      update.run([u.newScore, u.id]);
    }
    update.free();
  }
  return updates;
}

function saveToDisk() {
  const data = db.export();
  fs.writeFileSync(dbPath, Buffer.from(data));
}

/**
 * @returns {boolean} true if this player already submitted a score for the date
 */
function hasSubmitted(date, playerId) {
  if (!playerId) return false;
  const stmt = db.prepare('SELECT id FROM daily_scores WHERE date = ? AND player_id = ? LIMIT 1');
  stmt.bind([date, playerId]);
  const exists = stmt.step();
  stmt.free();
  return exists;
}

function submitScore(date, playerName, gameInfo, difficulty, mode, challengeTier, gameData, playerId, dailyReplay) {
  const stmt = db.prepare(`
    INSERT INTO daily_scores (
      date, player_name, hero_class, hero_subclass, hero_level, armor_tier,
      score, won, depth, duration, enemies_slain, gold_collected, food_eaten,
      items_crafted, challenges, str, version, seed, custom_seed, game_mode,
      difficulty, mode, challenge_tier, game_data, player_id, daily_replay
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `);
  stmt.run([
    date,
    playerName,
    gameInfo.heroClass,
    gameInfo.heroSubClass || 'NONE',
    gameInfo.heroLevel || 1,
    gameInfo.armorTier || 0,
    gameInfo.score || 0,
    gameInfo.won ? 1 : 0,
    gameInfo.depth || 0,
    gameInfo.duration || 0,
    gameInfo.enemiesSlain || 0,
    gameInfo.goldCollected || 0,
    gameInfo.foodEaten || 0,
    gameInfo.itemsCrafted || 0,
    gameInfo.challenges || 0,
    gameInfo.str || 10,
    gameInfo.version || '',
    gameInfo.seed || 0,
    gameInfo.customSeed || '',
    gameInfo.gameMode || 0,
    difficulty != null ? difficulty : 1,
    mode != null ? mode : 0,
    challengeTier != null ? challengeTier : 0,
    gameData,
    playerId || null,
    dailyReplay ? 1 : 0
  ]);
  stmt.free();

  const result = db.exec('SELECT last_insert_rowid() as id');
  const id = result[0].values[0][0];
  saveToDisk();
  return id;
}

function getTotalPlayers(date, filters = {}) {
  let sql = 'SELECT COUNT(*) as count FROM daily_scores WHERE date = ?';
  const params = [date];

  if (filters.difficulty != null) {
    sql += ' AND difficulty = ?';
    params.push(filters.difficulty);
  }
  if (filters.mode != null) {
    sql += ' AND mode = ?';
    params.push(filters.mode);
  }
  if (filters.challengeTier != null) {
    sql += ' AND challenge_tier = ?';
    params.push(filters.challengeTier);
  }

  const stmt = db.prepare(sql);
  stmt.bind(params);
  stmt.step();
  const row = stmt.getAsObject();
  stmt.free();
  return row.count;
}

function getPlayerRank(date, score, id, filters = {}) {
  let sql = `
    SELECT COUNT(*) as rank FROM daily_scores
    WHERE date = ? AND (score > ? OR (score = ? AND id < ?))
  `;
  const params = [date, score, score, id];

  if (filters.difficulty != null) {
    sql += ' AND difficulty = ?';
    params.push(filters.difficulty);
  }
  if (filters.mode != null) {
    sql += ' AND mode = ?';
    params.push(filters.mode);
  }
  if (filters.challengeTier != null) {
    sql += ' AND challenge_tier = ?';
    params.push(filters.challengeTier);
  }

  const stmt = db.prepare(sql);
  stmt.bind(params);
  stmt.step();
  const row = stmt.getAsObject();
  stmt.free();
  return row.rank + 1;
}

function getLeaderboard(date, page = 1, pageSize = 20, filters = {}) {
  const offset = (page - 1) * pageSize;

  let whereSql = 'WHERE date = ?';
  const params = [date];

  if (filters.difficulty != null) {
    whereSql += ' AND difficulty = ?';
    params.push(filters.difficulty);
  }
  if (filters.mode != null) {
    whereSql += ' AND mode = ?';
    params.push(filters.mode);
  }
  if (filters.challengeTier != null) {
    whereSql += ' AND challenge_tier = ?';
    params.push(filters.challengeTier);
  }

  const countStmt = db.prepare(`SELECT COUNT(*) as total FROM daily_scores ${whereSql}`);
  countStmt.bind(params);
  countStmt.step();
  const totalRow = countStmt.getAsObject();
  countStmt.free();
  const total = totalRow.total;

  const dataParams = [...params, pageSize, offset];
  const stmt = db.prepare(`
    SELECT
      date, player_name, player_id, hero_class, hero_subclass, hero_level, armor_tier,
      score, won, depth, duration, enemies_slain, gold_collected, food_eaten,
      items_crafted, challenges, str, version, seed, custom_seed, game_mode,
      difficulty, mode, challenge_tier, daily_replay, game_data, created_at
    FROM daily_scores
    ${whereSql}
    ORDER BY score DESC, id ASC
    LIMIT ? OFFSET ?
  `);
  stmt.bind(dataParams);

  const entries = [];
  let index = 0;
  while (stmt.step()) {
    const row = stmt.getAsObject();
    entries.push({
      rank: offset + index + 1,
      date: row.date,
      playerId: row.player_id || '',
      playerName: row.player_name,
      heroClass: row.hero_class,
      heroSubClass: row.hero_subclass,
      heroLevel: row.hero_level,
      armorTier: row.armor_tier,
      score: row.score,
      won: row.won === 1,
      depth: row.depth,
      duration: row.duration,
      enemiesSlain: row.enemies_slain,
      goldCollected: row.gold_collected,
      foodEaten: row.food_eaten,
      itemsCrafted: row.items_crafted,
      challenges: row.challenges,
      challengeCount: row.challenge_tier,
      str: row.str,
      version: row.version,
      seed: row.seed,
      customSeed: row.custom_seed,
      gameMode: row.game_mode,
      difficulty: row.difficulty,
      mode: row.mode,
      dailyReplay: row.daily_replay === 1,
      gameData: row.game_data || '',
      createdAt: row.created_at
    });
    index++;
  }
  stmt.free();

  return { total, entries };
}

/**
 * Weekly leaderboard: each player's best run in [weekStart, weekEnd] (UTC).
 * Ties are broken by depth and then by the earlier submission, matching the
 * daily board's ordering. Counts how many days the player placed 1st/2nd/3rd.
 *
 * @param {string} weekStart yyyy-MM-dd (Monday)
 * @param {string} weekEnd yyyy-MM-dd (Sunday)
 * @param {number} page 1-based
 * @param {number} pageSize
 */
function getWeeklyLeaderboard(weekStart, weekEnd, page = 1, pageSize = 20) {
  const stmt = db.prepare(`
    SELECT
      id, date, player_name, player_id, hero_class, hero_subclass, hero_level, armor_tier,
      score, won, depth, duration, enemies_slain, gold_collected, food_eaten,
      items_crafted, challenges, str, version, seed, custom_seed, game_mode,
      difficulty, mode, challenge_tier, daily_replay, game_data, created_at
    FROM daily_scores
    WHERE date >= ? AND date <= ? AND daily_replay = 0
    ORDER BY date ASC, score DESC, id ASC
  `);
  stmt.bind([weekStart, weekEnd]);

  const players = new Map();
  let currentDate = null;
  let dayRank = 0;

  while (stmt.step()) {
    const row = stmt.getAsObject();

    if (row.date !== currentDate) {
      currentDate = row.date;
      dayRank = 1;
    } else {
      dayRank++;
    }

    const key = row.player_id
        ? 'id:' + row.player_id
        : 'name:' + (row.player_name || '') + '|' + (row.hero_class || '');

    let player = players.get(key);
    if (!player) {
      player = {
        key,
        best: row,
        dates: new Set(),
        gold: 0,
        silver: 0,
        bronze: 0
      };
      players.set(key, player);
    } else if (row.score > player.best.score
        || (row.score === player.best.score && row.depth > player.best.depth)
        || (row.score === player.best.score && row.depth === player.best.depth
            && row.id < player.best.id)) {
      player.best = row;
    }

    player.dates.add(row.date);
    if (dayRank === 1) player.gold++;
    else if (dayRank === 2) player.silver++;
    else if (dayRank === 3) player.bronze++;
  }
  stmt.free();

  const list = Array.from(players.values());
  list.sort((a, b) => {
    if (a.best.score !== b.best.score) return b.best.score - a.best.score;
    if (a.best.depth !== b.best.depth) return b.best.depth - a.best.depth;
    const an = (a.best.player_name || '').toLowerCase();
    const bn = (b.best.player_name || '').toLowerCase();
    return an < bn ? -1 : (an > bn ? 1 : 0);
  });

  const total = list.length;
  const offset = (page - 1) * pageSize;
  const entries = list.slice(offset, offset + pageSize).map((player, index) => {
    const row = player.best;
    return {
      rank: offset + index + 1,
      date: row.date,
      playerId: row.player_id || '',
      playerName: row.player_name,
      heroClass: row.hero_class,
      heroSubClass: row.hero_subclass,
      heroLevel: row.hero_level,
      armorTier: row.armor_tier,
      score: row.score,
      won: row.won === 1,
      depth: row.depth,
      duration: row.duration,
      enemiesSlain: row.enemies_slain,
      goldCollected: row.gold_collected,
      foodEaten: row.food_eaten,
      itemsCrafted: row.items_crafted,
      challenges: row.challenges,
      challengeCount: row.challenge_tier,
      str: row.str,
      version: row.version,
      seed: row.seed,
      customSeed: row.custom_seed,
      gameMode: row.game_mode,
      difficulty: row.difficulty,
      mode: row.mode,
      dailyReplay: row.daily_replay === 1,
      gameData: row.game_data || '',
      createdAt: row.created_at,
      runs: player.dates.size,
      gold: player.gold,
      silver: player.silver,
      bronze: player.bronze
    };
  });

  return { total, entries };
}

/**
 * Replay leaderboard: one row per player per UTC day, keeping only the highest
 * replay score. Returns the same shape as the daily leaderboard.
 */
function submitReplayScore(date, playerName, gameInfo, difficulty, mode, challengeTier, gameData, playerId) {
  const find = db.prepare('SELECT id, score FROM replay_scores WHERE date = ? AND player_id = ? LIMIT 1');
  find.bind([date, playerId]);
  find.step();
  const existing = find.getAsObject();
  find.free();

  const score = gameInfo.score || 0;
  if (existing.id) {
    if (score > existing.score) {
      const upd = db.prepare(`
        UPDATE replay_scores SET
          player_name=?, hero_class=?, hero_subclass=?, hero_level=?, armor_tier=?,
          score=?, won=?, depth=?, duration=?, enemies_slain=?, gold_collected=?,
          food_eaten=?, items_crafted=?, challenges=?, str=?, version=?, seed=?,
          custom_seed=?, game_mode=?, difficulty=?, mode=?, challenge_tier=?,
          game_data=?, created_at=CURRENT_TIMESTAMP
        WHERE id=?
      `);
      upd.run([
        playerName,
        gameInfo.heroClass,
        gameInfo.heroSubClass || 'NONE',
        gameInfo.heroLevel || 1,
        gameInfo.armorTier || 0,
        score,
        gameInfo.won ? 1 : 0,
        gameInfo.depth || 0,
        gameInfo.duration || 0,
        gameInfo.enemiesSlain || 0,
        gameInfo.goldCollected || 0,
        gameInfo.foodEaten || 0,
        gameInfo.itemsCrafted || 0,
        gameInfo.challenges || 0,
        gameInfo.str || 10,
        gameInfo.version || '',
        gameInfo.seed || 0,
        gameInfo.customSeed || '',
        gameInfo.gameMode || 0,
        difficulty != null ? difficulty : 1,
        mode != null ? mode : 0,
        challengeTier != null ? challengeTier : 0,
        gameData,
        existing.id
      ]);
      upd.free();
      saveToDisk();
    }
    return { id: existing.id, score: Math.max(existing.score, score), updated: score > existing.score };
  }

  const stmt = db.prepare(`
    INSERT INTO replay_scores (
      date, player_id, player_name, hero_class, hero_subclass, hero_level, armor_tier,
      score, won, depth, duration, enemies_slain, gold_collected, food_eaten,
      items_crafted, challenges, str, version, seed, custom_seed, game_mode,
      difficulty, mode, challenge_tier, game_data
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
  `);
  stmt.run([
    date,
    playerId,
    playerName,
    gameInfo.heroClass,
    gameInfo.heroSubClass || 'NONE',
    gameInfo.heroLevel || 1,
    gameInfo.armorTier || 0,
    score,
    gameInfo.won ? 1 : 0,
    gameInfo.depth || 0,
    gameInfo.duration || 0,
    gameInfo.enemiesSlain || 0,
    gameInfo.goldCollected || 0,
    gameInfo.foodEaten || 0,
    gameInfo.itemsCrafted || 0,
    gameInfo.challenges || 0,
    gameInfo.str || 10,
    gameInfo.version || '',
    gameInfo.seed || 0,
    gameInfo.customSeed || '',
    gameInfo.gameMode || 0,
    difficulty != null ? difficulty : 1,
    mode != null ? mode : 0,
    challengeTier != null ? challengeTier : 0,
    gameData
  ]);
  stmt.free();

  const result = db.exec('SELECT last_insert_rowid() as id');
  const id = result[0].values[0][0];
  saveToDisk();
  return { id, score, updated: true };
}

function getReplayLeaderboard(date, page = 1, pageSize = 20) {
  const offset = (page - 1) * pageSize;

  const countStmt = db.prepare('SELECT COUNT(*) as total FROM replay_scores WHERE date = ?');
  countStmt.bind([date]);
  countStmt.step();
  const total = countStmt.getAsObject().total;
  countStmt.free();

  const stmt = db.prepare(`
    SELECT
      date, player_name, player_id, hero_class, hero_subclass, hero_level, armor_tier,
      score, won, depth, duration, enemies_slain, gold_collected, food_eaten,
      items_crafted, challenges, str, version, seed, custom_seed, game_mode,
      difficulty, mode, challenge_tier, game_data, created_at
    FROM replay_scores
    WHERE date = ?
    ORDER BY score DESC, id ASC
    LIMIT ? OFFSET ?
  `);
  stmt.bind([date, pageSize, offset]);

  const entries = [];
  let index = 0;
  while (stmt.step()) {
    const row = stmt.getAsObject();
    entries.push({
      rank: offset + index + 1,
      date: row.date,
      playerId: row.player_id || '',
      playerName: row.player_name,
      heroClass: row.hero_class,
      heroSubClass: row.hero_subclass,
      heroLevel: row.hero_level,
      armorTier: row.armor_tier,
      score: row.score,
      won: row.won === 1,
      depth: row.depth,
      duration: row.duration,
      enemiesSlain: row.enemies_slain,
      goldCollected: row.gold_collected,
      foodEaten: row.food_eaten,
      itemsCrafted: row.items_crafted,
      challenges: row.challenges,
      challengeCount: row.challenge_tier,
      str: row.str,
      version: row.version,
      seed: row.seed,
      customSeed: row.custom_seed,
      gameMode: row.game_mode,
      difficulty: row.difficulty,
      mode: row.mode,
      replay: true,
      dailyReplay: true,
      gameData: row.game_data || '',
      createdAt: row.created_at
    });
    index++;
  }
  stmt.free();

  return { total, entries };
}

function getReplayPlayerRank(date, score, id) {
  const stmt = db.prepare(`
    SELECT COUNT(*) as rank FROM replay_scores
    WHERE date = ? AND (score > ? OR (score = ? AND id < ?))
  `);
  stmt.bind([date, score, score, id]);
  stmt.step();
  const rank = stmt.getAsObject().rank;
  stmt.free();
  return rank + 1;
}

function getReplayTotalPlayers(date) {
  const stmt = db.prepare('SELECT COUNT(*) as count FROM replay_scores WHERE date = ?');
  stmt.bind([date]);
  stmt.step();
  const count = stmt.getAsObject().count;
  stmt.free();
  return count;
}

module.exports = {
  initDatabase,
  submitScore,
  hasSubmitted,
  getTotalPlayers,
  getPlayerRank,
  getLeaderboard,
  getWeeklyLeaderboard,
  submitReplayScore,
  getReplayLeaderboard,
  getReplayPlayerRank,
  getReplayTotalPlayers
};
