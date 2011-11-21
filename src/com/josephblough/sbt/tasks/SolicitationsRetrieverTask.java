package com.josephblough.sbt.tasks;

import java.util.List;

import com.josephblough.sbt.callbacks.SolicitationsRetrieverCallback;
import com.josephblough.sbt.criteria.SolicitationsSearchCriteria;
import com.josephblough.sbt.data.Solicitation;
import com.josephblough.sbt.transport.SbirTransport;

import android.os.AsyncTask;

public class SolicitationsRetrieverTask extends
	AsyncTask<SolicitationsSearchCriteria, Void, List<Solicitation>> {

    private SolicitationsRetrieverCallback callback;
    
    public SolicitationsRetrieverTask(final SolicitationsRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<Solicitation> doInBackground(
	    SolicitationsSearchCriteria... criteria) {
	
	    //	by Keyword search - http://www.sbir.gov/api/solicitations.json?keyword=some text without quotes
	    //	by Agency search - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE
	    //	Download all Open - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&open=1
	    //	Download all Closed - http://www.sbir.gov/api/solicitations.json?keyword=mars&agency=DOE&closed=1
	
	if (criteria[0].filter == SolicitationsSearchCriteria.SHOW_ALL_INDEX) {
	    if ((criteria[0].keyword == null || "".equals(criteria[0].keyword)) &&
		    (criteria[0].agency == null || "".equals(criteria[0].agency))) {
		return SbirTransport.getSolicitations();
	    }
	    else if (criteria[0].agency == null || "".equals(criteria[0].agency)) {
		return SbirTransport.getSolicitationsByKeyword(criteria[0].keyword);
	    }
	    else if (criteria[0].keyword == null || "".equals(criteria[0].keyword)) {
		return SbirTransport.getSolicitationsByAgency(criteria[0].agency);
	    }
	    else {
		return SbirTransport.getSolicitationsByKeywordAndAgency(criteria[0].keyword, criteria[0].agency);
	    }
	}
	else if (criteria[0].filter == SolicitationsSearchCriteria.SHOW_OPEN_INDEX) {
	    if ((criteria[0].keyword == null || "".equals(criteria[0].keyword)) &&
		    (criteria[0].agency == null || "".equals(criteria[0].agency))) {
		return SbirTransport.getOpenSolicitations();
	    }
	    else if (criteria[0].agency == null || "".equals(criteria[0].agency)) {
		return SbirTransport.getOpenSolicitationsByKeyword(criteria[0].keyword);
	    }
	    else if (criteria[0].keyword == null || "".equals(criteria[0].keyword)) {
		return SbirTransport.getOpenSolicitationsByAgency(criteria[0].agency);
	    }
	    else {
		return SbirTransport.getOpenSolicitationsByKeywordAndAgency(criteria[0].keyword, criteria[0].agency);
	    }
	}
	else if (criteria[0].filter == SolicitationsSearchCriteria.SHOW_CLOSED_INDEX) {
	    if ((criteria[0].keyword == null || "".equals(criteria[0].keyword)) &&
		    (criteria[0].agency == null || "".equals(criteria[0].agency))) {
		return SbirTransport.getClosedSolicitations();
	    }
	    else if (criteria[0].agency == null || "".equals(criteria[0].agency)) {
		return SbirTransport.getClosedSolicitationsByKeyword(criteria[0].keyword);
	    }
	    else if (criteria[0].keyword == null || "".equals(criteria[0].keyword)) {
		return SbirTransport.getClosedSolicitationsByAgency(criteria[0].agency);
	    }
	    else {
		return SbirTransport.getClosedSolicitationsByKeywordAndAgency(criteria[0].keyword, criteria[0].agency);
	    }
	}

	return null;
    }

    @Override
    protected void onPostExecute(List<Solicitation> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving solicitations.");
    }
}
