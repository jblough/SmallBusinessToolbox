package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class GenericSearchCriteria implements Parcelable {

    public boolean downloadAll;
    public boolean onlyNew;
    public String searchTerm;
    public String agency;
    public String type;
    
    
    public GenericSearchCriteria(boolean downloadAll, boolean onlyNew, String searchTerm, String agency, String type) {
	this.downloadAll = downloadAll;
	this.onlyNew = onlyNew;
	this.searchTerm = searchTerm;
	this.agency = agency;
	this.type = type;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(downloadAll ? 1 : 0);
	dest.writeInt(onlyNew ? 1 : 0);
	dest.writeString(searchTerm == null ? "" : searchTerm);
	dest.writeString(agency == null ? "" : agency);
	dest.writeString(type == null ? "" : type);
    }

    public static final Parcelable.Creator<GenericSearchCriteria> CREATOR = new Parcelable.Creator<GenericSearchCriteria>() {
	public GenericSearchCriteria createFromParcel(Parcel in) {
	    return new GenericSearchCriteria(in);
	}
	public GenericSearchCriteria[] newArray(int size) {
            return new GenericSearchCriteria[size];
        }
    };
    
    private GenericSearchCriteria(Parcel in) {
	downloadAll = in.readInt() == 1;
	onlyNew = in.readInt() == 1;
	searchTerm = in.readString();
	agency = in.readString();
	type = in.readString();
    }
}
