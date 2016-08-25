package com.example.l.gaadivaada;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;

public class MicroBus_Activity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private  GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private boolean isMapInitialized = false;
    private LocationResult locationResult;
    private LocationRequest mlocationrequest;
    private int charge = 0;
    private int chargeflag =13;
    private float rate = 7;
    private boolean startBtn = false;
    private float inkm = 0;
    private Location initialPosition;
    private Location oldLocation;
    private Location newLocation;
    private float totalDistance;
    private float totalChange;
    private Marker marker = null;
    private int test=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_bus_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        charge = charge + chargeflag;
        final Button myButton = (Button)findViewById(R.id.start);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startBtn){
                    startBtn = false;
                }
                else
                    myButton.setTextColor(Color.parseColor("#FF0000"));
                myButton.setText("Stop Journey");
                startBtn = true;
            }
        });


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
        mMap = googleMap;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        this.locationResult = new LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if(location!=null){
                    LatLng current_location = new LatLng(location.getLatitude(),location.getLongitude());
                    if(!isMapInitialized){
                        mMap.addMarker(new MarkerOptions().position(current_location).title("Your current Position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                        mMap.getMinZoomLevel();
                        isMapInitialized = true;
                        initialPosition = location;
                        oldLocation = location;
                    }
                    if(startBtn){
                        if(marker!=null){
                            marker.remove();
                        }
                        LatLng marker_location = new LatLng(location.getLatitude(),location.getLongitude());
                        newLocation = location;
                        totalChange = newLocation.distanceTo(oldLocation);
                        totalDistance+=totalChange;
                        inkm = totalDistance/1000;
                        TextView distanceMeter = (TextView) findViewById(R.id.dist);
                        TextView chargemeter = (TextView) findViewById(R.id.chargetext);

                        if(inkm>4 && inkm<=5){
                            if(test==0){
                                charge+=2;
                                test++;
                            }

                        }
                        else if(inkm>5 && inkm<=6){
                            if(test==1){
                                charge+=1;
                                test++;
                            }
                        }
                        else if(inkm>6 && inkm<=8){
                            if(test==2){
                                charge+=1;
                                test++;
                            }
                        else if(inkm>8 && inkm<=10){
                                if(test==3){
                                    charge+=2;
                                    test++;
                                }
                            }
                        else if(inkm>10 && inkm<=13){
                                if(test==4){
                                    charge+=2;
                                    test++;
                                }

                            }
                        else if(inkm >13 && inkm<=16){
                                if(test==5){
                                    charge+=2;
                                    test++;
                                }

                            }
                         else if(inkm >16 && inkm<=19){
                                if(test==6){
                                    charge+=1;
                                    test++;
                                }
                            }
                         else if(inkm>19){
                                if(test==7){
                                    charge+=1;
                                    test++;
                                }
                            }
                        }
                        chargemeter.setText("Rs."+charge+"");
                        distanceMeter.setText(""+ new DecimalFormat("##.##").format(inkm) + "KM");
                        Polyline line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(oldLocation.getLatitude(), oldLocation.getLongitude()), new LatLng(newLocation.getLatitude(), newLocation.getLongitude()))
                                .width(5)
                                .color(Color.RED));
                        oldLocation = newLocation;
                        marker = mMap.addMarker(new MarkerOptions().position(marker_location).title("Your current Position"));

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
            mlocationrequest.setSmallestDisplacement(10);

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
}
