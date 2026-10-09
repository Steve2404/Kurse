package ch18_design.projects.p03_storage;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Keys.java", "[a-z0-9/._-]+", "[a-z0-9/._-]*"),
            new Mutant("MemoryStorage.java", "new TreeMap<>()", "new java.util.LinkedHashMap<>()"),
            new Mutant("MemoryStorage.java", "List.copyOf(map.keySet())", "new java.util.ArrayList<>(map.keySet())"),
            new Mutant("MemoryStorage.java", "if (value == null) {", "if (value == null && key.isEmpty()) {"),
            new Mutant("MemoryStorage.java", "return map.remove(Keys.check(key)) != null;", "map.remove(Keys.check(key));\n        return true;"),
            new Mutant("Archive.java", "Map.copyOf(content)", "java.util.Collections.unmodifiableMap(content)"),
            new Mutant("Archive.java", "Optional.ofNullable(content.get(Keys.check(key)))", "Optional.of(content.get(Keys.check(key)))"),
            new Mutant("PrefixedStorage.java", ".filter(key -> key.startsWith(prefix))", ".filter(key -> key.startsWith(prefix.substring(0, prefix.length() - 1)))"),
            new Mutant("PrefixedStorage.java", ".map(key -> key.substring(prefix.length()))", ".map(key -> key)"),
            new Mutant("PrefixedStorage.java", "return inner.delete(prefix + Keys.check(key));", "return inner.delete(Keys.check(key));"),
            new Mutant("PrefixedStorage.java", "if (!prefix.endsWith(\"/\")) {", "if (prefix.isEmpty()) {"),
            new Mutant("AuditedStorage.java", "return existed;", "return true;"),
            new Mutant("AuditedStorage.java", "inner.write(key, value);\n        log.add(\"write \" + key);", "log.add(\"write \" + key);\n        inner.write(key, value);"),
            new Mutant("Backup.java", "keys.forEach(key -> to.write(", "keys.stream().filter(key -> to.read(key).isEmpty()).forEach(key -> to.write("),
            new Mutant("StorageStats.java", ".mapToInt(key -> storage.read(key).orElseThrow().length())", ".mapToInt(String::length)"));

    static final List<String> API_CODE = List.of(
            "interface ReadableStorage", "interface WritableStorage extends ReadableStorage",
            "final class MemoryStorage implements WritableStorage", "final class Archive implements ReadableStorage",
            "final class PrefixedStorage implements WritableStorage", "final class AuditedStorage implements WritableStorage",
            "final class Keys", "final class Backup", "final class StorageStats", "Map.copyOf(", "List.copyOf(",
            "!UnsupportedOperationException", "!instanceof", "!Legacy",
            "max:method=10",
            "in:Archive.java!write(##l'archive ne promet pas d'ecrire (write)",
            "in:Backup.java=copy(ReadableStorage##la sauvegarde lit un ReadableStorage",
            "in:StorageStats.java!WritableStorage##les statistiques ne demandent que la lecture");

    static final List<String> API_TESTS = List.of(
            "abstract class ReadableContractTest", "abstract class WritableContractTest extends ReadableContractTest",
            "3xextends WritableContractTest", "2xextends ReadableContractTest", "protected abstract",
            "@Override", "assertThrows(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 60, MUTANTS, API_CODE, API_TESTS);
    }
}
