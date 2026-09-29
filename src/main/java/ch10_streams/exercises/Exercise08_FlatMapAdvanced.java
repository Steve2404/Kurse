package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * EXERCICE 8 - flatMap avance : commandes imbriquees, flatMapToInt, split, pagination skip/limit (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Les donnees --
 *
 * Une commande (Order) a un id, un client, et une LISTE d'articles
 * (Item). C'est une structure "a 2 etages" : une liste de commandes,
 * chacune contenant une liste d'articles. Des qu'on veut raisonner sur
 * TOUS les articles de TOUTES les commandes, il faut "aplatir" les 2
 * etages en 1 : c'est le metier de flatMap.
 *
 *   O1 Alice : clavier (info, x1, 50.0), souris (info, x2, 20.0)
 *   O2 Bob   : pomme (fruit, x6, 0.5), souris (info, x1, 20.0)
 *   O3 Alice : ecran (info, x1, 150.0)
 *   O4 Dan   : banane (fruit, x3, 0.25)
 *   O5 Chloe : (aucun article)
 *
 *
 * ==================================================================
 * TODO 1 : allItemNames(orders)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu as 5 sacs de courses (commandes), chacun avec des produits. Tu
 * veux la liste de TOUS les noms de produits achetes, sans doublon, par
 * ordre alphabetique. map(o -> o.items()) te donnerait une liste de
 * SACS (Stream<List<Item>>) ; flatMap(o -> o.items().stream()) vide
 * tous les sacs sur la meme table (Stream<Item>). Un sac vide (O5) ne
 * pose aucun probleme : il ne verse rien.
 *
 * -- Essayons a la main --
 *
 *   tout verse : clavier, souris, pomme, souris, ecran, banane
 *   noms uniques tries : [banane, clavier, ecran, pomme, souris]
 *
 * -- Le plan --
 *
 *   1. Aplatir les commandes en un flux d'articles.
 *   2. Garder le nom de chaque article.
 *   3. Retirer les doublons, trier, rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un pipeline.
 *
 *
 * ==================================================================
 * TODO 2 : totalQuantity(orders)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut le nombre total d'unites achetees. On pourrait aplatir en
 * Stream<Item> puis mapToInt. Mais il existe un raccourci :
 * flatMapToInt, qui aplatit DIRECTEMENT en IntStream (chaque commande
 * fournit un petit IntStream de ses quantites). Utilise-le ici pour le
 * connaitre : il existe aussi flatMapToLong et flatMapToDouble.
 *
 * -- Essayons a la main --
 *
 *   O1 : 1 + 2 ; O2 : 6 + 1 ; O3 : 1 ; O4 : 3 ; O5 : rien
 *   total = 14
 *
 * -- Le plan --
 *
 *   1. Pour chaque commande, fabriquer le petit IntStream de ses
 *      quantites, et tout aplatir en un seul IntStream.
 *   2. Faire la somme.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : distinctWords(sentences)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque phrase est un "sac de mots". On veut la liste des mots
 * differents, en minuscules, dans l'ordre ou on les rencontre pour la
 * premiere fois. Deux pieges :
 *   - des espaces multiples ("le  CHIEN") : on decoupe sur "un ou
 *     plusieurs blancs" (regex "\\s+"), pas sur un seul espace ;
 *   - une phrase qui COMMENCE par des blancs ("  soir") : split rend
 *     alors une chaine VIDE "" en premiere case (["", "soir"]) - qu'il
 *     faut jeter. (Curiosite : une phrase faite UNIQUEMENT de blancs
 *     donne un tableau vide, car split supprime les "" de la fin.)
 *
 * -- Essayons a la main --
 *
 *   ["Le chat dort", "le  CHIEN dort", "  soir"]
 *   decoupe : Le, chat, dort | le, CHIEN, dort | "", soir
 *   minuscules, sans vide : le, chat, dort, le, chien, dort, soir
 *   sans doublon (ordre de 1re apparition) : [le, chat, dort, chien, soir]
 *
 * -- Le plan --
 *
 *   1. Decouper chaque phrase en mots, et aplatir tous les mots.
 *   2. Jeter les mots vides.
 *   3. Passer en minuscules.
 *   4. Retirer les doublons (distinct garde la 1re apparition).
 *   5. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Attention a l'ORDRE : il faut passer en minuscules AVANT
 * distinct, sinon "Le" et "le" sont consideres differents.
 *
 *
 * ==================================================================
 * TODO 4 : page(items, pageNumber, pageSize)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un catalogue en ligne montre 3 produits par page. Pour afficher la
 * page 3, on saute les 2 premieres pages (2 x 3 = 6 produits : skip),
 * puis on en prend 3 (limit). Les pages sont numerotees a partir de 1.
 * Une page au-dela de la fin est simplement vide (pas d'exception).
 *
 * -- Essayons a la main --
 *
 *   items = [a, b, c, d, e, f, g], pageSize = 3
 *   page 1 : skip 0, limit 3 -> [a, b, c]
 *   page 3 : skip 6, limit 3 -> [g]
 *   page 4 : skip 9, limit 3 -> []
 *
 * -- Le plan --
 *
 *   1. Sauter (pageNumber - 1) * pageSize elements.
 *   2. En garder pageSize.
 *   3. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (skip et limit prennent un long : un int passe tout seul.)
 *
 *
 * ==================================================================
 * TODO 5 : customersWhoBought(orders, category)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut les clients qui ont achete AU MOINS un article d'une
 * categorie. Ici, pas besoin d'aplatir : on garde les commandes dont
 * UN article correspond (un anyMatch sur le stream des articles, a
 * l'interieur du filter : un stream dans un stream !). Un client peut
 * avoir plusieurs commandes -> distinct.
 *
 * -- Essayons a la main --
 *
 *   "info"  : O1 (Alice), O2 (Bob), O3 (Alice) -> [Alice, Bob]
 *   "fruit" : O2 (Bob), O4 (Dan)               -> [Bob, Dan]
 *   "livre" : personne                         -> []
 *
 * -- Le plan --
 *
 *   1. Garder les commandes dont au moins un article est de la
 *      categorie demandee.
 *   2. Garder le client de chaque commande.
 *   3. Retirer les doublons, trier, rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Eventuellement "cette commande contient-elle la categorie ?" : elle
 * se raconte seule. A toi de voir si tu en fais une methode privee.
 *
 *
 * ==================================================================
 * TODO 6 : itemLabels(orders)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut des etiquettes "idCommande:nomArticle". Probleme : si on
 * aplatit d'abord en Stream<Item>, on a PERDU la commande (un Item ne
 * connait pas sa commande). L'astuce : faire le map A L'INTERIEUR du
 * flatMap, la ou la variable o (la commande) est encore visible.
 *
 * -- Essayons a la main --
 *
 *   O1 -> [O1:clavier, O1:souris] ; O2 -> [O2:pomme, O2:souris]
 *   O3 -> [O3:ecran] ; O4 -> [O4:banane] ; O5 -> []
 *   aplati : [O1:clavier, O1:souris, O2:pomme, O2:souris, O3:ecran, O4:banane]
 *
 * -- Le plan --
 *
 *   1. Pour chaque commande o : prendre le stream de ses articles, et
 *      transformer chaque article en o.id() + ":" + nom. Aplatir.
 *   2. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais remarque bien la lambda imbriquee : o -> o.items().stream().map(i -> ...o...i...).
 *
 *
 * Exemple a verifier :
 *
 *   allItemNames == [banane, clavier, ecran, pomme, souris]
 *   totalQuantity == 14
 *   distinctWords(["Le chat dort", "le  CHIEN dort", "  soir"]) == [le, chat, dort, chien, soir]
 *   page([a..g], 1, 3) == [a, b, c] ; page(..., 3, 3) == [g] ; page(..., 4, 3) == []
 *   customersWhoBought("info") == [Alice, Bob] ; ("fruit") == [Bob, Dan] ; ("livre") == []
 *   itemLabels == [O1:clavier, O1:souris, O2:pomme, O2:souris, O3:ecran, O4:banane]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - orders.stream().flatMap(o -> o.items().stream())
 *   - orders.stream().flatMapToInt(o -> o.items().stream().mapToInt(Item::quantity)).sum()
 *   - Arrays.stream(phrase.split("\\s+")) : un tableau -> un Stream<String>
 *   - filter(w -> !w.isEmpty()), map(String::toLowerCase), distinct()
 *   - skip((long) (pageNumber - 1) * pageSize).limit(pageSize)
 *   - filter(o -> o.items().stream().anyMatch(i -> i.category().equals(category)))
 */
public class Exercise08_FlatMapAdvanced {

    public record Item(String name, String category, int quantity, double unitPrice) {
    }

    public record Order(String id, String customer, List<Item> items) {
    }

    public static List<String> allItemNames(List<Order> orders) {
        throw new UnsupportedOperationException("TODO 1 : implementer allItemNames()");
    }

    public static int totalQuantity(List<Order> orders) {
        throw new UnsupportedOperationException("TODO 2 : implementer totalQuantity()");
    }

    public static List<String> distinctWords(List<String> sentences) {
        throw new UnsupportedOperationException("TODO 3 : implementer distinctWords()");
    }

    public static List<String> page(List<String> items, int pageNumber, int pageSize) {
        throw new UnsupportedOperationException("TODO 4 : implementer page()");
    }

    public static List<String> customersWhoBought(List<Order> orders, String category) {
        throw new UnsupportedOperationException("TODO 5 : implementer customersWhoBought()");
    }

    public static List<String> itemLabels(List<Order> orders) {
        throw new UnsupportedOperationException("TODO 6 : implementer itemLabels()");
    }

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("O1", "Alice", List.of(new Item("clavier", "info", 1, 50.0), new Item("souris", "info", 2, 20.0))),
                new Order("O2", "Bob", List.of(new Item("pomme", "fruit", 6, 0.5), new Item("souris", "info", 1, 20.0))),
                new Order("O3", "Alice", List.of(new Item("ecran", "info", 1, 150.0))),
                new Order("O4", "Dan", List.of(new Item("banane", "fruit", 3, 0.25))),
                new Order("O5", "Chloe", List.of()));

        ExerciseChecker.check("allItemNames == [banane, clavier, ecran, pomme, souris]",
                allItemNames(orders).equals(List.of("banane", "clavier", "ecran", "pomme", "souris")));

        ExerciseChecker.check("totalQuantity == 14", totalQuantity(orders) == 14);

        ExerciseChecker.check("distinctWords == [le, chat, dort, chien, soir]",
                distinctWords(List.of("Le chat dort", "le  CHIEN dort", "  soir"))
                        .equals(List.of("le", "chat", "dort", "chien", "soir")));

        List<String> catalog = List.of("a", "b", "c", "d", "e", "f", "g");
        ExerciseChecker.check("page 1 == [a, b, c]", page(catalog, 1, 3).equals(List.of("a", "b", "c")));
        ExerciseChecker.check("page 3 == [g]", page(catalog, 3, 3).equals(List.of("g")));
        ExerciseChecker.check("page 4 == []", page(catalog, 4, 3).isEmpty());

        ExerciseChecker.check("customersWhoBought(info) == [Alice, Bob]",
                customersWhoBought(orders, "info").equals(List.of("Alice", "Bob")));
        ExerciseChecker.check("customersWhoBought(fruit) == [Bob, Dan]",
                customersWhoBought(orders, "fruit").equals(List.of("Bob", "Dan")));
        ExerciseChecker.check("customersWhoBought(livre) == []",
                customersWhoBought(orders, "livre").isEmpty());

        ExerciseChecker.check("itemLabels == [O1:clavier, O1:souris, O2:pomme, O2:souris, O3:ecran, O4:banane]",
                itemLabels(orders).equals(List.of("O1:clavier", "O1:souris", "O2:pomme", "O2:souris", "O3:ecran", "O4:banane")));

        ExerciseChecker.summary();
    }
}
