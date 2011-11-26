package com.josephblough.sbt.criteria;

import android.os.Parcel;
import android.os.Parcelable;

public class AwardsSearchCriteria implements Parcelable {

    public boolean downloadAll;
    public String searchTerm;
    public String agency;
    public String company;
    public String institution;
    public int year;
    
    
    public AwardsSearchCriteria(boolean downloadAll, String searchTerm, String agency, String company, String institution, int year) {
	this.downloadAll = downloadAll;
	this.searchTerm = (searchTerm == null) ? null : searchTerm.trim();
	this.agency = (agency == null) ? null : agency.trim();
	this.company = (company == null) ? null : company.trim();
	this.institution = (institution == null) ? null : institution.trim();
	this.year = year;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(downloadAll ? 1 : 0);
	dest.writeString(searchTerm == null ? "" : searchTerm);
	dest.writeString(agency == null ? "" : agency);
	dest.writeString(company == null ? "" : company);
	dest.writeString(institution == null ? "" : institution);
	dest.writeInt(year);
    }

    public static final Parcelable.Creator<AwardsSearchCriteria> CREATOR = new Parcelable.Creator<AwardsSearchCriteria>() {
	public AwardsSearchCriteria createFromParcel(Parcel in) {
	    return new AwardsSearchCriteria(in);
	}
	public AwardsSearchCriteria[] newArray(int size) {
            return new AwardsSearchCriteria[size];
        }
    };
    
    private AwardsSearchCriteria(Parcel in) {
	downloadAll = in.readInt() == 1;
	searchTerm = in.readString();
	agency = in.readString();
	company = in.readString();
	institution = in.readString();
	year = in.readInt();
    }
}
