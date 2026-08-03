package collections.exercises;

import collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * EXERCICE 14 - Implementer Iterable/Iterator soi-meme (niveau : difficile)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * Jusqu'ici, dans tous les exercices precedents, on a toujours UTILISE
 * les iterateurs du JDK (le for-each sur une List, un Set, une Map...)
 * sans jamais se demander comment ils fonctionnent par en-dessous. Cet
 * exercice inverse les roles : c'est TOI qui fabriques la boite
 * magique qui rend un for-each possible sur TA PROPRE structure.
 *
 *
 * ==================================================================
 * TODO : ZigzagIterable<T> (implements Iterable<T>)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un marche avec plusieurs stands en file : le stand des
 * fruits, le stand des legumes, le stand du poisson. Chaque stand a sa
 * propre file de clients qui attendent, dans l'ordre. Un vigile fait
 * la ronde : il sert UN client du stand des fruits, puis UN client du
 * stand des legumes, puis UN client du stand du poisson, puis il
 * revient au stand des fruits, et ainsi de suite - jamais deux clients
 * du meme stand d'affilee, tant qu'il reste des clients ailleurs.
 *
 * Si un stand n'a plus de clients (sa file est vide), le vigile ne
 * s'arrete pas devant : il passe directement au stand suivant qui a
 * encore du monde, sans perdre de tour pour les autres.
 *
 * -- Essayons a la main --
 *
 * Stand fruits : [1, 2, 3]. Stand legumes : [4, 5, 6, 7]. Stand
 * poisson : [8, 9].
 *
 * Tour 1 : fruits->1, legumes->4, poisson->8. Servis dans cet ordre :
 * 1, 4, 8.
 * Tour 2 : fruits->2, legumes->5, poisson->9. Servis : 2, 5, 9. (le
 * stand poisson vient de se vider apres ce tour)
 * Tour 3 : fruits->3, legumes->6, poisson (vide, on saute directement
 * au suivant). Servis : 3, 6.
 * Tour 4 : fruits (vide, on saute), legumes->7, poisson (vide, on
 * saute). Servi : 7.
 * Plus aucun stand n'a de client : fini.
 *
 * Ordre complet des clients servis : 1, 4, 8, 2, 5, 9, 3, 6, 7.
 *
 * -- Ce qu'on remarque --
 *
 * Un iterateur, en vrai, ne fait toujours que repondre a DEUX
 * questions, encore et encore : "hasNext() : reste-t-il quelqu'un a
 * servir, ou plus personne nulle part ?" et "next() : donne-moi le
 * PROCHAIN a servir (et souviens-toi d'ou tu en es pour la prochaine
 * fois qu'on te demandera)". Le vigile n'a besoin de se souvenir que
 * de DEUX choses entre deux appels : "a quel stand j'en etais" (pour
 * tourner en rond entre les stands), et "combien de clients j'ai deja
 * servi a CHAQUE stand" (pour savoir lequel prendre ensuite dans
 * chaque file).
 *
 * -- Le plan --
 *
 *   1. Se souvenir, pour chaque stand (chaque sous-liste), combien de
 *      clients ont deja ete servis dedans (un compteur par stand).
 *   2. Se souvenir de quel stand on doit regarder en premier au
 *      prochain appel de next() (un pointeur "stand courant" qui
 *      tourne en rond entre les stands, 0, 1, 2, 0, 1, 2...).
 *   3. hasNext() : reste-t-il, sur AU MOINS un stand, des clients pas
 *      encore servis (compteur de ce stand < taille de ce stand) ?
 *   4. next() : a partir du "stand courant", avancer de stand en
 *      stand (en tournant en rond) jusqu'a en trouver un qui n'est
 *      pas encore vide ; y prendre le prochain client (grace a son
 *      compteur), avancer ce compteur de 1, et deplacer le pointeur
 *      "stand courant" sur le stand JUSTE APRES celui qu'on vient de
 *      servir (pour respecter le tour de ronde au prochain appel).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : hasNext() et next() sont deux methodes IMPOSEES par
 * l'interface Iterator<T> elle-meme - ce ne sont pas des etapes qu'on
 * choisit de decouper, c'est le contrat que Java demande de respecter
 * pour que le for-each fonctionne sur ta structure.
 *
 * Exemple a verifier : ZigzagIterable des listes [1,2,3], [4,5,6,7],
 * [8,9] parcourue dans un for-each donne : 1, 4, 8, 2, 5, 9, 3, 6, 7.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - iterator() renvoie un nouvel objet anonyme (ou une classe interne
 *     privee) qui implemente Iterator<T>, avec ses PROPRES compteurs
 *     (int[] indexInEachList, un par sous-liste, et int currentList).
 *   - hasNext() : parcourir tous les indices et verifier si au moins un
 *     indexInEachList[i] < lists.get(i).size().
 *   - next() : tant que la sous-liste pointee par currentList est deja
 *     epuisee (indexInEachList[currentList] >= sa taille), avancer
 *     currentList = (currentList + 1) % lists.size(). Une fois une
 *     sous-liste non epuisee trouvee, lire son element courant, faire
 *     indexInEachList[currentList]++, puis faire tourner currentList
 *     d'un cran de plus AVANT de retourner la valeur lue.
 *   - Si next() est appele alors que hasNext() aurait repondu false,
 *     lancer un java.util.NoSuchElementException (c'est le contrat de
 *     l'interface Iterator).
 */
public class Exercise14_ZigzagIterator {

    static final class ZigzagIterable<T> implements Iterable<T> {
        private final List<List<T>> lists;

        ZigzagIterable(List<List<T>> lists) {
            this.lists = lists;
        }

        @Override
        public Iterator<T> iterator() {
            throw new UnsupportedOperationException("TODO : implementer iterator()");
        }
    }

    public static void main(String[] args) {
        List<List<Integer>> stands = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6, 7),
                Arrays.asList(8, 9));

        List<Integer> served = new ArrayList<>();
        for (Integer client : new ZigzagIterable<>(stands)) {
            served.add(client);
        }

        ExerciseChecker.check("zigzag sur [1,2,3],[4,5,6,7],[8,9] == [1,4,8,2,5,9,3,6,7]",
                served.equals(Arrays.asList(1, 4, 8, 2, 5, 9, 3, 6, 7)));

        List<List<String>> withEmptyStand = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList(),
                Arrays.asList("c"));
        List<String> servedWithEmpty = new ArrayList<>();
        for (String client : new ZigzagIterable<>(withEmptyStand)) {
            servedWithEmpty.add(client);
        }
        ExerciseChecker.check("un stand vide est saute sans casser la ronde -> [a, c, b]",
                servedWithEmpty.equals(Arrays.asList("a", "c", "b")));

        ExerciseChecker.summary();
    }
}
