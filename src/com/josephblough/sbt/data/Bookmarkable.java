package com.josephblough.sbt.data;

import com.josephblough.sbt.ApplicationController;

public interface Bookmarkable {

    public String getType();
    public String getName();
    public String getUrl();
    public String toJson();
    
    public int getDetailCount();
    public boolean isVisible(final int detail);
    public String getDetailLabel(final int detail);
    public String getDetailValue(final int detail);
    public void removeFromBookmarks(ApplicationController app);
    public String formatForSharing();
}
