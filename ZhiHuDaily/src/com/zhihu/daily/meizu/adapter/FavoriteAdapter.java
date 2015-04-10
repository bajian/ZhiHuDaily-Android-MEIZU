package com.zhihu.daily.meizu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.FavoriteNews;
import com.zhihu.daily.meizu.model.Theme;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class FavoriteAdapter extends ArrayAdapter<FavoriteNews> {
	private LayoutInflater mLayoutInflater = null;
	private static ImageUtils imageUtils;
	private List<FavoriteNews> list;
	private Context mContext;

	public FavoriteAdapter(Context context, List<FavoriteNews> objects) {
		super(context, R.layout.fragment_favorites_list_item, objects);
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		imageUtils = ((DailyApplication) context.getApplicationContext())
				.getImageUtils();
		this.list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ItemViewHolder itemViewHolder;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.list_item, parent,
					false);
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.favorite_item_image = (ImageView) convertView
					.findViewById(R.id.list_item_image);
			itemViewHolder.favorite_item_text = (TextView) convertView
					.findViewById(R.id.list_item_title);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		final FavoriteNews item = getItem(position);
		if (item.getImage() != null) {
			itemViewHolder.favorite_item_image.setVisibility(View.VISIBLE);
			if (PerferenceHelper.getTheme(mContext) == Theme.LIGHT) {
				imageUtils.getImage(itemViewHolder.favorite_item_image,
						item.getImage(), R.drawable.image_small_default);
			} else {
				imageUtils.getImage(itemViewHolder.favorite_item_image,
						item.getImage(), R.drawable.dark_image_small_default);
			}
		} else {
			itemViewHolder.favorite_item_image.setVisibility(View.GONE);
		}

		itemViewHolder.favorite_item_text.setText(item.getTitle());
		return convertView;
	}

	private final class ItemViewHolder {
		public ImageView favorite_item_image;
		public TextView favorite_item_text;
	}

	public List<FavoriteNews> getList() {
		return list;
	}
}
