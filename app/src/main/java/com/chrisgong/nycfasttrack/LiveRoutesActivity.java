package com.chrisgong.nycfasttrack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//THIS CLASS IS ASSOCIATED WITH THE ROUTE ACTIVITY (SPELLED activity_route.xml DUE TO NAME CHANGE)
public class LiveRoutesActivity extends AppCompatActivity {
    private static boolean finishedParsingRealTime;
    private static HashMap<String, ArrayList<ArrayList<GTFSParser.Prediction>>> subwayRealTimeData;
    private static HashMap<String, GTFSParser.Stop> subwayStops;
    private static HashMap<String, GTFSParser.Route> subwayRoutes;
    private static HashMap<Button, String> routeButtons;
    ScrollView scrollView;
    LinearLayout innerRelativeLayout;
    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        finishedParsingRealTime = false;
        innerRelativeLayout = (LinearLayout)findViewById(R.id.innerLayout);
        homeButton = (Button) findViewById(R.id.homeButton);
        subwayStops = GTFSParser.getSubwayStops();
        subwayRoutes = GTFSParser.getSubwayRoutes();
        routeButtons = new HashMap<Button, String>();
        try {
            GTFSParser.makeSubwayRealTimeData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Handler h = new Handler();
        final Runnable r = new Runnable() {
            public void run(){
                h.postDelayed(this, 2000);
                while(finishedParsingRealTime){
                    Log.d("Message: ", "Real Time Data Finished Parsing");
                    finishedParsingRealTime = false;
                    subwayRealTimeData = GTFSParser.getSubwayRealTimeData();
                    Log.d("Size of realtimedata", String.valueOf(subwayRealTimeData.size()));
                    for(Map.Entry<String, ArrayList<ArrayList<GTFSParser.Prediction>>> e : subwayRealTimeData.entrySet()){
                        String key = e.getKey();//First list out all the routes
                        Button routeDisplay = new Button(getApplicationContext());
                        routeButtons.put(routeDisplay, key);
                        routeDisplay.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(LiveRoutesActivity.this, LiveFeedActivity.class);
                                LiveFeedActivity.route = routeButtons.get(v);
                                h.removeCallbacksAndMessages(null);
                                startActivity(intent);
                            }
                        });
                        GTFSParser.Route route = subwayRoutes.get(key);
                        String routeShortName = route.routeShortName;
                        String routeLongName = route.routeLongName;
                        //Log.d(routeShortName, routeLongName);
                        routeDisplay.setText(routeShortName + " - " + routeLongName);
                        routeDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
                        innerRelativeLayout.addView(routeDisplay);
                        Log.d("key", key);

                    }
                }

            }
        };
        h.postDelayed(r, 2000);
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LiveRoutesActivity.this, HomeActivity.class);
                h.removeCallbacks(r);
                startActivity(intent);
            }
        });
    }

    public static void setFinishedState(boolean finished){
        finishedParsingRealTime = finished;
    }
}
