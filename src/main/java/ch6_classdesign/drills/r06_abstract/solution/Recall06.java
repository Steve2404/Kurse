package ch6_classdesign.drills.r06_abstract.solution;

/**
 * SOLUTION du drill de rappel 6 - les classes abstraites.
 */
public class Recall06 {

    public static void main(String[] args) {
        System.out.println("D01 : " + new Guitar().play(1) + " " + new Violin().play(2));
        System.out.println("D02 : " + new Drum().play(3));
        Instrument[] band = {new Guitar(), new Drum(), new Violin()};
        int strings = 0;
        StringBuilder names = new StringBuilder();
        for (Instrument i : band) {
            names.append(i.getName()).append(' ');
            if (i instanceof Strings s) {
                strings += s.strings();
            }
        }
        System.out.println("D03 : " + names.toString().strip() + " " + strings);
        System.out.println("D04 : " + Instrument.count + " " + Instrument.describeAll());
        Instrument v = new Violin();
        System.out.println("D05 : " + (v instanceof Strings) + " " + (v instanceof Instrument) + " " + v.getClass().getSimpleName());
        Marker m = new Concrete();
        System.out.println("D06 : " + m.tag());
    }
}

abstract class Instrument {
    static int count;
    private final String name;

    // Une classe abstraite a des constructeurs : ils s'executent via super(...) des sous-classes.
    protected Instrument(String name) {
        this.name = name;
        count++;
    }

    abstract String sound();

    String getName() {
        return name;
    }

    // Methode concrete qui s'appuie sur une methode abstraite.
    String play(int times) {
        return name + ":" + sound().repeat(times);
    }

    // Une methode static dans une classe abstraite : appelable sans aucun objet.
    static String describeAll() {
        return "orchestre de " + count;
    }
}

// Abstraite elle aussi : elle implemente sound() mais declare une NOUVELLE methode abstraite.
abstract class Strings extends Instrument {
    protected Strings(String name) {
        super(name);
    }

    abstract int strings();

    @Override
    String sound() {
        return "~".repeat(strings());
    }
}

class Guitar extends Strings {
    Guitar() {
        super("guitare");
    }

    @Override
    int strings() {
        return 6;
    }
}

class Violin extends Strings {
    Violin() {
        super("violon");
    }

    @Override
    int strings() {
        return 4;
    }
}

class Drum extends Instrument {
    Drum() {
        super("tambour");
    }

    @Override
    String sound() {
        return "boum";
    }
}

// Abstraite sans aucune methode abstraite : permis, et toujours pas instanciable.
abstract class Marker {
    String tag() {
        return "marque par " + getClass().getSimpleName();
    }
}

class Concrete extends Marker {
}
