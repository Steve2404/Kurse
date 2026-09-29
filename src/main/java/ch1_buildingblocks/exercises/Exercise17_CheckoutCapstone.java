package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 17 - CAPSTONE : la caisse enregistreuse (tout le chapitre 1 en un exercice) (niveau : capstone)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * La caisse se lance en ligne de commande :
 *
 *   java Caisse --client=Lea --vip pomme:3:50 pain:1:120 oops lait:2:99 cafe:x:300 the:0:100
 *
 * Chaque article s'ecrit nom:quantite:prixUnitaireEnCentimes. Les
 * articles mal ecrits (pas 3 morceaux, quantite ou prix non numerique,
 * quantite 0 ou negative) sont IGNORES, sans planter. Un client --vip
 * a 10 % de remise (division entiere en centimes). On travaille en
 * CENTIMES (des int) : jamais de double pour de l'argent.
 *
 * Le ticket attendu pour la commande ci-dessus (1er ticket de la journee) :
 *
 *   TICKET #1
 *   Client : Lea (VIP)
 *   pomme x3 = 1.50
 *   pain x1 = 1.20
 *   lait x2 = 1.98
 *   Sous-total : 4.68
 *   Remise : 0.46
 *   Total : 4.22
 *
 * (chaque ligne se termine par \n, y compris la derniere)
 *
 * Tu reutilises tout le chapitre : args[] et options (Ex01-03),
 * wrappers et conversions (Ex08-09), variables initialisees sur tous
 * les chemins (Ex10), compteur static partage (Ex12-13), constructeur
 * (Ex14). Aucun code d'un autre exercice n'est appele : tout est a
 * ecrire ici.
 *
 *
 * ==================================================================
 * TODO 1 : clientName(args)  et  TODO 2 : isVip(args)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le nom du client suit "--client=". Pas d'option : "anonyme". Le
 * badge VIP, c'est l'argument exact "--vip".
 *
 * -- Essayons a la main --
 *
 *   clientName -> "Lea" (ou "anonyme") ; isVip -> true (ou false)
 *
 * -- Le plan --
 *
 *   clientName : chercher un argument qui commence par "--client=" et
 *                rendre ce qui suit ; sinon "anonyme".
 *   isVip      : vrai si un argument est exactement "--vip".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : parseArticle(token)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le caissier lit une etiquette "pomme:3:50". Si l'etiquette est
 * dechiree ou illisible, il la met de cote (null) au lieu de bloquer
 * toute la file.
 *
 * -- Essayons a la main --
 *
 *   "pomme:3:50" -> Article(pomme, 3, 50)
 *   "oops" -> null (1 morceau) ; "cafe:x:300" -> null (x n'est pas un nombre)
 *   "the:0:100" -> null (quantite 0)
 *
 * -- Le plan --
 *
 *   1. Decouper sur ":" ; pas exactement 3 morceaux -> null.
 *   2. Convertir quantite et prix ; si l'un n'est pas un nombre -> null.
 *   3. Quantite <= 0 ou prix < 0 -> null.
 *   4. Sinon, un nouvel Article.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 4 : parseArticles(args)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Pour chaque argument qui ne commence PAS par "--" : le lire
 *      (TODO 3) et le garder s'il n'est pas null.
 *   2. Rendre la liste, dans l'ordre des arguments.
 *
 * -- Essayons a la main --
 *
 *   -> [pomme x3, pain x1, lait x2]
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parseArticle.
 *
 *
 * ==================================================================
 * TODO 5 : formatCents(cents)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 422 centimes s'affichent "4.22". Attention au 0 : 5 centimes ->
 * "0.05", pas "0.5" ; 1200 -> "12.00".
 *
 * -- Le plan --
 *
 *   1. Euros = cents / 100 (division entiere), reste = cents % 100.
 *   2. Si le reste est < 10, ajouter un "0" devant.
 *   3. Euros + "." + reste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : elle sert a chaque montant du ticket.
 *
 *
 * ==================================================================
 * TODO 6 : receipt(args)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On assemble le ticket. Le numero de ticket vient d'un compteur
 * PARTAGE par toute la caisse (static ticketsPrinted) : chaque ticket
 * imprime le fait avancer. La remise est declaree sans valeur et
 * recoit une valeur sur chaque chemin (VIP ou pas).
 *
 * -- Le plan --
 *
 *   1. Augmenter ticketsPrinted ; ligne "TICKET #" + numero.
 *   2. Ligne "Client : " + nom, avec " (VIP)" si VIP.
 *   3. Une ligne par article : nom + " x" + quantite + " = " + montant.
 *   4. Sous-total (somme des lignes), remise (10 % si VIP, sinon 0),
 *      total = sous-total - remise, chacun sur sa ligne.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : clientName, isVip, parseArticles, formatCents, et le
 * lineTotalCents() de l'Article.
 *
 *
 * Exemple a verifier : le ticket ci-dessus, puis un 2e ticket
 * {"pain:2:120"} :
 *   "TICKET #2\nClient : anonyme\npain x2 = 2.40\nSous-total : 2.40\nRemise : 0.00\nTotal : 2.40\n"
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - arg.startsWith("--client="), arg.substring("--client=".length()), "--vip".equals(arg)
 *   - String[] parts = token.split(":"); parts.length
 *   - try { int q = Integer.parseInt(parts[1]); ... } catch (NumberFormatException e) { return null; }
 *   - cents / 100, cents % 100
 *   - String text = ""; text += ... + "\n";
 */
public class Exercise17_CheckoutCapstone {

    static int ticketsPrinted = 0;

    static class Article {
        final String name;
        final int quantity;
        final int unitPriceCents;

        Article(String name, int quantity, int unitPriceCents) {
            this.name = name;
            this.quantity = quantity;
            this.unitPriceCents = unitPriceCents;
        }

        int lineTotalCents() {
            return quantity * unitPriceCents;
        }
    }

    public static String clientName(String[] args) {
        throw new UnsupportedOperationException("TODO 1 : implementer clientName()");
    }

    public static boolean isVip(String[] args) {
        throw new UnsupportedOperationException("TODO 2 : implementer isVip()");
    }

    public static Article parseArticle(String token) {
        throw new UnsupportedOperationException("TODO 3 : implementer parseArticle()");
    }

    public static List<Article> parseArticles(String[] args) {
        throw new UnsupportedOperationException("TODO 4 : implementer parseArticles()");
    }

    public static String formatCents(int cents) {
        throw new UnsupportedOperationException("TODO 5 : implementer formatCents()");
    }

    public static String receipt(String[] args) {
        throw new UnsupportedOperationException("TODO 6 : implementer receipt()");
    }

    public static void main(String[] args) {
        String[] order = {"--client=Lea", "--vip", "pomme:3:50", "pain:1:120", "oops", "lait:2:99", "cafe:x:300", "the:0:100"};

        ExerciseChecker.check("1 clientName == Lea ; sans option == anonyme",
                clientName(order).equals("Lea") && clientName(new String[]{"pain:1:120"}).equals("anonyme"));
        ExerciseChecker.check("2 isVip == true ; sans --vip == false", isVip(order) && !isVip(new String[]{"--client=Hugo"}));

        Article pomme = parseArticle("pomme:3:50");
        ExerciseChecker.check("3 parseArticle(pomme:3:50) == pomme x3 a 50",
                pomme != null && pomme.name.equals("pomme") && pomme.quantity == 3 && pomme.unitPriceCents == 50);
        ExerciseChecker.check("3 oops, cafe:x:300, the:0:100, a:1 -> null",
                parseArticle("oops") == null && parseArticle("cafe:x:300") == null
                        && parseArticle("the:0:100") == null && parseArticle("a:1") == null);

        List<Article> articles = parseArticles(order);
        ExerciseChecker.check("4 parseArticles garde pomme, pain, lait (dans l'ordre)",
                articles.size() == 3 && articles.get(0).name.equals("pomme") && articles.get(1).name.equals("pain")
                        && articles.get(2).name.equals("lait"));

        ExerciseChecker.check("5 formatCents : 422 -> 4.22, 5 -> 0.05, 1200 -> 12.00, 0 -> 0.00",
                formatCents(422).equals("4.22") && formatCents(5).equals("0.05") && formatCents(1200).equals("12.00")
                        && formatCents(0).equals("0.00"));

        ticketsPrinted = 0;
        ExerciseChecker.check("6 1er ticket (Lea, VIP)", receipt(order).equals(
                "TICKET #1\nClient : Lea (VIP)\npomme x3 = 1.50\npain x1 = 1.20\nlait x2 = 1.98\n"
                        + "Sous-total : 4.68\nRemise : 0.46\nTotal : 4.22\n"));
        ExerciseChecker.check("6 2e ticket (anonyme, sans remise) et numero 2", receipt(new String[]{"pain:2:120"}).equals(
                "TICKET #2\nClient : anonyme\npain x2 = 2.40\nSous-total : 2.40\nRemise : 0.00\nTotal : 2.40\n"));

        ExerciseChecker.summary();
    }
}
