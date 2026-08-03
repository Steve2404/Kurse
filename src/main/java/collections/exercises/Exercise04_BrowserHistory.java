package collections.exercises;

import collections.ExerciseChecker;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * EXERCICE 4 - Historique de navigateur avec deux Deque (niveau : moyen/difficile)
 * ==================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO : completer BrowserHistory
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un livre "dont vous etes le heros" avec un marque-page. Tu
 * lis une page (la page "actuelle"). Quand tu tournes vers une
 * NOUVELLE page, tu empiles la page que tu quittes sur une pile "pages
 * d'avant" posee a gauche de toi.
 *
 * Si tu veux "revenir en arriere", tu reposes la page actuelle sur
 * une deuxieme pile, "pages d'apres", posee a droite, et tu reprends
 * la derniere page de la pile de gauche comme nouvelle page actuelle.
 *
 * Si tu veux "avancer" ensuite, c'est l'inverse : tu reposes la page
 * actuelle a gauche, et tu reprends la derniere page de la pile de
 * droite.
 *
 * MAIS, piege important : si a un moment tu decides de tourner vers
 * une page TOUTE NOUVELLE (pas juste "avancer" dans ce que tu avais
 * deja visite), la pile de droite ("pages d'apres") ne veut plus rien
 * dire - jette-la completement. C'est comme dans un livre-jeu : si tu
 * choisis un nouveau chemin, les anciennes pages "d'apres" de l'ancien
 * chemin n'existent plus.
 *
 * -- Essayons a la main, avec deux petites piles de cartes --
 *
 * Carte actuelle : home.com. Pile gauche (avant) : vide. Pile droite
 * (apres) : vide.
 *
 * Tu vas sur google.com : la carte home.com part sur la pile gauche.
 * Carte actuelle = google.com. Pile gauche = [home.com].
 *
 * Tu vas sur wikipedia.org : google.com part sur la pile gauche.
 * Carte actuelle = wikipedia.org. Pile gauche = [home.com, google.com]
 * (google.com est au sommet, donc le prochain "back" le retrouve en
 * premier).
 *
 * Tu fais "back" : la carte actuelle (wikipedia.org) part sur la
 * pile droite. La nouvelle carte actuelle est celle au sommet de la
 * pile gauche : google.com. Pile gauche = [home.com]. Pile droite =
 * [wikipedia.org].
 *
 * -- Ce qu'on remarque --
 *
 * On manipule toujours le SOMMET d'une pile (le dernier depose), on
 * ne regarde jamais "le 3eme depuis le bas" ou un index quelconque.
 * En Java, une structure qui donne un acces rapide UNIQUEMENT au
 * sommet, avec "en poser une" et "en reprendre une", ca s'appelle
 * une pile, et Deque sait tres bien jouer ce role avec push()/pop().
 *
 * -- Le plan --
 *
 * Trois actions possibles, chacune avec sa propre petite recette :
 *
 *   visiter(nouvellePage) :
 *     1. Poser la page actuelle sur la pile gauche (avant).
 *     2. Jeter completement la pile droite (apres) - elle n'a plus de
 *        sens.
 *     3. La nouvelle page devient la page actuelle.
 *
 *   revenirEnArriere() :
 *     1. Si la pile gauche est vide, ne rien faire (on ne peut pas
 *        reculer plus).
 *     2. Sinon : poser la page actuelle sur la pile droite (apres).
 *     3. Reprendre la carte du sommet de la pile gauche comme
 *        nouvelle page actuelle.
 *
 *   avancer() : exactement le symetrique de revenirEnArriere(), en
 *   inversant les roles des deux piles.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Remarque que avancer() est EXACTEMENT revenirEnArriere() en
 * inversant "gauche" et "droite" (Q2 : ca revient presque a
 * l'identique). Tu peux t'entrainer : essaie d'ecrire une seule boite
 * privee move(Deque<String> depart, Deque<String> arrivee) qui fait
 * le travail commun, et fais appeler cette boite par back() ET par
 * forward() avec les piles dans un ordre different. Ce n'est pas
 * obligatoire, mais c'est un tres bon entrainement pour sentir un
 * Q2 "ca revient plusieurs fois" bien net.
 *
 * Exemple a verifier : home -> google -> wikipedia -> github, puis
 * back(), back() -> doit revenir a google.com ; puis forward() ->
 * doit revenir a wikipedia.org.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Utilise exclusivement push()/pop()/peek() des Deque (pas de
 *     get(index)).
 *   - back() : si backStack est vide, retourner la page actuelle sans
 *     rien changer. Sinon : forwardStack.push(currentPage), puis
 *     currentPage = backStack.pop().
 *   - forward() : symetrique de back(), en inversant les deux piles.
 *   - visit(url) : backStack.push(currentPage), puis
 *     forwardStack.clear(), puis currentPage = url.
 */
public class Exercise04_BrowserHistory {

    static class BrowserHistory {
        private final Deque<String> backStack = new ArrayDeque<>();
        private final Deque<String> forwardStack = new ArrayDeque<>();
        private String currentPage;

        BrowserHistory(String homepage) {
            this.currentPage = homepage;
        }

        void visit(String url) {
            throw new UnsupportedOperationException("TODO : implementer visit()");
        }

        String back() {
            throw new UnsupportedOperationException("TODO : implementer back()");
        }

        String forward() {
            throw new UnsupportedOperationException("TODO : implementer forward()");
        }

        String current() {
            return currentPage;
        }
    }

    public static void main(String[] args) {
        BrowserHistory history = new BrowserHistory("home.com");
        history.visit("google.com");
        history.visit("wikipedia.org");
        history.visit("github.com");

        ExerciseChecker.check("Page courante = github.com", history.current().equals("github.com"));

        history.back();
        ExerciseChecker.check("Apres back(), page courante = wikipedia.org", history.current().equals("wikipedia.org"));

        history.back();
        ExerciseChecker.check("Apres 2x back(), page courante = google.com", history.current().equals("google.com"));

        history.forward();
        ExerciseChecker.check("Apres forward(), page courante = wikipedia.org", history.current().equals("wikipedia.org"));

        history.visit("stackoverflow.com");
        ExerciseChecker.check("Une nouvelle visite efface le forward", history.current().equals("stackoverflow.com"));
        String afterForward = history.forward();
        ExerciseChecker.check("forward() apres une nouvelle visite ne fait rien (plus de page suivante)",
                afterForward.equals("stackoverflow.com"));

        history.back();
        history.back();
        history.back();
        String home = history.back(); // deja au fond de la pile -> ne bouge plus
        ExerciseChecker.check("back() au-dela du debut de l'historique reste sur home.com", home.equals("home.com"));

        ExerciseChecker.summary();
    }
}