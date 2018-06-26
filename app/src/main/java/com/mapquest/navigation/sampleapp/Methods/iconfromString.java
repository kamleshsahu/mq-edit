package com.mapquest.navigation.sampleapp.Methods;


import android.content.Context;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapquest.navigation.sampleapp.R;

/**
 * Created by kamlesh on 28-04-2018.
 */

public class iconfromString {

    Icon icon=null;
   
    public iconfromString(IconFactory iconFactory, String name) {

        icon = iconFactory.fromResource(R.drawable.rain);
        switch (name){
            case "clear-day":icon = iconFactory.fromResource(R.drawable.clear_day);
                break;
            case "cloudy":icon = iconFactory.fromResource(R.drawable.cloudy);
                break;
            case "clear-night":icon = iconFactory.fromResource(R.drawable.clear_night);
                break;
            case "fog":icon = iconFactory.fromResource(R.drawable.fog);
                break;
            case "hail":icon = iconFactory.fromResource(R.drawable.hail);
                break;
            case "partly-cloudy-day":icon = iconFactory.fromResource(R.drawable.partly_cloudy_day);
                break;
            case "partly-cloudy-night":icon = iconFactory.fromResource(R.drawable.partly_cloudy_night);
                break;
            case "rain":icon = iconFactory.fromResource(R.drawable.rain);
                break;
            case "sleet":icon = iconFactory.fromResource(R.drawable.sleet);
                break;
            case "snow":icon = iconFactory.fromResource(R.drawable.snow);
                break;
            case "thunderstorm":icon = iconFactory.fromResource(R.drawable.thunderstorm);
                break;
            case "tornado":icon = iconFactory.fromResource(R.drawable.tornado);
                break;
            case "wind":icon = iconFactory.fromResource(R.drawable.wind);
                break;
        }

    }

    public Icon getIcon() {
        return icon;
    }
}
