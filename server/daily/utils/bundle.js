/**
 * Parser for the game's Bundle.toString() data.
 *
 * In the current Shattered Pixel Dungeon / MLPD code base Bundle.toString()
 * returns org.json.JSONObject.toString(), i.e. standard JSON. The old server
 * expected a Java-style "{key=value}" string and silently extracted zeroes
 * from every real snapshot; this module now parses JSON and maps the flat
 * snapshot keys that Dungeon.takeDailySnapshot() actually writes.
 */

const HERO_CLASSES = ['WARRIOR', 'MAGE', 'ROGUE', 'HUNTRESS', 'DUELIST', 'SPELLSWORD'];

function parseBundle(bundleStr) {
  if (!bundleStr) return {};
  if (typeof bundleStr === 'object') return bundleStr;
  if (typeof bundleStr !== 'string') return {};
  try {
    const parsed = JSON.parse(bundleStr);
    return (parsed && typeof parsed === 'object') ? parsed : {};
  } catch (e) {
    return {};
  }
}

function num(value, defaultValue = 0) {
  if (typeof value === 'number' && Number.isFinite(value)) return value;
  const n = Number(value);
  return Number.isFinite(n) ? n : defaultValue;
}

function int(value, defaultValue = 0) {
  return Math.trunc(num(value, defaultValue));
}

function bool(value, defaultValue = false) {
  if (typeof value === 'boolean') return value;
  if (typeof value === 'number') return value !== 0;
  if (typeof value === 'string') return value.toLowerCase() === 'true' || value === '1';
  return defaultValue;
}

function str(value, defaultValue = '') {
  if (value == null) return defaultValue;
  return typeof value === 'string' ? value : String(value);
}

function normalizeHeroClass(value) {
  let cls = str(value, 'WARRIOR');
  if (cls.includes('.')) cls = cls.split('.').pop();
  cls = cls.toUpperCase();
  return HERO_CLASSES.includes(cls) ? cls : 'WARRIOR';
}

function normalizeSubClass(value) {
  let sub = str(value, 'NONE');
  if (sub.includes('.')) sub = sub.split('.').pop();
  return sub.toUpperCase();
}

function popcount(mask) {
  let n = 0;
  let v = mask | 0;
  while (v !== 0) {
    v &= (v - 1);
    n++;
  }
  return n;
}

/**
 * Extracts leaderboard fields from a game snapshot.
 * @param {string|object} gameData - Bundle.toString() JSON, or an already parsed object
 * @returns {object}
 */
function extractGameInfo(gameData) {
  const bundle = parseBundle(gameData);
  const hero = (bundle.hero && typeof bundle.hero === 'object') ? bundle.hero : {};

  const conduct = Array.isArray(bundle.conduct)
    ? bundle.conduct.map(v => str(v).toUpperCase())
    : [];
  const difficultyName = conduct.includes('HARD')
    ? 'HARD'
    : (conduct.includes('NORMAL') ? 'NORMAL' : 'NORMAL');

  const challenges = Math.max(0, int(bundle.challenges, 0));
  let armorTier = int(hero.tier, 0);
  if (!armorTier && hero.armor && typeof hero.armor === 'object') {
    armorTier = int(hero.armor.tier, 0);
  }

  const result = {
    heroClass: normalizeHeroClass(hero.class),
    heroSubClass: normalizeSubClass(hero.subClass),
    heroLevel: Math.max(1, int(hero.lvl, 1)),
    heroName: str(hero.name, ''),
    armorTier: Math.max(0, armorTier),
    // Statistics.storeInBundle puts total_score at the top level.
    score: Math.max(0, int(bundle.total_score != null ? bundle.total_score : 0, 0)),
    won: bool(bundle.won, false),
    depth: Math.max(0, int(bundle.depth != null ? bundle.depth : bundle.maxDepth, 0)),
    duration: Math.max(0, Math.floor(num(bundle.duration, 0))),
    enemiesSlain: Math.max(0, int(bundle.enemiesSlain, 0)),
    // Statistics.GOLD is "score" (gold collected), not total score.
    goldCollected: Math.max(0, int(bundle.score, 0)),
    foodEaten: Math.max(0, int(bundle.foodEaten, 0)),
    // Statistics.ALCHEMY is "potionsCooked".
    itemsCrafted: Math.max(0, int(bundle.potionsCooked, 0)),
    challenges,
    challengeTier: popcount(challenges),
    str: int(hero.STR, 10),
    version: str(bundle.game_version != null ? bundle.game_version : bundle.version, ''),
    seed: Math.trunc(num(bundle.seed, 0)),
    customSeed: str(bundle.custom_seed != null ? bundle.custom_seed : bundle.customSeedText, ''),
    gameMode: bool(bundle.happy, false) ? 1 : (bool(bundle.randmode, false) ? 2 : 0),
    difficultyName,
    difficulty: difficultyName === 'HARD' ? 2 : 1,
    conduct
  };

  // Bottom-line validation.
  result.score = Math.max(0, result.score);
  result.depth = Math.max(0, result.depth);
  result.heroLevel = Math.max(1, result.heroLevel);
  return result;
}

