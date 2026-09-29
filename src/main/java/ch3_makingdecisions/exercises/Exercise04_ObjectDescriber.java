package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 4 - Decrire n'importe quel objet : une chaine de if + instanceof ou l'ORDRE decide tout (niveau : difficile)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Le contexte --
 *
 * Un journal recoit des valeurs de toute sorte (Object) et doit
 * ecrire une phrase pour chacune. Le piege : un Integer EST AUSSI un
 * Number. Si on teste "Number" avant "Integer", la branche Integer
 * n'est jamais atteinte. Il faut toujours tester du PLUS PRECIS au
 * PLUS GENERAL.
 *
 * Le resultat attendu, dans cet ordre de priorite :
 *
 *   null                         -> "rien"
 *   Integer negatif              -> "entier negatif"
 *   Integer                      -> "entier 42"
 *   Double qui est NaN           -> "pas un nombre"
 *   autre Number (Double, Long…) -> "nombre 2.5"
 *   String blanche               -> "texte vide"
 *   String                       -> "texte de 5 lettres"
 *   Character chiffre            -> "chiffre 7"
 *   int[]                        -> "tableau de 3 cases"
 *   List                         -> "liste de 2 elements"
 *   tout le reste                -> "inconnu Boolean"  (nom simple de la classe)
 *
 *
 * ==================================================================
 * TODO 1 : describeNumber(o)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On trie les nombres, du plus precis au plus general. Si ce n'est pas
 * un nombre du tout, on rend null ("pas mon rayon").
 *
 * -- Essayons a la main --
 *
 *   -3 -> "entier negatif" ; 42 -> "entier 42" ; Double.NaN -> "pas un nombre"
 *   2.5 -> "nombre 2.5" ; 7L -> "nombre 7" ; "x" -> null
 *
 * -- Le plan --
 *
 *   1. Integer i && i < 0 -> "entier negatif".
 *   2. Integer i -> "entier " + i.
 *   3. Double d && d.isNaN() -> "pas un nombre".
 *   4. Number n -> "nombre " + n.
 *   5. Sinon null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 2 : describeText(o)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "   " -> "texte vide" ; "java." -> "texte de 5 lettres" ;
 *   '7' -> "chiffre 7" ; 'x' -> null ; 3 -> null
 *
 * -- Le plan --
 *
 *   1. String s && s.isBlank() -> "texte vide".
 *   2. String s -> "texte de " + longueur + " lettres".
 *   3. Character c && c est un chiffre -> "chiffre " + c.
 *   4. Sinon null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 3 : describeContainer(o)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un tableau est un objet : "o instanceof int[] arr" marche. Pour une
 * liste, on teste List<?> (le ? veut dire "liste de n'importe quoi").
 *
 * -- Essayons a la main --
 *
 *   new int[3] -> "tableau de 3 cases" ; List.of("a", "b") -> "liste de 2 elements" ; "x" -> null
 *
 * -- Le plan --
 *
 *   1. int[] arr -> "tableau de " + arr.length + " cases".
 *   2. List<?> list -> "liste de " + list.size() + " elements".
 *   3. Sinon null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 4 : describe(o)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On demande a chaque specialiste (nombres, textes, conteneurs) ; le
 * premier qui repond autre chose que null a gagne. null tout seul est
 * gere en premier (aucun instanceof ne l'attrape).
 *
 * -- Le plan --
 *
 *   1. o == null -> "rien".
 *   2. Essayer describeNumber, puis describeText, puis describeContainer.
 *   3. Personne -> "inconnu " + nom simple de la classe de o.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1, 2 et 3.
 *
 *
 * Exemple a verifier : le tableau ci-dessus.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - if (o instanceof Integer i && i < 0) return "entier negatif";
 *   - Character.isDigit(c) ; o.getClass().getSimpleName()
 *   - String result = describeNumber(o); if (result != null) return result;
 */
public class Exercise04_ObjectDescriber {

    public static String describeNumber(Object o) {
        throw new UnsupportedOperationException("TODO 1 : implementer describeNumber()");
    }

    public static String describeText(Object o) {
        throw new UnsupportedOperationException("TODO 2 : implementer describeText()");
    }

    public static String describeContainer(Object o) {
        throw new UnsupportedOperationException("TODO 3 : implementer describeContainer()");
    }

    public static String describe(Object o) {
        throw new UnsupportedOperationException("TODO 4 : implementer describe()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 describeNumber : -3, 42, NaN, 2.5, 7L, x",
                "entier negatif".equals(describeNumber(-3)) && "entier 42".equals(describeNumber(42))
                        && "pas un nombre".equals(describeNumber(Double.NaN)) && "nombre 2.5".equals(describeNumber(2.5))
                        && "nombre 7".equals(describeNumber(7L)) && describeNumber("x") == null);
        ExerciseChecker.check("2 describeText : blanc, java., '7', 'x', 3",
                "texte vide".equals(describeText("   ")) && "texte de 5 lettres".equals(describeText("java."))
                        && "chiffre 7".equals(describeText('7')) && describeText('x') == null && describeText(3) == null);
        ExerciseChecker.check("3 describeContainer : int[3], List.of(a, b), x",
                "tableau de 3 cases".equals(describeContainer(new int[3]))
                        && "liste de 2 elements".equals(describeContainer(List.of("a", "b"))) && describeContainer("x") == null);
        ExerciseChecker.check("4 describe(null) == rien, (true) == inconnu Boolean",
                describe(null).equals("rien") && describe(true).equals("inconnu Boolean"));
        ExerciseChecker.check("4 describe : 42 -> entier 42 (et pas nombre 42 : l'ordre compte)",
                describe(42).equals("entier 42") && describe("java.").equals("texte de 5 lettres")
                        && describe(new int[3]).equals("tableau de 3 cases"));

        ExerciseChecker.summary();
    }
}
