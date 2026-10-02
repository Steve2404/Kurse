package ch8_lambdas.drills.r02_functional.solution;

import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 2 - les interfaces fonctionnelles.
 */
public class Recall02 {

    public static void main(String[] args) {
        System.out.println("D01 : " + Greeter.polite().greet("Ana"));
        Greeter g = n -> n + "!";
        System.out.println("D02 : " + g.twice().greet("hi") + " " + g.twice().twice().greet("x"));
        Shout s = n -> n.toUpperCase();
        Echo e = n -> n + n;
        Greeter asGreeter = e;
        System.out.println("D03 : " + s.greet("ok") + " " + e.greet("ab") + " " + asGreeter.greet("c"));
        // Une meme lambda, deux types cibles differents : c'est le CONTEXTE qui donne son type a la lambda.
        Greeter a = n -> "[" + n + "]";
        UnaryOperator<String> b = n -> "[" + n + "]";
        Object o = (Greeter) n -> n;                      // vers Object : un cast vers l'interface est obligatoire
        System.out.println("D04 : " + a.greet("x") + " " + b.apply("x") + " " + (o instanceof Greeter));
        Greeter anonymous = new Greeter() {
            @Override
            public String greet(String n) {
                return "anonyme " + n;
            }
        };
        System.out.println("D05 : " + anonymous.greet("z") + " " + Counter.start().next() + " " + Counter.start().next());
    }
}

// Interface fonctionnelle : UNE methode abstraite. Les default, static, private et les methodes publiques d'Object ne comptent pas.
@FunctionalInterface
interface Greeter {
    String greet(String name);

    default Greeter twice() {
        return n -> greet(greet(n));
    }

    static Greeter polite() {
        return n -> "Bonjour " + n;
    }

    @Override
    boolean equals(Object o);

    @Override
    String toString();
}

// Elle n'ajoute aucune methode abstraite : toujours fonctionnelle.
interface Shout extends Greeter {
}

// Elle redeclare la MEME methode : toujours une seule methode abstraite.
@FunctionalInterface
interface Echo extends Greeter {
    @Override
    String greet(String name);
}

@FunctionalInterface
interface Counter {
    int next();

    static Counter start() {
        int[] n = {0};
        return () -> ++n[0];
    }
}
