package com.example.neverendingservice_angela;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.Nullable;


public class RestartServiceBroadcastReceiver extends BroadcastReceiver {

    public  static  final  String TAG=RestartServiceBroadcastReceiver.class.getSimpleName();
    public  static JobScheduler jobScheduler;
    private RestartServiceBroadcastReceiver restartServiceSensorReceiver;

    public static long getVersionCode(Context context)
    {
        PackageInfo packageInfo;
        try{
            packageInfo=context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            long versionCode=System.currentTimeMillis();
            return versionCode;
        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
        return 0;
    }

    @RequiresApi(api=Build.VERSION_CODES.LOLLIPOP)
    private void registerRestartReceiveer(final Context context) {
        if(restartServiceSensorReceiver==null)
        {
            restartServiceSensorReceiver=new RestartServiceBroadcastReceiver();
        }else try
            {
                context.unregisterReceiver(restartServiceSensorReceiver);
            }catch (Exception e){}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Globals.RESTART_INTENT);
                try {
                    context.registerReceiver(restartServiceSensorReceiver, filter);
                } catch (Exception e) {
                    try {
                        context.getApplicationContext().registerReceiver(restartServiceSensorReceiver, filter);
                    } catch (Exception ex) {
                    }
                }
            }
        },1000);
        }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sheduleJob(Context context) {
        if(jobScheduler==null)
        {
            jobScheduler=(JobScheduler) context
                    .getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        ComponentName componentName=new ComponentName(context, JobService.class);
        JobInfo jobInfo=new JobInfo.Builder(1,componentName)
                .setOverrideDeadline(0)
                .setPersisted(true).build();
        jobScheduler.schedule(jobInfo);
    }

    public static void reStartTracker(Context context)
    {
        Log.i(TAG,"Restarting tracker");
        Intent broadcastIntent=new Intent(Globals.RESTART_INTENT);
        context.sendBroadcast(broadcastIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"about to start timer" +context.toString());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            sheduleJob(context);
        }else{
            registerRestartReceiveer(context);
            ProcessMainClass bck=new ProcessMainClass();
            bck.launchService(context);
        }

    }

    public static void scheduleJob(@Nullable Context applicationContext) {

    }
}
