
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartPoint implements Serializable
{

    @SerializedName("lng")
    @Expose
    private Float lng;
    @SerializedName("lat")
    @Expose
    private Float lat;
    private final static long serialVersionUID = 4251567724343703267L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StartPoint() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public StartPoint(Float lng, Float lat) {
        super();
        this.lng = lng;
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

}
