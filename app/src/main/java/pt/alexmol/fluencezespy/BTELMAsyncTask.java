package pt.alexmol.fluencezespy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.SystemClock;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
public class BTELMAsyncTask extends AsyncTask<Void, Long, Void> {

    private final Context mContext;
    public BTELMAsyncTask (Context context){
        mContext = context;
    }

    private long watchdog = 0L;
    private int detectapausa = 0;

    private static BluetoothSocket mmSocket2 = null;

    private ToastHandler mToastHandler = null;

    private static final UUID MY_UUID2 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static boolean BTREQ2 = false;
    private static boolean BTON2 = false;
    private static boolean READYtoRW2 = false;
    private static boolean BTSOCKET2 = false;
    private static int ELMREADY2 = 0;
    private boolean USERLIGOUBT = false;
    private boolean USERRECUSOU = false;

    private static InputStream tmpIn = null;
    private static OutputStream tmpOut = null;

    private static InputStream inputStream2;
    private static OutputStream outputStream2;

    private static BluetoothAdapter mBluetoothAdapter2;

    private static double socx475 = 0.0;

    private static String bluetoothDeviceAddress2 = null;

    private static boolean PAUSAMAIN = false;

    private static boolean backGroundMode = false;
    private static boolean debugMode = false;


    private static PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;

    //recebe ordem para arrancar
    @Override
    protected void onPreExecute() {
        //regista-se no bus
        MyBus.getInstance().register(this);


        powerManager = (PowerManager) mContext.getSystemService(Activity.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FluenceZESPYWakelockTag");

        mToastHandler = new ToastHandler(mContext);
        watchdog = 0;
        detectapausa = 0;
        READYtoRW2 = false;
        USERRECUSOU = false;
        USERLIGOUBT = false;
        //valores = new long[2];
        SharedPreferences settings = mContext.getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
        backGroundMode = settings.getBoolean("backgroundmodeon",false);
        debugMode = settings.getBoolean("debugmodeon", false);



        // Iniciar um listener para verificar se há mudanças no estado do bluetooth
        // Register for broadcasts on BluetoothAdapter state change
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mReceiver3, filter);

        //outro listener para ver se é perdida a ligação
        // register for bluetooth disconnection
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mContext.registerReceiver(mReceiver4, intentFilter);

        if (debugMode) tostax("Pre-execute");
        //informa main que arrancou
        MyBus.getInstance().post(new BTELMTaskResultEvent(2,1));

    }



