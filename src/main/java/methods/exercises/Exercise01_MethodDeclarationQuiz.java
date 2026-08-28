package methods.exercises;

/**
 * EXERCICE 1 - Quiz "ca compile ou pas ?" sur la declaration des methodes (niveau : examen OCP)
 * =======================================================================================================
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
 * -- La "recette" complete d'une declaration de methode, dans l'ordre --
 *
 *   [modificateurs d'acces + static, DANS N'IMPORTE QUEL ORDRE entre
 *   eux] [type de retour, JAMAIS omis - void si rien a renvoyer]
 *   [nom] ([liste de parametres, separes par des virgules, AU PLUS
 *   UN varargs et TOUJOURS en DERNIER]) [throws Exception1, Exception2, ...]
 *   { corps }.
 */
public class Exercise01_MethodDeclarationQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un ecrit "static public" au lieu du "public
    // static" habituel, en pensant que l'ordre EXACT compte, comme
    // dans une phrase francaise ou l'adjectif doit etre a la bonne
    // place.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static public void m() {
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un veut a la fois dire "accessible a tout le
    // monde" (public) ET "accessible seulement ici" (private) sur la
    // MEME methode - 2 promesses qui se contredisent frontalement.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // public private void m() {
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un ecrit "static" 2 fois de suite, "pour
    // etre bien sur que ca soit vraiment static".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static static void m() {
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : quelqu'un ecrit une methode qui NE RENVOIE RIEN, en
    // pensant qu'on peut simplement OMETTRE le mot "void" plutot que
    // de l'ecrire explicitement - comme si "rien" n'avait pas besoin
    // d'etre nomme.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // m() {
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : une methode accepte un nombre variable d'entiers
    // (varargs), PUIS encore un parametre normal APRES - comme un bus
    // qui accepterait "autant de passagers que tu veux, ET ENSUITE
    // encore UNE personne precise apres tout le monde" : Java ne sait
    // plus ou s'arrete le groupe et ou commence la personne precise.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void m(int... nums, String s) {
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : une methode peut echouer de 2 facons DIFFERENTES
    // (IOException OU SQLException) - le programmeur liste les 2,
    // separees par une virgule, apres throws.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void m() throws java.io.IOException, java.sql.SQLException {
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. Les modificateurs d'acces et static peuvent
     *   apparaitre DANS N'IMPORTE QUEL ORDRE entre eux (meme si
     *   "public static" reste la convention la plus lue) - seul le
     *   TYPE DE RETOUR doit, lui, rester juste avant le nom de la
     *   methode.
     *
     * Bloc B : NE COMPILE PAS. public et private sont 2
     *   modificateurs d'acces DIFFERENTS et INCOMPATIBLES : une seule
     *   regle d'acces par methode. Erreur : "illegal combination of
     *   modifiers: public and private".
     *
     * Bloc C : NE COMPILE PAS. Un modificateur ne peut PAS etre
     *   repete, meme identique a lui-meme. Erreur : "repeated
     *   modifier".
     *
     * Bloc D : NE COMPILE PAS. Le type de retour n'est JAMAIS
     *   optionnel : "rien a renvoyer" doit etre ECRIT explicitement
     *   avec le mot-cle void, jamais sous-entendu. Erreur : "invalid
     *   method declaration; return type required".
     *
     * Bloc E : NE COMPILE PAS. Un parametre varargs doit TOUJOURS
     *   etre le DERNIER de la liste - au maximum un seul varargs,
     *   et rien apres lui. Erreur : "varargs parameter must be the
     *   last parameter".
     *
     * Bloc F : COMPILE. La liste apres throws accepte plusieurs
     *   types d'exception, separes par une simple virgule.
     */
}
