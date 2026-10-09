package ch18_design.projects.p03_storage.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enveloppe un stockage et note chaque modification. Elle ajoute un comportement SANS changer
 * le contrat : delete rend exactement ce que rend le stockage enveloppe.
 */
public final class AuditedStorage implements WritableStorage {

    private final WritableStorage inner;
    private final List<String> log = new ArrayList<>();

    public AuditedStorage(WritableStorage inner) {
        this.inner = inner;
    }

    @Override
    public Optional<String> read(String key) {
        return inner.read(key);
    }

    @Override
    public List<String> keys() {
        return inner.keys();
    }

    // On note APRES l'appel : une ecriture refusee (cle invalide) ne laisse pas de trace.
    @Override
    public void write(String key, String value) {
        inner.write(key, value);
        log.add("write " + key);
    }

    @Override
    public boolean delete(String key) {
        boolean existed = inner.delete(key);
        log.add("delete " + key + (existed ? "" : " (absente)"));
        return existed;
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
