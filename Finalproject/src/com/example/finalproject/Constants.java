package com.example.finalproject;

public class Constants {
	
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String LOGIN_INFO = "LoginDetails";
	
	private String username;

	public Constants(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	
}
