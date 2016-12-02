package com.example.bernard.udoomiciot;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public void onResume() { // activity is in foreground (running state)
        super.onResume();
        new startRecordingAsync().execute();
    }
    @Override
    public void onPause() {  // activity is not in the foreground but still alive
        super.onPause();
    }
    @Override
    public void onDestroy() { // activity is about to be destroyed
        super.onDestroy();  // call super class implementation of onDestroy
    }


    public void startRecording(View v) {
        new startRecordingAsync().execute();
    }


    private class startRecordingAsync extends AsyncTask<Void, String, Void> {
        //start recording data and sending data here
        //TODO:
        //getAveAmp <- EIROS HERE
        //getTime
        //send

        private boolean running = true;

        public void pause(){
            running = false;
        }

        protected Void doInBackground(Void... params) {
            while (running){
                //getTime()
                //getAveAmp()
                //send(String identifier) -> Send to url specified with identifier, and the parameters obtained in getTime and getAveAmp
                publishProgress("some message");

            }
            return null;
        }

        //EIROS THIS IS YOUR SPACE DO YOUR JOB LOLOLOL
        //returns the average amplitude of sound received in the past 1 second. Implements the getAmp() method
        public Double getAveAmp() {
            return 0.0;
        }


        protected void onProgressUpdate(String... progress) {
            //TODO:
            //update the textview with the latest amplitude
        }
    }
}