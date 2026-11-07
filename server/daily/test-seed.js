/**
 * Daily seed v2 regression checks.
 * Run from server/daily: node test-seed.js
 */
const assert = require('assert');
const {
  TOTAL_SEEDS,
  DAILY_SEED_V2_START,
  generateDailyConfig,
  generateDailySeed,
  generateLegacyDailySeed,
  generateDailySeedV2
} = require('./utils/seed');

function check(name, fn) {
  try {
    fn();
    console.log('ok - ' + name);
  } catch (e) {
    console.error('FAILED - ' + name);
    throw e;
  }
}

check('cutoff is 2026-11-08', () => {
  assert.strictEqual(DAILY_SEED_V2_START, '2026-11-08');
});

check('legacy date keeps UTC-midnight + TOTAL_SEEDS', () => {
  const date = '2026-11-07';
  const expected = Date.UTC(2026, 10, 7) + TOTAL_SEEDS;
  assert.strictEqual(generateDailySeed(date), expected);
  assert.strictEqual(generateLegacyDailySeed(date), expected);
  assert.strictEqual(generateDailyConfig(date).seedVersion, 1);
  assert.ok(generateDailySeed(date) >= TOTAL_SEEDS);
});

check('v2 date is in [0, TOTAL_SEEDS)', () => {
  const date = '2026-11-08';
  const seed = generateDailySeed(date);
  assert.strictEqual(generateDailyConfig(date).seedVersion, 2);
  assert.ok(seed >= 0 && seed < TOTAL_SEEDS, `seed out of range: ${seed}`);
  assert.strictEqual(generateDailySeedV2(date), seed);
});

check('v2 is deterministic', () => {
  assert.strictEqual(generateDailySeed('2026-11-08'), generateDailySeed('2026-11-08'));
  assert.strictEqual(generateDailySeedV2('2026-11-08'), generateDailySeedV2('2026-11-08'));
});

check('v2 has no daily linear pattern', () => {
  const seeds = [];
  for (let d = 8; d <= 22; d++) {
    seeds.push(generateDailySeed(`2026-11-${String(d).padStart(2, '0')}`));
  }
  const diffs = new Set();
  for (let i = 1; i < seeds.length; i++) {
    diffs.add(seeds[i] - seeds[i - 1]);
  }
  assert.ok(diffs.size > 10, `seed differences are too regular: ${diffs.size} distinct`);
  assert.ok(!diffs.has(86400000), 'seed difference must not be exactly one day');
  assert.strictEqual(new Set(seeds).size, seeds.length, 'v2 seeds must be unique');
});

check('legacy and v2 differ for the same date', () => {
  const date = '2026-11-08';
  assert.notStrictEqual(generateDailySeed(date), generateLegacyDailySeed(date));
});

check('daily config ranges are valid for 60 v2 days', () => {
  for (let i = 0; i < 60; i++) {
    const day = new Date(Date.UTC(2026, 10, 8 + i));
    const date = day.toISOString().slice(0, 10);
    const config = generateDailyConfig(date);
    assert.ok(config.difficulty === 'NORMAL' || config.difficulty === 'HARD');
    assert.ok(config.challengeCount >= 1 && config.challengeCount <= 16);
    assert.ok(config.challenges > 0 && config.challenges <= 0xFFFF);
    assert.ok(config.seed >= 0 && config.seed < TOTAL_SEEDS);
  }
});

check('invalid dates are rejected', () => {
  assert.throws(() => generateDailySeed('2026-02-30'));
  assert.throws(() => generateDailySeed('not-a-date'));
  assert.throws(() => generateDailySeedV2('2026-13-01'));
});

console.log('seed v2 tests PASSED');
