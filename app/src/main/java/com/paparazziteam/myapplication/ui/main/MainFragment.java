package com.paparazziteam.myapplication.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.security.identity.ResultData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.paparazziteam.myapplication.R;
import com.paparazziteam.myapplication.activities.DispositivosVinculados;
import com.paparazziteam.myapplication.activities.bluetoothActivity;

import java.util.Set;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesArrayAdapter;
    Set<BluetoothDevice> pairedDevices;

    Button btnactivarbluetooh, btnconectarRasberry;
    ListView listView;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        btnactivarbluetooh = view.findViewById(R.id.activatebluetooth);
        btnconectarRasberry = view.findViewById(R.id.connectRasberry);
        listView = view.findViewById(R.id.listView);

        bluetoohCheckFeatures();
        setupBluetooth();

        btnactivarbluetooh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBluetooth();
            }
        });

        btnconectarRasberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conecctRasberry();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Toast.makeText(getContext(), ""+ address, Toast.LENGTH_SHORT).show();

                Intent intend = new Intent(getContext(), bluetoothActivity.class);
                intend.putExtra("EXTRA_DEVICE_ADDRESS", address);
                startActivity(intend);

            }
        });

        return view;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return false;
        }
    });


    private void conecctRasberry() {

        // Register for broadcasts when a device is discovered.
        mBtAdapter.startDiscovery();

        getPairedDevices();


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void getPairedDevices() {

        // Inicializa la array que contendra la lista de los dispositivos bluetooth vinculados
        mPairedDevicesArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);


        // Presenta los dispositivos vinculados en el ListView
        listView.setAdapter(mPairedDevicesArrayAdapter);

        // Obtiene el adaptador local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Obtiene el adaptador local Bluetooth adapter
        pairedDevices = mBtAdapter.getBondedDevices();



        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.e("DISPOSITIVOS PAIRED","DeviceName: "+deviceName+"  &&  MAC: "+deviceHardwareAddress+"");
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            }

            //Rellenar el arralist


        }
    }


    private void setupBluetooth() {

        // Obtiene el adaptador local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            // Device doesn't support Bluetooth
        }else
        {
            if(mBtAdapter!=null)
            {


                if (!mBtAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 100);
                }

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(resultCode == getActivity().RESULT_OK)
        {
            Toast.makeText(getContext(), "Bluetooh activado" , Toast.LENGTH_SHORT).show();


        }else
        {
            if(resultCode == getActivity().RESULT_CANCELED)
            {
                Toast.makeText(getContext(), "Operacion Cancelada!" , Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void bluetoohCheckFeatures() {

        // Use this check to determine whether Bluetooth is supported on the device.
        // Then you can selectively disable BLE-related features.
        if (! getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            Toast.makeText(getContext(), "R.string.bluetooth_not_supported" , Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), "R.string.ble_not_supported", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}