/**
 * Recomputes the final daily score from the statistics components stored in a
 * daily snapshot (Rankings.calculateScore() order, minus the custom-seed 0.5
 * penalty which does not apply to daily runs).
 *
 * @param {string|object} gameData - Bundle.toString() JSON, or parsed object
 * @param {number} difficulty - 2 = HARD (x2.5), 0 = EASY (x0.5), otherwise x1
 * @returns {number} expected final score, or 0 when the snapshot has no
 *                   score components (old/invalid data)
 */
function expectedDailyScore(gameData, difficulty) {
  const bundle = parseBundle(gameData);

  // Only trust a snapshot that carries the complete score component set.
  // Old snapshots (pre-1.3 scoring, or truncated data) would otherwise be
  // "recomputed" from zeros and could overwrite a correct stored score.
  const REQUIRED = [
    'prog_score', 'tres_score', 'expl_score', 'tot_boss', 'tot_quest',
    'win_mult', 'chal_mult'
  ];
  for (const key of REQUIRED) {
    if (bundle[key] == null) return 0;
  }

  const base = num(bundle.prog_score, 0)
      + num(bundle.tres_score, 0)
      + num(bundle.expl_score, 0)
      + num(bundle.tot_boss, 0)
      + num(bundle.tot_quest, 0);
  if (base <= 0) return 0;

  // The snapshot's conduct list is more reliable than the DB column for old
  // rows; fall back to the server's daily difficulty when it is absent.
  let diff = Number(difficulty) === 2 ? 2.5
      : (Number(difficulty) === 0 ? 0.5 : 1);
  if (Array.isArray(bundle.conduct)) {
    const conduct = bundle.conduct.map(v => str(v).toUpperCase());
    if (conduct.includes('HARD')) diff = 2.5;
    else if (conduct.includes('EASY')) diff = 0.5;
    else if (conduct.includes('NORMAL')) diff = 1;
  }

  // Mirror Rankings.calculateScore()'s int/float arithmetic exactly: the
  // client uses 32-bit floats and truncates after every compound assignment,
  // so using doubles + a single Math.round would be a few points off.
  const f = Math.fround;
  const win = f(num(bundle.win_mult, 1));
  const chal = f(num(bundle.chal_mult, 1));

  let score = base;
  // totalScore *= winMultiplier * chalMultiplier;
  score = Math.trunc(f(score * f(win * chal)));
  // daily runs: no custom-seed 0.5x penalty
  // totalScore *= DifficultyScore();
  score = Math.trunc(f(score * f(diff)));

  // int totalProps = (int)(badMultiplier + goodMultiplier);
  const props = Math.trunc(f(f(num(bundle.bad_mult, 0)) + f(num(bundle.good_mult, 0))));
  if (props !== 0) score *= props;

  return Math.max(0, score);
}

/**
 * Returns the authoritative daily score for a snapshot.
 *
 * The score components, multipliers and conduct stored in the snapshot are the
 * source of truth, not the client-supplied `total_score`: old clients applied
 * the 0.5 custom-seed penalty to daily runs (leaderboard showed half of the
 * real score), and a stale/incorrect total must not survive on the board.
 * When the snapshot does not carry a complete component set we keep the
 * stored value (old data / unknown format).
 *
 * @param {number} storedScore
 * @param {string|object} gameData
 * @param {number} difficulty
 * @returns {number} corrected score
 */
function reconcileDailyScore(storedScore, gameData, difficulty) {
  const stored = Math.max(0, int(storedScore, 0));
  const expected = expectedDailyScore(gameData, difficulty);
  if (expected <= 0) return stored;

  // Keep the client's exact integer when it already agrees with the formula
  // (Java truncates each multiplication step, JS rounds once, so a few points
  // of difference are expected).
  const tolerance = Math.max(5, Math.round(expected * 0.002));
  if (Math.abs(stored - expected) <= tolerance) return stored;
  return expected;
}

module.exports = {
  parseBundle,
  extractGameInfo,
  expectedDailyScore,
  reconcileDailyScore,
  popcount
};
