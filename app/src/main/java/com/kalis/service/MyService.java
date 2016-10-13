package com.kalis.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kalis.notification.Notification;
import com.kalis.shortstories.MainActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Kalis on 1/28/2016.
 */
public class MyService extends Service {
    IBinder mBinder;

    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("destroy", "Service died");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Calendar c = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 18);
        calendar.set(Calendar.SECOND, 0);
        int h1 = c.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar.get(Calendar.HOUR_OF_DAY);
        int m1 = c.get(Calendar.MINUTE);
        int m2 = calendar.get(Calendar.MINUTE);
        int s1 = c.get(Calendar.SECOND);
        int s2 = calendar.get(Calendar.SECOND);

        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.WEDNESDAY && h1 == h2 && m1 == m2 && s1 == s2) {
            addLocalNotification();
        }

        return START_STICKY;
    }

    private void addLocalNotification() {
//


        if (!isAppIsInBackground()) {
            Intent myIntent = new Intent(getBaseContext(), Notification.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), MainActivity.REQUEST_CODE, myIntent, 0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
//        am.setRepeating(am.RTC_WAKEUP, System.currentTimeMillis(), (((am.INTERVAL_DAY)/24)/60)/25, pendingIntent);
        }

    }

    private boolean isAppIsInBackground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals("com.kalis.englishshortstories")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
