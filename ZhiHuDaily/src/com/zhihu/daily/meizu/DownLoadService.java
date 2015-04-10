package com.zhihu.daily.meizu;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.model.Story;
import com.zhihu.daily.meizu.model.TopNews;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetDataCallBack;
import com.zhihu.daily.meizu.net.NetworkManager.GetStoryCallBack;

public class DownLoadService extends Service {
	private static final String TAG = "DownLoadService";
	private NotificationManager mNotifyManager;
	private Notification.Builder mBuilder;
	private int ID = 1;

	private SimpleNewsCallBack mSimpleNewsCallBack;

	private TopNewsCallBack mTopNewsCallBack;

	private int mTotalNumber;

	private int mCurrentNumber;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "----onCreate---->");
		mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mSimpleNewsCallBack = new SimpleNewsCallBack();

		mTopNewsCallBack = new TopNewsCallBack();
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mBuilder = new Notification.Builder(this);
		mBuilder.setContentTitle("正在离线下载").setContentText("正在准备下载")
				.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = mBuilder.build();
		// 设置不能取消
		notification.flags = Notification.FLAG_NO_CLEAR;
		mNotifyManager.notify(ID, notification);

		NetworkManager.getNetworkManager(getApplicationContext()).getLatest(
				mSimpleNewsCallBack, mTopNewsCallBack, false);

		return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "----onCreate---->");
	}

	@SuppressLint("NewApi")
	private void update() {
		mBuilder.setProgress(mTotalNumber, mCurrentNumber, false);
		Notification notification = mBuilder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		mNotifyManager.notify(ID, notification);
		if (mCurrentNumber == mTotalNumber) {
			mBuilder.setContentText("下载完成").setProgress(0, 0, false);
			mNotifyManager.notify(ID, mBuilder.build());
		}
	}
	
	@SuppressLint("NewApi")
	private void error(){
		mBuilder.setContentText("网络错误").setProgress(0, 0, false);
		mNotifyManager.notify(ID, mBuilder.build());
	}

	// SimpleNews回调
	class SimpleNewsCallBack implements GetDataCallBack<SimpleNews> {

		@Override
		public void onDataGot(List<SimpleNews> list, int mode) {
			mTotalNumber += list.size();
			for (SimpleNews simpleNews : list)
				NetworkManager.getNetworkManager(getApplicationContext())
						.getNewsPage(new GetStoryCallBack() {
							@Override
							public void onDataGotError() {
								mCurrentNumber += 1;
								update();
							}

							@Override
							public void onDataGot(Story story) {
								mCurrentNumber += 1;
								update();
							}
						}, simpleNews.getId());
		}

		@Override
		public void onDataGotError() {
			error();
		}
	}

	// TopNews回调
	class TopNewsCallBack implements GetDataCallBack<TopNews> {

		@Override
		public void onDataGot(List<TopNews> list, int mode) {
			mTotalNumber += list.size();
			for (TopNews topNews : list)
				NetworkManager.getNetworkManager(getApplicationContext())
						.getNewsPage(new GetStoryCallBack() {
							@Override
							public void onDataGotError() {
								mCurrentNumber += 1;
								update();
							}

							@Override
							public void onDataGot(Story story) {
								mCurrentNumber += 1;
								update();
							}
						}, topNews.getId());
		}

		@Override
		public void onDataGotError() {

		}

	}
}
