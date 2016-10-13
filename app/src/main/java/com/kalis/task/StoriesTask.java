package com.kalis.task;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Kalis on 1/23/2016.
 */

public class StoriesTask extends AsyncTask<String,Void,Integer>
{
    private int result = -1;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        try{

            String link = params[0];
            BufferedReader reader=null;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line,text="";
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                text+=line;
                SystemClock.sleep(200);
            }
            result =  Integer.valueOf(text.replace("\t",""));

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}