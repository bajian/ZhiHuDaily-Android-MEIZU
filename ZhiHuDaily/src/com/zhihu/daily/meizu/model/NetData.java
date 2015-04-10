package com.zhihu.daily.meizu.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

@Table(name = "NetData")
public class NetData extends Model {
	@Column(name = "path")
	private String path;
	@Column(name = "json")
	private String json;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public NetData() {
		super();
	}

	public NetData(String path, String json) {
		super();
		this.path = path;
		this.json = json;
	}

	public void store() {
		if (new Select().from(NetData.class).where("path = ?", this.path)
				.executeSingle() == null) {
			this.save();
		} else {
			new Update(NetData.class).set("json = ?", this.json)
					.where("path = ?", this.path).execute();
		}
	}
}
