package com.josephblough.sbt.data;

import org.json.JSONException;
import org.json.JSONObject;

import com.josephblough.sbt.ApplicationController;

import android.util.Log;

public class LoanAndGrantData implements Bookmarkable {

    private final static String TAG = "LoanAndGrantData";
    
    /*
Loans and Grants Industries:
   Agriculture
   Child Care
   Environmental Management
   Health Care
   Manufacturing
   Technology
   Tourism 
   
Loans and Grants Specialities:
   general_purpose
   development
   exporting
   contractor
   green
   military
   minority
   woman
   disabled
   rural
   disaster

Loans and Grants Government Types:
   Federal
   State
   Private
   Local

Loans and Grants Funding Types:
   Grant
   Loan
   Venture Capital
   Tax Incentive
     */
    
    public String state;
    public String governmentType;
    public String loanType;
    public String agency;
    public String industry;
    public String title;
    public String description;
    public String url;
    public Boolean isGeneralPurpose;
    public Boolean isDevelopment;
    public Boolean isExporting;
    public Boolean isContractorOnly;
    public Boolean isGreen;
    public Boolean isMilitaryOnly;
    public Boolean isMinority;
    public Boolean isWomenOnly;
    public Boolean isDisabledOnly;
    public Boolean isRural;
    public Boolean isDisaster;

    public LoanAndGrantData(final JSONObject json) throws JSONException {
	state = json.optString("state_name");
	governmentType = json.optString("gov_type");
	loanType = json.optString("loan_type");
	agency = json.optString("agency");
	industry = json.optString("industry");
	title = json.optString("title");
	description = json.optString("description");
	url = json.optString("url");
	isGeneralPurpose = (json.optInt("is_general_purpose") == 1);
	isDevelopment = (json.optInt("is_development") == 1);
	isExporting = (json.optInt("is_exporting") == 1);
	isContractorOnly = (json.optInt("is_contractor") == 1);
	isGreen = (json.optInt("is_green") == 1);
	isMilitaryOnly = (json.optInt("is_military") == 1);
	isMinority = (json.optInt("is_minority") == 1);
	isWomenOnly = (json.optInt("is_woman") == 1);
	isDisabledOnly = (json.optInt("is_disabled") == 1);
	isRural = (json.optInt("is_rural") == 1);
	isDisaster = (json.optInt("is_disaster") == 1);
    }

    @Override
    public String toString() {
        return "State: '" + state + "', " +
        	"Government Type: '" + governmentType + "', " +
        	"Loan Type: '" + loanType + "', " +
        	"Agency: '" + agency + "', " +
        	"Industry: '" + industry + "', " +
        	"Title: '" + title + "', " +
        	"Description: '" + description + "', " +
        	"Url: '" + url + "', " +
        	"Is General Purpose: " + isGeneralPurpose + ", " + 
        	"Is Development: " + isDevelopment + ", " + 
        	"Is Exporting: " + isExporting + ", " + 
        	"Is Contractor Only: " + isContractorOnly + ", " + 
        	"Is Green: " + isGreen + ", " + 
        	"Is Military Only: " + isMilitaryOnly + ", " + 
        	"Is Minority: " + isMinority + ", " + 
        	"Is Women Only: " + isWomenOnly + ", " + 
        	"Is Disabled Only: " + isDisabledOnly + ", " + 
        	"Is Rural: " + isRural + ", " + 
        	"Is Disaster: " + isDisaster; 
    }

    public String getType() {
	return "Load/Grant";
    }

    public String getName() {
	return title;
    }

