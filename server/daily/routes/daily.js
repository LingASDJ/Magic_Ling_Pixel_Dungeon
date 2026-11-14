const express = require('express');
const router = express.Router();
const {
  generateDailyConfig,
  getTodayString,
  parseUtcDay,
  shiftUtcDay,
  startOfUtcWeek,
  ALLOW_FUTURE_DATES
} = require('../utils/seed');
const { extractGameInfo, reconcileDailyScore } = require('../utils/bundle');
const {
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
} = require('../db');

function fail(res, code, message) {
  return res.json({ code, message, data: null });
}

/**
 * GET /today[?date=yyyy-MM-dd] - server-authoritative daily seed + configuration.
 */
router.get('/today', (req, res) => {
  try {
    const date = req.query.date ? String(req.query.date) : getTodayString();
    const day = parseUtcDay(date);
    if (day == null) {
      return fail(res, -1, '日期格式错误');
    }
    // Do not expose future daily seeds through the public endpoint. Admins can
    // temporarily set MLPD_DAILY_ALLOW_FUTURE_DATES=true for diagnostics.
    const todayDay = parseUtcDay(getTodayString());
    if (!ALLOW_FUTURE_DATES && todayDay != null && day > todayDay) {
      return fail(res, 1002, '日期不合法');
    }
    const config = generateDailyConfig(date);
    res.json({ code: 0, message: 'ok', data: config });
  } catch (e) {
    console.error('获取今日配置失败:', e);
    fail(res, -1, '服务器错误');
  }
});

/**
 * POST /submit
 * body: {
 *   playerId, playerName, date, seed, difficulty, challenges, replay, gameData
 * }
 *
 * gameData is the game's Bundle.toString() JSON. The server re-parses it for
 * score/depth/etc. and validates the run against the daily configuration.
 */
router.post('/submit', (req, res) => {
  try {
    const body = req.body || {};
    let { playerId, playerName, date, seed, difficulty, challenges, replay, gameData } = body;

    if (!gameData || typeof gameData !== 'string') {
      return fail(res, -1, '参数不完整: 缺少 gameData');
    }
    if (!playerId || typeof playerId !== 'string') {
      return fail(res, -1, '参数不完整: 缺少 playerId');
    }

    date = date ? String(date) : getTodayString();
    const day = parseUtcDay(date);
    if (day == null) {
      return fail(res, -1, '日期格式错误');
    }
    const todayDay = parseUtcDay(getTodayString());
    if (todayDay != null && day > todayDay) {
      return fail(res, 1002, '日期不合法');
    }

    const config = generateDailyConfig(date);
    if (Number(seed) !== config.seed) {
      return fail(res, 1002, '种子不匹配');
    }
    const difficultyName = String(difficulty || '').toUpperCase();
    if (difficultyName !== config.difficulty) {
      return fail(res, 1002, '难度不匹配');
    }
    if (Number(challenges) !== config.challenges) {
      return fail(res, 1002, '挑战不匹配');
    }
    if (replay === true) {
      // 重玩成绩单独记录到重玩榜：每名玩家每天只保留最高分。
      const info = extractGameInfo(gameData);
      info.seed = config.seed;
      info.customSeed = date;
      info.challenges = config.challenges;
      info.challengeTier = config.challengeCount;
      info.difficultyName = config.difficulty;
      info.difficulty = config.difficulty === 'HARD' ? 2 : 1;
      info.gameMode = 0;

      let name = String(playerName == null ? '' : playerName).trim();
      if (!name) name = info.heroName || '冒险者';
      if (name.length > 50) name = name.substring(0, 50);

      const difficultyInt = config.difficulty === 'HARD' ? 2 : 1;
      info.score = reconcileDailyScore(info.score, gameData, difficultyInt);

      const result = submitReplayScore(
        date,
        name,
        info,
        difficultyInt,
        0,
        config.challengeCount,
        gameData,
        playerId
      );
      const rank = getReplayPlayerRank(date, result.score, result.id);
      const totalPlayers = getReplayTotalPlayers(date);
      console.log(`[REPLAY] ${name} date=${date} score=${result.score} depth=${info.depth} rank=${rank}/${totalPlayers}`);

      return res.json({
        code: 0,
        message: result.updated ? '重玩成绩已记录' : '已保留更高的重玩成绩',
        data: { rank, totalPlayers, score: result.score }
      });
    }

    if (hasSubmitted(date, playerId)) {
      return fail(res, 1001, '今日已提交过分数');
    }

    const info = extractGameInfo(gameData);
    // The server's daily configuration is authoritative.
    info.seed = config.seed;
    info.customSeed = date;
    info.challenges = config.challenges;
    info.challengeTier = config.challengeCount;
    info.difficultyName = config.difficulty;
    info.difficulty = config.difficulty === 'HARD' ? 2 : 1;
    info.gameMode = 0;

    let name = String(playerName == null ? '' : playerName).trim();
    if (!name) name = info.heroName || '冒险者';
    if (name.length > 50) name = name.substring(0, 50);

    const difficultyInt = config.difficulty === 'HARD' ? 2 : 1;
    // Old clients applied the 0.5 custom-seed penalty to daily runs. Repair
    // those submissions so the stored leaderboard score is the real score.
    info.score = reconcileDailyScore(info.score, gameData, difficultyInt);
    info.difficulty = difficultyInt;
    let id;
    try {
      id = submitScore(
        date,
        name,
        info,
        difficultyInt,
        0,
        config.challengeCount,
        gameData,
        playerId,
        0
      );
    } catch (e) {
      // Unique (date, player_id) race: treat as already submitted.
      if (hasSubmitted(date, playerId)) {
        return fail(res, 1001, '今日已提交过分数');
      }
      throw e;
    }

    const rank = getPlayerRank(date, info.score, id, {});
    const totalPlayers = getTotalPlayers(date, {});

    console.log(`[SUBMIT] ${name} date=${date} score=${info.score} depth=${info.depth} rank=${rank}/${totalPlayers}`);

    res.json({
      code: 0,
      message: '提交成功',
      data: { rank, totalPlayers }
    });
  } catch (e) {
    console.error('提交成绩失败:', e);
    fail(res, -1, '服务器错误');
  }
});

