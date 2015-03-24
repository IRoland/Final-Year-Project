package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.ProfileActivity.PostPublic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewQuestionActivity extends Activity{
	
	String title = "";
	String question = "";
	String username = "";
	
	String editQuestion,editTitle;
	
	private TextView tvViewQuestion, tvViewTitle;
	
	private Button btnEdit, btnDelete;
	
	private EditText etEditTitle,etEditQuestion;
	
	JSONParser jsonParser = new JSONParser();
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_VALUES  = "userlist";
	
	private static final String EDITQUESTIONURL = "http://192.168.56.1:1234/FinalYearApp/editQuestion.php";
	private static final String DELETEQUESTIONURL = "http://192.168.56.1:1234/FinalYearApp/deleteQuestion.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewquestion);
		
		
		tvViewTitle     = (TextView) findViewById(R.id.tvViewTitle);
		tvViewQuestion  = (TextView) findViewById(R.id.tvViewQuestion);
		
		
		btnEdit 		= (Button) findViewById(R.id.btnEdit);
		btnDelete 		= (Button) findViewById(R.id.btnDelete);
		
		etEditTitle		= (EditText) findViewById(R.id.etEditTitle);
		etEditQuestion	= (EditText) findViewById(R.id.etEditQuestion);
		
		
		//Get Details from Prev List View Click Activity
		question = getIntent().getExtras().getString("Question");
		title    = getIntent().getExtras().getString("Title");
		username = getIntent().getExtras().getString("Username");
		
		//Set the Title and Question
		tvViewTitle.setText(title);
		tvViewQuestion.setText(question);
		
		etEditTitle.setVisibility(View.INVISIBLE);
		etEditQuestion.setVisibility(View.INVISIBLE);
		
		btnEdit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if(btnEdit.getText() == "Save"){
					new editQuestion().execute();

				}else{
					showEditField();
					btnEdit.setText("Save");
					btnDelete.setText("Cancel");
				}
			}
			
		});
		
		btnDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if(btnDelete.getText() == "Cancel"){
					etEditTitle.setVisibility(View.INVISIBLE);
					etEditQuestion.setVisibility(View.INVISIBLE);
					btnEdit.setText("Edit");
					btnDelete.setText("Delete");
				}else{
					new deleteQuestion().execute();
				}
				
			}
			
		});
		
	}

	public void showEditField(){
	etEditTitle.setVisibility(View.VISIBLE);
	etEditQuestion.setVisibility(View.VISIBLE);
	
	etEditTitle.setText(title);
	etEditQuestion.setText(question);
	
}

class editQuestion extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... args) {
		
		 // Check for success tag
        int success;
        editTitle    = etEditTitle.getText().toString();
        editQuestion = etEditQuestion.getText().toString();
       
        
        String error = isValidQuestionAndTitle();
        
        if(!error.isEmpty()){
        	return error;
        }
     
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("EditQuestion", editQuestion));
            params.add(new BasicNameValuePair("EditTitle", editTitle));
            params.add(new BasicNameValuePair("AskedQuestion", question));
            params.add(new BasicNameValuePair("AskedTitle", title));
            params.add(new BasicNameValuePair("Username", username));

            
            // getting product details by making HTTP requests
            JSONObject json = jsonParser.makeHttpRequest(
                   EDITQUESTIONURL, "POST", params);
            
          
            // json success tag
            success = json.getInt(TAG_SUCCESS);
            
            if (success == 1) {
            	  Intent askedQuestions = new Intent(ViewQuestionActivity.this, AskedQuestionsActivity.class);
        		  askedQuestions.putExtra("username", username);
        		  startActivity(askedQuestions);
        		  finish();
            	return json.getString(TAG_MESSAGE);
            }else{
            	
            	
            	return json.getString(TAG_MESSAGE);
            	
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      

        return null;
		
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		btnEdit.setText("Edit");
		btnDelete.setText("Delete");
		etEditTitle.setVisibility(View.INVISIBLE);
		etEditQuestion.setVisibility(View.INVISIBLE);
		
		  if (result != null){
          	Toast.makeText(ViewQuestionActivity.this, result, Toast.LENGTH_LONG).show();
          }
		
		
		
	}

	//Check if Title and Question are not empty 
	private String isValidQuestionAndTitle() {
		if(!isStringValid(editTitle)){
			return "Please Enter a Title";
		}
		else if(!isStringValid(editQuestion)){
			return "Please Enter a Question";
		}
		
		return "";
	}
	
	private boolean isStringValid(String string) {
		if(string.isEmpty()){
			return false;
		}
		return true;
	}

}

class deleteQuestion extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... args) {
		
		 // Check for success tag
        int success;

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("AskedQuestion", question));
            params.add(new BasicNameValuePair("AskedTitle", title));
            params.add(new BasicNameValuePair("Username", username));

            
            // getting product details by making HTTP requests
            JSONObject json = jsonParser.makeHttpRequest(
                   DELETEQUESTIONURL, "POST", params);
            
          
            // json success tag
            success = json.getInt(TAG_SUCCESS);
            
            if (success == 1) {
            	 Intent askedQuestions = new Intent(ViewQuestionActivity.this, AskedQuestionsActivity.class);
            	 askedQuestions.putExtra("username", username);
            	 startActivity(askedQuestions);
            	 finish();
            	return json.getString(TAG_MESSAGE);
            }else{
            	
            	return json.getString(TAG_MESSAGE);
            	
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
      

        return null;
		
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		btnEdit.setText("Edit");
		btnDelete.setText("Delete");
		etEditTitle.setVisibility(View.INVISIBLE);
		etEditQuestion.setVisibility(View.INVISIBLE);

	}

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
	        	 Intent users = new Intent(ViewQuestionActivity.this, SearchForUsersActivity.class);
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
