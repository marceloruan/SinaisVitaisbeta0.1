package com.example.marce.sinaisvitaisbeta01;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by root on 21/10/16.
 */

public class ListaDispositivo extends ListActivity{

    BluetoothAdapter dispositivo=null;
    static String ENDERECO_MAC= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    ArrayAdapter<String> ArrayBluetooth= new ArrayAdapter < String>(this,android.R.layout.simple_list_item_1);

      dispositivo = BluetoothAdapter.getDefaultAdapter();

        //tras as informacoes do dispositivo pareado



        Set<BluetoothDevice> dispositivospareados=dispositivo.getBondedDevices();

        if(dispositivospareados.size() > 0 ){
           // Toast.makeText(getApplicationContext(), " entrou ", Toast.LENGTH_LONG).show();
            for(BluetoothDevice tipos : dispositivospareados){
                String nomebt=tipos.getName();
                String macbt=tipos.getAddress();
                ArrayBluetooth.add(nomebt+"\n"+macbt);

            }
        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral=((TextView) v).getText().toString();

        String enderecoMac= informacaoGeral.substring(informacaoGeral.length()-17);

        Intent retornaMac=new Intent();
        retornaMac.putExtra(ENDERECO_MAC,enderecoMac);
        setResult(RESULT_OK,retornaMac);
        finish();

    }
}
