package methods.exercises;

import methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 9 - Pass-by-value (suite) : MUTER un objet parametre CHANGE bien l'appelant (niveau : moyen)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * methods.exercises.Exercise01_MethodDeclarationQuiz.java. Ce
 * qui suit fait directement suite a l'Exercise08 : LA MEME regle de
 * "photocopie de l'adresse", mais avec une consequence OPPOSEE.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'Exercise08 a montre que REASSIGNER le parametre (sb = ...) ne
 * change RIEN pour l'appelant, car ca ne fait que pointer LA
 * PHOTOCOPIE de l'adresse ailleurs. Mais si, au lieu de reassigner
 * sb, on appelle une methode QUI MODIFIE L'OBJET LUI-MEME
 * (sb.append("!")) - la, PAS DE PHOTOCOPIE DE L'OBJET, seulement de
 * son ADRESSE : la photocopie et l'original pointent tous les 2 vers
 * la MEME maison, donc modifier la maison (l'objet) EST BIEN visible
 * depuis les 2 adresses, y compris celle de l'appelant.
 *
 *
 * ==================================================================
 * TODO 1 : appendExclamation(sb)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler sb.append("!") - une MUTATION de l'objet existant,
 *      PAS une reassignation de sb.
 *
 *
 * ==================================================================
 * TODO 2 : addItem(list, item)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler list.add(item) - encore une mutation, PAS une
 *      reassignation de list.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : StringBuilder sb = new StringBuilder("Hi");
 * appendExclamation(sb); sb.toString() vaut MAINTENANT "Hi!" (change,
 * contrairement a l'Exercise08). List<String> list = new
 * ArrayList<>(); addItem(list, "a"); list contient MAINTENANT ["a"]
 * (change aussi).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "sb.append(...)" et "sb = new StringBuilder(...)" (Exercise08)
 *     se RESSEMBLENT, mais sont FONDAMENTALEMENT differents :
 *     append() modifie l'objet POINTE, l'affectation, elle, change
 *     ce QUE sb pointe - seule la 2e est "invisible" pour l'appelant.
 */
public class Exercise09_PassByValueMutation {

    public static void appendExclamation(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 1 : implementer appendExclamation()");
    }

    public static void addItem(List<String> list, String item) {
        throw new UnsupportedOperationException("TODO 2 : implementer addItem()");
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hi");
        appendExclamation(sb);
        ExerciseChecker.check("muter l'objet (append) CHANGE bien la variable de l'appelant",
                sb.toString().equals("Hi!"));

        List<String> list = new ArrayList<>();
        addItem(list, "a");
        ExerciseChecker.check("muter l'objet (add) CHANGE bien la variable de l'appelant",
                list.equals(List.of("a")));

        ExerciseChecker.summary();
    }
}
