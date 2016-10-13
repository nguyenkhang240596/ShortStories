package com.kalis.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalis.model.Story;
import com.kalis.shortstories.R;

import java.util.List;

/**
 * Created by Kalis on 1/15/2016.
 */
public class StoryAdapter extends ArrayAdapter<Story> {
    Activity activity;
    int resource;
    List<Story> objects;

    public StoryAdapter(Activity activity, int resource, List<Story> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rootView;
        LayoutInflater inflater = activity.getLayoutInflater();
        rootView = inflater.inflate(resource, null);
        try {


            TextView txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
            TextView txtAuthor = (TextView) rootView.findViewById(R.id.txtAuthor);
            ImageView imgBook = (ImageView) rootView.findViewById(R.id.imgBook);

            txtTitle.setText(objects.get(position).getTitle());
            txtAuthor.setText(objects.get(position).getAuthor());

//        imgBook.setImageResource(R.drawable.ic_letter);
            if (objects.get(position).getFavorite() == 1)
                imgBook.setImageResource(R.drawable.ic_favorite);

        }
        catch (Exception e)
        {
//            Log.e("Story Adapter",e.toString());
        }

        return rootView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }
}
