package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 8 - Records : ce que le compilateur fabrique TOUT SEUL (niveau : moyen)
 * =====================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ecrire une classe "POJO" classique (des champs prives, un
 * constructeur qui les remplit, des getters, equals(), hashCode(),
 * toString()...) demande BEAUCOUP de code repetitif pour dire une
 * chose toute simple : "voici les 2 informations qui DEFINISSENT un
 * Point : x et y, et rien d'autre ne changera jamais apres sa
 * creation". "record Point(int x, int y) {}" dit EXACTEMENT ca, en
 * UNE ligne - et le compilateur GENERE tout seul, en coulisses :
 *   - des accesseurs x() et y() (PAS getX()/getY() : pas de "get").
 *   - un constructeur "canonique" qui prend x ET y, dans cet ordre.
 *   - equals()/hashCode() qui comparent x ET y (2 Points avec les
 *     memes valeurs sont EGAUX, meme si ce sont 2 objets differents
 *     en memoire).
 *   - toString() qui affiche "Point[x=.., y=..]".
 * Un record PEUT quand meme avoir ses propres methodes
 * supplementaires (comme n'importe quelle classe), et meme PLUSIEURS
 * constructeurs - a condition que TOUT constructeur autre que le
 * canonique finisse par APPELER le canonique via this(...).
 *
 *
 * ==================================================================
 * TODO 1 : Point.distanceFromOrigin()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * new Point(3, 4).distanceFromOrigin() doit valoir 5.0 (le fameux
 * triangle 3-4-5).
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.sqrt(x * x + y * y) - remarque : x et y sont
 *      directement utilisables ICI, sans "this.x" ni "x()" : a
 *      l'interieur du record lui-meme (pas depuis l'exterieur), les
 *      composants sont accessibles comme des champs normaux.
 *
 *
 * ==================================================================
 * TODO 2 : le constructeur surcharge Point(int x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut un raccourci : "un point SUR LA DIAGONALE" (x et y egaux),
 * en ne donnant qu'UN SEUL nombre. Mais un record ne permet JAMAIS
 * d'inventer sa propre logique de remplissage des champs en dehors du
 * constructeur canonique - ce constructeur-raccourci doit donc
 * OBLIGATOIREMENT deleguer au vrai constructeur (x, y) via this(...),
 * comme premiere (et ici, unique) instruction.
 *
 * -- Le plan --
 *
 *   1. Remplacer ENTIEREMENT le corps de ce constructeur (les 2
 *      lignes deja presentes, y compris le this(0, 0) provisoire) par
 *      une seule ligne : this(x, x) (le meme nombre pour les 2
 *      composants).
 *
 * -- Pourquoi un this(0, 0) "provisoire" est-il deja la ? --
 *
 * Contrainte propre aux records : dans un constructeur NON canonique
 * (celui-ci ne prend qu'un seul parametre, alors que le record en a
 * 2), la toute PREMIERE instruction DOIT etre un appel this(...) -
 * impossible d'y mettre directement un throw comme dans les autres
 * exercices. Le this(0, 0) ci-dessous n'est qu'un appel BIDON pour
 * satisfaire le compilateur en attendant ta solution ; le vrai TODO
 * est de le remplacer par le bon appel.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : new Point(3, 4).distanceFromOrigin() == 5.0.
 * new Point(3) equals(new Point(3, 3)) (le constructeur-raccourci
 * cree bien un point diagonal). new Point(1, 2).toString() ==
 * "Point[x=1, y=2]" (format automatique).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "Point(int x) { this(x, x); }" : la ligne this(...) doit
 *     TOUJOURS etre la toute PREMIERE instruction du constructeur
 *     surcharge.
 *   - record Point(int x, int y) {} n'a besoin d'AUCUN champ ecrit a
 *     la main : x et y sont deja les champs prives finaux ET les
 *     parametres du constructeur canonique, tout en un.
 */
public class Exercise08_RecordBasics {

    record Point(int x, int y) {
        double distanceFromOrigin() {
            throw new UnsupportedOperationException("TODO 1 : implementer distanceFromOrigin()");
        }

        Point(int x) {
            this(0, 0); // TODO 2 : appel BIDON, a remplacer entierement (voir le plan plus haut)
            throw new UnsupportedOperationException("TODO 2 : implementer le constructeur Point(int)");
        }
    }

    public static void main(String[] args) {
        ExerciseChecker.check("distanceFromOrigin() sur (3, 4) == 5.0",
                new Point(3, 4).distanceFromOrigin() == 5.0);

        ExerciseChecker.check("Point(3) (constructeur-raccourci) == Point(3, 3)",
                new Point(3).equals(new Point(3, 3)));

        ExerciseChecker.check("toString() genere automatiquement 'Point[x=1, y=2]'",
                new Point(1, 2).toString().equals("Point[x=1, y=2]"));

        ExerciseChecker.check("les accesseurs x()/y() (pas getX()/getY()) fonctionnent",
                new Point(7, 9).x() == 7 && new Point(7, 9).y() == 9);

        ExerciseChecker.summary();
    }
}
