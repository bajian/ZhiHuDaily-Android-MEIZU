package com.zhihu.daily.meizu.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.activity.MainActivity;
import com.zhihu.daily.meizu.utils.ClearUtils;
import com.zhihu.daily.meizu.utils.ToastUtils;

public class ConfigFragment extends PreferenceFragment implements OnPreferenceChangeListener, OnPreferenceClickListener {
	private Preference mClearPreference;
	private Preference mCheakUpdatePreference;
	private Preference mSuggestPreference;
	private CheckBoxPreference mAutoOfflinePreference;
	private CheckBoxPreference mNoImagePreference;
	private CheckBoxPreference mBigFontPreference;
	@Override 
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		((MainActivity) getActivity()).onSectionAttached(6);
		((MainActivity) getActivity()).restoreActionBar();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		mClearPreference = findPreference("preferences_cache_clear");
		mClearPreference.setOnPreferenceClickListener(this);
		mSuggestPreference = findPreference("preferences_feedback");
		mSuggestPreference.setOnPreferenceClickListener(this);
		mCheakUpdatePreference = findPreference("preferences_app_check");
		mCheakUpdatePreference.setOnPreferenceClickListener(this);
		mAutoOfflinePreference = (CheckBoxPreference) findPreference(getString(R.string.preference_key_auto_offline_open));
		mAutoOfflinePreference.setOnPreferenceChangeListener(this);
		mNoImagePreference = (CheckBoxPreference) findPreference(getString(R.string.preference_key_no_image_mode_open));
		mNoImagePreference.setOnPreferenceChangeListener(this);
		mBigFontPreference = (CheckBoxPreference) findPreference(getString(R.string.preference_key_big_font_mode_open));
		mBigFontPreference.setOnPreferenceChangeListener(this);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		switch (preference.getKey()) {
		case "preferences_cache_clear":
			ClearUtils.cleanApplicationData(getActivity());
			((DailyApplication)getActivity().getApplicationContext()).getImageUtils().clear();
			ToastUtils.showToast(getActivity(), "缓存清理成功", 50);
			return true;
		case "preferences_feedback":
			ToastUtils.showToast(getActivity(), "谢谢合作", 50);
			return true;
		case "preferences_app_check":
			ToastUtils.showToast(getActivity(), "已经是最新版本", 50);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		ToastUtils.showToast(getActivity(), "设置成功", 50);
		return true;
	}
}
