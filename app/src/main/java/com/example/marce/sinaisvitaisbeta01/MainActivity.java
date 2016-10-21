package com.example.marce.sinaisvitaisbeta01;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button btnconexao;
    private static final int ATIVA_BLUETOOTH = 1;
    private static final int SOLICITA_CONEXAO = 2;

    BluetoothAdapter meuBluetoothAdapter= null;
    BluetoothDevice meuDevie=null;

    boolean conexao=false;

    private String MAC=null;
    BluetoothSocket meusocket=null;

    UUID MEU_UUID= UUID.fromString("00001101-0000-1000-800010085f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnconexao = (Button) findViewById(R.id.btnconexao);


        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(meuBluetoothAdapter== null){
            Toast.makeText(getApplicationContext(), " Seu dispositovo não possui bluetooth", Toast.LENGTH_LONG).show();

         /*verifica se o blutooth esta ativado*/

        }else if (!meuBluetoothAdapter.isEnabled()){

            Intent ativa = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativa, ATIVA_BLUETOOTH);
        }

        btnconexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conexao){
                    //desconectar
                    try{
                        meusocket.close();
                    }catch (IOException erro){

                    }

                }else{
                    //conectar
                    Intent abreLista= new Intent(MainActivity.this,ListaDispositivo.class);
                    startActivityForResult(abreLista,SOLICITA_CONEXAO);

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case ATIVA_BLUETOOTH:
                if(resultCode== Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), " Bluthooth foi Ativado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Bluthooth Nao foi Ativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if(resultCode==Activity.RESULT_OK){
                    MAC=data.getExtras().getString(ListaDispositivo.ENDERECO_MAC);

                    //Toast.makeText(getApplicationContext(), "MAC "+MAC, Toast.LENGTH_LONG).show();
                    meuDevie=meuBluetoothAdapter.getRemoteDevice(MAC);

                    try{

                        meusocket= meuDevie.createRfcommSocketToServiceRecord(MEU_UUID);

                        meusocket.connect();
                        Toast.makeText(getApplicationContext(), "Voce foi conetado como"+ MAC, Toast.LENGTH_LONG).show();

                    }catch (IOException erro){

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Falha do mac", Toast.LENGTH_LONG).show();

                }


        }
    }
}
