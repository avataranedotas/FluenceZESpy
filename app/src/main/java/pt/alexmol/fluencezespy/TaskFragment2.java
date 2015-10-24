package pt.alexmol.fluencezespy;

/**
 * Created by alexandre on 16-10-2015.
 */


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.UUID;

/**
 * TaskFragment manages a single background task and retains itself across
 * configuration changes.
 */
public class TaskFragment2 extends Fragment {
    private static final String TAG = TaskFragment2.class.getSimpleName();
    private static final boolean DEBUG = true; // Set this to false to disable logs.



    /**
     * Callback interface through which the fragment can report the task's
     * progress and results back to the Activity.
     */
    static interface TaskCallbacks2 {
        void onPreExecute2();

        void onProgressUpdate2(long indice, long dado);

        void onCancelled2();

        void onPostExecute2();

        //void onProgressUpdate3(long percent);

        //void obtervalores(double[] matriz);
    }

    private TaskCallbacks2 mCallbacks;
    private DummyTask2 mTask;
    private boolean mRunning;

    public static BluetoothSocket mmSocket2 = null;

    public ToastHandler mToastHandler = null;

    public static final UUID MY_UUID2 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public final static int SETTINGS_ACTIVITY2 = 7;
    public final static int REQUEST_ENABLE_BT2 = 3;

    public static boolean BTREQ2 = false;
    public static boolean BTON2 = false;
    public static boolean READYtoRW2 = false;
    protected static boolean BTSOCKET2 = false;
    protected static int ELMREADY2 = 0;

    private static InputStream tmpIn = null;
    private static OutputStream tmpOut = null;

    private static InputStream inputStream2;
    private static OutputStream outputStream2;

    protected static BluetoothAdapter mBluetoothAdapter2;

    public static double socx475 = 0.0;

    protected static String bluetoothDeviceAddress2 = null;

    private static boolean PAUSAMAIN = false;
    private static boolean ESPERAMAIN = false;


    protected static boolean backGroundMode = false;
    protected static boolean debugMode = false;


    public static PowerManager powerManager;
    public static PowerManager.WakeLock wakeLock;

    //public static NotificationCompat.Builder mBuilder2;
    //public static NotificationManager mNotificationManager2;

    //public static NotificationManager mNotificationManager3;



