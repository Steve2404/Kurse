package streams.exercises;

import streams.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 4 - Trier un stream avec sorted() (niveau : moyen)
 * ======================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- sorted(), en une phrase --
 *
 * sorted() est une operation INTERMEDIAIRE (elle ne trie pas tout de
 * suite, elle attend l'operation terminale comme le reste du
 * pipeline). Elle existe en 2 versions : sans argument (ordre naturel
 * - l'element doit etre Comparable), et avec un Comparator<T> en
 * argument (l'ordre que TU decides).
 *
 *
 * ==================================================================
 * TODO 1 : naturalSort(words)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. words.stream().sorted().collect(Collectors.toList()).
 *
 *
 * ==================================================================
 * TODO 2 : sortByLengthThenAlpha(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu classes des mots par longueur, du plus court au plus
 * long. Mais que faire si DEUX mots ont exactement la meme longueur ?
 * Il faut une regle de "depart" (tie-break) : ici, l'ordre
 * alphabetique classique entre les mots de meme longueur.
 *
 * -- Essayons a la main --
 *
 * words = ["banana", "kiwi", "fig", "apple"].
 *
 * Longueurs : fig=3, kiwi=4, apple=5, banana=6. Aucune egalite ici,
 * donc le resultat est simplement trie par longueur croissante :
 * ["fig", "kiwi", "apple", "banana"].
 *
 * -- Le plan --
 *
 *   1. words.stream().sorted(Comparator.comparingInt(String::length)
 *      .thenComparing(Comparator.naturalOrder()))
 *      .collect(Collectors.toList()).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule chaine d'appels.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Comparator.comparingInt(String::length) compare selon une cle
 *     int extraite de chaque element (ici, la longueur du mot).
 *   - .thenComparing(...) ne s'applique QUE si la comparaison
 *     precedente a trouve une EGALITE (0) - c'est le "tie-break".
 */
public class Exercise04_SortedStream {

    public static List<String> naturalSort(List<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer naturalSort()");
    }

    public static List<String> sortByLengthThenAlpha(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer sortByLengthThenAlpha()");
    }

    public static void main(String[] args) {
        List<String> words = List.of("banana", "kiwi", "fig", "apple");

        ExerciseChecker.check("naturalSort == [apple, banana, fig, kiwi]",
                naturalSort(words).equals(List.of("apple", "banana", "fig", "kiwi")));

        ExerciseChecker.check("sortByLengthThenAlpha == [fig, kiwi, apple, banana]",
                sortByLengthThenAlpha(words).equals(List.of("fig", "kiwi", "apple", "banana")));

        List<String> withTies = List.of("pear", "plum", "kiwi", "lime");
        ExerciseChecker.check("egalite de longueur -> depart alphabetique",
                sortByLengthThenAlpha(withTies).equals(List.of("kiwi", "lime", "pear", "plum")));

        ExerciseChecker.summary();
    }
}
