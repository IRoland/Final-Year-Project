package com.example.finalproject;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends Activity implements OnClickListener{
	
	//Text Fields 
	private EditText question,title;
	
	//Buttons
	private Button bAskGroup, bAskPublic;
	
	String username = "";
 
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php login script location:
    
  
    //To testing on your device
    //Put your local ip instead,
    
    //testing on Emulator:
    //When testing on GenyMotion Use ip : private static final String LOGIN_URL = "http://192.168.56.1:1234/webservice/index2.php"
    //When using emulator use : private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/index2.php"
    private static final String LOGIN_URL = "http://192.168.56.1:1234/webservice/addQuestion.php";
    
   
    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		//setup input fields
		question = (EditText)findViewById(R.id.etQuestion);
		title = (EditText)findViewById(R.id.etTitle);
		
		
		//setup buttons
		bAskGroup = (Button)findViewById(R.id.bAskGroup);
		bAskPublic = (Button)findViewById(R.id.bAskPublic);

		
		//Question listeners
		bAskGroup.setOnClickListener(this);
		bAskPublic.setOnClickListener(this);
		
		
		//Pass username from Login
		username = getIntent().getExtras().getString("Username");
		System.out.print(username);
		
	}

	//When a certain button is pressed 
	@Override
	public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bAskGroup:	
					break;
				case R.id.bAskPublic:
					new PostPublic().execute();
				break;
			default:
				}
	}
	

	
	//Post the Question to the Public
	class PostPublic extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
            
            String askedQuestion = question.getText().toString();
            String namedTitle = title.getText().toString();
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("AskedQuestion", askedQuestion));
                params.add(new BasicNameValuePair("Title", namedTitle));
                params.add(new BasicNameValuePair("Username", username));
                
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Saving Question", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                
                if (success == 1) {
                	Log.d("Question Posted!", json.toString());
                //  Intent i = new Intent(LoginActivity.this, ProfileActivity.class);
    			//	startActivity(i);
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

	}
		 
}
