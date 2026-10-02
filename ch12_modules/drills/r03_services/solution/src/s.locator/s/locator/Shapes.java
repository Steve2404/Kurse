package s.locator;

import s.api.Shape;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/** SOLUTION - le localisateur : ServiceLoader.load fait la recherche. */
public final class Shapes {
    private Shapes() {
    }

    public static List<Shape> all() {
        return ServiceLoader.load(Shape.class).stream().map(ServiceLoader.Provider::get).sorted(Comparator.comparing(Shape::name)).toList();
    }

    public static List<String> types() {
        return ServiceLoader.load(Shape.class).stream().map(p -> p.type().getSimpleName()).sorted().toList();
    }

    public static Optional<Shape> any() {
        return ServiceLoader.load(Shape.class).findFirst();
    }
}
