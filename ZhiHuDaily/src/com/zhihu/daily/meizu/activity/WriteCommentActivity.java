package com.zhihu.daily.meizu.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.utils.PerferenceHelper;
import com.zhihu.daily.meizu.utils.SmartBarUtils;
import com.zhihu.daily.meizu.utils.ToastUtils;

public class WriteCommentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (SmartBarUtils.hasSmartBar()) {
			// 如有SmartBar，则使用拆分ActionBar，使MenuItem显示在底栏
			getWindow().setUiOptions(
					ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
		} else {
			// 取消ActionBar拆分，使MenuItem显示在顶栏
			getWindow().setUiOptions(0);
		}
		
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_comment_compose);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_write, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ToastUtils.showToast(this, "暂未实现评论功能", 50);
		return true;
	}
}
