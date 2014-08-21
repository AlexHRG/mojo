package com.example.unrealmojo.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class GetImage {
	
	public static Bitmap getRemoteImage(String imageUrl) {
	    try {
	    	URL url = new URL(imageUrl);
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
	        final Bitmap bm = BitmapFactory.decodeStream(bis);
	        bis.close();

	        return bm;
	    } catch (IOException e) {
	        Log.d("myTAG", "Connection error");
	    }
	    return null;
	}

}
