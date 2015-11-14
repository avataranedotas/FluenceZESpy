package pt.alexmol.fluencezespy;

import android.content.Context;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class MainTaskContextEvent {

    private Context contexto;


    public MainTaskContextEvent(Context contexto) {
        this.contexto = contexto;
    }

    public Context getResult() {

        return contexto;
    }


}
