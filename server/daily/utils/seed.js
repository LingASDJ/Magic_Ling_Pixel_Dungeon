const crypto = require('crypto');

// Same value as com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed.TOTAL_SEEDS (26^9).
// Daily seeds are intentionally placed above this range so they cannot be entered
// as a custom seed in the game.
const TOTAL_SEEDS = 5429503678976;

// Daily seed v2: for dates on/after this UTC date, derive an in-range,
// non-linear seed instead of the legacy "UTC midnight + TOTAL_SEEDS" value.
// Past dates (and any records already submitted for them) keep the legacy
// seed so existing leaderboard rows remain valid.
const DAILY_SEED_V2_START = process.env.MLPD_DAILY_SEED_V2_START || '2026-11-08';
// Server-side salt for seed v2. Override with MLPD_DAILY_SEED_SECRET in
// production so future seeds cannot be precomputed from the source.
const DAILY_SEED_SECRET = process.env.MLPD_DAILY_SEED_SECRET || 'mlpd-daily-seed-v2-change-me';
// Diagnostic escape hatch: allow /today?date=... for future dates.
const ALLOW_FUTURE_DATES = process.env.MLPD_DAILY_ALLOW_FUTURE_DATES === 'true';

// Same order as Challenges.MASKS / Challenges.NAME_IDS in the game.
const CHALLENGE_MASKS = [
  1,      // no_food
  2,      // no_armor
  4,      // no_healing
  8,      // no_herbalism
  16,     // swarm_intelligence
  32,     // darkness
  64,     // no_scrolls
  128,    // aquaphobia
  256,    // champion_enemies
  512,    // rlpt
  1024,   // sbsg
  2048,   // exsg
  4096,   // stronger_bosses
  8192,   // dhxd
  16384,  // morelevel
  32768   // cs
];

const CHALLENGE_IDS = [
  'no_food', 'no_armor', 'no_healing', 'no_herbalism',
  'swarm_intelligence', 'darkness', 'no_scrolls', 'aquaphobia',
  'champion_enemies', 'rlpt', 'sbsg', 'exsg',
  'stronger_bosses', 'dhxd', 'morelevel', 'cs'
];

/**
 * Returns the UTC date (yyyy-MM-dd). The original Shattered Pixel Dungeon daily
 * run is based on the UTC day, not the local time of the client or server.
 * @param {Date} [date]
 * @returns {string}
 */
function getTodayString(date = new Date()) {
  const year = date.getUTCFullYear();
  const month = String(date.getUTCMonth() + 1).padStart(2, '0');
  const day = String(date.getUTCDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

/**
 * Parses yyyy-MM-dd as a UTC day and returns its epoch milliseconds.
 * Returns null for malformed or impossible dates.
 * @param {string} dateStr
 * @returns {number|null}
 */
function parseUtcDay(dateStr) {
  if (typeof dateStr !== 'string' || !/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) {
    return null;
  }
  const parts = dateStr.split('-').map(Number);
  const ms = Date.UTC(parts[0], parts[1] - 1, parts[2]);
  const check = new Date(ms);
  if (check.getUTCFullYear() !== parts[0]
      || check.getUTCMonth() !== parts[1] - 1
      || check.getUTCDate() !== parts[2]) {
    return null;
  }
  return ms;
}

/**
 * Legacy daily seed: UTC day start + TOTAL_SEEDS.
 * The result is above 26^9 and therefore outside the user-enterable seed range.
 * Kept only for dates before DAILY_SEED_V2_START so old records stay valid.
 * @param {string} dateStr
 * @returns {number}
 */
function generateLegacyDailySeed(dateStr) {
  const day = parseUtcDay(dateStr);
  if (day == null) {
    throw new Error(`invalid daily date: ${dateStr}`);
  }
  return day + TOTAL_SEEDS;
}

/**
 * Seed v2: deterministic HMAC-SHA256 of the UTC date, reduced into
 * [0, TOTAL_SEEDS). Unlike the legacy formula it is in the normal seed
 * range and has no linear daily pattern.
 * @param {string} dateStr
 * @returns {number}
 */
function generateDailySeedV2(dateStr) {
  if (parseUtcDay(dateStr) == null) {
    throw new Error(`invalid daily date: ${dateStr}`);
  }
  const digest = crypto.createHmac('sha256', DAILY_SEED_SECRET)
      .update(`mlpd-daily-seed-v2:${dateStr}`)
      .digest();
  const value = BigInt('0x' + digest.subarray(0, 8).toString('hex'))
      % BigInt(TOTAL_SEEDS);
  return Number(value);
}

/**
 * Daily seed dispatcher. Dates before the cutoff use the legacy formula;
 * dates on/after it use v2.
 * @param {string} dateStr
 * @returns {number}
 */
function generateDailySeed(dateStr) {
  if (parseUtcDay(dateStr) == null) {
    throw new Error(`invalid daily date: ${dateStr}`);
  }
  return dateStr < DAILY_SEED_V2_START
      ? generateLegacyDailySeed(dateStr)
      : generateDailySeedV2(dateStr);
}

/**
 * Deterministic per-day configuration. The server is authoritative; clients
 * only apply the returned values.
 * @param {string} dateStr
 * @returns {{date:string, seed:number, seedVersion:number, difficulty:string, challenges:number, challengeCount:number}}
 */
function generateDailyConfig(dateStr) {
  const seed = generateDailySeed(dateStr);
  const hash = crypto.createHash('sha256').update(`mlpd-daily-v1:${dateStr}`).digest();

  // 正式调查 (NORMAL) vs 直面灾厄 (HARD)
  const difficulty = (hash[0] & 1) === 0 ? 'NORMAL' : 'HARD';

  // Literal 1..16 random count. No guardrails/whitelists by design.
  const challengeCount = 1 + (hash[1] % CHALLENGE_MASKS.length);

  // Deterministic Fisher-Yates shuffle seeded from the date hash.
  let state = ((hash[4] << 24) | (hash[5] << 16) | (hash[6] << 8) | hash[7]) >>> 0;
  if (state === 0) state = 0x9e3779b9;
  const rand = () => {
    state ^= state << 13; state >>>= 0;
    state ^= state >>> 17;
    state ^= state << 5; state >>>= 0;
    return state;
  };

  const masks = CHALLENGE_MASKS.slice();
  for (let i = masks.length - 1; i > 0; i--) {
    const j = rand() % (i + 1);
    const tmp = masks[i];
    masks[i] = masks[j];
    masks[j] = tmp;
  }

  let challenges = 0;
  for (let i = 0; i < challengeCount; i++) {
    challenges |= masks[i];
  }

  return {
    date: dateStr,
    seed,
    seedVersion: dateStr < DAILY_SEED_V2_START ? 1 : 2,
    difficulty,
    challenges,
    challengeCount
  };
}

module.exports = {
  TOTAL_SEEDS,
  DAILY_SEED_V2_START,
  ALLOW_FUTURE_DATES,
  CHALLENGE_MASKS,
  CHALLENGE_IDS,
  getTodayString,
  parseUtcDay,
  generateLegacyDailySeed,
  generateDailySeedV2,
  generateDailySeed,
  generateDailyConfig
};
