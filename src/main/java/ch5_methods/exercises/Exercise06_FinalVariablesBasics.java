package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

/**
 * EXERCICE 6 - final sur une variable locale, un champ d'instance, et un champ static (niveau : moyen)
 * ==============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * final peut se poser sur 3 sortes de variables DIFFERENTES, avec
 * TOUJOURS la meme regle de fond ("une fois affectee, plus jamais
 * modifiable"), mais des CONSEQUENCES differentes :
 *   - une variable LOCALE final (dans une methode) : juste une
 *     protection pour TOI-MEME, pour eviter une reaffectation
 *     accidentelle plus loin dans la meme methode.
 *   - un champ D'INSTANCE final (voir aussi le chapitre "Class
 *     Design") : CHAQUE objet a SA PROPRE valeur, figee des la fin du
 *     constructeur, mais 2 objets DIFFERENTS peuvent avoir 2 valeurs
 *     DIFFERENTES.
 *   - un champ STATIC final : UNE SEULE valeur, partagee par TOUTE la
 *     classe (comme un champ static ordinaire, voir Exercise03), mais
 *     qui, elle, ne changera plus JAMAIS une fois la classe chargee -
 *     c'est LA vraie facon d'ecrire une CONSTANTE en Java (souvent en
 *     MAJUSCULES par convention, comme MAX_RETRIES ci-dessous).
 *
 *
 * ==================================================================
 * TODO : Config.describe()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec MAX_RETRIES = 3 (static final, partage) et name = "Prod"
 * (instance final, propre a CET objet), describe() doit rendre
 * "Prod : max 3 tentatives, double = 6" (localDoubled, une variable
 * LOCALE final, deja calculee juste au-dessus).
 *
 * -- Le plan --
 *
 *   1. Renvoyer name + " : max " + MAX_RETRIES + " tentatives, double = " + localDoubled
 *      - les 3 sortes de final (locale, instance, static) sont
 *      TOUTES lisibles directement, sans aucune difference de
 *      syntaxe entre elles.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : new Config("Prod").describe() ==
 * "Prod : max 3 tentatives, double = 6". new Config("Test").describe()
 * == "Test : max 3 tentatives, double = 6" (MAX_RETRIES, static,
 * reste le MEME pour les 2 objets ; name, instance, differe).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "final int localDoubled = MAX_RETRIES * 2;" (deja ecrit plus
 *     bas, avant le TODO) : une variable locale final DOIT recevoir
 *     sa valeur des sa declaration ou au plus tard avant sa premiere
 *     utilisation, exactement comme un champ final "blank" (voir
 *     Class Design) - ici, elle est directement initialisee.
 */
public class Exercise06_FinalVariablesBasics {

    static class Config {
        static final int MAX_RETRIES = 3;

        final String name;

        Config(String name) {
            this.name = name;
        }

        String describe() {
            final int localDoubled = MAX_RETRIES * 2;
            throw new UnsupportedOperationException("TODO : implementer describe()");
        }
    }

    public static void main(String[] args) {
        ExerciseChecker.check("describe() lit local final + instance final + static final",
                new Config("Prod").describe().equals("Prod : max 3 tentatives, double = 6"));

        ExerciseChecker.check("MAX_RETRIES (static final) est PARTAGE, name (instance final) est PROPRE a l'objet",
                new Config("Test").describe().equals("Test : max 3 tentatives, double = 6"));

        ExerciseChecker.summary();
    }
}
