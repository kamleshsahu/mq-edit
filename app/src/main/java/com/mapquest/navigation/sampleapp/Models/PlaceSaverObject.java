package com.mapquest.navigation.sampleapp.Models;

import java.util.ArrayList;

/**
 * Created by sahu on 6/8/2017.
 */

public class PlaceSaverObject {
    ArrayList<MPlace> list;
    public PlaceSaverObject(ArrayList<MPlace> list) {
       this.list=list;
    }

    public ArrayList<MPlace> getList() {
        return list;
    }
}
