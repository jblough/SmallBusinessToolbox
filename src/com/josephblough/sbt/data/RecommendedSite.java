package com.josephblough.sbt.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class RecommendedSite implements Bookmarkable {

    private final static String TAG = "RecommendedSite";

    /*
Recommended Sites Categories:
   managing a business
   financing a business
   registering a business
   starting a business
   business law
   other

Recommended Site Descriptions:
   Grant
   Loan
   Venture Capital
   Tax Incentive
     */
    
    public String title;
    public String url;
    public String description;
    public String keywords;
    public String category;
    public String orders;
    public String masterTerm;
    
    public RecommendedSite(final JSONArray array) throws JSONException {
	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    //Log.d("DATA", "" + json);
	    JSONArray names = json.names();
	    if (names != null && names.length() > 0) {
		String name = names.getString(0);
		if ("title".equals(name) || "link_title".equals(name)) {
		    title = json.getString(name);
		}
		else if ("description".equals(name)) {
		    description = json.getString("description");
		}
		else if ("url".equals(name)) {
		    url = json.getString("url");
		}
		else if ("keywords".equals(name)) {
		    keywords = json.getString("keywords");
		}
		else if ("category".equals(name)) {
		    category = json.getString("category");
		}
		else if ("orders".equals(name)) {
		    orders = json.getString("orders");
		}
		else if ("master_term".equals(name)) {
		    masterTerm = json.getString("master_term");
		}
	    }
	}
    }

    public RecommendedSite(final JSONObject json) throws JSONException {
	if (json.has("title"))
	    title = json.optString("title");
	else if (json.has("link_title"))
	    title = json.optString("linke_title");

	description = json.optString("description");
	url = json.optString("url");
	keywords = json.optString("keywords");
	category = json.optString("category");
	orders = json.optString("orders");
	masterTerm = json.optString("master_term");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Url: '" + url + "', " +
        	"Description: '" + description + "', " +
        	"Keywords: '" + keywords + "', " +
        	"Category: '" + category + "', " +
        	"Orders: '" + orders + "', " +
        	"Master Term: '" + masterTerm + "'";
    }

    public String getType() {
	return "Recommended Site";
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
	    if (keywords != null)
		json.put("keywords", keywords);
	    if (category != null)
		json.put("category", category);
	    if (orders != null)
		json.put("orders", orders);
	    if (masterTerm != null)
		json.put("master_term", masterTerm);
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
		+ ((category == null) ? 0 : category.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result
		+ ((keywords == null) ? 0 : keywords.hashCode());
	result = prime * result
		+ ((masterTerm == null) ? 0 : masterTerm.hashCode());
	result = prime * result + ((orders == null) ? 0 : orders.hashCode());
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
	RecommendedSite other = (RecommendedSite) obj;
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
	if (keywords == null) {
	    if (other.keywords != null)
		return false;
	} else if (!keywords.equals(other.keywords))
	    return false;
	if (masterTerm == null) {
	    if (other.masterTerm != null)
		return false;
	} else if (!masterTerm.equals(other.masterTerm))
	    return false;
	if (orders == null) {
	    if (other.orders != null)
		return false;
	} else if (!orders.equals(other.orders))
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
	return 7;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	/*
    public String title;
    public String description;
    public String url;
    public String keywords;
    public String category;
    public String orders;
    public String masterTerm;
	 */
	switch (detail) {
	case 0:
	    return "Title:";
	case 2:
	    return "Description:";
	case 3:
	    return "URL:";
	case 4:
	    return "Keywords:";
	case 5:
	    return "Category:";
	case 6:
	    return "Orders:";
	case 7:
	    return "Master Term:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0:
	    return title;
	case 2:
	    return description;
	case 3:
	    return null; // url
	case 4:
	    return keywords;
	case 5:
	    return category;
	case 6:
	    return orders;
	case 7:
	    return masterTerm;
	};
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
}
