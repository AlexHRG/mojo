package com.example.unrealmojo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MyAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List mData;
    ImageLoader imageLoader;

    public MyAdapter(Context context, List data){
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        
        imageLoader = ImageLoader.getInstance();
		//imageLoader.init(ImageLoaderConfiguration.createDefault());
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return (Map<String, String>) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

      View vi = view;             //trying to reuse a recycled view
      ViewHolder holder = null;

      if (vi == null) {
          //The view is not a recycled one: we have to inflate
          vi = mLayoutInflater.inflate(R.layout.list, parent, false);
          holder = new ViewHolder();

          holder.listTitle = (TextView) vi.findViewById(R.id.listTitle);
          holder.listDescription = (TextView) vi.findViewById(R.id.listDescription);
          holder.listImage = (ImageView) vi.findViewById(R.id.listImage);
          vi.setTag(holder);
      } else {
          // View recycled !
          // no need to inflate
          // no need to findViews by id
          holder = (ViewHolder) vi.getTag();
      }

      Map<String, String> item = getItem(position);

      holder.listTitle.setText(item.get(MainActivity.TAG_TITLE));
      holder.listDescription.setText(item.get(MainActivity.TAG_DESCRIPTION));
      imageLoader. displayImage(item.get(MainActivity.TAG_IMAGEURL), holder.listImage);

      return vi;
    }
}