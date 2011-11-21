package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.LocalityWebDataResultsActivity;
import com.josephblough.sbt.criteria.LocalityWebDataSearchCriteria;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;

public class LocalityWebDataCriteriaActivity extends Activity implements OnEditorActionListener {

    private Spinner typeSpinner;
    private Spinner stateSpinner;
    private Spinner scopeSpinner;
    private TextView localityLabel;
    private EditText localityField;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.locality_web_data_criteria);
        
        typeSpinner = (Spinner)findViewById(R.id.web_data_type_spinner);
        stateSpinner = (Spinner)findViewById(R.id.web_data_state_spinner);
        scopeSpinner = (Spinner)findViewById(R.id.web_data_scope_spinner);
        localityLabel = (TextView)findViewById(R.id.web_data_locality_label);
        localityField = (EditText)findViewById(R.id.web_data_locality_term);
        
        localityField.setOnEditorActionListener(this);
        
        scopeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		scopeChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
        
        ((Button)findViewById(R.id.web_data_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});

        preselectCurrentState();
    }
    
    private void scopeChanged(int position) {
	switch (position) {
	case LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_AND_COUNTIES_INDEX:
	case LocalityWebDataSearchCriteria.SCOPE_ALL_CITIES_INDEX:
	case LocalityWebDataSearchCriteria.SCOPE_ALL_COUNTIES_INDEX:
	    localityLabel.setVisibility(View.GONE);
	    localityField.setVisibility(View.GONE);
	    break;
	case LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_CITY_INDEX:
	    localityLabel.setText("City");
	    localityLabel.setVisibility(View.VISIBLE);
	    localityField.setVisibility(View.VISIBLE);
	    break;
	case LocalityWebDataSearchCriteria.SCOPE_SPECIFIC_COUNTY_DATA_INDEX:
	    localityLabel.setText("County");
	    localityLabel.setVisibility(View.VISIBLE);
	    localityField.setVisibility(View.VISIBLE);
	    break;
	};
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	final String state = ((ApplicationController)getApplicationContext()).stateLookupMap.get((String)stateSpinner.getSelectedItem());
	LocalityWebDataSearchCriteria criteria = new LocalityWebDataSearchCriteria(typeSpinner.getSelectedItemPosition(), 
		scopeSpinner.getSelectedItemPosition(), state, localityField.getText().toString());
	
	Intent intent = new Intent(this, LocalityWebDataResultsActivity.class);
	intent.putExtra(LocalityWebDataResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private void preselectCurrentState() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.preselectCurrentState(stateSpinner);
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	closeKeyboard();
	return true;
    }

    private void closeKeyboard() {
	// Close the keyboard if it's open
	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(localityField.getWindowToken(), 0);
	localityField.clearFocus();
    }
}
