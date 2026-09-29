package ch5_methods.drills.solutions;

import ch5_methods.drills.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.drills.exercises.Drill02_StaticAndFinal.
 */
public class SolutionDrill02_StaticAndFinal {

    private static int counter;

    public static final class Badge {
        private final String name;

        private Badge(String name) {
            this.name = name;
        }

        public static Badge of(String name) {
            return new Badge(name);
        }

        public static String kind() {
            return "badge";
        }

        public String text() {
            return "[" + name + "]";
        }
    }

    public static int nextId() {
        // Le compteur static est partage : chaque appel voit la valeur precedente.
        return ++counter;
    }

    public static void resetCounter() {
        // Une methode static peut ecrire un champ static sans objet.
        counter = 0;
    }

    public static int maxPlayers() {
        // Une constante static final se lit par le nom de sa classe.
        return Team.MAX_PLAYERS;
    }

    public static double circleArea(double r) {
        // PI vient de l'import static : pas de "Math." devant.
        return PI * r * r;
    }

    public static double hypotenuse(double a, double b) {
        // sqrt importe en static, comme PI.
        return sqrt(a * a + b * b);
    }

    public static String badge(String name) {
        // Le constructeur est private : on passe par la fabrique static of().
        return Badge.of(name).text();
    }

    @SuppressWarnings("static")
    public static String kindThroughNull() {
        // Appel static via une variable : seul le TYPE compte, null n'est jamais utilise.
        Badge nothing = null;
        return nothing.kind();
    }

    public static int finalArrayContent() {
        // final bloque la VARIABLE (a = autre tableau interdit), pas le contenu du tableau.
        final int[] a = {1, 2};
        a[0] = 9;
        return a[0];
    }

    public static IntUnaryOperator addBase(int base) {
        // base n'est jamais reassigne : capturable.
        return x -> x + base;
    }

    public static int copiesInLoop() {
        // copy est une nouvelle variable par tour ; i, reassigne par i++, ne serait pas capturable.
        List<IntSupplier> suppliers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int copy = i;
            suppliers.add(() -> copy);
        }
        int sum = 0;
        for (IntSupplier s : suppliers) {
            sum += s.getAsInt();
        }
        return sum;
    }

    public static int counterInLambda() {
        // La reference box est effectivement finale ; son contenu peut changer.
        int[] box = {0};
        Runnable r = () -> box[0]++;
        r.run();
        r.run();
        r.run();
        return box[0];
    }

    public static String clubUpper() {
        // Une constante String reste un objet : on peut appeler ses methodes.
        return Team.CLUB.toUpperCase();
    }
}
