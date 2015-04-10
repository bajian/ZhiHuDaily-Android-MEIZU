package com.zhihu.daily.meizu.activity;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.FavoriteNews;
import com.zhihu.daily.meizu.model.PageStory;
import com.zhihu.daily.meizu.model.Story;
import com.zhihu.daily.meizu.model.ThemeStory;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetStoryCallBack;
import com.zhihu.daily.meizu.net.NetworkManager.GetThemeStoryContentCallBack;
import com.zhihu.daily.meizu.utils.JsoupUtils;
import com.zhihu.daily.meizu.utils.NetStateUtils;
import com.zhihu.daily.meizu.utils.PerferenceHelper;
import com.zhihu.daily.meizu.utils.SmartBarUtils;
import com.zhihu.daily.meizu.utils.ToastUtils;

public class NewsPagingActivity extends Activity {

	public static final int TYPE_HOME = 0;
	public static final int TYPE_THEME = 1;

	private ViewPager mViewPager;
	private List<PageStory> mPageStories;
	private int mPosition = 0;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private static int screenHeight;
	private static ImageUtils imageUtils;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		if (SmartBarUtils.hasSmartBar()) {
			// 如有SmartBar，则使用拆分ActionBar，使MenuItem显示在底栏
			getWindow().setUiOptions(
					ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
		} else {
			// 取消ActionBar拆分，使MenuItem显示在顶栏
			getWindow().setUiOptions(0);
		}

		// 错误
		// getActionBar().setTitle("123".toString());

		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_news_paging);
		imageUtils = ((DailyApplication) getApplicationContext())
				.getImageUtils();

		mViewPager = (ViewPager) findViewById(R.id.news_pager);

		// 无效
		// mViewPager.setOffscreenPageLimit(1);

		mPageStories = (List<PageStory>) getIntent().getSerializableExtra(
				"news");

		mPosition = getIntent().getIntExtra("position", 0);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setCurrentItem(mPosition);

