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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class AllQuestionsActivity extends Activity{
	
	String Username;
	
	 // JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";
    
  //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //
    private static final String GetAllQuestionsURL = "http://192.168.56.1:1234/FinalYearApp/allQuestions.php";
	
	List<ListQuestion> askedQuestions = new ArrayList<ListQuestion>();

	private ListView lvQuestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allquestions);
		
		lvQuestions = (ListView) findViewById(R.id.lvQuestions);

		//Username
		Username = getIntent().getExtras().getString("username");
		
		ArrayAdapter<ListQuestion> askedQuestionsAdapter = new QuestionArrayAdapter();
		
		ListView askedQuestionsList = (ListView) findViewById(R.id.lvQuestions);
		
		askedQuestionsList.setAdapter(askedQuestionsAdapter);
		
		viewClickedQuestion();

	}
	

private void viewClickedQuestion() {
		
		
		lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
					long id) {
				
				ListQuestion clickedQuestion = askedQuestions.get(position);		
				
				
				Intent answerQuestion = new Intent(AllQuestionsActivity.this, AnswerQuestionActivity.class);
				answerQuestion.putExtra("Title", clickedQuestion.getTitle());
				answerQuestion.putExtra("Question", clickedQuestion.getDescription());
				answerQuestion.putExtra("questionAskedByUsername", clickedQuestion.getUsername());
				answerQuestion.putExtra("firstName", clickedQuestion.getFname());
				answerQuestion.putExtra("secondName", clickedQuestion.getSname());
				answerQuestion.putExtra("myUsername", Username);
 				startActivity(answerQuestion);
 				
			}
			
			
		});
		
	}

private class QuestionArrayAdapter extends ArrayAdapter<ListQuestion>{
		
		public QuestionArrayAdapter(){
			super(AllQuestionsActivity.this, R.layout.listviewitem, askedQuestions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewitem, parent, false);
			}
			
			ListQuestion currentAskedQuestion = askedQuestions.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivQuestionIcon);
			icon.setImageResource(currentAskedQuestion.getIcon());
			
			TextView title = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
			title.setText(currentAskedQuestion.getTitle());
			
			TextView description = (TextView) itemView.findViewById(R.id.tvQuestionDescription);
			description.setText(currentAskedQuestion.getDescription());
			
			return itemView;
		}
	}

public void onResume(){
	super.onResume();
	askedQuestions.clear();
	new AskedQuestions().execute();
}

class AskedQuestions extends AsyncTask<String, String, String> {
			
			List<ListQuestion> reterievedQuestions = new ArrayList<ListQuestion>();
		
		protected String doInBackground(String... args) {
			
			int success;
			JSONArray results,jArray;
			String message;
			String qTitle = "";
			String qDescription = "";
			String fname = "";
			String sname = "";
			String askedby = "";
			int qCategoryIconId;
			int qCategory = 0;

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Username", Username));
                
                Log.d("request!", "starting");
                // getting product details by making HTTP requests
                JSONObject json = jsonParser.makeHttpRequest(
                		GetAllQuestionsURL, "POST", params);
 
                // check your log for json response
                Log.d("Reterieving Questions", json.toString());
 
                //Json tags
                success = json.getInt(TAG_SUCCESS);
                message = json.getString(TAG_MESSAGE);
                results = json.getJSONArray(TAG_VALUES);
                
                Log.d("Questions Have been Retrieved!", json.toString());
                
                
                JSONObject json_data=null;
               
                for(int i=0;i<results.length();i++){
                    json_data = results.getJSONObject(i);
                    qTitle=json_data.getString("titles");
                    fname=json_data.getString("fNames");
                    sname=json_data.getString("sNames");
                    askedby=json_data.getString("usernames");
                    qDescription=json_data.getString("descriptions");
                    qCategory=json_data.getInt("category");
                    qCategoryIconId = Utils.getDrawableFor(qCategory);
                    reterievedQuestions.add(new ListQuestion(qCategoryIconId,qTitle,qDescription,fname,sname,askedby));
                }
                
                

                if (success == 1) {
                	Log.d("Questions Reterieved", json.toString());
                
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
			for(ListQuestion question: reterievedQuestions)
			 askedQuestions.add(question);
			 
			 
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
	        	 Intent users = new Intent(AllQuestionsActivity.this, SearchForUsersActivity.class);
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