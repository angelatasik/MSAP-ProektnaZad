package com.example.neverendingservice_angela;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtils{
    private  static  final String BASE_URL ="http://10.0.2.2:5000/getjobs/emulator";

    public static String getBookInfo(String queryString) throws IOException {
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String JSONString =null;

        try {
            Uri builtURL= Uri.parse(BASE_URL).buildUpon().build();
            Log.i("angela","connecting to " + builtURL);
            URL requestURL=new URL(builtURL.toString());

            urlConnection=(HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream=urlConnection.getInputStream();
            StringBuilder builder =new StringBuilder();

            if(inputStream==null)
            {
                return null;
            }
            reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line=reader.readLine()) !=null){
                builder.append(line + "\n");
            }
            if(builder.length()==0){
                return null;
            }
            JSONString=builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        Log.i("GetJson", "BackendJson: " + JSONString);
        return JSONString;
    }
}
