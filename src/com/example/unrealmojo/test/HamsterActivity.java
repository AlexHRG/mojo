package com.example.unrealmojo.test;

import java.net.URI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class HamsterActivity extends Activity {
	private static final String TAG_TITLE = "title";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_PINNED = "pinned";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityhamster);
		
		TextView titleView = (TextView) findViewById(R.id.hamsterTitle);
		TextView descView = (TextView) findViewById(R.id.hamsterDescription);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(TAG_TITLE);
		String description = intent.getStringExtra(TAG_DESCRIPTION);
		String imageUrl = intent.getStringExtra(TAG_IMAGE);
		imageView.setImageBitmap(GetImage.getRemoteImage(imageUrl));
		
		titleView.setText(title);
		descView.setText(description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hamster, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
