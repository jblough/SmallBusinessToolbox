package com.josephblough.sbt.tasks;

import java.util.List;

import com.josephblough.sbt.callbacks.ProgramFinderRetrieverCallback;
import com.josephblough.sbt.criteria.ProgramFinderSearchCriteria;
import com.josephblough.sbt.data.SmallBusinessProgram;
import com.josephblough.sbt.transport.SbirTransport;

import android.os.AsyncTask;

public class ProgramFinderRetrieverTask extends
	AsyncTask<ProgramFinderSearchCriteria, Void, List<SmallBusinessProgram>> {

    private ProgramFinderRetrieverCallback callback;
    
    public ProgramFinderRetrieverTask(final ProgramFinderRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<SmallBusinessProgram> doInBackground(
	    ProgramFinderSearchCriteria... criteria) {
	
	switch (criteria[0].type) {
	case ProgramFinderSearchCriteria.TYPE_BY_FEDERAL_INDEX:
	case ProgramFinderSearchCriteria.TYPE_BY_PRIVATE_INDEX:
	case ProgramFinderSearchCriteria.TYPE_BY_NATIONAL_INDEX:
	case ProgramFinderSearchCriteria.TYPE_BY_STATE_INDEX:
	    return SbirTransport.findSmallBusinessProgramsByLocation(criteria[0].criteria);
	case ProgramFinderSearchCriteria.TYPE_BY_INDUSTRY_INDEX:
	    return SbirTransport.findSmallBusinessProgramsByIndustry(criteria[0].criteria);
	case ProgramFinderSearchCriteria.TYPE_BY_TYPE_INDEX:
	    return SbirTransport.findSmallBusinessProgramsByProgramType(criteria[0].criteria);
	case ProgramFinderSearchCriteria.TYPE_BY_QUALIFICATION_INDEX:
	    return SbirTransport.findSmallBusinessProgramsByQualification(criteria[0].criteria);
	default:
	    break;
	}

	return null;
    }

    @Override
    protected void onPostExecute(List<SmallBusinessProgram> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving small business programs.");
    }
}
