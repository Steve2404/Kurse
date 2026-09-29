package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * EXERCICE 2 - Optional avance : ofNullable, map/flatMap/filter en chaine, or(), ifPresentOrElse, stream(), OptionalInt (niveau : difficile)
 * =====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Pourquoi un 2e exercice sur Optional ? --
 *
 * Exercise01 t'a appris a OUVRIR une boite Optional (isPresent/get,
 * orElseGet, orElseThrow, ifPresent). Ici, on apprend a TRAVAILLER
 * AVEC la boite SANS jamais l'ouvrir : on la transforme (map), on la
 * trie (filter), on la remplace (or), on l'aplatit (flatMap), et on
 * la melange avec des streams (Optional.stream(), OptionalInt).
 * C'est exactement ce que l'examen OCP adore pieger.
 *
 * Donnees utilisees : un "annuaire" Map<String, User> ou un User peut
 * ne PAS avoir d'adresse (address == null), et une Address peut avoir
 * une ville ou un code postal null.
 *
 *
 * ==================================================================
 * TODO 1 : findUser(directory, id)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu demandes a la maitresse le cahier de "Zoe", mais il n'y a pas de
 * Zoe dans la classe : la maitresse te tend... rien (null). Map.get()
 * fait pareil : il renvoie null si la cle n'existe pas. On veut
 * EMBALLER ce "peut-etre null" dans une boite Optional. Attention :
 * Optional.of(null) EXPLOSE (NullPointerException) - il faut la
 * version qui accepte le null : Optional.ofNullable.
 *
 * -- Essayons a la main --
 *
 *   directory.get("u1") -> User Alice    -> Optional[Alice]
 *   directory.get("zz") -> null          -> Optional.empty
 *
 * -- Le plan --
 *
 *   1. Recuperer la valeur associee a id dans la map.
 *   2. L'emballer dans un Optional qui tolere le null.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule ligne. Mais cette methode EST elle-meme une boite
 * magique que les TODO 2 et 4 vont reutiliser.
 *
 *
 * ==================================================================
 * TODO 2 : cityOf(directory, id)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour trouver la ville d'un utilisateur, il faut traverser 3 portes :
 * l'utilisateur existe ? il a une adresse ? l'adresse a une ville ?
 * Avec des if (x != null), ca fait un escalier de 3 if imbriques.
 * Avec Optional.map(), chaque porte fermee (null) transforme
 * AUTOMATIQUEMENT la boite en boite vide, et les map() suivants ne
 * font plus rien : pas besoin d'un seul if.
 *
 * -- Essayons a la main --
 *
 *   "u1" : Optional[Alice] -> map(address) -> Optional[Lyon/69001]
 *          -> map(city) -> Optional["Lyon"] -> map(upper) -> "LYON"
 *   "u2" : Optional[Bob] -> map(address) -> address est null
 *          -> Optional.empty -> ... -> "INCONNUE"
 *   "u4" : Dan a une adresse, mais city == null -> empty -> "INCONNUE"
 *   "zz" : Optional.empty des le depart -> "INCONNUE"
 *
 * -- Le plan --
 *
 *   1. Partir de findUser(directory, id).
 *   2. Transformer en l'adresse de l'utilisateur.
 *   3. Transformer en la ville de l'adresse.
 *   4. Transformer en majuscules.
 *   5. Si a un moment la boite est devenue vide, renvoyer "INCONNUE".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui pour l'etape 1 (reutiliser findUser, deja ecrite). Le reste tient
 * en une chaine d'appels.
 *
 *
 * ==================================================================
 * TODO 3 : parsePositive(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On te donne un papier ou quelqu'un a peut-etre ecrit un nombre.
 * Peut-etre que le papier est vide (null), peut-etre qu'il y a des
 * lettres dessus ("abc"), peut-etre que c'est 0 ou un nombre negatif.
 * On ne garde que les nombres STRICTEMENT POSITIFS. filter() sur un
 * Optional, c'est un videur de boite de nuit : si la valeur ne passe
 * pas le test, la boite devient vide.
 *
 * -- Essayons a la main --
 *
 *   null   -> empty (on ne peut meme pas commencer)
 *   "42"   -> que des chiffres -> 42 -> 42 > 0 -> Optional[42]
 *   "abc"  -> pas que des chiffres -> empty
 *   "-5"   -> le '-' n'est pas un chiffre -> empty
 *   "0"    -> que des chiffres -> 0 -> 0 > 0 est faux -> empty
 *   ""     -> aucun chiffre du tout -> empty
 *
 * -- Le plan --
 *
 *   1. Emballer le texte (peut-etre null) dans un Optional.
 *   2. Ne garder que s'il est compose de 1 a 9 chiffres (9 pour ne
 *      jamais depasser la capacite d'un int).
 *   3. Convertir en Integer.
 *   4. Ne garder que s'il est strictement positif.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : filter / map / filter enchaines sur une seule expression.
 *
 *
 * ==================================================================
 * TODO 4 : zipAsNumber(directory, id)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut le code postal d'un utilisateur, converti en nombre avec
 * parsePositive (TODO 3). Probleme : parsePositive renvoie DEJA une
 * boite. Si tu utilises map(), tu obtiens une boite DANS une boite
 * (Optional<Optional<Integer>>) - comme une poupee russe. flatMap(),
 * c'est "map puis ouvre la boite interieure" : on reste avec UNE
 * seule boite. Regle d'or : la fonction renvoie un Optional ?
 * -> flatMap. Elle renvoie une valeur normale ? -> map.
 *
 * -- Essayons a la main --
 *
 *   "u1" : zip "69001" -> parsePositive -> Optional[69001]
 *   "u3" : zip null    -> empty
 *   "u4" : zip "abc"   -> parsePositive -> empty
 *   "u2" : pas d'adresse -> empty
 *
 * -- Le plan --
 *
 *   1. Partir de findUser(directory, id).
 *   2. Aller a l'adresse, puis au code postal (valeurs normales).
 *   3. Passer le code postal a parsePositive (qui rend un Optional).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui, et elles existent deja : findUser (TODO 1) et parsePositive
 * (TODO 3). C'est tout l'interet de les avoir ecrites avant.
 *
 *
 * ==================================================================
 * TODO 5 : firstAvailable(primary, secondary)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu cherches un parapluie. Si tu en as un dans ton sac (primary), tu
 * le prends. Sinon SEULEMENT, tu vas en demander un au voisin
 * (secondary) - et le voisin peut lui aussi ne pas en avoir ! Donc le
 * resultat est encore une boite. orElseGet() te forcerait a sortir une
 * VALEUR ; or() (Java 9) te laisse rester dans le monde des boites, et
 * n'appelle le voisin QUE si ton sac est vide.
 *
 * -- Essayons a la main --
 *
 *   primary = Optional["A"], secondary jamais appele -> Optional["A"]
 *   primary = empty, secondary rend Optional["B"]    -> Optional["B"]
 *   primary = empty, secondary rend empty            -> empty
 *
 * -- Le plan --
 *
 *   1. Si primary a une valeur, le renvoyer tel quel.
 *   2. Sinon, renvoyer ce que fournit secondary.
 *   (Une seule methode de Optional fait exactement ca.)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une ligne.
 *
 *
 * ==================================================================
 * TODO 6 : describe(opt, log)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ifPresent() (Exercise01) n'avait pas de "sinon". ifPresentOrElse
 * (Java 9) prend DEUX recettes : une pour la boite pleine (Consumer,
 * elle recoit la valeur), une pour la boite vide (Runnable, elle ne
 * recoit RIEN - il n'y a rien a lui donner !).
 *
 * -- Essayons a la main --
 *
 *   Optional["chat"] -> ajoute "OK:chat" dans log
 *   Optional.empty   -> ajoute "VIDE" dans log
 *
 * -- Le plan --
 *
 *   1. Si valeur : ajouter "OK:" + valeur au log.
 *   2. Sinon : ajouter "VIDE" au log.
 *   (Sans isPresent() ni if : une seule methode de Optional.)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une ligne.
 *
 *
 * ==================================================================
 * TODO 7 : allPresentValues(boxes)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu as une rangee de boites, certaines pleines, certaines vides. Tu
 * veux la liste de ce qu'il y a DEDANS, en ignorant les vides.
 * Optional.stream() (Java 9) transforme une boite en mini-stream : 1
 * element si elle est pleine, 0 si elle est vide. Et flatMap sur un
 * stream "aplatit" tous ces mini-streams en un seul.
 *
 * -- Essayons a la main --
 *
 *   [Optional[a], empty, Optional[c], empty]
 *     -> mini-streams : [a], [], [c], []
 *     -> aplati       : [a, c]
 *
 * -- Le plan --
 *
 *   1. Parcourir la liste de boites en stream.
 *   2. Remplacer chaque boite par son mini-stream (0 ou 1 element) et
 *      tout aplatir.
 *   3. Rassembler dans une liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un pipeline d'une ligne (avec une reference de methode).
 *
 *
 * ==================================================================
 * TODO 8 : averageOrZero(values) et maxOrThrow(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les streams de nombres primitifs (IntStream) ont leurs PROPRES
 * boites : average() rend un OptionalDouble, max() rend un
 * OptionalInt. Ce ne sont PAS des Optional<Double> / Optional<Integer>
 * (ca ne compile pas si tu les melanges), et on les ouvre avec
 * getAsDouble() / getAsInt(), pas get(). Pourquoi une boite ? Parce
 * que la moyenne ou le max d'un tableau VIDE n'existent pas.
 *
 * -- Essayons a la main --
 *
 *   averageOrZero([2, 4, 9]) -> (2+4+9)/3 = 5.0
 *   averageOrZero([])        -> OptionalDouble.empty -> 0.0
 *   maxOrThrow([2, 4, 9])    -> 9
 *   maxOrThrow([])           -> IllegalArgumentException("tableau vide")
 *
 * -- Le plan --
 *
 *   averageOrZero : 1. transformer le tableau en IntStream,
 *                   2. calculer la moyenne (boite),
 *                   3. rendre 0.0 si la boite est vide.
 *   maxOrThrow    : 1. transformer le tableau en IntStream,
 *                   2. calculer le max (boite),
 *                   3. lancer IllegalArgumentException("tableau vide")
 *                      si la boite est vide.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chacune tient en une ligne.
 *
 *
 * Exemple a verifier :
 *
 *   findUser(dir, "u1") -> Optional contenant Alice ; findUser(dir, "zz") -> vide
 *   cityOf : "u1" -> "LYON", "u2" -> "INCONNUE", "u4" -> "INCONNUE", "zz" -> "INCONNUE"
 *   parsePositive : "42" -> Optional[42] ; null, "abc", "-5", "0", "" -> vide
 *   zipAsNumber : "u1" -> Optional[69001] ; "u2", "u3", "u4" -> vide
 *   firstAvailable : primary plein -> primary, secondary JAMAIS appele ;
 *                    primary vide -> resultat de secondary (appele 1 fois)
 *   describe : log == ["OK:chat", "VIDE"]
 *   allPresentValues([a, vide, c, vide]) -> [a, c]
 *   averageOrZero([2, 4, 9]) == 5.0 ; averageOrZero([]) == 0.0
 *   maxOrThrow([2, 4, 9]) == 9 ; maxOrThrow([]) lance IllegalArgumentException("tableau vide")
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Optional.ofNullable(x) : empty si x == null, sinon Optional[x].
 *   - opt.map(User::address) : si address() renvoie null, le resultat
 *     est Optional.empty (map utilise ofNullable en interne).
 *   - opt.filter(s -> s.matches("\\d{1,9}")) : regex = 1 a 9 chiffres.
 *   - opt.flatMap(Exercise02_OptionalAdvanced::parsePositive).
 *   - opt.or(supplierDOptional) : Supplier<Optional<T>>, pas Supplier<T>.
 *   - opt.ifPresentOrElse(v -> ..., () -> ...).
 *   - list.stream().flatMap(Optional::stream).toList().
 *   - IntStream.of(tableau).average() -> OptionalDouble ; .orElse(0.0).
 *   - IntStream.of(tableau).max() -> OptionalInt ;
 *     .orElseThrow(() -> new IllegalArgumentException("tableau vide")).
 */
public class Exercise02_OptionalAdvanced {

    public record Address(String city, String zip) {
    }

    public record User(String name, Address address) {
    }

    public static Optional<User> findUser(Map<String, User> directory, String id) {
        throw new UnsupportedOperationException("TODO 1 : implementer findUser()");
    }

    public static String cityOf(Map<String, User> directory, String id) {
        throw new UnsupportedOperationException("TODO 2 : implementer cityOf()");
    }

    public static Optional<Integer> parsePositive(String text) {
        throw new UnsupportedOperationException("TODO 3 : implementer parsePositive()");
    }

    public static Optional<Integer> zipAsNumber(Map<String, User> directory, String id) {
        throw new UnsupportedOperationException("TODO 4 : implementer zipAsNumber()");
    }

    public static Optional<String> firstAvailable(Optional<String> primary, Supplier<Optional<String>> secondary) {
        throw new UnsupportedOperationException("TODO 5 : implementer firstAvailable()");
    }

    public static void describe(Optional<String> opt, List<String> log) {
        throw new UnsupportedOperationException("TODO 6 : implementer describe()");
    }

    public static List<String> allPresentValues(List<Optional<String>> boxes) {
        throw new UnsupportedOperationException("TODO 7 : implementer allPresentValues()");
    }

    public static double averageOrZero(int[] values) {
        throw new UnsupportedOperationException("TODO 8 : implementer averageOrZero()");
    }

    public static int maxOrThrow(int[] values) {
        throw new UnsupportedOperationException("TODO 8 : implementer maxOrThrow()");
    }

    public static void main(String[] args) {
        Map<String, User> dir = Map.of(
                "u1", new User("Alice", new Address("Lyon", "69001")),
                "u2", new User("Bob", null),
                "u3", new User("Chloe", new Address("Paris", null)),
                "u4", new User("Dan", new Address(null, "abc")));

        ExerciseChecker.check("findUser(u1) contient Alice",
                findUser(dir, "u1").map(User::name).equals(Optional.of("Alice")));
        ExerciseChecker.check("findUser(zz) est vide", findUser(dir, "zz").isEmpty());

        ExerciseChecker.check("cityOf(u1) == LYON", cityOf(dir, "u1").equals("LYON"));
        ExerciseChecker.check("cityOf(u2) == INCONNUE (pas d'adresse)", cityOf(dir, "u2").equals("INCONNUE"));
        ExerciseChecker.check("cityOf(u4) == INCONNUE (ville null)", cityOf(dir, "u4").equals("INCONNUE"));
        ExerciseChecker.check("cityOf(zz) == INCONNUE (utilisateur absent)", cityOf(dir, "zz").equals("INCONNUE"));

        ExerciseChecker.check("parsePositive(\"42\") == Optional[42]", parsePositive("42").equals(Optional.of(42)));
        ExerciseChecker.check("parsePositive(null) est vide", parsePositive(null).isEmpty());
        ExerciseChecker.check("parsePositive(\"abc\") est vide", parsePositive("abc").isEmpty());
        ExerciseChecker.check("parsePositive(\"-5\") est vide", parsePositive("-5").isEmpty());
        ExerciseChecker.check("parsePositive(\"0\") est vide", parsePositive("0").isEmpty());
        ExerciseChecker.check("parsePositive(\"\") est vide", parsePositive("").isEmpty());

        ExerciseChecker.check("zipAsNumber(u1) == Optional[69001]", zipAsNumber(dir, "u1").equals(Optional.of(69001)));
        ExerciseChecker.check("zipAsNumber(u2) est vide", zipAsNumber(dir, "u2").isEmpty());
        ExerciseChecker.check("zipAsNumber(u3) est vide", zipAsNumber(dir, "u3").isEmpty());
        ExerciseChecker.check("zipAsNumber(u4) est vide", zipAsNumber(dir, "u4").isEmpty());

        int[] secondaryCalls = {0};
        Supplier<Optional<String>> neighbour = () -> {
            secondaryCalls[0]++;
            return Optional.of("B");
        };
        ExerciseChecker.check("firstAvailable(plein, ...) == A, voisin jamais appele",
                firstAvailable(Optional.of("A"), neighbour).equals(Optional.of("A")) && secondaryCalls[0] == 0);
        ExerciseChecker.check("firstAvailable(vide, ...) == B, voisin appele 1 fois",
                firstAvailable(Optional.empty(), neighbour).equals(Optional.of("B")) && secondaryCalls[0] == 1);
        ExerciseChecker.check("firstAvailable(vide, voisin vide) est vide",
                firstAvailable(Optional.empty(), Optional::empty).isEmpty());

        List<String> log = new ArrayList<>();
        describe(Optional.of("chat"), log);
        describe(Optional.empty(), log);
        ExerciseChecker.check("describe produit [OK:chat, VIDE]", log.equals(List.of("OK:chat", "VIDE")));

        List<Optional<String>> boxes = List.of(Optional.of("a"), Optional.empty(), Optional.of("c"), Optional.empty());
        ExerciseChecker.check("allPresentValues == [a, c]", allPresentValues(boxes).equals(List.of("a", "c")));

        ExerciseChecker.check("averageOrZero([2, 4, 9]) == 5.0", averageOrZero(new int[]{2, 4, 9}) == 5.0);
        ExerciseChecker.check("averageOrZero([]) == 0.0", averageOrZero(new int[]{}) == 0.0);
        ExerciseChecker.check("maxOrThrow([2, 4, 9]) == 9", maxOrThrow(new int[]{2, 4, 9}) == 9);
        String message = null;
        try {
            maxOrThrow(new int[]{});
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("maxOrThrow([]) lance IllegalArgumentException(\"tableau vide\")",
                "tableau vide".equals(message));

        ExerciseChecker.summary();
    }
}
