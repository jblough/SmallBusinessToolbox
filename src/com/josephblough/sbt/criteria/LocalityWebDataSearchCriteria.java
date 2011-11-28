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

public class LocalityWebDataSearchCriteria implements Parcelable {

    private final static String TAG = "LocalityWebDataSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String TYPE_JSON_ELEMENT = "type";
    private final static String SCOPE_JSON_ELEMENT = "scope";
    private final static String STATE_JSON_ELEMENT = "state";
    private final static String LOCALITY_JSON_ELEMENT = "locality";

    public static final int TYPE_ALL_URLS_INDEX = 0;
    public static final int TYPE_PRIMARY_URLS_INDEX = 1;
    public static final int TYPE_ALL_DATA_INDEX = 2;
    
    
    public static final int SCOPE_ALL_CITIES_AND_COUNTIES_INDEX = 0;
    public static final int SCOPE_ALL_CITIES_INDEX = 1;
    public static final int SCOPE_ALL_COUNTIES_INDEX = 2;
    public static final int SCOPE_SPECIFIC_CITY_INDEX = 3;
    public static final int SCOPE_SPECIFIC_COUNTY_DATA_INDEX = 4;
    
    public int type;
    public int scope;
    public String state;
    public String locality;
    
    
    public LocalityWebDataSearchCriteria(int type, int scope, String state, String locality) {
	this.type = type;
	this.scope = scope;
	this.state = (state == null) ? null : state.trim();
	this.locality = (locality == null) ? null : locality.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(type);
	dest.writeInt(scope);
	dest.writeString(state);
	dest.writeString(locality);
    }

    public static final Parcelable.Creator<LocalityWebDataSearchCriteria> CREATOR = new Parcelable.Creator<LocalityWebDataSearchCriteria>() {
	public LocalityWebDataSearchCriteria createFromParcel(Parcel in) {
	    return new LocalityWebDataSearchCriteria(in);
	}
	public LocalityWebDataSearchCriteria[] newArray(int size) {
            return new LocalityWebDataSearchCriteria[size];
        }
    };
    
    private LocalityWebDataSearchCriteria(Parcel in) {
	type = in.readInt();
	scope = in.readInt();
	state = in.readString();
	locality = in.readString();
    }
    
    public static Map<String, LocalityWebDataSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, LocalityWebDataSearchCriteria> searches = new HashMap<String, LocalityWebDataSearchCriteria>();
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			int type = jsonSearch.getInt(TYPE_JSON_ELEMENT);
			int scope = jsonSearch.getInt(SCOPE_JSON_ELEMENT);
			String state = jsonSearch.getString(STATE_JSON_ELEMENT);
			String locality = jsonSearch.getString(LOCALITY_JSON_ELEMENT);
			LocalityWebDataSearchCriteria search = new LocalityWebDataSearchCriteria(type, scope, state, locality);
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
    
    public static String convertToJson(final Map<String, LocalityWebDataSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, LocalityWebDataSearchCriteria> entry : criteria.entrySet()) {
		JSONObject jsonSearch = new JSONObject();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());
		LocalityWebDataSearchCriteria search = entry.getValue();
		jsonSearch.put(TYPE_JSON_ELEMENT, search.type);
		jsonSearch.put(SCOPE_JSON_ELEMENT, search.scope);
		jsonSearch.put(STATE_JSON_ELEMENT, search.state);
		jsonSearch.put(LOCALITY_JSON_ELEMENT, search.locality);

		jsonSearches.put(jsonSearch);
	    }
	    json.put(SEARCHES_JSON_ARRAY, jsonSearches);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json.toString();
    }
}
