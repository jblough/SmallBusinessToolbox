package com.josephblough.sbt.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class SmallBusinessProgram implements Bookmarkable {

    private final static String TAG = "SmallBusinessProgram";

    /*
Small Business Program Finder Industries:
   agriculture
   child-care
   disabled
   disaster-recovery
   export
   environment
   health-care
   technology
   tourism
   miscellaneous

Small Business Program Finder Type of Program/Service:
   woman-owned
   8a
   minority
   veteran

Small Business Program Finder Qualification:
   woman-owned
   minority
   veteran
     */
    
    public String category;
    public String state;
    public String agency;
    public String title;
    public String description;
    public String url;
    
    public SmallBusinessProgram(final JSONObject json) {
	category = json.optString("cat");
	state = json.optString("state");
	agency = json.optString("agency");
	title = json.optString("title");
	description = json.optString("description");
	url = json.optString("url");
    }
    
    @Override
    public String toString() {
        return "Category: '" + category + "', " +
        	"State: '" + state + "', " +
        	"Agency: '" + agency + "', " +
        	"Title: '" + title + "', " +
        	"Decription: '" + description + "', " +
        	"Url: '" + url + "'";
    }

    public String getType() {
	return "Small Business Program";
    }

    public String getName() {
	return title;
    }

    public String getUrl() {
	return url;
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("title", title);
	    if (description != null)
		json.put("description", description);
	    if (url != null)
		json.put("url", url);
	    if (category != null)
		json.put("cat", category);
	    if (state != null)
		json.put("state", state);
	    if (agency != null)
		json.put("agency", agency);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}

	return json.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((agency == null) ? 0 : agency.hashCode());
	result = prime * result
		+ ((category == null) ? 0 : category.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((state == null) ? 0 : state.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SmallBusinessProgram other = (SmallBusinessProgram) obj;
	if (agency == null) {
	    if (other.agency != null)
		return false;
	} else if (!agency.equals(other.agency))
	    return false;
	if (category == null) {
	    if (other.category != null)
		return false;
	} else if (!category.equals(other.category))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (state == null) {
	    if (other.state != null)
		return false;
	} else if (!state.equals(other.state))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }

    public int getDetailCount() {
	return 6;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	/*
    public String title;
    public String description;
    public String url;
    public String category;
    public String state;
    public String agency;
	 */
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "Description:";
	case 2:
	    return "URL:";
	case 3:
	    return "Category:";
	case 4:
	    return "State:";
	case 5:
	    return "Agency:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0:
	    return title;
	case 1:
	    return description;
	case 2:
	    return null; // url
	case 3:
	    return category;
	case 4:
	    return state;
	case 5:
	    return agency;
	};
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
    
    public String formatForSharing() {
	return url;
    }
}
