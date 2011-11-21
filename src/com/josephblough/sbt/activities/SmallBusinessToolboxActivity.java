package com.josephblough.sbt.activities;

import java.util.List;

import com.josephblough.sbt.R;
import com.josephblough.sbt.data.Award;
import com.josephblough.sbt.data.GenericPost;
import com.josephblough.sbt.data.GreenPost;
import com.josephblough.sbt.data.LicenseAndPermitDataCollection;
import com.josephblough.sbt.data.LoanAndGrantData;
import com.josephblough.sbt.data.LocalityWebData;
import com.josephblough.sbt.data.RecommendedSite;
import com.josephblough.sbt.data.SbaDistrictOffice;
import com.josephblough.sbt.data.SmallBusinessProgram;
import com.josephblough.sbt.data.Solicitation;
import com.josephblough.sbt.transport.SbaTransport;
import com.josephblough.sbt.transport.SbirTransport;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SmallBusinessToolboxActivity extends Activity {
    
    private static final String TAG = "SmallBusinessToolboxActivity";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // SBA
        ((Button)findViewById(R.id.test_licenses_and_permits_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        LicenseAndPermitDataCollection data = SbaTransport.getLicenseAndPermitDataByBusinessTypeStateAndCounty("child care services", "CA", "los angeles county");
	        Log.d(TAG, "" + data);
	    }
	});

        ((Button)findViewById(R.id.test_loans_and_grants_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<LoanAndGrantData> data = SbaTransport.getFederalLoansAndGrantsData();
	        Log.d(TAG, "" + data);
	    }
	});

        ((Button)findViewById(R.id.test_recommended_sites_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<RecommendedSite> data = SbaTransport.getAllRecommendedSites();
	        Log.d(TAG, "" + data);
	    }
	});

        ((Button)findViewById(R.id.test_web_data_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<LocalityWebData> data = SbaTransport.getWebDataAllCityAndCountyDataForState("MI");
	        Log.d(TAG, "" + data);
	    }
	});
        
        // SBIR
        ((Button)findViewById(R.id.test_program_finder_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<SmallBusinessProgram> data = SbirTransport.findSmallBusinessProgramsByIndustry("child-care");
	        Log.d(TAG, "" + data);
	    }
	});
        ((Button)findViewById(R.id.test_solicitations_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<Solicitation> data = SbirTransport.getSolicitationsByAgency("DOT");
	        Log.d(TAG, "" + data);
	    }
	});
        ((Button)findViewById(R.id.test_awards_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<Award> data = SbirTransport.getAwardsByKeywordAndAgency("mars", "DOE");
	        Log.d(TAG, "" + data);
	    }
	});
        ((Button)findViewById(R.id.test_green_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<GreenPost> data = SbirTransport.getGreenPostsByType("presolicitation");
	        Log.d(TAG, "" + data);
	    }
	});
        ((Button)findViewById(R.id.test_generic_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<GenericPost> data = SbirTransport.getGenericPostsByKeyword("green");
	        Log.d(TAG, "" + data);
	    }
	});
        ((Button)findViewById(R.id.test_district_offices_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
	        List<SbaDistrictOffice> data = SbirTransport.getDistrictOfficesByZipCode("91203");
	        Log.d(TAG, "" + data);
	    }
	});
    }
}