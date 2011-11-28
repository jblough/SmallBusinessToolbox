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

public class LicensesAndPermitsSearchCriteria implements Parcelable {

    private final static String TAG = "LicensesAndPermitsSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String SEARCH_BY_JSON_ELEMENT = "search_by";
    private final static String STATE_JSON_ELEMENT = "state";
    private final static String CATEGORY_JSON_ELEMENT = "category";
    private final static String BUSINESS_TYPE_JSON_ELEMENT = "business_type";
    private final static String BUSINESS_TYPE_SUBFILTER_JSON_ELEMENT = "business_type_subfilter";
    private final static String BUSINESS_TYPE_SUBFILTER_LOCALITY_JSON_ELEMENT = "business_type_subfilter_locality";

    public static final int CATEGORY_INDEX = 0;
    public static final int STATE_INDEX = 1;
    public static final int BUSINESS_TYPE_INDEX = 2;
    
    public static final int NO_SUBFILTER_INDEX = 0;
    public static final int STATE_SUBFILTER_INDEX = 1;
    public static final int COUNTY_SUBFILTER_INDEX = 2;
    public static final int CITY_SUBFILTER_INDEX = 3;
    public static final int ZIP_CODE_SUBFILTER_INDEX = 4;
    
    public int searchBy;
    public String state;
    public String category;
    public String businessType;
    public int businessTypeSubfilter;
    public String businessTypeSubfilterLocality;
    
    public LicensesAndPermitsSearchCriteria(int searchBy, String state, String category, String businessType, int businessTypeSubfilter, String businessTypeSubfilterLocality) {
	this.searchBy = searchBy;
	this.state = (state == null) ? null : state.trim();
	this.category = (category == null) ? null : category.trim();
	this.businessType = (businessType == null) ? null : businessType.trim();
	this.businessTypeSubfilter = businessTypeSubfilter;
	this.businessTypeSubfilterLocality = (businessTypeSubfilterLocality == null) ? null : 
	    businessTypeSubfilterLocality.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(searchBy);
	dest.writeString(state);
	dest.writeString(category);
	dest.writeString(businessType);
	dest.writeInt(businessTypeSubfilter);
	dest.writeString(businessTypeSubfilterLocality);
    }

    public static final Parcelable.Creator<LicensesAndPermitsSearchCriteria> CREATOR = new Parcelable.Creator<LicensesAndPermitsSearchCriteria>() {
	public LicensesAndPermitsSearchCriteria createFromParcel(Parcel in) {
	    return new LicensesAndPermitsSearchCriteria(in);
	}
	public LicensesAndPermitsSearchCriteria[] newArray(int size) {
            return new LicensesAndPermitsSearchCriteria[size];
        }
    };
    
    private LicensesAndPermitsSearchCriteria(Parcel in) {
	searchBy = in.readInt();
	state = in.readString();
	category = in.readString();
	businessType = in.readString();
	businessTypeSubfilter = in.readInt();
	businessTypeSubfilterLocality = in.readString();
    }
    
    public static Map<String, LicensesAndPermitsSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, LicensesAndPermitsSearchCriteria> searches = new HashMap<String, LicensesAndPermitsSearchCriteria>();
	
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
			String state = jsonSearch.getString(STATE_JSON_ELEMENT);
			String category = jsonSearch.getString(CATEGORY_JSON_ELEMENT);
			String businessType = jsonSearch.getString(BUSINESS_TYPE_JSON_ELEMENT);
			int businessTypeSubfilter = jsonSearch.getInt(BUSINESS_TYPE_SUBFILTER_JSON_ELEMENT);
			String businessTypeSubfilterLocality = jsonSearch.getString(BUSINESS_TYPE_SUBFILTER_LOCALITY_JSON_ELEMENT);
			LicensesAndPermitsSearchCriteria search = new LicensesAndPermitsSearchCriteria(searchBy, state, category, businessType, businessTypeSubfilter, businessTypeSubfilterLocality);
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
    
    public static String convertToJson(final Map<String, LicensesAndPermitsSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, LicensesAndPermitsSearchCriteria> entry : criteria.entrySet()) {
		JSONObject jsonSearch = new JSONObject();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());
		LicensesAndPermitsSearchCriteria search = entry.getValue();
		jsonSearch.put(SEARCH_BY_JSON_ELEMENT, search.searchBy);
		jsonSearch.put(STATE_JSON_ELEMENT, search.state);
		jsonSearch.put(CATEGORY_JSON_ELEMENT, search.category);
		jsonSearch.put(BUSINESS_TYPE_JSON_ELEMENT, search.businessType);
		jsonSearch.put(BUSINESS_TYPE_SUBFILTER_JSON_ELEMENT, search.businessTypeSubfilter);
		jsonSearch.put(BUSINESS_TYPE_SUBFILTER_LOCALITY_JSON_ELEMENT, search.businessTypeSubfilterLocality);

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
