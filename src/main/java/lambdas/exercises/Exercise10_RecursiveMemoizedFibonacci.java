package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * EXERCICE 10 - Un lambda qui s'appelle lui-meme, avec memoisation (niveau : difficile)
 * ============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * D'habitude, une methode peut s'appeler elle-meme (la recursion),
 * parce qu'elle a un NOM fixe qu'on peut reutiliser dans son propre
 * corps. Un lambda, lui, n'a PAS de nom : au moment ou on ecrit son
 * corps, la variable qui va le recevoir n'existe pas encore
 * completement (elle est "en cours de fabrication"), donc on ne peut
 * pas ecrire directement "maVariable.apply(...)" a l'interieur de sa
 * propre definition - le compilateur refuserait, la variable n'est pas
 * encore utilisable a cet endroit precis.
 *
 * L'astuce (deja vue en filigrane a l'Exercise07 avec le tableau a une
 * case) : on fabrique d'abord une "boite aux lettres" vide, un tableau
 * Function<Integer,Long>[1], PUIS on range le lambda DEDANS cette
 * boite - et a l'interieur du lambda, on ne fait plus reference a une
 * variable locale qui "n'existe pas encore", mais au CONTENU de la
 * boite (fibHolder[0]), qui lui, existe deja (le tableau, en tant
 * qu'objet, est cree AVANT qu'on remplisse sa case).
 *
 *
 * ==================================================================
 * TODO : buildMemoizedFibonacci(cache)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * fibonacci(0) = 0, fibonacci(1) = 1, et pour n >= 2 :
 * fibonacci(n) = fibonacci(n-1) + fibonacci(n-2).
 *
 * Sans memoisation, calculer fibonacci(10) recalcule PLUSIEURS FOIS
 * les memes petits sous-problemes (fibonacci(5) est redemande de
 * nombreuses fois au fil de la recursion) : le nombre d'appels explose
 * tres vite avec n. Avec un carnet (cache) qui retient "je connais deja
 * fibonacci(7) = 13", chaque valeur de n n'est calculee QU'UNE SEULE
 * FOIS, meme si on la redemande plus tard.
 *
 * -- Le plan --
 *
 *   1. Fabriquer une "boite aux lettres" vide : un tableau
 *      Function<Integer,Long>[1] (un seul emplacement).
 *   2. Ranger DANS cette boite un lambda qui, pour un n donne :
 *      a. si n <= 1, renvoie n directement (cas de base, pas besoin
 *         de memoire pour ca) ;
 *      b. sinon, si le cache contient deja n, renvoie cette valeur
 *         directement, SANS rien recalculer ;
 *      c. sinon, calcule le resultat en rappelant fibHolder[0] sur
 *         n-1 ET n-2 (c'est ici que le lambda "s'appelle lui-meme", en
 *         passant par la boite), RANGE ce resultat dans le cache pour
 *         n, et le renvoie.
 *   3. Renvoyer le contenu de la boite (fibHolder[0]).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est un seul lambda recursif, la boite (tableau) EST le
 * mecanisme qui permet cette auto-reference, pas besoin d'une methode
 * a part.
 *
 * Exemple a verifier : fibonacci(10) == 55, et apres ce calcul, le
 * cache contient exactement les entrees n=2..10 (9 entrees) - la
 * preuve que chaque sous-probleme n'a ete resolu qu'une seule fois.
 * fibonacci(20) == 6765.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Declaration de la boite (attention, tableau generique -
 *     avertissement du compilateur normal, sans consequence ici) :
 *       @SuppressWarnings("unchecked")
 *       Function<Integer, Long>[] fibHolder = new Function[1];
 *   - Corps du lambda :
 *       fibHolder[0] = n -> {
 *           if (n <= 1) return (long) n;
 *           if (cache.containsKey(n)) return cache.get(n);
 *           long result = fibHolder[0].apply(n - 1) + fibHolder[0].apply(n - 2);
 *           cache.put(n, result);
 *           return result;
 *       };
 *   - fibHolder est effectivement finale (jamais reassignee APRES sa
 *     toute premiere affectation) : le lambda a bien le droit de la
 *     capturer.
 */
public class Exercise10_RecursiveMemoizedFibonacci {

    public static Function<Integer, Long> buildMemoizedFibonacci(Map<Integer, Long> cache) {
        throw new UnsupportedOperationException("TODO : implementer buildMemoizedFibonacci()");
    }

    public static void main(String[] args) {
        Map<Integer, Long> cache = new HashMap<>();
        Function<Integer, Long> fib = buildMemoizedFibonacci(cache);

        long fib10 = fib.apply(10);
        ExerciseChecker.check("fibonacci(10) == 55", fib10 == 55);
        ExerciseChecker.check("le cache retient exactement les sous-problemes n=2..10 (9 entrees)",
                cache.size() == 9);

        long fib10Again = fib.apply(10);
        ExerciseChecker.check("rappel de fibonacci(10) : resultat stable, cache inchange (rien recalcule)",
                fib10Again == 55 && cache.size() == 9);

        long fib20 = fib.apply(20);
        ExerciseChecker.check("fibonacci(20) == 6765", fib20 == 6765);

        ExerciseChecker.summary();
    }
}
