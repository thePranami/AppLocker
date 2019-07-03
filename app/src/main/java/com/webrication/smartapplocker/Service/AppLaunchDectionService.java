package com.webrication.smartapplocker.Service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.webrication.smartapplocker.other.Constants;
import com.webrication.smartapplocker.Database.LockPackageDatabase;
import com.webrication.smartapplocker.other.PrefrancesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by pc on 2/1/2018.
 */

public class AppLaunchDectionService extends Service
{
    public static final String EXTRA_PACKAGE = "package";
    LockPackageDatabase databaseHandler;
    private boolean isAppRunning = false;
    private String currentActivity="";

    ArrayList<String> list=new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

            scheduleMethod();
        databaseHandler=new LockPackageDatabase(getApplicationContext());

       return  START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

            NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Smart AppLock")
                    .setContentText("Services is Running")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);
            Notification notification = builder.build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }

    private void scheduleMethod() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkRunningApps();
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    public void checkRunningApps()
    {
         String activityOnTop = getProcessName(this);

            if (!activityOnTop.equals("") && !activityOnTop.contains("com.google.android.gsf") && !activityOnTop.equals("com.vspl.smartapplocker")) {

                if (databaseHandler.getpack_name(activityOnTop))
                {
                    if (!activityOnTop.equals(currentActivity) && !isAppRunning)
                    {
                        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                        //am.killBackgroundProcesses(activityOnTop);

                        String type = PrefrancesUtils.getLockType();

                        if (type.equalsIgnoreCase("pin")) {

                            startService(new Intent(AppLaunchDectionService.this,PinLockService.class));
                            isAppRunning = true;
                            currentActivity = activityOnTop;

                        } else if (type.equalsIgnoreCase("pattern")) {

                            startService(new Intent(AppLaunchDectionService.this,PatternLockService.class));
                            isAppRunning = true;
                            currentActivity = activityOnTop;

                        } else if (type.equalsIgnoreCase("gesture")) {

                            startService(new Intent(AppLaunchDectionService.this,GestureLockService.class));
                            isAppRunning = true;
                            currentActivity = activityOnTop;
                        }
                    }
                }
                else {

                    isAppRunning = false;
                    currentActivity = "";
                }
            }
    }

    public static String getProcessName(Context context)
    {
        String foregroundProcess = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    String topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                       foregroundProcess = topPackageName;
                }
            }
        } else {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            @SuppressWarnings("deprecation") ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            foregroundProcess = foregroundTaskInfo.topActivity.getPackageName();

                foregroundProcess = foregroundTaskInfo.topActivity.getPackageName();
        }
        Log.i("packageName", foregroundProcess + "");

        return foregroundProcess;
    }

    public void getAllData()
    {
        Cursor cursor=databaseHandler.getAllData();

        if (cursor!=null && cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                list.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d("task", "TASK REMOVED");

        Intent intent = new Intent(getApplicationContext(), AppLaunchDectionService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 500, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.d("uncaut", "Uncaught exception start!");
            ex.printStackTrace();

            //Same as done in onTaskRemoved()
            PendingIntent service = PendingIntent.getService(
                    getApplicationContext(),
                    1001,
                    new Intent(getApplicationContext(),AppLaunchDectionService.class),
                    PendingIntent.FLAG_ONE_SHOT);
        }
    };
}
