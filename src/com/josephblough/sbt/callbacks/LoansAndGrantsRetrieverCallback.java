package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.LoanAndGrantData;

public interface LoansAndGrantsRetrieverCallback {

    //Return results
    public void success(List<LoanAndGrantData> results);

    //Error Callback
    public void error(String error);

}
