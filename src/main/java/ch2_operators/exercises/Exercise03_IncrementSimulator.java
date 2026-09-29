package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 3 - Simuler ++ et -- pas a pas, SANS les utiliser : prouver que tu sais comment Java evalue (niveau : difficile)
 * =======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * L'Exercise01 t'a fait ECRIRE x++ + ++x. Ici, tu vas le REJOUER a la
 * main, operation par operation, comme le fait la JVM : chaque
 * operande est evalue de GAUCHE a DROITE, et chaque ++/-- change x
 * IMMEDIATEMENT, avant que l'operande suivant soit lu.
 *
 * Interdit dans TOUT l'exercice : les operateurs ++ et -- (ni x++, ni
 * ++x, ni x--, ni --x). Seulement =, + et -. La variable x est
 * representee par une "boite" : un tableau d'une case, int[] x, pour
 * que les methodes puissent la modifier.
 *
 * main() compare tes simulations avec les VRAIS resultats de Java
 * (calcules dans main avec les vrais operateurs), pour 3 valeurs de
 * depart. Valeurs reelles pour x = 5 (verifiees) :
 *
 *   x++ + ++x      -> 12, puis x == 7
 *   ++x * x-- - x  -> 31, puis x == 5
 *   x += x++       -> x == 10   (le ++ est "perdu")
 *   x = x++        -> x == 5    (x ne bouge pas !)
 *
 *
 * ==================================================================
 * TODO 1 a 4 : postIncrement(x), preIncrement(x), postDecrement(x), preDecrement(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 *   x++ (APRES) : "je te montre la valeur ACTUELLE, puis j'ajoute 1".
 *   ++x (AVANT) : "j'ajoute 1, puis je te montre la NOUVELLE valeur".
 *   Meme chose pour -- avec -1.
 *
 * -- Essayons a la main --
 *
 *   x = {5} : postIncrement -> rend 5, x devient {6}
 *   x = {5} : preIncrement  -> x devient {6}, rend 6
 *
 * -- Le plan (postIncrement) --
 *
 *   1. Garder l'ancienne valeur. 2. x[0] = x[0] + 1. 3. Rendre l'ancienne.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Ce SONT les boites magiques des TODO 5 a 7.
 *
 *
 * ==================================================================
 * TODO 5 : simulatePostPlusPre(start)       ->  x++ + ++x
 * TODO 6 : simulatePreTimesPostMinusX(start) ->  ++x * x-- - x
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On lit l'expression de gauche a droite, operande par operande, et
 * on appelle la bonne boite magique pour chacun. Un "x" tout seul
 * lit simplement la valeur actuelle de la boite. Les * passent avant
 * les + et - (precedence), mais les OPERANDES, eux, sont toujours
 * EVALUES de gauche a droite.
 *
 * -- Essayons a la main (start = 5) --
 *
 *   TODO 5 : postIncrement -> 5 (x=6) ; preIncrement -> 7 (x=7) ; 5 + 7 = 12
 *   TODO 6 : preIncrement -> 6 (x=6) ; postDecrement -> 6 (x=5) ; x -> 5 ;
 *            6 * 6 - 5 = 31
 *
 * -- Le plan --
 *
 *   1. int[] x = {start}.
 *   2. Evaluer chaque operande dans l'ordre avec les boites magiques.
 *   3. Rendre {resultat, x[0]}.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1 a 4.
 *
 *
 * ==================================================================
 * TODO 7 : simulateCompoundSelf(start)  ->  x += x++
 * TODO 8 : simulateSelfAssign(start)    ->  x = x++
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "x += e" veut dire "x = x + e", et le x de GAUCHE est lu EN PREMIER,
 * avant d'evaluer e. Donc x += x++ : on lit x (5), puis x++ rend 5 et
 * met x a 6... puis l'affectation ECRASE x avec 5 + 5 = 10. Le +1 est
 * perdu. Meme piege pour x = x++ : x++ rend 5 et met x a 6, puis
 * l'affectation remet x a 5.
 *
 * -- Le plan --
 *
 *   TODO 7 : 1. lire x ; 2. appeler postIncrement ; 3. x[0] = lu + resultat ; rendre x[0].
 *   TODO 8 : 1. appeler postIncrement ; 2. x[0] = son resultat ; rendre x[0].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : postIncrement.
 *
 *
 * Exemple a verifier : pour start = 5, 0 et -3, chaque simulation
 * donne EXACTEMENT le resultat des vrais operateurs Java.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - int old = x[0]; x[0] = x[0] + 1; return old;
 *   - return new int[]{result, x[0]};
 */
public class Exercise03_IncrementSimulator {

    public static int postIncrement(int[] x) {
        throw new UnsupportedOperationException("TODO 1 : implementer postIncrement()");
    }

    public static int preIncrement(int[] x) {
        throw new UnsupportedOperationException("TODO 2 : implementer preIncrement()");
    }

    public static int postDecrement(int[] x) {
        throw new UnsupportedOperationException("TODO 3 : implementer postDecrement()");
    }

    public static int preDecrement(int[] x) {
        throw new UnsupportedOperationException("TODO 4 : implementer preDecrement()");
    }

    public static int[] simulatePostPlusPre(int start) {
        throw new UnsupportedOperationException("TODO 5 : implementer simulatePostPlusPre()");
    }

    public static int[] simulatePreTimesPostMinusX(int start) {
        throw new UnsupportedOperationException("TODO 6 : implementer simulatePreTimesPostMinusX()");
    }

    public static int simulateCompoundSelf(int start) {
        throw new UnsupportedOperationException("TODO 7 : implementer simulateCompoundSelf()");
    }

    public static int simulateSelfAssign(int start) {
        throw new UnsupportedOperationException("TODO 8 : implementer simulateSelfAssign()");
    }

    public static void main(String[] args) {
        int[] box = {5};
        ExerciseChecker.check("1 postIncrement({5}) rend 5, boite a 6", postIncrement(box) == 5 && box[0] == 6);
        box[0] = 5;
        ExerciseChecker.check("2 preIncrement({5}) rend 6, boite a 6", preIncrement(box) == 6 && box[0] == 6);
        box[0] = 5;
        ExerciseChecker.check("3 postDecrement({5}) rend 5, boite a 4", postDecrement(box) == 5 && box[0] == 4);
        box[0] = 5;
        ExerciseChecker.check("4 preDecrement({5}) rend 4, boite a 4", preDecrement(box) == 4 && box[0] == 4);

        boolean ok5 = true;
        boolean ok6 = true;
        boolean ok7 = true;
        boolean ok8 = true;
        for (int start : new int[]{5, 0, -3}) {
            int x = start;
            int real = x++ + ++x;
            int[] sim = simulatePostPlusPre(start);
            ok5 &= sim[0] == real && sim[1] == x;

            x = start;
            real = ++x * x-- - x;
            sim = simulatePreTimesPostMinusX(start);
            ok6 &= sim[0] == real && sim[1] == x;

            x = start;
            x += x++;
            ok7 &= simulateCompoundSelf(start) == x;

            x = start;
            x = x++;
            ok8 &= simulateSelfAssign(start) == x;
        }
        ExerciseChecker.check("5 x++ + ++x simule exactement (depart 5, 0, -3)", ok5);
        ExerciseChecker.check("6 ++x * x-- - x simule exactement", ok6);
        ExerciseChecker.check("7 x += x++ simule exactement (x == 10 pour 5)", ok7 && simulateCompoundSelf(5) == 10);
        ExerciseChecker.check("8 x = x++ simule exactement (x ne bouge pas)", ok8 && simulateSelfAssign(5) == 5);

        ExerciseChecker.summary();
    }
}
