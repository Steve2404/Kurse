package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EXERCICE 18 - La boite a outils Collectors : toMap (fusion, doublons, Map choisie), joining, averaging, summarizing, collectingAndThen (niveau : difficile)
 * ======================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Les donnees : un inventaire venant de 2 entrepots --
 *
 *   Product(name, category, price, stock)
 *   Stylo    fournitures   1.5  100
 *   Cahier   fournitures   3.0   40
 *   Stylo    fournitures   1.5   20   <- meme produit, 2e entrepot !
 *   Clavier  info         45.0    5
 *   Souris   info         20.0    0
 *   Ecran    info        150.0    3
 *
 * -- Les 4 versions de toMap a connaitre --
 *
 *   toMap(cle, valeur)                        doublon -> IllegalStateException
 *   toMap(cle, valeur, fusion)                doublon -> fusion(ancienne, nouvelle)
 *   toMap(cle, valeur, fusion, fabriqueDeMap) + on choisit la Map (TreeMap...)
 *   (toMap sans fabrique donne une HashMap : AUCUN ordre garanti)
 *
 *
 * ==================================================================
 * TODO 1 : stockByName(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut le stock total par produit, trie par nom. "Stylo" arrive 2
 * fois : il faut dire a toMap QUOI FAIRE quand 2 valeurs tombent sur
 * la meme cle (ici : les additionner). Et pour que la map soit triee,
 * on lui donne une fabrique de TreeMap.
 *
 * -- Essayons a la main --
 *
 *   Stylo : 100, puis 20 -> fusion 100 + 20 = 120
 *   -> {Cahier=40, Clavier=5, Ecran=3, Souris=0, Stylo=120}
 *
 * -- Le plan --
 *
 *   1. Cle = nom, valeur = stock.
 *   2. Fusion des doublons = somme.
 *   3. Map = TreeMap.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un seul collect avec toMap a 4 arguments.
 *
 *
 * ==================================================================
 * TODO 2 : indexByName(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut un "annuaire" nom -> produit (le produit LUI-MEME comme
 * valeur : Function.identity(), la fonction "x -> x"). Ici on utilise
 * VOLONTAIREMENT la version SANS fusion : si deux produits ont le meme
 * nom, c'est une erreur de donnees, et toMap lance lui-meme
 * IllegalStateException("Duplicate key Stylo (attempted merging values
 * ... and ...)"). main() verifie les deux cas.
 *
 * -- Essayons a la main --
 *
 *   sans doublon : {Cahier=Product[...], Clavier=Product[...], ...}
 *   avec les 2 Stylo : IllegalStateException, message "Duplicate key Stylo ..."
 *
 * -- Le plan --
 *
 *   1. Cle = nom, valeur = le produit lui-meme. Rien d'autre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : catalogLine(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * joining a une version a 3 arguments : separateur, prefixe, suffixe.
 * Le prefixe et le suffixe sont ajoutes MEME si le stream est vide.
 *
 * -- Essayons a la main --
 *
 *   noms uniques tries : Cahier, Clavier, Ecran, Souris, Stylo
 *   -> "[Cahier, Clavier, Ecran, Souris, Stylo]"
 *   liste vide -> "[]"
 *
 * -- Le plan --
 *
 *   1. Garder les noms, sans doublon, tries.
 *   2. Les joindre avec ", " entre crochets.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : averagePriceInStock(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Collectors.averagingDouble rend un Double (pas une boite !). Et la
 * moyenne de RIEN vaut... 0.0 (contrairement a DoubleStream.average()
 * qui rend un OptionalDouble vide). Piege d'examen classique.
 *
 * -- Essayons a la main --
 *
 *   en stock (stock > 0) : 1.5, 3.0, 1.5, 45.0, 150.0 -> somme 201.0 / 5 = 40.2
 *   que des ruptures -> 0.0
 *
 * -- Le plan --
 *
 *   1. Garder les produits en stock.
 *   2. Collecter la moyenne des prix.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : lockedSortedNames(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * collectingAndThen(collecteur, finisseur) : "collecte, PUIS applique
 * une derniere transformation au resultat". Ici on collecte en liste,
 * puis on la verrouille avec Collections::unmodifiableList (toute
 * tentative d'ajout lancera UnsupportedOperationException). Les
 * doublons sont gardes.
 *
 * -- Essayons a la main --
 *
 *   -> [Cahier, Clavier, Ecran, Souris, Stylo, Stylo], et add(...) lance
 *      UnsupportedOperationException
 *
 * -- Le plan --
 *
 *   1. Garder les noms, tries.
 *   2. Collecter en liste PUIS verrouiller, dans un seul collect.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : stockStatistics(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * summarizingInt est la version "Collector" de summaryStatistics() :
 * count, sum, min, max, average en un passage.
 *
 * -- Essayons a la main --
 *
 *   stocks : 100, 40, 20, 5, 0, 3 -> count 6, sum 168, min 0, max 100
 *
 * -- Le plan --
 *
 *   1. Collecter les statistiques des stocks.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : namesByCategoryInArrivalOrder(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut, pour chaque categorie, la liste des noms separes par "|",
 * DANS L'ORDRE D'ARRIVEE, et les categories dans l'ordre ou on les a
 * rencontrees. Avec toMap : la fusion concatene l'ancienne et la
 * nouvelle valeur ; la Map est une LinkedHashMap (qui retient l'ordre
 * d'insertion).
 *
 * -- Essayons a la main --
 *
 *   fournitures : Stylo -> Stylo|Cahier -> Stylo|Cahier|Stylo
 *   info        : Clavier -> Clavier|Souris -> Clavier|Souris|Ecran
 *   -> {fournitures=Stylo|Cahier|Stylo, info=Clavier|Souris|Ecran}
 *   (et l'ordre des cles est bien fournitures, puis info)
 *
 * -- Le plan --
 *
 *   1. Cle = categorie, valeur = nom.
 *   2. Fusion = ancienne + "|" + nouvelle.
 *   3. Map = LinkedHashMap.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : totalInventoryValue(products)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La valeur de l'inventaire = somme de (prix x stock). summingDouble
 * est la version Collector de mapToDouble(...).sum().
 *
 * -- Essayons a la main --
 *
 *   150.0 + 120.0 + 30.0 + 225.0 + 0.0 + 450.0 = 975.0
 *
 * -- Le plan --
 *
 *   1. Collecter la somme de prix x stock.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier :
 *
 *   stockByName == {Cahier=40, Clavier=5, Ecran=3, Souris=0, Stylo=120}, et c'est une TreeMap
 *   indexByName(sans doublon) a 5 cles ; indexByName(avec doublon) lance
 *     IllegalStateException dont le message commence par "Duplicate key Stylo"
 *   catalogLine == "[Cahier, Clavier, Ecran, Souris, Stylo]" ; catalogLine([]) == "[]"
 *   averagePriceInStock == 40.2 ; averagePriceInStock(que des ruptures) == 0.0
 *   lockedSortedNames == [Cahier, Clavier, Ecran, Souris, Stylo, Stylo], non modifiable
 *   stockStatistics : count 6, sum 168, min 0, max 100
 *   namesByCategoryInArrivalOrder == {fournitures=Stylo|Cahier|Stylo, info=Clavier|Souris|Ecran}
 *   totalInventoryValue == 975.0
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Collectors.toMap(Product::name, Product::stock, Integer::sum, TreeMap::new)
 *   - Collectors.toMap(Product::name, Function.identity())
 *   - Collectors.joining(", ", "[", "]")
 *   - Collectors.averagingDouble(Product::price)
 *   - Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)
 *   - Collectors.summarizingInt(Product::stock)
 *   - Collectors.toMap(Product::category, Product::name, (a, b) -> a + "|" + b, LinkedHashMap::new)
 *   - Collectors.summingDouble(p -> p.price() * p.stock())
 */
public class Exercise18_CollectorsAdvanced {

    public record Product(String name, String category, double price, int stock) {
    }

    public static TreeMap<String, Integer> stockByName(List<Product> products) {
        throw new UnsupportedOperationException("TODO 1 : implementer stockByName()");
    }

    public static Map<String, Product> indexByName(List<Product> products) {
        throw new UnsupportedOperationException("TODO 2 : implementer indexByName()");
    }

    public static String catalogLine(List<Product> products) {
        throw new UnsupportedOperationException("TODO 3 : implementer catalogLine()");
    }

    public static double averagePriceInStock(List<Product> products) {
        throw new UnsupportedOperationException("TODO 4 : implementer averagePriceInStock()");
    }

    public static List<String> lockedSortedNames(List<Product> products) {
        throw new UnsupportedOperationException("TODO 5 : implementer lockedSortedNames()");
    }

    public static IntSummaryStatistics stockStatistics(List<Product> products) {
        throw new UnsupportedOperationException("TODO 6 : implementer stockStatistics()");
    }

    public static LinkedHashMap<String, String> namesByCategoryInArrivalOrder(List<Product> products) {
        throw new UnsupportedOperationException("TODO 7 : implementer namesByCategoryInArrivalOrder()");
    }

    public static double totalInventoryValue(List<Product> products) {
        throw new UnsupportedOperationException("TODO 8 : implementer totalInventoryValue()");
    }

    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Stylo", "fournitures", 1.5, 100),
                new Product("Cahier", "fournitures", 3.0, 40),
                new Product("Stylo", "fournitures", 1.5, 20),
                new Product("Clavier", "info", 45.0, 5),
                new Product("Souris", "info", 20.0, 0),
                new Product("Ecran", "info", 150.0, 3));

        TreeMap<String, Integer> stock = stockByName(products);
        ExerciseChecker.check("stockByName == {Cahier=40, Clavier=5, Ecran=3, Souris=0, Stylo=120}",
                stock.toString().equals("{Cahier=40, Clavier=5, Ecran=3, Souris=0, Stylo=120}"));

        List<Product> unique = products.stream().filter(p -> p.stock() != 20).toList();
        ExerciseChecker.check("indexByName(sans doublon) a 5 cles et Ecran -> prix 150.0",
                indexByName(unique).size() == 5 && indexByName(unique).get("Ecran").price() == 150.0);
        String message = null;
        try {
            indexByName(products);
        } catch (IllegalStateException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("indexByName(avec doublon) lance IllegalStateException(\"Duplicate key Stylo ...\")",
                message != null && message.startsWith("Duplicate key Stylo"));

        ExerciseChecker.check("catalogLine == [Cahier, Clavier, Ecran, Souris, Stylo]",
                catalogLine(products).equals("[Cahier, Clavier, Ecran, Souris, Stylo]"));
        ExerciseChecker.check("catalogLine([]) == []", catalogLine(List.of()).equals("[]"));

        ExerciseChecker.check("averagePriceInStock == 40.2", averagePriceInStock(products) == 40.2);
        ExerciseChecker.check("averagePriceInStock(que des ruptures) == 0.0 (pas de boite !)",
                averagePriceInStock(List.of(new Product("Souris", "info", 20.0, 0))) == 0.0);

        List<String> locked = lockedSortedNames(products);
        ExerciseChecker.check("lockedSortedNames == [Cahier, Clavier, Ecran, Souris, Stylo, Stylo]",
                locked.equals(List.of("Cahier", "Clavier", "Ecran", "Souris", "Stylo", "Stylo")));
        boolean refused = false;
        try {
            locked.add("Gomme");
        } catch (UnsupportedOperationException e) {
            refused = true;
        }
        ExerciseChecker.check("lockedSortedNames est non modifiable", refused);

        IntSummaryStatistics stats = stockStatistics(products);
        ExerciseChecker.check("stockStatistics : count 6, sum 168, min 0, max 100",
                stats.getCount() == 6 && stats.getSum() == 168 && stats.getMin() == 0 && stats.getMax() == 100);

        LinkedHashMap<String, String> byCategory = namesByCategoryInArrivalOrder(products);
        ExerciseChecker.check("namesByCategoryInArrivalOrder == {fournitures=Stylo|Cahier|Stylo, info=Clavier|Souris|Ecran}",
                byCategory.toString().equals("{fournitures=Stylo|Cahier|Stylo, info=Clavier|Souris|Ecran}"));

        ExerciseChecker.check("totalInventoryValue == 975.0", totalInventoryValue(products) == 975.0);

        ExerciseChecker.summary();
    }
}
