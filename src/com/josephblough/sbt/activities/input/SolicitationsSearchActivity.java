package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.SolicitationsSearchResultsActivity;
import com.josephblough.sbt.criteria.SolicitationsSearchCriteria;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SolicitationsSearchActivity extends Activity implements OnEditorActionListener {

    EditText searchTermField;
    CheckBox agencyCheckBox;
    Spinner agencySpinner;
    Spinner filterSpinner;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitations_search_criteria);
        
        searchTermField = (EditText)findViewById(R.id.solicitations_search_term_field);
        agencyCheckBox = (CheckBox)findViewById(R.id.solicitations_agency_checkbox);
        agencySpinner = (Spinner)findViewById(R.id.solicitations_agency_spinner);
        filterSpinner = (Spinner)findViewById(R.id.solicitations_filter_spinner);
        
        searchTermField.setOnEditorActionListener(this);
        
        agencyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		agencySpinner.setVisibility(isChecked ? View.VISIBLE : View.GONE);
	    }
	});
        
        ((Button)findViewById(R.id.solicitations_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	String agency = agencyCheckBox.isChecked() ? 
		((ApplicationController)getApplicationContext()).
		agencyLookupMap.get((String)agencySpinner.getSelectedItem()) :
		    null;
	SolicitationsSearchCriteria criteria = new SolicitationsSearchCriteria(searchTermField.getText().toString(), agency, filterSpinner.getSelectedItemPosition());

	Intent intent = new Intent(this, SolicitationsSearchResultsActivity.class);
	intent.putExtra(SolicitationsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
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
}
