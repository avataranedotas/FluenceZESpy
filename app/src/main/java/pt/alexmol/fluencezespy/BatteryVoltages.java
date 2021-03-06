package pt.alexmol.fluencezespy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BatteryVoltages extends AppCompatActivity {



    private String[] celulas;
    private float maisalta;
    private float maisbaixa;
    private float inicio;
    private float fim;
    private int intervalo;
    private int lowest = 9999;
    private int highest = 0;


    private static BarChart mBarChart;

    private int coresbarras[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (MainActivity.noite) setContentView(R.layout.activity_battery_voltages_noite);
        else setContentView(R.layout.activity_battery_voltages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyBus.getInstance().register(this);

    }


    @Override
    protected void onResume() {
        super.onResume();


        //indica à task que não está em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(1));



        if (MainActivity.keepscreenMain) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }


        //actualizagrafico();



        obtemintervalo();



        mBarChart = (BarChart) findViewById(R.id.chart);





        //dados para o gráfico

        ArrayList<BarEntry> tensoescelulas = new ArrayList<BarEntry>();
        //ArrayList<Entry> outratreta = new ArrayList<Entry>();

        for (int i=0; i<=95;i++) {
            float tensao;
            if ( MainActivity.tensoesdascelulas[i] != Short.MAX_VALUE) tensao = (MainActivity.tensoesdascelulas[i])/1000.0f;
            else tensao = 0.0f;
            BarEntry dado = new BarEntry(tensao,i);
            tensoescelulas.add(dado);

        }


        for (int i=0; i<=95;i++) {
             //encontrar highest
            if (MainActivity.tensoesdascelulas[i] > highest) highest = MainActivity.tensoesdascelulas[i];
            //encontrar lowest
            if (MainActivity.tensoesdascelulas[i] < lowest) lowest = MainActivity.tensoesdascelulas[i];

        }





        int azul = getResources().getColor(R.color.azul);
        int vermelho = getResources().getColor(R.color.vermelho);
        int verde = getResources().getColor(R.color.verde);
        int laranja = getResources().getColor(R.color.laranja);
        int amarelo = getResources().getColor(R.color.amarelo);



        BarDataSet setComp1 = new BarDataSet(tensoescelulas,"Volts");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        setComp1.setDrawValues(false);


        //calcular JV

        int jv = 0;

        if (MainActivity.tipobateria==0) {

            jv = (int) ((2.5 * MainActivity.valoresmemorizados[12] / 100 - 0.144 * MainActivity.valoresmemorizados[36]) / 96.0 * 1000.0);
        }

        if (MainActivity.tipobateria==1) {

            jv = (int) ((2.5 * MainActivity.valoresmemorizados[7] / 2 - 1.44 * MainActivity.valoresmemorizados[36]) / 96.0 * 1000.0);
        }



        //criar uma array de ints com as 96 cores

        coresbarras = new int[96];

        for (int i=0; i<=95;i++) {
            coresbarras[i] = azul;
            if ( MainActivity.shuntscelulas[i] == true) coresbarras[i] = verde;
            //if ( MainActivity.tensoesdascelulas[i] < MainActivity.valoresmemorizados[76]) coresbarras[i] = amarelo;
            //if ( MainActivity.tensoesdascelulas[i] < MainActivity.valoresmemorizados[38]) coresbarras[i] = laranja;
            if ( MainActivity.tensoesdascelulas[i] < jv && MainActivity.tensoesdascelulas[i] < 3.7) coresbarras[i] = vermelho;

        }


        setComp1.setColors(coresbarras);

        setComp1.setBarSpacePercent(25.0f);






        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i=1; i<=96;i++) {
            xVals.add(Integer.toString(i));
        }

        BarData data = new BarData(xVals, dataSets);

        //eixo X
        mBarChart.getXAxis().setAxisLineWidth(3.0f);
        mBarChart.getXAxis().setAxisLineColor(Color.BLACK);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getXAxis().setGridColor(Color.GRAY);
        mBarChart.getXAxis().setDrawGridLines(false);
        //mBarChart.getXAxis().enableGridDashedLine(10.0F, 10.0F, 0.0F);

        //eixo Y esquerda
        mBarChart.getAxis(setComp1.getAxisDependency()).setStartAtZero(false);
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMaxValue(fim);
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMinValue(inicio);
        mBarChart.getAxis(setComp1.getAxisDependency()).setValueFormatter(new MyYAxisValueFormatter());

        mBarChart.getAxis(setComp1.getAxisDependency()).setLabelCount(intervalo, true);

        //mBarChart.getAxis(setComp1.getAxisDependency()).setSpaceTop(100f);

        //eixo Y direita
        //mBarChart.getAxisRight().setStartAtZero(false);
        //mBarChart.getAxisRight().setAxisMaxValue(4.200001f);
        //mBarChart.getAxisRight().setAxisMinValue(3.0f);
        //mBarChart.getAxisRight().setDrawGridLines(false);
        //mBarChart.getAxisRight().setValueFormatter(new MyYAxisValueFormatter());

        //mBarChart.getAxisRight().setLabelCount(13, true);

        mBarChart.getAxisRight().setEnabled(false);


        mBarChart.getLegend().setEnabled(false);

        mBarChart.setDescription("");




        if (MainActivity.noite) {

            mBarChart.setBackgroundColor(Color.BLACK);

            mBarChart.setDrawGridBackground(false);

            mBarChart.getAxis(setComp1.getAxisDependency()).setTextColor(Color.WHITE);

            mBarChart.getXAxis().setTextColor(Color.WHITE);


        }



        mBarChart.setData(data);

        mBarChart.invalidate(); // refresh


        actualizarvalores(MainActivity.valoresmemorizados);



    }


    @Override
    protected void onPause() {
        super.onPause();

        //indica à asynctask que vai entrar em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(2));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyBus.getInstance().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_battery_voltages, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //quando for necessário receber directamente actualizações da btelmasynctask

    @Subscribe
    public void recebereventos (BTELMTaskResultEvent event) {

        //evento de recepção de valores para memorizar
        //100 corresponde ao indice 0
        if (event.getResult()[0]>=100 && event.getResult()[0]<300 ) {
            MainActivity.valoresmemorizados[  ( event.getResult()[0] -100)  ]=event.getResult()[1];

        }


        //evento de recepção de tensões de células para memorizar
        //501 corresponde ao indice 0
        if (event.getResult()[0]>=501 &&  event.getResult()[0]<=596) {
            MainActivity.tensoesdascelulas[  ( event.getResult()[0] -501)  ]= (short) event.getResult()[1];

        }

        //se receber o 596 pode actualizar o gráfico
        if (event.getResult()[0]==596) {

            actualizagrafico();

        }


        //evento actualização de páginas
        if (event.getResult()[0]==3 && event.getResult()[1]==0 ) {


            actualizarvalores(MainActivity.valoresmemorizados);


        }


    }



    //actualizar gráfico
    private void actualizagrafico () {


        obtemintervalo();

        mBarChart = (BarChart) findViewById(R.id.chart);


        //dados para o gráfico

        ArrayList<BarEntry> tensoescelulas = new ArrayList<BarEntry>();

        for (int i=0; i<=95;i++) {
            float tensao;
            if ( MainActivity.tensoesdascelulas[i] != Short.MAX_VALUE) tensao = (MainActivity.tensoesdascelulas[i])/1000.0f;
            else tensao = 0.0f;
            BarEntry dado = new BarEntry(tensao,i);
            tensoescelulas.add(dado);
        }

        int azul = getResources().getColor(R.color.azul);
        int vermelho = getResources().getColor(R.color.vermelho);
        int verde = getResources().getColor(R.color.verde);
        int laranja = getResources().getColor(R.color.laranja);
        int amarelo = getResources().getColor(R.color.amarelo);


        BarDataSet setComp1 = new BarDataSet(tensoescelulas,"Volts");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setDrawValues(false);


        //calcular JV

        int jv = 0;

        if (MainActivity.tipobateria==0) {

            jv = (int) ((2.5 * MainActivity.valoresmemorizados[12] / 100 - 0.144 * MainActivity.valoresmemorizados[36]) / 96.0 * 1000.0);
        }

        if (MainActivity.tipobateria==1) {

            jv = (int) ((2.5 * MainActivity.valoresmemorizados[7] / 2 - 1.44 * MainActivity.valoresmemorizados[36]) / 96.0 * 1000.0);
        }




        //criar uma array de ints com as 96 cores

        coresbarras = new int[96];

        for (int i=0; i<=95;i++) {
            coresbarras[i] = azul;
            if ( MainActivity.shuntscelulas[i] == true) coresbarras[i] = verde;
            //if ( MainActivity.tensoesdascelulas[i] < MainActivity.valoresmemorizados[76]) coresbarras[i] = amarelo;
            //if ( MainActivity.tensoesdascelulas[i] < MainActivity.valoresmemorizados[38]) coresbarras[i] = laranja;
            if ( MainActivity.tensoesdascelulas[i] < jv && MainActivity.tensoesdascelulas[i] < 3.7) coresbarras[i] = vermelho;

        }

        //teste
        //coresbarras[10] = ciano;
        //coresbarras[90] = vermelho;

        setComp1.setColors(coresbarras);
        setComp1.setBarSpacePercent(25.0f);


        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i=1; i<=96;i++) {
            xVals.add(Integer.toString(i));
        }

        BarData data = new BarData(xVals, dataSets);

        /*
        //eixo X
        mBarChart.getXAxis().setAxisLineWidth(3.0f);
        mBarChart.getXAxis().setAxisLineColor(Color.BLACK);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getXAxis().setGridColor(Color.GRAY);
        mBarChart.getXAxis().setDrawGridLines(false);
        //mBarChart.getXAxis().enableGridDashedLine(10.0F, 10.0F, 0.0F);

        //eixo Y esquerda
        mBarChart.getAxis(setComp1.getAxisDependency()).setStartAtZero(false);
        */
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMaxValue(fim);
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMinValue(inicio);

        //mBarChart.getAxis(setComp1.getAxisDependency()).setValueFormatter(new MyYAxisValueFormatter());

        mBarChart.getAxis(setComp1.getAxisDependency()).setLabelCount(intervalo, true);

        /*
        //mBarChart.getAxis(setComp1.getAxisDependency()).setSpaceTop(100f);

        //eixo Y direita
        //mBarChart.getAxisRight().setStartAtZero(false);
        //mBarChart.getAxisRight().setAxisMaxValue(4.200001f);
        //mBarChart.getAxisRight().setAxisMinValue(3.0f);
        //mBarChart.getAxisRight().setDrawGridLines(false);
        //mBarChart.getAxisRight().setValueFormatter(new MyYAxisValueFormatter());

        //mBarChart.getAxisRight().setLabelCount(13, true);

        mBarChart.getAxisRight().setEnabled(false);


        mBarChart.getLegend().setEnabled(false);

        mBarChart.setDescription("");
        */


        mBarChart.setData(data);

        mBarChart.invalidate(); // refresh



    }


    //actualizar valores
    private void actualizarvalores(int[] arrayv) {






        TextView view = (TextView) findViewById(R.id.battery_information);
        String texto = "";

        //Soc
        if (MainActivity.valoresmemorizados[0]!=Integer.MAX_VALUE) {
            double temp = ((double) arrayv[0]) / 100.0;
            texto = texto + "SoC:"+String.format("%3.2f", temp) + "%    ";
        }

        //Real SoC
        if (MainActivity.valoresmemorizados[15]!=Integer.MAX_VALUE) {
            double temp = ((double) arrayv[15]) / 10000.0;
            texto = texto + "REAL SoC:"+String.format("%2.3f", temp) + "%    ";
        }

        //Health
        if (MainActivity.valoresmemorizados[18]!=Integer.MAX_VALUE) {
            double temp = ((double) arrayv[18]) / 2.0;
            texto = texto + "SoH:"+String.format("%3.1f", temp) + "%    ";
        }

        //Ah
        if (MainActivity.valoresmemorizados[16]!=Integer.MAX_VALUE) {
            double temp = ((double) arrayv[16]) / 10000.0;
            texto = texto + String.format("%2.3f", temp) + "Ah    ";
        }

        //odoBat km
        if (MainActivity.valoresmemorizados[19] != Integer.MAX_VALUE) {
            texto = texto + "OdoBat:";
            double temp;
            if (!MainActivity.MilesModeMain)
            {
                temp = ((double) MainActivity.valoresmemorizados[19]) / 1.0;
                if (MainActivity.valoresmemorizados[19]<65535) texto = texto + ((int) temp) + "km / ";
                else texto = texto + ">65535km / ";
            }
            else {
                temp = ((double) MainActivity.valoresmemorizados[19]) / 1.609344;
                if (MainActivity.valoresmemorizados[19]<65535) texto = texto + ((int) temp) + "mi / ";
                else texto = texto + (">40721mi / ");
            }


        }

        //Ageing counter
        if (MainActivity.valoresmemorizados[20]!=Integer.MAX_VALUE) {
            texto = texto + arrayv[20]+ "kWh    ";
        }


        //odoCar
        if (MainActivity.valoresmemorizados[66]!=Integer.MAX_VALUE) {

            if (!MainActivity.MilesModeMain) texto = texto + "OdoCar:" + (arrayv[66] / 100) + "km";
            else  texto = texto + "OdoCar:" + (int)( ((double)arrayv[66]) / 160.9344) + "mi";
        }


        /*
        //JV
        if (MainActivity.valoresmemorizados[12]!=Integer.MAX_VALUE && MainActivity.valoresmemorizados[36]!=Integer.MAX_VALUE) {
            int jv = (int) ((2.5 * MainActivity.valoresmemorizados[12] / 100 - 0.144 * MainActivity.valoresmemorizados[36]) / 96.0 * 1000.0);
            texto = texto + "JV:" + jv + "mV    ";
        }

        //weak cell
        if (MainActivity.valoresmemorizados[38]!=Integer.MAX_VALUE) {
            texto = texto + "WC:"+ arrayv[38]+ "mV    ";
        }
        */


        view.setText(texto);



        view = (TextView) findViewById(R.id.cell_information);
        texto = "";



        if (MainActivity.tipobateria==0) {

            if (MainActivity.valoresmemorizados[37] != Integer.MAX_VALUE && MainActivity.valoresmemorizados[36] != Integer.MAX_VALUE && MainActivity.valoresmemorizados[7] != Integer.MAX_VALUE) {
                double temp = ((double) arrayv[37]) / 1000.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                temp = ((double) arrayv[12]) / 100.0 / 96.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                temp = ((double) arrayv[36]) / 1000.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                texto = texto + (arrayv[36] - arrayv[37]) + "mV";

            }
        }

        if (MainActivity.tipobateria==1) {


            for (int i=0; i<=95;i++) {
                //encontrar highest
                if (MainActivity.tensoesdascelulas[i] > highest) highest = MainActivity.tensoesdascelulas[i];
                //encontrar lowest
                if (MainActivity.tensoesdascelulas[i] < lowest) lowest = MainActivity.tensoesdascelulas[i];

            }

            if (MainActivity.valoresmemorizados[7] != Integer.MAX_VALUE) {
                double temp = ((double) lowest) / 1000.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                temp = ((double) arrayv[7]) / 2.0 / 96.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                temp = ((double) highest) / 1000.0;
                texto = texto + String.format("%1.3f", temp) + "V      ";

                texto = texto + ((highest - lowest)) + "mV";

            }
        }







        view.setText(texto);





    }


    private void obtemintervalo() {

        if (MainActivity.tipobateria==0) {

            //células alta e baixa
            if (MainActivity.valoresmemorizados[36] != Integer.MAX_VALUE)
                maisalta = (float) (MainActivity.valoresmemorizados[36]) / 1000.0f;
            else maisalta = 4.0f;
            if (MainActivity.valoresmemorizados[37] != Integer.MAX_VALUE)
                maisbaixa = (float) (MainActivity.valoresmemorizados[37]) / 1000.0f;
            else maisbaixa = 3.9f;

        }


        if (MainActivity.tipobateria==1) {

            //células alta e baixa
            if (MainActivity.valoresmemorizados[36] != Integer.MAX_VALUE)
                maisalta = (float) (MainActivity.valoresmemorizados[36]) / 100.0f;
            else maisalta = 4.0f;
            if (MainActivity.valoresmemorizados[37] != Integer.MAX_VALUE)
                maisbaixa = (float) (MainActivity.valoresmemorizados[37]) / 100.0f;
            else maisbaixa = 3.9f;

        }



        //calculo do inicio e fim de escala para dar 500mV de gama excepto em casos expeciais

        intervalo = 11;
        //se diferença superior a 300mV

        if ((maisalta-maisbaixa)>=0.3) {


            //gama normal 3,1 a 4,1
            inicio = 3.1f;
            fim = 4.10001f;
            //intervalo = 11;

            //gama alta 3,2 a 4,2
            if (maisalta > 4.1f) {
                inicio = 3.2f;
                fim = 4.20001f;
            }

            //gama baixa 2,5 a 3,5
            if (maisbaixa <= 3.1f) {
                inicio = 2.5f;
                fim = 3.50001f;
            }
        }
        //se diferença inferior a 300mV
        else {
            //gama normal 3,6 a 4,1
            inicio = 3.6f;
            fim = 4.10001f;
            //intervalo = 6;

            //gama alta 3,7 a 4,2 - pior caso 3,801 a 4,101
            if (maisalta > 4.1f) {
                inicio = 3.7f;
                fim = 4.20001f;
            }

            //gama 3.4 a 3.9
            if (maisbaixa < 3.6f) {
                inicio = 3.4f;
                fim = 3.90001f;
            }

            //gama 3.2 a 3.7
            if (maisbaixa < 3.4f) {
                inicio = 3.2f;
                fim = 3.70001f;
            }

            //gama 3.0 a 3.5
            if (maisbaixa < 3.2f) {
                inicio = 3.0f;
                fim = 3.50001f;
            }

            //gama 2.8 a 3.3
            if (maisbaixa < 3.0f) {
                inicio = 2.8f;
                fim = 3.30001f;
            }

            //gama 2.6 a 3.1
            if (maisbaixa < 2.8f) {
                inicio = 2.6f;
                fim = 3.10001f;
            }

            //gama 2.4 a 2.9
            if (maisbaixa < 2.6f) {
                inicio = 2.4f;
                fim = 2.90001f;
            }


        }


    }




    public class MyYAxisValueFormatter implements YAxisValueFormatter {

        private DecimalFormat mFormat;

        public MyYAxisValueFormatter () {
            mFormat = new DecimalFormat("0.000"); // use 3 decimals
        }

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            // write your logic here
            // access the YAxis object to get more information
            return mFormat.format(value) + "V"; // e.g. append a dollar-sign
        }
    }

}
