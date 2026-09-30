package ch6_classdesign.drills.solutions;

import ch6_classdesign.drills.Zoo;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.drills.exercises.Drill01_InheritanceAndConstructors.
 */
public class SolutionDrill01_InheritanceAndConstructors {

    static class Animal {
        protected final String name;
        protected final int legs;

        Animal(String name, int legs) {
            // Chaque champ final recoit sa valeur une seule fois.
            this.name = name;
            this.legs = legs;
        }

        Animal(String name) {
            // this(...) en 1re ligne : on reutilise l'autre constructeur.
            this(name, 4);
        }

        String describe() {
            return name + " (" + legs + " pattes)";
        }
    }

    static class Bird extends Animal {
        private final boolean canFly;

        Bird(String name, boolean canFly) {
            // super(...) d'abord (le parent est construit), puis les champs de l'enfant.
            super(name, 2);
            this.canFly = canFly;
        }

        @Override
        String describe() {
            // super.describe() reutilise la phrase du parent.
            return super.describe() + (canFly ? ", vole" : ", ne vole pas");
        }
    }

    static class Fish extends Animal {
        Fish(String name) {
            super(name, 0);
        }

        @Override
        String describe() {
            return "poisson " + super.describe();
        }
    }

    public static Animal[] fromZoo() {
        // Un tableau du type parent peut contenir tous les enfants.
        return new Animal[] {
                new Animal(Zoo.NAMES[0]),
                new Bird(Zoo.NAMES[1], true),
                new Fish(Zoo.NAMES[2]),
                new Animal(Zoo.NAMES[3], 0)};
    }

    public static int totalLegs(Animal[] animals) {
        // legs est protected : visible dans la meme classe englobante et le meme paquet.
        int total = 0;
        for (Animal a : animals) {
            total += a.legs;
        }
        return total;
    }

    public static String describeAll(Animal[] animals) {
        // describe() est choisi sur l'objet reel de chaque case.
        StringBuilder sb = new StringBuilder();
        for (Animal a : animals) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append(a.describe());
        }
        return sb.toString();
    }

    public static boolean sameClass(Animal a, Animal b) {
        // getClass() donne la classe EXACTE (instanceof accepterait aussi les sous-classes).
        return a.getClass() == b.getClass();
    }
}
