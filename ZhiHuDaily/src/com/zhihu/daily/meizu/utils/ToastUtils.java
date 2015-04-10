package com.zhihu.daily.meizu.utils;

import android.app.Activity;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ToastUtils {
	public static void showToast(Activity activity, String message, int time) {
		Crouton crouton = Crouton.makeText(activity, message, Style.ALERT);
		Configuration configuration = new Configuration.Builder().setDuration(
				2000).build();
		crouton.setConfiguration(configuration).show();
	}
}
