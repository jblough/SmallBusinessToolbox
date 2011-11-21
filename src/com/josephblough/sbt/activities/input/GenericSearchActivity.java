package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.GenericSearchResultsActivity;
import com.josephblough.sbt.criteria.GenericSearchCriteria;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GenericSearchActivity extends Activity implements OnEditorActionListener {

    private CheckBox downloadAllCheckBox;
    private LinearLayout inputFieldWrapper;
    private EditText searchTermField;
    private CheckBox agencyCheckBox;
    private Spinner agencySpinner;
    private CheckBox searchTypeCheckBox;
    private Spinner searchTypeSpinner;
    private CheckBox newCheckBox;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.generic_search_criteria);

	downloadAllCheckBox = (CheckBox)findViewById(R.id.generic_download_all_checkbox);
	inputFieldWrapper = (LinearLayout)findViewById(R.id.generic_input_wrapper);
	searchTermField = (EditText)findViewById(R.id.generic_search_term_field);
	agencyCheckBox = (CheckBox)findViewById(R.id.generic_agency_checkbox);
	agencySpinner = (Spinner)findViewById(R.id.generic_agency_spinner);
	searchTypeCheckBox = (CheckBox)findViewById(R.id.generic_search_type_checkbox);
	searchTypeSpinner = (Spinner)findViewById(R.id.generic_search_type_spinner);
	newCheckBox = (CheckBox)findViewById(R.id.generic_search_new_checkbox);
	
	searchTermField.setOnEditorActionListener(this);
	
	downloadAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeKeyboard();
		inputFieldWrapper.setVisibility(isChecked ? View.GONE : View.VISIBLE);
	    }
	});
	
	newCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeKeyboard();
	    }
	});
	
	agencyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeKeyboard();
		agencySpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
	
	searchTypeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeKeyboard();
		searchTypeSpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
	
	((Button)findViewById(R.id.generic_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
    }
    
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	closeKeyboard();
	return true;
    }
    
    private void closeKeyboard() {
	// Close the keyboard if it's open
	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(searchTermField.getWindowToken(), 0);
	searchTermField.clearFocus();
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	boolean downloadAll = downloadAllCheckBox.isChecked();
	boolean onlyNew = (downloadAll) ? false : newCheckBox.isChecked();
	String searchTerm = (downloadAll) ? "" : searchTermField.getText().toString();
	String agency = (downloadAll || !agencyCheckBox.isChecked()) ? "" : (String)agencySpinner.getSelectedItem();
	String type = (downloadAll || !searchTypeCheckBox.isChecked()) ? "" : 
	    ((ApplicationController)getApplicationContext()).searchTypeMap.get((String)searchTypeSpinner.getSelectedItem());

	GenericSearchCriteria criteria = new GenericSearchCriteria(downloadAll, onlyNew, searchTerm, agency, type);
    
	Intent intent = new Intent(this, GenericSearchResultsActivity.class);
	intent.putExtra(GenericSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
}
