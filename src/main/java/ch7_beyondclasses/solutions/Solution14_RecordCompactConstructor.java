package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise14_RecordCompactConstructor.
 */
public class Solution14_RecordCompactConstructor {

    record Temperature(double celsius) {
        Temperature {
            // Compact : on valide puis on arrondit le PARAMETRE ; javac l'affecte au champ a la fin.
            if (celsius < -273.15) {
                throw new IllegalArgumentException("Temperature en dessous du zero absolu : " + celsius);
            }
            celsius = Math.round(celsius * 10) / 10.0;
        }

        double toFahrenheit() {
            // Methode de calcul en lecture seule sur le composant.
            return celsius * 9.0 / 5.0 + 32;
        }
    }
}
