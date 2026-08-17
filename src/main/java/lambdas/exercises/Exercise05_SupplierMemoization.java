package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * EXERCICE 5 - Supplier paresseux et memoisation (niveau : difficile)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 *
 * ==================================================================
 * TODO 1 : Lazy<T>.get()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu commandes un gateau d'anniversaire tres complique
 * chez un patissier. Tu ne veux PAS qu'il commence a le preparer des
 * que tu passes commande (ca prend des heures, et si finalement tu
 * changes d'avis, tout ce travail serait gaspille). Tu veux qu'il ne
 * commence a le fabriquer QUE quand tu viens vraiment le chercher
 * (paresse = ne travailler qu'au dernier moment necessaire). ET, si
 * tu reviens une deuxieme fois chercher "le meme gateau", il ne doit
 * PAS en refabriquer un tout neuf a chaque fois : il te redonne
 * CELUI qu'il a deja fait la premiere fois (memoire = ne jamais
 * refaire un travail deja fait).
 *
 * Supplier<T> est l'interface fonctionnelle qui represente EXACTEMENT
 * "la recette du gateau, pas encore preparee" (une methode T get(),
 * sans aucun parametre - elle sait fabriquer une valeur toute seule,
 * quand on le lui demande).
 *
 * -- Essayons a la main --
 *
 * Lazy<Integer> lazy = new Lazy<>(() -> { compteur++; return 42; });
 *
 * Juste apres avoir cree 'lazy' (avant tout appel a get()) : compteur
 * == 0. La recette n'a PAS encore ete executee, meme si elle est
 * prete.
 *
 * lazy.get() -> execute la recette (compteur devient 1), renvoie 42,
 * ET se souvient du resultat.
 *
 * lazy.get() a nouveau -> ne re-execute PAS la recette (compteur
 * reste a 1), renvoie directement le 42 deja calcule.
 *
 * -- Ce qu'on remarque --
 *
 * Il faut se souvenir de DEUX choses entre deux appels a get() : "ai-
 * je deja calcule la valeur ?" (un booleen), et "quelle etait cette
 * valeur ?" (le resultat garde de cote). Un Supplier tout seul, sans
 * classe autour de lui, ne sait pas se souvenir de son propre
 * historique - c'est pour ca qu'on l'enveloppe dans Lazy<T>.
 *
 * -- Le plan --
 *
 *   1. Au premier appel de get() (rien de calcule encore) : appeler
 *      supplier.get(), GARDER le resultat, marquer "calcule".
 *   2. Aux appels suivants : ne rien recalculer, renvoyer directement
 *      la valeur deja gardee.
 *
 *
 * ==================================================================
 * TODO 2 : memoize(function)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme idee que Lazy<T>, mais cette fois la "recette" prend un
 * INGREDIENT en entree (un Function<T,R> au lieu d'un Supplier<R>).
 * Le patissier doit maintenant se souvenir, pour CHAQUE saveur de
 * gateau commandee (chocolat, vanille, fraise...), s'il l'a deja
 * preparee ou non - un seul booleen ne suffit plus, il faut un CARNET
 * (une Map) qui associe chaque saveur a son gateau deja prepare.
 *
 * -- Le plan --
 *
 *   1. Preparer un carnet vide (une Map<T,R>) qui va grandir au fil
 *      des appels - AVANT de fabriquer la fonction memoisee, car le
 *      carnet doit survivre entre les appels a la fonction renvoyee.
 *   2. Renvoyer une NOUVELLE Function<T,R> qui, pour un ingredient
 *      recu : regarde d'abord si le carnet a deja ce resultat ; si
 *      oui, le rendre directement ; sinon, appeler la vraie fonction
 *      'function', RANGER le resultat dans le carnet, et le rendre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non pour l'etape 2 : le JDK fournit deja l'outil exact pour "si
 * absent du carnet, le calculer et le ranger, sinon le rendre tel
 * quel" en une seule ligne (voir l'indice).
 *
 * Exemple a verifier : Lazy autour d'un compteur -> compteur reste a
 * 0 avant le premier get(), passe a 1 apres, et NE bouge plus meme
 * apres plusieurs get() supplementaires.
 * memoize(carre) appele 2 fois avec 5 -> le carre reel n'est calcule
 * qu'UNE SEULE fois (verifie avec un compteur d'appels).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Lazy.get() :
 *       if (!computed) { cachedValue = supplier.get(); computed = true; }
 *       return cachedValue;
 *   - memoize() : Map<T, R> cache = new HashMap<>();
 *       return input -> cache.computeIfAbsent(input, function);
 *     (computeIfAbsent prend directement la Function en 2e argument :
 *     elle ne l'appelle QUE si la cle est absente du carnet)
 */
public class Exercise05_SupplierMemoization {

    static final class Lazy<T> {
        private final Supplier<T> supplier;
        private T cachedValue;
        private boolean computed;

        Lazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        T get() {
            throw new UnsupportedOperationException("TODO 1 : implementer get()");
        }
    }

    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        throw new UnsupportedOperationException("TODO 2 : implementer memoize()");
    }

    public static void main(String[] args) {
        int[] callCount = {0};
        Lazy<Integer> lazy = new Lazy<>(() -> {
            callCount[0]++;
            return 42;
        });

        ExerciseChecker.check("avant le premier get(), la recette n'a pas ete executee", callCount[0] == 0);

        int firstValue = lazy.get();
        ExerciseChecker.check("get() renvoie 42 et execute la recette une fois", firstValue == 42 && callCount[0] == 1);

        lazy.get();
        lazy.get();
        ExerciseChecker.check("les get() suivants ne re-executent pas la recette", callCount[0] == 1);

        int[] squareCalls = {0};
        Function<Integer, Integer> square = n -> {
            squareCalls[0]++;
            return n * n;
        };
        Function<Integer, Integer> memoizedSquare = memoize(square);

        ExerciseChecker.check("memoizedSquare(5) == 25", memoizedSquare.apply(5) == 25);
        memoizedSquare.apply(5);
        memoizedSquare.apply(5);
        ExerciseChecker.check("memoize() n'appelle la vraie fonction qu'une fois pour la meme entree",
                squareCalls[0] == 1);

        memoizedSquare.apply(7);
        ExerciseChecker.check("une entree DIFFERENTE declenche bien un nouveau calcul", squareCalls[0] == 2);

        ExerciseChecker.summary();
    }
}
