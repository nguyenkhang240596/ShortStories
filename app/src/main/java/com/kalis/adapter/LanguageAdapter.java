package com.kalis.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalis.shortstories.R;

import java.util.List;

/**
 * Created by Kalis on 1/15/2016.
 */
public class LanguageAdapter extends ArrayAdapter<String> {
    Activity activity;
    int resource;
    List<String> objects;
    public static int selectItem=-1;

    int [] flagImages = {
            R.drawable.flag_vietnam,
            R.drawable.flag_english,
            R.drawable.flag_france,
            R.drawable.flag_china,
            R.drawable.flag_germany,
            R.drawable.flag_japan,
            R.drawable.flag_spain,
            R.drawable.flag_thailand,
            R.drawable.flag_korea,
    };

    public LanguageAdapter(Activity activity, int resource, List<String> objects) {
        super(activity, resource, objects);
        this.activity = activity;
        this.resource = resource;
        this.objects = objects;

    }
    static class ViewHolder {

        private TextView txtTitle;
        private ImageView imgBook,imgBtnCheck;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(resource, null);
            viewHolder = new ViewHolder();

            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtLanguageTitle);
            viewHolder.imgBook = (ImageView) convertView.findViewById(R.id.imgFlagCountry);
            viewHolder.imgBtnCheck = (ImageView) convertView.findViewById(R.id.imgBtnCheck);



            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgBtnCheck.setVisibility(View.GONE);
        viewHolder.txtTitle.setText(objects.get(position).toString());
        viewHolder.imgBook.setImageResource(flagImages[position]);
        if(position==selectItem)
            viewHolder.imgBtnCheck.setVisibility(View.VISIBLE);
        return convertView;
    }

    @Override
    public int getCount() {
        return objects.size();
    }
}
