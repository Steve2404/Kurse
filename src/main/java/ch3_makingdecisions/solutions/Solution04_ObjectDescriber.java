package ch3_makingdecisions.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise04_ObjectDescriber.
 */
public class Solution04_ObjectDescriber {

    public static String describeNumber(Object o) {
        // Du plus precis au plus general : un Integer est aussi un Number, il doit passer avant.
        // Le && ajoute une condition sur la variable de pattern, deja disponible a droite.
        if (o instanceof Integer i && i < 0) {
            return "entier negatif";
        }
        if (o instanceof Integer i) {
            return "entier " + i;
        }
        if (o instanceof Double d && d.isNaN()) {
            return "pas un nombre";
        }
        if (o instanceof Number n) {
            return "nombre " + n;
        }
        return null;
    }

    public static String describeText(Object o) {
        // isBlank (Java 11) : vide ou seulement des espaces.
        if (o instanceof String s && s.isBlank()) {
            return "texte vide";
        }
        if (o instanceof String s) {
            return "texte de " + s.length() + " lettres";
        }
        if (o instanceof Character c && Character.isDigit(c)) {
            return "chiffre " + c;
        }
        return null;
    }

    public static String describeContainer(Object o) {
        // Un tableau est un objet ; pour une liste generique, on teste List<?>.
        if (o instanceof int[] arr) {
            return "tableau de " + arr.length + " cases";
        }
        if (o instanceof List<?> list) {
            return "liste de " + list.size() + " elements";
        }
        return null;
    }

    public static String describe(Object o) {
        // null d'abord : aucun instanceof ne l'attrape. Puis le premier specialiste qui repond gagne.
        if (o == null) {
            return "rien";
        }
        String result = describeNumber(o);
        if (result != null) {
            return result;
        }
        result = describeText(o);
        if (result != null) {
            return result;
        }
        result = describeContainer(o);
        if (result != null) {
            return result;
        }
        return "inconnu " + o.getClass().getSimpleName();
    }
}
