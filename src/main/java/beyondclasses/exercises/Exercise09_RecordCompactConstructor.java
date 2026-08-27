package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 9 - Records : le constructeur compact, pour VALIDER et TRANSFORMER (niveau : difficile)
 * =======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un record accepte AVEUGLEMENT n'importe quelle valeur par defaut -
 * meme une temperature de -500 degres Celsius, physiquement
 * impossible (le zero absolu est a -273.15). Le "constructeur
 * compact" est une version ALLEGEE du constructeur canonique : on
 * n'ecrit PAS la liste des parametres entre parentheses (elle est
 * deja connue, c'est celle du record), et on n'ecrit JAMAIS
 * "this.celsius = celsius" a la fin (le compilateur le fait tout
 * seul, APRES le corps du constructeur compact) - on n'ecrit QUE ce
 * qu'on veut VERIFIER (et lancer une exception si invalide) ou
 * TRANSFORMER (reassigner le parametre AVANT qu'il soit range dans le
 * champ final).
 *
 *
 * ==================================================================
 * TODO 1 : le constructeur compact de Temperature
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * new Temperature(-300) doit lancer IllegalArgumentException (en
 * dessous du zero absolu, physiquement impossible). new
 * Temperature(36.26) doit ARRONDIR a 1 decimale : celsius() rend
 * 36.3, pas 36.26.
 *
 * -- Le plan --
 *
 *   1. Si celsius < -273.15 : lancer new
 *      IllegalArgumentException("Temperature en dessous du zero
 *      absolu : " + celsius).
 *   2. Sinon, reassigner celsius = Math.round(celsius * 10) / 10.0
 *      (arrondir a 1 decimale) - PAS de "this.celsius = ..." : on
 *      reassigne juste le PARAMETRE celsius, le compilateur se charge
 *      de le ranger dans le champ final apres coup, AVEC la valeur
 *      arrondie.
 *
 *
 * ==================================================================
 * TODO 2 : Temperature.toFahrenheit()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer celsius * 9.0 / 5.0 + 32.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : new Temperature(36.26).celsius() == 36.3.
 * new Temperature(0).toFahrenheit() == 32.0. new Temperature(-300)
 * lance IllegalArgumentException.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "record Temperature(double celsius) { Temperature { ... } }" :
 *     remarquez l'absence de parametres entre "Temperature" et
 *     l'accolade du constructeur compact - c'est LA syntaxe qui le
 *     distingue d'un vrai constructeur classique (qui, lui, aurait
 *     ecrit "Temperature(double celsius) { ... }").
 *   - Un record ne PEUT PAS declarer de champ d'instance
 *     SUPPLEMENTAIRE en dehors de ses composants (celsius est le
 *     SEUL champ possible ici) - seuls des champs static sont admis
 *     en plus, exactement comme illustre par le Bloc E de la quiz
 *     finale (Exercise14).
 */
public class Exercise09_RecordCompactConstructor {

    record Temperature(double celsius) {
        Temperature {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur compact");
        }

        double toFahrenheit() {
            throw new UnsupportedOperationException("TODO 2 : implementer toFahrenheit()");
        }
    }

    public static void main(String[] args) {
        ExerciseChecker.check("le constructeur compact ARRONDIT a 1 decimale",
                new Temperature(36.26).celsius() == 36.3);

        ExerciseChecker.check("toFahrenheit() sur 0 celsius == 32.0",
                new Temperature(0).toFahrenheit() == 32.0);

        boolean caught = false;
        try {
            new Temperature(-300);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        ExerciseChecker.check("le constructeur compact REJETTE une temperature sous le zero absolu", caught);

        ExerciseChecker.summary();
    }
}
