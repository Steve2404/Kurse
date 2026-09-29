package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 9 - StringBuilder a fond : insert, delete, deleteCharAt, replace, setCharAt, setLength (niveau : difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   new StringBuilder("animals").insert(7, "-").insert(0, "-").insert(4, "-") -> "-ani-mals-"
 *   new StringBuilder("abcdef").delete(1, 3)           -> "adef"   (fin EXCLUE)
 *   new StringBuilder("abc").delete(1, 100)            -> "a"      (fin trop grande : acceptee)
 *   new StringBuilder("abc").insert(5, "x")            -> StringIndexOutOfBoundsException: offset 5, length 3
 *   new StringBuilder("abc").deleteCharAt(3)           -> StringIndexOutOfBoundsException: index 3, length 3
 *   new StringBuilder("pigeon dirty").replace(3, 6, "sty") -> "pigsty dirty"
 *   sb = "hello" ; sb.setCharAt(0, 'J') ; sb.setLength(3)  -> "Jel"
 *   new StringBuilder("0123456789").delete(2, 4).insert(2, "X").reverse().deleteCharAt(0) -> "87654X10"
 *   sb.substring(1) NE modifie PAS sb (il rend un String)
 *
 *
 * ==================================================================
 * TODO 1 : reverseWords(sentence)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On range les mots dans l'ordre inverse. Astuce : au lieu d'ajouter
 * chaque mot a la FIN, on l'insere au DEBUT (insert(0, ...)).
 *
 * -- Essayons a la main --
 *
 *   "un deux trois" : "un" -> "deux un" -> "trois deux un"
 *
 * -- Le plan --
 *
 *   1. sb vide ; pour chaque mot : si sb n'est pas vide, insert(0, ' ') ; puis insert(0, mot).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : removeVowels(sb)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On enleve les voyelles DIRECTEMENT dans le StringBuilder recu (rien a
 * rendre). Piege : si on supprime en avancant, les lettres suivantes
 * reculent d'une case et on en saute une. En partant de la FIN, rien ne
 * bouge devant nous.
 *
 * -- Essayons a la main --
 *
 *   "programmation" -> "prgrmmtn"      "aeiouy" -> ""
 *
 * -- Le plan --
 *
 *   1. Pour i de sb.length() - 1 jusqu'a 0 : si "aeiouy".indexOf(sb.charAt(i)) >= 0, sb.deleteCharAt(i).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : censor(text, word)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque fois que le mot interdit apparait, on le remplace par autant
 * d'etoiles que de lettres. StringBuilder a son propre indexOf.
 *
 * -- Essayons a la main --
 *
 *   ("le chat et le chaton", "chat") -> "le **** et le ****on"
 *
 * -- Le plan --
 *
 *   1. sb = new StringBuilder(text) ; i = sb.indexOf(word).
 *   2. Tant que i >= 0 : sb.replace(i, i + longueur, "*".repeat(longueur)) ;
 *      i = sb.indexOf(word, i + longueur).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : formatPhone(digits)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On glisse un espace tous les 2 chiffres. Si on insere depuis le debut,
 * chaque espace decale les positions suivantes. Depuis la FIN, les
 * positions de devant ne bougent pas.
 *
 * -- Essayons a la main --
 *
 *   "0612345678" -> "06 12 34 56 78"      "12345" -> "1 23 45"
 *
 * -- Le plan --
 *
 *   1. sb = new StringBuilder(digits).
 *   2. Pour i = longueur - 2 ; i > 0 ; i -= 2 : sb.insert(i, ' ').
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : truncate(text, max)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Si le texte est trop long, on le coupe et on finit par "...", le
 * tout en max caracteres. setLength coupe un StringBuilder net.
 *
 * -- Essayons a la main --
 *
 *   ("Bonjour tout le monde", 10) -> "Bonjour..."     ("court", 10) -> "court"
 *
 * -- Le plan --
 *
 *   1. Si text.length() <= max : rendre text.
 *   2. sb = new StringBuilder(text) ; sb.setLength(max - 3) ; sb.append("...").
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : toggleCase(sb)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On inverse majuscules et minuscules, case par case, DANS le
 * StringBuilder recu, avec setCharAt.
 *
 * -- Essayons a la main --
 *
 *   "JaVa 17!" -> "jAvA 17!"
 *
 * -- Le plan --
 *
 *   1. Pour chaque index : c = sb.charAt(i) ; si Character.isUpperCase(c),
 *      setCharAt(i, Character.toLowerCase(c)), sinon setCharAt(i, Character.toUpperCase(c)).
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
 *   - Toutes les methodes qui modifient (append, insert, delete, replace, reverse) rendent le
 *     MEME objet : on peut chainer.
 *   - setCharAt et setLength rendent void : pas de chainage.
 */
public class Exercise09_StringBuilderOperations {

    public static String reverseWords(String sentence) {
        throw new UnsupportedOperationException("TODO 1 : implementer reverseWords()");
    }

    public static void removeVowels(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 2 : implementer removeVowels()");
    }

    public static String censor(String text, String word) {
        throw new UnsupportedOperationException("TODO 3 : implementer censor()");
    }

    public static String formatPhone(String digits) {
        throw new UnsupportedOperationException("TODO 4 : implementer formatPhone()");
    }

    public static String truncate(String text, int max) {
        throw new UnsupportedOperationException("TODO 5 : implementer truncate()");
    }

    public static void toggleCase(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 6 : implementer toggleCase()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("reverseWords(\"un deux trois\") == \"trois deux un\"", reverseWords("un deux trois").equals("trois deux un"));

        StringBuilder word = new StringBuilder("programmation");
        removeVowels(word);
        StringBuilder vowels = new StringBuilder("aeiouy");
        removeVowels(vowels);
        ExerciseChecker.check("removeVowels : programmation -> prgrmmtn, aeiouy -> \"\" (meme objet modifie)",
                word.toString().equals("prgrmmtn") && vowels.length() == 0);

        ExerciseChecker.check("censor : \"le **** et le ****on\"", censor("le chat et le chaton", "chat").equals("le **** et le ****on"));
        ExerciseChecker.check("formatPhone : 06 12 34 56 78 et 1 23 45",
                formatPhone("0612345678").equals("06 12 34 56 78") && formatPhone("12345").equals("1 23 45"));
        ExerciseChecker.check("truncate : Bonjour... et court",
                truncate("Bonjour tout le monde", 10).equals("Bonjour...") && truncate("court", 10).equals("court"));

        StringBuilder mixed = new StringBuilder("JaVa 17!");
        toggleCase(mixed);
        ExerciseChecker.check("toggleCase : \"JaVa 17!\" -> \"jAvA 17!\"", mixed.toString().equals("jAvA 17!"));

        ExerciseChecker.summary();
    }
}
