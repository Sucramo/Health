package com.example.marcu.health;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {


    EditText editTextUserName;
    EditText editTextAge;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        editTextUserName = findViewById(R.id.username);
        editTextAge = findViewById(R.id.age);
        editTextUserName.setEnabled(false);
        editTextAge.setEnabled(false);

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

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(savedInstanceState, outPersistentState);
    }




    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



}
