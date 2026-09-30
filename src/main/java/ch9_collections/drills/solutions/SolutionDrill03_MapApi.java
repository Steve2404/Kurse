package ch9_collections.drills.solutions;

import ch9_collections.drills.Pantry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill03_MapApi.
 */
public class SolutionDrill03_MapApi {

    public static int kiwiPrice() {
        // get rend un Integer ; l'unboxing vers int lancerait NullPointerException si la cle manquait.
        return Pantry.prices().get("kiwi");
    }

    public static int mangoOrZero() {
        // getOrDefault evite le null (et donc le piege de l'unboxing).
        return Pantry.prices().getOrDefault("mangue", 0);
    }

    public static Integer putReturns() {
        // put remplace ET rend l'ancienne valeur.
        return Pantry.prices().put("kiwi", 5);
    }

    public static int sizeAfterPutIfAbsent() {
        // putIfAbsent n'ecrase pas kiwi : seule mangue s'ajoute.
        Map<String, Integer> prices = Pantry.prices();
        prices.putIfAbsent("mangue", 7);
        prices.putIfAbsent("kiwi", 9);
        return prices.size();
    }

    public static Map<String, Integer> counts() {
        // merge : 1 la premiere fois, ancien + 1 ensuite.
        Map<String, Integer> counts = new TreeMap<>();
        for (String f : Pantry.FRUITS) {
            counts.merge(f, 1, Integer::sum);
        }
        return counts;
    }

    public static Map<Character, List<String>> byFirstLetter() {
        // computeIfAbsent rend la liste existante ou nouvellement creee : add s'enchaine.
        Map<Character, List<String>> groups = new TreeMap<>();
        for (String f : Pantry.FRUITS) {
            groups.computeIfAbsent(f.charAt(0), k -> new ArrayList<>()).add(f);
        }
        return groups;
    }

    public static Map<String, Integer> doubled() {
        // replaceAll recoit la cle ET la valeur (BiFunction).
        Map<String, Integer> prices = Pantry.prices();
        prices.replaceAll((k, v) -> v * 2);
        return prices;
    }

    public static Set<String> withoutBanana() {
        // Une fonction de compute... qui rend null SUPPRIME la cle.
        Map<String, Integer> prices = Pantry.prices();
        prices.computeIfPresent("banane", (k, v) -> v < 2 ? null : v);
        return prices.keySet();
    }

    public static int total() {
        // values() est une vue : on la parcourt sans copier.
        int sum = 0;
        for (int p : Pantry.prices().values()) {
            sum += p;
        }
        return sum;
    }

    public static String describe() {
        // entrySet donne cle et valeur en un seul parcours (pas de get par cle).
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : Pantry.prices().entrySet()) {
            sb.append(e.getKey()).append('=').append(e.getValue()).append(';');
        }
        return sb.toString();
    }

    public static String atOrAbove5() {
        // ceilingEntry(5) : la plus petite cle >= 5 (ici 6) ; null s'il n'y en a pas.
        TreeMap<Integer, String> byPrice = new TreeMap<>();
        Pantry.prices().forEach((name, price) -> byPrice.put(price, name));
        return byPrice.ceilingEntry(5).getValue();
    }

    public static String firstAndLast() {
        // Construire un TreeMap depuis une autre map trie ses cles.
        TreeMap<String, Integer> sorted = new TreeMap<>(Pantry.prices());
        return sorted.firstKey() + "-" + sorted.lastKey();
    }
}
