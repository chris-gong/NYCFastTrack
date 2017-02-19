package com.chrisgong.nycfasttrack;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    private static Context context;
    private static boolean finishedParsingTextFiles;
    Button routeButton;
    Button scheduleButton;
    Button calculateRouteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        routeButton = (Button) findViewById(R.id.routeButton);
        scheduleButton = (Button) findViewById(R.id.scheduleButton);
        calculateRouteButton = (Button) findViewById(R.id.calculateRouteButton);
        routeButton.setEnabled(false);
        scheduleButton.setEnabled(false);
        calculateRouteButton.setEnabled(false);
        finishedParsingTextFiles = false;
        context = getApplicationContext();
        new asyncTaskParseRoutes().execute();
        //make button disabled until text files are done parsing
        //YOU HAVE TO MAKE THE HANDLER FINAL OR ELSE YOU CAN'T REMOVE THE RUNNABLE AFTER LEAVING THE ACTIVITY
        //AND IF YOU DON'T REMOVE THE RUNNABLE THEN IT WILL CONTINUE AFTER LEAVING THE ACTIVITY
        final Handler h = new Handler();
        final Runnable r = new Runnable() {
            public void run(){
                h.postDelayed(this, 2000);
                while(finishedParsingTextFiles){
                    Log.d("Message: ", "Text Files Finished Parsing");
                    routeButton.setEnabled(true);
                    scheduleButton.setEnabled(true);
                    calculateRouteButton.setEnabled(true);
                    finishedParsingTextFiles = false;

                }
            }
        };
        h.postDelayed(r, 2000);
        routeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, LiveRoutesActivity.class);
                h.removeCallbacks(r);
                startActivity(intent);
            }
        });
        scheduleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, GeneralRoutesActivity.class);
                h.removeCallbacks(r);
                startActivity(intent);
            }
        });
        calculateRouteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this, RouteSimulationActivity.class);
                h.removeCallbacks(r);
                startActivity(intent);
            }
        });
        /*
        try {
            GTFSParser.getSubwayData();
        } catch(IOException e) {
            e.printStackTrace();
        }*/
        //GTFSParser.convertTimeStampToDate();
    }
    private static class asyncTaskParseRoutes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            try {
                GTFSParser.parseRoutes(context);
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
            GTFSParser.printRoutes();
            new asyncTaskParseStops().execute();
        }
    }
    private static class asyncTaskParseStops extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            try {
                GTFSParser.parseStops(context);
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
            Log.d("Size of stops hashmap ", String.valueOf(GTFSParser.getSubwayStops().size()));
            finishedParsingTextFiles = true;
            //new asyncTaskParseStopTimes().execute();
        }
    }/*
    private static class asyncTaskParseStopTimes extends AsyncTask<Void, Integer, String>
    {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            Log.d(TAG + " PreExceute","On pre Exceute......");

        }

        protected String doInBackground(Void...arg0) {
            Log.d(TAG + " DoINBackGround","On doInBackground...");
            try {
                GTFSParser.parseStopTimes(context);
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
            HashMap<String, ArrayList<GTFSParser.StopTime>> subwayStopTimes = GTFSParser.getSubwayStopTimes();
            //Log.d("Size of stopTimes hashmap ", String.valueOf(subwayStopTimes.size()));
            finishedParsingTextFiles = true;
            //GTFSParser.printStopTimes();
        }
    }*/
}
