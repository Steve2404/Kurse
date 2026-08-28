package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * EXERCICE 7 - TreeMap / TreeSet et methodes "navigables" (niveau : difficile)
 * ================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO 1 : closestPrice(prices, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une etagere de magasin ou les prix sont deja ranges du plus
 * petit au plus grand, de gauche a droite. Tu as un budget precis en
 * poche, et tu veux le produit dont le prix est le PLUS PROCHE de ton
 * budget (un peu plus cher ou un peu moins cher, peu importe, le plus
 * proche gagne).
 *
 * -- Essayons a la main --
 *
 * Etagere triee : 10, 25, 50, 120. Ton budget : 30. Tu n'as pas
 * besoin de regarder TOUS les prix un par un depuis le debut : comme
 * l'etagere est deja triee, tu peux poser ton doigt a peu pres au
 * milieu, voir si c'est plus grand ou plus petit que ton budget, et
 * te decaler du bon cote. Tu trouves vite que 25 est juste avant ton
 * budget, et 50 juste apres. Lequel est le plus proche de 30 ? 25
 * (distance 5) est plus proche que 50 (distance 20). Reponse : 25.
 *
 * -- Ce qu'on remarque --
 *
 * Tu n'as jamais eu besoin de regarder TOUS les prix : juste "celui
 * juste en dessous de mon budget" et "celui juste au-dessus". Une
 * etagere deja triee (TreeMap) sait te donner directement ces deux
 * voisins-la, sans que tu aies a tout parcourir toi-meme.
 *
 * -- Le plan --
 *
 *   1. Demander a l'etagere triee : "quel est le prix juste en
 *      dessous (ou egal) a mon budget ?" et "quel est le prix juste
 *      au-dessus (ou egal) ?"
 *   2. Si l'un des deux n'existe pas (ton budget est plus petit que
 *      tout, ou plus grand que tout), prendre directement l'autre.
 *   3. Sinon, comparer les deux distances et garder le plus proche
 *      (en cas d'egalite parfaite des deux distances, on choisit
 *      celui du dessous, c'est la regle du jeu ici).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Les etapes "trouver le voisin du dessous" et "trouver le voisin du
 * dessus" sont deja des boites magiques toutes faites, fournies par
 * TreeMap lui-meme (tu n'as pas besoin de les re-ecrire). Ton propre
 * travail de decoupage se limite a la comparaison finale des 2
 * distances, qui est courte et reste dans la meme methode.
 *
 * Exemple a verifier : prix {10,25,50,120}, budget 30 -> le plus
 * proche est 25 ; budget 85 -> egalite de distance entre 50 et 120,
 * le dessous (50) gagne.
 *
 *
 * ==================================================================
 * TODO 2 : caseInsensitiveTreeSetWithComparator()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme idee qu'a l'exercice 2 (mots pareils sans tenir compte des
 * majuscules), mais cette fois, au lieu d'ecrire toi-meme la regle de
 * comparaison, tu dois utiliser un outil DEJA fabrique et fourni par
 * Java lui-meme, cache dans la classe String, plutot que d'ecrire de
 * nouveau ta propre formule.
 *
 * -- Le plan --
 *
 *   1. Chercher, dans la classe String, une "regle de comparaison
 *      toute prete qui ignore les majuscules" (elle existe deja, pas
 *      besoin de l'inventer).
 *   2. Donner cette regle au TreeSet a sa creation.
 *
 *
 * ==================================================================
 * TODO 3 : explainNullPointerException() (pas de code, juste une
 * phrase d'explication)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Range des jouets sur une etagere triee (TreeSet). Pour savoir OU
 * poser un nouveau jouet, l'etagere doit pouvoir le COMPARER a ceux
 * deja ranges : "es-tu plus petit, plus grand, ou pareil que celui-
 * la ?" Maintenant, essaie de poser... rien du tout (null) sur
 * l'etagere. La question "est-ce que rien est plus petit ou plus
 * grand que ce jouet ?" n'a tout simplement aucun sens - on ne peut
 * pas comparer "rien" a quelque chose. L'etagere ne sait pas quoi
 * faire et proteste bruyamment (une exception).
 *
 * Une simple boite a jouets en vrac (HashSet), elle, ne compare
 * jamais rien entre les objets : elle donne juste un "numero de
 * casier" a chaque objet (voir exercice 2), et un numero de casier
 * pour "rien" existe tout a fait (le JDK a prevu ce cas special) -
 * c'est pour ca qu'un HashSet accepte null sans probleme, alors qu'un
 * TreeSet non.
 *
 * Ta mission ici n'est pas de coder : c'est d'ecrire, en une phrase,
 * exactement ce raisonnement, comme si tu l'expliquais a un copain
 * qui ne comprend pas pourquoi son code plante.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - Integer floor = prices.floorKey(target); Integer ceiling =
 *     prices.ceilingKey(target);
 *   - Gerer les cas ou floor ou ceiling sont null.
 *
 * Indice TODO 2 :
 *   - String.CASE_INSENSITIVE_ORDER est une constante deja prete,
 *     directement utilisable comme Comparator.
 */
public class Exercise07_NavigableMapSet {

    public static String closestPrice(TreeMap<Integer, String> prices, int target) {
        throw new UnsupportedOperationException("TODO 1 : implementer closestPrice()");
    }

    public static TreeSet<String> caseInsensitiveTreeSetWithComparator() {
        throw new UnsupportedOperationException("TODO 2 : implementer caseInsensitiveTreeSetWithComparator()");
    }

    public static String explainNullPointerException() {
        throw new UnsupportedOperationException("TODO 3 : retourner une explication (String), pas de code TreeSet ici");
    }

    public static void main(String[] args) {
        TreeMap<Integer, String> prices = new TreeMap<>();
        prices.put(10, "Cle USB");
        prices.put(25, "Souris");
        prices.put(50, "Clavier");
        prices.put(120, "Ecran");

        ExerciseChecker.check("closestPrice(30) -> Souris (distance 5 vs Clavier distance 20)",
                "Souris".equals(closestPrice(prices, 30)));
        ExerciseChecker.check("closestPrice(85) -> Clavier (distance 35) plutot qu'Ecran (distance 35, egalite -> floor gagne)",
                "Clavier".equals(closestPrice(prices, 85)));
        ExerciseChecker.check("closestPrice(5) (avant le plus petit prix) -> Cle USB",
                "Cle USB".equals(closestPrice(prices, 5)));
        ExerciseChecker.check("closestPrice(500) (apres le plus grand prix) -> Ecran",
                "Ecran".equals(closestPrice(prices, 500)));
        ExerciseChecker.check("closestPrice sur une map vide -> null",
                closestPrice(new TreeMap<>(), 10) == null);

        TreeSet<String> set = caseInsensitiveTreeSetWithComparator();
        set.add("Banane");
        set.add("banane");
        set.add("Ananas");
        ExerciseChecker.check("TreeSet avec CASE_INSENSITIVE_ORDER deduplique Banane/banane", set.size() == 2);

        String explanation = explainNullPointerException();
        ExerciseChecker.check("explainNullPointerException() a ete rempli (pas vide)",
                explanation != null && !explanation.isBlank());

        ExerciseChecker.summary();
    }
}