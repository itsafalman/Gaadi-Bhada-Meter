package com.example.l.gaadivaada;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.Calendar;

public class TaxiActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private  GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean isMapInitialized = false;
    private LocationResult locationResult;
    private LocationRequest mlocationrequest;
    private float mincharge = 10;
    private float charge =0;
    private boolean startBtn = false;
    private float inkm = 0;
    private Location initialPosition;
    private Location oldLocation;
    private Location newLocation;
    private float totalDistance;
    private float totalChange;
    private float chargeflag = 0;
    private float rate= 7;
    private Marker marker=null;
    private Marker mapMarker ;
    private static int flagdown = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        charge = charge + mincharge;



    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Button myButton = (Button)findViewById(R.id.start);
        final Toolbar bottombar = (Toolbar)findViewById(R.id.toolbar_bottom);
        bottombar.setTitle("Searching Location");
        myButton.setVisibility(View.INVISIBLE);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startBtn) {
                    startBtn = false;
                    Intent i = new Intent(getApplicationContext(), RouteSummary.class);
                    i.putExtra("charge", charge);
                    i.putExtra("distance", inkm);
                    startActivity(i);

                } else
                    myButton.setTextColor(Color.parseColor("#FF0000"));
                myButton.setText("Stop Journey");
                startBtn = true;
            }
        });
        mMap = googleMap;
        String timeString="";
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
          if (hour>=5 && hour <= 18) {
            timeString ="day";
        } else  {
            timeString = "night";
            rate = 10.5f;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        this.locationResult = new LocationResult() {
            private int gotlocation=0;
            @Override
            public void gotLocation(Location location) {

                if(gotlocation == 0 ){
                    Toast.makeText(getApplicationContext(), "Location Found ", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Press Start Journey Button to start  ", Toast.LENGTH_LONG).show();
                    gotlocation=1;
                    bottombar.setVisibility(View.INVISIBLE);
                }
                myButton.setVisibility(View.VISIBLE);

                if(location!=null){
                    LatLng current_location = new LatLng(location.getLatitude(),location.getLongitude());
                    if(!isMapInitialized) {
                        mapMarker = mMap.addMarker(new MarkerOptions().position(current_location).title("You started here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                        //mMap.getMinZoomLevel();
                        isMapInitialized = true;
                        initialPosition = location;
                        oldLocation = location;
                    }
                    if(flagdown>=20)
                    {
                        charge+=5;
                        flagdown = 0;
                    }
                    if(startBtn){

                        if(marker!=null){
                            marker.remove();
                        }
                        LatLng marker_location = new LatLng(location.getLatitude(),location.getLongitude());
                        newLocation = location;
                        totalChange = newLocation.distanceTo(oldLocation);

                        if(totalDistance > 15){
                            totalDistance+=totalChange;

                            inkm = totalDistance/1000;
                            TextView distanceMeter = (TextView) findViewById(R.id.dist);
                            TextView chargemeter = (TextView) findViewById(R.id.chargetext);
                            distanceMeter.setText(""+ new DecimalFormat("##.##").format(inkm) + "KM");
                            chargeflag = rate/200;
                            chargeflag= chargeflag*totalChange;
                            charge = charge + chargeflag;
                            chargemeter.setText("Rs." + new DecimalFormat("##.##").format(charge) + "");
                            Polyline line = mMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(oldLocation.getLatitude(), oldLocation.getLongitude()), new LatLng(newLocation.getLatitude(), newLocation.getLongitude()))
                                    .width(5)
                                    .color(Color.RED));
                            oldLocation = newLocation;
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(marker_location));
                            marker = mMap.addMarker(new MarkerOptions().position(marker_location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("You are here"));

                        }
                        else{
                            flagdown++;
                        }

                     }
                }
            }
        };
    }
    LocationListener mylocationlistener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationResult.gotLocation(location);
        }
    };

    @Override
    public void onConnected(Bundle bundle) {

        try {
            mlocationrequest = LocationRequest.create();
            mlocationrequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mlocationrequest.setInterval(15000);
            mlocationrequest.setSmallestDisplacement(0);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationrequest, mylocationlistener);

        }catch(SecurityException e){
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
    public void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mylocationlistener);
    }

    @Override
    public void onBackPressed() {

    }
}
