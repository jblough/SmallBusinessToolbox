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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class RecommendedSitesSearchCriteriaActivity extends Activity implements OnEditorActionListener {

    private final static String TAG = "RecommendedSitesSearchCriteriaActivity";

    private final static String SEARCHES_PREFERENCE_KEY = "Searches";

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

	RecommendedSitesSearchCriteria criteria = createCriteria();
	
	Intent intent = new Intent(this, RecommendedSitesSearchResultsActivity.class);
	intent.putExtra(RecommendedSitesSearchResultsActivity.SEARCH_CRITERIA_EXTRA, criteria);
	startActivity(intent);
    }
    
    private RecommendedSitesSearchCriteria createCriteria() {
	String searchTerm = (searchBySpinner.getSelectedItemPosition() == RecommendedSitesSearchCriteria.CATEGORY_SEARCH_INDEX) ?
		(String)categorySpinner.getSelectedItem() : searchTermField.getText().toString();
	return new RecommendedSitesSearchCriteria(searchBySpinner.getSelectedItemPosition(), searchTerm);
    }
    
    private void loadSearch(final RecommendedSitesSearchCriteria criteria) {
	searchBySpinner.setSelection(criteria.searchBy);
	
	if (criteria.searchBy == RecommendedSitesSearchCriteria.CATEGORY_SEARCH_INDEX) {
	    if (criteria.searchTerm == null) {
		categorySpinner.setSelection(0);
	    }
	    else {
		for (int i=0; i<categorySpinner.getAdapter().getCount(); i++) {
		    if (criteria.searchTerm.equals(categorySpinner.getAdapter().getItem(i))) {
			categorySpinner.setSelection(i);
			break;
		    }
		}
	    }

	    searchTermField.setText("");
	}
	else {
	    categorySpinner.setSelection(0);
	    searchTermField.setText(criteria.searchTerm);
	}
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
	SharedPreferences prefs = getSharedPreferences(RecommendedSitesSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(key)) {
	    final Map<String, RecommendedSitesSearchCriteria> searches = (prefs.contains(key)) ? 
		    RecommendedSitesSearchCriteria.convertFromJson(prefs.getString(key, null)) : new HashMap<String, RecommendedSitesSearchCriteria>();
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
		    final SharedPreferences prefs = getSharedPreferences(RecommendedSitesSearchCriteriaActivity.TAG, 0);
		    final Map<String, RecommendedSitesSearchCriteria> searches = (prefs.contains(SEARCHES_PREFERENCE_KEY)) ? 
			    RecommendedSitesSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null)) : 
				new HashMap<String, RecommendedSitesSearchCriteria>();

			    // Add the current search
			    searches.put(value, createCriteria());

			    // Save the searches
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putString(SEARCHES_PREFERENCE_KEY, RecommendedSitesSearchCriteria.convertToJson(searches));
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
	final SharedPreferences prefs = getSharedPreferences(RecommendedSitesSearchCriteriaActivity.TAG, 0);
	if (prefs.contains(SEARCHES_PREFERENCE_KEY)) {
	    //final SearchCriteriaCollection searches = new SearchCriteriaCollection(prefs.getString(key, null));
	    final Map<String, RecommendedSitesSearchCriteria> searches = RecommendedSitesSearchCriteria.convertFromJson(prefs.getString(SEARCHES_PREFERENCE_KEY, null));
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
			editor.putString(SEARCHES_PREFERENCE_KEY, RecommendedSitesSearchCriteria.convertToJson(searches));
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
