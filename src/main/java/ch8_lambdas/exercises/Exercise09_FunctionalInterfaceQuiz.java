package ch8_lambdas.exercises;

/**
 * EXERCICE 9 - Quiz "ca compile ou pas ?" sur @FunctionalInterface et la syntaxe des lambdas (niveau : examen OCP)
 * ======================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * Exercise10_WildcardsQuiz du package collections. Pour chaque bloc,
 * c'est TOI la boite magique : lis d'abord l'histoire imagee, essaie
 * de repondre sur une feuille (compile / ne compile pas + pourquoi),
 * PUIS decommente le bloc et essaie de COMPILER (mvn compile ou votre
 * IDE) pour verifier.
 *
 * Remettez le bloc en commentaire (ou corrigez-le) avant de passer au
 * suivant, sinon les erreurs de compilation des blocs precedents
 * empecheront de tester les suivants.
 *
 * -- Les 2 regles a garder en tete pour tout le quiz --
 *
 *   1. @FunctionalInterface exige EXACTEMENT une methode abstraite.
 *      Les methodes default, static et private ne comptent pas. Les
 *      methodes qui existent DEJA sur Object (equals, hashCode,
 *      toString...) ne comptent PAS NON PLUS, meme redeclarees en
 *      abstract dans l'interface : n'importe quelle classe qui
 *      implemente l'interface herite deja d'un equals() concret
 *      d'Object, donc la "case" est toujours remplie gratuitement.
 *   2. Dans la liste de parametres d'un lambda : soit TOUS les
 *      parametres sont explicitement types (ou tous en var), soit
 *      AUCUN ne l'est. Melanger les styles dans la MEME liste est
 *      interdit. var compte comme "type explicite" pour cette regle -
 *      on ne peut donc pas melanger var et un type primitif/objet
 *      explicite non plus.
 */
public class Exercise09_FunctionalInterfaceQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un videur de boite de nuit qui devrait avoir UNE
    // SEULE regle d'entree, mais le patron lui a donne DEUX regles
    // differentes a appliquer en meme temps, sans dire laquelle est
    // "la vraie" regle fonctionnelle.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // @FunctionalInterface
    // interface Bouncer {
    //     boolean letIn(String name);
    //     boolean kickOut(String name);
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : la meme interface que Validator (Exercise01), mais on
    // rajoute une redeclaration explicite de equals(Object), comme si
    // on voulait "forcer" une regle d'egalite personnalisee sur les
    // implementations futures.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // @FunctionalInterface
    // interface StrictValidator<T> {
    //     boolean test(T value);
    //     boolean equals(Object other);
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : une interface qui EN HERITE une autre, deja
    // fonctionnelle, sans rien ajouter du tout - juste un nom
    // different, plus parlant pour le domaine metier.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Transformer<T> {
    //     T apply(T input);
    // }
    // @FunctionalInterface
    // interface PriceAdjuster extends Transformer<Double> {
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : un lambda a 2 parametres ou on a type le premier avec
    // grand soin, mais on a oublie de faire pareil pour le second
    // (peut-etre par flemme, en pensant que Java "devinera" le
    // second tout seul puisque le premier est deja clair).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Merger {
    //     String merge(String a, String b);
    // }
    // void useBlocD() {
    //     Merger m = (String a, b) -> a + b;
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : le meme lambda que Bloc D, mais cette fois les DEUX
    // parametres utilisent var, de facon bien homogene.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Merger {
    //     String merge(String a, String b);
    // }
    // void useBlocE() {
    //     Merger m = (var a, var b) -> a + b;
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : quelqu'un a lu que "var, c'est comme ne rien ecrire",
    // et essaie de melanger var pour le premier parametre avec un type
    // explicite classique pour le second, en pensant que ca revient
    // au meme que Bloc D (qui, lui, melangeait un type explicite avec
    // RIEN du tout).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Merger {
    //     String merge(String a, String b);
    // }
    // void useBlocF() {
    //     Merger m = (var a, String b) -> a + b;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Bouncer a 2 methodes abstraites
     *   (letIn ET kickOut), ni l'une ni l'autre n'existant deja sur
     *   Object : @FunctionalInterface exige EXACTEMENT une methode
     *   abstraite, l'annotation force le compilateur a rejeter ce cas.
     *
     * Bloc B : COMPILE. test(T) est la seule "vraie" methode abstraite
     *   qui compte. equals(Object) existe deja, concrete, sur Object :
     *   la redeclarer en abstract dans l'interface ne cree PAS une
     *   deuxieme methode a implementer, elle est deja fournie
     *   gratuitement par n'importe quelle classe (toute classe herite
     *   d'Object). Piege classique de l'examen OCP.
     *
     * Bloc C : COMPILE. PriceAdjuster herite de apply(T) via
     *   Transformer<Double>, sans en ajouter d'autre : il ne reste
     *   qu'une seule methode abstraite (apply(Double)), meme si
     *   PriceAdjuster elle-meme ne declare litteralement rien.
     *
     * Bloc D : NE COMPILE PAS. "(String a, b)" melange un parametre
     *   explicitement type (String a) et un parametre non type (b) :
     *   interdit. Il faut soit "(String a, String b)", soit "(a, b)".
     *
     * Bloc E : COMPILE (Java 11+). var pour TOUS les parametres est
     *   traite comme un style homogene (type explicite implicite) :
     *   autorise, meme si ce n'est pas tres different d'ecrire
     *   "(String a, String b)" ici.
     *
     * Bloc F : NE COMPILE PAS. Meme regle que Bloc D : var COMPTE
     *   comme "type explicite" pour cette regle de coherence. Melanger
     *   var et un type explicite classique dans la MEME liste est
     *   tout aussi interdit que melanger un type et rien du tout.
     */
}
