package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class DistrictOfficeSearchCriteria implements Parcelable {

    public static final int FIND_NEAREST_OFFICE_INDEX = 0;
    public static final int FIND_BY_STATE_INDEX = 1;
    public static final int FIND_BY_ZIP_CODE_INDEX = 2;
    
    public int type;
    public String criteria;
    
    
    public DistrictOfficeSearchCriteria(int type, String criteria) {
	this.type = type;
	this.criteria = criteria;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(type);
	dest.writeString(criteria == null ? "" : criteria);
    }

    public static final Parcelable.Creator<DistrictOfficeSearchCriteria> CREATOR = new Parcelable.Creator<DistrictOfficeSearchCriteria>() {
	public DistrictOfficeSearchCriteria createFromParcel(Parcel in) {
	    return new DistrictOfficeSearchCriteria(in);
	}
	public DistrictOfficeSearchCriteria[] newArray(int size) {
            return new DistrictOfficeSearchCriteria[size];
        }
    };
    
    private DistrictOfficeSearchCriteria(Parcel in) {
	type = in.readInt();
	criteria = in.readString();
    }
}
