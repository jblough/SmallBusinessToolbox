package com.josephblough.sbt.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class GreenPost implements Bookmarkable {

    private final static String TAG = "GreenPost";
    
    /*
Green Types: (not sure how these map.  example is 'presolicitation')
   Grants
   Solicitations
   Challenges
   R&D
   Patents and Science & Technology R&D data
     */
    
    public String title;
    public String url;
    
    public GreenPost(final JSONArray array) throws JSONException {
	if (array.length() == 2) {
	    title = array.getString(0);
	    url = array.getString(1);
	}
    }
    
    public GreenPost(final JSONObject json) throws JSONException {
	title = json.optString("title");
	url = json.optString("url");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Url: '" + url + "'";
    }

    public String getType() {
	return "Post";
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
	    if (url != null)
		json.put("url", url);
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
	GreenPost other = (GreenPost) obj;
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
	return 2;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0: // title
	    return "Title:";
	case 1: // link
	    return "URL:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0: // title
	    return title;
	case 1: // link
	    return url;
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
