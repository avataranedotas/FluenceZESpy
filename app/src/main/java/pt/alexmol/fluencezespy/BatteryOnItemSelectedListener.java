package pt.alexmol.fluencezespy;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by alexandre on 17-10-2017.
 */
public class BatteryOnItemSelectedListener implements AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + pos /*+"  "+ parent.getItemAtPosition(pos).toString()*/, Toast.LENGTH_SHORT).show();


        Settings.tipobateria=pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // nao fazer nada
    }

}