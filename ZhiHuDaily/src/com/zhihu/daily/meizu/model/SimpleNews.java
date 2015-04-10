package com.zhihu.daily.meizu.model;

import java.util.List;

public class SimpleNews {
	private String date;
	private String title;
	private String share_url;
	private String ga_prefix;
	private List<String> images;
	private int type;
	private int id;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getGa_prefix() {
		return ga_prefix;
	}

	public void setGa_prefix(String ga_prefix) {
		this.ga_prefix = ga_prefix;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SimpleNews [title=" + title + ", share_url=" + share_url
				+ ", ga_prefix=" + ga_prefix + ", images=" + images + ", type="
				+ type + ", id=" + id + "]";
	}
}
