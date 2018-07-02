package com.mapquest.navigation.sampleapp.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.RoutePolylinePresenter;
import com.mapquest.navigation.dataclient.RouteService;
import com.mapquest.navigation.dataclient.listener.RoutesResponseListener;
import com.mapquest.navigation.internal.ShapeCalculator;
import com.mapquest.navigation.internal.dataclient.NavigationRouteServiceFactory;
import com.mapquest.navigation.internal.util.LogUtil;
import com.mapquest.navigation.location.LocationListener;
import com.mapquest.navigation.location.LocationProviderAdapter;
import com.mapquest.navigation.model.CongestionSpan;
import com.mapquest.navigation.model.Route;
import com.mapquest.navigation.model.RouteLeg;
import com.mapquest.navigation.model.RouteOptions;
import com.mapquest.navigation.model.SystemOfMeasurement;
import com.mapquest.navigation.model.location.Coordinate;
import com.mapquest.navigation.model.location.Destination;
import com.mapquest.navigation.sampleapp.Adapters.DragupListAdapter;
import com.mapquest.navigation.sampleapp.BuildConfig;
import com.mapquest.navigation.sampleapp.MQNavigationSampleApplication;
import com.mapquest.navigation.sampleapp.Methods.FetchCloudData;
import com.mapquest.navigation.sampleapp.Methods.iconfromString;
import com.mapquest.navigation.sampleapp.Models.Input;
import com.mapquest.navigation.sampleapp.Models.Item;
import com.mapquest.navigation.sampleapp.Models.Output;
import com.mapquest.navigation.sampleapp.Models.Step;
import com.mapquest.navigation.sampleapp.R;
//import com.mapquest.navigation.sampleapp.location.CurrentLocationProvider;
import com.mapquest.navigation.sampleapp.searchahead.SearchAheadFragment;
import com.mapquest.navigation.sampleapp.searchahead.SearchBarView;
import com.mapquest.navigation.sampleapp.service.NavigationNotificationService;
import com.mapquest.navigation.sampleapp.util.IabBroadcastReceiver;
import com.mapquest.navigation.sampleapp.util.IabHelper;
import com.mapquest.navigation.sampleapp.util.IabResult;
import com.mapquest.navigation.sampleapp.util.Inventory;
import com.mapquest.navigation.sampleapp.util.LocationUtil;
import com.mapquest.navigation.sampleapp.util.Purchase;
import com.mapquest.navigation.util.ShapeSegmenter;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.vipul.hp_hp.library.Layout_to_Image;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.trialy.library.Trialy;
import io.trialy.library.TrialyCallback;

import static com.mapquest.navigation.sampleapp.util.ColorUtil.getCongestionColor;
import static com.mapquest.navigation.sampleapp.util.ColorUtil.setOpacity;
import static com.mapquest.navigation.sampleapp.util.MapUtils.toMapQuestLatLng;
import static com.mapquest.navigation.sampleapp.util.UiUtil.buildDownArrowMarkerOptions;
import static com.mapquest.navigation.sampleapp.util.UiUtil.toast;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_ENDED;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_NOT_YET_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_OVER;
import static io.trialy.library.Constants.STATUS_TRIAL_RUNNING;

public class RouteSelectionActivity extends AppCompatActivity
        implements
