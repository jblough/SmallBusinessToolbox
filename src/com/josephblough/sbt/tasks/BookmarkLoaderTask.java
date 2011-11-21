package com.josephblough.sbt.tasks;

import com.josephblough.sbt.ApplicationController;
import com.josephblough.sbt.data.Bookmarks;

import android.os.AsyncTask;

public class BookmarkLoaderTask extends AsyncTask<Bookmarks, Void, Void> {

    private ApplicationController context;
    
    public BookmarkLoaderTask(ApplicationController context) {
	this.context = context;
    }

    @Override
    protected Void doInBackground(Bookmarks... bookmarks) {
	bookmarks[0].loadBookmarks(context);
	
	return null;
    }
}
