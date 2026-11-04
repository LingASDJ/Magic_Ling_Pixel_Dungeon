package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.multiplayer.Mp;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DailyServiceImpl extends DailyService {

    /**
     * Server is fixed, like the multiplayer lobby. Can be overridden for local
     * testing with -Dmlpd.daily=http://127.0.0.1:28774/MLPD/api/v1/daily.
     */
    private static final String DEFAULT_API_BASE = "http://47.109.62.104:28773/MLPD/api/v1/daily";
    private static final long CACHE_TTL = 10 * 60 * 1000L;

    private static DailySeedData cachedSeed;
    private static long cachedAt;

    private static String apiBase() {
        try {
            String override = System.getProperty("mlpd.daily");
            if (override != null && !override.trim().isEmpty()) {
                return override.trim();
            }
        } catch (Throwable ignored) {
        }
        return DEFAULT_API_BASE;
    }

    private static synchronized DailySeedData cachedResult() {
        if (cachedSeed == null) return null;
        if (System.currentTimeMillis() - cachedAt > CACHE_TTL) return null;
        String today = DailyUtils.todayUtc();
        if (cachedSeed.date != null && !cachedSeed.date.isEmpty() && !cachedSeed.date.equals(today)) {
            return null;
        }
        return cachedSeed;
    }

    private static void setupSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            Game.reportException(e);
        }
    }

    private void httpGet(String url, Net.HttpResponseListener listener) {
        setupSSL();
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(url);
        request.setHeader("User-Agent", "MLPD-Daily/1.0");
        Gdx.net.sendHttpRequest(request, listener);
    }

    private void httpPost(String url, String jsonBody, Net.HttpResponseListener listener) {
        setupSSL();
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(url);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "MLPD-Daily/1.0");
        request.setContent(jsonBody);
        Gdx.net.sendHttpRequest(request, listener);
    }

    private static final JsonReader jsonReader = new JsonReader();

    @Override
    public void fetchTodaySeed(DailyResultCallback<DailySeedData> callback) {
        DailySeedData cached = cachedResult();
        if (cached != null) {
            callback.onSuccess(cached);
            return;
        }

        String url = apiBase() + "/today";

        httpGet(url, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    JsonValue root = jsonReader.parse(httpResponse.getResultAsString());
                    if (root.getInt("code") != 0) {
                        callback.onFailure("服务器错误: " + root.getInt("code"));
                        return;
                    }

                    JsonValue data = root.get("data");
                    DailySeedData result = new DailySeedData();
                    result.date = data.getString("date", DailyUtils.todayUtc());
                    // A1: the server sends the numeric daily seed, which is
                    // intentionally outside the user-enterable code range.
                    if (data.has("seed")) {
                        result.seed = data.getLong("seed");
                    } else if (data.has("code")) {
                        result.seed = DungeonSeed.convertFromCode(data.getString("code"));
                    } else {
                        callback.onFailure("服务器未返回种子");
                        return;
                    }
                    result.difficulty = data.getString("difficulty", "NORMAL");
                    result.challenges = data.getInt("challenges", 0);
                    result.challengeCount = data.getInt("challengeCount", 0);

                    synchronized (DailyServiceImpl.class) {
                        cachedSeed = result;
                        cachedAt = System.currentTimeMillis();
                    }
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("解析失败: " + e.getMessage());
                }
            }

            @Override public void failed(Throwable t) { callback.onFailure(t == null ? "网络错误" : t.getMessage()); }
            @Override public void cancelled()        { callback.onFailure("已取消"); }
        });
    }

    @Override
    public void submitScore(Bundle bundle, DailyResultCallback<SubmitResultData> callback) {
        if (bundle == null) {
            callback.onFailure("没有可提交的每日成绩");
            return;
        }

        // Original SPD rule: replays are unranked and must not be submitted.
        if (Dungeon.dailyReplay) {
            try {
                FileUtils.overwriteFile(Dungeon.TEMP_FILE, 1);
            } catch (Throwable ignored) {
            }
            DailyConfig.restore();
            return;
        }

        String url = apiBase() + "/submit";

        try {
            String gameData = bundle.toString();

            String playerName = "";
            try {
                playerName = Mp.get().getMyName();
            } catch (Throwable ignored) {
            }
            if (playerName == null) playerName = "";
            playerName = playerName.trim();
            if (playerName.isEmpty()) {
                try {
                    Bundle hero = bundle.getBundle("hero");
                    if (hero != null) playerName = hero.getString("name");
                } catch (Throwable ignored) {
                }
            }
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "冒险者";
            }

            String playerId = DailyIdentity.getPlayerId();

            String date = bundle.getString("custom_seed");
            if (date == null || date.isEmpty()) date = DailyConfig.getCurrentDate();
            if (date == null || date.isEmpty()) date = DailyUtils.todayUtc();

            long seed = bundle.getLong("seed");
            int challenges = bundle.getInt("challenges");
            if (challenges == 0) challenges = DailyConfig.getCurrentChallenges();

            String difficulty = DailyConfig.getCurrentDifficulty();
            try {
                String[] conduct = bundle.getStringArray("conduct");
                if (conduct != null) {
                    for (String c : conduct) {
                        if ("NORMAL".equalsIgnoreCase(c) || "HARD".equalsIgnoreCase(c)) {
                            difficulty = c.toUpperCase(Locale.ROOT);
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
            if (difficulty == null || difficulty.isEmpty()) difficulty = "NORMAL";

            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setOutputType(JsonWriter.OutputType.json);
            jsonWriter.object();
            jsonWriter.set("playerId", playerId);
            jsonWriter.set("playerName", playerName);
            jsonWriter.set("date", date);
            jsonWriter.set("seed", Long.toString(seed));
            jsonWriter.set("difficulty", difficulty);
            jsonWriter.set("challenges", challenges);
            jsonWriter.set("replay", false);
            jsonWriter.set("gameData", gameData);
            jsonWriter.pop();
            jsonWriter.close();

            String jsonBody = stringWriter.toString();

            httpPost(url, jsonBody, new Net.HttpResponseListener() {
                @Override
                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                    try {
                        JsonValue root = jsonReader.parse(httpResponse.getResultAsString());
                        SubmitResultData result = new SubmitResultData();
                        result.code = root.getInt("code");
                        result.message = root.getString("message", "");
                        if (root.has("data") && !root.get("data").isNull()) {
                            JsonValue data = root.get("data");
                            SubmitResultData.Data d = new SubmitResultData.Data();
                            d.rank = data.getInt("rank");
                            d.totalPlayers = data.getInt("totalPlayers");
                            result.data = d;
                        }
                        if (result.code == 1001) {
                            // Already submitted (or a replay was rejected): the
                            // local temp snapshot is no longer useful.
                            try {
                                FileUtils.overwriteFile(Dungeon.TEMP_FILE, 1);
                            } catch (Throwable ignored) {
                            }
                        }
                        DailyConfig.restore();
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        DailyConfig.restore();
                        callback.onFailure("解析失败: " + e.getMessage());
                    }
                }

                @Override public void failed(Throwable t) {
                    DailyConfig.restore();
                    callback.onFailure(t == null ? "网络错误" : t.getMessage());
                }
                @Override public void cancelled() {
                    DailyConfig.restore();
                    callback.onFailure("已取消");
                }
            });
        } catch (Exception e) {
            DailyConfig.restore();
            callback.onFailure("序列化失败: " + e.getMessage());
        }
    }

    @Override
    public void fetchLeaderboard(String date, DailyResultCallback<LeaderboardData> callback) {
        fetchLeaderboard(date, 1, 20, callback);
    }

    @Override
    public void fetchLeaderboard(String date, int page, int pageSize,
                                 DailyResultCallback<LeaderboardData> callback) {
        String url = apiBase() + "/leaderboard?date=" + date + "&page=" + page + "&pageSize=" + pageSize;

        httpGet(url, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    JsonValue root = jsonReader.parse(httpResponse.getResultAsString());
                    if (root.getInt("code") != 0) {
                        callback.onFailure("服务器错误: " + root.getInt("code"));
                        return;
                    }
                    JsonValue data = root.get("data");
                    LeaderboardData result = new LeaderboardData();
                    result.code = 0;
                    result.data = new LeaderboardData.Data();
                    LeaderboardData.Data d = result.data;

                    d.date = data.getString("date", date);
                    d.totalPlayers = data.getInt("totalPlayers", 0);
                    d.page = data.getInt("page", page);
                    d.pageSize = data.getInt("pageSize", pageSize);
                    d.myRank = data.getInt("myRank", 0);
                    d.myScore = data.getInt("myScore", 0);

                    d.entries = new ArrayList<>();
                    JsonValue entries = data.get("entries");
                    if (entries != null) {
                        for (JsonValue entry : entries) {
                            LeaderboardData.Entry e = new LeaderboardData.Entry();
                            e.rank = entry.getInt("rank");
                            e.playerId = entry.getString("playerId", "");
                            e.playerName = entry.getString("playerName", "");
                            try {
                                e.heroClass = HeroClass.valueOf(entry.getString("heroClass", "WARRIOR"));
                            } catch (Throwable ignored) {
                                e.heroClass = HeroClass.WARRIOR;
                            }
                            e.heroLevel = entry.getInt("heroLevel", 1);
                            e.armorTier = entry.getInt("armorTier", 0);
                            e.score = entry.getInt("score", 0);
                            e.won = entry.getBoolean("won", false);
                            e.depth = entry.getInt("depth", 0);
                            e.challenges = entry.getInt("challenges", 0);
                            e.challengeCount = entry.getInt("challengeCount", 0);
                            e.difficulty = entry.getInt("difficulty", 1);
                            d.entries.add(e);
                        }
                    }
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("解析失败: " + e.getMessage());
                }
            }

            @Override public void failed(Throwable t) { callback.onFailure(t == null ? "网络错误" : t.getMessage()); }
            @Override public void cancelled()        { callback.onFailure("已取消"); }
        });
    }
}
