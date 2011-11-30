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
import com.josephblough.sbt.activities.ShortcutActivity;
import com.josephblough.sbt.activities.results.LocalityWebDataResultsActivity;
import com.josephblough.sbt.criteria.LocalityWebDataSearchCriteria;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LocalityWebDataCriteriaActivity extends Activity implements OnEditorActionListener {

    private final static String TAG = "LocalityWebDataCriteriaActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

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
        
        final boolean creatingLauncher = getIntent().getBooleanExtra(ShortcutActivity.CREATE_LAUNCHER_KEY, false);
        
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
        
        Button searchButton = (Button)findViewById(R.id.web_data_search_button);
	if (creatingLauncher) {
	    searchButton.setText("Create Launcher");
	}
	
        searchButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		if (creatingLauncher) {
		    Intent data = new Intent();
		    data.putExtra(ShortcutActivity.SEARCH_TYPE, ShortcutActivity.URLS_INDEX);
		    data.putExtra(ShortcutActivity.CRITERIA, createCriteria().toJson().toString());
		    setResult(RESULT_OK, data);
		    finish();
		}
		else {
		    search();
		}
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

	LocalityWebDataSearchCriteria criteria = createCriteria();
	
	Intent intent = new Intent(this, LocalityWebDataResultsActivity.class);
	intent.putExtra(LocalityWebDataResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private LocalityWebDataSearchCriteria createCriteria() {
	final String state = ((ApplicationController)getApplicationContext()).stateLookupMap.get((String)stateSpinner.getSelectedItem());
	return new LocalityWebDataSearchCriteria(typeSpinner.getSelectedItemPosition(), 
		scopeSpinner.getSelectedItemPosition(), state, localityField.getText().toString());
    }
    
    private void loadSearch(final LocalityWebDataSearchCriteria criteria) {
	// Type
	typeSpinner.setSelection(criteria.type);

	// Scope
	scopeSpinner.setSelection(criteria.scope);
	
	// State
	stateSpinner.setSelection(((ApplicationController)getApplicationContext()).stateIndexArrayLookupMap.get(criteria.state));
	
	// Locality
	localityField.setText((criteria.locality == null) ? "" : criteria.locality);
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
	SharedPreferences prefs = getSharedPreferences(LocalityWebDataCriteriaActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, LocalityWebDataSearchCriteria> searches = (prefs.contains(key)) ? 
		    LocalityWebDataSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, LocalityWebDataSearchCriteria>();
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
		    final SharedPreferences prefs = getSharedPreferences(LocalityWebDataCriteriaActivity.TAG, 0);
		    final Map<String, LocalityWebDataSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    LocalityWebDataSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : 
				new HashMap<String, LocalityWebDataSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, LocalityWebDataSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(LocalityWebDataCriteriaActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, LocalityWebDataSearchCriteria> searches = LocalityWebDataSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, LocalityWebDataSearchCriteria.convertToJson(searches));
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
