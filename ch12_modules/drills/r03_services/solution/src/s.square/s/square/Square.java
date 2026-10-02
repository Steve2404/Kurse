package s.square;

import s.api.Shape;

/** SOLUTION - classe publique, constructeur public sans argument. */
public class Square implements Shape {
    @Override
    public String name() {
        return "carre";
    }

    @Override
    public int area(int size) {
        return size * size;
    }
}
