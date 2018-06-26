package com.mapquest.navigation.sampleapp.Methods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mapquest.navigation.model.location.Coordinate;
import com.mapquest.navigation.sampleapp.Models.Input;
import com.mapquest.navigation.sampleapp.Models.LatLng;
import com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FetchCloudData extends AsyncTask{

    Context context;
    Coordinate origincord,dstncord;
      long route=0;
      long interval=50000;
      String timezone="";
      long time=0;

    @Override
    protected String doInBackground(Object[] objects) {
          context= (Context) objects[0];
          origincord= (Coordinate) objects[1];
          dstncord =(Coordinate)objects[2];
          route=(long)objects[3];
          interval=(long)objects[4];
          timezone=(String)objects[5];
          time=(long)objects[6];

        try {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mgr.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = null;
                //nbsc-1518068960369.appspot.com
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

                String url="https://ou4ptj7z2b.execute-api.ap-south-1.amazonaws.com/dev";

                HttpPost request = new HttpPost(url);

                Input input=new Input();
                input.setOrigin(new LatLng(((float) origincord.getLatitude()), ((float) origincord.getLongitude())));
                input.setDestination(new LatLng(((float)dstncord.getLatitude()),((float)dstncord.getLongitude())));
                input.setRoute(route);
                input.setInterval(interval);
                input.setTimeZone(timezone);
                input.setTime(time);


                String json =new Gson().toJson(input);
                System.out.println("hre is json :\n"+json);
                StringEntity entity = new StringEntity(json);
                request.setEntity(entity);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");


                BufferedReader rd=null;

                response = client.execute(request);
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line="";
                StringBuilder sb=new StringBuilder();
                while ((line=rd.readLine())!=null){
                    sb.append(line);
                }
                return sb.toString();
            }else{
                return "NoInternet";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



}
