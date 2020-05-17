package com.example.neverendingservice_angela;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleAsyncTask extends  AsyncTask<Void,Void,String> {

    public static String pingResult;
    public NetworkCheck networkCheck;
    private SharedPreferences prefs;
    public Context mContext;

    public SimpleAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        networkCheck = new NetworkCheck(mContext.getApplicationContext());

        try {
            String data = NetworkUtils.getBookInfo("");
            Log.i("angela", data);
            JSONArray itemsArray = new JSONArray(data);

            int i = 0;
            String title = null;
            String authors = null;

            while (i < itemsArray.length()) {
                JSONObject book = itemsArray.getJSONObject(i);
                Log.i("angela", "json=" + book.toString());
                title = book.getString("jobType");
                Log.i("angela", "type=" + title);
                String host = book.getString("hoast");
                Log.i("angela", "host=" + host);
                String count = book.getString("count");
                Log.i("angela", "count=" + count);
                int packetSize = book.getInt("packetSize");
                Log.i("angela", "packetSize=" + packetSize);
                int JobPeriod = book.getInt("jobPeriod");
                Log.i("angela", "JobPeriod" + JobPeriod);

                for (int j = 0; j < (int) (600 / JobPeriod); j++) {
                    makePing(host, count, packetSize);

                    try {
                        Thread.sleep(JobPeriod * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    public String makePing(String host, String count, int packetSize) {
        try {
            String pingCmd = "ping-c" + count;
            pingCmd = pingCmd + "-s" + packetSize;
            pingCmd = pingCmd + "" + host;

            Process pping = Runtime.getRuntime().exec(pingCmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(pping.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                pingResult += inputLine;
            }
            in.close();
            prefs = mContext.getSharedPreferences("com.example.neverendingservice_angela.ActiveServiceRunning", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if (networkCheck.NetworkCheck()) {
                postJson.postBackendJson(pingResult);
                if (prefs.getString("result1", null) != null) {
                    postJson.postBackendJson(prefs.getString("result1", null));
                    editor.putString("result1", null);
                }
                if (prefs.getString("result2", null) != null) {
                    postJson.postBackendJson(prefs.getString("result2", null));
                    editor.putString("result2", null);
                } else {
                    if ((prefs.getString("result1", null) != null) && (prefs.getString("result1", null) != null)
                            || (prefs.getString("result1", null) == null) && (prefs.getString("result1", null) == null)) {
                        editor.putString("result1", pingResult);
                    } else {
                        editor.putString("result2", pingResult);
                    }
                    editor.apply();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pingResult;
    }
}


