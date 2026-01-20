/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.services.news;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.watabou.noosa.Game;

import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ShatteredNews extends NewsService {

	private static class TrustAllManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static void setupSSL() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = (hostname, session) -> true;
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	@Override
	public void checkForArticles(boolean useMetered, boolean preferHTTPS, NewsService.NewsResultCallback callback) {
		if (!useMetered && !Game.platform.connectedToUnmeteredNetwork()){
			callback.onConnectionFailed();
			return;
		}

		try {
			setupSSL();
		} catch (Exception e) {
			callback.onConnectionFailed();
			return;
		}

		Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
		httpGet.setUrl("https://gameupdate.insrv.mlpd.spldream.com/MLPD/news.xml");

		Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				try {
					ArrayList<NewsArticle> articles = new ArrayList<>();
					XmlReader reader = new XmlReader();
					XmlReader.Element xmlDoc = reader.parse(httpResponse.getResultAsStream());

					SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

					for (XmlReader.Element xmlArticle : xmlDoc.getChildrenByName("entry")){
						NewsArticle article = new NewsArticle();
						article.title = xmlArticle.get("title");
						article.top = xmlArticle.get("top");
						try {
							article.date = dateParser.parse(xmlArticle.get("published"));
						} catch (ParseException e) {
							Game.reportException(e);
						}
						article.summary = xmlArticle.get("summary");
						article.URL = xmlArticle.getChildByName("link").getAttribute("href");
						if (!preferHTTPS) {
							article.URL = article.URL.replace("https://", "http://");
						}

						Pattern versionCodeMatcher = Pattern.compile("v[0-9]+");
						try {
							Array<XmlReader.Element> properties = xmlArticle.getChildrenByName("category");
							for (XmlReader.Element prop : properties){
								String propVal = prop.getAttribute("term");
								if (propVal.startsWith("SHPD_ICON")){
									Matcher m = versionCodeMatcher.matcher(propVal);
									if (m.find()) {
										int iconGameVer = Integer.parseInt(m.group().substring(1));
										if (iconGameVer <= Game.versionCode) {
											article.icon = propVal.substring(propVal.indexOf(": ") + 2);
										}
									}
								}
							}
						} catch (Exception e){
							article.icon = null;
						}

						articles.add(article);
					}
					callback.onArticlesFound(articles);
				} catch (Exception e) {
					callback.onConnectionFailed();
				}
			}

			@Override
			public void failed(Throwable t) {
				callback.onConnectionFailed();
			}

			@Override
			public void cancelled() {
				callback.onConnectionFailed();
			}
		});
	}
}
