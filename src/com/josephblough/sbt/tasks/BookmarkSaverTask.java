package com.josephblough.sbt.tasks;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.data.Bookmarks;

import android.os.AsyncTask;

public class BookmarkSaverTask extends AsyncTask<Bookmarks, Void, Void> {

    private ApplicationController context;
    
    public BookmarkSaverTask(ApplicationController context) {
	this.context = context;
    }

    @Override
    protected Void doInBackground(Bookmarks... bookmarks) {
	bookmarks[0].saveBookmarks(context);
	
	return null;
    }
}
