package ch3_makingdecisions.exercises;

/**
 * EXERCICE 7 - Quiz "ca compile ou pas ?" sur les regles du switch expression (niveau : examen OCP)
 * ===========================================================================================================
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
 *   1. Un switch expression DOIT couvrir TOUTES les valeurs
 *      possibles (avec un default, SAUF pour un enum dont TOUTES
 *      les constantes sont explicitement listees - la, le
 *      compilateur sait deja qu'il n'y a rien d'autre).
 *   2. C'est une EXPRESSION : le point-virgule final, apres
 *      l'accolade fermante, est OBLIGATOIRE des qu'on l'utilise
 *      dans une instruction (return, affectation...).
 *   3. Impossible de melanger la syntaxe fleche (->) et la syntaxe
 *      deux-points (:) dans le MEME switch.
 *   4. Un bloc {} apres une fleche DOIT se terminer par yield
 *      (ou throw) - jamais "tomber dans le vide" sans fournir de
 *      valeur.
 */
public class Exercise07_SwitchExpressionRulesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un switch expression sur un int qui liste 2 cas
    // (1 et 2), SANS default - "de toute facon, x ne vaudra jamais
    // autre chose", pense le programmeur.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static String useA(int x) {
    //     return switch (x) {
    //         case 1 -> "one";
    //         case 2 -> "two";
    //     };
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le meme switch, avec un default cette fois, mais le
    // point-virgule final (apres l'accolade fermante du switch) a ete
    // oublie.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static String useB(int x) {
    //     return switch (x) {
    //         case 1 -> "one";
    //         default -> "other";
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un ecrit la plupart des case avec des
    // fleches, mais AJOUTE un unique case a l'ancienne (avec ":" et
    // yield) au milieu, en pensant que yield "compense" le manque de
    // fleche.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static String useC(int x) {
    //     return switch (x) {
    //         case 1 -> "one";
    //         case 2: yield "two";
    //         default -> "other";
    //     };
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : un bloc {} apres une fleche declare bien une
    // variable, mais "oublie" le yield final - comme une methode a
    // qui il manquerait son return.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static String useD(int x) {
    //     return switch (x) {
    //         case 1 -> {
    //             String s = "one";
    //         }
    //         default -> "other";
    //     };
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : un switch expression sur un enum (3 constantes),
    // avec TOUTES les 3 explicitement listees, mais SANS default du
    // tout.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // enum Color { RED, GREEN, BLUE }
    // static String useE(Color c) {
    //     return switch (c) {
    //         case RED -> "r";
    //         case GREEN -> "g";
    //         case BLUE -> "b";
    //     };
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Un int a bien plus de 2 valeurs
     *   possibles (tout l'intervalle des int) - sans default, le
     *   compilateur ne peut jamais etre CERTAIN que toutes les
     *   valeurs sont couvertes. Erreur : "the switch expression does
     *   not cover all possible input values".
     *
     * Bloc B : NE COMPILE PAS. Utilise dans un return, le switch
     *   expression est une EXPRESSION comme une autre : elle exige le
     *   meme point-virgule final qu'importe quel autre return. Erreur
     *   : "';' expected".
     *
     * Bloc C : NE COMPILE PAS. Impossible de melanger les 2 syntaxes
     *   dans le MEME switch, meme en ajoutant yield du cote ":".
     *   Erreur : "different case kinds used in the switch".
     *
     * Bloc D : NE COMPILE PAS. Un bloc {} apres une fleche DOIT se
     *   terminer par yield (ou throw) - "tomber dans le vide" sans
     *   fournir de valeur n'est jamais permis dans un switch
     *   EXPRESSION (contrairement a un switch statement classique).
     *   Erreur : "switch rule completes without providing a value".
     *
     * Bloc E : COMPILE. Un enum ferme la liste des valeurs possibles
     *   (voir le chapitre "Beyond Classes") : lister EXPLICITEMENT
     *   TOUTES ses constantes suffit a prouver l'exhaustivite, aucun
     *   default n'est necessaire.
     */
}
