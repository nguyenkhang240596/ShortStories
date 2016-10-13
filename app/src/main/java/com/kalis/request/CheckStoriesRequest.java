package com.kalis.request;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.kalis.key.KeySource;
import com.kalis.task.StoriesTask;

/**
 * Created by Kalis on 1/23/2016.
 */
public class CheckStoriesRequest {

    private Activity activity;
    private SQLiteDatabase database;

    public CheckStoriesRequest(Activity activity) {
        this.activity = activity;
    }

    public boolean compareStoriesLocalAndServer() {
        try {
            int numFromLocal = loadNumFromLocal();
            StoriesTask task = new StoriesTask();
            int numFromServer = task.execute(KeySource.BASESERVER + "?request=count_stories").get();

//            if (numFromLocal < numFromServer) {
            if (numFromLocal != numFromServer) {
                return true;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;

    }

    private int loadNumFromLocal() {
        int numFromLocal = 0;
        database = activity.openOrCreateDatabase(KeySource.DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor c = database.rawQuery("select count(*) from story", null);
        if (c.moveToNext()) {
            numFromLocal = c.getInt(0);
        }
        return numFromLocal;
    }

}
