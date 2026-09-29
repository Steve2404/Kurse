package ch1_buildingblocks.drills.solutions;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.drills.exercises.Drill02_LiteralsAndPrimitives.
 */
public class SolutionDrill02_LiteralsAndPrimitives {

    static class Defaults {
        byte b;
        double d;
        boolean z;
        char c;
        String s;
    }

    public static int binaryTen() {
        // Prefixe 0b : 1*8 + 0*4 + 1*2 + 0*1 = 10.
        return 0b1010;
    }

    public static int octalTen() {
        // Un 0 devant = octal : 1*8 + 2 = 10. Piege : 010 ne vaut PAS 10 mais 8.
        return 012;
    }

    public static int hexTen() {
        // Prefixe 0x : A vaut 10.
        return 0xA;
    }

    public static int oneMillion() {
        // Les _ sont decoratifs, entre deux chiffres seulement.
        return 1_000_000;
    }

    public static long nineBillion() {
        // Sans L, 9000000000 ne compile pas : "integer number too large".
        return 9_000_000_000L;
    }

    public static float half() {
        // Sans f, 0.5 est un double : "float x = 0.5;" ne compile pas.
        return 0.5f;
    }

    public static char letterA() {
        // Un char s'ecrit entre apostrophes simples.
        return 'A';
    }

    public static char unicodeA() {
        // Code unicode hexadecimal 0041 = 65 = 'A'.
        return 'A';
    }

    public static char nextLetter(char c) {
        // c + 1 est un int (promotion) : le cast (char) est obligatoire.
        return (char) (c + 1);
    }

    public static int codeOf(char c) {
        // char -> int est un elargissement : aucun cast necessaire.
        return c;
    }

    public static byte byteMax() {
        // byte : 8 bits signes, de -128 a 127.
        return Byte.MAX_VALUE;
    }

    public static short shortMin() {
        // short : 16 bits signes.
        return Short.MIN_VALUE;
    }

    public static int overflow() {
        // Aucun controle : le compteur fait le tour et repart du plus petit int.
        return Integer.MAX_VALUE + 1;
    }

    public static String defaults() {
        // Les CHAMPS recoivent 0 / 0.0 / false / '\u0000' / null ; le char est affiche par son code.
        Defaults x = new Defaults();
        return x.b + "/" + x.d + "/" + x.z + "/" + (int) x.c + "/" + x.s;
    }

    public static long widen(int value) {
        // int -> long est un elargissement automatique : pas de cast.
        return value;
    }
}
