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
import com.josephblough.sbt.activities.results.ProgramFinderSearchResultsActivity;
import com.josephblough.sbt.criteria.ProgramFinderSearchCriteria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProgramFinderCriteriaActivity extends Activity {

    private final static String TAG = "ProgramFinderCriteriaActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

    Spinner typeSpinner;
    TextView criteriaLabel;
    Spinner criteriaSpinner;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(R.layout.program_finder_search_criteria);
        
        final boolean creatingLauncher = getIntent().getBooleanExtra(ShortcutActivity.CREATE_LAUNCHER_KEY, false);
        
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
        
        Button searchButton = (Button)findViewById(R.id.program_finder_search_button);
	if (creatingLauncher) {
	    searchButton.setText("Create Launcher");
	}
	
        searchButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		if (creatingLauncher) {
		    Intent data = new Intent();
		    data.putExtra(ShortcutActivity.SEARCH_TYPE, ShortcutActivity.PROGRAM_FINDER_INDEX);
		    data.putExtra(ShortcutActivity.CRITERIA, createCriteria().toJson().toString());
		    setResult(RESULT_OK, data);
		    finish();
		}
		else {
		    search();
		}
	    }
	});
    }
    
    private void typeChanged(int position) {
	Log.d(TAG, "XXX: type changed to " + position);
	
	String currentSelection = (String)criteriaSpinner.getSelectedItem();
	
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
	
	// If the previous selection is still in the list, select it again
	if (currentSelection != null && !"".equals(currentSelection)) {
	    for (int i=0; i<criteriaSpinner.getAdapter().getCount(); i++) {
		if (currentSelection.equals(criteriaSpinner.getAdapter().getItem(i))) {
		    criteriaSpinner.setSelection(i);
		    break;
		}
	    }
	}
    }
    
    private void search() {
	ProgramFinderSearchCriteria criteria = createCriteria();

	Intent intent = new Intent(this, ProgramFinderSearchResultsActivity.class);
	intent.putExtra(ProgramFinderSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private ProgramFinderSearchCriteria createCriteria() {
	return new ProgramFinderSearchCriteria(typeSpinner.getSelectedItemPosition(), 
		(String)criteriaSpinner.getSelectedItem());
    }
    
    private void loadSearch(final ProgramFinderSearchCriteria criteria) {
	Log.d(TAG, "XXX: Loading search : " + criteria.type + ", " + criteria.criteria);
	// Type
	typeSpinner.setSelection(criteria.type);
	
	// Criteria
	if (criteria.criteria == null) {
	    criteriaSpinner.setSelection(0);
	}
	else {
	    // Update the criteria spinner adapter
	    ArrayAdapter<CharSequence> adapter = null;
	    switch (criteria.type) {
	    case ProgramFinderSearchCriteria.TYPE_BY_STATE_INDEX:
		adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
		break;
	    case ProgramFinderSearchCriteria.TYPE_BY_INDUSTRY_INDEX:
		adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_industries, android.R.layout.simple_spinner_item);
		break;
	    case ProgramFinderSearchCriteria.TYPE_BY_TYPE_INDEX:
		adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_service_types, android.R.layout.simple_spinner_item);
		break;
	    case ProgramFinderSearchCriteria.TYPE_BY_QUALIFICATION_INDEX:
		adapter = ArrayAdapter.createFromResource(this, R.array.program_finder_qualifications, android.R.layout.simple_spinner_item);
		break;
	    };

	    if (adapter != null) {
		criteriaSpinner.setAdapter(adapter);
		
		int selectedIndex = -1;
		for (int i=0; i<adapter.getCount() && selectedIndex == -1; i++) {
		    if (criteria.criteria.equals(adapter.getItem(i))) {
			selectedIndex = i;
		    }
		}
		
		if (selectedIndex > -1 && selectedIndex < criteriaSpinner.getAdapter().getCount()-1) {
		    criteriaSpinner.setSelection(selectedIndex);
		}
	    }
	}
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
	SharedPreferences prefs = getSharedPreferences(ProgramFinderCriteriaActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, ProgramFinderSearchCriteria> searches = (prefs.contains(key)) ? 
		    ProgramFinderSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, ProgramFinderSearchCriteria>();
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
		    final SharedPreferences prefs = getSharedPreferences(ProgramFinderCriteriaActivity.TAG, 0);
		    final Map<String, ProgramFinderSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    ProgramFinderSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : 
				new HashMap<String, ProgramFinderSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, ProgramFinderSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(ProgramFinderCriteriaActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, ProgramFinderSearchCriteria> searches = ProgramFinderSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, ProgramFinderSearchCriteria.convertToJson(searches));
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
