package com.zhihu.daily.meizu.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.adapter.EditorAdapter;
import com.zhihu.daily.meizu.adapter.ListGeneralAdapter;
import com.zhihu.daily.meizu.model.Editor;
import com.zhihu.daily.meizu.model.PageStory;
import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetThemeStoryCallBack;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class ThemeActivity extends Activity implements OnRefreshListener,
		GetThemeStoryCallBack {
	private int mThemeId;
	private String mTitle;
	private ListView themeListView;
	private PullToRefreshLayout mPullToRefreshLayout;
	private ListGeneralAdapter mListGeneralAdapter;
	private EditorAdapter mEditorAdapter;
	private View mHeadView;
	private ListView mEditorListView;
	// private TextView theme_image_source;
	// private TextView theme_intro;
	private ImageView theme_image;
	private ImageUtils imageUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_theme);
		mThemeId = getIntent().getIntExtra("id", 1);
		mTitle = getIntent().getStringExtra("title");
		getActionBar().setTitle(mTitle);
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.theme_layout);
		themeListView = (ListView) findViewById(R.id.theme_list_view);
		ActionBarPullToRefresh.from(this).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);

		imageUtils = ((DailyApplication) getApplicationContext())
				.getImageUtils();

		mHeadView = LayoutInflater.from(this).inflate(
				R.layout.activity_theme_header, null);

		// theme_image_source = (TextView)
		// mHeadView.findViewById(R.id.theme_image_source);

		// theme_intro = (TextView) mHeadView.findViewById(R.id.theme_intro);

		theme_image = (ImageView) mHeadView.findViewById(R.id.theme_image);

		mEditorListView = (ListView) mHeadView
				.findViewById(R.id.theme_editor_list_view);

		mEditorListView.setEnabled(false);

		mEditorAdapter = new EditorAdapter(this);

		mEditorListView.setAdapter(mEditorAdapter);

		themeListView.addHeaderView(mHeadView);

		mListGeneralAdapter = new ListGeneralAdapter(this);
		themeListView.setAdapter(mListGeneralAdapter);

		themeListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<PageStory> list = new ArrayList<>();
				List<SimpleNews> simpleNewsList = mListGeneralAdapter.getList();
				for (SimpleNews simpleNews : simpleNewsList) {
					PageStory pageStory = new PageStory();
					pageStory.setId(simpleNews.getId());
					pageStory.setTitle(simpleNews.getTitle());
					if(simpleNews.getImages()!=null){
						pageStory.setImage(simpleNews.getImages().get(0));
					}
					pageStory.setShare_url(simpleNews.getShare_url());
					pageStory.setType(NewsPagingActivity.TYPE_THEME);
					list.add(pageStory);
				}
				Intent intent = new Intent(ThemeActivity.this, NewsPagingActivity.class);
				intent.putExtra("news", (Serializable) list);
				intent.putExtra("position", position - 1);
				startActivity(intent);
			}
		});

		NetworkManager.getNetworkManager(this).getThemePage(this, true,
				mThemeId, NetworkManager.MODE_REFRESH);
	}

	@Override
	public void onRefreshStarted(View view) {
		NetworkManager.getNetworkManager(this).getThemePage(this, false,
				mThemeId, NetworkManager.MODE_REFRESH);
	}

	@Override
	public void onDataGot(List<SimpleNews> simpleNewsList,
			List<Editor> editorList, String background, int mode) {
		mPullToRefreshLayout.setRefreshComplete();
		if (mode == NetworkManager.MODE_REFRESH) {

			mEditorAdapter.bindData(editorList);

			mListGeneralAdapter.bindData(simpleNewsList);

			imageUtils.getImage(theme_image, background,
					R.drawable.image_top_default);
		}

		mEditorAdapter.notifyDataSetChanged();

		// ListViewUtils.setListViewHeightBasedOnChildren(mEditorListView);

		mListGeneralAdapter.notifyDataSetChanged();

		LayoutParams params = mEditorListView.getLayoutParams();
		params.height = editorList.size() * 60;
		mEditorListView.setLayoutParams(params);
	}

	@Override
	public void onDataGotError() {
		mPullToRefreshLayout.setRefreshComplete();
	}
}
