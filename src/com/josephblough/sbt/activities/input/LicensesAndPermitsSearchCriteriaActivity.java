package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.LicensesAndPermitsSearchResultsActivity;
import com.josephblough.sbt.criteria.LicensesAndPermitsSearchCriteria;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LicensesAndPermitsSearchCriteriaActivity extends Activity implements OnEditorActionListener {

    Spinner typeSpinner;
    Spinner byStateSpinner;
    Spinner byCategorySpinner;
    Spinner byBusinessTypeSpinner;
    Spinner byBusinessTypeSubfilterSpinner;
    
    View stateFields;
    View categoryFields;
    View businessTypeFields;

    TextView byBusinessTypeStateLabel;
    Spinner byBusinessTypeStateSpinner;
    TextView byBusinessTypeLocalityLabel;
    EditText byBusinessTypeLocalityEdit;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.licenses_and_permits_search_criteria);
        
        typeSpinner = (Spinner)findViewById(R.id.licenses_and_permits_search_by_spinner);
        byStateSpinner = (Spinner)findViewById(R.id.licenses_and_permits_state_spinner);
        byCategorySpinner = (Spinner)findViewById(R.id.licenses_and_permits_category_spinner);
        byBusinessTypeSpinner = (Spinner)findViewById(R.id.licenses_and_permits_business_types_spinner);
        byBusinessTypeSubfilterSpinner = (Spinner)findViewById(R.id.licenses_and_permits_business_types_subfilter_spinner);
        
        stateFields = findViewById(R.id.licenses_and_permits_state_extra_fields);
        categoryFields = findViewById(R.id.licenses_and_permits_category_extra_fields);
        businessTypeFields = findViewById(R.id.licenses_and_permits_business_type_extra_fields);
        
        // business type subfilter state fields
        byBusinessTypeStateLabel = (TextView)findViewById(R.id.licenses_and_permits_business_types_state_label);
        byBusinessTypeStateSpinner = (Spinner)findViewById(R.id.licenses_and_permits_business_types_state_spinner);
        
        // business type subfilter county, city, and zip code fields (use the same field
        byBusinessTypeLocalityLabel = (TextView)findViewById(R.id.licenses_and_permits_business_types_locality_label);
        byBusinessTypeLocalityEdit = (EditText)findViewById(R.id.licenses_and_permits_business_types_locality_edit);

        byBusinessTypeLocalityEdit.setOnEditorActionListener(this);
        
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		searchByChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
        
        byBusinessTypeSubfilterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		businessTypeSubfilterChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
        
        ((Button)findViewById(R.id.licenses_and_permits_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
        
        preselectCurrentState();
    }

    private void searchByChanged(int position) {
	switch (position) {
	case LicensesAndPermitsSearchCriteria.CATEGORY_INDEX:
	    stateFields.setVisibility(View.GONE);
	    businessTypeFields.setVisibility(View.GONE);
	    categoryFields.setVisibility(View.VISIBLE);
	    break;
	case LicensesAndPermitsSearchCriteria.STATE_INDEX:
	    businessTypeFields.setVisibility(View.GONE);
	    categoryFields.setVisibility(View.GONE);
	    stateFields.setVisibility(View.VISIBLE);
	    break;
	case LicensesAndPermitsSearchCriteria.BUSINESS_TYPE_INDEX:
	    stateFields.setVisibility(View.GONE);
	    categoryFields.setVisibility(View.GONE);
	    businessTypeFields.setVisibility(View.VISIBLE);
	    break;
	}
    }

    private void businessTypeSubfilterChanged(int position) {
	switch (position) {
	case LicensesAndPermitsSearchCriteria.NO_SUBFILTER_INDEX:
	    byBusinessTypeStateLabel.setVisibility(View.GONE);
	    byBusinessTypeStateSpinner.setVisibility(View.GONE);
	    byBusinessTypeLocalityLabel.setVisibility(View.GONE);
	    byBusinessTypeLocalityEdit.setVisibility(View.GONE);
	    break;
	case LicensesAndPermitsSearchCriteria.STATE_SUBFILTER_INDEX:
	    byBusinessTypeStateLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeStateSpinner.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityLabel.setVisibility(View.GONE);
	    byBusinessTypeLocalityEdit.setVisibility(View.GONE);
	    break;
	case LicensesAndPermitsSearchCriteria.COUNTY_SUBFILTER_INDEX:
	    byBusinessTypeStateLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeStateSpinner.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityLabel.setText("County");
	    byBusinessTypeLocalityLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityEdit.setVisibility(View.VISIBLE);
	    break;
	case LicensesAndPermitsSearchCriteria.CITY_SUBFILTER_INDEX:
	    byBusinessTypeStateLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeStateSpinner.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityLabel.setText("City");
	    byBusinessTypeLocalityLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityEdit.setVisibility(View.VISIBLE);
	    break;
	case LicensesAndPermitsSearchCriteria.ZIP_CODE_SUBFILTER_INDEX:
	    byBusinessTypeStateLabel.setVisibility(View.GONE);
	    byBusinessTypeStateSpinner.setVisibility(View.GONE);
	    byBusinessTypeLocalityLabel.setText("Zip Code");
	    byBusinessTypeLocalityLabel.setVisibility(View.VISIBLE);
	    byBusinessTypeLocalityEdit.setVisibility(View.VISIBLE);
	    break;
	}
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	// Validate the input (mainly zip code)
	if (typeSpinner.getSelectedItemPosition() == LicensesAndPermitsSearchCriteria.BUSINESS_TYPE_INDEX &&
		byBusinessTypeSubfilterSpinner.getSelectedItemPosition() == LicensesAndPermitsSearchCriteria.ZIP_CODE_SUBFILTER_INDEX) {
	    if (!isValidZipCode(byBusinessTypeLocalityEdit.getText().toString())) {
		Toast.makeText(this, "Invalid zip code", Toast.LENGTH_LONG).show();
		return;
	    }
	}
	
	// Perform the search
	final String state = ((ApplicationController)getApplicationContext()).stateLookupMap.get(
		(typeSpinner.getSelectedItemPosition() == LicensesAndPermitsSearchCriteria.STATE_INDEX) ? 
			(String)byStateSpinner.getSelectedItem() : (String)byBusinessTypeStateSpinner.getSelectedItem());

	LicensesAndPermitsSearchCriteria criteria = new LicensesAndPermitsSearchCriteria(
		typeSpinner.getSelectedItemPosition(), 
			state,
			(String)byCategorySpinner.getSelectedItem(), 
			(String)byCategorySpinner.getSelectedItem(), 
			byBusinessTypeSubfilterSpinner.getSelectedItemPosition(),
			byBusinessTypeLocalityEdit.getText().toString());
	
	Intent intent = new Intent(this, LicensesAndPermitsSearchResultsActivity.class);
	intent.putExtra(LicensesAndPermitsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private boolean isValidZipCode(final String zipCode) {
	// Probably use regular expressions here
	//	#####, #####-####, ##########
	return true;
    }
    
    private void preselectCurrentState() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	app.preselectCurrentState(byStateSpinner);
	app.preselectCurrentState(byBusinessTypeStateSpinner);
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	closeKeyboard();
	return true;
    }

    private void closeKeyboard() {
	// Close the keyboard if it's open
	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(byBusinessTypeLocalityEdit.getWindowToken(), 0);
	byBusinessTypeLocalityEdit.clearFocus();
    }
}
