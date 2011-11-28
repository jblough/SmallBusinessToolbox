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

public class RecommendedSitesSearchCriteria implements Parcelable {

    private final static String TAG = "RecommendedSitesSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String SEARCH_BY_JSON_ELEMENT = "searcy_by";
    private final static String SEARCH_TERM_JSON_ELEMENT = "search_term";

    public static final int ALL_SITES_INDEX = 0;
    public static final int KEYWORD_SEARCH_INDEX = 1;
    public static final int CATEGORY_SEARCH_INDEX = 2;
    public static final int MASTER_TERM_SEARCH_INDEX = 3;
    public static final int DOMAIN_FILTER_INDEX = 4;
    
    public int searchBy;
    public String searchTerm;
    
    
    public RecommendedSitesSearchCriteria(int searchBy, String searchTerm) {
	this.searchBy = searchBy;
	this.searchTerm = (searchTerm == null) ? null : searchTerm.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(searchBy);
	dest.writeString(searchTerm);
    }

    public static final Parcelable.Creator<RecommendedSitesSearchCriteria> CREATOR = new Parcelable.Creator<RecommendedSitesSearchCriteria>() {
	public RecommendedSitesSearchCriteria createFromParcel(Parcel in) {
	    return new RecommendedSitesSearchCriteria(in);
	}
	public RecommendedSitesSearchCriteria[] newArray(int size) {
            return new RecommendedSitesSearchCriteria[size];
        }
    };
    
    private RecommendedSitesSearchCriteria(Parcel in) {
	searchBy = in.readInt();
	searchTerm = in.readString();
    }
    
    public static Map<String, RecommendedSitesSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, RecommendedSitesSearchCriteria> searches = new HashMap<String, RecommendedSitesSearchCriteria>();
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			int searchBy = jsonSearch.getInt(SEARCH_BY_JSON_ELEMENT);
			String searchTerm = jsonSearch.getString(SEARCH_TERM_JSON_ELEMENT);
			RecommendedSitesSearchCriteria search = new RecommendedSitesSearchCriteria(searchBy, searchTerm);
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
    
    public static String convertToJson(final Map<String, RecommendedSitesSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, RecommendedSitesSearchCriteria> entry : criteria.entrySet()) {
		JSONObject jsonSearch = new JSONObject();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());
		RecommendedSitesSearchCriteria search = entry.getValue();
		jsonSearch.put(SEARCH_BY_JSON_ELEMENT, search.searchBy);
		jsonSearch.put(SEARCH_TERM_JSON_ELEMENT, search.searchTerm);

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
