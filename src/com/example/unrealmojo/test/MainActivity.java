package com.example.unrealmojo.test;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class MainActivity extends Activity {
        private static final String TAG_TITLE = "title";
        private static final String TAG_DESCRIPTION = "description";
        private static final String TAG_IMAGEURL = "image";
        private static final String TAG_PICTURE = "picture";
        private static final String TAG_PINNED = "pinned";
        private static final String LOG_TAG = "myLog";
        private static String url = "http://unrealmojo.com/porn/test3";
        private ProgressDialog pDialog;
        private Bitmap picture;
        JSONArray hamsters = null;
        ArrayList<HashMap<String, Object>> hamster_list = new ArrayList<HashMap<String, Object>>();
        ListView listView;

        
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView)findViewById(android.R.id.list);
        
        new GetContacts().execute();
    }
    
    private class GetContacts extends AsyncTask<Void, Void, Void> {
    	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @SuppressWarnings("unchecked")
		@Override
        protected Void doInBackground(Void... arg0) {
        	
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
 
            //Log.d("jsonStr: ", "> " + jsonStr);
 
            if (jsonStr != null) {
            	
            	
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    	for (int i = 0; i < 5; i++){
                    		JSONObject jsonObject = jsonArray.getJSONObject(i);
                    		Map hamster_data = new HashMap<String, Object>();
                    		String imageUrl = jsonObject.getString(TAG_IMAGEURL);
                    		
                    		hamster_data.put(TAG_TITLE, jsonObject.getString(TAG_TITLE));
                    		hamster_data.put(TAG_DESCRIPTION, jsonObject.getString(TAG_DESCRIPTION));
                    		//hamster_data.put(TAG_IMAGEURL, imageUrl);
                    		try {
								hamster_data.put(TAG_PICTURE, ImageDownloader.getRemoteImage(imageUrl));
							} catch (Exception e1) {
								Log.d(LOG_TAG, "Image downloading error");
								e1.printStackTrace();
							}
                    		
                    		String pinned = null;
                    		try{
                    			pinned = jsonObject.get(TAG_PINNED) == null ? "0" : "1";
                    		} catch (JSONException e) {
                    			pinned = "0";
                    		}
                    		hamster_data.put(TAG_PINNED, pinned);
                    		
                    		hamster_list.add((HashMap<String, Object>) hamster_data);
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
                    hamster_list, 
                    R.layout.list, new String[]{
                    TAG_TITLE,
                    TAG_DESCRIPTION,
                    TAG_PICTURE
                    }, new int[]{
                    R.id.listTitle,
                    R.id.listDescription,
                    R.id.listImage});

            	listView.setAdapter(adapter);
            	listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
            	listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                	@Override
                	public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                			long id) {
                		
                		Intent intent = new Intent(MainActivity.this, HamsterActivity.class);
                		Map currentItem = new HashMap<String, Object>();
                		currentItem = (Map) parent.getItemAtPosition(position);
                		
                		intent.putExtra(TAG_TITLE, currentItem.get(TAG_TITLE).toString());
                		intent.putExtra(TAG_DESCRIPTION, currentItem.get(TAG_DESCRIPTION).toString());
                		intent.putExtra(TAG_PICTURE, (Bitmap) currentItem.get(TAG_PICTURE));
                		
                		startActivity(intent);

                	}

                });
            	
        }
        
    }
    
}

//package com.example.unrealmojo.test;
//
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//public class MainActivity extends ActionBarActivity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//}