package com.example.marcu.health;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Date;

public class StopWatchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    Button buttonStart, buttonPause, buttonReset;
    private static ArrayList<Integer> al = new ArrayList<>();

    //RIGHT NOW THE SECONDS IS USED AS MINUTES FOR THE SAKE OF TESTING


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stopwatch);

        chronometer = findViewById(R.id.chronometer);
        buttonStart = (Button) findViewById(R.id.start_button);
        buttonPause = (Button) findViewById(R.id.pause_button);
        buttonReset = (Button) findViewById(R.id.reset_button);

    }

    public void startChronometer(View view) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;


            buttonStart.setVisibility(View.INVISIBLE);
            buttonPause.setVisibility(View.VISIBLE);
            buttonReset.setVisibility(View.VISIBLE);

        }

    }

    public void pauseChronometer(View view) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;

            System.out.println(pauseOffset);

            buttonStart.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.INVISIBLE);
        }

    }

    public void resetChronometer(View view) {
        chronometer.stop();

        //This is for getting the ACWR
        int seconds = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        int HR = getRandomHR();
        System.out.println("seconds: " + seconds);
        System.out.println("HR: " + HR);
        MathActivity mathActivity = new MathActivity();
        double ACWR = mathActivity.getACWR(seconds, HR, al);


        chronometer.setBase(SystemClock.elapsedRealtime());

        pauseOffset = 0;
        running = false;

        buttonStart.setVisibility(View.VISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);


    }

    private static int getRandomHR() {

        return (int) (Math.random() * ((200 - 60) + 1)) + 60;
    }
}
