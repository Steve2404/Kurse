package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 12 - Classes anonymes : implementer une interface (ou etendre une classe) SANS lui donner de nom (niveau : moyen/difficile)
 * ==============================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une classe anonyme, c'est une classe locale qu'on utilise UNE SEULE
 * FOIS, sur place, sans jamais lui donner de nom - comme griffonner
 * une recette sur un post-it plutot que d'ecrire tout un livre de
 * cuisine juste pour un plat qu'on ne refera jamais. "new Handler() {
 * ... }" cree, EN MEME TEMPS, une nouvelle classe (sans nom) qui
 * implemente Handler, ET une instance de cette classe, en une seule
 * expression. REGLE STRICTE : une classe anonyme doit implementer
 * EXACTEMENT une interface, OU etendre EXACTEMENT une classe - jamais
 * les deux, jamais plusieurs interfaces a la fois.
 *
 *
 * ==================================================================
 * TODO 1 : buildUppercaseHandler()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "new Handler() { ... }", avec un handle(input) qui
 *      renvoie input.toUpperCase().
 *
 *
 * ==================================================================
 * TODO 2 : buildLoggingHandler(delegate)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut "envelopper" un Handler existant (delegate) dans un AUTRE
 * Handler qui ajoute juste un prefixe de log avant de rendre la main
 * a l'original - exactement comme un emballage cadeau autour d'un
 * cadeau deja pret : le cadeau (delegate) ne change pas, on ajoute
 * juste du papier autour. La classe anonyme capture delegate (le
 * parametre de la methode englobante), qui doit donc etre effectively
 * final - exactement comme pour une classe locale (Exercise11).
 *
 * -- Le plan --
 *
 *   1. Renvoyer "new Handler() { ... }", avec un handle(input) qui
 *      renvoie "[LOG] " + delegate.handle(input).
 *
 *
 * ==================================================================
 * TODO 3 : buildFormalGreeter()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Cette fois, ce n'est pas une interface qu'on implemente, mais une
 * classe ABSTRAITE (Greeter, fournie plus bas) qu'on ETEND - meme
 * principe, meme syntaxe "new Greeter() { ... }", mais la classe
 * anonyme herite AUSSI de shout() (deja concrete dans Greeter), sans
 * avoir besoin de la reecrire.
 *
 * -- Le plan --
 *
 *   1. Renvoyer "new Greeter() { ... }", avec un greet(name) qui
 *      renvoie "Bonjour, " + name.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : la classe anonyme EST elle-meme deja la "boite" du plan.
 *
 * Exemple a verifier : buildUppercaseHandler().handle("ada") ==
 * "ADA". buildLoggingHandler(buildUppercaseHandler()).handle("ada")
 * == "[LOG] ADA". buildFormalGreeter().shout("Ada") ==
 * "BONJOUR, ADA" (shout(), herite de Greeter, appelle notre greet()
 * puis met tout en majuscules).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "return new Handler() { public String handle(String input) {
 *     return ...; } };" - n'oubliez pas le point-virgule final APRES
 *     l'accolade fermante : c'est une EXPRESSION (elle cree un objet),
 *     pas une declaration de classe classique.
 */
public class Exercise12_AnonymousClasses {

    interface Handler {
        String handle(String input);
    }

    abstract static class Greeter {
        abstract String greet(String name);

        String shout(String name) {
            return greet(name).toUpperCase();
        }
    }

    public static Handler buildUppercaseHandler() {
        throw new UnsupportedOperationException("TODO 1 : implementer buildUppercaseHandler()");
    }

    public static Handler buildLoggingHandler(Handler delegate) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildLoggingHandler()");
    }

    public static Greeter buildFormalGreeter() {
        throw new UnsupportedOperationException("TODO 3 : implementer buildFormalGreeter()");
    }

    public static void main(String[] args) {
        Handler uppercase = buildUppercaseHandler();
        ExerciseChecker.check("classe anonyme implementant Handler", uppercase.handle("ada").equals("ADA"));

        Handler logging = buildLoggingHandler(uppercase);
        ExerciseChecker.check("classe anonyme capturant un delegate effectively final",
                logging.handle("ada").equals("[LOG] ADA"));

        Greeter formal = buildFormalGreeter();
        ExerciseChecker.check("classe anonyme etendant une classe abstraite, herite de shout()",
                formal.shout("Ada").equals("BONJOUR, ADA"));

        ExerciseChecker.summary();
    }
}
