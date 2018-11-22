package com.example.marcu.health;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Date;

public class StopWatchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    ImageButton buttonStartOne, buttonStartTwo, buttonPause, buttonSave;
    private static ArrayList<Integer> al = new ArrayList<>();

    //RIGHT NOW THE SECONDS IS USED AS MINUTES FOR THE SAKE OF TESTING


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stopwatch);

        chronometer = findViewById(R.id.chronometer);
        buttonStartOne = (ImageButton) findViewById(R.id.start_button_one);
        buttonStartTwo = (ImageButton) findViewById(R.id.start_button_two);
        buttonPause = (ImageButton) findViewById(R.id.pause_button);
        buttonSave = (ImageButton) findViewById(R.id.save_button);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        // open profile fragment
                        break;
                    case R.id.action_tracking:
                        // open tracking fragment
                        break;
                    case R.id.action_history:
                        //open history fragment
                        break;

                }
                return true;
            }
        });

    }

    public void startChronometer(View view) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;


            buttonStartOne.setVisibility(View.INVISIBLE);
            buttonStartTwo.setVisibility(View.INVISIBLE);
            buttonPause.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);

        }

    }

    public void pauseChronometer(View view) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;

            System.out.println(pauseOffset);

            buttonStartOne.setVisibility(View.INVISIBLE);
            buttonStartTwo.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.INVISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
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

        buttonStartOne.setVisibility(View.VISIBLE);
        buttonStartTwo.setVisibility(View.INVISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.INVISIBLE);


    }

    private static int getRandomHR() {

        return (int) (Math.random() * ((200 - 60) + 1)) + 60;
    }
}
