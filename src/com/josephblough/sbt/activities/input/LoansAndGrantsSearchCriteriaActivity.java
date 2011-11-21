package com.josephblough.sbt.activities.input;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.LoansAndGrantsSearchResultsActivity;
import com.josephblough.sbt.criteria.LoansAndGrantsSearchCriteria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Spinner;

public class LoansAndGrantsSearchCriteriaActivity extends Activity {

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
	
	LoansAndGrantsSearchCriteria criteria = new LoansAndGrantsSearchCriteria(federalCheckBox.isChecked(), 
		stateCheckBox.isChecked(), state, industryCheckBox.isChecked(), 
		((String)industrySpinner.getSelectedItem()).toLowerCase(), filterBySpecialties, specialties);

	Intent intent = new Intent(this, LoansAndGrantsSearchResultsActivity.class);
	intent.putExtra(LoansAndGrantsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private void preselectCurrentState() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.preselectCurrentState(stateSpinner);
    }
}
