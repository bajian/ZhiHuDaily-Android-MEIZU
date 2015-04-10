package com.zhihu.daily.meizu.model;

public class Editor {
	private String url;
	private String bio;
	private String name;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Editor [url=" + url + ", bio=" + bio + ", name=" + name + "]";
	}

}
