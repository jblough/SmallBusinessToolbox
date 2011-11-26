package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalityWebDataSearchCriteria implements Parcelable {

    public static final int TYPE_ALL_URLS_INDEX = 0;
    public static final int TYPE_PRIMARY_URLS_INDEX = 1;
    public static final int TYPE_ALL_DATA_INDEX = 2;
    
    
    public static final int SCOPE_ALL_CITIES_AND_COUNTIES_INDEX = 0;
    public static final int SCOPE_ALL_CITIES_INDEX = 1;
    public static final int SCOPE_ALL_COUNTIES_INDEX = 2;
    public static final int SCOPE_SPECIFIC_CITY_INDEX = 3;
    public static final int SCOPE_SPECIFIC_COUNTY_DATA_INDEX = 4;
    
    public int type;
    public int scope;
    public String state;
    public String locality;
    
    
    public LocalityWebDataSearchCriteria(int type, int scope, String state, String locality) {
	this.type = type;
	this.scope = scope;
	this.state = (state == null) ? null : state.trim();
	this.locality = (locality == null) ? null : locality.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(type);
	dest.writeInt(scope);
	dest.writeString(state);
	dest.writeString(locality);
    }

    public static final Parcelable.Creator<LocalityWebDataSearchCriteria> CREATOR = new Parcelable.Creator<LocalityWebDataSearchCriteria>() {
	public LocalityWebDataSearchCriteria createFromParcel(Parcel in) {
	    return new LocalityWebDataSearchCriteria(in);
	}
	public LocalityWebDataSearchCriteria[] newArray(int size) {
            return new LocalityWebDataSearchCriteria[size];
        }
    };
    
    private LocalityWebDataSearchCriteria(Parcel in) {
	type = in.readInt();
	scope = in.readInt();
	state = in.readString();
	locality = in.readString();
    }
}
