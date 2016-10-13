package com.kalis.request;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kalis.adapter.StoryAdapter;
import com.kalis.key.KeySource;
import com.kalis.model.Story;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kalis on 1/23/2016.
 */
public class RequestStories extends AsyncTask<String, Object, Void> {
    private Activity activity;
    private SQLiteDatabase database;
    private long totalStories;
    private StoryAdapter allStoriesAdapter, favoriteStoriesAdapter;
    private int error = 0;

    public RequestStories(Activity activity, StoryAdapter allStoriesAdapter, StoryAdapter favoriteStoriesAdapter) {
        this.activity = activity;
        this.allStoriesAdapter = allStoriesAdapter;
        this.favoriteStoriesAdapter = favoriteStoriesAdapter;

    }

    public byte[] DownloadImageTaskCovertToByteArray(String link) {
        Bitmap bm = null;
        byte[] bytes = new byte[0];
        try {
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream is = con.getInputStream();
            bm = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Could be Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.WEBP
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        totalStories = 0;
//        if (error == 1){
//            Toast.makeText(activity, "Please check your connection !", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String link = params[0];
            URL url = new URL(link);
//            InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
//            Type listType = new TypeToken<List<Story>>() {
//            }.getType();
//            List<Story> stories = new Gson().fromJson(isr, listType);

            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            Story[] stories = new Gson().fromJson(reader, Story[].class);

            database = activity.openOrCreateDatabase(KeySource.DATABASE_NAME, Context.MODE_PRIVATE, null);

            long result;

            for (int i = 0; i < stories.length; i++) {
                SystemClock.sleep(100);

                ContentValues value = new ContentValues();
                byte[] bytes = DownloadImageTaskCovertToByteArray(stories[i].getUrl());
                stories[i].setBytes(bytes);
                value.put("id", stories[i].getId());
                value.put("title", stories[i].getTitle());
                value.put("author", stories[i].getAuthor());
                if (stories[i].getDescription() == null)
                {
                    value.put("description", "");
                }
                else
                {
                    value.put("description", stories[i].getDescription());
                }
                value.put("content", stories[i].getContent());
                value.put("favorite", stories[i].getFavorite());
                value.put("language", stories[i].getLaguage().toLowerCase());
                value.put("picture", bytes);
                totalStories++;
                result = database.insert(KeySource.TABLE, null, value);
                if (result != -1) {
                    Log.e("SAVING", "Saved " + i + " Stories to Local DataBase");

                    publishProgress(stories[i].getId(),stories[i].getTitle(),stories[i].getAuthor(),stories[i].getDescription(),
                            stories[i].getContent(), stories[i].getFavorite(),stories[i].getLaguage(),stories[i].getUrl(),bytes );
                }

            }

        } catch (Exception e) {
            error = 1;
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        Story story = new Story((int)values[0],(String)values[1],(String)values[2],(String)values[3],(String)values[4],
                (int)values[5],(String)values[6],(String)values[7],(byte[])values[8]);
        if (story != null) {
            allStoriesAdapter.add(story);
            if (story.getFavorite() == 1) {
                favoriteStoriesAdapter.add(story);
            }
        }

    }


}
