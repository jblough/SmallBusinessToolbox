package com.josephblough.sbt.activities.results;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.GenericPostDataAdapter;
import com.josephblough.sbt.callbacks.GenericPostRetrieverCallback;
import com.josephblough.sbt.criteria.GenericSearchCriteria;
import com.josephblough.sbt.data.GenericPost;
import com.josephblough.sbt.tasks.GenericPostsRetrieverTask;
import com.josephblough.sbt.tasks.PdfCheckerTask;

public class GenericSearchResultsActivity extends SearchResultsActivity implements GenericPostRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "GenericSearchResultsActivity.SearchCriteria";
    
    private List<GenericPost> data = null;
    private GenericSearchCriteria criteria = null;
    private ProgressDialog progress;

    private TextView titleLabel;
    private TextView urlLabel;
    private TextView createDateLabel;
    private TextView closeDateLabel;
    private TextView daysToCloseLabel;
    
    private TableRow titleRow;
    private TableRow urlRow;
    private TableRow createDateRow;
    private TableRow closeDateRow;
    private TableRow daysToCloseRow;
    
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private Button shareUrlButton;
    private View detailsView;
    private View detailsControls;
    
    private SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");

    private SimpleDateFormat date2Parser = new SimpleDateFormat("MMM dd, yyyy");
    
    private SimpleDateFormat dateWithTimeParser = new SimpleDateFormat(" \tMMM dd, yyyy h:mm a");
    private SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("MMMMM dd, yyyy h:mm a");
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.generic_search_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Posts", true, true);
	    new GenericPostsRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.generic_details_table);
	detailsControls = findViewById(R.id.detail_controls);
	dismissDetailsButton = (Button)findViewById(R.id.detail_controls_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.detail_controls_visit_link);
	shareUrlButton = (Button)findViewById(R.id.detail_controls_share_link);

	titleLabel = (TextView)findViewById(R.id.generic_details_title_value);
	urlLabel = (TextView)findViewById(R.id.generic_details_url_value);
	createDateLabel = (TextView)findViewById(R.id.generic_details_create_date_value);
	closeDateLabel = (TextView)findViewById(R.id.generic_details_close_date_value);
	daysToCloseLabel = (TextView)findViewById(R.id.generic_details_days_to_close_value);

	titleRow = (TableRow)findViewById(R.id.generic_details_title_row);
	urlRow = (TableRow)findViewById(R.id.generic_details_url_row);
	createDateRow = (TableRow)findViewById(R.id.generic_details_create_date_row);
	closeDateRow = (TableRow)findViewById(R.id.generic_details_close_date_row);
	daysToCloseRow = (TableRow)findViewById(R.id.generic_details_days_to_close_row);
	
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

		GenericPost post = ((GenericPostDataAdapter)getListAdapter()).getItem(position);
		showDetails(post);
		share(post);

		return true;
	    }
	});
    }
    
    private void share(final GenericPost post) {
	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	sharingIntent.setType("text/plain");
	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(post.title).toString());
	sharingIntent.putExtra(Intent.EXTRA_TEXT, post.formatForSharing());
	startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    public void success(List<GenericPost> results) {
	this.data = results;
	removeInvalidResults();
	
	Collections.sort(this.data, new Comparator<GenericPost>() {

	    public int compare(GenericPost post1, GenericPost post2) {
		return post1.title.compareTo(post2.title);
	    }
	});

	GenericPostDataAdapter adapter = new GenericPostDataAdapter(this, this.data);
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

	final GenericPost selectedItem = (GenericPost)getListAdapter().getItem(position);
	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final GenericPost post) {
	if (!isEmpty(post.title)) {
	    titleLabel.setText(post.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	
	// URL
	if (!isEmpty(post.url)) {
	    urlLabel.setText(post.url);
	    urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Create Date
	if (!isEmpty(post.createDate)) {
	    createDateLabel.setText(convertDateToString(post.createDate));
	    createDateRow.setVisibility(View.VISIBLE);
	}
	else
	    createDateRow.setVisibility(View.GONE);
	
	// Close Date
	if (!isEmpty(post.closeDate)) {
	    closeDateLabel.setText(convertDateToString(post.closeDate));
	    closeDateRow.setVisibility(View.VISIBLE);
	}
	else
	    closeDateRow.setVisibility(View.GONE);
	
	// Days to Close
	if (!isEmpty(post.daysToClose)) {
	    daysToCloseLabel.setText(post.daysToClose);
	    daysToCloseRow.setVisibility(View.VISIBLE);
	}
	else
	    daysToCloseRow.setVisibility(View.GONE);

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(post);
	    }
	});
	
	shareUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		share(post);
	    }
	});
    }
    
    private void visitData(final GenericPost post) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(post.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(post.url));
	startActivity(intent);
    }
    
    private void removeInvalidResults() {
	Iterator<GenericPost> it = this.data.iterator();
	while (it.hasNext()) {
	    GenericPost post = it.next();
	    if (post.title == null || "".equals(post.title) ||
		    post.url == null || "".equals(post.url)) {
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

    private String convertDateToString(final String dateString) {
	final String trimmedDateString = dateString.trim();
	
	try {
	    Date date = dateParser.parse(trimmedDateString);
	    return dateFormatter.format(date);
	}
	catch (ParseException e) {
	}
	
	try {
	    Date date = date2Parser.parse(trimmedDateString);
	    return dateFormatter.format(date);
	}
	catch (ParseException e) {
	}
	
	try {
	    Date date = dateWithTimeParser.parse(trimmedDateString);
	    return dateWithTimeFormatter.format(date);
	}
	catch (ParseException e) {
	}
	
	// Fall back to the original date string
	return trimmedDateString;
    }
}
