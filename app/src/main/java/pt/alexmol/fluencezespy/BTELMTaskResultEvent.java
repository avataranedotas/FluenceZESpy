package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class BTELMTaskResultEvent {

    private final int indice;
    private final int dado;

    public BTELMTaskResultEvent(int indice, int dado) {
        this.indice = indice;
        this.dado = dado;
    }

    public int[] getResult() {
        int[] resultado = new int[2];
        resultado[0] = indice;
        resultado[1] = dado;

        return resultado;
    }


}
