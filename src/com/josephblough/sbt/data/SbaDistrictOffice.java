package com.josephblough.sbt.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.josephblough.sbt.ApplicationController;

public class SbaDistrictOffice implements Bookmarkable {

    private final static String TAG = "SbaDistrictOffice";

    public final static String TYPE = "SBA District Office";
    
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

    public String formatForSharing() {
	final StringBuffer address = new StringBuffer();
	if (!isEmpty(street)) {
	    address.append(street);
	}
	if (!isEmpty(street2)) {
	    if (address.length() > 0)
		address.append(" ");
	    address.append(street2);
	}
	if (!isEmpty(city)) {
	    address.append(" " + city);
	}
	if (!isEmpty(province)) {
	    address.append(", " + province);
	}
	if (!isEmpty(postalCode)) {
	    address.append(" " + postalCode);
	}
	return address.toString();
    }
    
    private String formatLocation() {
	final StringBuffer location = new StringBuffer();
	if (!isEmpty(city)) {
	    location.append(city);
	}
	if (!isEmpty(province)) {
	    if (location.length() > 0)
		location.append(", ");
	    location.append(province);
	}
	if (!isEmpty(postalCode)) {
	    if (location.length() > 0)
		location.append(" ");
	    location.append(postalCode);
	}
	return location.toString();
    }
    
    private boolean isEmpty(final String value) {
	return value == null || "".equals(value) || "null".equals(value);
    }

    public String getType() {
	return TYPE;
    }

    public String getName() {
	return name;
    }

    public String getUrl() {
	return formatForSharing();
    }

    public String toJson() {
	JSONObject json = new JSONObject();
	try {
	    if (title != null)
		json.put("title", title);
	    if (name != null)
		json.put("field_name_value", name);
	    if (street != null)
		json.put("field_street_value", street);
	    if (street2 != null)
		json.put("field_street2_value", street2);
	    if (city != null)
		json.put("field_city_value", city);
	    if (province != null)
		json.put("field_province_value", province);
	    if (postalCode != null)
		json.put("field_postal_code_value", postalCode);
	    if (country != null)
		json.put("field_country_value", country);
	    if (latitude != null)
		json.put("field_latitude_value", latitude);
	    if (longitude != null)
		json.put("field_longitude_value", longitude);
	}
	catch (JSONException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return json.toString();
    }

    public int getDetailCount() {
	return 5;
    }

    public boolean isVisible(int detail) {
	return !isEmpty(getDetailValue(detail));
    }

    public String getDetailLabel(int detail) {
	switch (detail) {
	case 0:
	    return "Title:";
	case 1:
	    return "Name:";
	case 2:
	    return "Address:";
	};
	return "";
    }

    public String getDetailValue(int detail) {
	switch (detail) {
	case 0:
	    return title;
	case 1:
	    return name;
	case 2:
	    return street;
	case 3:
	    return street2;
	case 4:
	    return this.formatLocation();
	};
	return null;
    }

    public void removeFromBookmarks(ApplicationController app) {
	app.bookmarks.removeBookmark(this);
	app.saveBookmarks();
    }
}
