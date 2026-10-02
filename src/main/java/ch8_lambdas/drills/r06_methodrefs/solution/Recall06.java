package ch8_lambdas.drills.r06_methodrefs.solution;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 6 - les references de methode.
 */
public class Recall06 extends Base {

    private final String suffix = "!";

    String exclaim(String s) {
        return s + suffix;
    }

    @Override
    String describe(String s) {
        return "enfant(" + s + ")";
    }

    String demo() {
        Function<String, String> mine = this::exclaim;           // instance : this
        Function<String, String> parent = super::describe;       // la version du PARENT
        Function<String, String> child = this::describe;
        return mine.apply("a") + " " + parent.apply("b") + " " + child.apply("c");
    }

    public static void main(String[] args) {
        Function<String, Integer> parse = Integer::parseInt;                 // 1. static
        String prefix = "pre-";
        UnaryOperator<String> addPrefix = prefix::concat;                    // 2. instance d'un objet PRECIS
        UnaryOperator<String> upper = String::toUpperCase;                   // 3. instance sur le PARAMETRE
        BiFunction<String, String, Boolean> starts = String::startsWith;     //    le 1er parametre devient l'objet
        Supplier<StringBuilder> make = StringBuilder::new;                   // 4. constructeur
        Function<String, StringBuilder> makeWith = StringBuilder::new;       //    autre surcharge, choisie par le type cible
        IntFunction<int[]> array = int[]::new;                               //    constructeur de tableau
        System.out.println("D01 : " + (parse.apply("40") + 2) + " " + addPrefix.apply("fixe") + " " + upper.apply("abc") + " " + starts.apply("java", "ja"));
        System.out.println("D02 : " + make.get().append("vide").length() + " " + makeWith.apply("init") + " " + array.apply(3).length);
        // La meme reference Math::max s'adapte a deux interfaces : la surcharge depend du type cible.
        IntBinaryOperator intMax = Math::max;
        DoubleBinaryOperator doubleMax = Math::max;
        System.out.println("D03 : " + intMax.applyAsInt(3, 8) + " " + doubleMax.applyAsDouble(3, 8));
        Predicate<String> empty = String::isEmpty;
        Predicate<String> blank = s -> s.isBlank();                          // equivalent lambda de String::isBlank
        System.out.println("D04 : " + empty.test(" ") + " " + blank.test(" ") + " " + empty.negate().test("x"));
        System.out.println("D05 : " + new Recall06().demo());
        Function<Integer, String> toText = String::valueOf;
        BiFunction<String, Integer, Character> charAt = String::charAt;
        System.out.println("D06 : " + toText.apply(7) + toText.apply(8) + " " + charAt.apply("lambda", 2));
    }
}

class Base {
    String describe(String s) {
        return "parent(" + s + ")";
    }
}
