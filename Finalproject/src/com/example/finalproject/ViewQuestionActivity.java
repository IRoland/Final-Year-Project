package com.example.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewQuestionActivity extends Activity{
	
	String title = "";
	String question = "";
	String username = "";
	
	private TextView tvViewQuestion, tvViewTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewquestion);
		
		
		tvViewTitle     = (TextView) findViewById(R.id.tvViewTitle);
		tvViewQuestion  = (TextView) findViewById(R.id.tvViewQuestion);
		
		
		
		//Get Details from Prev List View Click Activity
		question = getIntent().getExtras().getString("Question");
		title    = getIntent().getExtras().getString("Title");
		username = getIntent().getExtras().getString("Username");
	
		
		//Set the Title and Question
		tvViewTitle.setText(title);
		tvViewQuestion.setText(question);
		
	}
	
	
	

}
