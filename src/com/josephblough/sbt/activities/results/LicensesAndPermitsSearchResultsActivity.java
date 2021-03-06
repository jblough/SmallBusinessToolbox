package com.josephblough.sbt.activities.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.LicenseAndPermitDataAdapter;
import com.josephblough.sbt.callbacks.LicensesAndPermitsRetrieverCallback;
import com.josephblough.sbt.criteria.LicensesAndPermitsSearchCriteria;
import com.josephblough.sbt.data.LicenseAndPermitData;
import com.josephblough.sbt.data.LicenseAndPermitDataCollection;
import com.josephblough.sbt.tasks.LicensesAndPermitsRetrieverTask;
import com.josephblough.sbt.tasks.PdfCheckerTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LicensesAndPermitsSearchResultsActivity extends SearchResultsActivity implements LicensesAndPermitsRetrieverCallback, OnItemClickListener {
    
    public final static String SEARCH_CRITERIA_EXTRA = "LicensesAndPermitsSearchResultsActivity.SearchCriteria";
    
    private LicenseAndPermitDataCollection data = null;
    private LicensesAndPermitsSearchCriteria criteria = null;
    private ProgressDialog progress;
    
    private TextView titleLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView locationLabel;
    private TextView categoryLabel;
    private TextView businessTypeLabel;
    private TextView sectionLabel;
    private TextView resourceGroupDescriptionLabel;
    
    private TableRow titleRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow locationRow;
    private TableRow categoryRow;
    private TableRow businessTypeRow;
    private TableRow sectionRow;
    private TableRow resourceGroupDescriptionRow;
    
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private Button shareUrlButton;
    private View detailsView;
    private View detailsControls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.licenses_and_permits_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Licenses and Permits", true, true);
	    new LicensesAndPermitsRetrieverTask(this).execute(criteria);
	}
	
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.licenses_and_permits_details_table);
	detailsControls = findViewById(R.id.detail_controls);
	dismissDetailsButton = (Button)findViewById(R.id.detail_controls_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.detail_controls_visit_link);
	shareUrlButton = (Button)findViewById(R.id.detail_controls_share_link);

	titleLabel = (TextView)findViewById(R.id.licenses_and_permits_details_title_value);
	descriptionLabel = (TextView)findViewById(R.id.licenses_and_permits_details_description_value);
	urlLabel = (TextView)findViewById(R.id.licenses_and_permits_details_url_value);
	locationLabel = (TextView)findViewById(R.id.licenses_and_permits_details_location_value);
	categoryLabel = (TextView)findViewById(R.id.licenses_and_permits_details_category_value);
	businessTypeLabel = (TextView)findViewById(R.id.licenses_and_permits_details_business_type_value);
	sectionLabel = (TextView)findViewById(R.id.licenses_and_permits_details_section_value);
	resourceGroupDescriptionLabel = (TextView)findViewById(R.id.licenses_and_permits_details_resource_group_description_value);

	titleRow = (TableRow)findViewById(R.id.licenses_and_permits_details_title_row);
	descriptionRow = (TableRow)findViewById(R.id.licenses_and_permits_details_description_row);
	urlRow = (TableRow)findViewById(R.id.licenses_and_permits_details_url_row);
	locationRow = (TableRow)findViewById(R.id.licenses_and_permits_details_location_row);
	categoryRow = (TableRow)findViewById(R.id.licenses_and_permits_details_category_row);
	businessTypeRow = (TableRow)findViewById(R.id.licenses_and_permits_details_business_type_row);
	sectionRow = (TableRow)findViewById(R.id.licenses_and_permits_details_section_row);
	resourceGroupDescriptionRow = (TableRow)findViewById(R.id.licenses_and_permits_details_resource_group_description_row);
	
	descriptionLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	resourceGroupDescriptionLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	
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

		LicenseAndPermitData licenseAndPermitData = (LicenseAndPermitData)getListAdapter().getItem(position);
		showDetails(licenseAndPermitData);
		share(licenseAndPermitData);
		return true;
	    }
	});
    }

    private void share(final LicenseAndPermitData licenseAndPermitData) {
	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	sharingIntent.setType("text/plain");
	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(licenseAndPermitData.getName()).toString());
	sharingIntent.putExtra(Intent.EXTRA_TEXT, licenseAndPermitData.formatForSharing());
	startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    public void success(LicenseAndPermitDataCollection results) {
	this.data = results;
	
	final List<LicenseAndPermitData> everything = new ArrayList<LicenseAndPermitData>();
	everything.addAll(data.businessTypes);
	everything.addAll(data.categories);
	everything.addAll(data.states);
	everything.addAll(data.counties);
	everything.addAll(data.localities);
	
	removeInvalidResults(everything);
	Collections.sort(everything, new LicenseAndPermitDataComparator());

	LicenseAndPermitDataAdapter adapter = new LicenseAndPermitDataAdapter(this, everything);
	setListAdapter(adapter);
	
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	if (everything.size() == 0)
	    Toast.makeText(this, R.string.no_data_returned, Toast.LENGTH_LONG).show();
	else {
	    if (((ApplicationController)getApplicationContext()).shouldShowTooltip(R.string.filter_results_tooltip))
		Toast.makeText(this, R.string.filter_results_tooltip, Toast.LENGTH_LONG).show();
	}
    }
