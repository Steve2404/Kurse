package ch7_beyondclasses.drills.r02_defaults.solution;

/**
 * SOLUTION du drill de rappel 2 - methodes default, static et private des interfaces.
 */
public class Recall02 {

    public static void main(String[] args) {
        System.out.println("D01 : " + new Duck().move() + " " + new Robot().move() + " " + new Fish().move());
        System.out.println("D02 : " + Walker.info() + " " + new Robot().describe());
        System.out.println("D03 : " + new Ticker().next() + " " + Counter.reset());
        System.out.println("D04 : " + new Frog().move() + " " + new Sloth().move());
        Walker w = new Duck();
        Swimmer s = new Duck();
        System.out.println("D05 : " + w.move() + " " + s.move());
    }
}

interface Walker {
    default String move() {
        return "marche";
    }

    default String describe() {
        return "je " + move() + label();
    }

    // static : appelee par Walker.info(), jamais heritee par les classes.
    static String info() {
        return "bipede";
    }

    // private : un outil reserve aux methodes de l'interface.
    private String label() {
        return " (walker)";
    }
}

interface Swimmer {
    default String move() {
        return "nage";
    }
}

// Deux defaults de meme signature : la classe DOIT redefinir, et peut appeler chacune avec X.super.
class Duck implements Walker, Swimmer {
    @Override
    public String move() {
        return Walker.super.move() + "+" + Swimmer.super.move();
    }
}

class Robot implements Walker {
}

class Fish implements Swimmer {
    @Override
    public String move() {
        return "fretille";
    }
}

interface Counter {
    default int next() {
        return step() * 2;
    }

    private int step() {
        return 3;
    }

    static int reset() {
        return zero();
    }

    private static int zero() {
        return 0;
    }
}

class Ticker implements Counter {
}

class Base {
    public String move() {
        return "classe";
    }
}

// Regle : la CLASSE gagne. Base.move() l'emporte sur la default de Swimmer.
class Frog extends Base implements Swimmer {
}

// Une interface peut redeclarer une default en abstraite : les classes doivent alors la fournir.
interface Lazy extends Walker {
    @Override
    String move();
}

class Sloth implements Lazy {
    @Override
    public String move() {
        return "dort";
    }
}
