package com.josephblough.sbt.tasks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class PdfCheckerTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "PdfCheckerTask";
    
    private final Context context;
    
    public PdfCheckerTask(Context context) {
	this.context = context;
    }
    
    @Override
    protected Boolean doInBackground(String... links) {
	String contentType = null;
	try {
	    URL url = new URL(links[0]);
	    HttpURLConnection connection = null;
	    try {
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.connect();
		//Log.d(TAG, "Link content type: " + connection.getContentType());
		if (connection != null) {
		    contentType = connection.getContentType();
		}
	    }
	    finally {
		if (connection != null) {
		    connection.disconnect();
		}
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}

	return ("application/pdf".equals(contentType));
    }

    @Override
    protected void onPostExecute(Boolean isPdf) {
        super.onPostExecute(isPdf);
        
	if (isPdf) {
	    Toast toast = Toast.makeText(context.getApplicationContext(), 
		    "This document is a PDF. You may need to launch this document from the notification area", Toast.LENGTH_LONG);
	    toast.setGravity(Gravity.BOTTOM, 0, 0);
	    toast.show();
	}
    }
}
