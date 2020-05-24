package com.example.neverendingservice_angela;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Top {
    public static final String URL="http://10.0.2.2:5000/postresults";

    public static void doTop()
    {
        String returnString=null;
        String startResult= "";

        try{
            Process pstat=Runtime.getRuntime().exec("top -n 1");
            BufferedReader in=new BufferedReader(new InputStreamReader(pstat.getInputStream()));
            String inputLine;

            while(returnString==null || returnString.contentEquals("")){
                returnString=in.readLine();
            }
            startResult += returnString + ",";
            while((inputLine = in.readLine()) !=null){
                inputLine += ";";
                startResult += inputLine;
            }
            in.close();
            if(pstat !=null)
            {
                pstat.getOutputStream().close();
                pstat.getInputStream().close();
                pstat.getErrorStream().close();
            }
            Log.i("MakePing", "StartResult =" +startResult);
            postJson.postBackendJson(startResult);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
