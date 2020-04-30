package com.example.neverendingservice_angela;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Service extends android.app.Service {
    protected static final int NOTIFICATION_ID =1337;
    private static String TAG="Servcie";
    private static Service mCurrentService;
    public Context context;
    public NetworkCheck networkCheck;

    public int pingNumb=0;
    private int counter=0;
    private final static int INTERVAL=60*1000*10;

    public Service(){
        super();
    }

    public void onCrate(){
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            restartForeground();
        }
        mCurrentService=this;
    }
    public int onStartCommand(Intent intent, int flags,int startId){
        super.onStartCommand(intent,flags,startId);
        Log.d(TAG,"Restart Service!!");
//        counter=0;
        networkCheck=new NetworkCheck(this);
        boolean network=networkCheck.NetworkCheck();
        if(network){
            Log.i("angela","Connected to the internet");
            taskDelauLoop();
        }else{
            Log.i("angela","Not connected to the internet");
        }
        Log.i("angela","The onStartCommand() is called");

        SharedPreferences prefs=getSharedPreferences("com.example.neverendingservice_angela.utilities.ActiveSeviceRunnning" , MODE_PRIVATE);

        if(prefs.getInt("TimeCounter:",0)!=0){
            counter=prefs.getInt("counter" ,0);
        }

        if(intent==null){
            ProcessMainClass bck=new ProcessMainClass();
            bck.launchService(this);
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            restartForeground();
        }
        startTimer();

        return START_STICKY;
    }

    private void taskDelauLoop() {
        final Handler handler=new Handler();
        new SimpleAsyncTask().execute();
        new Runnable(){
            @Override
            public void run() {
                handler.postDelayed(this,INTERVAL);
                Log.i("pingNumb:", " "+pingNumb++);
                new SimpleAsyncTask().execute();

            }
        }.run();
    }

    public IBinder onBind(Intent intent){
        return  null;
    }

    public void restartForeground(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "resarting foreground successful");
        try{
            Notification notification=new Notification();
            startForeground(NOTIFICATION_ID,notification.setNotification(this,"Setvice notification","This is the service's notification", R.drawable.ic_sleep));
            Log.i(TAG,"restarting foreground succesfull");
            startTimer();
        }catch (Exception e){
            Log.e(TAG,"Error in notification" + e.getMessage());
        }
        }
    }

    private void startTimer() {
        Log.i(TAG,"Starting timer");
//        new SimpleAsyncTask().execute();
        stoptimertask();
        timer=new Timer();
        initalizerTimerTask();
        Log.i(TAG,"Scheduling");
        timer.schedule(timerTask,1000,1000);
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestroy called");
        Intent broadcastIntent=new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }
    public  void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        Log.i(TAG,"onTaskRemoved called");
        Intent broadcastIntent=new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
    }

    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime=0;


    public void  initalizerTimerTask(){
        Log.i(TAG,"initalising TimerTask");
        timerTask=new TimerTask() {
            @Override
            public void run() {
                Log.i("in timer ", "in timer +++++" + (counter++));
            }
        };
    }
    public void stoptimertask(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }
//    public static Service getmCurrentService(){
//        return mCurrentService;
//    }
//    public static void setmCurrentService(Service mCurrentService){
//        Service.mCurrentService=mCurrentService;
//    }
}
