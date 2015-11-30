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
import android.widget.ImageButton;
import android.widget.ImageView;
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


        if (!MainActivity.noite) {

            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section2_land, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section2, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section2, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section2_land, null);                                                           //vista landscape phone
            }

        }
        else {
            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section2_land_noite, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section2_noite, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section2_noite, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section2_land_noite, null);                                                           //vista landscape phone
            }

        }


        Button botaoreset1 = (Button) v.findViewById(R.id.button1t);

        botaoreset1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View va) {


                //envia pedido ao Main para reset trip 1

                MyBus.getInstance().post(new Page2TaskResultEvent(3001));

                return true;

            }
        });


        Button botaoreset2 = (Button) v.findViewById(R.id.button2t);

        botaoreset2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View va) {


                //envia pedido ao Main para reset trip 2

                MyBus.getInstance().post(new Page2TaskResultEvent(3002));

                return true;

            }
        });







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


        TextView view;
        ImageView imgview;

        /*
        if (array2[0]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_2);
            double temp = ((double) array2[0]) / 100.0;
            view.setText("SOC:" + String.format("%3.2f", temp) + "%");
        }
        */


        //Trip 1
        double parkwh1 = 0.0;
        double parkm1 = 0.0;
        double media1 = 0.0;

        //parcial kwh = total - ponto 0
        if (array2[199]!=invalido && array2[59]!= invalido) {
            view = (TextView) getView().findViewById(R.id.t1_kwh);

            parkwh1 = (array2[59] / 1000.0) - (array2[199] / 1000.0);

            //se der negativo algo correu mal ou o contador deu a volta, faz reset
            if (parkwh1<0.0) MyBus.getInstance().post(new Page2TaskResultEvent(3001));

            view.setText(String.format("%3.2f", parkwh1));
        }


        //parcial km = total - ponto 0
        if (array2[198]!=invalido && array2[66]!= invalido) {
            view = (TextView) getView().findViewById(R.id.t1_km);

            parkm1 = (array2[66] / 100.0) - (array2[198] / 100.0);
            view.setText(String.format("%3.1f", parkm1));
        }


        //media
        if (parkm1 >0.0) {
            media1 = parkwh1 / (parkm1 / 100.0);
            if (media1 > 99.99) media1 = 99.99;
        }

        view = (TextView) getView().findViewById(R.id.t1_avg);
        view.setText(String.format("%2.2f", media1));




        //Trip 2
        double parkwh2 = 0.0;
        double parkm2 = 0.0;
        double media2 = 0.0;

        //parcial kwh = total - ponto 0
        if (array2[197]!=invalido && array2[59]!= invalido) {
            view = (TextView) getView().findViewById(R.id.t2_kwh);

            parkwh2 = (array2[59] / 1000.0) - (array2[197] / 1000.0);

            //se der negativo algo correu mal ou o contador deu a volta, faz reset
            if (parkwh2<0.0) MyBus.getInstance().post(new Page2TaskResultEvent(3002));

            view.setText(String.format("%3.2f", parkwh2));
        }


        //parcial km = total - ponto 0
        if (array2[196]!=invalido && array2[66]!= invalido) {
            view = (TextView) getView().findViewById(R.id.t2_km);

            parkm2 = (array2[66] / 100.0) - (array2[196] / 100.0);
            view.setText(String.format("%3.1f", parkm2));
        }


        //media
        if (parkm2 >0.0) {
            media2 = parkwh2 / (parkm2 / 100.0);
            if (media2 > 99.99) media2 = 99.99;
        }

        view = (TextView) getView().findViewById(R.id.t2_avg);
        view.setText(String.format("%2.2f", media2));




    }



}