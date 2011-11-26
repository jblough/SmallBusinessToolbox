package com.josephblough.sbt.adapters;

import java.util.List;

import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.BookmarksActivity;
import com.josephblough.sbt.data.Bookmarkable;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarkDataAdapter extends ArrayAdapter<Bookmarkable> {

    private LayoutInflater inflater = null;
    BookmarksActivity context;
    
    public BookmarkDataAdapter(Context context, List<Bookmarkable> items) {
	super(context, R.layout.search_results_row, R.id.search_result_row_title, items);
	this.context = (BookmarksActivity)context;
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
	
	final Bookmarkable bookmark = getItem(position);
	holder.titleText.setText(Html.fromHtml(bookmark.getName()));
	holder.subTitleText.setText(bookmark.getUrl());
	
	holder.bookmarkedImage.setImageResource(R.drawable.bookmarked);
	
	holder.bookmarkedImage.setOnClickListener(new View.OnClickListener() {
	    
	    public void onClick(View v) {
		context.removeBookmark(bookmark);
	    }
	});

	return row;
    }
}
