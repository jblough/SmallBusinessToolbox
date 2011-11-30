package com.josephblough.sbt.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public String createDate;
    public String closeDate;
    public String daysToClose;
    
    public GreenPost(final JSONArray array) throws JSONException {
	if (array.length() == 2) {
	    title = array.getString(0);
	    url = array.getString(1);
	}
    }
    
    public GreenPost(final JSONObject json) throws JSONException {
	title = json.optString("title");
	url = json.optString("url");
	createDate = json.optString("create_date");
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
	    if (createDate != null)
		json.put("create_date", createDate);
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
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
	result = prime * result
		+ ((createDate == null) ? 0 : createDate.hashCode());
	result = prime * result
		+ ((closeDate == null) ? 0 : closeDate.hashCode());
	result = prime * result
		+ ((daysToClose == null) ? 0 : daysToClose.hashCode());
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
	if (createDate == null) {
	    if (other.createDate != null)
		return false;
	} else if (!createDate.equals(other.createDate))
	    return false;
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
	return 5;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "URL:";
	case 2:
	    return "Create Date:";
	case 3:
	    return "Close Date:";
	case 4:
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
	case 2:
	    if (isEmpty(createDate)) {
		return createDate;
	    }
	    else {
		try {
		    SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
		    Date createdDate = dateParser.parse(createDate);
		    return dateFormatter.format(createdDate);
		}
		catch (ParseException e) {
			try {
			    SimpleDateFormat dateWithTimeParser = new SimpleDateFormat(" \tMMM dd, yyyy h:mm a");
			    SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
			    Date createdDate = dateWithTimeParser.parse(createDate);
			    return dateWithTimeFormatter.format(createdDate);
			}
			catch (ParseException e2) {
			    return createDate;
			}
		}
	    }
	case 3:
	    if (isEmpty(closeDate)) {
		return closeDate;
	    }
	    else {
		try {
		    SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
		    Date closingDate = dateParser.parse(closeDate);
		    return dateFormatter.format(closingDate);
		}
		catch (ParseException e) {
			try {
			    SimpleDateFormat dateWithTimeParser = new SimpleDateFormat(" \tMMM dd, yyyy h:mm a");
			    SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMMMM dd, yyyy h:mm a");
			    Date closingDate = dateWithTimeParser.parse(closeDate);
			    return dateWithTimeFormatter.format(closingDate);
			}
			catch (ParseException e2) {
			    return closeDate;
			}
		}
	    }
	case 4:
	    return daysToClose;
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
