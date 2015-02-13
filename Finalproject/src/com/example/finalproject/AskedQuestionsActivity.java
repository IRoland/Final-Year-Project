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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class AskedQuestionsActivity extends Activity{
	
	String Username;
	
	 // JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";
    
  //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //
    private static final String GetQuestionsURL = "http://192.168.56.1:1234/FinalYearApp/askedQuestions.php";
	
	List<Question> askedQuestions = new ArrayList<Question>();

	private ListView lvAskedQuestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_askedquestions);
		
		
		lvAskedQuestions = (ListView) findViewById(R.id.lvAskedQuestion);
		
		//Run the class
		new AskedQuestions().execute();
		
		//Username
		Username = getIntent().getExtras().getString("username");
		
		ArrayAdapter<Question> askedQuestionsAdapter = new QuestionArrayAdapter();
		
		ListView askedQuestionsList = (ListView) findViewById(R.id.lvAskedQuestion);
		
		askedQuestionsList.setAdapter(askedQuestionsAdapter);
		
		viewClickedQuestion();

		
		
	}
	

private void viewClickedQuestion() {
		
		
		lvAskedQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position,
					long id) {
				
				Question clickedQuestion = askedQuestions.get(position);		

				Intent viewQuestion = new Intent(AskedQuestionsActivity.this, ViewQuestionActivity.class);
 				viewQuestion.putExtra("Title", clickedQuestion.getTitle());
 				viewQuestion.putExtra("Question", clickedQuestion.getDescription());
 				viewQuestion.putExtra("Username", Username);
 				startActivity(viewQuestion);
			}
			
			
		});
		
	}

private class QuestionArrayAdapter extends ArrayAdapter<Question>{
		
		public QuestionArrayAdapter(){
			super(AskedQuestionsActivity.this, R.layout.listviewitem, askedQuestions);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = getLayoutInflater().inflate(R.layout.listviewitem, parent, false);
			}
			
			Question currentAskedQuestion = askedQuestions.get(position);		
			
			ImageView icon = (ImageView) itemView.findViewById(R.id.ivQuestionIcon);
			icon.setImageResource(currentAskedQuestion.getIcon());
			
			TextView title = (TextView) itemView.findViewById(R.id.tvQuestionTitle);
			title.setText(currentAskedQuestion.getTitle());
			
			TextView description = (TextView) itemView.findViewById(R.id.tvQuestionDescription);
			description.setText(currentAskedQuestion.getDescription());
			
			return itemView;
		}
	}
	
	 		
	
class AskedQuestions extends AsyncTask<String, String, String> {
			
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
                params.add(new BasicNameValuePair("Username", Username));
                
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
			 askedQuestions.add(question);
			 
			 
		}
       }
	
	
	

}