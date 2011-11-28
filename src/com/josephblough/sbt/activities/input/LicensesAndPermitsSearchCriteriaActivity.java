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
import com.josephblough.sbt.activities.results.LicensesAndPermitsSearchResultsActivity;
import com.josephblough.sbt.criteria.LicensesAndPermitsSearchCriteria;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LicensesAndPermitsSearchCriteriaActivity extends Activity implements OnEditorActionListener {

    private final static String TAG = "LicensesAndPermitsSearchCriteriaActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

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
	LicensesAndPermitsSearchCriteria criteria = createCriteria();
	
	Intent intent = new Intent(this, LicensesAndPermitsSearchResultsActivity.class);
	intent.putExtra(LicensesAndPermitsSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private LicensesAndPermitsSearchCriteria createCriteria() {
	final String state = ((ApplicationController)getApplicationContext()).stateLookupMap.get(
		(typeSpinner.getSelectedItemPosition() == LicensesAndPermitsSearchCriteria.STATE_INDEX) ? 
			(String)byStateSpinner.getSelectedItem() : (String)byBusinessTypeStateSpinner.getSelectedItem());

	return new LicensesAndPermitsSearchCriteria(
		typeSpinner.getSelectedItemPosition(), 
			state,
			(String)byCategorySpinner.getSelectedItem(), 
			(String)byBusinessTypeSpinner.getSelectedItem(), 
			byBusinessTypeSubfilterSpinner.getSelectedItemPosition(),
			byBusinessTypeLocalityEdit.getText().toString());
    }
    
    private void loadSearch(final LicensesAndPermitsSearchCriteria criteria) {
	// By Type
	typeSpinner.setSelection(criteria.searchBy);
	
	// State
	byStateSpinner.setSelection(((ApplicationController)getApplicationContext()).stateIndexArrayLookupMap.get(criteria.state));
	byBusinessTypeStateSpinner.setSelection(((ApplicationController)getApplicationContext()).stateIndexArrayLookupMap.get(criteria.state));
	// Category
	if (criteria.category == null) {
	    byCategorySpinner.setSelection(0);
	}
	else {
	    for (int i=0; i<byCategorySpinner.getAdapter().getCount(); i++) {
		if (criteria.category.equals(byCategorySpinner.getAdapter().getItem(i))) {
		    byCategorySpinner.setSelection(i);
		    break;
		}
	    }
	}

	// Business Type
	if (criteria.businessType == null) {
	    byBusinessTypeSpinner.setSelection(0);
	}
	else {
	    for (int i=0; i<byBusinessTypeSpinner.getAdapter().getCount(); i++) {
		if (criteria.businessType.equals(byBusinessTypeSpinner.getAdapter().getItem(i))) {
		    byBusinessTypeSpinner.setSelection(i);
		    break;
		}
	    }
	}
	// Business Type Subfilter
	byBusinessTypeSubfilterSpinner.setSelection(criteria.businessTypeSubfilter);
	
	// Business Type Subfilter locality
	if (criteria.businessTypeSubfilterLocality == null || "".equals(criteria.businessTypeSubfilterLocality)) {
	    byBusinessTypeLocalityEdit.setText("");
	}
	else {
	    byBusinessTypeLocalityEdit.setText(criteria.businessTypeSubfilterLocality);
	}
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
	SharedPreferences prefs = getSharedPreferences(LicensesAndPermitsSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, LicensesAndPermitsSearchCriteria> searches = (prefs.contains(key)) ? 
		    LicensesAndPermitsSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, LicensesAndPermitsSearchCriteria>();
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
		Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	    }
	}
	else {
	    Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
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
		    final SharedPreferences prefs = getSharedPreferences(LicensesAndPermitsSearchCriteriaActivity.TAG, 0);
		    final Map<String, LicensesAndPermitsSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    LicensesAndPermitsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : 
				new HashMap<String, LicensesAndPermitsSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, LicensesAndPermitsSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(LicensesAndPermitsSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, LicensesAndPermitsSearchCriteria> searches = LicensesAndPermitsSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, LicensesAndPermitsSearchCriteria.convertToJson(searches));
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
		Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	    }
	}
	else {
	    Toast.makeText(this, R.string.no_saved_searches, Toast.LENGTH_SHORT).show();
	}
    }
}
