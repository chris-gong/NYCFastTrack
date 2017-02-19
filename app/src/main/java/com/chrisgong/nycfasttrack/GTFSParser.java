package com.chrisgong.nycfasttrack;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.transit.realtime.GtfsRealtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by User on 2/17/2017.
 */

public class GTFSParser {
    private static HashMap<String, Route> subwayRoutes; //key is routeID, value is Route object holding both route names
    private static HashMap<String, Stop> subwayStops; //key is stopID, value is Stop object holding stop name and geopoints
    //key is routeID, value is an arraylist of arraylists(arraylist to preserve order of stops for a route and arraylist of prediction times for each stop)
    //each route is techically two routes, the normal route and the route in reverse
    private static HashMap<String, ArrayList<ArrayList<Prediction>>> subwayRealTimeData;
    //make two priorityqueues per route when dispaying stops and time until train arrives at stop by going through hashmap arraylist of arraylists to analyze direction
    private static ArrayList<String> urlStrings;
    private static HashMap<String, ArrayList<StopTime>> subwayStopTimes; //key is route (differentiate between north and south), value is a stopTime object

    public class PredictionComparator implements Comparator<Prediction>
    {
        @Override
        public int compare(Prediction p1, Prediction p2)
        {

            if (p1.minutes < p2.minutes)
            {
                return -1;
            }
            if (p1.minutes > p2.minutes)
            {
                return 1;
            }
            return 0;
        }
    }

