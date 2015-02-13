package com.example.finalproject;

public class notificationHolder {
	
	private String firstName, secondName, username;
	private int id;
	public notificationHolder(String firstName, String secondName,
			String username, int id) {
		super();
		this.firstName = firstName;
		this.secondName = secondName;
		this.username = username;
		this.id = id;
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
	public int getId() {
		return id;
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
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	

}
