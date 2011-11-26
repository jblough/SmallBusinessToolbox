package com.josephblough.sbt.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class LicenseAndPermitData implements Bookmarkable {

    private final static String TAG = "LicenseAndPermitData";

    public String title;
    public String description;
    public String url;
    public String state;
    public String category;
    public String county;
    public String businessType;
    public String section;
    public String resourceGroupDescription;
    
    public LicenseAndPermitData(final JSONArray array) throws JSONException {
	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    //Log.d("DATA", "" + json);
	    JSONArray names = json.names();
	    if (names != null && names.length() > 0) {
		String name = names.getString(0);
		if ("title".equals(name)) {
		    title = json.getString("title");
		}
		else if ("description".equals(name)) {
		    description = json.getString("description");
		}
		else if ("url".equals(name)) {
		    url = json.getString("url");
		}
		else if ("state".equals(name)) {
		    state = json.getString("state");
		}
		else if ("category".equals(name)) {
		    category = json.getString("category");
		}
		else if ("county".equals(name)) {
		    county = json.getString("county");
		}
		else if ("business_type".equals(name)) {
		    businessType = json.getString("business_type");
		}
		else if ("section".equals(name)) {
		    section = json.getString("section");
		}
		else if ("resource_group_description".equals(name)) {
		    resourceGroupDescription = json.getString("resource_group_description");
		}
	    }
	}
    }
    
    public LicenseAndPermitData(final JSONObject json) throws JSONException {
	title = json.optString("title");
	description = json.optString("description");
	url = json.optString("url");
	state = json.optString("state");
	category = json.optString("category");
	county = json.optString("county");
	businessType = json.optString("business_type");
	section = json.optString("section");
	resourceGroupDescription = json.optString("resource_group_description");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Description: '" + description + "', " +
        	"Url: '" + url + "', " +
        	"State: '" + state + "', " +
        	"Category: '" + category + "', " +
        	"County: '" + county + "', " +
        	"Business Type: '" + businessType + "', " +
        	"Section: '" + section + "', " +
        	"Resource Group Description: '" + resourceGroupDescription + "'";
    }

    public String getType() {
	return "License/Permit";
    }

    public String getName() {
	if (title != null && !"".equals(title) &&
		!"null".equals(title))
	    return title;
	else if (county != null && !"".equals(county) &&
		!"null".equals(county))
	    return county;
	else if (state != null && !"".equals(state) &&
		!"null".equals(state))
	    return state;
	
	return "";
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
	    if (state != null)
		json.put("state", state);
	    if (category != null)
		json.put("category", category);
	    if (county != null)
		json.put("county", county);
	    if (businessType != null)
		json.put("business_type", businessType);
	    if (section != null)
		json.put("section", section);
	    if (resourceGroupDescription != null)
		json.put("resource_group_description", resourceGroupDescription);
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
		+ ((businessType == null) ? 0 : businessType.hashCode());
	result = prime * result
		+ ((category == null) ? 0 : category.hashCode());
	result = prime * result + ((county == null) ? 0 : county.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime
		* result
		+ ((resourceGroupDescription == null) ? 0
			: resourceGroupDescription.hashCode());
	result = prime * result + ((section == null) ? 0 : section.hashCode());
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
	LicenseAndPermitData other = (LicenseAndPermitData) obj;
	if (businessType == null) {
	    if (other.businessType != null)
		return false;
	} else if (!businessType.equals(other.businessType))
	    return false;
	if (category == null) {
	    if (other.category != null)
		return false;
	} else if (!category.equals(other.category))
	    return false;
	if (county == null) {
	    if (other.county != null)
		return false;
	} else if (!county.equals(other.county))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (resourceGroupDescription == null) {
	    if (other.resourceGroupDescription != null)
		return false;
	} else if (!resourceGroupDescription
		.equals(other.resourceGroupDescription))
	    return false;
	if (section == null) {
	    if (other.section != null)
		return false;
	} else if (!section.equals(other.section))
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
	return 9;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0: // title
	    return "Title:";
	case 1: // description
	    return "Description:";
	case 2: // link
	    return "URL:";
	case 3: // state
	    return "State:";
	case 4: // category
	    return "Category:";
	case 5: // county
	    return "County:";
	case 6: // business type
	    return "Business Type:";
	case 7: // section
	    return "Section:";
	case 8: // resource description
	    return "Description:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	/*
    public String title;
    public String description;
    public String url;
    public String state;
    public String category;
    public String county;
    public String businessType;
    public String section;
    public String resourceGroupDescription;
	 */
	switch (detail) {
	case 0: // title
	    return title;
	case 1: // description
	    return description;
	case 2: // link
	    return null; //url;
	case 3: // state
	    return state;
	case 4: // category
	    return category;
	case 5: // county
	    return county;
	case 6: // business type
	    return businessType;
	case 7: // section
	    return section;
	case 8: // resource group description
	    return resourceGroupDescription;
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
