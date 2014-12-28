package com.example.jessezhou.bluetoothblink2;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Jesse Zhou on 12/26/2014.
 */
public class BlinkConnectThread extends Thread{

    // Bluetooth Serial UUID (needed for connection). The UUID should be
    // almost universal for all BT serial boards
    private final UUID btSerialBoard = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Bluetooth objects
    private BluetoothSocket btSocket;
    private BluetoothDevice btDev;

    // Handler for main thread
    private Handler handler;

    public BlinkConnectThread(BluetoothDevice remote, Handler uiHandler){
        btDev = remote;
        handler = uiHandler;

        // Create socket and attempt to connect
        try {
            btSocket = btDev.createRfcommSocketToServiceRecord(btSerialBoard);
        }
        catch(IOException e){
            handler.obtainMessage(Constants.BLUETOOTH_CONNECT, Constants.CONNECTION_FAILED, -1)
                    .sendToTarget();
        }
    }

    public void run(){

        try {
            btSocket.connect();
            handler.obtainMessage(Constants.BLUETOOTH_CONNECT, Constants.CONNECTION_SUCCESS, -1)
                    .sendToTarget();
        }
        catch(IOException e){
            handler.obtainMessage(Constants.BLUETOOTH_CONNECT, Constants.CONNECTION_FAILED, -1)
                    .sendToTarget();
        }

    }

    public BluetoothSocket getBTSocket(){
        return btSocket;
    }

    public void cancel(){
        try {
            btSocket.close();
        }
        catch(IOException e){}
    }
}
