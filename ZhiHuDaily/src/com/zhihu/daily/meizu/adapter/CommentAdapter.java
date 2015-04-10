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
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhihu.daily.meizu.DailyApplication;
import com.zhihu.daily.meizu.R;
import com.zhihu.daily.meizu.model.Comment;
import com.zhihu.daily.meizu.net.ImageUtils;
import com.zhihu.daily.meizu.utils.UNIXTimeUtils;

public class CommentAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {

	private int[] mSectionIndices;
	private String[] mSectionLetters;
	private List<Comment> mList;
	private LayoutInflater mInflater;
	private int mLongCommentLength;
	private int mShortCommentLength;
	private static ImageUtils imageUtils;

	public CommentAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mList = new ArrayList<>();
		mSectionIndices = getSectionIndices();
		mSectionLetters = getSectionLetters();
		imageUtils = ((DailyApplication) context.getApplicationContext())
				.getImageUtils();
	}

	private int[] getSectionIndices() {
		int[] sections = new int[2];
		sections[0] = 0;
//		if(mLongCommentLength==0){
//			sections[1] = 1;
//		}else{
//			sections[1] = mLongCommentLength;
//		}
		sections[1] = mLongCommentLength;
		return sections;
	}

	private String[] getSectionLetters() {
		String[] letters = new String[mSectionIndices.length];
		letters[0] = mLongCommentLength + "条长评论";
		letters[1] = mShortCommentLength + "条短评论";
		return letters;
	}

	public void bindLongComment(List<Comment> list) {
		mLongCommentLength = list.size();
		this.mList = list;
	}

	public void bindShortComment(List<Comment> list) {
		mShortCommentLength = list.size();
		this.mList.addAll(list);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Comment getItem(int position) {
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
			itemViewHolder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.activity_comment_list_item, parent, false);
			itemViewHolder.comment_item_author = (TextView) convertView.findViewById(R.id.comment_item_author);
			itemViewHolder.comment_item_time = (TextView) convertView.findViewById(R.id.comment_item_time);
			itemViewHolder.comment_item_avatar = (ImageView) convertView.findViewById(R.id.comment_item_avatar);
			itemViewHolder.comment_item_content = (TextView) convertView.findViewById(R.id.comment_item_content);
			itemViewHolder.comment_item_like_count = (TextView) convertView.findViewById(R.id.comment_item_like_count);
			itemViewHolder.comment_item_empty = (RelativeLayout) convertView.findViewById(R.id.comment_item_empty);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		Comment comment = (Comment) getItem(position);
		
		itemViewHolder.comment_item_author.setText(comment.getAuthor());
		if(comment.getAuthor()!=null){
			imageUtils.getImage(itemViewHolder.comment_item_avatar, comment.getAvatar(), R.drawable.image_small_default);
		}
		itemViewHolder.comment_item_content.setText(comment.getContent());
		itemViewHolder.comment_item_like_count.setText(comment.getLikes()+"");
		itemViewHolder.comment_item_time.setText(UNIXTimeUtils.toLocalTime(comment.getTime()+""));
		itemViewHolder.comment_item_empty.setVisibility(View.GONE);
		return convertView;
	}

	private final class ItemViewHolder {
		public TextView comment_item_author;
		public TextView comment_item_time;
		public ImageView comment_item_avatar;
		public TextView comment_item_content;
		public TextView comment_item_like_count;
		public RelativeLayout comment_item_empty;
	}

	@Override
	public String[] getSections() {
		return mSectionLetters;
	}

	@Override
	public int getPositionForSection(int section) {
		if(section==0){
			return 0;
		}
		else {
			return mLongCommentLength;
		}
	}

	@Override
	public int getSectionForPosition(int position) {
		if(position<mLongCommentLength)
			return 0;
		else {
			return 1;
		}
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		final HeaderViewHolder headerViewHolder;
		if (convertView == null) {
			headerViewHolder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.activity_comment_list_header, parent, false);
			headerViewHolder.comment_list_header_icon = (ImageView) convertView.findViewById(R.id.comment_list_header_icon);
			headerViewHolder.comment_list_header_text = (TextView) convertView.findViewById(R.id.comment_list_header_text);
			convertView.setTag(headerViewHolder);
		} else {
			headerViewHolder = (HeaderViewHolder) convertView.getTag();
		}
		
		int index = getSectionForPosition(position);
		
		headerViewHolder.comment_list_header_text.setText(mSectionLetters[index]);
		if(index==1){
			headerViewHolder.comment_list_header_icon.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	private final class HeaderViewHolder {
		public TextView comment_list_header_text;
		public ImageView comment_list_header_icon;
	}

	@Override
	public long getHeaderId(int position) {
		if(position<mLongCommentLength)
			return 0;
		else {
			return 1;
		}
	}
	
	public void restore() {
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }


}
