package com.example.test.gsontest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by test on 8/6/2015.
 */
public class HttpUtil {
    public static String sendHttpRequest(String address){
        HttpURLConnection httpURLConnection = null;
        String response = "";
        try {
            URL url = new URL(address);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(8888);
            httpURLConnection.setConnectTimeout(8888);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }
        return response;
    }
}
