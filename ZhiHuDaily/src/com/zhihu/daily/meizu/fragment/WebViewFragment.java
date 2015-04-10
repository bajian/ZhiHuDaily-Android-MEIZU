package com.zhihu.daily.meizu.fragment;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.activity.MainActivity;
import com.zhihu.daily.meizu.net.ConnectURL;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {
	private WebView webView;
	
	
	@Override 
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(5);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		((MainActivity) getActivity()).onSectionAttached(5);
		((MainActivity) getActivity()).restoreActionBar();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_webview, container, false);
		webView = (WebView) view.findViewById(R.id.webView1);
		webView.loadUrl(ConnectURL.URL_PROMOTION);
		return view;
	}
}
