package com.example.finalproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.AsyncTask;
import android.util.Log;



//1: Type passed to doInBackground();
//2: Type passed to onProgressUpdate();
//3: Type passed to onPostExecute() by doInBackground();
class Get extends AsyncTask<String, String, String[]> {
	
	//JSON parser class
    JSONParser jsonParser = new JSONParser();

	public String[] returnValues;
	
	//Response Tags
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_VALUES = "values";
	
   
	private static final String LOGIN_URL = "http://192.168.56.1:1234/FinalYearApp/get.php";
	
	
	@Override
	protected String[] doInBackground(String... args) {
		
		 // Check for success tag
       int success;
       String username = args[0];
       String table = args[1];
       returnValues = new String[args.length-2];
       
       try {
           // Building Parameters
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           for(int i = 0; i < returnValues.length; i++ ){
        	   
           params.add(new BasicNameValuePair("returnValues"+i,args[i+2]));
     
           }
           params.add(new BasicNameValuePair("username",username));
           params.add(new BasicNameValuePair("table",table));
           
           Log.d("request!", "starting");
           // getting product details by making HTTP request
           JSONObject json = jsonParser.makeHttpRequest(
                  LOGIN_URL, "POST", params);

           // check your log for json response
           Log.d("Login attempt", json.toString());

           // json success tag
           
           returnValues = JSONArrayToArray(new JSONArray().put(json.getJSONObject(TAG_VALUES)));
           
           String returnString = returnValues[0];
           returnValues = stringToArray(returnString);
           
           success = json.getInt(TAG_SUCCESS);
           if (success == 1) {
           	Log.d("Retrived Data", json.toString());          	
           }else{
           	Log.d("Failed to get Data!", json.getString(TAG_MESSAGE));  	
           }
       } catch (JSONException e) {
           e.printStackTrace();
       }
     

       return returnValues;
		
	}
	
	
	private String[] stringToArray(String returnString) {
		
		returnString = returnString.substring(1,returnString.length()-1);
		String[] returnArray = returnString.split("\",");
		
		for(int i = 0; i < returnArray.length; i++){
			returnArray[i] = returnArray[i].split("\":")[1].replace("\"","");
		}
		
		return returnArray;
	}

	
	public String[] JSONArrayToArray(JSONArray JSONAr){
		ArrayList<String> list = new ArrayList<String>();     
		JSONArray jsonArray = (JSONArray)JSONAr; 
		if (jsonArray != null) { 
		   int len = jsonArray.length();
		   for (int i=0;i<len;i++){ 
		    try {
				list.add(jsonArray.get(i).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   } 
		}
		String[] array = new String[list.size()];
		for(int i =0; i < list.size(); i++){
			array[i] = list.get(i);
		}
		return array;
		
	}
	
    protected void onPostExecute(String file_url) {
     System.out.println("I FOUND THESE " + returnValues);
     
    }

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
    
  
	
	
}