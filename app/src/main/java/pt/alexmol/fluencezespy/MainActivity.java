package pt.alexmol.fluencezespy;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.squareup.otto.Subscribe;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private static MainActivity instance = null;
    public final static int SETTINGS_ACTIVITY = 7;
    public final static int REQUEST_ENABLE_BT = 3;
    protected static boolean debugModeMain = false;
    protected static boolean backgroundMain = false;
    protected static boolean keepscreenMain= false;
    public static int ELMREADY = 0;
    private boolean sair = false;
    public static long[] valoresmemorizados;
    public static NotificationCompat.Builder mBuilder;
    public static NotificationManager mNotificationManager;
    public static BTELMAsyncTask tarefa;


    /**
     * The android.support.v4.view.PagerAdapter that will provide fragments for
     * each of the sections. We use a
     * android.support.v4.app.FragmentPagerAdapter derivative, which will keep
     * every loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * android.support.v4.app.FragmentStatePagerAdapter.
     */
    MyPagerAdapter mSectionsPagerAdapter;

    /**
     * The ViewPager that will host the section contents.
     */
    ViewPager mViewPager;


    //função para mostrar notificações no ecran
    public static void toast(final String message)
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
    public static void toastlong(final String message)
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
    //100, Socx475 x100




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


        //evento de recepção de valores para memorizar
        //100 corresponde ao indice 0
        if (event.getResult()[0]>=100 ) {
            valoresmemorizados[ (int) ( event.getResult()[0] -100L)  ]=event.getResult()[1];

        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valoresmemorizados = new long[100];

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            valoresmemorizados = savedInstanceState.getLongArray("matriz");

        } else {
            //invalida valores
            valoresmemorizados[0]= -100;
        }


        instance = this;

        setContentView(R.layout.main_act);


        MyBus.getInstance().register(this);

        //carrega preferências
        SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);

        debugModeMain = settings.getBoolean("debugmodeon", false);
        backgroundMain = settings.getBoolean("backgroundmodeon",false);
        keepscreenMain = settings.getBoolean("keepscreenon",false);


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
        actionBar.setSelectedNavigationItem(1);


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
                        .setSmallIcon(R.drawable.ic_notification1)
                        .setContentTitle("Fluence ZE Spy")
                        .setContentText("Running in background")
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

        //inicia asynctaskelm caso não esteja a correr
        try {

            //se a tarefa é null tem de ser criada e arrancada
            if (tarefa == null) {
                tarefa = new BTELMAsyncTask(this);
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
                    tarefa = new BTELMAsyncTask(this);
                    tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            }

        }
        catch (Exception e){  if (debugModeMain) toast ("Exception ao iniciar a thread:"+e); }

        //envia informação à tarefa que não está em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(1));

        sair = false;
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
        savedInstanceState.putLongArray("matriz", valoresmemorizados);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
        if (debugModeMain) toast("Destroy Main");



        //se estiver isFinishing significa que está a acabar de vez ou que foi chamada pela notificação
        if (isFinishing()) {
            if (debugModeMain) toast("Main finishing");

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
                    tarefa = new BTELMAsyncTask(this);
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
                        tarefa = new BTELMAsyncTask(this);
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
    public static void refresca() {
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
    public void actualizapasso(String link) {


        Page1 fragmento = (Page1) mSectionsPagerAdapter.getRegisteredFragment(1);

        if (fragmento !=null) {

            fragmento.setPasso(link);
        }
        //if (fragmento == null & debugModeMain) toast("Null");



    }


    //Ligar icone amarelo
    public void vermelho() {
        ELMREADY = 0;
        refresca();

    }

    //Ligar icone amarelo
    public void amarelo() {
        ELMREADY = 1;
        refresca();

    }
    //Ligar icone amarelo
    public void verde() {
        ELMREADY = 2;
        refresca();

    }



    //actualizar páginas
    public void actualizarpaginas(long[] arrayd) {

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

        }
        catch (Exception e) {
            if (debugModeMain)toast("excepcao:"+e);
        }

    }



    public void mostranotificacao(int codigo) {

        mNotificationManager.notify(codigo, mBuilder.build());
    }


    public void cancelatodasnotif() {

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


}
