
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mapquest.navigation.sampleapp.Models.LatLng;

public class Input implements Serializable
{

    @SerializedName("origin")
    @Expose
    private LatLng origin;
    @SerializedName("destination")
    @Expose
    private LatLng destination;
    @SerializedName("route")
    @Expose
    private Long route;
    @SerializedName("interval")
    @Expose
    private Long interval;
    @SerializedName("timeZone")
    @Expose
    private String timeZone;
    @SerializedName("time")
    @Expose
    private Long time;
    private final static long serialVersionUID = -784466641420971157L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Input() {
    }

    /**
     * 
     * @param time
     * @param interval
     * @param route
     * @param origin
     * @param timeZone
     * @param destination
     */
    public Input(LatLng origin, LatLng destination, Long route, Long interval, String timeZone, Long time) {
        super();
        this.origin = origin;
        this.destination = destination;
        this.route = route;
        this.interval = interval;
        this.time = time;
        if(timeZone.contains("/")){
            this.timeZone = timeZone.replace("/",".");
        }else{
            this.timeZone=timeZone;
        }
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public Long getRoute() {
        return route;
    }

    public void setRoute(Long route) {
        this.route = route;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        if(timeZone.contains("/")){
            this.timeZone = timeZone.replace("/",".");
        }else{
            this.timeZone=timeZone;
        }
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }



}
