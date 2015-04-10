package com.zhihu.daily.meizu.model;

public class ThemeStory {
	private String body;
	private String theme_name;
	private String title;
	private String share_url;
	private String ga_prefix;
	private String editor_name;
	private int theme_id;
	private int type;
	private int id;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTheme_name() {
		return theme_name;
	}

	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
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

	public String getEditor_name() {
		return editor_name;
	}

	public void setEditor_name(String editor_name) {
		this.editor_name = editor_name;
	}

	public int getTheme_id() {
		return theme_id;
	}

	public void setTheme_id(int theme_id) {
		this.theme_id = theme_id;
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
		return "ThemeStory [body=" + body + ", theme_name=" + theme_name
				+ ", title=" + title + ", share_url=" + share_url
				+ ", ga_prefix=" + ga_prefix + ", editor_name=" + editor_name
				+ ", theme_id=" + theme_id + ", type=" + type + ", id=" + id
				+ "]";
	}

}
