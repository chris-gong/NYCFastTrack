package com.chrisgong.nycfasttrack;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.chrisgong.nycfasttrack.NYCGraph.routeLinkedLists;

public class LiveFeedActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EditText input;
    Button enterButton;
    boolean firstTimeEntering = true;
    static String route = "";
    String sourceStop = "";
    String destAddress = "";
    LatLngBounds.Builder builder = null;
    Address source;
    Address dest;
    LatLng sourcePoint;
    LatLng destPoint;
    private static HashMap<String, GTFSParser.Stop> subwayStops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_feed);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        subwayStops = GTFSParser.getSubwayStops();
        input = (EditText)findViewById(R.id.input);
        enterButton = (Button)findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = input.getText().toString();
                if (location != null || !location.equals("")) {
                    enterButton.setEnabled(false);
                    input.setEnabled(false);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> addressList = null;
                    sourceStop = location;
                    try {
                        addressList = geocoder.getFromLocationName(subwayStops.get(sourceStop).stopName, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(!(routeLinkedLists.size() > 0)){
                        NYCGraph.makeNYCGraph();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    source = address;
                    sourcePoint = latLng;
                    //builder needed to provide bounds for zooming in so that marker remains centered
                    builder = new LatLngBounds.Builder();
                    builder.include(latLng);
                    LatLngBounds bounds = builder.build();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                    //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                    routeLinkedLists = routeLinkedLists;
                    NYCGraph.Vertex curr = routeLinkedLists.get(route).head;
                    NYCGraph.Vertex prev = null;
                    Log.d("selectedRoute", route);
                    PolylineOptions polyline = new PolylineOptions().color(Color.BLUE).width(5).visible(true).zIndex(30);
                    while(curr != null){
                        Log.d("hi", "test");
                        LatLng vertexLatLng = new LatLng(curr.latitude, curr.longitude);
                        mMap.addMarker(new MarkerOptions().position(vertexLatLng).title(address.getAddressLine(0)));
                        polyline = polyline.add(vertexLatLng);
                        prev = curr;
                        curr = curr.next;

                    }
                    Polyline line = mMap.addPolyline(polyline);
                    /*Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            long elapsed = SystemClock.uptimeMillis() - start;
                            float t = interpolator.getInterpolation((float) elapsed
                                    / duration);
                            double lng = t * toPosition.longitude + (1 - t)
                                    * startLatLng.longitude;
                            double lat = t * toPosition.latitude + (1 - t)
                                    * startLatLng.latitude;
                            marker.setPosition(new LatLng(lat, lng));

                            if (t < 1.0) {
                                // Post again 16ms later.
                                handler.postDelayed(this, 16);
                            } else {
                                if (hideMarker) {
                                    marker.setVisible(false);
                                } else {
                                    marker.setVisible(true);
                                }
                            }
                        }
                    });*/
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
