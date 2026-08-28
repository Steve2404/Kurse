package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * EXERCICE 6 - Interfaces fonctionnelles pour primitives, sans autoboxing (niveau : difficile)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu dois compter des billes en vrac, des MILLIERS de
 * billes. Si, pour chaque bille, tu dois d'abord la ranger dans une
 * petite boite individuelle etiquetee AVANT de la compter (comme
 * transformer un int en Integer, "l'autoboxing"), tu perds un temps
 * fou a fabriquer et jeter des milliers de petites boites, juste pour
 * un travail qui n'avait besoin que des nombres bruts.
 *
 * Predicate<Integer>, Function<Integer,Integer> etc. utilisent le
 * type "boite" (Integer). IntPredicate, IntUnaryOperator,
 * IntBinaryOperator utilisent directement le nombre BRUT (int), sans
 * jamais fabriquer de boite. Cet exercice t'interdit d'utiliser les
 * versions "boite" : tu dois tout faire avec les versions "int cru".
 *
 *
 * ==================================================================
 * TODO 1 : countMatching(values, predicate)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * values = [1, 2, 3, 4, 5, 6]. predicate = "est pair" (n % 2 == 0).
 *
 * On regarde chaque bille une par une, brute, sans jamais la mettre
 * en boite : 1 (non), 2 (oui), 3 (non), 4 (oui), 5 (non), 6 (oui).
 * Total de billes "oui" : 3.
 *
 * -- Le plan --
 *
 *   1. Preparer un compteur a 0.
 *   2. Pour chaque valeur brute du tableau, si predicate.test(valeur)
 *      est vrai, incrementer le compteur.
 *
 *
 * ==================================================================
 * TODO 2 : transformAll(values, operator)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * IntUnaryOperator, c'est une machine qui prend UN nombre brut et en
 * rend UN AUTRE (par exemple, "double-le"). transformAll applique
 * cette machine a CHAQUE bille du tas, et fabrique un nouveau tas de
 * billes transformees.
 *
 * -- Le plan --
 *
 *   1. Preparer un nouveau tableau brut, de la meme taille.
 *   2. Pour chaque position, y ranger operator.applyAsInt(valeur
 *      d'origine a cette position).
 *
 *
 * ==================================================================
 * TODO 3 : reduce(values, identity, operator)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un rouleau compresseur qui avance sur le tas de billes, une
 * par une, en ecrasant a chaque fois "ce qu'il porte deja" avec "la
 * bille suivante", pour n'en faire plus qu'UN SEUL nombre a la fin.
 * IntBinaryOperator, c'est la regle d'ecrasement (prend DEUX nombres
 * bruts, en rend UN). 'identity' est ce que le rouleau porte AVANT de
 * croiser la toute premiere bille (le point de depart neutre).
 *
 * -- Essayons a la main --
 *
 * values = [3, 7, 2, 9]. identity = 0. operator = "le plus grand des
 * deux" (Math::max).
 *
 * Depart : 0. Ecrase avec 3 -> max(0,3)=3. Ecrase avec 7 -> max(3,7)=7.
 * Ecrase avec 2 -> max(7,2)=7. Ecrase avec 9 -> max(7,9)=9. Resultat
 * final : 9 (le plus grand de tout le tas).
 *
 * Avec identity=0 et operator=addition, ce meme rouleau donnerait la
 * SOMME de toutes les billes (3+7+2+9=21) : c'est la meme boite
 * magique generale, juste une regle d'ecrasement differente.
 *
 * -- Le plan --
 *
 *   1. Demarrer un accumulateur a 'identity'.
 *   2. Pour chaque valeur du tableau, remplacer l'accumulateur par
 *      operator.applyAsInt(accumulateur, valeur).
 *   3. Renvoyer l'accumulateur final.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee entre eux ? --
 *
 * Non : chacun est deja sa propre boite (une methode publique dediee).
 * Mais a l'INTERIEUR de chacun, tout tient en une petite boucle, pas
 * besoin de redecouper davantage.
 *
 * Exemple a verifier :
 *   countMatching([1,2,3,4,5,6], n -> n % 2 == 0) == 3
 *   transformAll([1,2,3], n -> n * 2) == [2,4,6]
 *   reduce([3,7,2,9], 0, Math::max) == 9
 *   reduce([3,7,2,9], 0, (a, b) -> a + b) == 21
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - IntPredicate.test(int) renvoie un boolean (pas Boolean).
 *   - IntUnaryOperator.applyAsInt(int) renvoie un int (pas Integer).
 *   - IntBinaryOperator.applyAsInt(int, int) renvoie un int.
 *   - Aucune de ces 3 interfaces n'a de parametre generique <T> :
 *     elles sont deja figees sur int, c'est tout leur interet.
 */
public class Exercise06_PrimitiveFunctionalInterfaces {

    public static int countMatching(int[] values, IntPredicate predicate) {
        throw new UnsupportedOperationException("TODO 1 : implementer countMatching()");
    }

    public static int[] transformAll(int[] values, IntUnaryOperator operator) {
        throw new UnsupportedOperationException("TODO 2 : implementer transformAll()");
    }

    public static int reduce(int[] values, int identity, IntBinaryOperator operator) {
        throw new UnsupportedOperationException("TODO 3 : implementer reduce()");
    }

    public static void main(String[] args) {
        int[] values = {1, 2, 3, 4, 5, 6};
        ExerciseChecker.check("countMatching(pair) == 3", countMatching(values, n -> n % 2 == 0) == 3);

        int[] tripled = transformAll(new int[]{1, 2, 3}, n -> n * 2);
        ExerciseChecker.check("transformAll(doubler) == [2,4,6]",
                tripled[0] == 2 && tripled[1] == 4 && tripled[2] == 6);

        int[] toReduce = {3, 7, 2, 9};
        ExerciseChecker.check("reduce(max) == 9", reduce(toReduce, 0, Math::max) == 9);
        ExerciseChecker.check("reduce(somme) == 21", reduce(toReduce, 0, (a, b) -> a + b) == 21);

        ExerciseChecker.summary();
    }
}
