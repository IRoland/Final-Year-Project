package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUserProfileActivity extends Activity implements OnClickListener{
	
	String firstName, secondName, clickedUsername, currentUsername, friendButtonText;

	private TextView tvUserFirstName, tvUserSecondName;
	
	private ImageView ivUserProfilePic;
	
	RoundImage roundedImage;
	
	Button btnAdd,btnChat,btnChallenge,btnBlock;
	
	List<Question> userAskedQuestions = new ArrayList<Question>();
	
	private ListView lvUserAskedQuestion;
	
	JSONParser jsonParser = new JSONParser();
	
	 private static final String TAG_SUCCESS = "success";
	 private static final String TAG_MESSAGE = "message";
	 private static final String TAG_VALUES  = "userlist";
	
	private static final String addFriendURl = "http://192.168.56.1:1234/FinalYearApp/AddFriend.php";
	private static final String GetQuestionsURL = "http://192.168.56.1:1234/FinalYearApp/askedQuestions.php";
	
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
		
		lvUserAskedQuestion = (ListView) findViewById(R.id.lvUserAskedQuestion);
		
		//Run the class
		new userAskedQuestions().execute();
		
		ArrayAdapter<Question> userAskedQuestionsAdapter = new QuestionArrayAdapter();
		
		ListView userAskedQuestionsList = (ListView) findViewById(R.id.lvUserAskedQuestion);
		
		userAskedQuestionsList.setAdapter(userAskedQuestionsAdapter);
		
		//viewClickedQuestion();
		
		//Round Profile Pic
		Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic_icon);
        roundedImage = new RoundImage(bm);
		ivUserProfilePic.setImageDrawable(roundedImage);
	}
	

private class QuestionArrayAdapter extends ArrayAdapter<Question>{
		
		public QuestionArrayAdapter(){
			super(ViewUserProfileActivity.this, R.layout.listviewitem, userAskedQuestions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewitem, parent, false);
			}
			
			Question currentAskedQuestion = userAskedQuestions.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivQuestionIcon);
			icon.setImageResource(currentAskedQuestion.getIcon());
			
			TextView title = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
			title.setText(currentAskedQuestion.getTitle());
			
			TextView description = (TextView) itemView.findViewById(R.id.tvQuestionDescription);
			description.setText(currentAskedQuestion.getDescription());
			
			return itemView;
		}
	}

	
public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddUser:
			 friendButtonText = btnAdd.getText().toString();
			 if(friendButtonText.equals("Add")){
				 new AddFriend().execute();
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


class AddFriend extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... args) {
		
		 // Check for success tag
        int success;
        
        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", currentUsername));
            params.add(new BasicNameValuePair("friendusername", clickedUsername));
                        
            Log.d("request!", "starting");
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(
            		addFriendURl, "POST", params);

            // json success tag
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
            	Log.d("Friend Added!", json.toString());
            
            	return json.getString(TAG_MESSAGE);
            }else{
            	Log.d("Failed", json.getString(TAG_MESSAGE));
            	return json.getString(TAG_MESSAGE);
            	
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return null;
	}
	
}
	

class userAskedQuestions extends AsyncTask<String, String, String> {
	
	List<Question> reterievedQuestions = new ArrayList<Question>();

protected String doInBackground(String... args) {
	
	int success;
	JSONArray results,jArray;
	String message;
	String qTitle = "";
	String qDescription = "";

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Username", clickedUsername));
        
        Log.d("request!", "starting");
        // getting product details by making HTTP requests
        JSONObject json = jsonParser.makeHttpRequest(
        		GetQuestionsURL, "POST", params);

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
            qDescription=json_data.getString("descriptions");
            reterievedQuestions.add(new Question(R.drawable.english_icon,qTitle,qDescription));
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
	for(Question question: reterievedQuestions)
		userAskedQuestions.add(question);
	 
	 
				}
			}





	}