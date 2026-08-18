package streams.exercises;

import streams.ExerciseChecker;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * EXERCICE 1 - Optional : creer, lire, et les 3 methodes "fonctionnelles" (niveau : moyen/difficile)
 * ============================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Optional<T>, en une phrase --
 *
 * Optional<T> est une "boite" qui contient PEUT-ETRE une valeur, ou
 * PEUT-ETRE rien du tout (vide) - c'est une facon explicite de dire
 * "attention, ce resultat pourrait ne pas exister", au lieu de
 * renvoyer null en silence et laisser l'appelant decouvrir le probleme
 * plus tard avec une NullPointerException surprise.
 *
 *
 * ==================================================================
 * TODO 1 : safeDivide(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Diviser par zero n'a pas de sens. Plutot que de planter le
 * programme, on renvoie une boite VIDE (Optional.empty()) pour dire
 * "desole, pas de resultat ici", et une boite PLEINE
 * (Optional.of(resultat)) sinon.
 *
 * -- Le plan --
 *
 *   1. Si b == 0, renvoyer Optional.empty().
 *   2. Sinon, renvoyer Optional.of(a / b).
 *
 *
 * ==================================================================
 * TODO 2 : describeSafely(opt)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La maniere la plus "classique" de lire un Optional : d'abord
 * demander "y a-t-il quelque chose dedans ?" (isPresent()), et
 * SEULEMENT si la reponse est oui, l'ouvrir (get()) - ouvrir une boite
 * vide avec get() lance une exception.
 *
 * -- Le plan --
 *
 *   1. Si opt.isPresent() : renvoyer "Resultat : " + opt.get().
 *   2. Sinon : renvoyer "Pas de resultat".
 *
 *
 * ==================================================================
 * TODO 3 : valueOrFallback(opt, fallback)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que "fallback" est une recette de secours COUTEUSE (elle
 * prend du temps a preparer). Si la boite Optional a deja une valeur,
 * ce serait du gaspillage de preparer quand meme la recette de
 * secours "juste au cas ou". orElseGet(Supplier) est PARESSEUX :
 * il n'appelle la recette que si elle est VRAIMENT necessaire.
 *
 * -- Le plan --
 *
 *   1. Renvoyer opt.orElseGet(fallback) directement (une seule ligne).
 *
 *
 * ==================================================================
 * TODO 4 : requireValue(opt)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Parfois, une boite vide est carrement une ERREUR (pas juste une
 * absence normale) : on prefere alors lancer soi-meme une exception
 * PARLANTE, plutot que de laisser Java en lancer une generique plus
 * tard. orElseThrow(Supplier) prend "la recette qui fabrique
 * l'exception a lancer", et ne l'appelle QUE si la boite est vide.
 *
 * -- Le plan --
 *
 *   1. Renvoyer opt.orElseThrow(() -> new IllegalStateException("Aucune valeur presente")).
 *
 *
 * ==================================================================
 * TODO 5 : logIfPresent(opt, sink)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ifPresent(Consumer) execute une action SEULEMENT si la boite a une
 * valeur - rien ne se passe du tout si elle est vide (contrairement a
 * un if/else classique, il n'y a pas de "sinon" ici).
 *
 * -- Le plan --
 *
 *   1. Appeler opt.ifPresent(v -> sink.add("Valeur presente : " + v)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chaque TODO est deja sa propre methode publique, et chacun
 * tient en 1 a 3 lignes une fois traduit en Java.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Optional.empty() / Optional.of(valeur) : jamais Optional.of(null)
 *     (ca lance une NullPointerException immediatement - utiliser
 *     Optional.ofNullable(x) si x pourrait etre null).
 *   - opt.isPresent() renvoie un boolean ; opt.get() lance
 *     NoSuchElementException si la boite est vide.
 *   - opt.orElseGet(supplier) : appelle supplier.get() UNIQUEMENT si
 *     opt est vide.
 *   - opt.orElseThrow(supplier) : appelle supplier.get() (qui doit
 *     rendre une exception) UNIQUEMENT si opt est vide, et LA LANCE.
 *   - opt.ifPresent(consumer) : appelle consumer.accept(valeur)
 *     UNIQUEMENT si opt a une valeur.
 */
public class Exercise01_OptionalBasics {

    public static Optional<Integer> safeDivide(int a, int b) {
        throw new UnsupportedOperationException("TODO 1 : implementer safeDivide()");
    }

    public static String describeSafely(Optional<Integer> opt) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeSafely()");
    }

    public static int valueOrFallback(Optional<Integer> opt, Supplier<Integer> fallback) {
        throw new UnsupportedOperationException("TODO 3 : implementer valueOrFallback()");
    }

    public static int requireValue(Optional<Integer> opt) {
        throw new UnsupportedOperationException("TODO 4 : implementer requireValue()");
    }

    public static void logIfPresent(Optional<Integer> opt, List<String> sink) {
        throw new UnsupportedOperationException("TODO 5 : implementer logIfPresent()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("safeDivide(10, 2) == Optional.of(5)", safeDivide(10, 2).equals(Optional.of(5)));
        ExerciseChecker.check("safeDivide(10, 0) == Optional.empty()", safeDivide(10, 0).isEmpty());

        ExerciseChecker.check("describeSafely(present) == 'Resultat : 5'",
                describeSafely(Optional.of(5)).equals("Resultat : 5"));
        ExerciseChecker.check("describeSafely(vide) == 'Pas de resultat'",
                describeSafely(Optional.empty()).equals("Pas de resultat"));

        int[] fallbackCalls = {0};
        Supplier<Integer> fallback = () -> {
            fallbackCalls[0]++;
            return 99;
        };
        ExerciseChecker.check("valueOrFallback(present, ...) == 5, fallback jamais appele",
                valueOrFallback(Optional.of(5), fallback) == 5 && fallbackCalls[0] == 0);
        ExerciseChecker.check("valueOrFallback(vide, ...) == 99, fallback appele",
                valueOrFallback(Optional.empty(), fallback) == 99 && fallbackCalls[0] == 1);

        ExerciseChecker.check("requireValue(present) == 5", requireValue(Optional.of(5)) == 5);
        boolean threw = false;
        try {
            requireValue(Optional.empty());
        } catch (IllegalStateException e) {
            threw = true;
        }
        ExerciseChecker.check("requireValue(vide) lance IllegalStateException", threw);

        List<String> sink = new java.util.ArrayList<>();
        logIfPresent(Optional.of(5), sink);
        logIfPresent(Optional.empty(), sink);
        ExerciseChecker.check("logIfPresent n'ajoute que pour la boite pleine",
                sink.equals(List.of("Valeur presente : 5")));

        ExerciseChecker.summary();
    }
}
