package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * EXERCICE 13 - Curryfier une BiFunction (niveau : difficile)
 * ======================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un distributeur de boissons qui exige DEUX pieces avant de
 * donner la boisson : tu inseres la 1ere piece, il ne se passe rien
 * tout de suite, la machine RETIENT juste que tu as deja mis une
 * piece et attend la seconde. Des que tu inseres la 2eme piece, LA, il
 * te donne enfin la boisson.
 *
 * "Curryfier" une fonction a 2 ingredients (BiFunction<A,B,R>, qui
 * exige les DEUX ingredients EN MEME TEMPS), c'est la transformer en
 * une fonction a 1 seul ingredient qui, une fois nourrie du premier
 * (A), ne rend pas encore le resultat final (R) mais... une TOUTE
 * NOUVELLE fonction (Function<B,R>) qui n'attend plus que le second
 * ingredient (B). C'est exactement la machine a boissons : "inserer la
 * 1ere piece" te donne "une machine qui n'attend plus que la 2eme
 * piece", pas directement la boisson.
 *
 *
 * ==================================================================
 * TODO 1 : curry(biFunction)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * add = (a, b) -> a + b (une BiFunction<Integer,Integer,Integer>).
 *
 * curriedAdd = curry(add) -- curriedAdd est maintenant une
 * Function<Integer, Function<Integer,Integer>>.
 *
 * add5 = curriedAdd.apply(5) -- add5 est une TOUTE NOUVELLE
 * Function<Integer,Integer> qui "se souvient" que le premier
 * ingredient valait 5, et n'attend plus que le second.
 *
 * add5.apply(3) -> 8 (5 + 3). add5.apply(10) -> 15 (5 + 10). On peut
 * reutiliser add5 autant de fois qu'on veut avec des 2emes ingredients
 * differents : le "5" reste fige dedans.
 *
 * -- Le plan --
 *
 *   1. Renvoyer une Function<A, Function<B,R>> (un lambda qui prend un
 *      premier ingredient 'a').
 *   2. Le CORPS de ce lambda doit lui-meme renvoyer une DEUXIEME
 *      Function<B,R> (un lambda IMBRIQUE, qui prend le second
 *      ingredient 'b') dont le corps appelle enfin
 *      biFunction.apply(a, b).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est un lambda qui RENVOIE un autre lambda, imbrique
 * directement - c'est exactement le sens du mot "curryfier", pas
 * besoin de le decouper en methodes separees.
 *
 *
 * ==================================================================
 * TODO 2 : uncurry(curried)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le chemin inverse : on a la machine "1 piece a la fois", et on veut
 * refabriquer une machine classique "les 2 pieces d'un coup".
 *
 * -- Le plan --
 *
 *   1. Renvoyer une BiFunction<A,B,R> (un lambda a 2 parametres, a et
 *      b).
 *   2. Son corps appelle curried.apply(a) (ce qui rend une
 *      Function<B,R>), PUIS appelle .apply(b) sur CE resultat, dans la
 *      MEME expression (deux appels enchaines).
 *
 * Exemple a verifier : curry(add).apply(5).apply(3) == 8.
 * uncurry(curry(add)).apply(2, 3) == 5.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - curry() : return a -> b -> biFunction.apply(a, b);
 *     (c'est un lambda dont le CORPS est un AUTRE lambda - Java
 *     accepte cette ecriture "en chaine" sans accolades, tant que
 *     chaque etape est une seule expression)
 *   - uncurry() : return (a, b) -> curried.apply(a).apply(b);
 */
public class Exercise13_CurryBiFunction {

    public static <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> biFunction) {
        throw new UnsupportedOperationException("TODO 1 : implementer curry()");
    }

    public static <A, B, R> BiFunction<A, B, R> uncurry(Function<A, Function<B, R>> curried) {
        throw new UnsupportedOperationException("TODO 2 : implementer uncurry()");
    }

    public static void main(String[] args) {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

        Function<Integer, Function<Integer, Integer>> curriedAdd = curry(add);
        Function<Integer, Integer> add5 = curriedAdd.apply(5);
        ExerciseChecker.check("curry: add5.apply(3) == 8", add5.apply(3) == 8);
        ExerciseChecker.check("curry: add5.apply(10) == 15 (le 5 reste fige)", add5.apply(10) == 15);
        ExerciseChecker.check("curry: un autre premier ingredient donne une autre fonction",
                curriedAdd.apply(100).apply(1) == 101);

        BiFunction<Integer, Integer, Integer> backToBi = uncurry(curriedAdd);
        ExerciseChecker.check("uncurry: backToBi.apply(2, 3) == 5", backToBi.apply(2, 3) == 5);

        ExerciseChecker.summary();
    }
}
