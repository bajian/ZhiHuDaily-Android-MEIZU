package com.zhihu.daily.meizu.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.adapter.CommentAdapter;
import com.zhihu.daily.meizu.model.Comment;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetDataCallBack;
import com.zhihu.daily.meizu.utils.PerferenceHelper;
import com.zhihu.daily.meizu.utils.SmartBarUtils;
import com.zhihu.daily.meizu.utils.ToastUtils;
import com.zhihu.daily.meizu.widget.PullHeaderListView;

public class CommentActivity extends Activity implements
		GetDataCallBack<Comment> {
	private int newsId;
	private PullHeaderListView mPullHeaderListView;
	private CommentAdapter mCommentAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		
		if (SmartBarUtils.hasSmartBar()) {
			// 如有SmartBar，则使用拆分ActionBar，使MenuItem显示在底栏
			getWindow().setUiOptions(
					ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
		} else {
			// 取消ActionBar拆分，使MenuItem显示在顶栏
			getWindow().setUiOptions(0);
		}
		
		setTheme(PerferenceHelper.getTheme(this));
		setContentView(R.layout.activity_comment);

		mPullHeaderListView = (PullHeaderListView) findViewById(R.id.comment_list_view);

		mPullHeaderListView.setAreHeadersSticky(false);

		mCommentAdapter = new CommentAdapter(this);

		mPullHeaderListView.setAdapter(mCommentAdapter);

		newsId = getIntent().getIntExtra("id", 0);
		NetworkManager.getNetworkManager(this).getComment(this, newsId,
				NetworkManager.TYPE_LONG);
		begin();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.comment_look, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this,WriteCommentActivity.class);
		startActivity(intent);
		return true;
	}

	public void begin() {
		setProgressBarIndeterminateVisibility(true);
	}

	public void end() {
		setProgressBarIndeterminateVisibility(false);
		if (mCommentAdapter.getCount() == 0) {
			ToastUtils.showToast(this, "无评论", 1000);
		}
	}

	@Override
	public void onDataGot(List<Comment> list, int mode) {
		if (mode == NetworkManager.TYPE_LONG) {
			mCommentAdapter.bindLongComment(list);
			NetworkManager.getNetworkManager(this).getComment(this, newsId,
					NetworkManager.TYPE_SHORT);
		} else {
			mCommentAdapter.bindShortComment(list);
			end();
		}
		mCommentAdapter.restore();
	}

	@Override
	public void onDataGotError() {
		// ToastUtils.showToast(this, "网络错误", 50);
	}
}
