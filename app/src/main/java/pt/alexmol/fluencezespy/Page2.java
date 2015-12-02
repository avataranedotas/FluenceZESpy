package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 15-10-2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

        handler.post(timedTask);

        View vista;
        vista = getView().findViewById(R.id.cima3);

        //vista.setCont


        SurfaceView surface = (SurfaceView) getView().findViewById(R.id.surface3);


        surface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Do some drawing when surface is ready
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.RED);






                float width = (float) getView().getWidth();

                float height = (float) getView().getHeight();

                float radius;



                if (width > height){

                    radius = height/4;

                }else{

                    radius = width/4;

                }



                Path path = new Path();

                path.addCircle(width/2,

                        height/2, radius,

                        Path.Direction.CW);



                Paint paint = new Paint();

                paint.setColor(Color.WHITE);

                paint.setStrokeWidth(5);



                paint.setStyle(Paint.Style.FILL);

                float center_x, center_y;

                center_x = width/2;

                center_y = height/4;

                final RectF oval = new RectF();

                oval.set(center_x - radius,

                        center_y - radius,

                        center_x + radius,

                        center_y + radius);

                canvas.drawArc(oval, 90, 270, true, paint);



                paint.setStyle(Paint.Style.STROKE);

                center_x = width/2;

                center_y = height/2;

                oval.set(center_x - radius,

                        center_y - radius,

                        center_x + radius,

                        center_y + radius);

                canvas.drawArc(oval, 90, 270, true, paint);



                paint.setStyle(Paint.Style.STROKE);

                center_x = width/2;

                center_y = height * 3/4;

                oval.set(center_x - radius,

                        center_y - radius,

                        center_x + radius,

                        center_y + radius);

                canvas.drawArc(oval, 90, 270, false, paint);





                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(timedTask);
    }

    @Override public void onDestroy() {
        //MyBus.getInstance().unregister(this);
        super.onDestroy();

    }


    private int cnt = 0;

    private Handler handler = new Handler();


    private Runnable timedTask = new Runnable(){

        @Override
        public void run() {
            cnt=cnt+5;
            if (cnt>250) cnt = 0;
            if (visivel) {
                //try {
                    ProgressBar pb = (ProgressBar) getView().findViewById(R.id.yourId);
                    pb.setProgress(cnt);
                //} catch (Exception e) {

                //}
            }



            handler.postDelayed(timedTask, 30);
        }};



    private static boolean visivel;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visivel = isVisibleToUser;

    }


    public class MyView extends View {



        public MyView(Context context) {

            super(context);

            // TODO Auto-generated constructor stub

        }



        @Override

        protected void onDraw(Canvas canvas) {

            // TODO Auto-generated method stub

            super.onDraw(canvas);





            float width = (float)getWidth();

            float height = (float)getHeight();

            float radius;



            if (width > height){

                radius = height/4;

            }else{

                radius = width/4;

            }



            Path path = new Path();

            path.addCircle(width/2,

                    height/2, radius,

                    Path.Direction.CW);



            Paint paint = new Paint();

            paint.setColor(Color.WHITE);

            paint.setStrokeWidth(5);



            paint.setStyle(Paint.Style.FILL);

            float center_x, center_y;

            center_x = width/2;

            center_y = height/4;

            final RectF oval = new RectF();

            oval.set(center_x - radius,

                    center_y - radius,

                    center_x + radius,

                    center_y + radius);

            canvas.drawArc(oval, 90, 270, true, paint);



            paint.setStyle(Paint.Style.STROKE);

            center_x = width/2;

            center_y = height/2;

            oval.set(center_x - radius,

                    center_y - radius,

                    center_x + radius,

                    center_y + radius);

            canvas.drawArc(oval, 90, 270, true, paint);



            paint.setStyle(Paint.Style.STROKE);

            center_x = width/2;

            center_y = height * 3/4;

            oval.set(center_x - radius,

                    center_y - radius,

                    center_x + radius,

                    center_y + radius);

            canvas.drawArc(oval, 90, 270, false, paint);



        }



    }



}