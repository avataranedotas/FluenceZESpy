package pt.alexmol.fluencezespy;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class Settings extends AppCompatActivity {



    private boolean keepscreenon;
    private boolean debugmodeon;
    private boolean backgroundmodeon;
    private boolean reversemodeon;
    private boolean simplepointeron;
    private boolean milesmodeon;
    private boolean alttripon;

    protected static boolean nightmodeautomatic;
    protected static boolean forcenightmode;

    protected static int tipounidades;

    protected static int tipobateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        //não é necessário parar a btelmtask pois foi parada pelo botão de acesso aos settings




        //Criar lista de dispositivos bluetooth emparelhados
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayAdapter mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);

        CheckBox reverse = (CheckBox) findViewById(R.id.checkboxreversemode);

        // If there are paired devices
        if (pairedDevices.size() > 0) {

            //load settings
            SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
            String deviceAddress=settings.getString("deviceAddress", null);
            keepscreenon=settings.getBoolean("keepscreenon",false);
            backgroundmodeon=settings.getBoolean("backgroundmodeon",false);
            debugmodeon=settings.getBoolean("debugmodeon",false);
            debugmodeon=false;
            reversemodeon=settings.getBoolean("reversemodeon",false);
            simplepointeron=settings.getBoolean("simplepointeron",false);
            alttripon=settings.getBoolean("alttripon",false);
            milesmodeon=settings.getBoolean("milesmodeon", false);
            tipounidades=settings.getInt("tipounidades", 0);
            tipobateria=settings.getInt("tipobateria",0);

            CheckBox keep = (CheckBox) findViewById(R.id.checkBoxScreen);
            keep.setChecked(keepscreenon);

            CheckBox back = (CheckBox) findViewById(R.id.checkBoxBackground);
            back.setChecked(backgroundmodeon);

            CheckBox simple = (CheckBox) findViewById(R.id.checkBoxSimple);
            simple.setChecked(simplepointeron);

            CheckBox trip = (CheckBox) findViewById(R.id.checkboxalttrip);
            trip.setChecked(alttripon);

            CheckBox miles = (CheckBox) findViewById(R.id.checkboxmiles);
            miles.setChecked(milesmodeon);

            CheckBox debug = (CheckBox) findViewById(R.id.checkBoxDebug);
            debug.setChecked(debugmodeon);


            reverse.setChecked(reversemodeon);
            //check current mode
            //se estamos num tablet
            if ( MainActivity.TABLET) reverse.setText("Switch to phone mode");
            else reverse.setText("Switch to tablet mode");



            // Loop through paired devices
            int i = 0;
            int index=-1;
            for (BluetoothDevice device : pairedDevices) {

                String deviceAlias = device.getName();
                try {
                    Method method = device.getClass().getMethod("getAliasName");
                    if(method != null) {
                        deviceAlias = (String)method.invoke(device);
                    }
                } catch (NoSuchMethodException e) {
                    // e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // e.printStackTrace();
                }

                mArrayAdapter.add(deviceAlias + "\n" + device.getAddress());
                // get the index of the selected item
                if(device.getAddress().equals(deviceAddress))
                    index=i;
                i++;


                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());


            }

            // display the list
            Spinner deviceList = (Spinner) findViewById(R.id.bluetoothDeviceList);
            deviceList.setAdapter(mArrayAdapter);
            // select the actual device
            deviceList.setSelection(index);
            deviceList.setSelected(true);


            Spinner spinnernight = (Spinner) findViewById(R.id.nightmodespinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapternight = ArrayAdapter.createFromResource(this, R.array.nightmode_array, android.R.layout.simple_list_item_1);
            // Specify the layout to use when the list of choices appears
            adapternight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnernight.setAdapter(adapternight);






            int modonoite = 0;

            if (!MainActivity.autonightmode) {
                if (MainActivity.noite) modonoite = 2;
                else modonoite = 1;
            }

            spinnernight.setSelection(modonoite);
            //spinnernight.setSelected(true);


            spinnernight.setOnItemSelectedListener(new NightModeOnItemSelectedListener());



            //spinner unidades

            Spinner spinnerunits = (Spinner) findViewById(R.id.unitsspinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapterunits = ArrayAdapter.createFromResource(this, R.array.units_array, android.R.layout.simple_list_item_1);
            // Specify the layout to use when the list of choices appears
            adapterunits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerunits.setAdapter(adapterunits);


            spinnerunits.setSelection(tipounidades);

            spinnerunits.setOnItemSelectedListener(new UnitsOnItemSelectedListener());



            //spinner tipo de bateria

            Spinner spinnerbattery = (Spinner) findViewById(R.id.batteryspinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapterbattery = ArrayAdapter.createFromResource(this, R.array.battery_array, android.R.layout.simple_list_item_1);
            // Specify the layout to use when the list of choices appears
            adapterbattery.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinnerbattery.setAdapter(adapterbattery);


            spinnerbattery.setSelection(tipobateria);

            spinnerbattery.setOnItemSelectedListener(new BatteryOnItemSelectedListener());



        }

        String versionName = BuildConfig.VERSION_NAME;



        try {
            String teste = getPackageName();
            PackageInfo pInfo = getPackageManager().getPackageInfo(teste,0);
            versionName = pInfo.versionName;
        }
        catch (Exception e ) {
            versionName = "";
        }


        TextView view = (TextView) findViewById(R.id.version);
        view.setText("Version "+ versionName);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //se carregar no botão aceitar
        if (id == R.id.action_ok) {


            // save settings
            SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
            SharedPreferences.Editor editor = settings.edit();
            Spinner deviceList = (Spinner) findViewById(R.id.bluetoothDeviceList);

            if (deviceList.getSelectedItem() != null) {
                //    MainActivity.debug("Settings.deviceAddress = " + deviceList.getSelectedItem().toString().split("\n")[1].trim());
                //    MainActivity.debug("Settings.deviceName = " + deviceList.getSelectedItem().toString().split("\n")[0].trim());
                editor.putString("deviceAddress", deviceList.getSelectedItem().toString().split("\n")[1].trim());
                editor.putString("deviceName", deviceList.getSelectedItem().toString().split("\n")[0].trim());


            }




            editor.putBoolean("autonightmode",nightmodeautomatic);
            MainActivity.autonightmode = nightmodeautomatic;

            editor.putBoolean("night",forcenightmode);
            MainActivity.noite = forcenightmode;

            editor.putBoolean("keepscreenon",keepscreenon);
            MainActivity.keepscreenMain = keepscreenon;
            editor.putBoolean("backgroundmodeon",backgroundmodeon);
            MainActivity.backgroundMain = backgroundmodeon;
            editor.putBoolean("debugmodeon",debugmodeon);
            MainActivity.debugModeMain = debugmodeon;
            editor.putBoolean("reversemodeon",reversemodeon);

            MainActivity.SimplePointerModeMain = simplepointeron;
            editor.putBoolean("simplepointeron",simplepointeron);

            MainActivity.AltTripModeMain = alttripon;
            editor.putBoolean("alttripon",alttripon);

            MainActivity.MilesModeMain = milesmodeon;
            editor.putBoolean("milesmodeon", milesmodeon);

            MainActivity.tipounidades = tipounidades;
            editor.putInt("tipounidades", tipounidades);

            MainActivity.tipobateria = tipobateria;
            editor.putInt("tipobateria", tipobateria);

            //MainActivity.reverseModeMain = reversemodeon;


            editor.commit();
            // finish
            finish();
            return true;

        }

        if (id == R.id.action_cancel) {
            // finish without saving the settings

            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBoxBackground:
                backgroundmodeon = checked;
                break;

            case R.id.checkBoxDebug:
                debugmodeon = checked;
                break;


            case R.id.checkBoxSimple:
                simplepointeron = checked;
                break;

            case R.id.checkboxmiles:
                milesmodeon = checked;
                break;

            case R.id.checkboxalttrip:
                alttripon = checked;


                if (alttripon) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                    // set title
                    alertDialogBuilder.setTitle("Alternative Trip Meter Mode");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("To get correct results on the Fluence ZE Spy Trip Meters DO NOT RESET the dashboard partial meters.")
                            .setCancelable(true);


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }







                break;


            case R.id.checkBoxScreen:
                keepscreenon = checked;
                    break;

            case R.id.checkboxreversemode:
                reversemodeon = checked;
                MainActivity.redesenhar = true;
                break;


        }
    }








}
