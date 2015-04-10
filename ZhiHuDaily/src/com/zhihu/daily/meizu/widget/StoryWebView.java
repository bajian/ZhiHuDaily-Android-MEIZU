package com.zhihu.daily.meizu.widget;

import com.zhihu.daily.meizu.model.Theme;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebView;

public class StoryWebView extends WebView {

	public StoryWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (PerferenceHelper.getTheme(context) == Theme.DARK)
			setBackgroundColor(Color.argb(1, 0, 0, 0));
	}

	public StoryWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StoryWebView(Context context) {
		this(context, null);
	}

}
