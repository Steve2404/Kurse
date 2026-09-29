package ch1_buildingblocks.solutions;

import java.util.*;
import java.sql.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise04_ImportsAndFileRules.
 */
public class Solution04_ImportsAndFileRules {

    public static AtomicInteger newCounter(int start) {
        // AtomicInteger vient d'un SOUS-paquet de java.util : import java.util.* ne suffirait pas,
        // d'ou l'import par nom complet en haut du fichier.
        return new AtomicInteger(start);
    }

    public static Date sqlDate(long millis) {
        // "Date" tout court = java.sql.Date : l'import par nom l'emporte sur le wildcard java.util.*.
        return new Date(millis);
    }

    public static java.util.Date utilDate(long millis) {
        // Pour l'autre Date, on ecrit le nom complet : aucun import n'est alors necessaire.
        return new java.util.Date(millis);
    }

    public static int lineTotalCents(ReceiptLine line) {
        // ReceiptLine est une 2e classe (non public) du meme fichier : ses champs "package"
        // sont visibles ici. Les centimes evitent les erreurs d'arrondi des double.
        return line.quantity * line.unitPriceCents;
    }

    public static String longestWord(List<String> words) {
        // List vient du wildcard java.util.*, String de java.lang (jamais importe).
        // > strict : a egalite, le premier mot garde sa place.
        String best = "";
        for (String w : words) {
            if (w.length() > best.length()) {
                best = w;
            }
        }
        return best;
    }
}

class ReceiptLine {

    String label() {
        return name + " x" + quantity;
    }

    final String name;
    final int quantity;
    final int unitPriceCents;

    ReceiptLine(String name, int quantity, int unitPriceCents) {
        this.name = name;
        this.quantity = quantity;
        this.unitPriceCents = unitPriceCents;
    }
}
