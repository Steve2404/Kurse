package s.triangle;

import s.api.Shape;

/** SOLUTION - public static provider() : la classe n'implemente pas Shape. */
public final class TriangleFactory {
    private TriangleFactory() {
    }

    public static Shape provider() {
        return new Shape() {
            @Override
            public String name() {
                return "triangle";
            }

            @Override
            public int area(int size) {
                return size * size / 2;
            }
        };
    }
}
