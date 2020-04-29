package com.example.neverendingservice_angela;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.security.Provider;

public class ProcessMainClass {
    public static Intent serviceIntent = null;
    public static final String TAG = ProcessMainClass.class.getSimpleName();

    public ProcessMainClass() {

    }

    public void setServiceIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, Provider.Service.class);
        }
    }

    public void launchService(Context context) {
        if (context == null) {
            return;
        }
        setServiceIntent(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
        Log.d(TAG, "ProcessMainClass: start service goo!!!");
    }
}
