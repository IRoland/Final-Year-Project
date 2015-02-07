package com.example.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationReceiverActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notifications);
    
	TextView tv = new TextView(this);
	tv.setText("Yo!");
	
	setContentView(tv);
  }
} 