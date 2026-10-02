package ch1_buildingblocks.drills.r06_init.solution;

/**
 * SOLUTION du drill de rappel 6 - ordre d'initialisation et ramasse-miettes.
 */
public class Recall06 {

    public static void main(String[] args) {
        System.out.println("D01 : " + new Egg().trace);
        System.out.println("D02 : " + new Egg(3).trace);
        Egg e = new Egg();
        System.out.println("D03 : " + e.late + " " + e.copyOfLate);
        System.out.println("D04 : " + Egg.made);
        Egg f = e;
        e = null;
        // f designe encore l'objet : il n'est pas eligible.
        System.out.println("D05 : " + f.size);
    }
}

class Egg {
    static int made;
    String trace = "champ";

    {
        trace = trace + " > bloc1";
    }

    int copyOfLate = readLate();
    int late = 8;
    int size = 1;

    {
        trace = trace + " > bloc2";
    }

    Egg() {
        trace = trace + " > constructeur()";
        made = made + 1;
    }

    Egg(int size) {
        trace = trace + " > constructeur(" + size + ")";
        this.size = size;
        made = made + 1;
    }

    int readLate() {
        return late;
    }
}
