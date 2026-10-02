package ch7_beyondclasses.drills.r08_casting.solution;

/**
 * SOLUTION du drill de rappel 8 - polymorphisme et casts.
 */
public class Recall08 {

    // Un cast sur, apres instanceof : jamais de ClassCastException.
    static String fetchIfDog(Animal a) {
        if (a instanceof Dog) {
            Dog d = (Dog) a;
            return d.fetch();
        }
        return "impossible";
    }

    public static void main(String[] args) {
        Animal a = new Dog();
        System.out.println("D01 : " + a.sound() + " " + ((Dog) a).fetch() + " " + fetchIfDog(new Cat()));
        System.out.println("D02 : " + (a instanceof Pet) + " " + (a instanceof Cat) + " " + (new Cat() instanceof Pet) + " " + (a instanceof Object));
        System.out.println("D03 : " + a.name + " " + ((Dog) a).name + " " + a.label());
        Object o = "texte";
        String len = o instanceof String s && s.length() > 3 ? "long " + s.length() : "court";
        System.out.println("D04 : " + len + " " + ((String) o).toUpperCase());
        Pet p = new Dog();
        Animal back = (Animal) p;                 // interface -> classe : cast explicite
        System.out.println("D05 : " + p.owner() + " " + back.sound() + " " + ((Object) p == back));
        Animal[] zoo = {new Dog(), new Cat(), new Animal()};
        StringBuilder sb = new StringBuilder();
        for (Animal x : zoo) {
            sb.append(x.sound()).append(x instanceof Pet ? "(pet) " : " ");
        }
        System.out.println("D06 : " + sb.toString().strip());
    }
}

interface Pet {
    default String owner() {
        return "Leo";
    }
}

class Animal {
    String name = "animal";

    String sound() {
        return "...";
    }

    String label() {
        return name;   // le champ de Animal : les champs ne sont pas polymorphes
    }
}

class Dog extends Animal implements Pet {
    String name = "chien";

    @Override
    String sound() {
        return "ouaf";
    }

    String fetch() {
        return "rapporte";
    }
}

class Cat extends Animal {
    @Override
    String sound() {
        return "miaou";
    }
}

final class Rock {
}
