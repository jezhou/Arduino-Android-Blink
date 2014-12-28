package com.example.jessezhou.bluetoothblink2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
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
    private Button blinkButton, bList;

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
        blinkButton = (Button)findViewById(R.id.blinkButton);
        bList = (Button)findViewById(R.id.bList);
        listView = (ListView)findViewById(R.id.listView);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case Constants.BLUETOOTH_CONNECT:
                        if(msg.arg1 == Constants.CONNECTION_SUCCESS){
                            Toast.makeText(getApplicationContext(), "Connection worked!", Toast.LENGTH_SHORT).show();
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

    public void off(View view){
        btAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth Off", Toast.LENGTH_LONG).show();
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
                BluetoothSocket btSocket = connect((BluetoothDevice) btArray[position]);
                startCommand(btSocket);
            }
        });

    }

    private synchronized BluetoothSocket connect(BluetoothDevice tmp){
        Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
        BlinkConnectThread connectBlink = new BlinkConnectThread(tmp, handler);
        connectBlink.start();
        return connectBlink.getBTSocket();
    }

    private synchronized void startCommand(BluetoothSocket btSocket){
        final BlinkCommand controller = new BlinkCommand(btSocket);
        blinkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        controller.blinkOn();
                        break;
                    case MotionEvent.ACTION_UP:
                        controller.blinkOff();
                        break;
                }

                return true;
            }
        });
    }

}
