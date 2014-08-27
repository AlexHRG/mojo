package com.example.unrealmojo.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_IMAGEURL = "image";
	private static final String TAG_IMAGEFILE = "imageFile";
	private static final String TAG_PINNED = "pinned";
	private static final String LOG_TAG = "myLog";
	private static String url = "http://unrealmojo.com/porn/test3";
	private static String myFolder = "/myTestFolder";
	private ProgressDialog pDialog;
	ArrayList<Map<String, Object>> hamster_list = new ArrayList<Map<String, Object>>();
	ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(android.R.id.list);

		new GetHamsters().execute();
	}

	private class GetHamsters extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {

			ServiceHandler sh = new ServiceHandler();

			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

			if (jsonStr != null) {

				try {
					JSONArray jsonArray = new JSONArray(jsonStr);

					String extStorageDirectory = Environment
							.getExternalStorageDirectory().toString();
					File myNewFolder = new File(extStorageDirectory + myFolder);
					try {
						myNewFolder.mkdir();
					} catch (Exception ex) {
						Log.d(LOG_TAG, ex.getMessage());
					}

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						Map<String, Object> hamster_data = new HashMap<String, Object>();
						String imageUrl = jsonObject.getString(TAG_IMAGEURL);

						hamster_data.put(TAG_TITLE,
								jsonObject.getString(TAG_TITLE));
						hamster_data.put(TAG_DESCRIPTION,
								jsonObject.getString(TAG_DESCRIPTION));
						hamster_data.put(TAG_IMAGEURL, imageUrl);
						hamster_data.put(TAG_IMAGEFILE, ImageDownloader
								.loadImageToDisc(imageUrl,
										myNewFolder.toString() + i));

						String pinned = null;
						try {
							pinned = jsonObject.get(TAG_PINNED) == null ? "0"
									: "1";
						} catch (JSONException e) {
							pinned = "0";
						}
						hamster_data.put(TAG_PINNED, pinned);

						hamster_list
								.add((HashMap<String, Object>) hamster_data);
						
						Collections.sort(hamster_list, new HamsterComparator());
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from server");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (pDialog.isShowing())
				pDialog.dismiss();

			SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
					hamster_list, R.layout.list, new String[] { TAG_TITLE,
							TAG_DESCRIPTION, TAG_IMAGEFILE }, new int[] {
							R.id.listTitle, R.id.listDescription,
							R.id.listImage });

			listView.setAdapter(adapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

			listView.setOnItemClickListener(new ListView.OnItemClickListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void onItemClick(AdapterView<?> parent,
						View itemClicked, int position, long id) {

					Intent intent = new Intent(MainActivity.this,
							HamsterActivity.class);
					Map<String, Object> currentItem = new HashMap<String, Object>();
					currentItem = (Map<String, Object>) parent.getItemAtPosition(position);
					String imagePath = null;
					try {
						imagePath = currentItem.get(TAG_IMAGEFILE).toString();
					} catch (NullPointerException e) {
						imagePath = "NONE";
					} finally {
						intent.putExtra(TAG_TITLE, currentItem.get(TAG_TITLE)
								.toString());
						intent.putExtra(TAG_DESCRIPTION,
								currentItem.get(TAG_DESCRIPTION).toString());
						intent.putExtra(TAG_IMAGEFILE, imagePath);
					}
					startActivity(intent);

				}

			});
		}

	}

}
