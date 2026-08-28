package ch3_makingdecisions.exercises;

/**
 * EXERCICE 5 - Quiz "ca compile ou pas ?" sur les regles du switch statement (niveau : examen OCP)
 * ==========================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main. Pour
 * chaque bloc, c'est TOI la boite magique : lis d'abord l'histoire
 * imagee, essaie de repondre sur une feuille (compile / ne compile
 * pas + pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. La VALEUR testee par un switch doit etre d'un type COMPATIBLE :
 *      byte/short/char/int (et leurs boites Integer/Short/...),
 *      String, ou un enum - JAMAIS long, float, double ou boolean.
 *   2. Chaque valeur de case doit etre une CONSTANTE connue A LA
 *      COMPILATION (un litteral, ou une variable final dont la
 *      valeur est elle-meme connue a la compilation) - JAMAIS une
 *      variable ordinaire, meme si sa valeur ne changera jamais en
 *      pratique.
 *   3. 2 case ne peuvent JAMAIS partager la MEME valeur.
 */
public class Exercise05_SwitchStatementRulesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un essaie un switch sur un long, en pensant
    // "c'est juste un int plus grand, ca doit marcher pareil".
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA(long x) {
    //     switch (x) {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le meme genre de switch, mais cette fois sur un
    // String - un type de reference, et pourtant parfaitement
    // autorise.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB(String x) {
    //     switch (x) {
    //         case "hello":
    //             break;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un utilise une variable ORDINAIRE (pas
    // final) comme valeur de case, en pensant que Java "devinera"
    // sa valeur au moment de l'execution, comme pour une condition
    // if normale.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC(int x, int notConstant) {
    //     switch (x) {
    //         case notConstant:
    //             break;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : le meme genre d'idee, mais cette fois LIMIT est
    // declaree final, et sa valeur (5) est un litteral connu d'avance
    // - une VRAIE constante de compilation, meme si elle "a l'air"
    // d'etre une variable ordinaire au premier coup d'oeil.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD(int x) {
    //     final int LIMIT = 5;
    //     switch (x) {
    //         case LIMIT:
    //             break;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : par erreur de copier-coller, la meme valeur (1)
    // apparait 2 fois comme case dans le MEME switch.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useE(int x) {
    //     switch (x) {
    //         case 1:
    //             break;
    //         case 1:
    //             break;
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. long n'est PAS un type accepte par un
     *   switch statement classique - seuls byte/short/char/int (et
     *   leurs boites), String et les enum le sont. Erreur reelle
     *   (le message peut surprendre) : "patterns in switch statements
     *   are a preview feature and are disabled by default" - le
     *   compilateur, ne reconnaissant pas long comme un type de
     *   switch classique valide, suppose qu'on tente un pattern
     *   matching de switch (qui, LUI, accepterait bien plus de
     *   types), une fonctionnalite encore en PREVIEW en Java 17 et
     *   donc desactivee par defaut.
     *
     * Bloc B : COMPILE. String est un type de reference explicitement
     *   autorise pour un switch classique (depuis Java 7).
     *
     * Bloc C : NE COMPILE PAS. notConstant n'est PAS final : sa
     *   valeur n'est connue qu'A L'EXECUTION, jamais A LA
     *   COMPILATION - un case l'exige pourtant. Erreur : "constant
     *   expression required".
     *
     * Bloc D : COMPILE. LIMIT est final ET sa valeur (5) est un
     *   litteral connu d'avance : une VRAIE constante de compilation,
     *   malgre son apparence de variable.
     *
     * Bloc E : NE COMPILE PAS. 2 case ne peuvent jamais partager la
     *   meme valeur, meme par erreur de copier-coller. Erreur :
     *   "duplicate case label".
     */
}