    /**
     * Hold a reference to the parent Activity so we can report the task's current
     * progress and results. The Android framework will pass us a reference to the
     * newly created Activity after each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        if (DEBUG) Log.i(TAG, "onAttach(Activity)");
        super.onAttach(activity);
        if (!(activity instanceof TaskCallbacks2)) {
            throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
        }

        // Hold a reference to the parent Activity so we can report back the task's
        // current progress and results.
        mCallbacks = (TaskCallbacks2) activity;

        mToastHandler = new ToastHandler(activity);
    }

    /**
     * This method is called once when the Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.i(TAG, "onCreate(Bundle)");

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        powerManager = (PowerManager) getActivity().getSystemService(Activity.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FluenceZESPYWakelockTag");


        //TODO: passar as notificações para a Main, verificar se rebenta quando se chama a notif na própria janela main
        //barra de notificações

        /*
        mBuilder2 =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_notification1)
                        .setContentTitle("Fluence ZE Spy")
                        .setContentText("Running in background mode")
                        .setPriority(-1);
        mBuilder2.setOngoing(true);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getContext(), MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder2.setContentIntent(pendingIntent);

        mNotificationManager2 = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        */




    }

    /**
     * Note that this method is <em>not</em> called when the Fragment is being
     * retained across Activity instances. It will, however, be called when its
     * parent Activity is being destroyed for good (such as when the user clicks
     * the back button, etc.).
     */
    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(TAG, "onDestroy()");
        super.onDestroy();
        if (debugMode) tosta("fragtask2 destroy");

        // colocar aqui todas as acções necessárias quando a aplicação fecha de vez

        //TODO: cancela a async task, verificar se é possível não cancelar em caso de chamada pelas notificações
        cancel();

        //desligar BTREQ para forçar nova verificação de bluetooth
        BTREQ2 = false;

        //save state ?

    }

    /*
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
    */


    /*****************************/
    /***** TASK FRAGMENT API *****/
    /*****************************/

    /**
     * Start the background task.
     */
    public void start() {
        //tosta("taskfragment2 iniciando ligacao bluetooth");
        if (!mRunning) {
            /*if (mTask == null)*/ mTask = new DummyTask2();

            mTask.execute();
            mRunning = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (mRunning) {
            mTask.cancel(false);

            if (ESPERAMAIN && debugMode) tosta("ESPERA MAIN");

            mTask = null;
            mRunning = false;

            // desactivar os listeners
            try {
                getActivity().unregisterReceiver(mReceiver3);
                getActivity().unregisterReceiver(mReceiver4);
            }
            catch (Exception e) {
                if (debugMode) tosta("exception ao unregisterreceivers no oncancelled da task2");
            }

        }
    }

    /**
     * Returns the current state of the background task.
     */
    public boolean isRunning() {
        return mRunning;
    }

    //set main activity pause status
    public void pausamain(boolean pausa)
    {
        PAUSAMAIN = pausa;
    }

    //set delay
    public void delay(boolean esperar)
    {
        ESPERAMAIN = esperar;
    }



    /***************************/
    /***** BACKGROUND TASK *****/
    /***************************/

    /**
     * A dummy task that performs some (dumb) background work and proxies progress
     * updates and results back to the Activity.
     */
    private class DummyTask2 extends AsyncTask<Void, Long, Void> {

        private long watchdog = 0L;
        private int detectapausa = 0;

        //recebe ordem para arrancar
        @Override
        protected void onPreExecute() {
            // Proxy the call to the Activity.
            mCallbacks.onPreExecute2();
            mRunning = true;
            watchdog = 0;
            detectapausa = 0;
            READYtoRW2 = false;
            //valores = new long[2];
            SharedPreferences settings = getActivity().getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
            backGroundMode = settings.getBoolean("backgroundmodeon",false);
            debugMode = settings.getBoolean("debugmodeon", false);

            if (debugMode) tosta("Pre-execute");






        }

        /**
         * Note that we do NOT call the callback object's methods directly from the
         * background thread, as this could result in a race condition.
         */


        @Override
        protected Void doInBackground(Void... ignore) {
            int passo = 1;
            String resposta = "";
            String resposta2 = "";
            long longo = 0L;
            int passoanterior = 0;
            boolean ultimavalida = false;








            //tosta ("teste tosta nova");



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


                    if (ESPERAMAIN) {
                        if(debugMode) tosta ("ESPERA PASSO 1");
                        //SystemClock.sleep(3000L);

                        for (int i=0; i<50 && !isCancelled();i++ ){
                            SystemClock.sleep(10L);
                        }
                    }

                    // Iniciar um listener para verificar se há mudanças no estado do bluetooth
                    // Register for broadcasts on BluetoothAdapter state change
                    IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    getActivity().registerReceiver(mReceiver3, filter);

                    //outro listener para ver se é perdida a ligação
                    // register for bluetooth disconnection
                    IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                    getActivity().registerReceiver(mReceiver4, intentFilter);

                    passo = 2;


                }

                if (passo ==2) {
                    publishProgress(0L, 2L);

                    //verificar se bluetooth existe
                    mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();

                    //se não existir bluetooth informa o utilizador e termina async task
                    if (mBluetoothAdapter2 == null) {
                        // Device does not support Bluetooth
                        tostalonga(getString(R.string.no_bt_support));
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
                            if (debugMode) tosta("pedir ligar");
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT2);

                            //se responder sim passa ao passo 4, se responder não cancela a task2
                            //timeout não conta neste passo para poder esperar pelo utilizador
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
                    SharedPreferences settings = getActivity().getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
                    bluetoothDeviceAddress2 = settings.getString("deviceAddress", null);

                    //verifica se foi escolhido dispositivo remoto
                    if (bluetoothDeviceAddress2 == null) {
                        tostalonga("Please choose remote device in Settings");
                        BTREQ2 = false;
                        passo = 4;
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
                    if (!PAUSAMAIN && debugMode) tosta(getString(R.string.task2_start));

                    //try to close any previous socket

                    try {
                        mmSocket2.close();
                    }
                    catch (Exception e2) {
                        e2.printStackTrace();
                        if (debugMode) tosta("Tried to close socket with no success in step 11");
                    }



                    //cria um dispositivo remoto
                    BluetoothDevice dispositivoBTremoto = mBluetoothAdapter2.getRemoteDevice(bluetoothDeviceAddress2);
                    BluetoothSocket tmp2 = null;


                    // Get a BluetoothSocket to connect with the given BluetoothDevice
                    try {
                        // MY_UUID is the app's UUID string, also used by the server code
                        tmp2 = dispositivoBTremoto.createRfcommSocketToServiceRecord(MY_UUID2);

                    } catch (IOException e) {
                        tosta("Could not create RFComm Connection");
                        passo=11;
                        SystemClock.sleep(5000L);
                    }


                    mmSocket2 = tmp2;
                    if (mmSocket2 != null) {
                        //tosta("Created RFComm Connection");
                        passo=12;
                    }
                    else {
                        tosta("Unable to create RFComm Connection");
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
                        if (debugMode) tosta("Socket Connected!");

                    }
                    catch (IOException e) {
                        if(!PAUSAMAIN) tosta("Trying to connect to remote device, wait...");
                        BTSOCKET2 = false;


                        //connection to device failed so close the socket


                        try {
                            mmSocket2.close();
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                            if (debugMode) tosta("Tried to close socket with no success in step 12");
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
                        tosta("Unable to get Input/Output streams, retrying.");
                        BTSOCKET2 = false;
                        passo = 11;

                        //connection to device failed so close the socket

                        try {
                            mmSocket2.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            if (debugMode) tosta("Tried to close socket with no success in step 13");
                        }
                        SystemClock.sleep(5000L);
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
                        tosta("Failed to attribute Input/Output streams.");
                        BTSOCKET2 = false;
                        passo = 11;
                        //connection to device failed so close the socket
                        try {
                            mmSocket2.close();
                        }
                        catch (Exception e2) {
                            if (debugMode) tosta("Tried to close socket with no success in step 14");
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
                            if (debugMode) tosta("Resposta ao atws:" + resposta);
                            //se a resposta for nula tenta mais uma vez com atz
                            if (resposta.trim().equals("")) {
                                tosta("Trying to reset ELM...");
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
                                tosta("No response, check ELM327");

                                return null;
                            }
                            //se a resposta contiver v1.5 ou v1.4 continuar e incrementar contagem
                            if ((resposta.toLowerCase().contains("v1.5".toLowerCase())) || (resposta.toLowerCase().contains("v1.4".toLowerCase())))
                                contagem++;
                            else {
                                tosta("ELM not version 1.5 or 1.4!");

                            }

                            flushbytes(50);
                            //enviar echo off
                            write("ate0\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command ate0");

                            }

                            flushbytes(50);
                            //enviar no spaces
                            write("ats0\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command ats0");

                            }

                            flushbytes(50);
                            //atsp6 (CAN 500K 11 bit)
                            write("atsp6\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atsp6");

                            }

                            flushbytes(50);
                            //atat1 (auto timing)
                            write("atat1\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atat1");

                            }

                            flushbytes(50);
                            //atcaf0 (no formatting)
                            write("atcaf0\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atcaf");

                            }

                            flushbytes(50);
                            //atfcsh79b        Set flow control response ID to 79b (the LBC)
                            write("atfcsh79b\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atfcsh79b");

                            }

                            flushbytes(50);
                            //atfcsd300020     Set the flow control response data to 300000
                            write("atfcsd300000\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atfcsd300000");

                            }

                            flushbytes(50);
                            //atfcsm1          Set flow control mode 1 (ID and data suplied)
                            write("atfcsm1\r");
                            //esperar por OK
                            if (esperatexto(200, "ok")) contagem++;
                            else {
                                tosta("Bad response to command atfcsm1");

                            }

                            if (contagem == 9) {
                                passo = 21;
                                tosta("Connected to ELM327");
                                ELMREADY2 = 1;
                                publishProgress(1L,1L); //actualizar icone para amarelo


                            } else {
                                tostalonga("Incompatible ELM327, only " + contagem + "/9 tests passed.");
                                tostalonga(getString(R.string.click_retry));


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
                                    if (debugMode) tosta("excepcao na 1a conversão para long:" + e);
                                    longo = 0;
                                }

                                //converter para flutuante
                                socx475 = longo / 47.5;
                                //validação
                                if (socx475 < 0.0 || socx475 > 150.0) socx475 = -1.0;


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





                    passo =23;  //voltar para o 21 - free frames
                }


                if (passo == 23) { //passo de actualizar ecrans na UI
                    publishProgress(0L,23L);


                    if (ELMREADY2==2)publishProgress(2L,0L);
                    //volta ao passo 21
                    passo = 21;
                }


                //verificação do watchdog
                //excepções são o passo 3 onde fica à espera do input do utilizador e
                //e os passos 21 e 22 que normalmente acabam sempre no 22
                if ((passo == passoanterior) && (passo!=3) && (passo<=20)) watchdog++;
                else watchdog = 0;

                if (watchdog > 200) {
                    if (debugMode) tosta("Watchdog expired");
                    return null;
                }
                //grava o passo actual
                passoanterior = passo;

                //verificação de pausa
                if (PAUSAMAIN) detectapausa++;
                else detectapausa = 0;
                //quando detecta pausa pode ter 2 comportamentos
                //se background mode ligado então espera mais tempo
                if (detectapausa>50 && backGroundMode) {
                    //liberta recursos CPU

                    try { if (wakeLock.isHeld()) wakeLock.release(); }
                    catch (Exception x) { if (debugMode) tosta("exception wakelock.release:"+x); }
                    //espera

                    for (int i=0; i<500 && !isCancelled();i++ ){
                        SystemClock.sleep(10L);
                    }

                    //SystemClock.sleep(10000L);
                    //pede recursos CPU
                    try { wakeLock.acquire(); }
                    catch (Exception y) { if (debugMode) tosta("exception wakelock.aquire:"+y); }
                }
                //se background mode desligado então cancela a tarefa
                if (detectapausa>20 && !backGroundMode) return null;



                //tempo entre ciclos, necessário ?
                SystemClock.sleep(10);
            }

            return null;
        }




        @Override
        protected void onProgressUpdate(Long... percent) {
            // Proxy the call to the Activity.
            mCallbacks.onProgressUpdate2(percent[0], percent[1]);
        }

        //quando é cancelada
        @Override
        protected void onCancelled() {
            // Proxy the call to the Activity.
            //mNotificationManager2.cancelAll();
            mCallbacks.onCancelled2();
            mRunning = false;


            // desactivar os listeners
            try {
                getActivity().unregisterReceiver(mReceiver3);
                getActivity().unregisterReceiver(mReceiver4);
            }
            catch (Exception e) {
                if (debugMode) tosta("exception ao unregisterreceivers no oncancelled da task2");
            }

            try {
                mmSocket2.close();
            }
            catch (Exception e2) {
                if (debugMode) tosta("Tried to close socket after task2 cancelled with no success");
                e2.printStackTrace();
            }

            try { if (wakeLock.isHeld()) wakeLock.release(); }
            catch (Exception x) { if (debugMode) tosta("exception wakelock.release:"+x); }


        }

        //quando completa
        @Override
        protected void onPostExecute(Void ignore) {
            // Proxy the call to the Activity.
            //mNotificationManager2.cancelAll();
            mCallbacks.onPostExecute2();
            mRunning = false;

            // desactivar os listeners
            try {
                getActivity().unregisterReceiver(mReceiver3);
                getActivity().unregisterReceiver(mReceiver4);
            }
            catch (Exception e) {
                if (debugMode) tosta("exception ao unregisterreceivers no onpostexecute da task2");
            }

            try {
                mmSocket2.close();
            }
            catch (Exception e2) {
                if (debugMode) tosta("Tried to close socket after task2 completed with no success");
                e2.printStackTrace();
            }

            try { if (wakeLock.isHeld()) wakeLock.release(); }
            catch (Exception x) { if (debugMode) tosta("exception wakelock.release:"+x); }
        }
    }






    /************************/
    /***** LOGS & STUFF *****/
    /************************/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (DEBUG) Log.i(TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);
        //((MainActivity)this.getActivity()).getBTSocket();
    }

    @Override
    public void onStart() {
        if (DEBUG) Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.i(TAG, "onStop()");
        super.onStop();
    }

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
                if (debugMode) tosta("Write failed! Socket is closed or not ready. Tried to send:" + message);
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
                if (debugMode) tosta("Write failed! Socket is closed or not ready. Tried to send 0x00");
            }
        }
        catch (Exception e) {
            //tosta("Error sending > " + e.getMessage());
        }
    }


    private int read() throws IOException {

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
            if (debugMode) tosta("IOException getting available bytes from ELM");
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
                if (debugMode) tosta("IOException reading from ELM");
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
                if (debugMode) tosta("IOException reading from ELM");
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
                if (debugMode) tosta("IOException reading from ELM");
            }

            if (readBuffer.contains("§")) {
            //if (readBuffer.contains("\r")) {
                parar = true;
            }

            if (Calendar.getInstance().getTimeInMillis() - start >= timeout) parar = true;

        }

        return readBuffer;

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
                        BTSOCKET2 = false; //faz com que a asynctask volte ao passo 3 o que actualiza o icone no main
                        BTREQ2 = false;
                        BTON2 = false;
                        tostalonga(getString(R.string.bt_turned_off));
                        break;
                    */
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        BTREQ2 = false;
                        BTON2 = false;
                        BTSOCKET2 = false;
                        tostalonga(getString(R.string.bt_turned_off));

                        break;


                    /*
                    case BluetoothAdapter.STATE_ON:
                        setButtonText("Bluetooth on");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        setButtonText("Turning Bluetooth on...");
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
            BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                BTSOCKET2 = false;

                cancel();

                // inform user
                tostalonga(getString(R.string.bt_conn_lost));
                SystemClock.sleep(2000L);

                if (!PAUSAMAIN) tostalonga(getString(R.string.click_retry));

                //try to close the socket


                try {
                    mmSocket2.close();
                }
                catch (Exception e2) {
                    if (debugMode) tosta("Tried to close socket after lost connection with no success");
                    e2.printStackTrace();
                }



            }
        }
    };




    public void tosta (String mensagem) {

        mToastHandler.showToast(mensagem,Toast.LENGTH_SHORT);
    }

    public void tostalonga (String mensagem) {

        mToastHandler.showToast(mensagem,Toast.LENGTH_LONG);
    }


    /**
     * A class for showing a <code>Toast</code> from background processes using a
     * <code>Handler</code>.
     *
     * @author kaolick
     */
    public class ToastHandler
    {
        // General attributes
        private Context mContext;
        private Handler mHandler;

        /**
         * Class constructor.
         *
         * @param _context
         *            The <code>Context</code> for showing the <code>Toast</code>
         */
        public ToastHandler(Context _context)
        {
            this.mContext = _context;
            this.mHandler = new Handler();
        }

        /**
         * Runs the <code>Runnable</code> in a separate <code>Thread</code>.
         *
         * @param _runnable
         *            The <code>Runnable</code> containing the <code>Toast</code>
         */
        private void runRunnable(final Runnable _runnable)
        {
            Thread thread = new Thread()
            {
                public void run()
                {
                    mHandler.post(_runnable);
                }
            };

            thread.start();
            thread.interrupt();
            thread = null;
        }

        /**
         * Shows a <code>Toast</code> using a <code>Handler</code>. Can be used in
         * background processes.
         *
         * @param _resID
         *            The resource id of the string resource to use. Can be
         *            formatted text.
         * @param _duration
         *            How long to display the message. Only use LENGTH_LONG or
         *            LENGTH_SHORT from <code>Toast</code>.
         */
        public void showToast(final int _resID, final int _duration)
        {
            final Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    // Get the text for the given resource ID
                    String text = mContext.getResources().getString(_resID);

                    Toast.makeText(mContext, text, _duration).show();
                }
            };

            runRunnable(runnable);
        }

        /**
         * Shows a <code>Toast</code> using a <code>Handler</code>. Can be used in
         * background processes.
         *
         * @param _text
         *            The text to show. Can be formatted text.
         * @param _duration
         *            How long to display the message. Only use LENGTH_LONG or
         *            LENGTH_SHORT from <code>Toast</code>.
         */
        public void showToast(final CharSequence _text, final int _duration)
        {
            final Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(mContext, _text, _duration).show();
                }
            };

            runRunnable(runnable);
        }
    }


    //função que verifica o resultado do pedido de ligação de bluetooth ao utilizador
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verificar se o bluetooth foi ligado com sucesso
        if (requestCode == REQUEST_ENABLE_BT2) {

            if (resultCode==Activity.RESULT_OK) {
                BTON2 = true;

            }

            if (resultCode==Activity.RESULT_CANCELED) {
                tostalonga(getString(R.string.pse_turnon_bt));
                BTON2 = false;



            }
        }



    }

    /*
    private void startNotificationOnStatusBar() {
        try {
            Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

            NotificationCompat.Builder mBuilder3 = new NotificationCompat.Builder(getActivity())
                    .setSmallIcon(R.mipmap.ic_settings)
                    .setContentTitle("Title")
                    .setContentIntent(intent)
                    .setPriority(0)
                    .setContentText("Content text")
                    .setAutoCancel(true);
            mNotificationManager3 = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager3.notify(0, mBuilder3.build());
        } catch (Exception e) {
        }
    }
    */


}