/*
    public void success(LicenseAndPermitDataCollection results) {
	this.data = results;

        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
	
        LicenseAndPermitDataComparator comparator = new LicenseAndPermitDataComparator();
        
	removeInvalidResults(data.businessTypes);
	if (data.businessTypes.size() > 0) {
	    Collections.sort(data.businessTypes, comparator);
	    LicenseAndPermitDataAdapter dataAdapter = new LicenseAndPermitDataAdapter(this, data.businessTypes);
	    adapter.addSection("Business Types", dataAdapter);
	}
	
	removeInvalidResults(data.categories);
	if (data.categories.size() > 0) {
	    Collections.sort(data.categories, comparator);
	    LicenseAndPermitDataAdapter dataAdapter = new LicenseAndPermitDataAdapter(this, data.categories);
	    adapter.addSection("Categories", dataAdapter);
	}
	
	removeInvalidResults(data.states);
	if (data.states.size() > 0) {
	    Collections.sort(data.states, comparator);
	    LicenseAndPermitDataAdapter dataAdapter = new LicenseAndPermitDataAdapter(this, data.states);
	    adapter.addSection("States", dataAdapter);
	}
	
	removeInvalidResults(data.counties);
	if (data.counties.size() > 0) {
	    Collections.sort(data.counties, comparator);
	    LicenseAndPermitDataAdapter dataAdapter = new LicenseAndPermitDataAdapter(this, data.counties);
	    adapter.addSection("Counties", dataAdapter);
	}
	
	removeInvalidResults(data.localities);
	if (data.localities.size() > 0) {
	    Collections.sort(data.localities, comparator);
	    LicenseAndPermitDataAdapter dataAdapter = new LicenseAndPermitDataAdapter(this, data.localities);
	    adapter.addSection("Localiies", dataAdapter);
	}

	setListAdapter(adapter);
	
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	if (adapter.sections.size() == 0)
	    Toast.makeText(this, R.string.no_data_returned, Toast.LENGTH_LONG).show();
	else
	    Toast.makeText(this, R.string.filter_results_tooltip, Toast.LENGTH_LONG).show();
    }
*/
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

	final LicenseAndPermitData selectedItem = (LicenseAndPermitData)getListAdapter().getItem(position);
	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final LicenseAndPermitData licenseAndPermitData) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(licenseAndPermitData.title)) {
	    titleLabel.setText(licenseAndPermitData.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	// Description
	if (!isEmpty(licenseAndPermitData.description)) {
	    descriptionLabel.setText(licenseAndPermitData.description);
	    // Reset the label scroll to the top
	    descriptionLabel.scrollTo(0, 0);
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(licenseAndPermitData.url)) {
	    urlLabel.setText(licenseAndPermitData.url);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Location
	if (!isEmpty(licenseAndPermitData.state) || !isEmpty(licenseAndPermitData.county)) {
	    final StringBuffer location = new StringBuffer();
	    if (!isEmpty(licenseAndPermitData.county)) {
		location.append(licenseAndPermitData.county);
	    }
	    if (!isEmpty(licenseAndPermitData.state)) {
		if (location.length() > 0)
		    location.append(", ");
		location.append(licenseAndPermitData.state);
	    }
	    locationLabel.setText(location);
	    locationRow.setVisibility(View.VISIBLE);
	}
	else
	    locationRow.setVisibility(View.GONE);
	
	// Category
	if (!isEmpty(licenseAndPermitData.category)) {
	    categoryLabel.setText(licenseAndPermitData.category);
	    categoryRow.setVisibility(View.VISIBLE);
	}
	else
	    categoryRow.setVisibility(View.GONE);
	
	// Business Type
	if (!isEmpty(licenseAndPermitData.businessType)) {
	    businessTypeLabel.setText(licenseAndPermitData.businessType);
	    businessTypeRow.setVisibility(View.VISIBLE);
	}
	else
	    businessTypeRow.setVisibility(View.GONE);
	
	// Section
	if (!isEmpty(licenseAndPermitData.section)) {
	    sectionLabel.setText(licenseAndPermitData.section);
	    sectionRow.setVisibility(View.VISIBLE);
	}
	else
	    sectionRow.setVisibility(View.GONE);
	
	// Resource Group Description
	if (!isEmpty(licenseAndPermitData.resourceGroupDescription)) {
	    resourceGroupDescriptionLabel.setText(licenseAndPermitData.resourceGroupDescription);
	    // Reset the label scroll to the top
	    resourceGroupDescriptionLabel.scrollTo(0, 0);
	    resourceGroupDescriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    resourceGroupDescriptionRow.setVisibility(View.GONE);

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(licenseAndPermitData);
	    }
	});
	
	shareUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		share(licenseAndPermitData);
	    }
	});
    }
    
    private void visitData(final LicenseAndPermitData licenseAndPermitData) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(licenseAndPermitData.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(licenseAndPermitData.url));
	startActivity(intent);
    }
    
    private void removeInvalidResults(List<LicenseAndPermitData> results) {
	Iterator<LicenseAndPermitData> it = results.iterator();
	while (it.hasNext()) {
	    LicenseAndPermitData licenseAndPermitData = it.next();
	    if (!isValid(licenseAndPermitData))
		it.remove();
	}
    }
    
    private boolean isValid(final LicenseAndPermitData licenseAndPermitData) {
	final String name = licenseAndPermitData.getName();
	return (name != null && !"".equals(name) &&
		licenseAndPermitData.url != null && !"".equals(licenseAndPermitData.url) &&
		!"null".equals(licenseAndPermitData.url));
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
    
    private class LicenseAndPermitDataComparator implements Comparator<LicenseAndPermitData> {
	public int compare(LicenseAndPermitData data1, LicenseAndPermitData data2) {
	    return data1.getName().compareTo(data2.getName());
	}
    }
}
