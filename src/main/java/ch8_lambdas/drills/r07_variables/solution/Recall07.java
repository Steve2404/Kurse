package ch8_lambdas.drills.r07_variables.solution;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * SOLUTION du drill de rappel 7 - les variables dans les lambdas.
 */
public class Recall07 {

    private int instanceCounter;
    private static int staticCounter;
    private final String name = "drill";

    // Une lambda peut lire ET modifier les champs (d'instance ou static) : seules les variables LOCALES sont figees.
    IntSupplier counters() {
        return () -> ++instanceCounter + 10 * ++staticCounter;
    }

    // Dans une lambda, this designe l'objet englobant ; dans une classe anonyme, this designe l'anonyme.
    String whoIsThis() {
        Supplier<String> lambda = () -> this.name;
        Supplier<String> anonymous = new Supplier<>() {
            private final String name = "anonyme";

            @Override
            public String get() {
                return this.name;
            }
        };
        return lambda.get() + " " + anonymous.get();
    }

    public static void main(String[] args) {
        int base = 5;                                   // effectively final : jamais reaffecte
        IntSupplier plusBase = () -> base + 1;
        System.out.println("D01 : " + plusBase.getAsInt());
        Recall07 r = new Recall07();
        IntSupplier c = r.counters();
        c.getAsInt();
        System.out.println("D02 : " + c.getAsInt() + " " + r.instanceCounter + " " + staticCounter);
        int[] box = {0};                                // la reference box ne change pas ; son contenu, si
        Runnable inc = () -> box[0]++;
        inc.run();
        inc.run();
        inc.run();
        System.out.println("D03 : " + box[0]);
        System.out.println("D04 : " + r.whoIsThis());
        String label = "x";
        // Une variable capturee ne peut plus changer : ni apres la lambda, ni dedans (sinon erreur de compilation).
        Supplier<String> twice = () -> label + label;
        System.out.println("D05 : " + twice.get());
        StringBuilder sb = new StringBuilder("a");
        Supplier<String> view = sb::toString;          // sb est capture ; l'OBJET reste modifiable
        sb.append("b");
        System.out.println("D06 : " + view.get());
    }
}
