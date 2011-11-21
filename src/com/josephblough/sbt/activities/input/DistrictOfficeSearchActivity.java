package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.DistrictOfficeSearchResultsActivity;
import com.josephblough.sbt.criteria.DistrictOfficeSearchCriteria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class DistrictOfficeSearchActivity extends Activity implements OnEditorActionListener {

    private Spinner searchTypeSpinner;
    private TextView searchTypeLabel;
    private EditText zipCodeField;
    private Spinner stateSpinner;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.district_office_search_criteria);

	searchTypeSpinner = (Spinner)findViewById(R.id.district_office_finder_search_type);
	searchTypeLabel = (TextView)findViewById(R.id.district_office_search_type_label);
	zipCodeField = (EditText)findViewById(R.id.district_office_zipcode_field);
	stateSpinner = (Spinner)findViewById(R.id.district_office_finder_state_spinner);
	
	zipCodeField.setOnEditorActionListener(this);
	
	searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		searchByChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
	
	((Button)findViewById(R.id.district_office_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
        
        preselectCurrentState();
    }
    
    private void searchByChanged(int position) {
	switch(position) {
	case DistrictOfficeSearchCriteria.FIND_NEAREST_OFFICE_INDEX:
	    searchTypeLabel.setVisibility(View.GONE);
	    zipCodeField.setVisibility(View.GONE);
	    stateSpinner.setVisibility(View.GONE);
	    break;
	case DistrictOfficeSearchCriteria.FIND_BY_STATE_INDEX:
	    searchTypeLabel.setText("State");
	    zipCodeField.setVisibility(View.GONE);
	    searchTypeLabel.setVisibility(View.VISIBLE);
	    stateSpinner.setVisibility(View.VISIBLE);
	    break;
	case DistrictOfficeSearchCriteria.FIND_BY_ZIP_CODE_INDEX:
	    searchTypeLabel.setText("Zip Code");
	    stateSpinner.setVisibility(View.GONE);
	    searchTypeLabel.setVisibility(View.VISIBLE);
	    zipCodeField.setVisibility(View.VISIBLE);
	    break;
	}
    }
    
    private void closeKeyboard() {
	// Close the keyboard if it's open
	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(zipCodeField.getWindowToken(), 0);
	zipCodeField.clearFocus();
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	int type = searchTypeSpinner.getSelectedItemPosition();
	String searchCriteria = null;
	if (type == DistrictOfficeSearchCriteria.FIND_BY_ZIP_CODE_INDEX)
	    searchCriteria = zipCodeField.getText().toString();
	else if (type == DistrictOfficeSearchCriteria.FIND_BY_STATE_INDEX) {
	    ApplicationController app = (ApplicationController)getApplicationContext();
	    searchCriteria = app.stateLookupMap.get((String)stateSpinner.getSelectedItem());
	}

	DistrictOfficeSearchCriteria criteria = new DistrictOfficeSearchCriteria(type, searchCriteria);
    
	Intent intent = new Intent(this, DistrictOfficeSearchResultsActivity.class);
	intent.putExtra(DistrictOfficeSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	closeKeyboard();
	return true;
    }

    private void preselectCurrentState() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.preselectCurrentState(stateSpinner);
    }
}
