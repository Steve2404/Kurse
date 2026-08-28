package methods.exercises;

/**
 * EXERCICE 4 - Quiz "ca compile ou pas ?" : static ne peut JAMAIS appeler instance directement, et les imports static (niveau : examen OCP)
 * ====================================================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz de ce chapitre. Pour chaque bloc, c'est TOI la
 * boite magique : lis d'abord l'histoire imagee, essaie de repondre
 * sur une feuille (compile / ne compile pas + pourquoi), PUIS
 * decommente le bloc et essaie de COMPILER (mvn compile ou votre IDE)
 * pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. Une methode d'INSTANCE peut TOUJOURS appeler un membre static
 *      sans probleme (voir Exercise03). L'INVERSE est FAUX : une
 *      methode STATIC ne peut JAMAIS appeler un membre d'instance
 *      "tout seul", SANS d'abord avoir un OBJET concret sur lequel
 *      appeler ce membre (une methode static n'a pas de "this").
 *   2. Un import static s'ecrit "import static ...", jamais "static
 *      import ..." - et il n'importe QUE des membres static (methodes
 *      ou champs), JAMAIS un nom de classe entiere.
 */
public class Exercise04_StaticVsInstanceQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : staticMethod() appelle instanceMethod() TOUT SEUL,
    // sans passer par aucun objet - comme si elle demandait "quel est
    // TON age ?" sans jamais preciser DE QUI elle parle.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class A {
    //     int instanceField = 42;
    //     int instanceMethod() {
    //         return instanceField;
    //     }
    //     static void staticMethod() {
    //         int x = instanceMethod();
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le meme staticMethod(), mais cette fois elle cree
    // D'ABORD un VRAI objet (new B()), PUIS appelle instanceMethod()
    // SUR CET OBJET precis - plus d'ambiguite sur le "qui".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class B {
    //     int instanceField = 42;
    //     int instanceMethod() {
    //         return instanceField;
    //     }
    //     static void staticMethod() {
    //         B b = new B();
    //         int x = b.instanceMethod();
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un veut utiliser PI directement, sans jamais
    // ecrire "Math." devant - "import static java.lang.Math.PI"
    // exactement dans cet ordre.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // import static java.lang.Math.PI;
    // static void m() {
    //     double x = PI;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : le meme import, mais quelqu'un inverse les 2 mots
    // par erreur : "static import" au lieu de "import static" - en
    // pensant que l'ordre n'a pas d'importance, comme pour les
    // modificateurs de methode (Exercise01, Bloc A).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static import java.lang.Math.PI;

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : quelqu'un essaie d'importer directement le NOM
    // D'UNE CLASSE ENTIERE (List) via "import static", en confondant
    // avec un import CLASSIQUE (import java.util.List, sans static).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // import static java.util.List;

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. staticMethod() n'a AUCUN objet
     *   implicite ("this") sur lequel appeler instanceMethod() : le
     *   compilateur ne sait PAS de quel B parler. Erreur : "non-static
     *   method instanceMethod() cannot be referenced from a static
     *   context".
     *
     * Bloc B : COMPILE. staticMethod() cree D'ABORD un objet concret
     *   (b), PUIS appelle instanceMethod() explicitement SUR CET
     *   OBJET - plus aucune ambiguite, une methode static peut
     *   parfaitement utiliser des membres d'instance de CETTE facon.
     *
     * Bloc C : COMPILE. "import static java.lang.Math.PI" (dans le
     *   BON ordre) importe le champ static PI, utilisable ensuite
     *   sans le prefixe "Math.".
     *
     * Bloc D : NE COMPILE PAS. Contrairement aux modificateurs de
     *   methode (Bloc A d'Exercise01), l'ordre des MOTS-CLES d'un
     *   import, lui, est FIGE : c'est TOUJOURS "import static", jamais
     *   l'inverse. Erreur : "class, interface, enum, or record
     *   expected" (le compilateur ne reconnait meme pas "static
     *   import" comme un debut d'instruction valide).
     *
     * Bloc E : NE COMPILE PAS. import static n'importe QUE des
     *   MEMBRES static (methodes ou champs) d'une classe, JAMAIS le
     *   nom de la classe elle-meme - pour ca, un import CLASSIQUE
     *   (sans static) suffit amplement. Erreur : "static import only
     *   from classes and interfaces" (List, ici, est traite comme
     *   la CIBLE dont on voudrait un membre, pas comme le membre
     *   lui-meme).
     */
}
