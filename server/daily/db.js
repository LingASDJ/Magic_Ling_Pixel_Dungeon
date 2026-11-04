const fs = require('fs');
const path = require('path');

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

  saveToDisk();
  console.log('[DB] 数据库初始化完成');
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

module.exports = {
  initDatabase,
  submitScore,
  hasSubmitted,
  getTotalPlayers,
  getPlayerRank,
  getLeaderboard
};
