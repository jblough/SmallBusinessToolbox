package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.LocalityWebData;

public interface LocalityWebDataRetrieverCallback {

    //Return results
    public void success(List<LocalityWebData> results);

    //Error Callback
    public void error(String error);

}