//        CurrentLocationProvider
         SearchAheadFragment.OnSearchResultSelectedListener
        , IabBroadcastReceiver.IabBroadcastListener{

    private static final String TAG = LogUtil.generateLoggingTag(RouteSelectionActivity.class);

    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private static final float DEFAULT_ZOOM_LEVEL = 13;
    private static final float CENTER_ON_USER_ZOOM_LEVEL = 16;

    private static final float DEFAULT_ROUTE_WIDTH = 5;
    private static final float SELECTED_ROUTE_WIDTH = 10;
    private static final float DEFAULT_ROUTE_OPACITY = 0.25f;
    private static final float SELECTED_ROUTE_OPACITY = 1.00f;

    private static final String SEARCH_AHEAD_FRAGMENT_TAG = "tag_search_ahead_fragment";

    private static final String SHARED_PREFERENCE_NAME = "com.kamlesh.mapquest.activity.RouteSelectionActivity";
    private static final String USER_TRACKING_CONSENT_KEY = "user_tracking_consent";

    static SharedPreferences sd;
    @BindView(R.id.start)
    protected Button mStartButton;

    @BindView(R.id.retrieve_routes)
    protected Button mRetrieveRoutesButton;

//    @BindView(R.id.clear_routes)
//    protected FloatingActionButton mClearRoutesButton;

//    @BindView(R.id.route_name_text_view)
//    protected TextView mRouteNameTextView;

    @BindView(R.id.map)
    protected MapView mMap;

    private MapboxMap mMapController;
    private RoutePolylinePresenter mRoutePolylinePresenter;
    private MapboxMap.OnMapLongClickListener mMapLongClickListener;
    private ProgressDialog mRoutingDialog;

 //   private com.mapquest.navigation.model.location.Location mLastLocation;
    private LocationPermissionsResultListener locationPermissionsResultListener;

//    @BindView(R.id.gps_center_on_user_location_button)
//    protected FloatingActionButton mGpsCenterOnUserLocationButton;
//
//    @OnClick(R.id.gps_center_on_user_location_button)
//    protected void centerOnUserLocation() {
//        if (hasLocationPermissions()) {
//            if (mLastLocation != null) {
//                mMapController.moveCamera(CameraUpdateFactory.newLatLngZoom(toLatLng(mLastLocation), CENTER_ON_USER_ZOOM_LEVEL));
//            } else {
//                Toast.makeText(this, R.string.no_location, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private Coordinate mStartingCoordinate;
   static  List<Destination> mDestinationLocations = new ArrayList<>();

    private Marker mOriginMarker;
    private List<Marker> mDestinationMarkers = new ArrayList<>();

    private RouteService mRouteService;
    private Map<Route, List<PolylineOptions>> mRoutePolylineOptionsListByRoute = new HashMap<>();
    private Route mSelectedRoute;

    private MQNavigationSampleApplication mApp;
    private LocationListener mFollowUserLocationListener;

    private Float mMapExtentPaddingTop = null;
    private Float mMapExtentPaddingBottom = null;

    static TextView origin;
    static TextView dstn;
    static TextView Go;
    static Coordinate originCord;
    static Destination dstnCord;
    DatePicker datePicker;
    TimePicker timePicker;
    RelativeLayout datePickerCV,timePickerCV;

    TextView date;
    TextView time;
    long route=0;
    long interval=50000;
    String timezone="";
 

    static SlidingUpPanelLayout slidingUpPanelLayout;
    static RecyclerView link;

    static String TRIALY_APP_KEY = "Z3P6OFOXJYPFGZHIVEX"; //TODO: Replace with your app key, which can be found on your Trialy developer dashboard
    static String TRIALY_SKU = "t2_test"; //TODO: Replace with a trial SKU, which can be found on your Trialy developer dashboard. Each app can have multiple trials
    static String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwv07/4K0h67uPlJLcYW0cH70JfX2Ac8IX2aQzjIi9mR42RD18jhlt9wnNhCwasuMT5eOFDEOYM7mHgEWqO5TpPKAsx9peImuX8xdEftZkuMs3xxsOl1mUQAdkgm/siYv0CKpx2mIfaFKT8moAvjogPMrgHwwcSGTRmgLoZPTeRYqImwoDMk22LX7agstU9AoBFo3XSLpKCWdNbFvYtb8oMDPQsfbrsf45a3ZEA6zmYRwQt+zzVaOErqgyxqwFQ8vT54vEQ5nf3FNOUBwBoP+a39tEDFtrLLPBNWhbgym0hj1AYJsTJoTqXR5grfqmCkDdRCAVUQ/x2GqZXHvhtYMdwIDAQAB" ;

    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    Trialy mTrialy;

    String SKU_INFINITE_GAS_MONTHLY = "infinite_gas_monthly";
    String SKU_INFINITE_GAS_QUATERLY = "quaterly";
    String SKU_INFINITE_GAS_HALFYEARLY = "halfyearly";
    String SKU_INFINITE_GAS_YEARLY = "infinite_gas_yearly";

    boolean mSubscribedToInfiniteGas = true;
    boolean mAutoRenewEnabled = false;
    static boolean havetrial=true;
    String mInfiniteGasSku = "";

    final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    static long jstart_time_millis;
    int day,month,year,hour,min;
     ExpandableLayout expandableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        mTrialy = new Trialy(this, TRIALY_APP_KEY);
        mTrialy.checkTrial(TRIALY_SKU, mTrialyCallback);
//Expandable view...................................................................................

        expandableLayout = findViewById(R.id.expandable_layout);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                expandableLayout.expand();
            }
        }, 700);

        findViewById(R.id.exp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout.toggle();
            }
        });
//..................................................................................................

