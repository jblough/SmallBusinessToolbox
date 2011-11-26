package com.josephblough.sbt.adapters;

import java.util.List;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.R;
import com.josephblough.sbt.data.Award;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AwardDataAdapter extends ArrayAdapter<Award> {

    private LayoutInflater inflater = null;
    Context context;
    
    public AwardDataAdapter(Context context, List<Award> items) {
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
	
	final Award award = getItem(position);
	holder.titleText.setText(Html.fromHtml(award.title));
	holder.subTitleText.setText(award.link);
	
	final ApplicationController app = (ApplicationController)context.getApplicationContext();
	holder.bookmarkedImage.setImageResource((app.bookmarks.isBookmarked(award)) ? 
		R.drawable.bookmarked : R.drawable.unbookmarked);
	
	holder.bookmarkedImage.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		if (app.bookmarks.isBookmarked(award)) {
		    app.bookmarks.removeBookmark(award);
		    ((ImageView)v).setImageResource(R.drawable.unbookmarked);
		    Toast.makeText(context, R.string.unbookmarked, Toast.LENGTH_SHORT).show();
		}
		else {
		    app.bookmarks.addBookmark(award);
		    ((ImageView)v).setImageResource(R.drawable.bookmarked);
		    Toast.makeText(context, R.string.bookmarked, Toast.LENGTH_SHORT).show();
		}
		app.saveBookmarks();
	    }
	});

	return row;
    }
}
