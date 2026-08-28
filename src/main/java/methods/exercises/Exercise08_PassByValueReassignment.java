package methods.exercises;

import methods.ExerciseChecker;

/**
 * EXERCICE 8 - Pass-by-value : REASSIGNER un parametre NE CHANGE JAMAIS la variable de l'appelant (niveau : moyen)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java passe TOUJOURS ses parametres "par valeur" - c'est-a-dire
 * qu'une methode recoit une PHOTOCOPIE de ce que l'appelant lui
 * donne, jamais l'original lui-meme. Pour un int, la photocopie est
 * le NOMBRE lui-meme (5 recopie en 5). Pour un objet (comme un
 * StringBuilder), la photocopie n'est PAS l'objet - c'est
 * L'ADRESSE POSTALE de l'objet (la reference) qui est recopiee ; les
 * 2 adresses (celle de l'appelant, celle de la methode) pointent
 * ENCORE vers la MEME maison. Consequence dans les 2 cas :
 * REASSIGNER le parametre a l'interieur de la methode (value = ...,
 * sb = new StringBuilder(...)) ne modifie QUE la photocopie locale -
 * la variable de L'APPELANT, elle, ne bouge JAMAIS.
 *
 *
 * ==================================================================
 * TODO 1 : tryToDouble(value)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Reassigner value = value * 2 (juste pour PROUVER que ca ne
 *      change rien pour l'appelant - aucune valeur de retour ici).
 *
 *
 * ==================================================================
 * TODO 2 : tryToReplace(sb)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Reassigner sb = new StringBuilder("Replaced") (un TOUT NOUVEL
 *      objet - sb, la photocopie locale de l'ADRESSE, pointe
 *      desormais ailleurs, mais l'adresse de L'APPELANT, elle, n'a
 *      jamais bouge).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : int x = 5; tryToDouble(x); x vaut TOUJOURS 5
 * apres l'appel. StringBuilder original = new
 * StringBuilder("Original"); tryToReplace(original); original.toString()
 * vaut TOUJOURS "Original" apres l'appel.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Cette regle vaut EXACTEMENT pareil pour un int (une "vraie"
 *     valeur recopiee) et pour un StringBuilder (une reference
 *     recopiee) : dans les 2 cas, c'est la PHOTOCOPIE locale qui
 *     change de valeur, jamais l'original de l'appelant.
 */
public class Exercise08_PassByValueReassignment {

    public static void tryToDouble(int value) {
        throw new UnsupportedOperationException("TODO 1 : implementer tryToDouble()");
    }

    public static void tryToReplace(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 2 : implementer tryToReplace()");
    }

    public static void main(String[] args) {
        int x = 5;
        tryToDouble(x);
        ExerciseChecker.check("reassigner un parametre int ne change PAS la variable de l'appelant",
                x == 5);

        StringBuilder original = new StringBuilder("Original");
        tryToReplace(original);
        ExerciseChecker.check("reassigner un parametre objet ne change PAS la reference de l'appelant",
                original.toString().equals("Original"));

        ExerciseChecker.summary();
    }
}
