package com.mapquest.navigation.sampleapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mapquest.android.commoncore.model.LatLng;
import com.mapquest.navigation.model.location.Coordinate;
import com.mapquest.navigation.model.location.Destination;
import com.mapquest.navigation.sampleapp.R;
import com.mapquest.navigation.sampleapp.location.CurrentLocationProvider;
import com.mapquest.navigation.sampleapp.searchahead.SearchAheadFragment;
import com.mapquest.navigation.sampleapp.searchahead.SearchBarView;

import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.dstnCord;
import static com.mapquest.navigation.sampleapp.activity.RouteSelectionActivity.originCord;

public class SelectPlace extends AppCompatActivity
        implements CurrentLocationProvider, SearchAheadFragment.OnSearchResultSelectedListener{
    private static final String SEARCH_AHEAD_FRAGMENT_TAG = "tag_search_ahead_fragment";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selectplace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    }

    @Nullable
    @Override
    public LatLng getCurrentLocation() {
        return null;
    }

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
        finish();
      //   this.finish();

    }
}
