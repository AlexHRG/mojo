package com.example.unrealmojo.test;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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

public class DetailActivity extends Activity {;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		
		TextView titleView = (TextView) findViewById(R.id.itemTitle);
		TextView descView = (TextView) findViewById(R.id.itemDescription);
		ImageView imageView = (ImageView) findViewById(R.id.itemImage);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(MainActivity.TAG_TITLE);
		String description = intent.getStringExtra(MainActivity.TAG_DESCRIPTION);
		String imagePath = intent.getStringExtra(MainActivity.TAG_IMAGEURL);
		
		Log.d(MainActivity.LOG_TAG, imagePath);
		
		if (imagePath != MainActivity.TAG_NONE){
//			Bitmap image = BitmapFactory.decodeFile(imagePath);
//			imageView.setImageBitmap(image);
			imageLoader. displayImage(imagePath, imageView);
		}
		titleView.setText(title);
		descView.setText(description);
	}
	
}