//Date and Time Picker..............................................................................
        datePicker=findViewById(R.id.datepicker);
        timePicker=findViewById(R.id.timepicker);
        datePickerCV=findViewById(R.id.datepickerCV);
        timePickerCV=findViewById(R.id.timepickerCV);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);

        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth() + 1;
        year = datePicker.getYear();



        date.setText(","+day+" "+MONTH[month]+" "+String.valueOf(year).replace("20",""));

        hour = timePicker.getCurrentHour();
        min = timePicker.getCurrentMinute();
        String minute = null;

        if (min<10){
            minute="0"+String.valueOf(min);

            time.setText(hour+":"+minute);
        }else
            time.setText(hour+":"+min);

        final Calendar c = Calendar.getInstance();
        timezone=c.getTimeZone().getID();
        if(timezone.contains("/")){
            timezone=timezone.replace("/",".");
        }
        jstart_time_millis=c.getTimeInMillis();



        findViewById(R.id.datepickerclicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        datePickerCV.setVisibility(View.VISIBLE);
                        findViewById(R.id.datecheck).setVisibility(View.VISIBLE);
                    }
                }, 300);


                StartSmartAnimation.startAnimation( datePickerCV , AnimationType.SlideInDown , 700 , 0 , true );
                StartSmartAnimation.startAnimation( findViewById(R.id.datecheck) , AnimationType.SlideInUp , 700 , 0 , true );

           //     datePicker.setMinDate(System.currentTimeMillis() - 1000);

            }
        });



        findViewById(R.id.datecheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        timePickerCV.setVisibility(View.VISIBLE);
                        datePickerCV.setVisibility(View.GONE);
                    }
                }, 700);
                StartSmartAnimation.startAnimation( datePickerCV , AnimationType.SlideOutUp , 700 , 0 , true );
                StartSmartAnimation.startAnimation( timePickerCV , AnimationType.SlideInDown , 1000 , 0 , true );
                findViewById(R.id.timecheck).setVisibility(View.VISIBLE);
                StartSmartAnimation.startAnimation(     findViewById(R.id.timecheck), AnimationType.SlideInUp , 0 , 0 , true );
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                date.setText(","+day+" "+MONTH[month]+" "+String.valueOf(year).replace("20",""));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH,day);
                cal.set(Calendar.MONTH,month-1);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);

                jstart_time_millis=cal.getTimeInMillis();
                findViewById(R.id.datecheck).setVisibility(View.GONE);


            }
        });
        findViewById(R.id.timecheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        timePickerCV.setVisibility(View.GONE);
                        findViewById(R.id.timecheck).setVisibility(View.GONE);
                    }
                }, 700);

                StartSmartAnimation.startAnimation( timePickerCV , AnimationType.SlideOutUp , 700 , 0 , true );
                StartSmartAnimation.startAnimation( findViewById(R.id.timecheck) , AnimationType.SlideOutDown , 700 , 0 , true );
                hour = timePicker.getCurrentHour();
                min = timePicker.getCurrentMinute();
                String minute = null;
                if (min<10){
                    minute="0"+String.valueOf(min);
                    time.setText(hour+":"+minute);
                }else
                    time.setText(hour+":"+min);

                jstart_time_millis+=(hour*60+min)*60*1000;
            }

        });
//..................................................................................................
        ButterKnife.bind(this);
        sd = this.getSharedPreferences(getApplication().getPackageName(), Context.MODE_PRIVATE);
        origin=findViewById(R.id.origin);
        dstn=findViewById(R.id.dstn);
        Go=findViewById(R.id.go);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        slidingUpPanelLayout=findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelHeight(0);


        link = (RecyclerView) findViewById(R.id.dragup_list_recycler);
        link.setLayoutManager(new LinearLayoutManager(this));


        mApp = (MQNavigationSampleApplication) getApplication();
        mRouteService = new RouteService.Builder().build(getApplicationContext(), BuildConfig.API_KEY);


        mMap.onCreate(savedInstanceState);
        mMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapController) {
                mMapController = mapController;
                mRoutePolylinePresenter = new RoutePolylinePresenter(mMap, mMapController);
  //              initGpsButton();

                // note: regular click on map will select a *route* to navigate
                mapController.setOnMapClickListener(new RouteClickListener());

                // long-clicks on map are used to define the start and end-points for a route to request
                mMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
                    // long-pressed location is now the next "waypoint" destination
                    @Override
                    public void onMapLongClick(@NonNull LatLng clickedLocation) {
                        Coordinate destinationCoordinate = toCoordinate(clickedLocation);
                        addDestinationToRoute(destinationCoordinate, null);
                    }
                };

//                requestLocationPermissions(new LocationPermissionsResultListener() {
//                    @Override
//                    public void onRequestLocationPermissionsResult(boolean locationPermissionsWereGranted) {
//                        if (locationPermissionsWereGranted) {
//                            // OK, now that we have location permissions, init our GPS location-provider, and zoom the map-view
//                            mApp.getLocationProviderAdapter().initialize();
//                            acquireCurrentLocationAndZoom(mMapController);
//
//                        } else {
//                            // permission "denied"... so we cannot acquire the current location; explain why perms are needed
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(RouteSelectionActivity.this);
//                            builder.setTitle("Location Permissions Required");
//                            builder.setMessage(
//                                    "Without Location Permissions most of the app functionality will be disabled.  " +
//                                            "You will need to restart the app and accept the Location Permissions request");
//                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                }
//                            });
//                            builder.show();
//                        }
//                    }
//                });
            }
        });
//        mFollowUserLocationListener = new FollowUserLocationListener();




        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RouteSelectionActivity.this,SelectPlace.class);
                intent.putExtra("data",0);
                startActivity(intent);
            }
        });

        dstn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RouteSelectionActivity.this,SelectPlace.class);
                intent.putExtra("data",1);
                startActivity(intent);
            }
        });

        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestData();

            }});


        clearMappedRoutes();
        for (Marker marker : mDestinationMarkers) {
            removeDestinationMarker(marker);
        }

        mDestinationLocations.clear();

