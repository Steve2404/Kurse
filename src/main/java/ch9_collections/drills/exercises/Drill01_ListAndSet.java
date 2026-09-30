package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;
import ch9_collections.drills.Pantry;

import java.util.List;
import java.util.Set;

/**
 * DRILL 01 - List et Set : les methodes a connaitre par coeur
 * ===========================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en une a trois lignes
 * et vise UNE methode precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch9_collections.drills.Pantry. Pantry.FRUITS est IMMUABLE
 * (List.of) : pour modifier, travaille sur une copie new ArrayList<>(...).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : firstTwo()          [subList] les 2 premiers fruits -> [kiwi, pomme].
 * TODO 2  : withMango()         [add(index, e)] une copie avec "mangue" a l'index 1.
 * TODO 3  : removeTwice()       [remove(int) puis remove(Object)] copie de NUMBERS, retirer l'INDEX 1 puis la VALEUR 1 -> [5, 8, 9, 2].
 * TODO 4  : lastKiwi()          [lastIndexOf] -> 3.
 * TODO 5  : withoutShort()      [removeIf] une copie sans les fruits de moins de 5 lettres.
 * TODO 6  : shouting()          [replaceAll] une copie en majuscules.
 * TODO 7  : distinctInOrder()   [LinkedHashSet] sans doublon, ordre d'arrivee.
 * TODO 8  : distinctSorted()    [TreeSet] sans doublon, ordre alphabetique.
 * TODO 9  : addTwice()          [Set.add rend un boolean] ajouter "kiwi" deux fois a un HashSet vide -> "true false".
 * TODO 10 : common()            [retainAll] les fruits aussi presents dans {pomme, mangue, kiwi}, tries -> [kiwi, pomme].
 * TODO 11 : beforeKiwi()        [headSet] les fruits tries strictement avant "kiwi" -> [abricot, banane, cerise].
 * TODO 12 : addToListOf()       [List.of est immuable] essayer List.of("a").add("b") et rendre le nom simple de l'exception.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   list.subList(from, to)            to EXCLU ; c'est une VUE sur la liste
 *   list.add(i, e) / set(i, e)        inserer (decale) / remplacer (ne decale pas)
 *   remove(1) sur List<Integer>       retire l'INDEX 1 ; remove(Integer.valueOf(1)) retire la VALEUR
 *   indexOf / lastIndexOf             -1 si absent
 *   removeIf(pred) / replaceAll(op)   Collection / List ; modifient sur place
 *   HashSet (aucun ordre) / LinkedHashSet (ordre d'ajout) / TreeSet (trie)
 *   set.add(e)                        false si deja present
 *   retainAll(c) = intersection ; addAll = union ; removeAll = difference
 *   TreeSet : first() last() headSet(x) [exclu] tailSet(x) [inclus] floor ceiling lower higher
 *   List.of / Set.of / Map.of         immuables -> UnsupportedOperationException ; refusent null
 * ---------------------------------------------------------------------
 */
public class Drill01_ListAndSet {

    public static List<String> firstTwo() {
        throw new UnsupportedOperationException("TODO 1 : implementer firstTwo()");
    }

    public static List<String> withMango() {
        throw new UnsupportedOperationException("TODO 2 : implementer withMango()");
    }

    public static List<Integer> removeTwice() {
        throw new UnsupportedOperationException("TODO 3 : implementer removeTwice()");
    }

    public static int lastKiwi() {
        throw new UnsupportedOperationException("TODO 4 : implementer lastKiwi()");
    }

    public static List<String> withoutShort() {
        throw new UnsupportedOperationException("TODO 5 : implementer withoutShort()");
    }

    public static List<String> shouting() {
        throw new UnsupportedOperationException("TODO 6 : implementer shouting()");
    }

    public static Set<String> distinctInOrder() {
        throw new UnsupportedOperationException("TODO 7 : implementer distinctInOrder()");
    }

    public static Set<String> distinctSorted() {
        throw new UnsupportedOperationException("TODO 8 : implementer distinctSorted()");
    }

    public static String addTwice() {
        throw new UnsupportedOperationException("TODO 9 : implementer addTwice()");
    }

    public static Set<String> common() {
        throw new UnsupportedOperationException("TODO 10 : implementer common()");
    }

    public static Set<String> beforeKiwi() {
        throw new UnsupportedOperationException("TODO 11 : implementer beforeKiwi()");
    }

    public static String addToListOf() {
        throw new UnsupportedOperationException("TODO 12 : implementer addToListOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  firstTwo", firstTwo().equals(List.of("kiwi", "pomme")));
        ExerciseChecker.check("2  withMango", withMango().equals(List.of("kiwi", "mangue", "pomme", "banane", "kiwi", "cerise", "pomme", "abricot")));
        ExerciseChecker.check("3  removeTwice == [5, 8, 9, 2]", removeTwice().equals(List.of(5, 8, 9, 2)));
        ExerciseChecker.check("4  lastKiwi == 3", lastKiwi() == 3);
        ExerciseChecker.check("5  withoutShort", withoutShort().equals(List.of("pomme", "banane", "cerise", "pomme", "abricot")));
        ExerciseChecker.check("6  shouting", shouting().equals(List.of("KIWI", "POMME", "BANANE", "KIWI", "CERISE", "POMME", "ABRICOT")));
        ExerciseChecker.check("7  distinctInOrder", distinctInOrder().toString().equals("[kiwi, pomme, banane, cerise, abricot]"));
        ExerciseChecker.check("8  distinctSorted", distinctSorted().toString().equals("[abricot, banane, cerise, kiwi, pomme]"));
        ExerciseChecker.check("9  addTwice == true false", addTwice().equals("true false"));
        ExerciseChecker.check("10 common == [kiwi, pomme]", common().toString().equals("[kiwi, pomme]"));
        ExerciseChecker.check("11 beforeKiwi == [abricot, banane, cerise]", beforeKiwi().toString().equals("[abricot, banane, cerise]"));
        ExerciseChecker.check("12 addToListOf == UnsupportedOperationException", addToListOf().equals("UnsupportedOperationException"));
        ExerciseChecker.check("   Pantry.FRUITS intact", Pantry.FRUITS.size() == 7);

        ExerciseChecker.summary();
    }
}
