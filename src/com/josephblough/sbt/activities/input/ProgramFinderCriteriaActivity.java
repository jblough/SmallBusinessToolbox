package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.ProgramFinderSearchResultsActivity;
import com.josephblough.sbt.criteria.ProgramFinderSearchCriteria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ProgramFinderCriteriaActivity extends Activity {

    Spinner typeSpinner;
    TextView criteriaLabel;
    Spinner criteriaSpinner;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.program_finder_search_criteria);
        
        typeSpinner = (Spinner)findViewById(R.id.program_finder_type_spinner);
        criteriaLabel = (TextView)findViewById(R.id.program_finder_criteria_label);
        criteriaSpinner = (Spinner)findViewById(R.id.program_finder_criteria_spinner);
        
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		typeChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
        
        ((Button)findViewById(R.id.program_finder_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
    }
    
    private void typeChanged(int position) {
	switch (position) {
	case ProgramFinderSearchCriteria.TYPE_BY_FEDERAL_INDEX:
	case ProgramFinderSearchCriteria.TYPE_BY_PRIVATE_INDEX:
	case ProgramFinderSearchCriteria.TYPE_BY_NATIONAL_INDEX:
	    criteriaLabel.setVisibility(View.GONE);
	    criteriaSpinner.setVisibility(View.GONE);
	    break;
	case ProgramFinderSearchCriteria.TYPE_BY_STATE_INDEX:
	{
	    criteriaLabel.setText("State");
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    criteriaSpinner.setAdapter(adapter);
	    ((ApplicationController)getApplicationContext()).preselectCurrentState(criteriaSpinner);
	    criteriaLabel.setVisibility(View.VISIBLE);
	    criteriaSpinner.setVisibility(View.VISIBLE);
	}
	    break;
	case ProgramFinderSearchCriteria.TYPE_BY_INDUSTRY_INDEX:
	{
	    criteriaLabel.setText("Industry");
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_industries, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    criteriaSpinner.setAdapter(adapter);
	    criteriaLabel.setVisibility(View.VISIBLE);
	    criteriaSpinner.setVisibility(View.VISIBLE);
	}
	    break;
	case ProgramFinderSearchCriteria.TYPE_BY_TYPE_INDEX:
	{
	    criteriaLabel.setText("Program/Service Type");
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_service_types, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    criteriaSpinner.setAdapter(adapter);
	    criteriaLabel.setVisibility(View.VISIBLE);
	    criteriaSpinner.setVisibility(View.VISIBLE);
	}
	    break;
	case ProgramFinderSearchCriteria.TYPE_BY_QUALIFICATION_INDEX:
	{
	    criteriaLabel.setText("Qualification");
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_qualifications, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    criteriaSpinner.setAdapter(adapter);
	    criteriaLabel.setVisibility(View.VISIBLE);
	    criteriaSpinner.setVisibility(View.VISIBLE);
	}
	    break;
	};
    }
    
    private void search() {
	ProgramFinderSearchCriteria criteria = new ProgramFinderSearchCriteria(typeSpinner.getSelectedItemPosition(), (String)criteriaSpinner.getSelectedItem());

	Intent intent = new Intent(this, ProgramFinderSearchResultsActivity.class);
	intent.putExtra(ProgramFinderSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
}
