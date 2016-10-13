package com.kalis.task;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.kalis.adapter.StoryAdapter;
import com.kalis.key.KeySource;
import com.kalis.model.Story;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Kalis on 1/23/2016.
 */

public class UpdateStoriesTask extends AsyncTask<Void,Story,Integer>
{
    private int result = -1;
    private int requestCode;
    private String currentLanguage;
    private SQLiteDatabase sqLiteDatabase;
    ArrayList<Story> arrayListAllStories,arrayListFavoriteStories;
    StoryAdapter adapterAllStories,adapterFavoriteStories;
    ArrayList<Story> arrStories = null;
    StoryAdapter adapterStories = null;

    public UpdateStoriesTask(int requestCode, String currentLanguage, SQLiteDatabase sqLiteDatabase, ArrayList<Story> arrayListAllStories, ArrayList<Story> arrayListFavoriteStories, StoryAdapter adapterAllStories, StoryAdapter adapterFavoriteStories) {
        this.requestCode = requestCode;
        this.currentLanguage = currentLanguage;
        this.sqLiteDatabase = sqLiteDatabase;
        this.arrayListAllStories = arrayListAllStories;
        this.arrayListFavoriteStories = arrayListFavoriteStories;
        this.adapterAllStories = adapterAllStories;
        this.adapterFavoriteStories = adapterFavoriteStories;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {

            Cursor c = null;
            if (requestCode == KeySource.requestShowAllData) {
                if (currentLanguage != null) {
                    c = sqLiteDatabase.query("story", null, "language=?", new String[]{currentLanguage}, null, null, null, null);
                } else {
                    c = sqLiteDatabase.query("story", null, null, null, null, null, null, null);
                }
                arrStories = arrayListAllStories;
                adapterStories = adapterAllStories;

            } else if (requestCode == KeySource.requestShowFavorite) {
                if (currentLanguage != null) {
                    c = sqLiteDatabase.query("story", null, "language=? and favorite=?", new String[]{currentLanguage, "1"}, null, null, null, null);
                } else {
                    c = sqLiteDatabase.query("story", null, "favorite=?", new String[]{"1"}, null, null, null, null);
                }
                arrStories = arrayListFavoriteStories;
                adapterStories = adapterFavoriteStories;

            }
            arrStories.clear();
            while (c.moveToNext()) {
                Story story = new Story();
                story.setId(c.getInt(0));
                story.setTitle(c.getString(1));
                story.setAuthor(c.getString(2));
                story.setDescription(c.getString(3));
                story.setContent(c.getString(4));
                story.setFavorite(c.getInt(5));
                story.setLaguage(c.getString(6));
                story.setBytes(c.getBlob(7));

//                SystemClock.sleep(300);
                publishProgress(story);

            }
            c.close();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Story... values) {
        super.onProgressUpdate(values);
        try
        {
            arrStories.add(values[0]);
            adapterStories.notifyDataSetChanged();

        }
        catch (Exception e)
        {
            Log.e("Update Story : " , e.toString());
        }
    }


}