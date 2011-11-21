package com.josephblough.sbt.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class GenericPost implements Bookmarkable {

    private final static String TAG = "GenericPost";
    
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
    public String closeDate;
    public String daysToClose;
    
    public GenericPost() {
	
    }
    
    public GenericPost(final JSONObject json) {
	title = json.optString("title");
	url = json.optString("url");
	closeDate = json.optString("close_date");
	daysToClose = json.optString("days_to_close");
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
	    if (closeDate != null)
		json.put("close_date", closeDate);
	    if (daysToClose != null)
		json.put("days_to_close", daysToClose);
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
	result = prime * result
		+ ((closeDate == null) ? 0 : closeDate.hashCode());
	result = prime * result
		+ ((daysToClose == null) ? 0 : daysToClose.hashCode());
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
	GenericPost other = (GenericPost) obj;
	if (closeDate == null) {
	    if (other.closeDate != null)
		return false;
	} else if (!closeDate.equals(other.closeDate))
	    return false;
	if (daysToClose == null) {
	    if (other.daysToClose != null)
		return false;
	} else if (!daysToClose.equals(other.daysToClose))
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
	return 4;
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
	case 2: // close date
	    return "Close Date:";
	case 3: // days to close
	    return "Days to Close:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0: // title
	    return title;
	case 1: // link
	    return url;
	case 2: // close date
	    if (isEmpty(closeDate)) {
		return closeDate;
	    }
	    else {
		try {
		    SimpleDateFormat closeDateParser = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat closeDateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
		    Date closingDate = closeDateParser.parse(closeDate);
		    return closeDateFormatter.format(closingDate);
		}
		catch (ParseException e) {
		    return closeDate;
		}
	    }
	case 3: // days to close
	    return null; // Don't return this since it may change since it was bookmarked
	};
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
}
