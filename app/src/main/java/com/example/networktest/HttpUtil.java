package com.example.networktest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendHttpRequest(final String address,final HttpCalllbackListener httpCalllbackListener){
        new Thread(() -> {
            HttpURLConnection connection=null;
            try {
                URL url=new URL(address);
                connection=(HttpURLConnection)url.openConnection();
                connection.setReadTimeout(8000);
                connection.setConnectTimeout(8000);
                InputStream in = connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                StringBuilder data=new StringBuilder();
                String line;
                if ((line=reader.readLine())!=null){
                    data.append(line);
                }
                if (httpCalllbackListener!=null){
                    httpCalllbackListener.onFinsh(data.toString());
                }
            } catch (Exception e) {
               if (httpCalllbackListener!=null){
                   httpCalllbackListener.onError(e);
               }
            }finally {
                if (connection!=null){
                    connection.disconnect();
                }
            }
        }).start();


    }
    public static void sendOKHttpRequest(final String address,okhttp3.Callback callback){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request=new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
