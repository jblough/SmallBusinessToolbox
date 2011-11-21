package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.GreenPost;

public interface GreenPostRetrieverCallback {

    //Return results
    public void success(List<GreenPost> results);

    //Error Callback
    public void error(String error);

}
