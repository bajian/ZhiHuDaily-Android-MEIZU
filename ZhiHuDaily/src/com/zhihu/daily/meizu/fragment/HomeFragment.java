package com.zhihu.daily.meizu.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh.SetupWizard;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.viewdelegates.AbsListViewDelegate;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.activity.MainActivity;
import com.zhihu.daily.meizu.activity.NewsPagingActivity;
import com.zhihu.daily.meizu.adapter.ListWithHeaderAdapter;
import com.zhihu.daily.meizu.model.PageStory;
import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.model.TopNews;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetDataCallBack;
import com.zhihu.daily.meizu.widget.PullHeaderListView;

public class HomeFragment extends Fragment implements OnRefreshListener {
	private static final String TAG = "HomeFragment";

	private PullToRefreshLayout mPullToRefreshLayout;
	private SimpleNewsCallBack mSimpleNewsCallBack = new SimpleNewsCallBack();
	private TopNewsCallBack mTopNewsCallBack = new TopNewsCallBack();
	private PullHeaderListView mListView;
	private View mHeadView;
	private ViewPager mPager;
	private PageIndicator mIndicator;
	private ListWithHeaderAdapter mListAdapter;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private boolean isBottom;
	private static ImageUtils imageUtils;
	private static List<TopNews> mTopNewsList;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// 更改主界面标题
//		((MainActivity) activity).onSectionAttached(2);
//		((MainActivity) activity).restoreActionBar();

		imageUtils = ((DailyApplication) activity.getApplicationContext())
				.getImageUtils();
	}


	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		
		
		
		mTopNewsList = new ArrayList<>();
		mListAdapter = new ListWithHeaderAdapter(getActivity());

		
		//版本判断
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
			mSectionsPagerAdapter = new SectionsPagerAdapter(
			getChildFragmentManager());
		}else{
			mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		}
		

		NetworkManager.getNetworkManager(getActivity()).getLatest(
				mSimpleNewsCallBack, mTopNewsCallBack, true);

		View view = inflater.inflate(R.layout.fragment_home, container, false);

		mListView = (PullHeaderListView) view.findViewById(R.id.home_listview);
		mListView.setAreHeadersSticky(false);

		mHeadView = inflater.inflate(R.layout.fragment_home_header, null);
		mPager = (ViewPager) mHeadView.findViewById(R.id.topstory_pager);

		mIndicator = (CirclePageIndicator) mHeadView
				.findViewById(R.id.topstory_pager_indicator);

		mPager.setAdapter(mSectionsPagerAdapter);
		mIndicator.setViewPager(mPager);
		
		
		
