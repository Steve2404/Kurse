package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * EXERCICE 25 - CAPSTONE : tableau de bord des ventes (tout le chapitre 10 en un exercice) (niveau : capstone, tres difficile)
 * ===========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Le contexte --
 *
 * Tu codes le tableau de bord commercial d'une entreprise. Chaque vente
 * (Sale) a un id, un vendeur, une region, un produit, une quantite, un
 * prix unitaire et un mois (1 a 12). Une vente de quantite 0 est une
 * vente ANNULEE : elle ne doit compter NULLE PART (TODO 1 est la boite
 * magique que tous les autres TODO utilisent).
 *
 *   id  vendeur region produit quantite prix   mois  montant
 *   S1  Alice   Nord   Laptop  2        900.0  1     1800.0
 *   S2  Bob     Sud    Phone   5        400.0  1     2000.0
 *   S3  Alice   Nord   Phone   1        400.0  2      400.0
 *   S4  Chloe   Est    Laptop  1        950.0  2      950.0
 *   S5  Bob     Sud    Tablet  3        300.0  2      900.0
 *   S6  Dan     Nord   Tablet  0        300.0  3        0.0  (annulee)
 *   S7  Chloe   Est    Phone   4        380.0  3     1520.0
 *   S8  Alice   Nord   Laptop  1        900.0  3      900.0
 *
 * Le montant d'une vente est deja calcule par la methode amount() du
 * record.
 *
 * Aucun indice "tout fait" dans ce capstone : seulement des rappels
 * des outils a mobiliser. C'est le moment de montrer ce que tu sais.
 *
 *
 * ==================================================================
 * TODO 1 : validSales(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Avant de compter les billes, on retire les billes cassees. Toutes
 * les autres methodes doivent partir de CE stream nettoye.
 *
 * -- Le plan --
 *
 *   1. Rendre un Stream<Sale> des ventes de quantite > 0.
 *   (Oui, la methode rend un Stream, pas une List : chaque appelant le
 *   consomme UNE fois et enchaine directement dessus.)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'EST la boite magique de base du capstone.
 *
 *
 * ==================================================================
 * TODO 2 : revenueByMonth(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le chiffre d'affaires de chaque mois, mois tries.
 *
 * -- Essayons a la main --
 *
 *   mois 1 : 1800 + 2000 = 3800.0
 *   mois 2 : 400 + 950 + 900 = 2250.0
 *   mois 3 : 1520 + 900 = 2420.0 (S6 annulee)
 *   -> {1=3800.0, 2=2250.0, 3=2420.0}
 *
 * -- Le plan --
 *
 *   1. Partir des ventes valides, grouper par mois (TreeMap), sommer
 *      les montants.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : validSales. Et cette methode resservira au TODO 5.
 *
 *
 * ==================================================================
 * TODO 3 : bestSellerPerRegion(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans chaque region, le vendeur qui a rapporte le PLUS au total (pas
 * la plus grosse vente : la plus grosse SOMME). Il faut d'abord un
 * total par vendeur DANS chaque region, puis choisir le max de ces
 * totaux. Tout peut tenir dans UN collect : groupingBy region, avec en
 * aval collectingAndThen(groupingBy vendeur + somme, finisseur qui
 * cherche l'entree de plus grande valeur).
 *
 * -- Essayons a la main --
 *
 *   Nord : Alice 1800 + 400 + 900 = 3100 (Dan : annulee, ignore) -> Alice
 *   Sud  : Bob 2000 + 900 = 2900 -> Bob
 *   Est  : Chloe 950 + 1520 = 2470 -> Chloe
 *   -> {Est=Chloe, Nord=Alice, Sud=Bob}
 *
 * -- Le plan --
 *
 *   1. Ventes valides, groupees par region (TreeMap).
 *   2. En aval : total par vendeur, PUIS garder le nom du vendeur au
 *      plus gros total.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Le finisseur "cle de la plus grande valeur d'une Map<String, Double>"
 * se raconte seul : fais-en une methode privee.
 *
 *
 * ==================================================================
 * TODO 4 : topProducts(sales, n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le podium des produits par chiffre d'affaires : "Produit:total", du
 * plus grand au plus petit, les n premiers.
 *
 * -- Essayons a la main --
 *
 *   Phone  : 2000 + 400 + 1520 = 3920.0
 *   Laptop : 1800 + 950 + 900  = 3650.0
 *   Tablet : 900.0 (S6 annulee)
 *   n = 2 -> [Phone:3920.0, Laptop:3650.0]
 *
 * -- Le plan --
 *
 *   1. Total par produit (collect).
 *   2. Nouveau stream sur les entrees : tri par valeur decroissante,
 *      n premieres, format "cle:valeur".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : monthOverMonthGrowth(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour chaque mois (sauf le premier), l'evolution en % par rapport au
 * mois PRECEDENT, arrondie a l'entier le plus proche (Math.round), avec
 * un "+" devant si >= 0. Un stream ne sait pas regarder "l'element
 * d'avant"... sauf si on parcourt des INDICES : IntStream.range(1,
 * taille) donne i, et on compare l'element i a l'element i - 1 d'une
 * liste.
 *
 * -- Essayons a la main --
 *
 *   revenus par mois (TODO 2) : [3800.0, 2250.0, 2420.0] pour les mois [1, 2, 3]
 *   i = 1 : (2250 - 3800) * 100 / 3800 = -40.79 -> -41 -> "M2:-41%"
 *   i = 2 : (2420 - 2250) * 100 / 2250 =   7.56 ->   8 -> "M3:+8%"
 *   -> [M2:-41%, M3:+8%]
 *
 * -- Le plan --
 *
 *   1. revenueByMonth -> 2 listes paralleles : les mois et les revenus.
 *   2. Pour i de 1 a taille - 1 : calculer le % arrondi, formater
 *      "M" + mois + ":" + signe + % + "%".
 *   3. Rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : revenueByMonth (TODO 2). Et le formatage "signe + %" peut
 * etre une petite methode privee.
 *
 *
 * ==================================================================
 * TODO 6 : sellerRanking(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le classement des vendeurs, avec leur RANG : "1. Alice (3100.0)".
 * Le rang, c'est la position dans la liste triee : encore une fois, un
 * IntStream d'indices sur une liste deja triee.
 *
 * -- Essayons a la main --
 *
 *   Alice 3100.0, Bob 2900.0, Chloe 2470.0 (Dan n'a que des ventes
 *   annulees : il n'apparait pas)
 *   -> [1. Alice (3100.0), 2. Bob (2900.0), 3. Chloe (2470.0)]
 *
 * -- Le plan --
 *
 *   1. Total par vendeur, puis liste des entrees triees par total
 *      decroissant.
 *   2. Pour chaque indice i : (i + 1) + ". " + vendeur + " (" + total + ")".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : regionSummary(sales)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour chaque region : "N ventes / moyenne M", ou M est le montant
 * moyen d'une vente, arrondi a l'entier (Math.round). Deux infos en un
 * passage PAR GROUPE : teeing en aval de groupingBy.
 *
 * -- Essayons a la main --
 *
 *   Est  : S4, S7 -> 2 ventes, (950 + 1520) / 2 = 1235.0 -> 1235
 *   Nord : S1, S3, S8 -> 3 ventes, 3100 / 3 = 1033.33 -> 1033
 *   Sud  : S2, S5 -> 2 ventes, 2900 / 2 = 1450.0 -> 1450
 *   -> {Est=2 ventes / moyenne 1235, Nord=3 ventes / moyenne 1033, Sud=2 ventes / moyenne 1450}
 *
 * -- Le plan --
 *
 *   1. Ventes valides groupees par region (TreeMap).
 *   2. En aval : teeing(compter, moyenne des montants, formater).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : firstBigSaleAfter(sales, month, threshold)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les ventes sont deja dans l'ordre chronologique. On veut l'id de la
 * PREMIERE vente valide faite APRES le mois donne (strictement) et d'un
 * montant >= threshold. Peut-etre qu'il n'y en a pas : boite.
 *
 * -- Essayons a la main --
 *
 *   (1, 1500.0) -> mois 2 : S3 400, S4 950, S5 900 (non) ; mois 3 : S7 1520 (oui) -> Optional[S7]
 *   (3, 100.0)  -> aucune vente apres le mois 3 -> vide
 *
 * -- Le plan --
 *
 *   1. Ventes valides, filtre mois > month et montant >= threshold.
 *   2. Premiere, transformee en id.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "-> ..." de chaque TODO ci-dessus.
 *
 *
 * Rappels (pas de code tout fait) : filter, map, groupingBy (3
 * arguments), summingDouble, collectingAndThen, Map.Entry.comparingByValue,
 * entrySet().stream(), IntStream.range, Math.round, teeing, counting,
 * averagingDouble, findFirst, Optional.map.
 */
public class Exercise25_SalesAnalyticsCapstone {

    public record Sale(String id, String seller, String region, String product, int quantity, double unitPrice, int month) {
        public double amount() {
            return quantity * unitPrice;
        }
    }

    public static java.util.stream.Stream<Sale> validSales(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 1 : implementer validSales()");
    }

    public static TreeMap<Integer, Double> revenueByMonth(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 2 : implementer revenueByMonth()");
    }

    public static TreeMap<String, String> bestSellerPerRegion(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 3 : implementer bestSellerPerRegion()");
    }

    public static List<String> topProducts(List<Sale> sales, int n) {
        throw new UnsupportedOperationException("TODO 4 : implementer topProducts()");
    }

    public static List<String> monthOverMonthGrowth(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 5 : implementer monthOverMonthGrowth()");
    }

    public static List<String> sellerRanking(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 6 : implementer sellerRanking()");
    }

    public static TreeMap<String, String> regionSummary(List<Sale> sales) {
        throw new UnsupportedOperationException("TODO 7 : implementer regionSummary()");
    }

    public static Optional<String> firstBigSaleAfter(List<Sale> sales, int month, double threshold) {
        throw new UnsupportedOperationException("TODO 8 : implementer firstBigSaleAfter()");
    }

    public static void main(String[] args) {
        List<Sale> sales = List.of(
                new Sale("S1", "Alice", "Nord", "Laptop", 2, 900.0, 1),
                new Sale("S2", "Bob", "Sud", "Phone", 5, 400.0, 1),
                new Sale("S3", "Alice", "Nord", "Phone", 1, 400.0, 2),
                new Sale("S4", "Chloe", "Est", "Laptop", 1, 950.0, 2),
                new Sale("S5", "Bob", "Sud", "Tablet", 3, 300.0, 2),
                new Sale("S6", "Dan", "Nord", "Tablet", 0, 300.0, 3),
                new Sale("S7", "Chloe", "Est", "Phone", 4, 380.0, 3),
                new Sale("S8", "Alice", "Nord", "Laptop", 1, 900.0, 3));

        ExerciseChecker.check("validSales retire S6 (7 ventes restantes)",
                validSales(sales).map(Sale::id).toList().equals(List.of("S1", "S2", "S3", "S4", "S5", "S7", "S8")));

        ExerciseChecker.check("revenueByMonth == {1=3800.0, 2=2250.0, 3=2420.0}",
                revenueByMonth(sales).toString().equals("{1=3800.0, 2=2250.0, 3=2420.0}"));

        ExerciseChecker.check("bestSellerPerRegion == {Est=Chloe, Nord=Alice, Sud=Bob}",
                bestSellerPerRegion(sales).toString().equals("{Est=Chloe, Nord=Alice, Sud=Bob}"));

        ExerciseChecker.check("topProducts(2) == [Phone:3920.0, Laptop:3650.0]",
                topProducts(sales, 2).equals(List.of("Phone:3920.0", "Laptop:3650.0")));
        ExerciseChecker.check("topProducts(10) a 3 produits, Tablet:900.0 en dernier",
                topProducts(sales, 10).equals(List.of("Phone:3920.0", "Laptop:3650.0", "Tablet:900.0")));

        ExerciseChecker.check("monthOverMonthGrowth == [M2:-41%, M3:+8%]",
                monthOverMonthGrowth(sales).equals(List.of("M2:-41%", "M3:+8%")));

        ExerciseChecker.check("sellerRanking == [1. Alice (3100.0), 2. Bob (2900.0), 3. Chloe (2470.0)]",
                sellerRanking(sales).equals(List.of("1. Alice (3100.0)", "2. Bob (2900.0)", "3. Chloe (2470.0)")));

        ExerciseChecker.check("regionSummary",
                regionSummary(sales).toString().equals(
                        "{Est=2 ventes / moyenne 1235, Nord=3 ventes / moyenne 1033, Sud=2 ventes / moyenne 1450}"));

        ExerciseChecker.check("firstBigSaleAfter(1, 1500.0) == S7",
                firstBigSaleAfter(sales, 1, 1500.0).equals(Optional.of("S7")));
        ExerciseChecker.check("firstBigSaleAfter(3, 100.0) est vide", firstBigSaleAfter(sales, 3, 100.0).isEmpty());

        ExerciseChecker.summary();
    }
}
