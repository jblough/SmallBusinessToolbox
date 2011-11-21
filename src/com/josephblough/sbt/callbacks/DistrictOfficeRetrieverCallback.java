package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.SbaDistrictOffice;

public interface DistrictOfficeRetrieverCallback {

    //Return results
    public void success(List<SbaDistrictOffice> results);

    //Error Callback
    public void error(String error);

}
