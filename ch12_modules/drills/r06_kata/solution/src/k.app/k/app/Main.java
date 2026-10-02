package k.app;

import k.core.Api;
import k.core.util.Text;

import java.lang.reflect.Field;

/** SOLUTION - le programme du drill 6. */
public class Main {
    public static void main(String[] args) throws ReflectiveOperationException {
        Class<?> point = Class.forName("k.core.model.Point");
        Object p = point.getDeclaredConstructor().newInstance();
        Field x = point.getDeclaredField("x");
        x.setAccessible(true);
        System.out.println(Api.version() + " | " + Text.reverse("module") + " | x=" + x.get(p) + " | k.core.model exporte "
                + point.getModule().isExported("k.core.model") + ", ouvert " + point.getModule().isOpen("k.core.model"));
    }
}
