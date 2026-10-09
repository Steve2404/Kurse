package ch18_design.projects.p03_storage.solution;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** Le stockage en memoire. Une TreeMap garde les cles triees : le contrat de keys() vient tout seul. */
public final class MemoryStorage implements WritableStorage {

    private final Map<String, String> map = new TreeMap<>();

    @Override
    public Optional<String> read(String key) {
        return Optional.ofNullable(map.get(Keys.check(key)));
    }

    @Override
    public List<String> keys() {
        return List.copyOf(map.keySet());
    }

    @Override
    public void write(String key, String value) {
        Keys.check(key);
        if (value == null) {
            throw new IllegalArgumentException("valeur absente pour " + key);
        }
        map.put(key, value);
    }

    @Override
    public boolean delete(String key) {
        return map.remove(Keys.check(key)) != null;
    }
}
