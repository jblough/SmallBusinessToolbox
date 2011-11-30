package com.josephblough.sbt.activities;

import org.json.JSONException;

import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.input.AwardsSearchActivity;
import com.josephblough.sbt.activities.input.GenericSearchActivity;
import com.josephblough.sbt.activities.input.GreenSearchActivity;
import com.josephblough.sbt.activities.input.LicensesAndPermitsSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.LoansAndGrantsSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.LocalityWebDataCriteriaActivity;
import com.josephblough.sbt.activities.input.ProgramFinderCriteriaActivity;
import com.josephblough.sbt.activities.input.RecommendedSitesSearchCriteriaActivity;
import com.josephblough.sbt.activities.input.SolicitationsSearchActivity;
import com.josephblough.sbt.activities.results.AwardsSearchResultsActivity;
import com.josephblough.sbt.activities.results.GenericSearchResultsActivity;
import com.josephblough.sbt.activities.results.GreenSearchResultsActivity;
import com.josephblough.sbt.activities.results.LicensesAndPermitsSearchResultsActivity;
import com.josephblough.sbt.activities.results.LoansAndGrantsSearchResultsActivity;
import com.josephblough.sbt.activities.results.LocalityWebDataResultsActivity;
import com.josephblough.sbt.activities.results.ProgramFinderSearchResultsActivity;
import com.josephblough.sbt.activities.results.RecommendedSitesSearchResultsActivity;
import com.josephblough.sbt.activities.results.SolicitationsSearchResultsActivity;
import com.josephblough.sbt.criteria.AwardsSearchCriteria;
import com.josephblough.sbt.criteria.GenericSearchCriteria;
import com.josephblough.sbt.criteria.GreenSearchCriteria;
import com.josephblough.sbt.criteria.LicensesAndPermitsSearchCriteria;
import com.josephblough.sbt.criteria.LoansAndGrantsSearchCriteria;
import com.josephblough.sbt.criteria.LocalityWebDataSearchCriteria;
import com.josephblough.sbt.criteria.ProgramFinderSearchCriteria;
import com.josephblough.sbt.criteria.RecommendedSitesSearchCriteria;
import com.josephblough.sbt.criteria.SolicitationsSearchCriteria;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ShortcutActivity extends ListActivity implements OnItemClickListener {

    private static final String TAG = "ShortcutActivity";
    
    public static final String CREATE_LAUNCHER_KEY = "com.josephblough.sbt.activities.ShortcutActivity.CreatingLauncher";
    public static final String SEARCH_TYPE = "com.josephblough.sbt.activities.ShortcutActivity.SearchType";
    public static final String CRITERIA = "com.josephblough.sbt.activities.ShortcutActivity.SearchCriteria";
    
    public static final int BOOKMARKS_INDEX = 0;
    public static final int LICENSES_INDEX = 1;
    public static final int LOANS_INDEX = 2;
    public static final int RECOMMENDED_SITES_INDEX = 3;
    public static final int URLS_INDEX = 4;
    public static final int PROGRAM_FINDER_INDEX = 5;
    public static final int SOLICITATIONS_INDEX = 6;
    public static final int AWARDS_INDEX = 7;
    public static final int GREEN_INDEX = 8;
    public static final int GENERIC_INDEX = 9;
    
    private static final int[] icons = {R.drawable.bookmark, R.drawable.license, R.drawable.contract, 
	R.drawable.recommended_sites, R.drawable.webdata, R.drawable.document_wrench, 
	R.drawable.solicitations, R.drawable.award, R.drawable.green_search, R.drawable.generic_search};
    
    private static final String[] items = {"Bookmarks", "Licenses and Permits",
	"Loans and Grants",
	"Recommended Sites",
	"City/County Websites",
	"Small Business Programs",
	"Solicitations",
	"Awards",
	"Green Search",
	"Search"};

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
            // Create a shortcut
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            setListAdapter(adapter);

            getListView().setOnItemClickListener(this);
        }
        else {
            // Perform the search
            final int searchType = intent.getIntExtra(SEARCH_TYPE, 0);
            final String criteria = intent.getStringExtra(CRITERIA);
            try {
        	performSearch(searchType, criteria);
            }
            catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
        	Toast.makeText(this, "There was an error reading search criteria", Toast.LENGTH_LONG).show();
            }
        }
        
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	switch (position) {
	case BOOKMARKS_INDEX:
	    createLauncher(BOOKMARKS_INDEX, null);
	    break;
	case LICENSES_INDEX:
	{
	    Intent intent = new Intent(this, LicensesAndPermitsSearchCriteriaActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case LOANS_INDEX:
	{
	    Intent intent = new Intent(this, LoansAndGrantsSearchCriteriaActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case RECOMMENDED_SITES_INDEX:
	{
	    Intent intent = new Intent(this, RecommendedSitesSearchCriteriaActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case URLS_INDEX:
	{
	    Intent intent = new Intent(this, LocalityWebDataCriteriaActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case PROGRAM_FINDER_INDEX:
	{
	    Intent intent = new Intent(this, ProgramFinderCriteriaActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case SOLICITATIONS_INDEX:
	{
	    Intent intent = new Intent(this, SolicitationsSearchActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case AWARDS_INDEX:
	{
	    Intent intent = new Intent(this, AwardsSearchActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case GREEN_INDEX:
	{
	    Intent intent = new Intent(this, GreenSearchActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	case GENERIC_INDEX:
	{
	    Intent intent = new Intent(this, GenericSearchActivity.class);
	    intent.putExtra(CREATE_LAUNCHER_KEY, true);
	    startActivityForResult(intent, 0);
	}
	    break;
	}
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If criteria has been selected then load the details data
        if (resultCode == RESULT_OK && data != null &&
        	data.hasExtra(SEARCH_TYPE)) {
            final int searchType = data.getIntExtra(SEARCH_TYPE, 0);
            final String criteria = data.getStringExtra(CRITERIA);

            createLauncher(searchType, criteria);
        }
        else {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        }
    }
    
    private void createLauncher(final int searchType, final String criteria) {
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(this, this.getClass().getName());
        shortcutIntent.putExtra(SEARCH_TYPE, searchType);
        if (criteria != null)
            shortcutIntent.putExtra(CRITERIA, criteria); // Pass back the criteria from the activity

        // Set up the container intent (the response to the caller)
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, items[searchType]);
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this, icons[searchType]);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

        // Return the result to the launcher
        setResult(RESULT_OK, intent);
        finish();
    }
    
    private void performSearch(final int searchType, final String criteria) throws JSONException {
	switch (searchType) {
	case BOOKMARKS_INDEX:
	{
	    Intent intent = new Intent(this, BookmarksActivity.class);
	    startActivity(intent);
	}
	    break;
	case LICENSES_INDEX:
	{
	    Intent intent = new Intent(this, LicensesAndPermitsSearchResultsActivity.class);
	    intent.putExtra(LicensesAndPermitsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new LicensesAndPermitsSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case LOANS_INDEX:
	{
	    Intent intent = new Intent(this, LoansAndGrantsSearchResultsActivity.class);
	    intent.putExtra(LoansAndGrantsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new LoansAndGrantsSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case RECOMMENDED_SITES_INDEX:
	{
	    Intent intent = new Intent(this, RecommendedSitesSearchResultsActivity.class);
	    intent.putExtra(RecommendedSitesSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new RecommendedSitesSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case URLS_INDEX:
	{
	    Intent intent = new Intent(this, LocalityWebDataResultsActivity.class);
	    intent.putExtra(LocalityWebDataResultsActivity.SEARCH_CRITERIA_EXTRA, new LocalityWebDataSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case PROGRAM_FINDER_INDEX:
	{
	    Intent intent = new Intent(this, ProgramFinderSearchResultsActivity.class);
	    intent.putExtra(ProgramFinderSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new ProgramFinderSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case SOLICITATIONS_INDEX:
	{
	    Intent intent = new Intent(this, SolicitationsSearchResultsActivity.class);
	    intent.putExtra(SolicitationsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new SolicitationsSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case AWARDS_INDEX:
	{
	    Intent intent = new Intent(this, AwardsSearchResultsActivity.class);
	    intent.putExtra(AwardsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new AwardsSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case GREEN_INDEX:
	{
	    Intent intent = new Intent(this, GreenSearchResultsActivity.class);
	    intent.putExtra(GreenSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new GreenSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	case GENERIC_INDEX:
	{
	    Intent intent = new Intent(this, GenericSearchResultsActivity.class);
	    intent.putExtra(GenericSearchResultsActivity.SEARCH_CRITERIA_EXTRA, new GenericSearchCriteria(criteria));
	    startActivity(intent);
	}
	    break;
	}
	finish(); // Don't let the user back up to this screen once the spelling test has started
    }
}
