package pt.alexmol.fluencezespy;

import com.squareup.otto.Bus;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
public class MyBus {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }


}
