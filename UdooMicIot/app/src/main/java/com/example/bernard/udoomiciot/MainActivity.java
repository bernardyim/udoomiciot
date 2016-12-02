package com.example.bernard.udoomiciot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //TODO:
    //getAveAmp
    //getTime
    //send(String identifier) -> Send to url specified with identifier, and the parameters obtained in getTime and getAveAmp

    //returns the average amplitude of sound received in the past 1 second. Implements the getAmp() method
    public double getAveAmp(){return 0.0;}

    public void startRecording(View v){
        //start recording data and sending data here
    }
}
