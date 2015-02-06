package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.LoginActivity.AttemptLogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUserProfileActivity extends Activity implements OnClickListener{
	
	String firstName, secondName, clickedUsername, currentUsername, friendButtonText;

	private TextView tvUserFirstName, tvUserSecondName;
	
	private ImageView ivUserProfilePic;
	
	RoundImage roundedImage;
	
	Button btnAdd,btnChat,btnChallenge,btnBlock;
	
	JSONParser jsonParser = new JSONParser();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userprofile);
		
		tvUserFirstName    = (TextView) findViewById(R.id.tvUserFirstName);
		tvUserSecondName   = (TextView) findViewById(R.id.tvUserSecondName);
		
		ivUserProfilePic   = (ImageView) findViewById(R.id.ivUserProfilePic);
		
		btnAdd 			   = (Button) findViewById(R.id.btnAddUser);
		btnChat		 	   = (Button) findViewById(R.id.btnChat);
		btnChallenge	   = (Button) findViewById(R.id.btnChallenge);
		btnBlock		   = (Button) findViewById(R.id.btnBlockUser);
		
		//Get Details from Prev List View Click Activity
		firstName    	   = getIntent().getExtras().getString("firstName");
		secondName    	   = getIntent().getExtras().getString("secondName");
		clickedUsername    = getIntent().getExtras().getString("clickedUsername");
		currentUsername    = getIntent().getExtras().getString("Username");
		
		
		//Set the Title and Question
		tvUserFirstName.setText(firstName);
		tvUserSecondName.setText(secondName);
		
		//register listeners
		btnAdd.setOnClickListener(this);
		btnChat.setOnClickListener(this);
		btnChallenge.setOnClickListener(this);
		btnBlock.setOnClickListener(this);
		
		//Round Profile Pic
		Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic_icon);
        roundedImage = new RoundImage(bm);
		ivUserProfilePic.setImageDrawable(roundedImage);
	}
	
public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddUser:
			 friendButtonText = btnAdd.getText().toString();
			 if(friendButtonText.equals("Add")){
				//new AddFriend().execute();
				 btnAdd.setText("Unfriend");
			 }
			 else{
				//new UnFriend().execute();
				 btnAdd.setText("Add");
			 }
			break;
		case R.id.btnBlockUser:
			//	new BlockUser().execute();
			break;
		case R.id.btnChallenge:
			//	new ChallengeUser().execute();
			break;
		case R.id.btnChat:
			//	new ChatWith().execute();
		break;
	default:
		}
}




}
