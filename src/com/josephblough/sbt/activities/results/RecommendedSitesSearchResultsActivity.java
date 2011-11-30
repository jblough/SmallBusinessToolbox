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

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.RecommendedSitesDataAdapter;
import com.josephblough.sbt.callbacks.RecommendedSitesRetrieverCallback;
import com.josephblough.sbt.criteria.RecommendedSitesSearchCriteria;
import com.josephblough.sbt.data.RecommendedSite;
import com.josephblough.sbt.tasks.PdfCheckerTask;
import com.josephblough.sbt.tasks.RecommendedSitesRetrieverTask;

public class RecommendedSitesSearchResultsActivity extends SearchResultsActivity implements RecommendedSitesRetrieverCallback, OnItemClickListener {

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
    
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private Button shareUrlButton;
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
	detailsControls = findViewById(R.id.detail_controls);
	dismissDetailsButton = (Button)findViewById(R.id.detail_controls_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.detail_controls_visit_link);
	shareUrlButton = (Button)findViewById(R.id.detail_controls_share_link);

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
	getListView().setTextFilterEnabled(true);
	
	// Set a long click handler
	getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    int position, long id) {
		hideSearch();

		RecommendedSite site = ((RecommendedSitesDataAdapter)getListAdapter()).getItem(position);
		showDetails(site);
		share(site);

		return true;
	    }
	});
    }
    
    private void share(final RecommendedSite site) {
	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	sharingIntent.setType("text/plain");
	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(site.title).toString());
	sharingIntent.putExtra(Intent.EXTRA_TEXT, site.formatForSharing());
	startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    public void success(List<RecommendedSite> results) {
	this.data = results;
	removeInvalidResults();
	
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
	    Toast.makeText(this, R.string.no_data_returned, Toast.LENGTH_LONG).show();
	else {
	    if (((ApplicationController)getApplicationContext()).shouldShowTooltip(R.string.filter_results_tooltip))
		Toast.makeText(this, R.string.filter_results_tooltip, Toast.LENGTH_LONG).show();
	}
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
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(site);
	    }
	});
	
	shareUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		share(site);
	    }
	});
    }
    
    private void visitData(final RecommendedSite site) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(site.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(site.url));
	startActivity(intent);
    }
    
    private void removeInvalidResults() {
	Iterator<RecommendedSite> it = this.data.iterator();
	while (it.hasNext()) {
	    RecommendedSite site = it.next();
	    if (site.title == null || "".equals(site.title) ||
		    site.url == null || "".equals(site.url)) {
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
