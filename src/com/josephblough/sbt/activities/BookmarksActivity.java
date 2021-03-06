package com.josephblough.sbt.activities;

import java.util.ArrayList;
import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.adapters.BookmarkDataAdapter;
import com.josephblough.sbt.adapters.SeparatedListAdapter;
import com.josephblough.sbt.data.Award;
import com.josephblough.sbt.data.Bookmarkable;
import com.josephblough.sbt.data.GenericPost;
import com.josephblough.sbt.data.GreenPost;
import com.josephblough.sbt.data.LicenseAndPermitData;
import com.josephblough.sbt.data.LoanAndGrantData;
import com.josephblough.sbt.data.LocalityWebData;
import com.josephblough.sbt.data.RecommendedSite;
import com.josephblough.sbt.data.SbaDistrictOffice;
import com.josephblough.sbt.data.SmallBusinessProgram;
import com.josephblough.sbt.data.Solicitation;
import com.josephblough.sbt.tasks.PdfCheckerTask;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BookmarksActivity extends ListActivity implements OnItemClickListener {

    private TableRow row[] = new TableRow[10];
    private TextView rowLabel[] = new TextView[10];
    private TextView rowValue[] = new TextView[10];

    private Button dismissDetailsButton;
    private Button visitUrlButton;
    private Button shareUrlButton;
    private View detailsView;
    private View detailsControls;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);
        
        ApplicationController app = (ApplicationController)getApplicationContext();
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);

        // Licenses and Permits
        if (app.bookmarks.licensePermitBookmarks.size() > 0) {
            List<Bookmarkable> licensePermitBookmarks = new ArrayList<Bookmarkable>();
            licensePermitBookmarks.addAll(app.bookmarks.licensePermitBookmarks);
            adapter.addSection("Licenses and Permits", new BookmarkDataAdapter(this, licensePermitBookmarks));
        }
        // Loans and Grants
        if (app.bookmarks.loanGrantBookmarks.size() > 0) {
            List<Bookmarkable> loanGrantBookmarks = new ArrayList<Bookmarkable>();
            loanGrantBookmarks.addAll(app.bookmarks.loanGrantBookmarks);
            adapter.addSection("Loans and Grants", new BookmarkDataAdapter(this, loanGrantBookmarks));
        }
        // Recommended Sites
        if (app.bookmarks.recommendedSitesBookmarks.size() > 0) {
            List<Bookmarkable> sitesBookmarks = new ArrayList<Bookmarkable>();
            sitesBookmarks.addAll(app.bookmarks.recommendedSitesBookmarks);
            adapter.addSection("Recommended Sites", new BookmarkDataAdapter(this, sitesBookmarks));
        }
        // City/County Web Data
        if (app.bookmarks.localityWebDataBookmarks.size() > 0) {
            List<Bookmarkable> webDataBookmarks = new ArrayList<Bookmarkable>();
            webDataBookmarks.addAll(app.bookmarks.localityWebDataBookmarks);
            adapter.addSection("City/County Web Data", new BookmarkDataAdapter(this, webDataBookmarks));
        }
        // Small Business Programs
        if (app.bookmarks.smallBusinessProgramBookmarks.size() > 0) {
            List<Bookmarkable> programsBookmarks = new ArrayList<Bookmarkable>();
            programsBookmarks.addAll(app.bookmarks.smallBusinessProgramBookmarks);
            adapter.addSection("Small Business Programs", new BookmarkDataAdapter(this, programsBookmarks));
        }
        // Solicitations
        if (app.bookmarks.solicitationBookmarks.size() > 0) {
            List<Bookmarkable> solicitationBookmarks = new ArrayList<Bookmarkable>();
            solicitationBookmarks.addAll(app.bookmarks.solicitationBookmarks);
            adapter.addSection("Solicitations", new BookmarkDataAdapter(this, solicitationBookmarks));
        }
        // Awards
        if (app.bookmarks.awardBookmarks.size() > 0) {
            List<Bookmarkable> awardBookmarks = new ArrayList<Bookmarkable>();
            awardBookmarks.addAll(app.bookmarks.awardBookmarks);
            adapter.addSection("Awards", new BookmarkDataAdapter(this, awardBookmarks));
        }
        // Green Posts
        if (app.bookmarks.greenPostBookmarks.size() > 0) {
            List<Bookmarkable> greenPostBookmarks = new ArrayList<Bookmarkable>();
            greenPostBookmarks.addAll(app.bookmarks.greenPostBookmarks);
            adapter.addSection("Green Search Results", new BookmarkDataAdapter(this, greenPostBookmarks));
        }
        // Generic Posts
        if (app.bookmarks.genericPostBookmarks.size() > 0) {
            List<Bookmarkable> genericPostBookmarks = new ArrayList<Bookmarkable>();
            genericPostBookmarks.addAll(app.bookmarks.genericPostBookmarks);
            adapter.addSection("Generic Search Results", new BookmarkDataAdapter(this, genericPostBookmarks));
        }
        // Offices
        if (app.bookmarks.officeBookmarks.size() > 0) {
            List<Bookmarkable> officeBookmarks = new ArrayList<Bookmarkable>();
            officeBookmarks.addAll(app.bookmarks.officeBookmarks);
            adapter.addSection("SBA District Offices", new BookmarkDataAdapter(this, officeBookmarks));
        }
        
        setListAdapter(adapter);

	getListView().setOnItemClickListener(this);
	
	detailsView = findViewById(R.id.bookmarks_details_table);
	detailsControls = findViewById(R.id.detail_controls);
	dismissDetailsButton = (Button)findViewById(R.id.detail_controls_dismiss_details);
	visitUrlButton = (Button)findViewById(R.id.detail_controls_visit_link);
	shareUrlButton = (Button)findViewById(R.id.detail_controls_share_link);
        
        row[0] = (TableRow)findViewById(R.id.bookmarks_details_row1);
        row[1] = (TableRow)findViewById(R.id.bookmarks_details_row2);
        row[2] = (TableRow)findViewById(R.id.bookmarks_details_row3);
        row[3] = (TableRow)findViewById(R.id.bookmarks_details_row4);
        row[4] = (TableRow)findViewById(R.id.bookmarks_details_row5);
        row[5] = (TableRow)findViewById(R.id.bookmarks_details_row6);
        row[6] = (TableRow)findViewById(R.id.bookmarks_details_row7);
        row[7] = (TableRow)findViewById(R.id.bookmarks_details_row8);
        row[8] = (TableRow)findViewById(R.id.bookmarks_details_row9);
        row[9] = (TableRow)findViewById(R.id.bookmarks_details_row10);

        rowLabel[0] = (TextView)findViewById(R.id.bookmarks_details_label_row1);
        rowLabel[1] = (TextView)findViewById(R.id.bookmarks_details_label_row2);
        rowLabel[2] = (TextView)findViewById(R.id.bookmarks_details_label_row3);
        rowLabel[3] = (TextView)findViewById(R.id.bookmarks_details_label_row4);
        rowLabel[4] = (TextView)findViewById(R.id.bookmarks_details_label_row5);
        rowLabel[5] = (TextView)findViewById(R.id.bookmarks_details_label_row6);
        rowLabel[6] = (TextView)findViewById(R.id.bookmarks_details_label_row7);
        rowLabel[7] = (TextView)findViewById(R.id.bookmarks_details_label_row8);
        rowLabel[8] = (TextView)findViewById(R.id.bookmarks_details_label_row9);
        rowLabel[9] = (TextView)findViewById(R.id.bookmarks_details_label_row10);
        
        rowValue[0] = (TextView)findViewById(R.id.bookmarks_details_value_row1);
        rowValue[1] = (TextView)findViewById(R.id.bookmarks_details_value_row2);
        rowValue[2] = (TextView)findViewById(R.id.bookmarks_details_value_row3);
        rowValue[3] = (TextView)findViewById(R.id.bookmarks_details_value_row4);
        rowValue[4] = (TextView)findViewById(R.id.bookmarks_details_value_row5);
        rowValue[5] = (TextView)findViewById(R.id.bookmarks_details_value_row6);
        rowValue[6] = (TextView)findViewById(R.id.bookmarks_details_value_row7);
        rowValue[7] = (TextView)findViewById(R.id.bookmarks_details_value_row8);
        rowValue[8] = (TextView)findViewById(R.id.bookmarks_details_value_row9);
        rowValue[9] = (TextView)findViewById(R.id.bookmarks_details_value_row10);
        
        rowValue[0].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[1].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[2].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[3].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[4].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[5].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[6].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[7].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[8].setMovementMethod(ScrollingMovementMethod.getInstance());
        rowValue[9].setMovementMethod(ScrollingMovementMethod.getInstance());
        
	dismissDetailsButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		detailsView.setVisibility(View.GONE);
		detailsControls.setVisibility(View.GONE);
	    }
	});
	
	getListView().setFastScrollEnabled(true);
		
	if (adapter.sections.size() == 0)
	    Toast.makeText(this, "No bookmarks", Toast.LENGTH_SHORT).show();
    
	
	// Set a long click handler
	getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	    public boolean onItemLongClick(AdapterView<?> parent, View view,
		    int position, long id) {
		Bookmarkable bookmark = (Bookmarkable)getListAdapter().getItem(position);
		showDetails(bookmark);
		share(bookmark);
		
		return true;
	    }
	});
    }

    private void share(final Bookmarkable bookmark) {
	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	sharingIntent.setType("text/plain");
	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(bookmark.getName()).toString());
	sharingIntent.putExtra(Intent.EXTRA_TEXT, bookmark.formatForSharing());
	startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	Bookmarkable bookmark = (Bookmarkable)getListAdapter().getItem(position);
	
	showDetails(bookmark);
    }

    private void showDetails(final Bookmarkable bookmark) {
	for (int i=0; i<10; i++) {
	    if (i<bookmark.getDetailCount() && bookmark.isVisible(i)) {
		rowLabel[i].setText(bookmark.getDetailLabel(i));
		rowValue[i].setText(Html.fromHtml(bookmark.getDetailValue(i)));
		// Reset the label scroll to the top
		rowValue[i].scrollTo(0, 0);
		
		row[i].setVisibility(View.VISIBLE);
	    }
	    else {
		row[i].setVisibility(View.GONE);
	    }
	}
	
	if (SbaDistrictOffice.TYPE.equals(bookmark.getType())) {
	    visitUrlButton.setText(R.string.map_address);
	}
	else {
	    visitUrlButton.setText(R.string.visit_link);
	}
	
	visitUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		visitData(bookmark);
	    }
	});
	
	shareUrlButton.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		share(bookmark);
	    }
	});
	
	detailsView.setVisibility(View.VISIBLE);
	detailsControls.setVisibility(View.VISIBLE);
    }
    
    private void visitData(final Bookmarkable bookmark) {
	if (SbaDistrictOffice.TYPE.equals(bookmark.getType())) {
	    SbaDistrictOffice office = (SbaDistrictOffice)bookmark;
	    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
		    Uri.parse("geo:0,0?q=" + office.latitude + "," + office.longitude + 
			    " (" + office.title + " " + office.street + ")"));
	    startActivity(intent);
	}
	else {
	    // Display a warning message to the user if this is a PDF document
	    new PdfCheckerTask(this).execute(bookmark.getUrl());

	    // Launch the document
	    final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(bookmark.getUrl()));
	    startActivity(intent);
	}
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

    public void removeBookmark(final Bookmarkable bookmark) {
	// Close the details view
	detailsView.setVisibility(View.GONE);
	detailsControls.setVisibility(View.GONE);

	// Remove the bookmark
	ApplicationController app = (ApplicationController)getApplicationContext();
	bookmark.removeFromBookmarks(app);
	app.saveBookmarks();

	if (app.shouldShowTooltip(R.string.unbookmarked))
	    Toast.makeText(this, R.string.unbookmarked, Toast.LENGTH_SHORT).show();
	
	// Refresh the adapter
	updateAdapter();
    }
    
    private void updateAdapter() {
	ApplicationController app = (ApplicationController)getApplicationContext();
	//SeparatedListAdapter adapter = (SeparatedListAdapter)getListAdapter();
        SeparatedListAdapter adapter = new SeparatedListAdapter(this);
	
        // Licenses and Permits
        if (app.bookmarks.licensePermitBookmarks.size() > 0) {
            List<Bookmarkable> licensePermitBookmarks = new ArrayList<Bookmarkable>();
            licensePermitBookmarks.addAll(app.bookmarks.licensePermitBookmarks);
            adapter.addSection("Licenses and Permits", new BookmarkDataAdapter(this, licensePermitBookmarks));
        }
        else {
            adapter.sections.remove("Licenses and Permits");
        }
        // Loans and Grants
        if (app.bookmarks.loanGrantBookmarks.size() > 0) {
            List<Bookmarkable> loanGrantBookmarks = new ArrayList<Bookmarkable>();
            loanGrantBookmarks.addAll(app.bookmarks.loanGrantBookmarks);
            adapter.addSection("Loans and Grants", new BookmarkDataAdapter(this, loanGrantBookmarks));
        }
        else {
            adapter.sections.remove("Loans and Grants");
        }
        // Recommended Sites
        if (app.bookmarks.recommendedSitesBookmarks.size() > 0) {
            List<Bookmarkable> sitesBookmarks = new ArrayList<Bookmarkable>();
            sitesBookmarks.addAll(app.bookmarks.recommendedSitesBookmarks);
            adapter.addSection("Recommended Sites", new BookmarkDataAdapter(this, sitesBookmarks));
        }
        else {
            adapter.sections.remove("Recommended Sites");
        }
        // City/County Web Data
        if (app.bookmarks.localityWebDataBookmarks.size() > 0) {
            List<Bookmarkable> webDataBookmarks = new ArrayList<Bookmarkable>();
            webDataBookmarks.addAll(app.bookmarks.localityWebDataBookmarks);
            adapter.addSection("City/County Web Data", new BookmarkDataAdapter(this, webDataBookmarks));
        }
        else {
            adapter.sections.remove("City/County Web Data");
        }
        // Small Business Programs
        if (app.bookmarks.smallBusinessProgramBookmarks.size() > 0) {
            List<Bookmarkable> programsBookmarks = new ArrayList<Bookmarkable>();
            programsBookmarks.addAll(app.bookmarks.smallBusinessProgramBookmarks);
            adapter.addSection("Small Business Programs", new BookmarkDataAdapter(this, programsBookmarks));
        }
        else {
            adapter.sections.remove("Small Business Programs");
        }
        // Solicitations
        if (app.bookmarks.solicitationBookmarks.size() > 0) {
            List<Bookmarkable> solicitationBookmarks = new ArrayList<Bookmarkable>();
            solicitationBookmarks.addAll(app.bookmarks.solicitationBookmarks);
            adapter.addSection("Solicitations", new BookmarkDataAdapter(this, solicitationBookmarks));
        }
        else {
            adapter.sections.remove("Solicitations");
        }
        // Awards
        if (app.bookmarks.awardBookmarks.size() > 0) {
            List<Bookmarkable> awardBookmarks = new ArrayList<Bookmarkable>();
            awardBookmarks.addAll(app.bookmarks.awardBookmarks);
            adapter.addSection("Awards", new BookmarkDataAdapter(this, awardBookmarks));
        }
        else {
            adapter.sections.remove("Awards");
        }
        // Green Posts
        if (app.bookmarks.greenPostBookmarks.size() > 0) {
            List<Bookmarkable> greenPostBookmarks = new ArrayList<Bookmarkable>();
            greenPostBookmarks.addAll(app.bookmarks.greenPostBookmarks);
            adapter.addSection("Green Search Results", new BookmarkDataAdapter(this, greenPostBookmarks));
        }
        else {
            adapter.sections.remove("Green Search Results");
        }
        // Generic Posts
        if (app.bookmarks.genericPostBookmarks.size() > 0) {
            List<Bookmarkable> genericPostBookmarks = new ArrayList<Bookmarkable>();
            genericPostBookmarks.addAll(app.bookmarks.genericPostBookmarks);
            adapter.addSection("Generic Search Results", new BookmarkDataAdapter(this, genericPostBookmarks));
        }
        else {
            adapter.sections.remove("Generic Search Results");
        }
        // Offices
        if (app.bookmarks.officeBookmarks.size() > 0) {
            List<Bookmarkable> officeBookmarks = new ArrayList<Bookmarkable>();
            officeBookmarks.addAll(app.bookmarks.officeBookmarks);
            adapter.addSection("SBA District Offices", new BookmarkDataAdapter(this, officeBookmarks));
        }
        else {
            adapter.sections.remove("SBA District Offices");
        }
        
        if (adapter.sections.size() == 0) {
            finish();
        }
        else {
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.bookmarks_menu, menu);
	
	return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.email_bookmarks_menu_item:
	    emailBookmarks();
	    break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    private void emailBookmarks() {
	Intent sharingIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bookmarks");
	sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(generateBookmarkMessage()));
	startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }
    
    private String generateBookmarkMessage() {
	final StringBuffer msg = new StringBuffer("<html><body>");

        ApplicationController app = (ApplicationController)getApplicationContext();
        // Licenses and Permits
        if (app.bookmarks.licensePermitBookmarks.size() > 0) {
            msg.append("<h1>Licenses and Permits</h1><br/>");
            for (LicenseAndPermitData data : app.bookmarks.licensePermitBookmarks) {
        	msg.append(generateBookmarkHtml(data));
            }
            msg.append("<br/>");
        }
        
        // Loans and Grants
        if (app.bookmarks.loanGrantBookmarks.size() > 0) {
            msg.append("<h1>Loans and Grants</h1><br/>");
            for (LoanAndGrantData data : app.bookmarks.loanGrantBookmarks) {
        	msg.append(generateBookmarkHtml(data));
            }
            msg.append("<br/>");
        }

        // Recommended Sites
        if (app.bookmarks.recommendedSitesBookmarks.size() > 0) {
            msg.append("<h1>Recommended Sites</h1><br/>");
            for (RecommendedSite site : app.bookmarks.recommendedSitesBookmarks) {
        	msg.append(generateBookmarkHtml(site));
            }
            msg.append("<br/>");
        }
        
        // City/County Web Data
        if (app.bookmarks.localityWebDataBookmarks.size() > 0) {
            msg.append("<h1>City/County Web Data</h1><br/>");
            for (LocalityWebData data : app.bookmarks.localityWebDataBookmarks) {
        	msg.append(generateBookmarkHtml(data));
            }
            msg.append("<br/>");
        }
        
        // Small Business Programs
        if (app.bookmarks.smallBusinessProgramBookmarks.size() > 0) {
            msg.append("<h1>Small Business Programs</h1><br/>");
            for (SmallBusinessProgram program : app.bookmarks.smallBusinessProgramBookmarks) {
        	msg.append(generateBookmarkHtml(program));
            }
            msg.append("<br/>");
        }
        
        // Solicitations
        if (app.bookmarks.solicitationBookmarks.size() > 0) {
            msg.append("<h1>Solicitations</h1><br/>");
            for (Solicitation solicitation : app.bookmarks.solicitationBookmarks) {
        	msg.append(generateBookmarkHtml(solicitation));
            }
            msg.append("<br/>");
        }
        
        // Awards
        if (app.bookmarks.awardBookmarks.size() > 0) {
            msg.append("<h1>Awards</h1><br/>");
            for (Award award : app.bookmarks.awardBookmarks) {
        	msg.append(generateBookmarkHtml(award));
            }
            msg.append("<br/>");
        }
        
        // Green Posts
        if (app.bookmarks.greenPostBookmarks.size() > 0) {
            msg.append("<h1>Green Search Results</h1><br/>");
            for (GreenPost post : app.bookmarks.greenPostBookmarks) {
        	msg.append(generateBookmarkHtml(post));
            }
            msg.append("<br/>");
        }
        
        // Generic Posts
        if (app.bookmarks.genericPostBookmarks.size() > 0) {
            msg.append("<h1>Generic Search Results</h1><br/>");
            for (GenericPost post : app.bookmarks.genericPostBookmarks) {
        	msg.append(generateBookmarkHtml(post));
            }
            msg.append("<br/>");
        }
        
        // Offices
        if (app.bookmarks.officeBookmarks.size() > 0) {
            msg.append("<h1>SBA District Offices</h1><br/>");
            for (SbaDistrictOffice office : app.bookmarks.officeBookmarks) {
        	msg.append("<h6>" + office.getName() + "</h6>");
        	for (int i=0; i<office.getDetailCount(); i++) {
        	    if (office.isVisible(i)) {
        		msg.append("<b>" + office.getDetailLabel(i) + "</b> " + office.getDetailValue(i) + "<br/>");
        	    }
        	}
        	//msg.append("<b>Map address:</b> " + office.getUrl() + "<br/>");
        	msg.append("<br/>");
            }
        }
	
        msg.append("</body></html>");
        
	return msg.toString();
    }
    
    public static String generateBookmarkHtml(final Bookmarkable bookmark) {
	final StringBuffer msg = new StringBuffer();
	
	msg.append("<h6>" + bookmark.getName() + "</h6>");
	for (int i=0; i<bookmark.getDetailCount(); i++) {
	    if (bookmark.isVisible(i) && !"URL:".equals(bookmark.getDetailLabel(i))) {
		msg.append("<b>" + bookmark.getDetailLabel(i) + "</b> " + bookmark.getDetailValue(i) + "<br/>");
	    }
	}
	msg.append("<b>URL:</b> " + bookmark.getUrl() + "<br/>");
	msg.append("<br/>");
	
	return msg.toString();
    }
}
