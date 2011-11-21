package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.AwardsSearchResultsActivity;
import com.josephblough.sbt.criteria.AwardsSearchCriteria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class AwardsSearchActivity extends Activity implements OnEditorActionListener {
    
    private CheckBox downloadAllCheckBox;
    private EditText searchTermField;
    private CheckBox agencyCheckBox;
    private Spinner agencySpinner;
    private CheckBox companyCheckBox;
    private EditText companyField;
    private CheckBox institutionCheckBox;
    private EditText institutionField;
    private CheckBox yearCheckBox;
    private EditText yearField;
    private ScrollView inputWrapper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.awards_search_criteria);
        
        downloadAllCheckBox = (CheckBox)findViewById(R.id.awards_download_all_checkbox);
        searchTermField = (EditText)findViewById(R.id.awards_search_term_field);
        agencyCheckBox = (CheckBox)findViewById(R.id.awards_agency_checkbox);
        agencySpinner = (Spinner)findViewById(R.id.awards_agency_spinner);
        companyCheckBox = (CheckBox)findViewById(R.id.awards_company_checkbox);
        companyField = (EditText)findViewById(R.id.awards_company_field);
        institutionCheckBox = (CheckBox)findViewById(R.id.awards_institution_checkbox);
        institutionField = (EditText)findViewById(R.id.awards_institution_field);
        yearCheckBox = (CheckBox)findViewById(R.id.awards_year_checkbox);
        yearField = (EditText)findViewById(R.id.awards_year_field);
        inputWrapper = (ScrollView)findViewById(R.id.awards_input_wrapper);
        
        searchTermField.setOnEditorActionListener(this);
        companyField.setOnEditorActionListener(this);
        institutionField.setOnEditorActionListener(this);
        yearField.setOnEditorActionListener(this);
        
        downloadAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeAllKeyboards();
		toggleFields();
	    }
	});
        
        agencyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeAllKeyboards();
		agencySpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
        
        companyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeAllKeyboards();
		companyField.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
        
        institutionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeAllKeyboards();
		institutionField.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
        
        yearCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		yearField.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});

        ((Button)findViewById(R.id.awards_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
    }

    private void closeAllKeyboards() {
	closeKeyboard(searchTermField);
	closeKeyboard(companyField);
	closeKeyboard(institutionField);
	closeKeyboard(yearField);
    }
    
    private void toggleFields() {
	boolean showFields = !downloadAllCheckBox.isChecked();
	int visibility = showFields ? View.VISIBLE : View.GONE;
	/*
	searchTermLabel.setVisibility(visibility);
	searchTermField.setVisibility(visibility);
	agencyCheckBox.setVisibility(visibility);
	agencySpinner.setVisibility((showFields && agencyCheckBox.isChecked()) ? View.VISIBLE : View.GONE);
	companyCheckBox.setVisibility(visibility);
	companyField.setVisibility((showFields && companyCheckBox.isChecked()) ? View.VISIBLE : View.GONE);
	institutionCheckBox.setVisibility(visibility);
	institutionField.setVisibility((showFields && institutionCheckBox.isChecked()) ? View.VISIBLE : View.GONE);
	yearCheckBox.setVisibility(visibility);
	yearField.setVisibility((showFields && yearCheckBox.isChecked()) ? View.VISIBLE : View.GONE);
	*/
	inputWrapper.setVisibility(visibility);
    }
    
    private void search() {
	// close the keyboard before the search
	closeAllKeyboards();
	
	boolean downloadAll = downloadAllCheckBox.isChecked();
	String searchTerm = (downloadAll) ? "" : searchTermField.getText().toString();
	String agency = (downloadAll || !agencyCheckBox.isChecked()) ? "" : ((ApplicationController)getApplicationContext()).agencyLookupMap.get((String)agencySpinner.getSelectedItem());
	String company = (downloadAll || !companyCheckBox.isChecked()) ? "" : companyField.getText().toString();
	String institution = (downloadAll || !institutionCheckBox.isChecked()) ? "" : institutionField.getText().toString();
	int year = (downloadAll || !yearCheckBox.isChecked() || 
		"".equals(yearField.getText().toString())) ? 0 : Integer.valueOf(yearField.getText().toString());
	
	AwardsSearchCriteria criteria = new AwardsSearchCriteria(downloadAll, searchTerm, agency, company, institution, year);

	Intent intent = new Intent(this, AwardsSearchResultsActivity.class);
	intent.putExtra(AwardsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	closeKeyboard(v);
	return true;
    }

    private void closeKeyboard(TextView v) {
	// Close the keyboard if it's open
	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	v.clearFocus();
    }
}
