package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Context;
import android.graphics.Color;
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

public class Page1 extends Fragment {
    // --Commented out by Inspection (05-11-2015 14:55):Context c;

    //private pagina1interface listener;



    private int rotafan = 0;


    public Page1(){

    }

    //será necessário ?
    //public Page1(Context c) {
    //    this.c = c;
    //}


    /*
    public interface pagina1interface {
        //public void actualizarTexto(String link);
        //public void onBotaoRandom();
    }
    */

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        //MainActivity.toast("onattach");

        /*
        if (context instanceof pagina1interface) {
            listener = (pagina1interface) context;
            //MainActivity.toast("listener");
        } else {
            MainActivity.toast("must implement pagina1interface");
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.pagina1interface");

        }
        */

        //MyBus.getInstance().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v;


        if (!MainActivity.noite) {

            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section1_land, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section1, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section1, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section1_land, null);                                                           //vista landscape phone
            }

        }
        else {
            if (!MainActivity.landscape) {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section1_land_noite, null);   //vista portrait tablet
                else
                    v = inflater.inflate(R.layout.section1_noite, null);                                                             //vista portrait phone
            } else {
                if (MainActivity.TABLET ^ MainActivity.reverseModeMain)
                    v = inflater.inflate(R.layout.section1_noite, null); //vista landscape tablet
                else
                    v = inflater.inflate(R.layout.section1_land_noite, null);                                                           //vista landscape phone
            }

        }




        /*
        if (!MainActivity.landscape) {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section1_land, null);   //vista portrait tablet
            else v = inflater.inflate(R.layout.section1, null);                                                             //vista portrait phone
        }
        else {
            if (MainActivity.TABLET ^ MainActivity.reverseModeMain) v = inflater.inflate(R.layout.section1, null); //vista landscape tablet
            else v = inflater.inflate(R.layout.section1_land, null);                                                           //vista landscape phone
        }
        */



        //botão para ver o VIN completo

        ImageButton botao = (ImageButton) v.findViewById(R.id.vinbutton);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {

            MyBus.getInstance().post(new Page1TaskResultEvent(4001));

            }
        });


        //botao para iniciar uma task


        /*
        Button button = (Button) v.findViewById(R.id.buttonxpto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                //MainActivity.toast("botao carregado");

                //envia pedido para desbloquear idle da BTELMtask (commando =1)

                MyBus.getInstance().post(new Page1TaskResultEvent(1));


                //BTELMAsyncTask tarefa  =new BTELMAsyncTask(getActivity());
                //tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });
        */



        /*
        Button button = (Button) v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {
                //MainActivity.toast("botão pressionado");
                updateDetail("teste");

            }
        });
        */

        /*
        //encontrar botão para iniciar randomgenerator
        Button mButton = (Button) v.findViewById(R.id.buttonIniciar);
        //criar listener
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vb) {
            //MainActivity.toast("botao iniciar random");
            page1IniciarRandom();

            }
        });
        */


        /*
        Button botao = (Button) v.findViewById(R.id.button_reset_1);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {


                //envia pedido ao Main para igualar parciais ao odo

                MyBus.getInstance().post(new Page1TaskResultEvent(3001));

            }
        });

        */


        return       v;


    }

    // triggers update of the details fragment
    /*
    public void updateDetail(String uri) {
        // create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // send data to activity
        listener.actualizarTexto(newTime);
        //MainActivity.toast("updateDetail completo");
    }
    */

    /*

    // triggers update of the details fragment
    public void page1IniciarRandom() {

        //MainActivity.toast("page1iniciar");
        // send data to activity
        listener.onBotaoRandom();

        //MainActivity.toast("updateDetail completo");
    }

    */


    //comentário

    @Override
    public void onResume() {
        super.onResume();
        //MainActivity actividademain1 = (MainActivity)getActivity();
        actpag1(MainActivity.valoresmemorizados);


        TextView view = (TextView) getView().findViewById(R.id.swdp);
        view.setText("swdp:" + MainActivity.smallestwidthdp);

        if (MainActivity.debugModeMain) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);

        view = (TextView) getView().findViewById(R.id.passo);
        if (MainActivity.debugModeMain) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);

        view = (TextView) getView().findViewById(R.id.type);
        if (MainActivity.debugModeMain) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);

        //se modo tablet esconde algumas coisas
        if (MainActivity.TABLET ^ MainActivity.reverseModeMain) {
            view = (TextView) getView().findViewById(R.id.socx475_1);
            view.setVisibility(View.INVISIBLE);

            ProgressBar progresso = (ProgressBar) getView().findViewById(R.id.pb_soc_1);
            progresso.setVisibility(ProgressBar.INVISIBLE);


        }

        handler.post(timedTask);




    }

    @Override public void onDestroy() {
        //MyBus.getInstance().unregister(this);
        super.onDestroy();
    }



    public void setPasso(String url) {
        TextView view = (TextView) getView().findViewById(R.id.passo);
        view.setText(url);

    }


    private Handler handler = new Handler();

    private int cnt = 0;

    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {

            cnt=cnt+rotafan;
            if (cnt>89) cnt = 0;


            ImageView view2 = (ImageView) getView().findViewById(R.id.ventaxrad);
            view2.setRotation(cnt*4);


            handler.postDelayed(timedTask, 30);
        }};



    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(timedTask);
    }




    public void actpag1(int[] array1) {
        int invalido = Integer.MAX_VALUE;

        TextView view;
        ImageView view2;


        /*
        Animation rodar = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rodar.setInterpolator(new LinearInterpolator());
        */

        if (array1[0]!= invalido) {
            view = (TextView) getView().findViewById(R.id.socx475_1);
            double temp = ((double) array1[0]) / 100.0;
            view.setText(String.format("%3.2f", temp) + "%");


            ProgressBar pb = (ProgressBar) getView().findViewById(R.id.pb_soc_1);
            if (temp >100.0) temp = 100.0;
            pb.setProgress((int) temp);






        }

        /*
        if (array1[7]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.motorfanspeed_1);
            double temp = ((double) array1[7]) / 2.0;
            view.setText("Pack Voltage:"+ String.format("%3.1f", temp) + "V");
        }
        */

        if (array1[14]!= invalido) {
            view = (TextView) getView().findViewById(R.id.battery12v_1);
            double temp = ((double) array1[14]) / 1000.0;
            view.setText(String.format("%2.2f", temp) + "V");


            if (array1[14] <= 11700) view.setTextColor(getResources().getColor(R.color.laranja));
            if (array1[14] <= 10500) view.setTextColor(Color.RED);
            else {
                if (MainActivity.noite) view.setTextColor(Color.WHITE);
                else view.setTextColor(Color.BLACK);
            }

        }


        if (array1[86]!= invalido) {
            view2 = (ImageView) getView().findViewById(R.id.ventaxrad);

            rotafan = array1[86];

            //double temp = ((double) array1[14]) / 1000.0;
            //view.setText(String.format("%2.2f", temp) + "V");


            //view = (TextView) getView().findViewById(R.id.textView10);
            //view.setText(array1[86] + "S" );

            /*
            if (array1[86]!=0) {
                view2.setVisibility(View.VISIBLE);
                if (!rodar.isInitialized()) {
                    rodar.setRepeatCount(-1);
                    view2.startAnimation(rodar);
                }
                rodar.setDuration(1000 / (array1[86] + 1));
            }
            else {
                rodar.setRepeatCount(1);
                rodar.cancel();
                rodar.reset();
                view.setText("Parou");
                view2.setVisibility(View.INVISIBLE);
            }
            */

            if (array1[86]==0) view2.setVisibility(View.INVISIBLE);
            else view2.setVisibility(View.VISIBLE);

            //view2.setPivotX(view2.getWidth() / 2);
            //view2.setPivotY(view2.getHeight() / 2);


            //view2.setRotation(((array1[86]*(System.currentTimeMillis() / 10)))%360  );

        }









        if (array1[28]!= invalido) {
            view = (TextView) getView().findViewById(R.id.hvcons);
            double temp = ((double) array1[28]) / 100.0;
            view.setText(String.format("%1.1f", temp) + "kW");

            ImageView image = (ImageView) getView().findViewById(R.id.aux_hv);

            //se o valor for zero ou o contactor principal não estiver ligado
            if (temp==0.0 || array1[27]!=2 ) {
                view.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            }
            else {
                view.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
            }
        }


        //autonomia
        view = (TextView) getView().findViewById(R.id.range_1);

        if (array1[33]!= invalido && array1[33]>9) {
            //double temp = ((double) array1[14]) / 1000.0;
            view.setText(array1[33]+ "km");
        }
        else view.setText("- - -");

        //until charge complete
        if (array1[34]!= invalido) {
            view = (TextView) getView().findViewById(R.id.timetofull_1);
            ImageView image = (ImageView) getView().findViewById(R.id.timetofullicon_1);
            //double temp = ((double) array1[14]) / 1000.0;
            if (array1[34]!=1023) {
                view.setText(array1[34]/60 + ":" + String.format("%02d",array1[34]%60 )   );
                view.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
            }
            else {
                view.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            }

        }

        //evse current
        if (array1[6]!= invalido) {
            view = (TextView) getView().findViewById(R.id.evse_1);
            if (array1[6]!=0 && array1[6]<48 && array1[5]!=0 && array1[5] != invalido && !(MainActivity.TABLET ^ MainActivity.reverseModeMain ) ) {
                view.setVisibility(View.VISIBLE);
                view.setText(array1[6] + "A");
            }
            else view.setVisibility(View.INVISIBLE);

        }


        //cable plugged
        if (array1[5]!= invalido) {
            view2 = (ImageView) getView().findViewById(R.id.carrocharging);
            if (array1[5]!=0) view2.setVisibility(View.VISIBLE);
            else view2.setVisibility(View.INVISIBLE);

        }

        /*

        if (array1[42]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.daynight1_1);

            if (array1[42]==0) view.setText("Day");
            else view.setText("Night");



        }
        */


        /*

        if (array1[48]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.temp1_1);
            double temp = (((double) array1[48]) /10.0) -40.0;
            view.setText("Evap SP:"+String.format("%3.1f", temp) + "C");
        }


        if (array1[49]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.temp2_1);
            double temp = ((double) array1[49]) / 1.0;
            view.setText("Water SP:"+String.format("%4.0f", temp) + "C");
        }

        if (array1[50]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.temp3_1);
            double temp = ((double) array1[50]) / 10.0 - 40.0 ;
            //double temp2 = ((double) array1[52]) / 2.5 - 40.0;
            view.setText("Evap:"+String.format("%3.1f", temp) + "C");
        }


        if (array1[51]!= invalido && array1[53]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.temp4_1);
            //water temp
            double temp = ((double) array1[51]) - 40.0;
            //internal temp
            double temp2 = ((double) array1[53]) /2.5 - 40.0;
            //internal humidity
            double temp3 = ((double) array1[54]) /2.0;

            double temp4 = ((double) array1[46]) /1.0;

            double temp5 = ((double) array1[47]) /1.0;

            view.setText("Water:"+String.format("%3.0f", temp) + "C / "+String.format("%3.1f", temp2)+"C / "+String.format("%3.1f", temp3)+"% /"+String.format("%4.0f", temp4)+" /"+String.format("%4.0f", temp5));
        }



        if (array1[55]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.temp5_1);
            //motor water temp
            double temp = ((double) array1[55]);
            //charger water temp
            double temp2 = ((double) array1[56]);

            double temp3 = ((double) array1[57]);

            //double temp4 = ((double) array1[46]) /1.0;

            //double temp5 = ((double) array1[47]) /1.0;

            view.setText("MWT:"+String.format("%3.0f", temp) + " CWT:"+String.format("%3.0f", temp2)+" HWP:"+String.format("%3.0f", temp3)/*+" 23"+String.format("%4.0f", temp4)+" /"+String.format("%4.0f", temp5));
        }
        */



        //temperatura do conversor DCDC ??
        if (array1[60]!= invalido && array1[70]!= invalido) {
            view = (TextView) getView().findViewById(R.id.dcdctemp_1);
            if (array1[27]==2) {
                view.setVisibility(View.VISIBLE);
                double temp = ((double) array1[60]) /64.0;
                //double temp2 = ((double) array1[70]) /256.0;
                view.setText(String.format("%3.0f", temp)+"C");


                if (array1[27] >= 3200) view.setTextColor(getResources().getColor(R.color.laranja));
                if (array1[27] >= 3520) view.setTextColor(Color.RED);
                else {
                    if (MainActivity.noite) view.setTextColor(Color.WHITE);
                    else view.setTextColor(Color.BLACK);
                }




            }
            else view.setVisibility(View.INVISIBLE);

        }

        //temperatura da bateria
        if (array1[40]!= invalido) {

            view = (TextView) getView().findViewById(R.id.battemp_1);

            double tempt = array1[40] / 10.0;
            view.setText(String.format("%2.0f", tempt) + "C");

            if (array1[40] >= 270) view.setTextColor(getResources().getColor(R.color.laranja));
            if (array1[40] >= 280) view.setTextColor(Color.RED);
            else {
                if (MainActivity.noite) view.setTextColor(Color.WHITE);
                else view.setTextColor(Color.BLACK);
            }


        }


        //temperatura do inversor
        if (array1[61]!= invalido && array1[77]!= invalido) {
            view = (TextView) getView().findViewById(R.id.invtemp_1);
            if (array1[77]==1) {
                view.setVisibility(View.VISIBLE);
                double temp = ((double) array1[61]) /64.0;
                view.setText(String.format("%3.0f", temp)+"C");


                if (array1[77] >= 3200) view.setTextColor(getResources().getColor(R.color.laranja));
                if (array1[77] >= 3520) view.setTextColor(Color.RED);
                else {
                    if (MainActivity.noite) view.setTextColor(Color.WHITE);
                    else view.setTextColor(Color.BLACK);
                }



            }
            else view.setVisibility(View.INVISIBLE);

        }






        //sunshine
        if (array1[46]!= invalido && array1[47]!= invalido) {

            view = (TextView) getView().findViewById(R.id.sunshine_1);

            if (array1[46]>0 && array1[47]>0) view.setText("Sun:"+(array1[46]+ array1[47]));
            else view.setText("");
        }




        //evaporador, temperatura e humidade interna
        if (array1[53]!= invalido && array1[54]!= invalido && array1[52]!= invalido && array1[51]!=invalido) {

            view = (TextView) getView().findViewById(R.id.tempinside_1);

            //double evap = array1[52] /2.5 - 40.0;
            double tempt = array1[53] /2.5 - 40.0;
            double hum = array1[54] / 2.0;
            //double heatw= array1[51] - 40.0;
            view.setText(/*"Cold:"+String.format("%2.0f", evap) + "C\nIntake:"+*/String.format("%2.0f", tempt)+"C/"+ String.format("%2.0f", hum) + "%RH");
        }



        //temperatura externa
        if (array1[65]!= invalido) {

            view = (TextView) getView().findViewById(R.id.tempoutside_1);

            double tempt = array1[65] - 40.0;
            view.setText(String.format("%2.0f", tempt) + "C");
        }



        // corrente conversor DCDC
        if ( (array1[84]!= invalido) && (array1[14]!= invalido) ) {

            view = (TextView) getView().findViewById(R.id.Dcc);

            double tempt;

            if (array1[27]==2) tempt = ((double) array1[84])/64.0;
            else tempt = 0.0;

            view.setText(String.format("%3.1f", tempt) + "A");

            view = (TextView) getView().findViewById(R.id.dcdcw);

            double tempt2 = tempt * (((double) array1[14])/1000.0);

            view.setText(String.format("%4.0f", tempt2) + "W");


            if (tempt2 >= 1000.0) view.setTextColor(getResources().getColor(R.color.laranja));
            if (tempt2 >= 1500.0) view.setTextColor(Color.RED);
            else {
                if (MainActivity.noite) view.setTextColor(Color.WHITE);
                else view.setTextColor(Color.BLACK);
            }



        }

        //carga conversor DCDC

        if ( (array1[85]!= invalido)  ) {

            view = (TextView) getView().findViewById(R.id.dcdcp );

            if (array1[85]!=255) {

                double tempt = ((double) array1[85]) / 255.0 * 100.0;
                view.setText(String.format("%3.1f", tempt) + "%");
            }
            else {
                view.setText("");
            }

        }



        //partial energy spent
        /*
        if (array1[59]!= invalido) {

            TextView view = (TextView) getView().findViewById(R.id.totspent_1);

            double tempt = array1[59] / 1000.0;
            view.setText(String.format("%6.2f", tempt) + "kWh");
        }
        */


        //total km
        if (array1[66]!= invalido) {

            view = (TextView) getView().findViewById(R.id.odokm_1);

            double tempt = array1[66] / 100.0;
            view.setText(String.format("%6.0f", tempt)/* + "km"*/);
        }


        //VIN
        if (array1[79]!= invalido) {

            view = (TextView) getView().findViewById(R.id.vin_1);

            char a = (char) (array1[79] & 255);
            char b = (char) (array1[79] >> 8 & 255);
            char c = (char) (array1[79] >> 16 & 255);
            char d = (char) (array1[79] >> 24 & 255);

            //double tempt = array1[66] / 100.0;
            view.setText("\u2026"+d+c+b+a);
        }



        //se estiver em GO mostra a potência do motor
        if (array1[67]!= invalido) {

            view = (TextView) getView().findViewById(R.id.motoramp_1);

            if (array1[77]==1) {
                view.setVisibility(View.VISIBLE);
                double temp = (array1[67] / 32.0) / 1.44;
                view.setText(String.format("%3.1f", temp) + "kW");
            }
            else view.setVisibility(View.INVISIBLE);



        }

        /*
        //teste wiper
        if (array1[68]!= invalido) {

            TextView view = (TextView) getView().findViewById(R.id.testew_1);

            double tempt = array1[68] ;
            view.setText(String.format("%6.0f", tempt) + "km");
        }
        */

        /*
        if (array1[71]!= invalido && array1[73]!=invalido && array1[74]!=invalido) {

            view = (TextView) getView().findViewById(R.id.testa_1);
            double temp = array1[71] / 256.0;
            double temp3 = array1[73] / 1.0;
            double temp4 = array1[74] / 1.0;
            view.setText(String.format("%3.0f", temp) + "% / "+String.format("%6.0f", temp3) + " / "+String.format("%6.0f", temp4));

        }
        */


        /*
        //total kwh
        if (array1[59]!= invalido) {

            view = (TextView) getView().findViewById(R.id.testb_1);

                double temp = ((double) array1[59]) / 1000.0;
                view.setText("Tot:"+ String.format("%4.3f", temp) + " kWh");




        }
        */

        /*

        double parkwh = 0.0;
        double parkm = 0.0;
        double media = 0.0;

        //parcial kwh = total - ponto 0
        if (array1[199]!=invalido && array1[59]!= invalido) {
            view = (TextView) getView().findViewById(R.id.pkwh_1);

            parkwh = (array1[59] / 1000.0) - (array1[199] / 1000.0);

            //se der negativo algo correu mal ou o contador deu a volta, faz reset
            if (parkwh<0.0) MyBus.getInstance().post(new Page1TaskResultEvent(3001));

            view.setText(String.format("%3.2f", parkwh) + "kWh");
        }


        //parcial km = total - ponto 0
        if (array1[198]!=invalido && array1[66]!= invalido) {
            view = (TextView) getView().findViewById(R.id.pkm_1);

            parkm = (array1[66] / 100.0) - (array1[198] / 100.0);
            view.setText(String.format("%3.1f", parkm) + "km");
        }


        //media
        if (parkm >0.0) {
            media = parkwh / (parkm / 100.0);
            if (media > 99.99) media = 99.99;
        }

        view = (TextView) getView().findViewById(R.id.pmed_1);
        view.setText(String.format("%2.2f", media));

        */



    }
}







