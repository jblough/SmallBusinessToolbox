package com.josephblough.sbt.criteria;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class LoansAndGrantsSearchCriteria implements Parcelable {

    public boolean includeFederal;
    public boolean includeState;
    public String state;
    public boolean filterByIndustry;
    public String industry;
    public boolean filterBySpecialty;
    public List<String> specialties;
    
    
    public LoansAndGrantsSearchCriteria(boolean includeFederal, boolean includeState, String state, 
	    boolean filterByIndustry, String industry, boolean filteryBySpecialty, List<String> specialties) {
	this.includeFederal = includeFederal;
	this.includeState = includeState;
	this.state = (state == null) ? null : state.trim();
	this.filterByIndustry = filterByIndustry;
	this.industry = (industry == null) ? null : industry.trim();
	this.filterBySpecialty = filteryBySpecialty;
	this.specialties = specialties;
    }
    
    public int describeContents() {
	return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
	dest.writeInt(includeFederal ? 1 : 0);
	dest.writeInt(includeState ? 1 : 0);
	dest.writeString(state);
	dest.writeInt(filterByIndustry ? 1 : 0);
	dest.writeString(industry);
	dest.writeInt(filterBySpecialty ? 1 : 0);
	dest.writeInt(specialties == null ? 0 : specialties.size());
	if (specialties != null) {
	    for (String speciality : specialties) {
		dest.writeString(speciality);
	    }
	}
    }

    public static final Parcelable.Creator<LoansAndGrantsSearchCriteria> CREATOR = new Parcelable.Creator<LoansAndGrantsSearchCriteria>() {
	public LoansAndGrantsSearchCriteria createFromParcel(Parcel in) {
	    return new LoansAndGrantsSearchCriteria(in);
	}
	public LoansAndGrantsSearchCriteria[] newArray(int size) {
            return new LoansAndGrantsSearchCriteria[size];
        }
    };
    
    private LoansAndGrantsSearchCriteria(Parcel in) {
	includeFederal = (in.readInt() == 1);
	includeState = (in.readInt() == 1);
	state = in.readString();
	filterByIndustry = (in.readInt() == 1);
	industry = in.readString();
	filterBySpecialty = (in.readInt() == 1);
	int specialtyCount = in.readInt();
	specialties = new ArrayList<String>();
	for (int i=0; i<specialtyCount; i++) {
	    specialties.add(in.readString());
	}
    }
}
