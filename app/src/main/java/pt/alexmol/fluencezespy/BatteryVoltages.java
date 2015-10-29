package pt.alexmol.fluencezespy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import pt.alexmol.fluencezespy.R;

public class BatteryVoltages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_voltages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyBus.getInstance().register(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        //indica à task que não está em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyBus.getInstance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battery_voltages, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
