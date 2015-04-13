package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class FriendRequestsActivity extends Activity{

	
	String Username = "";
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";
    
    //Game Requests
    private static final String TAG_SUCCESS1 = "success";
    private static final String TAG_MESSAGE1 = "message";
    private static final String TAG_VALUES1 = "userlist";
    
    JSONParser jsonParser = new JSONParser();
    
    //Game Requests
    JSONParser jsonParser1 = new JSONParser();
    
    private static final String GET_NOTIFICATIONS_URL = "http://192.168.56.1:1234/FinalYearApp/getNotifications.php";
    private static final String GET_GAMEINVITES_URL = "http://192.168.56.1:1234/FinalYearApp/getGameInvites.php";
    private static final String ACCEPTFRIEND_URL = "http://192.168.56.1:1234/FinalYearApp/acceptFriendRequest.php";
    private static final String ACCEPTGAMEINVITE_URL = "http://192.168.56.1:1234/FinalYearApp/acceptGameInvite.php";
    private static final String DELETEFRIEND_URL = "http://192.168.56.1:1234/FinalYearApp/deleteFriendRequest.php";
    
    List<Friend> friendRequestsList = new ArrayList<Friend>();
    
    List<Friend> gameInvitesList = new ArrayList<Friend>();
    
    ArrayAdapter<Friend> FriendRequestsListAdapter,GameInviteListAdapter ;
    
    private ListView lvFriendRequestsList, lvGameInvitesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendrequests);
		
		lvFriendRequestsList = (ListView) findViewById(R.id.lvFriendRequestsList);
		
		lvGameInvitesList = (ListView) findViewById(R.id.lvGameInvitesList);
		
		new FriendRequestsList().execute();
		
		Username = getIntent().getExtras().getString("username");
		
		FriendRequestsListAdapter = new FriendRequestsArrayAdapter();
		
		//Gm
		GameInviteListAdapter = new GameInviteListAdapter();
		
		
		ListView FriendRequestsList = (ListView) findViewById(R.id.lvFriendRequestsList);
		
		//Gm
		ListView GameInvitesList = (ListView) findViewById(R.id.lvGameInvitesList);
		
		
		FriendRequestsList.setAdapter(FriendRequestsListAdapter);
		
		//Gm
		GameInvitesList.setAdapter(GameInviteListAdapter);
				
		viewUserProfile();
		
	}

//Not needed not working
private void viewUserProfile() {

		
		lvFriendRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
					long id) {
				
				Friend clickedFriend = friendRequestsList.get(position);		

				Intent viewUserProfrile = new Intent(FriendRequestsActivity.this, ViewUserProfileActivity.class);
					viewUserProfrile.putExtra("firstName", clickedFriend.getFirstName());
					viewUserProfrile.putExtra("secondName", clickedFriend.getSecondName());
					viewUserProfrile.putExtra("clickedUsername", clickedFriend.getUsername());
					viewUserProfrile.putExtra("Username", Username);
					startActivity(viewUserProfrile);

			}
			
			
		});
		}

private class FriendRequestsArrayAdapter extends ArrayAdapter<Friend> {

		public FriendRequestsArrayAdapter(){
			super(FriendRequestsActivity.this, R.layout.listviewfriendrequests, friendRequestsList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewfriendrequests, parent, false);
			}
			
			final Friend currentFriendRequest = friendRequestsList.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivRequestProfileIcon);
			icon.setImageResource(currentFriendRequest.getIcon());
			
			TextView name = (TextView) itemView.findViewById(R.id.tvRequestName);
			String fullName = currentFriendRequest.getFirstName() + " " + currentFriendRequest.getSecondName();
			name.setText(fullName);
			
			Button btnConfirm = (Button) itemView.findViewById(R.id.btnRequestConfirm);
			Button btnDelete  = (Button) itemView.findViewById(R.id.btnRequestDelete);
			
			btnConfirm.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					new AcceptFriendRequest().execute(currentFriendRequest.getUsername());
				}
				
			});
			
			btnDelete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					new DeleteFriendRequest().execute(currentFriendRequest.getUsername());
					
				}
				
			});
			
			
			return itemView;
		}


	}

private class GameInviteListAdapter extends ArrayAdapter<Friend> {

	public GameInviteListAdapter(){
		super(FriendRequestsActivity.this, R.layout.listviewgameinvites, gameInvitesList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View itemView = convertView;
		if(itemView == null){
			itemView = getLayoutInflater().inflate(R.layout.listviewgameinvites, parent, false);
		}
		
		final Friend currentGame = gameInvitesList.get(position);		
		
		ImageView icon = (ImageView) itemView.findViewById(R.id.ivGameInviteProfileIcon);
		icon.setImageResource(currentGame.getIcon());
		
		TextView name = (TextView) itemView.findViewById(R.id.tvGameInviteName);
		String fullName = currentGame.getFirstName() + " " + currentGame.getSecondName();
		name.setText(fullName);
		
		Button btnAccept = (Button) itemView.findViewById(R.id.btnRequestAccept);
		Button btnDecline  = (Button) itemView.findViewById(R.id.btnRequestDecline);
		
		btnAccept.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				new AcceptGameInvite().execute(currentGame.getUsername());
				
			}
			
		});
		
		btnDecline.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				new DeleteFriendRequest().execute(currentGame.getUsername());
				
			}
			
		});
		
		
		return itemView;
	}


}

