package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.RecommendedSite;

public interface RecommendedSitesRetrieverCallback {

    //Return results
    public void success(List<RecommendedSite> results);

    //Error Callback
    public void error(String error);

}
