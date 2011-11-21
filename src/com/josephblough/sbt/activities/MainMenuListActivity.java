package com.josephblough.sbt.activities;

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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class MainMenuListActivity extends ListActivity implements OnItemClickListener {

    private static final int LICENSES_INDEX = 0;
    private static final int LOANS_INDEX = 1;
    private static final int RECOMMENDED_SITES_INDEX = 2;
    private static final int URLS_INDEX = 3;
    private static final int PROGRAM_FINDER_INDEX = 4;
    private static final int SOLICITATIONS_INDEX = 5;
    private static final int AWARDS_INDEX = 6;
    private static final int GREEN_INDEX = 7;
    private static final int GENERIC_INDEX = 8;
    private static final int OFFICE_INDEX = 9;
    
    private static final String[] items = {"Licenses and Permits",
	"Loans and Grants",
	"Sites Recommended by the SBA",
	"City and County Websites",
	"Small Business Program Finder",
	"Search Solicitations",
	"Awards",
	"Green Search",
	"Search",
    	"SBA District Offices"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
	setListAdapter(adapter);

	getListView().setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	switch (position) {
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
	case URLS_INDEX:
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
}
