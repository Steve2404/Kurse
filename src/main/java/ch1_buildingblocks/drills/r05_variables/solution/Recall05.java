package ch1_buildingblocks.drills.r05_variables.solution;

/**
 * SOLUTION du drill de rappel 5 - variables, identificateurs, var, portee.
 */
public class Recall05 {

    static int counter;
    int level = 1;

    public static void main(String[] args) {
        // Identificateurs valides : lettre, $ ou _ en tete, puis chiffres permis ; pas de mot-cle, pas de _ seul.
        int $price = 5;
        int _count = 2;
        int Integer = 3;
        int total$_2 = $price * _count + Integer;
        System.out.println("D01 : " + $price + " " + _count + " " + Integer + " " + total$_2);

        // Plusieurs declarations sur une ligne : seule c est initialisee ici.
        int a, b, c = 7;
        a = 1;
        b = a + c;
        System.out.println("D02 : " + a + " " + b + " " + c);

        var text = "var";
        var number = 10;
        var pi = 3.14;
        var big = 5_000_000_000L;
        System.out.println("D03 : " + text + " " + number + " " + pi + " " + big);

        final int limit = 3;
        final String label;
        label = "affecte une fois";
        System.out.println("D04 : " + limit + " " + label);

        // Le champ static a une valeur par defaut ; le champ d'instance n'existe qu'avec un objet.
        Recall05 r = new Recall05();
        System.out.println("D05 : " + counter + " " + r.level);

        int level = 9;
        System.out.println("D06 : " + level + " " + r.level + " " + r.shadow());

        {
            int inner = level + 1;
            System.out.println("D07 : " + inner);
        }
        int inner = 100;
        System.out.println("D08 : " + inner);
    }

    int shadow() {
        int level = 5;
        return level + this.level;
    }
}
