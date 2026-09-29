package ch1_buildingblocks.drills.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * DRILL 04 - Variables : var, declarations, portee, masquage, static, initialisation
 * ==================================================================================
 *
 * Mode d'emploi : voir Drill01_WrapperApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Pour les TODO 1 a 4 : declare la variable avec var, puis rends-la
 * (le type Object de retour la met en boite : le test verifie le type
 * que var a choisi).
 *
 *
 * -- Les TODO (regle visee entre crochets) --
 *
 * TODO 1  : varLong()          [var + suffixe L] valeur 5 -> doit etre un Long.
 * TODO 2  : varChar()          [var + char] 'x' -> un Character.
 * TODO 3  : varMixed()         [var + int + double] 1 + 2.0 -> un Double valant 3.0.
 * TODO 4  : varText()          [var + String] "caisse" -> un String.
 * TODO 5  : sumWithVar(values) [for (var v : values)]
 * TODO 6  : groupedSum()       [declaration groupee int a = 1, b = 2, c = 3] -> 6.
 * TODO 7  : doubledFinal()     [variable locale final] final int x = 21 -> 42.
 * TODO 8  : varVar()           [var comme NOM de variable] var var = "ok" -> "ok".
 * TODO 9  : blockSum(n)        [variable declaree DANS la boucle] somme de i * 2 pour i de 1 a n.
 * TODO 10 : Counter.set(count) [masquage : this.count = count]
 * TODO 11 : Counter()          [compteur static partage] chaque new augmente created.
 * TODO 12 : label(vip)         [initialisation sur tous les chemins] String label; puis if/else.
 *                              vip -> "VIP", sinon "standard".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   var : variable LOCALE seulement, initialisee sur la meme ligne, pas avec null seul,
 *         pas de declaration groupee, pas de tableau {1, 2}, type fige a la compilation.
 *         "var" reste utilisable comme nom de variable.
 *   Portees : locale (bloc/methode), d'instance (un par objet), de classe (static, partagee)
 *   Masquage : un parametre du meme nom qu'un champ le cache -> this.champ = parametre
 *   Locale : aucune valeur par defaut ; doit etre initialisee sur TOUS les chemins avant lecture
 *   int a = 1, b = 2; : permis avec un type explicite
 * ---------------------------------------------------------------------
 */
public class Drill04_VariablesAndVar {

    static class Counter {
        static int created = 0;
        int count;

        Counter() {
            throw new UnsupportedOperationException("TODO 11 : implementer le constructeur Counter()");
        }

        void set(int count) {
            throw new UnsupportedOperationException("TODO 10 : implementer set()");
        }
    }

    public static Object varLong() {
        throw new UnsupportedOperationException("TODO 1 : implementer varLong()");
    }

    public static Object varChar() {
        throw new UnsupportedOperationException("TODO 2 : implementer varChar()");
    }

    public static Object varMixed() {
        throw new UnsupportedOperationException("TODO 3 : implementer varMixed()");
    }

    public static Object varText() {
        throw new UnsupportedOperationException("TODO 4 : implementer varText()");
    }

    public static int sumWithVar(int[] values) {
        throw new UnsupportedOperationException("TODO 5 : implementer sumWithVar()");
    }

    public static int groupedSum() {
        throw new UnsupportedOperationException("TODO 6 : implementer groupedSum()");
    }

    public static int doubledFinal() {
        throw new UnsupportedOperationException("TODO 7 : implementer doubledFinal()");
    }

    public static String varVar() {
        throw new UnsupportedOperationException("TODO 8 : implementer varVar()");
    }

    public static int blockSum(int n) {
        throw new UnsupportedOperationException("TODO 9 : implementer blockSum()");
    }

    public static String label(boolean vip) {
        throw new UnsupportedOperationException("TODO 12 : implementer label()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  varLong() est un Long valant 5", varLong() instanceof Long && varLong().equals(5L));
        ExerciseChecker.check("2  varChar() est un Character 'x'", varChar() instanceof Character && varChar().equals('x'));
        ExerciseChecker.check("3  varMixed() est un Double valant 3.0", varMixed() instanceof Double && varMixed().equals(3.0));
        ExerciseChecker.check("4  varText() est le String \"caisse\"", "caisse".equals(varText()));
        ExerciseChecker.check("5  sumWithVar([3, 4, 5]) == 12", sumWithVar(new int[]{3, 4, 5}) == 12);
        ExerciseChecker.check("6  groupedSum() == 6", groupedSum() == 6);
        ExerciseChecker.check("7  doubledFinal() == 42", doubledFinal() == 42);
        ExerciseChecker.check("8  varVar() == \"ok\"", varVar().equals("ok"));
        ExerciseChecker.check("9  blockSum(3) == 12 (2 + 4 + 6), blockSum(0) == 0", blockSum(3) == 12 && blockSum(0) == 0);

        Counter.created = 0;
        Counter first = new Counter();
        Counter second = new Counter();
        first.set(5);
        ExerciseChecker.check("10 set(5) change le champ du 1er seulement", first.count == 5 && second.count == 0);
        ExerciseChecker.check("11 2 new Counter() -> created == 2", Counter.created == 2);
        ExerciseChecker.check("12 label(true) == VIP, label(false) == standard",
                label(true).equals("VIP") && label(false).equals("standard"));

        ExerciseChecker.summary();
    }
}
