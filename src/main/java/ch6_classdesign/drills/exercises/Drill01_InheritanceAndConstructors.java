package ch6_classdesign.drills.exercises;

import ch6_classdesign.ExerciseChecker;
import ch6_classdesign.drills.Zoo;

/**
 * DRILL 01 - Heritage et constructeurs : this(...), super(...), super.methode() (zoo)
 * ==================================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch6_classdesign.drills.Zoo.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Animal(name, legs)         [affecter des champs final] this.name = name ; this.legs = legs.
 * TODO 2  : Animal(name)               [this(...) en 1re ligne] delegue avec 4 pattes.
 * TODO 3  : Animal.describe()          [methode du parent] "Rex (4 pattes)".
 * TODO 4  : Bird(name, canFly)         [apres super(...)] super(name, 2) est DEJA ecrit : affecte canFly.
 * TODO 5  : Bird.describe()            [super.methode()] super.describe() + ", vole" ou ", ne vole pas".
 * TODO 6  : Fish.describe()            [redefinition] "poisson " + super.describe().
 * TODO 7  : fromZoo()                  [construire les bons types] Rex : Animal(name) ; Tweety : Bird volant ;
 *                                       Nemo : Fish ; Kaa : Animal(name, 0).
 * TODO 8  : totalLegs(animals)         [champ herite protected] somme -> 6.
 * TODO 9  : describeAll(animals)       [appel polymorphe] les describe() separes par " | ".
 * TODO 10 : sameClass(a, b)            [getClass()] meme classe exacte.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   class B extends A : un seul parent ; tout herite d'Object (equals, hashCode, toString, getClass)
 *   Constructeur : 1re ligne = this(...) OU super(...) (jamais les deux) ; sinon super() implicite
 *   Aucun constructeur ecrit -> constructeur par defaut sans argument (avec super())
 *   this.x = le champ ; super.x = le champ du parent ; super.m() = la methode du parent
 *   Ordre : static parent, static enfant (une fois), puis parent (champs, blocs, constructeur), puis enfant
 * ---------------------------------------------------------------------
 */
public class Drill01_InheritanceAndConstructors {

    static class Animal {
        protected final String name;
        protected final int legs;

        Animal(String name, int legs) {
            throw new UnsupportedOperationException("TODO 1 : implementer Animal(name, legs)");
        }

        Animal(String name) {
            throw new UnsupportedOperationException("TODO 2 : implementer Animal(name) avec this(...)");
        }

        String describe() {
            throw new UnsupportedOperationException("TODO 3 : implementer Animal.describe()");
        }
    }

    static class Bird extends Animal {
        private final boolean canFly;

        Bird(String name, boolean canFly) {
            super(name, 2);
            throw new UnsupportedOperationException("TODO 4 : affecter canFly");
        }

        @Override
        String describe() {
            throw new UnsupportedOperationException("TODO 5 : implementer Bird.describe()");
        }
    }

    static class Fish extends Animal {
        Fish(String name) {
            super(name, 0);
        }

        @Override
        String describe() {
            throw new UnsupportedOperationException("TODO 6 : implementer Fish.describe()");
        }
    }

    public static Animal[] fromZoo() {
        throw new UnsupportedOperationException("TODO 7 : implementer fromZoo()");
    }

    public static int totalLegs(Animal[] animals) {
        throw new UnsupportedOperationException("TODO 8 : implementer totalLegs()");
    }

    public static String describeAll(Animal[] animals) {
        throw new UnsupportedOperationException("TODO 9 : implementer describeAll()");
    }

    public static boolean sameClass(Animal a, Animal b) {
        throw new UnsupportedOperationException("TODO 10 : implementer sameClass()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  Animal(\"Rex\", 4)", new Animal("Rex", 4).legs == 4);
        ExerciseChecker.check("2  Animal(\"Rex\") -> 4 pattes", new Animal("Rex").legs == 4);
        ExerciseChecker.check("3  describe : Rex (4 pattes)", new Animal("Rex").describe().equals("Rex (4 pattes)"));
        ExerciseChecker.check("4  Bird : 2 pattes (super) et canFly", new Bird("Tweety", true).legs == 2);
        ExerciseChecker.check("5  Bird.describe", new Bird("Tweety", true).describe().equals("Tweety (2 pattes), vole")
                && new Bird("Pingu", false).describe().equals("Pingu (2 pattes), ne vole pas"));
        ExerciseChecker.check("6  Fish.describe", new Fish("Nemo").describe().equals("poisson Nemo (0 pattes)"));
        Animal[] zoo = fromZoo();
        ExerciseChecker.check("7  fromZoo : 4 animaux des bons types",
                zoo.length == 4 && zoo[0].getClass() == Animal.class && zoo[1] instanceof Bird && zoo[2] instanceof Fish && zoo[3].legs == 0);
        ExerciseChecker.check("8  totalLegs == 6", totalLegs(zoo) == 6);
        ExerciseChecker.check("9  describeAll",
                describeAll(zoo).equals("Rex (4 pattes) | Tweety (2 pattes), vole | poisson Nemo (0 pattes) | Kaa (0 pattes)"));
        ExerciseChecker.check("10 sameClass : Animal/Animal oui, Animal/Bird non",
                sameClass(zoo[0], zoo[3]) && !sameClass(zoo[0], zoo[1]));

        ExerciseChecker.summary();
    }
}
