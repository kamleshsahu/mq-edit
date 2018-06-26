
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Step_ implements Serializable
{

    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("streets")
    @Expose
    private List<String> streets = null;
    @SerializedName("narrative")
    @Expose
    private String narrative;
    @SerializedName("turnType")
    @Expose
    private String turnType;
    @SerializedName("startPoint")
    @Expose
    private LatLng startPoint;
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("formattedTime")
    @Expose
    private String formattedTime;
    @SerializedName("directionName")
    @Expose
    private String directionName;
    @SerializedName("maneuverNotes")
    @Expose
    private List<Object> maneuverNotes = null;
    @SerializedName("linkIds")
    @Expose
    private List<Object> linkIds = null;
    @SerializedName("signs")
    @Expose
    private List<Sign> signs = null;
    @SerializedName("mapUrl")
    @Expose
    private String mapUrl;
    @SerializedName("transportMode")
    @Expose
    private String transportMode;
    @SerializedName("attributes")
    @Expose
    private String attributes;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("direction")
    @Expose
    private String direction;
    private final static long serialVersionUID = -1381914854734398839L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Step_() {
    }

    /**
     * 
     * @param signs
     * @param index
     * @param maneuverNotes
     * @param direction
     * @param narrative
     * @param iconUrl
     * @param time
     * @param distance
     * @param linkIds
     * @param streets
     * @param attributes
     * @param transportMode
     * @param formattedTime
     * @param directionName
     * @param turnType
     * @param startPoint
     * @param mapUrl
     */
    public Step_(String distance, List<String> streets, String narrative, String turnType,LatLng startPoint, String index, String formattedTime, String directionName, List<Object> maneuverNotes, List<Object> linkIds, List<Sign> signs, String mapUrl, String transportMode, String attributes, String time, String iconUrl, String direction) {
        super();
        this.distance = distance;
        this.streets = streets;
        this.narrative = narrative;
        this.turnType = turnType;
        this.startPoint = startPoint;
        this.index = index;
        this.formattedTime = formattedTime;
        this.directionName = directionName;
        this.maneuverNotes = maneuverNotes;
        this.linkIds = linkIds;
        this.signs = signs;
        this.mapUrl = mapUrl;
        this.transportMode = transportMode;
        this.attributes = attributes;
        this.time = time;
        this.iconUrl = iconUrl;
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<String> getStreets() {
        return streets;
    }

    public void setStreets(List<String> streets) {
        this.streets = streets;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getTurnType() {
        return turnType;
    }

    public void setTurnType(String turnType) {
        this.turnType = turnType;
    }

    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public List<Object> getManeuverNotes() {
        return maneuverNotes;
    }

    public void setManeuverNotes(List<Object> maneuverNotes) {
        this.maneuverNotes = maneuverNotes;
    }

    public List<Object> getLinkIds() {
        return linkIds;
    }

    public void setLinkIds(List<Object> linkIds) {
        this.linkIds = linkIds;
    }

    public List<Sign> getSigns() {
        return signs;
    }

    public void setSigns(List<Sign> signs) {
        this.signs = signs;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
