package com.example.jessezhou.bluetoothblink2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    // UI elements
    private ListView listView;
    private Button bOn,bOff,bList;

    //Bluetooth elements
    private BluetoothAdapter btAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private OutputStream sender;

    //Handler to handle background thread work
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init(){
        bOn = (Button)findViewById(R.id.bOn);
        bOff = (Button)findViewById(R.id.bOff);
        bList = (Button)findViewById(R.id.bList);
        listView = (ListView)findViewById(R.id.listView);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case Constants.BLUETOOTH_CONNECT:
                        System.out.println(msg.arg1);
                        if(msg.arg1 == Constants.CONNECTION_SUCCESS){
                            Toast.makeText(getApplicationContext(), "It worked!", Toast.LENGTH_SHORT).show();
                            System.out.println("Handler is working");
                        }
                        else if(msg.arg1 == Constants.CONNECTION_FAILED){
                            Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                        }

                }
            }
        };

    }

    public void on(View view){
        if(!btAdapter.isEnabled()){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on"
                    , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void list(View view){
        pairedDevices = btAdapter.getBondedDevices();

        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : pairedDevices)
            list.add(bt.getName() + "\n" + bt.getAddress());

        final Object[] btArray = pairedDevices.toArray();

        Toast.makeText(getApplicationContext(),"Showing Paired Devices", Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice tmp = (BluetoothDevice)btArray[position];
                System.out.println(tmp);
                ConnectThread connectDrawbot = new ConnectThread(tmp, handler);
                connectDrawbot.start();
            }
        });

    }

    public void off(View view){
        btAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Off", Toast.LENGTH_LONG).show();
    }

    /*
    public void sendOnByte(View view){
        if(!btSocket.isConnected()){
            Toast.makeText(getApplicationContext(), "Connect your shit first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            sender = btSocket.getOutputStream();
            sender.write(1);
            sender.flush();
        }
        catch(IOException e){}

    }

    public void sendOffByte(View view){
        if(!btSocket.isConnected()){
            Toast.makeText(getApplicationContext(), "Connect your shit first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            sender = btSocket.getOutputStream();
            sender.write(0);
            sender.flush();
        }
        catch(IOException e){}

    }
    */

}