    @Override
    protected Void doInBackground(Void... ignore) {

        publishProgress(2L,2L);

        int passo = 1;
        String resposta = "";
        String resposta2 = "";
        long longo = 0L;
        int passoanterior = 0;
        boolean ultimavalida = false;

        //informa main que começou ciclo
        publishProgress(2L,3L);
        if (debugMode) tostax("BTELM task starting");

        //ciclo principal

        while (!isCancelled()) {

            //se o BTON ou BTREQ forem desligados volta ao passo 3
            if (passo >=11 && (!BTON2 || !BTREQ2)) {
                passo = 3;
                ELMREADY2 = 0;
                publishProgress(1L, 0L); //actualizar icone para vermelho
            }


            //se cair a ligação bluetooth por perda de sinal (ou outra) volta ao passo 11
            if (passo >= 20 && !BTSOCKET2) {
                passo = 11;
                ELMREADY2 = 0;
                publishProgress(1L, 0L); //actualizar icone para vermelho
            }

            if (passo == 1) {

                publishProgress(0L, 1L);

                passo = 2;

            }


            if (passo ==2) {
                publishProgress(0L, 2L);

                //verificar se bluetooth existe
                mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();

                //se não existir bluetooth informa o utilizador e termina async task
                if (mBluetoothAdapter2 == null) {
                    // Device does not support Bluetooth
                    tostalongax(mContext.getString(R.string.no_bt_support));
                    return null;
                }
                else passo =3;
            }


            if (passo == 3) {
                publishProgress(0L, 3L);

                //verificar se bluetooth está ligado
                //se não estiver chamar a janela de sistema a pedir para ligar
                if (!mBluetoothAdapter2.isEnabled())  {
                    //se veio de outro passo que não o próprio 3 chama a janela
                    if (passoanterior != 3) {
                        if (debugMode) tostax("pedir ligar");
                        publishProgress(4L,0L);

                    }
                    //se veio deste passo está à espera que o utilizador se decida
                    else {
                        if (USERLIGOUBT) { //utilizador ligou BT
                            BTON2 = true;
                            passo = 4;
                        }
                        if (USERRECUSOU) { //utilizador recusou, termina a tarefa
                            return null;
                        }

                    }
                }
                //se já estiver ligado
                else {
                    BTON2 = true;
                    passo = 4;
                }
            }

            if (passo==4) {
                publishProgress(0L,4L);


                //carrega preferências
                SharedPreferences settings = mContext.getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
                bluetoothDeviceAddress2 = settings.getString("deviceAddress", null);

                //verifica se foi escolhido dispositivo remoto
                if (bluetoothDeviceAddress2 == null) {
                    tostalongax("Please choose remote device in Settings");
                    BTREQ2 = false;
                    //passo = 4;
                    return null; //termina a async task
                }
                else {
                    BTREQ2 = true;
                    passo = 11;
                }

            }

            if (passo == 11) {
                publishProgress(0L,11L);
                READYtoRW2 = false;

                if (backGroundMode) {
                    //mNotificationManager2.notify(2, mBuilder2.build());
                    //startNotificationOnStatusBar();
                }
                if (!PAUSAMAIN && debugMode) tostax(mContext.getString(R.string.task2_start));

                //try to close any previous socket

                try {
                    mmSocket2.close();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                    if (debugMode) tostax("Tried to close socket with no success in step 11");
                }



                //cria um dispositivo remoto
                BluetoothDevice dispositivoBTremoto = mBluetoothAdapter2.getRemoteDevice(bluetoothDeviceAddress2);
                BluetoothSocket tmp2 = null;


                // Get a BluetoothSocket to connect with the given BluetoothDevice
                try {
                    // MY_UUID is the app's UUID string, also used by the server code
                    tmp2 = dispositivoBTremoto.createRfcommSocketToServiceRecord(MY_UUID2);

                } catch (IOException e) {
                    tostax("Could not create RFComm Connection");
                    passo=11;
                    SystemClock.sleep(5000L);
                }


                mmSocket2 = tmp2;
                if (mmSocket2 != null) {
                    //tosta("Created RFComm Connection");
                    passo=12;
                }
                else {
                    tostax("Unable to create RFComm Connection");
                    passo=11;
                }

            }


            if (passo == 12) {

                publishProgress(0L, 12L);
                //now make the socket connection

                // Always cancel discovery because it will slow down a connection
                //mBluetoothAdapter.cancelDiscovery();

                // Make a connection to the BluetoothSocket
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    mmSocket2.connect();
                    BTSOCKET2 = true;
                    passo = 13;
                    if (debugMode) tostax("Socket Connected!");

                }
                catch (IOException e) {
                    if(!PAUSAMAIN) tostax("Trying to connect to remote device, wait...");
                    BTSOCKET2 = false;


                    //connection to device failed so close the socket


                    try {
                        mmSocket2.close();
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                        if (debugMode) tostax("Tried to close socket with no success in step 12");
                    }
                    for (int i=0; i<100 && !isCancelled();i++ ){
                        SystemClock.sleep(10L);
                    }


                    if (!PAUSAMAIN || backGroundMode) passo = 11;
                    else return null;

                }

            }


            if (passo == 13) {
                publishProgress(0L, 13L);


                // reset streams

                // Get the input and output streams, using temp objects because
                // member streams are final
                try {
                    tmpIn  = mmSocket2.getInputStream();
                    tmpOut = mmSocket2.getOutputStream();
                    //tosta("Got Input/Output streams.");
                    passo = 14;
                }
                catch (IOException e) {
                    tostax("Unable to get Input/Output streams, retrying.");
                    BTSOCKET2 = false;
                    passo = 11;

                    //connection to device failed so close the socket

                    try {
                        mmSocket2.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        if (debugMode) tostax("Tried to close socket with no success in step 13");
                    }
                    for (int i=0; i<500 && !isCancelled();i++ ){
                        SystemClock.sleep(10L);
                    }
                }


            }


            if (passo == 14) {
                publishProgress(0L, 14L);

                try {
                    inputStream2 = tmpIn;
                    outputStream2 = tmpOut;
                    //tosta("Ready to Read/Write!");
                    READYtoRW2 = true;
                    passo = 20;
                }
                catch (Exception e) {
                    tostax("Failed to attribute Input/Output streams.");
                    BTSOCKET2 = false;
                    passo = 11;
                    //connection to device failed so close the socket
                    try {
                        mmSocket2.close();
                    }
                    catch (Exception e2) {
                        if (debugMode) tostax("Tried to close socket with no success in step 14");
                        e2.printStackTrace();
                    }
                    SystemClock.sleep(5000L);

                }

            }


            if (passo == 20) {
                int contagem = 0;
                publishProgress(0L, 20L);
                //passo de iniciar ELM
                //espera pelo socket ligado e ready to rw
                if (mmSocket2 != null) {
                    if (mmSocket2.isConnected() && READYtoRW2) {
                        //começa a inicialização
                        //enviar caracter 00 de forma a para eventual atma em curso
                        write0();
                        //limpar buffer durante 100ms
                        flushbytes(100);
                        //enviar pedido de reset
                        write("atws\r");
                        //tentar ler 5 linhas
                        resposta = esperalinha(500) + esperalinha(100) + esperalinha(100) + esperalinha(100) + esperalinha(100);
                        if (debugMode) tostax("Resposta ao atws:" + resposta);
                        //se a resposta for nula tenta mais uma vez com atz
                        if (resposta.trim().equals("")) {
                            tostax("Trying to reset ELM...");
                            write0();
                            flushbytes(100);
                            SystemClock.sleep(2000L);
                            flushbytes(100);
                            write0();
                            flushbytes(100);
                            SystemClock.sleep(500L);
                            flushbytes(100);
                            write("atz\r");
                            SystemClock.sleep(1000L);
                            resposta = esperalinha(2000) + esperalinha(100) + esperalinha(100) + esperalinha(100) + esperalinha(100);
                        }
                        //se resposta (continuar) nula avisa
                        if (resposta.trim().equals("")) {
                            tostax("No response, check ELM327");

                            return null;
                        }
                        //se a resposta contiver v1.5 ou v1.4 continuar e incrementar contagem
                        if ((resposta.toLowerCase().contains("v1.5".toLowerCase())) || (resposta.toLowerCase().contains("v1.4".toLowerCase())))
                            contagem++;
                        else {
                            tostax("ELM not version 1.5 or 1.4!");

                        }

                        flushbytes(50);
                        //enviar echo off
                        write("ate0\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command ate0");

                        }

                        flushbytes(50);
                        //enviar no spaces
                        write("ats0\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command ats0");

                        }

                        flushbytes(50);
                        //atsp6 (CAN 500K 11 bit)
                        write("atsp6\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atsp6");

                        }

                        flushbytes(50);
                        //atat1 (auto timing)
                        write("atat1\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atat1");

                        }