//        mClearRoutesButton.setVisibility(View.GONE);
        enableButton(mRetrieveRoutesButton, false);
        enableButton(mStartButton, false);


        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(RouteSelectionActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });


    }

    private void requestData(){
        if (originCord==null||dstnCord==null){
            Toast.makeText(mApp, "Please Enter Source & Destination.", Toast.LENGTH_SHORT).show();
        }else {
            mMapController.clear();
            expandableLayout.toggle();
            mDestinationLocations = new ArrayList<>();
            mDestinationLocations.add(dstnCord);
            retrieveRouteFromStartingLocationToDestinations(originCord, mDestinationLocations);
            markOrigin(originCord);
            addDestinationToRoute(dstnCord, dstnCord.getMqId());

        };
    }


    private void requestLocationPermissions(LocationPermissionsResultListener permissionsResultListener) {
        this.locationPermissionsResultListener = permissionsResultListener;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Location Permissions Required");
            builder.setMessage("In Order to use the app you will need to accept the Location Permission request");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(RouteSelectionActivity.this,
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            REQUEST_LOCATION_PERMISSIONS);
                }
            });
            builder.show();
        } else {
            // permission has not been granted yet, so request it directly
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);
        }
    }

    // callback received when permission request has been acted on by user; so call our listener with the result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            // check that the requested permission has been granted
            boolean locationPermissionsWereGranted = (grantResults.length == 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            locationPermissionsResultListener.onRequestLocationPermissionsResult(locationPermissionsWereGranted);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    protected interface LocationPermissionsResultListener {
        void onRequestLocationPermissionsResult(boolean locationPermissionsWereGranted);
    }

    protected boolean hasLocationPermissions() {
        return(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @OnClick(R.id.retrieve_routes)
    protected void retrieveRoutes() {
        //mRouteNameTextView.setVisibility(View.GONE);
     //   acquireCurrentLocationAndRetrieveRoutesToDestinations(mDestinationLocations);
    }

//    @OnClick(R.id.clear_routes)
    protected void clearRoutes() {
        clearMappedRoutes();
        for (Marker marker : new ArrayList<>(mDestinationMarkers)) {
            removeDestinationMarker(marker);
        }

        mDestinationLocations.clear();

       // mRouteNameTextView.setVisibility(View.GONE);
//        mClearRoutesButton.setVisibility(View.GONE);
        enableButton(mRetrieveRoutesButton, false);
        enableButton(mStartButton, false);

        Toast.makeText(getApplicationContext(), "All points cleared. Starting location is current location.\n\n" +
                "Long-press on the map to add a new destination location...", Toast.LENGTH_LONG).show();
    }

    private void enableButton(Button button, boolean enable) {
        button.setEnabled(enable);
        button.setTextColor(enable ? getResources().getColor(R.color.black) :
                getResources().getColor(R.color.disabled_grey));
    }

    @Override
    public void onSearchResultSelected(String displayName, Coordinate coordinate, String mqId) {
        // add selected search-result as (another) destination to route
        addDestinationToRoute(coordinate, mqId);
    }

    private void addDestinationToRoute(Coordinate destinationCoordinate, String mqId) {

        mDestinationMarkers.add(markDestination(destinationCoordinate, R.color.marker_blue));

        // get bounding-rect of origin point and all destinations; adjust map-view accordingly to show all
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        latLngBoundsBuilder.include(mOriginMarker.getPosition());
        for (Marker marker: mDestinationMarkers) {
            latLngBoundsBuilder.include(marker.getPosition());
        }

        mMapController.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(),
                100,
                (int) getMapExtentPaddingTop(),
                100,
                (int) getMapExtentPaddingBottom()));

        mSelectedRoute = null;

        Log.d(TAG, "addDestinationToRoute: adding new destination location with mqid " + mqId);
        mDestinationLocations.add(new Destination(destinationCoordinate, mqId));

//        mClearRoutesButton.setVisibility(View.VISIBLE);
//        StartSmartAnimation.startAnimation( mClearRoutesButton , AnimationType.SlideInUp , 700 , 0 , true );
        enableButton(mRetrieveRoutesButton, true);
        enableButton(mStartButton, false);
    }

    // Lazy load map top padding
    private float getMapExtentPaddingTop() {
        if (mMapExtentPaddingTop == null) {
            int searchBarViewHeightWithPadding = 85 + (2*16); // Based off search bar view height, padding, & marker height
            mMapExtentPaddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    searchBarViewHeightWithPadding, getResources().getDisplayMetrics());
        }
        return mMapExtentPaddingTop;
    }

    // Lazy load map bottom padding
    private float getMapExtentPaddingBottom() {
        if (mMapExtentPaddingBottom == null) {
            int routeButtons = 50 + (2*16); // per route buttons at bottom of map's height & padding
            mMapExtentPaddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    routeButtons, getResources().getDisplayMetrics());
        }
        return mMapExtentPaddingBottom;
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();

         mMap.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        mMap.onResume();
