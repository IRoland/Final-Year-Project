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

public class RegisterActivity extends Activity implements OnClickListener{
	
	//Text Fields 
	private EditText firstname, secondname, user, pass, passconfirm, email;
	
	//Buttons
	private Button bRegister;
	
	//Progress Dialog
    private ProgressDialog pDialog;
 
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //testing on Emulator:
    private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/register.php";

    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register); //Iffy here
		
		//setup input fields
		firstname 	= (EditText)findViewById(R.id.etFirstName);
		secondname  = (EditText)findViewById(R.id.etSecondName);
		email	    = (EditText)findViewById(R.id.etEmail);
		user	    = (EditText)findViewById(R.id.etUsername);
		pass	    = (EditText)findViewById(R.id.etPassword);
		passconfirm = (EditText)findViewById(R.id.etPasswordConfirm);
		
		//setup buttons
		bRegister = (Button)findViewById(R.id.bRegister);
		

		
		//register listeners
		bRegister.setOnClickListener(this);
		
	}

	//When a certain button is pressed 
	@Override
	public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bRegister:
						new RegisterUser().execute();
					break;
			default:
			}
	}
	

	//Login In Page
	class RegisterUser extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;
		
        @Override
        protected void onPreExecute() {        
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Registering User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
            String fname = firstname.getText().toString();
            String sname = secondname.getText().toString();
            String Email = email.getText().toString();
            String password = pass.getText().toString();
            String username = user.getText().toString();
            String passwordconfirm = passconfirm.getText().toString();
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("firstname", fname));
                params.add(new BasicNameValuePair("secondname", sname));
                params.add(new BasicNameValuePair("email", Email));
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("passwordconfirm", passwordconfirm));
 
                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Register Attempt", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Registeration Successful!", json.toString());
                	Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
    				startActivity(i);
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Registeration Failed!", json.getString(TAG_MESSAGE));
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
            	Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
