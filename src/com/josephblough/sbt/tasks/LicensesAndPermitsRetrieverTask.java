package com.josephblough.sbt.tasks;

import com.josephblough.sbt.callbacks.LicensesAndPermitsRetrieverCallback;
import com.josephblough.sbt.criteria.LicensesAndPermitsSearchCriteria;
import com.josephblough.sbt.data.LicenseAndPermitDataCollection;
import com.josephblough.sbt.transport.SbaTransport;

import android.os.AsyncTask;

public class LicensesAndPermitsRetrieverTask extends
	AsyncTask<LicensesAndPermitsSearchCriteria, Void, LicenseAndPermitDataCollection> {

    private LicensesAndPermitsRetrieverCallback callback;
    
    public LicensesAndPermitsRetrieverTask(final LicensesAndPermitsRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected LicenseAndPermitDataCollection doInBackground(
	    LicensesAndPermitsSearchCriteria... criteria) {
	
	if (criteria[0].searchBy == LicensesAndPermitsSearchCriteria.CATEGORY_INDEX) {
	    return SbaTransport.getLicenseAndPermitDataByCategory(criteria[0].category);
	}
	else if (criteria[0].searchBy == LicensesAndPermitsSearchCriteria.STATE_INDEX) {
	    // Convert the state name to the abbreviation
	    return SbaTransport.getLicenseAndPermitDataByState(criteria[0].state);
	}
	else if (criteria[0].searchBy == LicensesAndPermitsSearchCriteria.BUSINESS_TYPE_INDEX) {
	    final String businessType = criteria[0].businessType;
	    if (criteria[0].businessTypeSubfilter == LicensesAndPermitsSearchCriteria.NO_SUBFILTER_INDEX) {
		return SbaTransport.getLicenseAndPermitDataByBusinessType(businessType);
	    }
	    else if (criteria[0].businessTypeSubfilter == LicensesAndPermitsSearchCriteria.STATE_SUBFILTER_INDEX) {
		return SbaTransport.getLicenseAndPermitDataByBusinessTypeAndState(businessType, 
			criteria[0].state);
	    }
	    else if (criteria[0].businessTypeSubfilter == LicensesAndPermitsSearchCriteria.COUNTY_SUBFILTER_INDEX) {
		return SbaTransport.getLicenseAndPermitDataByBusinessTypeStateAndCounty(businessType, 
			criteria[0].state,
			criteria[0].businessTypeSubfilterLocality);
	    }
	    else if (criteria[0].businessTypeSubfilter == LicensesAndPermitsSearchCriteria.CITY_SUBFILTER_INDEX) {
		return SbaTransport.getLicenseAndPermitDataByBusinessTypeStateAndCity(businessType, 
			criteria[0].state,
			criteria[0].businessTypeSubfilterLocality);
	    }
	    else if (criteria[0].businessTypeSubfilter == LicensesAndPermitsSearchCriteria.ZIP_CODE_SUBFILTER_INDEX) {
		return SbaTransport.getLicenseAndPermitDataByBusinessTypeAndZipCode(businessType, 
			criteria[0].businessTypeSubfilterLocality);
	    }
	}
	
	return null;
    }

    @Override
    protected void onPostExecute(LicenseAndPermitDataCollection results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving licenses and permits data.");
    }
}
