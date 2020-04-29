package com.example.neverendingservice_angela;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {
    public boolean connection;
    public Context mcontext;

    public NetworkCheck(Context context)
    {
        mcontext=context;
    }

    public boolean NetworkCheck(){
        ConnectivityManager connMgr=(ConnectivityManager) mcontext.getSystemService(mcontext.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        if(networkInfo!=null)
        {
            connection=true;
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                connection=true;
            }else if(networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                connection=true;
            }
        }else {
            connection=false;
        }
        return connection;

    }

}
