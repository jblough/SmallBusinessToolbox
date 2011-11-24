package com.josephblough.sbt.activities.results;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.DistrictOfficeDataAdapter;
import com.josephblough.sbt.callbacks.DistrictOfficeRetrieverCallback;
import com.josephblough.sbt.criteria.DistrictOfficeSearchCriteria;
import com.josephblough.sbt.data.SbaDistrictOffice;
import com.josephblough.sbt.tasks.DistrictOfficeRetrieverTask;

public class DistrictOfficeSearchResultsActivity extends ListActivity implements DistrictOfficeRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "DistrictOfficeSearchResultsActivity.SearchCriteria";
    
    private List<SbaDistrictOffice> data = null;
    private DistrictOfficeSearchCriteria criteria = null;
    private ProgressDialog progress;

    private TextView titleLabel;
    private TextView nameLabel;
    private TextView street1Label;
    private TextView street2Label;
    private TextView locationLabel;
    
    private TableRow titleRow;
    private TableRow nameRow;
    private TableRow street1Row;
    private TableRow street2Row;
    private TableRow locationRow;
    
    private Button dismissDetailsButton;
    private Button mapAddressButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.district_office_search_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving SBA District Offices", true, true);
	    new DistrictOfficeRetrieverTask(this, this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.district_office_details_table);
	detailsControls = findViewById(R.id.district_office_details_controls);
	dismissDetailsButton = (Button)findViewById(R.id.district_office_details_dismiss_details);
	mapAddressButton = (Button)findViewById(R.id.district_office_details_map_site);

	titleLabel = (TextView)findViewById(R.id.district_office_details_title_value);
	nameLabel = (TextView)findViewById(R.id.district_office_details_name_value);
	street1Label = (TextView)findViewById(R.id.district_office_details_street1_value);
	street2Label = (TextView)findViewById(R.id.district_office_details_street2_value);
	locationLabel = (TextView)findViewById(R.id.district_office_details_location_value);

	titleRow = (TableRow)findViewById(R.id.district_office_details_title_row);
	nameRow = (TableRow)findViewById(R.id.district_office_details_name_row);
	street1Row = (TableRow)findViewById(R.id.district_office_details_street1_row);
	street2Row = (TableRow)findViewById(R.id.district_office_details_street2_row);
	locationRow = (TableRow)findViewById(R.id.district_office_details_location_row);
	
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
		SbaDistrictOffice office = ((DistrictOfficeDataAdapter)getListAdapter()).getItem(position);
		showDetails(office);

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(office.title));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, office.formatForSharing());
		startActivity(Intent.createChooser(sharingIntent,"Share using"));
		return true;
	    }
	});
    }
    
    public void success(List<SbaDistrictOffice> results) {
	this.data = results;
	
	Collections.sort(this.data, new Comparator<SbaDistrictOffice>() {

	    public int compare(SbaDistrictOffice office1, SbaDistrictOffice office2) {
		return office1.title.compareTo(office2.title);
	    }
	});

	DistrictOfficeDataAdapter adapter = new DistrictOfficeDataAdapter(this, this.data);
	setListAdapter(adapter);
	
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	if (this.data == null || this.data.size() == 0)
	    Toast.makeText(this, R.string.no_data_returned, Toast.LENGTH_LONG).show();
    }

    public void error(String error) {
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	Toast.makeText(this, error, Toast.LENGTH_LONG).show();
	finish();
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	final SbaDistrictOffice selectedItem = (SbaDistrictOffice)getListAdapter().getItem(position);

	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final SbaDistrictOffice office) {
	// Title
	if (!isEmpty(office.title)) {
	    titleLabel.setText(office.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	
	// Name
	if (!isEmpty(office.name)) {
	    nameLabel.setText(office.name);
	    nameRow.setVisibility(View.VISIBLE);
	}
	else
	    nameRow.setVisibility(View.GONE);
	
	// Street1
	if (!isEmpty(office.street)) {
	    street1Label.setText(office.street);
	    street1Row.setVisibility(View.VISIBLE);
	}
	else
	    street1Row.setVisibility(View.GONE);
	
	// Street2
	if (!isEmpty(office.street2)) {
	    street2Label.setText(office.name);
	    street2Row.setVisibility(View.VISIBLE);
	}
	else
	    street2Row.setVisibility(View.GONE);
	
	// Location
	final String location = formatLocation(office);
	if (!isEmpty(location)) {
	    locationLabel.setText(location);
	    locationRow.setVisibility(View.VISIBLE);
	}
	else
	    locationRow.setVisibility(View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	mapAddressButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		mapAddress(office);
	    }
	});
    }
    
    private String formatLocation(final SbaDistrictOffice office) {
	final StringBuffer location = new StringBuffer();
	if (!isEmpty(office.city)) {
	    location.append(office.city);
	}
	if (!isEmpty(office.province)) {
	    if (location.length() > 0)
		location.append(", ");
	    location.append(office.province);
	}
	if (!isEmpty(office.postalCode)) {
	    if (location.length() > 0)
		location.append(" ");
	    location.append(office.postalCode);
	}
	return location.toString();
    }
    
    private void mapAddress(final SbaDistrictOffice office) {
	//Toast.makeText(DistrictOfficeSearchResultsActivity.this, "Go to map...", Toast.LENGTH_SHORT).show();
	Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
		Uri.parse("geo:0,0?q=" + office.latitude + "," + office.longitude + 
			" (" + office.title + " " + office.street + ")"));
	startActivity(intent);
    }

    // Hide the details view on BACK key press if it's showing
    @Override
    public void onBackPressed() {
	if (detailsView.isShown()) {
	    detailsView.setVisibility(View.GONE);
	    detailsControls.setVisibility(View.GONE);
	}
	else {
	    super.onBackPressed();
	}
    }
}
