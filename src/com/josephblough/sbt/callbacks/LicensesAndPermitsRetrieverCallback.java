package com.josephblough.sbt.callbacks;

import com.josephblough.sbt.data.LicenseAndPermitDataCollection;

public interface LicensesAndPermitsRetrieverCallback {

    //Return results
    public void success(LicenseAndPermitDataCollection results);

    //Error Callback
    public void error(String error);

}
