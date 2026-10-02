package ch7_beyondclasses.drills.r07_nested.solution;

/**
 * SOLUTION du drill de rappel 7 - les classes imbriquees.
 */
public class Recall07 {

    public static void main(String[] args) {
        Outer o = new Outer();
        Outer.Inner in = o.new Inner();          // une interne a besoin d'une instance de Outer
        System.out.println("D01 : " + in.sum() + " " + o.makeInner().sum());
        System.out.println("D02 : " + in.who());
        Outer.Nested n = new Outer.Nested();     // une static imbriquee n'en a pas besoin
        System.out.println("D03 : " + n.value() + " " + Outer.Nested.LIMIT);
        System.out.println("D04 : " + o.local(3));
        Greeter g = o.anon("Ana");
        System.out.println("D05 : " + g.greet() + " " + g.getClass().isAnonymousClass() + " [" + g.getClass().getSimpleName() + "]");
        Greeter inline = new Greeter() {
            @Override
            public String greet() {
                return "bonjour";
            }
        };
        System.out.println("D06 : " + inline.greet() + " " + o.counter());
    }
}

interface Greeter {
    String greet();
}

class Outer {
    private int secret = 7;
    private static int count = 4;

    // Classe INTERNE : chaque instance est liee a un Outer ; elle voit tout, meme le prive.
    class Inner {
        private int secret = 1;   // masque Outer.secret
        int x = 10;

        int sum() {
            return x + Outer.this.secret;
        }

        String who() {
            return secret + "/" + this.secret + "/" + Outer.this.secret;
        }
    }

    // Classe imbriquee STATIC : pas d'instance de Outer ; elle ne voit que les membres static.
    static class Nested {
        static final int LIMIT = 99;

        int value() {
            return count + 1;
        }
    }

    Inner makeInner() {
        return new Inner();      // this.new Inner() implicite
    }

    // Classe LOCALE : elle lit base et factor, qui doivent etre effectively final.
    int local(int factor) {
        int base = 2;
        class Local {
            int calc() {
                return base * factor * secret;
            }
        }
        return new Local().calc();
    }

    // Classe ANONYME : elle implemente l'interface et lit le parametre name (effectively final).
    Greeter anon(String name) {
        return new Greeter() {
            @Override
            public String greet() {
                return "salut " + name;
            }
        };
    }

    int counter() {
        int[] calls = {0};
        // Une anonyme qui etend une CLASSE (ici Object) et redefinit toString.
        Object o = new Object() {
            @Override
            public String toString() {
                calls[0]++;
                return "anonyme";
            }
        };
        String s = o.toString() + o.toString();
        return calls[0] + s.length();
    }
}
