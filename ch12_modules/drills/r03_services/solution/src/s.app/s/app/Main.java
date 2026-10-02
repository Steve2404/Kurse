package s.app;

import s.api.Shape;
import s.locator.Shapes;

import java.util.List;

/** SOLUTION - le consommateur du drill 3. */
public class Main {
    public static void main(String[] args) {
        List<Shape> shapes = Shapes.all();
        StringBuilder sb = new StringBuilder();
        shapes.forEach(s -> sb.append(' ').append(s.name()).append('=').append(s.area(6)));
        System.out.println("formes (cote 6) :" + (shapes.isEmpty() ? " aucune" : sb) + " ; types " + Shapes.types() + " ; findFirst present "
                + Shapes.any().isPresent());
    }
}
