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
import com.josephblough.sbt.activities.results.GreenSearchResultsActivity;
import com.josephblough.sbt.criteria.GreenSearchCriteria;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class GreenSearchActivity extends Activity implements OnEditorActionListener {

    private final static String TAG = "GreenSearchActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

    private CheckBox downloadAllCheckBox;
    private LinearLayout inputFieldWrapper;
    private EditText searchTermField;
    private CheckBox agencyCheckBox;
    private Spinner agencySpinner;
    private CheckBox searchTypeCheckBox;
    private Spinner searchTypeSpinner;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.green_search_criteria);

	downloadAllCheckBox = (CheckBox)findViewById(R.id.green_download_all_checkbox);
	inputFieldWrapper = (LinearLayout)findViewById(R.id.green_input_wrapper);
	searchTermField = (EditText)findViewById(R.id.green_search_term_field);
	agencyCheckBox = (CheckBox)findViewById(R.id.green_agency_checkbox);
	agencySpinner = (Spinner)findViewById(R.id.green_agency_spinner);
	searchTypeCheckBox = (CheckBox)findViewById(R.id.green_search_type_checkbox);
	searchTypeSpinner = (Spinner)findViewById(R.id.green_search_type_spinner);
	
	searchTermField.setOnEditorActionListener(this);
	
	downloadAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	    
	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		closeKeyboard();
		inputFieldWrapper.setVisibility(isChecked ? View.GONE : View.VISIBLE);
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
	
	((Button)findViewById(R.id.green_search_button)).setOnClickListener(new View.OnClickListener() {
	    
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

	GreenSearchCriteria criteria = createCriteria();
    
	Intent intent = new Intent(this, GreenSearchResultsActivity.class);
	intent.putExtra(GreenSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }

    private GreenSearchCriteria createCriteria() {
	boolean downloadAll = downloadAllCheckBox.isChecked();
	String searchTerm = (downloadAll) ? "" : searchTermField.getText().toString();
	String agency = (downloadAll || !agencyCheckBox.isChecked()) ? "" : (String)agencySpinner.getSelectedItem();
	String type = (downloadAll || !searchTypeCheckBox.isChecked()) ? "" : 
	    ((ApplicationController)getApplicationContext()).searchTypeMap.get((String)searchTypeSpinner.getSelectedItem());

	return new GreenSearchCriteria(downloadAll, searchTerm, agency, type);
    }
    
    private void loadSearch(final GreenSearchCriteria criteria) {
	downloadAllCheckBox.setChecked(criteria.downloadAll);
	searchTermField.setText((criteria.searchTerm == null) ? "" : criteria.searchTerm);
	
	// Agency
	if (criteria.agency == null || "".equals(criteria.agency)) {
	    agencyCheckBox.setChecked(false);
	    agencySpinner.setSelection(0);
	}
	else {
	    agencyCheckBox.setChecked(true);
	    for (int i=0; i<agencySpinner.getAdapter().getCount(); i++) {
		if (criteria.agency.equals(agencySpinner.getAdapter().getItem(i))) {
		    agencySpinner.setSelection(i);
		    break;
		}
	    }
	}
	
	// Type
	if (criteria.type == null || "".equals(criteria.type)) {
	    searchTypeCheckBox.setChecked(false);
	    searchTypeSpinner.setSelection(0);
	}
	else {
	    searchTypeCheckBox.setChecked(true);
	    final String type = ((ApplicationController)getApplicationContext()).searchTypeMap.get(criteria.type);
	    for (int i=0; i<searchTypeSpinner.getAdapter().getCount(); i++) {
		if (type.equals(searchTypeSpinner.getAdapter().getItem(i))) {
		    searchTypeSpinner.setSelection(i);
		    break;
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
	SharedPreferences prefs = getSharedPreferences(GreenSearchActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, GreenSearchCriteria> searches = (prefs.contains(key)) ? 
		    GreenSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, GreenSearchCriteria>();
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
		    final SharedPreferences prefs = getSharedPreferences(GreenSearchActivity.TAG, 0);
		    final Map<String, GreenSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    GreenSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : new HashMap<String, GreenSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, GreenSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(GreenSearchActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, GreenSearchCriteria> searches = GreenSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, GreenSearchCriteria.convertToJson(searches));
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
