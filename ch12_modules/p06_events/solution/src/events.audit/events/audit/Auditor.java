package events.audit;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * SOLUTION - lit les champs PRIVES d'un objet (exige que son paquet soit ouvert a events.audit).
 */
public final class Auditor {

    private Auditor() {
    }

    public static String inspect(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields()).sorted(Comparator.comparing(Field::getName)).map(f -> {
            try {
                f.setAccessible(true);
                return f.getName() + "=" + f.get(object);
            } catch (IllegalAccessException e) {
                return f.getName() + "=?";
            }
        }).collect(Collectors.joining(", ", object.getClass().getSimpleName() + "{", "}"));
    }
}
