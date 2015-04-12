package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.AnswerQuestionActivity.PostAnswer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class SearchForUsersActivity extends Activity implements OnQueryTextListener{
	
	String Username;

	private SearchView mSearchView;
	// JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";

    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    private static final String GetUsersURL = "http://192.168.56.1:1234/FinalYearApp/UsersList.php";
    
    List<Friend> UsersList = new ArrayList<Friend>();
    
    private ListView lvUsersList;
    
    private SearchView svUsers;
    
    ArrayAdapter<Friend> UsersListAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_searchusers);
		
		
		lvUsersList = (ListView) findViewById(R.id.lvUsersList);
		
		svUsers		 = (SearchView) findViewById(R.id.sVUsers);
	
		//Run the class
		//new UsersList().execute();
				
		//Username
		Username = getIntent().getExtras().getString("username");
				
		UsersListAdapter = new UsersListAdapter();
				
		ListView UsersList = (ListView) findViewById(R.id.lvUsersList);
		
		UsersList.setAdapter(UsersListAdapter);
				
		viewUserProfile();
		
		svUsers.setOnQueryTextListener(this);
		
	}


public boolean onQueryTextSubmit(String query){
	new UsersList().execute(query);
	return false;
}
private void viewUserProfile() {

	
	lvUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
			
			Friend clickedFriend = UsersList.get(position);		

			Intent viewUserProfrile = new Intent(SearchForUsersActivity.this, ViewUserProfileActivity.class);
			viewUserProfrile.putExtra("firstName", clickedFriend.getFirstName());
			viewUserProfrile.putExtra("secondName", clickedFriend.getSecondName());
			viewUserProfrile.putExtra("clickedUsername", clickedFriend.getUsername());
			viewUserProfrile.putExtra("Username", Username);
			startActivity(viewUserProfrile);
						
		}

		
	});
	}

private class UsersListAdapter extends ArrayAdapter<Friend>{

		public UsersListAdapter(){
			super(SearchForUsersActivity.this, R.layout.listviewfriends, UsersList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewfriends, parent, false);
			}
			
			Friend currentUser = UsersList.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivProfileIcon);
			icon.setImageResource(currentUser.getIcon());
			
			TextView name = (TextView) itemView.findViewById(R.id.tvName);
			String fullName = currentUser.getFirstName() + " " + currentUser.getSecondName();
			name.setText(fullName);
			
			TextView status = (TextView) itemView.findViewById(R.id.tvUserStatus);
			 status.setText(currentUser.getUsername());
			
			return itemView;
		}
	}

class UsersList extends AsyncTask<String, String, String> {
	
	List<Friend> reterievedUsers = new ArrayList<Friend>();

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
        params.add(new BasicNameValuePair("Username", args[0]));
     
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		GetUsersURL, "POST", params);

        // check your log for json response
        Log.d("Reterieving Users", json.toString());

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
            reterievedUsers.add(new Friend(R.drawable.ic_action_person,firstNames,secondNames,usernames));
        }

        if (success == 1) {

        	return json.getString(TAG_MESSAGE);
        }else{

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
	UsersList.clear();
	if(result == null){
		Toast.makeText(SearchForUsersActivity.this, "Sorry User not found", Toast.LENGTH_SHORT).show();
		UsersListAdapter.notifyDataSetChanged();
	}else{
		for(Friend friend: reterievedUsers)
			UsersList.add(friend);
		UsersListAdapter.notifyDataSetChanged();
	}
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
	        	 Intent users = new Intent(SearchForUsersActivity.this, SearchForUsersActivity.class);
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

@Override
public boolean onQueryTextChange(String newText) {

	return false;
}

}
