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

public class GenericSearchResultsActivity extends ListActivity implements GenericPostRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "GenericSearchResultsActivity.SearchCriteria";
    
    private List<GenericPost> data = null;
    private GenericSearchCriteria criteria = null;
    private ProgressDialog progress;

    private TextView titleLabel;
    private TextView urlLabel;
    private TextView closeDateLabel;
    private TextView daysToCloseLabel;
    
    private TableRow titleRow;
    private TableRow urlRow;
    private TableRow closeDateRow;
    private TableRow daysToCloseRow;
    
    private Button addBookmarkButton;
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    private SimpleDateFormat closeDateParser = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat closeDateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
    
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
	detailsControls = findViewById(R.id.generic_details_controls);
	addBookmarkButton = (Button)findViewById(R.id.generic_details_add_bookmark);
	dismissDetailsButton = (Button)findViewById(R.id.generic_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.generic_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.generic_details_title_value);
	urlLabel = (TextView)findViewById(R.id.generic_details_url_value);
	closeDateLabel = (TextView)findViewById(R.id.generic_details_close_date_value);
	daysToCloseLabel = (TextView)findViewById(R.id.generic_details_days_to_close_value);

	titleRow = (TableRow)findViewById(R.id.generic_details_title_row);
	urlRow = (TableRow)findViewById(R.id.generic_details_url_row);
	closeDateRow = (TableRow)findViewById(R.id.generic_details_close_date_row);
	daysToCloseRow = (TableRow)findViewById(R.id.generic_details_days_to_close_row);
	
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
    }
    
    public void success(List<GenericPost> results) {
	this.data = results;
	
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
	
	// Close Date
	if (!isEmpty(post.closeDate)) {
	    try {
		Date closingDate = closeDateParser.parse(post.closeDate);
		closeDateLabel.setText(closeDateFormatter.format(closingDate));
	    }
	    catch (ParseException e) {
		closeDateLabel.setText(post.closeDate);
	    }
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
	
	addBookmarkButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		toggleBookmark(post);
	    }
	});
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(post);
	    }
	});
	
	if (((ApplicationController)getApplicationContext()).bookmarks.isBookmarked(post))
	    addBookmarkButton.setText("Remove Bookmark");
	else
	    addBookmarkButton.setText("Add Bookmark");
    }
    
    private void visitData(final GenericPost post) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(post.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(post.url));
	startActivity(intent);
    }
    
    private void toggleBookmark(final GenericPost post) {
	ApplicationController app = (ApplicationController)getApplicationContext();
	
	if (app.bookmarks.isBookmarked(post)) {
		app.bookmarks.removeBookmark(post);
		addBookmarkButton.setText("Add Bookmark");
		//Toast.makeText(GenericSearchResultsActivity.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
	}
	else {
		app.bookmarks.addBookmark(post);
		addBookmarkButton.setText("Remove Bookmark");
		//Toast.makeText(GenericSearchResultsActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
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
