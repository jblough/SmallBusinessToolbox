package com.josephblough.sbt.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class Solicitation implements Bookmarkable {

    private final static String TAG = "Solicitation";

    /*
Solicitation Agencies:
   DOD = Dept. of Defense
   HHS = Dept. of Health and Human Services
   NASA = National Aeronautics and Space Administration
   NSF = National Science Foundation
   DOE = Dept. of Energy
   USDA = United States Dept. of Agriculture
   EPA = Environmental Protection Agency
   DOC = Dept. of Commerce
   ED = Dept. of Education
   DOT = Dept. of Transportation
   DHS = Dept. of Homeland Security
     */
    
    public String title;
    public String link;
    public String description;
    public String agency;
    public String status;
    public String closeDate;
    
    public Solicitation(final JSONObject json) {
	title = json.optString("title");
	link = json.optString("link");
	description = json.optString("description");
	agency = json.optString("agency");
	status = json.optString("status");
	closeDate = json.optString("close_date");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Link: '" + link + "', " +
        	"Description: '" + description + "', " +
        	"Agency: '" + agency + "', " +
        	"Status: '" + status + "'" +
        	"Close date: " + closeDate;
    }

    public String getType() {
	return "Solicitation";
    }

    public String getName() {
	return title;
    }

    public String getUrl() {
	return link;
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("title", title);
	    if (description != null)
		json.put("description", description);
	    if (link != null)
		json.put("link", link);
	    if (status != null)
		json.put("status", status);
	    if (agency != null)
		json.put("agency", agency);
	    if (closeDate != null)
		json.put("close_date", closeDate);
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
		+ ((closeDate == null) ? 0 : closeDate.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((link == null) ? 0 : link.hashCode());
	result = prime * result + ((status == null) ? 0 : status.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
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
	Solicitation other = (Solicitation) obj;
	if (agency == null) {
	    if (other.agency != null)
		return false;
	} else if (!agency.equals(other.agency))
	    return false;
	if (closeDate == null) {
	    if (other.closeDate != null)
		return false;
	} else if (!closeDate.equals(other.closeDate))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (link == null) {
	    if (other.link != null)
		return false;
	} else if (!link.equals(other.link))
	    return false;
	if (status == null) {
	    if (other.status != null)
		return false;
	} else if (!status.equals(other.status))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
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
    public String link;
    public String description;
    public String agency;
    public String status;
    public String closeDate;
	 */
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "Description:";
	case 2:
	    return "URL:";
	case 3:
	    return "Agency:";
	case 4:
	    return "Status:";
	case 5:
	    return "Close Date:";
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
	    return agency;
	case 4:
	    return status;
	case 5:
	    if (isEmpty(closeDate)) {
		return closeDate;
	    }
	    else {
		try {
		    SimpleDateFormat closeDateParser = new SimpleDateFormat("yyyyMMdd");
		    SimpleDateFormat closeDateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
		    Date closingDate = closeDateParser.parse(closeDate);
		    return closeDateFormatter.format(closingDate);
		}
		catch (ParseException e) {
		    return closeDate;
		}
	    }
	};
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
    
    public String formatForSharing() {
	return link;
    }
}
