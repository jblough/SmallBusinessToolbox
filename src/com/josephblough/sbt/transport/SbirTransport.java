package com.josephblough.sbt.transport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.josephblough.sbt.data.Award;
import com.josephblough.sbt.data.GenericPost;
import com.josephblough.sbt.data.GreenPost;
import com.josephblough.sbt.data.SbaDistrictOffice;
import com.josephblough.sbt.data.SmallBusinessProgram;
import com.josephblough.sbt.data.Solicitation;
import com.josephblough.sbt.handlers.AwardsXmlHandler;
import com.josephblough.sbt.handlers.GenericPostsXmlHandler;

public class SbirTransport {

    public static final String TAG = "SbirTransport";
    
    public static final String BASE_GREEN_API_HOST = "http://green.sba.gov/";
    public static final String BASE_SBIR_API_HOST = "http://www.sbir.gov/api/";
    
    public static final String SMALL_BUSINESS_PROGRAM_SEARCH_BASE_URL = BASE_GREEN_API_HOST + "ek/api/";
    public static final String GREEN_SEARCH_BASE_URL = BASE_GREEN_API_HOST + "api/green_search";
    public static final String GENERIC_SEARCH_BASE_URL = BASE_GREEN_API_HOST + "api/sb_search";
    public static final String OFFICE_SEARCH_BASE_URL = BASE_GREEN_API_HOST + "api/office_search";
    
    public static final String SOLICITATIONS_BASE_URL = BASE_SBIR_API_HOST + "solicitations";
    public static final String AWARDS_BASE_URL = BASE_SBIR_API_HOST + "awards";
    
    // Instead of having a separate method for each parameter combination, pass a Map<String, String>
    //	into the method with the predefined *_PARAMETER elements for the keys of the map and call
    //	formatForUrl on the values in the map when adding them to the url 
    public static final String KEYWORD_PARAMETER = "keyword";
    public static final String COMPANY_PARAMETER = "company";
    public static final String RESEARCH_INSTITUTION_PARAMETER = "ri";
    public static final String AGENCY_PARAMETER = "agency";
    public static final String YEAR_PARAMETER = "year";
    public static final String TYPE_PARAMETER = "type";
    public static final String NEW_PARAMETER = "new";
    
    // "Small Business Program" finder
    //	by Location - http://green.sba.gov/ek/api/[STATE]/json
    //	by Industry/Interest - http://green.sba.gov/ek/api/[INDUSTRY]/json
    //	by Type of Program/Service - http://green.sba.gov/ek/api/[PROGRAM]/json
    //	by Qualification - http://green.sba.gov/ek/api/[QUALIFICATION]/json
    public static List<SmallBusinessProgram> findSmallBusinessProgramsByLocation(final String location) {
	final String url = SMALL_BUSINESS_PROGRAM_SEARCH_BASE_URL + formatForUrl(location) + "/json";
	return findSmallBusinessPrograms(url);
    }
    
    public static List<SmallBusinessProgram> findSmallBusinessProgramsByIndustry(final String industry) {
	final String url = SMALL_BUSINESS_PROGRAM_SEARCH_BASE_URL + formatForUrl(industry) + "/json";
	return findSmallBusinessPrograms(url);
    }
    
    public static List<SmallBusinessProgram> findSmallBusinessProgramsByProgramType(final String programType) {
	final String url = SMALL_BUSINESS_PROGRAM_SEARCH_BASE_URL + formatForUrl(programType) + "/json";
	return findSmallBusinessPrograms(url);
    }
    
    public static List<SmallBusinessProgram> findSmallBusinessProgramsByQualification(final String qualification) {
	final String url = SMALL_BUSINESS_PROGRAM_SEARCH_BASE_URL + formatForUrl(qualification) + "/json";
	return findSmallBusinessPrograms(url);
    }
    
