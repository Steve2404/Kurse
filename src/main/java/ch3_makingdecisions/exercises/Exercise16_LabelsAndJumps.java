package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 16 - Sauts avances : break d'un switch DANS une boucle, continue etiquete, bloc etiquete (niveau : difficile)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Verdicts REELS de javac 17 --
 *
 *   switch (x) { case 1: continue; }  sans boucle     -> error: continue outside of loop
 *   if (flag) { break; }  sans boucle ni switch       -> error: break outside switch or loop
 *   outer: for (...) { }  puis, dans une AUTRE boucle, break outer;
 *                                                      -> error: undefined label: outer
 *   lbl: { continue lbl; }                            -> error: not a loop label: lbl
 *   lbl: { ... break lbl; ... }                       -> COMPILE (break sort d'un bloc etiquete)
 *   outer: for (...) { for (...) { continue outer; } } -> COMPILE
 *
 * Le piege le plus sournois (verifie a l'execution) : dans une boucle
 * qui contient un switch, un break tout seul sort du SWITCH, pas de la
 * boucle. La boucle continue comme si de rien n'etait.
 *
 *
 * ==================================================================
 * TODO 1 : sumUntilStop(commands)   [break etiquete pour sortir de la boucle depuis un switch]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On additionne des nombres donnes sous forme de texte, jusqu'au mot
 * "stop". "skip" veut dire "ignore ce tour". Avec un switch dans la
 * boucle, "case "stop": break;" ne quitte que le switch : la version
 * naive additionnerait aussi ce qui suit "stop" (8 au lieu de 3
 * ci-dessous). Il faut une etiquette sur la boucle et "break etiquette;".
 *
 * -- Essayons a la main --
 *
 *   ["1", "2", "stop", "5"] -> 3 ; ["4", "skip", "6"] -> 10 ; [] -> 0
 *
 * -- Le plan --
 *
 *   1. total = 0 ; loop: for (commande : commands) { switch (commande) {
 *        case "stop": break loop;  case "skip": continue;  default: total += nombre; } }
 *   2. Rendre total.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : rowsWithoutNegatives(grid)   [continue etiquete]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On compte les lignes sans aucun nombre negatif. Des qu'on trouve un
 * negatif, cette ligne est perdue : on passe DIRECTEMENT a la ligne
 * suivante avec "continue outer;" (un continue simple ne ferait que
 * passer a la case suivante de la meme ligne).
 *
 * -- Essayons a la main --
 *
 *   {{1, 2}, {3, -4}, {5, 6}, {-7, 8}} -> 2
 *
 * -- Le plan --
 *
 *   1. count = 0 ; outer: pour chaque ligne { pour chaque valeur { si < 0 : continue outer; } count++; }
 *   2. Rendre count.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : unchangedWithoutMatch(x)   [switch sans default qui ne fait rien]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un switch STATEMENT sans default, dont aucun case ne correspond, ne
 * fait simplement rien : ce n'est pas une erreur.
 *
 * -- Essayons a la main --
 *
 *   1 -> 100 ; 99 -> 5 (inchange)
 *
 * -- Le plan --
 *
 *   1. before = 5 ; switch (x) { case 1: before = 100; break; } ; rendre before.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : firstPairSummingTo(values, target)   [break etiquete sur 2 boucles]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ([1, 4, 6, 3], 9) -> "2,3" (6 + 3 : on rend les INDICES de la 1re paire trouvee) ;
 *   ([1, 2], 10) -> "aucune"
 *
 * -- Le plan --
 *
 *   1. result = "aucune".
 *   2. search: for i { for j de i + 1 { si somme == target : result = i + "," + j ; break search; } }
 *   3. Rendre result.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : checkSign(x)   [break sur un bloc etiquete, sans boucle]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une etiquette peut aussi nommer un simple bloc { }. "break nom;"
 * saute alors a la fin du bloc : une sortie anticipee sans boucle.
 * (continue, lui, n'a de sens que pour une boucle.)
 *
 * -- Essayons a la main --
 *
 *   -3 -> "negatif" ; 0 -> "zero" ; 8 -> "positif"
 *
 * -- Le plan --
 *
 *   1. String result = "negatif";
 *   2. check: { if (x < 0) break check; result = "zero"; if (x == 0) break check; result = "positif"; }
 *   3. Rendre result.
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
 *   - l'etiquette se place juste avant la boucle : "loop: for (String c : commands) {"
 *   - Integer.parseInt(c)
 */
public class Exercise16_LabelsAndJumps {

    public static int sumUntilStop(String[] commands) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumUntilStop()");
    }

    public static int rowsWithoutNegatives(int[][] grid) {
        throw new UnsupportedOperationException("TODO 2 : implementer rowsWithoutNegatives()");
    }

    public static int unchangedWithoutMatch(int x) {
        throw new UnsupportedOperationException("TODO 3 : implementer unchangedWithoutMatch()");
    }

    public static String firstPairSummingTo(int[] values, int target) {
        throw new UnsupportedOperationException("TODO 4 : implementer firstPairSummingTo()");
    }

    public static String checkSign(int x) {
        throw new UnsupportedOperationException("TODO 5 : implementer checkSign()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 sumUntilStop([1, 2, stop, 5]) == 3 (pas 8)", sumUntilStop(new String[]{"1", "2", "stop", "5"}) == 3);
        ExerciseChecker.check("1 sumUntilStop([4, skip, 6]) == 10, ([]) == 0",
                sumUntilStop(new String[]{"4", "skip", "6"}) == 10 && sumUntilStop(new String[0]) == 0);
        ExerciseChecker.check("2 rowsWithoutNegatives == 2",
                rowsWithoutNegatives(new int[][]{{1, 2}, {3, -4}, {5, 6}, {-7, 8}}) == 2);
        ExerciseChecker.check("3 unchangedWithoutMatch : 1 -> 100, 99 -> 5", unchangedWithoutMatch(1) == 100 && unchangedWithoutMatch(99) == 5);
        ExerciseChecker.check("4 firstPairSummingTo([1, 4, 6, 3], 9) == 2,3 ; ([1, 2], 10) == aucune",
                firstPairSummingTo(new int[]{1, 4, 6, 3}, 9).equals("2,3") && firstPairSummingTo(new int[]{1, 2}, 10).equals("aucune"));
        ExerciseChecker.check("5 checkSign : -3 negatif, 0 zero, 8 positif",
                checkSign(-3).equals("negatif") && checkSign(0).equals("zero") && checkSign(8).equals("positif"));

        ExerciseChecker.summary();
    }
}
