package com.josephblough.sbt.activities.results;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.LocalityWebDataAdapter;
import com.josephblough.sbt.callbacks.LocalityWebDataRetrieverCallback;
import com.josephblough.sbt.criteria.LocalityWebDataSearchCriteria;
import com.josephblough.sbt.data.LocalityWebData;
import com.josephblough.sbt.tasks.LocalityWebDataRetrieverTask;
import com.josephblough.sbt.tasks.PdfCheckerTask;

public class LocalityWebDataResultsActivity extends SearchResultsActivity implements LocalityWebDataRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "LocalityWebDataResultsActivity.SearchCriteria";
    
    private List<LocalityWebData> data = null;
    private LocalityWebDataSearchCriteria criteria = null;
    private ProgressDialog progress;
    
    private TextView titleLabel;
    private TextView nameLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView countyLabel;
    private TextView locationLabel;
    private TextView featClassLabel;
    private TextView fipsClassLabel;
    
    private TableRow titleRow;
    private TableRow nameRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow countyRow;
    private TableRow locationRow;
    private TableRow featClassRow;
    private TableRow fipsClassRow;
    
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.locality_web_data_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Web Data", true, true);
	    new LocalityWebDataRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.locality_web_data_details_table);
	detailsControls = findViewById(R.id.locality_web_data_details_controls);
	dismissDetailsButton = (Button)findViewById(R.id.locality_web_data_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.locality_web_data_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.locality_web_data_details_title_value);
	nameLabel = (TextView)findViewById(R.id.locality_web_data_details_name_value);
	descriptionLabel = (TextView)findViewById(R.id.locality_web_data_details_description_value);
	urlLabel = (TextView)findViewById(R.id.locality_web_data_details_url_value);
	countyLabel = (TextView)findViewById(R.id.locality_web_data_details_county_value);
	locationLabel = (TextView)findViewById(R.id.locality_web_data_details_location_value);
	featClassLabel = (TextView)findViewById(R.id.locality_web_data_details_feat_value);
	fipsClassLabel = (TextView)findViewById(R.id.locality_web_data_details_fips_value);

	titleRow = (TableRow)findViewById(R.id.locality_web_data_details_title_row);
	nameRow = (TableRow)findViewById(R.id.locality_web_data_details_name_row);
	descriptionRow = (TableRow)findViewById(R.id.locality_web_data_details_description_row);
	urlRow = (TableRow)findViewById(R.id.locality_web_data_details_url_row);
	countyRow = (TableRow)findViewById(R.id.locality_web_data_details_county_row);
	locationRow = (TableRow)findViewById(R.id.locality_web_data_details_location_row);
	featClassRow = (TableRow)findViewById(R.id.locality_web_data_details_feat_row);
	fipsClassRow = (TableRow)findViewById(R.id.locality_web_data_details_fips_row);
	
	descriptionLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
	getListView().setTextFilterEnabled(true);
	
	// Set a long click handler
	getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    int position, long id) {
		hideSearch();

		LocalityWebData webData = ((LocalityWebDataAdapter)getListAdapter()).getItem(position);
		showDetails(webData);

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(webData.title));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, webData.formatForSharing());
		startActivity(Intent.createChooser(sharingIntent,"Share using"));
		return true;
	    }
	});
    }
    
    public void success(List<LocalityWebData> results) {
	this.data = results;
	removeInvalidResults();
	
	Collections.sort(this.data, new Comparator<LocalityWebData>() {

	    public int compare(LocalityWebData data1, LocalityWebData data2) {
		return data1.title.compareTo(data2.title);
	    }
	});

	LocalityWebDataAdapter adapter = new LocalityWebDataAdapter(this, this.data);
	setListAdapter(adapter);
	
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	if (this.data == null || this.data.size() == 0)
	    Toast.makeText(this, R.string.no_data_returned, Toast.LENGTH_LONG).show();
	else
	    Toast.makeText(this, R.string.filter_results_tooltip, Toast.LENGTH_LONG).show();
    }

    public void error(String error) {
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	Toast.makeText(this, R.string.too_much_data, Toast.LENGTH_LONG).show();
	finish();
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	hideSearch();

	final LocalityWebData selectedItem = (LocalityWebData)getListAdapter().getItem(position);
	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final LocalityWebData webData) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(webData.title)) {
	    titleLabel.setText(webData.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	
	// Name
	if (!isEmpty(webData.name)) {
	    nameLabel.setText(webData.name);
	    nameRow.setVisibility(View.VISIBLE);
	}
	else {
	    nameRow.setVisibility(View.GONE);
	}
	
	// Description
	if (!isEmpty(webData.description)) {
	    descriptionLabel.setText(webData.description);
	    // Reset the label scroll to the top
	    descriptionLabel.scrollTo(0, 0);
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(webData.url)) {
	    urlLabel.setText(webData.url);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// County
	if (!isEmpty(webData.fullCountyName)) {
	    countyLabel.setText(webData.fullCountyName);
	    countyRow.setVisibility(View.VISIBLE);
	}
	else
	    countyRow.setVisibility(View.GONE);
	
	// Location
	if (!isEmpty(webData.stateAbbreviation) || !isEmpty(webData.stateName)) {
	    locationLabel.setText((!isEmpty(webData.stateName)) ? webData.stateName : webData.stateAbbreviation);
	    locationRow.setVisibility(View.VISIBLE);
	}
	else
	    locationRow.setVisibility(View.GONE);
	
	// Feat Class
	if (!isEmpty(webData.featClass)) {
	    featClassLabel.setText(webData.featClass);
	    featClassRow.setVisibility(View.VISIBLE);
	}
	else
	    featClassRow.setVisibility(View.GONE);
	
	// Fips Class
	if (!isEmpty(webData.fipsClass)) {
	    fipsClassLabel.setText(webData.fipsClass);
	    fipsClassRow.setVisibility(View.VISIBLE);
	}
	else
	    fipsClassRow.setVisibility(View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(webData);
	    }
	});
    }
    
    private void visitData(final LocalityWebData webData) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(webData.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webData.url));
	startActivity(intent);
    }
    
    private void removeInvalidResults() {
	Iterator<LocalityWebData> it = this.data.iterator();
	while (it.hasNext()) {
	    LocalityWebData webData = it.next();
	    if (webData.name == null || "".equals(webData.name) || "null".equals(webData.name) ||
		    webData.url == null || "".equals(webData.url) || "null".equals(webData.url)) {
		it.remove();
	    }
	}
    }
    
    @Override
    protected boolean isDetailsViewShowing() {
	return detailsView.isShown();
    }

    @Override
    protected void hideDetailsView() {
	detailsView.setVisibility(View.GONE);
	detailsControls.setVisibility(View.GONE);
    }
}
