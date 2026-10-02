package ch4_coreapis.projects.p01_textstats.solution;

import ch4_coreapis.projects.p01_textstats.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 1 - une conception possible.
 */
public class TextStats {

    // Nettoyage : on remplace la ponctuation par des espaces, puis on decoupe sur les espaces (un ou plusieurs).
    static String[] words(String text) {
        String clean = text.replace('.', ' ').replace(',', ' ').replace(':', ' ').replace(';', ' ').replace('\n', ' ');
        return clean.strip().split(" +");
    }

    static boolean palindrome(String word) {
        String w = word.toLowerCase();
        for (int i = 0, j = w.length() - 1; i < j; i++, j--) {
            if (w.charAt(i) != w.charAt(j)) {
                return false;
            }
        }
        return w.length() > 2;
    }

    static int vowels(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            // indexOf rend -1 si le caractere n'est pas une voyelle.
            if ("aeiouy".indexOf(Character.toLowerCase(text.charAt(i))) >= 0) {
                count++;
            }
        }
        return count;
    }

    static String capitalize(String word) {
        return word.isEmpty() ? word : word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static void main(String[] args) {
        String text = Data.TEXT;
        String[] lines = text.split("\n");
        String[] words = words(text);
        System.out.println("LIGNES : " + lines.length + ", MOTS : " + words.length + ", CARACTERES : " + text.length()
                + ", VOYELLES : " + vowels(text));

        String longest = "";
        String shortest = words[0];
        for (String w : words) {
            if (w.length() > longest.length()) {
                longest = w;
            }
            if (w.length() < shortest.length()) {
                shortest = w;
            }
        }
        System.out.println("PLUS LONG : " + longest + " (" + longest.length() + "), PLUS COURT : " + shortest);

        // Frequences sans collection : on TRIE une copie en minuscules, les mots egaux deviennent voisins.
        String[] sorted = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            sorted[i] = words[i].toLowerCase();
        }
        Arrays.sort(sorted);
        String top = "";
        int topCount = 0;
        String second = "";
        int secondCount = 0;
        for (int i = 0; i < sorted.length; ) {
            int j = i;
            while (j < sorted.length && sorted[j].equals(sorted[i])) {
                j++;
            }
            int run = j - i;
            if (run > topCount) {
                second = top;
                secondCount = topCount;
                top = sorted[i];
                topCount = run;
            } else if (run > secondCount) {
                second = sorted[i];
                secondCount = run;
            }
            i = j;
        }
        System.out.println("FREQUENTS : " + top + " x" + topCount + ", " + second + " x" + secondCount);

        String palindromes = "";
        String seen = " ";
        for (String w : words) {
            String key = " " + w.toLowerCase() + " ";
            if (palindrome(w) && !seen.contains(key)) {
                palindromes = palindromes + w.toLowerCase() + " ";
                seen = seen + w.toLowerCase() + " ";
            }
        }
        System.out.println("PALINDROMES : " + palindromes.strip());

        // indexOf(texte, depart) : toutes les positions d'un motif.
        String positions = "";
        int from = 0;
        int found;
        while ((found = text.indexOf(Data.CENSORED, from)) >= 0) {
            positions = positions + found + " ";
            from = found + 1;
        }
        System.out.println("POSITIONS de \"" + Data.CENSORED + "\" : " + positions.strip());
        System.out.println("CENSURE : " + lines[2].replace(Data.CENSORED, "*".repeat(Data.CENSORED.length())));

        String title = "";
        for (String w : lines[0].split(" ")) {
            title = title + capitalize(w) + " ";
        }
        System.out.println("TITRE : " + title.strip());

        int startsWithLe = 0;
        int endsWithDot = 0;
        for (String line : lines) {
            startsWithLe += line.startsWith("Le ") ? 1 : 0;
            endsWithDot += line.endsWith(".") ? 1 : 0;
        }
        System.out.println("COMMENCENT par \"Le \" : " + startsWithLe + ", FINISSENT par \".\" : " + endsWithDot
                + ", contient \"Bob\" : " + text.contains("Bob") + ", egal sans casse : " + "KAYAK".equalsIgnoreCase(words[3]));

        String messy = Data.MESSY;
        System.out.println("SALE : [" + messy + "] -> strip [" + messy.strip() + "] -> stripLeading [" + messy.stripLeading()
                + "] -> stripTrailing [" + messy.stripTrailing() + "]");
        System.out.println("ECHAPPEMENTS : [" + messy.strip().translateEscapes() + "], vide " + "".isEmpty() + ", blanc " + "   ".isBlank()
                + ", trim [" + " \t x \t ".trim() + "]");
        System.out.print("INDENTE :\n" + "a\nb".indent(2));
        // stripIndent retire l'indentation COMMUNE ; une ligne finale vide (apres un \n) compte pour 0 et bloque tout.
        System.out.println("DESINDENTE :\n" + "   x\n     y".stripIndent() + "\n" + "   x\n     y\n".stripIndent().length());
        System.out.println("FORMATE : " + "%-6s|%4d|%s".formatted(shortest, words.length, true)
                + " " + String.format("[%5s]", "ok"));
        System.out.println("CHAINAGE : " + "  Hello World  ".strip().toLowerCase().replace("o", "0").substring(6).concat("!"));
    }
}
