package com.example.finalproject;

public class Friend {
	
	private int icon;
	private String firstName, secondName, username;
	
	public Friend(int icon, String firstName,String secondName, String username) {
		super();
		this.icon = icon;
		this.firstName = firstName;
		this.secondName = secondName;
		this.username = username;
	}

	public int getIcon() {
		return icon;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public String getUsername() {
		return username;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public void setUsername(String username) {
		this.username = username;
	}



}
