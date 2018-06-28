
package com.mapquest.navigation.sampleapp.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sign implements Serializable
{

    @SerializedName("extraText")
    @Expose
    private String extraText;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("type")
    @Expose
    private Long type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("direction")
    @Expose
    private Long direction;
    private final static long serialVersionUID = -1522765674300964609L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Sign() {
    }

    /**
     * 
     * @param text
     * @param extraText
     * @param direction
     * @param type
     * @param url
     */
    public Sign(String extraText, String text, Long type, String url, Long direction) {
        super();
        this.extraText = extraText;
        this.text = text;
        this.type = type;
        this.url = url;
        this.direction = direction;
    }

    public String getExtraText() {
        return extraText;
    }

    public void setExtraText(String extraText) {
        this.extraText = extraText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDirection() {
        return direction;
    }

    public void setDirection(Long direction) {
        this.direction = direction;
    }

}
