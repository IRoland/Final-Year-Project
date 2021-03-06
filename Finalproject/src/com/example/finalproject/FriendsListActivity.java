package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsListActivity extends Activity {
	
	String Username;
	
	private Toolbar toolbar;

	// JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";

    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    private static final String GetFriendsURL = "http://192.168.56.1:1234/FinalYearApp/friendsList.php";
    
    List<Friend> friendsList = new ArrayList<Friend>();
    
    private ListView lvFriendsList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendslist);
		
		
		lvFriendsList = (ListView) findViewById(R.id.lvFriendsList);
	
//		toolbar    = (Toolbar)  findViewById(R.id.toolbar);
//		setSupportActionBar(toolbar);
		
		//Run the class
		new FriendsList().execute();
				
		//Username
		Username = getIntent().getExtras().getString("username");
				
		ArrayAdapter<Friend> FriendsListAdapter = new FriendsArrayAdapter();
				
		ListView FriendsList = (ListView) findViewById(R.id.lvFriendsList);
		
		FriendsList.setAdapter(FriendsListAdapter);
				
		viewUserProfile();

	}


private void viewUserProfile() {

	
	lvFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
				long id) {
			
			Friend clickedFriend = friendsList.get(position);		

			Intent viewUserProfrile = new Intent(FriendsListActivity.this, ViewUserProfileActivity.class);
				viewUserProfrile.putExtra("firstName", clickedFriend.getFirstName());
				viewUserProfrile.putExtra("secondName", clickedFriend.getSecondName());
				viewUserProfrile.putExtra("clickedUsername", clickedFriend.getUsername());
				viewUserProfrile.putExtra("Username", Username);
				startActivity(viewUserProfrile);

		}
		
		
	});
	}



private class FriendsArrayAdapter extends ArrayAdapter<Friend>{

	
		
		public FriendsArrayAdapter(){
			super(FriendsListActivity.this, R.layout.listviewfriends, friendsList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewfriends, parent, false);
			}
			
			Friend currentFriend = friendsList.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivProfileIcon);
			icon.setImageResource(currentFriend.getIcon());
			
			TextView name = (TextView) itemView.findViewById(R.id.tvName);
			String fullName = currentFriend.getFirstName() + " " + currentFriend.getSecondName();
			name.setText(fullName);
			
			TextView status = (TextView) itemView.findViewById(R.id.tvUserStatus);
			 status.setText(currentFriend.getUsername());
			
			return itemView;
		}
	}

class FriendsList extends AsyncTask<String, String, String> {
	
	List<Friend> reterievedFriends = new ArrayList<Friend>();

protected String doInBackground(String... args) {
	
	int success;
	JSONArray results,jArray;
	String message;
	String firstNames  = "";
	String secondNames = "";
	String usernames   = "";

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Username", Username));
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		GetFriendsURL, "POST", params);

        // check your log for json response
        Log.d("Reterieving Friends", json.toString());

        //Json tags
        success = json.getInt(TAG_SUCCESS);
        message = json.getString(TAG_MESSAGE);
        results = json.getJSONArray(TAG_VALUES);
        
        Log.d("Friends Have been Retrieved!", json.toString());
        
        
        JSONObject json_data=null;
        
        for(int i=0;i<results.length();i++){
            json_data   = results.getJSONObject(i);
            firstNames  = json_data.getString("first_names");
            secondNames = json_data.getString("second_names");
            usernames   = json_data.getString("usernames");
            reterievedFriends.add(new Friend(R.drawable.ic_action_person,firstNames,secondNames,usernames));
        }

        if (success == 1) {
        	Log.d("Friends Reterieved", json.toString());
        	return json.getString(TAG_MESSAGE);
        }else{
        	Log.d("Failed!", json.getString(TAG_MESSAGE));
        	return json.getString(TAG_MESSAGE);
        	
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }

	return null;
		}

//Update Question list when response from server is received 
@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub
	super.onPostExecute(result);
	for(Friend friend: reterievedFriends)
	 friendsList.add(friend);
	 
	 
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
		         home.putExtra("Username", Username);
		         startActivity(home);
	            	return true;
	    	case R.id.action_allQuestions:
	    		Intent allquestions = new Intent(FriendsListActivity.this, AllQuestionsActivity.class);
	    		allquestions.putExtra("username", Username);
	    		startActivity(allquestions);
       			return true;
	        case R.id.action_friends:
		         Intent friendsList = new Intent(this, FriendsListActivity.class);
		         friendsList.putExtra("username", Username);
		         startActivity(friendsList);
	            	return true;
	        case R.id.action_askedQuestions:
	        	 Intent askedQuestions = new Intent(this, AskedQuestionsActivity.class);
	        	 askedQuestions.putExtra("username", Username);
	        	 startActivity(askedQuestions);
	        		return true;
	        case R.id.action_Notifications:
	        	 Intent notifications = new Intent(this, FriendRequestsActivity.class);
	        	 notifications.putExtra("username", Username);
	        	 startActivity(notifications);
	        		return true;
	        case R.id.action_Users:
	        	 Intent users = new Intent(FriendsListActivity.this, SearchForUsersActivity.class);
	        	 users.putExtra("username", Username);
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
