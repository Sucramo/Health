package com.example.marcu.health;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.Locale;


public class StopWatchActivity extends AppCompatActivity {
    private static final String TAG = "StopWatchActivity";
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    ImageButton buttonStartOne, buttonStartTwo, buttonPause, buttonSave;
    TextView textViewACWR;

    Handler h;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    private CustomSeekBar seekbar;
    private static DecimalFormat df = new DecimalFormat("#.##");
    public static DatabaseHelper databaseHelper;
    static ArrayList<Integer> listData;
    static ArrayList<String> listDataDates;
    //private static int daysBetween;

    public BluetoothDevice btDevice;

    public byte[] packetBytes;

    private byte[] readBuffer;
    private int readBufferIndex;

    private TextView heartRate;
    private InputStream iStream;
    private boolean stopListening;

    //RIGHT NOW THE SECONDS IS USED AS MINUTES FOR THE SAKE OF TESTING
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_stopwatch);

        chronometer = findViewById(R.id.chronometer);

        //Bottomnavigation stuff
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        buttonStartOne = findViewById(R.id.start_button_one);
        buttonStartTwo = findViewById(R.id.start_button_two);
        buttonPause = findViewById(R.id.pause_button);
        buttonSave = findViewById(R.id.save_button);
        Button bt_button = findViewById(R.id.bt_button);
        heartRate = findViewById(R.id.heartrate);
        textViewACWR = findViewById(R.id.text_view_acwr);
        seekbar = findViewById(R.id.seekBar);
        initDataToSeekbar();
        seekbar.setEnabled(false);
        //Button button = findViewById(R.id.dbButton);
        databaseHelper = new DatabaseHelper(this);

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

        /*
        // Button for opening database
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent dbmanager = new Intent(StopWatchActivity.this, AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
        */

        //Button connects only to the smart accessory we have set up
        bt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findArduino();
                try {
                    connectArduino();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean findArduino() {

        try {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "No Bluetooth adapter available", Toast.LENGTH_SHORT).show();
        }

            if (!mBtAdapter.isEnabled()) {
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT, 0);
                Toast.makeText(getBaseContext(), "Turn on Bluetooth and try again", Toast.LENGTH_SHORT).show();
                return false;
            }

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String devName = device.getName();
                devName = devName.replaceAll(" (\\r|\\n) ", "");

                //Checks for the arduino board
                if (devName.equals("Healthkit")) {
                    btDevice = device;
                    Toast.makeText(getBaseContext(), "Healthkit connected", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        } catch (NullPointerException e) {
            Toast.makeText(getBaseContext(), "Bluetooth device NOT found", Toast.LENGTH_LONG).show();
        }


        return false;
    }


    private void connectArduino() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        BluetoothSocket mBTSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
        mBTSocket.connect();
        OutputStream oStream = mBTSocket.getOutputStream();
        iStream = mBTSocket.getInputStream();
        ackListener();
        Toast.makeText(getBaseContext(), "Bluetooth connection established", Toast.LENGTH_LONG).show();
    }


    //Listens for data via Bluetooth and displays the data on the TextView "heartrate"
    private void ackListener() {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopListening = false;
        readBufferIndex = 0;
        readBuffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopListening) {
                    try {
                        int input = iStream.available();
                        if (input > 0) {
                            packetBytes = new byte[input];
                            iStream.read(packetBytes);
                            for (int i = 0; i < input; i++) {
                                byte b = packetBytes[i];

                                if (b == '>') {
                                    final byte[] encodedBytes = new byte[readBufferIndex];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    final String data = new String(encodedBytes);
                                    final int var = Integer.parseInt(data);
                                    readBufferIndex = 1;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //int var = Integer.decode(data);
                                            heartRate.setText(var);
                                            System.out.println(data + "byte array: " + encodedBytes);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferIndex++] = b;
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    heartRate.setText("" + packetBytes[0]);
                                }
                            });
                        }
                    } catch (IOException e) {
                        stopListening = true;
                    }
                }
            }
        });
        thread.start();
    }

    //Important to read
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private void initDataToSeekbar() {
        ArrayList<ProgressItem> progressItemList = new ArrayList<>();
        // red span
        ProgressItem mProgressItem = new ProgressItem();
        float totalSpan = 20;
        float redSpan = 8;
        mProgressItem.progressItemPercentage = ((redSpan / totalSpan) * 100);
        Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);
        // green span
        mProgressItem = new ProgressItem();
        float greenSpan = 5;
        mProgressItem.progressItemPercentage = (greenSpan / totalSpan) * 100;
        mProgressItem.color = R.color.green;
        progressItemList.add(mProgressItem);
        // red span 2
        mProgressItem = new ProgressItem();
        float redspan2 = 7;
        mProgressItem.progressItemPercentage = (redspan2 / totalSpan) * 100;
        mProgressItem.color = R.color.red;
        progressItemList.add(mProgressItem);

        seekbar.initData(progressItemList);
        seekbar.invalidate();
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

    public void resetChronometer(View view) throws ParseException {
        chronometer.stop();

        //This is for getting the ACWR
        MathActivity mathActivity = new MathActivity();
        int seconds = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
        int HR = mathActivity.getRandomHR();
        System.out.println("seconds: " + seconds);
        System.out.println("HR: " + HR);
        int workLoadCurrentDay = (seconds * (HR / 10)) + 1;
        addTrainingToDatabase(workLoadCurrentDay);

        setSeekbar(mathActivity.getACWR(mathActivity.getAcuteWorkload(getAl()), mathActivity.getChronicWorkload(getAl())));
        Log.d(TAG, "Al from database: " + getAl());

        chronometer.setBase(SystemClock.elapsedRealtime());

        pauseOffset = 0;
        running = false;

        buttonStartOne.setVisibility(View.VISIBLE);
        buttonStartTwo.setVisibility(View.INVISIBLE);
        buttonPause.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.INVISIBLE);

    }

    public static ArrayList<Integer> getAl() {
        Cursor cursor = databaseHelper.getData();
        listData = new ArrayList<>();
        while (cursor.moveToNext()) {
            listData.add(cursor.getInt(1));
        }
        return listData;
    }

    public static ArrayList<String> getDateAl() {
        Cursor cursor = databaseHelper.getData();
        listDataDates = new ArrayList<>();
        while (cursor.moveToNext()) {
            listDataDates.add(cursor.getString(2));
        }
        return listDataDates;
    }

    public void addTrainingToDatabase(int workloadCurrentDay) throws ParseException {
        ArrayList<Integer> al = getAl();
        if (al.size() == 0) {
            databaseHelper.addData(workloadCurrentDay);
        } else {
            MathActivity mathActivity = new MathActivity();
            Date oldDate = simpleDateFormat.parse(getDateAl().get(getDateAl().size() - 1));

            System.out.println("old date: " + oldDate);
            System.out.println("new date: " + getNewDate());
            System.out.println("days between old and new date: " + mathActivity.getDaysBetween(oldDate, getNewDate()));
            if (mathActivity.getDaysBetween(oldDate, getNewDate()) == 0) {
                int AuCurrentDay = al.get(al.size() - 1);
                databaseHelper.updateAu(workloadCurrentDay + AuCurrentDay, al.size());
            } else {
                for (int i = 0; i < mathActivity.getDaysBetween(oldDate, getNewDate()) - 1; i++) {
                    databaseHelper.addData(0);
                }
                databaseHelper.addData(workloadCurrentDay);
            }
        }
    }

    private static Date getNewDate() {
        return new Date();
    }

    public void setSeekbar(double ACWR) {
        if (Double.isNaN(ACWR)) {
            String daysLeft = Integer.toString(28 - getAl().size());
            textViewACWR.setText(daysLeft + " days left");
        } else {
            String StringACWR = df.format(Double.valueOf(ACWR));
            textViewACWR.setText(StringACWR);
            int percentageACWR = (int) (ACWR * 100) / 2;
            seekbar.setProgress(percentageACWR);
        }
    }
}
