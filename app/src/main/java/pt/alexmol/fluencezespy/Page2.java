package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



public class Page2 extends Fragment {
    // --Commented out by Inspection (05-11-2015 14:55):Context c;


    private static int largura = -123;

    private boolean hsm_mode = false;

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


        botaoreset1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vb) {


                //Informa que se tem que fazer long press

                MyBus.getInstance().post(new Page2TaskResultEvent(3004));


                return;

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


        botaoreset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vb) {


                //Informa que se tem que fazer long press

                MyBus.getInstance().post(new Page2TaskResultEvent(3004));


                return;

            }
        });



        /*
        Button buttonhsm = (Button) v.findViewById(R.id.button_hsm);

        buttonhsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vb) {

                //se estiver desligado ligar
                if ( !hsm_mode && (MainActivity.ELMREADY ==2)) {
                    MyBus.getInstance().post(new Page2TaskResultEvent(3010));
                    hsm_mode = true;
                }
                //se estiver ligado desligar
                else {
                    MyBus.getInstance().post(new Page2TaskResultEvent(3011));
                    hsm_mode = false;
                }


            }
        });
        */



        ImageButton buttonrange = (ImageButton) v.findViewById(R.id.botao_range_2);

        buttonrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View va) {


                MyBus.getInstance().post(new Page2TaskResultEvent(3003));




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


        scaledone = false;
        largura = -123;

        if  (MainActivity.TABLET ^ MainActivity.reverseModeMain) visivel = true;
        //else visivel = false;

        handler.post(timedTask);

        final FrameLayout myLayout;

        myLayout = (FrameLayout) getView().findViewById(R.id.contemprog2);
        myLayout.setVisibility(View.INVISIBLE);
        myLayout.post(new Runnable()
        {

            @Override
            public void run()
            {
                //Log.i("TEST", "Layout width : "+ myLayout.getWidth());
                largura = myLayout.getWidth();
                if (myLayout.getHeight()<largura) largura =myLayout.getHeight();
                //largura = 200;
            }
        });


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

        //199 = ponto 0
        //106 = parcial perpetuo

        //parcial kwh = total - ponto 0
        if (array2[199]!=invalido && array2[59]!= invalido && !MainActivity.AltTripModeMain) {
            view = (TextView) getView().findViewById(R.id.t1_kwh);

            parkwh1 = (array2[59] / 1000.0) - (array2[199] / 1000.0);

            //float densidade = MainActivity.densidade;

            //se der muito negativo algo correu mal ou o contador deu a volta, faz reset
            if (parkwh1<-20.0) MyBus.getInstance().post(new Page2TaskResultEvent(3001));

            view.setText(String.format("%3.2f", parkwh1));
        }

        //199 = ponto 0
        //106 = parcial resetable

        //o 106 vai chegar com valores inferiores ao actual, quando for feito o reset
        //nesses casos é porque o utilizador fez o reset no dash

        if (array2[199]!=invalido && array2[106]!= invalido && MainActivity.AltTripModeMain) {
            view = (TextView) getView().findViewById(R.id.t1_kwh);

            parkwh1 = (array2[106] / 1000.0) - (array2[199] / 1000.0);

            //float densidade = MainActivity.densidade;

            //se der negativo algo correu mal ou o contador deu a volta ou o utilizador fez reset, faz reset
            if (parkwh1<0.0) MyBus.getInstance().post(new Page2TaskResultEvent(3001));

            view.setText(String.format("%3.2f", parkwh1));
        }





        //parcial km = total - ponto 0
        if (array2[198]!=invalido && array2[66]!= invalido ) {
            view = (TextView) getView().findViewById(R.id.t1_km);

            parkm1 = (array2[66] / 100.0) - (array2[198] / 100.0);
            view.setText(String.format("%3.1f", parkm1));
        }


        //media
        if (parkm1 >0.0) {
            media1 = parkwh1 / (parkm1 / 100.0);
            if (media1 > 99.99) media1 = 99.99;
        }
        else media1 = 99.99;

        //float densidade = MainActivity.densidade;

        view = (TextView) getView().findViewById(R.id.t1_avg);
        view.setText(String.format("%2.2f", media1));




        //Trip 2
        double parkwh2 = 0.0;
        double parkm2 = 0.0;
        double media2 = 0.0;

        //parcial kwh = total - ponto 0
        if (array2[197]!=invalido && array2[59]!= invalido && !MainActivity.AltTripModeMain) {
            view = (TextView) getView().findViewById(R.id.t2_kwh);

            parkwh2 = (array2[59] / 1000.0) - (array2[197] / 1000.0);

            //se der muito negativo algo correu mal ou o contador deu a volta, faz reset
            if (parkwh2<-20.0) MyBus.getInstance().post(new Page2TaskResultEvent(3002));

            view.setText(String.format("%3.2f", parkwh2));
        }

        if (array2[197]!=invalido && array2[106]!= invalido && MainActivity.AltTripModeMain) {
            view = (TextView) getView().findViewById(R.id.t2_kwh);

            parkwh2 = (array2[106] / 1000.0) - (array2[197] / 1000.0);

            //se der negativo algo correu mal ou o contador deu a volta ou o utilizador fez reset, faz reset
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
        else media2 = 99.99;

        view = (TextView) getView().findViewById(R.id.t2_avg);
        view.setText(String.format("%2.2f", media2));


        //velocidade
        if (array2[78]!=invalido ) {
            // cheg x100, divide-se por 10 para ficar x10
            velocidade = array2[78]/10;

        }


        //potencia
        if (array2[67]!=invalido ) {
            // x10

            //double temp = (array2[67] / 32.0) / 1.44; //em kW
            double temp = (array2[67] / 16.0) *  (array2[99]/32.0) / 1000.0 * 1.0;

            potenciaactual = (int) (temp * 10.0);
            if (potenciaactual>800) potenciaactual = 800;
            if (potenciaactual<-400) potenciaactual = -400;

        }


        //soc
        if (array2[0]!= invalido) {
            view = (TextView) getView().findViewById(R.id.soc_2);
            double temp = ((double) array2[0]) / 100.0;
            view.setText(String.format("%3.2f", temp) + "%");

            //verde acima dos 35%
            //amarelo entre 20 e 35%
            //vermelho abaixo dos 20%


            ProgressBar pb3 = (ProgressBar) getView().findViewById(R.id.yourId3);
            if (temp >100.0) temp = 100.0;
            pb3.setProgress((int) temp);
            //pb3.setProgress(100);

        }


        //autonomia
        view = (TextView) getView().findViewById(R.id.range_2);
        TextView t1 = (TextView) getView().findViewById(R.id.t1l_2);
        TextView t2 = (TextView) getView().findViewById(R.id.t2l_2);

        /*
        if (array2[33]!= invalido && array2[33]>9) {
            //double temp = ((double) array1[14]) / 1000.0;
            view.setText(array2[33] + "km");
        }
        else view.setText("- - -");
        */

        //autonomia nova
        if (array2[16]!=invalido && array2[15]!=invalido & array2[33]!=invalido) {
            // 22kWH com SOH 100%, menos 1kWh
            // ((AH / 59.09) * 22 kWh )- 1kWh
            double total = ( array2[16] / 10000.0 / 59.09 * 22500 ) -1000 ; //total em Wh
            // considerar o Real SOC com máximo de 95%
            double restante = array2[15] / 1000000.0 / 0.95 * total;  //restante em Wh

            //se for para usar a autonomia do carro
            if (array2[195]==0) {
                if (array2[33]!= invalido && array2[33]>9) {
                    view.setText(array2[33] + "km");
                }
                else view.setText("- - -");

                view.setTextColor(Color.BLACK);
                if (!MainActivity.noite) {
                    t1.setTextColor(Color.BLACK);
                    t2.setTextColor(Color.BLACK);
                }
                else {
                    t1.setTextColor(Color.WHITE);
                    t2.setTextColor(Color.WHITE);
                }
            }

            //se for para usar o Trip 1
            if (array2[195]==1) {
                double autonomia = restante / media1 / 10.0;
                view.setText((int) autonomia + "km");
                view.setTextColor(Color.BLUE);
                if (!MainActivity.noite) {
                    t1.setTextColor(Color.BLUE);
                    t2.setTextColor(Color.BLACK);
                }
                else {
                    t1.setTextColor(Color.BLUE);
                    t2.setTextColor(Color.WHITE);
                }
            }


            //se for para usar o Trip 2
            if (array2[195]==2) {
                double autonomia = restante / media2 / 10.0;
                view.setText((int) autonomia + "km");
                view.setTextColor(Color.BLUE);
                if (!MainActivity.noite) {
                    t2.setTextColor(Color.BLUE);
                    t1.setTextColor(Color.BLACK);
                }
                else {
                    t2.setTextColor(Color.BLUE);
                    t1.setTextColor(Color.WHITE);
                }
            }


            /*

            //se nenhum dos parciais tiver mais que 1km então usa valor do carro
            if ( parkm1 < 1.0 && parkm2 < 1.0) {
                view.setText(array2[33] + "km");
            }
            else {
                double autonomia;
                //se o t1 for inferior ao t2
                if (parkm1 < parkm2) autonomia = restante / media1 / 10.0;
                //caso contrário
                else autonomia = restante / media2 / 10.0;
                view.setText((int) autonomia + "km");
            }
            */
        }

        //ponteiro regen

        if (array2[10]!=invalido) {

            ImageView ponteireg = (ImageView) getView().findViewById(R.id.ponteiro_regen2);

            ponteireg.setPivotX(ponteireg.getWidth() / 2);
            ponteireg.setPivotY(ponteireg.getHeight());



            //obter max regen
            int potmain = array2[10];

            if ( potmain<4000 && potmain >=0 )potregen = potmain; //x100kW
            potregen = potregen / 10;    //passa à gama -400 a 0
            potregen = potregen * -1;

            //potencia
            //recebe valor entre -400 e 0
            //deverá sair um valor entre -250 e 0

            //potencias entre -5,0 e 0 é linear
            if (potregen>=-50 && potregen <0) cntregen = (potregen * 125) / 100;

            //potencias entre -40,0 e -5,0 escala logaritmica
            if (potregen <-50) {
                double temp = potregen * -0.1;
                temp = temp / 5.0;
                temp = Math.log(temp) / Math.log(2.0);
                temp = temp + 1.0;
                temp = temp * -62.5;
                cntregen = (int) temp;
            }

            //cntregen = (potmain /50 );

            ponteireg.setRotation(cntregen / 250f * 89.5f);



        }

        //temperatura da bateria


        if (array2[40]!= invalido) {

            view = (TextView) getView().findViewById(R.id.tempbat_2);

            double tempt = array2[40] / 10.0;
            view.setText(String.format("%2.1f", tempt) + "C");

            if (array2[40] >= 270) view.setTextColor(getResources().getColor(R.color.laranja));
            if (array2[40] >= 280) view.setTextColor(Color.RED);
            else {
                if (MainActivity.noite) view.setTextColor(Color.WHITE);
                else view.setTextColor(Color.BLACK);
            }

        }



        //total kwh

        if (array2[59]!= invalido) {

            view = (TextView) getView().findViewById(R.id.totaltrip2);

            double tempt = array2[59] / 1000.0;
            view.setText(String.format("%6.3f", tempt) );

        }


        //partial kwh

        if (array2[106]!= invalido) {

            view = (TextView) getView().findViewById(R.id.partrip2);

            double tempt = array2[106] / 1000.0;
            view.setText(String.format("%6.2f", tempt) );

        }





        //temperatura exterior

        //temperatura externa
        if (array2[65]!= invalido) {

            view = (TextView) getView().findViewById(R.id.tempext_2);

            double tempt = array2[65] - 40.0;
            view.setText(String.format("%2.0f", tempt) + "C");

            if (tempt < -39.0 || tempt > 213) view.setVisibility(View.INVISIBLE);
            else view.setVisibility(View.VISIBLE);

        }



        //consumo auxiliar HV



        if (array2[84]!= invalido && array2[103]!=invalido && array2[105]!=invalido) {
            view = (TextView) getView().findViewById(R.id.aux_cons_2);
            ProgressBar pb5 = (ProgressBar) getView().findViewById(R.id.progress_aux_2);


            double tempt;

            if (array2[27]==2) tempt = ((double) array2[84])/64.0;
            else tempt = 0.0;
            double dcdc = tempt * (((double) array2[14])/1000.0) * 1.10 /1000.0; //dcdc
            double clim = (((double) array2[103])*0.025);  // climate power
            double temp3;

            if (clim>0 && clim <6.350) temp3 =dcdc + clim;
            else temp3 = dcdc;

            view.setText(String.format("%1.1f", temp3) + "kW");



            double temp = temp3 * 100 / 6.0;  //converte 0 a 100%
            if (temp >100.0) temp = 100.0;  //limita 100%
            pb5.setProgress((int) temp);


            //se o valor for zero ou o contactor principal não estiver ligado
            if (temp3<= 0.0 || array2[27]!=2 ) {
                view.setVisibility(View.INVISIBLE);
                pb5.setVisibility(View.INVISIBLE);
            }
            else {
                view.setVisibility(View.VISIBLE);
                pb5.setVisibility(View.VISIBLE);
            }


        }


        /*

        if (array2[28]!= invalido && array2[27]==2) {
            view = (TextView) getView().findViewById(R.id.aux_cons_2);
            view.setVisibility(View.VISIBLE);
            double temp = ((double) array2[28]) / 100.0;
            view.setText(String.format("%1.1f", temp) + "kW");



        }
        else {
            view = (TextView) getView().findViewById(R.id.aux_cons_2);
            view.setVisibility(View.INVISIBLE);
            ProgressBar pb5 = (ProgressBar) getView().findViewById(R.id.progress_aux_2);
            pb5.setVisibility(View.INVISIBLE);
        }

        */

        //consumo instantaneo
        if (array2[67]!=invalido && array2[78]!=invalido ) {
            // potencia motor
            //double temppotmot = (array2[67] / 32.0) / 1.44; //em kW

            double temppotmot = (array2[67] / 16.0) *  (array2[99]/32.0) / 1000.0 * 1.0;

            //potencia auxiliar
            double temppotaux = 0.0;
            if (array2[28]!= invalido && array2[27]==2) temppotaux = ((double) array2[28]) / 100.0; //em kW

            // velocidade
            double tempvel = (array2[78] / 100.0); //em km/h

            double instant = (temppotmot+temppotaux)/tempvel*100.0;
            if (instant >99.9) instant = 99.9;
            if (instant <-99.9) instant = -99.9;
            if (temppotmot==0.0 && temppotaux==0.0) instant = 0.0;

            view = (TextView) getView().findViewById(R.id.inst_2);
            view.setVisibility(View.VISIBLE);
            view.setText(String.format("%2.1f", instant));

        }
        else {
            view = (TextView) getView().findViewById(R.id.inst_2);
            view.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(timedTask);
    }

    @Override public void onDestroy() {
        //MyBus.getInstance().unregister(this);
        super.onDestroy();

    }

    private int potenciaactual = 0;
    private int cntpedido = 0;
    //private int cntsuave = 0;
    private int potregen = 0;
    private int cntregen = 0;

    private int cnt = 0;
    private int velocidade = 0;
    private int velocanterior = 0;
    private int incre = 1;

    private Handler handler = new Handler();

    private boolean scaledone = false;

    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {



            //velocidade
            //se a velocidade actual for superior à anterior sobe 0.5, se for inferior desce 0.5
            if (velocidade > velocanterior ) {
                velocanterior=velocanterior+5;
                TextView textoveloc = (TextView) getView().findViewById(R.id.vel_2);
                textoveloc.setText(""+velocanterior/10);
            }
            if (velocidade < velocanterior ) {
                velocanterior=velocanterior-5;
                TextView textoveloc = (TextView) getView().findViewById(R.id.vel_2);
                textoveloc.setText(""+velocanterior/10);
            }




            if (MainActivity.debugModeMain) {

                //testes

                cnt = cnt + incre;
                if (cnt >= 250) incre = -1;
                if (cnt <= -250) incre = 1;

            }
            else

            {




            //potencia
            //recebe valor entre -400 e 800
            //deverá sair um valor entre -250 e 250

            //potencias entre 0 e 5,0 é linear
            if (potenciaactual>=0 && potenciaactual <=50) cntpedido = potenciaactual;

            //potencias entre -5,0 e 0 é linear
            if (potenciaactual>=-50 && potenciaactual <0) cntpedido = (potenciaactual * 125) / 100;

            //potencias entre 5,0 e 80,0 escala logaritmica
            if (potenciaactual >50) {
                double temp = potenciaactual * 0.1;
                temp = temp/5.0;
                temp = Math.log(temp) / Math.log(2.0);
                temp = temp + 1.0;
                temp = temp * 50.0;
                cntpedido = (int) temp;
            }

            //potencias entre -40,0 e -5,0 escala logaritmica
            if (potenciaactual <-50) {
                double temp = potenciaactual * -0.1;
                temp = temp / 5.0;
                temp = Math.log(temp) / Math.log(2.0);
                temp = temp + 1.0;
                temp = temp * -62.5;
                cntpedido = (int) temp;
            }

            //suavização
            //se diferença for inferior a 50 vai de 1 em 1
            if ( Math.abs(cnt-cntpedido) <=50) {
                if (cntpedido>cnt) cnt++;
                if (cntpedido<cnt) cnt--;
            }
            //se diferença for entre 100 e 200 vai de 5 em 5
            if ( Math.abs(cnt-cntpedido) >50) {
                if (cntpedido>cnt) cnt=cnt+5;
                if (cntpedido<cnt) cnt=cnt-5;
            }
            //se diferença for superior a 150 vai de 10 em 10
            if ( Math.abs(cnt-cntpedido) >150) {
                if (cntpedido>cnt) cnt=cnt+10;
                if (cntpedido<cnt) cnt=cnt-10;
            }




            }

            //first draw
            if (largura !=-123 && !scaledone) {

                float factor = MainActivity.densidade/160f;

                ProgressBar pb = (ProgressBar) getView().findViewById(R.id.anel_power);

                pb.setScaleX(largura/200f);
                pb.setScaleY(largura / 200f);
                pb.setTranslationY((largura-200f)/2f);
                //pb.setRotation((-cnt / 1000f * 360f) - 90f);
                pb.setProgress(0);


                ProgressBar pb2 = (ProgressBar) getView().findViewById(R.id.anel_regen);
                //pb2.setRotation((-cnt / 1000f * 360f) - 0f);
                //pb2.setTranslationX(-100);
                //pb2.setTranslationY(cnt/5);

                pb2.setScaleX(largura / 200f);
                pb2.setScaleY(largura / 200f);
                pb2.setTranslationY((largura - 200f) / 2f);
                pb2.setProgress(0);

                if (MainActivity.SimplePointerModeMain) pb2.setVisibility(View.INVISIBLE);
                else pb2.setVisibility(View.VISIBLE);

                //} catch (Exception e) {

                //}

                ImageView pontei = (ImageView) getView().findViewById(R.id.ponteiro_2);
                pontei.setTranslationY((largura - 200f) / 2f);
                pontei.setScaleX(largura / 200f);
                pontei.setScaleY(largura / 200f);

                pontei.setPivotX(pontei.getWidth() / 2);
                pontei.setPivotY(pontei.getHeight());
                pontei.setRotation(0);


                ImageView ponteireg = (ImageView) getView().findViewById(R.id.ponteiro_regen2);
                ponteireg.setTranslationY((largura - 200f) / 2f);
                ponteireg.setScaleX(largura / 200f);
                ponteireg.setScaleY(largura / 200f);

                ponteireg.setPivotX(ponteireg.getWidth() / 2);
                ponteireg.setPivotY(ponteireg.getHeight());



                //obter max regen
                int potmain = MainActivity.valoresmemorizados[10];


                if ( potmain<4000 && potmain >=0 )potregen = potmain; //x100kW
                potregen = potregen / 10;    //passa à gama -400 a 0
                potregen = potregen * -1;

                //potencia
                //recebe valor entre -400 e 0
                //deverá sair um valor entre -250 e 0

                //potencias entre -5,0 e 0 é linear
                if (potregen>=-50 && potregen <0) cntregen = (potregen * 125) / 100;

                //potencias entre -40,0 e -5,0 escala logaritmica
                if (potregen <-50) {
                    double temp = potregen * -0.1;
                    temp = temp / 5.0;
                    temp = Math.log(temp) / Math.log(2.0);
                    temp = temp + 1.0;
                    temp = temp * -62.5;
                    cntregen = (int) temp;
                }

                //cntregen = (potmain /50 );

                ponteireg.setRotation(cntregen / 250f * 89.5f);


                ImageView semi = (ImageView) getView().findViewById(R.id.semicirculo_2);
                semi.setScaleX(largura / 200f);
                semi.setScaleY(largura / 200f);
                semi.setTranslationY((largura - 200f) / 2f);
                semi.getBackground().setLevel(5000);


                ImageView dash = (ImageView) getView().findViewById(R.id.dash_2);
                dash.setScaleX(largura / 200f);
                dash.setScaleY(largura / 200f);
                dash.setTranslationY((largura - 200f) / 2f);



                ImageView batt = (ImageView) getView().findViewById(R.id.imgbatt_2);
                batt.setScaleX(largura / 400f);
                batt.setScaleY(largura / 400f);

                batt.setTranslationY(/*((largura / 400f) * 5f) +*/ ((largura - 400f) / 11.111111111111111111f));
                batt.setTranslationX(1.0f * (((largura / 400f) * 160f) /*+ ((largura - 200f) / 11.11111111111f)*/));



                ImageView weather = (ImageView) getView().findViewById(R.id.imgout_2);
                weather.setScaleX(largura / 400f);
                weather.setScaleY(largura / 400f);

                weather.setTranslationY(/*((largura / 400f) * 10f) +*/ ((largura - 400f) / 11.42857f));
                weather.setTranslationX(-1.0f * (((largura / 400f) * 158f) /*+ ((largura - 200f) / 11.11111111111f)*/));


                TextView tempbat = (TextView) getView().findViewById(R.id.tempbat_2);
                tempbat.setScaleX(largura / 200f);
                tempbat.setScaleY(largura / 200f);


                tempbat.setTranslationY(((largura / 200f) * 5f) + ((largura - 200f) / 20.0f));
                tempbat.setTranslationX(1.0f * (((largura / 200f) * 78f) /*+ ((largura - 200f) / 11.11111111111f)*/));


                TextView tempext = (TextView) getView().findViewById(R.id.tempext_2);
                tempext.setScaleX(largura / 200f);
                tempext.setScaleY(largura / 200f);


                tempext.setTranslationY(((largura / 200f) * 5f) + ((largura - 200f) / 20.0f));
                tempext.setTranslationX(-1.0f * (((largura / 200f) * 78f) /*+ ((largura - 200f) / 11.11111111111f)*/));


              /*  TextView tempint = (TextView) getView().findViewById(R.id.tempint_2);
                tempint.setScaleX(largura / 200f);
                tempint.setScaleY(largura / 200f);


                tempint.setTranslationY(((largura / 200f) * 5f) + ((largura - 200f) / 20.0f));
                tempint.setTranslationX(-1.0f * (((largura / 200f) * 78f) *//*+ ((largura - 200f) / 11.11111111111f)*//*));


*/

                TextView velo = (TextView) getView().findViewById(R.id.vel_2);

                //FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) velo.getLayoutParams();
                //params.setMargins(0,(int) (60f*largura/200f), 0, 0); //substitute parameters for left, top, right, bottom
                //velo.setLayoutParams(params);


                velo.setScaleX(largura / 200f);
                velo.setScaleY(largura / 200f);
                velo.setTranslationY(((largura / 200f) * 45f) + ((largura - 200f) / 10f));
                //velo.setTranslationX(-((largura / 200f) * 10f) + ((largura - 200f) / 10f));
                //velo.setTranslationX((-1.0f* (largura / 200f) * 5f)  );

                /*
                ImageView rect = (ImageView) getView().findViewById(R.id.rect_2);
                rect.setScaleX(largura / 200f);
                rect.setScaleY(largura / 200f);
                rect.setTranslationY(((largura / 200f) * 103f) + ((largura - 200f) / 18.1818f));
                //rect.setTranslationX((-1.0f* (largura / 200f) * 5f)  );
                */

                ProgressBar pb3 = (ProgressBar) getView().findViewById(R.id.yourId3);

                pb3.setScaleX(largura / 200f);
                pb3.setScaleY(largura / 200f);
                pb3.setTranslationY(((largura / 200f) * 106f) + ((largura - 200f) / 20f));
                //pb3.setProgress(50);


                ImageButton rangebar = (ImageButton) getView().findViewById(R.id.botao_range_2);

                rangebar.setScaleX(largura / 200f);
                rangebar.setScaleY(largura / 200f);
                rangebar.setTranslationY(((largura / 200f) * 106f) + ((largura - 200f) / 20f));



                TextView soc = (TextView) getView().findViewById(R.id.soc_2);

                soc.setScaleX(largura / 200f);
                soc.setScaleY(largura / 200f);
                soc.setTranslationY(((largura / 200f) * 103f) + ((largura - 200f) / 18.1818f));
                soc.setTranslationX(-1.0f * (((largura / 200f) * 50f) /*- ((largura - 200f) / 6.666666666666f)*/));


                TextView range = (TextView) getView().findViewById(R.id.range_2);

                range.setScaleX(largura / 200f);
                range.setScaleY(largura / 200f);
                range.setTranslationY(((largura / 200f) * 103f) + ((largura - 200f) / 18.1818f));
                range.setTranslationX(1.0f * (((largura / 200f) * 50f) /*- ((largura - 200f) / 6.666666666666f)*/));


                FrameLayout myLayout = (FrameLayout) getView().findViewById(R.id.contemprog2);
                myLayout.setVisibility(View.VISIBLE);



                scaledone = true;
            }



            //updates
            if (visivel && scaledone) {
                //try {






                ProgressBar pb = (ProgressBar) getView().findViewById(R.id.anel_power);
                    if (cnt>0) {

                        //pb.setRotation((-cnt / 1000f * 360f) - 90f);
                        pb.setProgress(cnt);
                    }
                else pb.setProgress(0);

                //pb.setVisibility(View.INVISIBLE);


                    ProgressBar pb2 = (ProgressBar) getView().findViewById(R.id.anel_regen);
                if (cnt<0) {
                    pb2.setRotation((cnt / 1000f * 360f) - 0f);
                    //pb2.setTranslationX(-100);
                    //pb2.setTranslationY(cnt/5);
                    pb2.setProgress(-cnt);
                }
                else pb2.setProgress(0);

                //pb2.setVisibility(View.INVISIBLE);




                ImageView ponte = (ImageView)  getView().findViewById(R.id.ponteiro_2);
                ponte.setPivotX(ponte.getWidth() / 2);
                ponte.setPivotY(ponte.getHeight());



                ponte.setRotation(cnt / 250f * 89.5f);



                Drawable background = ponte.getBackground();

                if (cnt<0) {

                    if (background instanceof ShapeDrawable) {
                        // cast to 'ShapeDrawable'
                        ShapeDrawable shapeDrawable = (ShapeDrawable) background;
                        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.verdep));
                        //shapeDrawable.getPaint().setStrokeWidth(10);

                    } else if (background instanceof GradientDrawable) {
                        // cast to 'GradientDrawable'
                        GradientDrawable gradientDrawable = (GradientDrawable) background;
                        gradientDrawable.setColor(getResources().getColor(R.color.verdep));
                        //gradientDrawable.setStroke(10,getResources().getColor(R.color.verdep));

                    }
                    if (MainActivity.SimplePointerModeMain) ponte.setScaleX(largura / 150f);

                }
                else {
                    if (background instanceof ShapeDrawable) {
                        // cast to 'ShapeDrawable'
                        ShapeDrawable shapeDrawable = (ShapeDrawable) background;
                        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.laranjap));
                    } else if (background instanceof GradientDrawable) {
                        // cast to 'GradientDrawable'
                        GradientDrawable gradientDrawable = (GradientDrawable) background;
                        gradientDrawable.setColor(getResources().getColor(R.color.laranjap));
                    }
                    if (MainActivity.SimplePointerModeMain) ponte.setScaleX(largura / 200f);

                }




                //} catch (Exception e) {

                //}
            }

            //if (cnt >=240) return;

            handler.postDelayed(timedTask, 30);
        }};



    private static boolean visivel;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visivel = isVisibleToUser;

    }



}