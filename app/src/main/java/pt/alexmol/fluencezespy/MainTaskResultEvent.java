package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
public class MainTaskResultEvent {

    private int comando;


    public MainTaskResultEvent(int comando) {
        this.comando = comando;
    }

    public int getResult() {

        return comando;
    }


}
