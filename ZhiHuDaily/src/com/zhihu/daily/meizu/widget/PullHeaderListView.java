package com.zhihu.daily.meizu.widget;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class PullHeaderListView extends StickyListHeadersListView {
	private int headerHeight;
	private View headerView;

	public PullHeaderListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PullHeaderListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullHeaderListView(Context context) {
		this(context, null);
	}

	public void setHeaderHeight(int paramInt) {
		this.headerHeight = paramInt;
	}

	@Override
	public void addHeaderView(View v) {
		this.headerView = v;
		AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(
				getLayoutParams().width, this.headerHeight);
		this.headerView.setLayoutParams(localLayoutParams);
		super.addHeaderView(this.headerView);
	}
}
