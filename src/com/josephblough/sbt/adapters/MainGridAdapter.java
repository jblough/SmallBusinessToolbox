package com.josephblough.sbt.adapters;

import com.josephblough.sbt.R;
import com.josephblough.sbt.activities.MainMenuGridActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGridAdapter extends ArrayAdapter<String> {

    private MainMenuGridActivity context;
    
    public MainGridAdapter(MainMenuGridActivity context, String[] items) {
	super(context, R.layout.main_grid_cell, R.id.grid_cell_title, items);
	this.context = context;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = super.getView(position, convertView, parent);
	
	ImageView image = (ImageView)row.findViewById(R.id.grid_cell_image);
	image.setImageResource(context.getMenuImage(position));
	((TextView)row.findViewById(R.id.grid_cell_title)).setText(getItem(position));
	
        return row;
    }
}