                        flushbytes(50);
                        //atcaf0 (no formatting)
                        write("atcaf0\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atcaf");

                        }

                        flushbytes(50);
                        //atfcsh79b        Set flow control response ID to 79b (the LBC)
                        write("atfcsh79b\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atfcsh79b");

                        }

                        flushbytes(50);
                        //atfcsd300020     Set the flow control response data to 300000
                        write("atfcsd300000\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atfcsd300000");

                        }

                        flushbytes(50);
                        //atfcsm1          Set flow control mode 1 (ID and data suplied)
                        write("atfcsm1\r");
                        //esperar por OK
                        if (esperatexto(200, "ok")) contagem++;
                        else {
                            tostax("Bad response to command atfcsm1");

                        }

                        if (contagem == 9) {
                            passo = 21;
                            tostax("Connected to ELM327");
                            ELMREADY2 = 1;
                            publishProgress(1L,1L); //actualizar icone para amarelo


                        } else {
                            tostalongax("Incompatible ELM327, only " + contagem + "/9 tests passed.");
                            tostalongax(mContext.getString(R.string.click_retry));


                            return null; //vai terminar a async task executando o oncomplete
                        }


                    }
                }
            }



            if (passo == 21) {
                publishProgress(0L,21L);
                //passo de ler free frames
                //espera pelo socket ligado e ready to rw

                if (mmSocket2 != null) {
                    if (mmSocket2.isConnected() && READYtoRW2) {


                        //escolher filtro
                        write("atcra42e\r");
                        ultimavalida = (esperatexto(150, "OK\r\r>")) ;

                        //ligar monitor
                        write("atma\r");
                        //obter resposta, esperar de acordo com a periodicidade do ID
                        resposta = esperalinha(200);
                        //parar monitor
                        write0();
                        ultimavalida = ultimavalida && (esperatexto(150, "STOPPED\r\r>")) ; //tosta("STOPPED OK");
                        //tosta("flushed after stop:" + flushbytes(500));

                        //processar o valor
                        //obter os 4 caracteres da esquerda

                        //tosta("resphex:" + resposta);

                        if ((resposta.length() >= 4) && ultimavalida) {
                            resposta2 = resposta.substring(0, 4);

                            if (ELMREADY2 != 2) {
                                ELMREADY2 = 2;
                                publishProgress(1L,2L); //para actualizar o icone verde
                            }

                        } else {
                            resposta2 = "FFFF";
                            if (ELMREADY2 != 1) {
                                ELMREADY2 = 1;
                                publishProgress(1L,1L); //para actualizar o icone amarelo
                            }

                        }

                        //converter caracteres em bits
                        //extrair inteiro

                        if (ultimavalida) {
                            try {
                                longo = Long.parseLong(resposta2, 16);
                                //shiftar à direita 3 bits, introduzindo zeros à esquerda
                                longo = longo >>> 3;
                            } catch (Exception e) {
                                if (debugMode) tostax("excepcao na 1a conversão para long:" + e);
                                longo = 0;
                            }

                            //converter para flutuante
                            socx475 = longo / 47.5;
                            //validação
                            if (socx475 <= 0.0 || socx475 > 150.0) socx475 = -1.0;


                            //publica valor soc multiplicado por 100
                            //SOCx475 é o índice 100
                            if (ELMREADY2 == 2) publishProgress(100L, (long) (socx475 * 100));

                        }

                    }
                }


                passo = 22;


            }


            if (passo == 22) {  //passo de ler iso-tp
                publishProgress(0L, 22L);





                passo =23;
            }



            if (passo == 23) { //passo de actualizar ecrans na UI
                publishProgress(0L,23L);


                if (ELMREADY2==2)publishProgress(3L,0L);
                //volta ao passo 21
                passo = 21;
            }


            //do some work
            //SystemClock.sleep(4000L);



            //verificação do watchdog
            //excepções são o passo 3 onde fica à espera do input do utilizador e
            //e os passos 21 e 22 que normalmente acabam sempre no 22
            if ((passo == passoanterior) && (passo!=3) && (passo<=20)) watchdog++;
            else watchdog = 0;

            if (watchdog > 200) {
                if (debugMode) tostax("Watchdog expired");
                return null;
            }
            //grava o passo actual
            passoanterior = passo;

            //verificação de pausa
            if (PAUSAMAIN && passo!=3) detectapausa++;
            else detectapausa = 0;
            //quando detecta pausa pode ter 2 comportamentos
            //se background mode ligado então espera mais tempo
            if (detectapausa>50 && backGroundMode) {
                //liberta recursos CPU

                try { if (wakeLock.isHeld()) wakeLock.release(); }
                catch (Exception x) { if (debugMode) tostax("exception wakelock.release:"+x); }
                //espera

                for (int i=0; i<500 && !isCancelled();i++ ){
                    SystemClock.sleep(10L);
                }

                //pede recursos CPU
                try { wakeLock.acquire(); }
                catch (Exception y) { if (debugMode) tostax("exception wakelock.aquire:"+y); }
            }
            //se background mode desligado então cancela a tarefa
            if (detectapausa>30 && !backGroundMode) return null;


        }



        if (debugMode) tostax("BTELM task finishing");
        return null;
    }


    @Override
    protected void onProgressUpdate(Long... valores) {

        //coloca valores no bus
        MyBus.getInstance().post(new BTELMTaskResultEvent(valores[0], valores[1]));

    }



    @Override
    protected void onPostExecute(Void ignore) {


        // desactivar os listeners
        try {
            mContext.unregisterReceiver(mReceiver3);
            mContext.unregisterReceiver(mReceiver4);
        }
        catch (Exception e) {
            if (debugMode) tostax("exception ao unregisterreceivers no postexecute");
        }

        try {
            mmSocket2.close();
        }
        catch (Exception e2) {
            if (debugMode) tostax("Tried to close socket after task complete with no success");
            e2.printStackTrace();
        }

        try { if (wakeLock.isHeld()) wakeLock.release(); }
        catch (Exception x) { if (debugMode) tostax("exception wakelock.release:" + x); }




        if (debugMode) tostax("Asynctask complete");

        //informa main que completou
        MyBus.getInstance().post(new BTELMTaskResultEvent(2,5));
        //desliga-se do bus
        MyBus.getInstance().unregister(this);



    }



    @Override
    protected void onCancelled(Void ignore) {


        // desactivar os listeners
        try {
            mContext.unregisterReceiver(mReceiver3);
            mContext.unregisterReceiver(mReceiver4);
        }
        catch (Exception e) {
            if (debugMode) tostax("exception ao unregisterreceivers no oncancelled");
        }

        try {
            mmSocket2.close();
        }
        catch (Exception e2) {
            if (debugMode) tostax("Tried to close socket after task canceled with no success");
            e2.printStackTrace();
        }

        try { if (wakeLock.isHeld()) wakeLock.release(); }
        catch (Exception x) { if (debugMode) tostax("exception wakelock.release:"+x); }




        if (debugMode) tostax("Asynctask canceled");

        //informa main que foi cancelada
        MyBus.getInstance().post(new BTELMTaskResultEvent(2,4));
        //desliga-se do bus
        MyBus.getInstance().unregister(this);

    }


    private void tostax(String mensagem) {

        mToastHandler.showToast(mensagem, Toast.LENGTH_SHORT);
    }

    private void tostalongax(String mensagem) {

        mToastHandler.showToast(mensagem, Toast.LENGTH_LONG);
    }

    @Subscribe
    public void recebereventospage1 (Page1TaskResultEvent comando) {
        if (comando.getResult()==1) {
            if (debugMode) tostax("Asynctaskelm recebeu comando 1 da page 1");

        }

    }


    @Subscribe
    public void recebereventosmain (MainTaskResultEvent comando) {
        if (comando.getResult()==1) {
            if (debugMode) tostax("Asynctaskelm recebeu nao pausa do main");
            PAUSAMAIN = false;

        }

        if (comando.getResult()==2) {
            if (debugMode) tostax("Asynctaskelm recebeu pausa do main");
            PAUSAMAIN = true;

        }

        if (comando.getResult()==3) {
            if (debugMode) tostax("Asynctaskelm recebeu bluetooth ligado do main");
            USERLIGOUBT = true;
        }

        if (comando.getResult()==4) {
            if (debugMode) tostax("Asynctaskelm recebeu bluetooth recusado do main");
            USERRECUSOU = true;
        }


    }



    //Listen for bluetooth adapter changes
    private final BroadcastReceiver mReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {

                    //Se o bluetooth foi desligado
                    /*
                    case BluetoothAdapter.STATE_OFF:

                        break;
                    */
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        BTREQ2 = false;
                        BTON2 = false;
                        BTSOCKET2 = false;
                        tostalongax(mContext.getString(R.string.bt_turned_off));
                        break;


                    /*
                    case BluetoothAdapter.STATE_ON:

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:

                        break;
                        */
                }
            }
        }

    };

    //The BroadcastReceiver that listens for bluetooth disconnection
    private final BroadcastReceiver mReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                BTSOCKET2 = false;
                // inform user
                tostalongax(mContext.getString(R.string.bt_conn_lost));
                SystemClock.sleep(2000L);
                if (!PAUSAMAIN) tostalongax(mContext.getString(R.string.click_retry));

                //finaliza a tarefa
                cancel(true);




                //try to close the socket


                try {
                    mmSocket2.close();
                }
                catch (Exception e2) {
                    if (debugMode) tostax("Tried to close socket after lost connection with no success");
                    e2.printStackTrace();
                }



            }
        }
    };




    //****************************************************************************************
    // funções para comunicação com o ELM
    //****************************************************************************************


    // write a message to the output stream
    private void write(String message) {

        try {
            if (mmSocket2.isConnected() && READYtoRW2) {
                byte[] msgBuffer = message.getBytes();
                outputStream2.write(msgBuffer);
            } else {
                if (debugMode) tostax("Write failed! Socket is closed or not ready. Tried to send:" + message);
            }
        } catch (IOException e) {
            //tosta("Error sending > " + e.getMessage());
        }
    }

    // write a message to the output stream
    private void write0() {
        try {
            if (mmSocket2.isConnected() && READYtoRW2) {
                byte zero = 0x00;
                outputStream2.write(zero);
            } else {
                if (debugMode) tostax("Write failed! Socket is closed or not ready. Tried to send 0x00");
            }
        }
        catch (Exception e) {
            //tosta("Error sending > " + e.getMessage());
        }
    }


    private int read() {

        try {
            if (mmSocket2.isConnected() && READYtoRW2)
                return inputStream2.read();
            else
                return -1;
        }
        catch (Exception e) {
            return -1;
        }
    }

    private int available() throws IOException {

        try {
            if (mmSocket2.isConnected() && READYtoRW2)
                return inputStream2.available();
            else
                return 0;
        }
        catch (Exception e) {
            if (debugMode) tostax("IOException getting available bytes from ELM");
            return 0;
        }
    }



    //função esperatexto
    private boolean esperatexto (long timeout, String texto) {

        boolean parar = false;
        String readBuffer = "";
        boolean encontrou = false;

        long start = Calendar.getInstance().getTimeInMillis();

        while (!parar) {
            try {
                //Se houver dados em espera para leitura
                if (available() > 0) {
                    //ler um byte e converter para int
                    int data = read();
                    if (data == -1) return false;
                    // convert it to a character
                    char ch = (char) data;
                    //if (ch == '\r') ch = '§';
                    //if (ch == '\n') ch = '£';
                    // add it to the readBuffer
                    readBuffer += ch;
                }

            }
            catch (IOException e) {
                if (debugMode) tostax("IOException reading from ELM");
            }

            if (readBuffer.toLowerCase().contains(texto.toLowerCase())) {
                parar = true;
                encontrou = true;
            }
            if (Calendar.getInstance().getTimeInMillis() - start >= timeout) {
                parar = true;
                //tosta("timeout esperando:" + texto);
            }

        }

        return encontrou;

    }

    //função flush com retorno de numero de bytes limpos

    private int flushbytes (long timeout) {

        boolean parar = false;
        int contagem = 0;

        long start = Calendar.getInstance().getTimeInMillis();

        while (!parar) {
            try {
                //Se houver dados em espera para leitura
                if (available() > 0) {
                    //ler um byte e converter para int
                    int data = read();
                    //tosta("Flushed:"+data);
                    contagem++;
                }

            }
            catch (IOException e) {
                if (debugMode) tostax("IOException reading from ELM");
            }

            if (Calendar.getInstance().getTimeInMillis() - start >= timeout) parar = true;

        }

        return contagem;

    }


    //função esperalinha


    private String esperalinha (long timeout) {

        boolean parar = false;
        String readBuffer = "";

        long start = Calendar.getInstance().getTimeInMillis();

        while (!parar) {
            try {
                //Se houver dados em espera para leitura
                if (available() > 0) {
                    //ler um byte e converter para int
                    int data = read();
                    if (data == -1) return "";
                    // convert it to a character
                    char ch = (char) data;
                    if (ch == '\r') ch = '§';
                    //if (ch == '\n') ch = '£';
                    // add it to the readBuffer
                    readBuffer += ch;
                }

            }
            catch (IOException e) {
                if (debugMode) tostax("IOException reading from ELM");
            }

            if (readBuffer.contains("§")) {
                //if (readBuffer.contains("\r")) {
                parar = true;
            }

            if (Calendar.getInstance().getTimeInMillis() - start >= timeout) parar = true;

        }

        return readBuffer;

    }





}