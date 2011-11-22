package com.josephblough.sbt.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class Award implements Bookmarkable {

    private static final String TAG = "Award";
    
    /*
Award Agencies:
   DOD = Dept. of Defense
   HHS = Dept. of Health and Human Services
   NASA = National Aeronautics and Space Administration
   NSF = National Science Foundation
   DOE = Dept. of Energy
   USDA = United States Dept. of Agriculture
   EPA = Environmental Protection Agency
   DOC = Dept. of Commerce
   ED = Dept. of Education
   DOT = Dept. of Transportation
   DHS = Dept. of Homeland Security
     */
    
    public String title;
    public String link;
    public String awardAbstract;
    public String agency;
    public String program;
    public Integer phase;
    public Integer year;
    public String company;
    public String researchInstitution;
    
    public Award() {
	
    }
    
    public Award(final JSONObject json) {
	title = json.optString("title");
	link = json.optString("link");
	awardAbstract = json.optString("abstract");
	agency = json.optString("agency");
	program = json.optString("program");
	if (json.has("phase"))
	    phase = json.optInt("phase");
	if (json.has("year"))
	    year = json.optInt("year");
	company = json.optString("company");
	researchInstitution = json.optString("ri");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Link: '" + link + "', " +
        	"Abstract: '" + awardAbstract + "', " +
        	"Agency: '" + agency + "', " +
        	"Program: '" + program + "', " +
        	"Phase: '" + phase + "', " +
        	"Year: '" + year + "', " +
        	"Company: '" + company + "', " +
        	"Research Institution: '" + researchInstitution + "'";
    }

    public String getType() {
	return "Award";
    }

    public String getName() {
	return title;
    }

    public String getUrl() {
	return link;
    }

    public String toJson() {
	/*
	title = json.optString("title");
	link = json.optString("link");
	awardAbstract = json.optString("abstract");
	agency = json.optString("agency");
	program = json.optString("program");
	if (json.has("phase"))
	    phase = json.optInt("phase");
	if (json.has("year"))
	    year = json.optInt("year");
	company = json.optString("company");
	researchInstitution = json.optString("ri");

	 * 
	 */

	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("title", title);
	    if (link != null)
		json.put("link", link);
	    if (awardAbstract != null)
		json.put("abstract", awardAbstract);
	    if (agency != null)
		json.put("agency", agency);
	    if (phase != null)
		json.put("phase", phase);
	    if (year != null)
		json.put("year", year);
	    if (company != null)
		json.put("company", company);
	    if (researchInstitution != null)
		json.put("ri", researchInstitution);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return json.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((agency == null) ? 0 : agency.hashCode());
	result = prime * result
		+ ((awardAbstract == null) ? 0 : awardAbstract.hashCode());
	result = prime * result + ((company == null) ? 0 : company.hashCode());
	result = prime * result + ((link == null) ? 0 : link.hashCode());
	result = prime * result + ((phase == null) ? 0 : phase.hashCode());
	result = prime * result + ((program == null) ? 0 : program.hashCode());
	result = prime
		* result
		+ ((researchInstitution == null) ? 0 : researchInstitution
			.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((year == null) ? 0 : year.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Award other = (Award) obj;
	if (agency == null) {
	    if (other.agency != null)
		return false;
	} else if (!agency.equals(other.agency))
	    return false;
	if (awardAbstract == null) {
	    if (other.awardAbstract != null)
		return false;
	} else if (!awardAbstract.equals(other.awardAbstract))
	    return false;
	if (company == null) {
	    if (other.company != null)
		return false;
	} else if (!company.equals(other.company))
	    return false;
	if (link == null) {
	    if (other.link != null)
		return false;
	} else if (!link.equals(other.link))
	    return false;
	if (phase == null) {
	    if (other.phase != null)
		return false;
	} else if (!phase.equals(other.phase))
	    return false;
	if (program == null) {
	    if (other.program != null)
		return false;
	} else if (!program.equals(other.program))
	    return false;
	if (researchInstitution == null) {
	    if (other.researchInstitution != null)
		return false;
	} else if (!researchInstitution.equals(other.researchInstitution))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (year == null) {
	    if (other.year != null)
		return false;
	} else if (!year.equals(other.year))
	    return false;
	return true;
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    public int getDetailCount() {
	return 8;
    }
    
    public boolean isVisible(final int detail) {
	return !isEmpty(getDetailValue(detail));
    }
    
    public String getDetailLabel(final int detail) {
	switch (detail) {
	case 0: // title
	    return "Title:";
	case 1: // link
	    return "URL:";
	case 2: // abstract
	    return "Abstract:";
	case 3: // agency
	    return "Agency:";
	case 4: // program
	    return "Program:";
	case 5: // phase
	    return "Phase:";
	case 6: // year
	    return "Year:";
	case 7: // company
	    return "Company:";
	case 8: // research institution
	    return "RI:";
	}
	return "";
    }
    
    public String getDetailValue(final int detail) {
	switch (detail) {
	case 0: // title
	    return title;
	case 1: // link
	    return null; //link;
	case 2: // abstract
	    return awardAbstract;
	case 3: // agency
	    return agency;
	case 4: // program
	    return program;
	case 5: // phase
	    return phase == null ? null : phase.toString();
	case 6: // year
	    return year == null ? null : year.toString();
	case 7: // company
	    return company;
	case 8: // research institution
	    return researchInstitution;
	}
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
    
    public String formatForSharing() {
	return link;
    }
}
