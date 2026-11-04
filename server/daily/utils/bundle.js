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

module.exports = {
  parseBundle,
  extractGameInfo,
  popcount
};
