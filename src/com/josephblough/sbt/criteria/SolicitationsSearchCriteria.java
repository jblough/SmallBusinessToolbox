package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class SolicitationsSearchCriteria implements Parcelable {

    public static final int SHOW_ALL_INDEX = 0;
    public static final int SHOW_OPEN_INDEX = 1;
    public static final int SHOW_CLOSED_INDEX = 2;
    
    public String keyword;
    public String agency;
    public int filter;
    
    
    public SolicitationsSearchCriteria(String keyword, String agency, int filter) {
	this.keyword = (keyword == null) ? null : keyword.trim();
	this.agency = (agency == null) ? null : agency.trim();
	this.filter = filter;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(keyword == null ? "" : keyword);
	dest.writeString(agency == null ? "" : agency);
	dest.writeInt(filter);
    }

    public static final Parcelable.Creator<SolicitationsSearchCriteria> CREATOR = new Parcelable.Creator<SolicitationsSearchCriteria>() {
	public SolicitationsSearchCriteria createFromParcel(Parcel in) {
	    return new SolicitationsSearchCriteria(in);
	}
	public SolicitationsSearchCriteria[] newArray(int size) {
            return new SolicitationsSearchCriteria[size];
        }
    };
    
    private SolicitationsSearchCriteria(Parcel in) {
	keyword = in.readString();
	agency = in.readString();
	filter = in.readInt();
    }
}
