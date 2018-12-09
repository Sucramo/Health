package com.example.marcu.health;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

class HistoryActivity extends AppCompatActivity {

    //initialization of stuff
    ArrayList listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //date and date formatter
    GregorianCalendar date = new GregorianCalendar();
    SimpleDateFormat formater = new SimpleDateFormat("EEEE, dd MMMM");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);


        //Bottomnavigation stuff
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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

        if (savedInstanceState != null) {

            listItems = savedInstanceState.getStringArrayList("savedList");

        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(savedInstanceState, outPersistentState);

        savedInstanceState.putStringArrayList("savedList", listItems);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listItems = savedInstanceState.getStringArrayList("savedList");

    }


    //method for adding data to the list
    public void addItems(View v) {

        ListView listView = findViewById(R.id.listView);


        listItems.add(0, formater.format(date.getTime()));
        date.roll(Calendar.DAY_OF_YEAR, true);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);

        //set adapter into listview
        listView.setAdapter(adapter);


        if (listItems.size() == 30) {
            listItems.remove(28);

        }


    }


    private void loadHistoryList() {
        //get data from database
    }


}