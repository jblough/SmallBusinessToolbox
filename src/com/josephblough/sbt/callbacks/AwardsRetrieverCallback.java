package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.Award;

public interface AwardsRetrieverCallback {

    //Return results
    public void success(List<Award> results);

    //Error Callback
    public void error(String error);

}
