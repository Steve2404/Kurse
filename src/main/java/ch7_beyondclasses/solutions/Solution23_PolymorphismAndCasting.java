package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 23. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise23_PolymorphismAndCasting.
 */
public class Solution23_PolymorphismAndCasting {

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
        // instanceof avant le cast : jamais de ClassCastException.
        if (a instanceof Dog dog) {
            return dog.fetch();
        }
        return "Ce n'est pas un chien, impossible de rapporter la balle";
    }

    public static String forceCastToDog(Animal a) {
        // Le cast compile (Dog est un Animal) mais explose a l'execution si l'objet n'est pas un Dog.
        Dog dog = (Dog) a;
        return dog.fetch();
    }
}
