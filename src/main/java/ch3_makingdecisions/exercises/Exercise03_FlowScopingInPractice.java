package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 3 - La portee d'une variable de pattern suit la LOGIQUE : 4 facons correctes de l'utiliser (niveau : difficile)
 * ======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- La regle ("flow scoping") --
 *
 * Dans "o instanceof String s", la variable s n'existe QUE la ou le
 * compilateur peut PROUVER que le test a reussi. Pas une ligne de plus.
 *
 * Ce qui NE COMPILE PAS (messages REELS de javac 17) :
 *
 *   if (o instanceof String s) { } System.out.println(s);
 *     -> cannot find symbol  (apres un if simple, rien n'est prouve)
 *   if (o instanceof String s || s.length() > 3) { }
 *     -> cannot find symbol  (avec ||, le cote droit s'execute quand le test a ECHOUE)
 *   if (o instanceof String s) { } else { System.out.println(s); }
 *     -> cannot find symbol  (le else, c'est justement "le test a echoue")
 *   String s = "x"; if (o instanceof String s) { }
 *     -> variable s is already defined in method m(Object)
 *   Integer i = 5; if (i instanceof Integer j) { }
 *     -> expression type Integer is a subtype of pattern type Integer  (test inutile)
 *   String t = "x"; if (t instanceof Integer j) { }
 *     -> incompatible types: String cannot be converted to Integer (test impossible)
 *
 * Ce qui COMPILE : les 4 formes des TODO ci-dessous (et aussi apres un
 * "while (!(o instanceof String s)) { ... }" : apres la boucle, s existe).
 * Rappel utile : null instanceof X est toujours false.
 *
 *
 * ==================================================================
 * TODO 1 : lengthOrMinusOne(o)   [sortie anticipee, SANS accolades]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "Si ce n'est pas une String, je m'en vais." Apres cette ligne, le
 * compilateur SAIT que c'est une String : s est utilisable partout
 * ensuite. Les accolades ne jouent aucun role, seul le return compte.
 *
 * -- Essayons a la main --
 *
 *   "abc" -> 3 ; 42 -> -1 ; null -> -1
 *
 * -- Le plan --
 *
 *   1. if (!(o instanceof String s)) return -1;   (sur UNE ligne, sans accolades)
 *   2. return s.length();
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : upperOrQuestionMark(o)   [le else d'un if NEGATIF]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans "if (!(o instanceof String s)) { ... } else { ... }", le else
 * veut dire "la negation est fausse", donc "c'est bien une String" :
 * s y existe.
 *
 * -- Essayons a la main --
 *
 *   "java" -> "JAVA" ; 3.5 -> "?"
 *
 * -- Le plan --
 *
 *   1. if (!(o instanceof String s)) { return "?"; } else { return s en majuscules; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : longWordOrEmpty(o)   [&& apres le pattern]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avec &&, le cote droit ne s'execute QUE si le cote gauche a reussi :
 * s y existe deja. On peut donc filtrer la longueur dans le meme test.
 *
 * -- Essayons a la main --
 *
 *   "ordinateur" -> "ordinateur" ; "ok" -> "" ; 7 -> ""
 *
 * -- Le plan --
 *
 *   1. Rendre s si o est une String s ET s.length() > 3, sinon "".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : totalStringLength(items)   [continue dans une boucle]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "Si ce n'est pas une String, passe au suivant." Apres ce continue,
 * le reste du tour de boucle sait que c'est une String.
 *
 * -- Essayons a la main --
 *
 *   ["ab", 3, null, "cde"] -> 2 + 3 = 5
 *
 * -- Le plan --
 *
 *   1. total = 0.
 *   2. Pour chaque element : if (!(item instanceof String s)) continue; total += s.length();
 *   3. Rendre total.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - les parentheses de !(o instanceof String s) sont obligatoires
 *   - o instanceof String s && s.length() > 3 ? s : ""
 */
public class Exercise03_FlowScopingInPractice {

    public static int lengthOrMinusOne(Object o) {
        throw new UnsupportedOperationException("TODO 1 : implementer lengthOrMinusOne()");
    }

    public static String upperOrQuestionMark(Object o) {
        throw new UnsupportedOperationException("TODO 2 : implementer upperOrQuestionMark()");
    }

    public static String longWordOrEmpty(Object o) {
        throw new UnsupportedOperationException("TODO 3 : implementer longWordOrEmpty()");
    }

    public static int totalStringLength(Object[] items) {
        throw new UnsupportedOperationException("TODO 4 : implementer totalStringLength()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 lengthOrMinusOne : abc -> 3, 42 -> -1, null -> -1",
                lengthOrMinusOne("abc") == 3 && lengthOrMinusOne(42) == -1 && lengthOrMinusOne(null) == -1);
        ExerciseChecker.check("2 upperOrQuestionMark : java -> JAVA, 3.5 -> ?",
                upperOrQuestionMark("java").equals("JAVA") && upperOrQuestionMark(3.5).equals("?"));
        ExerciseChecker.check("3 longWordOrEmpty : ordinateur garde, ok et 7 -> \"\"",
                longWordOrEmpty("ordinateur").equals("ordinateur") && longWordOrEmpty("ok").isEmpty()
                        && longWordOrEmpty(7).isEmpty());
        ExerciseChecker.check("4 totalStringLength([ab, 3, null, cde]) == 5",
                totalStringLength(new Object[]{"ab", 3, null, "cde"}) == 5);

        ExerciseChecker.summary();
    }
}
