package com.josephblough.sbt.adapters;

import java.util.List;

import com.josephblough.sbt.data.SbaDistrictOffice;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DistrictOfficeDataAdapter extends ArrayAdapter<SbaDistrictOffice> {

    public DistrictOfficeDataAdapter(Context context, List<SbaDistrictOffice> items) {
	super(context, android.R.layout.simple_list_item_2, android.R.id.text1, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = super.getView(position, convertView, parent);
	
	((TextView)row.findViewById(android.R.id.text1)).setText(getItem(position).title);
	((TextView)row.findViewById(android.R.id.text2)).setText(formatAddress(getItem(position)));
	
	return row;
    }
    
    private String formatAddress(final SbaDistrictOffice office) {
	final StringBuffer address = new StringBuffer();
	if (!isEmpty(office.street)) {
	    address.append(office.street);
	}
	if (!isEmpty(office.street2)) {
	    if (address.length() > 0)
		address.append(" ");
	    address.append(office.street2);
	}
	if (!isEmpty(office.city)) {
	    address.append(" " + office.city);
	}
	if (!isEmpty(office.province)) {
	    address.append(", " + office.province);
	}
	if (!isEmpty(office.postalCode)) {
	    address.append(" " + office.postalCode);
	}
	return address.toString();
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }
}
