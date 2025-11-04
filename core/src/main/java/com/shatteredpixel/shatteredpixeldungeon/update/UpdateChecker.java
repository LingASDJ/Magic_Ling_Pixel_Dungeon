package com.shatteredpixel.shatteredpixeldungeon.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UpdateChecker {
	public static JsonNode config;
	// 配置获取地址
	private static final String CONFIG_URL = "https://gameupdate.insrv.mlpd.spldream.com/MLPD/GameUpdate.json";

	public static void refreshConfig() {
		refreshConfig(new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
			}

			@Override
			public void failed(Throwable t) {
			}

			@Override
			public void cancelled() {
			}
		});
	}

	/**
	 * 从服务器更新配置文件
	 */
	public static void refreshConfig(final Net.HttpResponseListener externalListener) {
		final String[] json = new String[1];
		Net.HttpResponseListener listener1 = new Net.HttpResponseListener() {
			@Override
			public void handleHttpResponse(Net.HttpResponse httpResponse) {
				json[0] = httpResponse.getResultAsString();
				ObjectMapper mapper = new ObjectMapper();
				try {
					config = mapper.readTree(json[0]);
					externalListener.handleHttpResponse(httpResponse);
				} catch (JsonProcessingException ignored) {
				}
			}

			@Override
			public void failed(Throwable t) {
			}

			@Override
			public void cancelled() {
			}
		};
		getHttpStringFromUrl(CONFIG_URL, listener1);
		if (json[0] == null) {
			externalListener.cancelled();
		}
	}

	public static void getHttpStringFromUrl(String url, Net.HttpResponseListener listener) {
		try {
			// 如果是HTTPS请求，设置SSL上下文
			if (url.startsWith("https://")) {
				// 创建信任所有证书的TrustManager
				TrustManager[] trustAllCerts = new TrustManager[] {
						new X509TrustManager() {
							public X509Certificate[] getAcceptedIssuers() {
								return null;
							}
							public void checkClientTrusted(X509Certificate[] certs, String authType) {
							}
							public void checkServerTrusted(X509Certificate[] certs, String authType) {
							}
						}
				};

				// 安装全信任的TrustManager
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				// 创建不验证主机名的HostnameVerifier
				HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
			}

			Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
			request.setUrl(url);
			request.setHeader("User-Agent", "Mozilla/5.0");

			Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
				@Override
				public void handleHttpResponse(Net.HttpResponse httpResponse) {
					listener.handleHttpResponse(httpResponse);
				}

				@Override
				public void failed(Throwable t) {
					listener.failed(t);
				}

				@Override
				public void cancelled() {
					listener.cancelled();
				}
			});
		} catch (Exception e) {
			listener.failed(e);
		}
	}

}
