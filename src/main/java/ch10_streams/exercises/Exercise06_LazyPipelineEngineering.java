package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * EXERCICE 6 - Exploiter la paresse : traces, compteurs, streams infinis qui SE TERMINENT (niveau : difficile)
 * ==========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Ce que main() va mesurer --
 *
 * Ici, il ne suffit pas de rendre le bon RESULTAT : main() verifie
 * aussi COMBIEN de fois tes lambdas sont appelees, et dans QUEL ORDRE
 * (grace a des compteurs et des listes "trace"). Un pipeline correct
 * mais qui travaille pour rien echouera aux tests. Les 4 regles a
 * appliquer :
 *
 *   - Les elements traversent le pipeline UN PAR UN, de haut en bas.
 *   - findFirst / anyMatch / limit / takeWhile arretent tout des qu'ils
 *     ont leur reponse.
 *   - sorted() doit voir TOUS les elements avant d'en laisser sortir un.
 *   - Un stream infini se termine SEULEMENT si une operation
 *     court-circuit est garantie d'etre satisfaite un jour.
 *
 *
 * ==================================================================
 * TODO 1 : isPrime(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un nombre premier ne se partage equitablement qu'entre 1 et
 * lui-meme. Pour le verifier, on essaie les diviseurs de 2 jusqu'a sa
 * racine carree (au-dela, on retomberait sur les memes paires). Si
 * AUCUN ne divise n, il est premier. "Aucun" = noneMatch, qui
 * s'arrete des le premier diviseur trouve.
 *
 * -- Essayons a la main --
 *
 *   1  -> pas premier (par definition, n < 2)
 *   2  -> diviseurs a tester : aucun (racine de 2 < 2) -> premier
 *   9  -> teste 2 (non), 3 (oui !) -> pas premier
 *   13 -> teste 2, 3 (racine de 13 = 3.6) -> aucun -> premier
 *
 * -- Le plan --
 *
 *   1. Si n < 2 : faux.
 *   2. Sinon : vrai si aucun d de 2 a racine(n) (inclus) ne divise n.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'EST une boite magique, reutilisee par les TODO 2 et 3.
 *
 *
 * ==================================================================
 * TODO 2 : firstNPrimes(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ne sait pas jusqu'ou chercher pour trouver n nombres premiers.
 * Alors on part d'une suite INFINIE 2, 3, 4, 5, ... et on laisse limit
 * couper le robinet des qu'on en a n. Grace a la paresse, on ne teste
 * que les nombres necessaires.
 *
 * -- Essayons a la main --
 *
 *   n = 6 -> 2, 3, (4), 5, (6), 7, (8, 9, 10), 11, (12), 13 -> [2, 3, 5, 7, 11, 13]
 *
 * -- Le plan --
 *
 *   1. Suite infinie d'entiers a partir de 2.
 *   2. Garder les premiers (TODO 1).
 *   3. En prendre n, rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isPrime (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : firstPrimeAbove(threshold, tested)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche le premier nombre premier STRICTEMENT plus grand que
 * threshold. En plus, on doit noter dans tested[0] combien de
 * candidats ont ete TESTES. S'il est bien paresseux, ton pipeline ne
 * teste que les candidats jusqu'au bon.
 *
 * Remarque : une lambda ne peut pas faire "compteur++" sur un int local,
 * mais tested est un TABLEAU : tested[0]++ modifie une case, pas la
 * variable. C'est l'astuce classique.
 *
 * -- Essayons a la main --
 *
 *   threshold = 20 -> 21 (non), 22 (non), 23 (oui) -> 23, tested[0] == 3
 *   threshold = 13 -> 14, 15, 16, 17 -> 17, tested[0] == 4
 *
 * -- Le plan --
 *
 *   1. Suite infinie a partir de threshold + 1.
 *   2. Filtre qui incremente tested[0] PUIS rend isPrime(x).
 *   3. Premier element trouve.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isPrime.
 *
 *
 * ==================================================================
 * TODO 4 : firstLongUppercase(words, minLength, trace)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche le premier mot d'au moins minLength lettres, en
 * majuscules. Ton filter doit ajouter "f:" + mot a trace, ton map doit
 * ajouter "m:" + mot. main() compare la trace EXACTE : elle prouve que
 * chaque mot descend tout le pipeline avant que le suivant commence,
 * que les mots rejetes ne sont jamais mappes, et que rien n'est
 * regarde apres le mot trouve.
 *
 * -- Essayons a la main --
 *
 *   words = [a, bb, cccc, ddddd], minLength = 3
 *   a    : f:a (rejete)
 *   bb   : f:bb (rejete)
 *   cccc : f:cccc (accepte) -> m:cccc -> findFirst a sa reponse, STOP
 *   ddddd : jamais regarde
 *   trace == [f:a, f:bb, f:cccc, m:cccc], resultat Optional[CCCC]
 *
 * -- Le plan --
 *
 *   1. Filtrer (en tracant) les mots assez longs.
 *   2. Mapper (en tracant) en majuscules.
 *   3. Premier resultat.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : smallestKWithTrace(values, k, trace)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut les k plus petits nombres. Un peek AVANT sorted ajoute
 * "in:" + x, un peek APRES sorted ajoute "out:" + x. La trace va
 * montrer la "barriere" de sorted : TOUS les "in:" d'abord (il doit
 * tout voir), puis seulement k "out:" (limit coupe ensuite).
 *
 * -- Essayons a la main --
 *
 *   values = [5, 3, 8, 1], k = 2
 *   trace  == [in:5, in:3, in:8, in:1, out:1, out:3]
 *   resultat == [1, 3]
 *
 * -- Le plan --
 *
 *   1. Tracer l'entree, trier, tracer la sortie, limiter a k.
 *   2. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : distinctFromCycle(cycleLength, wanted)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La suite 1, 2, ..., cycleLength, 1, 2, ... tourne en rond pour
 * toujours. On veut ses wanted premieres valeurs DIFFERENTES. Si on
 * ecrit naivement distinct().limit(wanted) avec wanted > cycleLength,
 * distinct attend une valeur nouvelle qui n'arrivera JAMAIS : le
 * programme tourne a l'infini. Ta mission : que la methode se termine
 * TOUJOURS, en ne demandant jamais plus de valeurs distinctes qu'il
 * n'en existe.
 *
 * -- Essayons a la main --
 *
 *   (3, 2) -> 1, 2 -> [1, 2]
 *   (3, 5) -> il n'y a que 3 valeurs distinctes -> [1, 2, 3] (et ca se termine !)
 *   (4, 4) -> [1, 2, 3, 4]
 *
 * -- Le plan --
 *
 *   1. Suite infinie : graine 1, suivant = x % cycleLength + 1.
 *   2. Retirer les doublons.
 *   3. Limiter au plus petit entre wanted et cycleLength.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Si tu lances main() et qu'il ne se termine jamais : c'est
 * l'etape 3 qui manque. Arrete le programme avec le bouton Stop.)
 *
 *
 * ==================================================================
 * TODO 7 : firstRepeated(words, visited)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche le premier mot qui apparait pour la 2e fois. Un Set
 * "deja vus" sert de memoire : Set.add rend false si l'element y etait
 * DEJA. Un filter qui garde les mots pour lesquels add rend false,
 * suivi de findFirst, s'arrete au premier doublon. On note chaque mot
 * examine dans visited : les mots APRES le premier doublon ne doivent
 * jamais etre examines.
 *
 * (Une lambda qui modifie un Set exterieur est "a etat" : acceptable
 * ici car le stream est sequentiel, mais a eviter en parallele.)
 *
 * -- Essayons a la main --
 *
 *   [a, b, c, b, a, d] -> a (nouveau), b (nouveau), c (nouveau),
 *                          b (DEJA VU) -> STOP -> Optional[b]
 *   visited == [a, b, c, b]
 *   [x, y] -> aucun doublon -> vide, visited == [x, y]
 *
 * -- Le plan --
 *
 *   1. Creer un Set vide "deja vus".
 *   2. Pour chaque mot : l'ajouter a visited, puis le garder seulement
 *      si l'ajout au Set echoue.
 *   3. Premier mot garde.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier :
 *
 *   isPrime : 1 -> faux, 2 -> vrai, 9 -> faux, 13 -> vrai, 97 -> vrai, 100 -> faux
 *   firstNPrimes(6) == [2, 3, 5, 7, 11, 13] ; firstNPrimes(0) == []
 *   firstPrimeAbove(20) == 23 avec tested == 3 ; firstPrimeAbove(13) == 17 avec tested == 4
 *   firstLongUppercase([a, bb, cccc, ddddd], 3) == Optional[CCCC],
 *     trace == [f:a, f:bb, f:cccc, m:cccc]
 *   firstLongUppercase([a, bb], 3) vide, trace == [f:a, f:bb]
 *   smallestKWithTrace([5, 3, 8, 1], 2) == [1, 3],
 *     trace == [in:5, in:3, in:8, in:1, out:1, out:3]
 *   distinctFromCycle(3, 2) == [1, 2] ; (3, 5) == [1, 2, 3] ; (4, 4) == [1, 2, 3, 4]
 *   firstRepeated([a, b, c, b, a, d]) == Optional[b], visited == [a, b, c, b]
 *   firstRepeated([x, y]) vide, visited == [x, y]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0)
 *   - IntStream.iterate(2, x -> x + 1).filter(Exercise06_LazyPipelineEngineering::isPrime)
 *       .limit(n).boxed().toList()
 *   - IntStream.iterate(threshold + 1, x -> x + 1)
 *       .filter(x -> { tested[0]++; return isPrime(x); }).findFirst().getAsInt()
 *   - .filter(w -> { trace.add("f:" + w); return w.length() >= minLength; })
 *   - .peek(x -> trace.add("in:" + x)).sorted().peek(x -> trace.add("out:" + x)).limit(k)
 *   - Stream.iterate(1, x -> x % cycleLength + 1).distinct().limit(Math.min(wanted, cycleLength))
 *   - Set<String> seen = new HashSet<>(); ... .filter(w -> !seen.add(w))
 */
public class Exercise06_LazyPipelineEngineering {

    public static boolean isPrime(int n) {
        throw new UnsupportedOperationException("TODO 1 : implementer isPrime()");
    }

    public static List<Integer> firstNPrimes(int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer firstNPrimes()");
    }

    public static int firstPrimeAbove(int threshold, int[] tested) {
        throw new UnsupportedOperationException("TODO 3 : implementer firstPrimeAbove()");
    }

    public static Optional<String> firstLongUppercase(List<String> words, int minLength, List<String> trace) {
        throw new UnsupportedOperationException("TODO 4 : implementer firstLongUppercase()");
    }

    public static List<Integer> smallestKWithTrace(List<Integer> values, int k, List<String> trace) {
        throw new UnsupportedOperationException("TODO 5 : implementer smallestKWithTrace()");
    }

    public static List<Integer> distinctFromCycle(int cycleLength, int wanted) {
        throw new UnsupportedOperationException("TODO 6 : implementer distinctFromCycle()");
    }

    public static Optional<String> firstRepeated(List<String> words, List<String> visited) {
        throw new UnsupportedOperationException("TODO 7 : implementer firstRepeated()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("isPrime(1) == false", !isPrime(1));
        ExerciseChecker.check("isPrime(2) == true", isPrime(2));
        ExerciseChecker.check("isPrime(9) == false", !isPrime(9));
        ExerciseChecker.check("isPrime(13) == true", isPrime(13));
        ExerciseChecker.check("isPrime(97) == true", isPrime(97));
        ExerciseChecker.check("isPrime(100) == false", !isPrime(100));

        ExerciseChecker.check("firstNPrimes(6) == [2, 3, 5, 7, 11, 13]",
                firstNPrimes(6).equals(List.of(2, 3, 5, 7, 11, 13)));
        ExerciseChecker.check("firstNPrimes(0) == []", firstNPrimes(0).isEmpty());

        int[] tested = {0};
        ExerciseChecker.check("firstPrimeAbove(20) == 23", firstPrimeAbove(20, tested) == 23);
        ExerciseChecker.check("... en testant exactement 3 candidats (21, 22, 23)", tested[0] == 3);
        tested[0] = 0;
        ExerciseChecker.check("firstPrimeAbove(13) == 17", firstPrimeAbove(13, tested) == 17);
        ExerciseChecker.check("... en testant exactement 4 candidats", tested[0] == 4);

        List<String> trace = new ArrayList<>();
        ExerciseChecker.check("firstLongUppercase([a, bb, cccc, ddddd], 3) == CCCC",
                firstLongUppercase(List.of("a", "bb", "cccc", "ddddd"), 3, trace).equals(Optional.of("CCCC")));
        ExerciseChecker.check("trace == [f:a, f:bb, f:cccc, m:cccc]",
                trace.equals(List.of("f:a", "f:bb", "f:cccc", "m:cccc")));
        trace.clear();
        ExerciseChecker.check("firstLongUppercase([a, bb], 3) est vide",
                firstLongUppercase(List.of("a", "bb"), 3, trace).isEmpty());
        ExerciseChecker.check("trace == [f:a, f:bb]", trace.equals(List.of("f:a", "f:bb")));

        trace.clear();
        ExerciseChecker.check("smallestKWithTrace([5, 3, 8, 1], 2) == [1, 3]",
                smallestKWithTrace(List.of(5, 3, 8, 1), 2, trace).equals(List.of(1, 3)));
        ExerciseChecker.check("trace == [in:5, in:3, in:8, in:1, out:1, out:3]",
                trace.equals(List.of("in:5", "in:3", "in:8", "in:1", "out:1", "out:3")));

        ExerciseChecker.check("distinctFromCycle(3, 2) == [1, 2]", distinctFromCycle(3, 2).equals(List.of(1, 2)));
        ExerciseChecker.check("distinctFromCycle(3, 5) == [1, 2, 3] (et se termine)",
                distinctFromCycle(3, 5).equals(List.of(1, 2, 3)));
        ExerciseChecker.check("distinctFromCycle(4, 4) == [1, 2, 3, 4]",
                distinctFromCycle(4, 4).equals(List.of(1, 2, 3, 4)));

        List<String> visited = new ArrayList<>();
        ExerciseChecker.check("firstRepeated([a, b, c, b, a, d]) == b",
                firstRepeated(List.of("a", "b", "c", "b", "a", "d"), visited).equals(Optional.of("b")));
        ExerciseChecker.check("visited == [a, b, c, b] (rien apres le 1er doublon)",
                visited.equals(List.of("a", "b", "c", "b")));
        visited.clear();
        ExerciseChecker.check("firstRepeated([x, y]) est vide", firstRepeated(List.of("x", "y"), visited).isEmpty());
        ExerciseChecker.check("visited == [x, y]", visited.equals(List.of("x", "y")));

        ExerciseChecker.summary();
    }
}
