package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 10 - Autoboxing et unboxing : Java convertit tout seul, mais unboxing null EXPLOSE (niveau : moyen/difficile)
 * ================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un int, c'est un nombre "nu" - une simple valeur, sans aucune
 * "boite" autour. Un Integer, lui, c'est ce MEME nombre range DANS
 * UNE BOITE (un objet). AUTOBOXING, c'est quand Java, tout seul,
 * range automatiquement un int dans sa boite Integer des que le
 * CONTEXTE l'exige (comme numbers.add(1) : List<Integer> ne peut
 * accueillir QUE des boites, jamais un nombre nu). UNBOXING, c'est
 * l'inverse : Java OUVRE automatiquement la boite pour en ressortir
 * le nombre nu, des que le contexte l'exige (comme "total += n" : +=
 * sur un int exige un nombre nu, pas une boite). LE DANGER : ouvrir
 * une boite qui est... VIDE (null, "aucune boite du tout") lance une
 * NullPointerException A L'EXECUTION - une des rares facons de voir
 * apparaitre un NullPointerException a partir d'un simple "return"
 * ou d'une simple addition, sans jamais ecrire ".quoi que ce soit()"
 * dessus.
 *
 *
 * ==================================================================
 * TODO 1 : sumViaAutobox(numbers)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec numbers = [1, 2, 3] (des Integer, remplis via autoboxing dans
 * main()), sumViaAutobox() doit rendre 6.
 *
 * -- Le plan --
 *
 *   1. Declarer int total = 0.
 *   2. Pour chaque n (Integer) de numbers : total += n (n, une
 *      BOITE, est automatiquement OUVERTE - unboxing - pour pouvoir
 *      etre ajoutee a total, un int "nu").
 *   3. Renvoyer total.
 *
 *
 * ==================================================================
 * TODO 2 : unboxOrThrow(value)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * value est un Integer (une boite, potentiellement VIDE). La methode
 * doit renvoyer un int "nu" : Java ouvre donc AUTOMATIQUEMENT la
 * boite (unboxing) au moment du return - si la boite est vide
 * (null), CETTE ouverture-la explose en NullPointerException.
 *
 * -- Le plan --
 *
 *   1. Renvoyer directement value (le unboxing se fait tout seul,
 *      AUTOMATIQUEMENT, au moment de rendre un int alors que value
 *      est un Integer).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne (ou quelques lignes pour la boucle).
 *
 * Exemple a verifier : sumViaAutobox(List.of(1, 2, 3)) == 6.
 * unboxOrThrow(5) == 5. unboxOrThrow(null) lance
 * NullPointerException.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "numbers.add(1)" (voir main()) : 1 est un int LITTERAL, mais
 *     add() attend un Integer - Java l'autoboxe tout seul avant
 *     l'appel, sans qu'on ait besoin d'ecrire "Integer.valueOf(1)".
 */
public class Exercise10_AutoboxingAndUnboxing {

    public static int sumViaAutobox(List<Integer> numbers) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumViaAutobox()");
    }

    public static int unboxOrThrow(Integer value) {
        throw new UnsupportedOperationException("TODO 2 : implementer unboxOrThrow()");
    }

    public static void main(String[] args) {
        List<Integer> numbers = new java.util.ArrayList<>();
        numbers.add(1); // autoboxing : int 1 -> Integer
        numbers.add(2);
        numbers.add(3);

        ExerciseChecker.check("sumViaAutobox() unboxe chaque Integer pour l'additionner",
                sumViaAutobox(numbers) == 6);

        ExerciseChecker.check("unboxOrThrow(5) unboxe normalement", unboxOrThrow(5) == 5);

        boolean caught = false;
        try {
            unboxOrThrow(null);
        } catch (NullPointerException e) {
            caught = true;
        }
        ExerciseChecker.check("unboxOrThrow(null) lance NullPointerException a l'unboxing", caught);

        ExerciseChecker.summary();
    }
}
