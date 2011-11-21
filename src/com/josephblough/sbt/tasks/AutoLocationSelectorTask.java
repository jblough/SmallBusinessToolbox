package com.josephblough.sbt.tasks;

import java.io.IOException;
import java.util.List;

import com.josephblough.sbt.ApplicationController;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

public class AutoLocationSelectorTask extends AsyncTask<Void, Void, Void> {

    private final static String TAG = "AutoLocationSelectorTask";
    
    private ApplicationController app;
    private Location location = null;
    private Address address = null;
    
    public AutoLocationSelectorTask(ApplicationController app) {
	this.app = app;
    }
    
    @Override
    protected Void doInBackground(Void... arg0) {
	LocationManager locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
	Criteria criteria = new Criteria();
	String provider = locationManager.getBestProvider(criteria, false);
	location = locationManager.getLastKnownLocation(provider);
	if (location != null) {
	    Geocoder geocoder = new Geocoder(app);
	    try {
		List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
		if (addresses.size() > 0) {
		    address = addresses.get(0);
		}
	    }
	    catch (IOException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}

	return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        app.location = this.location;
        app.address = this.address;
    }
}
