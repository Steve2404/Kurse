package ch4_coreapis.projects.p08_textlab.solution;

import ch4_coreapis.projects.p08_textlab.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 8 - le laboratoire d'algorithmes sur les chaines.
 */
public class TextLab {

    public static void main(String[] args) {
        anagrams();
        rle();
        ciphers();
        bigNumbers();
        prefixAndRotation();
        palindrome();
        justify();
        caseInsensitiveSort();
        romans();
        bases();
    }

    // ---------------------------------------------------------------- 1. anagrammes
    static void anagrams() {
        StringBuilder out = new StringBuilder("anagrammes :");
        for (String pair : Data.ANAGRAMS) {
            String[] parts = pair.split("/");
            // Un compteur par lettre : +1 pour la 1re chaine, -1 pour la 2e ; anagrammes si tout revient a 0.
            int[] counts = new int[26];
            String a = parts[0].toLowerCase().replace(" ", "");
            String b = parts[1].toLowerCase().replace(" ", "");
            for (int i = 0; i < a.length(); i++) {
                counts[a.charAt(i) - 'a']++;          // char - 'a' donne l'indice 0 a 25
            }
            for (int i = 0; i < b.length(); i++) {
                counts[b.charAt(i) - 'a']--;
            }
            boolean same = a.length() == b.length();
            for (int c : counts) {
                if (c != 0) {
                    same = false;
                    break;
                }
            }
            // Deuxieme methode : trier les lettres et comparer.
            char[] sa = a.toCharArray();
            char[] sb = b.toCharArray();
            Arrays.sort(sa);
            Arrays.sort(sb);
            out.append(' ').append(parts[0]).append('=').append(same ? "oui" : "non").append(same == Arrays.equals(sa, sb) ? "" : "!");
        }
        System.out.println(out);
    }

