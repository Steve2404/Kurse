package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 2 - Pattern matching instanceof : et la "flow scoping", la portee qui suit la LOGIQUE (niveau : difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_IfElseBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avant le pattern matching, verifier un type demandait 3 etapes
 * separees : "if (obj instanceof String)", PUIS "String s =
 * (String) obj;" (un cast MANUEL, redondant - on vient DEJA de
 * verifier le type !). "if (obj instanceof String s)" fait les 3 EN
 * UNE FOIS : verifie, caste, ET declare s - s n'existe QUE si le test
 * a reussi.
 *
 * "Flow scoping" veut dire que la portee de s ne suit PAS les
 * accolades classiques {} - elle suit ce que le COMPILATEUR peut
 * PROUVER logiquement. Exemple classique : "if (!(obj instanceof
 * String s)) { return ...; }" - APRES ce if (donc EN DEHORS de ses
 * accolades !), s reste UTILISABLE : le compilateur "comprend" que
 * si on arrive jusque-la, c'est FORCEMENT que obj EST une String
 * (sinon la methode aurait deja quitte via le return).
 *
 *
 * ==================================================================
 * TODO 1 : describeViaEarlyReturn(obj)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec obj = "hello" : ce n'est PAS instanceof String NEGATIF (c'est
 * bien une String), donc pas de sortie anticipee - on continue et on
 * utilise s EN DEHORS du if.
 *
 * -- Le plan --
 *
 *   1. Si obj n'est PAS instanceof String s (le "!" NEGATIVE le
 *      test) : renvoyer "pas une String".
 *   2. APRES ce if (donc en dehors de ses accolades) : renvoyer
 *      "String de longueur " + s.length() - s reste utilisable ICI
 *      grace a la flow scoping.
 *
 *
 * ==================================================================
 * TODO 2 : describeIfLongString(obj)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le pattern variable peut se combiner avec une condition SUPPLEMENTAIRE
 * via && - la 2e condition (s.length() > 3) peut directement UTILISER
 * s, puisque && verifie TOUJOURS son cote gauche EN PREMIER (si le
 * cote gauche est faux, le cote droit n'est meme pas evalue - donc s
 * est deja garanti exister des qu'on l'utilise).
 *
 * -- Le plan --
 *
 *   1. Si obj instanceof String s ET s.length() > 3 (en UNE seule
 *      condition, avec &&) : renvoyer "longue String : " + s.
 *   2. Sinon : renvoyer "trop courte ou pas une String".
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : describeViaEarlyReturn("hello") ==
 * "String de longueur 5". describeViaEarlyReturn(42) == "pas une
 * String". describeIfLongString("hello") == "longue String : hello".
 * describeIfLongString("hi") == "trop courte ou pas une String" (une
 * String, mais TROP COURTE).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "if (!(obj instanceof String s)) { return \"pas une String\"; }"
 *     - les parentheses autour de "obj instanceof String s" sont
 *     OBLIGATOIRES avant le "!" (sinon Java essaierait de negativer
 *     obj lui-meme, ce qui n'a aucun sens).
 */
public class Exercise02_PatternMatchingAndFlowScoping {

    public static String describeViaEarlyReturn(Object obj) {
        throw new UnsupportedOperationException("TODO 1 : implementer describeViaEarlyReturn()");
    }

    public static String describeIfLongString(Object obj) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeIfLongString()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("describeViaEarlyReturn(\"hello\") utilise s APRES le if (flow scoping)",
                describeViaEarlyReturn("hello").equals("String de longueur 5"));
        ExerciseChecker.check("describeViaEarlyReturn(42) sort tot via le if negatif",
                describeViaEarlyReturn(42).equals("pas une String"));

        ExerciseChecker.check("describeIfLongString(\"hello\") : condition combinee avec &&",
                describeIfLongString("hello").equals("longue String : hello"));
        ExerciseChecker.check("describeIfLongString(\"hi\") : String, mais trop courte",
                describeIfLongString("hi").equals("trop courte ou pas une String"));

        ExerciseChecker.summary();
    }
}
