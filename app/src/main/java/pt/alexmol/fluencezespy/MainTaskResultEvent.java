package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class MainTaskResultEvent {

    private final int comando;


    public MainTaskResultEvent(int comando) {
        this.comando = comando;
    }

    public int getResult() {

        return comando;
    }


}
