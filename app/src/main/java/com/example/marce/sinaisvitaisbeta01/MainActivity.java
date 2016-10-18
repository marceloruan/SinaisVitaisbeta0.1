package com.example.marce.sinaisvitaisbeta01;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int ATIVA_BLUETOOTH = 1;

    BluetoothAdapter meuBluetoothAdapter= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(meuBluetoothAdapter== null){
            Toast.makeText(getApplicationContext(), " Seu dispositovo não possui bluetooth", Toast.LENGTH_LONG).show();

        }else if (!meuBluetoothAdapter.isEnabled()){
            Intent ATIVA_BLUETOOTH = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, ATIVA_BLUETOOTH);



        }

    }
}
