package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 4 - Chercher et comparer dans un String : indexOf(x, depart), lastIndexOf, compareTo (niveau : difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   "banana".indexOf('a', 2)     -> 3     (cherche A PARTIR de l'index 2, inclus)
 *   "banana".indexOf("na", 3)    -> 4
 *   "banana".indexOf('a', 99)    -> -1    (pas d'exception si le depart depasse)
 *   "banana".lastIndexOf('a')    -> 5     "banana".lastIndexOf('a', 4) -> 3 (cherche vers la GAUCHE)
 *   "apple".compareTo("banana")  -> -1    ('a' - 'b')
 *   "abc".compareTo("abcd")      -> -1    (prefixe : difference des LONGUEURS)
 *   "B".compareTo("a")           -> -31   (les MAJUSCULES passent avant : 'B' = 66, 'a' = 97)
 *
 *
 * ==================================================================
 * TODO 1 : countOccurrences(text, part)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche un mot dans une phrase, on met le doigt dessus, puis on
 * repart JUSTE APRES lui pour chercher le suivant. On compte combien de
 * fois on a pu poser le doigt. Les morceaux ne se chevauchent pas.
 *
 * -- Essayons a la main --
 *
 *   "banana", "an"  : trouve en 1, on repart de 3 ; trouve en 3, on repart de 5 ; rien -> 2
 *   "aaaa", "aa"    : trouve en 0, repart de 2 ; trouve en 2, repart de 4 ; rien -> 2 (pas 3)
 *   "abc", "z"      -> 0
 *
 * -- Le plan --
 *
 *   1. count = 0 ; from = 0.
 *   2. Tant que text.indexOf(part, from) rend un index >= 0 :
 *      count++ ; from = index + part.length().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : lastWord(sentence)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le dernier mot, c'est ce qui suit le DERNIER espace. Mais attention
 * aux espaces qui trainent a la fin : on les enleve d'abord.
 *
 * -- Essayons a la main --
 *
 *   "  le chat dort  " -> strip -> "le chat dort" -> dernier espace en 7 -> "dort"
 *   "seul"             -> aucun espace (-1) -> -1 + 1 = 0 -> "seul" (ca marche tout seul !)
 *
 * -- Le plan --
 *
 *   1. clean = sentence.strip().
 *   2. Renvoyer clean.substring(clean.lastIndexOf(' ') + 1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : myCompareTo(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * C'est TOI qui ecris la regle de compareTo(). Deux mots, lettre par
 * lettre. A la premiere lettre differente, on rend "lettre de a moins
 * lettre de b" (des nombres : chaque char a un code). Si l'un est le
 * debut de l'autre, on rend la difference des longueurs. main() compare
 * ta version au VRAI compareTo() sur 8 paires.
 *
 * -- Essayons a la main --
 *
 *   "hello" / "help" : h=h, e=e, l=l, 'l' - 'p' = 108 - 112 = -4
 *   "abc" / "abcd"   : pas de difference sur 3 lettres -> 3 - 4 = -1
 *   "same" / "same"  -> 0
 *
 * -- Le plan --
 *
 *   1. n = la plus petite des 2 longueurs (Math.min).
 *   2. Pour i de 0 a n - 1 : si a.charAt(i) != b.charAt(i), rendre leur difference.
 *   3. Rendre a.length() - b.length().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : isRotation(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "erbottlewat" est "waterbottle" qu'on a fait tourner comme un
 * bracelet. Astuce : colle le mot a lui-meme ("waterbottlewaterbottle") :
 * TOUTES les rotations sont dedans. Il faut aussi la meme longueur.
 *
 * -- Essayons a la main --
 *
 *   "waterbottle", "erbottlewat" -> true
 *   "abc", "acb"                 -> false
 *   "abc", "ab"                  -> false (longueurs differentes, meme si "ab" est dedans)
 *
 * -- Le plan --
 *
 *   1. Rendre a.length() == b.length() && (a + a).contains(b).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : positionsOf(text, c)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme idee que le TODO 1, mais on NOTE chaque position trouvee, et on
 * les rend separees par des virgules.
 *
 * -- Essayons a la main --
 *
 *   "banana", 'a' -> "1,3,5" ; "banana", 'z' -> ""
 *
 * -- Le plan --
 *
 *   1. StringBuilder ; i = text.indexOf(c).
 *   2. Tant que i >= 0 : ajouter une virgule si ce n'est pas le premier, ajouter i ;
 *      i = text.indexOf(c, i + 1).
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
 *   - indexOf existe en 4 versions : (char), (char, depart), (String), (String, depart).
 *   - Un char moins un char donne un int : return a.charAt(i) - b.charAt(i);
 */
public class Exercise04_StringSearchAndCompare {

    public static int countOccurrences(String text, String part) {
        throw new UnsupportedOperationException("TODO 1 : implementer countOccurrences()");
    }

    public static String lastWord(String sentence) {
        throw new UnsupportedOperationException("TODO 2 : implementer lastWord()");
    }

    public static int myCompareTo(String a, String b) {
        throw new UnsupportedOperationException("TODO 3 : implementer myCompareTo()");
    }

    public static boolean isRotation(String a, String b) {
        throw new UnsupportedOperationException("TODO 4 : implementer isRotation()");
    }

    public static String positionsOf(String text, char c) {
        throw new UnsupportedOperationException("TODO 5 : implementer positionsOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("countOccurrences : banana/an 2, aaaa/aa 2, abc/z 0",
                countOccurrences("banana", "an") == 2 && countOccurrences("aaaa", "aa") == 2 && countOccurrences("abc", "z") == 0);
        ExerciseChecker.check("lastWord : \"  le chat dort  \" -> dort, seul -> seul",
                lastWord("  le chat dort  ").equals("dort") && lastWord("seul").equals("seul"));

        String[][] pairs = {{"apple", "banana"}, {"abc", "abcd"}, {"B", "a"}, {"a", "B"}, {"same", "same"},
                {"hello", "help"}, {"Zoo", "apple"}, {"abcd", ""}};
        boolean allSame = true;
        for (String[] p : pairs) {
            allSame &= myCompareTo(p[0], p[1]) == p[0].compareTo(p[1]);
        }
        ExerciseChecker.check("myCompareTo() rend EXACTEMENT la meme valeur que compareTo() sur 8 paires", allSame);

        ExerciseChecker.check("isRotation : waterbottle/erbottlewat oui, abc/acb non, abc/ab non",
                isRotation("waterbottle", "erbottlewat") && !isRotation("abc", "acb") && !isRotation("abc", "ab"));
        ExerciseChecker.check("positionsOf : banana/a -> 1,3,5 ; banana/z -> \"\"",
                positionsOf("banana", 'a').equals("1,3,5") && positionsOf("banana", 'z').isEmpty());

        ExerciseChecker.summary();
    }
}
