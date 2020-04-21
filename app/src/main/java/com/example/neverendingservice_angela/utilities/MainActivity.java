package com.example.neverendingservice_angela.utilities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.example.neverendingservice_angela.R;
import com.example.neverendingservice_angela.restarter.RestartServiceBroadcastReceiver;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
    }


}