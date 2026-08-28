package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 8 - while vs do/while : la SEULE vraie difference - le do/while s'execute AU MOINS 1 fois (niveau : moyen)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_IfElseBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * while (condition) { ... } verifie la condition AVANT chaque tour -
 * si elle est FAUSSE des le debut, le corps ne s'execute JAMAIS, pas
 * meme une fois. do { ... } while (condition); fait l'INVERSE :
 * execute le corps D'ABORD, PUIS verifie la condition - le corps
 * s'execute donc TOUJOURS AU MOINS UNE FOIS, meme si la condition
 * etait deja fausse avant meme de commencer. C'est LA seule vraie
 * difference entre les 2.
 *
 *
 * ==================================================================
 * TODO 1 : countdownWhile(n)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * countdownWhile(3) doit rendre [3, 2, 1]. countdownWhile(0) doit
 * rendre [] (liste VIDE : la condition est FAUSSE des le debut, le
 * corps du while ne s'execute donc jamais).
 *
 * -- Le plan --
 *
 *   1. Creer une liste vide.
 *   2. Tant que n > 0 : ajouter n a la liste, PUIS decrementer n.
 *   3. Renvoyer la liste.
 *
 *
 * ==================================================================
 * TODO 2 : runAtLeastOnce(startValue)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * runAtLeastOnce(0) doit rendre 1 (le corps s'execute UNE FOIS, MEME
 * si startValue vaut deja 0 des le debut - contrairement a
 * countdownWhile(0), qui, LUI, rend une liste VIDE dans le meme cas
 * de figure).
 *
 * -- Le plan --
 *
 *   1. Declarer int count = 0 et int n = startValue.
 *   2. do { compter (count++), PUIS decrementer n } while (n > 0).
 *   3. Renvoyer count.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans une seule boucle.
 *
 * Exemple a verifier : countdownWhile(3) == [3, 2, 1].
 * countdownWhile(0) == [] (while : 0 execution). runAtLeastOnce(0) ==
 * 1 (do/while : 1 execution GARANTIE, meme condition de depart).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "do { ... } while (condition);" - n'oubliez pas le
 *     point-virgule final APRES la condition : contrairement au
 *     while classique, c'est ici une VRAIE instruction complete.
 */
public class Exercise08_WhileVsDoWhile {

    public static List<Integer> countdownWhile(int n) {
        throw new UnsupportedOperationException("TODO 1 : implementer countdownWhile()");
    }

    public static int runAtLeastOnce(int startValue) {
        throw new UnsupportedOperationException("TODO 2 : implementer runAtLeastOnce()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("countdownWhile(3) == [3, 2, 1]", countdownWhile(3).equals(List.of(3, 2, 1)));
        ExerciseChecker.check("countdownWhile(0) == [] (while : condition fausse des le debut)",
                countdownWhile(0).isEmpty());

        ExerciseChecker.check("runAtLeastOnce(0) == 1 (do/while : execution GARANTIE au moins 1 fois)",
                runAtLeastOnce(0) == 1);

        ExerciseChecker.summary();
    }
}
