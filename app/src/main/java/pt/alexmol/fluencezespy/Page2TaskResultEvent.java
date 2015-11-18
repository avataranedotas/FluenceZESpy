package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class Page2TaskResultEvent {

    private final int comando;


    public Page2TaskResultEvent(int comando) {
        this.comando = comando;
    }

    public int getResult() {

        return comando;
    }


}