    public String getUrl() {
	return url;
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("title", title);
	    if (description != null)
		json.put("description", description);
	    if (url != null)
		json.put("url", url);
	    if (state != null)
		json.put("state", state);
	    if (governmentType != null)
		json.put("gov_type", governmentType);
	    if (loanType != null)
		json.put("loan_type", loanType);
	    if (agency != null)
		json.put("agency", agency);
	    if (industry != null)
		json.put("industry", industry);
	    if (isGeneralPurpose != null)
		json.put("is_general_purpose", (isGeneralPurpose) ? 1 : 0);
	    if (isDevelopment != null)
		json.put("is_development", (isDevelopment) ? 1 : 0);
	    if (isExporting != null)
		json.put("is_exporting", (isExporting) ? 1 : 0);
	    if (isContractorOnly != null)
		json.put("is_contractor", (isContractorOnly) ? 1 : 0);
	    if (isGreen != null)
		json.put("is_green", (isGreen) ? 1 : 0);
	    if (isMilitaryOnly != null)
		json.put("is_military", (isMilitaryOnly) ? 1 : 0);
	    if (isMinority != null)
		json.put("is_minority", (isMinority) ? 1 : 0);
	    if (isWomenOnly != null)
		json.put("is_woman", (isWomenOnly) ? 1 : 0);
	    if (isDisabledOnly != null)
		json.put("is_disabled", (isDisabledOnly) ? 1 : 0);
	    if (isRural != null)
		json.put("is_rural", (isRural) ? 1 : 0);
	    if (isDisaster != null)
		json.put("is_disaster", (isDisaster) ? 1 : 0);
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
		+ ((description == null) ? 0 : description.hashCode());
	result = prime * result
		+ ((governmentType == null) ? 0 : governmentType.hashCode());
	result = prime * result
		+ ((industry == null) ? 0 : industry.hashCode());
	result = prime * result
		+ ((loanType == null) ? 0 : loanType.hashCode());
	result = prime * result + ((state == null) ? 0 : state.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
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
	LoanAndGrantData other = (LoanAndGrantData) obj;
	if (agency == null) {
	    if (other.agency != null)
		return false;
	} else if (!agency.equals(other.agency))
	    return false;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (governmentType == null) {
	    if (other.governmentType != null)
		return false;
	} else if (!governmentType.equals(other.governmentType))
	    return false;
	if (industry == null) {
	    if (other.industry != null)
		return false;
	} else if (!industry.equals(other.industry))
	    return false;
	if (loanType == null) {
	    if (other.loanType != null)
		return false;
	} else if (!loanType.equals(other.loanType))
	    return false;
	if (state == null) {
	    if (other.state != null)
		return false;
	} else if (!state.equals(other.state))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	return true;
    }

    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }

    public int getDetailCount() {
	return 9;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "Description:";
	case 2:
	    return "URL:";
	case 3:
	    return "Location:";
	case 4:
	    return "Gov't Type:";
	case 5:
	    return "Loan Type:";
	case 6:
	    return "Agency:";
	case 7:
	    return "Industry:";
	case 8:
	    return "";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	/*
    public String title;
    public String description;
    public String url;
    public String state;
    public String governmentType;
    public String loanType;
    public String agency;
    public String industry;
    public Boolean isGeneralPurpose;
    public Boolean isDevelopment;
    public Boolean isExporting;
    public Boolean isContractorOnly;
    public Boolean isGreen;
    public Boolean isMilitaryOnly;
    public Boolean isMinority;
    public Boolean isWomenOnly;
    public Boolean isDisabledOnly;
    public Boolean isRural;
    public Boolean isDisaster;
	 */
	
	switch (detail) {
	case 0:
	    return title;
	case 1:
	    return description;
	case 2:
	    return null; //url;
	case 3:
	    return state;
	case 4:
	    return governmentType;
	case 5:
	    return loanType;
	case 6:
	    return agency;
	case 7:
	    return industry;
	case 8:
	    return generateSpecialtyValue();
	};
	return null;
    }
    
    private String generateSpecialtyValue() {
	StringBuffer value = new StringBuffer();
	if (isGeneralPurpose)
	    value.append("General Purpose");
	if (isDevelopment) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Development");
	}
	if (isExporting) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Exporting");
	}
	if (isContractorOnly) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Contractor only");
	}
	if (isGreen) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Green");
	}
	if (isMilitaryOnly) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Military owned");
	}
	if (isMinority) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Minority owned");
	}
	if (isWomenOnly) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Woman owned");
	}
	if (isDisabledOnly) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Disabled");
	}
	if (isRural) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Rural areas");
	}
	if (isDisaster) {
	    if (value.length() > 0)
		value.append(", ");
	    value.append("Disaster Relief");
	}
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
    
    public String formatForSharing() {
	return url;
    }
}
