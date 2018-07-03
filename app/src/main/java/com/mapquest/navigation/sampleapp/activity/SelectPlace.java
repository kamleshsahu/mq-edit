package com.mapquest.navigation.sampleapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mapquest.navigation.model.Route;
import com.mapquest.navigation.sampleapp.Adapters.*;

import com.google.gson.Gson;
import com.mapquest.android.commoncore.model.LatLng;
import com.mapquest.navigation.model.location.Coordinate;
import com.mapquest.navigation.model.location.Destination;
import com.mapquest.navigation.sampleapp.Methods.PlaceSaver;
import com.mapquest.navigation.sampleapp.Models.MPlace;
import com.mapquest.navigation.sampleapp.Models.PlaceSaverObject;
import com.mapquest.navigation.sampleapp.R;
//import com.mapquest.navigation.sampleapp.location.CurrentLocationProvider;
import com.mapquest.navigation.sampleapp.searchahead.SearchAheadFragment;
import com.mapquest.navigation.sampleapp.searchahead.SearchBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.dstnCord;
import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.mDestinationLocations;
import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.originCord;
import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.sd;

public class SelectPlace extends AppCompatActivity
        implements
//        CurrentLocationProvider,
        SearchAheadFragment.OnSearchResultSelectedListener{
    private static final String SEARCH_AHEAD_FRAGMENT_TAG = "tag_search_ahead_fragment";
    List<MPlace> recentSearches=new ArrayList<>();
    ListView recent_listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selectplace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recent_listview =findViewById(R.id.listviewRecentSearch);
        recent_listview.setDividerHeight(0);
        SearchBarView searchBarView = toolbar.findViewById(R.id.fake_search_bar_view);
        searchBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchAheadFragment searchAheadFragment = SearchAheadFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                            .add(android.R.id.content, searchAheadFragment, SEARCH_AHEAD_FRAGMENT_TAG)
                            .addToBackStack(null)
                            .commit();
            }
        });


        final Gson gson = new Gson();
        if(!sd.getString("PlaceSaver", "").equals("")) {
            String json1 = sd.getString("PlaceSaver", "");

            PlaceSaverObject obj = gson.fromJson(json1, PlaceSaverObject.class);
            recentSearches = obj.getList();
            Collections.reverse(recentSearches);
        }

        Place_name_listView place_name_listView =new Place_name_listView(getApplicationContext(),recentSearches);
        recent_listview.setAdapter(place_name_listView);
//
        recent_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Object item = arg0.getItemAtPosition(arg2);
                System.out.println(recentSearches.get(arg2).getAdress()+""+recentSearches.get(arg2).getName());

                try {
                    System.out.println("save fn executed....");
                    Thread thread =new Thread(new PlaceSaver(sd,new MPlace(recentSearches.get(arg2).getId(),recentSearches.get(arg2).getName().toString(),recentSearches.get(arg2).getAdress().toString(),recentSearches.get(arg2).getLatLng())));
                    thread.start();
                }catch (Error e){
                    System.out.println("save fn error");
                }
                Toast.makeText(getApplicationContext(),recentSearches.get(arg2).getAdress(),Toast.LENGTH_LONG).show();
                if(getIntent().getIntExtra("data",0) == 0) {
                    RouteSelectionActivity.origin.setText(recentSearches.get(arg2).getAdress());
                    originCord =recentSearches.get(arg2).getLatLng();
                }else {
                    RouteSelectionActivity.dstn.setText(recentSearches.get(arg2).getAdress());
                    dstnCord =new Destination(recentSearches.get(arg2).getLatLng(),recentSearches.get(arg2).getId());
                }
                finish();

            }
        });
    }

//    @Nullable
//    @Override
//    public LatLng getCurrentLocation() {
//        return null;
//    }

    @Override
    public void onSearchResultSelected(String displayName, Coordinate coordinate, String mqId) {
        Toast.makeText(getApplicationContext(),displayName,Toast.LENGTH_LONG).show();
        if(getIntent().getIntExtra("data",0) == 0) {
            RouteSelectionActivity.origin.setText(displayName);
            originCord =coordinate;
        }else {

            RouteSelectionActivity.dstn.setText(displayName);
            dstnCord =new Destination(coordinate,mqId);


        }

        Thread thread =new Thread(new PlaceSaver(sd,new MPlace(mqId,"",displayName,coordinate)));
        thread.start();
        finish();
      //   this.finish();

    }
}