    public class Route {
        String routeShortName;
        String routeLongName;
        private Route(String shortName, String longName){
            routeShortName = shortName;
            routeLongName = longName;
        }
    }
    public class Stop {
        String stopName;
        double latitude;
        double longitude;
        private Stop(String stopName, double latitude, double longitude){
            this.stopName = stopName;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
    public class StopTime{
        String tripID;
        String stopID;
        char direction;
        String timeString; //stop_time.txt uses 1 to 24 clock hour system
        long minutes;
        public StopTime(String tripID, String stopID, char direction, String timeString, long minutes){
            this.tripID = tripID;
            this.stopID = stopID;
            this.direction = direction;
            this.timeString = timeString;
            this.minutes = minutes;
        }

    }
    private class PredictionQueue {
        PriorityQueue<Prediction> queue;
        String stopID;
    }
    public class Prediction {
        String stopID;
        int minutes; //will denote how many minutes until the train arrives at the stop
        int seconds;
        private Prediction(String stopID, int minutes){
            this.stopID = stopID;
            this.minutes= minutes;
        }


    }
    public static void makeSubwayRealTimeData() throws IOException {
        new asyncTaskSubwayRealTimeData().execute();
    }
    public static void parseRoutes(Context context) throws IOException {
        subwayRoutes = new HashMap<String, Route>();
        AssetManager assetManager = context.getAssets();
        InputStream routesFile = assetManager.open("routes.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(routesFile));
        String line = br.readLine(); //skip the first line of text file
        while ((line = br.readLine()) != null){
            int firstComma = line.indexOf(",");
            String routeID = line.substring(0, firstComma);
            int secondComma = line.indexOf(",", firstComma + 1);
            int thirdComma = line.indexOf(",", secondComma + 1);
            int fourthComma = line.indexOf(",", thirdComma + 1);
            String routeShortName = line.substring(secondComma + 1, thirdComma);
            String routeLongName = line.substring(thirdComma + 1, fourthComma);
            GTFSParser parser = new GTFSParser();
            subwayRoutes.put(routeID, parser.new Route(routeShortName, routeLongName));
        }
    }
    public static void parseStops(Context context) throws IOException{
        subwayStops = new HashMap<String, Stop>();
        AssetManager assetManager = context.getAssets();
        InputStream stopsFile = assetManager.open("stops.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stopsFile));
        String line = br.readLine(); //skip the first line of text file
        while ((line = br.readLine()) != null){
            int firstComma = line.indexOf(",");
            String stopID = line.substring(0, firstComma);
            int secondComma = line.indexOf(",", firstComma + 1);
            int thirdComma = line.indexOf(",", secondComma + 1);
            int fourthComma = line.indexOf(",", thirdComma + 1);
            int fifthComma = line.indexOf(",", fourthComma + 1);
            int sixthComma = line.indexOf(",", fifthComma + 1);
            String stopName = line.substring(secondComma + 1, thirdComma);
            double lat = Double.parseDouble(line.substring(fourthComma + 1, fifthComma));
            double lon = Double.parseDouble(line.substring(fifthComma + 1, sixthComma));
            GTFSParser parser = new GTFSParser();
            subwayStops.put(stopID, parser.new Stop(stopName, lat, lon));
        }
    }/*
    public static void parseStopTimes(Context context) throws IOException{
        subwayStopTimes = new HashMap<String, ArrayList<StopTime>>();
        AssetManager assetManager = context.getAssets();
        InputStream stopsFile = assetManager.open("stop_times.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stopsFile));
        String line = br.readLine(); //skip the first line of text file
        while ((line = br.readLine()) != null){
            int firstUnderscore = line.indexOf("_");
            int secondUnderscore = line.indexOf("_", firstUnderscore + 1);
            int period = line.indexOf(".");
            String routeID = line.substring(secondUnderscore + 1, period);
            int firstComma = line.indexOf(",");
            String tripID = line.substring(0, firstComma);
            int secondComma = line.indexOf(",", firstComma + 1);
            int thirdComma = line.indexOf(",", secondComma + 1);
            int fourthComma = line.indexOf(",", thirdComma + 1);
            String timeString = line.substring(firstComma + 1, secondComma);
            String stopID = line.substring(thirdComma + 1, fourthComma);
            char direction = stopID.charAt(stopID.length()-1);
            GTFSParser parser = new GTFSParser();
            if (!subwayStopTimes.containsKey(routeID)){
                subwayStopTimes.put(routeID, new ArrayList<StopTime>());
            }
            subwayStopTimes.get(routeID).add(parser.new StopTime(tripID, stopID, direction, timeString));
        }
    }*/
    public static void printRoutes(){
        for(Map.Entry<String, Route> e : subwayRoutes.entrySet()){
            String key = e.getKey();
            Route value = e.getValue();
            Log.d("Route id: " + key, "Route short name " + value.routeShortName + " and route long name " + value.routeLongName);
        }
    }

    public static void printStops(){
        for(Map.Entry<String, Stop> e : subwayStops.entrySet()){
            String key = e.getKey();
            Stop value = e.getValue();
            Log.d("Stop id: " + key, "Stop name " + value.stopName + " latitude " + value.latitude + " longitude " + value.longitude);
        }
    }

    public static void printStopTimes(){
        for(Map.Entry<String, ArrayList<StopTime>> e :subwayStopTimes.entrySet()){
            String key = e.getKey();
            ArrayList<StopTime> value = e.getValue();
            for(StopTime st : value){
                Log.d(key, st.tripID + " " + st.stopID + " " + st.direction + " " + st.timeString);
            }
        }
    }
    //use for System.currentTimeMillis()
    public static String convertThirteenDigitTimeStampToDate(long time){
        java.sql.Timestamp stamp = new java.sql.Timestamp(time);
        java.sql.Date date = new java.sql.Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd h:mm a");
        String timeString = sdf.format(date);
        return timeString;
    }
    //use for gtfs timestamps
    public static String convertTenDigitTimeStampToDate(long time){
        java.sql.Timestamp stamp = new java.sql.Timestamp(time);
        java.sql.Date date = new java.sql.Date(stamp.getTime() * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd h:mm a");
        String timeString = sdf.format(date);
        //Log.d("currentmills", String.valueOf(System.currentTimeMillis()));
        //Log.d("timestamp", stamp.toString());
        //Log.d("date", timeString);
        return timeString;
    }
    // calculates the number of minutes train will arrive from now
    public static int calculateMinutesFromNow(String timeString){
        String currentTime = convertThirteenDigitTimeStampToDate(System.currentTimeMillis());
        //Log.d("Now is ", currentTime);
        //Log.d("Time String is ", timeString);
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd h:mm a");
        java.util.Date now = null;
        java.util.Date input = null;
        try {
            now = sdf.parse(currentTime);
            input = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = input.getTime() - now.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        return (int) diffMinutes;
    }
    public static int calculateSecondsFromNow(String timeString){
        String currentTime = convertThirteenDigitTimeStampToDate(System.currentTimeMillis());

        return 0;
    }
    private static class asyncTaskStopTimes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");
            subwayRealTimeData = new HashMap<String, ArrayList<ArrayList<Prediction>>>();
            urlStrings = new ArrayList<String>();
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=16");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=21");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=2");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=11");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            // must do network tasks/web requests in this asynctask method
            for(String urlString : urlStrings) {
                try {
                    //String urlString = "http://datamine.mta.info/files/k38dkwh992dk/gtfs"; // this link is the same as the one below
                    //Log.d("testing size of list", String.valueOf(urlStrings.size()));
                    //String urlString = "http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e";
                    URL url = new URL(urlString.toString());
                    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());

                    for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                        if (entity.hasTripUpdate()) {
                            String tripUpdate = entity.getTripUpdate().toString();
                            BufferedReader br = new BufferedReader(new StringReader(tripUpdate));
                            String line = null;
                            String currentRoute = "";
                            int currentMinutes = 0;
                            //boolean firstEntry = true;
                            //int currentSecondsUntilNow = 0;
                            while ((line = br.readLine()) != null) {
                                line = line.trim();

                                if (line.contains("route_id")) {
                                    int firstDoubleQuoteIndex = line.indexOf("\"");
                                    int secondDoubleQuoteIndex = line.indexOf("\"", firstDoubleQuoteIndex + 1);
                                    currentRoute = line.substring(firstDoubleQuoteIndex + 1, secondDoubleQuoteIndex);
                                    //Log.d("Route line", line);
                                    if (!subwayRealTimeData.containsKey(currentRoute)) {
                                        subwayRealTimeData.put(currentRoute, new ArrayList<ArrayList<Prediction>>());
                                        //Log.d("Route ID ", currentRoute);
                                    }

                                    subwayRealTimeData.get(currentRoute).add(new ArrayList<Prediction>());
                                } else if (line.contains("time:")) {
                                    int spaceIndex = line.indexOf(" ");
                                    long tenDigitTime = Long.parseLong(line.substring(spaceIndex + 1));
                                    String timeString = convertTenDigitTimeStampToDate(tenDigitTime);
                                    currentMinutes = calculateMinutesFromNow(timeString);
                                    //Log.d("time until", String.valueOf(currentMinutes));
                                    //currentSecondsUntilNow = calculateSecondsFromNow(timeString);
                                } else if (line.contains("stop_id")) {
                                    int doubleQuoteIndex = line.indexOf("\"");
                                    int secondDoubleQuoteIndex = line.indexOf("\"", doubleQuoteIndex + 1);
                                    String stopID = line.substring(doubleQuoteIndex + 1, secondDoubleQuoteIndex);
                                    GTFSParser parser = new GTFSParser();
                                    subwayRealTimeData.get(currentRoute).get(subwayRealTimeData.get(currentRoute).size() - 1).add(parser.new Prediction(stopID, currentMinutes));
                                }
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            //Log.d("Current Time", convertTimeStampToDate(System.currentTimeMillis()));
            LiveRoutesActivity.setFinishedState(true);
        }
    }
    private static class asyncTaskSubwayRealTimeData extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");
            subwayRealTimeData = new HashMap<String, ArrayList<ArrayList<Prediction>>>();
            urlStrings = new ArrayList<String>();
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=16");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=21");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=2");
            urlStrings.add("http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e&feed_id=11");
        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            // must do network tasks/web requests in this asynctask method
            for(String urlString : urlStrings) {
                try {
                    //String urlString = "http://datamine.mta.info/files/k38dkwh992dk/gtfs"; // this link is the same as the one below
                    //Log.d("testing size of list", String.valueOf(urlStrings.size()));
                    //String urlString = "http://datamine.mta.info/mta_esi.php?key=d5127c6c6b646cd6a2816be886bda12e";
                    URL url = new URL(urlString.toString());
                    GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream());

                    for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                        if (entity.hasTripUpdate()) {
                            String tripUpdate = entity.getTripUpdate().toString();
                            BufferedReader br = new BufferedReader(new StringReader(tripUpdate));
                            String line = null;
                            String currentRoute = "";
                            int currentMinutes = 0;
                            //boolean firstEntry = true;
                            //int currentSecondsUntilNow = 0;
                            while ((line = br.readLine()) != null) {
                                line = line.trim();

                                if (line.contains("route_id")) {
                                    int firstDoubleQuoteIndex = line.indexOf("\"");
                                    int secondDoubleQuoteIndex = line.indexOf("\"", firstDoubleQuoteIndex + 1);
                                    currentRoute = line.substring(firstDoubleQuoteIndex + 1, secondDoubleQuoteIndex);
                                    //Log.d("Route line", line);
                                    if (!subwayRealTimeData.containsKey(currentRoute)) {
                                        subwayRealTimeData.put(currentRoute, new ArrayList<ArrayList<Prediction>>());
                                        //Log.d("Route ID ", currentRoute);
                                    }

                                    subwayRealTimeData.get(currentRoute).add(new ArrayList<Prediction>());
                                } else if (line.contains("time:")) {
                                    int spaceIndex = line.indexOf(" ");
                                    long tenDigitTime = Long.parseLong(line.substring(spaceIndex + 1));
                                    String timeString = convertTenDigitTimeStampToDate(tenDigitTime);
                                    currentMinutes = calculateMinutesFromNow(timeString);
                                    //Log.d("time until", String.valueOf(currentMinutes));
                                    //currentSecondsUntilNow = calculateSecondsFromNow(timeString);
                                } else if (line.contains("stop_id")) {
                                    int doubleQuoteIndex = line.indexOf("\"");
                                    int secondDoubleQuoteIndex = line.indexOf("\"", doubleQuoteIndex + 1);
                                    String stopID = line.substring(doubleQuoteIndex + 1, secondDoubleQuoteIndex);
                                    GTFSParser parser = new GTFSParser();
                                    subwayRealTimeData.get(currentRoute).get(subwayRealTimeData.get(currentRoute).size() - 1).add(parser.new Prediction(stopID, currentMinutes));
                                }
                            }

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer...a){
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            Log.d(TAG + " onPostExecute", "" + result);
            //Log.d("Current Time", convertTimeStampToDate(System.currentTimeMillis()));
            LiveRoutesActivity.setFinishedState(true);
        }
    }
    public static HashMap<String, Route> getSubwayRoutes(){
        return subwayRoutes;
    }
    public static HashMap<String, Stop> getSubwayStops(){
        return subwayStops;
    }
    public static HashMap<String, ArrayList<StopTime>> getSubwayStopTimes() {return subwayStopTimes;}
    public static HashMap<String, ArrayList<ArrayList<Prediction>>> getSubwayRealTimeData(){
        return subwayRealTimeData;
    }

}
