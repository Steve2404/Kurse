package ch2_operators.projects.p04_sensors.solution;

/**
 * SOLUTION du projet 4 - une conception possible.
 */
public class Sensors {

    static int frames;
    static int controls;

    // Chaque controle COMPTE son execution : c'est la preuve visible du court-circuit.
    static boolean temperatureOk(Frame f) {
        controls++;
        return f.temperature >= -20 && f.temperature <= 50;
    }

    static boolean humidityOk(Frame f) {
        controls++;
        return f.humidity >= 0 && f.humidity <= 100;
    }

    static boolean batteryOk(Frame f) {
        controls++;
        return f.battery > 10;
    }

    static boolean signatureOk(Frame f) {
        controls++;
        return f.signed;
    }

    static String report(Frame f) {
        controls = 0;
        // && : s'arrete au premier false ; les controles suivants ne s'executent pas.
        boolean lazy = temperatureOk(f) && humidityOk(f) && batteryOk(f) && signatureOk(f);
        int lazyControls = controls;
        controls = 0;
        // & sur des booleens : evalue TOUJOURS les deux cotes (meme resultat, plus de travail).
        boolean eager = temperatureOk(f) & humidityOk(f) & batteryOk(f) & signatureOk(f);
        int eagerControls = controls;
        // ^ : vrai si EXACTEMENT un des deux est vrai.
        boolean oneClimateFault = !temperatureOk(f) ^ !humidityOk(f);
        boolean alert = f.temperature > 45 || f.battery < 20;
        return "trame #" + ++frames + " (" + f.temperature + " C, " + f.humidity + " %, batterie " + f.battery + " %, "
                + (f.signed ? "signee" : "non signee") + ") : " + (lazy ? "valide" : "rejetee")
                + " | && " + lazyControls + " controle(s), & " + eagerControls + " | meme verdict " + (lazy == eager)
                + " | un seul defaut climat " + oneClimateFault + " | alerte " + (alert ? "oui" : "non");
    }

    public static void main(String[] args) {
        System.out.println(report(new Frame(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Integer.parseInt(args[2]), Boolean.parseBoolean(args[3]))));
        System.out.println(report(new Frame(70, 40, 90, true)));
        System.out.println(report(new Frame(25, 120, 5, false)));
        System.out.println(report(new Frame(-30, 150, 50, true)));
        System.out.println(report(new Frame(10, 10, 11, false)));

        // Incrementations : la valeur rendue (avant ou apres) ET l'effet sur la variable.
        int id = 5;
        int a = id++ + ++id;   // 5 (puis id=6) + 7 (id=7)
        int b = id-- - --id;   // 7 (puis id=6) - 5 (id=5)
        System.out.println("increments : a = " + a + ", b = " + b + ", id = " + id);

        // Une affectation est une EXPRESSION : elle a une valeur.
        int x;
        int y;
        x = y = 4;
        int z = (x += 2) * x;  // x devient 6 avant la multiplication : 6 * 6
        boolean flag = false;
        String trap = (flag = true) ? "affectation (vraie)" : "comparaison";
        System.out.println("affectations : x = " + x + ", y = " + y + ", z = " + z + ", " + trap);

        // == sur des references : meme OBJET, pas meme contenu.
        Frame f1 = new Frame(20, 50, 60, true);
        Frame f2 = new Frame(20, 50, 60, true);
        Frame f3 = f1;
        System.out.println("references : f1 == f2 " + (f1 == f2) + ", f1 == f3 " + (f1 == f3) + ", f1 != f2 " + (f1 != f2));

        Object number = Integer.valueOf(42);
        Object text = "42";
        Object nothing = null;
        System.out.println("instanceof : Integer " + (number instanceof Integer) + ", Number " + (number instanceof Number)
                + ", texte Integer " + (text instanceof Integer) + ", null Object " + (nothing instanceof Object));
        System.out.println("trames analysees : " + frames);
    }
}

class Frame {
    int temperature;
    int humidity;
    int battery;
    boolean signed;

    Frame(int temperature, int humidity, int battery, boolean signed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.battery = battery;
        this.signed = signed;
    }
}
