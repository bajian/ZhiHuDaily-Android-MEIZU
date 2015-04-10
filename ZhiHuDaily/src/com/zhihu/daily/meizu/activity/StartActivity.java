package com.zhihu.daily.meizu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class StartActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_start);
		Intent intent = new Intent(this, WelcomeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
