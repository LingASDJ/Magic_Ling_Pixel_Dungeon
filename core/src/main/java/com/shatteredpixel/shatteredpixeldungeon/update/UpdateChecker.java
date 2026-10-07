package com.shatteredpixel.shatteredpixeldungeon.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.watabou.noosa.Game;

public class UpdateChecker {
	public static JsonValue config;
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
				try {
					config = new JsonReader().parse(json[0]);
					externalListener.handleHttpResponse(httpResponse);
				} catch (Exception ignored) {
					config = null;
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
			// TLS configuration is platform-specific; desktop installs an
			// insecure trust-all context, web uses the browser's native TLS.
			Game.platform.setupInsecureTls();

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
