package com.example.unrealmojo.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity {
	 private static final String TAG_TITLE = "title";
     private static final String TAG_DESCRIPTION = "description";
     private static final String TAG_IMAGEPATH = "imagePath";
     private static final String TAG_NONE = "none";
     private static final String LOG_TAG = "myLog";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		
		TextView titleView = (TextView) findViewById(R.id.itemTitle);
		TextView descView = (TextView) findViewById(R.id.itemDescription);
		ImageView imageView = (ImageView) findViewById(R.id.itemImage);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(TAG_TITLE);
		String description = intent.getStringExtra(TAG_DESCRIPTION);
		String imagePath = intent.getStringExtra(TAG_IMAGEPATH);
		if (imagePath != TAG_NONE){
			Bitmap image = BitmapFactory.decodeFile(imagePath);
			imageView.setImageBitmap(image);	
		}
		titleView.setText(title);
		descView.setText(description);
	}
	
}
