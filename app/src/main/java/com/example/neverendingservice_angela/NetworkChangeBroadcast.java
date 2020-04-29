package com.example.neverendingservice_angela;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeBroadcast extends BroadcastReceiver {
    public void onReceive(final Context  context, final Intent intent)
    {
        NetworkCheck network=new NetworkCheck(context);

        if(network.NetworkCheck()==true)
        {
            Toast.makeText(context,"NETWORK_CHANGE_BROAD: Connected",Toast.LENGTH_SHORT).show();
            Log.i("NETWORK_CHANGE_BROAD","Connected");
        }else if(network.NetworkCheck()==false)
        {
            Toast.makeText(context,"NETWORK_CHANGE_BROAD: Disconncected",Toast.LENGTH_SHORT).show();
            Log.i("NETWORK_CHANGE_BROAD","Dissconnected");
        }
    }
}
