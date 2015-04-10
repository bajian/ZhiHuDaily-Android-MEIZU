package com.zhihu.daily.meizu.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.fragment.ConfigFragment;
import com.zhihu.daily.meizu.fragment.FavoriteFragment;
import com.zhihu.daily.meizu.fragment.HomeFragment;
import com.zhihu.daily.meizu.fragment.NavigationDrawerFragment;
import com.zhihu.daily.meizu.fragment.ThemeFragment;
import com.zhihu.daily.meizu.fragment.WebViewFragment;
import com.zhihu.daily.meizu.model.Theme;
import com.zhihu.daily.meizu.utils.PerferenceHelper;
import com.zhihu.daily.meizu.utils.SmartBarUtils;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private int mTheme = Theme.LIGHT;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// 取出保存的主题
		if (savedInstanceState == null) {
			mTheme = PerferenceHelper.getTheme(this);
		} else {
			mTheme = savedInstanceState.getInt(Theme.KEY);
		}
		setTheme(mTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 什么意思
		if (mTheme != PerferenceHelper.getTheme(this)) {
			reload();
		}
	}

	public void changeTheme() {
		if (mTheme == Theme.LIGHT) {
			mTheme = Theme.DARK;

		} else {
			mTheme = Theme.LIGHT;
		}
		PerferenceHelper.saveTheme(this, mTheme);
		reload();
	}

	private void reload() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// 保护主题
		outState.putInt(Theme.KEY, mTheme);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (position) {
		case 0:
			// 登录
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		case 1:
			// 首页
			transaction.replace(R.id.container, new HomeFragment());
			break;
		case 2:
			// 主题日报
			transaction.replace(R.id.container, new ThemeFragment());
			transaction.addToBackStack(null);
			break;
		case 3:
			// 我的收藏
			transaction.replace(R.id.container, new FavoriteFragment());
			transaction.addToBackStack(null);
			break;
		case 4:
			// 应用推荐
			transaction.replace(R.id.container, new WebViewFragment());
			transaction.addToBackStack(null);
			break;
		case 5:
			// 设置
			transaction.replace(R.id.container, new ConfigFragment());
			transaction.addToBackStack(null);
			break;
		default:
			break;
		}
		transaction.setCustomAnimations(R.anim.slide_in_right,
				R.anim.slide_out_left, R.anim.slide_in_left,
				R.anim.slide_out_right);
		transaction.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 2:
			mTitle = getString(R.string.title_section1);
			break;
		case 3:
			mTitle = getString(R.string.title_section2);
			break;
		case 4:
			mTitle = getString(R.string.title_section3);
			break;
		case 5:
			mTitle = getString(R.string.title_section4);
			break;
		case 6:
			mTitle = getString(R.string.title_section5);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			if (SmartBarUtils.hasSmartBar()) {
				getMenuInflater().inflate(R.menu.main, menu);
			}
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		switch (id) {
		case R.id.action_theme:
			changeTheme();
			return true;
		case R.id.action_home:
			transaction.replace(R.id.container, new HomeFragment());
			transaction.commit();
			return true;
		case R.id.action_category:
			transaction.replace(R.id.container, new ThemeFragment());
//			transaction.addToBackStack(null);
			transaction.commit();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onThemeChanged() {
		changeTheme();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getSharedPreferences("nick_name", 0).edit().putString("nick_name", "用户登录").commit();
		getSharedPreferences("image", 0).edit().putString("image",null).commit();
	}


}
