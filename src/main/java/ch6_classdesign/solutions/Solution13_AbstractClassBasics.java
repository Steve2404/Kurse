package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise13_AbstractClassBasics.
 */
public class Solution13_AbstractClassBasics {

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
            // La 1re classe concrete remplit le trou abstract ; describe() du parent l'appellera.
            return Math.PI * radius * radius;
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
            // Dog est la 1re classe concrete sous Pet : elle doit ecrire trick().
            return name + " fait le beau";
        }
    }
}
