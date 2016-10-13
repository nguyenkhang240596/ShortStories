package com.kalis.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    public static boolean isConnected(Context _context)
    {
        ConnectivityManager conMgr =  (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);


            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            if (netInfo == null)
            {
                return false;
            }
            else
            {
                    return true;
            }
    }
}
