package com.mapquest.navigation.sampleapp.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mapquest.navigation.sampleapp.Models.Step;
import com.mapquest.navigation.sampleapp.R;

import java.util.List;
import java.util.Locale;


/**
 * Created by RAJA on 18-12-2017.
 */

public class DragupListAdapter extends RecyclerView.Adapter<DragupListAdapter.PnrViewHolder>{

    private Context context;
    private List<Step> mSteps;
    public DragupListAdapter(Context context, List<Step> mSteps){
        this.context=context;
        this.mSteps=mSteps;
    }



    @Override
    public PnrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.dragup_list_layout,parent,false);
        return new PnrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PnrViewHolder holder, int position) {
        Step mStep =mSteps.get(position);
       // Glide.with(holder.image.getContext()).load(passengerList.getLink()).into(holder.image);



            holder.instr.setText(mStep.getStep().getNarrative());

        try {
            holder.distance.setText(String.format("%.2f", (float) mStep.getAft_distance() / (float) 1000 * (0.621371)) + " miles");
            holder.arrtime.setText(mStep.getArrtime());
            holder.weather.setText(mStep.getWlist().getSummary());
            holder.temp.setText(mStep.getWlist().getTemperature() + "°F");
            holder.stepLength.setText( mStep.getStep().getDistance()+ " miles");

            Drawable icon = context.getResources().getDrawable( R.drawable.clear_day );


            switch (mStep.getWlist().getIcon()){
                case "clear-day":icon = context.getResources().getDrawable(R.drawable.clear_day);
                    break;
                case "cloudy":icon = context.getResources().getDrawable(R.drawable.cloudy);
                    break;
                case "clear-night":icon = context.getResources().getDrawable(R.drawable.clear_night);
                    break;
                case "fog":icon = context.getResources().getDrawable(R.drawable.fog);
                    break;
                case "hail":icon = context.getResources().getDrawable(R.drawable.hail);
                    break;
                case "partly-cloudy-day":icon = context.getResources().getDrawable(R.drawable.partly_cloudy_day);
                    break;
                case "partly-cloudy-night":icon = context.getResources().getDrawable(R.drawable.partly_cloudy_night);
                    break;
                case "rain":icon = context.getResources().getDrawable(R.drawable.rain);
                    break;
                case "sleet":icon = context.getResources().getDrawable(R.drawable.sleet);
                    break;
                case "snow":icon = context.getResources().getDrawable(R.drawable.snow);
                    break;
                case "thunderstorm":icon = context.getResources().getDrawable(R.drawable.thunderstorm);
                    break;
                case "tornado":icon = context.getResources().getDrawable(R.drawable.tornado);
                    break;
                case "wind":icon = context.getResources().getDrawable(R.drawable.wind);
                    break;
            }
            Glide.with(context)
                    //  .load("http://openweathermap.org/img/w/"+itemslist.get(position).weather.get(0).icon+".png")
                    .load(icon)
                    //     .override(100,100)
                    .into(holder.weatherimg);
        //    System.out.println(mStep.getStep().getStart_location().getLat());
         //   System.out.println(mStep.getStep().getStart_location().getLng());


  //          String address = new Geocoder(context, Locale.ENGLISH).getFromLocation(mStep.getStep().getStartPoint().getLat(), mStep.getStep().getStartPoint().getLng(), 1).get(0).getAddressLine(0);
            //  System.out.println("hre is address :"+address);
 //           holder.address.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

   @Override
    public int getItemCount() {
      return mSteps.size();
    }

    public class PnrViewHolder extends RecyclerView.ViewHolder {



        TextView instr,distance,arrtime,temp,weather,stepLength,address;
        ImageView weatherimg;
        public PnrViewHolder(View itemView) {
            super(itemView);
            instr= (TextView) itemView.findViewById(R.id.instr);
            weather= (TextView) itemView.findViewById(R.id.weather);
            temp= (TextView) itemView.findViewById(R.id.temp);
            distance= (TextView) itemView.findViewById(R.id.distance);
            arrtime= (TextView) itemView.findViewById(R.id.arrtime);
            weatherimg=itemView.findViewById(R.id.weatherImg);
            stepLength=itemView.findViewById(R.id.stepLength);
         //   address=itemView.findViewById(R.id.address);
        }
    }
}
