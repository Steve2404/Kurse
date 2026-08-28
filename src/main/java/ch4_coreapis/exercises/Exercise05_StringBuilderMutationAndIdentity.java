package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 5 - StringBuilder : substring() ne mute PAS, mais append/delete/insert OUI - et equals() compare l'IDENTITE (niveau : difficile)
 * ====================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise04_StringBuilderBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * PIEGE CLASSIQUE de l'examen : parmi toutes les methodes de
 * StringBuilder, substring() fait EXCEPTION - elle se comporte comme
 * chez String (elle REND un NOUVEAU String, sans jamais toucher au
 * StringBuilder d'origine). append(), delete() et insert(), EUX,
 * modifient VRAIMENT l'objet sur lequel on les appelle, EN PLACE.
 *
 * AUTRE PIEGE, encore plus sournois : equals() sur StringBuilder n'a
 * JAMAIS ete redefini pour comparer le CONTENU (contrairement a
 * String) - il se comporte comme l'equals() par defaut d'Object (voir
 * le chapitre "Class Design") : 2 StringBuilder avec EXACTEMENT le
 * meme texte restent "differents" pour equals(), sauf si c'est
 * VRAIMENT le MEME objet. == se comporte pareil (identite), comme
 * toujours pour un type reference.
 *
 *
 * ==================================================================
 * TODO 1 : extractWithoutMutating(sb)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer sb.substring(0, 3) - RIEN d'autre : sb, LUI, ne doit
 *      subir AUCUNE modification.
 *
 *
 * ==================================================================
 * TODO 2 : deleteMiddle(sb)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec sb = "HelloWorld" (H=0, e=1, l=2, l=3, o=4, W=5...), delete(2, 5)
 * enleve les caracteres des index 2 (inclus) a 5 (EXCLU) - "l", "l",
 * "o" - laissant "He" + "World" = "HeWorld".
 *
 * -- Le plan --
 *
 *   1. Appeler sb.delete(2, 5) (AUCUN retour a recuperer : sb est
 *      modifie EN PLACE).
 *
 *
 * ==================================================================
 * TODO 3 : insertAtStart(sb, text)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler sb.insert(0, text) (encore une fois, AUCUN retour a
 *      recuperer).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : avec sb1 = new StringBuilder("Hello"),
 * extractWithoutMutating(sb1) == "Hel", ET sb1.toString() vaut
 * TOUJOURS "Hello" apres l'appel (substring() n'a RIEN mute). Avec
 * sb2 = new StringBuilder("HelloWorld"), deleteMiddle(sb2) : sb2
 * vaut ENSUITE "HeWorld" (mute en place). Avec sb3 = new
 * StringBuilder("World"), insertAtStart(sb3, "Hello "): sb3 vaut
 * ENSUITE "Hello World" (mute en place). Enfin, 2 StringBuilder
 * differents avec EXACTEMENT le meme texte ne sont JAMAIS .equals()
 * (voir main()).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - delete(debut, fin) suit la MEME regle "fin exclu" que
 *     substring() (voir Exercise03).
 */
public class Exercise05_StringBuilderMutationAndIdentity {

    public static String extractWithoutMutating(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 1 : implementer extractWithoutMutating()");
    }

    public static void deleteMiddle(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 2 : implementer deleteMiddle()");
    }

    public static void insertAtStart(StringBuilder sb, String text) {
        throw new UnsupportedOperationException("TODO 3 : implementer insertAtStart()");
    }

    public static void main(String[] args) {
        StringBuilder sb1 = new StringBuilder("Hello");
        String extracted = extractWithoutMutating(sb1);
        ExerciseChecker.check("extractWithoutMutating() rend le bon extrait", extracted.equals("Hel"));
        ExerciseChecker.check("substring() ne mute PAS sb1", sb1.toString().equals("Hello"));

        StringBuilder sb2 = new StringBuilder("HelloWorld");
        deleteMiddle(sb2);
        ExerciseChecker.check("delete() mute sb2 EN PLACE", sb2.toString().equals("HeWorld"));

        StringBuilder sb3 = new StringBuilder("World");
        insertAtStart(sb3, "Hello ");
        ExerciseChecker.check("insert() mute sb3 EN PLACE", sb3.toString().equals("Hello World"));

        StringBuilder same1 = new StringBuilder("Twin");
        StringBuilder same2 = new StringBuilder("Twin");
        ExerciseChecker.check("equals() sur StringBuilder compare l'IDENTITE, pas le contenu",
                !same1.equals(same2) && !(same1 == same2));

        ExerciseChecker.summary();
    }
}
