package com.mapquest.navigation.sampleapp.Methods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FetchCloudData extends AsyncTask{

    Context context;



    @Override
    protected Object doInBackground(Object[] objects) {
          context= (Context) objects[0];


        try {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mgr.getActiveNetworkInfo();

//            if (netInfo != null && netInfo.isConnected()) {
//                HttpClient client = new DefaultHttpClient();
//                HttpResponse response = null;
//                //nbsc-1518068960369.appspot.com
//                System.out.println("https://nbsc-1518068960369.appspot.com/_ah/api/darksky/v1/wdata?"
//                        +"olat="+origin.latitude+"&olng="+origin.longitude
//                        +"&dlat="+destination.latitude+"&dlng="+destination.longitude
//                        +"&route="+selectedroute
//                        +"&interval="+interval
//                        +"&tz="+timezone
//                        +"&jstime="+(jstart_date_millis+jstart_time_millis));
//                HttpGet request=new HttpGet("https://nbsc-1518068960369.appspot.com/_ah/api/darksky/v1/wdata?" +
//                        "olat="+origin.latitude +
//                        "&olng="+origin.longitude +
//                        "&dlat="+destination.latitude +
//                        "&dlng="+destination.longitude +
//                        "&route="+selectedroute +
//                        "&interval="+interval +
//                        "&tz=" +timezone.replace("/","%2F") +
//                        "&jstime="+(jstart_date_millis+jstart_time_millis)
//                );
//                BufferedReader rd=null;
//
//                response = client.execute(request);
//                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                String line="";
//                StringBuilder sb=new StringBuilder();
//                while ((line=rd.readLine())!=null){
//                    sb.append(line);
//                }
//                return sb.toString();
//            }else{
//                return "NoInternet";
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



}