//        mApp.getLocationProviderAdapter().addLocationListener(mFollowUserLocationListener);

        enableButton(mStartButton, (mSelectedRoute != null)); // enable "start nav" if there's (still) a selected route

        // Clear out NavigationNotificationService when coming back from NavigationActivity to destroy
        // all references to the existing NavigationManager and LocationProviderAdapter
        Intent mServiceIntent = new Intent(this, NavigationNotificationService.class);
        stopService(mServiceIntent);
    }

    @Override
    protected void onPause() {
        mMap.onPause();
//        mApp.getLocationProviderAdapter().removeLocationListener(mFollowUserLocationListener);

        super.onPause();
    }

    @Override
    protected void onStop() {
        mMap.onStop();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMap.onDestroy();

        super.onDestroy();
    }




    private void retrieveRouteFromStartingLocationToDestinations(final Coordinate startingCoordinate, final List<Destination> destinationLocations) {
        RoutesResponseListener responseListener = new RoutesResponseListener() {
            @Override
            public void onRoutesRetrieved(@NonNull List<com.mapquest.navigation.model.Route> routes) {

                System.out.println("origin :"+origin+","+"dstn :"+dstnCord.getCoordinate());
                System.out.println("here is route data :e"+new Gson().toJson(routes));

                if(mRoutingDialog != null) {
                    mRoutingDialog.dismiss();
                }
                mapRoutes(routes, null);
           //     if(routes.size() > 1) {
           //         toast(RouteSelectionActivity.this, routes.size() + " routes returned.\n\nChoose one to navigate by tapping on it.");
          //      } else {
                    // if only one route returned, auto-select it
                    setSelectedRoute(routes.get(0));
             //   }
            }

            @Override
            public void onRequestMade() {
                mRoutingDialog = displayProgressDialog("Routing", "Getting routes...");
            }

            @Override
            public void onRequestFailed(@Nullable Integer httpStatusCode,
                                        @Nullable IOException exception) {
                Log.d(TAG, "RoutesResponseListener.onRequestFailed: statusCode: " + httpStatusCode + "; exception: " + exception);

                mRoutingDialog.dismiss();
                displayInformationalDialog("Error",
                        "Sorry, couldn't get routes. :(\n\nCode: " + httpStatusCode
                                + "\nException: " + exception);
            }
        };

        mRouteService = NavigationRouteServiceFactory.getNavigationRouteService(getApplicationContext(), BuildConfig.API_KEY);
        RouteOptions routeOptions = new RouteOptions.Builder()
                .systemOfMeasurementForDisplayText(SystemOfMeasurement.UNITED_STATES_CUSTOMARY) // or specify METRIC
                .language("en_US") // NOTE: alternately, specify "es_US" for Spanish in the US
                .build();

        try {
            mRouteService.requestRoutes(startingCoordinate, destinationLocations, routeOptions, responseListener);
        } catch (IllegalArgumentException e) {
            toast(RouteSelectionActivity.this, e.getLocalizedMessage());
        }
    }

    private void mapRoutes(Iterable<Route> routes, Route selectedRoute) {
        clearMappedRoutes();
        for (Route route : routes) {
            List<PolylineOptions> routeSegmentPolylines = route.equals(selectedRoute) ?
                    mapRoute(route, SELECTED_ROUTE_WIDTH, SELECTED_ROUTE_OPACITY) :
                    mapRoute(route, DEFAULT_ROUTE_WIDTH, DEFAULT_ROUTE_OPACITY);

            mRoutePolylineOptionsListByRoute.put(route, routeSegmentPolylines);
        }
    }

    private List<PolylineOptions> mapRoute(Route route, float lineWidth, float lineOpacity) {

        List<PolylineOptions> polylinesOptionsList = new ArrayList<>();
        for (RouteLeg routeLeg: route.getLegs()) {
            polylinesOptionsList.addAll(mapLeg(routeLeg, lineWidth, lineOpacity));
        }
        return polylinesOptionsList;
    }

    private List<PolylineOptions> mapLeg(RouteLeg leg, float lineWidth, float lineOpacity) {

        List<ShapeSegmenter.SpanPathPair<CongestionSpan>> segments = new ShapeSegmenter.Builder().build()
                .segmentPath(leg.getShape(), leg.getTraffic().getConditions());

        List<PolylineOptions> polylinesList = new ArrayList<>();
        for (ShapeSegmenter.SpanPathPair<CongestionSpan> segment : segments) {
            PolylineOptions polylineOptions = buildSegmentPolylineOptions(
                    segment.getShapeCoordinates(),
                    setOpacity(getCongestionColor(segment.getSpan()), lineOpacity),
                    lineWidth);
            polylinesList.add(polylineOptions);
            mRoutePolylinePresenter.addPolyline(polylineOptions);
        }
        return polylinesList;
    }

    private PolylineOptions buildSegmentPolylineOptions(List<Coordinate> path, int color, float width) {
        PolylineOptions options = new PolylineOptions()
                .width(width)
                .color(color);

        for (Coordinate coordinate : path) {
            options.add(toLatLng(coordinate));
        }
        return options;
    }

    private void clearMappedRoutes() {
        for (List<PolylineOptions> polylineOptionsList : mRoutePolylineOptionsListByRoute.values()) {
            for (PolylineOptions polylineOptions : polylineOptionsList) {
                mRoutePolylinePresenter.removePolyline(polylineOptions);
            }
        }

        mRoutePolylineOptionsListByRoute.clear();
    }

    private ProgressDialog displayProgressDialog(String title, String message) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    private AlertDialog displayInformationalDialog(String title, String message) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private static Coordinate toCoordinate(LatLng location) {
        return new Coordinate(location.getLatitude(), location.getLongitude());
    }

    private void markOrigin(Coordinate location) {
        if (mMapController != null) {
            if (mOriginMarker != null) {
                mMapController.removeMarker(mOriginMarker);
            }


            mOriginMarker = markLocation(toLatLng(location), R.color.colorAccent);
//            IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
//            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.chat);
//            Icon iconbitmap = iconFactory.fromBitmap(icon);
//            mMapController.addMarker(new MarkerOptions()
//                    .position(toLatLng(location))
//                    .icon(iconbitmap));
        }
    }

    private Marker markDestination(Coordinate location, int color) {
        return(markLocation(toLatLng(location), color));
    }

    private Marker markLocation(LatLng latLng, @ColorRes int fillColorResourceId) {
//        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
//        Icon icon = iconFactory.fromResource(R.drawable.rain);
        return mMapController.addMarker(buildDownArrowMarkerOptions(this, fillColorResourceId)
                .position(latLng));
    }

    private Marker markLocationwithIcon(LatLng latLng, @ColorRes int fillColorResourceId,String iconname,String time) {
//        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
//               Icon icon=new iconfromString(iconFactory,iconname).getIcon();

// Markers with text.................................................................................

        Layout_to_Image layout_to_image;
        LinearLayout relativeLayout;
        TextView step_time,step_weather;
        ImageView step_icon;
        //provide layout with its id in Xml
        relativeLayout=findViewById(R.id.show);
        step_time=findViewById(R.id.step_time);
        step_weather=findViewById(R.id.step_weather);
        step_icon=findViewById(R.id.step_icon);
        layout_to_image=new Layout_to_Image(getApplicationContext(),relativeLayout);
        //now call the main working function ;) and hold the returned image in bitmap
        step_time.setText(time);

        switch (iconname){
            case "clear_day":step_icon.setBackgroundResource(R.drawable.clear_day);
                step_weather.setText("Clear Day");
                break;
            case "cloudy":step_icon.setBackgroundResource(R.drawable.cloudy);
                step_weather.setText("Cloudy");
                break;
            case "clear-night":step_icon.setBackgroundResource(R.drawable.clear_night);
                step_weather.setText("Clear Night");
                break;
            case "fog":step_icon.setBackgroundResource(R.drawable.fog);
                step_weather.setText("Fog");
                break;
            case "hail":step_icon.setBackgroundResource(R.drawable.hail);
                step_weather.setText("Hail");
                break;
            case "partly-cloudy-day":step_icon.setBackgroundResource(R.drawable.partly_cloudy_day);
                step_weather.setText("Partly Cloudy Day");
                break;
            case "partly-cloudy-night":step_icon.setBackgroundResource(R.drawable.partly_cloudy_night);
                step_weather.setText("Partly Cloudy Night");
                break;
            case "rain":step_icon.setBackgroundResource(R.drawable.rain);
                step_weather.setText("Rain");
                break;
            case "sleet":step_icon.setBackgroundResource(R.drawable.sleet);
                step_weather.setText("Sleet");
                break;
            case "snow":step_icon.setBackgroundResource(R.drawable.snow);
                step_weather.setText("Snow");
                break;
            case "thunderstorm":step_icon.setBackgroundResource(R.drawable.thunderstorm);
                step_weather.setText("Thunderstorm");
                break;
            case "tornado":step_icon.setBackgroundResource(R.drawable.tornado);
                step_weather.setText("Tornado");
                break;
            case "wind":step_icon.setBackgroundResource(R.drawable.wind);
                step_weather.setText("Wind");
                break;
            default:step_icon.setBackgroundResource(R.drawable.clear_day);
                step_weather.setText("Clear Day");
        }
        Bitmap bitmap=layout_to_image.convert_layout();
//..................................................................................................

        IconFactory iconFactory = IconFactory.getInstance(getApplicationContext());
        Icon icon = iconFactory.fromBitmap(bitmap);

        return mMapController.addMarker(buildDownArrowMarkerOptions(this, fillColorResourceId)
                .icon(icon)
                .position(latLng));
    }

    private void removeDestinationMarker(Marker marker) {
        if (marker != null) {
            mMapController.removeMarker(marker);
            mDestinationMarkers.remove(marker);
        }
    }

    private static LatLng toLatLng(Coordinate coordinate) {
        return new LatLng(coordinate.getLatitude(), coordinate.getLongitude());
    }

