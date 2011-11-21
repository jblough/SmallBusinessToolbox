package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.Solicitation;

public interface SolicitationsRetrieverCallback {

    //Return results
    public void success(List<Solicitation> results);

    //Error Callback
    public void error(String error);

}
