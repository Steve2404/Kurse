package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * EXERCICE 3 - Optional en situation reelle : un lecteur de configuration a plusieurs couches (niveau : difficile/capstone Optional)
 * ===============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Le contexte --
 *
 * Une application lit ses reglages dans 3 "couches", par ordre de
 * priorite : les variables d'environnement (env), puis un fichier de
 * config (file), puis des valeurs par defaut (defaults). Pour une cle,
 * c'est la PREMIERE couche qui a une valeur NON VIDE qui gagne.
 *
 *   env      : port=" 8080 ", debug="  "
 *   file     : port="9090", host="example.org", timeout="abc", retries="42"
 *   defaults : host="localhost", timeout="30", retries="3", level="INFO"
 *
 * Tu vas utiliser, SANS JAMAIS ecrire "== null" ni appeler get() sur
 * un Optional : ofNullable, map, filter, flatMap, Optional::stream,
 * findFirst, OptionalInt, et la combinaison de 2 Optional.
 *
 *
 * ==================================================================
 * TODO 1 : lookup(source, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu ouvres UN tiroir (une couche) et tu cherches l'etiquette key.
 * Trois cas : pas d'etiquette (null), une etiquette avec juste des
 * espaces (inutilisable), ou une vraie valeur (qu'on nettoie de ses
 * espaces autour). Les 2 premiers cas donnent une boite vide.
 *
 * -- Essayons a la main --
 *
 *   lookup(env, "port")  -> " 8080 " -> nettoye -> Optional["8080"]
 *   lookup(env, "debug") -> "  "     -> nettoye -> "" -> vide
 *   lookup(env, "host")  -> null     -> vide
 *
 * -- Le plan --
 *
 *   1. Emballer values.get(key) dans une boite qui tolere null.
 *   2. Nettoyer les espaces autour (strip).
 *   3. Ne garder que si non vide.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Cette methode EST la boite magique de base : tous les autres TODO
 * l'utilisent.
 *
 *
 * ==================================================================
 * TODO 2 : resolve(sources, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu ouvres les tiroirs DANS L'ORDRE et tu t'arretes au premier qui
 * contient une valeur utilisable. Tu n'ouvres PAS les tiroirs suivants
 * (c'est peut-etre couteux : un fichier, un reseau...). Un stream
 * paresseux + findFirst fait exactement ca : main() le verifie en
 * comptant combien de fois on fouille le tiroir "file".
 *
 * Astuce : chaque tiroir donne une BOITE (Optional). Un stream de
 * boites, ca s'aplatit avec flatMap(Optional::stream) : les boites
 * vides disparaissent, les pleines donnent leur valeur.
 *
 * -- Essayons a la main --
 *
 *   "port"    : env -> "8080" -> STOP (file et defaults jamais ouverts)
 *   "host"    : env vide, file -> "example.org" -> STOP
 *   "level"   : env vide, file vide, defaults -> "INFO"
 *   "debug"   : env "  " -> vide ; file, defaults vides -> vide
 *   "timeout" : env vide, file -> "abc" -> STOP (valeur pourrie, mais
 *               PRESENTE : resolve ne juge pas le contenu)
 *
 * -- Le plan --
 *
 *   1. Parcourir les sources dans l'ordre.
 *   2. Transformer chaque source en boite (TODO 1).
 *   3. Aplatir les boites, prendre la premiere valeur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : lookup (TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : resolveInt(sources, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Certains reglages sont des nombres. On veut une OptionalInt (la
 * boite a int, sans objet Integer). Probleme : Optional<String> n'a
 * PAS de mapToInt. Il faut donc :
 *   Optional<String> --filter (que des chiffres)--> --map--> Optional<Integer>
 *   puis passer de Optional<Integer> a OptionalInt : map(OptionalInt::of)
 *   donne un Optional<OptionalInt>, qu'on ouvre avec
 *   orElseGet(OptionalInt::empty).
 *
 * PIEGE voulu : pour "timeout", resolve rend "abc" (file), qui n'est
 * pas un nombre -> vide. On ne va PAS chercher le "30" des defaults :
 * la couche file a gagne, meme avec une valeur invalide. (C'est le
 * comportement de beaucoup de vraies applis : une config invalide est
 * une erreur, pas un "essaie la suivante".)
 *
 * -- Essayons a la main --
 *
 *   "port"    -> "8080" -> 8080 -> OptionalInt[8080]
 *   "retries" -> "42"   -> OptionalInt[42]
 *   "timeout" -> "abc"  -> vide
 *   "host"    -> "example.org" -> vide
 *   "zzz"     -> vide
 *
 * -- Le plan --
 *
 *   1. resolve(sources, key).
 *   2. Ne garder que si c'est "un signe moins optionnel puis 1 a 9
 *      chiffres".
 *   3. Convertir en Integer.
 *   4. Convertir la boite Optional<Integer> en OptionalInt.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : resolve (TODO 2).
 *
 *
 * ==================================================================
 * TODO 4 : resolveIntInRange(sources, key, min, max, defaultValue)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un port doit etre entre 1 et 65535 ; si la valeur est absente,
 * invalide OU hors limites, on prend defaultValue. Surprise : OptionalInt
 * n'a NI filter NI map ! Mais il a stream() (Java 9), qui donne un
 * IntStream de 0 ou 1 element... et IntStream, lui, a filter et
 * findFirst.
 *
 * -- Essayons a la main --
 *
 *   ("port", 1, 65535, 80)  -> 8080 dans les limites -> 8080
 *   ("retries", 0, 10, 3)   -> 42 hors limites -> 3
 *   ("timeout", 1, 60, 15)  -> vide (abc) -> 15
 *   ("zzz", 0, 100, 7)      -> vide -> 7
 *
 * -- Le plan --
 *
 *   1. resolveInt(sources, key).
 *   2. En faire un IntStream, garder si min <= v <= max.
 *   3. Prendre le premier, sinon defaultValue.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : resolveInt (TODO 3).
 *
 *
 * ==================================================================
 * TODO 5 : whoDefined(sources, key)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour deboguer, on veut savoir QUELLE couche a fourni la valeur. Cette
 * fois, on garde la SOURCE (pas la valeur) : on filtre les sources dont
 * lookup est plein, et on prend le nom de la premiere.
 *
 * -- Essayons a la main --
 *
 *   "port" -> env ; "host" -> file ; "level" -> defaults ; "debug" -> vide
 *
 * -- Le plan --
 *
 *   1. Parcourir les sources dans l'ordre.
 *   2. Garder celles ou lookup trouve une valeur.
 *   3. Prendre le nom de la premiere.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : lookup (TODO 1).
 *
 *
 * ==================================================================
 * TODO 6 : describeAll(sources, keys)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour chaque cle, on veut une ligne "cle=valeur (source)" ou
 * "cle=<absent>". On a DEUX boites : la valeur (resolve) et le nom
 * (whoDefined). Comment combiner 2 boites en 1 SANS les ouvrir ?
 *   valeur.flatMap(v -> nom.map(n -> ...v...n...))
 * Si l'une des deux est vide, le resultat est vide. C'est LE motif a
 * retenir pour "combiner 2 Optional".
 *
 * -- Essayons a la main --
 *
 *   keys = [port, debug, level]
 *   port  -> "8080" et "env"      -> "port=8080 (env)"
 *   debug -> vide                 -> "debug=<absent>"
 *   level -> "INFO" et "defaults" -> "level=INFO (defaults)"
 *
 * -- Le plan --
 *
 *   1. Pour chaque cle (stream des cles) :
 *      a. combiner resolve et whoDefined en une ligne formatee,
 *      b. sinon "cle=<absent>".
 *   2. Rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : l'etape 1 "decrire UNE cle" se raconte seule. Fais-en une
 * methode privee describeOne(sources, key), puis appelle-la dans un
 * map().
 *
 *
 * ==================================================================
 * TODO 7 : definedKeys(sources)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut la liste triee de toutes les cles qui ont une valeur
 * utilisable dans AU MOINS une couche ("debug" n'en fait pas partie :
 * sa seule valeur est blanche). Il faut aplatir les ensembles de cles
 * de toutes les couches, mais on doit garder la source a portee de
 * main pour appeler lookup(source, cle) : le filter se fait donc A
 * L'INTERIEUR du flatMap.
 *
 * -- Essayons a la main --
 *
 *   env      : port (ok), debug (blanc -> non)
 *   file     : port, host, timeout, retries
 *   defaults : host, timeout, retries, level
 *   -> uniques, tries : [host, level, port, retries, timeout]
 *
 * -- Le plan --
 *
 *   1. Pour chaque source : stream de ses cles, filtre "lookup est
 *      plein". Aplatir.
 *   2. Retirer les doublons, trier, rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : lookup (TODO 1).
 *
 *
 * Exemple a verifier :
 *
 *   lookup(env, port) == Optional[8080] ; lookup(env, debug) vide ; lookup(env, host) vide
 *   resolve : port -> 8080, host -> example.org, level -> INFO, timeout -> abc, debug -> vide, zzz -> vide
 *   resolve(port) n'ouvre JAMAIS le tiroir "file" (0 appel a get)
 *   resolveInt : port -> 8080, retries -> 42, timeout -> vide, host -> vide, zzz -> vide
 *   resolveIntInRange : (port,1,65535,80) -> 8080 ; (retries,0,10,3) -> 3 ;
 *                       (timeout,1,60,15) -> 15 ; (zzz,0,100,7) -> 7
 *   whoDefined : port -> env, host -> file, level -> defaults, debug -> vide
 *   describeAll([port, debug, level]) == [port=8080 (env), debug=<absent>, level=INFO (defaults)]
 *   definedKeys == [host, level, port, retries, timeout]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Optional.ofNullable(source.values().get(key)).map(String::strip).filter(v -> !v.isEmpty())
 *   - sources.stream().map(s -> lookup(s, key)).flatMap(Optional::stream).findFirst()
 *   - .filter(v -> v.matches("-?\\d{1,9}")).map(Integer::parseInt)
 *       .map(OptionalInt::of).orElseGet(OptionalInt::empty)
 *   - optionalInt.stream().filter(v -> v >= min && v <= max).findFirst().orElse(defaultValue)
 *   - sources.stream().filter(s -> lookup(s, key).isPresent()).map(Source::name).findFirst()
 *   - resolve(...).flatMap(v -> whoDefined(...).map(n -> key + "=" + v + " (" + n + ")"))
 *       .orElse(key + "=<absent>")
 *   - sources.stream().flatMap(s -> s.values().keySet().stream().filter(k -> lookup(s, k).isPresent()))
 */
public class Exercise03_OptionalConfigResolver {

    public record Source(String name, Map<String, String> values) {
    }

    public static Optional<String> lookup(Source source, String key) {
        throw new UnsupportedOperationException("TODO 1 : implementer lookup()");
    }

    public static Optional<String> resolve(List<Source> sources, String key) {
        throw new UnsupportedOperationException("TODO 2 : implementer resolve()");
    }

    public static OptionalInt resolveInt(List<Source> sources, String key) {
        throw new UnsupportedOperationException("TODO 3 : implementer resolveInt()");
    }

    public static int resolveIntInRange(List<Source> sources, String key, int min, int max, int defaultValue) {
        throw new UnsupportedOperationException("TODO 4 : implementer resolveIntInRange()");
    }

    public static Optional<String> whoDefined(List<Source> sources, String key) {
        throw new UnsupportedOperationException("TODO 5 : implementer whoDefined()");
    }

    public static List<String> describeAll(List<Source> sources, List<String> keys) {
        throw new UnsupportedOperationException("TODO 6 : implementer describeAll()");
    }

    public static List<String> definedKeys(List<Source> sources) {
        throw new UnsupportedOperationException("TODO 7 : implementer definedKeys()");
    }

    public static void main(String[] args) {
        int[] fileGets = {0};
        Map<String, String> fileValues = new HashMap<>(Map.of(
                "port", "9090", "host", "example.org", "timeout", "abc", "retries", "42")) {
            @Override
            public String get(Object key) {
                fileGets[0]++;
                return super.get(key);
            }
        };
        Source env = new Source("env", Map.of("port", " 8080 ", "debug", "  "));
        Source file = new Source("file", fileValues);
        Source defaults = new Source("defaults", Map.of("host", "localhost", "timeout", "30", "retries", "3", "level", "INFO"));
        List<Source> sources = List.of(env, file, defaults);

        ExerciseChecker.check("lookup(env, port) == 8080 (espaces retires)", lookup(env, "port").equals(Optional.of("8080")));
        ExerciseChecker.check("lookup(env, debug) est vide (valeur blanche)", lookup(env, "debug").isEmpty());
        ExerciseChecker.check("lookup(env, host) est vide (absente)", lookup(env, "host").isEmpty());

        fileGets[0] = 0;
        ExerciseChecker.check("resolve(port) == 8080 (env gagne)", resolve(sources, "port").equals(Optional.of("8080")));
        ExerciseChecker.check("resolve(port) n'a jamais ouvert le tiroir file (paresse)", fileGets[0] == 0);
        ExerciseChecker.check("resolve(host) == example.org", resolve(sources, "host").equals(Optional.of("example.org")));
        ExerciseChecker.check("resolve(level) == INFO", resolve(sources, "level").equals(Optional.of("INFO")));
        ExerciseChecker.check("resolve(timeout) == abc", resolve(sources, "timeout").equals(Optional.of("abc")));
        ExerciseChecker.check("resolve(debug) est vide", resolve(sources, "debug").isEmpty());
        ExerciseChecker.check("resolve(zzz) est vide", resolve(sources, "zzz").isEmpty());

        ExerciseChecker.check("resolveInt(port) == 8080", resolveInt(sources, "port").equals(OptionalInt.of(8080)));
        ExerciseChecker.check("resolveInt(retries) == 42", resolveInt(sources, "retries").equals(OptionalInt.of(42)));
        ExerciseChecker.check("resolveInt(timeout) est vide (abc gagne, pas de repli sur 30)",
                resolveInt(sources, "timeout").isEmpty());
        ExerciseChecker.check("resolveInt(host) est vide", resolveInt(sources, "host").isEmpty());
        ExerciseChecker.check("resolveInt(zzz) est vide", resolveInt(sources, "zzz").isEmpty());

        ExerciseChecker.check("resolveIntInRange(port, 1, 65535, 80) == 8080", resolveIntInRange(sources, "port", 1, 65535, 80) == 8080);
        ExerciseChecker.check("resolveIntInRange(retries, 0, 10, 3) == 3 (42 hors limites)", resolveIntInRange(sources, "retries", 0, 10, 3) == 3);
        ExerciseChecker.check("resolveIntInRange(timeout, 1, 60, 15) == 15", resolveIntInRange(sources, "timeout", 1, 60, 15) == 15);
        ExerciseChecker.check("resolveIntInRange(zzz, 0, 100, 7) == 7", resolveIntInRange(sources, "zzz", 0, 100, 7) == 7);

        ExerciseChecker.check("whoDefined(port) == env", whoDefined(sources, "port").equals(Optional.of("env")));
        ExerciseChecker.check("whoDefined(host) == file", whoDefined(sources, "host").equals(Optional.of("file")));
        ExerciseChecker.check("whoDefined(level) == defaults", whoDefined(sources, "level").equals(Optional.of("defaults")));
        ExerciseChecker.check("whoDefined(debug) est vide", whoDefined(sources, "debug").isEmpty());

        ExerciseChecker.check("describeAll([port, debug, level])",
                describeAll(sources, List.of("port", "debug", "level"))
                        .equals(List.of("port=8080 (env)", "debug=<absent>", "level=INFO (defaults)")));

        ExerciseChecker.check("definedKeys == [host, level, port, retries, timeout]",
                definedKeys(sources).equals(List.of("host", "level", "port", "retries", "timeout")));

        ExerciseChecker.summary();
    }
}
