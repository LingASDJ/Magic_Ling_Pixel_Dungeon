package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

import java.io.StringWriter;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DailyServiceImpl extends DailyService {

    private static final String API_BASE = "https://gameupdate.insrv.mlpd.spldream.com/MLPD/api/v1/daily";


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
        String url = API_BASE + "/today";

        httpGet(url, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    JsonValue root = jsonReader.parse(httpResponse.getResultAsString());
                    if ( root.getInt("code") != 0) {
                        callback.onFailure("服务器错误: " + root.getInt("code"));
                        return;
                    }

                    JsonValue data = root.get("data");
                    DailySeedData result = new DailySeedData();
                    result.date = data.getString("date");
                    result.seed = data.getLong("seed");
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("解析失败: " + e.getMessage());
                }
            }

            @Override public void failed(Throwable t) { callback.onFailure(t.getMessage()); }
            @Override public void cancelled()        { callback.onFailure("已取消"); }
        });
    }

    @Override
    public void submitScore(Bundle bundle, DailyResultCallback<SubmitResultData> callback) {
        String url = API_BASE + "/submit";

        try {
            String gameData = bundle.toString();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.object();
            jsonWriter.set("playerName",bundle.getString("player_name"));
            jsonWriter.set("gameData",gameData);
            jsonWriter.pop();

            httpPost(url, jsonWriter.toString(), new Net.HttpResponseListener() {
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
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        callback.onFailure("解析失败: " + e.getMessage());
                    }
                }

                @Override public void failed(Throwable t) { callback.onFailure(t.getMessage()); }
                @Override public void cancelled()        { callback.onFailure("已取消"); }
            });
        } catch (Exception e) {
            callback.onFailure("序列化失败: " + e.getMessage());
        }
    }

    @Override
    public void fetchLeaderboard(String date, DailyResultCallback<LeaderboardData> callback) {
        String url = API_BASE + "/leaderboard?date=" + date;

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

                    d.date = data.getString("date");
                    d.totalPlayers = data.getInt("totalPlayers");
                    d.page = data.getInt("page");
                    d.pageSize = data.getInt("pageSize");
                    d.myRank = data.getInt("myRank", 0);
                    d.myScore = data.getInt("myScore", 0);

                    d.entries = new java.util.ArrayList<>();
                    for (JsonValue entry : data.get("entries")) {
                        LeaderboardData.Entry e = new LeaderboardData.Entry();
                        e.rank = entry.getInt("rank");
                        e.playerName = entry.getString("playerName");
                        e.heroClass = HeroClass.valueOf( ( entry.getString("heroClass") ) );
                        e.score = entry.getInt("score");
                        e.won = entry.getBoolean("won");
                        e.depth = entry.getInt("depth");
                        d.entries.add(e);
                    }
                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onFailure("解析失败: " + e.getMessage());
                }
            }

            @Override public void failed(Throwable t) { callback.onFailure(t.getMessage()); }
            @Override public void cancelled()        { callback.onFailure("已取消"); }
        });
    }
}