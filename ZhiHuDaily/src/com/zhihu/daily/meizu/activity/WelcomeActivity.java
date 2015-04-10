package com.zhihu.daily.meizu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class WelcomeActivity extends Activity {
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_welcome);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_to_left);
		ImageView imageView = (ImageView)findViewById(R.id.welcome_image);
		imageView.startAnimation(animation);
		handler.sendEmptyMessageDelayed(0, 1200);
	}
}
