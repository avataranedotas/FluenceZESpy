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

public class Page0 extends Fragment {
    Context c;

    public Page0(){

    }



    /*
    public Page0(Context cxpto) {
        this.c = cxpto;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.section0, null);
        return       v;
    }



    //comentario
    @Override
    public void onResume() {
        super.onResume();
        MainActivity actividademain0 = (MainActivity)getActivity();
        actpag0(actividademain0.valoresmemorizados);

    }




    public void actpag0(long[] array0) {
        if (array0[0]!=-100) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_0);
            double temp = ((double) array0[0]) / 100.0;
            view.setText("SOC:" + String.format("%3.2f", temp) + "%");
        }

    }

}