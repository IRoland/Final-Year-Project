package com.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private EditText etChangeFirstName, etChangeSecondName, etChangeEmail, etChangePassword;
	
	private Button btnSave, btnLogout;
	
	private ImageView ivChangeProfilePic;
	
	String username;

	SharedPreferences settings;
	
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    
    
    etChangeFirstName  = (EditText) findViewById(R.id.etChangeFirstName);
    etChangeSecondName = (EditText) findViewById(R.id.etChangeSecondName);
    etChangeEmail      = (EditText) findViewById(R.id.etChangeEmail);
    etChangePassword   = (EditText) findViewById(R.id.etChangePassword);
    
    btnLogout          = (Button) findViewById(R.id.btnLogout);
    btnSave			   = (Button) findViewById(R.id.btnSave);
    
    settings = getSharedPreferences(Constants.LOGIN_INFO,0);

	username = settings.getString("username", null);
	
	btnLogout.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
		Logout();
		}
		
	});
	
	btnSave.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
		//new SaveDetails().execute();
		}
		
	});
    
  }
    
public void Logout(){
	SharedPreferences settings = getSharedPreferences(Constants.LOGIN_INFO,0);
    SharedPreferences.Editor editor = settings.edit();
	editor.remove("hasLoggedIn");
	editor.commit();
	
	Intent login = new Intent(this, LoginActivity.class);	
	startActivity(login);
	finish();
	
	}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
			
	}
	
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    case R.id.action_home:
		         Intent home = new Intent(this, ProfileActivity.class);
		         home.putExtra("Username", username);
		         startActivity(home);
	            	return true;
	        case R.id.action_friends:
		         Intent friendsList = new Intent(this, FriendsListActivity.class);
		         friendsList.putExtra("username", username);
		         startActivity(friendsList);
	            	return true;
	        case R.id.action_askedQuestions:
	        	 Intent askedQuestions = new Intent(this, AskedQuestionsActivity.class);
	        	 askedQuestions.putExtra("username", username);
	        	 startActivity(askedQuestions);
	        		return true;
	        case R.id.action_Notifications:
	        	 Intent notifications = new Intent(this, FriendRequestsActivity.class);
	        	 notifications.putExtra("username", username);
	        	 startActivity(notifications);
	        		return true;
	        case R.id.action_Users:
	        	 Intent users = new Intent(SettingsActivity.this, SearchForUsersActivity.class);
	        	 users.putExtra("username", username);
	        	 startActivity(users);
	        		return true;
	        case R.id.action_settings:
	        	 Intent settings = new Intent(this, SettingsActivity.class);
	        	 startActivity(settings);
	        		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
 
  
} 