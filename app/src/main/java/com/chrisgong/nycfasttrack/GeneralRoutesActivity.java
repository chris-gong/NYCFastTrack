package com.chrisgong.nycfasttrack;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.chrisgong.nycfasttrack.NYCGraph.routeLinkedLists;

public class GeneralRoutesActivity extends AppCompatActivity {

    private static HashMap<String, GTFSParser.Route> subwayRoutes;
    private static HashMap<String, GTFSParser.Stop> subwayStops;
    private static HashMap<String, ArrayList<GTFSParser.StopTime>> subwayStopTimes;
    private static HashMap<Button, String> routeButtons;
    private static HashMap<Button, String> destButtons;
    LinearLayout innerLayout;
    Button homeButton;
    private static String selectedRoute;
    private static String selectedDestination;
    private static Context context;
    private static boolean startedParsing, finishedParsing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_routes);
        if(!(NYCGraph.routeLinkedLists.size() > 0)){
            NYCGraph.makeNYCGraph();
        }
        context = getApplicationContext();
        routeButtons = new HashMap<Button, String>();
        destButtons = new HashMap<Button, String>();
        subwayRoutes = GTFSParser.getSubwayRoutes();
        subwayStops = GTFSParser.getSubwayStops();
        innerLayout = (LinearLayout) findViewById(R.id.innerLayout);
        homeButton = (Button) findViewById(R.id.homeButton);
        for(Map.Entry<String, NYCGraph.LinkedList> e : routeLinkedLists.entrySet()){
            String key = e.getKey();//First list out all the routes
            Button routeDisplay = new Button(getApplicationContext());
            routeButtons.put(routeDisplay, key);
            GTFSParser.Route route = subwayRoutes.get(key);
            String routeShortName = route.routeShortName;
            String routeLongName = route.routeLongName;
            //Log.d(routeShortName, routeLongName);
            routeDisplay.setText(routeShortName + " - " + routeLongName);
            routeDisplay.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    selectedRoute = routeButtons.get(v);
                    NYCGraph.LinkedList destinations = routeLinkedLists.get(selectedRoute);
                    innerLayout.removeAllViews();
                    TextView prompt = new TextView(getApplicationContext());
                    prompt.setText("Choose a destination to know when the next " + subwayRoutes.get(selectedRoute).routeShortName + " train is coming!");
                    prompt.setTextSize(24);
                    prompt.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    innerLayout.addView(prompt);
                    Button homeButton = new Button(getApplicationContext());
                    homeButton.setText("Go back");
                    homeButton.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
                    homeButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(GeneralRoutesActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                    innerLayout.addView(homeButton);
                    NYCGraph.Vertex curr = destinations.head;
                    while(curr != null){
                        Button destDisplay = new Button(getApplicationContext());
                        destButtons.put(destDisplay, curr.stopID);
                        destDisplay.setText(curr.stopName);
                        destDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
                        destDisplay.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                selectedDestination = destButtons.get(v);
                                for (int i = 0; i < innerLayout.getChildCount(); i++) {
                                    View child = innerLayout.getChildAt(i);
                                    child.setEnabled(false);
                                }
                                //data is skewed at night when there are zero trains
                                new asyncTaskParseStopTimes().execute();
                                final Handler h = new Handler();
                                final Runnable r = new Runnable() {
                                    public void run(){
                                        h.postDelayed(this, 2000);
                                        while(finishedParsing){
                                            Log.d("Message: ", "Stop Times Finished Parsing");
                                            TextView dataDisplay = new TextView(getApplicationContext());
                                            String southDirection = "";
                                            String northDirection = "";
                                            NYCGraph.LinkedList destList = NYCGraph.routeLinkedLists.get(selectedRoute);
                                            Log.d("size", String.valueOf(subwayStopTimes.get(selectedDestination).size()));
                                            Collections.sort(subwayStopTimes.get(selectedDestination), new Comparator<GTFSParser.StopTime>() {
                                                @Override
                                                public int compare(GTFSParser.StopTime s1, GTFSParser.StopTime s2)
                                                {
                                                    if(s1.minutes < 0 && s2.minutes >= 0){
                                                        return 1;
                                                    }
                                                    if(s1.minutes >= 0 && s2.minutes < 0){
                                                        return -1;
                                                    }
                                                    if ((s1.minutes) < (s2.minutes))
                                                    {
                                                        return -1;
                                                    }
                                                    if ((s1.minutes) > (s2.minutes))
                                                    {
                                                        return 1;
                                                    }
                                                    return 0;
                                                }
                                            });
                                            if(subwayStopTimes.get(selectedDestination).size() > 10){
                                                subwayStopTimes.put(selectedDestination, new ArrayList<GTFSParser.StopTime>(subwayStopTimes.get(selectedDestination).subList(0,10)));
                                            }
                                            for(int i = 0; i < subwayStopTimes.get(selectedDestination).size(); i++){
                                                GTFSParser.StopTime st = subwayStopTimes.get(selectedDestination).get(i);
                                                Log.d("Time of arrival", String.valueOf(st.minutes));
                                                if(st.direction == 'S'){
                                                    NYCGraph.Vertex curr = destList.head;
                                                    while(curr != null){
                                                        if(curr.stopID.equals(selectedDestination)){
                                                            break;
                                                        }
                                                        curr = curr.next;
                                                    }
                                                    if(southDirection.equals("")){
                                                        if(curr.next == null){
                                                            southDirection = "Arriving in " +String.valueOf(st.minutes) + " minutes";
                                                        }
                                                        else{
                                                            southDirection = "Arriving in " +String.valueOf(st.minutes) + " minutes to " + curr.next.stopName;
                                                        }
                                                    }
                                                    else{
                                                        int minutesIndex = southDirection.indexOf(" minutes");
                                                        southDirection = southDirection.substring(0, minutesIndex) + ", " + String.valueOf(st.minutes) + " " + southDirection.substring(minutesIndex + 1);
                                                    }
                                                }
                                                else{
                                                    NYCGraph.Vertex curr = destList.head;
                                                    NYCGraph.Vertex prev = null;
                                                    while(curr != null){
                                                        if(curr.stopID.equals(selectedDestination)){
                                                            break;
                                                        }
                                                        prev = curr;
                                                        curr = curr.next;
                                                    }
                                                    if(northDirection.equals("")){
                                                        if(prev == null){
                                                            northDirection = "Arriving in " +String.valueOf(st.minutes) + " minutes";
                                                        }
                                                        else{
                                                            northDirection = "Arriving in " +String.valueOf(st.minutes) + " minutes to " + prev.stopName;
                                                        }
                                                    }
                                                    else{
                                                        int minutesIndex = northDirection.indexOf(" minutes");
                                                        northDirection = northDirection.substring(0, minutesIndex) + ", " + String.valueOf(st.minutes) + " " + northDirection.substring(minutesIndex + 1);

                                                    }
                                                }
                                            }
                                            dataDisplay.setText(southDirection + "\n" + northDirection);
                                            dataDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
                                            //getting index of the button
                                            Button btn = new Button(getApplicationContext());
                                            for(Map.Entry<Button, String> e : destButtons.entrySet()){
                                                if(selectedDestination.equals(e.getValue())){
                                                    btn = e.getKey();
                                                    break;
                                                }
                                            }
                                            int locationOfButton = 0;
                                            for (int i = 0; i < innerLayout.getChildCount(); i++) {
                                                View child = innerLayout.getChildAt(i);
                                                if(btn == child){
                                                    locationOfButton = i;
                                                    break;
                                                }
                                            }
                                            innerLayout.addView(dataDisplay, locationOfButton + 1);
                                            for (int i = 0; i < innerLayout.getChildCount(); i++) {
                                                View child = innerLayout.getChildAt(i);
                                                child.setEnabled(true);
                                            }
                                            finishedParsing = false;
                                            h.removeCallbacksAndMessages(null);
                                            //subwayStopTimes = new HashMap<String, ArrayList<GTFSParser.StopTime>>();
                                        }
                                    }
                                };
                                h.postDelayed(r, 2000);
                            }
                        });
                        innerLayout.addView(destDisplay);
                        curr = curr.next;
                    }
                }
            });
            routeDisplay.setLayoutParams(new ViewGroup.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
            innerLayout.addView(routeDisplay);
            //Log.d("key", key);
        }
        homeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GeneralRoutesActivity.this, HomeActivity.class);

                startActivity(intent);
            }
        });
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
        }
    }

