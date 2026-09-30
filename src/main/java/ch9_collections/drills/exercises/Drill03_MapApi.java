package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DRILL 03 - L'API de Map (HashMap, LinkedHashMap, TreeMap)
 * =========================================================
 *
 * Mode d'emploi : voir Drill01_ListAndSet. Donnees : Pantry.prices() =
 * {kiwi=3, pomme=2, banane=1, cerise=6, abricot=4} (une nouvelle map a
 * chaque appel : tu peux la modifier) et Pantry.FRUITS.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : kiwiPrice()          [get] -> 3.
 * TODO 2  : mangoOrZero()        [getOrDefault] prix de "mangue", 0 si absent -> 0.
 * TODO 3  : putReturns()         [put rend l'ANCIENNE valeur] prices().put("kiwi", 5) -> 3.
 * TODO 4  : sizeAfterPutIfAbsent() [putIfAbsent] ajouter mangue=7 puis kiwi=9 ; rendre la taille -> 6.
 * TODO 5  : counts()             [merge] nombre de chaque fruit de FRUITS, trie par nom (TreeMap).
 * TODO 6  : byFirstLetter()      [computeIfAbsent] FRUITS ranges par 1re lettre (TreeMap<Character, List<String>>).
 * TODO 7  : doubled()            [replaceAll] tous les prix multiplies par 2.
 * TODO 8  : withoutBanana()      [computeIfPresent qui rend null] supprimer banane si son prix < 2 ; rendre les cles.
 * TODO 9  : total()              [values()] la somme des prix -> 16.
 * TODO 10 : describe()           [entrySet, getKey, getValue] "kiwi=3;pomme=2;banane=1;cerise=6;abricot=4;".
 * TODO 11 : atOrAbove5()         [TreeMap.ceilingEntry] map prix -> fruit ; le fruit du plus petit prix >= 5 -> cerise.
 * TODO 12 : firstAndLast()       [TreeMap firstKey / lastKey] cles triees : "abricot-pomme".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   put / putIfAbsent / remove / replace   rendent l'ANCIENNE valeur (ou null)
 *   compute / computeIfAbsent / computeIfPresent / merge   rendent la NOUVELLE valeur
 *   merge(k, v, f)            absent -> v ; present -> f(ancien, v) ; f rend null -> cle supprimee
 *   computeIfAbsent(k, k -> new ArrayList<>()).add(x)    le patron "grouper"
 *   replaceAll((k, v) -> ...)  keySet() / values() / entrySet() : vues sur la map
 *   HashMap (aucun ordre) / LinkedHashMap (ordre d'ajout) / TreeMap (cles triees)
 *   TreeMap : firstKey lastKey floorKey ceilingKey headMap(k) [exclu] tailMap(k) [inclus] descendingMap
 *   Map.of(...) immuable, refuse null et les cles en double (IllegalArgumentException)
 * ---------------------------------------------------------------------
 */
public class Drill03_MapApi {

    public static int kiwiPrice() {
        throw new UnsupportedOperationException("TODO 1 : implementer kiwiPrice()");
    }

    public static int mangoOrZero() {
        throw new UnsupportedOperationException("TODO 2 : implementer mangoOrZero()");
    }

    public static Integer putReturns() {
        throw new UnsupportedOperationException("TODO 3 : implementer putReturns()");
    }

    public static int sizeAfterPutIfAbsent() {
        throw new UnsupportedOperationException("TODO 4 : implementer sizeAfterPutIfAbsent()");
    }

    public static Map<String, Integer> counts() {
        throw new UnsupportedOperationException("TODO 5 : implementer counts()");
    }

    public static Map<Character, List<String>> byFirstLetter() {
        throw new UnsupportedOperationException("TODO 6 : implementer byFirstLetter()");
    }

    public static Map<String, Integer> doubled() {
        throw new UnsupportedOperationException("TODO 7 : implementer doubled()");
    }

    public static Set<String> withoutBanana() {
        throw new UnsupportedOperationException("TODO 8 : implementer withoutBanana()");
    }

    public static int total() {
        throw new UnsupportedOperationException("TODO 9 : implementer total()");
    }

    public static String describe() {
        throw new UnsupportedOperationException("TODO 10 : implementer describe()");
    }

    public static String atOrAbove5() {
        throw new UnsupportedOperationException("TODO 11 : implementer atOrAbove5()");
    }

    public static String firstAndLast() {
        throw new UnsupportedOperationException("TODO 12 : implementer firstAndLast()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  kiwiPrice == 3", kiwiPrice() == 3);
        ExerciseChecker.check("2  mangoOrZero == 0", mangoOrZero() == 0);
        ExerciseChecker.check("3  putReturns == 3 (l'ancienne valeur)", putReturns() == 3);
        ExerciseChecker.check("4  sizeAfterPutIfAbsent == 6", sizeAfterPutIfAbsent() == 6);
        ExerciseChecker.check("5  counts", counts().toString().equals("{abricot=1, banane=1, cerise=1, kiwi=2, pomme=2}"));
        ExerciseChecker.check("6  byFirstLetter",
                byFirstLetter().toString().equals("{a=[abricot], b=[banane], c=[cerise], k=[kiwi, kiwi], p=[pomme, pomme]}"));
        ExerciseChecker.check("7  doubled", doubled().toString().equals("{kiwi=6, pomme=4, banane=2, cerise=12, abricot=8}"));
        ExerciseChecker.check("8  withoutBanana", withoutBanana().toString().equals("[kiwi, pomme, cerise, abricot]"));
        ExerciseChecker.check("9  total == 16", total() == 16);
        ExerciseChecker.check("10 describe", describe().equals("kiwi=3;pomme=2;banane=1;cerise=6;abricot=4;"));
        ExerciseChecker.check("11 atOrAbove5 == cerise", atOrAbove5().equals("cerise"));
        ExerciseChecker.check("12 firstAndLast == abricot-pomme", firstAndLast().equals("abricot-pomme"));

        ExerciseChecker.summary();
    }
}
