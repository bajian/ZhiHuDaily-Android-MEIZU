package com.zhihu.daily.meizu.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.query.Select;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhihu.daily.meizu.model.NetData;
import com.zhihu.daily.meizu.net.NetworkManager.ParseJsonCallBack;

public class Networker {

	public static void getJsonObject(final ParseJsonCallBack callBack,
			final String path, RequestQueue queue, boolean isUseCache,
			boolean isOnlyUseCache) {
		if (isUseCache) {
			NetData netData = new Select().from(NetData.class)
					.where("path = ?", path).executeSingle();
			if (netData != null) {
				try {
					callBack.onDataGeted(new JSONObject(netData.getJson()));
				} catch (JSONException e) {
					e.printStackTrace();
					callBack.onDataGetError(null);
				}
				if (isOnlyUseCache) {
					return;
				}
			}
		}
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.GET, path, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						callBack.onDataGeted(response);
						// 保存到数据库
						new NetData(path, response.toString()).store();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						callBack.onDataGetError(error);
					}
				});
		queue.add(jsonObjectRequest);
	}
}
