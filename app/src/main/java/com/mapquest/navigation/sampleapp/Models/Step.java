
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step implements Serializable
{

    @SerializedName("arrtime")
    @Expose
    private String arrtime;
    @SerializedName("steplength")
    @Expose
    private String steplength;
    @SerializedName("step")
    @Expose
    private Step_ step;
    @SerializedName("aft_distance")
    @Expose
    private Float aft_distance;
    @SerializedName("wlist")
    @Expose
    private Wlist_ wlist;
    private final static long serialVersionUID = -1800829508257657767L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step() {
    }

    /**
     * 
     * @param steplength
     * @param arrtime
     * @param aft_distance
     * @param wlist
     * @param step
     */
    public Step(String arrtime, String steplength, Step_ step, Float aft_distance, Wlist_ wlist) {
        super();
        this.arrtime = arrtime;
        this.steplength = steplength;
        this.step = step;
        this.aft_distance = aft_distance;
        this.wlist = wlist;
    }

    public String getArrtime() {
        return arrtime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }

    public String getSteplength() {
        return steplength;
    }

    public void setSteplength(String steplength) {
        this.steplength = steplength;
    }

    public Step_ getStep() {
        return step;
    }

    public void setStep(Step_ step) {
        this.step = step;
    }

    public Float getAft_distance() {
        return aft_distance;
    }

    public void setAft_distance(Float aft_distance) {
        this.aft_distance = aft_distance;
    }

    public Wlist_ getWlist() {
        return wlist;
    }

    public void setWlist(Wlist_ wlist) {
        this.wlist = wlist;
    }

}
