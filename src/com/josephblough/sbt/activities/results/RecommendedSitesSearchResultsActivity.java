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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.RecommendedSitesDataAdapter;
import com.josephblough.sbt.callbacks.RecommendedSitesRetrieverCallback;
import com.josephblough.sbt.criteria.RecommendedSitesSearchCriteria;
import com.josephblough.sbt.data.RecommendedSite;
import com.josephblough.sbt.tasks.PdfCheckerTask;
import com.josephblough.sbt.tasks.RecommendedSitesRetrieverTask;

public class RecommendedSitesSearchResultsActivity extends ListActivity implements RecommendedSitesRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "RecommendedSitesSearchResultsActivity.SearchCriteria";
    
    private List<RecommendedSite> data = null;
    private RecommendedSitesSearchCriteria criteria = null;
    private ProgressDialog progress;
    
    private TextView titleLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView keywordsLabel;
    private TextView categoryLabel;
    private TextView ordersLabel;
    private TextView masterTermLabel;
    
    private TableRow titleRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow keywordsRow;
    private TableRow categoryRow;
    private TableRow ordersRow;
    private TableRow masterTermRow;
    
    private Button addBookmarkButton;
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.recommended_sites_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Recommended Sites", true, true);
	    new RecommendedSitesRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.recommended_sites_details_table);
	detailsControls = findViewById(R.id.recommended_sites_details_controls);
	addBookmarkButton = (Button)findViewById(R.id.recommended_sites_details_add_bookmark);
	dismissDetailsButton = (Button)findViewById(R.id.recommended_sites_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.recommended_sites_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.recommended_sites_details_title_value);
	descriptionLabel = (TextView)findViewById(R.id.recommended_sites_details_description_value);
	urlLabel = (TextView)findViewById(R.id.recommended_sites_details_url_value);
	keywordsLabel = (TextView)findViewById(R.id.recommended_sites_details_keywords_value);
	categoryLabel = (TextView)findViewById(R.id.recommended_sites_details_category_value);
	ordersLabel = (TextView)findViewById(R.id.recommended_sites_details_orders_value);
	masterTermLabel = (TextView)findViewById(R.id.recommended_sites_details_master_term_value);

	titleRow = (TableRow)findViewById(R.id.recommended_sites_details_title_row);
	descriptionRow = (TableRow)findViewById(R.id.recommended_sites_details_description_row);
	urlRow = (TableRow)findViewById(R.id.recommended_sites_details_url_row);
	keywordsRow = (TableRow)findViewById(R.id.recommended_sites_details_keywords_row);
	categoryRow = (TableRow)findViewById(R.id.recommended_sites_details_category_row);
	ordersRow = (TableRow)findViewById(R.id.recommended_sites_details_orders_row);
	masterTermRow = (TableRow)findViewById(R.id.recommended_sites_details_master_term_row);
	
	keywordsLabel.setMovementMethod(ScrollingMovementMethod.getInstance());
	
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
    }
    
    public void success(List<RecommendedSite> results) {
	this.data = results;
	
	Collections.sort(this.data, new Comparator<RecommendedSite>() {

	    public int compare(RecommendedSite site1, RecommendedSite site2) {
		return site1.title.compareTo(site2.title);
	    }
	});

	RecommendedSitesDataAdapter adapter = new RecommendedSitesDataAdapter(this, this.data);
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
	final RecommendedSite selectedItem = (RecommendedSite)getListAdapter().getItem(position);

	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final RecommendedSite site) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(site.title)) {
	    titleLabel.setText(site.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	// Description
	if (!isEmpty(site.description)) {
	    descriptionLabel.setText(site.description);
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(site.url)) {
	    urlLabel.setText(site.url);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Keywords
	if (!isEmpty(site.keywords)) {
	    keywordsLabel.setText(Html.fromHtml(site.keywords));
	    // Reset the label scroll to the top
	    keywordsLabel.scrollTo(0, 0);
	    keywordsRow.setVisibility(View.VISIBLE);
	}
	else
	    keywordsRow.setVisibility(View.GONE);
	
	// Category
	if (!isEmpty(site.category)) {
	    categoryLabel.setText(site.category);
	    categoryRow.setVisibility(View.VISIBLE);
	}
	else
	    categoryRow.setVisibility(View.GONE);
	
	// Orders
	if (!isEmpty(site.orders)) {
	    ordersLabel.setText(site.orders);
	    ordersRow.setVisibility(View.VISIBLE);
	}
	else
	    ordersRow.setVisibility(View.GONE);
	
	// Master term
	if (!isEmpty(site.masterTerm)) {
	    masterTermLabel.setText(site.masterTerm);
	    masterTermRow.setVisibility(View.VISIBLE);
	}
	else
	    masterTermRow.setVisibility(View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	addBookmarkButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		toggleBookmark(site);
	    }
	});
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(site);
	    }
	});
	
	if (((ApplicationController)getApplicationContext()).bookmarks.isBookmarked(site))
	    addBookmarkButton.setText("Remove Bookmark");
	else
	    addBookmarkButton.setText("Add Bookmark");
    }
    
    private void visitData(final RecommendedSite site) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(site.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(site.url));
	startActivity(intent);
    }
    
    private void toggleBookmark(final RecommendedSite site) {
	ApplicationController app = (ApplicationController)getApplicationContext();
	
	if (app.bookmarks.isBookmarked(site)) {
		app.bookmarks.removeBookmark(site);
		addBookmarkButton.setText("Add Bookmark");
		//Toast.makeText(RecommendedSitesSearchResultsActivity.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
	}
	else {
		app.bookmarks.addBookmark(site);
		addBookmarkButton.setText("Remove Bookmark");
		//Toast.makeText(RecommendedSitesSearchResultsActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
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
