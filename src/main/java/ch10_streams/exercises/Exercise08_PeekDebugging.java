package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EXERCICE 8 - peek() pour observer un pipeline element par element (niveau : difficile)
 * ===================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * peek() est une operation INTERMEDIAIRE qui ne change RIEN au
 * contenu du stream : elle laisse juste passer chaque element tel
 * quel, mais execute une petite action AU PASSAGE (typiquement, pour
 * du debug : "regarder discretement ce qui traverse ce point precis
 * du tuyau, sans rien y toucher").
 *
 * Rappel de l'Exercise02 (paresse) : un pipeline traite chaque element
 * UN PAR UN, a travers TOUTE la chaine, avant de passer au suivant.
 * Cet exercice va rendre ce comportement VISIBLE, en enregistrant
 * dans une liste "trace" l'ordre exact des passages.
 *
 *
 * ==================================================================
 * TODO : processWithTrace(values, trace)
 * ==================================================================
 *
 * -- Le plan --
 *
 * Construire le pipeline suivant, dans cet ORDRE PRECIS :
 *
 *   1. peek() qui ajoute "vu:X" a trace pour CHAQUE element qui entre
 *      dans le pipeline (avant tout filtre).
 *   2. filter() qui garde uniquement les nombres pairs.
 *   3. peek() qui ajoute "garde:X" a trace, mais SEULEMENT pour les
 *      elements qui ont survecu au filter() precedent.
 *   4. map() qui eleve chaque element restant au carre.
 *   5. collect(Collectors.toList()) pour finalement declencher tout
 *      le pipeline et recuperer le resultat.
 *
 * -- Essayons a la main --
 *
 * values = [1, 2, 3, 4].
 *
 * Element 1 : "vu:1" ajoute -> filter(1) = faux (impair) -> ARRETE ici
 *   pour cet element (jamais de "garde:1", jamais de carre).
 * Element 2 : "vu:2" ajoute -> filter(2) = vrai -> "garde:2" ajoute ->
 *   carre = 4.
 * Element 3 : "vu:3" ajoute -> filter(3) = faux -> arrete.
 * Element 4 : "vu:4" ajoute -> filter(4) = vrai -> "garde:4" ajoute ->
 *   carre = 16.
 *
 * Trace finale, DANS CET ORDRE EXACT :
 *   ["vu:1", "vu:2", "garde:2", "vu:3", "vu:4", "garde:4"]
 *
 * Resultat final (les carres) : [4, 16].
 *
 * Remarque bien : "vu:3" apparait AVANT "vu:4" ET avant "garde:4" -
 * chaque element va JUSQU'AU BOUT du pipeline (ou s'arrete au filter)
 * avant que l'element SUIVANT ne commence son propre voyage.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule chaine d'appels suffit, l'important est l'ORDRE des
 * operations dans la chaine, pas leur decoupage en methodes.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - values.stream()
 *         .peek(v -> trace.add("vu:" + v))
 *         .filter(v -> v % 2 == 0)
 *         .peek(v -> trace.add("garde:" + v))
 *         .map(v -> v * v)
 *         .collect(Collectors.toList());
 */
public class Exercise08_PeekDebugging {

    public static List<Integer> processWithTrace(List<Integer> values, List<String> trace) {
        throw new UnsupportedOperationException("TODO : implementer processWithTrace()");
    }

    public static void main(String[] args) {
        List<Integer> values = List.of(1, 2, 3, 4);
        List<String> trace = new java.util.ArrayList<>();

        List<Integer> result = processWithTrace(values, trace);

        ExerciseChecker.check("resultat final (carres des pairs) == [4, 16]", result.equals(List.of(4, 16)));
        ExerciseChecker.check("trace : chaque element est 'vu', mais 'garde' seulement s'il passe le filter",
                trace.equals(List.of("vu:1", "vu:2", "garde:2", "vu:3", "vu:4", "garde:4")));

        ExerciseChecker.summary();
    }
}
