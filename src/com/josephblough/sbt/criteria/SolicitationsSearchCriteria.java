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

public class SolicitationsSearchCriteria implements Parcelable {

    private final static String TAG = "SolicitationsSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String KEYWORD_JSON_ELEMENT = "keyword";
    private final static String AGENCY_JSON_ELEMENT = "agency";
    private final static String FILTER_JSON_ELEMENT = "filter";

    public static final int SHOW_ALL_INDEX = 0;
    public static final int SHOW_OPEN_INDEX = 1;
    public static final int SHOW_CLOSED_INDEX = 2;
    
    public String keyword;
    public String agency;
    public int filter;
    
    
    public SolicitationsSearchCriteria(String keyword, String agency, int filter) {
	this.keyword = (keyword == null) ? null : keyword.trim();
	this.agency = (agency == null) ? null : agency.trim();
	this.filter = filter;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(keyword == null ? "" : keyword);
	dest.writeString(agency == null ? "" : agency);
	dest.writeInt(filter);
    }

    public static final Parcelable.Creator<SolicitationsSearchCriteria> CREATOR = new Parcelable.Creator<SolicitationsSearchCriteria>() {
	public SolicitationsSearchCriteria createFromParcel(Parcel in) {
	    return new SolicitationsSearchCriteria(in);
	}
	public SolicitationsSearchCriteria[] newArray(int size) {
            return new SolicitationsSearchCriteria[size];
        }
    };
    
    private SolicitationsSearchCriteria(Parcel in) {
	keyword = in.readString();
	agency = in.readString();
	filter = in.readInt();
    }
    
    public static Map<String, SolicitationsSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, SolicitationsSearchCriteria> searches = new HashMap<String, SolicitationsSearchCriteria>();
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			SolicitationsSearchCriteria search = new SolicitationsSearchCriteria(jsonSearch);
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
    
    public static String convertToJson(final Map<String, SolicitationsSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, SolicitationsSearchCriteria> entry : criteria.entrySet()) {
		SolicitationsSearchCriteria search = entry.getValue();
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
    
    public SolicitationsSearchCriteria(final String jsonString) throws JSONException {
	this(new JSONObject(jsonString));
    }
    
    public SolicitationsSearchCriteria(final JSONObject json) {
	try {
	    keyword = json.getString(KEYWORD_JSON_ELEMENT);
	    agency = json.optString(AGENCY_JSON_ELEMENT);
	    if ("".equals(agency))
		agency = null;
	    filter = json.getInt(FILTER_JSON_ELEMENT);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }
    
    public JSONObject toJson() {
	JSONObject json = new JSONObject();
	try {
	    json.put(KEYWORD_JSON_ELEMENT, keyword);
	    json.put(AGENCY_JSON_ELEMENT, (agency == null) ? "" : agency);
	    json.put(FILTER_JSON_ELEMENT, filter);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json;
    }
}
