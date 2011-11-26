package com.josephblough.sbt.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Bookmarks {

    private static final String TAG = "SmallBusinessToolbox.Bookmarks";
    
    // Bookmarks will be stored under the categories:
    //	awards
    //	generic_posts
    //	green_posts
    //	license_and_permit_data
    // 	loan_and_grant_data
    //	locality_web_data
    //	recommended_sites
    //	small_business_programs
    //	solicitations
    //
    // When bookmarks are added, we will know which of these objects are being added
    private static final String AWARDS_KEY = "awards";
    private static final String GENERIC_POSTS_KEY = "generic_posts";
    private static final String GREEN_POSTS_KEY = "green_posts";
    private static final String LICENSES_AND_PERMITS_KEY = "licenses_and_permits";
    private static final String LOANS_AND_GRANTS_KEY = "loans_and_grants";
    private static final String LOCALITY_WEB_DATA_KEY = "locality_web_data";
    private static final String RECOMMENDED_SITES_KEY = "recommended_sites";
    private static final String SMALL_BUSINESS_PROGRAMS_KEY = "small_business_programs";
    private static final String SOLICITATIONS_KEY = "solicitations";
    private static final String OFFICES_KEY = "offices";
    
    public List<Award> awardBookmarks;
    public List<GenericPost> genericPostBookmarks;
    public List<GreenPost> greenPostBookmarks;
    public List<LicenseAndPermitData> licensePermitBookmarks;
    public List<LoanAndGrantData> loanGrantBookmarks;
    public List<LocalityWebData> localityWebDataBookmarks;
    public List<RecommendedSite> recommendedSitesBookmarks;
    public List<SmallBusinessProgram> smallBusinessProgramBookmarks;
    public List<Solicitation> solicitationBookmarks;
    public List<SbaDistrictOffice> officeBookmarks;
    
    public Bookmarks() {
	awardBookmarks = new ArrayList<Award>();
	genericPostBookmarks = new ArrayList<GenericPost>();
	greenPostBookmarks = new ArrayList<GreenPost>();
	licensePermitBookmarks = new ArrayList<LicenseAndPermitData>();
	loanGrantBookmarks = new ArrayList<LoanAndGrantData>();
	localityWebDataBookmarks = new ArrayList<LocalityWebData>();
	recommendedSitesBookmarks = new ArrayList<RecommendedSite>();
	smallBusinessProgramBookmarks = new ArrayList<SmallBusinessProgram>();
	solicitationBookmarks = new ArrayList<Solicitation>();
	officeBookmarks = new ArrayList<SbaDistrictOffice>();
    }
    
    public void addBookmark(final Award award) {
	if (!isBookmarked(award))
	    awardBookmarks.add(award);
    }
    
    public boolean isBookmarked(final Award award) {
	return awardBookmarks.contains(award);
    }
    
    public void removeBookmark(final Award award) {
	awardBookmarks.remove(award);
    }
    
    public void addBookmark(final GenericPost post) {
	if (!isBookmarked(post))
	genericPostBookmarks.add(post);
    }
    
    public boolean isBookmarked(final GenericPost post) {
	return genericPostBookmarks.contains(post);
    }
    
    public void removeBookmark(final GenericPost post) {
	genericPostBookmarks.remove(post);
    }

    public void addBookmark(final GreenPost post) {
	if (!isBookmarked(post))
	greenPostBookmarks.add(post);
    }

    public boolean isBookmarked(final GreenPost post) {
	return greenPostBookmarks.contains(post);
    }
    
    public void removeBookmark(final GreenPost post) {
	greenPostBookmarks.remove(post);
    }
    
    public void addBookmark(final LicenseAndPermitData data) {
	if (!isBookmarked(data))
	licensePermitBookmarks.add(data);
    }
    
    public boolean isBookmarked(final LicenseAndPermitData data) {
	return licensePermitBookmarks.contains(data);
    }
    
    public void removeBookmark(final LicenseAndPermitData data) {
	licensePermitBookmarks.remove(data);
    }
    
    public void addBookmark(final LoanAndGrantData data) {
	if (!isBookmarked(data))
	loanGrantBookmarks.add(data);
    }
    
    public boolean isBookmarked(final LoanAndGrantData data) {
	return loanGrantBookmarks.contains(data);
    }
    
    public void removeBookmark(final LoanAndGrantData data) {
	loanGrantBookmarks.remove(data);
    }
    
    public void addBookmark(final LocalityWebData data) {
	if (!isBookmarked(data))
	localityWebDataBookmarks.add(data);
    }
    
    public boolean isBookmarked(final LocalityWebData data) {
	return localityWebDataBookmarks.contains(data);
    }
    
    public void removeBookmark(final LocalityWebData data) {
	localityWebDataBookmarks.remove(data);
    }
    
    public void addBookmark(final RecommendedSite site) {
	if (!isBookmarked(site))
	    recommendedSitesBookmarks.add(site);
    }
    
    public boolean isBookmarked(final RecommendedSite site) {
	return recommendedSitesBookmarks.contains(site);
    }
    
    public void removeBookmark(final RecommendedSite site) {
	recommendedSitesBookmarks.remove(site);
    }
    
    public void addBookmark(final SmallBusinessProgram program) {
	if (!isBookmarked(program))
	    smallBusinessProgramBookmarks.add(program);
    }
    
    public boolean isBookmarked(final SmallBusinessProgram program) {
	return smallBusinessProgramBookmarks.contains(program);
    }
    
    public void removeBookmark(final SmallBusinessProgram program) {
	smallBusinessProgramBookmarks.remove(program);
    }
    
    public void addBookmark(final Solicitation solicitation) {
	if (!isBookmarked(solicitation))
	    solicitationBookmarks.add(solicitation);
    }
    
    public boolean isBookmarked(final Solicitation solicitation) {
	return solicitationBookmarks.contains(solicitation);
    }
    
    public void removeBookmark(final Solicitation solicitation) {
	solicitationBookmarks.remove(solicitation);
    }
    
    public void addBookmark(final SbaDistrictOffice office) {
	if (!isBookmarked(office))
	    officeBookmarks.add(office);
    }
    
    public boolean isBookmarked(final SbaDistrictOffice office) {
	return officeBookmarks.contains(office);
    }
    
    public void removeBookmark(final SbaDistrictOffice office) {
	officeBookmarks.remove(office);
    }
    
    public void loadBookmarks(final Context context) {
	// Probably do this in an async task
	try {
	    SharedPreferences prefs = context.getSharedPreferences(TAG, 0);
	    
	    // Awards
	    int ctr = 1;
	    while (prefs.contains(AWARDS_KEY + ctr)) {
		String awardAsJson = prefs.getString(AWARDS_KEY + ctr, null);
		awardBookmarks.add(new Award(new JSONObject(awardAsJson)));
		ctr++;
	    }

	    //	Generic Posts
	    ctr = 1;
	    while (prefs.contains(GENERIC_POSTS_KEY + ctr)) {
		String postAsJson = prefs.getString(GENERIC_POSTS_KEY + ctr, null);
		genericPostBookmarks.add(new GenericPost(new JSONObject(postAsJson)));
		ctr++;
	    }
	    
	    //	Green Posts
	    ctr = 1;
	    while (prefs.contains(GREEN_POSTS_KEY + ctr)) {
		String postAsJson = prefs.getString(GREEN_POSTS_KEY + ctr, null);
		greenPostBookmarks.add(new GreenPost(new JSONObject(postAsJson)));
		ctr++;
	    }
	    
	    //	License and Permit Data
	    ctr = 1;
	    while (prefs.contains(LICENSES_AND_PERMITS_KEY + ctr)) {
		String licensePermitAsJson = prefs.getString(LICENSES_AND_PERMITS_KEY + ctr, null);
		licensePermitBookmarks.add(new LicenseAndPermitData(new JSONObject(licensePermitAsJson)));
		ctr++;
	    }
	    
	    // 	Loan and Grant Data
	    ctr = 1;
	    while (prefs.contains(LOANS_AND_GRANTS_KEY + ctr)) {
		String loanGrantAsJson = prefs.getString(LOANS_AND_GRANTS_KEY + ctr, null);
		loanGrantBookmarks.add(new LoanAndGrantData(new JSONObject(loanGrantAsJson)));
		ctr++;
	    }
	    
	    //	Locality Web Data
	    ctr = 1;
	    while (prefs.contains(LOCALITY_WEB_DATA_KEY + ctr)) {
		String webDataAsJson = prefs.getString(LOCALITY_WEB_DATA_KEY + ctr, null);
		localityWebDataBookmarks.add(new LocalityWebData(new JSONObject(webDataAsJson)));
		ctr++;
	    }
	    
	    //	Recommended Sites
	    ctr = 1;
	    while (prefs.contains(RECOMMENDED_SITES_KEY + ctr)) {
		String siteAsJson = prefs.getString(RECOMMENDED_SITES_KEY + ctr, null);
		recommendedSitesBookmarks.add(new RecommendedSite(new JSONObject(siteAsJson)));
		ctr++;
	    }
	    
	    //	Small Business Programs
	    ctr = 1;
	    while (prefs.contains(SMALL_BUSINESS_PROGRAMS_KEY + ctr)) {
		String programAsJson = prefs.getString(SMALL_BUSINESS_PROGRAMS_KEY + ctr, null);
		smallBusinessProgramBookmarks.add(new SmallBusinessProgram(new JSONObject(programAsJson)));
		ctr++;
	    }
	    
	    //	Solicitations
	    ctr = 1;
	    while (prefs.contains(SOLICITATIONS_KEY + ctr)) {
		String solicitationAsJson = prefs.getString(SOLICITATIONS_KEY + ctr, null);
		solicitationBookmarks.add(new Solicitation(new JSONObject(solicitationAsJson)));
		ctr++;
	    }
	    
	    //	Offices
	    ctr = 1;
	    while (prefs.contains(OFFICES_KEY + ctr)) {
		String officeAsJson = prefs.getString(OFFICES_KEY + ctr, null);
		officeBookmarks.add(new SbaDistrictOffice(new JSONObject(officeAsJson)));
		ctr++;
	    }
	    
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
    }
    
    public void saveBookmarks(final Context context) {
	// Probably do this in an async task
	SharedPreferences prefs = context.getSharedPreferences(TAG, 0);
	SharedPreferences.Editor editor = prefs.edit();
	editor.clear();

	// Awards
	for (int ctr = 0; ctr < awardBookmarks.size(); ctr++) {
	    editor.putString(AWARDS_KEY + (ctr+1), awardBookmarks.get(ctr).toJson());
	}

	// Generic Posts
	for (int ctr = 0; ctr < genericPostBookmarks.size(); ctr++) {
	    editor.putString(GENERIC_POSTS_KEY + (ctr+1), genericPostBookmarks.get(ctr).toJson());
	}

	// Green Posts
	for (int ctr = 0; ctr < greenPostBookmarks.size(); ctr++) {
	    editor.putString(GREEN_POSTS_KEY + (ctr+1), greenPostBookmarks.get(ctr).toJson());
	}

	// License and Permit Data
	for (int ctr = 0; ctr < licensePermitBookmarks.size(); ctr++) {
	    editor.putString(LICENSES_AND_PERMITS_KEY + (ctr+1), licensePermitBookmarks.get(ctr).toJson());
	}

	// Loan and Grant Data
	for (int ctr = 0; ctr < loanGrantBookmarks.size(); ctr++) {
	    editor.putString(LOANS_AND_GRANTS_KEY + (ctr+1), loanGrantBookmarks.get(ctr).toJson());
	}

	// Locality Web Data
	for (int ctr = 0; ctr < localityWebDataBookmarks.size(); ctr++) {
	    editor.putString(LOCALITY_WEB_DATA_KEY + (ctr+1), localityWebDataBookmarks.get(ctr).toJson());
	}

	// Recommended Sites
	for (int ctr = 0; ctr < recommendedSitesBookmarks.size(); ctr++) {
	    editor.putString(RECOMMENDED_SITES_KEY + (ctr+1), recommendedSitesBookmarks.get(ctr).toJson());
	}

	// Small Business Programs
	for (int ctr = 0; ctr < smallBusinessProgramBookmarks.size(); ctr++) {
	    editor.putString(SMALL_BUSINESS_PROGRAMS_KEY + (ctr+1), smallBusinessProgramBookmarks.get(ctr).toJson());
	}

	// Solicitations
	for (int ctr = 0; ctr < solicitationBookmarks.size(); ctr++) {
	    editor.putString(SOLICITATIONS_KEY + (ctr+1), solicitationBookmarks.get(ctr).toJson());
	}

	// Offices
	for (int ctr = 0; ctr < officeBookmarks.size(); ctr++) {
	    editor.putString(OFFICES_KEY + (ctr+1), officeBookmarks.get(ctr).toJson());
	}

	editor.commit();
    }
    
    public boolean isEmpty() {
	return awardBookmarks.isEmpty() &&
		genericPostBookmarks.isEmpty() &&
		greenPostBookmarks.isEmpty() &&
		licensePermitBookmarks.isEmpty() &&
		loanGrantBookmarks.isEmpty() &&
		localityWebDataBookmarks.isEmpty() &&
		recommendedSitesBookmarks.isEmpty() &&
		smallBusinessProgramBookmarks.isEmpty() &&
		solicitationBookmarks.isEmpty();
    }
}
