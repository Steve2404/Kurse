package ch6_classdesign.drills.exercises;

import ch6_classdesign.ExerciseChecker;
import ch6_classdesign.drills.Zoo;

import java.util.ArrayList;
import java.util.List;

/**
 * DRILL 05 - Kata melange : tout le chapitre 6 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_InheritanceAndConstructors. Ici, PAS de
 * crochet. Fais ce drill seulement quand les drills 01 a 04 passent.
 *
 * Un Keeper (soigneur) nourrit des Creature. Creature est abstraite ;
 * Mammal et Reptile la completent. Les donnees viennent de Zoo.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : Creature(name, food)         affecte les champs final ; nom vide -> IllegalArgumentException("nom").
 * TODO 2  : Creature.dailyFood()          final : baseFood() + extraFood() (extraFood est redefinie).
 * TODO 3  : Mammal.extraFood()            10 % de baseFood, arrondi a l'entier inferieur.
 * TODO 4  : Reptile.extraFood()           0.
 * TODO 5  : Creature.toString()           "Mammal Rex (550 g)" (nom simple de la vraie classe).
 * TODO 6  : fromZoo()                     les chiens et oiseaux sont des Mammal (oui, simplifie), les autres des Reptile,
 *                                          avec Zoo.FOOD.
 * TODO 7  : totalFood(creatures)          somme des dailyFood -> 793 (550 + 33 + 10 + 200).
 * TODO 8  : hungriest(creatures)          la creature au plus grand dailyFood.
 * TODO 9  : Creature.equals / hashCode    meme classe et meme nom.
 * TODO 10 : reptileNames(creatures)       les noms des Reptile, dans l'ordre -> [Nemo, Kaa].
 */
public class Drill05_MixedKata {

    abstract static class Creature {
        private final String name;
        private final int baseFood;

        Creature(String name, int baseFood) {
            throw new UnsupportedOperationException("TODO 1 : implementer Creature(name, food)");
        }

        String name() {
            return name;
        }

        int baseFood() {
            return baseFood;
        }

        abstract int extraFood();

        final int dailyFood() {
            throw new UnsupportedOperationException("TODO 2 : implementer dailyFood()");
        }

        @Override
        public String toString() {
            throw new UnsupportedOperationException("TODO 5 : implementer toString()");
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("TODO 9 : implementer equals()");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("TODO 9 : implementer hashCode()");
        }
    }

    static class Mammal extends Creature {
        Mammal(String name, int baseFood) {
            super(name, baseFood);
        }

        @Override
        int extraFood() {
            throw new UnsupportedOperationException("TODO 3 : implementer Mammal.extraFood()");
        }
    }

    static class Reptile extends Creature {
        Reptile(String name, int baseFood) {
            super(name, baseFood);
        }

        @Override
        int extraFood() {
            throw new UnsupportedOperationException("TODO 4 : implementer Reptile.extraFood()");
        }
    }

    public static List<Creature> fromZoo() {
        throw new UnsupportedOperationException("TODO 6 : implementer fromZoo()");
    }

    public static int totalFood(List<Creature> creatures) {
        throw new UnsupportedOperationException("TODO 7 : implementer totalFood()");
    }

    public static Creature hungriest(List<Creature> creatures) {
        throw new UnsupportedOperationException("TODO 8 : implementer hungriest()");
    }

    public static List<String> reptileNames(List<Creature> creatures) {
        throw new UnsupportedOperationException("TODO 10 : implementer reptileNames()");
    }

    public static void main(String[] args) {
        String error = null;
        try {
            new Reptile(" ", 1);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        Creature rex = new Mammal("Rex", 500);
        ExerciseChecker.check("1  constructeur : champs et refus d'un nom vide", rex.name().equals("Rex") && "nom".equals(error));
        ExerciseChecker.check("2+3 dailyFood Mammal : 500 + 50 = 550", rex.dailyFood() == 550);
        ExerciseChecker.check("4  dailyFood Reptile : 200", new Reptile("Kaa", 200).dailyFood() == 200);
        ExerciseChecker.check("5  toString : Mammal Rex (550 g)", rex.toString().equals("Mammal Rex (550 g)"));
        List<Creature> zoo = fromZoo();
        ExerciseChecker.check("6  fromZoo : 4 creatures des bons types",
                zoo.size() == 4 && zoo.get(0) instanceof Mammal && zoo.get(1) instanceof Mammal && zoo.get(2) instanceof Reptile);
        ExerciseChecker.check("7  totalFood == 793", totalFood(zoo) == 793);
        ExerciseChecker.check("8  hungriest == Rex", hungriest(zoo).name().equals("Rex"));
        ExerciseChecker.check("9  equals/hashCode : meme classe et meme nom",
                rex.equals(new Mammal("Rex", 1)) && !rex.equals(new Reptile("Rex", 500)) && rex.hashCode() == new Mammal("Rex", 9).hashCode());
        ExerciseChecker.check("10 reptileNames == [Nemo, Kaa]", reptileNames(zoo).equals(List.of("Nemo", "Kaa")));

        ExerciseChecker.summary();
    }
}
