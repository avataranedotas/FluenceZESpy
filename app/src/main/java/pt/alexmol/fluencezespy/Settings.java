package pt.alexmol.fluencezespy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class Settings extends AppCompatActivity {



    private boolean keepscreenon;
    private boolean debugmodeon;
    private boolean backgroundmodeon;

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


        // If there are paired devices
        if (pairedDevices.size() > 0) {

            //load settings
            SharedPreferences settings = getSharedPreferences("pt.alexmol.fluencezespy.settings", 0);
            String deviceAddress=settings.getString("deviceAddress", null);
            keepscreenon=settings.getBoolean("keepscreenon",false);
            backgroundmodeon=settings.getBoolean("backgroundmodeon",false);
            debugmodeon=settings.getBoolean("debugmodeon",false);

            CheckBox keep = (CheckBox) findViewById(R.id.checkBoxScreen);
            keep.setChecked(keepscreenon);

            CheckBox back = (CheckBox) findViewById(R.id.checkBoxBackground);
            back.setChecked(backgroundmodeon);

            CheckBox debug = (CheckBox) findViewById(R.id.checkBoxDebug);
            debug.setChecked(debugmodeon);



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


        }





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
            editor.putBoolean("keepscreenon",keepscreenon);
            MainActivity.keepscreenMain = keepscreenon;
            editor.putBoolean("backgroundmodeon",backgroundmodeon);
            MainActivity.backgroundMain = backgroundmodeon;
            editor.putBoolean("debugmodeon",debugmodeon);
            MainActivity.debugModeMain = debugmodeon;


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
                if (checked)
                backgroundmodeon = true;
                else
                backgroundmodeon = false;
                break;
            case R.id.checkBoxDebug:
                if (checked)
                debugmodeon = true;
                else
                debugmodeon = false;
                break;

            case R.id.checkBoxScreen:
                if (checked)
                    keepscreenon = true;
                else
                    keepscreenon = false;
                    break;

        }
    }








}
