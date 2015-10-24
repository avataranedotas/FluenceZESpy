package pt.alexmol.fluencezespy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Page1.pagina1interface,ActionBar.TabListener,TaskFragment.TaskCallbacks,TaskFragment2.TaskCallbacks2 {

    private static final boolean DEBUG = true; // Set this to false to disable logs.

    private static final String TAG = MainActivity.class.getSimpleName();

    private static MainActivity instance = null;

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private static final String TAG_TASK_FRAGMENT2 = "task_fragment2";

    public final static int SETTINGS_ACTIVITY = 7;

    public TaskFragment mTaskFragment;
    public TaskFragment2 mTaskFragment2;

    private static boolean debugModeMain = false;
    private static boolean backgroundMain = false;
    public static int ELMREADY = 0;
    private static boolean botaoexit = false;
    private static boolean pausado = false;


    public static long[] valoresmemorizados;

    public static NotificationCompat.Builder mBuilder;
    public static NotificationManager mNotificationManager;



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

    protected static BluetoothAdapter mBluetoothAdapter;


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

        SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
        if (settings.getBoolean("backgroundmodeon",false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        //carrega preferências
        debugModeMain = settings.getBoolean("debugmodeon", false);
        backgroundMain = settings.getBoolean("backgroundmodeon",false);

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



        //Fragmentos headless

        FragmentManager fm = getSupportFragmentManager();
        //mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        mTaskFragment2 = (TaskFragment2) fm.findFragmentByTag(TAG_TASK_FRAGMENT2);

        /*
        // If the Fragment is non-null, then it is being retained
        // over a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new TaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
        */


        // If the Fragment is non-null, then it is being retained
        // over a configuration change.
        if (mTaskFragment2 == null) {
            mTaskFragment2 = new TaskFragment2();
            fm.beginTransaction().add(mTaskFragment2, TAG_TASK_FRAGMENT2).commit();
        }



    }

    @Override
    public void onResume() {
        super.onResume();

        //toast ("inicio do onresume");


        mTaskFragment2.pausamain(false);
        pausado = false;


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
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        //PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        resultIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        cancelatodasnotif();

        //iniciar background task de ligação bluetooth
        if (!(mTaskFragment2.isRunning())) {
            //mostranotificacao(1);
            mTaskFragment2.start();
        }

        botaoexit = false;

    }



    @Override
    protected void onPause() {
        super.onPause();


        mTaskFragment2.pausamain(true);
        pausado = true;

        // mId 1 allows you to update the notification later on.

        if (mTaskFragment2.isRunning() && backgroundMain) mostranotificacao(1);


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
        super.onDestroy();
        if (debugModeMain) toast("Destroy Main");

        if (backgroundMain) {
            if (isFinishing()) {
                if (!botaoexit) mTaskFragment2.delay(true);
                if (debugModeMain) toast("Main beeing finished");
                if ((mTaskFragment2.isRunning())) {
                    cancelatodasnotif();
                    mTaskFragment2.cancel();
                }


            } else {
                mTaskFragment2.delay(false);
            }
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


        //ELMREADY é também actualizado pela task2 via métodos verde,amarelo e vermelho

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

            //cancela a task2 se estiver a correr
            if ((mTaskFragment2.isRunning()) ) {
                cancelatodasnotif();
                mTaskFragment2.cancel();
            }
            Toast.makeText(MainActivity.this,"Please wait for bluetooth disconnection...", Toast.LENGTH_LONG).show();
            while ((mTaskFragment2.isRunning()));

            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivityForResult(intent, SETTINGS_ACTIVITY);
            return true;
        }


        if ((id == R.id.action_exit) ){

            //Pára a taskfragment2 caso esteja a correr,o que deve fechar o socket
            botaoexit = true;

            if ((mTaskFragment2.isRunning()) ) {
                cancelatodasnotif();
                mTaskFragment2.cancel();
            }


            finish();

            return true;
        }



        if ((id == R.id.connected_no) ){

            //Inicia a task2 caso esteja parada
            if (!(mTaskFragment2.isRunning()) ) {
                //mostranotificacao(1);
                mTaskFragment2.start();
            }




            return true;
        }



        return super.onOptionsItemSelected(item);
    }




    //função para actualizar os botões da barra de acção
    public static void refresca() {
        instance.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }



    /*********************************/
    /***** TASK CALLBACK METHODS *****/
    /*********************************/

    @Override
    public void onPreExecute() {
        if (DEBUG) Log.i(TAG, "onPreExecute()");
        //mButton.setText(getString(R.string.cancel));
        if (debugModeMain) Toast.makeText(this, R.string.task_started_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int percent) {
        if (DEBUG) Log.i(TAG, "onProgressUpdate(" + percent + "%)");
        if (debugModeMain)  toast("recebido progresso da task1:" + percent);
        //mProgressBar.setProgress(percent * mProgressBar.getMax() / 100);
        //actualizarTexto(String.valueOf(percent));

    }

    @Override
    public void onCancelled() {
        if (DEBUG) Log.i(TAG, "onCancelled()");
        //mButton.setText(getString(R.string.start));
        //mProgressBar.setProgress(0);
        //mPercent.setText(getString(R.string.zero_percent));
        if (debugModeMain) Toast.makeText(this, R.string.task_cancelled_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostExecute() {
        if (DEBUG) Log.i(TAG, "onPostExecute()");
        //mButton.setText(getString(R.string.start));
        //mProgressBar.setProgress(mProgressBar.getMax());
        //mPercent.setText(getString(R.string.one_hundred_percent));
        if (debugModeMain) Toast.makeText(this, R.string.task_complete_msg, Toast.LENGTH_SHORT).show();
    }





    /*********************************/
    /***** TASK CALLBACK METHODS 2 *****/
    /*********************************/

    @Override
    public void onPreExecute2() {
        if (DEBUG) Log.i(TAG, "onPreExecute()");
        //mButton.setText(getString(R.string.cancel));
        ELMREADY = 0;
        refresca();
        if (debugModeMain) Toast.makeText(this, R.string.task2_start, Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onProgressUpdate2(long indice, long dado) {
        if (DEBUG) Log.i(TAG, "onProgressUpdate2(" + indice);
        //indice 0 é actualizar passo no écran
        if (indice == 0L) actualizapasso(String.valueOf(dado));

        //outros casos
        int temp = (int) indice;

        switch (temp) {

            case 1: //actualizar icone de estado
                if (dado == 0L) vermelho();
                if (dado == 1L) amarelo();
                if (dado == 2L) verde();
                break;

            case 2: //actualizar écrans
                actualizarpaginas(valoresmemorizados);
                break;

            case 100: //escrever SOCx475 na array de valores actuais
                valoresmemorizados[0] = dado;
                break;



        }

    }


    @Override
    public void onCancelled2() {
        if (DEBUG) Log.i(TAG, "onCancelled()");


        if (pausado)Toast.makeText(this,R.string.task2_canc, Toast.LENGTH_SHORT).show();

        cancelatodasnotif();
        ELMREADY = 0;
        refresca();


    }

    @Override
    public void onPostExecute2() {
        if (DEBUG) Log.i(TAG, "onPostExecute()");
        //mButton.setText(getString(R.string.start));
        //mProgressBar.setProgress(mProgressBar.getMax());
        //mPercent.setText(getString(R.string.one_hundred_percent));
        Toast.makeText(this, R.string.task2_canc, Toast.LENGTH_SHORT).show();

        cancelatodasnotif();
        ELMREADY = 0;
        refresca();


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




    @Override
    public void onBotaoRandom() {

        if (debugModeMain)toast("onBotaoRandom");

        if (mTaskFragment.isRunning()) {
            mTaskFragment.cancel();
        }
        else {
            mTaskFragment.start();
        }


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



    public void mostranotificacao (int codigo) {

        mNotificationManager.notify(codigo, mBuilder.build());
    }


    public void cancelatodasnotif() {

        mNotificationManager.cancelAll();
    }

}
