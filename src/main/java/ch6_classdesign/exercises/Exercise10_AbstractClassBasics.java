package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

/**
 * EXERCICE 10 - Classes abstraites : un plan a moitie rempli, que la 1ere classe concrete DOIT terminer (niveau : moyen/difficile)
 * =======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * classdesign.exercises.Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe abstract, c'est un FORMULAIRE A MOITIE REMPLI : certaines
 * cases sont deja ecrites (les methodes CONCRETES, comme describe()
 * plus bas), d'autres sont laissees VOLONTAIREMENT vides, juste avec
 * un intitule (les methodes abstract, SANS AUCUN corps - meme pas
 * des accolades vides). Impossible d'utiliser un formulaire a moitie
 * rempli tel quel ("new Shape()" ne compile JAMAIS) : il faut d'abord
 * une classe CONCRETE (Circle) qui remplit TOUTES les cases vides
 * restantes. Autre regle utile : une classe abstract PEUT parfaitement
 * etendre une classe NORMALE (non-abstract) - et inversement, une
 * classe normale peut etendre une classe abstract (c'est meme la
 * seule facon de "terminer" le formulaire).
 *
 *
 * ==================================================================
 * TODO 1 : Circle.area()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * new Circle(2).area() doit valoir PI * 2 * 2 (environ 12.57).
 *
 * -- Le plan --
 *
 *   1. Renvoyer Math.PI * radius * radius.
 *
 *
 * ==================================================================
 * TODO 2 : Dog.trick()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pet (abstract) etend Animal (NORMALE, pas abstract) : rien
 * n'empeche une classe abstraite d'avoir un parent parfaitement
 * concret. Dog est la 1ere classe VRAIMENT concrete de toute la
 * chaine Animal -> Pet -> Dog : elle DOIT donc completer trick(), la
 * seule case encore vide, en plus d'avoir DEJA herite de name
 * (depuis Animal, 2 generations plus haut).
 *
 * -- Le plan --
 *
 *   1. Renvoyer name + " fait le beau".
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : Shape s = new Circle(2); s.describe()
 * (methode CONCRETE, herite telle quelle) contient "12.566" (elle
 * appelle area(), qui, LUI, est different pour chaque forme
 * concrete). new Dog().trick() == "Animal fait le beau".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "abstract double area();" (point-virgule direct, JAMAIS
 *     d'accolades, meme vides) - une methode abstract N'A PAS DE
 *     CORPS DU TOUT, contrairement a une methode normale.
 */
public class Exercise10_AbstractClassBasics {

    abstract static class Shape {
        abstract double area();

        String describe() {
            return "Shape avec une aire de " + area();
        }
    }

    static class Circle extends Shape {
        double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            throw new UnsupportedOperationException("TODO 1 : implementer area()");
        }
    }

    static class Animal {
        String name = "Animal";
    }

    abstract static class Pet extends Animal {
        abstract String trick();
    }

    static class Dog extends Pet {
        @Override
        String trick() {
            throw new UnsupportedOperationException("TODO 2 : implementer trick()");
        }
    }

    public static void main(String[] args) {
        Shape s = new Circle(2);
        ExerciseChecker.check("describe() (concrete, heritee) appelle area() (abstract, completee par Circle)",
                s.describe().contains("12.566"));

        ExerciseChecker.check("Dog (1ere classe concrete de la chaine) complete trick() ET herite de name",
                new Dog().trick().equals("Animal fait le beau"));

        ExerciseChecker.summary();
    }
}
