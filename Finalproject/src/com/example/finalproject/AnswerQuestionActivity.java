package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.finalproject.FriendRequestsActivity.AcceptFriendRequest;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AnswerQuestionActivity extends Activity{
	
	List<Answer> questionAnswers = new ArrayList<Answer>();

	private ListView lvAnswerQuestions;

	private String Username, questionAskedByUsername, qtitle, questionDesc, fName, sName;
	
	private String askedQuestion, namedTitle , answer;
	
	private Button btnSubmitAnswer;
	
	private EditText etEnterAnswer;
	
	private TextView tvQuestionTitleSection, tvQuestionSection,tvUserName,tvDateAsked;
	
	private ArrayAdapter<Answer> QuestionAnswersAdapter ;  
	
	
	 // JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";
    
  //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    private static final String GetAnswersURL = "http://192.168.56.1:1234/FinalYearApp/getAnswers.php";
    private static final String AddAnswersURL = "http://192.168.56.1:1234/FinalYearApp/addAnswer.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answerquestionactivity);
		
		lvAnswerQuestions = (ListView) findViewById(R.id.lvAnswers);
		
		tvQuestionTitleSection = (TextView) findViewById(R.id.tvQuestionTitleSection);
		tvQuestionSection      = (TextView) findViewById(R.id.tvQuestionSection);
		tvUserName             = (TextView) findViewById(R.id.tvUserName);
		tvDateAsked            = (TextView) findViewById(R.id.tvDateAsked);
		
		
		etEnterAnswer		   = (EditText) findViewById(R.id.etEnterAnswer);
		
		btnSubmitAnswer		   =  (Button) findViewById(R.id.btnSubmitAnswer);
		
		btnSubmitAnswer	.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				new PostAnswer().execute();

			}
			
		});
		
		//My username
		Username = getIntent().getExtras().getString("myUsername");
		
		//There Details
		qtitle 				     = getIntent().getExtras().getString("Title");
		questionDesc			 = getIntent().getExtras().getString("Question");
		fName					 = getIntent().getExtras().getString("firstName");
		sName					 = getIntent().getExtras().getString("secondName");
		questionAskedByUsername  = getIntent().getExtras().getString("questionAskedByUsername");
		
		
		tvQuestionTitleSection.setText(qtitle);
		tvQuestionSection     .setText(questionDesc);
		tvUserName            .setText(fName + " " + sName);
		tvDateAsked           .setText("2015");
		
		
		new GetAnswers().execute();
		
		QuestionAnswersAdapter = new AnswerArrayAdapter();
		
		ListView questionAnswersList = (ListView) findViewById(R.id.lvAnswers);
		
		questionAnswersList.setAdapter(QuestionAnswersAdapter);
		
		//viewClickedQuestion();
	}
	
	
private class AnswerArrayAdapter extends ArrayAdapter<Answer>{
		
		public AnswerArrayAdapter(){
			super(AnswerQuestionActivity.this, R.layout.listviewanswer, questionAnswers);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewanswer, parent, false);
			}
			
			Answer currentQuestionAnswer = questionAnswers.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivAnswerIcon);
			icon.setImageResource(currentQuestionAnswer.getIcon());
			
			TextView answer = (TextView) itemView.findViewById(R.id.tvAnswer);
			answer.setText(currentQuestionAnswer.getAnswer());
			
			TextView name = (TextView) itemView.findViewById(R.id.tvAnswererName);
			name.setText(currentQuestionAnswer.getFirst_name() +" "+ currentQuestionAnswer.getSecond_name());
			
			TextView date = (TextView) itemView.findViewById(R.id.tvAnswerDate);
			date.setText(currentQuestionAnswer.getDate());
			
			return itemView;
		}
	}

class GetAnswers extends AsyncTask<String, String, String> {

	
	List<Answer> reterievedAnswers = new ArrayList<Answer>();
    

protected String doInBackground(String... args) {
	
	int success, answererid;
	JSONArray results,jArray;
	String message, firstNames, secondNames, dates, answers;
	
	askedQuestion = tvQuestionSection.getText().toString();
    namedTitle    = tvQuestionTitleSection.getText().toString();

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("QuestionByUsername", questionAskedByUsername));
        params.add(new BasicNameValuePair("AskedQuestion", askedQuestion));
        params.add(new BasicNameValuePair("Title", namedTitle));
 
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		GetAnswersURL, "POST", params);

        // check your log for json response
        Log.d("Reterieving Questions", json.toString());

        //Json tags
        success = json.getInt(TAG_SUCCESS);
        message = json.getString(TAG_MESSAGE);
        results = json.getJSONArray(TAG_VALUES);
        
        Log.d("Answers Have been Retrieved!", json.toString());
        
        
        JSONObject json_data=null;
       
        for(int i=0;i<results.length();i++){
            json_data = results.getJSONObject(i);
            answers=json_data.getString("answersFrom");
            firstNames=json_data.getString("first_name");
            secondNames=json_data.getString("second_name");
            dates=json_data.getString("date");
            answererid=json_data.getInt("answereriD");
            reterievedAnswers.add(new Answer(R.drawable.profilepic_icon,answererid,answers,firstNames,secondNames,dates));
        }
        
        

        if (success == 1) {
        	Log.d("Answers Reterieved", json.toString());
        
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
	super.onPostExecute(result);
	for(Answer answer: reterievedAnswers){
		questionAnswers.add(answer);
	}
	QuestionAnswersAdapter.notifyDataSetChanged(); 
	
			}
		
		}

class PostAnswer extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... args) {
		
		 // Check for success tag
        int success;
        askedQuestion = tvQuestionSection.getText().toString();
        namedTitle    = tvQuestionTitleSection.getText().toString();
        answer        = etEnterAnswer.getText().toString();

                  
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("AskedQuestion", askedQuestion));
            params.add(new BasicNameValuePair("Title", namedTitle));
            params.add(new BasicNameValuePair("Username", Username));
            params.add(new BasicNameValuePair("QuestionUsername", questionAskedByUsername));
            params.add(new BasicNameValuePair("Answer", answer));
            
            Log.d("request!", "starting");
            // getting product details by making HTTP requests
            JSONObject json = jsonParser.makeHttpRequest(
            		AddAnswersURL, "POST", params);

            // check your log for json response
            Log.d("Saving Answer", json.toString());

            // json success tag
            success = json.getInt(TAG_SUCCESS);
            
            if (success == 1) {
            	Log.d("Answer Posted!", json.toString());
            	//Notify Adapter Through Method OnPostExecute
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

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		questionAnswers.clear();
		etEnterAnswer.setText(null);
		new GetAnswers().execute();
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
	        case R.id.action_settings:
	        	 Intent settings = new Intent(this, SettingsActivity.class);
	        	 startActivity(settings);
	        		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}


}
