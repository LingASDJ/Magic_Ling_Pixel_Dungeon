/**
 * Local regression test for the weekly leaderboard aggregation and the legacy
 * daily score repair. Run with: node test-weekly.js
 * Uses a throwaway database in the OS temp directory.
 */
const assert = require('assert');
const fs = require('fs');
const os = require('os');
const path = require('path');

const dbPath = path.join(os.tmpdir(), `mlpd-weekly-test-${process.pid}.db`);
try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }
process.env.DAILY_DB = dbPath;

const db = require('./db');
const { reconcileDailyScore, expectedDailyScore } = require('./utils/bundle');

function submit(name, id, score, depth, date, difficulty) {
  db.submitScore(
      date,
      name,
      {
        heroClass: 'MAGE',
        heroSubClass: 'NONE',
        heroLevel: 10,
        score,
        depth,
        duration: 100,
        enemiesSlain: 1,
        goldCollected: 1,
        foodEaten: 1,
        itemsCrafted: 0,
        challenges: 0,
        str: 10,
        version: 'test',
        seed: 1,
        customSeed: date,
        gameMode: 0,
        difficulty
      },
      difficulty,
      0,
      0,
      '{}',
      id,
      0
  );
}

(async () => {
  await db.initDatabase();

  submit('A', 'p1', 100, 5, '2026-11-09', 2);
  submit('B', 'p2', 200, 6, '2026-11-09', 2);
  submit('C', 'p3', 300, 7, '2026-11-09', 2);
  submit('A', 'p1', 500, 9, '2026-11-10', 2);
  submit('C', 'p3', 250, 8, '2026-11-10', 2);
  submit('B', 'p2', 150, 4, '2026-11-11', 2);
  submit('D', 'p4', 50, 2, '2026-11-11', 2);

  const week = db.getWeeklyLeaderboard('2026-11-09', '2026-11-15', 1, 20);
  assert.strictEqual(week.total, 4, 'weekly player count');
  assert.deepStrictEqual(
      week.entries.map(e => [e.rank, e.playerName, e.score, e.runs, e.gold, e.silver, e.bronze]),
      [
        [1, 'A', 500, 2, 1, 0, 1],
        [2, 'C', 300, 2, 1, 1, 0],
        [3, 'B', 200, 2, 1, 1, 0],
        [4, 'D', 50, 1, 0, 1, 0]
      ],
      'weekly aggregation'
  );

  // Pagination must preserve the same global ranks.
  const page2 = db.getWeeklyLeaderboard('2026-11-09', '2026-11-15', 2, 2);
  assert.deepStrictEqual(page2.entries.map(e => e.rank), [3, 4], 'weekly pagination');

  // Legacy half-score repair: full = base * win * chal * HARD(2.5).
  const snapshot = {
    prog_score: 50000,
    tres_score: 20000,
    expl_score: 7200,
    tot_boss: 37340,
    tot_quest: 10000,
    win_mult: 2,
    chal_mult: 11.649999618530273,
    bad_mult: 0,
    good_mult: 0,
    seedCustom: true
  };
  const full = expectedDailyScore(snapshot, 2);
  assert.strictEqual(reconcileDailyScore(Math.floor(full / 2), snapshot, 2), full,
      'legacy half score is repaired');
  assert.strictEqual(reconcileDailyScore(full, snapshot, 2), full,
      'full score is unchanged');

  console.log('test-weekly: OK');
  try { fs.unlinkSync(dbPath); } catch (e) { /* ignore */ }
})().catch((e) => {
  console.error(e);
  try { fs.unlinkSync(dbPath); } catch (err) { /* ignore */ }
  process.exit(1);
});
