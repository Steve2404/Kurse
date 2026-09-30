package ch6_classdesign.drills.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.drills.exercises.Drill04_ImmutabilityAndInit.
 */
public class SolutionDrill04_ImmutabilityAndInit {

    static final class Badge {
        private final String name;
        private final List<String> skills;

        Badge(String name, List<String> skills) {
            // Copie a l'entree : la liste de l'appelant peut changer, pas la notre.
            this.name = name;
            this.skills = List.copyOf(skills);
        }

        String name() {
            return name;
        }

        List<String> skills() {
            // List.copyOf est deja non modifiable : on peut la donner telle quelle.
            return skills;
        }

        Badge withSkill(String skill) {
            // "Modifier" un immuable = fabriquer un nouvel objet.
            List<String> copy = new ArrayList<>(skills);
            copy.add(skill);
            return new Badge(name, copy);
        }

        @Override
        public boolean equals(Object other) {
            // Objet valeur : egal si memes donnees.
            if (!(other instanceof Badge b)) {
                return false;
            }
            return name.equals(b.name) && skills.equals(b.skills);
        }

        @Override
        public int hashCode() {
            // Les memes champs que equals.
            return Objects.hash(name, skills);
        }
    }

    static class Counter {
        static int created;
        final int id;

        Counter() {
            // Le compteur static est partage ; id (final) est propre a chaque objet.
            created++;
            id = created;
        }
    }

    static final List<String> LOG = new ArrayList<>();

    static class Logged {
        static {
            // Une seule fois, au premier usage de la classe.
            LOG.add("static");
        }

        {
            // A chaque new, avant le corps du constructeur.
            LOG.add("instance");
        }

        Logged() {
            // Apres les blocs d'instance.
            LOG.add("ctor");
        }
    }

    static class Config {
        final String name;
        final int retries;

        Config(String name) {
            // Deux champs final vides : chacun affecte exactement une fois ici.
            this.name = name;
            this.retries = 3;
        }
    }
}
