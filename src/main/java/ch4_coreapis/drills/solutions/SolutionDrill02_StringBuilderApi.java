package ch4_coreapis.drills.solutions;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill02_StringBuilderApi.
 */
public class SolutionDrill02_StringBuilderApi {

    public static String greet(String name) {
        // append rend le meme sb : on chaine, puis toString a la fin.
        return new StringBuilder("Bonjour, ").append(name).append(" !").toString();
    }

    public static String prefix(StringBuilder sb) {
        // insert(0, ...) ajoute devant.
        return sb.insert(0, "> ").toString();
    }

    public static String cut(StringBuilder sb) {
        // "journal " occupe les index 0 a 7 : fin EXCLUE = 8.
        return sb.delete(0, 8).toString();
    }

    public static String dropFirst(StringBuilder sb) {
        // deleteCharAt enleve UNE case.
        return sb.deleteCharAt(0).toString();
    }

    public static String swapWord(StringBuilder sb) {
        // replace(debut, fin, texte) : la longueur peut changer.
        return sb.replace(3, 6, "sty").toString();
    }

    public static String mirror(String text) {
        // String n'a pas reverse : on passe par un StringBuilder.
        return new StringBuilder(text).reverse().toString();
    }

    public static String capitalize(StringBuilder sb) {
        // setCharAt rend void : pas de chainage, on rend sb ensuite.
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static String keep(StringBuilder sb, int n) {
        // setLength plus petit coupe ; plus grand ajouterait des '\0'.
        sb.setLength(n);
        return sb.toString();
    }

    public static int findSecond(StringBuilder sb) {
        // StringBuilder a son propre indexOf(String, depart) (pas de version char).
        return sb.indexOf("an", 2);
    }

    public static String middle(StringBuilder sb) {
        // substring LIT sans modifier sb (piege classique).
        return sb.substring(1, 3);
    }

    public static boolean sameContent(String a, StringBuilder b) {
        // contentEquals compare le contenu ; b.equals(...) comparerait les adresses.
        return a.contentEquals(b);
    }

    public static String chain() {
        // Chaque appel travaille sur le resultat du precedent : 01456789 -> 01X456789 -> 987654X10 -> 87654X10.
        return new StringBuilder("0123456789").delete(2, 4).insert(2, "X").reverse().deleteCharAt(0).toString();
    }

    public static String mixed() {
        // append est surcharge pour chaque type primitif.
        return new StringBuilder().append(1).append('c').append(true).append(2.5).toString();
    }
}
