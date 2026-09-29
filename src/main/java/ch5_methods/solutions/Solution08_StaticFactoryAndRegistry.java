package ch5_methods.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise08_StaticFactoryAndRegistry.
 */
public class Solution08_StaticFactoryAndRegistry {

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
            // La fabrique valide AVANT d'appeler le constructeur private.
            if (celsius < ABSOLUTE_ZERO) {
                throw new IllegalArgumentException("sous le zero absolu");
            }
            return new Temperature(celsius);
        }

        public static Temperature ofFahrenheit(double fahrenheit) {
            // On convertit puis on passe par ofCelsius : une seule validation a maintenir.
            return ofCelsius((fahrenheit - 32) * 5 / 9);
        }

        public double toFahrenheit() {
            // Methode d'instance : elle lit le champ de CET objet.
            return celsius * 9 / 5 + 32;
        }

        public boolean isFreezing() {
            // Une methode d'instance lit une constante static sans rien ecrire devant.
            return celsius <= FREEZING.getCelsius();
        }

        public static Temperature warmest(Temperature... temps) {
            // Static + varargs : aucun objet "courant" n'est necessaire.
            requireSome(temps);
            Temperature best = temps[0];
            for (Temperature t : temps) {
                if (t.celsius > best.celsius) {
                    best = t;
                }
            }
            return best;
        }

        public static Temperature average(Temperature... temps) {
            // Le resultat est une NOUVELLE Temperature, creee par la fabrique.
            requireSome(temps);
            double sum = 0;
            for (Temperature t : temps) {
                sum += t.celsius;
            }
            return ofCelsius(sum / temps.length);
        }

        private static void requireSome(Temperature[] temps) {
            // Petite boite : le meme refus pour warmest et average.
            if (temps.length == 0) {
                throw new IllegalArgumentException("aucune temperature");
            }
        }

        public static int created() {
            // Le compteur static est partage par tous les objets (y compris FREEZING et BOILING).
            return count;
        }
    }
}
