package ch9_collections.drills;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Les donnees partagees par TOUS les drills du chapitre 9.
 * =======================================================
 *
 * Toujours le meme petit marche : ton cerveau se concentre sur l'API
 * des collections et sur les generiques, pas sur les donnees.
 *
 *   FRUITS   : [kiwi, pomme, banane, kiwi, cerise, pomme, abricot]   (7 elements, 5 differents)
 *   NUMBERS  : [5, 3, 8, 1, 9, 2]                                    (somme 28, max 9)
 *   prices() : {kiwi=3, pomme=2, banane=1, cerise=6, abricot=4}      (une NOUVELLE LinkedHashMap modifiable a chaque appel ; total 16)
 *   items()  : les memes prix sous forme de records Item(name, price), dans le meme ordre
 */
public final class Pantry {

    public static final List<String> FRUITS = List.of("kiwi", "pomme", "banane", "kiwi", "cerise", "pomme", "abricot");

    public static final List<Integer> NUMBERS = List.of(5, 3, 8, 1, 9, 2);

    public record Item(String name, int price) {
    }

    public static Map<String, Integer> prices() {
        Map<String, Integer> prices = new LinkedHashMap<>();
        prices.put("kiwi", 3);
        prices.put("pomme", 2);
        prices.put("banane", 1);
        prices.put("cerise", 6);
        prices.put("abricot", 4);
        return prices;
    }

    public static List<Item> items() {
        return List.of(new Item("kiwi", 3), new Item("pomme", 2), new Item("banane", 1),
                new Item("cerise", 6), new Item("abricot", 4));
    }

    private Pantry() {
    }
}
