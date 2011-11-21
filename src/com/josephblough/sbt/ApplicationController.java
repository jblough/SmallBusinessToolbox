package com.josephblough.sbt;

import java.util.HashMap;
import java.util.Map;

import com.josephblough.sbt.data.Bookmarks;
import com.josephblough.sbt.tasks.AutoLocationSelectorTask;
import com.josephblough.sbt.tasks.BookmarkLoaderTask;
import com.josephblough.sbt.tasks.BookmarkSaverTask;

import android.app.Application;
import android.location.Address;
import android.location.Location;
import android.util.Log;
import android.widget.Spinner;

public class ApplicationController extends Application {

    private final static String TAG = "ApplicationController";

    public final Map<String, String> stateLookupMap = new HashMap<String, String>();
    public final Map<String, String> agencyLookupMap = new HashMap<String, String>();
    private final Map<String, Integer> stateIndexArrayLookupMap = new HashMap<String, Integer>();
    public final Map<String, String> searchTypeMap = new HashMap<String, String>();
    
    public Location location = null;
    public Address address = null;
    public Bookmarks bookmarks;
    
    @Override
    public void onCreate() {
        super.onCreate();

        //Do Application initialization over here
	Log.d(TAG, "onCreate");
	
	loadBookmarks();

	initStateMap();
	initAgencyMap();
	initSearchTypeMap();
	
	// get current location for autoselecting current state
	new AutoLocationSelectorTask(this).execute();
    }
    
