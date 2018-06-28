
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Output implements Serializable
{

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    private final static long serialVersionUID = -7289965944423689676L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Output() {
    }

    /**
     * 
     * @param items
     * @param steps
     */
    public Output(List<Item> items, List<Step> steps) {
        super();
        this.items = items;
        this.steps = steps;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

}
