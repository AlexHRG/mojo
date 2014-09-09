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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG_TITLE = "title";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_IMAGEURL = "image";
	private static final String TAG_IMAGEPATH = "imagePath";
	private static final String TAG_PINNED = "pinned";
	private static final String LOG_TAG = "myLog";
	private static String url = "http://unrealmojo.com/porn/test3";
	private static String folderName = "/UnrealMojo";
	private ProgressDialog pDialog;
	private ArrayList<Map<String, String>> hamster_list = new ArrayList<Map<String, String>>();
	private ListView listView;
	private SimpleAdapter adapter;
	private String about = "UnrealMojo test task\nversion: pre release\nAuthor: HIRURG";

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(android.R.id.list);

		if (savedInstanceState == null){
			new HardTask().execute();
		} else {
			hamster_list = (ArrayList<Map<String, String>>) savedInstanceState.get("hamsters");
		}
		
		adapter = new SimpleAdapter(MainActivity.this,
				hamster_list, R.layout.list, new String[] { TAG_TITLE,
						TAG_DESCRIPTION, TAG_IMAGEPATH }, new int[] {
						R.id.listTitle, R.id.listDescription,
						R.id.listImage });
		
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent,
					View itemClicked, int position, long id) {

				Intent intent = new Intent(MainActivity.this,
						HamsterActivity.class);
				Map<String, String> currentItem = (Map<String, String>) parent.getItemAtPosition(position);

				intent.putExtra(TAG_TITLE, currentItem.get(TAG_TITLE));
				intent.putExtra(TAG_DESCRIPTION,currentItem.get(TAG_DESCRIPTION));
				intent.putExtra(TAG_IMAGEPATH, currentItem.get(TAG_IMAGEPATH));
				
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
        	hamster_list.clear();
            this.onCreate(null);
            return true;
        case R.id.about:
            Toast.makeText(this, about, Toast.LENGTH_LONG).show();
            return true;
        case R.id.exit:
            System.exit(0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
	
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putSerializable("hamsters", hamster_list);
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
			if (!pDialog.isShowing()){
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

					String extStorageDirectory = Environment
							.getExternalStorageDirectory().toString();
					File filePath = new File(extStorageDirectory + folderName);
					filePath.mkdir();

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						Map<String, String> hamster_data = new HashMap<String, String>();
						String title = null;
						String description = null;
						String imageUrl = null;
						String imagePath = null;
						String pinned = null;						

						title = jsonObject.getString(TAG_TITLE);
						description = jsonObject.getString(TAG_DESCRIPTION);
						imageUrl = jsonObject.getString(TAG_IMAGEURL);
						
						String[] fileNameTokens = imageUrl.split("/");
						int last = fileNameTokens.length - 1;
						String fileName = fileNameTokens[last];
						
						imagePath = ImageDownloader.loadImageToDisc(imageUrl,
									String.format("%s/%s", filePath.toString(), fileName));
						pinned = jsonObject.isNull(TAG_PINNED) ? "0" : "1";
						
						hamster_data.put(TAG_TITLE, title);
						hamster_data.put(TAG_DESCRIPTION, description);
						hamster_data.put(TAG_IMAGEPATH, imagePath);
						hamster_data.put(TAG_PINNED, pinned);

						hamster_list.add((HashMap<String, String>) hamster_data);
						
						Collections.sort(hamster_list, new HamsterComparator());
						
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
			
			adapter.notifyDataSetChanged();
		}

	}

}
