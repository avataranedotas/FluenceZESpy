package pt.alexmol.fluencezespy;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by alexandre on 19-11-2015.
 */
public class UnitsOnItemSelectedListener implements AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + pos /*+"  "+ parent.getItemAtPosition(pos).toString()*/, Toast.LENGTH_SHORT).show();


        Settings.tipounidades=pos;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // nao fazer nada
    }

}
