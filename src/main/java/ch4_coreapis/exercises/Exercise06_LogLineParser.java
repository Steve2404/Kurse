package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 6 - Analyseur de lignes de journal : substring, indexOf, charAt, split (niveau : avance)
 * =================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un serveur ecrit son journal, une ligne par evenement, toujours
 * dans le meme format :
 *
 *   2024-03-10 07:42 WARN disque presque plein
 *   0         1         2
 *   0123456789012345678901234...
 *
 * La date occupe les index 0 a 9, l'heure 11 a 15, le niveau commence
 * en 17 et s'arrete au premier espace suivant, le message est tout le
 * reste. On decoupe la ligne en morceaux, puis on la remet en forme.
 *
 *
 * ==================================================================
 * TODO 1 : date(line)    et    TODO 2 : time(line)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   date -> substring(0, 10) = "2024-03-10" (l'index de fin est EXCLU)
 *   time -> substring(11, 16) = "07:42"
 *
 * -- Le plan --
 *
 *   1. Un seul substring par methode.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : level(line)    et    TODO 4 : message(line)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le niveau n'a pas toujours la meme longueur (INFO, WARN, ERROR). On
 * cherche l'espace qui le suit, A PARTIR de l'index 17.
 *
 * -- Essayons a la main --
 *
 *   "2024-03-10 09:15 ERROR connexion perdue" : indexOf(' ', 17) = 22
 *   level   -> substring(17, 22) = "ERROR"
 *   message -> substring(23)     = "connexion perdue"
 *
 * -- Le plan --
 *
 *   1. end = line.indexOf(' ', 17).
 *   2. level : substring(17, end) ; message : substring(end + 1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : isValid(line)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avant de decouper, on verifie que la ligne a la bonne forme, sinon
 * substring lancerait StringIndexOutOfBoundsException.
 *
 * -- Essayons a la main --
 *
 *   "2024-03-10 07:42 WARN disque presque plein" -> true
 *   "2024/03/10 07:42 WARN x"                    -> false (charAt(4) n'est pas '-')
 *   "2024-03-10 07:42 DEBUG x"                   -> false (niveau inconnu)
 *   "2024-03-10 07:42 INFO"                      -> false (pas d'espace apres le niveau)
 *   "trop court"                                 -> false
 *
 * -- Le plan --
 *
 *   1. Si null ou longueur < 18 -> false.
 *   2. charAt(4) et charAt(7) == '-', charAt(10) et charAt(16) == ' ', charAt(13) == ':'.
 *   3. end = indexOf(' ', 17) ; si end < 0 -> false.
 *   4. Le niveau vaut "INFO", "WARN" ou "ERROR".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui pour l'etape 4 : on reutilise level(line) du TODO 3.
 *
 *
 * ==================================================================
 * TODO 6 : capitalizeWords(text)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "disque presque plein" -> "Disque Presque Plein"
 *
 * -- Le plan --
 *
 *   1. Pour chaque mot de text.split(" ") : premiere lettre en majuscule
 *      (substring(0, 1).toUpperCase()) + le reste (substring(1)).
 *   2. Recoller avec des espaces (StringBuilder ou String.join).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : pretty(line)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "2024-03-10 07:42 WARN disque presque plein" -> "[WARN] 07:42 Disque Presque Plein"
 *   ligne invalide                                -> "?"
 *
 * -- Le plan --
 *
 *   1. Si !isValid(line) -> "?".
 *   2. "[" + level + "] " + time + " " + capitalizeWords(message).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Il assemble les boites des TODO 2 a 6 : c'est tout l'interet.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - substring(debut, fin) : fin EXCLUE ; substring(debut) : jusqu'au bout.
 *   - Comparer des String avec equals, jamais avec ==.
 */
public class Exercise06_LogLineParser {

    public static String date(String line) {
        throw new UnsupportedOperationException("TODO 1 : implementer date()");
    }

    public static String time(String line) {
        throw new UnsupportedOperationException("TODO 2 : implementer time()");
    }

    public static String level(String line) {
        throw new UnsupportedOperationException("TODO 3 : implementer level()");
    }

    public static String message(String line) {
        throw new UnsupportedOperationException("TODO 4 : implementer message()");
    }

    public static boolean isValid(String line) {
        throw new UnsupportedOperationException("TODO 5 : implementer isValid()");
    }

    public static String capitalizeWords(String text) {
        throw new UnsupportedOperationException("TODO 6 : implementer capitalizeWords()");
    }

    public static String pretty(String line) {
        throw new UnsupportedOperationException("TODO 7 : implementer pretty()");
    }

    public static void main(String[] args) {
        String warn = "2024-03-10 07:42 WARN disque presque plein";
        String error = "2024-03-10 09:15 ERROR connexion perdue";
        ExerciseChecker.check("date == 2024-03-10", date(warn).equals("2024-03-10"));
        ExerciseChecker.check("time == 07:42", time(warn).equals("07:42"));
        ExerciseChecker.check("level : WARN et ERROR", level(warn).equals("WARN") && level(error).equals("ERROR"));
        ExerciseChecker.check("message : \"connexion perdue\"", message(error).equals("connexion perdue"));
        ExerciseChecker.check("isValid : 1 bonne ligne, 4 mauvaises",
                isValid(warn) && !isValid("2024/03/10 07:42 WARN x") && !isValid("2024-03-10 07:42 DEBUG x")
                        && !isValid("2024-03-10 07:42 INFO") && !isValid("trop court") && !isValid(null));
        ExerciseChecker.check("capitalizeWords(\"disque presque plein\")", capitalizeWords("disque presque plein").equals("Disque Presque Plein"));
        ExerciseChecker.check("pretty : \"[WARN] 07:42 Disque Presque Plein\" et \"?\"",
                pretty(warn).equals("[WARN] 07:42 Disque Presque Plein") && pretty("n'importe quoi").equals("?"));

        ExerciseChecker.summary();
    }
}
