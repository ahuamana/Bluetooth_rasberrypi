package com.paparazziteam.myapplication.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
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
import com.paparazziteam.myapplication.activities.bluetoothActivity;
import com.paparazziteam.myapplication.utils.AcceptThread;
import com.paparazziteam.myapplication.utils.ConnectThread;
import com.paparazziteam.myapplication.utils.MyBluetoothService;
import com.paparazziteam.myapplication.viewModels.MainViewModel;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private BluetoothAdapter mBtAdapter;
    private BluetoothDevice mDevice = null;
    private MyBluetoothService myBluetoothService;

    private ArrayAdapter mPairedDevicesArrayAdapter;
    Set<BluetoothDevice> pairedDevices;

    Button btnpairdevice, btnconectarRasberry;
    ListView listView;

    String address;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        btnpairdevice = view.findViewById(R.id.activatebluetooth);
        btnconectarRasberry = view.findViewById(R.id.connectRasberry);
        listView = view.findViewById(R.id.listView);

        bluetoohCheckFeatures();
        setupBluetooth();

        btnpairdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setupBluetooth();
                openBluetoothSettings();
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
                address = info.substring(info.length() - 17);

                Toast.makeText(getContext(), "Selecionaste: "+ address, Toast.LENGTH_SHORT).show();


            }
        });

        return view;
    }

    private void openBluetoothSettings() {
        Intent intentOpenBluetoothSettings = new Intent();
        intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(intentOpenBluetoothSettings);
    }



    private void conecctRasberry() {

        Log.e("TAG", "clicked connected");

        if(mDevice != null)
        {
           new Thread(new AcceptThread()).start();//instanciate server

           //new Thread(new ConnectThread(mDevice)).start();//instanciate cliente conection


        }else
        {
            Toast.makeText(getContext(), "No seleccionaste ningun dispositivo", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        getPairedDevices();
    }

    @Override
    public void onStart() {
        super.onStart();

        getPairedDevices();
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

                mDevice = device;



                //Log.d("TAG", "Trying to pair with " + deviceName);
                //boolean outcome = device.createBond();
                //Log.d("TAG", "Paired status: " + outcome);

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