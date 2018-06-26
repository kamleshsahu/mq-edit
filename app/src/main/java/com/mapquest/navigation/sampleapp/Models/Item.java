
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable
{

    @SerializedName("point")
    @Expose
    private Point point;
    @SerializedName("wlist")
    @Expose
    private Wlist wlist;
    @SerializedName("arrtime")
    @Expose
    private String arrtime;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("lname")
    @Expose
    private String lname;
    private final static long serialVersionUID = 9016147734475667393L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Item() {
    }

    /**
     * 
     * @param point
     * @param lname
     * @param distance
     * @param arrtime
     * @param wlist
     */
    public Item(Point point, Wlist wlist, String arrtime, String distance, String lname) {
        super();
        this.point = point;
        this.wlist = wlist;
        this.arrtime = arrtime;
        this.distance = distance;
        this.lname = lname;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Wlist getWlist() {
        return wlist;
    }

    public void setWlist(Wlist wlist) {
        this.wlist = wlist;
    }

    public String getArrtime() {
        return arrtime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

}