//    @Nullable
//    @Override
//    public com.mapquest.android.commoncore.model.LatLng getCurrentLocation() {
//        return toMapQuestLatLng(mMapController.getCameraPosition().target);
//    }

    private class RouteClickListener implements MapboxMap.OnMapClickListener {
        @Override
        public void onMapClick(@NonNull LatLng latLng) {
 //           List<Route> drawnRoutes = new ArrayList<>(mRoutePolylineOptionsListByRoute.keySet());
//            setSelectedRoute(findNearestRoute(drawnRoutes, toCoordinate(latLng)));

//            List<Route> routes = new ArrayList<>(mRoutePolylineOptionsListByRoute.keySet());
//            clearMappedRoutes();
//
//            mSelectedRoute = findNearestRoute(routes, toCoordinate(latLng));
//            if(mSelectedRoute != null) {
//                // Move to the end, so the selected route gets drawn over the rest
//                if(routes.remove(mSelectedRoute)) {
//                    routes.add(mSelectedRoute);
//
//                    mapRoutes(routes, mSelectedRoute);
//                }
//                mRouteNameTextView.setText(mSelectedRoute.getName() != null ? mSelectedRoute.getName() : "");
//                mRouteNameTextView.setVisibility(View.VISIBLE);
//
//                enableButton(mStartButton, true);
//            }
        }
    }

    private void setSelectedRoute(Route selectedRoute) {
        if(selectedRoute != null) {
            mSelectedRoute = selectedRoute;

            // re-draw routes; highlighting the selected-route
            List<Route> drawnRoutes = new ArrayList<>(mRoutePolylineOptionsListByRoute.keySet());
            clearMappedRoutes();

            // note: move selected-route to the end, so that it gets drawn on top of the rest
            if(drawnRoutes.remove(selectedRoute)) {
                drawnRoutes.add(selectedRoute);
                mapRoutes(drawnRoutes, selectedRoute);
            }
            //mRouteNameTextView.setVisibility(View.VISIBLE);
         //   mRouteNameTextView.setText(mSelectedRoute.getName() != null ? mSelectedRoute.getName() : "");


 //           enableButton(mStartButton, true);

            try {
               new FetchCloudData().execute(getApplicationContext(),originCord,dstnCord,route,interval,timezone,jstart_time_millis);
               }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Route findNearestRoute(Iterable<Route> routes, Coordinate point) {
        Double nearestDistance = null;
        Route nearestRoute = null;

        ShapeSegmenter shapeSegmenter = new ShapeSegmenter.Builder().build();
        for (Route route : routes) {
            ShapeCalculator.LineStringPoint result = null;
            for (RouteLeg routeLeg: route.getLegs()) {
                ShapeCalculator.LineStringPoint pointForLeg = ((ShapeCalculator) shapeSegmenter)
                        .findClosestPoint(point, routeLeg.getShape().getCoordinates());
                if (result == null || pointForLeg.getArcLengthFromTestPoint() < result.getArcLengthFromTestPoint()) {
                    result = pointForLeg;
                }
            }
            if (nearestDistance == null || result.getArcLengthFromTestPoint() < nearestDistance) {
                nearestDistance = result.getArcLengthFromTestPoint();
                nearestRoute = route;
            }
        }
        return nearestRoute;
    }


    public void puttomap(Output output){
        if(output != null) {
            List<Step> steps = output.getSteps();
            link.setAdapter(new DragupListAdapter(getApplicationContext(), output.getSteps()));
            //      if (route.getLegs().get(0).getDuration().getText() != null) {
            slidingUpPanelLayout.setPanelHeight(getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dragupsize));
            //     }
            for (int k = 0; k < steps.size(); k++) {
                markLocationwithIcon(new LatLng(steps.get(k).getStep().getStartPoint().getLat(), steps.get(k).getStep().getStartPoint().getLng()), R.color.marker_orange, steps.get(k).getWlist().getIcon(),steps.get(k).getArrtime());
            }

            List<Item> interm = output.getItems();
            for (int k = 0; k < interm.size(); k++) {
                markLocationwithIcon(new LatLng(interm.get(k).getPoint().getLat(), interm.get(k).getPoint().getLng()), R.color.marker_blue, interm.get(k).getWlist().getIcon(),steps.get(k).getArrtime());
            }
        }
    }

//    private void initGpsButton() {
//        mGpsCenterOnUserLocationButton.setVisibility(View.VISIBLE);
//        mGpsCenterOnUserLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (hasLocationPermissions()) {
//                    // re-acquire current location and zoom/center map to that location
//                    acquireCurrentLocationAndZoom(mMapController);
//                } else {
//                    requestLocationPermissions(new LocationPermissionsResultListener() {
//                        @Override
//                        public void onRequestLocationPermissionsResult(boolean locationPermissionsWereGranted) {
//                            if (locationPermissionsWereGranted) {
//                                acquireCurrentLocationAndZoom(mMapController);
//                            } else {
//                                // location-permissions were *denied* by the user...
//                                // so nothing to do (until the user taps the "GPS center" button again)
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }

