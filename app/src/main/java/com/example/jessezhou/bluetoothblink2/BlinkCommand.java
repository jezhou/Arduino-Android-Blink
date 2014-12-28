package com.example.jessezhou.bluetoothblink2;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Jesse Zhou on 12/27/2014.
 */
public class BlinkCommand{

    OutputStream sender;

    public BlinkCommand(BluetoothSocket btSocket){
        try {
            sender = btSocket.getOutputStream();
        }
        catch(IOException e){}
    }

    public void blinkOn(){
        try {
            sender.write(1);
            sender.flush();
        }
        catch(IOException e){}
    }

    public void blinkOff(){
        try {
            sender.write(0);
            sender.flush();
        }
        catch(IOException e){}
    }
}
