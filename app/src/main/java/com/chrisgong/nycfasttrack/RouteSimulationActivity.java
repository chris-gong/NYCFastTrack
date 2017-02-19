package com.chrisgong.nycfasttrack;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteSimulationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    EditText input;
    Button enterButton;
    boolean firstTimeEntering = true;
    String sourceAddress = "";
    String destAddress = "";
    LatLngBounds.Builder builder = null;
    Address source;
    Address dest;
    LatLng sourcePoint;
    LatLng destPoint;
    private GoogleApiClient mGoogleApiClient;
    boolean timeToFindStations = false;
    int PLACE_PICKER_REQUEST = 1;
    String selectedRoute;
    String selectedDestination;
    HashMap<String,ArrayList<GTFSParser.StopTime>> subwayStopTimes;
    HashMap<String, NYCGraph.LinkedList> routeLinkedLists;
    @Override
    public void onConnectionFailed(ConnectionResult result){
        Log.d("hi", "hi");
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_simulation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        input = (EditText)findViewById(R.id.input);
        enterButton = (Button)findViewById(R.id.enterButton);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        enterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String location = input.getText().toString();
                if(location != null || !location.equals("")){
                    if(firstTimeEntering){
                        sourceAddress = location;
                        firstTimeEntering = false;
                        enterButton.setText("Enter end address");
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = null;
                        try{
                            addressList = geocoder.getFromLocationName(sourceAddress,1);
                        } catch (IOException e){
                            e.printStackTrace();
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
                        input.setText("");
                    }
                    else{
                        destAddress = location;
                        //do other stuff
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        List<Address> addressList = null;
                        try{
                            addressList = geocoder.getFromLocationName(destAddress,1);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        dest = address;
                        destPoint = latLng;
                        //builder needed to provide bounds for zooming in so that marker remains centered
                        //best way to get all points within zoomin view
                        builder.include(latLng);
                        LatLngBounds bounds = builder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.25);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
                        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        input.setEnabled(false);
                        enterButton.setEnabled(false);
                        if(!(NYCGraph.routeLinkedLists.size() > 0)){
                            NYCGraph.makeNYCGraph();
                        }
                        routeLinkedLists = NYCGraph.routeLinkedLists;
                        NYCGraph.Vertex curr = routeLinkedLists.get(selectedRoute).head;
                        NYCGraph.Vertex prev = null;
                        PolylineOptions polyline = new PolylineOptions().color(Color.BLUE).width(5).visible(true).zIndex(30);
                        while(curr != null){
                            LatLng vertexLatLng = new LatLng(curr.latitude, curr.longitude);
                            mMap.addMarker(new MarkerOptions().position(vertexLatLng).title(address.getAddressLine(0)));
                            polyline.add(vertexLatLng);

                        }
                        mMap.addPolyline(polyline);
                        //zoom back after 5 seconds
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                //Actions to do after two seconds
                                LatLngBounds.Builder temp = new LatLngBounds.Builder();
                                temp.include(sourcePoint);
                                LatLngBounds bounds = temp.build();
                                mMap.addMarker(new MarkerOptions().position(sourcePoint).title(source.getAddressLine(0)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                                //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                                drawCircle(sourcePoint);
                                //get nearby train stations and draw paths to each, find shortest path
                                timeToFindStations = true;
                            }
                        }, 5000);

                    }
                }
            }
        });
        while(!timeToFindStations){

        }

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent intent;
        try {
            intent = builder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        }catch(GooglePlayServicesRepairableException e){

        }catch(GooglePlayServicesNotAvailableException e){

        }
    }
    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle, in meters
        circleOptions.radius(536); //approximately one third a mile

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

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
    /*
    public static void parseStopTimes(Context context, String selectedRoute, String selectedDestination) throws IOException {
        subwayStopTimes = new HashMap<String, ArrayList<GTFSParser.StopTime>>();
        int northCounter = 0; //WE ONLY WANT FIVE TIMES/PREDICTIONS AT MOST FOR EACH DIRECTION
        int southCounter = 0;
        AssetManager assetManager = context.getAssets();
        InputStream stopsFile = assetManager.open("stop_times.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stopsFile));
        String line = br.readLine(); //skip the first line of text file
        while (((line = br.readLine()) != null)) {
            int firstUnderscore = line.indexOf("_");
            int secondUnderscore = line.indexOf("_", firstUnderscore + 1);
            int period = line.indexOf(".");
            String routeID = line.substring(secondUnderscore + 1, period);
            if (!routeID.equals(selectedRoute)) {
                continue;
            }
            int firstComma = line.indexOf(",");
            int secondComma = line.indexOf(",", firstComma + 1);
            int thirdComma = line.indexOf(",", secondComma + 1);
            int fourthComma = line.indexOf(",", thirdComma + 1);
            String stopID = line.substring(thirdComma + 1, fourthComma);
            if (!stopID.substring(0,stopID.length()-1).equals(selectedDestination)) {
                continue;
            } else {
                String timeString = line.substring(firstComma + 1, secondComma);
                String tripID = line.substring(0, firstComma);
                char direction = stopID.charAt(stopID.length() - 1);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                java.util.Date destTime = null;
                java.util.Date currentTime = new java.util.Date();
                java.sql.Timestamp stamp = new java.sql.Timestamp(System.currentTimeMillis());
                java.sql.Date date = new java.sql.Date(stamp.getTime());
                String currentTimeString = format.format(date);
                try {
                    destTime = format.parse(currentTimeString.substring(0,11) + " " + timeString);
                    currentTime = format.parse(currentTimeString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                //Log.d(timeString, currentTimeString);
                long diff = destTime.getTime() - currentTime.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);
                diffMinutes += diffHours * 60;
                if (diffMinutes > 0 && direction == 'S') {
                    southCounter++;
                } else if (diffMinutes > 0 && direction == 'N') {
                    northCounter++;
                }
                Log.d(currentTimeString.substring(0,11) + " " + timeString, String.valueOf(diffMinutes));
                GTFSParser parser = new GTFSParser();
                if (!subwayStopTimes.containsKey(selectedDestination)) {
                    subwayStopTimes.put(selectedDestination, new ArrayList<GTFSParser.StopTime>());
                    //Log.d("array", "made");
                }

                subwayStopTimes.get(selectedDestination).add(parser.new StopTime(tripID, stopID, direction, timeString, diffMinutes));
            }
        }
    }

    public static void parseStopTimes(Context context, String selectedRoute, String selectedDestination) throws IOException {
        subwayStopTimes = new HashMap<String, ArrayList<GTFSParser.StopTime>>();
        int northCounter = 0; //WE ONLY WANT FIVE TIMES/PREDICTIONS AT MOST FOR EACH DIRECTION
        int southCounter = 0;
        AssetManager assetManager = context.getAssets();
        InputStream stopsFile = assetManager.open("stop_times.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stopsFile));
        String line = br.readLine(); //skip the first line of text file
        while (((line = br.readLine()) != null)) {
            int firstUnderscore = line.indexOf("_");
            int secondUnderscore = line.indexOf("_", firstUnderscore + 1);
            int period = line.indexOf(".");
            String routeID = line.substring(secondUnderscore + 1, period);
            if (!routeID.equals(selectedRoute)) {
                continue;
            }
            int firstComma = line.indexOf(",");
            int secondComma = line.indexOf(",", firstComma + 1);
            int thirdComma = line.indexOf(",", secondComma + 1);
            int fourthComma = line.indexOf(",", thirdComma + 1);
            String stopID = line.substring(thirdComma + 1, fourthComma);
            if (!stopID.substring(0, stopID.length() - 1).equals(selectedDestination)) {
                continue;
            } else {
                String timeString = line.substring(firstComma + 1, secondComma);
                String tripID = line.substring(0, firstComma);
                char direction = stopID.charAt(stopID.length() - 1);
                java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                java.util.Date destTime = null;
                java.util.Date currentTime = new java.util.Date();
                java.sql.Timestamp stamp = new java.sql.Timestamp(System.currentTimeMillis());
                java.sql.Date date = new java.sql.Date(stamp.getTime());
                String currentTimeString = format.format(date);
                try {
                    destTime = format.parse(currentTimeString.substring(0, 11) + " " + timeString);
                    currentTime = format.parse(currentTimeString);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                //Log.d(timeString, currentTimeString);
                long diff = destTime.getTime() - currentTime.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);
                diffMinutes += diffHours * 60;
                if (diffMinutes > 0 && direction == 'S') {
                    southCounter++;
                } else if (diffMinutes > 0 && direction == 'N') {
                    northCounter++;
                }
                Log.d(currentTimeString.substring(0, 11) + " " + timeString, String.valueOf(diffMinutes));
                GTFSParser parser = new GTFSParser();
                if (!subwayStopTimes.containsKey(selectedDestination)) {
                    subwayStopTimes.put(selectedDestination, new ArrayList<GTFSParser.StopTime>());
                    //Log.d("array", "made");
                }

                subwayStopTimes.get(selectedDestination).add(parser.new StopTime(tripID, stopID, direction, timeString, diffMinutes));
            }
        }
    }
    private static class asyncTaskParseStopTimes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            try {
                parseStopTimes(context, selectedRoute, selectedDestination);
            } catch(IOException e) {
                e.printStackTrace();
            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            //GTFSParser.printStops();
            //HashMap<String, ArrayList<GTFSParser.StopTime>> subwayStopTimes = GTFSParser.getSubwayStopTimes();
            //Log.d("Size of stopTimes hashmap ", String.valueOf(subwayStopTimes.size()));
            finishedParsing = true;
            //GTFSParser.printStopTimes();
        }
    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }
}
