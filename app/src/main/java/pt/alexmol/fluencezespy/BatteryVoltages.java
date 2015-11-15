package pt.alexmol.fluencezespy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pt.alexmol.fluencezespy.R;

public class BatteryVoltages extends AppCompatActivity {



    private String[] celulas;
    //private float maisalta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_voltages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyBus.getInstance().register(this);

    }


    @Override
    protected void onResume() {
        super.onResume();

        //indica à task que não está em pausa
        MyBus.getInstance().post(new MainTaskResultEvent(1));


        //célula mais alta

        //maisalta = (float) ((MainActivity.valoresmemorizados[36] /100) *100) / 1000.0f + 0.100001f;

        BarChart mBarChart = (BarChart) findViewById(R.id.chart);


        //dados para o gráfico

        ArrayList<BarEntry> tensoescelulas = new ArrayList<BarEntry>();
        //ArrayList<Entry> outratreta = new ArrayList<Entry>();

        BarEntry c1e1 = new BarEntry(4.055f, 0); // 0 == quarter 1
        tensoescelulas.add(c1e1);
        BarEntry c1e2 = new BarEntry(4.040f, 1); // 1 == quarter 2 ...
        tensoescelulas.add(c1e2);

        BarEntry c1e3 = new BarEntry(4.042f, 2); // 1 == quarter 2 ...
        tensoescelulas.add(c1e3);

        //Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        //outratreta.add(c2e1);
        //Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        //outratreta.add(c2e2);
        //...

        int azul = getResources().getColor(R.color.azul);
        int vermelho = getResources().getColor(R.color.vermelho);


        BarDataSet setComp1 = new BarDataSet(tensoescelulas,"Volts");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setDrawValues(false);
        setComp1.setColors(new int[]{ azul, vermelho});
        setComp1.setBarSpacePercent(15f);


        //LineDataSet setComp2 = new LineDataSet(outratreta, "Company 2");
        //setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setComp1);
        //dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i=1; i<=96;i++) {
            xVals.add(Integer.toString(i));
        }

        BarData data = new BarData(xVals, dataSets);

        //eixo X
        mBarChart.getXAxis().setAxisLineWidth(3.0f);
        mBarChart.getXAxis().setAxisLineColor(Color.BLACK);
        mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBarChart.getXAxis().enableGridDashedLine(4.0F, 5.0F, 0.0F);

        //eixo Y esquerda
        mBarChart.getAxis(setComp1.getAxisDependency()).setStartAtZero(false);
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMaxValue(4.10000001f);
        mBarChart.getAxis(setComp1.getAxisDependency()).setAxisMinValue(3.0f);
        mBarChart.getAxis(setComp1.getAxisDependency()).setValueFormatter(new MyYAxisValueFormatter());

        mBarChart.getAxis(setComp1.getAxisDependency()).setLabelCount(12, true);

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



        mBarChart.setData(data);

        mBarChart.invalidate(); // refresh



        /*
        LineChart mLineChart = (LineChart) findViewById(R.id.chart);


        //dados para o gráfico

        ArrayList<Entry> tensoescelulas = new ArrayList<Entry>();
        //ArrayList<Entry> outratreta = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        tensoescelulas.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        tensoescelulas.add(c1e2);


        //Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        //outratreta.add(c2e1);
        //Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        //outratreta.add(c2e2);
        //...

        LineDataSet setComp1 = new LineDataSet(tensoescelulas, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //LineDataSet setComp2 = new LineDataSet(outratreta, "Company 2");
        //setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        //dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");

        LineData data = new LineData(xVals, dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate(); // refresh
        */

        //refresh chart
        //chart.invalidate();




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
