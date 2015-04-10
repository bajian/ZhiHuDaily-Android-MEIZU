package com.zhihu.daily.meizu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.Editor;

public class EditorAdapter extends BaseAdapter {
	
	private List<Editor> mList = new ArrayList<Editor>();
	private LayoutInflater mInflater;
	
	public EditorAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	public void bindData(List<Editor> list){
		this.mList = list;
	}
	
	public void addData(List<Editor> list){
		this.mList.addAll(list);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Editor getItem(int position) {
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
			convertView = mInflater.inflate(R.layout.activity_theme_editor_list_item, parent, false);
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.theme_editor_name = (TextView) convertView.findViewById(R.id.theme_editor_name);
			itemViewHolder.theme_editor_bio = (TextView) convertView.findViewById(R.id.theme_editor_bio);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		Editor editor = (Editor) getItem(position);
		itemViewHolder.theme_editor_name.setText(editor.getName());

		itemViewHolder.theme_editor_bio.setText(editor.getBio());
		
		return convertView;
	}
	
	private final class ItemViewHolder {
		public TextView theme_editor_name;
		public TextView theme_editor_bio;
	}

	public List<Editor> getList() {
		return mList;
	}

}
