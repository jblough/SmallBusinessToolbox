package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgramFinderSearchCriteria implements Parcelable {

    public static final int TYPE_BY_FEDERAL_INDEX = 0;
    public static final int TYPE_BY_PRIVATE_INDEX = 1;
    public static final int TYPE_BY_NATIONAL_INDEX = 2;
    public static final int TYPE_BY_STATE_INDEX = 3;
    public static final int TYPE_BY_INDUSTRY_INDEX = 4;
    public static final int TYPE_BY_TYPE_INDEX = 5;
    public static final int TYPE_BY_QUALIFICATION_INDEX = 6;
    
    public int type;
    public String criteria;
    
    
    public ProgramFinderSearchCriteria(int type, String criteria) {
	this.type = type;
	
	if (type == TYPE_BY_FEDERAL_INDEX)
	    this.criteria = "Federal";
	else if (type == TYPE_BY_PRIVATE_INDEX)
	    this.criteria = "Private";
	else if (type == TYPE_BY_NATIONAL_INDEX)
	    this.criteria = "National";
	else
	    this.criteria = criteria;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(type);
	dest.writeString(criteria);
    }

    public static final Parcelable.Creator<ProgramFinderSearchCriteria> CREATOR = new Parcelable.Creator<ProgramFinderSearchCriteria>() {
	public ProgramFinderSearchCriteria createFromParcel(Parcel in) {
	    return new ProgramFinderSearchCriteria(in);
	}
	public ProgramFinderSearchCriteria[] newArray(int size) {
            return new ProgramFinderSearchCriteria[size];
        }
    };
    
    private ProgramFinderSearchCriteria(Parcel in) {
	type = in.readInt();
	criteria = in.readString();
    }
}
