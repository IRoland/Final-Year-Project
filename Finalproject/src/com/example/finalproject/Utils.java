package com.example.finalproject;

public class Utils {
	
	public static int getDrawableFor(int drawable){
	    switch(drawable){
	    case 1:
	    	drawable = R.drawable.english_icon;
	    	break;
	    case 2:
	    	drawable = R.drawable.maths_icon;
	    	break;
	    case 3:
	    	drawable = R.drawable.art_icon;
	    	break;
	    case 4:
	    	drawable = R.drawable.science_icon;
	    	break;
	    case 5:
	    	drawable = R.drawable.geography_icon;
	    	break;
	    case 6:
	    	drawable = R.drawable.religion_icon;
	    	break;
	    default:
	    	drawable = R.drawable.history_icon;
	    }
	    
	    return drawable;
	}
	

}
