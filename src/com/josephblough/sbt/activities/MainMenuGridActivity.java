package com.josephblough.sbt.activities;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.input.AwardsSearchActivity;
import com.josephblough.sbt.activities.input.DistrictOfficeSearchActivity;
import com.josephblough.sbt.activities.input.GenericSearchActivity;
import com.josephblough.sbt.activities.input.GreenSearchActivity;
import com.josephblough.sbt.activities.input.LicensesAndPermitsSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.LoansAndGrantsSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.LocalityWebDataCriteriaActivity;
import com.josephblough.sbt.activities.input.ProgramFinderCriteriaActivity;
import com.josephblough.sbt.activities.input.RecommendedSitesSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.SolicitationsSearchActivity;
import com.josephblough.sbt.adapters.MainGridAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenuGridActivity extends Activity implements OnItemClickListener {

    private static final int BOOKMARK_INDEX = 0;
    private static final int LICENSES_INDEX = 1;
    private static final int LOANS_INDEX = 2;
    private static final int RECOMMENDED_SITES_INDEX = 3;
    private static final int WEB_DATA_INDEX = 4;
    private static final int PROGRAM_FINDER_INDEX = 5;
    private static final int SOLICITATIONS_INDEX = 6;
    private static final int AWARDS_INDEX = 7;
    private static final int GREEN_INDEX = 8;
    private static final int GENERIC_INDEX = 9;
    private static final int OFFICE_INDEX = 10;
    
    private static final String[] items = {"Bookmarks",
	"Licenses and Permits",
	"Loans and Grants",
	"Recommended Sites",
	"City/County Web Data",
	"Small Business Programs",
	"Solicitations",
	"Awards",
	"Green Search",
	"Search",
    	"SBA District Offices"};

    private GridView gridView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_grid_layout);
        
	gridView = (GridView) findViewById(R.id.main_gridview);
	//ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_gallery_item, items);
	MainGridAdapter adapter = new MainGridAdapter(this, items);
	gridView.setAdapter(adapter);
	gridView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	switch (position) {
	case BOOKMARK_INDEX:
	{
	    ApplicationController app = (ApplicationController)getApplicationContext();
	    if (app.bookmarks.isEmpty()) {
		Toast toast = Toast.makeText(this, "No bookmarks.  Bookmarks can be added on any of the other menu items.", 
			Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	    }
	    else {
		Intent intent = new Intent(this, BookmarksActivity.class);
		startActivity(intent);
	    }
	}
	    break;
	case LICENSES_INDEX:
	{
	    Intent intent = new Intent(this, LicensesAndPermitsSearchCriteriaActivity.class);
	    startActivity(intent);
	}
	    break;
	case LOANS_INDEX:
	{
	    Intent intent = new Intent(this, LoansAndGrantsSearchCriteriaActivity.class);
	    startActivity(intent);
	}
	    break;
	case RECOMMENDED_SITES_INDEX:
	{
	    Intent intent = new Intent(this, RecommendedSitesSearchCriteriaActivity.class);
	    startActivity(intent);
	}
	    break;
	case WEB_DATA_INDEX:
	{
	    Intent intent = new Intent(this, LocalityWebDataCriteriaActivity.class);
	    startActivity(intent);
	}
	    break;
	case PROGRAM_FINDER_INDEX:
	{
	    Intent intent = new Intent(this, ProgramFinderCriteriaActivity.class);
	    startActivity(intent);
	}
	    break;
	case SOLICITATIONS_INDEX:
	{
	    Intent intent = new Intent(this, SolicitationsSearchActivity.class);
	    startActivity(intent);
	}
	    break;
	case AWARDS_INDEX:
	{
	    Intent intent = new Intent(this, AwardsSearchActivity.class);
	    startActivity(intent);
	}
	    break;
	case GREEN_INDEX:
	{
	    Intent intent = new Intent(this, GreenSearchActivity.class);
	    startActivity(intent);
	}
	    break;
	case GENERIC_INDEX:
	{
	    Intent intent = new Intent(this, GenericSearchActivity.class);
	    startActivity(intent);
	}
	    break;
	case OFFICE_INDEX:
	{
	    Intent intent = new Intent(this, DistrictOfficeSearchActivity.class);
	    startActivity(intent);
	}
	    break;
	}
    }

    public int getMenuImage(final int position) {
	switch (position) {
	case BOOKMARK_INDEX:
	    return R.drawable.bookmark;
	case LICENSES_INDEX:
	    return R.drawable.license;
	case LOANS_INDEX:
	    return R.drawable.contract;
	case RECOMMENDED_SITES_INDEX:
	    return R.drawable.recommended_sites;
	case WEB_DATA_INDEX:
	    return R.drawable.webdata;
	case PROGRAM_FINDER_INDEX:
	    return R.drawable.document_wrench;
	case SOLICITATIONS_INDEX:
	    return R.drawable.solicitations;
	case AWARDS_INDEX:
	    return R.drawable.award;
	case GREEN_INDEX:
	    return R.drawable.green_search;
	case GENERIC_INDEX:
	    return R.drawable.generic_search;
	case OFFICE_INDEX:
	    return R.drawable.house;
	}
	
	return R.drawable.document_wrench;
    }

}
