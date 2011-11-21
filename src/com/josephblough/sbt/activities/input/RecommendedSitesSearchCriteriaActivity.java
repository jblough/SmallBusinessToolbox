package com.josephblough.sbt.activities.input;

import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.results.RecommendedSitesSearchResultsActivity;
import com.josephblough.sbt.criteria.RecommendedSitesSearchCriteria;

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

public class RecommendedSitesSearchCriteriaActivity extends Activity implements OnEditorActionListener {

    private Spinner searchBySpinner;
    private TextView searchTermLabel;
    private EditText searchTermField;
    private Spinner categorySpinner;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.recommended_sites_search_criteria);
        
        searchBySpinner = (Spinner)findViewById(R.id.recommended_sites_options);
        searchTermLabel = (TextView)findViewById(R.id.recommended_sites_search_label);
        searchTermField = (EditText)findViewById(R.id.recommended_sites_search_term);
        categorySpinner = (Spinner)findViewById(R.id.recommended_sites_category);
        
        searchTermField.setOnEditorActionListener(this);
        
        searchBySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    public void onItemSelected(AdapterView<?> parent, View view,
		    int position, long id) {
		searchByChanged(position);
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    }
	});
        
        ((Button)findViewById(R.id.recommended_sites_search_button)).setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		search();
	    }
	});
    }
    
    private void searchByChanged(int position) {
	if (position == RecommendedSitesSearchCriteria.ALL_SITES_INDEX) {
	    searchTermLabel.setVisibility(View.GONE);
	    searchTermField.setVisibility(View.GONE);
	}
	else {
	    switch (position) {
	    case RecommendedSitesSearchCriteria.KEYWORD_SEARCH_INDEX:
		searchTermLabel.setText("Keyword");
		categorySpinner.setVisibility(View.GONE);
		searchTermField.setVisibility(View.VISIBLE);
		break;
	    case RecommendedSitesSearchCriteria.CATEGORY_SEARCH_INDEX:
		searchTermLabel.setText("Category");
		searchTermField.setVisibility(View.GONE);
		categorySpinner.setVisibility(View.VISIBLE);
		break;
	    case RecommendedSitesSearchCriteria.MASTER_TERM_SEARCH_INDEX:
		searchTermLabel.setText("Master Term");
		categorySpinner.setVisibility(View.GONE);
		searchTermField.setVisibility(View.VISIBLE);
		break;
	    case RecommendedSitesSearchCriteria.DOMAIN_FILTER_INDEX:
		searchTermLabel.setText("Domain (e.g., sba.gov)");
		categorySpinner.setVisibility(View.GONE);
		searchTermField.setVisibility(View.VISIBLE);
		break;
	    default:
		searchTermLabel.setText("Keyword");
	    };
	    
	    searchTermLabel.setVisibility(View.VISIBLE);
	}
    }
    
    private void search() {
	// close the keyboard before the search
	closeKeyboard();

	String searchTerm = (searchBySpinner.getSelectedItemPosition() == RecommendedSitesSearchCriteria.CATEGORY_SEARCH_INDEX) ?
		(String)categorySpinner.getSelectedItem() : searchTermField.getText().toString();
	RecommendedSitesSearchCriteria criteria = new RecommendedSitesSearchCriteria(
		searchBySpinner.getSelectedItemPosition(), searchTerm);

	Intent intent = new Intent(this, RecommendedSitesSearchResultsActivity.class);
	intent.putExtra(RecommendedSitesSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
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
