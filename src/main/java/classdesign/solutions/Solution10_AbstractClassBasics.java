package classdesign.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise10_AbstractClassBasics.
 */
public class Solution10_AbstractClassBasics {

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
            return name + " fait le beau";
        }
    }
}
