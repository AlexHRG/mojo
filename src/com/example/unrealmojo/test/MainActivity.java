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
	private static final String TAG_IMAGEPATH = "imagePath";
	private static final String TAG_PINNED = "pinned";
	private static final String TAG_NONE = "none";
	private static final String LOG_TAG = "myLog";
	private static String url = "http://unrealmojo.com/porn/test3";
	private static String myFolderName = "/UnrealMojo";
	private static String myFileName = "mojo";
	private ProgressDialog pDialog;
	ArrayList<Map<String, String>> hamster_list = new ArrayList<Map<String, String>>();
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
					File myFilePath = new File(extStorageDirectory + myFolderName);
					try {
						myFilePath.mkdir();
					} catch (Exception ex) {
						Log.d(LOG_TAG, ex.getMessage());
					}

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						Map<String, String> hamster_data = new HashMap<String, String>();
						String title = null;
						String description = null;
						String imageUrl = null;
						String imagePath = null;
						String pinned = null;
						
						try{
							title = jsonObject.getString(TAG_TITLE);
						} catch (JSONException e){
							title = TAG_NONE;
						}
						
						try{
							description = jsonObject.getString(TAG_DESCRIPTION);
						} catch (JSONException e){
							description = TAG_NONE;
						}
						
						try{
							imageUrl = jsonObject.getString(TAG_IMAGEURL);
						} catch (JSONException e){
							imageUrl = TAG_NONE;
						}
						
						try{
							imagePath = ImageDownloader.loadImageToDisc(imageUrl,
									String.format("%s/%s%d", myFilePath.toString(), myFileName, i));
						} catch (Exception e){
							imagePath = TAG_NONE;
						}
						
						try {
							pinned = jsonObject.get(TAG_PINNED) == null ? "0"
									: "1";
						} catch (JSONException e) {
							pinned = "0";
						}
						
						hamster_data.put(TAG_TITLE, title);
						hamster_data.put(TAG_DESCRIPTION, description);
						hamster_data.put(TAG_IMAGEPATH, imagePath);
						hamster_data.put(TAG_PINNED, pinned);

						hamster_list
								.add((HashMap<String, String>) hamster_data);
						
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
							TAG_DESCRIPTION, TAG_IMAGEPATH }, new int[] {
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
					Map<String, String> currentItem = (Map<String, String>) parent.getItemAtPosition(position);
					String imagePath = currentItem.get(TAG_IMAGEPATH);

					intent.putExtra(TAG_TITLE, currentItem.get(TAG_TITLE));
					intent.putExtra(TAG_DESCRIPTION,
							currentItem.get(TAG_DESCRIPTION));
					intent.putExtra(TAG_IMAGEPATH, imagePath);
					
					startActivity(intent);

				}

			});
		}

	}

}
