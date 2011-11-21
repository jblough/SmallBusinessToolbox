package com.josephblough.sbt.data;

import org.json.JSONObject;

public class SbaDistrictOffice {

    public String title;
    public String name;
    public String street;
    public String street2;
    public String city;
    public String province;
    public String postalCode;
    public String country;
    public String latitude;
    public String longitude;
    
    public SbaDistrictOffice(final JSONObject json) {
	title = json.optString("title");
	name = json.optString("field_name_value");
	street = json.optString("field_street_value");
	street2 = json.optString("field_street2_value");
	city = json.optString("field_city_value");
	province = json.optString("field_province_value");
	postalCode = json.optString("field_postal_code_value");
	country = json.optString("field_country_value");
	latitude = json.optString("field_latitude_value");
	longitude = json.optString("field_longitude_value");
    }
    
    @Override
    public String toString() {
        return "Title: '" + title + "', " +
        	"Name: '" + name + "', " +
        	"Street: '" + street + "', " +
        	"Street 2: '" + street2 + "', " +
        	"City: '" + city + "', " +
        	"Province: '" + province + "', " +
        	"Postal Code: '" + postalCode + "', " +
        	"Country: '" + country + "', " +
        	"Latitude: '" + latitude + "', " +
        	"Longitude: '" + longitude + "'";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((city == null) ? 0 : city.hashCode());
	result = prime * result + ((country == null) ? 0 : country.hashCode());
	result = prime * result
		+ ((latitude == null) ? 0 : latitude.hashCode());
	result = prime * result
		+ ((longitude == null) ? 0 : longitude.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result
		+ ((postalCode == null) ? 0 : postalCode.hashCode());
	result = prime * result
		+ ((province == null) ? 0 : province.hashCode());
	result = prime * result + ((street == null) ? 0 : street.hashCode());
	result = prime * result + ((street2 == null) ? 0 : street2.hashCode());
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SbaDistrictOffice other = (SbaDistrictOffice) obj;
	if (city == null) {
	    if (other.city != null)
		return false;
	} else if (!city.equals(other.city))
	    return false;
	if (country == null) {
	    if (other.country != null)
		return false;
	} else if (!country.equals(other.country))
	    return false;
	if (latitude == null) {
	    if (other.latitude != null)
		return false;
	} else if (!latitude.equals(other.latitude))
	    return false;
	if (longitude == null) {
	    if (other.longitude != null)
		return false;
	} else if (!longitude.equals(other.longitude))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (postalCode == null) {
	    if (other.postalCode != null)
		return false;
	} else if (!postalCode.equals(other.postalCode))
	    return false;
	if (province == null) {
	    if (other.province != null)
		return false;
	} else if (!province.equals(other.province))
	    return false;
	if (street == null) {
	    if (other.street != null)
		return false;
	} else if (!street.equals(other.street))
	    return false;
	if (street2 == null) {
	    if (other.street2 != null)
		return false;
	} else if (!street2.equals(other.street2))
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	return true;
    }

}
