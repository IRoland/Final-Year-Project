package com.example.finalproject;
 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity implements OnClickListener, OnItemSelectedListener{
	
	//Text Fields 
	private EditText question,title;
	
	//Text Views
	private TextView firstname,secondname;
	
	//Image Views
	private ImageView profilepic;
	
	//Buttons
	private Button bAskGroup, bAskPublic, Profilebtn;
	
	//Spinner
	private Spinner spinner;
	
	String username = "";
	String notificationUsernames;
	
	//For Notifications
	String accept, decline; 
	
	//Spinner selection
	String category, askedQuestion , namedTitle;
	ArrayAdapter adapter;
 
    //JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //To testing on your device
    //Put your local ip instead,
    
    List<Friend> notificationList = new ArrayList<Friend>();
    List<notificationHolder> notificationList2 = new ArrayList<notificationHolder>();
    
    //testing on Emulator:
    //When testing on GenyMotion Use ip : private static final String LOGIN_URL = "http://192.168.56.1:1234/webservice/index2.php"
    //When using emulator use : private static final String LOGIN_URL = "http://10.0.2.2:1234/webservice/index2.php"
    private static final String LOGIN_URL = "http://192.168.56.1:1234/FinalYearApp/addQuestion.php";
    private static final String GET_NOTIFICATIONS_URL = "http://192.168.56.1:1234/FinalYearApp/getNotifications.php";
  
   
    //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    // JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES  = "userlist";

   // For use with the camera
	protected static final int SELECT_FILE = 0;
	protected static final int REQUEST_CAMERA = 0;
	
	RoundImage roundedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// setup input fields
		question   = (EditText) findViewById(R.id.etQuestion);
		title      = (EditText) findViewById(R.id.etTitle);
		
		// Names
		firstname  = (TextView) findViewById(R.id.tvFirstnameProfile);
		secondname = (TextView) findViewById(R.id.tvSecondnameProfile);
		
		// Picture
		profilepic = (ImageView) findViewById(R.id.ivProfilePic);
		
		// Spinner element
        spinner    = (Spinner) findViewById(R.id.sCategorySelector);
		
		// setup buttons
		bAskGroup  = (Button) findViewById(R.id.bAskGroup);
		bAskPublic = (Button) findViewById(R.id.bAskPublic);
		Profilebtn = (Button) findViewById(R.id.Profilebtn);

		
		// listeners
		bAskGroup.setOnClickListener(this);
		bAskPublic.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        Profilebtn.setOnClickListener(this);
        
        //Check for any notifications
        new CheckForNotifications().execute();
		
		
		// Pass username from Login
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
	    
	    //Takes a resource from drawable and makes it round 
	    Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic_icon);
        roundedImage = new RoundImage(bm);
        profilepic.setImageDrawable(roundedImage);
		
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
				case R.id.Profilebtn:
					selectImage();
					break;
			default:
				}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		 // On selecting a spinner item
		category = parent.getItemAtPosition(position).toString();
	
 
	}
	

	//Post the Question to the Public
	class PostPublic extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
            askedQuestion = question.getText().toString();
            namedTitle = title.getText().toString();
            category = spinner.getSelectedItem().toString();
            
            String error = isValidQuestionAndTitle();
            
            if(!error.isEmpty()){
            	return error;
            }
            
                      
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
                    Intent askedQuestions = new Intent(ProfileActivity.this, AskedQuestionsActivity.class);
                    askedQuestions.putExtra("username", username);
    				startActivity(askedQuestions);
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

		//Check if Title and Question are not empty 
		private String isValidQuestionAndTitle() {
			if(!isStringValid(namedTitle)){
				return "Please Enter a Title";
			}
			else if(!isStringValid(askedQuestion)){
				return "Please Enter a Question";
			}
			
			return "";
		}
		
		private boolean isStringValid(String string) {
			if(string.isEmpty()){
				return false;
			}
			return true;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
			
	}
	
	//Select How to set image
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };
		
		//Creates an Alert with options
		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
		builder.setTitle("Add Photo");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					//Start the Camera, If camera is started through intent, no need for permissions 
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
					
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					
					Bitmap bm, circleBitmap;
					
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					//Make image 80x80
					bm = Bitmap.createScaledBitmap(bm, 80, 80, true);
					
					//Create a Circular Bitmap Image 
					circleBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);

					BitmapShader shader = new BitmapShader (bm,  TileMode.CLAMP, TileMode.CLAMP);
					Paint paint = new Paint();
					paint.setShader(shader);
					paint.setAntiAlias(true);
					Canvas canvas = new Canvas(circleBitmap);
					canvas.drawCircle(bm.getWidth()/2, bm.getHeight()/2, bm.getWidth()/2, paint);

					profilepic.setImageBitmap(circleBitmap);
					
					String path = android.os.Environment
							.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
					f.delete();
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, ProfileActivity.this);
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);;
				profilepic.setImageBitmap(bm);
				
			}
		}
	}
	
	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
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
	        case R.id.action_friends:
	         Intent friendsList = new Intent(ProfileActivity.this, FriendsListActivity.class);
	         friendsList.putExtra("username", username);
	         startActivity(friendsList);
	            	return true;
	        case R.id.action_askedQuestions:
	        	Intent askedQuestions = new Intent(ProfileActivity.this, AskedQuestionsActivity.class);
	        	askedQuestions.putExtra("username", username);
	        	startActivity(askedQuestions);
	        		return true;
	        case R.id.action_Notifications:
	        	Intent notifications = new Intent(ProfileActivity.this, FriendRequestsActivity.class);
	        	notifications.putExtra("username", username);
	        	startActivity(notifications);
	        		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	public void createNotification(int notiID) {
		    // Prepare intent which is triggered if the
		    // notification is selected
			// User new Intent() to not start an intent on pending intent.
		    Intent viewIntent = new Intent(this, FriendRequestsActivity.class);
		    viewIntent.putExtra("username", username);
		    	
		    PendingIntent vIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

		    // Build notification
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle("Friend Request")
		        .setContentText("You have " + notiID + " Pending Friend Request(s)...").setSmallIcon(R.drawable.profilepic_icon)
		        .setContentIntent(vIntent)
		        .addAction(0, "View Notifications", vIntent)
		        .build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    
		    // hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(notiID, noti);

		  }
	
	
	class CheckForNotifications extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			
			 // Check for success tag
            int success;
			JSONArray results,jArray;
			String message;
			String first_names = "";
			String second_names = "";

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Username", username));
                
                // getting product details by making HTTP requests
                JSONObject json = jsonParser.makeHttpRequest(
                       GET_NOTIFICATIONS_URL, "POST", params);
 
                // check your log for json response
                Log.d("Getting notifications", json.toString());
 
                // json success tag   
                success = json.getInt(TAG_SUCCESS);
                message = json.getString(TAG_MESSAGE);
                results = json.getJSONArray(TAG_VALUES);

                createNotification(results.length());
                
                
                if (success == 1) {
                	Log.d("Success!", json.toString());
                	
                    
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
