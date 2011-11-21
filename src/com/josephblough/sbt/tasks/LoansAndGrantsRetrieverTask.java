package com.josephblough.sbt.tasks;

import java.util.List;

import com.josephblough.sbt.callbacks.LoansAndGrantsRetrieverCallback;
import com.josephblough.sbt.criteria.LoansAndGrantsSearchCriteria;
import com.josephblough.sbt.data.LoanAndGrantData;
import com.josephblough.sbt.transport.SbaTransport;

import android.os.AsyncTask;

public class LoansAndGrantsRetrieverTask
	extends
	AsyncTask<LoansAndGrantsSearchCriteria, Void, List<LoanAndGrantData>> {

    private LoansAndGrantsRetrieverCallback callback;
    
    public LoansAndGrantsRetrieverTask(final LoansAndGrantsRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<LoanAndGrantData> doInBackground(
	    LoansAndGrantsSearchCriteria... criteria) {
	
	// Loan and Grants data can be retrieved by:
	
	// Federal
	if (criteria[0].includeFederal && !criteria[0].includeState && !criteria[0].filterByIndustry && !criteria[0].filterBySpecialty) {
	    return SbaTransport.getFederalLoansAndGrantsData();
	}
	// State
	else if (!criteria[0].includeFederal && criteria[0].includeState && !criteria[0].filterByIndustry && !criteria[0].filterBySpecialty) {
	    return SbaTransport.getStateLoansAndGrantsData(criteria[0].state);
	}
	// Federal and State
	else if (criteria[0].includeFederal && criteria[0].includeState && !criteria[0].filterByIndustry && !criteria[0].filterBySpecialty) {
	    return SbaTransport.getFederalAndStateLoansAndGrantsData(criteria[0].state);
	}
	// by Industry
	else if (!criteria[0].includeState && criteria[0].filterByIndustry && !criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataByIndustry(criteria[0].industry);
	}
	// by Specialty
	else if (!criteria[0].includeState && !criteria[0].filterByIndustry && criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataBySpecialties(criteria[0].specialties);
	}
	// by Industry and Specialty
	else if (!criteria[0].includeState && !criteria[0].filterByIndustry && criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataByIndustryAndSpecialties(criteria[0].industry, criteria[0].specialties);
	}
	// by State and Industry
	else if (criteria[0].includeState && criteria[0].filterByIndustry && !criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataByStateAndIndustry(criteria[0].state, criteria[0].industry);
	}
	// by State and Specialty
	else if (criteria[0].includeState && !criteria[0].filterByIndustry && criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataByStateAndSpecialties(criteria[0].state, criteria[0].specialties);
	}
	// by State, Industry, and Specialty
	else if (criteria[0].includeState && criteria[0].filterByIndustry && criteria[0].filterBySpecialty) {
	    return SbaTransport.getLoansAndGrantsDataByStateAndIndustryAndSpecialties(criteria[0].state, criteria[0].industry, criteria[0].specialties);
	}

	return null;
    }

    @Override
    protected void onPostExecute(List<LoanAndGrantData> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving loans and grants data.");
    }
}