    private void initStateMap() {
	stateLookupMap.put("Alabama", "AL");
	stateLookupMap.put("Alaska", "AK");
	stateLookupMap.put("Arizona", "AZ");
	stateLookupMap.put("Arkansas", "AR");
	stateLookupMap.put("California", "CA");
	stateLookupMap.put("Colorado", "CO");
	stateLookupMap.put("Connecticut", "CT");
	stateLookupMap.put("Delaware", "DE");
	stateLookupMap.put("District of Columbia", "DC");
	stateLookupMap.put("Florida", "FL");
	stateLookupMap.put("Georgia", "GA");
	stateLookupMap.put("Guam", "GU");
	stateLookupMap.put("Hawaii", "HI");
	stateLookupMap.put("Idaho", "ID");
	stateLookupMap.put("Illinois", "IL");
	stateLookupMap.put("Indiana", "IN");
	stateLookupMap.put("Iowa", "IA");
	stateLookupMap.put("Kansas", "KS");
	stateLookupMap.put("Kentucky", "KY");
	stateLookupMap.put("Louisiana", "LA");
	stateLookupMap.put("Maine", "ME");
	stateLookupMap.put("Maryland", "MD");
	stateLookupMap.put("Massachusetts", "MA");
	stateLookupMap.put("Michigan", "MI");
	stateLookupMap.put("Minnesota", "MN");
	stateLookupMap.put("Mississippi", "MS");
	stateLookupMap.put("Missouri", "MO");
	stateLookupMap.put("Montana", "MT");
	stateLookupMap.put("Nebraska", "NE");
	stateLookupMap.put("Nevada", "NV");
	stateLookupMap.put("New Hampshire", "NH");
	stateLookupMap.put("New Jersey", "NJ");
	stateLookupMap.put("New Mexico", "NM");
	stateLookupMap.put("New York", "NY");
	stateLookupMap.put("North Carolina", "NC");
	stateLookupMap.put("North Dakota", "ND");
	stateLookupMap.put("Ohio", "OH");
	stateLookupMap.put("Oklahoma", "OK");
	stateLookupMap.put("Oregon", "OR");
	stateLookupMap.put("Pennsylvania", "PA");
	stateLookupMap.put("Puerto Rico", "PR");
	stateLookupMap.put("Rhode Island", "RI");
	stateLookupMap.put("South Carolina", "SC");
	stateLookupMap.put("South Dakota", "SD");
	stateLookupMap.put("Tennessee", "TN");
	stateLookupMap.put("Texas", "TX");
	stateLookupMap.put("Utah", "UT");
	stateLookupMap.put("Vermont", "VT");
	stateLookupMap.put("Virginia", "VA");
	stateLookupMap.put("Virgin Islands", "VI");
	stateLookupMap.put("Washington", "WA");
	stateLookupMap.put("West Virginia", "WV");
	stateLookupMap.put("Wisconsin", "WI");
	stateLookupMap.put("Wyoming", "WY");

	// State abbreviations
	stateIndexArrayLookupMap.put("AL", 0);
	stateIndexArrayLookupMap.put("AK", 1);
	stateIndexArrayLookupMap.put("AZ", 2);
	stateIndexArrayLookupMap.put("AR", 3);
	stateIndexArrayLookupMap.put("CA", 4);
	stateIndexArrayLookupMap.put("CO", 5);
	stateIndexArrayLookupMap.put("CT", 6);
	stateIndexArrayLookupMap.put("DE", 7);
	stateIndexArrayLookupMap.put("DC", 8);
	stateIndexArrayLookupMap.put("FL", 9);
	stateIndexArrayLookupMap.put("GA", 10);
	stateIndexArrayLookupMap.put("GU", 11);
	stateIndexArrayLookupMap.put("HI", 12);
	stateIndexArrayLookupMap.put("ID", 13);
	stateIndexArrayLookupMap.put("IL", 14);
	stateIndexArrayLookupMap.put("IN", 15);
	stateIndexArrayLookupMap.put("IA", 16);
	stateIndexArrayLookupMap.put("KS", 17);
	stateIndexArrayLookupMap.put("KY", 18);
	stateIndexArrayLookupMap.put("LA", 19);
	stateIndexArrayLookupMap.put("ME", 20);
	stateIndexArrayLookupMap.put("MD", 21);
	stateIndexArrayLookupMap.put("MA", 22);
	stateIndexArrayLookupMap.put("MI", 23);
	stateIndexArrayLookupMap.put("MN", 24);
	stateIndexArrayLookupMap.put("MS", 25);
	stateIndexArrayLookupMap.put("MO", 26);
	stateIndexArrayLookupMap.put("MT", 27);
	stateIndexArrayLookupMap.put("NE", 28);
	stateIndexArrayLookupMap.put("NV", 29);
	stateIndexArrayLookupMap.put("NH", 30);
	stateIndexArrayLookupMap.put("NJ", 31);
	stateIndexArrayLookupMap.put("NM", 32);
	stateIndexArrayLookupMap.put("NY", 33);
	stateIndexArrayLookupMap.put("NC", 34);
	stateIndexArrayLookupMap.put("ND", 35);
	stateIndexArrayLookupMap.put("OH", 36);
	stateIndexArrayLookupMap.put("OK", 37);
	stateIndexArrayLookupMap.put("OR", 38);
	stateIndexArrayLookupMap.put("PA", 39);
	stateIndexArrayLookupMap.put("PR", 40);
	stateIndexArrayLookupMap.put("RI", 41);
	stateIndexArrayLookupMap.put("SC", 42);
	stateIndexArrayLookupMap.put("SD", 43);
	stateIndexArrayLookupMap.put("TN", 44);
	stateIndexArrayLookupMap.put("TX", 45);
	stateIndexArrayLookupMap.put("UT", 46);
	stateIndexArrayLookupMap.put("VT", 47);
	stateIndexArrayLookupMap.put("VA", 48);
	stateIndexArrayLookupMap.put("VI", 49);
	stateIndexArrayLookupMap.put("WA", 50);
	stateIndexArrayLookupMap.put("WV", 51);
	stateIndexArrayLookupMap.put("WI", 52);
	stateIndexArrayLookupMap.put("WY", 53);
	
	// State names
	stateIndexArrayLookupMap.put("Alabama", 0);
	stateIndexArrayLookupMap.put("Alaska", 1);
	stateIndexArrayLookupMap.put("Arizona", 2);
	stateIndexArrayLookupMap.put("Arkansas", 3);
	stateIndexArrayLookupMap.put("California", 4);
	stateIndexArrayLookupMap.put("Colorado", 5);
	stateIndexArrayLookupMap.put("Connecticut", 6);
	stateIndexArrayLookupMap.put("Delaware", 7);
	stateIndexArrayLookupMap.put("District of Columbia", 8);
	stateIndexArrayLookupMap.put("Florida", 9);
	stateIndexArrayLookupMap.put("Georgia", 10);
	stateIndexArrayLookupMap.put("Guam", 11);
	stateIndexArrayLookupMap.put("Hawaii", 12);
	stateIndexArrayLookupMap.put("Idaho", 13);
	stateIndexArrayLookupMap.put("Illinois", 14);
	stateIndexArrayLookupMap.put("Indiana", 15);
	stateIndexArrayLookupMap.put("Iowa", 16);
	stateIndexArrayLookupMap.put("Kansas", 17);
	stateIndexArrayLookupMap.put("Kentucky", 18);
	stateIndexArrayLookupMap.put("Louisiana", 19);
	stateIndexArrayLookupMap.put("Maine", 20);
	stateIndexArrayLookupMap.put("Maryland", 21);
	stateIndexArrayLookupMap.put("Massachusetts", 22);
	stateIndexArrayLookupMap.put("Michigan", 23);
	stateIndexArrayLookupMap.put("Minnesota", 24);
	stateIndexArrayLookupMap.put("Mississippi", 25);
	stateIndexArrayLookupMap.put("Missouri", 26);
	stateIndexArrayLookupMap.put("Montana", 27);
	stateIndexArrayLookupMap.put("Nebraska", 28);
	stateIndexArrayLookupMap.put("Nevada", 29);
	stateIndexArrayLookupMap.put("New Hampshire", 30);
	stateIndexArrayLookupMap.put("New Jersey", 31);
	stateIndexArrayLookupMap.put("New Mexico", 32);
	stateIndexArrayLookupMap.put("New York", 33);
	stateIndexArrayLookupMap.put("North Carolina", 34);
	stateIndexArrayLookupMap.put("North Dakota", 35);
	stateIndexArrayLookupMap.put("Ohio", 36);
	stateIndexArrayLookupMap.put("Oklahoma", 37);
	stateIndexArrayLookupMap.put("Oregon", 38);
	stateIndexArrayLookupMap.put("Pennsylvania", 39);
	stateIndexArrayLookupMap.put("Puerto Rico", 40);
	stateIndexArrayLookupMap.put("Rhode Island", 41);
	stateIndexArrayLookupMap.put("South Carolina", 42);
	stateIndexArrayLookupMap.put("South Dakota", 43);
	stateIndexArrayLookupMap.put("Tennessee", 44);
	stateIndexArrayLookupMap.put("Texas", 45);
	stateIndexArrayLookupMap.put("Utah", 46);
	stateIndexArrayLookupMap.put("Vermont", 47);
	stateIndexArrayLookupMap.put("Virginia", 48);
	stateIndexArrayLookupMap.put("Virgin Islands", 49);
	stateIndexArrayLookupMap.put("Washington", 50);
	stateIndexArrayLookupMap.put("West Virginia", 51);
	stateIndexArrayLookupMap.put("Wisconsin", 52);
	stateIndexArrayLookupMap.put("Wyoming", 53);
    }

