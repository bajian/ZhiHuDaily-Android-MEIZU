package com.zhihu.daily.meizu.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class LoginActivity extends Activity implements OnClickListener,PlatformActionListener, Callback{
	private RelativeLayout sina_weibo_login;
	private RelativeLayout tencent_weibo_login;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_login);
		ShareSDK.initSDK(this);
		sina_weibo_login = (RelativeLayout) this
				.findViewById(R.id.sina_weibo_login);
		tencent_weibo_login = (RelativeLayout) this
				.findViewById(R.id.tencent_weibo_login);
		sina_weibo_login.setOnClickListener(this);
		tencent_weibo_login.setOnClickListener(this);
		handler = new Handler(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String weiboName  = getSharedPreferences("weibo", 0).getString("weibo", null);
		switch (v.getId()) {
		case R.id.sina_weibo_login:
			Platform sinaWeibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
			if(sinaWeibo.isValid()){
				sinaWeibo.removeAccount();
				getSharedPreferences("nick_name", 0).edit().putString("nick_name", "用户登录").commit();
			}
			
			sinaWeibo.setPlatformActionListener(this);
//			sinaWeibo.SSOSetting(false);
//			sinaWeibo.authorize();
			sinaWeibo.showUser(null);
			break;
		case R.id.tencent_weibo_login:
			
			Platform tencentWeibo = ShareSDK.getPlatform(this, TencentWeibo.NAME);
			if(tencentWeibo.isValid()){
				getSharedPreferences("nick_name", 0).edit().putString("nick_name", "用户登录").commit();
				tencentWeibo.removeAccount();
			}
			
			tencentWeibo.setPlatformActionListener(this);
//			tencentWeibo.SSOSetting(false);
//			tencentWeibo.authorize();
			tencentWeibo.showUser(null);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCancel(Platform weibo, int arg1) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 3;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}

	@Override
	public void onComplete(Platform weibo, int arg1, HashMap<String, Object> res) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		String nick = null;
		String image = null;
		msg.arg1 = 1;
		msg.obj = weibo;
		if(weibo==ShareSDK.getPlatform(this, SinaWeibo.NAME)){
			nick = (String) res.get("screen_name");
			image = res.get("avatar_large").toString();
			
		}else{
			nick = (String) res.get("nick");
			image = res.get("https_head").toString()+"/100";
		}
		
		getSharedPreferences("weibo", 0).edit().putString("weibo", weibo.getName()).commit();
		
		
		
		Bundle data = new Bundle();
		data.putString("data", nick);
		data.putString("image", image);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	@Override
	public void onError(Platform weibo, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 2;
		msg.obj = weibo;
		handler.sendMessage(msg);
	}


	@Override
	public boolean handleMessage(Message msg) {
		
		Platform weibo = (Platform) msg.obj;
		switch (msg.arg1) {
		case 1: { // 成功
			String user_name = msg.getData().getString("data", "用户登录");
			getSharedPreferences("nick_name", 0).edit().putString("nick_name", user_name).commit();
			getSharedPreferences("image", 0).edit().putString("image", msg.getData().getString("image")).commit();
			
			Toast.makeText(this, weibo.getName() + "登录成功", Toast.LENGTH_SHORT)
					.show();
		}
			break;
		case 2: { // 失败
			Toast.makeText(this, weibo.getName() + "登录失败", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		case 3: { // 取消
			Toast.makeText(this, weibo.getName() + "取消登录", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		}
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		return false;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
}
