package ch1_buildingblocks.projects.p02_specsheet.solution;

/**
 * SOLUTION du projet 2 - une conception possible.
 */
public class SpecSheet {

    // Des CHAMPS jamais initialises : Java leur donne une valeur par defaut.
    // (Une variable LOCALE non initialisee, elle, ne compilerait pas si on la lisait.)
    static byte defaultByte;
    static short defaultShort;
    static int defaultInt;
    static long defaultLong;
    static float defaultFloat;
    static double defaultDouble;
    static char defaultChar;
    static boolean defaultBoolean;

    public static void main(String[] args) {
        System.out.println("=== LES 8 TYPES PRIMITIFS ===");
        System.out.println("byte    : " + Byte.SIZE + " bits, de " + Byte.MIN_VALUE + " a " + Byte.MAX_VALUE + ", defaut " + defaultByte);
        System.out.println("short   : " + Short.SIZE + " bits, de " + Short.MIN_VALUE + " a " + Short.MAX_VALUE + ", defaut " + defaultShort);
        System.out.println("int     : " + Integer.SIZE + " bits, de " + Integer.MIN_VALUE + " a " + Integer.MAX_VALUE + ", defaut " + defaultInt);
        System.out.println("long    : " + Long.SIZE + " bits, de " + Long.MIN_VALUE + " a " + Long.MAX_VALUE + ", defaut " + defaultLong);
        System.out.println("float   : " + Float.SIZE + " bits, defaut " + defaultFloat);
        System.out.println("double  : " + Double.SIZE + " bits, defaut " + defaultDouble);
        // char est numerique (non signe) : l'affecter a un int donne son code, sans perte.
        int minChar = Character.MIN_VALUE;
        int maxChar = Character.MAX_VALUE;
        int defaultCharCode = defaultChar;
        System.out.println("char    : " + Character.SIZE + " bits, de " + minChar + " a " + maxChar + ", defaut (code) " + defaultCharCode);
        System.out.println("boolean : taille non fixee par Java, defaut " + defaultBoolean);

        System.out.println("=== UN NOMBRE, CINQ ECRITURES ===");
        int decimal = 255;
        int binary = 0b1111_1111;  // prefixe 0b ; les _ ne sont permis qu'ENTRE deux chiffres
        int octal = 0377;          // un 0 en tete = OCTAL (piege classique : 010 vaut 8)
        int hexa = 0xFF;           // prefixe 0x, lettres en majuscules ou minuscules
        int underscored = 2_5_5;
        System.out.println("decimal " + decimal + " | binaire " + binary + " | octal " + octal + " | hexa " + hexa + " | avec _ " + underscored);
        System.out.println("255 s'ecrit " + Integer.toBinaryString(255) + " en binaire, " + Integer.toOctalString(255)
                + " en octal, " + Integer.toHexString(255) + " en hexa");
        long big = 3_000_000_000L;  // sans le L, le litteral serait un int trop grand : erreur de compilation
        float price = 2.5f;          // sans le f, 2.5 est un double
        double thousand = 1e3;
        System.out.println("litteraux : " + big + " " + price + " " + thousand + " " + 1_000_000);

        System.out.println("=== CARACTERES ===");
        char letter = 'A';
        char unicode = '\u0041';
        char fromCode = 65;         // un int CONSTANT qui tient dans un char s'affecte sans cast
        int nextCode = 'B';
        System.out.println(letter + " " + unicode + " " + fromCode + " " + nextCode);

        System.out.println("=== CONVERSIONS PAR LES ENVELOPPES ===");
        System.out.println("Double.valueOf(\"3.99\").intValue() = " + Double.valueOf("3.99").intValue());
        System.out.println("Integer.valueOf(300).byteValue() = " + Integer.valueOf(300).byteValue());
        System.out.println("Integer.parseInt(\"ff\", 16) = " + Integer.parseInt("ff", 16)
                + ", Integer.parseInt(\"-101\", 2) = " + Integer.parseInt("-101", 2));
        System.out.println("Double.parseDouble(\"1e3\") = " + Double.parseDouble("1e3")
                + ", Float.valueOf(\"2.5\").doubleValue() = " + Float.valueOf("2.5").doubleValue());
        System.out.println("Boolean.parseBoolean(\"TRUE\") = " + Boolean.parseBoolean("TRUE")
                + ", Boolean.parseBoolean(\"oui\") = " + Boolean.parseBoolean("oui"));
        System.out.println("Long.valueOf(\"42\").longValue() + 1 = " + (Long.valueOf("42").longValue() + 1)
                + ", Short.parseShort(\"-7\") = " + Short.parseShort("-7"));
    }
}
