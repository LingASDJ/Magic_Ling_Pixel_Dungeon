package com.shatteredpixel.shatteredpixeldungeon.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UpdateChecker {
	public static JsonNode config;
	// 配置获取地址
	private static final String CONFIG_URL = "https://pd.qinyueqwq.top/FTP/pd/net/ds.json";

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
		Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, listener);
	}
}
