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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.LoanAndGrantDataAdapter;
import com.josephblough.sbt.callbacks.LoansAndGrantsRetrieverCallback;
import com.josephblough.sbt.criteria.LoansAndGrantsSearchCriteria;
import com.josephblough.sbt.data.LoanAndGrantData;
import com.josephblough.sbt.tasks.LoansAndGrantsRetrieverTask;
import com.josephblough.sbt.tasks.PdfCheckerTask;

public class LoansAndGrantsSearchResultsActivity extends ListActivity implements
	LoansAndGrantsRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "LoansAndGrantsSearchResultsActivity.SearchCriteria";
    
    private List<LoanAndGrantData> data = null;
    private LoansAndGrantsSearchCriteria criteria = null;
    private ProgressDialog progress;

    private TextView titleLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView locationLabel;
    private TextView governmentTypeLabel;
    private TextView loanTypeLabel;
    private TextView agencyLabel;
    private TextView industryLabel;
    
    private TableRow titleRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow locationRow;
    private TableRow governmentTypeRow;
    private TableRow loanTypeRow;
    private TableRow agencyRow;
    private TableRow industryRow;
    // Special qualifications rows
    private TableRow generalPurposeRow;
    private TableRow developmentRow;
    private TableRow exportingRow;
    private TableRow contractorRow;
    private TableRow greenRow;
    private TableRow militaryRow;
    private TableRow minorityRow;
    private TableRow womanRow;
    private TableRow disabledRow;
    private TableRow ruralRow;
    private TableRow disasterRow;
    
    private Button addBookmarkButton;
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.loans_and_grants_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Loans and Grants", true, true);
	    new LoansAndGrantsRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.loans_and_grants_details_table);
	detailsControls = findViewById(R.id.loans_and_grants_details_controls);
	addBookmarkButton = (Button)findViewById(R.id.loans_and_grants_details_add_bookmark);
	dismissDetailsButton = (Button)findViewById(R.id.loans_and_grants_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.loans_and_grants_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.loans_and_grants_details_title_value);
	descriptionLabel = (TextView)findViewById(R.id.loans_and_grants_details_description_value);
	urlLabel = (TextView)findViewById(R.id.loans_and_grants_details_url_value);
	locationLabel = (TextView)findViewById(R.id.loans_and_grants_details_location_value);
	governmentTypeLabel = (TextView)findViewById(R.id.loans_and_grants_details_government_type_value);
	loanTypeLabel = (TextView)findViewById(R.id.loans_and_grants_details_loan_type_value);
	agencyLabel = (TextView)findViewById(R.id.loans_and_grants_details_agency_value);
	industryLabel = (TextView)findViewById(R.id.loans_and_grants_details_industry_value);

	titleRow = (TableRow)findViewById(R.id.loans_and_grants_details_title_row);
	descriptionRow = (TableRow)findViewById(R.id.loans_and_grants_details_description_row);
	urlRow = (TableRow)findViewById(R.id.loans_and_grants_details_url_row);
	locationRow = (TableRow)findViewById(R.id.loans_and_grants_details_location_row);
	governmentTypeRow = (TableRow)findViewById(R.id.loans_and_grants_details_government_type_row);
	loanTypeRow = (TableRow)findViewById(R.id.loans_and_grants_details_loan_type_row);
	agencyRow = (TableRow)findViewById(R.id.loans_and_grants_details_agency_row);
	industryRow = (TableRow)findViewById(R.id.loans_and_grants_details_industry_row);
	
	// Special qualifications rows
	generalPurposeRow = (TableRow)findViewById(R.id.loans_and_grants_details_general_purpose_row);
	developmentRow = (TableRow)findViewById(R.id.loans_and_grants_details_development_row);
	exportingRow = (TableRow)findViewById(R.id.loans_and_grants_details_exporting_row);
	contractorRow = (TableRow)findViewById(R.id.loans_and_grants_details_contractor_row);
	greenRow = (TableRow)findViewById(R.id.loans_and_grants_details_green_row);
	militaryRow = (TableRow)findViewById(R.id.loans_and_grants_details_military_row);
	minorityRow = (TableRow)findViewById(R.id.loans_and_grants_details_minority_row);
	womanRow = (TableRow)findViewById(R.id.loans_and_grants_details_woman_row);
	disabledRow = (TableRow)findViewById(R.id.loans_and_grants_details_disabled_row);
	ruralRow = (TableRow)findViewById(R.id.loans_and_grants_details_rural_row);
	disasterRow = (TableRow)findViewById(R.id.loans_and_grants_details_disaster_row);
	
	descriptionLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
    }
    
    public void success(List<LoanAndGrantData> results) {
	this.data = results;
	
	Collections.sort(this.data, new Comparator<LoanAndGrantData>() {

	    public int compare(LoanAndGrantData data1, LoanAndGrantData data2) {
		return data1.title.compareTo(data2.title);
	    }
	});

	LoanAndGrantDataAdapter adapter = new LoanAndGrantDataAdapter(this, this.data);
	setListAdapter(adapter);
	
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	if (this.data == null || this.data.size() == 0)
	    Toast.makeText(this, "No data returned", Toast.LENGTH_LONG).show();
    }

    public void error(String error) {
	if (progress != null) {
	    progress.dismiss();
	    progress = null;
	}
	
	Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	Toast.makeText(this, "More specific search parameters may be needed to reduce the amount of data returned", Toast.LENGTH_LONG).show();
	finish();
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	final LoanAndGrantData selectedItem = (LoanAndGrantData)getListAdapter().getItem(position);

	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final LoanAndGrantData loanAndGrantData) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(loanAndGrantData.title)) {
	    titleLabel.setText(loanAndGrantData.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	// Description
	if (!isEmpty(loanAndGrantData.description)) {
	    descriptionLabel.setText(Html.fromHtml(loanAndGrantData.description));
	    // Reset the label scroll to the top
	    descriptionLabel.scrollTo(0, 0);
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(loanAndGrantData.url)) {
	    urlLabel.setText(loanAndGrantData.url);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Location
	if (!isEmpty(loanAndGrantData.state)) {
	    locationLabel.setText(loanAndGrantData.state);
	    locationRow.setVisibility(View.VISIBLE);
	}
	else
	    locationRow.setVisibility(View.GONE);
	
	// Government type
	if (!isEmpty(loanAndGrantData.governmentType)) {
	    governmentTypeLabel.setText(loanAndGrantData.governmentType);
	    governmentTypeRow.setVisibility(View.VISIBLE);
	}
	else
	    governmentTypeRow.setVisibility(View.GONE);
	
	// Loan type
	if (!isEmpty(loanAndGrantData.loanType)) {
	    loanTypeLabel.setText(loanAndGrantData.loanType);
	    loanTypeRow.setVisibility(View.VISIBLE);
	}
	else
	    loanTypeRow.setVisibility(View.GONE);
	
	// Agency
	if (!isEmpty(loanAndGrantData.agency)) {
	    agencyLabel.setText(loanAndGrantData.agency);
	    agencyRow.setVisibility(View.VISIBLE);
	}
	else
	    agencyRow.setVisibility(View.GONE);
	
	// Industry
	if (!isEmpty(loanAndGrantData.industry)) {
	    industryLabel.setText(loanAndGrantData.industry);
	    industryRow.setVisibility(View.VISIBLE);
	}
	else
	    industryRow.setVisibility(View.GONE);
	
	generalPurposeRow.setVisibility(loanAndGrantData.isGeneralPurpose ? View.VISIBLE : View.GONE);
	developmentRow.setVisibility(loanAndGrantData.isDevelopment ? View.VISIBLE : View.GONE);
	exportingRow.setVisibility(loanAndGrantData.isExporting ? View.VISIBLE : View.GONE);
	contractorRow.setVisibility(loanAndGrantData.isContractorOnly ? View.VISIBLE : View.GONE);
	greenRow.setVisibility(loanAndGrantData.isGreen ? View.VISIBLE : View.GONE);
	militaryRow.setVisibility(loanAndGrantData.isMilitaryOnly ? View.VISIBLE : View.GONE);
	minorityRow.setVisibility(loanAndGrantData.isMinority ? View.VISIBLE : View.GONE);
	womanRow.setVisibility(loanAndGrantData.isWomenOnly ? View.VISIBLE : View.GONE);
	disabledRow.setVisibility(loanAndGrantData.isDisabledOnly ? View.VISIBLE : View.GONE);
	ruralRow.setVisibility(loanAndGrantData.isRural ? View.VISIBLE : View.GONE);
	disasterRow.setVisibility(loanAndGrantData.isDisaster ? View.VISIBLE : View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	addBookmarkButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		toggleBookmark(loanAndGrantData);
	    }
	});
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(loanAndGrantData);
	    }
	});
	
	if (((ApplicationController)getApplicationContext()).bookmarks.isBookmarked(loanAndGrantData))
	    addBookmarkButton.setText("Remove Bookmark");
	else
	    addBookmarkButton.setText("Add Bookmark");
    }
    
    private void visitData(final LoanAndGrantData loanAndGrantData) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(loanAndGrantData.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(loanAndGrantData.url));
	startActivity(intent);
    }
    
    private void toggleBookmark(final LoanAndGrantData loanAndGrantData) {
	ApplicationController app = (ApplicationController)getApplicationContext();
	
	if (app.bookmarks.isBookmarked(loanAndGrantData)) {
		app.bookmarks.removeBookmark(loanAndGrantData);
		addBookmarkButton.setText("Add Bookmark");
		//Toast.makeText(LoansAndGrantsSearchResultsActivity.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
	}
	else {
		app.bookmarks.addBookmark(loanAndGrantData);
		addBookmarkButton.setText("Remove Bookmark");
		//Toast.makeText(LoansAndGrantsSearchResultsActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
	}
	app.saveBookmarks();
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
