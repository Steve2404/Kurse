package ch18_design.projects.p03_storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * FOURNI (ne pas modifier) : l'ancienne couche de stockage de l'application de notes.
 * Une seule grosse interface, LegacyStorage, que TOUT le monde doit implementer en entier,
 * meme l'archive, qui est en lecture seule : elle lance donc des exceptions. Lance main pour voir le resultat.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        LegacyStorage notes = new LegacyMemory();
        notes.write("courses", "lait, pain");
        notes.write("idees", "un jardin sur le toit");
        LegacyStorage archive = new LegacyArchive(Map.of("2023/bilan", "ok"));
        System.out.println("copie vers la memoire : " + LegacyBackup.copy(archive, new LegacyMemory()) + " cle(s)");
        try {
            LegacyBackup.copy(notes, archive);
        } catch (UnsupportedOperationException e) {
            System.out.println("copie vers l'archive : " + e);
        }
    }

    /** L'interface d'origine : lire, ecrire, effacer, lister. Tout ou rien. */
    public interface LegacyStorage {
        Optional<String> read(String key);

        void write(String key, String value);

        void delete(String key);

        List<String> keys();
    }

    public static final class LegacyMemory implements LegacyStorage {
        private final Map<String, String> map = new TreeMap<>();

        @Override
        public Optional<String> read(String key) {
            return Optional.ofNullable(map.get(key));
        }

        @Override
        public void write(String key, String value) {
            map.put(key, value);
        }

        @Override
        public void delete(String key) {
            map.remove(key);
        }

        @Override
        public List<String> keys() {
            return new ArrayList<>(map.keySet());
        }
    }

    /** Une archive en lecture seule... qui promet pourtant write et delete. */
    public static final class LegacyArchive implements LegacyStorage {
        private final Map<String, String> map;

        public LegacyArchive(Map<String, String> content) {
            this.map = new TreeMap<>(content);
        }

        @Override
        public Optional<String> read(String key) {
            return Optional.ofNullable(map.get(key));
        }

        @Override
        public void write(String key, String value) {
            throw new UnsupportedOperationException("archive en lecture seule");
        }

        @Override
        public void delete(String key) {
            throw new UnsupportedOperationException("archive en lecture seule");
        }

        @Override
        public List<String> keys() {
            return new ArrayList<>(map.keySet());
        }
    }

    public static final class LegacyBackup {
        private LegacyBackup() {
        }

        /** Copie toutes les cles de from dans to. Compile avec N'IMPORTE QUEL LegacyStorage en destination. */
        public static int copy(LegacyStorage from, LegacyStorage to) {
            for (String key : from.keys()) {
                to.write(key, from.read(key).orElseThrow());
            }
            return from.keys().size();
        }
    }
}
