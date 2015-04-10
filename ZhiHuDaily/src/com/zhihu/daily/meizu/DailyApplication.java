package com.zhihu.daily.meizu;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.activeandroid.app.Application;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class DailyApplication extends Application {
	private String todayString;
	private ImageUtils imageUtils;
	private int weekDay;

	BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectMgr
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				// unconnect network
			} else {
				// connect network
				if(wifiNetInfo.isConnected()){
					if(PerferenceHelper.getIsDownloadWhenWIFI(DailyApplication.this)){
						Intent serverIntent = new Intent(DailyApplication.this,DownLoadService.class);
						DailyApplication.this.startService(serverIntent);
					}
					
				}
			}
		}
	};
	@Override
	public void onTerminate() {
		super.onTerminate();
		unregisterReceiver(connectionReceiver);
	};

	@Override
	public void onCreate() {
		super.onCreate();
		
		IntentFilter intentFilter = new IntentFilter();
		  intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		  registerReceiver(connectionReceiver, intentFilter);
		
		imageUtils = ImageUtils.getInstance(this);
		getToday();
	}
	
	

	public String getTodayString() {
		return todayString;
	}

	public ImageUtils getImageUtils() {
		return imageUtils;
	}

	public int getWeekDay() {
		return weekDay;
	}

	private void getToday() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int monthOfYear = c.get(Calendar.MONTH);
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		if (monthOfYear + 1 < 10) {
			builder.append(0);
		}
		builder.append(monthOfYear + 1);
		builder.append(dayOfMonth);
		todayString = builder.toString();
		weekDay = dayOfWeek;
	}
	
	
}
