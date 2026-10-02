package ch4_coreapis.projects.p08_textlab;

/**
 * Les donnees du projet 8 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les paires a tester : anagrammes ? (separees par '/'). */
    public static final String[] ANAGRAMS = {"Listen/Silent", "triangle/integral", "Dormitory/dirty room", "apple/paple", "abc/abd"};

    /** Le texte a compresser par RLE. */
    public static final String RLE = "aaabccddddde";

    /** Le message a chiffrer. */
    public static final String MESSAGE = "Hello, World!";

    /** Le decalage de Cesar. */
    public static final int SHIFT = 3;

    /** Le texte et la cle de Vigenere (majuscules seulement). */
    public static final String VIGENERE_TEXT = "ATTACKATDAWN";
    public static final String VIGENERE_KEY = "LEMON";

    /** Deux grands nombres, trop grands pour un long. */
    public static final String BIG_A = "98765432109876543210";
    public static final String BIG_B = "12345678901234567890";

    /** Le nombre dont on calcule la factorielle exacte. */
    public static final int FACTORIAL = 25;

    /** Les mots dont on cherche le plus long prefixe commun. */
    public static final String[] PREFIX_WORDS = {"interstellar", "internet", "interval", "internal"};

    /** Le texte ou l'on cherche le plus long palindrome. */
    public static final String PALINDROME_SOURCE = "forgeeksskeegfor";

    /** Le paragraphe a justifier, et la largeur des lignes. */
    public static final String PARAGRAPH = "Java strings are immutable so every method returns a new object while a builder changes itself";
    public static final int WIDTH = 24;

    /** Les mots a trier sans tenir compte de la casse. */
    public static final String[] UNSORTED = {"banana", "Apple", "cherry", "apple", "Banana", "date"};

    /** Les nombres a convertir en chiffres romains, et les romains a relire. */
    public static final int[] ROMAN_NUMBERS = {4, 9, 14, 1994, 2026, 3999};
    public static final String[] ROMAN_TEXTS = {"XLII", "MCMXC", "CDXLIV"};

    private Data() {
    }
}
