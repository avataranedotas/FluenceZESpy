package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class Page1TaskResultEvent {

    private final int comando;


    public Page1TaskResultEvent(int comando) {
        this.comando = comando;
    }

    public int getResult() {

        return comando;
    }


}
