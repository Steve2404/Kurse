package ch13_concurrency.projects.p08_async.solution;

import java.util.concurrent.RecursiveAction;

/**
 * SOLUTION - une tache Fork/Join SANS resultat : elle modifie le tableau sur place (ici : ramene chaque valeur dans [-50, 50]).
 */
public class ClampAction extends RecursiveAction {

    private final int[] values;
    private final int from;
    private final int to;

    public ClampAction(int[] values, int from, int to) {
        this.values = values;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void compute() {
        if (to - from <= 50_000) {
            for (int i = from; i < to; i++) {
                values[i] = Math.max(-50, Math.min(50, values[i]));
            }
            return;
        }
        int mid = (from + to) >>> 1;
        invokeAll(new ClampAction(values, from, mid), new ClampAction(values, mid, to));   // fork des deux + join
    }
}
