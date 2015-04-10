package com.zhihu.daily.meizu.fragment;

import java.io.Serializable;
import java.util.ArrayList;
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
import android.widget.ListView;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.activity.MainActivity;
import com.zhihu.daily.meizu.activity.NewsPagingActivity;
import com.zhihu.daily.meizu.adapter.FavoriteAdapter;
import com.zhihu.daily.meizu.model.FavoriteNews;
import com.zhihu.daily.meizu.model.PageStory;

public class FavoriteFragment extends Fragment {
	private ListView mListView;
	private FavoriteAdapter mFavoriteAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(4);
		mFavoriteAdapter = new FavoriteAdapter(activity, FavoriteNews.getAll());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		((MainActivity) getActivity()).onSectionAttached(4);
		((MainActivity) getActivity()).restoreActionBar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_favorites,
				container, false);
		mListView = (ListView) rootView.findViewById(R.id.favorite_listview);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						NewsPagingActivity.class);

				List<PageStory> list = new ArrayList<>();
				List<FavoriteNews> favoriteNewsList = mFavoriteAdapter
						.getList();
				for (FavoriteNews favoriteNews : favoriteNewsList) {
					PageStory pageStory = new PageStory();
					pageStory.setId(favoriteNews.getNewsId());
					pageStory.setImage(favoriteNews.getImage());
					pageStory.setType(favoriteNews.getType());
					pageStory.setTitle(favoriteNews.getTitle());
					pageStory.setShare_url(favoriteNews.getUrl());
					list.add(pageStory);
				}
				intent.putExtra("news", (Serializable) list);
				intent.putExtra("position", position);

				intent.putExtra("position", position);

				startActivity(intent);
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mListView.setAdapter(mFavoriteAdapter);
	}
}
