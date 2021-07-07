package com.paparazziteam.myapplication.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;

    BluetoothAdapter bluetoothAdapter;

    public AcceptThread() {

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try
        {
            //MY_UUID is the app's UUID string, also used by the client code.
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("NAME", UUID.fromString("b3000e31-52bd-464a-af23-2ba26a2bea55"));
        }
        catch (IOException e)
        {
           Log.e("TAG", "Socket's listen() method failed", e);
        }

        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.


        while (socket==null) {

            Log.e("SOCKET", "is null");

            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e("TAG", "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                Log.e("TAG", "Conexion Aceptada!");
                //manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e("TAG", "Could not close the connect socket", e);
        }
    }

}
