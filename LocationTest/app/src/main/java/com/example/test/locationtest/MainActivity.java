package com.example.test.locationtest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private String provider;
    private LocationManager locationManager;
    private static final int SHOW_RESPONSE = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_RESPONSE){
                String getResponse = (String) msg.obj;
                textView.setText(getResponse);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.show_location_textView);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        List<String> location_service = locationManager.getProviders(true);
        if (location_service.contains(LocationManager.GPS_PROVIDER)){
            provider = LocationManager.GPS_PROVIDER;
        }else if (location_service.contains(LocationManager.NETWORK_PROVIDER)){
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            Toast.makeText(this, "nothing at all", Toast.LENGTH_LONG ).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null){
            showLocation(location);
        }
        locationManager.requestLocationUpdates(provider, 5000, 1,locationListener);
    }

    private void showLocation(final Location location) {
       /* String currentLocation = "latitude is " + location.getLatitude() + "\n"
                + "longitude is " + location.getLongitude();
        textView.setText(currentLocation);*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    StringBuilder urlBuilder = new StringBuilder();
                    urlBuilder.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
                    urlBuilder.append(location.getLatitude());
                    urlBuilder.append(",");
                    urlBuilder.append(location.getLongitude());
                    urlBuilder.append("&sensor=false");
                    String webSiteUrl = urlBuilder.toString();
                    URL url = new URL(webSiteUrl);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setReadTimeout(8888);
                    httpURLConnection.setConnectTimeout(9999);
                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.addRequestProperty("Accept-Language", "zh-CN");
                    httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null){
                        response.append(line);
                    }
                    String new_responese = response.toString();
                    JSONObject jsonObject = new JSONObject(new_responese);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        JSONObject subJSONObject = jsonArray.getJSONObject(0);
                        String address = subJSONObject.getString("formatted_address");

                        Message message = new Message();
                        message.what = SHOW_RESPONSE;
                        message.obj = address;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
