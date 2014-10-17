package com.example.unrealmojo.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	static final String TAG_TITLE = "title";
    static final String TAG_DESCRIPTION = "description";
	static final String TAG_IMAGEURL = "image";
	static final String TAG_NONE = "none";
    static final String TAG_IMAGEPATH = "imagePath";
	static final String TAG_PINNED = "pinned";
	static final String LOG_TAG = "myLog";
	private static String url = "http://unrealmojo.com/porn/test3";
	private ProgressDialog pDialog;
	private ArrayList<Map<String, String>> item_list = new ArrayList<Map<String, String>>();
	private ListView listView;
	private String aboutTitle = "UnrealMojo test task";
	private String aboutVersion = "pre release";
	private String aboutAuthor = "HIRURG";
	ImageLoader imageLoader;
	MyAdapter myAdapter;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		listView = (ListView) findViewById(android.R.id.list);

		if (savedInstanceState == null) {
			new HardTask().execute();
		} else {
			item_list = (ArrayList<Map<String, String>>) savedInstanceState
					.get("items");
		}

		createList();
	}

	private void createList() {
		myAdapter = new MyAdapter(this, item_list);

		listView.setAdapter(myAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		listView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked,
					int position, long id) {

				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);
				Map<String, String> currentItem = (Map<String, String>) parent
						.getItemAtPosition(position);

				intent.putExtra(TAG_TITLE, currentItem.get(TAG_TITLE));
				intent.putExtra(TAG_DESCRIPTION,
						currentItem.get(TAG_DESCRIPTION));
				intent.putExtra(TAG_IMAGEURL, currentItem.get(TAG_IMAGEURL));

				startActivity(intent);

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			item_list.clear();
			this.onCreate(null);
			return true;
		case R.id.about:
			showInfo();
			return true;
		case R.id.exit:
			System.exit(0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showInfo() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		String info_line = String.format("%s: %s\n%s: %s\n%s: %s",
				getResources().getString(R.string.program_title), aboutTitle,
				getResources().getString(R.string.pogram_version),
				aboutVersion,
				getResources().getString(R.string.program_author), aboutAuthor);

		builder.setTitle(R.string.info)
				.setMessage(info_line)
				.setCancelable(true)
				.setNegativeButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("items", item_list);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	class HardTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getResources().getString(R.string.wait));
			pDialog.setCancelable(true);
			if (!pDialog.isShowing()) {
				pDialog.show();
			}
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			ServiceHandler sh = new ServiceHandler();

			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			if (jsonStr != null) {

				try {
					JSONArray jsonArray = new JSONArray(jsonStr);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						Map<String, String> item_data = new HashMap<String, String>();
						String title = null;
						String description = null;
						String imageUrl = null;
						String pinned = null;

						title = jsonObject.getString(TAG_TITLE);
						description = jsonObject.getString(TAG_DESCRIPTION);
						imageUrl = jsonObject.getString(TAG_IMAGEURL);
						pinned = jsonObject.isNull(TAG_PINNED) ? "0" : "1";

						item_data.put(TAG_TITLE, title);
						item_data.put(TAG_DESCRIPTION, description);
						item_data.put(TAG_IMAGEURL, imageUrl);
						item_data.put(TAG_PINNED, pinned);

						item_list.add((HashMap<String, String>) item_data);

						Collections.sort(item_list, new ItemComparator());

					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception ex) {
					Log.d(LOG_TAG, ex.getMessage());
				}
			} else {
				Log.d(LOG_TAG, "Couldn't get any data from server");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (pDialog.isShowing())
				pDialog.dismiss();

			myAdapter.notifyDataSetChanged();
		}

	}

}
