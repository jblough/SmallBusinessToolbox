package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.GenericPost;

public interface GenericPostRetrieverCallback {

    //Return results
    public void success(List<GenericPost> results);

    //Error Callback
    public void error(String error);

}
