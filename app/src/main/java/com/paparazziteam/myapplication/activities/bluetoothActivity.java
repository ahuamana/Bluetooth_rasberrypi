package com.paparazziteam.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.paparazziteam.myapplication.R;
import com.paparazziteam.myapplication.utils.AcceptThread;
import com.paparazziteam.myapplication.utils.ConnectThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class bluetoothActivity extends AppCompatActivity {



    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;

    private String mExtraMAC;
    private TextView textviewMAC;
    Button btnTomarFoto;

    AcceptThread thread;
    BluetoothAdapter btAdapter;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mExtraMAC = getIntent().getStringExtra("EXTRA_DEVICE_ADDRESS");

        textviewMAC = findViewById(R.id.textView2);
        btnTomarFoto = findViewById(R.id.btntomarfoto);



        textviewMAC.setText(mExtraMAC);


        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter



        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {

                getRemoteDevice();
            }
        });


    }

    private void getRemoteDevice() {

        Intent intent = getIntent();
        address = intent.getStringExtra("EXTRA_DEVICE_ADDRESS");
        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        AcceptThread hilo = new AcceptThread();
        hilo.start();

        ConnectThread hilo2 = new ConnectThread(device);
        hilo2.start();

    }


}