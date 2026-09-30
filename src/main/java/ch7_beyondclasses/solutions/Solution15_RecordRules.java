package ch7_beyondclasses.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise15_RecordRules.
 */
public class Solution15_RecordRules {

    public static String declarationError(List<String> modifiers, boolean extendsClass, boolean hasInstanceField,
                                          boolean hasInstanceInitializer) {
        // Tout ce qui menace l'immuabilite (heritage, abstract, etat en plus) est refuse.
        if (extendsClass) {
            return "'{' expected";
        }
        if (modifiers.contains("abstract")) {
            return "modifier abstract not allowed here";
        }
        if (hasInstanceField) {
            return "field declaration must be static";
        }
        if (hasInstanceInitializer) {
            return "instance initializers not allowed in records";
        }
        return "OK";
    }

    public static String accessorError(boolean isPublic, boolean sameTypeAsComponent) {
        // Un accesseur ecrit a la main doit ressembler exactement a celui que javac aurait genere.
        return isPublic && sameTypeAsComponent ? "OK" : "invalid accessor method in record R";
    }

    public static String constructorError(String kind, boolean assignsField, boolean hasReturn, boolean firstCallsThis) {
        // Compact : on travaille sur les PARAMETRES, javac affecte les champs a la fin.
        return switch (kind) {
            case "compact" -> hasReturn ? "invalid compact constructor in record <init>"
                    : assignsField ? "cannot assign a value to final variable x" : "OK";
            case "canonical" -> assignsField ? "OK" : "variable x might not have been initialized";
            default -> firstCallsThis ? "OK"
                    : "constructor is not canonical, so its first statement must invoke another constructor of class R";
        };
    }

    public static List<String> generatedMembers(List<String> components) {
        // Un accesseur par composant (meme nom, sans "get"), puis les 3 methodes d'Object.
        List<String> members = new ArrayList<>();
        for (String component : components) {
            String[] parts = component.split(" ");
            members.add("public " + parts[0] + " " + parts[1] + "()");
        }
        members.add("equals");
        members.add("hashCode");
        members.add("toString");
        return members;
    }
}
