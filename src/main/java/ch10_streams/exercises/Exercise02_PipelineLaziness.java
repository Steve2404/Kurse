package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EXERCICE 2 - Un pipeline de stream est paresseux (niveau : difficile)
 * ================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un pipeline de stream a 3 parties : une SOURCE (obligatoire, la
 * matiere premiere), des operations INTERMEDIAIRES (zero ou plus,
 * comme filter()/map() - elles decrivent le travail a faire, SANS le
 * faire tout de suite), et une operation TERMINALE (celle qui
 * declenche VRAIMENT le travail). Tant qu'il n'y a pas d'operation
 * terminale, rien ne s'execute - c'est comme donner une recette de
 * cuisine a quelqu'un : tant qu'il ne se met pas VRAIMENT aux
 * fourneaux, rien n'est cuisine, meme si la recette est deja ecrite en
 * entier.
 *
 * Encore plus surprenant : un pipeline ne traite pas "TOUS les
 * elements a travers filter(), PUIS tous a travers map()". Il traite
 * chaque element, UN PAR UN, a travers TOUTE la chaine d'un coup,
 * avant de passer a l'element suivant - et certaines operations
 * terminales (comme findFirst()) peuvent s'arreter en cours de route
 * des qu'elles ont ce qu'il leur faut, sans jamais toucher aux
 * elements restants (le "court-circuit").
 *
 *
 * ==================================================================
 * TODO 1 : filterEvenThenTimesTen(values, filterCalls, mapCalls)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer values.stream().filter(...).map(...), SANS ajouter la
 *      moindre operation terminale (pas de collect(), pas de
 *      forEach()...).
 *   2. Le filter garde les nombres pairs, et incremente filterCalls[0]
 *      a CHAQUE fois qu'il examine un element (peu importe le
 *      resultat).
 *   3. Le map multiplie par 10, et incremente mapCalls[0] a CHAQUE
 *      fois qu'il transforme un element (donc seulement pour ceux qui
 *      ont passe le filter).
 *
 * Exemple a verifier : sur values = [1,2,3,4,5], TANT QU'aucune
 * operation terminale n'a ete appelee sur le Stream renvoye,
 * filterCalls et mapCalls doivent rester a 0 - meme si la methode a
 * deja "fini de s'executer" et renvoye son resultat !
 *
 *
 * ==================================================================
 * TODO 2 : firstEvenTimesTen(values, filterCalls)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Cette fois, on va jusqu'au bout AVEC une operation terminale
 * (findFirst()), pour observer le "court-circuit" : des qu'un element
 * pair est trouve, le pipeline s'arrete, sans jamais regarder les
 * elements plus loin dans la liste.
 *
 * -- Le plan --
 *
 *   1. Construire values.stream().filter(pair, en incrementant
 *      filterCalls[0] a chaque examen).map(x10).findFirst().
 *   2. Renvoyer le resultat (un Optional<Integer>).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee entre eux ? --
 *
 * Non : chacun est deja sa propre methode, ils illustrent juste 2
 * facettes differentes de la paresse (rien ne s'execute sans
 * terminal / le court-circuit s'arrete des que possible).
 *
 * Exemple a verifier : values = [1,3,5,7,2,4,6,8,9,10]. Le premier
 * nombre pair est a l'index 4 (valeur 2). firstEvenTimesTen doit
 * renvoyer Optional.of(20), et filterCalls[0] doit valoir EXACTEMENT
 * 5 (les 5 premiers elements examines : 1,3,5,7,2) - PAS 10, meme si
 * la liste contient 10 elements au total.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - filter/map avec compteur :
 *       values.stream()
 *           .filter(n -> { filterCalls[0]++; return n % 2 == 0; })
 *           .map(n -> { mapCalls[0]++; return n * 10; })
 *   - findFirst() renvoie un Optional<T> ; c'est une operation
 *     terminale qui COURT-CIRCUITE (elle n'a pas besoin de parcourir
 *     tout le stream si elle trouve un resultat plus tot).
 */
public class Exercise02_PipelineLaziness {

    public static Stream<Integer> filterEvenThenTimesTen(List<Integer> values, int[] filterCalls, int[] mapCalls) {
        throw new UnsupportedOperationException("TODO 1 : implementer filterEvenThenTimesTen()");
    }

    public static Optional<Integer> firstEvenTimesTen(List<Integer> values, int[] filterCalls) {
        throw new UnsupportedOperationException("TODO 2 : implementer firstEvenTimesTen()");
    }

    public static void main(String[] args) {
        List<Integer> values = List.of(1, 2, 3, 4, 5);
        int[] filterCalls = {0};
        int[] mapCalls = {0};

        Stream<Integer> pipeline = filterEvenThenTimesTen(values, filterCalls, mapCalls);
        ExerciseChecker.check("avant l'operation terminale, rien ne s'est execute",
                filterCalls[0] == 0 && mapCalls[0] == 0);

        List<Integer> result = pipeline.collect(Collectors.toList());
        ExerciseChecker.check("collect() declenche enfin le pipeline -> [20, 40]", result.equals(List.of(20, 40)));
        ExerciseChecker.check("filter a examine les 5 elements", filterCalls[0] == 5);
        ExerciseChecker.check("map n'a transforme que les 2 elements pairs", mapCalls[0] == 2);

        List<Integer> values2 = List.of(1, 3, 5, 7, 2, 4, 6, 8, 9, 10);
        int[] filterCalls2 = {0};
        Optional<Integer> first = firstEvenTimesTen(values2, filterCalls2);
        ExerciseChecker.check("firstEvenTimesTen == Optional.of(20)", first.equals(Optional.of(20)));
        ExerciseChecker.check("court-circuit : seuls les 5 premiers elements sont examines (pas les 10)",
                filterCalls2[0] == 5);

        ExerciseChecker.summary();
    }
}
