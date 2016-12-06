package com.example.bernard.udoomiciot;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import static android.os.SystemClock.uptimeMillis;

public class MainActivity extends AppCompatActivity {
    final static int udooID = 2;
    final static String herokuUrl = "http://www.dummyurl12341515.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView deviceData = (TextView)findViewById(R.id.deviceData);
        deviceData.setText(Integer.toString(udooID));
    }

    public void startRecording(View v) {
        new startRecordingAsync().execute();
    }


    private class startRecordingAsync extends AsyncTask<Void, String, Void> {
        //start recording data and sending data here
        //TODO:
        //getAveAmp <- EIROS HERE

        private boolean running = true;

        protected Void doInBackground(Void... params) {
            Socket socket = null;
            OutputStream outputStream;
            try {
                socket = new Socket(herokuUrl,80);
                while (running){
                //getTime(), getAveAmp(), and parse message all in one! :)
                String udoomsg = "time="+uptimeMillis()+";aveAmp="+getAveAmp();
                    delay(1000);

                //send(String identifier) -> Open socket connection to url specified with identifier, and the parameters obtained in getTime and getAveAmp

                    /*
                    outputStream = socket.getOutputStream();
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print(udoomsg);
                    printStream.close();
                    */

                    //update to textview that which we published
                    publishProgress(udoomsg);

                }

            } catch (IOException e) {e.printStackTrace(); System.out.print("Prolly cannot socket");}
            return null;
        }

        //EIROS THIS IS YOUR SPACE DO YOUR JOB LOLOLOL
        //returns the average amplitude of sound received in the past 1 second. Implements the getAmp() method
        public Double getAveAmp() {
            return 0.0;
        }

        protected void onProgressUpdate(String... progress) {
            //update the textview with the latest amplitude
            TextView logData = (TextView)findViewById(R.id.logData);
            logData.setText(progress[0]);
        }
    }
}