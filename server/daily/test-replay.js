/**
 * Local regression test for the replay leaderboard (one best replay per player
 * per UTC day). Run with: node test-replay.js
 */
const assert = require('assert');
const fs = require('fs');
const os = require('os');
const path = require('path');

const dbPath = path.join(os.tmpdir(), `mlpd-replay-test-${process.pid}.db`);
try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }
process.env.DAILY_DB = dbPath;

const db = require('./db');

function info(score, depth) {
  return {
    heroClass: 'MAGE',
    heroSubClass: 'NONE',
    heroLevel: 20,
    score,
    depth,
    duration: 100,
    enemiesSlain: 10,
    goldCollected: 100,
    foodEaten: 5,
    itemsCrafted: 1,
    challenges: 3,
    str: 12,
    version: 'test',
    seed: 1,
    customSeed: '2026-11-14',
    gameMode: 0,
    difficulty: 2
  };
}

(async () => {
  await db.initDatabase();

  const date = '2026-11-14';
  db.submitReplayScore(date, 'A', info(300, 5), 2, 0, 0, '{}', 'p1');
  const bestA = db.submitReplayScore(date, 'A', info(800, 9), 2, 0, 0, '{}', 'p1');
  // Lower later replay must not replace the best.
  db.submitReplayScore(date, 'A', info(400, 6), 2, 0, 0, '{}', 'p1');
  db.submitReplayScore(date, 'B', info(600, 7), 2, 0, 0, '{}', 'p2');

  const board = db.getReplayLeaderboard(date, 1, 20);
  assert.strictEqual(board.total, 2, 'one row per player');
  assert.deepStrictEqual(
      board.entries.map(e => [e.rank, e.playerName, e.score]),
      [[1, 'A', 800], [2, 'B', 600]],
      'best replay score only'
  );

  assert.strictEqual(db.getReplayTotalPlayers(date), 2);
  assert.strictEqual(db.getReplayPlayerRank(date, 800, bestA.id), 1);

  console.log('test-replay: OK');
  try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }
})().catch((e) => {
  console.error(e);
  try { fs.unlinkSync(dbPath); } catch (err) { /* ignore */ }
  process.exit(1);
});
