package ch6_classdesign.drills.r01_inherit.solution;

/**
 * SOLUTION du drill de rappel 1 - l'heritage. Plusieurs classes non publiques dans un meme fichier : permis.
 */
public class Recall01 {

    public static void main(String[] args) {
        Puppy p = new Puppy();
        System.out.println("D01 : " + p.name + " " + p.legs + " " + p.sound() + " " + p.describe());
        Object o = p;
        Animal a = p;
        System.out.println("D02 : " + o + " " + a.sound() + " " + (p instanceof Dog) + " " + (o instanceof Animal) + " " + (a instanceof Puppy));
        System.out.println("D03 : " + Puppy.class.getSuperclass().getSimpleName() + " " + Dog.class.getSuperclass().getSimpleName() + " "
                + Animal.class.getSuperclass().getSimpleName() + " " + Object.class.getSuperclass());
        System.out.println("D04 : " + p.id() + " " + new Dog().id() + " " + Animal.count);
        Rock r1 = new Rock();
        Rock r2 = new Rock();
        System.out.println("D05 : " + r1.equals(r2) + " " + r1.equals(r1) + " " + r1.toString().contains(".Rock@"));
    }
}

class Animal {
    static int count;
    public String name = "animal";
    protected int legs = 4;
    private final int id;

    Animal() {
        id = ++count;
    }

    String sound() {
        return "...";
    }

    // final : les sous-classes en heritent mais ne peuvent pas la redefinir.
    final int id() {
        return id;
    }
}

class Dog extends Animal {
    Dog() {
        name = "chien";   // champ herite : la sous-classe le voit et le modifie
    }

    @Override
    String sound() {
        return "ouaf";
    }

    String describe() {
        return "chien";
    }
}

class Puppy extends Dog {
    Puppy() {
        name = "Rex";
    }

    @Override
    String sound() {
        return "kai";
    }

    @Override
    String describe() {
        return "chiot<" + super.describe() + ">";
    }

    @Override
    public String toString() {
        return "Puppy " + name;
    }
}

// final : aucune classe ne peut en heriter. Rock herite implicitement d'Object.
final class Rock {
}
