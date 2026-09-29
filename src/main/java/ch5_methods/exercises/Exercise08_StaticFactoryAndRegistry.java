package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 8 - Fabriques static, compteur partage, bloc static et registre : une classe Temperature (niveau : avance)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On construit une petite classe Temperature ou le static sert a
 * quelque chose de vrai :
 *   - un constructeur private et des FABRIQUES static (ofCelsius,
 *     ofFahrenheit) qui verifient les valeurs ;
 *   - un compteur static partage par tous les objets (combien en a-t-on cree ?) ;
 *   - des constantes static final (FREEZING, BOILING) creees UNE fois
 *     dans un bloc static, au chargement de la classe ;
 *   - des methodes static utilitaires (warmest, average) et des methodes
 *     d'instance qui utilisent le static sans probleme.
 *
 * -- Rappels verifies avec javac 17 --
 *
 *   static final int X;   (jamais initialise) -> error: variable X not initialized in the default constructor
 *   Le bloc static s'execute UNE seule fois, au premier usage de la classe, avant tout objet.
 *
 *
 * ==================================================================
 * TODO 1 : ofCelsius(celsius)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ofCelsius(21.5).getCelsius() == 21.5
 *   ofCelsius(-300) -> IllegalArgumentException("sous le zero absolu")  (limite : -273.15)
 *
 * -- Le plan --
 *
 *   1. Si celsius < ABSOLUTE_ZERO -> exception.
 *   2. Rendre new Temperature(celsius).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : ofFahrenheit(fahrenheit)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   212 F -> (212 - 32) * 5 / 9 = 100 C       32 F -> 0 C
 *
 * -- Le plan --
 *
 *   1. Rendre ofCelsius((fahrenheit - 32) * 5 / 9) (la verification est reutilisee).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : ofCelsius (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : toFahrenheit()    [methode d'instance]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   100 C -> 100 * 9 / 5 + 32 = 212.0
 *
 * -- Le plan --
 *
 *   1. Rendre celsius * 9 / 5 + 32.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : isFreezing()    [instance qui lit une constante static]
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Rendre celsius <= FREEZING.getCelsius().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : warmest(temps...)    et    TODO 6 : average(temps...)    [static + varargs]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   warmest(10 C, 35 C, 20 C) -> celle a 35 C     average(10 C, 20 C) -> une NOUVELLE Temperature a 15 C
 *   warmest() ou average() -> IllegalArgumentException("aucune temperature")
 *
 * -- Le plan --
 *
 *   1. Si temps.length == 0 -> exception.
 *   2. warmest : garder la plus chaude. average : somme des celsius / nombre, puis ofCelsius(...).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : ofCelsius (TODO 1) pour average.
 *
 *
 * ==================================================================
 * TODO 7 : created()    [lire le compteur static]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le constructeur (deja ecrit) fait count++. Attention : FREEZING et
 * BOILING, creees dans le bloc static, comptent aussi ! main() mesure
 * donc une DIFFERENCE avant/apres.
 *
 * -- Le plan --
 *
 *   1. Rendre count.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une methode static appelle le constructeur private : elle est DANS la classe.
 *   - Une methode d'instance peut lire FREEZING, count, ABSOLUTE_ZERO sans rien ecrire devant.
 */
public class Exercise08_StaticFactoryAndRegistry {

    public static final class Temperature {
        public static final double ABSOLUTE_ZERO = -273.15;
        public static final Temperature FREEZING;
        public static final Temperature BOILING;
        public static final List<String> LOADING_LOG = new ArrayList<>();
        private static int count;

        static {
            FREEZING = new Temperature(0);
            BOILING = new Temperature(100);
            LOADING_LOG.add("bloc static execute");
        }

        private final double celsius;

        private Temperature(double celsius) {
            this.celsius = celsius;
            count++;
        }

        public double getCelsius() {
            return celsius;
        }

        public static Temperature ofCelsius(double celsius) {
            throw new UnsupportedOperationException("TODO 1 : implementer ofCelsius()");
        }

        public static Temperature ofFahrenheit(double fahrenheit) {
            throw new UnsupportedOperationException("TODO 2 : implementer ofFahrenheit()");
        }

        public double toFahrenheit() {
            throw new UnsupportedOperationException("TODO 3 : implementer toFahrenheit()");
        }

        public boolean isFreezing() {
            throw new UnsupportedOperationException("TODO 4 : implementer isFreezing()");
        }

        public static Temperature warmest(Temperature... temps) {
            throw new UnsupportedOperationException("TODO 5 : implementer warmest()");
        }

        public static Temperature average(Temperature... temps) {
            throw new UnsupportedOperationException("TODO 6 : implementer average()");
        }

        public static int created() {
            throw new UnsupportedOperationException("TODO 7 : implementer created()");
        }
    }

    public static void main(String[] args) {
        Temperature room = Temperature.ofCelsius(21.5);
        ExerciseChecker.check("ofCelsius(21.5)", room.getCelsius() == 21.5);
        ExerciseChecker.check("ofFahrenheit : 212 F -> 100 C, 32 F -> 0 C",
                Temperature.ofFahrenheit(212).getCelsius() == 100.0 && Temperature.ofFahrenheit(32).getCelsius() == 0.0);
        ExerciseChecker.check("toFahrenheit : 100 C -> 212.0", Temperature.BOILING.toFahrenheit() == 212.0);
        ExerciseChecker.check("isFreezing : -5 oui, 21.5 non", Temperature.ofCelsius(-5).isFreezing() && !room.isFreezing());

        Temperature a = Temperature.ofCelsius(10);
        Temperature b = Temperature.ofCelsius(35);
        Temperature c = Temperature.ofCelsius(20);
        ExerciseChecker.check("warmest -> celle a 35 C (le MEME objet)", Temperature.warmest(a, b, c) == b);
        ExerciseChecker.check("average(10, 20) -> 15 C", Temperature.average(a, c).getCelsius() == 15.0);

        int before = Temperature.created();
        Temperature.ofCelsius(1);
        Temperature.ofFahrenheit(50);
        ExerciseChecker.check("created() compte chaque objet (+2)", Temperature.created() - before == 2);

        ExerciseChecker.check("refus : sous le zero absolu, warmest() vide",
                "sous le zero absolu".equals(errorOf(() -> Temperature.ofCelsius(-300)))
                        && "aucune temperature".equals(errorOf(Temperature::warmest)));
        ExerciseChecker.check("le bloc static ne s'est execute qu'UNE fois", Temperature.LOADING_LOG.size() == 1);

        ExerciseChecker.summary();
    }

    // Deja ecrit : le message de l'IllegalArgumentException lancee, ou null.
    private static String errorOf(Runnable action) {
        try {
            action.run();
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
