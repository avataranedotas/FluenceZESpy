package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Page0 extends Fragment {
    // --Commented out by Inspection (05-11-2015 14:55):Context c;

    private int larguracarregador = -12345;


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


        View v;


        if (!MainActivity.noite) {

            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section0_land, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section0, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section0, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section0_land, null);                                                           //vista landscape phone
            }

        }
        else {
            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section0_land_noite, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section0_noite, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section0_noite, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section0_land_noite, null);                                                           //vista landscape phone
            }

        }



        /*
        if (!MainActivity.landscape) {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section0_land, null);   //vista portrait tablet
            else v = inflater.inflate(R.layout.section0, null);                                                             //vista portrait phone
        }
        else {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section0, null); //vista landscape tablet
            else v = inflater.inflate(R.layout.section0_land, null);                                                           //vista landscape phone
        }
        */


        /*
        if (!MainActivity.landscape) v = inflater.inflate(R.layout.section0, null);
        else v = inflater.inflate(R.layout.section0_land, null);
        */


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
        //MainActivity actividademain0 = (MainActivity)getActivity();
        actpag0(MainActivity.valoresmemorizados);

        temp_anterior = -255;

        larguracarregador = -12345;

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


        handler.post(timedTask);

    }


    private Handler handler = new Handler();

    private int cnt = 0;

    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {
            cnt=cnt+1;
            if (cnt>59) cnt = 0;


            ImageView image = (ImageView) getView().findViewById(R.id.ventax);
            image.setRotation(cnt*6);


            handler.postDelayed(timedTask, 30);
        }};



    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(timedTask);
    }



    public void actpag0(int[] array0) {
        int invalido = Integer.MAX_VALUE;

        if (array0[0]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_0);
            double temp = ((double) array0[0]) / 100.0;
            view.setText(String.format("%3.2f", temp) + "%");

            ProgressBar pb = (ProgressBar) getView().findViewById(R.id.pb_soc_0);
            if (temp >100.0) temp = 100.0;
            pb.setProgress((int) temp);


        }



        if (array0[1]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.battemp_0);
            int temp1 = ( array0[1]);
            int temp2 = ( array0[2]);
            int temp3 = ( array0[3]);
            int temp4 = ( array0[4]);
            view.setText("Temperatures: " + temp1 + "/" + temp2 + "/" + temp3 + "/" + temp4 + "C");
        }


        if (array0[40]!= invalido) {

            TextView view = (TextView) getView().findViewById(R.id.battempmin_0);

            double tempt = array0[40] / 10.0;
            view.setText(String.format("%2.1f", tempt) + "C");


            //old
            //view.setText(array0[17] + "C");

            //se a temperatura mudou
            if (array0[17]!=temp_anterior) {

                //cor da bola e do risco do termometro
                ShapeDrawable oval = new ShapeDrawable(new OvalShape());
                oval.setIntrinsicHeight(18);
                oval.setIntrinsicWidth(18);

                ShapeDrawable rectangulo = new ShapeDrawable(new RoundRectShape(new float[8], new RectF(), new float[8]));


                rectangulo.setIntrinsicHeight(52);
                rectangulo.setIntrinsicWidth(6);

                if (array0[17] < 15) {
                    oval.getPaint().setColor(Color.BLUE);
                    rectangulo.getPaint().setColor(Color.BLUE);
                }
                if (array0[17] >= 15 && array0[17] < 22) {
                    oval.getPaint().setColor(Color.GREEN);
                    rectangulo.getPaint().setColor(Color.GREEN);
                }
                if (array0[17] >= 22 && array0[17] < 25) {
                    oval.getPaint().setColor(getResources().getColor(R.color.amarelo));
                    rectangulo.getPaint().setColor(getResources().getColor(R.color.amarelo));
                }
                if (array0[17] >= 25 && array0[17] < 28) {
                    oval.getPaint().setColor(Color.rgb(255, 128, 0));
                    rectangulo.getPaint().setColor(Color.rgb(255, 128, 0));
                }
                if (array0[17] >= 28) {
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


        if (array0[5]!= invalido) {
            ImageView view = (ImageView) getView().findViewById(R.id.plug_0);
            if (array0[5]!=0 && array0[13]<0) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.INVISIBLE);

        }

        if (array0[6]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.evsepilot_0);
            if (array0[6]>=0 && array0[6]<48 && array0[5]!=0 && array0[5] != invalido && array0[13]<0) {
                view.setText(array0[6] + "A");
                view.setVisibility(View.VISIBLE);
            }
            else view.setVisibility(View.INVISIBLE);

        }



        if (array0[9]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxchargingpower_0);
            double temp;
            if (array0[25]!=invalido)  temp = ((double) array0[25]) / 10.0;
            else temp = ((double) array0[9]) / 10.0;
            if (temp>0.0 && array0[13]<0 && array0[5]==1) {
                view.setText("Max:" + String.format("%2.1f", temp));
                view.setVisibility(View.VISIBLE);
            }
            else view.setVisibility(View.INVISIBLE);
        }

        if (array0[10]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxinputpower_0);
            double temp = ((double) array0[10]) / 100.0;
            view.setText("Max Input Power: " + String.format("%2.1f", temp) + "kW");
        }

        if (array0[11]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.maxoutputpower_0);
            double temp = ((double) array0[11]) / 100.0;
            view.setText("Max Output Power: " + String.format("%2.1f", temp) + "kW");
        }

        if (array0[12]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.packvoltage_0);
            double temp = ((double) array0[12]) / 100.0;
            view.setText(String.format("%3.2f", temp) + "V");

            view = (TextView) getView().findViewById(R.id.cellmed_0);
            temp = temp / 96.0;
            view.setText(String.format("%1.3f", temp));
        }



        if (array0[13]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.batcurrent_0);
            double temp = Math.abs(((double) array0[13]) / 1000.0);
            view.setText(String.format("%3.2f", temp) + "A");
            if (temp == 0.0) view.setVisibility(View.INVISIBLE);
            else view.setVisibility(View.VISIBLE);
        }


        if (array0[15]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.realsoc_0);
            double temp = ((double) array0[15]) / 10000.0;
            view.setText("REAL: " + String.format("%3.3f", temp) + "%");
        }


        if (array0[16]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.ah_0);
            double temp = ((double) array0[16]) / 10000.0;
            view.setText(String.format("%2.3f", temp) + "Ah");
        }

        if (array0[18]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.health_0);
            double temp = ((double) array0[18]) / 2.0;
            view.setText(String.format("%3.1f", temp) + "%");
        }


        if (array0[19]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.batkm_0);
            //double temp = ((double) array0[19]) / 1.0;
            view.setText(array0[19] +"km");
        }

        if (array0[20]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.totcharged_0);
            //double temp = ((double) array0[20]) / 10.0;
            view.setText("Totalizer: " + array0[20] + "kWh");
        }

        //calculo de potencia e seta de fluxo de corrente
        if (array0[13]!= invalido && array0[12]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.kwbat_0);
            double temp = ((double) array0[13]) / 1000.0 * ((double) array0[12]) / 100.0 / 1000.0;
            view.setText( String.format("%2.1f", Math.abs(temp)) + "kW");
            if (temp == 0.0) view.setVisibility(View.INVISIBLE);
            else view.setVisibility(View.VISIBLE);


            //seta da bateria
            ImageView image = (ImageView) getView().findViewById(R.id.seta_0);


            //obtem largura da seta da primeira vez que corre para poder escalar a seguir

            if (larguracarregador == -12345) {
                View pai = getView().findViewById(R.id.esqbateria);
                pai.requestLayout();
                larguracarregador = pai.getWidth();
            }


            //temp = 30;



            //se kw entre -35 e -5
            if (temp <-5.0) {
                image.getLayoutParams().width = (int) ((0.3+ (Math.abs(temp+5)/50) ) * larguracarregador);
                image.setScaleX((float) 1.0);
            }

            //se kw entre -5 e 0
            if (temp >=-5.0 && temp <0.0) {
                image.getLayoutParams().width = (int) (larguracarregador * 0.3);
                image.setScaleX((float) 1.0);

            }

            //se kw entre 0 e 5
            if (temp >=0 && temp <5.0) {
                image.getLayoutParams().width = (int) (larguracarregador * 0.3);
                image.setScaleX((float) -1.0);
            }

            //se kw entre 5 e 80
            if (temp >=5.0) {
                image.getLayoutParams().width = (int) ((0.3+ (Math.abs(temp-5)/125) ) * larguracarregador);
                image.setScaleX((float) -1.0);
            }



            //se corrente for zero esconde seta
            if (temp == 0.0) image.setVisibility(View.INVISIBLE);
            else image.setVisibility(View.VISIBLE);
        }




        if (array0[21]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.bat_data_0);
            view.setText(""+
                            "IR: " + array0[23] + "m\u2126\n" +
                            "Ah Degradation: " + String.format("%3.2f",(((float)array0[91])/100.0) )  +"%\n"+
                            "IR Degradation: " + String.format("%3.2f",(((float)array0[92])/100.0) )  +"%\n"+
                            "Normal Charges: " + array0[29]   +"\n"+
                            "Full Charges: " + array0[30]   +"\n"+
                            "Partial Charges: " + array0[39]   +"\n"+
                            "Quick Charges: " + array0[26]   +
                            ""

            );
        }

        if (array0[87]!= invalido && array0[93]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.hvbatfail_0);

            view.setText("");
            if (array0[93]>0) view.setText("Isolation Fault Level:"+ (array0[93]) );
            if (array0[87]==2) view.setText("Battery Fail Alarm");
        }


        if (array0[8]!= invalido) {
            TextView view= (TextView) getView().findViewById(R.id.desconhecido3_0);



            view.setText(

                    "Unknown RAW Data\n"+
                    //"Pack:"+ (array0[12] / 100.0) + "/" + String.format("%3.2f",(array0[99]/32.0))+"/"+ String.format("%1.2f",  ((array0[12] / 100.0) - (array0[99]/32.0)))+"\n"+

                            "..."

            );
        }


        /*
        if (array0[90]!= invalido ) {

            TextView view = (TextView) getView().findViewById(R.id.desconhecido4_0);
            view.setText("744/764/2121/34/36:" + array0[90]+"\n"


            );

        }

        */

         /*   view = (TextView) getView().findViewById(R.id.desconhecido4_0);
            //view.setText("79B/7BB/2101/86/90: " + array0[24]);

            view = (TextView) getView().findViewById(R.id.desconhecido6_0);
            //view.setText("79B/7BB/2101/118/120: " + array0[26]);
            view = (TextView) getView().findViewById(R.id.desconhecido7_0);
            //view.setText("79B/7BB/2101/120/122: " + array0[29]);
            view = (TextView) getView().findViewById(R.id.desconhecido8_0);
            //view.setText("79B/7BB/2101/122/124: " + array0[30]);

        */


        //remaining kwh in the battery
        if (array0[31]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.kwhleft_0);
            double temp = ((double) array0[31]) / 10.0;
            view.setText(String.format("%2.1f", temp) + "kWh");
        }

        //estimated total capacity
        if (array0[31]!= invalido && array0[0]!= invalido && array0[16]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.totalkwh_0);

            //se Soc superior a 25%
            if (array0[0]>2500) {
                double temp = ((double) array0[31]) / (array0[0] / 100.0) * 10.0;
                view.setText(String.format("%2.1f", temp) + "kWh");
            }
            else {
                double temp = array0[16] / 10000.0 * 322.0 / 1000.0;
                view.setText("\u2248"+String.format("%2.1f", temp) + "kWh");
            }

        }

        /*
        //dashboard SOC
        if (array0[32]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.dashsoc_0);
            //double temp = ((double) array0[31]) / 10.0;
            view.setText("Dashboard SOC: "+array0[32]+"%");
        }
        */


        //highest cell voltage
        if (array0[36]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.cellmax_0);
            double temp = ((double) array0[36]) / 1000.0;
            view.setText(String.format("%1.3f", temp));

        }


        //lowest cell voltage
        if (array0[37]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.cellmin_0);
            double temp = ((double) array0[37]) / 1000.0;
            view.setText(String.format("%1.3f", temp));

            //diference
            view  = (TextView) getView().findViewById(R.id.dif_0);
            view.setText("Dif:"+ (array0[36] - array0[37]) + "mV");

        }


        //JV
        if (array0[36]!= invalido && array0[12]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.jv_0);
            double temp = (2.5*array0[12]/100 - 0.144*array0[36]) / 96.0;
            view.setText("JV:"+String.format("%1.3f", temp));
            //só mostrar o JV se a célula mais alta estiver abaixo de 3,71V
            if (array0[36]<3710) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.INVISIBLE);

        }

        /*
        //weak cells
        if ( array0[38]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.xptcellvolt_0);
            view.setText("Weak Cell threshold:"  + array0[38] + "mV");

        }
        */



        //ventoinha da bateria

        if (array0[95]!=invalido) {

            ImageView image = (ImageView) getView().findViewById(R.id.ventax);

            if (array0[95]==1) image.setVisibility(View.VISIBLE);
            else image.setVisibility(View.INVISIBLE);


        }

        /*
        // corrente conversor DCDC
        if ( (array0[84]!= invalido) && (array0[14]!= invalido) &&  (array0[5]!= invalido)) {


            ImageView image = (ImageView) getView().findViewById(R.id.ventax);

            double tempt;

            if (array0[27]==2) tempt = ((double) array0[84])/64.0;
            else tempt = 0.0;
            double potenciadcdc = tempt * (((double) array0[14])/1000.0);

            if ((potenciadcdc>500.0) && (array0[5]!=0)) {

                image.setVisibility(View.VISIBLE);
                //image.setRotation(((System.currentTimeMillis() / 25)) % 360);


            }
            else image.setVisibility(View.INVISIBLE);


        }

        */


        /*
        //desconhecido 10
        if (array0[39]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.desconhecido10_0);
            view.setText("Unknown 10: " + array0[39]);

        }
        */



    }

}