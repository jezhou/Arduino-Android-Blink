package com.example.jessezhou.bluetoothblink2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ListView;

import java.util.Set;


public class MainActivity extends ActionBarActivity {

    private Button bOn,bOff,bVisible,bList;
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void init(){
        bOn = (Button)findViewById(R.id.bOn);
        bOff = (Button)findViewById(R.id.bOff);
        bVisible = (Button)findViewById(R.id.bVisible);
        bList = (Button)findViewById(R.id.bList);

        ListView listView = (ListView)findViewById(R.id.listView);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void on(){
        
    }

}
