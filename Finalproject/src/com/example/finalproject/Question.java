package com.example.finalproject;

public class Question {
	
	private int icon;
	private String title, description;
	public Question(int icon, String title, String description) {
		super();
		this.icon = icon;
		this.title = title;
		this.description = description;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIcon() {
		return icon;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	

}
