package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EXERCICE 16 - Le ramasse-miettes : ecris toi-meme "quels objets sont encore atteignables ?" (niveau : difficile)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- La regle de l'examen --
 *
 * Un objet devient ELIGIBLE au ramasse-miettes (garbage collector) des
 * qu'AUCUN chemin de references ne mene plus a lui depuis une variable
 * encore vivante (une variable locale en cours d'utilisation, un champ
 * static...). Ce n'est PAS "plus personne ne pointe sur lui" : deux
 * objets qui se pointent l'un l'autre, mais que plus aucune variable
 * n'atteint, sont eligibles TOUS LES DEUX (un "ilot"). Et "eligible"
 * ne veut pas dire "detruit tout de suite" : Java choisit le moment,
 * et System.gc() n'est qu'une suggestion.
 *
 * On modelise la memoire avec 2 cartes :
 *   variables : nom de variable -> objet qu'elle designe (null = rien)
 *   fields    : objet -> objets que ses champs designent
 *
 * Scenarios (repris des exemples classiques du livre) :
 *
 *   A : Widget a = new Widget("A"); Widget b = new Widget("B"); a = b;
 *       variables {a=B, b=B}, objets [A, B]            -> eligibles [A]
 *   B : n1.next = n2; n2.next = n1; n1 = null; n2 = null;
 *       variables {n1=null, n2=null}, fields {N1->N2, N2->N1}  -> [N1, N2]
 *   C : fin d'une methode : sa variable locale temp n'existe plus
 *       variables {}, objets [Temp]                    -> [Temp]
 *   D : une chaine depuis une racine : root=X, X->Y->Z, W seul -> [W]
 *   E : un ilot RACCROCHE a un objet vivant : keep=K, K->N1, N1<->N2 -> []
 *
 *
 * ==================================================================
 * TODO 1 : reachable(variables, fields)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On part de chaque variable vivante comme d'un point de depart, et
 * on suit les fils (les references) d'objet en objet, en coloriant
 * chaque objet atteint. On ne repasse jamais par un objet deja
 * colorie (sinon, dans un ilot, on tournerait en rond pour toujours).
 *
 * -- Essayons a la main --
 *
 *   Scenario D : depart X -> colorie X ; fils de X : Y -> colorie ;
 *   fils de Y : Z -> colorie ; fils de Z : aucun -> fin. {X, Y, Z}
 *   Scenario B : les 2 variables valent null -> rien a colorier -> {}
 *
 * -- Le plan --
 *
 *   1. Un ensemble "atteints" vide et une file "a visiter".
 *   2. Mettre dans la file chaque objet designe par une variable
 *      (ignorer les null).
 *   3. Tant que la file n'est pas vide : prendre un objet ; s'il est
 *      deja atteint, passer ; sinon l'ajouter aux atteints et mettre
 *      dans la file tous les objets de ses champs.
 *   4. Rendre "atteints".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est la boite magique principale ; le TODO 2 l'utilise.
 *
 *
 * ==================================================================
 * TODO 2 : eligibleForGc(allObjects, variables, fields)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tous les objets NON colories sont eligibles.
 *
 * -- Le plan --
 *
 *   1. Calculer les atteignables (TODO 1).
 *   2. Garder, dans l'ordre de allObjects, ceux qui n'y sont pas.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : reachable (TODO 1).
 *
 *
 * Exemple a verifier : les 5 scenarios ci-dessus.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Ces collections sont detaillees au chapitre 9 ; ici on n'en utilise
 *     que quelques methodes : Set<String> seen = new HashSet<>();
 *     Deque<String> toVisit = new ArrayDeque<>(); toVisit.push(x); toVisit.pop(); toVisit.isEmpty()
 *   - for (String target : variables.values()) if (target != null) toVisit.push(target);
 *   - fields.getOrDefault(obj, List.of()) : les champs d'un objet, ou une liste vide
 *   - seen.add(obj) rend false si obj y etait deja
 */
public class Exercise16_GarbageCollectionReachability {

    public static Set<String> reachable(Map<String, String> variables, Map<String, List<String>> fields) {
        throw new UnsupportedOperationException("TODO 1 : implementer reachable()");
    }

    public static List<String> eligibleForGc(List<String> allObjects, Map<String, String> variables,
                                             Map<String, List<String>> fields) {
        throw new UnsupportedOperationException("TODO 2 : implementer eligibleForGc()");
    }

    public static void main(String[] args) {
        Map<String, String> nullVars = new java.util.HashMap<>();
        nullVars.put("n1", null);
        nullVars.put("n2", null);

        ExerciseChecker.check("1 reachable(D) == {X, Y, Z}",
                reachable(Map.of("root", "X"), Map.of("X", List.of("Y"), "Y", List.of("Z"))).equals(Set.of("X", "Y", "Z")));
        ExerciseChecker.check("1 reachable(B) == {} (variables a null)",
                reachable(nullVars, Map.of("N1", List.of("N2"), "N2", List.of("N1"))).isEmpty());

        ExerciseChecker.check("2 scenario A : a = b -> [A]",
                eligibleForGc(List.of("A", "B"), Map.of("a", "B", "b", "B"), Map.of()).equals(List.of("A")));
        ExerciseChecker.check("2 scenario B : ilot N1 <-> N2 sans variable -> [N1, N2]",
                eligibleForGc(List.of("N1", "N2"), nullVars, Map.of("N1", List.of("N2"), "N2", List.of("N1")))
                        .equals(List.of("N1", "N2")));
        ExerciseChecker.check("2 scenario C : variable locale disparue -> [Temp]",
                eligibleForGc(List.of("Temp"), Map.of(), Map.of()).equals(List.of("Temp")));
        ExerciseChecker.check("2 scenario D : chaine X -> Y -> Z vivante, W seul -> [W]",
                eligibleForGc(List.of("X", "Y", "Z", "W"), Map.of("root", "X"),
                        Map.of("X", List.of("Y"), "Y", List.of("Z"))).equals(List.of("W")));
        ExerciseChecker.check("2 scenario E : ilot raccroche a un objet vivant -> []",
                eligibleForGc(List.of("K", "N1", "N2"), Map.of("keep", "K"),
                        Map.of("K", List.of("N1"), "N1", List.of("N2"), "N2", List.of("N1"))).isEmpty());

        ExerciseChecker.summary();
    }
}
