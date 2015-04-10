package com.zhihu.daily.meizu.model;


public class Comment {
	private String author;
	private int comment_id;
	private String content;
	private int likes;
	private int time;
	private String avatar;
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	@Override
	public String toString() {
		return "Comment [author=" + author + ", comment_id=" + comment_id
				+ ", content=" + content + ", likes=" + likes + ", time="
				+ time + ", avatar=" + avatar + "]";
	}
	
}
