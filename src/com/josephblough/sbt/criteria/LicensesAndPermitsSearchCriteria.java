package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class LicensesAndPermitsSearchCriteria implements Parcelable {

    public static final int CATEGORY_INDEX = 0;
    public static final int STATE_INDEX = 1;
    public static final int BUSINESS_TYPE_INDEX = 2;
    
    public static final int NO_SUBFILTER_INDEX = 0;
    public static final int STATE_SUBFILTER_INDEX = 1;
    public static final int COUNTY_SUBFILTER_INDEX = 2;
    public static final int CITY_SUBFILTER_INDEX = 3;
    public static final int ZIP_CODE_SUBFILTER_INDEX = 4;
    
    public int searchBy;
    public String state;
    public String category;
    public String businessType;
    public int businessTypeSubfilter;
    public String businessTypeSubfilterLocality;
    
    public LicensesAndPermitsSearchCriteria(int searchBy, String state, String category, String businessType, int businessTypeSubfilter, String businessTypeSubfilterLocality) {
	this.searchBy = searchBy;
	this.state = (state == null) ? null : state.trim();
	this.category = (category == null) ? null : category.trim();
	this.businessType = (businessType == null) ? null : businessType.trim();
	this.businessTypeSubfilter = businessTypeSubfilter;
	this.businessTypeSubfilterLocality = (businessTypeSubfilterLocality == null) ? null : 
	    businessTypeSubfilterLocality.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(searchBy);
	dest.writeString(state);
	dest.writeString(category);
	dest.writeString(businessType);
	dest.writeInt(businessTypeSubfilter);
	dest.writeString(businessTypeSubfilterLocality);
    }

    public static final Parcelable.Creator<LicensesAndPermitsSearchCriteria> CREATOR = new Parcelable.Creator<LicensesAndPermitsSearchCriteria>() {
	public LicensesAndPermitsSearchCriteria createFromParcel(Parcel in) {
	    return new LicensesAndPermitsSearchCriteria(in);
	}
	public LicensesAndPermitsSearchCriteria[] newArray(int size) {
            return new LicensesAndPermitsSearchCriteria[size];
        }
    };
    
    private LicensesAndPermitsSearchCriteria(Parcel in) {
	searchBy = in.readInt();
	state = in.readString();
	category = in.readString();
	businessType = in.readString();
	businessTypeSubfilter = in.readInt();
	businessTypeSubfilterLocality = in.readString();
    }
}
