package com.example.marcu.health;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;

public class StopWatchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    ImageButton buttonStartOne, buttonStartTwo, buttonPause, buttonSave;
    ImageView heartRate, clock;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stopwatch);

        chronometer = findViewById(R.id.chronometer);
        buttonStartOne = (ImageButton) findViewById(R.id.start_button_one);
        buttonStartTwo = (ImageButton) findViewById(R.id.start_button_two);
        buttonPause = (ImageButton) findViewById(R.id.pause_button);
        buttonSave = (ImageButton) findViewById(R.id.save_button);
        heartRate = (ImageView) findViewById(R.id.heart_rate_icon);
        clock = (ImageView) findViewById(R.id.clock_icon);

        heartRate.setImageResource(R.drawable.ic_favorite_black_24dp);
        clock.setImageResource(R.drawable.ic_timer_black_24dp);

    }

    public void startChronometer(View view) {
        if(!running) {
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
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;

            buttonStartOne.setVisibility(View.INVISIBLE);
            buttonStartTwo.setVisibility(View.VISIBLE);
            buttonPause.setVisibility(View.INVISIBLE);
        }

    }

    public void resetChronometer(View view) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        running = false;

        buttonStartOne.setVisibility(View.VISIBLE);
        buttonStartTwo.setVisibility(View.INVISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.INVISIBLE);

    }

}
