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

public class Page1 extends Fragment {
    Context c;

    private final int invalido = Integer.MAX_VALUE;

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



        View v = inflater.inflate(R.layout.section1, null);

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
        MainActivity actividademain1 = (MainActivity)getActivity();
        actpag1(actividademain1.valoresmemorizados);

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
        if (array1[0]!=invalido) {
            TextView view = (TextView) getView().findViewById(R.id.socx475_1);
            double temp = ((double) array1[0]) / 100.0;
            view.setText("SOC:" + String.format("%3.2f", temp) + "%");
        }


    }
}







