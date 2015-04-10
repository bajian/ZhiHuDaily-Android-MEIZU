package com.zhihu.daily.meizu.net;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zhihu.daily.meizu.model.CategoryTheme;
import com.zhihu.daily.meizu.model.Comment;
import com.zhihu.daily.meizu.model.Editor;
import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.model.Story;
import com.zhihu.daily.meizu.model.ThemeStory;
import com.zhihu.daily.meizu.model.TopNews;
import com.zhihu.daily.meizu.utils.JSONUtils;

public class NetworkManager {
	public static NetworkManager mNetworkManager;

	private RequestQueue mQueue;

	// 刷新模式
	public static final int MODE_REFRESH = 0;
	// 加载模式
	public static final int MODE_ADD = 1;

	private String lastDate;

	public static NetworkManager getNetworkManager(Context context) {
		if (mNetworkManager == null) {
			mNetworkManager = new NetworkManager(context);
		}
		return mNetworkManager;
	}

	private NetworkManager(Context context) {
		mQueue = Volley.newRequestQueue(context);
	}

	public void getLatest(final GetDataCallBack<SimpleNews> simpleCallBack,
			final GetDataCallBack<TopNews> topCallBack, boolean isUseCache) {
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				try {
					String date = jsonObject.getString("date");
					List<SimpleNews> list = JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("stories").toString(),
							SimpleNews.class);
					for (SimpleNews simpleNews : list) {
						simpleNews.setDate(date);
					}
					simpleCallBack.onDataGot(list, MODE_REFRESH);

				} catch (JSONException e) {
					e.printStackTrace();
					simpleCallBack.onDataGotError();
				}
				try {
					topCallBack.onDataGot(JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("top_stories").toString(),
							TopNews.class), MODE_REFRESH);
				} catch (JSONException e) {
					topCallBack.onDataGotError();
				}
			}

			@Override
			public void onDataGetError(VolleyError error) {
				simpleCallBack.onDataGotError();
				topCallBack.onDataGotError();
			}
		}, ConnectURL.URL_LATEST, mQueue, isUseCache, false);
	}

	public void getBefore(final GetDataCallBack<SimpleNews> simpleCallBack,
			String date, boolean isUseCache) {
		if (lastDate != date) {
			// 保证不能重复加载
			lastDate = date;
			Networker.getJsonObject(new ParseJsonCallBack() {
				@Override
				public void onDataGeted(JSONObject jsonObject) {
					try {
						String date = jsonObject.getString("date");
						List<SimpleNews> list = JSONUtils
								.getListObjectFromJSON(
										jsonObject.getJSONArray("stories")
												.toString(), SimpleNews.class);
						for (SimpleNews simpleNews : list) {
							simpleNews.setDate(date);
						}
						simpleCallBack.onDataGot(list, MODE_ADD);

					} catch (JSONException e) {
						e.printStackTrace();
						simpleCallBack.onDataGotError();
					}
				}

				@Override
				public void onDataGetError(VolleyError error) {
					simpleCallBack.onDataGotError();
					// 保证错误后仍可加载
					lastDate = null;
				}
			}, ConnectURL.URL_BEFORE + date, mQueue, true, true);
		}

	}

	public void getNewsPage(final GetStoryCallBack callBack, int id) {
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				callBack.onDataGot(JSONUtils.getObjectFromJSON(
						jsonObject.toString(), Story.class));
			}

			@Override
			public void onDataGetError(VolleyError error) {
				callBack.onDataGotError();
			}
		}, ConnectURL.URL_STORY + id, mQueue, true, true);
	}

	public void getCategoryTheme(final GetDataCallBack<CategoryTheme> callBack) {
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				try {
					List<CategoryTheme> list = JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("others").toString(),
							CategoryTheme.class);

					for (CategoryTheme categoryTheme : list) {
						categoryTheme.setCategory("更多领域推荐");
					}

					callBack.onDataGot(list, MODE_REFRESH);

				} catch (JSONException e) {
					e.printStackTrace();
					callBack.onDataGotError();
				}
			}

			@Override
			public void onDataGetError(VolleyError error) {
				callBack.onDataGotError();
			}
		}, ConnectURL.URL_THEMES, mQueue, true, true);
	}

	public void getThemePage(final GetThemeStoryCallBack callBack,
			boolean isUseCache, int id, final int mode) {
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				try {
					List<SimpleNews> list = JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("stories").toString(),
							SimpleNews.class);
					List<Editor> editors = JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("editors").toString(),
							Editor.class);
					callBack.onDataGot(list, editors,
							jsonObject.getString("background"), mode);

				} catch (JSONException e) {
					e.printStackTrace();
					callBack.onDataGotError();
				}
			}

			@Override
			public void onDataGetError(VolleyError error) {
				callBack.onDataGotError();
			}
		}, ConnectURL.URL_THEME + id, mQueue, isUseCache, false);
	}

	public void getThemeNewsPage(final GetThemeStoryContentCallBack callBack,
			int id) {
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				callBack.onDataGot(JSONUtils.getObjectFromJSON(
						jsonObject.toString(), ThemeStory.class));
			}

			@Override
			public void onDataGetError(VolleyError error) {
				callBack.onDataGotError();
			}
		}, ConnectURL.URL_STORY + id, mQueue, true, true);
	}

	public static final int TYPE_LONG = 0;
	public static final int TYPE_SHORT = 1;

	public void getComment(final GetDataCallBack<Comment> callBack, int id,
			final int type) {
		String path = (type == NetworkManager.TYPE_LONG) ? ConnectURL.URL_LONG_COMMENTS
				: ConnectURL.URL_SHORT_COMMENTS;
		Networker.getJsonObject(new ParseJsonCallBack() {
			@Override
			public void onDataGeted(JSONObject jsonObject) {
				try {
					List<Comment> list = JSONUtils.getListObjectFromJSON(
							jsonObject.getJSONArray("comments").toString(),
							Comment.class);
					callBack.onDataGot(list, type);
				} catch (JSONException e) {
					e.printStackTrace();
					callBack.onDataGotError();
				}
			}

			@Override
			public void onDataGetError(VolleyError error) {
				callBack.onDataGotError();
			}
		}, ConnectURL.URL_STORY + id + path, mQueue, false, false);
	}

	// 获取主题新闻具体内容的接口
	public interface GetThemeStoryContentCallBack {
		public void onDataGot(ThemeStory themeStory);

		public void onDataGotError();
	}

	// 获取主题新闻接口
	public interface GetThemeStoryCallBack {
		public void onDataGot(List<SimpleNews> simpleNewsList,
				List<Editor> editorList, String background, int mode);

		public void onDataGotError();
	}

	// 获取具体新闻的接口
	public interface GetStoryCallBack {
		public void onDataGot(Story story);

		public void onDataGotError();
	}

	// 解析json
	public interface ParseJsonCallBack {
		// JSONObject获取成功
		public void onDataGeted(JSONObject jsonString);

		// JSONObject获取失败
		public void onDataGetError(VolleyError error);
	}

	// 数据回调
	public interface GetDataCallBack<T> {
		// 数据获得成功
		public void onDataGot(List<T> list, int mode);

		// 数据获得失败
		public void onDataGotError();
	}
}
