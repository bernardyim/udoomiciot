package com.example.bernard.udoomiciot;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import static android.os.SystemClock.uptimeMillis;

public class MainActivity extends AppCompatActivity {
    final static int udooID = 2;
    final static String herokuUrl = "http://www.dummyurl12341515.com";

    private MediaRecorder mediaRecorder = null;

    public void start(){
        if (mediaRecorder == null){
            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.setOutputFile("/dev/null");
                mediaRecorder.prepare();
                mediaRecorder.start();
                mediaRecorder.getMaxAmplitude();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public double getAmplitude(){
        if (mediaRecorder != null){
            return mediaRecorder.getMaxAmplitude();
        } else {
            return 0;
        }
    }

    public double getAveAmplitude(ArrayList<Double> array){
        double total = 0;
        for(double val:array){
            total += val;
        }
        return total/array.size();
    }

    // Value obtained by getMaxAmplitude is arbitrary ranging from 0-32767
    public double toDecibel(double input){
        double REFERENCE = 0.00002;
        double pressure = input/51805.5336;
        return (20*Math.log10(pressure/REFERENCE));
    }

    private void updateAmplitude(String input){
        final TextView ampData = (TextView) findViewById(R.id.ampData);
        ampData.setText(input);
    }

    private void updateServer(double aveAmplitude){
        Socket socket = null;
        OutputStream outputStream;
        try {
            socket = new Socket(herokuUrl, 80);
            String udoomsg = "time="+uptimeMillis()+";aveAmp="+aveAmplitude;
            //send(String identifier) -> Open socket connection to url specified with identifier, and the parameters obtained in getTime and getAveAmp
            /*
            outputStream = socket.getOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            printStream.print(udoomsg);
            printStream.close();
            */
            //update to textview that which we published
            TextView logData = (TextView)findViewById(R.id.logData);
            logData.setText(udoomsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView deviceData = (TextView)findViewById(R.id.deviceData);
        deviceData.setText(Integer.toString(udooID));

        start();

        final ArrayList<Double> ampBuffer = new ArrayList<>();

        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(200);
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                double amp = getAmplitude();
                                double output;
                                ampBuffer.add(amp);
                                if(ampBuffer.size()<6){
                                    output = ampBuffer.get(ampBuffer.size()-1);
                                } else {
                                    ampBuffer.remove(0);
                                    output = getAveAmplitude(ampBuffer);
                                }
                                double dbOutput = toDecibel(output);
                                updateAmplitude(String.valueOf(dbOutput));
                                //updateServer(dbOutput);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();

    }


    /////////////////////////////////////////////


//    public void startRecording(View v) {
//        new startRecordingAsync().execute();
//    }
//
//
//    private class startRecordingAsync extends AsyncTask<Void, String, Void> {
//        //start recording data and sending data here
//        //TODO:
//        //getAveAmp <- EIROS HERE
//
//        private boolean running = true;
//
//        protected Void doInBackground(Void... params) {
//            Socket socket = null;
//            OutputStream outputStream;
//            try {
//                socket = new Socket(herokuUrl,80);
//                while (running){
//                //getTime(), getAveAmp(), and parse message all in one! :)
//                String udoomsg = "time="+uptimeMillis()+";aveAmp="+getAveAmp();
//
//                //send(String identifier) -> Open socket connection to url specified with identifier, and the parameters obtained in getTime and getAveAmp
//
//                    /*
//                    outputStream = socket.getOutputStream();
//                    PrintStream printStream = new PrintStream(outputStream);
//                    printStream.print(udoomsg);
//                    printStream.close();
//                    */
//
//                    //update to textview that which we published
//                    publishProgress(udoomsg);
//
//                }
//
//            } catch (IOException e) {e.printStackTrace(); System.out.print("Prolly cannot socket");}
//            return null;
//        }
//
//        //EIROS THIS IS YOUR SPACE DO YOUR JOB LOLOLOL
//        //returns the average amplitude of sound received in the past 1 second. Implements the getAmp() method
//        public Double getAveAmp() {
//            return 0.0;
//        }
//
//        protected void onProgressUpdate(String... progress) {
//            //update the textview with the latest amplitude
//            TextView logData = (TextView)findViewById(R.id.logData);
//            logData.setText(progress[0]);
//        }
//    }
}