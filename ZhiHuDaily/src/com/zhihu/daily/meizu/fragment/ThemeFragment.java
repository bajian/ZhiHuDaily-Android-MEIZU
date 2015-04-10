package com.zhihu.daily.meizu.fragment;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.activity.MainActivity;
import com.zhihu.daily.meizu.activity.ThemeActivity;
import com.zhihu.daily.meizu.adapter.ThemeAdapter;
import com.zhihu.daily.meizu.model.CategoryTheme;
import com.zhihu.daily.meizu.net.NetworkManager;
import com.zhihu.daily.meizu.net.NetworkManager.GetDataCallBack;

public class ThemeFragment extends Fragment implements
		GetDataCallBack<CategoryTheme> {
	StickyGridHeadersGridView mStickyGridHeadersGridView;
	ThemeAdapter adapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(3);
		((MainActivity) activity).restoreActionBar();

	}
	
	@Override
	public void onStart() {
		super.onStart();
		((MainActivity) getActivity()).onSectionAttached(3);
		((MainActivity) getActivity()).restoreActionBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_subscribe_grid,
				container, false);
		mStickyGridHeadersGridView = (StickyGridHeadersGridView) rootView
				.findViewById(R.id.subscribe_grid_view);
		NetworkManager.getNetworkManager(getActivity()).getCategoryTheme(this);
		mStickyGridHeadersGridView
				.setAreHeadersSticky(!((StickyGridHeadersGridView) mStickyGridHeadersGridView)
						.areHeadersSticky());
		
		mStickyGridHeadersGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),ThemeActivity.class);
				CategoryTheme categoryTheme = adapter.getItem(position);
				intent.putExtra("title", categoryTheme.getName());
				intent.putExtra("id", categoryTheme.getId());
				startActivity(intent);
			}
		});
		
		return rootView;
	}

	@Override
	public void onDataGot(List<CategoryTheme> list, int mode) {
		adapter = new ThemeAdapter(getActivity(), list);
		mStickyGridHeadersGridView.setAdapter(adapter);
	}

	@Override
	public void onDataGotError() {

	}
}
