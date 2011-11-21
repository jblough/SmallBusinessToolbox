package com.josephblough.sbt.data;

import java.util.ArrayList;
import java.util.List;

public class LicenseAndPermitDataCollection {

    /*
Licenses and Permits Categories:
   Tax Registration (tax registration)
   State Licenses and Permits (state licenses)
   Business Entity Filing (entity filing)
   Fictitious Name Filing (doing business as)
   Employer Requirements (employer requirements)
   
Licenses and Permits Business Types:
   specific types of businesses:
      General Business Licenses
      Auto Dealership
      Barber Shop
      Beauty Salon
      Child Care Services
      Construction Contractor
      Debt Collection Agency
      Electrician
      Massage Therapist
      Plumber
      Restaurant
   specific employer requirements:
      Insurance Requirements
      New Hire Reporting Requirements
      State Tax Registration
      Workplace Poster Requirements

Licenses and Permits Sections (differ by business type):
      Insurance Requirements
         Disability Insurance
         Unemployment Insurance Tax
         Workers' Compensation Insurance
      Restaurant
         Food Service Establishment Permit
         Liquor License
      Workplace Poster Requirements
         State Posters
     */
    
    public List<LicenseAndPermitData> counties;
    public List<LicenseAndPermitData> localities;
    public List<LicenseAndPermitData> states;
    public List<LicenseAndPermitData> businessTypes;
    public List<LicenseAndPermitData> categories;
    
    public LicenseAndPermitDataCollection() {
	counties = new ArrayList<LicenseAndPermitData>();
	localities = new ArrayList<LicenseAndPermitData>();
	states = new ArrayList<LicenseAndPermitData>();
	businessTypes = new ArrayList<LicenseAndPermitData>();
	categories = new ArrayList<LicenseAndPermitData>();
    }
    
    public void addCounty(LicenseAndPermitData county) {
	counties.add(county);
    }
    
    public void addLocality(LicenseAndPermitData locality) {
	localities.add(locality);
    }
    
    public void addState(LicenseAndPermitData state) {
	states.add(state);
    }
    
    public void addBusinessType(LicenseAndPermitData businessType) {
	businessTypes.add(businessType);
    }
    
    public void addCategory(LicenseAndPermitData category) {
	categories.add(category);
    }
    
    @Override
    public String toString() {
        return "Countries: '" + counties + "', " +
        	"Localities: '" + localities + "', " +
        	"States: '" + states + "', " +
        	"Business Types: '" + businessTypes + "', " +
        	"Categories: '" + categories + "'";
    }
}
