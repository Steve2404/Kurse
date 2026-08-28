package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise13_PolymorphismAndCasting.
 */
public class Solution13_PolymorphismAndCasting {

    static class Animal {
        String sound() {
            return "...";
        }
    }

    static class Dog extends Animal {
        @Override
        String sound() {
            return "Wouf";
        }

        String fetch() {
            return "Rapporte la balle";
        }
    }

    static class Cat extends Animal {
        @Override
        String sound() {
            return "Miaou";
        }
    }

    public static String castAndFetch(Animal a) {
        if (a instanceof Dog dog) {
            return dog.fetch();
        }
        return "Ce n'est pas un chien, impossible de rapporter la balle";
    }

    public static String forceCastToDog(Animal a) {
        Dog dog = (Dog) a;
        return dog.fetch();
    }
}
