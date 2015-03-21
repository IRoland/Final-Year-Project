package com.example.finalproject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends ActionBarActivity{

	
	private SlidingTabLayout slidingTab;
	
	private ViewPager viewPager;
	
	private Toolbar toolbar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		slidingTab = (SlidingTabLayout) findViewById(R.id.slidingTabs);
		
		toolbar    = (Toolbar) findViewById(R.id.toolbar);
		
		viewPager  = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new TabsAdapter(this));
		
        slidingTab.setViewPager(viewPager);
		
	}

}
