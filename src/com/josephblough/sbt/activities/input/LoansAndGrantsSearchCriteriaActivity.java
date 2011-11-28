package com.josephblough.sbt.activities.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.LoansAndGrantsSearchResultsActivity;
import com.josephblough.sbt.criteria.LoansAndGrantsSearchCriteria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class LoansAndGrantsSearchCriteriaActivity extends Activity {

    private final static String TAG = "LoansAndGrantsSearchCriteriaActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

    private CheckBox federalCheckBox;
    private CheckBox stateCheckBox;
    private Spinner stateSpinner;
    private CheckBox industryCheckBox;
    private Spinner industrySpinner;
    private CheckBox specialtiesCheckBox;
    private ScrollView specialtiesScrollView;
    private CheckBox generalPurposeCheckBox;
    private CheckBox existingBusinessCheckBox;
    private CheckBox exportingBusinessCheckBox;
    private CheckBox contractorCheckBox;
    private CheckBox greenCheckBox;
    private CheckBox militaryCheckBox;
    private CheckBox minorityCheckBox;
    private CheckBox womanCheckBox;
    private CheckBox disabledCheckBox;
    private CheckBox ruralCheckBox;
    private CheckBox disasterCheckBox;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.loans_and_grants_search_criteria);
        
        federalCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_include_federal_checkbox);
        stateCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_state_filter_checkbox);
        stateSpinner = (Spinner)findViewById(R.id.loans_and_grants_state_spinner);
        industryCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_industry_filter_checkbox);
        industrySpinner = (Spinner)findViewById(R.id.loans_and_grants_industry_spinner);
        specialtiesCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_specialties_checkbox);
        specialtiesScrollView = (ScrollView)findViewById(R.id.loans_and_grants_specialties_wrapper);
        generalPurposeCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_general_purpose_specialty);
        existingBusinessCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_existing_specialty);
        exportingBusinessCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_exporting_specialty);
        contractorCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_contractor_specialty);
        greenCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_green_specialty);
        militaryCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_military_specialty);
        minorityCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_minority_specialty);
        womanCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_woman_specialty);
        disabledCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_disabled_specialty);
        ruralCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_rural_specialty);
        disasterCheckBox = (CheckBox)findViewById(R.id.loans_and_grants_disaster_specialty);
        
        // Hide the State spinner if not filtering by state
        stateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		stateSpinner.setVisibility((isChecked) ? View.VISIBLE : View.GONE);
		updateFederalCheckbox();
	    }
	});
        
        // Hide the Industries spinner if not filtering by industry
        industryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		industrySpinner.setVisibility((isChecked) ? View.VISIBLE : View.GONE);
		updateFederalCheckbox();
	    }
	});
        
        // Hide the Specialties listing if not filtering by specialties
        specialtiesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		specialtiesScrollView.setVisibility((isChecked) ? View.VISIBLE : View.GONE);
		updateFederalCheckbox();
	    }
	});
        
        // Default to include federal results
        federalCheckBox.setChecked(true);
        
        ((Button)findViewById(R.id.loans_and_grants_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
        
        preselectCurrentState();
    }
    
    private void updateFederalCheckbox() {
        // Only enable the federal checkbox if industry and specialties are being filtered on
	if (industryCheckBox.isChecked() || specialtiesCheckBox.isChecked()) {
	    federalCheckBox.setEnabled(false);
	    federalCheckBox.setChecked(!stateCheckBox.isChecked());
	    stateCheckBox.setText("Filter by State");
	}
	else {
	    federalCheckBox.setEnabled(true);
	    stateCheckBox.setText("Include State Results");
	}
    }
    
    private void search() {
	LoansAndGrantsSearchCriteria criteria = createCriteria();
	
	Intent intent = new Intent(this, LoansAndGrantsSearchResultsActivity.class);
	intent.putExtra(LoansAndGrantsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private LoansAndGrantsSearchCriteria createCriteria() {
	/*
	    private CheckBox generalPurposeCheckBox;
	    private CheckBox existingBusinessCheckBox;
	    private CheckBox exportingBusinessCheckBox;
	    private CheckBox contractorCheckBox;
	    private CheckBox greenCheckBox;
	    private CheckBox militaryCheckBox;
	    private CheckBox minorityCheckBox;
	    private CheckBox womanCheckBox;
	    private CheckBox disabledCheckBox;
	    private CheckBox ruralCheckBox;
	    private CheckBox disasterCheckBox;
	 */
	List<String> specialties = new ArrayList<String>();
	if (specialtiesCheckBox.isChecked()) {
	    if (generalPurposeCheckBox.isChecked()) {
		specialties.add("general_purpose");
	    }
	    if (existingBusinessCheckBox.isChecked()) {
		specialties.add("development");
	    }
	    if (exportingBusinessCheckBox.isChecked()) {
		specialties.add("exporting");
	    }
	    if (contractorCheckBox.isChecked()) {
		specialties.add("contractor");
	    }
	    if (greenCheckBox.isChecked()) {
		specialties.add("green");
	    }
	    if (militaryCheckBox.isChecked()) {
		specialties.add("military");
	    }
	    if (minorityCheckBox.isChecked()) {
		specialties.add("minority");
	    }
	    if (womanCheckBox.isChecked()) {
		specialties.add("woman");
	    }
	    if (disabledCheckBox.isChecked()) {
		specialties.add("disabled");
	    }
	    if (ruralCheckBox.isChecked()) {
		specialties.add("rural");
	    }
	    if (disasterCheckBox.isChecked()) {
		specialties.add("disaster");
	    }
	}
	// Double check that at least one specialty was selected
	boolean filterBySpecialties = specialtiesCheckBox.isChecked() && (specialties.size() > 0);
	
	final String state = ((ApplicationController)getApplicationContext()).stateLookupMap.get(
		(String)stateSpinner.getSelectedItem());
	
	return new LoansAndGrantsSearchCriteria(federalCheckBox.isChecked(), 
		stateCheckBox.isChecked(), state, industryCheckBox.isChecked(), 
		((String)industrySpinner.getSelectedItem()).toLowerCase(), filterBySpecialties, specialties);
    }
    
    private void loadSearch(final LoansAndGrantsSearchCriteria criteria) {
	federalCheckBox.setChecked(criteria.includeFederal);
	stateCheckBox.setChecked(criteria.includeState);
	industryCheckBox.setChecked(criteria.filterByIndustry);
	if (criteria.industry == null) {
	    industrySpinner.setSelection(0);
	}
	else {
	    for (int i=0; i<industrySpinner.getAdapter().getCount(); i++) {
		if (criteria.industry.equalsIgnoreCase((String)industrySpinner.getAdapter().getItem(i))) {
		    industrySpinner.setSelection(i);
		    break;
		}
	    }
	}
	
	specialtiesCheckBox.setChecked(criteria.filterBySpecialty);
	// Specialties
	generalPurposeCheckBox.setChecked(criteria.specialties.contains("general_purpose"));
	existingBusinessCheckBox.setChecked(criteria.specialties.contains("development"));
	exportingBusinessCheckBox.setChecked(criteria.specialties.contains("exporting"));
	contractorCheckBox.setChecked(criteria.specialties.contains("contractor"));
	greenCheckBox.setChecked(criteria.specialties.contains("green"));
	militaryCheckBox.setChecked(criteria.specialties.contains("military"));
	minorityCheckBox.setChecked(criteria.specialties.contains("minority"));
	womanCheckBox.setChecked(criteria.specialties.contains("woman"));
	disabledCheckBox.setChecked(criteria.specialties.contains("disabled"));
	ruralCheckBox.setChecked(criteria.specialties.contains("rural"));
	disasterCheckBox.setChecked(criteria.specialties.contains("disaster"));
    }
    
    private void preselectCurrentState() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.preselectCurrentState(stateSpinner);
    }
    
    // Load/Save search functionality
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.criteria_menu, menu);
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.load_search_menu_item:
	    loadSearches();
	    return true;
	case R.id.save_search_menu_item:
	    saveSearch();
	    return true;
	case R.id.delete_search_menu_item:
	    deleteSearches();
	    return true;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    private void loadSearches() {
	final String key = SEARCHES_PREFERENCE_KEY;
	SharedPreferences prefs = getSharedPreferences(LoansAndGrantsSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, LoansAndGrantsSearchCriteria> searches = (prefs.contains(key)) ? 
		    LoansAndGrantsSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, LoansAndGrantsSearchCriteria>();
	    Collection<String> searchCollection = searches.keySet();
	    if (searchCollection.size() > 0) {
		List<String> sortedSearchCollection = new ArrayList<String>(searchCollection);
		Collections.sort(sortedSearchCollection);
		final String[] searchNames = new String[sortedSearchCollection.size()];
		Iterator<String> it = sortedSearchCollection.iterator();
		for (int i=0; i<sortedSearchCollection.size(); i++) {
		    searchNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Load Saved Search");
		builder.setSingleChoiceItems(searchNames, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
			dialog.dismiss();

			loadSearch(searches.get(searchNames[item]));
		    }
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
			dialog.cancel();
		    }
		});
		
		builder.show();
	    }
	    else {
		Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	    }
	}
	else {
	    Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	}
    }
    
    private void saveSearch() {
	// see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Save Search");

	// Set an EditText view to get user input 
	final EditText input = new EditText(this);
	builder.setView(input);

	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		String value = input.getText().toString();
		if (!"".equals(value)) {
		    // Load the saved searches
		    final SharedPreferences prefs = getSharedPreferences(LoansAndGrantsSearchCriteriaActivity.TAG, 0);
		    final Map<String, LoansAndGrantsSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    LoansAndGrantsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : 
				new HashMap<String, LoansAndGrantsSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, LoansAndGrantsSearchCriteria.convertToJson(searches));
			    editor.commit();
		}
	    }
	});

	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int whichButton) {
		// Canceled.
		dialog.cancel();
	    }
	});

	builder.show();
    }
    
    private void deleteSearches() {
	final SharedPreferences prefs = getSharedPreferences(LoansAndGrantsSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, LoansAndGrantsSearchCriteria> searches = LoansAndGrantsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
	    Collection<String> searchCollection = searches.keySet();
	    if (searchCollection.size() > 0) {
		final String[] searchNames = new String[searchCollection.size()];
		Iterator<String> it = searchCollection.iterator();
		for (int i=0; i<searchCollection.size(); i++) {
		    searchNames[i] = it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Saved Searches");
		final Set<String> searchesToRemove = new HashSet<String>();
		builder.setMultiChoiceItems(searchNames, null, new DialogInterface.OnMultiChoiceClickListener() {

		    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			String name = searchNames[which];
			if (isChecked)
			    searchesToRemove.add(name);
			else
			    searchesToRemove.remove(name);
		    }
		});

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Remove the searches
			for (String name : searchesToRemove) {
			    searches.remove(name);
			}

			// Commit the changes
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(SEARCHES_PREFERENCE_KEY, LoansAndGrantsSearchCriteria.convertToJson(searches));
			editor.commit();

			dialog.dismiss();
		    }
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
			// Canceled.
			dialog.cancel();
		    }
		});

		builder.show();
	    }
	    else {
		Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	    }
	}
	else {
	    Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	}
    }
}
