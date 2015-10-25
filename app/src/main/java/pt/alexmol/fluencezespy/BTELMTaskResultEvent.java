package pt.alexmol.fluencezespy;

/**
 * Created by alexandre.moleiro on 24-10-2015.
 */
class BTELMTaskResultEvent {

    private final long indice;
    private final long dado;

    public BTELMTaskResultEvent(long indice, long dado) {
        this.indice = indice;
        this.dado = dado;
    }

    public long[] getResult() {
        long[] resultado = new long[2];
        resultado[0] = indice;
        resultado[1] = dado;

        return resultado;
    }


}
