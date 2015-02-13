package com.example.finalproject;

import com.example.finalproject.ProfileActivity.PostPublic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewQuestionActivity extends Activity{
	
	String title = "";
	String question = "";
	String username = "";
	
	private TextView tvViewQuestion, tvViewTitle;
	
	private Button btnEdit, btnDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewquestion);
		
		
		tvViewTitle     = (TextView) findViewById(R.id.tvViewTitle);
		tvViewQuestion  = (TextView) findViewById(R.id.tvViewQuestion);
		
		
		btnEdit 		= (Button) findViewById(R.id.btnEdit);
		btnDelete 		= (Button) findViewById(R.id.btnDelete);
		
		
		//Get Details from Prev List View Click Activity
		question = getIntent().getExtras().getString("Question");
		title    = getIntent().getExtras().getString("Title");
		username = getIntent().getExtras().getString("Username");
		
		//Set the Title and Question
		tvViewTitle.setText(title);
		tvViewQuestion.setText(question);
		
	}
	
public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnEdit:	
					break;
				case R.id.btnDelete:
					//new editQuestion().execute();
					break;
			default:
				}
	}
	
	
	
	
	

}
