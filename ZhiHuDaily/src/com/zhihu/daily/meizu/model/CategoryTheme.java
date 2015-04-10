package com.zhihu.daily.meizu.model;

public class CategoryTheme {
	private int color;
	private String image;
	private int id;
	private String name;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CategoryTheme [color=" + color + ", image=" + image + ", id="
				+ id + ", name=" + name + "]";
	}

}
