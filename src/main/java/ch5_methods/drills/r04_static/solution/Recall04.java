package ch5_methods.drills.r04_static.solution;

import static java.lang.Math.max;
import static java.lang.Math.PI;

/**
 * SOLUTION du drill de rappel 4 - static et final.
 */
public class Recall04 {

    static {
        System.out.println("D01 : Recall04 charge");
    }

    public static void main(String[] args) {
        System.out.println("D02 : main");
        Counter a = new Counter();
        Counter b = new Counter();
        System.out.println("D04 : " + a.id + " " + b.id + " " + Counter.total() + " " + a.next() + " " + a.next() + " " + b.next());
        Counter.base = 100;
        Counter c = new Counter();
        Counter none = null;
        System.out.println("D05 : " + c.value + " " + none.total() + " " + b.total());
        final int[] box = {1};
        box[0]++;                         // final interdit la REASSIGNATION, pas la modification du contenu
        final StringBuilder sb = new StringBuilder("a");
        sb.append("b");
        System.out.println("D06 : " + box[0] + " " + sb);
        System.out.println("D07 : " + max(3, 8) + " " + Math.round(PI * 100));
    }
}
