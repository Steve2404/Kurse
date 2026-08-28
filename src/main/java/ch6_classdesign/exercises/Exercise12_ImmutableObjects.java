package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 12 - Objets immuables : la "copie de securite" a l'entree ET a la sortie (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un objet immuable, c'est un objet qu'on ne peut JAMAIS modifier
 * APRES sa creation - un peu comme une photo imprimee : une fois
 * developpee, elle ne change plus jamais, meme si le paysage
 * qu'elle montrait a change depuis. La recette classique : un
 * constructeur PRIVATE (la SEULE facon d'en creer un passe par la
 * "fabrique" of(...) fournie plus bas), AUCUN setter, et pour les
 * champs qui contiennent eux-memes un objet MUTABLE (comme une
 * List<String>, qui PEUT changer apres coup) : une COPIE DE SECURITE,
 * a la fois en ENTRANT (au moment de recevoir la liste dans le
 * constructeur - sinon l'APPELANT pourrait la modifier de son cote
 * plus tard, et notre "photo" changerait quand meme !) et en
 * SORTANT (au moment de la rendre via tags() - sinon L'APPELANT
 * pourrait modifier NOTRE copie interne directement !).
 *
 *
 * ==================================================================
 * TODO 1 : le constructeur - copie de securite a l'ENTREE
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ranger x et y directement (des int, pas des objets mutables :
 *      aucune copie necessaire pour eux).
 *   2. Pour tags : this.tags = new ArrayList<>(tags) - une COPIE de
 *      la liste recue, jamais la liste recue ELLE-MEME.
 *
 *
 * ==================================================================
 * TODO 2 : tags() - copie de securite a la SORTIE
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer new ArrayList<>(tags) - encore une COPIE, jamais le
 *      champ tags LUI-MEME.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : on cree originalList = ["a", "b"], puis point
 * = ImmutablePoint.of(1, 2, originalList). On modifie ENSUITE
 * originalList (on y ajoute "c") : point.tags() doit rester ["a",
 * "b"] (immunise grace a la copie a l'ENTREE). On recupere aussi
 * received = point.tags(), et on modifie CETTE liste recue : un
 * DEUXIEME point.tags() doit ENCORE rendre ["a", "b"] (immunise
 * grace a la copie a la SORTIE).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "new ArrayList<>(tags)" cree une TOUTE NOUVELLE liste, avec les
 *     MEMES elements recopies dedans - contrairement a "this.tags =
 *     tags", qui, lui, ne fait que pointer les 2 noms vers LA MEME
 *     liste en memoire (aucune protection).
 */
public class Exercise12_ImmutableObjects {

    static final class ImmutablePoint {
        private final int x;
        private final int y;
        private final List<String> tags;

        private ImmutablePoint(int x, int y, List<String> tags) {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur");
        }

        static ImmutablePoint of(int x, int y, List<String> tags) {
            return new ImmutablePoint(x, y, tags);
        }

        int x() {
            return x;
        }

        int y() {
            return y;
        }

        List<String> tags() {
            throw new UnsupportedOperationException("TODO 2 : implementer tags()");
        }
    }

    public static void main(String[] args) {
        List<String> originalList = new ArrayList<>(List.of("a", "b"));
        ImmutablePoint point = ImmutablePoint.of(1, 2, originalList);

        originalList.add("c");
        ExerciseChecker.check("copie de securite a l'ENTREE : modifier la liste ORIGINALE ne touche pas le point",
                point.tags().equals(List.of("a", "b")));

        List<String> received = point.tags();
        received.add("z");
        ExerciseChecker.check("copie de securite a la SORTIE : modifier la liste RECUE ne touche pas le point",
                point.tags().equals(List.of("a", "b")));

        ExerciseChecker.check("x() et y() restent inchanges", point.x() == 1 && point.y() == 2);

        ExerciseChecker.summary();
    }
}
