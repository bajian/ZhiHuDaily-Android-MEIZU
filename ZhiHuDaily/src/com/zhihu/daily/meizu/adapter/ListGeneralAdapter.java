package com.zhihu.daily.meizu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.model.Theme;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class ListGeneralAdapter extends BaseAdapter {
	private List<SimpleNews> mList = new ArrayList<SimpleNews>();
	private LayoutInflater mInflater;
	private static ImageUtils imageUtils;
	private Context mContext;

	public ListGeneralAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		imageUtils = ((DailyApplication) context.getApplicationContext())
				.getImageUtils();
	}
	
	public void bindData(List<SimpleNews> list){
		this.mList = list;
	}
	
	public void addData(List<SimpleNews> list){
		this.mList.addAll(list);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SimpleNews getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ItemViewHolder itemViewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.list_item_title = (TextView) convertView
					.findViewById(R.id.list_item_title);
			// itemViewHolder.list_item_intro = (TextView)
			// convertView.findViewById(R.id.list_item_intro);
			itemViewHolder.list_item_image = (ImageView) convertView
					.findViewById(R.id.list_item_image);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		SimpleNews story = (SimpleNews) getItem(position);
		itemViewHolder.list_item_title.setText(story.getTitle());

		if (story.getImages() != null) {
			itemViewHolder.list_item_image.setVisibility(View.VISIBLE);
			if(PerferenceHelper.getTheme(mContext)==Theme.LIGHT){
				imageUtils.getImage(itemViewHolder.list_item_image, story
						.getImages().get(0), R.drawable.image_small_default);
			}else{
				imageUtils.getImage(itemViewHolder.list_item_image, story
						.getImages().get(0), R.drawable.dark_image_small_default);
			}
		} else {
			itemViewHolder.list_item_image.setVisibility(View.GONE);
		}
		return convertView;
	}

	private final class ItemViewHolder {
		public TextView list_item_title;
		// public TextView list_item_intro;
		public ImageView list_item_image;
	}

	public List<SimpleNews> getList() {
		return mList;
	}
}
