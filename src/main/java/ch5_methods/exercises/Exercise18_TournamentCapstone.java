package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

/**
 * EXERCICE 18 (CAPSTONE) - Un tournoi : fabrique static, varargs, surcharges, boxing, copie defensive, lambda (niveau : capstone)
 * ==========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On organise des tournois de 4 joueurs au plus. Chaque notion du
 * chapitre a sa place :
 *   - static : un compteur de tournois crees, une constante MAX_PLAYERS,
 *     une fabrique create() (le constructeur est private) ;
 *   - varargs : register("Ada", "Linus", ...) ;
 *   - surcharge : score(joueur, int), score(joueur, int, bonus),
 *     score(joueur, Integer) (qui accepte null = "pas de score") ;
 *   - passage par valeur : snapshot() rend une COPIE des scores ;
 *   - effectivement final : une lambda qui capture un seuil.
 *
 *
 * ==================================================================
 * TODO 1 : createdCount()    et    TODO 2 : create(name)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. createdCount : rendre created.
 *   2. create : name null ou blank -> IllegalArgumentException("nom obligatoire") ;
 *      sinon created++ et rendre new Tournament(name).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : register(names...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tout ou rien : si l'inscription ferait depasser MAX_PLAYERS, ou si un
 * nom est deja inscrit (ou repete dans la meme demande), on n'inscrit
 * PERSONNE et on rend false.
 *
 * -- Essayons a la main --
 *
 *   register("Ada", "Linus")          -> true (2 joueurs)
 *   register("Grace", "Ada")          -> false (Ada deja la) ; toujours 2 joueurs
 *   register("Grace", "Alan", "Tim")  -> false (5 > 4)
 *   register("Grace", "Alan")         -> true (4 joueurs)
 *
 * -- Le plan --
 *
 *   1. Si players.size() + names.length > MAX_PLAYERS -> false.
 *   2. Pour chaque nom : deja dans players, ou present deux fois dans names -> false.
 *   3. Tout ajouter ; true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : score(player, points)    TODO 5 : score(player, points, bonus)    TODO 6 : score(player, Integer points)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Trois surcharges. score("Ada", 5) choisit la version int (exacte, sans
 * boxing). La version a 3 parametres REUTILISE la premiere. La version
 * Integer sert quand le score peut manquer (null) : on ne compte rien.
 * Chacune rend false si le joueur n'est pas inscrit.
 *
 * -- Le plan --
 *
 *   1. score(p, int) : i = players.indexOf(p) ; -1 -> false ; scores[i] += points ; true.
 *   2. score(p, int, int) : rendre score(p, points + bonus).
 *   3. score(p, Integer) : null -> false ; sinon rendre score(p, points.intValue()).
 *      (Attention : score(p, points) avec un Integer rappellerait la version Integer... sans fin !)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les surcharges s'appellent entre elles.
 *
 *
 * ==================================================================
 * TODO 7 : snapshot()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On donne les scores des joueurs inscrits, mais une COPIE : si
 * l'appelant la modifie, le vrai tableau ne bouge pas.
 *
 * -- Le plan --
 *
 *   1. Rendre Arrays.copyOf(scores, players.size()).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : leader()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Aucun joueur -> "personne" ; sinon le nom du meilleur score (le premier en cas d'egalite).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : playersAbove(threshold)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On fabrique d'abord une lambda IntPredicate qui capture threshold (un
 * parametre jamais modifie : effectivement final), puis on l'applique a
 * chaque score.
 *
 * -- Le plan --
 *
 *   1. IntPredicate high = score -> score > threshold.
 *   2. Garder les noms des joueurs dont le score passe le test.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Arrays.asList(names).subList(i + 1, names.length).contains(names[i]) detecte un doublon.
 *   - high.test(scores[i]).
 */
public class Exercise18_TournamentCapstone {

    public static final class Tournament {
        public static final int MAX_PLAYERS = 4;
        private static int created;

        private final String name;
        private final List<String> players = new ArrayList<>();
        private final int[] scores = new int[MAX_PLAYERS];

        private Tournament(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<String> getPlayers() {
            return List.copyOf(players);
        }

        public static Tournament create(String name) {
            throw new UnsupportedOperationException("TODO 2 : implementer create()");
        }

        public static int createdCount() {
            throw new UnsupportedOperationException("TODO 1 : implementer createdCount()");
        }

        public boolean register(String... names) {
            throw new UnsupportedOperationException("TODO 3 : implementer register()");
        }

        public boolean score(String player, int points) {
            throw new UnsupportedOperationException("TODO 4 : implementer score(String, int)");
        }

        public boolean score(String player, int points, int bonus) {
            throw new UnsupportedOperationException("TODO 5 : implementer score(String, int, int)");
        }

        public boolean score(String player, Integer points) {
            throw new UnsupportedOperationException("TODO 6 : implementer score(String, Integer)");
        }

        public int[] snapshot() {
            throw new UnsupportedOperationException("TODO 7 : implementer snapshot()");
        }

        public String leader() {
            throw new UnsupportedOperationException("TODO 8 : implementer leader()");
        }

        public List<String> playersAbove(int threshold) {
            throw new UnsupportedOperationException("TODO 9 : implementer playersAbove()");
        }
    }

    public static void main(String[] args) {
        int before = Tournament.createdCount();
        Tournament cup = Tournament.create("Coupe");
        Tournament.create("Open");
        ExerciseChecker.check("create + createdCount (+2)", cup.getName().equals("Coupe") && Tournament.createdCount() - before == 2);
        String error = null;
        try {
            Tournament.create(" ");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("create(\" \") -> \"nom obligatoire\" et le compteur ne bouge pas",
                "nom obligatoire".equals(error) && Tournament.createdCount() - before == 2);

        ExerciseChecker.check("register : tout ou rien",
                cup.register("Ada", "Linus") && !cup.register("Grace", "Ada") && !cup.register("Grace", "Grace")
                        && !cup.register("Grace", "Alan", "Tim") && cup.getPlayers().equals(List.of("Ada", "Linus"))
                        && cup.register("Grace", "Alan") && cup.getPlayers().size() == 4);

        Integer missing = null;
        boolean scored = cup.score("Ada", 10) && cup.score("Linus", 7, 5) && cup.score("Grace", Integer.valueOf(3))
                && !cup.score("Alan", missing) && !cup.score("Inconnu", 1);
        ExerciseChecker.check("score : les 3 surcharges et les refus", scored);
        ExerciseChecker.check("snapshot == [10, 12, 3, 0]", Arrays.equals(cup.snapshot(), new int[] {10, 12, 3, 0}));
        int[] copy = cup.snapshot();
        copy[0] = 999;
        ExerciseChecker.check("snapshot est une copie : le tournoi n'a pas change", cup.snapshot()[0] == 10);
        ExerciseChecker.check("leader : Linus (12) ; tournoi vide -> personne",
                cup.leader().equals("Linus") && Tournament.create("Vide").leader().equals("personne"));
        ExerciseChecker.check("playersAbove(5) == [Ada, Linus]", cup.playersAbove(5).equals(List.of("Ada", "Linus")));

        ExerciseChecker.summary();
    }
}