//		//版本判断
//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
////			mSectionsPagerAdapter = new SectionsPagerAdapter(
////			getChildFragmentManager());
//		}else{
//			mTopNewsList = new ArrayList<>();
//			
//			mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
//			mPager.setAdapter(mSectionsPagerAdapter);
//			mIndicator.setViewPager(mPager);
//		}

		// HeadView高度设为屏幕一半
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		super.getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(localDisplayMetrics);
		int height = (int) (0.625F * localDisplayMetrics.widthPixels);
		// 顺序不能变
		mListView.setHeaderHeight(height);
		mListView.setDividerHeight(0);
		mListView.addHeaderView(mHeadView);

		// mListAdapter.clear();

		mListView.setAdapter(mListAdapter);

		mPullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.home_layout);

		// 事件委托
		SetupWizard setupWizard = ActionBarPullToRefresh.from(getActivity());
		setupWizard.useViewDelegate(StickyListHeadersListView.class,
				new AbsListViewDelegate() {
					@Override
					public boolean isReadyForPull(View view, float x, float y) {
						return super.isReadyForPull(
								((StickyListHeadersListView) view)
										.getWrappedList(), x, y);
					}
				});
		setupWizard.allChildrenArePullable().listener(this)
				.setup(mPullToRefreshLayout);

		// ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
		// .listener(this).setup(mPullToRefreshLayout);

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isBottom
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// ToastUtils.showToast(getActivity(), "正在加载数据", 30);
					NetworkManager.getNetworkManager(getActivity()).getBefore(
							mSimpleNewsCallBack,
							((SimpleNews) (mListAdapter.getItem(mListAdapter
									.getCount() - 1))).getDate(), true);

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				isBottom = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						NewsPagingActivity.class);
				List<PageStory> list = new ArrayList<>();
				List<SimpleNews> simpleNewsList = mListAdapter.getList();
				for (SimpleNews simpleNews : simpleNewsList) {
					PageStory pageStory = new PageStory();
					pageStory.setId(simpleNews.getId());
					if(simpleNews.getImages()!=null){
						pageStory.setImage(simpleNews.getImages().get(0));
					}
					
					pageStory.setType(NewsPagingActivity.TYPE_HOME);
					pageStory.setTitle(simpleNews.getTitle());
					pageStory.setShare_url(simpleNews.getShare_url());
					list.add(pageStory);
				}
				intent.putExtra("news", (Serializable) list);
				intent.putExtra("position", position - 1);
				startActivity(intent);
			}
		});

		return view;
	}

	@SuppressLint("NewApi")
	@Override
	public void onStart() {
		super.onStart();
		
		((MainActivity) getActivity()).onSectionAttached(2);
		((MainActivity) getActivity()).restoreActionBar();

//		
//		//版本判断
//		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
//			mTopNewsList = new ArrayList<>();
//			
//			mSectionsPagerAdapter = new SectionsPagerAdapter(
//			getChildFragmentManager());
//			
//			
//			mPager.setAdapter(mSectionsPagerAdapter);
//			mIndicator.setViewPager(mPager);
//		}else{
////			mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
//		}
//		
//		
//		
//		
//		
//
//		NetworkManager.getNetworkManager(getActivity()).getLatest(
//				mSimpleNewsCallBack, mTopNewsCallBack, true);
		
		
	}

	@Override
	public void onRefreshStarted(View view) {
		NetworkManager.getNetworkManager(getActivity()).getLatest(
				mSimpleNewsCallBack, mTopNewsCallBack, false);
	}

	// SimpleNews回调
	class SimpleNewsCallBack implements GetDataCallBack<SimpleNews> {

		@Override
		public void onDataGot(List<SimpleNews> list, int mode) {
			if (mPullToRefreshLayout != null) {
				mPullToRefreshLayout.setRefreshComplete();
			}
			if (mode == NetworkManager.MODE_REFRESH) {
				mListAdapter.bindData(list);
				NetworkManager.getNetworkManager(getActivity()).getBefore(
						mSimpleNewsCallBack,
						list.get(list.size() - 1).getDate(), true);
			} else {
				mListAdapter.addData(list);
			}

		}

		@Override
		public void onDataGotError() {
			if (mPullToRefreshLayout != null) {
				mPullToRefreshLayout.setRefreshComplete();
			}
			// ToastUtils.showToast(getActivity(), "网络错误", 50);
		}
	}

	// TopNews回调
	class TopNewsCallBack implements GetDataCallBack<TopNews> {

		@Override
		public void onDataGot(List<TopNews> list, int mode) {
			mTopNewsList = list;
			mSectionsPagerAdapter.notifyDataSetChanged();
		}

		@Override
		public void onDataGotError() {

		}

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PlaceholderFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return mTopNewsList.size();
		}

	}

	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";

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

			final int position = getArguments().getInt(ARG_SECTION_NUMBER);

			View rootView = inflater.inflate(
					R.layout.fragment_home_header_topstory_pager, container,
					false);

			final TopNews top_Story = mTopNewsList.get(position);

			TextView topstory_title = (TextView) rootView
					.findViewById(R.id.topstory_title);

			topstory_title.setText(top_Story.getTitle());

			ImageView imageView = (ImageView) rootView
					.findViewById(R.id.topstory_image);

			if (top_Story.getImage() != null) {
				imageUtils.getImage(imageView, top_Story.getImage(),
						R.drawable.image_top_default);
			}

			rootView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							NewsPagingActivity.class);
					List<PageStory> list = new ArrayList<>();
					for (TopNews topNews : mTopNewsList) {
						PageStory pageStory = new PageStory();
						pageStory.setId(topNews.getId());
						pageStory.setImage(topNews.getImage());
						pageStory.setType(NewsPagingActivity.TYPE_HOME);
						pageStory.setTitle(top_Story.getTitle());
						pageStory.setShare_url(top_Story.getShare_url());
						list.add(pageStory);
					}
					intent.putExtra("news", (Serializable) list);

					intent.putExtra("position", position);
					startActivity(intent);
				}
			});

			return rootView;
		}
	}

}