    // ---------------------------------------------------------------- 2. RLE
    static void rle() {
        String text = Data.RLE;
        StringBuilder packed = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            int run = 0;
            while (i < text.length() && text.charAt(i) == c) {   // longueur de la serie
                run++;
                i++;
            }
            packed.append(run).append(c);
        }
        // Decompression : on lit un nombre (peut-etre a plusieurs chiffres), puis la lettre.
        StringBuilder unpacked = new StringBuilder();
        int number = 0;
        for (int j = 0; j < packed.length(); j++) {
            char c = packed.charAt(j);
            if (c >= '0' && c <= '9') {
                number = number * 10 + (c - '0');
            } else {
                unpacked.append(String.valueOf(c).repeat(number));
                number = 0;
            }
        }
        System.out.println("rle : " + text + " -> " + packed + " -> " + unpacked + " aller-retour " + unpacked.toString().equals(text)
                + " gain " + (text.length() - packed.length()));
    }

    // ---------------------------------------------------------------- 3. chiffrements
    static String caesar(String s, int shift) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            // On ramene la lettre a 0..25, on decale modulo 26, on reconvertit : (char) est obligatoire.
            if (c >= 'a' && c <= 'z') {
                sb.setCharAt(i, (char) ('a' + (c - 'a' + shift) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.setCharAt(i, (char) ('A' + (c - 'A' + shift) % 26));
            }
        }
        return sb.toString();
    }

    static void ciphers() {
        String coded = caesar(Data.MESSAGE, Data.SHIFT);
        // Decoder = decaler de 26 - shift (un decalage negatif donnerait un % negatif en Java).
        System.out.println("cesar : " + coded + " | " + caesar(coded, 26 - Data.SHIFT) + " | rot13 deux fois " + caesar(caesar(Data.MESSAGE, 13), 13));

        String text = Data.VIGENERE_TEXT;
        String key = Data.VIGENERE_KEY;
        char[] enc = new char[text.length()];
        char[] dec = new char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            int k = key.charAt(i % key.length()) - 'A';     // la cle se repete
            enc[i] = (char) ('A' + (text.charAt(i) - 'A' + k) % 26);
        }
        for (int i = 0; i < enc.length; i++) {
            int k = key.charAt(i % key.length()) - 'A';
            dec[i] = (char) ('A' + (enc[i] - 'A' - k + 26) % 26);
        }
        System.out.println("vigenere : " + new String(enc) + " | " + String.valueOf(dec));
    }

    // ---------------------------------------------------------------- 4. grands nombres
    static void bigNumbers() {
        String a = Data.BIG_A;
        String b = Data.BIG_B;
        // Addition comme a l'ecole : de droite a gauche, avec une retenue ; on construit a l'envers puis reverse().
        StringBuilder sum = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int d = carry;
            if (i >= 0) {
                d += a.charAt(i--) - '0';
            }
            if (j >= 0) {
                d += b.charAt(j--) - '0';
            }
            sum.append(d % 10);
            carry = d / 10;
        }
        System.out.println("addition : " + sum.reverse() + " (" + sum.length() + " chiffres, long max " + Long.MAX_VALUE + ")");

        // Factorielle exacte : un int[] de chiffres, a l'envers (unites en case 0).
        int[] digits = new int[40];
        digits[0] = 1;
        int size = 1;
        for (int f = 2; f <= Data.FACTORIAL; f++) {
            int c = 0;
            for (int k = 0; k < size; k++) {
                int product = digits[k] * f + c;
                digits[k] = product % 10;
                c = product / 10;
            }
            while (c > 0) {
                digits[size++] = c % 10;
                c /= 10;
            }
        }
        StringBuilder fact = new StringBuilder();
        for (int k = size - 1; k >= 0; k--) {
            fact.append(digits[k]);
        }
        int zeros = 0;
        while (fact.charAt(fact.length() - 1 - zeros) == '0') {   // chaque 0 final vient d'un facteur 10 = 2 x 5
            zeros++;
        }
        System.out.println("factorielle " + Data.FACTORIAL + " : " + fact + " (" + zeros + " zeros a la fin)");
    }

    // ---------------------------------------------------------------- 5. prefixe et rotation
    static void prefixAndRotation() {
        String prefix = Data.PREFIX_WORDS[0];
        for (String w : Data.PREFIX_WORDS) {
            // On raccourcit le prefixe tant que le mot ne commence pas par lui.
            while (!w.startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
        System.out.println("prefixe commun : " + prefix);

        String[][] tests = {{"waterbottle", "erbottlewat"}, {"abcd", "cdab"}, {"abcd", "acbd"}, {"aa", "a"}};
        StringBuilder out = new StringBuilder("rotations :");
        for (String[] t : tests) {
            // Astuce : b est une rotation de a si b apparait dans a + a (et meme longueur).
            boolean rotation = t[0].length() == t[1].length() && t[0].concat(t[0]).contains(t[1]);
            out.append(' ').append(t[1]).append('=').append(rotation).append(rotation ? "(" + (t[0].concat(t[0]).indexOf(t[1])) + ")" : "");
        }
        System.out.println(out);

        String hay = "bananarama ananas";
        String needle = "ana";
        int overlapping = 0;
        for (int from = hay.indexOf(needle); from >= 0; from = hay.indexOf(needle, from + 1)) {
            overlapping++;              // from + 1 : on accepte les chevauchements
        }
        int separate = 0;
        for (int from = hay.indexOf(needle); from >= 0; from = hay.indexOf(needle, from + needle.length())) {
            separate++;
        }
        System.out.println("occurrences de " + needle + " : " + overlapping + " avec chevauchement, " + separate + " sans");
    }

    // ---------------------------------------------------------------- 6. palindrome
    static void palindrome() {
        String s = Data.PALINDROME_SOURCE;
        int bestStart = 0;
        int bestLength = 1;
        // Expansion autour de chaque centre : 2n - 1 centres (sur une lettre, ou entre deux lettres).
        for (int center = 0; center < 2 * s.length() - 1; center++) {
            int left = center / 2;
            int right = left + center % 2;
            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
            }
            int length = right - left - 1;
            if (length > bestLength) {
                bestLength = length;
                bestStart = left + 1;
            }
        }
        String best = s.substring(bestStart, bestStart + bestLength);
        System.out.println("plus long palindrome : " + best + " (" + bestLength + ") verifie " + new StringBuilder(best).reverse().toString().equals(best));
    }

    // ---------------------------------------------------------------- 7. justification
    static void justify() {
        String[] words = Data.PARAGRAPH.split(" ");
        int i = 0;
        while (i < words.length) {
            // 1. Glouton : on prend autant de mots que possible (longueurs + un espace minimum entre eux).
            int j = i;
            int letters = 0;
            while (j < words.length && letters + words[j].length() + (j - i) <= Data.WIDTH) {
                letters += words[j].length();
                j++;
            }
            StringBuilder line = new StringBuilder();
            int gaps = j - i - 1;
            if (j == words.length || gaps == 0) {
                // Derniere ligne (ou mot seul) : alignee a gauche, completee a droite.
                line.append(String.join(" ", Arrays.copyOfRange(words, i, j)));
                line.append(" ".repeat(Data.WIDTH - line.length()));
            } else {
                // 2. Les espaces en trop sont repartis, les premiers trous en recoivent un de plus.
                int spaces = Data.WIDTH - letters;
                for (int k = i; k < j; k++) {
                    line.append(words[k]);
                    if (k < j - 1) {
                        int g = k - i;
                        line.append(" ".repeat(spaces / gaps + (g < spaces % gaps ? 1 : 0)));
                    }
                }
            }
            System.out.println("|" + line + "|");
            i = j;
        }
    }

    // ---------------------------------------------------------------- 8. tri sans casse
    static void caseInsensitiveSort() {
        String[] w = Data.UNSORTED.clone();
        // Tri par insertion avec compareToIgnoreCase ; a egalite, l'ordre d'origine est garde (stable).
        for (int i = 1; i < w.length; i++) {
            String key = w[i];
            int j = i - 1;
            while (j >= 0 && w[j].compareToIgnoreCase(key) > 0) {
                w[j + 1] = w[j];
                j--;
            }
            w[j + 1] = key;
        }
        String[] natural = Data.UNSORTED.clone();
        Arrays.sort(natural);
        System.out.println("sans casse : " + String.join(" ", w) + " | naturel : " + String.join(" ", natural));
    }

    // ---------------------------------------------------------------- 9. chiffres romains
    static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    static void romans() {
        StringBuilder out = new StringBuilder("romains :");
        for (int n : Data.ROMAN_NUMBERS) {
            StringBuilder roman = new StringBuilder();
            int rest = n;
            for (int k = 0; k < VALUES.length; k++) {   // glouton : la plus grande valeur possible d'abord
                while (rest >= VALUES[k]) {
                    roman.append(SYMBOLS[k]);
                    rest -= VALUES[k];
                }
            }
            out.append(' ').append(n).append('=').append(roman);
        }
        System.out.println(out);

        StringBuilder back = new StringBuilder("relus :");
        for (String r : Data.ROMAN_TEXTS) {
            int total = 0;
            for (int k = 0; k < r.length(); k++) {
                int v = value(r.charAt(k));
                // Une valeur plus petite AVANT une plus grande se soustrait (IV, XC, CM).
                if (k + 1 < r.length() && v < value(r.charAt(k + 1))) {
                    total -= v;
                } else {
                    total += v;
                }
            }
            back.append(' ').append(r).append('=').append(total);
        }
        System.out.println(back);
    }

    static int value(char c) {
        return switch (c) {
            case 'I' -> 1;
            case 'V' -> 5;
            case 'X' -> 10;
            case 'L' -> 50;
            case 'C' -> 100;
            case 'D' -> 500;
            case 'M' -> 1000;
            default -> 0;
        };
    }

    // ---------------------------------------------------------------- 10. bases
    static void bases() {
        int[] numbers = {10, 255, 2026};
        StringBuilder out = new StringBuilder("bases :");
        String digits = "0123456789ABCDEF";
        for (int n : numbers) {
            StringBuilder bin = new StringBuilder();
            StringBuilder hex = new StringBuilder();
            for (int x = n; x > 0; x /= 2) {
                bin.insert(0, x % 2);                  // insert(0, …) : on ecrit de droite a gauche
            }
            for (int x = n; x > 0; x /= 16) {
                hex.insert(0, digits.charAt(x % 16));
            }
            boolean same = bin.toString().equals(Integer.toBinaryString(n)) && hex.toString().equalsIgnoreCase(Integer.toHexString(n));
            out.append(' ').append(n).append('=').append(bin).append("b/").append(hex).append('h').append(same ? "" : "!");
        }
        System.out.println(out + " relu " + Integer.parseInt("11111101010", 2) + " " + Integer.parseInt("FF", 16));
    }
}
