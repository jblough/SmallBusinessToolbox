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

public class AwardsSearchCriteria implements Parcelable {

    private final static String TAG = "AwardsSearchCriteria";
    
    private final static String SEARCHES_JSON_ARRAY = "searches";
    private final static String NAME_JSON_ELEMENT = "name";
    private final static String DOWLOAD_ALL_JSON_ELEMENT = "download_all";
    private final static String SEARCH_TERM_JSON_ELEMENT = "search_term";
    private final static String AGENCY_JSON_ELEMENT = "agency";
    private final static String COMPANY_JSON_ELEMENT = "company";
    private final static String INSTITUTION_JSON_ELEMENT = "institution";
    private final static String YEAR_JSON_ELEMENT = "year";
    
    public boolean downloadAll;
    public String searchTerm;
    public String agency;
    public String company;
    public String institution;
    public int year;
    
    
    public AwardsSearchCriteria(boolean downloadAll, String searchTerm, String agency, String company, String institution, int year) {
	this.downloadAll = downloadAll;
	this.searchTerm = (searchTerm == null) ? null : searchTerm.trim();
	this.agency = (agency == null) ? null : agency.trim();
	this.company = (company == null) ? null : company.trim();
	this.institution = (institution == null) ? null : institution.trim();
	this.year = year;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(downloadAll ? 1 : 0);
	dest.writeString(searchTerm == null ? "" : searchTerm);
	dest.writeString(agency == null ? "" : agency);
	dest.writeString(company == null ? "" : company);
	dest.writeString(institution == null ? "" : institution);
	dest.writeInt(year);
    }

    public static final Parcelable.Creator<AwardsSearchCriteria> CREATOR = new Parcelable.Creator<AwardsSearchCriteria>() {
	public AwardsSearchCriteria createFromParcel(Parcel in) {
	    return new AwardsSearchCriteria(in);
	}
	public AwardsSearchCriteria[] newArray(int size) {
            return new AwardsSearchCriteria[size];
        }
    };
    
    private AwardsSearchCriteria(Parcel in) {
	downloadAll = in.readInt() == 1;
	searchTerm = in.readString();
	agency = in.readString();
	company = in.readString();
	institution = in.readString();
	year = in.readInt();
    }
    
    public static Map<String, AwardsSearchCriteria> convertFromJson(final String jsonString) {
	Map<String, AwardsSearchCriteria> searches = new HashMap<String, AwardsSearchCriteria>();
	
	if (jsonString != null && !"".equals(jsonString)) {
	    try {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsonSearches = json.optJSONArray(SEARCHES_JSON_ARRAY);
		if (jsonSearches != null) {
		    int length = jsonSearches.length();
		    for (int i=0; i<length; i++) {
			JSONObject jsonSearch = jsonSearches.getJSONObject(i);
			boolean downloadAll = jsonSearch.getBoolean(DOWLOAD_ALL_JSON_ELEMENT);
			String name = jsonSearch.getString(NAME_JSON_ELEMENT);
			String searchTerm = jsonSearch.getString(SEARCH_TERM_JSON_ELEMENT);
			String agency = jsonSearch.getString(AGENCY_JSON_ELEMENT);
			String company = jsonSearch.getString(COMPANY_JSON_ELEMENT);
			String institution = jsonSearch.getString(INSTITUTION_JSON_ELEMENT);
			int year = jsonSearch.optInt(YEAR_JSON_ELEMENT, 0);
			AwardsSearchCriteria search = new AwardsSearchCriteria(downloadAll, searchTerm, agency, company, institution, year);
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
    
    public static String convertToJson(final Map<String, AwardsSearchCriteria> criteria) {
	JSONObject json = new JSONObject();
	try {
	    JSONArray jsonSearches = new JSONArray();
	    for (Entry<String, AwardsSearchCriteria> entry : criteria.entrySet()) {
		JSONObject jsonSearch = new JSONObject();
		jsonSearch.put(NAME_JSON_ELEMENT, entry.getKey());
		AwardsSearchCriteria search = entry.getValue();
		jsonSearch.put(DOWLOAD_ALL_JSON_ELEMENT, search.downloadAll);
		jsonSearch.put(SEARCH_TERM_JSON_ELEMENT, search.searchTerm);
		jsonSearch.put(AGENCY_JSON_ELEMENT, search.agency);
		jsonSearch.put(COMPANY_JSON_ELEMENT, search.company);
		jsonSearch.put(INSTITUTION_JSON_ELEMENT, search.institution);
		jsonSearch.put(YEAR_JSON_ELEMENT, search.year);

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
