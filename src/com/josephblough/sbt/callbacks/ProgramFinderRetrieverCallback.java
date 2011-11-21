package com.josephblough.sbt.callbacks;

import java.util.List;

import com.josephblough.sbt.data.SmallBusinessProgram;

public interface ProgramFinderRetrieverCallback {

    //Return results
    public void success(List<SmallBusinessProgram> results);

    //Error Callback
    public void error(String error);

}
