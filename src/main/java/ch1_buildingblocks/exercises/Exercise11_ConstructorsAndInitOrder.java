package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 11 - Un constructeur, c'est "comme une methode sans type de retour" - et il parle TOUJOURS EN DERNIER (niveau : moyen)
 * =========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise10_VariableScopeBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un constructeur PORTE LE NOM EXACT de sa classe, et N'A JAMAIS de
 * type de retour (pas meme void) - c'est ce qui le distingue d'une
 * methode ordinaire qui, par coincidence, porterait le meme nom que
 * la classe. Quand un objet est cree (new Widget()), l'ordre est
 * TOUJOURS le meme : D'ABORD les initialiseurs de champs et les
 * blocs d'instance (dans l'ordre ou ils apparaissent dans le
 * fichier), et SEULEMENT ENSUITE le CORPS du constructeur - jamais
 * l'inverse, meme si le constructeur "a l'air" d'etre la toute
 * premiere chose executee.
 *
 *
 * ==================================================================
 * TODO : Widget() - le constructeur
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ajouter "constructor" a events (deja rempli, avant meme
 *      d'arriver ici, par le champ et le bloc d'instance - voir plus
 *      bas).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule ligne suffit.
 *
 * Exemple a verifier : new Widget().events == ["field-init",
 * "instance-block", "constructor"] - DANS CET ORDRE PRECIS, meme si
 * le constructeur, LUI, n'ajoute QUE le tout dernier element.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "Widget()" (sans aucun type de retour, pas meme void, et
 *     portant EXACTEMENT le nom de la classe) EST la signature d'un
 *     constructeur - "void Widget()" serait, LUI, une methode tout
 *     a fait ordinaire, qui ne s'executerait JAMAIS automatiquement
 *     a la creation d'un objet.
 */
public class Exercise11_ConstructorsAndInitOrder {

    static class Widget {
        private final List<String> events = new ArrayList<>();

        {
            events.add("field-init");
            events.add("instance-block");
        }

        Widget() {
            throw new UnsupportedOperationException("TODO : implementer le constructeur Widget()");
        }

        List<String> getEvents() {
            return events;
        }
    }

    public static void main(String[] args) {
        Widget widget = new Widget();

        ExerciseChecker.check("champs/blocs D'ABORD, constructeur EN DERNIER",
                widget.getEvents().equals(List.of("field-init", "instance-block", "constructor")));

        ExerciseChecker.summary();
    }
}
