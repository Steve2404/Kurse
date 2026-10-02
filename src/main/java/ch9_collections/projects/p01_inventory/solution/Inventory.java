package ch9_collections.projects.p01_inventory.solution;

import ch9_collections.projects.p01_inventory.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * SOLUTION du projet 1 - l'inventaire : l'API List et la classe utilitaire Collections.
 */
public class Inventory {

    static String money(long cents) {
        return cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100;
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();               // le type de la variable : l'interface ; diamant <> a droite
        for (String line : Data.ITEMS) {
            items.add(Item.parse(line));
        }
        System.out.println("charge : " + items.size() + " articles, premier " + items.get(0) + ", dernier " + items.get(items.size() - 1)
                + ", vide " + items.isEmpty());

        // Le piege remove(int) / remove(Object) sur une List<Integer>.
        List<Integer> sales = new ArrayList<>();
        for (int s : Data.SALES) {
            sales.add(s);                                   // autoboxing
        }
        sales.remove(1);                                    // retire l'element a l'INDICE 1 (le 7)
        sales.remove(Integer.valueOf(3));                   // retire la PREMIERE valeur 3
        System.out.println("ventes : " + sales + ", frequence de 3 : " + Collections.frequency(sales, 3) + ", indexOf(3) " + sales.indexOf(3)
                + ", lastIndexOf(3) " + sales.lastIndexOf(3) + ", contains(12) " + sales.contains(12));

        // removeIf et replaceAll : des lambdas (chapitre 8) appliquees a la liste elle-meme.
        List<Item> rupture = new ArrayList<>(items);
        rupture.removeIf(i -> i.stock() > 0);
        items.removeIf(i -> i.stock() == 0);
        items.replaceAll(i -> i.category().equals("cuisine") ? i.withPrice(i.price() + i.price() / 10) : i);
        StringBuilder kitchen = new StringBuilder();
        for (Item i : items) {
            if (i.category().equals("cuisine")) {
                kitchen.append(' ').append(i.name()).append('=').append(money(i.price()));
            }
        }
        System.out.println("ruptures retirees " + rupture + ", reste " + items.size() + " ; cuisine +10 % :" + kitchen);

        // Trier avec un Comparator compose (les combinateurs que tu avais ecrits a la main au chapitre 8).
        items.sort(Comparator.comparing(Item::category).thenComparing(Item::price, Comparator.reverseOrder()));
        System.out.println("par categorie puis prix decroissant : " + items);
        List<Item> top3 = items.subList(0, 3);              // une VUE : modifier top3 modifie items
        System.out.println("subList(0, 3) : " + top3 + ", max par valeur " + Collections.max(items, Comparator.comparingLong(Item::value))
                + ", min par stock " + Collections.min(items, Comparator.comparingInt(Item::stock)));

        // ListIterator : parcourir, remplacer (set) et inserer (add) pendant le parcours.
        ListIterator<Item> it = items.listIterator();
        int restocked = 0;
        while (it.hasNext()) {
            Item i = it.next();
            if (i.stock() < Data.TARGET_STOCK) {
                it.set(i.withStock(Data.TARGET_STOCK));
                restocked++;
            }
        }
        Iterator<Item> remover = items.iterator();
        while (remover.hasNext()) {
            if (remover.next().name().startsWith("c")) {
                remover.remove();                           // la seule facon sure de retirer pendant un parcours
            }
        }
        System.out.println("reassort de " + restocked + " articles ; sans les noms en c : " + items);

        // Analyse ABC : trier par valeur decroissante, puis classer par part cumulee de la valeur totale.
        List<Item> byValue = new ArrayList<>(items);
        byValue.sort(Comparator.comparingLong(Item::value).reversed());
        long total = 0;
        for (Item i : byValue) {
            total += i.value();
        }
        StringBuilder abc = new StringBuilder("ABC :");
        long cumulated = 0;
        for (Item i : byValue) {
            cumulated += i.value();
            char cls = cumulated * 100 <= total * 70 ? 'A' : cumulated * 100 <= total * 90 ? 'B' : 'C';
            abc.append(' ').append(i.name()).append('=').append(cls);
        }
        System.out.println(abc + " (total " + money(total) + ")");

        // LinkedList comme file de commandes, et les utilitaires de Collections.
        LinkedList<String> orders = new LinkedList<>(List.of("cmd1", "cmd2", "cmd3"));
        orders.addFirst("urgent");
        orders.addLast("cmd4");
        String first = orders.removeFirst();
        List<String> copy = new ArrayList<>(orders);
        Collections.reverse(copy);
        Collections.swap(copy, 0, 1);
        Collections.rotate(orders, 1);
        List<String> shuffled = new ArrayList<>(List.of("a", "b", "c", "d", "e"));
        Collections.shuffle(shuffled, new Random(Data.SEED));
        System.out.println("file : servi " + first + ", restant " + orders + ", inverse+swap " + copy + ", melange " + shuffled + ", nCopies "
                + Collections.nCopies(3, "x"));

        // Recherche dichotomique : la liste DOIT etre triee selon le meme ordre.
        List<Integer> sorted = new ArrayList<>(sales);
        Collections.sort(sorted);
        System.out.println("trie " + sorted + ", binarySearch(7) " + Collections.binarySearch(sorted, 7) + ", binarySearch(5) " + Collections.binarySearch(sorted, 5));

        // Les listes de taille fixe ou immuables.
        List<String> fixed = Arrays.asList("x", "y", "z");  // adossee au tableau : set permis, add/remove interdits
        fixed.set(0, "X");
        List<String> frozen = List.of("a", "b");            // immuable : aucune modification
        Item[] asArray = items.toArray(new Item[0]);
        System.out.println("asList " + fixed + ", List.of " + frozen + ", toArray " + asArray.length + ", egalite " + List.of(1, 2).equals(Arrays.asList(1, 2))
                + ", copyOf " + List.copyOf(fixed));
    }
}
