package ch18_design.projects.p03_storage.solution;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Une archive : figee a sa creation. Elle n'implemente que ReadableStorage, donc le compilateur
 * interdit de la passer la ou l'on veut ecrire : l'erreur du legacy ne peut plus arriver.
 */
public final class Archive implements ReadableStorage {

    private final Map<String, String> content;

    public Archive(Map<String, String> content) {
        content.keySet().forEach(Keys::check);
        // Une COPIE, pas une vue : si l'appelant modifie sa Map ensuite, l'archive ne bouge pas.
        this.content = Map.copyOf(content);
    }

    @Override
    public Optional<String> read(String key) {
        return Optional.ofNullable(content.get(Keys.check(key)));
    }

    // Map.copyOf ne garde aucun ordre : on trie a chaque appel (le contrat le demande).
    @Override
    public List<String> keys() {
        return content.keySet().stream().sorted().toList();
    }
}
