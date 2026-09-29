package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise07_IdentifierValidator.
 */
public class Solution07_IdentifierValidator {

    public static final String[] RESERVED = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
            "volatile", "while", "true", "false", "null"};

    public static boolean isReserved(String word) {
        // equals est sensible a la casse, comme Java : "Class" n'est pas "class".
        for (String reserved : RESERVED) {
            if (reserved.equals(word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasValidCharacters(String word) {
        // Java fournit deja ses propres regles : Start pour le 1er caractere (lettre, $, _),
        // Part pour les suivants (qui acceptent aussi les chiffres).
        if (word.isEmpty() || !Character.isJavaIdentifierStart(word.charAt(0))) {
            return false;
        }
        for (int i = 1; i < word.length(); i++) {
            if (!Character.isJavaIdentifierPart(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidIdentifier(String word) {
        // "_" seul passe les regles de caracteres mais est un mot-cle depuis Java 9 :
        // il faut le refuser a part.
        if (word == null || "_".equals(word)) {
            return false;
        }
        return hasValidCharacters(word) && !isReserved(word);
    }
}