		// 设置无效
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		screenHeight = (int) (0.625F * localDisplayMetrics.widthPixels);
	}

	public void begin() {
		setProgressBarIndeterminateVisibility(true);
	}

	public void end() {
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.news_paging, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favorite:
			boolean flag;
			PageStory pageStory = mPageStories.get(mViewPager.getCurrentItem());
			if (pageStory != null) {
				FavoriteNews favoriteNews = new FavoriteNews();
				favoriteNews.setImage(pageStory.getImage());
				favoriteNews.setNewsId(pageStory.getId());
				favoriteNews.setTitle(pageStory.getTitle());
				favoriteNews.setUrl(pageStory.getShare_url());
				favoriteNews.setType(pageStory.getType());
				flag = favoriteNews.store();
				if (flag) {
					ToastUtils.showToast(this, "收藏成功", 50);
				} else {
					ToastUtils.showToast(this, "删除成功", 50);
				}
			} else {
				// ToastUtils.showToast(this, "收藏失败", 50);
			}
			return true;
		case R.id.action_comment:
			Intent intent = new Intent(this, CommentActivity.class);
			intent.putExtra("id", mPageStories.get(mViewPager.getCurrentItem())
					.getId());
			startActivity(intent);
			break;
		case R.id.action_share:
			Intent intent1 = new Intent(Intent.ACTION_SEND);
			intent1.setType("text/plain");
			intent1.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent1.putExtra(Intent.EXTRA_TEXT,
					mPageStories.get(mViewPager.getCurrentItem())
							.getShare_url());
			startActivity(Intent.createChooser(intent1, "请选择分享方式！"));

			break;
		default:

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment;
			if (mPageStories.get(position).getType() == NewsPagingActivity.TYPE_HOME) {
				fragment = PlaceholderFragment.newInstance(mPageStories.get(
						position).getId());

			} else {
				fragment = ThemePlaceholderFragment.newInstance(mPageStories
						.get(position).getId());
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return mPageStories.size();
		}
	}

	public static class PlaceholderFragment extends Fragment implements
			GetStoryCallBack {
		private static final String ARG_SECTION_NUMBER = "section_id";
		private ImageView image;
		private WebView webView;
		private TextView textView;
		private TextView text;
		public Story story;

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_newspage,
					container, false);
			webView = (WebView) rootView.findViewById(R.id.pageWebView);

			image = (ImageView) rootView.findViewById(R.id.topstory_image);

			ViewGroup.LayoutParams params = image.getLayoutParams();
			params.height = screenHeight;
			params.width = LayoutParams.MATCH_PARENT;
			image.setLayoutParams(params);

			textView = (TextView) rootView.findViewById(R.id.topstory_title);

			text = (TextView) rootView.findViewById(R.id.littleText);

			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
			int id = getArguments().getInt(ARG_SECTION_NUMBER);
			((NewsPagingActivity) getActivity()).begin();
			NetworkManager.getNetworkManager(getActivity()).getNewsPage(this,
					id);
		}

		@Override
		public void onDataGot(Story story) {
			this.story = story;
			String HTMLString = JsoupUtils.getPage(story.getBody(),
					getActivity());

			
			
			if(PerferenceHelper.getIsOnlyDownImageInWifi(getActivity())){
				if(!NetStateUtils.isWifiConnected(getActivity())){
					// 设置不加载图片true不加载
					 webView.getSettings().setBlockNetworkImage(true);
				}
			}

			webView.loadDataWithBaseURL(null, HTMLString, "text/html", "utf-8",
					null);

			// 设置webView背景黑色
			// try {
			// Class clsWebSettingsClassic = getActivity().getClassLoader()
			// .loadClass("android.webkit.WebSettingsClassic");
			// Method md = clsWebSettingsClassic.getMethod("setProperty",
			// String.class, String.class);
			// md.invoke(webView.getSettings(), "inverted", "true");
			// md.invoke(webView.getSettings(), "inverted_contrast", "1");
			//
			// } catch (Exception e) {
			// }
			if (story.getImage() != null) {
				imageUtils.getImage(image, story.getImage(),
						R.drawable.image_top_default);
			}

			textView.setText(story.getTitle());
			text.setText(story.getImage_source());
			if (getActivity() != null) {
				((NewsPagingActivity) getActivity()).end();
			}

		}

		@Override
		public void onDataGotError() {
			// ToastUtils.showToast(getActivity(), "网络错误", 50);
		}

	}

	public static class ThemePlaceholderFragment extends Fragment implements
			GetThemeStoryContentCallBack {
		private static final String ARG_SECTION_NUMBER = "section_id";

		private WebView news_webview;

		private TextView news_editor_name;

		private TextView news_theme_name;

		private LinearLayout news_toolbar;

		private ThemeStory themeStory;

		public static ThemePlaceholderFragment newInstance(int sectionNumber) {
			ThemePlaceholderFragment fragment = new ThemePlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public ThemePlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_news, container,
					false);

			news_webview = (WebView) rootView.findViewById(R.id.news_webview);

			news_editor_name = (TextView) rootView
					.findViewById(R.id.news_editor_name);

			news_theme_name = (TextView) rootView
					.findViewById(R.id.news_theme_name);

			news_toolbar = (LinearLayout) rootView
					.findViewById(R.id.news_toolbar);

			news_toolbar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// getActivity().onBackPressed();
					if(themeStory!=null){
						Intent intent = new Intent(getActivity(),
								ThemeActivity.class);
						intent.putExtra("title", themeStory.getTheme_name());
						intent.putExtra("id", themeStory.getTheme_id());
						startActivity(intent);
						getActivity().finish();
					}
					
				}
			});

			return rootView;
		}

		@Override
		public void onResume() {
			super.onResume();
			int id = getArguments().getInt(ARG_SECTION_NUMBER);
			((NewsPagingActivity) getActivity()).begin();
			NetworkManager.getNetworkManager(getActivity()).getThemeNewsPage(
					this, id);
		}

		@Override
		public void onDataGot(ThemeStory themeStory) {
			this.themeStory = themeStory;

			String HTMLString = JsoupUtils.getPage(themeStory.getBody(),
					getActivity());
			
			if(PerferenceHelper.getIsOnlyDownImageInWifi(getActivity())){
				if(!NetStateUtils.isWifiConnected(getActivity())){
					// 设置不加载图片true不加载
					news_webview.getSettings().setBlockNetworkImage(true);
				}
			}

			news_webview.loadDataWithBaseURL(null, HTMLString, "text/html",
					"utf-8", null);

			news_editor_name.setText(themeStory.getEditor_name());
			news_theme_name.setText(themeStory.getTheme_name());
			((NewsPagingActivity) getActivity()).end(); 
			
		}

		@Override
		public void onDataGotError() {

		}

	}
}
