package com.example.richard.bookrian;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by richard on 6/5/17.
 */

public class Library_detailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000;  // 2 Seconds


    private static final int LOCATION_REQUEST_CODE = 1337;

    private Boolean m_bCanAccessLocation;

    // Pre-defined locations for each of Monash's 6 campuses (Derived from Google Maps)
    private static final LatLng LOCATION_Hargrave_Andrew
            = new LatLng(-37.909793, 145.132179);
    private static final LatLng LOCATION_CAUFIELD
            = new LatLng(-37.877124, 145.045360);
    private static final LatLng LOCATION_LOUIS_MATHESON
            = new LatLng(-37.912793, 145.134299);
    private static final LatLng LOCATION_PENINSULA
            = new LatLng(-38.153168, 145.134966);


    // Instance Variables
    private GoogleMap m_cGoogleMap;
    private Location m_cCurrentLocation;
    private GoogleApiClient m_cAPIClient;
    private String cur_lib;
    private String cur_location;
    private TextView libraryname;
    private TextView librarylocation;
    private TextView libraryphone;
    private TextView libraryadd1;
    private TextView libraryadd2;
    private TextView libraryadd3;


    Toolbar mActionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_detail);

        mActionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Refer Text View
        libraryname = (TextView) findViewById(R.id.l_library_name);
        librarylocation = (TextView) findViewById((R.id.l_lib_location));
        libraryphone = (TextView) findViewById(R.id.l_phone);
        libraryadd1 = (TextView) findViewById(R.id.l_add1);
        libraryadd2 = (TextView) findViewById(R.id.l_add2);
        libraryadd3 = (TextView) findViewById(R.id.l_add3);


        Library receive = getIntent().getExtras().getParcelable("Library"); //retrieve parcel

        cur_lib = receive.getM_sName();
        cur_location =receive.getM_sLocation();


        libraryname.setText(cur_lib);
        librarylocation.setText(cur_location);
        libraryphone.setText(receive.getLib_phone());
        libraryadd1.setText(receive.getLib_add1());
        libraryadd2.setText(receive.getLib_add2());
        libraryadd2.setText(receive.getLib_add3());



        //Request Permission
        m_bCanAccessLocation =
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);

        // If we do not have permissions then request them
        if(!m_bCanAccessLocation) {
            RequestPermissions();
        }


        // Check to see if our APIClient is null.
        if(m_cAPIClient == null) {
            // Create our API Client and tell it to connect to Location Services
            m_cAPIClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // Get access to our MapFragment
        MapFragment mapFrag = (MapFragment)
                getFragmentManager().findFragmentById(R.id.map_fragment);
        // Set up an asyncronous callback to let us know when the map has loaded
        mapFrag.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Function is called once the map has fully loaded.
        // Set our map object to reference the loaded map
        m_cGoogleMap = googleMap;

        if (cur_lib.equals("Hargrave Andrew Library") ){

            // Move the focus of the map to be on Hargrave Andrew Library. 15 is for zoom
            m_cGoogleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(LOCATION_Hargrave_Andrew, 18));
        };

        if (cur_lib.equals("Monash Caufiled Library")){

            // Move the focus of the map to be on Monash Caufield Library. 15 is for zoom
            m_cGoogleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(LOCATION_CAUFIELD, 18));
        };
        if (cur_lib.equals("Sir Louis Mantheson Library")) {

            // Move the focus of the map to be on Sir Louis Mantheson Library. 15 is for zoom
            m_cGoogleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(LOCATION_LOUIS_MATHESON, 18));
        };
        if (cur_lib.equals("Monash Peninsula Library")) {

            // Move the focus of the map to be on Monash Peninsula Library . 15 is for zoom
            m_cGoogleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(LOCATION_PENINSULA, 18));
        };


        // Call our Add Default Markers function
        // NOTE: In a proper application it may be better to load these from a DB
        AddDefaultMarkers();
    }

    private void AddDefaultMarkers() {
        // Create a series of markers for each campus with the title being the campus name
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_Hargrave_Andrew).title("Hargrave-Andrew Library"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_CAUFIELD).title("Monash Caufield Library"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_LOUIS_MATHESON).title("Sir Louis Mantheson Library"));
        m_cGoogleMap.addMarker(new MarkerOptions()
                .position(LOCATION_PENINSULA).title("Monash Peninsula Library"));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Android 6.0 & up added security permissions
        // If the user rejects allowing access to location data then this try block
        // will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);

            LocationServices.FusedLocationApi
                    .requestLocationUpdates(m_cAPIClient, locationRequest, this);
        }
        catch (SecurityException secEx) {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing for now. This function is called should the connection halt
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Do nothing for now. This function is called if the connection fails initially
    }

    @Override
    public void onLocationChanged(Location location) {
        // This is our function that is called whenever we change locations
        // Update our current location variable
        m_cCurrentLocation = location;
        //ChangeMapLocation();
    }

    private void ChangeMapLocation() {
        // Check to ensure map and location are not null
        if(m_cCurrentLocation != null && m_cGoogleMap != null) {
            // Create a new LatLng based on our new location
            LatLng newPos = new LatLng(m_cCurrentLocation.getLatitude(),
                    m_cCurrentLocation.getLongitude());
            // Change the map focus to be our new location
            m_cGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos, 15));
        }
    }

    public void RequestPermissions() {
        // Check if we need to provide information to the user
        // We are checking if we need permission for fine location
        // NOTE: Both are in the same permission group so granting one grants both
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show the user an explanation on why we need the permission and then ask again
            // http://stackoverflow.com/questions/41310510/what-is-the-difference-between-shouldshowrequestpermissionrationale-and-requestp
            new AlertDialog.Builder(Library_detailActivity.this)
                    .setTitle("Permission required")
                    .setMessage("This is a map application. You need to enable location services for it to work!")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Library_detailActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    })
                    .show();
        }
        else {
            // We do not need to show the user info we can just request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // Check with request code has been given to us
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // This is a location permission request so lets handle it
                if(grantResults.length > 0) {
                    // Can access coarse is equal to
                    m_bCanAccessLocation = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                break;
        }

        // If at this point we have permissions for location attempt to start it
        if(m_bCanAccessLocation) {
            // Check to see if our APIClient is null.
            if(m_cAPIClient == null) {
                // Create our API Client and tell it to connect to Location Services
                m_cAPIClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                m_cAPIClient.connect();
            }
        }
        else {
            // Display error saying we cannot start location service without permission
            Toast.makeText(this, "Locations will not be displayed without permissions", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        m_cAPIClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        m_cAPIClient.disconnect();
        super.onStop();
    }

}
