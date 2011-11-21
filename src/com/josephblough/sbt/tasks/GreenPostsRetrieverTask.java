package com.josephblough.sbt.tasks;

import java.util.HashMap;
import java.util.List;

import com.josephblough.sbt.callbacks.GreenPostRetrieverCallback;
import com.josephblough.sbt.criteria.GreenSearchCriteria;
import com.josephblough.sbt.data.GreenPost;
import com.josephblough.sbt.transport.SbirTransport;

import android.os.AsyncTask;

public class GreenPostsRetrieverTask extends
	AsyncTask<GreenSearchCriteria, Void, List<GreenPost>> {

    private GreenPostRetrieverCallback callback;
    
    public GreenPostsRetrieverTask(final GreenPostRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<GreenPost> doInBackground(
	    GreenSearchCriteria... criteria) {
	
	    // Green
	    //	Download all - http://green.sba.gov/api/green_search.json
	    //	by type - http://green.sba.gov/api/green_search.json?type=presolicitation
	    // 	Keyword search for all or by type - http://green.sba.gov/api/green_search.json?keyword=green
	    //	by Agency - http://green.sba.gov/api/green_search.json?agency=Department%20of%20the%20Navy
	if (criteria[0].downloadAll) {
	    return SbirTransport.getAllGreenPosts();
	}
	else {
	    HashMap<String, String> parameters = new HashMap<String, String>();
	    if (criteria[0].searchTerm != null && !"".equals(criteria[0].searchTerm)) {
		parameters.put(SbirTransport.KEYWORD_PARAMETER, criteria[0].searchTerm);
	    }
	    if (criteria[0].agency != null && !"".equals(criteria[0].agency)) {
		parameters.put(SbirTransport.AGENCY_PARAMETER, criteria[0].agency);
	    }
	    if (criteria[0].type != null && !"".equals(criteria[0].type)) {
		parameters.put(SbirTransport.TYPE_PARAMETER, criteria[0].type);
	    }
	    
	    if (parameters.size() > 0) {
		return SbirTransport.getGreenPosts(parameters);
	    }
	    else {
		return SbirTransport.getAllGreenPosts();
	    }
	}
    }

    @Override
    protected void onPostExecute(List<GreenPost> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving posts.");
    }
}
