package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Page0 extends Fragment {
    Context c;

    private final int invalido = Integer.MAX_VALUE;

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


        //botao para tensões das células


        Button button = (Button) v.findViewById(R.id.buttonbatterycell);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {



                Intent intent = new Intent(getActivity(), BatteryVoltages.class);
                startActivity(intent);


            }
        });






        return       v;
    }



    //comentario
    @Override
    public void onResume() {
        super.onResume();
        MainActivity actividademain0 = (MainActivity)getActivity();
        actpag0(actividademain0.valoresmemorizados);

    }




    public void actpag0(int[] array0) {
        if (array0[0]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_0);
            double temp = ((double) array0[0]) / 100.0;
            view.setText("SOC:" + String.format("%3.2f", temp) + "%");
        }

        if (array0[1]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.battemp_0);
            int temp1 = ( array0[1]);
            int temp2 = ( array0[2]);
            int temp3 = ( array0[3]);
            int temp4 = ( array0[4]);
            view.setText("Battery Temps:" + temp1+"C "+temp2+"C "+temp3+"C "+temp4+"C");
        }

        if (array0[5]!=invalido) {
            ImageView view = (ImageView) getView().findViewById(R.id.plug_0);
            if (array0[5]!=0) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.INVISIBLE);

        }

        if (array0[6]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.evsepilot0);
            if (array0[6]>0 && array0[6]<48 ) {
                view.setText(array0[6] + "A");
                view.setVisibility(View.VISIBLE);
            }
            else view.setVisibility(View.INVISIBLE);

        }

        /*
        if (array0[8]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.hvbatterytemp_0);
            view.setText("HVBatteryTemp:" +array0[8]);
        }
        */

        if (array0[9]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxchargingpower_0);
            double temp = ((double) array0[9]) / 10.0;
            if (temp>0.0) {
                view.setText("Max Charging Power: " + String.format("%2.1f", temp) + "kW");
                view.setVisibility(View.VISIBLE);
            }
            else view.setVisibility(View.INVISIBLE);
        }

        if (array0[10]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxinputpower_0);
            double temp = ((double) array0[10]) / 100.0;
            view.setText("Max Input Power: " + String.format("%2.2f", temp) + "kW");
        }

        if (array0[11]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxoutputpower_0);
            double temp = ((double) array0[11]) / 100.0;
            view.setText("Max Output Power: " + String.format("%2.2f", temp) + "kW");
        }

        if (array0[12]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.packvoltage_0);
            double temp = ((double) array0[12]) / 100.0;
            view.setText("Pack Voltage: " + String.format("%3.2f", temp) + "V");
        }

        if (array0[13]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.batcurrent_0);
            double temp = ((double) array0[13]) / 1000.0;
            view.setText("Battery current: " + String.format("%3.2f", temp) + "A");
        }

        if (array0[15]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.realsoc_0);
            double temp = ((double) array0[15]) / 10000.0;
            view.setText("REAL SOC: " + String.format("%3.3f", temp) + "%");
        }

        if (array0[16]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.ah_0);
            double temp = ((double) array0[16]) / 10000.0;
            view.setText("Capacity: " + String.format("%2.3f", temp) + "Ah");
        }



    }

}