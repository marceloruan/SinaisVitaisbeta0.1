package com.example.marce.sinaisvitaisbeta01;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    /* Definição dos objetos que serão usados na Activity Principal
        statusMessage mostrará mensagens de status sobre a conexão
        counterMessage mostrará o valor do contador como recebido do Arduino
        connect é a thread de gerenciamento da conexão Bluetooth
     */
    Button btnconexao;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final int ATIVA_BLUETOOTH = 1;
    private static final int SOLICITA_CONEXAO = 2;
    boolean conexao=false;
    BluetoothSocket meusocket=null;
    private String MAC=null;



    static TextView statusMessage;
    static TextView counterMessage;
    static TextView temperatura;

    ConnectedThread connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Link entre os elementos da interface gráfica e suas
            representações em Java.
         */
        statusMessage = (TextView) findViewById(R.id.statusMessage);
        counterMessage = (TextView) findViewById(R.id.counterMessage);
        temperatura=(TextView) findViewById(R.id.temperatura);
        btnconexao = (Button) findViewById(R.id.btnconectar);


        /* Teste rápido. O hardware Bluetooth do dispositivo Android
            está funcionando ou está bugado de forma misteriosa?
            Será que existe, pelo menos? Provavelmente existe.
         */
        /*
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            statusMessage.setText("Que pena! Hardware Bluetooth não está funcionando :(");
        } else {
            statusMessage.setText("Ótimo! Hardware Bluetooth está funcionando :)");
        }*/

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter== null){
            Toast.makeText(getApplicationContext(), " Seu dispositovo não possui bluetooth", Toast.LENGTH_LONG).show();

             /*verifica se o blutooth esta ativado*/

        }else if (!btAdapter.isEnabled()){

            Intent ativa = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativa, ATIVA_BLUETOOTH);
        }


        btnconexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conexao){
                    //desconectar


                    try{
                        connect.cancel();

                        meusocket.close();



                        conexao=false;
                        btnconexao.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth foi Desconectado", Toast.LENGTH_LONG).show();

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
        switch (requestCode) {

            case ATIVA_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                   // Toast.makeText(getApplicationContext(), " Bluthooth foi Ativado", Toast.LENGTH_LONG).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "Bluthooth Nao foi Ativado", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListaDispositivo.ENDERECO_MAC);

                    //Toast.makeText(getApplicationContext(), "MAC "+MAC, Toast.LENGTH_LONG).show();

                    // meuDevie = meuBluetoothAdapter.getRemoteDevice(MAC);


                        /*meusocket = meuDevie.createRfcommSocketToServiceRecord(MEU_UUID);

                        meusocket.connect();*/
                    try {
                        connect = new ConnectedThread(MAC);
                        connect.start();

                        /* Um descanso rápido, para evitar bugs esquisitos.
                         */


                        Thread.sleep(1000);
                    } catch (Exception E) {
                        E.printStackTrace();
                    }

                    conexao = true;

                    btnconexao.setText("Desconectar");
                    Toast.makeText(getApplicationContext(), "Voce foi conetado com" + MAC, Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Falha do mac", Toast.LENGTH_LONG).show();

                }


        }
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            /* Esse método é invocado na Activity principal
                sempre que a thread de conexão Bluetooth recebe
                uma mensagem.
             */
            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);

            /* Aqui ocorre a decisão de ação, baseada na string
                recebida. Caso a string corresponda à uma das
                mensagens de status de conexão (iniciadas com --),
                atualizamos o status da conexão conforme o código.
             */
            if(dataString.equals("---N"))
                statusMessage.setText("Ocorreu um erro durante a conexão!");
            else if(dataString.equals("---S"))
                statusMessage.setText("Conectado!");
            else {

                /* Se a mensagem não for um código de status,
                    então ela deve ser tratada pelo aplicativo
                    como uma mensagem vinda diretamente do outro
                    lado da conexão. Nesse caso, simplesmente
                    atualizamos o valor contido no TextView do
                    contador.
                 */
                String valores[];
                valores=dataString.split(" ");

                counterMessage.setText(valores[0].toString());
                temperatura.setText(valores[1].toString());
            }

        }
    };

    /* Esse método é invocado sempre que o usuário clicar na TextView
        que contem o contador. O app Android transmite a string "restart",
        seguido de uma quebra de linha, que é o indicador de fim de mensagem.
     */
    public void restartCounter(View view) {
        connect.write("restart\n".getBytes());
    }
}