/**
 * GET /leaderboard?date=yyyy-MM-dd&page=1&pageSize=20
 * Only total score ordering is used.
 */
router.get('/leaderboard', (req, res) => {
  try {
    let { date, page, pageSize } = req.query;
    if (!date) date = getTodayString();
    date = String(date);
    if (parseUtcDay(date) == null) {
      return fail(res, -1, '日期格式错误');
    }

    page = parseInt(page, 10) || 1;
    pageSize = parseInt(pageSize, 10) || 20;
    if (page < 1) page = 1;
    if (pageSize < 1) pageSize = 1;
    if (pageSize > 100) pageSize = 100;

    const { total, entries } = getLeaderboard(date, page, pageSize, {});

    res.json({
      code: 0,
      message: 'ok',
      data: {
        date,
        totalPlayers: total,
        page,
        pageSize,
        myRank: 0,
        myScore: 0,
        entries
      }
    });
  } catch (e) {
    console.error('获取排行榜失败:', e);
    fail(res, -1, '服务器错误');
  }
});

/**
 * GET /weekly?weekStart=yyyy-MM-dd&page=1&pageSize=20
 *
 * Aggregates Monday..Sunday (UTC). Each player keeps their best run of the
 * week; `rank`, `runs`, `gold`, `silver`, `bronze` are computed server-side.
 * `date=` (any day in the week) can be used instead of `weekStart=`.
 */
router.get('/weekly', (req, res) => {
  try {
    let { weekStart, date, page, pageSize } = req.query;

    if (!weekStart && !date) date = getTodayString();
    if (!weekStart && date) weekStart = startOfUtcWeek(String(date));
    if (!weekStart) weekStart = startOfUtcWeek(getTodayString());
    if (parseUtcDay(String(weekStart)) == null) {
      return fail(res, -1, '日期格式错误');
    }

    // Normalize to the Monday of the requested week and never expose future
    // weeks (they have no data anyway, but keep the range bounded).
    weekStart = startOfUtcWeek(String(weekStart));
    let weekEnd = shiftUtcDay(weekStart, 6);
    const today = getTodayString();
    if (weekStart > today) {
      return fail(res, 1002, '日期不合法');
    }
    if (weekEnd > today) weekEnd = today;

    page = parseInt(page, 10) || 1;
    pageSize = parseInt(pageSize, 10) || 20;
    if (page < 1) page = 1;
    if (pageSize < 1) pageSize = 1;
    if (pageSize > 100) pageSize = 100;

    const { total, entries } = getWeeklyLeaderboard(weekStart, weekEnd, page, pageSize);

    res.json({
      code: 0,
      message: 'ok',
      data: {
        date: weekStart,
        weekStart,
        weekEnd,
        totalPlayers: total,
        page,
        pageSize,
        myRank: 0,
        myScore: 0,
        entries
      }
    });
  } catch (e) {
    console.error('获取周榜失败:', e);
    fail(res, -1, '服务器错误');
  }
});

/**
 * GET /replay?date=yyyy-MM-dd&page=1&pageSize=20
 *
 * 重玩榜：每名玩家每天只保留最高分的一条重玩记录，按分数降序。
 * 与日榜分开存储，不影响首次挑战排行榜。
 */
router.get('/replay', (req, res) => {
  try {
    let { date, page, pageSize } = req.query;
    if (!date) date = getTodayString();
    date = String(date);
    if (parseUtcDay(date) == null) {
      return fail(res, -1, '日期格式错误');
    }

    page = parseInt(page, 10) || 1;
    pageSize = parseInt(pageSize, 10) || 20;
    if (page < 1) page = 1;
    if (pageSize < 1) pageSize = 1;
    if (pageSize > 100) pageSize = 100;

    const { total, entries } = getReplayLeaderboard(date, page, pageSize);

    res.json({
      code: 0,
      message: 'ok',
      data: {
        date,
        totalPlayers: total,
        page,
        pageSize,
        myRank: 0,
        myScore: 0,
        entries
      }
    });
  } catch (e) {
    console.error('获取重玩榜失败:', e);
    fail(res, -1, '服务器错误');
  }
});

module.exports = router;
