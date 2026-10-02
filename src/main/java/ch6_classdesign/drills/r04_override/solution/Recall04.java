package ch6_classdesign.drills.r04_override.solution;

/**
 * SOLUTION du drill de rappel 4 - redefinir (override) contre surcharger (overload).
 */
public class Recall04 {

    public static void main(String[] args) {
        A a = new C();
        C c = new C();
        System.out.println("D01 : " + a.hello() + " " + new B().hello() + " " + new A().hello());
        Number n = a.value();
        Integer i = c.value();                  // covariance : C.value() rend un Integer, sans cast
        System.out.println("D02 : " + n + " " + i + " " + a.value().getClass().getSimpleName());
        System.out.println("D03 : " + c.say() + " " + c.say("bonjour") + " " + a.say());
        System.out.println("D04 : " + a.visible() + " " + c.fixed());
        System.out.println("D05 : " + a + " " + c.equals(new C()));
    }
}

class A {
    String hello() {
        return "A";
    }

    Number value() {
        return 1.5;
    }

    String say() {
        return "a";
    }

    protected String visible() {
        return "A protected";
    }

    final String fixed() {
        return "final de A";
    }

    @Override
    public String toString() {
        return "objet " + getClass().getSimpleName();
    }
}

class B extends A {
    @Override
    String hello() {
        return "B+" + super.hello();
    }
}

class C extends B {
    @Override
    String hello() {
        return "C+" + super.hello();            // super : la version de B, qui appelle celle de A
    }

    @Override
    Integer value() {                           // type de retour covariant (sous-type de Number)
        return 7;
    }

    @Override
    String say() {
        return "c";
    }

    String say(String word) {                   // SURCHARGE : une nouvelle methode, pas une redefinition
        return "c dit " + word;
    }

    @Override
    public String visible() {                   // l'acces peut s'ELARGIR (protected -> public), jamais se reduire
        return "C public";
    }
}
