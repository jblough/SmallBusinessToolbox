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
import com.josephblough.sbt.activities.results.AwardsSearchResultsActivity;
import com.josephblough.sbt.criteria.AwardsSearchCriteria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class AwardsSearchActivity extends Activity implements OnEditorActionListener {
    
    private final static String TAG = "AwardsSearchActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";
    
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
	
	AwardsSearchCriteria criteria = createCriteria();
	
	Intent intent = new Intent(this, AwardsSearchResultsActivity.class);
	intent.putExtra(AwardsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private AwardsSearchCriteria createCriteria() {
	boolean downloadAll = downloadAllCheckBox.isChecked();
	String searchTerm = (downloadAll) ? "" : searchTermField.getText().toString();
	String agency = (downloadAll || !agencyCheckBox.isChecked()) ? "" : ((ApplicationController)getApplicationContext()).agencyLookupMap.get((String)agencySpinner.getSelectedItem());
	String company = (downloadAll || !companyCheckBox.isChecked()) ? "" : companyField.getText().toString();
	String institution = (downloadAll || !institutionCheckBox.isChecked()) ? "" : institutionField.getText().toString();
	int year = (downloadAll || !yearCheckBox.isChecked() || 
		"".equals(yearField.getText().toString())) ? 0 : Integer.valueOf(yearField.getText().toString());
	
	return new AwardsSearchCriteria(downloadAll, searchTerm, agency, company, institution, year);
    }
    
    private void loadSearch(final AwardsSearchCriteria criteria) {
	downloadAllCheckBox.setChecked(criteria.downloadAll);
	searchTermField.setText((criteria.searchTerm == null) ? "" : criteria.searchTerm);
	if (criteria.agency == null || "".equals(criteria.agency)) {
	    agencyCheckBox.setChecked(false);
	    agencySpinner.setSelection(0);
	}
	else {
	    agencyCheckBox.setChecked(true);
	    final String agencyName = ((ApplicationController)getApplicationContext()).agencyLookupMap.get(criteria.agency);
	    for (int i=0; i<agencySpinner.getAdapter().getCount(); i++) {
		if (agencyName.equals(agencySpinner.getAdapter().getItem(i))) {
		    agencySpinner.setSelection(i);
		    break;
		}
	    }
	}
	
	if (criteria.company == null || "".equals(criteria.company)) {
	    companyCheckBox.setChecked(false);
	    companyField.setText("");
	}
	else {
	    companyCheckBox.setChecked(true);
	    companyField.setText(criteria.company);
	}
	
	if (criteria.institution == null || "".equals(criteria.institution)) {
	    institutionCheckBox.setChecked(false);
	    institutionField.setText("");
	}
	else {
	    institutionCheckBox.setChecked(true);
	    institutionField.setText(criteria.institution);
	}
	
	if (criteria.year == 0) {
	    yearCheckBox.setChecked(false);
	    yearField.setText("");
	}
	else {
	    yearCheckBox.setChecked(true);
	    yearField.setText(Integer.toString(criteria.year));
	}
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
	SharedPreferences prefs = getSharedPreferences(AwardsSearchActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, AwardsSearchCriteria> searches = (prefs.contains(key)) ? 
		    AwardsSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, AwardsSearchCriteria>();
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
		Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
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
		    final SharedPreferences prefs = getSharedPreferences(AwardsSearchActivity.TAG, 0);
		    final Map<String, AwardsSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    AwardsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : new HashMap<String, AwardsSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, AwardsSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(AwardsSearchActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, AwardsSearchCriteria> searches = AwardsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, AwardsSearchCriteria.convertToJson(searches));
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
		Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	    }
	}
	else {
	    Toast.makeText(this, "No searches have been saved", Toast.LENGTH_LONG).show();
	}
    }
}
