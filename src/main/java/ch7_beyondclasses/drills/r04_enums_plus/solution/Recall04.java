package ch7_beyondclasses.drills.r04_enums_plus.solution;

/**
 * SOLUTION du drill de rappel 4 - enums avec champs, constructeurs, methodes et corps par constante.
 */
public class Recall04 {

    public static void main(String[] args) {
        System.out.println("D01 : " + Coin.QUARTER.value() + " " + Coin.CENT.ordinal() + " " + Coin.valueOf("TEN").value() + " " + Coin.largest());
        System.out.println("D02 : " + Coin.change(68) + " | " + Coin.change(30));
        Light l = Light.RED;
        StringBuilder cycle = new StringBuilder();
        int seconds = 0;
        for (int i = 0; i < 4; i++) {
            cycle.append(l).append(' ');
            seconds += l.seconds();
            l = l.next();
        }
        System.out.println("D03 : " + cycle.toString().strip() + " " + seconds);
        System.out.println("D04 : [" + Light.RED.getClass().getSimpleName() + "] " + Light.RED.getDeclaringClass().getSimpleName() + " "
                + (Light.RED instanceof Timed));
        System.out.println("D05 : " + Light.GREEN.describe() + " " + Coin.FIVE);
    }
}

interface Timed {
    int seconds();

    default String describe() {
        return this + " " + seconds() + "s";
    }
}

enum Coin {
    CENT(1), FIVE(5), TEN(10), QUARTER(25);

    private final int value;

    // Le constructeur d'un enum est implicitement private ; il s'execute une fois par constante.
    Coin(int value) {
        this.value = value;
    }

    int value() {
        return value;
    }

    static Coin largest() {
        Coin[] all = values();
        return all[all.length - 1];
    }

    // Rendu de monnaie glouton : de la plus grosse piece a la plus petite.
    static String change(int amount) {
        StringBuilder sb = new StringBuilder();
        Coin[] all = values();
        for (int i = all.length - 1; i >= 0; i--) {
            int n = amount / all[i].value;
            if (n > 0) {
                sb.append(n).append('x').append(all[i]).append(' ');
                amount %= all[i].value;
            }
        }
        return sb.toString().strip();
    }

    @Override
    public String toString() {
        return name().toLowerCase();   // redefinir toString change l'affichage, pas name()
    }
}

// Chaque constante a un CORPS : elle redefinit la methode abstraite next().
enum Light implements Timed {
    RED(30) {
        @Override
        Light next() {
            return GREEN;
        }
    },
    GREEN(25) {
        @Override
        Light next() {
            return YELLOW;
        }
    },
    YELLOW(5) {
        @Override
        Light next() {
            return RED;
        }
    };

    private final int seconds;

    Light(int seconds) {
        this.seconds = seconds;
    }

    abstract Light next();

    @Override
    public int seconds() {
        return seconds;
    }
}
