package com.josephblough.sbt.tasks;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.callbacks.DistrictOfficeRetrieverCallback;
import com.josephblough.sbt.criteria.DistrictOfficeSearchCriteria;
import com.josephblough.sbt.data.SbaDistrictOffice;
import com.josephblough.sbt.transport.SbirTransport;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class DistrictOfficeRetrieverTask extends
	AsyncTask<DistrictOfficeSearchCriteria, Void, List<SbaDistrictOffice>> {

    private Context context;
    private DistrictOfficeRetrieverCallback callback;
    
    public DistrictOfficeRetrieverTask(final Context context, final DistrictOfficeRetrieverCallback callback) {
	this.context = context;
	this.callback = callback;
    }
    
    @Override
    protected List<SbaDistrictOffice> doInBackground(DistrictOfficeSearchCriteria... criteria) {
	
	if (criteria[0].type == DistrictOfficeSearchCriteria.FIND_NEAREST_OFFICE_INDEX ||
		criteria[0].criteria == null && "".equals(criteria[0].criteria))
	    return findNearestOffices();
	else if (criteria[0].type == DistrictOfficeSearchCriteria.FIND_BY_STATE_INDEX)
	    return findOfficesForState(criteria[0].criteria);
	else if (criteria[0].type == DistrictOfficeSearchCriteria.FIND_BY_ZIP_CODE_INDEX)
	    return SbirTransport.getDistrictOfficesByZipCode(criteria[0].criteria);
	else
	    return new ArrayList<SbaDistrictOffice>();
    }

    @Override
    protected void onPostExecute(List<SbaDistrictOffice> results) {
        super.onPostExecute(results);
        
        if (results != null)
            this.callback.success(results);
        else
            this.callback.error("Error retrieving offices.");
    }
    
    private List<SbaDistrictOffice> findNearestOffices() {
	List<SbaDistrictOffice> nearestOffices = new ArrayList<SbaDistrictOffice>();
	
	ApplicationController app = (ApplicationController)context.getApplicationContext();
	List<SbaDistrictOffice> offices = SbirTransport.getAllDistrictOffices();
	if (offices != null && app.location != null) {
	    //Location city = new Location(app.location.getProvider());
	    SbaDistrictOffice closestOffice = null;
	    float distanceToBeat = 100000f;
	    for (SbaDistrictOffice office : offices) {
		//city.setLatitude(Double.valueOf(office.latitude));
		//city.setLongitude(Double.valueOf(office.longitude));
		float results[] = new float[1];
		Location.distanceBetween(app.location.getLatitude(), app.location.getLongitude(), 
			Double.valueOf(office.latitude), Double.valueOf(office.longitude), results);
		//float distance = app.location.distanceTo(city);
		if (results[0] < distanceToBeat) {
		    closestOffice = office;
		    distanceToBeat = results[0];
		}
	    }
	    if (closestOffice != null)
		nearestOffices.add(closestOffice);
	    else {
		nearestOffices.addAll(offices);
	    }
	}
	return nearestOffices;
    }

    private List<SbaDistrictOffice> findOfficesForState(final String state) {
	List<SbaDistrictOffice> stateOffices = new ArrayList<SbaDistrictOffice>();
	
	List<SbaDistrictOffice> offices = SbirTransport.getAllDistrictOffices();
	if (offices != null) {
	    for (SbaDistrictOffice office : offices) {
		if (state.equals(office.province))
		    stateOffices.add(office);
	    }
	}
	return stateOffices;
    }
}
