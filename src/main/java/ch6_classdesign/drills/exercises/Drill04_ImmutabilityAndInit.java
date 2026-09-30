package ch6_classdesign.drills.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * DRILL 04 - Immuabilite, champs final et blocs d'initialisation
 * ==============================================================
 *
 * Mode d'emploi : voir Drill01_InheritanceAndConstructors.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Badge(name, skills)       [copie defensive a l'entree] List.copyOf(skills).
 * TODO 2  : Badge.skills()            [liste non modifiable] rendre le champ (deja une copie immuable).
 * TODO 3  : Badge.withSkill(skill)    ["modifier" = nouvel objet] un NOUVEAU Badge avec une competence de plus.
 * TODO 4  : Badge.equals(other)       [objet valeur] meme nom et memes competences.
 * TODO 5  : Badge.hashCode()          [coherent] java.util.Objects.hash(name, skills).
 * TODO 6  : Counter()                 [compteur static dans un constructeur] created++ ; id = created.
 * TODO 7  : Logged : bloc static      [static { }] ajouter "static" a LOG (le bloc vide est deja la).
 * TODO 8  : Logged : bloc d'instance  [{ }] ajouter "instance" a LOG.
 * TODO 9  : Logged()                  [constructeur] ajouter "ctor" a LOG.
 * TODO 10 : Config(name)              [final vide affecte dans le constructeur] name, puis retries = 3.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Immuable : classe final, champs private final, pas de setter, copies a l'entree ET a la sortie,
 *              chaque "modification" rend un NOUVEL objet
 *   final : une seule affectation (declaration, bloc, ou CHAQUE constructeur)
 *   static final : a la declaration ou dans un bloc static, jamais dans un constructeur
 *   static { } : une fois, au chargement de la classe ; { } : a chaque new, avant le corps du constructeur
 * ---------------------------------------------------------------------
 */
public class Drill04_ImmutabilityAndInit {

    static final class Badge {
        private final String name;
        private final List<String> skills;

        Badge(String name, List<String> skills) {
            throw new UnsupportedOperationException("TODO 1 : implementer Badge(name, skills)");
        }

        String name() {
            return name;
        }

        List<String> skills() {
            throw new UnsupportedOperationException("TODO 2 : implementer skills()");
        }

        Badge withSkill(String skill) {
            throw new UnsupportedOperationException("TODO 3 : implementer withSkill()");
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("TODO 4 : implementer equals()");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("TODO 5 : implementer hashCode()");
        }
    }

    static class Counter {
        static int created;
        final int id;

        Counter() {
            throw new UnsupportedOperationException("TODO 6 : implementer Counter()");
        }
    }

    static final List<String> LOG = new ArrayList<>();

    static class Logged {
        static {
            // TODO 7 : ajouter "static" a LOG
        }

        {
            // TODO 8 : ajouter "instance" a LOG
        }

        Logged() {
            // TODO 9 : ajouter "ctor" a LOG
        }
    }

    static class Config {
        final String name;
        final int retries;

        Config(String name) {
            throw new UnsupportedOperationException("TODO 10 : implementer Config(name)");
        }
    }

    public static void main(String[] args) {
        List<String> source = new ArrayList<>(List.of("java"));
        Badge ada = new Badge("Ada", source);
        source.add("hack");
        ExerciseChecker.check("1  Badge copie la liste a l'entree", ada.skills().equals(List.of("java")));
        boolean readOnly;
        try {
            ada.skills().add("x");
            readOnly = false;
        } catch (UnsupportedOperationException e) {
            readOnly = true;
        }
        ExerciseChecker.check("2  skills() non modifiable", readOnly);
        Badge more = ada.withSkill("sql");
        ExerciseChecker.check("3  withSkill : nouvel objet, ancien intact",
                more.skills().equals(List.of("java", "sql")) && ada.skills().equals(List.of("java")) && more != ada);
        ExerciseChecker.check("4  equals : meme nom et memes competences",
                ada.equals(new Badge("Ada", List.of("java"))) && !ada.equals(more) && !ada.equals(null));
        ExerciseChecker.check("5  hashCode coherent", ada.hashCode() == new Badge("Ada", List.of("java")).hashCode());
        int before = Counter.created;
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        ExerciseChecker.check("6  Counter : ids qui se suivent", c2.id == c1.id + 1 && Counter.created == before + 2);
        LOG.clear();
        new Logged();
        new Logged();
        ExerciseChecker.check("7  bloc static une seule fois", LOG.stream().filter("static"::equals).count() == 1);
        ExerciseChecker.check("8  bloc d'instance a chaque new, avant le constructeur", LOG.indexOf("instance") < LOG.indexOf("ctor")
                && LOG.stream().filter("instance"::equals).count() == 2);
        ExerciseChecker.check("9  ordre complet", LOG.equals(List.of("static", "instance", "ctor", "instance", "ctor")));
        Config config = new Config("prod");
        ExerciseChecker.check("10 Config : name et retries", config.name.equals("prod") && config.retries == 3);

        ExerciseChecker.summary();
    }
}
