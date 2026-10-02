package ch9_collections.projects.p01_inventory;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Inventory, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "charge : 10 articles, premier clavier, dernier horloge, vide false",
            "ventes : [3, 12, 7, 3, 1], frequence de 3 : 2, indexOf(3) 0, lastIndexOf(3) 3, contains(12) true",
            "ruptures retirees [souris, poele], reste 8 ; cuisine +10 % : tasse=9.79 bouilloire=32.89",
            "par categorie puis prix decroissant : [bouilloire, tasse, ecran, clavier, cable, vase, lampe, horloge]",
            "subList(0, 3) : [bouilloire, tasse, ecran], max par valeur clavier, min par stock vase",
            "reassort de 3 articles ; sans les noms en c : [bouilloire, tasse, ecran, vase, lampe, horloge]",
            "ABC : ecran=A tasse=A vase=B horloge=B bouilloire=C lampe=C (total 1916.70)",
            "file : servi urgent, restant [cmd4, cmd1, cmd2, cmd3], inverse+swap [cmd3, cmd4, cmd2, cmd1], melange [a, c, d, b, e], nCopies [x, x, x]",
            "trie [1, 3, 3, 7, 12], binarySearch(7) 3, binarySearch(5) -4",
            "asList [X, y, z], List.of [a, b], toArray 6, egalite true, copyOf [X, y, z]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ITEMS", "Data.SALES", "List<Item> items = new ArrayList<>()", "Integer.valueOf(",
            ".removeIf(", ".replaceAll(", "Comparator.comparing(", ".thenComparing(",
            "Comparator.reverseOrder()", ".subList(", "Collections.max(", "Collections.min(",
            "ListIterator<Item>", ".set(", "Iterator<Item>", "remover.remove()",
            "Collections.frequency(", "Collections.reverse(", "Collections.swap(", "Collections.rotate(",
            "Collections.shuffle(", "Collections.nCopies(", "Collections.binarySearch(", "Collections.sort(",
            "Arrays.asList(", "List.of(", "List.copyOf(", ".toArray(new Item[0])",
            "LinkedList<String>", ".addFirst(", ".removeFirst()",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Inventory", args, EXPECTED, API);
    }
}
