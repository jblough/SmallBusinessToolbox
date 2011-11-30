package com.josephblough.sbt.criteria;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ProgramFinderSearchCriteria implements Parcelable {

    private final static String TAG = "ProgramFinderSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String TYPE_JSON_ELEMENT = "type";
    private final static String CRITERIA_JSON_ELEMENT = "criteria";

    public static final int TYPE_BY_FEDERAL_INDEX = 0;
    public static final int TYPE_BY_PRIVATE_INDEX = 1;
    public static final int TYPE_BY_NATIONAL_INDEX = 2;
    public static final int TYPE_BY_STATE_INDEX = 3;
    public static final int TYPE_BY_INDUSTRY_INDEX = 4;
    public static final int TYPE_BY_TYPE_INDEX = 5;
    public static final int TYPE_BY_QUALIFICATION_INDEX = 6;
    
    public int type;
    public String criteria;
    
    
    public ProgramFinderSearchCriteria(int type, String criteria) {
	this.type = type;
	
	if (type == TYPE_BY_FEDERAL_INDEX)
	    this.criteria = "Federal";
	else if (type == TYPE_BY_PRIVATE_INDEX)
	    this.criteria = "Private";
	else if (type == TYPE_BY_NATIONAL_INDEX)
	    this.criteria = "National";
	else
	    this.criteria = (criteria == null) ? null : criteria.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(type);
	dest.writeString(criteria);
    }

    public static final Parcelable.Creator<ProgramFinderSearchCriteria> CREATOR = new Parcelable.Creator<ProgramFinderSearchCriteria>() {
	public ProgramFinderSearchCriteria createFromParcel(Parcel in) {
	    return new ProgramFinderSearchCriteria(in);
	}
	public ProgramFinderSearchCriteria[] newArray(int size) {
            return new ProgramFinderSearchCriteria[size];
        }
    };
    
    private ProgramFinderSearchCriteria(Parcel in) {
	type = in.readInt();
	criteria = in.readString();
    }
    
    public static Map<String, ProgramFinderSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, ProgramFinderSearchCriteria> searches = new HashMap<String, ProgramFinderSearchCriteria>();
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			ProgramFinderSearchCriteria search = new ProgramFinderSearchCriteria(jsonSearch);
			searches.put(name, search);
		    }
		}
	    }
	    catch (JSONException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}

	return searches;
    }
    
    public static String convertToJson(final Map<String, ProgramFinderSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, ProgramFinderSearchCriteria> entry : criteria.entrySet()) {
		ProgramFinderSearchCriteria search = entry.getValue();
		JSONObject jsonSearch = search.toJson();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());

		jsonSearches.put(jsonSearch);
	    }
	    json.put(SEARCHES_JSON_ARRAY, jsonSearches);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json.toString();
    }
    
    public ProgramFinderSearchCriteria(final String jsonString) throws JSONException {
	this(new JSONObject(jsonString));
    }
    
    public ProgramFinderSearchCriteria(final JSONObject json) {
	try {
	    type = json.getInt(TYPE_JSON_ELEMENT);
	    criteria = json.getString(CRITERIA_JSON_ELEMENT);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }
    
    public JSONObject toJson() {
	JSONObject json = new JSONObject();
	try {
	    json.put(TYPE_JSON_ELEMENT, type);
	    json.put(CRITERIA_JSON_ELEMENT, criteria);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json;
    }
}
