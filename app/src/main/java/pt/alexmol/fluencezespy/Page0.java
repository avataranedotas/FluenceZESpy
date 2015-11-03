package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Page0 extends Fragment {
    Context c;

    private final int invalido = Integer.MAX_VALUE;

    public Page0(){

    }

    private int temp_anterior = -255;

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


        ImageButton button = (ImageButton) v.findViewById(R.id.buttonbatterycell);

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

        temp_anterior = -255;

        /*

        //desenhar a bola do termometro, 18x18
        ShapeDrawable oval = new ShapeDrawable (new OvalShape());
        oval.setIntrinsicHeight(18);
        oval.setIntrinsicWidth(18);
        oval.getPaint().setColor(Color.BLACK);

        ImageView image = (ImageView) getView().findViewById(R.id.contembola);
        image.setBackgroundDrawable(oval);

        //desenhar o risco do termometro, rectangulo 6x52
        ShapeDrawable rectangulo = new ShapeDrawable(new RoundRectShape(new float[8],new RectF(),new float[8]));
        rectangulo.setIntrinsicHeight(52);
        rectangulo.setIntrinsicWidth(6);
        rectangulo.getPaint().setColor(Color.BLACK);

        ImageView image2 = (ImageView) getView().findViewById(R.id.contemrisco);
        image2.setBackgroundDrawable(rectangulo);
        */

        //ProgressBar pb = (ProgressBar) getView().findViewById(R.id.pb_soc_0);
        //pb.setProgress(100);


    }




    public void actpag0(int[] array0) {
        if (array0[0]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_0);
            double temp = ((double) array0[0]) / 100.0;
            view.setText(String.format("%3.2f", temp) + "%");

            ProgressBar pb = (ProgressBar) getView().findViewById(R.id.pb_soc_0);
            if (temp >100.0) temp = 100.0;
            pb.setProgress((int) temp);


        }



        if (array0[1]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.battemp_0);
            int temp1 = ( array0[1]);
            int temp2 = ( array0[2]);
            int temp3 = ( array0[3]);
            int temp4 = ( array0[4]);
            view.setText("Battery Temps:" + temp1+"C "+temp2+"C "+temp3+"C "+temp4+"C");
        }


        if (array0[17]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.battempmin_0);
            view.setText(array0[17] + "C");

            //se a temperatura mudou
            if (array0[17]!=temp_anterior) {

                //cor da bola e do risco do termometro
                ShapeDrawable oval = new ShapeDrawable(new OvalShape());
                oval.setIntrinsicHeight(18);
                oval.setIntrinsicWidth(18);

                ShapeDrawable rectangulo = new ShapeDrawable(new RoundRectShape(new float[8], new RectF(), new float[8]));


                rectangulo.setIntrinsicHeight(52);
                rectangulo.setIntrinsicWidth(6);

                if (array0[17] < 20) {
                    oval.getPaint().setColor(Color.BLUE);
                    rectangulo.getPaint().setColor(Color.BLUE);
                }
                if (array0[17] >= 20 && array0[17] < 25) {
                    oval.getPaint().setColor(Color.YELLOW);
                    rectangulo.getPaint().setColor(Color.YELLOW);
                }
                if (array0[17] >= 25 && array0[17] < 30) {
                    oval.getPaint().setColor(Color.rgb(255, 128, 0));
                    rectangulo.getPaint().setColor(Color.rgb(255, 128, 0));
                }
                if (array0[17] >= 30) {
                    oval.getPaint().setColor(Color.RED);
                    rectangulo.getPaint().setColor(Color.RED);
                }

                ImageView image = (ImageView) getView().findViewById(R.id.contembola);
                image.setBackgroundDrawable(oval);


                //tamanho do risco,
                double temp = array0[17] / 35.0 * 52.0;
                if  (temp >52.0) temp = 52.0;


                ImageView image2 = (ImageView) getView().findViewById(R.id.contemrisco);
                image2.setBackgroundDrawable(rectangulo);
                image2.getLayoutParams().height = (int) temp;




            }


            //guarda o valor anterior
            temp_anterior=array0[17];


        }


        if (array0[5]!=invalido) {
            ImageView view = (ImageView) getView().findViewById(R.id.plug_0);
            if (array0[5]!=0) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.INVISIBLE);

        }

        if (array0[6]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.evsepilot_0);
            if (array0[6]>0 && array0[6]<48 ) {
                view.setText(array0[6] + "A");
                view.setVisibility(View.VISIBLE);
            }
            else view.setVisibility(View.INVISIBLE);

        }



        if (array0[9]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxchargingpower_0);
            double temp = ((double) array0[9]) / 10.0;
            if (temp>0.0) {
                view.setText("Max:" + String.format("%2.1f", temp));
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
            view.setText(String.format("%3.2f", temp) + "V");
        }



        if (array0[13]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.batcurrent_0);
            double temp = Math.abs(((double) array0[13]) / 1000.0);
            view.setText(String.format("%3.2f", temp) + "A");
        }


        if (array0[15]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.realsoc_0);
            double temp = ((double) array0[15]) / 10000.0;
            view.setText("REAL: " + String.format("%3.3f", temp) + "%");
        }


        if (array0[16]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.ah_0);
            double temp = ((double) array0[16]) / 10000.0;
            view.setText(String.format("%2.3f", temp) + "Ah");
        }

        if (array0[18]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.health_0);
            double temp = ((double) array0[18]) / 2.0;
            view.setText(String.format("%3.1f", temp) + "%");
        }


        if (array0[19]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.batkm_0);
            //double temp = ((double) array0[19]) / 1.0;
            view.setText(array0[19] +"km");
        }

        if (array0[20]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.totcharged_0);
            //double temp = ((double) array0[19]) / 1.0;
            view.setText("Total Charged: " + array0[20] + "kWh");
        }

        if (array0[13]!=invalido && array0[12]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.kwbat_0);
            double temp = ((double) array0[13]) / 1000.0 * ((double) array0[12]) / 100.0 / 1000.0;
            view.setText( String.format("%2.1f", Math.abs(temp)) + "kW");

            //seta da bateria
            ImageView image = (ImageView) getView().findViewById(R.id.seta_0);
            //se kw entre -35 e -5
            if (temp <-5.0) {
                image.setScaleX((float) (1.0 + (-1.0 * ((temp + 5) / 17.5))));
                image.setScaleType(ImageView.ScaleType.FIT_END);
            }
            //se kw entre -5 e 0
            if (temp >=-5.0 && temp <0.0) {
                image.setScaleX((float) 1.0);
                image.setScaleType(ImageView.ScaleType.FIT_END);
            }
            //se kw entre 0 e 5
            if (temp >=0 && temp <5.0) {
                image.setScaleX((float) -1.0);
                image.setScaleType(ImageView.ScaleType.FIT_START);
            }
            //se kw entre 5 e 80
            if (temp >=5.0) {
                image.setScaleX((float) (-1.0 + (-1.0 * ((temp - 5) / 37.5))));
                image.setScaleType(ImageView.ScaleType.FIT_START);
            }

            //se corrente for zero esconde seta
            if (temp == 0.0) image.setVisibility(View.INVISIBLE);
            else image.setVisibility(View.VISIBLE);
        }



    }

}