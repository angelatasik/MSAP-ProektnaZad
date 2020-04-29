package com.example.neverendingservice_angela;

import android.os.AsyncTask;
import android.util.Log;

import com.example.neverendingservice_angela.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleAsyncTask extends  AsyncTask<Void,Void,String>{

   public static String pingResult;

    @Override
    protected String doInBackground(Void... voids) {
//        Random r=new Random();
//        int n=r.nextInt(11);
//
//        int s=n*200;

//        try{
//            Thread.sleep(s);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        try{
            String data= NetworkUtils.getBookInfo("");
            Log.i("angela", data);
            JSONArray itemsArray=new JSONArray(data);

            int i=0;
            String title=null;
            String authors=null;

            while (i<itemsArray.length()){
                JSONObject book=itemsArray.getJSONObject(i);
                Log.i("angela" , "json=" + book.toString());
                title=book.getString("jobType");
                Log.i("angela","type=" +title);
                String host=book.getString("hoast");
                Log.i("angela" , "host=" + host);
                String count=book.getString("count");
                Log.i("angela" , "count=" + count);
                String packetSize=book.getString("packetSize");
                Log.i("angela","packetSize=" + packetSize);

                String pingCmd="ping-c" + count;
                pingCmd=pingCmd + "-s" + packetSize;
                pingCmd=pingCmd+ "" + host;

//                String pingResult="";
                Process pping=Runtime.getRuntime().exec(pingCmd);
                BufferedReader in=new BufferedReader(new InputStreamReader(pping.getInputStream()));
                String inputLine;
                while((inputLine = in.readLine()) !=null){
                    pingResult +=inputLine;
                }
                in.close();
                if(pping!=null){
                    pping.getOutputStream().close();
                    pping.getInputStream().close();
                    pping.getErrorStream().close();
                }
                Log.i("angela" , "pingResult=" + pingResult);
            }


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return pingResult;
    }
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
