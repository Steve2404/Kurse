package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise17_CheckoutCapstone.
 */
public class Solution17_CheckoutCapstone {

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
        // Java ne verifie rien dans args : c'est a nous de chercher, avec une valeur par defaut.
        for (String arg : args) {
            if (arg.startsWith("--client=")) {
                return arg.substring("--client=".length());
            }
        }
        return "anonyme";
    }

    public static boolean isVip(String[] args) {
        // "--vip".equals(arg) plutot que arg.equals("--vip") : ne plante jamais meme si arg est null.
        for (String arg : args) {
            if ("--vip".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    public static Article parseArticle(String token) {
        // Une etiquette illisible donne null au lieu d'une exception : la caisse ne bloque pas.
        String[] parts = token.split(":");
        if (parts.length != 3) {
            return null;
        }
        int quantity;
        int price;
        try {
            quantity = Integer.parseInt(parts[1]);
            price = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return null;
        }
        if (quantity <= 0 || price < 0) {
            return null;
        }
        return new Article(parts[0], quantity, price);
    }

    public static List<Article> parseArticles(String[] args) {
        // Les options commencent par "--" ; tout le reste est un article, peut-etre illisible.
        List<Article> articles = new ArrayList<>();
        for (String arg : args) {
            if (!arg.startsWith("--")) {
                Article article = parseArticle(arg);
                if (article != null) {
                    articles.add(article);
                }
            }
        }
        return articles;
    }

    public static String formatCents(int cents) {
        // Division entiere et reste : 5 centimes doivent s'afficher "0.05", d'ou le "0" ajoute.
        int euros = cents / 100;
        int rest = cents % 100;
        return euros + "." + (rest < 10 ? "0" : "") + rest;
    }

    public static String receipt(String[] args) {
        // ticketsPrinted est partage (static) : chaque ticket imprime le fait avancer.
        // discount n'a pas de valeur de depart : if ET else lui en donnent une.
        ticketsPrinted++;
        boolean vip = isVip(args);
        String text = "TICKET #" + ticketsPrinted + "\n";
        text += "Client : " + clientName(args) + (vip ? " (VIP)" : "") + "\n";
        int subtotal = 0;
        for (Article article : parseArticles(args)) {
            text += article.name + " x" + article.quantity + " = " + formatCents(article.lineTotalCents()) + "\n";
            subtotal += article.lineTotalCents();
        }
        int discount;
        if (vip) {
            discount = subtotal / 10;
        } else {
            discount = 0;
        }
        text += "Sous-total : " + formatCents(subtotal) + "\n";
        text += "Remise : " + formatCents(discount) + "\n";
        text += "Total : " + formatCents(subtotal - discount) + "\n";
        return text;
    }
}
