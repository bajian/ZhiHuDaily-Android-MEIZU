package com.zhihu.daily.meizu.adapter;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.CategoryTheme;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.widget.DynamicImageView;
import com.zhihu.daily.meizu.widget.DynamicView;

public class ThemeAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter {

	private LayoutInflater mInflater;

	private List<CategoryTheme> mItems;

	private static ImageUtils imageUtils;

	public ThemeAdapter(Context context, List<CategoryTheme> items) {
		init(context, items);
	}

	public ThemeAdapter(Context context, CategoryTheme[] items) {
		init(context, Arrays.asList(items));
	}

	private void init(Context context, List<CategoryTheme> items) {
		this.mItems = items;
		mInflater = LayoutInflater.from(context);
		imageUtils = ((DailyApplication) context.getApplicationContext())
				.getImageUtils();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public long getHeaderId(int position) {
		CategoryTheme item = getItem(position);
		String value;
		value = item.getCategory();
		if (value.toString().equals("更多领域推荐"))
			return 0;
		else
			return 1;
	}

	@Override
	public CategoryTheme getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.fragment_subscribe_grid_item, parent, false);
			holder = new ViewHolder();
			holder.fragment_subscribe_text = (TextView) convertView
					.findViewById(R.id.fragment_subscribe_text);
			holder.dynamicImageView = (DynamicImageView) convertView
					.findViewById(R.id.fragment_subscribe_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CategoryTheme item = getItem(position);
		holder.fragment_subscribe_text.setText(item.getName());
		imageUtils.getImage(holder.dynamicImageView, item.getImage(),
				R.drawable.image_small_default);
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.fragment_subscribe_grid_header, parent, false);
			holder = new HeaderViewHolder();
			holder.textView = (TextView) convertView
					.findViewById(R.id.subscribe_list_header_text);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		CategoryTheme item = getItem(position);
		holder.textView.setText(item.getCategory());

		return convertView;
	}

	protected class HeaderViewHolder {
		public TextView textView;
	}

	protected class ViewHolder {
		public DynamicImageView dynamicImageView;
		public DynamicView dynamicView;
		public TextView fragment_subscribe_text;
	}
}
