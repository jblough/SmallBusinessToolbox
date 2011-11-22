package com.josephblough.sbt.activities.results;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
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

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.SolicitationDataAdapter;
import com.josephblough.sbt.callbacks.SolicitationsRetrieverCallback;
import com.josephblough.sbt.criteria.SolicitationsSearchCriteria;
import com.josephblough.sbt.data.Solicitation;
import com.josephblough.sbt.tasks.PdfCheckerTask;
import com.josephblough.sbt.tasks.SolicitationsRetrieverTask;

public class SolicitationsSearchResultsActivity extends ListActivity implements SolicitationsRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "SolicitationsSearchResultsActivity.SearchCriteria";
    
    private List<Solicitation> data = null;
    private SolicitationsSearchCriteria criteria = null;
    private ProgressDialog progress;
    
    private TextView titleLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView agencyLabel;
    private TextView statusLabel;
    private TextView closeDateLabel;
    
    private TableRow titleRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow agencyRow;
    private TableRow statusRow;
    private TableRow closeDateRow;
    
    private Button addBookmarkButton;
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    private SimpleDateFormat closeDateParser = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat closeDateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.solicitations_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Solicitations", true, true);
	    new SolicitationsRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.solicitations_details_table);
	detailsControls = findViewById(R.id.solicitations_details_controls);
	addBookmarkButton = (Button)findViewById(R.id.solicitations_details_add_bookmark);
	dismissDetailsButton = (Button)findViewById(R.id.solicitations_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.solicitations_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.solicitations_details_title_value);
	descriptionLabel = (TextView)findViewById(R.id.solicitations_details_description_value);
	urlLabel = (TextView)findViewById(R.id.solicitations_details_url_value);
	agencyLabel = (TextView)findViewById(R.id.solicitations_details_agency_value);
	statusLabel = (TextView)findViewById(R.id.solicitations_details_status_value);
	closeDateLabel = (TextView)findViewById(R.id.solicitations_details_close_date_value);

	titleRow = (TableRow)findViewById(R.id.solicitations_details_title_row);
	descriptionRow = (TableRow)findViewById(R.id.solicitations_details_description_row);
	urlRow = (TableRow)findViewById(R.id.solicitations_details_url_row);
	agencyRow = (TableRow)findViewById(R.id.solicitations_details_agency_row);
	statusRow = (TableRow)findViewById(R.id.solicitations_details_status_row);
	closeDateRow = (TableRow)findViewById(R.id.solicitations_details_close_date_row);
	
	descriptionLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
	
	// Set a long click handler
	getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Solicitation solicitation = ((SolicitationDataAdapter)getListAdapter()).getItem(position);
		showDetails(solicitation);

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(solicitation.title));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, solicitation.formatForSharing());
		startActivity(Intent.createChooser(sharingIntent,"Share using"));
		return true;
	    }
	});
    }
    
    public void success(List<Solicitation> results) {
	this.data = results;
	
	Collections.sort(this.data, new Comparator<Solicitation>() {

	    public int compare(Solicitation solicitation1, Solicitation solicitation2) {
		return solicitation1.title.compareTo(solicitation2.title);
	    }
	});

	SolicitationDataAdapter adapter = new SolicitationDataAdapter(this, this.data);
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
	final Solicitation selectedItem = (Solicitation)getListAdapter().getItem(position);

	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final Solicitation solicitation) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(solicitation.title)) {
	    titleLabel.setText(Html.fromHtml(solicitation.title));
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	
	// Description
	if (!isEmpty(solicitation.description)) {
	    descriptionLabel.setText(Html.fromHtml(solicitation.description));
	    // Reset the label scroll to the top
	    descriptionLabel.scrollTo(0, 0);
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(solicitation.link)) {
	    urlLabel.setText(solicitation.link);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Agency
	if (!isEmpty(solicitation.agency)) {
	    agencyLabel.setText(solicitation.agency);
	    agencyRow.setVisibility(View.VISIBLE);
	}
	else
	    agencyRow.setVisibility(View.GONE);
	
	// Status
	if (!isEmpty(solicitation.status)) {
	    statusLabel.setText(solicitation.status);
	    statusRow.setVisibility(View.VISIBLE);
	}
	else
	    statusRow.setVisibility(View.GONE);
	
	// Close Date
	if (!isEmpty(solicitation.closeDate)) {
	    try {
		Date closingDate = closeDateParser.parse(solicitation.closeDate);
		closeDateLabel.setText(closeDateFormatter.format(closingDate));
	    }
	    catch (ParseException e) {
		closeDateLabel.setText(solicitation.closeDate);
	    }
	    closeDateRow.setVisibility(View.VISIBLE);
	}
	else
	    closeDateRow.setVisibility(View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	addBookmarkButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		toggleBookmark(solicitation);
	    }
	});
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(solicitation);
	    }
	});
	
	if (((ApplicationController)getApplicationContext()).bookmarks.isBookmarked(solicitation))
	    addBookmarkButton.setText("Remove Bookmark");
	else
	    addBookmarkButton.setText("Add Bookmark");
    }
    
    private void visitData(final Solicitation solicitation) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(solicitation.link);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(solicitation.link));
	startActivity(intent);
    }
    
    private void toggleBookmark(final Solicitation solicitation) {
	ApplicationController app = (ApplicationController)getApplicationContext();
	
	if (app.bookmarks.isBookmarked(solicitation)) {
		app.bookmarks.removeBookmark(solicitation);
		addBookmarkButton.setText("Add Bookmark");
		//Toast.makeText(SolicitationsSearchResultsActivity.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
	}
	else {
		app.bookmarks.addBookmark(solicitation);
		addBookmarkButton.setText("Remove Bookmark");
		//Toast.makeText(SolicitationsSearchResultsActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
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
