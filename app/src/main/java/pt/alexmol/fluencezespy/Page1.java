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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Page1 extends Fragment {
    // --Commented out by Inspection (05-11-2015 14:55):Context c;

    //private pagina1interface listener;

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

        MyBus.getInstance().register(this);
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



    }

    @Override public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }



    public void setPasso(String url) {
        TextView view = (TextView) getView().findViewById(R.id.passo);
        view.setText(url);

    }


    public void actpag1(int[] array1) {
        int invalido = Integer.MAX_VALUE;
        if (array1[0]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_1);
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
            TextView view = (TextView) getView().findViewById(R.id.battery12v_1);
            double temp = ((double) array1[14]) / 1000.0;
            view.setText(String.format("%2.2f", temp) + "V");
        }


        if (array1[28]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.hvcons);
            double temp = ((double) array1[28]) / 100.0;
            view.setText(String.format("%1.1f", temp) + "kW");

            ImageView image = (ImageView) getView().findViewById(R.id.aux_hv);

            if (temp==0.0) {
                view.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            }
            else {
                view.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
            }
        }

        if (array1[33]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.range_1);
            //double temp = ((double) array1[14]) / 1000.0;
            view.setText(array1[33]+ "km");
        }

        if (array1[34]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.timetofull_1);
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


        if (array1[6]!= invalido) {
            TextView view = (TextView) getView().findViewById(R.id.evse_1);
            if (array1[6]!=0 && array1[6]<48 && array1[5]!=0 && array1[5] != invalido && !(MainActivity.TABLET ^ MainActivity.reverseModeMain ) ) {
                view.setVisibility(View.VISIBLE);
                view.setText(array1[6] + "A");
            }
            else view.setVisibility(View.INVISIBLE);

        }



        if (array1[5]!= invalido) {
            ImageView view = (ImageView) getView().findViewById(R.id.carrocharging);
            if (array1[5]!=0) view.setVisibility(View.VISIBLE);
            else view.setVisibility(View.INVISIBLE);

        }

        /*

        if (array1[42]!= invalido ) {
            TextView view = (TextView) getView().findViewById(R.id.daynight1_1);

            if (array1[42]==0) view.setText("Day");
            else view.setText("Night");



        }
        */



    }
}