    private void initAgencyMap() {
	agencyLookupMap.put("Dept. of Defense", "DOD");
	agencyLookupMap.put("Dept. of Health and Human Services", "HHS");
	agencyLookupMap.put("National Aeronautics and Space Administration", "NASA");
	agencyLookupMap.put("National Science Foundation", "NSF");
	agencyLookupMap.put("Dept. of Energy", "DOE");
	agencyLookupMap.put("United States Dept. of Agriculture", "USDA");
	agencyLookupMap.put("Environmental Protection Agency", "EPA");
	agencyLookupMap.put("Dept. of Commerce", "DOC");
	agencyLookupMap.put("Dept. of Education", "ED");
	agencyLookupMap.put("Dept. of Transportation", "DOT");
	agencyLookupMap.put("Dept. of Homeland Security", "DHS");
    }

    private void initSearchTypeMap() {
	/*
	Grants - grants 
	Solicitation - presolicitation 
	Challenges - challenges 
	R&D - randd 
	Patents - patent 
	Science & Technology R&D data -	science_accelerator	 
	*/
	searchTypeMap.put("Grants", "grants");
	searchTypeMap.put("Solicitations", "presolicitation");
	searchTypeMap.put("Challenges", "challenges");
	searchTypeMap.put("Research & Development", "randd");
	searchTypeMap.put("Patents", "patent");
	searchTypeMap.put("Science & Technology R&D data", "science_accelerator");
    }    
    
    public void preselectCurrentState(final Spinner stateSpinner) {
	if (address != null && address.getAdminArea() != null) {
	    final Integer stateIndex = stateIndexArrayLookupMap.get(address.getAdminArea());
	    if (stateIndex != null) {
		stateSpinner.setSelection(stateIndex);
	    }
	}
    }
    
    
    // Bookmark code
    public void loadBookmarks() {
	if (bookmarks == null) {
	    bookmarks = new Bookmarks();
	}
	
	//bookmarks.loadBookmarks(this);
	new BookmarkLoaderTask(this).execute(bookmarks);
    }
    
    public void saveBookmarks() {
	if (bookmarks == null) {
	    bookmarks = new Bookmarks();
	}
	
	//bookmarks.saveBookmarks(this);
	new BookmarkSaverTask(this).execute(bookmarks);
    }
}
