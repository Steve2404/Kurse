package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise09_RecordCompactConstructor.
 */
public class Solution09_RecordCompactConstructor {

    record Temperature(double celsius) {
        Temperature {
            if (celsius < -273.15) {
                throw new IllegalArgumentException("Temperature en dessous du zero absolu : " + celsius);
            }
            celsius = Math.round(celsius * 10) / 10.0;
        }

        double toFahrenheit() {
            return celsius * 9.0 / 5.0 + 32;
        }
    }
}
