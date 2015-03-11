package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity{

	
	String Username,ChallengerUsername;
	
	private TextView QuestionSection, Player1Name,Player1Score,Player2Name,Player2Score;
	
	private ImageView Player1Pic,Player2Pic;
	
	private Button Answer_A,Answer_B,Answer_C,Answer_D;
	
	//Counter for Current Question
	int Question_No,Randomizer;
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "questionlist";
	
	//JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    List<QuizQuestion> QuizQuestions = new ArrayList<QuizQuestion>();
    
    private static final String GetQuizQuestionsURL = "http://192.168.56.1:1234/FinalYearApp/quizQuestions.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		QuestionSection  = (TextView) findViewById(R.id.tvQuizQuestion);
		
		Player1Name  = (TextView) findViewById(R.id.tvPlayer1Name);
		Player1Score = (TextView) findViewById(R.id.tvPlayer1Score);
		
		Player2Name  = (TextView) findViewById(R.id.tvPlayer2Name);
		Player2Score = (TextView) findViewById(R.id.tvPlayer2Score);
		
		Player1Pic   = (ImageView) findViewById(R.id.ivPlayer1);
		Player2Pic   = (ImageView) findViewById(R.id.ivPlayer2);
		
		Answer_A     = (Button) findViewById(R.id.btnAnswerA);
		Answer_B     = (Button) findViewById(R.id.btnAnswerB);
		Answer_C     = (Button) findViewById(R.id.btnAnswerC);
		Answer_D     = (Button) findViewById(R.id.btnAnswerD);
		

		Username = getIntent().getExtras().getString("username");	
		ChallengerUsername = getIntent().getExtras().getString("challengerusername");		

		
		Player1Name.setText(Username);
		Player2Name.setText(ChallengerUsername);
		Player1Score.setText("0");
		Player2Score.setText("0");
		
		new GetQuizQuestions().execute();
		
		
	}
	

public void askQuestion(int index,int randomizer){
	final QuizQuestion question;
	
	if(index < QuizQuestions.size()){
		
	
	question = QuizQuestions.get(index);
	
	QuestionSection.setText(question.getQuestion());
	
	//Determine Which button Displays what answer
	//A
	if(randomizer == 1){
		Answer_A.setText(question.getCorrectAnswer());
	}else if(randomizer == 2){
		Answer_A.setText(question.getChoice2());
	}else if(randomizer == 3){
		Answer_A.setText(question.getChoice3());
	}else if(randomizer == 4){
		Answer_A.setText(question.getChoice4());
	}
	
	//B
	if(randomizer == 2){
		Answer_B.setText(question.getCorrectAnswer());
	}else if(randomizer == 1){
		Answer_B.setText(question.getChoice2());
	}else if(randomizer == 3){
		Answer_B.setText(question.getChoice3());
	}else if(randomizer == 4){
		Answer_B.setText(question.getChoice4());
	}
	
	//C
	if(randomizer == 3){
		Answer_C.setText(question.getCorrectAnswer());
	}else if(randomizer == 2){
		Answer_C.setText(question.getChoice2());
	}else if(randomizer == 1){
		Answer_C.setText(question.getChoice3());
	}else if(randomizer == 4){
		Answer_C.setText(question.getChoice4());
	}
	
	//D
	if(randomizer == 4){
		Answer_D.setText(question.getCorrectAnswer());
	}else if(randomizer == 2){
		Answer_D.setText(question.getChoice2());
	}else if(randomizer == 3){
		Answer_D.setText(question.getChoice3());
	}else if(randomizer == 1){
		Answer_D.setText(question.getChoice4());
	}
	
	Answer_A.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			if(Answer_A.getText().toString() == question.getCorrectAnswer()){
				//new upDateScore().execute(); 
				getIndexAndRandomizer();
			}else{
				getIndexAndRandomizer();
			}
		}
		
	});
	
	Answer_B.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			if(Answer_B.getText().toString() == question.getCorrectAnswer()){
				//new upDateScore().execute(); 
				getIndexAndRandomizer();
			}else{
				getIndexAndRandomizer();
			}
		}
		
	});
	
	Answer_C.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			if(Answer_C.getText().toString() == question.getCorrectAnswer()){
				//new upDateScore().execute(); 
				getIndexAndRandomizer();
			}else{
				getIndexAndRandomizer();
			}
		}
		
	});
	
	Answer_D.setOnClickListener(new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			if(Answer_D.getText().toString() == question.getCorrectAnswer()){
				//new upDateScore().execute(); 
				getIndexAndRandomizer();
			}else{
				getIndexAndRandomizer();
			}
		}
		
	});
	
	}else{
		
		//QuizOver
		Toast.makeText(this, "GameOver", Toast.LENGTH_LONG).show();
	}
	
}

public void getIndexAndRandomizer(){
	Random rand = new Random();
	int min = 1,max = 4;
    int randomNum = rand.nextInt((max - min) + 1) + min;
	askQuestion(Question_No, randomNum);
	Question_No++;
}

class GetQuizQuestions extends AsyncTask<String, String, String> {
		
		List<QuizQuestion> reterievedQuizQuestions = new ArrayList<QuizQuestion>();
	
	protected String doInBackground(String... args) {
		
		int success;
		JSONArray results;
		String message;
		String question;
		String answerA,answerB,answerC,answerD;
		

        try {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
     
            Log.d("request!", "Getting Quiz Questions");
            // getting product details by making HTTP requests
            JSONObject json = jsonParser.makeHttpRequest(
            		GetQuizQuestionsURL, "POST", params);

            // check your log for json response
            Log.d("Reterieving Questions", json.toString());

            //Json tags
            success = json.getInt(TAG_SUCCESS);
            message = json.getString(TAG_MESSAGE);
            results = json.getJSONArray(TAG_VALUES);
            
            Log.d("Quiz Questions Have been Retrieved!", json.toString());
            
            
            JSONObject json_data=null;
           
            for(int i=0;i<results.length();i++){
                json_data = results.getJSONObject(i);
                question=json_data.getString("QuizQuestion");
                answerA=json_data.getString("AnswerA");
                answerB=json_data.getString("AnswerB");
                answerC=json_data.getString("AnswerC");
                answerD=json_data.getString("AnswerD");
                reterievedQuizQuestions.add(new QuizQuestion(question,answerA,answerB,answerC,answerD));
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
			for(QuizQuestion question: reterievedQuizQuestions){
				QuizQuestions.add(question);
			}
				getIndexAndRandomizer();

		}

	
	}


	

}
