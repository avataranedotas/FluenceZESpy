package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Page2 extends Fragment {
    // --Commented out by Inspection (05-11-2015 14:55):Context c;

    public Page2(){

    }
    /*
    public Page2(Context c) {
        this.c = c;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;


        if (!MainActivity.landscape) {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section2_land, null);   //vista portrait tablet
            else v = inflater.inflate(R.layout.section2, null);                                                             //vista portrait phone
        }
        else {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section2, null); //vista landscape tablet
            else v = inflater.inflate(R.layout.section2_land, null);                                                           //vista landscape phone
        }


        return       v;
    }

    //Ao iniciar a interacção com o utilizador vai fazer a actualização dos campos
    @Override
    public void onResume() {
        super.onResume();
        //MainActivity actividademain2 = (MainActivity)getActivity();
        actpag2(MainActivity.valoresmemorizados);

    }




    public void actpag2(int[] array2) {
        int invalido = Integer.MAX_VALUE;
        if (array2[0]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_2);
            double temp = ((double) array2[0]) / 100.0;
            view.setText("SOC:" + String.format("%3.2f", temp) + "%");
        }
    }



}