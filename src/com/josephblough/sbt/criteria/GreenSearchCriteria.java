package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class GreenSearchCriteria implements Parcelable {

    public boolean downloadAll;
    public String searchTerm;
    public String agency;
    public String type;
    
    
    public GreenSearchCriteria(boolean downloadAll, String searchTerm, String agency, String type) {
	this.downloadAll = downloadAll;
	this.searchTerm = (searchTerm == null) ? null : searchTerm.trim();
	this.agency = (agency == null) ? null : agency.trim();
	this.type = (type == null) ? null : type.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(downloadAll ? 1 : 0);
	dest.writeString(searchTerm == null ? "" : searchTerm);
	dest.writeString(agency == null ? "" : agency);
	dest.writeString(type == null ? "" : type);
    }

    public static final Parcelable.Creator<GreenSearchCriteria> CREATOR = new Parcelable.Creator<GreenSearchCriteria>() {
	public GreenSearchCriteria createFromParcel(Parcel in) {
	    return new GreenSearchCriteria(in);
	}
	public GreenSearchCriteria[] newArray(int size) {
            return new GreenSearchCriteria[size];
        }
    };
    
    private GreenSearchCriteria(Parcel in) {
	downloadAll = in.readInt() == 1;
	searchTerm = in.readString();
	agency = in.readString();
	type = in.readString();
    }
}
