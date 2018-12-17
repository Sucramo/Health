package com.example.marcu.health;

import android.app.assist.AssistStructure;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


/**
 * Class handles Bluetooth connection.
 * The connection is limited to the smart accessory that we have created.
 */
public class BluetoothHandler {

    BluetoothSocket mBTSocket;
    BluetoothDevice btDevice;

    InputStream iStream;
    OutputStream oStream;
    private volatile boolean stopListening;

    public byte[] packetBytes;
    public String data = new String();

    public String dataStored = new String();

    private byte[] readBuffer;
    private int readBufferIndex;
    private BluetoothAdapter mBtAdapter;


    /**
     * This connects the Arduino to a Bluetoothsocket
     * @throws IOException
     */
    public void connectArduino() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        mBTSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
        mBTSocket.connect();
        oStream = mBTSocket.getOutputStream();
        iStream = mBTSocket.getInputStream();
        ackListener();
    }

    /**
     *
     * @return true if it finds the Arduino
     */
    public boolean findArduino(){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        /*
        if (mBtAdapter == null){
            Toast.makeText(getBaseContext(),"No Bluetooth adapter available", Toast.LENGTH_SHORT).show();
        }*/

        /*
        if(!mBtAdapter.isEnabled()){
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, 0);
            Toast.makeText(getBaseContext(),"Turn on Bluetooth and try again", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices){
                String devName = device.getName();
                devName = devName.replaceAll(" (\\r|\\n) ", "");

                if (devName.equals("Healthkit")){
                    btDevice = device;
                    return true;
                }
            }
        }

        return false;
    }

    //Listens for data via Bluetooth and displays the data on the TextView "heartrate"
    public void ackListener() {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopListening = false;
        readBufferIndex = 0;
        readBuffer = new byte[1024];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopListening){
                    try{
                        int input = iStream.available();
                        if (input > 0){
                            packetBytes = new byte[input];
                            iStream.read(packetBytes);
                            for (int i = 0; i < input; i++){
                                byte b = packetBytes[i];

                                if (b == '>'){
                                    byte[] encodedBytes = new byte[readBufferIndex];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                    data = new String(encodedBytes, "US-ASCII");
                                    readBufferIndex = 0;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            dataStored = data;
                                        }
                                    });
                                }
                                else{
                                    readBuffer[readBufferIndex++] = b;
                                }
                            }

                        }
                    } catch (IOException e){
                        stopListening = true;
                    }
                }
            }
        });
        thread.start();

    }

    public String getData() {
        return dataStored;
    }
}
