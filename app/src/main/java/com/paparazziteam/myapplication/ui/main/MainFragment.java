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
import android.security.identity.ResultData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.paparazziteam.myapplication.R;

import java.util.Set;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;

    Button btnactivarbluetooh, btnconectarRasberry;
    boolean isActiveBluetooth = false;

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



        return view;
    }

    private void conecctRasberry() {

        // Register for broadcasts when a device is discovered.
        bluetoothAdapter.startDiscovery();

        getPairedDevices();


    }

    private void getPairedDevices() {

        pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.e("DISPOSITIVOS PAIRED","DeviceName: "+deviceName+"  &&  MAC: "+deviceHardwareAddress+"");

            }
        }
    }


    private void setupBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }else
        {
            if(bluetoothAdapter!=null)
            {


                if (!bluetoothAdapter.isEnabled()) {
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