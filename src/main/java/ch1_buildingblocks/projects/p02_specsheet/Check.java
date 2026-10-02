package ch1_buildingblocks.projects.p02_specsheet;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON SpecSheet, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "=== LES 8 TYPES PRIMITIFS ===",
            "byte    : 8 bits, de -128 a 127, defaut 0",
            "short   : 16 bits, de -32768 a 32767, defaut 0",
            "int     : 32 bits, de -2147483648 a 2147483647, defaut 0",
            "long    : 64 bits, de -9223372036854775808 a 9223372036854775807, defaut 0",
            "float   : 32 bits, defaut 0.0",
            "double  : 64 bits, defaut 0.0",
            "char    : 16 bits, de 0 a 65535, defaut (code) 0",
            "boolean : taille non fixee par Java, defaut false",
            "=== UN NOMBRE, CINQ ECRITURES ===",
            "decimal 255 | binaire 255 | octal 255 | hexa 255 | avec _ 255",
            "255 s'ecrit 11111111 en binaire, 377 en octal, ff en hexa",
            "litteraux : 3000000000 2.5 1000.0 1000000",
            "=== CARACTERES ===",
            "A A A 66",
            "=== CONVERSIONS PAR LES ENVELOPPES ===",
            "Double.valueOf(\"3.99\").intValue() = 3",
            "Integer.valueOf(300).byteValue() = 44",
            "Integer.parseInt(\"ff\", 16) = 255, Integer.parseInt(\"-101\", 2) = -5",
            "Double.parseDouble(\"1e3\") = 1000.0, Float.valueOf(\"2.5\").doubleValue() = 2.5",
            "Boolean.parseBoolean(\"TRUE\") = true, Boolean.parseBoolean(\"oui\") = false",
            "Long.valueOf(\"42\").longValue() + 1 = 43, Short.parseShort(\"-7\") = -7");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Byte.SIZE", "Byte.MIN_VALUE", "Short.MAX_VALUE", "Integer.MAX_VALUE", "Long.MIN_VALUE", "Float.SIZE",
            "Double.SIZE", "Character.SIZE", "Character.MAX_VALUE",
            "re:\\b0[bB][01_]*_[01_]*\\b##litteral binaire avec _ (0b..._...)",
            "re:\\b0[0-7]+\\b##litteral octal (0...)", "re:\\b0[xX][0-9a-fA-F]+\\b##litteral hexadecimal (0x...)",
            "re:\\b\\d+(_\\d+)+L\\b##litteral long avec _ et L", "re:\\b\\d+\\.\\d+[fF]\\b##litteral float (...f)",
            "re:\\b\\d+[eE]\\d+\\b##litteral double en notation scientifique (1e3)", "'\\u",
            "Integer.toBinaryString(", "Integer.toOctalString(", "Integer.toHexString(",
            ".intValue()", ".byteValue()", ".doubleValue()", ".longValue()", "Integer.parseInt(", "Double.parseDouble(",
            "Float.valueOf(", "Boolean.parseBoolean(", "Short.parseShort(",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale", "!(int)##cast (int) (chapitre 2)", "!(char)##cast (char) (chapitre 2)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "SpecSheet", args, EXPECTED, API);
    }
}
