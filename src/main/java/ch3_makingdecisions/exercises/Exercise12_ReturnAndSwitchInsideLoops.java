package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 12 - return dans une boucle, et continue A TRAVERS un switch (niveau : difficile)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_LabeledLoops.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * return, c'est encore plus radical qu'un break etiquete (Exercise11)
 * : il ne se contente pas d'arreter des boucles, il quitte
 * IMMEDIATEMENT toute la METHODE, en rendant sa valeur - peu importe
 * combien de boucles (ou de switch) sont imbriques autour, TOUT
 * s'arrete d'un coup.
 *
 * PIEGE CLASSIQUE de l'examen : un switch DANS une boucle "capture"
 * le break (un break SANS etiquette a l'interieur d'un switch
 * n'arrete QUE le switch, jamais la boucle qui l'entoure - meme si
 * ce switch est la SEULE chose dans le corps de la boucle) - mais un
 * continue, LUI, ignore completement le switch et va directement
 * chercher la boucle qui l'entoure (continue ne "s'applique" JAMAIS
 * a un switch, uniquement a une boucle).
 *
 *
 * ==================================================================
 * TODO 1 : processExceptThree(nums)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec nums = {1, 2, 3, 4, 3, 5} : on garde tout SAUF les 3 - resultat
 * attendu [1, 2, 4, 5].
 *
 * -- Le plan --
 *
 *   1. Creer une liste vide.
 *   2. for (int n : nums) : switch (n) { case 3 -> continue (le
 *      continue "traverse" le switch et va directement au TOUR
 *      SUIVANT de la boucle for, sans jamais toucher a la liste) ;
 *      default -> ajouter n a la liste }.
 *   3. Renvoyer la liste.
 *
 *
 * ==================================================================
 * TODO 2 : findFirstNegativeOrZero(nums)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec nums = {5, 8, -3, 9} : -3 est le 1er negatif (ou nul)
 * rencontre - on le renvoie IMMEDIATEMENT, sans meme regarder 9.
 *
 * -- Le plan --
 *
 *   1. for (int n : nums) : si n <= 0 : return n (quitte TOUT DE
 *      SUITE la methode entiere, pas besoin de break ni de variable
 *      "trouve").
 *   2. APRES la boucle (si rien n'a ete trouve) : renvoyer null.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans une seule boucle.
 *
 * Exemple a verifier : processExceptThree({1, 2, 3, 4, 3, 5}) == [1,
 * 2, 4, 5]. findFirstNegativeOrZero({5, 8, -3, 9}) == -3.
 * findFirstNegativeOrZero({5, 8, 9}) == null (rien trouve).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Dans un switch EXPRESSION (avec ->, voir Exercise06), "case 3
 *     -> continue;" fonctionne comme une INSTRUCTION normale dans le
 *     corps du switch STATEMENT ici (switch (n) { ... }, sans return
 *     direct) - continue reste une instruction de FLUX, pas une
 *     valeur, donc ce switch-ci doit rester un switch STATEMENT
 *     classique, pas une expression utilisee comme valeur.
 */
public class Exercise12_ReturnAndSwitchInsideLoops {

    public static List<Integer> processExceptThree(int[] nums) {
        throw new UnsupportedOperationException("TODO 1 : implementer processExceptThree()");
    }

    public static Integer findFirstNegativeOrZero(int[] nums) {
        throw new UnsupportedOperationException("TODO 2 : implementer findFirstNegativeOrZero()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("processExceptThree() : continue traverse le switch jusqu'a la boucle",
                processExceptThree(new int[] {1, 2, 3, 4, 3, 5}).equals(List.of(1, 2, 4, 5)));

        ExerciseChecker.check("findFirstNegativeOrZero() : return quitte TOUT immediatement",
                findFirstNegativeOrZero(new int[] {5, 8, -3, 9}) == -3);
        ExerciseChecker.check("findFirstNegativeOrZero() : rien trouve -> null",
                findFirstNegativeOrZero(new int[] {5, 8, 9}) == null);

        ExerciseChecker.summary();
    }
}
