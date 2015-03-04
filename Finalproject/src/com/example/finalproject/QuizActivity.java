package com.example.finalproject;

import java.net.URISyntaxException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizActivity extends Activity{

	
	String Username,ChallengerUsername;
	
	private TextView Player1Name,Player1Score,Player2Name,Player2Score;
	
	private ImageView Player1Pic,Player2Pic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		
		
		
		Player1Name  = (TextView) findViewById(R.id.tvPlayer1Name);
		Player1Score = (TextView) findViewById(R.id.tvPlayer1Score);
		
		Player2Name  = (TextView) findViewById(R.id.tvPlayer2Name);
		Player2Score = (TextView) findViewById(R.id.tvPlayer2Score);
		
		Player1Pic   = (ImageView) findViewById(R.id.ivPlayer1);
		Player2Pic   = (ImageView) findViewById(R.id.ivPlayer2);
		

		Username = getIntent().getExtras().getString("username");	
		ChallengerUsername = getIntent().getExtras().getString("challengerusername");		

		
		Player1Name.setText(Username);
		Player2Name.setText(ChallengerUsername);
		Player1Score.setText("0");
		Player2Score.setText("0");
		
	}
	
	

}
