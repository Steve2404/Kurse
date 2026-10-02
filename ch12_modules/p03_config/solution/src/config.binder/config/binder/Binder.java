package config.binder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SOLUTION - remplit les champs PRIVES d'un objet a partir de cles texte, et le decrit recursivement.
 * setAccessible(true) sur un membre prive d'un autre module exige que son paquet soit OUVERT a config.binder.
 */
public final class Binder {

    private Binder() {
    }

    // Convertit le texte vers le type du champ.
    static Object convert(String text, Class<?> type) {
        if (type == int.class) {
            return Integer.parseInt(text);
        }
        if (type == long.class) {
            return Long.parseLong(text);
        }
        if (type == boolean.class) {
            return Boolean.parseBoolean(text);
        }
        if (type == List.class) {
            return List.of(text.split(","));
        }
        return text;
    }

    public static <T> T bind(Class<T> type, Map<String, String> values) throws ReflectiveOperationException {
        Constructor<T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        T instance = constructor.newInstance();
        for (Map.Entry<String, String> e : values.entrySet()) {
            Field field = type.getDeclaredField(e.getKey());
            field.setAccessible(true);                       // InaccessibleObjectException si le paquet n'est pas ouvert
            field.set(instance, convert(e.getValue(), field.getType()));
        }
        return instance;
    }

    // {champ=valeur, ...} trie par nom ; les objets de nos modules (ni String, ni primitif enveloppe, ni List) sont decrits a leur tour.
    public static String dump(Object object) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder("{");
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields()).sorted(Comparator.comparing(Field::getName)).collect(Collectors.toList());
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            f.setAccessible(true);
            Object value = f.get(object);
            boolean nested = value != null && !f.getType().isPrimitive() && f.getType().getModule().isNamed()
                    && !f.getType().getModule().getName().startsWith("java.");
            sb.append(i == 0 ? "" : ", ").append(f.getName()).append('=').append(nested ? dump(value) : value);
        }
        return sb.append('}').toString();
    }
}
