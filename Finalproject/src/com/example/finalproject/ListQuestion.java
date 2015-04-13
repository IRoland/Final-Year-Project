package com.example.finalproject;

public class ListQuestion {
	
	private int icon;
	private String title, description, fname, sname, username;
	public ListQuestion(int icon, String title, String description,
			String fname, String sname, String username) {
		super();
		this.icon = icon;
		this.title = title;
		this.description = description;
		this.fname = fname;
		this.sname = sname;
		this.username = username;
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
	public void setFname(String fname) {
		this.fname = fname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getFname() {
		return fname;
	}
	public String getSname() {
		return sname;
	}
	public String getUsername() {
		return username;
	}

	

}
