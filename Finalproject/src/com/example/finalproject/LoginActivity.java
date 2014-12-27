package com.example.finalproject;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener{
	
	//Text Fields 
	private EditText user, pass;
	
	//Buttons
	private Button bSubmit, bReg;
	
	//Progress Dialog
    private ProgressDialog pDialog;
 
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php login script location:
    
  
    //To testing on your device
    //Put your local ip instead,
    
    //testing on Emulator:
    //When testing on GenyMotion Use ip : private static final String LOGIN_URL = "http://192.168.56.1:1234/webservice/index2.php"
    //When using emulator use : private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/index2.php"
    private static final String LOGIN_URL = "http://192.168.56.1:1234/webservice/index2.php";
    
   
    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//setup input fields
		user = (EditText)findViewById(R.id.etUser);
		pass = (EditText)findViewById(R.id.etPass);
		
		//setup buttons
		bSubmit = (Button)findViewById(R.id.bSubmit);
		bReg = (Button)findViewById(R.id.bReg);

		
		//register listeners
		bSubmit.setOnClickListener(this);
		bReg.setOnClickListener(this);
		
	}

	//When a certain button is pressed 
	@Override
	public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bSubmit:
						new AttemptLogin().execute();
					break;
				case R.id.bReg:
					Intent i = new Intent(this, RegisterActivity.class);
					startActivity(i);
				break;
			default:
				}
	}
	

	//Login In Page
	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting To Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
 
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Login attempt", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Login Successful!", json.toString());
                	Intent profile = new Intent(LoginActivity.this, ProfileActivity.class);
                	//
                	profile.putExtra("Username", username );
    				startActivity(profile);
    				
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
