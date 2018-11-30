package com.example.marcu.health;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;

import java.util.ArrayList;

public class StopWatchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    ImageButton buttonStartOne, buttonStartTwo, buttonPause, buttonSave;
    private static ArrayList<Integer> al = new ArrayList<>();
    Context context;

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



        //Bottomnavigation stuff
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_tracking:
                        Intent intent2 = new Intent(getApplicationContext(), StopWatchActivity.class);
                        startActivity(intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        break;
                    case R.id.action_history:
                        Intent intent3 = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
