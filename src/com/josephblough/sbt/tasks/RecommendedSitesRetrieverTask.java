package com.josephblough.sbt.tasks;

import java.util.List;

import com.josephblough.sbt.callbacks.RecommendedSitesRetrieverCallback;
import com.josephblough.sbt.criteria.RecommendedSitesSearchCriteria;
import com.josephblough.sbt.data.RecommendedSite;
import com.josephblough.sbt.transport.SbaTransport;

import android.os.AsyncTask;

public class RecommendedSitesRetrieverTask extends
	AsyncTask<RecommendedSitesSearchCriteria, Void, List<RecommendedSite>> {

    private RecommendedSitesRetrieverCallback callback;
    
    public RecommendedSitesRetrieverTask(final RecommendedSitesRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<RecommendedSite> doInBackground(
	    RecommendedSitesSearchCriteria... criteria) {
	
	    //	All recommended sites
	    //	by Keyword
	    //	by Category
	    //	by Master Term
	    //	by Domain
	if (criteria[0].searchBy == RecommendedSitesSearchCriteria.ALL_SITES_INDEX) {
	    return SbaTransport.getAllRecommendedSites();
	}
	else if (criteria[0].searchBy == RecommendedSitesSearchCriteria.KEYWORD_SEARCH_INDEX) {
	    return SbaTransport.getRecommendedSitesByKeyword(criteria[0].searchTerm);
	}
	else if (criteria[0].searchBy == RecommendedSitesSearchCriteria.CATEGORY_SEARCH_INDEX) {
	    return SbaTransport.getRecommendedSitesByCategory(criteria[0].searchTerm);
	}
	else if (criteria[0].searchBy == RecommendedSitesSearchCriteria.MASTER_TERM_SEARCH_INDEX) {
	    return SbaTransport.getRecommendedSitesByMasterTerm(criteria[0].searchTerm);
	}
	else if (criteria[0].searchBy == RecommendedSitesSearchCriteria.DOMAIN_FILTER_INDEX) {
	    return SbaTransport.getRecommendedSitesByDomain(criteria[0].searchTerm);
	}

	return null;
    }

    @Override
    protected void onPostExecute(List<RecommendedSite> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving recommended sites.");
    }
}