//    private class FollowUserLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(com.mapquest.navigation.model.location.Location location) {
//            mLastLocation = location;
//            markOrigin(location);
//        }
//    }
//..................................................................................................

    class FetchCloudData extends AsyncTask<Object,Object,String> {

        Context context;
        Coordinate origincord,dstncord;
        long route=0;
        long interval=50000;
        String timezone="";
        long time=0;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Output output = new Gson().fromJson(result, Output.class);
                if(output!=null) {
                    ((TextView)findViewById(R.id.distance)).setText(output.getDistance());
                    ((TextView)findViewById(R.id.duration)).setText(output.getDuration());
                    puttomap(output);
                }
            }catch (Exception e){
                e.fillInStackTrace();
            }
        }



        @Override
        protected String doInBackground(Object[] objects) {
            context= (Context) objects[0];
            origincord= (Coordinate) objects[1];
            dstncord =(Coordinate)objects[2];
            route=(long)objects[3];
            interval=(long)objects[4];
            timezone=(String)objects[5];
            time=(long)objects[6];

            try {
                ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mgr.getActiveNetworkInfo();

                if (netInfo != null && netInfo.isConnected()) {
                    HttpClient client = new DefaultHttpClient();
                    HttpResponse response = null;
                 //   String url="https://ou4ptj7z2b.execute-api.ap-south-1.amazonaws.com/dev";
                    String url ="https://nk0031nuf4.execute-api.ap-south-1.amazonaws.com/dev";
                    HttpPost request = new HttpPost(url);

                    Input input=new Input();
                    input.setOrigin(new com.mapquest.navigation.sampleapp.Models.LatLng(((float) origincord.getLatitude()), ((float) origincord.getLongitude())));
                    input.setDestination(new com.mapquest.navigation.sampleapp.Models.LatLng(((float)dstncord.getLatitude()),((float)dstncord.getLongitude())));
                    input.setRoute(route);
                    input.setInterval(interval);
                    input.setTimeZone(timezone);
                    input.setTime(time);


                    String json =new Gson().toJson(input);
                    System.out.println("hre is json :\n"+json);
                    StringEntity entity = new StringEntity(json);
                    request.setEntity(entity);
                    request.setHeader("Accept", "application/json");
                    request.setHeader("Content-type", "application/json");


                    BufferedReader rd=null;

                    response = client.execute(request);
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line="";
                    StringBuilder sb=new StringBuilder();
                    while ((line=rd.readLine())!=null){
                        sb.append(line);
                    }
                    return sb.toString();
                }else{
                    return "NoInternet";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d("TAG", "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d("TAG", "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
//            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // First find out which subscription is auto renewing
            Purchase gasMonthly = inventory.getPurchase(SKU_INFINITE_GAS_MONTHLY);
            Purchase gasQuaterly = inventory.getPurchase(SKU_INFINITE_GAS_QUATERLY);
            Purchase gasHalfYearly = inventory.getPurchase(SKU_INFINITE_GAS_HALFYEARLY);
            Purchase gasYearly = inventory.getPurchase(SKU_INFINITE_GAS_YEARLY);
            if (gasMonthly != null && gasMonthly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_MONTHLY;
                mAutoRenewEnabled = true;
            }else if (gasQuaterly != null && gasQuaterly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_QUATERLY;
                mAutoRenewEnabled = true;
            }else if (gasHalfYearly!= null && gasHalfYearly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_HALFYEARLY;
                mAutoRenewEnabled = true;
            }
            else if (gasYearly != null && gasYearly.isAutoRenewing()) {
                mInfiniteGasSku = SKU_INFINITE_GAS_YEARLY;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteGasSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteGas = (gasMonthly != null && verifyDeveloperPayload(gasMonthly))
                    || (gasQuaterly != null && verifyDeveloperPayload(gasQuaterly))
                    || (gasHalfYearly != null && verifyDeveloperPayload(gasHalfYearly))
                    || (gasYearly != null && verifyDeveloperPayload(gasYearly));
            Log.d("TAG", "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            haveTrialorSubs();

        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d("TAG", "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    private TrialyCallback mTrialyCallback = new TrialyCallback() {
        @Override
        public void onResult(int status, long timeRemaining, String sku) {
            switch (status){
                case STATUS_TRIAL_JUST_STARTED:

                    break;
                case STATUS_TRIAL_RUNNING:

                    break;
                case STATUS_TRIAL_JUST_ENDED:
                    havetrial=false;
                    haveTrialorSubs();
                    break;
                case STATUS_TRIAL_NOT_YET_STARTED:
                    havetrial=false;
                    haveTrialorSubs();
                    break;
                case STATUS_TRIAL_OVER:
                    havetrial=false;
                    haveTrialorSubs();
                    break;
            }
            Log.i("TRIALY", "Returned status: " + Trialy.getStatusMessage(status));
        }

    };

    void complain(String message) {
        Log.e("TAG", "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d("TAG", "Showing alert dialog: " + message);
        bld.create().show();


    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    void haveTrialorSubs(){
        if(!havetrial && !mSubscribedToInfiniteGas) {
            startActivity(new Intent(RouteSelectionActivity.this, Subscription.class));
            finish();
        }
    }
    //menu..............................................................................................
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.km20:
                item.setChecked(true);
                interval=20000;
                return true;
            case R.id.km30:
                item.setChecked(true);
                interval=30000;
                Toast.makeText(this, "30km", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.km40:
                item.setChecked(true);
                interval=40000;
                return true;
            case R.id.km50:
                item.setChecked(true);
                interval=50000;
                return true;
            case R.id.action_retry:
                expandableLayout.toggle();
                requestData();
                Toast.makeText(this, "Retrying...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_clr:
                Toast.makeText(this, "clear", Toast.LENGTH_SHORT).show();
                originCord=null;
                dstnCord=null;
                recreate();
                return true;
            case R.id.btnSubsc:
                startActivity(new Intent(RouteSelectionActivity.this,Subscription.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//..................................................................................................

}