class FriendRequestsList extends AsyncTask<String, String, String> {
	
	List<Friend> reterievedFriendRequests = new ArrayList<Friend>();
	
	List<Friend> reterievedGameInvites = new ArrayList<Friend>();

protected String doInBackground(String... args) {
	
	int success,success2;
	JSONArray results,results2;
	String message,message2;
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
        		GET_NOTIFICATIONS_URL, "POST", params);
        
        JSONObject gameInvite = jsonParser.makeHttpRequest(
        		GET_GAMEINVITES_URL, "POST", params);

        // check your log for json response
        Log.d("Reterieving Requests", json.toString());

        //Json tags
        success = json.getInt(TAG_SUCCESS);
        message = json.getString(TAG_MESSAGE);
        results = json.getJSONArray(TAG_VALUES);
        
        success2 = gameInvite.getInt(TAG_SUCCESS1);
        message2 = gameInvite.getString(TAG_MESSAGE1);
        results2 = gameInvite.getJSONArray(TAG_VALUES1); 
        
        Log.d("FriendRequets Have been Retrieved!", json.toString());
        
        
        JSONObject json_data=null;
        
        JSONObject json_data1=null;
        
        for(int i=0;i<results.length();i++){
            json_data   = results.getJSONObject(i);
            firstNames  = json_data.getString("first_names");
            secondNames = json_data.getString("second_names");
            usernames   = json_data.getString("notification_usernames");
            reterievedFriendRequests.add(new Friend(R.drawable.ic_action_person,firstNames,secondNames,usernames));
        }
        
        for(int i=0;i<results2.length();i++){
            json_data1   = results2.getJSONObject(i);
            firstNames  = json_data1.getString("first_names");
            secondNames = json_data1.getString("second_names");
            usernames   = json_data1.getString("gameinvite_usernames");
            reterievedGameInvites.add(new Friend(R.drawable.ic_action_person,firstNames,secondNames,usernames));
        }


        if (success == 1) {
        	Log.d("FriendRequests Reterieved", json.toString());
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
	for(Friend friend: reterievedFriendRequests)
		friendRequestsList.add(friend);
	 
	for(Friend friend: reterievedGameInvites)
		gameInvitesList.add(friend);
	 
			}

	}

class AcceptFriendRequest extends AsyncTask<String, String, String> {

protected String doInBackground(String... args) {
	

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", Username));
        params.add(new BasicNameValuePair("friendusername", args[0]));
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		ACCEPTFRIEND_URL, "POST", params);

      
        return json.getString(TAG_MESSAGE);
   
        
    } catch (JSONException e) {
        e.printStackTrace();
    }

	return null;
		}

@Override
protected void onPostExecute(String result) {
	super.onPostExecute(result);
	friendRequestsList.clear();
	new FriendRequestsList().execute();
	FriendRequestsListAdapter.notifyDataSetChanged(); 

}


	}

class AcceptGameInvite extends AsyncTask<String, String, String> {

protected String doInBackground(String... args) {
	

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", Username));
        params.add(new BasicNameValuePair("challengerusername", args[0]));
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		ACCEPTGAMEINVITE_URL, "POST", params);
        
   
       
       Intent AcceptQuiz = new Intent(FriendRequestsActivity.this, QuizActivity.class);
       AcceptQuiz.putExtra("username", Username);
       AcceptQuiz.putExtra("challengerusername", args[0]);
       startActivity(AcceptQuiz);
       finish();

      
        return json.getString(TAG_MESSAGE);
   
        
    } catch (JSONException e) {
        e.printStackTrace();
    }

	return null;
		}


	}

class DeleteFriendRequest extends AsyncTask<String, String, String> {

protected String doInBackground(String... args) {
	

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", Username));
        params.add(new BasicNameValuePair("friendusername", args[0]));
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		DELETEFRIEND_URL, "POST", params);

      
        return json.getString(TAG_MESSAGE);
   
        
    } catch (JSONException e) {
        e.printStackTrace();
    }

	return null;
		}

@Override
protected void onPostExecute(String result) {
	super.onPostExecute(result);
	friendRequestsList.clear();
	new FriendRequestsList().execute();
	FriendRequestsListAdapter.notifyDataSetChanged(); 
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
	    		Intent allquestions = new Intent(FriendRequestsActivity.this, AllQuestionsActivity.class);
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
	        	 Intent users = new Intent(FriendRequestsActivity.this, SearchForUsersActivity.class);
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
