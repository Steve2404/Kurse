package ch18_design.projects.p03_storage.solution;

import java.util.List;
import java.util.Optional;

/**
 * Une VUE d'un autre stockage, limitee aux cles qui commencent par un prefixe ("users/").
 * Pour l'utilisateur, elle se comporte comme un stockage complet (meme contrat) ; elle ne voit
 * et ne touche JAMAIS les cles d'a cote, meme "users2/...".
 */
public final class PrefixedStorage implements WritableStorage {

    private final WritableStorage inner;
    private final String prefix;

    public PrefixedStorage(WritableStorage inner, String prefix) {
        // Le "/" final est indispensable : sans lui, "users" verrait aussi "users2/x".
        if (!prefix.endsWith("/")) {
            throw new IllegalArgumentException("prefixe invalide : " + prefix);
        }
        this.inner = inner;
        this.prefix = Keys.check(prefix);
    }

    @Override
    public Optional<String> read(String key) {
        return inner.read(prefix + Keys.check(key));
    }

    @Override
    public List<String> keys() {
        return inner.keys().stream()
                .filter(key -> key.startsWith(prefix))
                .map(key -> key.substring(prefix.length()))
                .toList();
    }

    @Override
    public void write(String key, String value) {
        inner.write(prefix + Keys.check(key), value);
    }

    @Override
    public boolean delete(String key) {
        return inner.delete(prefix + Keys.check(key));
    }
}
