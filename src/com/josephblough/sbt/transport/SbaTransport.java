package com.josephblough.sbt.transport;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.data.LicenseAndPermitData;
import com.josephblough.sbt.data.LicenseAndPermitDataCollection;
import com.josephblough.sbt.data.LoanAndGrantData;
import com.josephblough.sbt.data.LocalityWebData;
import com.josephblough.sbt.data.RecommendedSite;

import android.util.Log;

public class SbaTransport {

    public static final String TAG = "SbaTransport";
    
    public static final String BASE_SBA_API_HOST = "http://api.sba.gov/";
    
    public static final String LICENSE_AND_PERMIT_BASE_URL = BASE_SBA_API_HOST + "license_permit/";
    public static final String LOANS_AND_GRANTS_BASE_URL = BASE_SBA_API_HOST + "loans_grants/";
    public static final String RECOMMENDED_SITES_BASE_URL = BASE_SBA_API_HOST + "rec_sites/";
    public static final String WEB_DATA_BASE_URL = BASE_SBA_API_HOST + "geodata/";
    
    // License and permit data can be retrieved by:
    //	category
    //	state
    //	business type
    //	business type, state, county
    //	business type, state, city
    //	business type, zip code
    //	business type, state
    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByCategory(final String category) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "by_category/" + formatForUrl(category) + ".json";
	return getLicenseAndPermitData(url);
    }

    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByState(final String stateAbbreviation) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "all_by_state/" + stateAbbreviation.toLowerCase() + ".json";
	return getLicenseAndPermitData(url);
    }
    
    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByBusinessType(final String businessType) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "by_business_type/" + formatForUrl(businessType) + ".json";
	return getLicenseAndPermitData(url);
    }

    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByBusinessTypeAndState(final String businessType, final String stateAbbreviation) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "state_only/" + formatForUrl(businessType) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getLicenseAndPermitData(url);
    }

    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByBusinessTypeStateAndCounty(final String businessType, final String stateAbbreviation, final String county) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "state_and_county/" + formatForUrl(businessType) + "/" + stateAbbreviation.toLowerCase() + "/" + formatForUrl(county) + ".json";
	return getLicenseAndPermitData(url);
    }

    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByBusinessTypeStateAndCity(final String businessType, final String stateAbbreviation, final String city) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "state_and_city/" + formatForUrl(businessType) + "/" + stateAbbreviation.toLowerCase() + "/" + formatForUrl(city) + ".json";
	return getLicenseAndPermitData(url);
    }

    public static LicenseAndPermitDataCollection getLicenseAndPermitDataByBusinessTypeAndZipCode(final String businessType, final String zipCode) {
	final String url = LICENSE_AND_PERMIT_BASE_URL + "by_zip/" + formatForUrl(businessType) + "/" + zipCode + ".json";
	return getLicenseAndPermitData(url);
    }
    
    private static LicenseAndPermitDataCollection getLicenseAndPermitData(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    
	    return processLicenseAndPermitData(json);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static LicenseAndPermitDataCollection processLicenseAndPermitData(final JSONObject json) {
	LicenseAndPermitDataCollection data = new LicenseAndPermitDataCollection();

	// Categories
	JSONArray categories = json.optJSONArray("sites_for_category");
	if (categories != null) {
	    int count = categories.length();
	    for (int i=0; i<count; i++) {
		try {
		    data.addCategory(new LicenseAndPermitData(categories.getJSONObject(i)));
		}
		catch (JSONException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }
	}

	// States
	JSONArray states = json.optJSONArray("state_sites");
	if (states != null) {
	    int count = states.length();
	    for (int i=0; i<count; i++) {
		try {
		    data.addState(new LicenseAndPermitData(states.getJSONObject(i)));
		}
		catch (JSONException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }
	}

	// Counties
	JSONArray counties = json.optJSONArray("county_sites");
	if (counties != null) {
	    int count = counties.length();
	    for (int i=0; i<count; i++) {
		try {
		    data.addCounty(new LicenseAndPermitData(counties.getJSONObject(i)));
		}
		catch (JSONException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }
	}

	// Locality
	JSONArray localities = json.optJSONArray("local_sites");
	if (localities != null) {
	    int count = localities.length();
	    for (int i=0; i<count; i++) {
		try {
		    data.addLocality(new LicenseAndPermitData(localities.getJSONObject(i)));
		}
		catch (JSONException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }
	}	

	// Business types
	JSONArray businessTypes = json.optJSONArray("sites_for_business_type");
	if (businessTypes != null) {
	    int count = businessTypes.length();
	    for (int i=0; i<count; i++) {
		try {
		    data.addBusinessType(new LicenseAndPermitData(businessTypes.getJSONObject(i)));
		}
		catch (JSONException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
	    }
	}

	return data;
    }

    // Loan and Grants data can be retrieved by:
    //	Federal
    //	State
    //	Federal and State
    //	by Industry
    //	by Specialty
    //	by Industry and Specialty
    //	by State and Industry
    //	by State and Specialty
    //	by State, Industry, and Specialty
    public static List<LoanAndGrantData> getFederalLoansAndGrantsData() {
	final String url = LOANS_AND_GRANTS_BASE_URL + "federal.json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getStateLoansAndGrantsData(final String stateAbbreviation) {
	final String url = LOANS_AND_GRANTS_BASE_URL + "state_financing_for/" + stateAbbreviation.toLowerCase() + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getFederalAndStateLoansAndGrantsData(final String stateAbbreviation) {
	final String url = LOANS_AND_GRANTS_BASE_URL + "federal_and_state_financing_for/" + stateAbbreviation.toLowerCase() + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByIndustry(final String industry) {
	final String url = LOANS_AND_GRANTS_BASE_URL + "nil/for_profit/" + formatForUrl(industry) + "/nil.json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataBySpecialty(final String specialty) {
	final String url = LOANS_AND_GRANTS_BASE_URL + "nil/for_profit/nil/" + formatForUrl(specialty) + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataBySpecialties(final List<String> specialties) {
	final StringBuffer parameters = new StringBuffer();
	for (String speciality : specialties) {
	    if (parameters.length() > 0) {
		parameters.append("-");
	    }
	    parameters.append(formatForUrl(speciality));
	}
	final String url = LOANS_AND_GRANTS_BASE_URL + "nil/for_profit/nil/" + parameters.toString() + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByIndustryAndSpecialty(final String industry, final String specialty) {
	final String url = LOANS_AND_GRANTS_BASE_URL + "nil/for_profit/" + formatForUrl(industry) + "/" + formatForUrl(specialty) + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByIndustryAndSpecialties(final String industry, final List<String> specialties) {
	final StringBuffer parameters = new StringBuffer();
	for (String speciality : specialties) {
	    if (parameters.length() > 0) {
		parameters.append("-");
	    }
	    parameters.append(formatForUrl(speciality));
	}
	final String url = LOANS_AND_GRANTS_BASE_URL + "nil/for_profit/" + formatForUrl(industry) + "/" + parameters.toString() + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByStateAndIndustry(final String stateAbbreviation, final String industry) {
	final String url = LOANS_AND_GRANTS_BASE_URL + stateAbbreviation.toLowerCase() + "/for_profit/" + formatForUrl(industry) + "/nil.json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByStateAndSpecialty(final String stateAbbreviation, final String specialty) {
	final String url = LOANS_AND_GRANTS_BASE_URL + stateAbbreviation.toLowerCase() + "/for_profit/nil/" + formatForUrl(specialty) + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByStateAndSpecialties(final String stateAbbreviation, final List<String> specialties) {
	final StringBuffer parameters = new StringBuffer();
	for (String speciality : specialties) {
	    if (parameters.length() > 0) {
		parameters.append("-");
	    }
	    parameters.append(formatForUrl(speciality));
	}
	final String url = LOANS_AND_GRANTS_BASE_URL + stateAbbreviation.toLowerCase() + "/for_profit/nil/" + parameters.toString() + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByStateAndIndustryAndSpecialty(final String stateAbbreviation, final String industry, final String specialty) {
	final String url = LOANS_AND_GRANTS_BASE_URL + stateAbbreviation.toLowerCase() + "/for_profit/" + 
		formatForUrl(industry) + "/" + formatForUrl(specialty) + ".json";
	return getLoansAndGrantsData(url);
    }
    
    public static List<LoanAndGrantData> getLoansAndGrantsDataByStateAndIndustryAndSpecialties(final String stateAbbreviation, final String industry, final List<String> specialties) {
	final StringBuffer parameters = new StringBuffer();
	for (String speciality : specialties) {
	    if (parameters.length() > 0) {
		parameters.append("-");
	    }
	    parameters.append(formatForUrl(speciality));
	}
	final String url = LOANS_AND_GRANTS_BASE_URL + stateAbbreviation.toLowerCase() + "/for_profit/" + 
		formatForUrl(industry) + "/" + parameters + ".json";
	return getLoansAndGrantsData(url);
    }
    
    private static List<LoanAndGrantData> getLoansAndGrantsData(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONArray array = new JSONArray(response);

	    return processLoansAndGrantsData(array);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
	    
    private static List<LoanAndGrantData> processLoansAndGrantsData(final JSONArray array) throws JSONException {
	List<LoanAndGrantData> data = new ArrayList<LoanAndGrantData>();

	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    data.add(new LoanAndGrantData(json));
	}

	return data;
    }
    
    // Recommended sites can be retrieved by:
    //	All recommended sites
    //	by Keyword
    //	by Category
    //	by Master Term
    //	by Domain
    public static List<RecommendedSite> getAllRecommendedSites() {
	final String url = RECOMMENDED_SITES_BASE_URL + "all_sites/keywords.json";
	return getRecommendedSites(url);
    }
    
    public static List<RecommendedSite> getRecommendedSitesByKeyword(final String keyword) {
	final String url = RECOMMENDED_SITES_BASE_URL + "keywords/" + formatForUrl(keyword) + ".json";
	return getRecommendedSites(url);
    }
    
    public static List<RecommendedSite> getRecommendedSitesByCategory(final String category) {
	final String url = RECOMMENDED_SITES_BASE_URL + "category/" + formatForUrl(category) + ".json";
	return getRecommendedSites(url);
    }
    
    public static List<RecommendedSite> getRecommendedSitesByMasterTerm(final String term) {
	final String url = RECOMMENDED_SITES_BASE_URL + "keywords/master_term/" + formatForUrl(term) + ".json";
	return getRecommendedSites(url);
    }
    
    public static List<RecommendedSite> getRecommendedSitesByDomain(final String domain) {
	final String url = RECOMMENDED_SITES_BASE_URL + "keywords/domain/" + formatForUrl(domain) + ".json";
	return getRecommendedSites(url);
    }
    
    private static List<RecommendedSite> getRecommendedSites(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONArray array = new JSONArray(response);
	    
	    return processRecommendedSites(array);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }

    private static List<RecommendedSite> processRecommendedSites(final JSONArray array) throws JSONException {
	List<RecommendedSite> data = new ArrayList<RecommendedSite>();

	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    data.add(new RecommendedSite(json));
	}
	

	return data;
    }

    // U.S. City and County Web Data can be retrieved by:
    //	City/County Data - All URLs
    //	City/County Data - Only Primary URLs
    //	City/County Data - All Data
    
    // all URLs
    public static List<LocalityWebData> getWebDataAllCityAndCountyUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "city_county_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllCityUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "city_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllCountyUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "county_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllUrlsForCity(final String stateAbbreviation, final String city) {
	final String url = WEB_DATA_BASE_URL + "all_links_for_city_of/" + formatForUrl(city) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllUrlsForCounty(final String stateAbbreviation, final String county) {
	final String url = WEB_DATA_BASE_URL + "all_links_for_county_of/" + formatForUrl(county) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    //primary URLs
    public static List<LocalityWebData> getWebDataPrimaryCityAndCountyUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "primary_city_county_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataPrimaryCityUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "primary_city_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataPrimaryCountyUrlsForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "primary_county_links_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataPrimaryUrlsForCity(final String stateAbbreviation, final String city) {
	final String url = WEB_DATA_BASE_URL + "primary_links_for_city_of/" + formatForUrl(city) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataPrimaryUrlsForCounty(final String stateAbbreviation, final String county) {
	final String url = WEB_DATA_BASE_URL + "primary_links_for_county_of/" + formatForUrl(county) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    //all data
    public static List<LocalityWebData> getWebDataAllCityAndCountyDataForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "city_county_data_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllCityDataForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "city_data_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllCountyDataForState(final String stateAbbreviation) {
	final String url = WEB_DATA_BASE_URL + "county_data_for_state_of/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllDataForCity(final String stateAbbreviation, final String city) {
	final String url = WEB_DATA_BASE_URL + "all_data_for_city_of/" + formatForUrl(city) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    public static List<LocalityWebData> getWebDataAllDataForCounty(final String stateAbbreviation, final String county) {
	final String url = WEB_DATA_BASE_URL + "all_data_for_county_of/" + formatForUrl(county) + "/" + stateAbbreviation.toLowerCase() + ".json";
	return getWebData(url);
    }
    
    private static List<LocalityWebData> getWebData(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONArray array = new JSONArray(response);

	    return processWebData(array);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }

    private static List<LocalityWebData> processWebData(final JSONArray array) throws JSONException {
	List<LocalityWebData> data = new ArrayList<LocalityWebData>();

	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    data.add(new LocalityWebData(json));
	}
	return data;
    }

    private static String formatForUrl(final String param) {
	return param.replace(" ", "%20");
    }
}
