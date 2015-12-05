package pt.alexmol.fluencezespy;

import android.app.Activity;
//import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.*;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.otto.Subscribe;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private static MainActivity instance = null;
    private final static int SETTINGS_ACTIVITY = 7;
    private final static int REQUEST_ENABLE_BT = 3;
    static boolean debugModeMain = false;
    static boolean backgroundMain = false;
    static boolean keepscreenMain= false;
    static boolean reverseModeMain = false;
    private static int ELMREADY = 0;
    private boolean sair = false;
    public static int[] valoresmemorizados;
    public static short[] tensoesdascelulas;
    public static boolean[] shuntscelulas;
    private static NotificationCompat.Builder mBuilder;
    private static NotificationManager mNotificationManager;
    private static BTELMAsyncTask tarefa;
    public static int smallestwidthdp;
    public static float densidade;
    public static boolean TABLET = false;
    public static boolean landscape;

    public Intent starterIntent;

    public static boolean noite = false;
    public static boolean autonightmode = false;
    public static boolean redesenhar = false;

    public static int ecran;

    /**
     * The android.support.v4.view.PagerAdapter that will provide fragments for
     * each of the sections. We use a
     * android.support.v4.app.FragmentPagerAdapter derivative, which will keep
     * every loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * android.support.v4.app.FragmentStatePagerAdapter.
     */
    private MyPagerAdapter mSectionsPagerAdapter;

    /**
     * The ViewPager that will host the section contents.
     */
    private ViewPager mViewPager;


    //função para mostrar notificações no ecran
    private static void toast(final String message)
    {
        //as notificações correm numa thread à parte
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(instance, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //função para mostrar notificações no ecran
    private static void toastlong(final String message)
    {
        //as notificações correm numa thread à parte
        instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(instance, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    //**********************************************************************
    // códigos de eventos recebidos
    //  2, 1 btelmasync started
    //  2, 2 btelmasync idling
    //  2, 3 btelmasync running
    //  2, 4 btelmasync canceled
    //  2, 5 btelmasync complete
    //
    //  1, 0 ELM vermelho
    //  1, 1 ELM amarelo
    //  1, 2 ELM verde
    //
    //  0, Passo
    //
    //  3, 0 Actualizar páginas
    //
    //  4, 0 Pedido de ligação de bluetooth
    //
    //100, Socx475 x100 %
    //101, tempbat1 C
    //102, tempbat2 C
    //103, tempbat3 C
    //104, tempbat4 C
    //105, cableplugged 0=disconnected 1=inserted and ready 2=inserted release pressed
    //106, evse current pilot A
    //107, Pack voltage x2 V
    //108, hv battery temp ?
    //109, maxchargingpower x10 kW
    //110, batterymaxinput x100 kW
    //111, batterymaxoutput x100kW
    //112, pack voltage x100 V
    //113, battery current x1000 A signed
    //114, 12V voltage x1000 V
    //115, REAL SOC x10000 %
    //116, Ah x10000 Ah
    //117, tempbat minimum C
    //118, Pack health x2 %
    //119, bat mileage km
    //120, bat total kwh
    //121, desconhecido1
    //122, desconhecido2
    //123, desconhecido3
    //124, desconhecido4
    //125, max battery charging power kw x10
    //126, desconhecido6
    //127, main contactor 0 OFF, 1 PRECharge, 2 ON
    //128, hv consumption loads x10W
    //129, desconhecido7
    //130, desconhecido8
    //131, remaining kWh x10
    //132, dashboard SOC %
    //133, range km
    //134, minutes until charge complete
    //135, average kWh/100km x10
    //136 highest cell voltage  mV
    //137 lowest cell voltage mV
    //138 weak cell voltage mV (unknow9)
    //139 desconhecido10
    //140 temperatura da bateria C x10
    //141 dashboard iluminated
    //142 day/night 0=day 1=night
    //143 right solar - SUSPENSO
    //144 left solar - SUSPENSO
    //145 solar - SUSPENSO
    //146 sunshine right - SUSPENSO
    //147 sunshine left - SUSPENSO
    //148 evporator setpoint C +40 x10 - SUSPENSO
    //149 Water setpoint C - SUSPENSO
    //150 Evaporator temperature C +40 x10
    //151 heating water temperature +40C
    //152 evaporator temperature C +40 x2.5 - SUSPENSO
    //153 internal temp C +40 x2.5
    //154 internal humidity x2 %
    //155 motor water pump, 10=stopped 20=running - SUSPENSO
    //156 charger water pump, 10=stopped 20=running  - SUSPENSO
    //157 heater water pump, 10=stopped 20=speed 0? 41.. speed  - SUSPENSO
    //158 heater pump requested speed 10=stopped 20=speed 0? 41.. speed - SUSPENSO
    //159 partial energy spent by car kWh x1000
    //160 DCDC converter temperature ??????
    //161 inverter temperature  ????
    //162 battery cooling fans - SUSPENSO
    //163 battery fan external speed - SUSPENSO
    //164 battery fan internal speed - SUSPENSO
    //165 external temperature +40C
    //166 odometer km x100
    //167 motor current x32 A
    //168 wiper stalk buttons 1=Down 2=Up 0=nothing - SUSPENSO
    //169 a/c key pressed - SUSPENSO
    //170 DCDC converter temperature % ???
    //171 inverter temperature % ??? - SUSPENSO
    //172 peb current x4 - SUSPENSO
    //173 PEB 1E - SUSPENSO
    //174 PEB 1F - SUSPENSO
    //175 bad cell threshold mV - SUSPENSO
    //176 weak cell threshold mV - SUSPENSO
    //177 GO 0=off 1=on

    //296 Ponto 0 do parcial 2 km
    //297 Ponto 0 do parcial 2 kWh
    //298 Ponto 0 do parcial 1 km
    //299 Ponto 0 do parcial 1 kWh



    //501, tensão célula 1
    //502, tensão célula 2
    //.........
    //596, tensão célula 96

    //601, shunt célula 1
    //602, shunt célula 2
    //..
    //696, shunt célula 96





    @Subscribe
    public void recebereventos (BTELMTaskResultEvent event) {
        //Toast.makeText(this,"Resultado:"+ event.getResult()[0] +"/"+event.getResult()[1], Toast.LENGTH_SHORT).show();

        //evento de actualização de passo
        if (event.getResult()[0]==0 ) {
        actualizapasso(  String.valueOf(event.getResult()[1] ));

        }

        //evento de início da btelmtask, liga notificação
        if (event.getResult()[0]==2 && event.getResult()[1]==1 ) {
            if(backgroundMain) mostranotificacao(1);
        }

        //evento de btelmtask cancelada, desliga notificação
        if (event.getResult()[0]==2 && event.getResult()[1]==4 ) {
            if(backgroundMain) cancelatodasnotif();
            if (sair) {
                finish();
                return;
            }
        }

        //evento de btelmtask completa, desliga notificação
        if (event.getResult()[0]==2 && event.getResult()[1]==5 ) {
            if(backgroundMain) cancelatodasnotif();
            if (sair) {
                finish();
                return;
            }
        }

        //evento de estado do ELM
        if (event.getResult()[0]==1 && event.getResult()[1]==0 ) {
            vermelho();
        }

        //evento de estado do ELM
        if (event.getResult()[0]==1 && event.getResult()[1]==1 ) {
            amarelo();
        }

        //evento de estado do ELM
        if (event.getResult()[0]==1 && event.getResult()[1]==2 ) {
            verde();
        }

        //evento actualização de páginas
        if (event.getResult()[0]==3 && event.getResult()[1]==0 ) {
            actualizarpaginas(valoresmemorizados);
        }


        //evento chamar janela de ligar bluetooth
        if (event.getResult()[0]==4 && event.getResult()[1]==0 ) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        /*
        //no caso particular dos botões do a/c

        if (event.getResult()[0]==169 ) {
            //se botão premido
            if (event.getResult()[1] != valoresmemorizados[69]) {
                ecran++;
                if (ecran >2) ecran=0;

                ActionBar actionBar = getSupportActionBar();
                actionBar.setSelectedNavigationItem(ecran);
            }

        }
        */




        //evento de recepção de valores para memorizar
        //100 corresponde ao indice 0
        if (event.getResult()[0]>=100 && event.getResult()[0]<300 ) {
            valoresmemorizados[  ( event.getResult()[0] -100)  ]=event.getResult()[1];

        }

        //no caso particular da corrente da bateria (113) se o contactor principal estiver aberto (diferente de 2) forçar valor 0 na corrente

        if (event.getResult()[0]==113) {
            if (valoresmemorizados[27]!=2) valoresmemorizados[13] = 0;
        }

        //no caso particular do bit de dia/noite em modo automático

        if (event.getResult()[0]==142 && autonightmode) {
            //se é de dia e recebe noite
            if (!noite && event.getResult()[1]==1) recriar();

            //se é de noite e recebe dia
            if (noite && event.getResult()[1]==0) recriar();

        }

        /*
        //no caso particular dos botões do limpa-vidros

        if (event.getResult()[0]==168 ) {
            //se botão para baixo
            if (event.getResult()[1]==1 && ecran >0) {
                ecran--;
                ActionBar actionBar = getSupportActionBar();
                actionBar.setSelectedNavigationItem(ecran);
            }

            //se botão para cima
            if (event.getResult()[1]==2 && ecran <2) {
                ecran++;
                ActionBar actionBar = getSupportActionBar();
                actionBar.setSelectedNavigationItem(ecran);
            }

        }
        */



        //evento de recepção de tensões de células para memorizar
        //501 corresponde ao indice 0
        if (event.getResult()[0]>=501 &&  event.getResult()[0]<=596) {
            tensoesdascelulas[  ( event.getResult()[0] -501)  ]= (short) event.getResult()[1];

        }

        //evento de recepção de shunts de células para memorizar
        //601 corresponde ao indice 0
        if (event.getResult()[0]>=601 &&  event.getResult()[0]<=696) {
            if (event.getResult()[1]==1 ) shuntscelulas[  ( event.getResult()[0] -601)  ]= true;
            else shuntscelulas[  ( event.getResult()[0] -601)  ]= false;

        }


    }


    //receber eventos da page2
    @Subscribe
    public void recebereventospage2 (Page2TaskResultEvent event) {
        //Toast.makeText(this,"Resultado:"+ event.getResult()[0] +"/"+event.getResult()[1], Toast.LENGTH_SHORT).show();

        //evento acertar parciais 1 kWh e km
        if (event.getResult() == 3001) {


            //acertar kWh
            valoresmemorizados[199]=valoresmemorizados[59];
            actualizarpaginas(valoresmemorizados);

            //acertar km
            valoresmemorizados[198]=valoresmemorizados[66];
            actualizarpaginas(valoresmemorizados);

            toast("Trip 1 was reset!");

        }


        //evento acertar parciais 2 kWh e km
        if (event.getResult() == 3002) {


            //acertar kWh
            valoresmemorizados[197]=valoresmemorizados[59];
            actualizarpaginas(valoresmemorizados);

            //acertar km
            valoresmemorizados[196]=valoresmemorizados[66];
            actualizarpaginas(valoresmemorizados);


            toast("Trip 2 was reset!");


        }


    }



    //receber eventos da page1
    @Subscribe
    public void recebereventospage1 (Page1TaskResultEvent event) {
        //Toast.makeText(this,"Resultado:"+ event.getResult()[0] +"/"+event.getResult()[1], Toast.LENGTH_SHORT).show();

        //evento acertar parciais 1 kWh e km
        if (event.getResult() == 3001) {

            //acertar kWh
            valoresmemorizados[199]=valoresmemorizados[59];
            actualizarpaginas(valoresmemorizados);


            //acertar km
            valoresmemorizados[198]=valoresmemorizados[66];
            actualizarpaginas(valoresmemorizados);
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        /*
        if (getIntent().hasExtra("bundle") && savedInstanceState==null){
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }
        */
        starterIntent = getIntent();


        super.onCreate(savedInstanceState);

        valoresmemorizados = new int[200];
        tensoesdascelulas = new short[96];
        shuntscelulas = new boolean[96];





        //carrega preferências
        SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);

        debugModeMain = settings.getBoolean("debugmodeon", false);
        backgroundMain = settings.getBoolean("backgroundmodeon", false);
        keepscreenMain = settings.getBoolean("keepscreenon",false);
        reverseModeMain = settings.getBoolean("reversemodeon", false);
        autonightmode = settings.getBoolean("autonightmode", false);
        noite = settings.getBoolean("night", false);


        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            valoresmemorizados = savedInstanceState.getIntArray("matriz");
            tensoesdascelulas = savedInstanceState.getShortArray("celulas");
            shuntscelulas = savedInstanceState.getBooleanArray("shunts");
            ecran = savedInstanceState.getInt("ecranactual");

        } else {
            //invalida valores
            int i;
            int invalido = Integer.MAX_VALUE;
            for (i=0;i<200;i++) valoresmemorizados[i]= invalido;
            short invalidoshort = Short.MAX_VALUE;
            for (i=0;i<96;i++) tensoesdascelulas[i]= invalidoshort;
            for (i=0;i<96;i++) shuntscelulas[i]= false;
            ecran = settings.getInt("ecran", 1);

            //recupera valores actuais
            //toast("Loading...");
            for (i=0; i<=199;i++) valoresmemorizados[i]=settings.getInt("valoresmemorizados"+i,invalido );
            for (i=0;i<96;i++) tensoesdascelulas[i]= (short) settings.getInt("tensoesdascelulas"+i,invalidoshort);
            for (i=0;i<96;i++) shuntscelulas[i]= settings.getBoolean("shunts"+i,false);




        }

        //tamanho do ecran utilizável, dimensão mínima em dp
        smallestwidthdp = this.getResources().getConfiguration().smallestScreenWidthDp;
        densidade = this.getResources().getConfiguration().densityDpi;





        landscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;



        // se o swdp for superior a 580 estamos num tablet
        TABLET = smallestwidthdp >= 580;
        //teste
        //TABLET = true;

        instance = this;






        if (!landscape) {
            if (TABLET ^ reverseModeMain) setContentView(R.layout.main_act_tablet);
            else setContentView(R.layout.main_act);
        }
        else {
            if (TABLET ^ reverseModeMain) setContentView(R.layout.main_act_tablet_land);
            else setContentView(R.layout.main_act_land);
        }


        MyBus.getInstance().register(this);


        //comunica à BTELMasynctask o novo contexto

        MyBus.getInstance().post(new MainTaskContextEvent(instance));



        if (!(TABLET  ^ reverseModeMain)) {


            // Set up the action bar.
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.setIcon(R.mipmap.ic_launcher);


            //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            //getSupportActionBar().setDisplayShowTitleEnabled(true);
            //getSupportActionBar().setTitle("xpto");
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);






            // Create the adapter that will return a fragment for each of the three
            // primary sections
            // of the app.
            mSectionsPagerAdapter = new MyPagerAdapter(this, this,
                    getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);


            // When swiping between different sections, select the corresponding
            // tab. We can also use ActionBar.Tab#select() to do this if we have
            // a reference to the Tab.
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                    ecran = position;
                    //toast("Pagina:"+position);
                }
            });

            // For each of the sections in the app, add a tab to the action bar.
            for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                // Create a tab with text corresponding to the page title defined by
                // the adapter. Also specify this Activity object, which implements
                // the TabListener interface, as the callback (listener) for when
                // this tab is selected.


                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));


            }

            //Escolher por defeito a 2ªpágina (indice 1)
            actionBar.setSelectedNavigationItem(ecran);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //toast ("inicio do onresume");



        if (keepscreenMain) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        //barra de notificações


        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notification_2_launcher)
                        .setContentTitle("Fluence ZE Spy")
                        .setContentText("Running in background")
                        //.setCategory(Notification.CATEGORY_STATUS)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setPriority(-1);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class );

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        final SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);


        //inicia asynctaskelm caso não esteja a correr
        try {

            //se a tarefa é null tem de ser criada e arrancada
            if (tarefa == null) {
                tarefa = new BTELMAsyncTask(instance);
                tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                if (debugModeMain) toast("tarefa era null, arrancada");
            }
            //se não é null verificar se já está a correr
            else {
                //se está em run ou pending (pre-execute)
                if ( tarefa.getStatus()==AsyncTask.Status.RUNNING || tarefa.getStatus()==AsyncTask.Status.PENDING  ) {
                    if (debugModeMain) toast ("tarefa estava em run ou pre-execute");
                }
                //se está já finalizada
                else {
                    if (debugModeMain) toast ("tarefa estava finalizada, arrancando novamente");
                    tarefa = new BTELMAsyncTask(instance);
                    tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            }

        }
        catch (Exception e){  if (debugModeMain) toast ("Exception ao iniciar a thread:"+e); }



        if(!settings.getBoolean("disclaimer", false)) {

            tarefa.cancel(true);


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Disclaimer");

            // set dialog message
            alertDialogBuilder
                    .setMessage(Html.fromHtml("<html>Fluence ZE Spy (“the software”) is provided as is. <b>Use the software at your own risk.</b> " +
                            "The authors make no warranties as to performance or fitness for a particular purpose, " +
                            "or any other warranties whether expressed or implied. No oral or written communication " +
                            "from or information provided by the authors shall create a warranty. Under no circumstances " +
                            "shall the authors be liable for direct, indirect, special, incidental, or consequential " +
                            "damages resulting from the use, misuse, or inability to use the software, even if the author " +
                            "has been advised of the possibility of such damages. These exclusions and limitations may not " +
                            "apply in all jurisdictions. You may have additional rights and some of these limitations may not " +
                            "apply to you. This software is only intended for scientific usage." +
                            "<br>" +
                            "<br>" +
                            "<b>By using this software you are interfering with your car and doing that with hardware and " +
                            "software beyond your control, created by a loose team of interested amateurs in this field. Any " +
                            "car is a possibly lethal piece of machinery and you might hurt or kill yourself or others using " +
                            "it, or even paying attention to the displays instead of watching the road.</b></html>"))
                    .setCancelable(true)
                    .setPositiveButton("I accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("disclaimer", true);
                            editor.apply();

                            if (!( tarefa.getStatus()==AsyncTask.Status.RUNNING || tarefa.getStatus()==AsyncTask.Status.PENDING  )) {
                                tarefa = new BTELMAsyncTask(instance);
                                tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            }


                            // current activity
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("I don't understand",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                    //MainActivity.this.finishAffinity(); requires API16
                                    finish();
                                    //android.os.Process.killProcess(android.os.Process.myPid());
                                    //System.exit(0);
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

        }
        else { //envia informação à tarefa que não está em pausa
            MyBus.getInstance().post(new MainTaskResultEvent(1));
            sair = false;
        }


        //if (landscape) toast("Landscape");
        //else toast ("Portrait");

        if (redesenhar) {
            redesenhar = false;
            recriar();
        }

    }



    @Override
    protected void onPause() {
        super.onPause();


        //indica à asynctask que vai entrar em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(2));

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // gravar os valores guardados
        savedInstanceState.putIntArray("matriz", valoresmemorizados);
        savedInstanceState.putShortArray("celulas", tensoesdascelulas);
        savedInstanceState.putBooleanArray("shunts", shuntscelulas);

        savedInstanceState.putInt("ecranactual", ecran);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
        if (debugModeMain) toast("Destroy Main");

        MyBus.getInstance().post(new MainTaskResultEvent(5));





        //se estiver isFinishing significa que está a acabar de vez ou que foi chamada pela notificação
        if (isFinishing()) {
            if (debugModeMain) toast("Main finishing");

            SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("ecran", ecran);

            //grava valores actuais
            //toast("Saving...");
            int i;
            for (i=0; i<=199;i++) editor.putInt("valoresmemorizados"+i, valoresmemorizados[i]);
            for (i=0;i<96;i++) editor.putInt("tensoesdascelulas"+i,tensoesdascelulas[i]);
            for (i=0;i<96;i++) editor.putBoolean("shunts"+i,shuntscelulas[i]);


            editor.commit();


        }

    }




    //criação da barra de menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //mostrar os botões de ligado e desligado
        MenuItem ligado = menu.findItem(R.id.connected_yes);
        MenuItem desligado = menu.findItem(R.id.connected_no);
        MenuItem aviso = menu.findItem(R.id.connected_elm);


        //ELMREADY é também actualizado pela task via métodos verde,amarelo e vermelho

        if (ELMREADY == 2 ) {
            ligado.setVisible(true);
            aviso.setVisible(false);
            desligado.setVisible(false);
        }
        if (ELMREADY == 1 ) {
            ligado.setVisible(false);
            aviso.setVisible(true);
            desligado.setVisible(false);
        }
        if (ELMREADY == 0) {
            ligado.setVisible(false);
            aviso.setVisible(false);
            desligado.setVisible(true);
        }



        return true;
    }


    //função que é chamada quando se carrega num botão da barra de opções
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Abre a página de settings caso o bluetooth seja supportado e esteja ligado
        //noinspection SimplifiableIfStatement

        if ((id == R.id.action_settings) && (mBluetoothAdapter != null) && (mBluetoothAdapter.isEnabled()) ){

            //pára asynctaskelm
            tarefa.cancel(true);

            Toast.makeText(MainActivity.this,getString(R.string.pse_wait_disc), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivityForResult(intent, SETTINGS_ACTIVITY);
            return true;
        }


        if ((id == R.id.action_exit) ){


            //se está em run ou pending (pre-execute)
            if ( tarefa.getStatus()==AsyncTask.Status.RUNNING || tarefa.getStatus()==AsyncTask.Status.PENDING  ) {
                //pára asynctaskelm
                tarefa.cancel(true);
                Toast.makeText(MainActivity.this, getString(R.string.pse_wait_disc), Toast.LENGTH_SHORT).show();
                //liga flag sair
                sair = true;

            }
            //se está já finalizada
            else {
                if (debugModeMain) toast ("tarefa estava finalizada, saindo");
                finish();
                return true;
            }

            return true;
        }



        if ((id == R.id.connected_no) ){


            //inicia asynctaskelm caso não esteja a correr
            try {

                //se a tarefa é null tem de ser criada e arrancada
                if (tarefa == null) {
                    tarefa = new BTELMAsyncTask(instance);
                    tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    if (debugModeMain) toast("tarefa era null, arrancada");
                }
                //se não é null verificar se já está a correr
                else {
                    //se está em run ou pending (pre-execute)
                    if ( tarefa.getStatus()==AsyncTask.Status.RUNNING || tarefa.getStatus()==AsyncTask.Status.PENDING  ) {
                        if (debugModeMain) toast ("tarefa estava em run ou pre-execute");
                    }
                    //se está já finalizada
                    else {
                        if (debugModeMain) toast ("tarefa estava finalizada, arrancando novamente");
                        tarefa = new BTELMAsyncTask(instance);
                        tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                }

            }
            catch (Exception e){  if (debugModeMain) toast ("Exception ao iniciar a thread:"+e); }

            return true;
        }



        return super.onOptionsItemSelected(item);
    }




    //função para actualizar os botões da barra de acção
    private static void refresca() {
        instance.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }



    //navegação por tabs

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    //@Override
    private void actualizapasso(String link) {


        if ( !(TABLET ^ reverseModeMain)) {

            Page1 fragmento = (Page1) mSectionsPagerAdapter.getRegisteredFragment(1);

            if (fragmento != null) {

                fragmento.setPasso(link);
            }
            //if (fragmento == null & debugModeMain) toast("Null");
        }
        else {


            //se for usada uma página main sem fragmentos remover o seguinte

            Page1 fragmento = (Page1) getSupportFragmentManager().findFragmentById(R.id.page1);

            fragmento.setPasso(link);



        }




    }


    //Ligar icone amarelo
    private void vermelho() {
        ELMREADY = 0;
        refresca();

    }

    //Ligar icone amarelo
    private void amarelo() {
        ELMREADY = 1;
        refresca();

    }
    //Ligar icone amarelo
    private void verde() {
        ELMREADY = 2;
        refresca();

    }



    //actualizar páginas
    private void actualizarpaginas(int[] arrayd) {

        //se modo swipe views
        if ( !(TABLET ^ reverseModeMain)) {
            try {

                Page1 fragmentoy1 = (Page1) mSectionsPagerAdapter.getRegisteredFragment(1);

                if (fragmentoy1 != null) {
                    fragmentoy1.actpag1(arrayd);
                }

                Page2 fragmentoy2 = (Page2) mSectionsPagerAdapter.getRegisteredFragment(2);

                if (fragmentoy2 != null) {
                    fragmentoy2.actpag2(arrayd);
                }

                Page0 fragmentoy0 = (Page0) mSectionsPagerAdapter.getRegisteredFragment(0);

                if (fragmentoy0 != null) {
                    fragmentoy0.actpag0(arrayd);
                }

            } catch (Exception e) {
                if (debugModeMain) toast("excepcao:" + e);
            }
        }
        //se modo ecran inteiro por fragmentos
        else {



            Page1 fragmento1 = (Page1) getSupportFragmentManager().findFragmentById(R.id.page1);
            fragmento1.actpag1(arrayd);

            Page0 fragmento0 = (Page0) getSupportFragmentManager().findFragmentById(R.id.page0);
            fragmento0.actpag0(arrayd);

            Page2 fragmento2 = (Page2) getSupportFragmentManager().findFragmentById(R.id.page2);
            fragmento2.actpag2(arrayd);


            //se for usada uma página main sem fragmentos
            //actpagmain(arrayd);

        }


    }

    //se for usada uma página main sem fragmentos
    /*
    public void actpagmain(int[] arraym) {
        int invalido = Integer.MAX_VALUE;

        if (arraym[28]!= invalido) {

                TextView view = (TextView) findViewById(R.id.hvloads_m);
                double temp = ((double) arraym[28]) / 10.0;
                view.setText("HV Loads: " + String.format("%2.1f", temp) + "kW");

        }

    }
    */





    private void mostranotificacao(int codigo) {

        mNotificationManager.notify(codigo, mBuilder.build());
    }


    private void cancelatodasnotif() {

        mNotificationManager.cancelAll();
    }




    //função que verifica o resultado do pedido de ligação de bluetooth ao utilizador
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verificar se o bluetooth foi ligado com sucesso
        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode== Activity.RESULT_OK) {
                //envia informação à tarefa que bluetooth foi ligado
                MyBus.getInstance().post(new MainTaskResultEvent(3));


            }

            if (resultCode==Activity.RESULT_CANCELED) {
                toastlong(getString(R.string.pse_turnon_bt));
                MyBus.getInstance().post(new MainTaskResultEvent(4));



            }
        }



    }


    public void recriar() {



        if (autonightmode) {


            if (valoresmemorizados[42] != Integer.MAX_VALUE && valoresmemorizados[42] == 1)
                noite = true;

            if (valoresmemorizados[42] != Integer.MAX_VALUE && valoresmemorizados[42] == 0)
                noite = false;

        }

        SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("ecran", ecran);
        editor.putBoolean("night", noite);
        editor.commit();


        finish();
        startActivity(starterIntent);



    }



}
