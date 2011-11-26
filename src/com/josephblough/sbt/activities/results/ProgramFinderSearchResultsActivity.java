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
import com.josephblough.sbt.adapters.SmallBusinessProgramDataAdapter;
import com.josephblough.sbt.callbacks.ProgramFinderRetrieverCallback;
import com.josephblough.sbt.criteria.ProgramFinderSearchCriteria;
import com.josephblough.sbt.data.SmallBusinessProgram;
import com.josephblough.sbt.tasks.PdfCheckerTask;
import com.josephblough.sbt.tasks.ProgramFinderRetrieverTask;

public class ProgramFinderSearchResultsActivity extends SearchResultsActivity implements ProgramFinderRetrieverCallback, OnItemClickListener {

    public final static String SEARCH_CRITERIA_EXTRA = "ProgramFinderSearchResultsActivity.SearchCriteria";
    
    private List<SmallBusinessProgram> data = null;
    private ProgramFinderSearchCriteria criteria = null;
    private ProgressDialog progress;
    
    private TextView titleLabel;
    private TextView descriptionLabel;
    private TextView urlLabel;
    private TextView categoryLabel;
    private TextView stateLabel;
    private TextView agencyLabel;
    
    private TableRow titleRow;
    private TableRow descriptionRow;
    private TableRow urlRow;
    private TableRow categoryRow;
    private TableRow stateRow;
    private TableRow agencyRow;
    
    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.program_finder_results);
	
	criteria = getIntent().getParcelableExtra(SEARCH_CRITERIA_EXTRA);
	
	if (data == null && criteria != null) {
	    progress = ProgressDialog.show(this, "Loading...", "Retrieving Small Business Programs", true, true);
	    new ProgramFinderRetrieverTask(this).execute(criteria);
	}
	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.program_finder_details_table);
	detailsControls = findViewById(R.id.program_finder_details_controls);
	dismissDetailsButton = (Button)findViewById(R.id.program_finder_details_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.program_finder_details_visit_link);

	titleLabel = (TextView)findViewById(R.id.program_finder_details_title_value);
	descriptionLabel = (TextView)findViewById(R.id.program_finder_details_description_value);
	urlLabel = (TextView)findViewById(R.id.program_finder_details_url_value);
	categoryLabel = (TextView)findViewById(R.id.program_finder_details_category_value);
	stateLabel = (TextView)findViewById(R.id.program_finder_details_state_value);
	agencyLabel = (TextView)findViewById(R.id.program_finder_details_agency_value);

	titleRow = (TableRow)findViewById(R.id.program_finder_details_title_row);
	descriptionRow = (TableRow)findViewById(R.id.program_finder_details_description_row);
	urlRow = (TableRow)findViewById(R.id.program_finder_details_url_row);
	categoryRow = (TableRow)findViewById(R.id.program_finder_details_category_row);
	stateRow = (TableRow)findViewById(R.id.program_finder_details_state_row);
	agencyRow = (TableRow)findViewById(R.id.program_finder_details_agency_row);
	
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

		SmallBusinessProgram program = ((SmallBusinessProgramDataAdapter)getListAdapter()).getItem(position);
		showDetails(program);

		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(program.title));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, program.formatForSharing());
		startActivity(Intent.createChooser(sharingIntent,"Share using"));
		return true;
	    }
	});
    }
    
    public void success(List<SmallBusinessProgram> results) {
	this.data = results;
	removeInvalidResults();
	
	Collections.sort(this.data, new Comparator<SmallBusinessProgram>() {

	    public int compare(SmallBusinessProgram program1, SmallBusinessProgram program2) {
		return program1.title.compareTo(program2.title);
	    }
	});

	SmallBusinessProgramDataAdapter adapter = new SmallBusinessProgramDataAdapter(this, this.data);
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

	final SmallBusinessProgram selectedItem = (SmallBusinessProgram)getListAdapter().getItem(position);
	showDetails(selectedItem);
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
    
    private void showDetails(final SmallBusinessProgram program) {
	//Toast.makeText(LicensesAndPermitsSearchResultsActivity.this, "Details", Toast.LENGTH_SHORT).show();
	if (!isEmpty(program.title)) {
	    titleLabel.setText(program.title);
	    titleRow.setVisibility(View.VISIBLE);
	}
	else {
	    titleRow.setVisibility(View.GONE);
	}
	
	// Description
	if (!isEmpty(program.description)) {
	    descriptionLabel.setText(Html.fromHtml(program.description));
	    // Reset the label scroll to the top
	    descriptionLabel.scrollTo(0, 0);
	    
	    descriptionRow.setVisibility(View.VISIBLE);
	}
	else
	    descriptionRow.setVisibility(View.GONE);
	
	// URL
	urlRow.setVisibility(View.GONE);
	if (!isEmpty(program.url)) {
	    urlLabel.setText(program.url);
	    //urlRow.setVisibility(View.VISIBLE);
	}
	else
	    urlRow.setVisibility(View.GONE);
	
	// Category
	if (!isEmpty(program.category)) {
	    categoryLabel.setText(program.category);
	    categoryRow.setVisibility(View.VISIBLE);
	}
	else
	    categoryRow.setVisibility(View.GONE);
	
	// State
	if (!isEmpty(program.state)) {
	    stateLabel.setText(program.state);
	    stateRow.setVisibility(View.VISIBLE);
	}
	else
	    stateRow.setVisibility(View.GONE);
	
	// Agency
	if (!isEmpty(program.agency)) {
	    agencyLabel.setText(program.agency);
	    agencyRow.setVisibility(View.VISIBLE);
	}
	else
	    agencyRow.setVisibility(View.GONE);
	

	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(program);
	    }
	});
    }
    
    private void visitData(final SmallBusinessProgram program) {
	// Display a warning message to the user if this is a PDF document
	new PdfCheckerTask(this).execute(program.url);
	
	// Launch the document
	final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(program.url));
	startActivity(intent);
    }
    
    private void removeInvalidResults() {
	Iterator<SmallBusinessProgram> it = this.data.iterator();
	while (it.hasNext()) {
	    SmallBusinessProgram program = it.next();
	    if (program.title == null || "".equals(program.title) ||
		    program.url == null || "".equals(program.url)) {
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
