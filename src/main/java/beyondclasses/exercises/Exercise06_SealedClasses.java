package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 6 - Sealed classes : une liste FERMEE de sous-types autorises (niveau : difficile)
 * =================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * D'habitude, une interface (ou une classe) publique peut etre
 * implementee/etendue par N'IMPORTE QUI, meme du code que tu n'as
 * jamais vu, ecrit apres coup. "sealed" ferme cette porte : "Shape
 * permits Circle, Square, Rectangle" dit "SEULES ces 3 formes-la ont
 * le droit d'exister, JAMAIS une 4e forme inventee ailleurs" - un peu
 * comme un enum, mais ou chaque "valeur" peut porter des donnees et
 * un comportement completement differents (un Circle a un rayon, un
 * Rectangle a une largeur ET une hauteur).
 *
 * CHAQUE sous-type autorise DOIT choisir explicitement son propre
 * avenir avec un modificateur :
 *   - final : ferme la porte a son tour, personne ne peut en heriter.
 *   - sealed : rouvre une NOUVELLE liste fermee (elle-meme avec son
 *     propre permits).
 *   - non-sealed : rouvre la porte en GRAND, n'importe qui peut a
 *     nouveau en heriter librement (comme avant, sans sealed).
 *
 * Grace a cette liste FERMEE et CONNUE A L'AVANCE, le compilateur (et
 * nous, humains) peut RAISONNER sur TOUS les cas possibles - c'est
 * exactement ce que fait areaOf() plus bas, avec une chaine de
 * instanceof (le pattern matching de switch sur sealed, lui, est
 * encore une fonctionnalite "preview" en Java 17 - hors programme de
 * l'examen, on reste donc sur du instanceof classique, deja stable
 * depuis Java 16).
 *
 *
 * ==================================================================
 * TODO : areaOf(shape)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * areaOf(new Circle(2)) doit valoir PI * 2 * 2 (environ 12.57).
 * areaOf(new Square(3)) doit valoir 9. areaOf(new Rectangle(2, 5))
 * doit valoir 10.
 *
 * -- Le plan --
 *
 *   1. Si shape instanceof Circle c : renvoyer Math.PI * c.radius() * c.radius().
 *   2. Sinon si shape instanceof Square s : renvoyer s.side() * s.side().
 *   3. Sinon si shape instanceof Rectangle r : renvoyer r.width() * r.height().
 *   4. (Aucun "sinon" final necessaire : comme Shape est sealed et
 *      liste EXACTEMENT ces 3 sous-types, ces 3 cas couvrent TOUTES
 *      les possibilites - mais le compilateur, lui, exige quand meme
 *      un retour "de secours" en toute fin de methode pour etre
 *      totalement sur ; renvoyer 0 ne sera JAMAIS vraiment atteint.)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Circle, Square et Rectangle sont ici des records (voir
 *     Exercise08/09 pour le detail de ce que ca signifie) : radius(),
 *     side(), width(), height() sont leurs accesseurs AUTOMATIQUES,
 *     pas des champs a acceder directement.
 *   - "if (shape instanceof Circle c)" declare ET verifie ET caste
 *     EN UNE SEULE FOIS : si vrai, c EST DEJA du type Circle a
 *     l'interieur du bloc if, aucun cast manuel (Circle) necessaire.
 */
public class Exercise06_SealedClasses {

    sealed interface Shape permits Circle, Square, Rectangle {
    }

    record Circle(double radius) implements Shape {
    }

    record Square(double side) implements Shape {
    }

    record Rectangle(double width, double height) implements Shape {
    }

    public static double areaOf(Shape shape) {
        throw new UnsupportedOperationException("TODO : implementer areaOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("areaOf(Circle(2)) est proche de PI*4",
                Math.abs(areaOf(new Circle(2)) - Math.PI * 4) < 0.001);
        ExerciseChecker.check("areaOf(Square(3)) == 9.0", areaOf(new Square(3)) == 9.0);
        ExerciseChecker.check("areaOf(Rectangle(2, 5)) == 10.0", areaOf(new Rectangle(2, 5)) == 10.0);

        ExerciseChecker.summary();
    }
}
