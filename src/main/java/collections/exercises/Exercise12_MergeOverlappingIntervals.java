package collections.exercises;

import collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 12 - Fusionner des intervalles qui se chevauchent (niveau : moyen/difficile)
 * ========================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO : mergeIntervals(intervals)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une regle graduee en heures, posee sur la table, de 0 a 20.
 * Pour chaque reunion prevue dans une salle, tu colles un bout de
 * scotch sur la regle, du debut a la fin de la reunion. Si deux bouts
 * de scotch se touchent ou se chevauchent, ca veut dire que la salle
 * est occupee SANS INTERRUPTION pendant tout ce temps-la : autant les
 * remplacer par UN SEUL long bout de scotch continu, du debut du
 * premier au bout du dernier.
 *
 * -- Essayons a la main --
 *
 * Reunions (debut, fin) : [1,3], [2,6], [8,10], [15,18].
 *
 * Colle les bouts de scotch sur une regle, dans cet ordre, sans les
 * trier d'abord : tu vas vite t'emmeler les crayons. Range-les
 * D'ABORD par heure de debut croissante (ils le sont deja ici) : [1,3],
 * [2,6], [8,10], [15,18].
 *
 * Regarde le 1er bout de scotch : [1,3]. Le 2eme, [2,6], commence a 2,
 * qui est AVANT la fin du 1er bout (3) : ils se chevauchent, donc ils
 * ne font plus qu'un seul bout, de 1 a 6 (le plus grand des deux
 * fins). Le 3eme, [8,10], commence a 8, largement APRES la fin du bout
 * fusionne (6) : pas de chevauchement, on cloture le bout precedent
 * ([1,6]) et [8,10] devient un nouveau bout a part. Le 4eme, [15,18],
 * commence apres la fin de [8,10] (10) : encore un nouveau bout a
 * part.
 *
 * Resultat : [1,6], [8,10], [15,18].
 *
 * -- Ce qu'on remarque --
 *
 * Une fois les reunions triees par heure de DEBUT, on n'a plus jamais
 * besoin de comparer une reunion a toutes les autres : il suffit de la
 * comparer au SEUL bout de scotch "en cours de collage". Si ca colle
 * (chevauchement ou contact), on agrandit ce bout. Sinon, on le
 * cloture et on en commence un nouveau. Trier d'abord est ce qui rend
 * tout le reste tres simple.
 *
 * -- Le plan --
 *
 *   1. Trier les intervalles par debut croissant.
 *   2. Commencer avec le premier intervalle trie comme "intervalle en
 *      cours".
 *   3. Pour chaque intervalle suivant : s'il commence avant ou juste a
 *      la fin de l'intervalle en cours, les fusionner (la fin de
 *      l'intervalle en cours devient le plus grand des deux fins).
 *      Sinon, ranger l'intervalle en cours dans le resultat, et faire
 *      de l'intervalle suivant le nouvel "intervalle en cours".
 *   4. A la toute fin, ne pas oublier de ranger le dernier intervalle
 *      en cours dans le resultat (personne ne l'a cloture pour toi).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * L'etape 1 (trier) est un outil du JDK, pas besoin de le refabriquer.
 * Les etapes 2 a 4 forment un seul parcours logique et continu : les
 * separer en plusieurs methodes casserait le fil du raisonnement pour
 * rien, elles restent dans mergeIntervals().
 *
 * Exemple a verifier : [[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]
 *
 * Piege a tester en second : que se passe-t-il si un intervalle est
 * ENTIEREMENT contenu dans le precedent, comme [1,10] suivi de [2,5] ?
 * La fin du bout fusionne ne doit PAS redescendre a 5 : il faut garder
 * le PLUS GRAND des deux fins (10), pas juste la fin du dernier
 * intervalle regarde.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Trier par debut croissant : intervals.sort((a, b) -> a.start - b.start);
 *     (ou Comparator.comparingInt si vous preferez l'ecrire ainsi)
 *   - "commence avant ou juste a la fin de l'intervalle en cours" :
 *     next.start <= current.end
 *   - Fusionner : current.end = Math.max(current.end, next.end);
 *     (le piege du "contenu entierement" ci-dessus se resout avec ce
 *     Math.max, JAMAIS avec une simple affectation current.end = next.end)
 *   - Interval implemente equals()/hashCode() comme le Point de
 *     l'exercice 2, pour que les comparaisons de listes dans les tests
 *     fonctionnent directement.
 */
public class Exercise12_MergeOverlappingIntervals {

    static final class Interval {
        final int start;
        final int end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Interval)) {
                return false;
            }
            Interval other = (Interval) o;
            return this.start == other.start && this.end == other.end;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(start, end);
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    public static List<Interval> mergeIntervals(List<Interval> intervals) {
        throw new UnsupportedOperationException("TODO : implementer mergeIntervals()");
    }

    public static void main(String[] args) {
        List<Interval> meetings = Arrays.asList(
                new Interval(1, 3), new Interval(2, 6), new Interval(8, 10), new Interval(15, 18));
        List<Interval> merged = mergeIntervals(meetings);
        ExerciseChecker.check("mergeIntervals([1,3],[2,6],[8,10],[15,18]) == [1,6],[8,10],[15,18]",
                merged.equals(Arrays.asList(new Interval(1, 6), new Interval(8, 10), new Interval(15, 18))));

        List<Interval> contained = Arrays.asList(new Interval(1, 10), new Interval(2, 5));
        List<Interval> mergedContained = mergeIntervals(contained);
        ExerciseChecker.check("un intervalle entierement contenu dans un autre ne fait pas redescendre la fin -> [1,10]",
                mergedContained.equals(new ArrayList<>(Arrays.asList(new Interval(1, 10)))));

        List<Interval> noOverlap = Arrays.asList(new Interval(1, 2), new Interval(5, 6));
        ExerciseChecker.check("aucun chevauchement -> intervalles inchanges",
                mergeIntervals(noOverlap).equals(Arrays.asList(new Interval(1, 2), new Interval(5, 6))));

        ExerciseChecker.summary();
    }
}