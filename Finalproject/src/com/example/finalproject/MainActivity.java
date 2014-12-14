package com.example.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener, android.view.View.OnClickListener{
	
	private static final String TAG = "Cheeseballs";
	EditText etUser,etPass;
	Button bSubmit;

	String username, password;

	HttpClient httpclient;
	
	HttpPost httppost;
	
	ArrayList<NameValuePair> nameValuePairs;
	
	HttpResponse response;
	HttpEntity entity;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        
        initialise();
    }


    private void initialise() {

		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		bSubmit = (Button) findViewById(R.id.bSubmit);
		
		bSubmit.setOnClickListener(this);
		
	}



	public void onClick(View v) {
		// This Auto-generated method stub
		new LongOperation().execute();
	}

	
private class LongOperation extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... params) {
	      
	                try {
	                  httpclient = new DefaultHttpClient();
	       	          httppost = new HttpPost("http://10.0.2.2:1234/FinalYearApp/index.php");
	       	          ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	       	          postParameters.add(new BasicNameValuePair("username", etUser.getText().toString()));
	       	          postParameters.add(new BasicNameValuePair("password", etPass.getText().toString()));

	       	          httppost.setEntity(new UrlEncodedFormEntity(postParameters));                   
	       	          HttpResponse response = httpclient.execute(httppost);
	       	          Log.i("postData", response.getStatusLine().toString());
	       	          Log.i("postData", "YESSSSSSSSS");
	                } catch (Exception e) {
	                	Log.e(TAG, "Error:  "+e.toString());
	     	    	   System.out.println("Failed");
	                }
	            
	                
	           return null;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        }

	        @Override
	        protected void onPreExecute() {
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {
	        }
	    }
	

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

private static String convertStreamToString(InputStream is){
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();
	
	String line = null;
	try{
		while((line = reader.readLine()) != null ){
			sb.append(line + "\n");
		}
	}catch(IOException e){
	  e.printStackTrace();
	} finally {
		try{
			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	return sb.toString();
	}
}