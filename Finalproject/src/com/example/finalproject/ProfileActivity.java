package com.example.finalproject;
 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity implements OnClickListener, OnItemSelectedListener{
	
	//Text Fields 
	private EditText question,title;
	
	//Text Views
	private TextView firstname,secondname;
	
	//Buttons
	private Button bAskGroup, bAskPublic;
	
	//Spinner
	private Spinner spinner;
	
	String username = "";
	
	//Spinner selection
	String category;
	ArrayAdapter adapter;
 
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
		
		//Names
		firstname = (TextView)findViewById(R.id.tvFirstnameProfile);
		secondname = (TextView)findViewById(R.id.tvSecondnameProfile);
		
		// Spinner element
		
        spinner = (Spinner) findViewById(R.id.spinner1);
 
        
		
		//setup buttons
		bAskGroup = (Button)findViewById(R.id.bAskGroup);
		bAskPublic = (Button)findViewById(R.id.bAskPublic);

		
		//listeners
		bAskGroup.setOnClickListener(this);
		bAskPublic.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
		
		
		//Pass username from Login
		username = getIntent().getExtras().getString("Username");
		
		Get getNames = new Get();
		
		getNames.execute(username,"users","first_name","second_name");
		
		try {
			
			firstname.setText(""+getNames.get()[0]);
			secondname.setText(""+getNames.get()[1]);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		//Adapter for the spinner
		adapter = ArrayAdapter.createFromResource(this,R.array.category_array,android.R.layout.simple_spinner_dropdown_item);

	   //Attaching data adapter to spinner
	    spinner.setAdapter(adapter);
		
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
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		 // On selecting a spinner item
		category = parent.getItemAtPosition(position).toString();
		// Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
 
	}
	

	//Post the Question to the Public
	class PostPublic extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
            
            String askedQuestion = question.getText().toString();
            String namedTitle = title.getText().toString();
            category = spinner.getSelectedItem().toString();
            
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("AskedQuestion", askedQuestion));
                params.add(new BasicNameValuePair("Title", namedTitle));
                params.add(new BasicNameValuePair("Username", username));
                params.add(new BasicNameValuePair("Category", category));
                
                System.out.print("Currently Selected" + category);
                Log.d("request!", "starting");
                // getting product details by making HTTP requests
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


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
			
	}
		 
}
