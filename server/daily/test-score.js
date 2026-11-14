/**
 * Regression test for authoritative daily score reconciliation.
 *
 * Covers the 2026-11-12 row that exposed the bug: an old client applied the
 * 0.5 custom-seed penalty to a daily run and stored 29,017,816 while the
 * snapshot components produce 58,035,638. Also verifies that the startup
 * migration repairs an existing database (v2 marker).
 *
 * Run with: node test-score.js
 */
const assert = require('assert');
const fs = require('fs');
const os = require('os');
const path = require('path');
const initSqlJs = require('sql.js');
const { expectedDailyScore, reconcileDailyScore } = require('./utils/bundle');

const componentGameData = {
  prog_score: 50000,
  tres_score: 20000,
  expl_score: 7200,
  tot_boss: 37340,
  tot_quest: 10000,
  win_mult: 2,
  chal_mult: 11.649999618530273,
  bad_mult: 6.5,
  good_mult: 2.000000238418579,
  conduct: ['HARD'],
  challenges: 60027,
  // Old client value: components * 0.5 (custom-seed penalty).
  total_score: 29017816
};

const EXPECTED = 58035640; // matches the client's int/float calculation exactly

// 1. Pure reconciliation.
assert.strictEqual(expectedDailyScore(componentGameData, 2), EXPECTED);
assert.strictEqual(reconcileDailyScore(29017816, componentGameData, 2), EXPECTED,
    'half score must be replaced by the component score');
assert.strictEqual(reconcileDailyScore(EXPECTED, componentGameData, 2), EXPECTED,
    'correct score must stay unchanged');
assert.strictEqual(reconcileDailyScore(12345678, componentGameData, 2), EXPECTED,
    'any clearly wrong stored score must be replaced');
assert.strictEqual(reconcileDailyScore(123, { total_score: 123 }, 2), 123,
    'snapshot without components keeps the stored score');

// 2. Startup migration on an old database.
const dbPath = path.join(os.tmpdir(), `mlpd-score-test-${process.pid}.db`);
try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }

(async () => {
  const SQL = await initSqlJs();
  const old = new SQL.Database();
  old.run(`
    CREATE TABLE daily_scores (
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
  old.run(
      `INSERT INTO daily_scores (date, player_name, hero_class, score, won, depth, game_data)
       VALUES (?, ?, ?, ?, ?, ?, ?)`,
      ['2026-11-12', 'knox-小刻', 'MAGE', 29017816, 1, 30,
        JSON.stringify(componentGameData)]);
  fs.writeFileSync(dbPath, Buffer.from(old.export()));
  old.close();

  process.env.DAILY_DB = dbPath;
  const db = require('./db');
  await db.initDatabase();

  const board = db.getLeaderboard('2026-11-12', 1, 20);
  assert.strictEqual(board.entries.length, 1);
  assert.strictEqual(board.entries[0].score, EXPECTED,
      'startup migration must repair the stored score');

  console.log('test-score: OK');
  try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }
})().catch((e) => {
  console.error(e);
  try { fs.unlinkSync(dbPath); } catch (err) { /* ignore */ }
  process.exit(1);
});
