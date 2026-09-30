package ch6_classdesign.drills.solutions;

import ch6_classdesign.drills.Zoo;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    abstract static class Creature {
        private final String name;
        private final int baseFood;

        Creature(String name, int baseFood) {
            // Valider avant d'affecter les champs final.
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("nom");
            }
            this.name = name;
            this.baseFood = baseFood;
        }

        String name() {
            return name;
        }

        int baseFood() {
            return baseFood;
        }

        abstract int extraFood();

        final int dailyFood() {
            // Methode template final : le calcul est commun, extraFood() est propre a chaque enfant.
            return baseFood() + extraFood();
        }

        @Override
        public String toString() {
            // getClass() donne la vraie classe (Mammal, Reptile).
            return getClass().getSimpleName() + " " + name + " (" + dailyFood() + " g)";
        }

        @Override
        public boolean equals(Object other) {
            // Meme classe exacte et meme nom.
            if (other == null || other.getClass() != getClass()) {
                return false;
            }
            return name.equals(((Creature) other).name);
        }

        @Override
        public int hashCode() {
            // Coherent avec equals.
            return name.hashCode();
        }
    }

    static class Mammal extends Creature {
        Mammal(String name, int baseFood) {
            super(name, baseFood);
        }

        @Override
        int extraFood() {
            // Division entiere : 10 % arrondi vers le bas.
            return baseFood() / 10;
        }
    }

    static class Reptile extends Creature {
        Reptile(String name, int baseFood) {
            super(name, baseFood);
        }

        @Override
        int extraFood() {
            return 0;
        }
    }

    public static List<Creature> fromZoo() {
        // Le type concret depend de la donnee ; la liste est du type abstrait.
        List<Creature> creatures = new ArrayList<>();
        for (int i = 0; i < Zoo.NAMES.length; i++) {
            boolean warm = Zoo.KINDS[i].equals("chien") || Zoo.KINDS[i].equals("oiseau");
            creatures.add(warm ? new Mammal(Zoo.NAMES[i], Zoo.FOOD[i]) : new Reptile(Zoo.NAMES[i], Zoo.FOOD[i]));
        }
        return creatures;
    }

    public static int totalFood(List<Creature> creatures) {
        // dailyFood() utilise l'extraFood() de chaque objet reel.
        int total = 0;
        for (Creature c : creatures) {
            total += c.dailyFood();
        }
        return total;
    }

    public static Creature hungriest(List<Creature> creatures) {
        // Garder le meilleur au fur et a mesure.
        Creature best = creatures.get(0);
        for (Creature c : creatures) {
            if (c.dailyFood() > best.dailyFood()) {
                best = c;
            }
        }
        return best;
    }

    public static List<String> reptileNames(List<Creature> creatures) {
        // instanceof filtre les Reptile.
        List<String> names = new ArrayList<>();
        for (Creature c : creatures) {
            if (c instanceof Reptile) {
                names.add(c.name());
            }
        }
        return names;
    }
}
