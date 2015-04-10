package com.zhihu.daily.meizu.adapter;

import java.util.List;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.DrawerLayoutMenuItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerLayoutAdapter extends ArrayAdapter<DrawerLayoutMenuItem> {
	private LayoutInflater mLayoutInflater = null;

	public DrawerLayoutAdapter(Context context,
			List<DrawerLayoutMenuItem> objects) {
		super(context, R.layout.fragment_navigation_drawer_list_item, objects);
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ItemViewHolder itemViewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.fragment_navigation_drawer_list_item, parent, false);
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.logo = (ImageView) convertView.findViewById(R.id.drawer_item_logo);
			itemViewHolder.title = (TextView) convertView.findViewById(R.id.drawer_item_title);
			convertView.setTag(itemViewHolder);
		}else{
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		final DrawerLayoutMenuItem item = getItem(position);
		itemViewHolder.logo.setImageResource(item.getImage());
		itemViewHolder.title.setText(item.getTitle());
		return convertView;
	}

	private final class ItemViewHolder {
		public ImageView logo;
		public TextView title;
	}

}
