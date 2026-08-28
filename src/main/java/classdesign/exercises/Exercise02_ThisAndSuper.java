package classdesign.exercises;

import classdesign.ExerciseChecker;

/**
 * EXERCICE 2 - this et super : demeler 3 "label" portant le MEME nom (niveau : difficile)
 * ================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine 3 personnes qui portent EXACTEMENT le meme prenom dans la
 * meme piece : le PARAMETRE de la methode (le "invite du jour"), LE
 * CHAMP de la classe actuelle (this.label - "l'habitant de cet
 * etage"), et LE CHAMP du parent (super.label - "l'habitant de
 * l'etage du dessous", jamais efface, juste CACHE par celui de
 * l'etage du dessus - contrairement aux methodes overridees, un champ
 * "cache" ne disparait JAMAIS, les 2 versions continuent d'exister en
 * memoire cote a cote). Sans prefixe, Java prend TOUJOURS la personne
 * la plus proche (le parametre, s'il existe, sinon this implicite) ;
 * this. et super. permettent de choisir EXPLICITEMENT laquelle des 2
 * autres on veut vraiment.
 *
 *
 * ==================================================================
 * TODO 1 : Child.describeAll(label)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * new Child().describeAll("Local-label") doit distinguer les 3 :
 * le parametre local re recu ("Local-label"), this.label
 * ("Child-label", le champ de Child), et super.label
 * ("Parent-label", le champ de Parent - CACHE mais toujours la).
 *
 * -- Le plan --
 *
 *   1. Renvoyer "param=" + label + " | this=" + this.label + " | super=" + super.label
 *      - "label" tout seul (sans prefixe) designe le PARAMETRE (le
 *      plus proche) ; this.label et super.label forcent chacun
 *      l'acces au champ precis voulu.
 *
 *
 * ==================================================================
 * TODO 2 : Child.describe() (methode SURCHARGEE, pas cachee)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Contrairement aux champs, les methodes d'instance se COMPORTENT
 * differemment quand on les redeclare : Child.describe() REMPLACE
 * (override) completement Parent.describe() pour tout appel
 * polymorphique - mais super.describe() permet quand meme d'appeler
 * EXPLICITEMENT l'ancienne version, pour la REUTILISER plutot que de
 * tout reecrire depuis zero.
 *
 * -- Le plan --
 *
 *   1. Renvoyer super.describe() + " + Child.describe".
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : new Child().describeAll("Local-label") ==
 * "param=Local-label | this=Child-label | super=Parent-label". new
 * Child().describe() == "Parent.describe + Child.describe".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "this.label" et "super.label" compilent TOUS LES DEUX ici
 *     precisement parce que Child DECLARE SON PROPRE champ label
 *     (le cache) - s'il ne le declarait pas, "this.label" et
 *     "super.label" designeraient exactement le MEME champ herite.
 */
public class Exercise02_ThisAndSuper {

    static class Parent {
        String label = "Parent-label";

        String describe() {
            return "Parent.describe";
        }
    }

    static class Child extends Parent {
        String label = "Child-label";

        String describeAll(String label) {
            throw new UnsupportedOperationException("TODO 1 : implementer describeAll()");
        }

        @Override
        String describe() {
            throw new UnsupportedOperationException("TODO 2 : implementer describe()");
        }
    }

    public static void main(String[] args) {
        Child child = new Child();

        ExerciseChecker.check("describeAll() distingue parametre / this.label / super.label",
                child.describeAll("Local-label")
                        .equals("param=Local-label | this=Child-label | super=Parent-label"));

        ExerciseChecker.check("describe() surcharge reutilise super.describe() explicitement",
                child.describe().equals("Parent.describe + Child.describe"));

        ExerciseChecker.summary();
    }
}
