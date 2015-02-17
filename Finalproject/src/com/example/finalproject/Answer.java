package com.example.finalproject;

public class Answer {
	
	private int icon, answererID;
	private String answer,first_name, second_name,date;
	public Answer(int icon, int answererID, String answer, String first_name,
			String second_name, String date) {
		super();
		this.icon = icon;
		this.answer = answer;
		this.first_name = first_name;
		this.second_name = second_name;
		this.date = date;
	}
	public void setAnswererID(int answererID) {
		this.answererID = answererID;
	}
	public int getAnswererID() {
		return answererID;
	}
	public Answer(int answererID) {
		super();
		this.answererID = answererID;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getIcon() {
		return icon;
	}
	public String getAnswer() {
		return answer;
	}
	public String getFirst_name() {
		return first_name;
	}
	public String getSecond_name() {
		return second_name;
	}
	public String getDate() {
		return date;
	}

}
