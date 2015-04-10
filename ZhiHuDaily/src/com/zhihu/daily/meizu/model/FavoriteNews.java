package com.zhihu.daily.meizu.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "FavoriteNews")
public class FavoriteNews extends Model {

	@Column(name = "news_id", onUniqueConflict = Column.ConflictAction.IGNORE, unique = true)
	private int newsId;

	@Column(name = "image")
	private String image;

	@Column(name = "title")
	private String title;

	@Column(name = "url")
	private String url;
	
	@Column(name = "type")
	private int type;

	public FavoriteNews() {
	}

	public FavoriteNews(SimpleNews paramStory) {
		this.newsId = paramStory.getId();
		this.title = paramStory.getTitle();
		this.url = paramStory.getShare_url();
		if (paramStory.getImages() != null)
			this.image = paramStory.getImages().get(0);
	}

	public static List<FavoriteNews> getAll() {
		return new Select().from(FavoriteNews.class).orderBy(" ID DESC ")
				.execute();
	}
	
	public boolean store() {
		if (new Select().from(FavoriteNews.class).where("news_id = ?", this.newsId)
				.executeSingle() != null) {
			new Delete().from(FavoriteNews.class).where("news_id = ?", this.newsId).execute();
			return false;
		}else{
			this.save();
			return true;
		}
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}