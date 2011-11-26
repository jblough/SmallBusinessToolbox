package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class RecommendedSitesSearchCriteria implements Parcelable {

    public static final int ALL_SITES_INDEX = 0;
    public static final int KEYWORD_SEARCH_INDEX = 1;
    public static final int CATEGORY_SEARCH_INDEX = 2;
    public static final int MASTER_TERM_SEARCH_INDEX = 3;
    public static final int DOMAIN_FILTER_INDEX = 4;
    
    public int searchBy;
    public String searchTerm;
    
    
    public RecommendedSitesSearchCriteria(int searchBy, String searhTerm) {
	this.searchBy = searchBy;
	this.searchTerm = (searhTerm == null) ? null : searhTerm.trim();
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(searchBy);
	dest.writeString(searchTerm);
    }

    public static final Parcelable.Creator<RecommendedSitesSearchCriteria> CREATOR = new Parcelable.Creator<RecommendedSitesSearchCriteria>() {
	public RecommendedSitesSearchCriteria createFromParcel(Parcel in) {
	    return new RecommendedSitesSearchCriteria(in);
	}
	public RecommendedSitesSearchCriteria[] newArray(int size) {
            return new RecommendedSitesSearchCriteria[size];
        }
    };
    
    private RecommendedSitesSearchCriteria(Parcel in) {
	searchBy = in.readInt();
	searchTerm = in.readString();
    }
}
