package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 3 - Les methodes String les plus utilisees, et le piege classique de substring() (niveau : moyen)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les index d'un String commencent TOUJOURS a 0 (le 1er caractere est
 * a l'index 0, pas 1). substring(debut, fin) est le piege le plus
 * classique de l'examen : "fin" est EXCLU - on recupere tout depuis
 * "debut" JUSQU'A JUSTE AVANT "fin", jamais "fin" lui-meme (un peu
 * comme demander "de la page 7 a la page 12" en musee : tu visites
 * les salles 7, 8, 9, 10, 11 - la salle 12 n'est PAS incluse, elle
 * marque juste "ou s'arreter"). indexOf(car) cherche la PREMIERE
 * position d'un caractere, et rend -1 s'il ne le trouve JAMAIS.
 *
 *
 * ==================================================================
 * TODO 1 : extractWorld(text)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * "Hello, World!" : H(0) e(1) l(2) l(3) o(4) ,(5) espace(6) W(7)
 * o(8) r(9) l(10) d(11) !(12). "World" commence a l'index 7 et finit
 * juste avant l'index 12 (le "!").
 *
 * -- Le plan --
 *
 *   1. Renvoyer text.substring(7, 12).
 *
 *
 * ==================================================================
 * TODO 2 : firstWord(sentence)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * "Bonjour le monde" : le 1er espace est a l'index 7 - le premier mot
 * va donc de l'index 0 (inclus) a l'index 7 (exclu) : "Bonjour". Si
 * AUCUN espace n'existe ("Solo"), toute la phrase est UN SEUL mot.
 *
 * -- Le plan --
 *
 *   1. Chercher la position du 1er espace avec sentence.indexOf(' ').
 *   2. Si cette position vaut -1 (aucun espace trouve) : renvoyer
 *      sentence tel quel.
 *   3. Sinon : renvoyer sentence.substring(0, position).
 *
 *
 * ==================================================================
 * TODO 3 : cleanedTrim(text)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer text.strip() (enleve les espaces - et autres
 *      "blancs" Unicode - au debut ET a la fin, jamais au milieu).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : extractWorld("Hello, World!") == "World".
 * firstWord("Bonjour le monde") == "Bonjour". firstWord("Solo") ==
 * "Solo" (pas d'espace du tout). cleanedTrim("  Steve  ") ==
 * "Steve".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - strip() (depuis Java 11) est prefere a trim() : il comprend
 *     TOUS les caracteres Unicode "blancs", pas seulement l'espace
 *     ASCII classique.
 */
public class Exercise03_CommonStringMethods {

    public static String extractWorld(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer extractWorld()");
    }

    public static String firstWord(String sentence) {
        throw new UnsupportedOperationException("TODO 2 : implementer firstWord()");
    }

    public static String cleanedTrim(String text) {
        throw new UnsupportedOperationException("TODO 3 : implementer cleanedTrim()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("extractWorld() coupe JUSTE AVANT l'index de fin",
                extractWorld("Hello, World!").equals("World"));

        ExerciseChecker.check("firstWord() avec espace", firstWord("Bonjour le monde").equals("Bonjour"));
        ExerciseChecker.check("firstWord() SANS espace (indexOf == -1)", firstWord("Solo").equals("Solo"));

        ExerciseChecker.check("cleanedTrim() enleve les espaces au debut/fin",
                cleanedTrim("  Steve  ").equals("Steve"));

        ExerciseChecker.summary();
    }
}
