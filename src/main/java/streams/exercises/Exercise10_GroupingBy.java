package streams.exercises;

import streams.ExerciseChecker;

import java.util.List;
import java.util.Map;

/**
 * EXERCICE 10 - Regrouper avec Collectors.groupingBy() (niveau : difficile)
 * ======================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine trier une pile de cartes en plusieurs petits tas, selon une
 * regle (par exemple, "toutes les cartes de meme couleur ensemble").
 * groupingBy(fonctionDeCle) fait exactement ca : il calcule une CLE
 * pour chaque element (la "regle de tri"), et fabrique une Map ou
 * chaque cle pointe vers la LISTE de tous les elements qui partagent
 * cette cle.
 *
 * On peut aussi donner un DEUXIEME collector ("downstream") qui dit
 * quoi faire de chaque petit tas, au lieu de le garder tel quel en
 * liste - par exemple, ne garder que le COMPTE de cartes dans chaque
 * tas (Collectors.counting()), plutot que les cartes elles-memes.
 *
 *
 * ==================================================================
 * TODO 1 : groupByLength(words)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * words = ["a", "bb", "cc", "ddd", "e"].
 *
 * Cle = longueur du mot. "a"(1), "bb"(2), "cc"(2), "ddd"(3), "e"(1).
 *
 * Resultat : {1=[a, e], 2=[bb, cc], 3=[ddd]}.
 *
 * -- Le plan --
 *
 *   1. words.stream().collect(Collectors.groupingBy(String::length)).
 *
 *
 * ==================================================================
 * TODO 2 : countByLength(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme regroupement que TODO 1, mais cette fois, au lieu de garder la
 * LISTE des mots dans chaque tas, on ne garde que leur NOMBRE (un
 * collector "downstream" different).
 *
 * -- Essayons a la main --
 *
 * Meme words qu'au-dessus. Resultat : {1=2, 2=2, 3=1} (2 mots de
 * longueur 1, 2 mots de longueur 2, 1 mot de longueur 3) - notez que
 * les valeurs sont des Long (pas des int), car counting() renvoie un
 * Collector<T, ?, Long>.
 *
 * -- Le plan --
 *
 *   1. words.stream().collect(Collectors.groupingBy(String::length,
 *      Collectors.counting())).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule chaine d'appels, c'est le CHOIX du
 * downstream collector (ou son absence) qui fait toute la difference.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - groupingBy(classifier) tout seul utilise Collectors.toList()
 *     comme downstream PAR DEFAUT (implicite).
 *   - groupingBy(classifier, downstream) applique EXPLICITEMENT le
 *     downstream a chaque groupe, au lieu de la liste par defaut.
 */
public class Exercise10_GroupingBy {

    public static Map<Integer, List<String>> groupByLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 1 : implementer groupByLength()");
    }

    public static Map<Integer, Long> countByLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer countByLength()");
    }

    public static void main(String[] args) {
        List<String> words = List.of("a", "bb", "cc", "ddd", "e");

        Map<Integer, List<String>> grouped = groupByLength(words);
        ExerciseChecker.check("groupByLength : cle 1 -> [a, e]", grouped.get(1).equals(List.of("a", "e")));
        ExerciseChecker.check("groupByLength : cle 2 -> [bb, cc]", grouped.get(2).equals(List.of("bb", "cc")));
        ExerciseChecker.check("groupByLength : cle 3 -> [ddd]", grouped.get(3).equals(List.of("ddd")));

        Map<Integer, Long> counted = countByLength(words);
        ExerciseChecker.check("countByLength : {1=2, 2=2, 3=1}",
                counted.get(1) == 2L && counted.get(2) == 2L && counted.get(3) == 1L);

        ExerciseChecker.summary();
    }
}
