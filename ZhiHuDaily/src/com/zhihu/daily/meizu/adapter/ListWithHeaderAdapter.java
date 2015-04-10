package com.zhihu.daily.meizu.adapter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.SimpleNews;
import com.zhihu.daily.meizu.model.Theme;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.utils.DateUtils;
import com.zhihu.daily.meizu.utils.PerferenceHelper;

public class ListWithHeaderAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {

	private Context mContext;
	private LayoutInflater mInflater;
	private int[] mSectionIndices;
	private String[] mSectionLetters;
	private List<SimpleNews> mList;
	private static ImageUtils imageUtils;

	public ListWithHeaderAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		mList = new ArrayList<>();
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		imageUtils = ((DailyApplication) context.getApplicationContext())
				.getImageUtils();
	}

	private int[] getSectionIndices() {
		ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
		if (!mList.isEmpty()) {
			String lastDate = mList.get(0).getDate();
			sectionIndices.add(0);
			for (int i = 1; i < mList.size(); i++) {
				if (mList.get(i).getDate() != lastDate) {
					lastDate = mList.get(i).getDate();
					sectionIndices.add(i);
				}
			}
			int[] sections = new int[sectionIndices.size()];
			for (int i = 0; i < sectionIndices.size(); i++) {
				sections[i] = sectionIndices.get(i);
			}
			return sections;
		}
		return new int[0];
	}

	private String[] getSectionLetters() {
		String[] letters = new String[mSectionIndices.length];
		for (int i = 0; i < mSectionIndices.length; i++) {
			// letters[i] = mList.get(mSectionIndices[i]).getDate();
			letters[i] = DateUtils.getDate(mList.get(mSectionIndices[i])
					.getDate(), mContext);
		}
		return letters;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
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

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.fragment_home_list_header,
					parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.home_list_header_date);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
		holder.text.setText(mSectionLetters[getSectionForPosition(position)]);
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public int getPositionForSection(int section) {
		if (section >= mSectionIndices.length) {
			section = mSectionIndices.length - 1;
		} else if (section < 0) {
			section = 0;
		}
		return mSectionIndices[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		for (int i = 0; i < mSectionIndices.length; i++) {
			if (position < mSectionIndices[i]) {
				return i - 1;
			}
		}
		return mSectionIndices.length - 1;
	}

	@Override
	public Object[] getSections() {
		return mSectionLetters;
	}

	public void clear() {
		mList.clear();
		mSectionIndices = new int[0];
		mSectionLetters = new String[0];
		notifyDataSetChanged();
	}

	public void addData(List<SimpleNews> simpleNews) {
		this.mList.addAll(simpleNews);
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}

	public void bindData(List<SimpleNews> simpleNews) {
		this.mList = simpleNews;
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		notifyDataSetChanged();
	}
	
	public List<SimpleNews> getList(){
		return mList;
	}

	class HeaderViewHolder {
		TextView text;
	}

	private final class ItemViewHolder {
		public TextView list_item_title;
		// public TextView list_item_intro;
		public ImageView list_item_image;
	}
}
