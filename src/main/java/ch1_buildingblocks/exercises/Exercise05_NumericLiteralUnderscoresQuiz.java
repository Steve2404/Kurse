package ch1_buildingblocks.exercises;

/**
 * EXERCICE 5 - Quiz "ca compile ou pas ?" : le _ dans un litteral numerique, JAMAIS au bord ni pres du point (niveau : examen OCP)
 * ==========================================================================================================================================
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
 * -- La regle a garder en tete pour tout le quiz --
 *
 *   Un _ dans un litteral numerique n'est autorise QU'ENTRE 2
 *   CHIFFRES - jamais au tout debut, jamais a la toute fin, et
 *   jamais collé au point decimal (ni juste avant, ni juste apres).
 */
public class Exercise05_NumericLiteralUnderscoresQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un million, ecrit avec des _ pour le rendre plus
    // lisible, exactement comme on separerait les milliers a la main
    // sur papier.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     int x = 1_000_000;
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un met le _ TOUT AU DEBUT du nombre, en
    // pensant "separer visuellement" le signe du reste.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     int x = _1000;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le probleme INVERSE - le _ est place TOUT A LA FIN
    // du nombre.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     int x = 1000_;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : un double avec un _ entre les milliers, mais LOIN du
    // point decimal (separe par 3 chiffres).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD() {
    //     double d = 1_000.0;
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : cette fois, le _ colle DIRECTEMENT au point decimal,
    // juste AVANT lui.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useE() {
    //     double d = 1_.0;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. Le _ est entoure de chiffres des 2 cotes, a
     *   chaque occurrence - parfaitement autorise.
     *
     * Bloc B : NE COMPILE PAS - mais PAS pour la raison qu'on pourrait
     *   croire ! _1000 est en realite un IDENTIFIANT VALIDE (les
     *   identifiants ont le droit de commencer par _, voir
     *   Exercise08) : Java ne le lit MEME PAS comme un nombre du
     *   tout, il cherche une VARIABLE nommee _1000, qui n'existe
     *   nulle part. Erreur : "cannot find symbol : variable _1000".
     *
     * Bloc C : NE COMPILE PAS. Ici, 1000_ EST bien lu comme un
     *   litteral numerique (il commence par un chiffre) - et un _ en
     *   toute derniere position y est explicitement interdit. Erreur
     *   : "illegal underscore".
     *
     * Bloc D : COMPILE. Le _ est loin du point decimal (separe par 3
     *   chiffres de chaque cote) - aucun probleme.
     *
     * Bloc E : NE COMPILE PAS. Le _ est COLLE directement au point
     *   decimal (juste avant lui) - interdit, meme si le _ est par
     *   ailleurs entoure de "quelque chose" des 2 cotes en apparence.
     *   Erreur : "illegal underscore".
     */
}