    private static List<SmallBusinessProgram> findSmallBusinessPrograms(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    if ("No results found".equals(response))
		return new ArrayList<SmallBusinessProgram>();
	    
	    JSONArray array = new JSONArray(response);
	    
	    return processSmallBusinessPrograms(array);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static List<SmallBusinessProgram> processSmallBusinessPrograms(final JSONArray array) throws JSONException {
	List<SmallBusinessProgram> data = new ArrayList<SmallBusinessProgram>();

	int length = array.length();
	for (int i=0; i<length; i++) {
	    JSONObject json = array.getJSONObject(i);
	    data.add(new SmallBusinessProgram(json));
	}
	return data;
    }
    
    // Solicitations
    //	by Keyword search - http://www.sbir.gov/api/solicitations.json?keyword=some text without quotes
    //	by Agency search - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE
    //	Download all Open - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&open=1
    //	Download all Closed - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&closed=1
    public static List<Solicitation> getSolicitations() {
	final String url = SOLICITATIONS_BASE_URL + ".json";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getSolicitationsByKeyword(final String keyword) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword);
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getSolicitationsByAgency(final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?agency=" + formatForUrl(agency);
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getSolicitationsByKeywordAndAgency(final String keyword, final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword) + "&agency=" + formatForUrl(agency);
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getOpenSolicitations() {
	final String url = SOLICITATIONS_BASE_URL + ".json?open=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getOpenSolicitationsByKeyword(final String keyword) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword) + "&open=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getOpenSolicitationsByAgency(final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?agency=" + formatForUrl(agency) + "&open=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getOpenSolicitationsByKeywordAndAgency(final String keyword, final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword) + "&agency=" + formatForUrl(agency) + "&open=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getClosedSolicitations() {
	final String url = SOLICITATIONS_BASE_URL + ".json?closed=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getClosedSolicitationsByKeyword(final String keyword) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword) + "&closed=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getClosedSolicitationsByAgency(final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?agency=" + formatForUrl(agency) + "&closed=1";
	return getSolicitations(url);
    }
    
    public static List<Solicitation> getClosedSolicitationsByKeywordAndAgency(final String keyword, final String agency) {
	final String url = SOLICITATIONS_BASE_URL + ".json?keyword=" + formatForUrl(keyword) + "&agency=" + formatForUrl(agency) + "&closed=1";
	return getSolicitations(url);
    }

    private static List<Solicitation> getSolicitations(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    
	    return processSolicitations(json);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }

    private static List<Solicitation> processSolicitations(final JSONObject json) throws JSONException {
	//int length = json.optInt("numFound");
	
	List<Solicitation> data = new ArrayList<Solicitation>();
	
	int ctr = 0;
	JSONObject solicitation = json.optJSONObject("" + ctr++);
	while (solicitation != null) {
	    data.add(new Solicitation(solicitation));
	    solicitation = json.optJSONObject("" + ctr++);
	}
	
	return data;
    }
    
    // Awards
    //	by Keyword search - http://www.sbir.gov/api/awards.json?keyword=some text without quotes
    //	by Agency search - http://www.sbir.gov/api/awards.json?keyword=mars&agency=DOE
    //	by Company search - http://www.sbir.gov/api/awards.json?keyword=mars&company=tristar%20engineering
    //	by Research Institution search - http://www.sbir.gov/api/awards.json?keyword=mars&ri=ucla
    //	by Year search - http://www.sbir.gov/api/awards.xml?keyword=mars&agency=DOE&year=2010
    //	Download all - http://www.sbir.gov/api/awards.json
    public static List<Award> getAllAwardsAsXml() {
	final String url = AWARDS_BASE_URL +".xml";
	return getAwardsAsXml(url);
    }

    public static List<Award> getAwardsAsXml(final Map<String, String> parameters) {
	final StringBuffer url = new StringBuffer(AWARDS_BASE_URL +".xml?");
	boolean first = true;
	for (Map.Entry<String, String> entry : parameters.entrySet()) {
	    if (!first)
		url.append("&");
	    url.append(entry.getKey() + "=" + formatForUrl(entry.getValue()));
	    first = false;
	}

	return getAwardsAsXml(url.toString());
    }
    
    public static List<Award> getAwardsAsXml(final String url) {
	Log.d(TAG, "url: " + url);
	
	try {
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpMethod = new HttpGet(url);
	    HttpResponse response = client.execute(httpMethod);
	    HttpEntity entity = response.getEntity();

	    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactory.newSAXParser();
	    XMLReader reader = parser.getXMLReader();
	    AwardsXmlHandler handler = new AwardsXmlHandler();
	    reader.setContentHandler(handler);
	    reader.parse(new InputSource(entity.getContent()));
	    return handler.awards;
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<Award> getAllAwards() {
	final String url = AWARDS_BASE_URL +".json";
	return getAwards(url);
    }

    public static List<Award> getAwards(final Map<String, String> parameters) {
	final StringBuffer url = new StringBuffer(AWARDS_BASE_URL +".json?");
	boolean first = true;
	for (Map.Entry<String, String> entry : parameters.entrySet()) {
	    if (!first)
		url.append("&");
	    url.append(entry.getKey() + "=" + formatForUrl(entry.getValue()));
	    first = false;
	}
	return getAwards(url.toString());
    }
    
    public static List<Award> getAwardsByKeyword(final String keyword) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByAgency(final String agency) {
	final String url = AWARDS_BASE_URL +".json?agency=" + formatForUrl(agency);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByKeywordAndAgency(final String keyword, final String agency) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword) + "&agency=" + formatForUrl(agency);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByCompany(final String company) {
	final String url = AWARDS_BASE_URL +".json?company=" + formatForUrl(company);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByKeywordAndCompany(final String keyword, final String company) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword) + "&company=" + formatForUrl(company);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByResearchInstitution(final String institution) {
	final String url = AWARDS_BASE_URL +".json?ri=" + formatForUrl(institution);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByKeywordAndResearchInstitution(final String keyword, final String institution) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword) + "ri=" + formatForUrl(institution);
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByYear(final int year) {
	final String url = AWARDS_BASE_URL +".json?year=" + year;
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByKeywordAndYear(final String keyword, final int year) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword) + "&year=" + year;
	return getAwards(url);
    }
    
    public static List<Award> getAwardsByKeywordAndYear(final String keyword, final String agency, final int year) {
	final String url = AWARDS_BASE_URL +".json?keyword=" + formatForUrl(keyword) + "&agency=" + formatForUrl(agency) + "&year=" + year;
	return getAwards(url);
    }
    
    private static List<Award> getAwards(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    
	    return processAwards(json);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }

    private static List<Award> processAwards(final JSONObject json) throws JSONException {
	//int length = json.optInt("numFound");
	
	List<Award> data = new ArrayList<Award>();
	
	int ctr = 0;
	JSONObject award = json.optJSONObject("" + ctr++);
	while (award != null) {
	    data.add(new Award(award));
	    award = json.optJSONObject("" + ctr++);
	}
	
	return data;
    }
    
    // Green
    //	Download all - http://green.sba.gov/api/green_search.json
    //	by type - http://green.sba.gov/api/green_search.json?type=presolicitation
    // 	Keyword search for all or by type - http://green.sba.gov/api/green_search.json?keyword=green
    //	by Agency - http://green.sba.gov/api/green_search.json?agency=Department%20of%20the%20Navy
    public static List<GreenPost> getAllGreenPosts() {
	final String url = GREEN_SEARCH_BASE_URL + ".json";
	return getGreenPosts(url);
    }
    
    public static List<GreenPost> getGreenPosts(final Map<String, String> parameters) {
	final StringBuffer url = new StringBuffer(GREEN_SEARCH_BASE_URL +".json?");
	boolean first = true;
	for (Map.Entry<String, String> entry : parameters.entrySet()) {
	    if (!first)
		url.append("&");
	    url.append(entry.getKey() + "=" + formatForUrl(entry.getValue()));
	    first = false;
	}
	return getGreenPosts(url.toString());
    }
    
    public static List<GreenPost> getGreenPostsByType(final String type) {
	final String url = GREEN_SEARCH_BASE_URL + ".json?type="  + formatForUrl(type);
	return getGreenPosts(url);
    }
    
    public static List<GreenPost> getGreenPostsByKeyword(final String keyword) {
	final String url = GREEN_SEARCH_BASE_URL + ".json?keyword=" + formatForUrl(keyword);
	return getGreenPosts(url);
    }
    
    public static List<GreenPost> getGreenPostsByAgency(final String agency) {
	final String url = GREEN_SEARCH_BASE_URL + ".json?agency=" + formatForUrl(agency);
	return getGreenPosts(url);
    }
    
    private static List<GreenPost> getGreenPosts(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    JSONObject json = new JSONObject(response);
	    
	    return processGreenPosts(json);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static List<GreenPost> processGreenPosts(final JSONObject json) throws JSONException {
	//int length = json.optInt("numFound");
	
	List<GreenPost> data = new ArrayList<GreenPost>();
	
	int ctr = 0;
	//JSONArray post = json.optJSONArray("" + ctr++);
	JSONObject post = json.optJSONObject("" + ctr++);
	while (post != null) {
	    data.add(new GreenPost(post));
	    //post = json.optJSONArray("" + ctr++);
	    post = json.optJSONObject("" + ctr++);
	}
	
	return data;
    }
    
    // Generic
    //	Download all - http://green.sba.gov/api/sb_search.json
    //	by type - http://green.sba.gov/api/sb_search.json?type=presolicitation
    // 	Keyword search for all or by type - http://green.sba.gov/api/sb_search.json?keyword=green
    //	by Agency - http://green.sba.gov/api/sb_search.json?agency=Department%20of%20the%20Navy
    public static List<GenericPost> getAllGenericPostsAsXml() {
	final String url = GENERIC_SEARCH_BASE_URL +".xml";
	return getGenericPostsAsXml(url);
    }

    public static List<GenericPost> getGenericPostsAsXml(final Map<String, String> parameters) {
	final StringBuffer url = new StringBuffer(GENERIC_SEARCH_BASE_URL +".xml?");
	boolean first = true;
	for (Map.Entry<String, String> entry : parameters.entrySet()) {
	    if (!first)
		url.append("&");
	    url.append(entry.getKey() + "=" + formatForUrl(entry.getValue()));
	    first = false;
	}

	return getGenericPostsAsXml(url.toString());
    }
    
    public static List<GenericPost> getGenericPostsAsXml(final String url) {
	Log.d(TAG, "url: " + url);
	
	try {
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpMethod = new HttpGet(url);
	    HttpResponse response = client.execute(httpMethod);
	    HttpEntity entity = response.getEntity();

	    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactory.newSAXParser();
	    XMLReader reader = parser.getXMLReader();
	    GenericPostsXmlHandler handler = new GenericPostsXmlHandler();
	    reader.setContentHandler(handler);
	    reader.parse(new InputSource(entity.getContent()));
	    return handler.posts;
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    public static List<GenericPost> getAllGenericPosts() {
	final String url = GENERIC_SEARCH_BASE_URL + ".json";
	return getGenericPosts(url);
    }
    
    public static List<GenericPost> getGenericPosts(final Map<String, String> parameters) {
	final StringBuffer url = new StringBuffer(GENERIC_SEARCH_BASE_URL +".json?");
	boolean first = true;
	for (Map.Entry<String, String> entry : parameters.entrySet()) {
	    if (!first)
		url.append("&");
	    url.append(entry.getKey() + "=" + formatForUrl(entry.getValue()));
	    first = false;
	}
	return getGenericPosts(url.toString());
    }
    
    public static List<GenericPost> getGenericPostsByType(final String type) {
	final String url = GENERIC_SEARCH_BASE_URL + ".json?type=" + formatForUrl(type);
	return getGenericPosts(url);
    }
    
    public static List<GenericPost> getGenericPostsByKeyword(final String keyword) {
	final String url = GENERIC_SEARCH_BASE_URL + ".json?keyword=" + formatForUrl(keyword);
	return getGenericPosts(url);
    }
    
    public static List<GenericPost> getGenericPostsByAgency(final String agency) {
	final String url = GENERIC_SEARCH_BASE_URL + ".json?agency=" + formatForUrl(agency);
	return getGenericPosts(url);
    }
    
    private static List<GenericPost> getGenericPosts(final String url) {
	try {
	    //HttpClient client = new DefaultHttpClient();
	    AndroidHttpClient client = AndroidHttpClient.newInstance("Android Small Business Toolbox");
	    Log.d(TAG, "url: " + url);
	    HttpGet httpMethod = new HttpGet(url);
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);
	    JSONObject json = new JSONObject(response);
/*
	    HttpResponse response = client.execute(httpMethod);
	    HttpEntity entity = response.getEntity();

	    StringWriter writer = new StringWriter();
	    char[] buffer = new char[1024];
	    Reader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
	    int n;
	    while ((n = reader.read(buffer)) != -1) {
		writer.write(buffer, 0, n);
	    }

	    JSONObject json = new JSONObject(writer.toString());
*/
	    return processGenericPosts(json);
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }
    
    private static List<GenericPost> processGenericPosts(final JSONObject json) throws JSONException {
	//int length = json.optInt("numFound");
	
	List<GenericPost> data = new ArrayList<GenericPost>();
	
	int ctr = 0;
	JSONObject post = json.optJSONObject("" + ctr++);
	while (post != null) {
	    data.add(new GenericPost(post));
	    post = json.optJSONObject("" + ctr++);
	}
	
	return data;
    }
    
    // SBA District Offices
    //	search by zip code - http://green.sba.gov/api/office_search.json?zip=91203
    public static List<SbaDistrictOffice> getAllDistrictOffices() {
	final String url = OFFICE_SEARCH_BASE_URL + ".json";
	return getDistrictOffices(url);
    }
    
    public static List<SbaDistrictOffice> getDistrictOfficesByZipCode(final String zipCode) {
	final String url = OFFICE_SEARCH_BASE_URL + ".json?zip=" + formatForUrl(zipCode);
	return getDistrictOffices(url);
    }

    private static List<SbaDistrictOffice> getDistrictOffices(final String url) {
	try {
	    HttpClient client = new DefaultHttpClient();
	    Log.d(TAG, "url: " + url.toString());
	    HttpGet httpMethod = new HttpGet(url.toString());
	    ResponseHandler<String> handler = new BasicResponseHandler();
	    String response = client.execute(httpMethod, handler);

	    List<SbaDistrictOffice> data = new ArrayList<SbaDistrictOffice>();
	    JSONArray array = new JSONArray(response);
	    int length = array.length();
	    for (int i=0; i<length; i++) {
		JSONObject json = array.getJSONObject(i);
		data.add(new SbaDistrictOffice(json));
	    }
	    
	    return data;
	}
	catch (OutOfMemoryError e) {
	    return null;
	}
	catch (Exception e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	return null;
    }

    private static String formatForUrl(final String param) {
	return param.replace(" ", "%20");
    }
}
