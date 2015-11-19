package pt.alexmol.fluencezespy;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by alexandre on 19-11-2015.
 */
public class NightModeOnItemSelectedListener  implements AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        //Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + pos /*+"  "+ parent.getItemAtPosition(pos).toString()*/, Toast.LENGTH_SHORT).show();

        if (pos==0) Settings.nightmodeautomatic = true;
        else {
            Settings.nightmodeautomatic = false;
            MainActivity.redesenhar = true;
            if (pos==1)  Settings.forcenightmode = false;
            if (pos==2)  Settings.forcenightmode = true;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
