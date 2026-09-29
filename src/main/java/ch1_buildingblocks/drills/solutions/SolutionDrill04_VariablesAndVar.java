package ch1_buildingblocks.drills.solutions;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.drills.exercises.Drill04_VariablesAndVar.
 */
public class SolutionDrill04_VariablesAndVar {

    static class Counter {
        static int created = 0;
        int count;

        Counter() {
            // created est static : une seule copie partagee par tous les Counter.
            created++;
        }

        void set(int count) {
            // Le parametre masque le champ : sans "this.", on s'affecterait a soi-meme.
            this.count = count;
        }
    }

    public static Object varLong() {
        // Le suffixe L fait choisir long a var (5 seul donnerait un int).
        var x = 5L;
        return x;
    }

    public static Object varChar() {
        // Des apostrophes simples : char, pas String.
        var c = 'x';
        return c;
    }

    public static Object varMixed() {
        // int + double -> double : var prend le type du resultat de l'expression.
        var d = 1 + 2.0;
        return d;
    }

    public static Object varText() {
        // Le type est fige : on ne pourra plus y mettre un nombre.
        var text = "caisse";
        return text;
    }

    public static int sumWithVar(int[] values) {
        // var fonctionne dans un for-each : v est un int.
        var total = 0;
        for (var v : values) {
            total += v;
        }
        return total;
    }

    public static int groupedSum() {
        // Declaration groupee : permise avec int, interdite avec var.
        int a = 1, b = 2, c = 3;
        return a + b + c;
    }

    public static int doubledFinal() {
        // final sur une locale : une seule affectation possible.
        final int x = 21;
        return x * 2;
    }

    public static String varVar() {
        // var n'est pas un mot-cle : c'est un nom de type reserve, utilisable comme nom de variable.
        var var = "ok";
        return var;
    }

    public static int blockSum(int n) {
        // doubled n'existe qu'a l'interieur de la boucle ; total vit dans toute la methode.
        int total = 0;
        for (int i = 1; i <= n; i++) {
            int doubled = i * 2;
            total += doubled;
        }
        return total;
    }

    public static String label(boolean vip) {
        // Declaree sans valeur : if ET else doivent chacun l'initialiser.
        String label;
        if (vip) {
            label = "VIP";
        } else {
            label = "standard";
        }
        return label;
    }
}
