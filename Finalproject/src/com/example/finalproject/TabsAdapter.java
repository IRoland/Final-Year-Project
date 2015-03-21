package com.example.finalproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabsAdapter extends PagerAdapter {

    /**
     * @return the number of pages to display
     */
	
	private final int FRIENDS = 0;
	private final int USERS = 1;
	private Context context;

	
	public TabsAdapter(Context context){
		this.context = context;
		
	}
	
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * @return true if the value returned from
     *         {@link #instantiateItem(ViewGroup, int)} is the same object
     *         as the {@link View} added to the {@link ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)
    /**
     * Return the title of the item at {@code position}. This is important
     * as what this method returns is what is displayed in the
     * {@link SlidingTabLayout}.
     * <p>
     * Here we construct one using the position value, but for real
     * application the title should refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int position) {
    	if(position + 1 == 1){
    		return "Friends " + (position + 1);
    	}else if (position + 1 == 2){
    		return "Users " + (position + 1);
    	}
    		return "item" + (position + 1);
        
    }

    // END_INCLUDE (pageradapter_getpagetitle)

    /**
     * Instantiate the {@link View} which should be displayed at
     * {@code position}. Here we inflate a layout from the apps resources
     * and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
    	View view;
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	switch(position){
    	case FRIENDS:
    		 view = inflater.inflate(R.layout.activity_profile, null);
    		break;
    	case USERS:
    		view = inflater.inflate(R.layout.fragment_red, null);
    		break;
    	default:
    		return view = new View(context);
    	}
        // Add the newly created View to the ViewPager
        container.addView(view);

        // Return the View
        return view;
    }

	/**
     * Destroy the item from the {@link ViewPager}. In our case this is
     * simply removing the {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

}