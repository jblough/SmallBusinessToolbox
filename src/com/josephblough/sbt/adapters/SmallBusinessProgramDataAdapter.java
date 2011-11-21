package com.josephblough.sbt.adapters;

import java.util.List;

import com.josephblough.sbt.data.SmallBusinessProgram;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SmallBusinessProgramDataAdapter extends ArrayAdapter<SmallBusinessProgram> {

    public SmallBusinessProgramDataAdapter(Context context, List<SmallBusinessProgram> items) {
	super(context, android.R.layout.simple_list_item_2, android.R.id.text1, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = super.getView(position, convertView, parent);
	
	((TextView)row.findViewById(android.R.id.text1)).setText(getItem(position).title);
	((TextView)row.findViewById(android.R.id.text2)).setText(getItem(position).url);
	
	return row;
    }
}
