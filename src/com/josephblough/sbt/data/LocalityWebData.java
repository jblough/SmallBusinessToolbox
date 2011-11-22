package com.josephblough.sbt.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class LocalityWebData implements Bookmarkable {

    private final static String TAG = "LocalityWebData";

    public String name;
    public String fipsCountyCode;
    public String fipsClass;
    public String featClass;
    public String countyName;
    public String latitude;
    public String longitude;
    public String stateName;
    public String stateAbbreviation;
    public String url;
    public String description;
    public String title;
    public String featureId;
    public String fullCountyName;
    
    public LocalityWebData(final JSONObject json) throws JSONException {
	name = json.optString("name");
	fipsCountyCode = json.optString("fips_county_cd");
	fipsClass = json.optString("fips_class");
	featClass = json.optString("feat_class");
	countyName = json.optString("county_name");
	latitude = json.optString("primary_latitude");
	longitude = json.optString("primary_longitude");
	stateName = json.optString("state_name");
	stateAbbreviation = json.optString("state_abbreviation");
	url = json.optString("url");
	description = json.optString("description");
	title = json.optString("link_title");
	featureId = json.optString("feature_id");
	fullCountyName = json.optString("full_county_name");
    }

    @Override
    public String toString() {
        return "Name: '" + name + "', " +
        	"FIPS County Code: '" + fipsCountyCode + "', " +
        	"FIPS Class: '" + fipsClass + "', " +
        	"Feat Class: '" + featClass + "', " +
        	"County Name: '" + countyName + "', " +
        	"Latitude: '" + latitude + "', " +
        	"Longitude: '" + longitude + "', " +
        	"State Name: '" + stateName + "', " +
        	"State Abbreviation: '" + stateAbbreviation + "', " +
        	"Url: '" + url + "', " +
        	"Description: '" + description + "', " +
        	"Title: '" + title + "', " +
        	"Feature ID: '" + featureId + "', " +
        	"Full County Name: '" + fullCountyName + "'";
    }

    public String getType() {
	return "Locality Web Data";
    }

    public String getName() {
	return name;
    }

    public String getUrl() {
	return url;
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("link_title", title);
	    if (description != null)
		json.put("description", description);
	    if (url != null)
		json.put("url", url);
	    if (name != null)
		json.put("name", name);
	    if (fipsCountyCode != null)
		json.put("fips_county_cd", fipsCountyCode);
	    if (fipsClass != null)
		json.put("fips_class", fipsClass);
	    if (featClass != null)
		json.put("feat_class", featClass);
	    if (countyName != null)
		json.put("county_name", countyName);
	    if (url != null)
		json.put("primary_latitude", latitude);
	    if (longitude != null)
		json.put("primary_longitude", longitude);
	    if (stateName != null)
		json.put("state_name", stateName);
	    if (stateAbbreviation != null)
		json.put("state_abbreviation", stateAbbreviation);
	    if (featureId != null)
		json.put("feature_id", featureId);
	    if (fullCountyName != null)
		json.put("full_county_name", fullCountyName);
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
		+ ((countyName == null) ? 0 : countyName.hashCode());
	result = prime * result
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result
		+ ((featClass == null) ? 0 : featClass.hashCode());
	result = prime * result
		+ ((featureId == null) ? 0 : featureId.hashCode());
	result = prime * result
		+ ((fipsClass == null) ? 0 : fipsClass.hashCode());
	result = prime * result
		+ ((fipsCountyCode == null) ? 0 : fipsCountyCode.hashCode());
	result = prime * result
		+ ((fullCountyName == null) ? 0 : fullCountyName.hashCode());
	result = prime * result
		+ ((latitude == null) ? 0 : latitude.hashCode());
	result = prime * result
		+ ((longitude == null) ? 0 : longitude.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime
		* result
		+ ((stateAbbreviation == null) ? 0 : stateAbbreviation
			.hashCode());
	result = prime * result
		+ ((stateName == null) ? 0 : stateName.hashCode());
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
	LocalityWebData other = (LocalityWebData) obj;
	if (countyName == null) {
	    if (other.countyName != null)
		return false;
	} else if (!countyName.equals(other.countyName))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (featClass == null) {
	    if (other.featClass != null)
		return false;
	} else if (!featClass.equals(other.featClass))
	    return false;
	if (featureId == null) {
	    if (other.featureId != null)
		return false;
	} else if (!featureId.equals(other.featureId))
	    return false;
	if (fipsClass == null) {
	    if (other.fipsClass != null)
		return false;
	} else if (!fipsClass.equals(other.fipsClass))
	    return false;
	if (fipsCountyCode == null) {
	    if (other.fipsCountyCode != null)
		return false;
	} else if (!fipsCountyCode.equals(other.fipsCountyCode))
	    return false;
	if (fullCountyName == null) {
	    if (other.fullCountyName != null)
		return false;
	} else if (!fullCountyName.equals(other.fullCountyName))
	    return false;
	if (latitude == null) {
	    if (other.latitude != null)
		return false;
	} else if (!latitude.equals(other.latitude))
	    return false;
	if (longitude == null) {
	    if (other.longitude != null)
		return false;
	} else if (!longitude.equals(other.longitude))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (stateAbbreviation == null) {
	    if (other.stateAbbreviation != null)
		return false;
	} else if (!stateAbbreviation.equals(other.stateAbbreviation))
	    return false;
	if (stateName == null) {
	    if (other.stateName != null)
		return false;
	} else if (!stateName.equals(other.stateName))
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
	return 8;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "Name:";
	case 2:
	    return "Description:";
	case 3:
	    return "URL:";
	case 4:
	    return "County:";
	case 5:
	    return "Location:";
	case 6:
	    return "Feat Class:";
	case 7:
	    return "Fips Class:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0:
	    return title;
	case 1:
	    return name;
	case 2:
	    return description;
	case 3:
	    return null; //url
	case 4:
	    return countyName;
	case 5:
	    return (!isEmpty(stateName)) ? stateName : stateAbbreviation;
	case 6:
	    return featClass;
	case 7:
	    return fipsClass;
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
