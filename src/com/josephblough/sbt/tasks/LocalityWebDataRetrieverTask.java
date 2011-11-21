package com.josephblough.sbt.tasks;

import java.util.List;

import com.josephblough.sbt.callbacks.LocalityWebDataRetrieverCallback;
import com.josephblough.sbt.criteria.LocalityWebDataSearchCriteria;
import com.josephblough.sbt.data.LocalityWebData;
import com.josephblough.sbt.transport.SbaTransport;

import android.os.AsyncTask;

public class LocalityWebDataRetrieverTask extends
	AsyncTask<LocalityWebDataSearchCriteria, Void, List<LocalityWebData>> {

    private LocalityWebDataRetrieverCallback callback;
    
    public LocalityWebDataRetrieverTask(final LocalityWebDataRetrieverCallback callback) {
	this.callback = callback;
    }
    
    @Override
    protected List<LocalityWebData> doInBackground(
	    LocalityWebDataSearchCriteria... criteria) {
	
	    // U.S. City and County Web Data can be retrieved by:
	    //	City/County Data - All URLs
	    //	City/County Data - Only Primary URLs
	    //	City/County Data - All Data

	if (criteria[0].type == LocalityWebDataSearchCriteria.TYPE_ALL_URLS_INDEX) {
	    if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_AND_COUNTIES_INDEX) {
		return SbaTransport.getWebDataAllCityAndCountyUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_INDEX) {
		return SbaTransport.getWebDataAllCityUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_COUNTIES_INDEX) {
		return SbaTransport.getWebDataAllCountyUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_CITY_INDEX) {
		return SbaTransport.getWebDataAllUrlsForCity(criteria[0].state, criteria[0].locality);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_COUNTY_DATA_INDEX) {
		return SbaTransport.getWebDataAllUrlsForCounty(criteria[0].state, criteria[0].locality);
	    }
	}
	else if (criteria[0].type == LocalityWebDataSearchCriteria.TYPE_PRIMARY_URLS_INDEX) {
	    if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_AND_COUNTIES_INDEX) {
		return SbaTransport.getWebDataPrimaryCityAndCountyUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_INDEX) {
		return SbaTransport.getWebDataPrimaryCityUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_COUNTIES_INDEX) {
		return SbaTransport.getWebDataPrimaryCountyUrlsForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_CITY_INDEX) {
		return SbaTransport.getWebDataPrimaryUrlsForCity(criteria[0].state, criteria[0].locality);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_COUNTY_DATA_INDEX) {
		return SbaTransport.getWebDataPrimaryUrlsForCounty(criteria[0].state, criteria[0].locality);
	    }
	}
	else if (criteria[0].type == LocalityWebDataSearchCriteria.TYPE_ALL_DATA_INDEX) {
	    if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_AND_COUNTIES_INDEX) {
		return SbaTransport.getWebDataAllCityAndCountyDataForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_INDEX) {
		return SbaTransport.getWebDataAllCityDataForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_ALL_COUNTIES_INDEX) {
		return SbaTransport.getWebDataAllCountyDataForState(criteria[0].state);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_CITY_INDEX) {
		return SbaTransport.getWebDataAllDataForCity(criteria[0].state, criteria[0].locality);
	    }
	    else if (criteria[0].scope == LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_COUNTY_DATA_INDEX) {
		return SbaTransport.getWebDataAllDataForCounty(criteria[0].state, criteria[0].locality);
	    }
	}

	return null;
    }

    @Override
    protected void onPostExecute(List<LocalityWebData> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving web data.");
    }
}
