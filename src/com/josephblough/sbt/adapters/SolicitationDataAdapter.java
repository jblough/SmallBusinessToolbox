package com.josephblough.sbt.adapters;

import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.data.Solicitation;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SolicitationDataAdapter extends ArrayAdapter<Solicitation> {

    private LayoutInflater inflater = null;
    Context context;
    
    public SolicitationDataAdapter(Context context, List<Solicitation> items) {
	super(context, R.layout.search_results_row, R.id.search_result_row_title, items);
	this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public TextView titleText;
        public TextView subTitleText;
        public ImageView bookmarkedImage;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = convertView;
        ViewHolder holder;

	if (row == null) {
	    row = inflater.inflate(R.layout.search_results_row, null);
            holder = new ViewHolder();
            holder.titleText = (TextView)row.findViewById(R.id.search_result_row_title);
            holder.subTitleText = (TextView)row.findViewById(R.id.search_result_row_subtitle);
            holder.bookmarkedImage = (ImageView)row.findViewById(R.id.search_result_row_bookmark);
            row.setTag(holder);
	}
        else
            holder = (ViewHolder)row.getTag();
	
	final Solicitation solicitation = getItem(position);
	holder.titleText.setText(Html.fromHtml(solicitation.title));
	holder.subTitleText.setText(solicitation.link);
	
	final ApplicationController app = (ApplicationController)context.getApplicationContext();
	holder.bookmarkedImage.setImageResource((app.bookmarks.isBookmarked(solicitation)) ? 
		R.drawable.bookmarked : R.drawable.unbookmarked);
	
	holder.bookmarkedImage.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		if (app.bookmarks.isBookmarked(solicitation)) {
		    app.bookmarks.removeBookmark(solicitation);
		    ((ImageView)v).setImageResource(R.drawable.unbookmarked);
		    if (app.shouldShowTooltip(R.string.unbookmarked))
			Toast.makeText(context, R.string.unbookmarked, Toast.LENGTH_SHORT).show();
		}
		else {
		    app.bookmarks.addBookmark(solicitation);
		    ((ImageView)v).setImageResource(R.drawable.bookmarked);
		    if (app.shouldShowTooltip(R.string.bookmarked))
			Toast.makeText(context, R.string.bookmarked, Toast.LENGTH_SHORT).show();
		}
		app.saveBookmarks();
	    }
	});

	return row;
    }
}
