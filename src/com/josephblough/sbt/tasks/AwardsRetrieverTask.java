package com.josephblough.sbt.tasks;

import java.util.HashMap;
import java.util.List;

import com.josephblough.sbt.callbacks.AwardsRetrieverCallback;
import com.josephblough.sbt.criteria.AwardsSearchCriteria;
import com.josephblough.sbt.data.Award;
import com.josephblough.sbt.transport.SbirTransport;

import android.os.AsyncTask;

public class AwardsRetrieverTask extends
	AsyncTask<AwardsSearchCriteria, Void, List<Award>> {

    private AwardsRetrieverCallback callback;
    
    public AwardsRetrieverTask(final AwardsRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<Award> doInBackground(
	    AwardsSearchCriteria... criteria) {
	
	    //	by Keyword search - http://www.sbir.gov/api/solicitations.json?keyword=some text without quotes
	    //	by Agency search - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE
	    //	Download all Open - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&open=1
	    //	Download all Closed - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&closed=1
	if (criteria[0].downloadAll) {
	    return SbirTransport.getAllAwardsAsXml();
	}
	else {
	    HashMap<String, String> parameters = new HashMap<String, String>();
	    if (criteria[0].searchTerm != null && !"".equals(criteria[0].searchTerm)) {
		parameters.put(SbirTransport.KEYWORD_PARAMETER, criteria[0].searchTerm);
	    }
	    if (criteria[0].agency != null && !"".equals(criteria[0].agency)) {
		parameters.put(SbirTransport.AGENCY_PARAMETER, criteria[0].agency);
	    }
	    if (criteria[0].company != null && !"".equals(criteria[0].company)) {
		parameters.put(SbirTransport.COMPANY_PARAMETER, criteria[0].company);
	    }
	    if (criteria[0].institution != null && !"".equals(criteria[0].institution)) {
		parameters.put(SbirTransport.RESEARCH_INSTITUTION_PARAMETER, criteria[0].institution);
	    }
	    if (criteria[0].year > 0) {
		parameters.put(SbirTransport.YEAR_PARAMETER, Integer.toString(criteria[0].year));
	    }
	    
	    if (parameters.size() > 0) {
		//return SbirTransport.getAwards(parameters);
		return SbirTransport.getAwardsAsXml(parameters);
	    }
	    else {
		return SbirTransport.getAllAwardsAsXml();
	    }
	}
    }

    @Override
    protected void onPostExecute(List<Award> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving awards.");
    }
}
