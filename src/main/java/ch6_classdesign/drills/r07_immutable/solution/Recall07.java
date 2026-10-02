package ch6_classdesign.drills.r07_immutable.solution;

/**
 * SOLUTION du drill de rappel 7 - les objets immuables.
 */
public class Recall07 {

    public static void main(String[] args) {
        Temperature t = Temperature.ofCelsius(20);
        Temperature warmer = t.plus(5);
        System.out.println("D01 : " + t + " " + warmer + " " + t.fahrenheit());
        int[] raw = {3, 1, 4};
        Series s = Series.of(raw);
        raw[0] = 99;
        int[] leaked = s.values();
        leaked[1] = 77;
        System.out.println("D02 : " + s + " " + s.get(0) + " " + s.get(1));
        System.out.println("D03 : " + t.equals(Temperature.ofCelsius(20)) + " " + (t == Temperature.ofCelsius(20)) + " "
                + (t.hashCode() == Temperature.ofCelsius(20).hashCode()));
        Series changed = s.with(2, 9);
        System.out.println("D04 : " + s + " " + changed + " " + changed.average());
        final StringBuilder notImmutable = new StringBuilder("final");
        notImmutable.append(" mais modifiable");
        System.out.println("D05 : " + notImmutable + " " + "abc".toUpperCase().equals("ABC"));
    }
}

// 1. classe final ; 2. champs private final ; 3. pas de setter ; 4. copies defensives ; 5. les "modifications" rendent un nouvel objet.
final class Temperature {
    private final double celsius;

    private Temperature(double celsius) {
        this.celsius = celsius;
    }

    static Temperature ofCelsius(double c) {
        return new Temperature(c);
    }

    Temperature plus(double delta) {
        return new Temperature(celsius + delta);
    }

    double fahrenheit() {
        return celsius * 9 / 5 + 32;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Temperature other && other.celsius == celsius;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(celsius);
    }

    @Override
    public String toString() {
        return celsius + "C";
    }
}

final class Series {
    private final int[] values;

    private Series(int[] values) {
        this.values = values;
    }

    static Series of(int[] source) {
        return new Series(source.clone());      // copie a l'entree
    }

    int get(int i) {
        return values[i];
    }

    int[] values() {
        return values.clone();                  // copie a la sortie
    }

    Series with(int index, int value) {
        int[] copy = values.clone();
        copy[index] = value;
        return new Series(copy);
    }

    double average() {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return (double) sum / values.length;
    }

    @Override
    public String toString() {
        return java.util.Arrays.toString(values);
    }
}
