package com.zhihu.daily.meizu.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.zhihu.daily.meizu.model.Theme;

public class PerferenceHelper {
	public static int getTheme(Context context) {
		try {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getInt(Theme.KEY, Theme.LIGHT);

		} catch (Exception e) {
			return Theme.LIGHT;
		}

	}

	public static void saveTheme(Context context, int theme) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt(Theme.KEY, theme).commit();
	}

	public static boolean getIsLarge(Context context) {
		try {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean("key_big_font_mode_open", false);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean getIsDownloadWhenWIFI(Context context){
		try {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean("key_auto_offline_open", false);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean getIsOnlyDownImageInWifi(Context context){
		try {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean("key_no_image_mode_open", false);
		} catch (Exception e) {
			return false;
		}
	}
}
