package com.josephblough.sbt.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class LoansAndGrantsSearchCriteria implements Parcelable {

    private final static String TAG = "LoansAndGrantsSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String INCLUDE_FEDERAL_JSON_ELEMENT = "include_federal";
    private final static String INCLUDE_STATE_JSON_ELEMENT = "include_state";
    private final static String STATE_JSON_ELEMENT = "state";
    private final static String FILTER_BY_INDUSTRY_JSON_ELEMENT = "by_industry";
    private final static String INDUSTRY_JSON_ELEMENT = "industry";
    private final static String FILTER_BY_SPECIALTY_JSON_ELEMENT = "by_specialty";
    private final static String SPECIALTIES_JSON_ARRAY = "specialties";

    public boolean includeFederal;
    public boolean includeState;
    public String state;
    public boolean filterByIndustry;
    public String industry;
    public boolean filterBySpecialty;
    public List<String> specialties;
    
    
    public LoansAndGrantsSearchCriteria(boolean includeFederal, boolean includeState, String state, 
	    boolean filterByIndustry, String industry, boolean filteryBySpecialty, List<String> specialties) {
	this.includeFederal = includeFederal;
	this.includeState = includeState;
	this.state = (state == null) ? null : state.trim();
	this.filterByIndustry = filterByIndustry;
	this.industry = (industry == null) ? null : industry.trim();
	this.filterBySpecialty = filteryBySpecialty;
	this.specialties = specialties;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(includeFederal ? 1 : 0);
	dest.writeInt(includeState ? 1 : 0);
	dest.writeString(state);
	dest.writeInt(filterByIndustry ? 1 : 0);
	dest.writeString(industry);
	dest.writeInt(filterBySpecialty ? 1 : 0);
	dest.writeInt(specialties == null ? 0 : specialties.size());
	if (specialties != null) {
	    for (String speciality : specialties) {
		dest.writeString(speciality);
	    }
	}
    }

    public static final Parcelable.Creator<LoansAndGrantsSearchCriteria> CREATOR = new Parcelable.Creator<LoansAndGrantsSearchCriteria>() {
	public LoansAndGrantsSearchCriteria createFromParcel(Parcel in) {
	    return new LoansAndGrantsSearchCriteria(in);
	}
	public LoansAndGrantsSearchCriteria[] newArray(int size) {
            return new LoansAndGrantsSearchCriteria[size];
        }
    };
    
    private LoansAndGrantsSearchCriteria(Parcel in) {
	includeFederal = (in.readInt() == 1);
	includeState = (in.readInt() == 1);
	state = in.readString();
	filterByIndustry = (in.readInt() == 1);
	industry = in.readString();
	filterBySpecialty = (in.readInt() == 1);
	int specialtyCount = in.readInt();
	specialties = new ArrayList<String>();
	for (int i=0; i<specialtyCount; i++) {
	    specialties.add(in.readString());
	}
    }

    public static Map<String, LoansAndGrantsSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, LoansAndGrantsSearchCriteria> searches = new HashMap<String, LoansAndGrantsSearchCriteria>();
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			LoansAndGrantsSearchCriteria search = new LoansAndGrantsSearchCriteria(jsonSearch);
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
    
    public static String convertToJson(final Map<String, LoansAndGrantsSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, LoansAndGrantsSearchCriteria> entry : criteria.entrySet()) {
		LoansAndGrantsSearchCriteria search = entry.getValue();
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

    public LoansAndGrantsSearchCriteria(final String json) throws JSONException {
	this(new JSONObject(json));
    }
    
    public LoansAndGrantsSearchCriteria(final JSONObject json) {
	try {
	    includeFederal = json.getBoolean(INCLUDE_FEDERAL_JSON_ELEMENT);
	    includeState = json.getBoolean(INCLUDE_STATE_JSON_ELEMENT);
	    state = json.getString(STATE_JSON_ELEMENT);
	    filterByIndustry = json.getBoolean(FILTER_BY_INDUSTRY_JSON_ELEMENT);
	    industry = json.getString(INDUSTRY_JSON_ELEMENT);
	    filterBySpecialty = json.getBoolean(FILTER_BY_SPECIALTY_JSON_ELEMENT);
	    JSONArray jsonSpecialties = json.getJSONArray(SPECIALTIES_JSON_ARRAY);
	    specialties = new ArrayList<String>();
	    for (int j=0; j<jsonSpecialties.length(); j++) {
		specialties.add(jsonSpecialties.getString(j));
	    }
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }

    public JSONObject toJson() {
	JSONObject json = new JSONObject();
	try {
	    json.put(INCLUDE_FEDERAL_JSON_ELEMENT, includeFederal);
	    json.put(INCLUDE_STATE_JSON_ELEMENT, includeState);
	    json.put(STATE_JSON_ELEMENT, state);
	    json.put(FILTER_BY_INDUSTRY_JSON_ELEMENT, filterByIndustry);
	    json.put(INDUSTRY_JSON_ELEMENT, industry);
	    json.put(FILTER_BY_SPECIALTY_JSON_ELEMENT, filterBySpecialty);
	    JSONArray jsonSpecialties = new JSONArray();
	    for (String specialty : specialties) {
		jsonSpecialties.put(specialty);
	    }
	    json.put(SPECIALTIES_JSON_ARRAY, jsonSpecialties);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return json;
    }
}
