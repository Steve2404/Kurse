package ch6_classdesign.drills.r05_hiding.solution;

/**
 * SOLUTION du drill de rappel 5 - masquer (hide) contre redefinir (override).
 */
public class Recall05 {

    public static void main(String[] args) {
        C c = new C();
        P asP = c;
        System.out.println("D01 : " + asP.name + " " + c.name + " " + asP.readName() + " " + c.both());
        System.out.println("D02 : " + asP.s() + " " + c.s() + " " + P.s() + " " + C.s());
        System.out.println("D03 : " + asP.i() + " " + c.i());
        System.out.println("D04 : " + c.reveal() + " " + c.ownSecret());
        System.out.println("D05 : " + asP.count + " " + c.count + " " + P.count + " " + C.count);
    }
}

class P {
    String name = "P";
    static int count = 1;

    static String s() {
        return "P.s";
    }

    String i() {
        return "P.i";
    }

    String readName() {
        return name;                            // dans P, name designe TOUJOURS le champ de P
    }

    private String secret() {
        return "secret de P";
    }

    String reveal() {
        return secret();                        // methode privee : pas de liaison dynamique
    }
}

class C extends P {
    String name = "C";                          // masque P.name : l'objet contient les deux champs
    static int count = 2;                       // masque aussi

    static String s() {                         // MASQUE P.s() : choix a la compilation (type de la reference)
        return "C.s";
    }

    @Override
    String i() {                                // REDEFINIT P.i() : choix a l'execution (type de l'objet)
        return "C.i";
    }

    private String secret() {                   // une autre methode, sans lien avec celle de P
        return "secret de C";
    }

    String ownSecret() {
        return secret();
    }

    String both() {
        return name + "/" + super.name + "/" + this.name;
    }
}
