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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.GreenPostDataAdapter;
import com.josephblough.sbt.callbacks.GreenPostRetrieverCallback;
import com.josephblough.sbt.criteria.GreenSearchCriteria;
import com.josephblough.sbt.data.GreenPost;
import com.josephblough.sbt.tasks.GreenPostsRetrieverTask;
import com.josephblough.sbt.tasks.PdfCheckerTask;

public class GreenSearchResultsActivity extends ListActivity implements GreenPostRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "GreenSearchResultsActivity.SearchCriteria";
    
    private List<GreenPost> data = null;
    private GreenSearchCriteria criteria = null;
    private ProgressDialog progress;

    private TextView titleLabel;
    private TextView urlLabel;
    
    private TableRow titleRow;
    private TableRow urlRow;
    
    private Button addBookmarkButton;
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.green_search_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Green Posts", true, true);
	    new GreenPostsRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.green_details_table);
	detailsControls = findViewById(R.id.green_details_controls);
	addBookmarkButton = (Button)findViewById(R.id.green_details_add_bookmark);
	dismissDetailsButton = (Button)findViewById(R.id.green_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.green_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.green_details_title_value);
	urlLabel = (TextView)findViewById(R.id.green_details_url_value);

	titleRow = (TableRow)findViewById(R.id.green_details_title_row);
	urlRow = (TableRow)findViewById(R.id.green_details_url_row);
	
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
		GreenPost post = ((GreenPostDataAdapter)getListAdapter()).getItem(position);
		showDetails(post);

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(post.title));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, post.formatForSharing());
		startActivity(Intent.createChooser(sharingIntent,"Share using"));
		return true;
	    }
	});
    }
    
    public void success(List<GreenPost> results) {
	this.data = results;
	
	Collections.sort(this.data, new Comparator<GreenPost>() {

	    public int compare(GreenPost post1, GreenPost post2) {
		return post1.title.compareTo(post2.title);
	    }
	});

	GreenPostDataAdapter adapter = new GreenPostDataAdapter(this, this.data);
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
	final GreenPost selectedItem = (GreenPost)getListAdapter().getItem(position);

	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final GreenPost post) {
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
    
    private void visitData(final GreenPost post) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(post.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(post.url));
	startActivity(intent);
    }
    
    private void toggleBookmark(final GreenPost post) {
	ApplicationController app = (ApplicationController)getApplicationContext();
	
	if (app.bookmarks.isBookmarked(post)) {
		app.bookmarks.removeBookmark(post);
		addBookmarkButton.setText("Add Bookmark");
		//Toast.makeText(GreenSearchResultsActivity.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
	}
	else {
		app.bookmarks.addBookmark(post);
		addBookmarkButton.setText("Remove Bookmark");
		//Toast.makeText(GreenSearchResultsActivity.this, "Bookmark added", Toast.LENGTH_SHORT).show();
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
