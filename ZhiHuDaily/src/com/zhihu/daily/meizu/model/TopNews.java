package com.zhihu.daily.meizu.model;

public class TopNews{
	private String title;
	private String image;
	private String share_url;
	private String ga_prefix;
	private int type;
	private int id;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
		return "Top_Story [title=" + title + ", image=" + image
				+ ", share_url=" + share_url + ", ga_prefix=" + ga_prefix
				+ ", type=" + type + ", id=" + id + "]";
	}
}
