package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AnswerQuestionActivity extends Activity{
	
	List<Answer> questionAnswers = new ArrayList<Answer>();

	private ListView lvAnswerQuestions;

	private String Username, questionAskedByUsername, qtitle, questionDesc, fName, sName;
	
	private Button btnSubmitAnswer;
	
	private TextView tvQuestionTitleSection, tvQuestionSection,tvUserName,tvDateAsked;
	
	
	 // JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";
    
  //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    private static final String GetAnswersURL = "http://192.168.56.1:1234/FinalYearApp/questionAnswers.php";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answerquestionactivity);
		
		lvAnswerQuestions = (ListView) findViewById(R.id.lvAnswers);
		
		tvQuestionTitleSection = (TextView) findViewById(R.id.tvQuestionTitleSection);
		tvQuestionSection      = (TextView) findViewById(R.id.tvQuestionSection);
		tvUserName             = (TextView) findViewById(R.id.tvUserName);
		tvDateAsked            = (TextView) findViewById(R.id.tvDateAsked);
		
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
		
		
		//new GetAnswers().execute();
		
		ArrayAdapter<Answer> QuestionAnswersAdapter = new AnswerArrayAdapter();
		
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

    try {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("QuestionBy", questionAskedByUsername));
        
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
            answers=json_data.getString("answers");
            firstNames=json_data.getString("first_name");
            secondNames=json_data.getString("second_name");
            dates=json_data.getString("date");
            answererid=json_data.getInt("answereriD");
            reterievedAnswers.add(new Answer(R.drawable.english_icon,answererid,answers,firstNames,secondNames,dates));
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
	// TODO Auto-generated method stub
	super.onPostExecute(result);
	for(Answer answer: reterievedAnswers)
		questionAnswers.add(answer);
			}
		}
}
