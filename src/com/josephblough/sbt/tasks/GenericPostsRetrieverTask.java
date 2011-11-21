package com.josephblough.sbt.tasks;

import java.util.HashMap;
import java.util.List;

import com.josephblough.sbt.callbacks.GenericPostRetrieverCallback;
import com.josephblough.sbt.criteria.GenericSearchCriteria;
import com.josephblough.sbt.data.GenericPost;
import com.josephblough.sbt.transport.SbirTransport;

import android.os.AsyncTask;

public class GenericPostsRetrieverTask extends
	AsyncTask<GenericSearchCriteria, Void, List<GenericPost>> {

    private GenericPostRetrieverCallback callback;
    
    public GenericPostsRetrieverTask(final GenericPostRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<GenericPost> doInBackground(
	    GenericSearchCriteria... criteria) {
	
	    // Generic
	    //	Download all - http://green.sba.gov/api/sb_search.json
	    //	by type - http://green.sba.gov/api/sb_search.json?type=presolicitation
	    // 	Keyword search for all or by type - http://green.sba.gov/api/sb_search.json?keyword=green
	    //	by Agency - http://green.sba.gov/api/sb_search.json?agency=Department%20of%20the%20Navy
	if (criteria[0].downloadAll) {
	    return SbirTransport.getAllGenericPostsAsXml();
	}
	else {
	    HashMap<String, String> parameters = new HashMap<String, String>();
	    if (criteria[0].searchTerm != null && !"".equals(criteria[0].searchTerm)) {
		parameters.put(SbirTransport.KEYWORD_PARAMETER, criteria[0].searchTerm);
	    }
	    if (criteria[0].onlyNew) {
		parameters.put(SbirTransport.NEW_PARAMETER, "true");
	    }
	    if (criteria[0].agency != null && !"".equals(criteria[0].agency)) {
		parameters.put(SbirTransport.AGENCY_PARAMETER, criteria[0].agency);
	    }
	    if (criteria[0].type != null && !"".equals(criteria[0].type)) {
		parameters.put(SbirTransport.TYPE_PARAMETER, criteria[0].type.toLowerCase());
	    }
	    
	    if (parameters.size() > 0) {
		return SbirTransport.getGenericPostsAsXml(parameters);
	    }
	    else {
		return SbirTransport.getAllGenericPostsAsXml();
	    }
	}
    }

    @Override
    protected void onPostExecute(List<GenericPost> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving posts.");
    }
}
