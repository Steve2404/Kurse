package ch3_makingdecisions.exercises;

/**
 * EXERCICE 13 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz du chapitre. Pour chaque bloc, c'est TOI la boite
 * magique : lis d'abord l'histoire imagee, essaie de repondre sur une
 * feuille (compile / ne compile pas + pourquoi, ou "que se passe-t-il
 * a l'execution ?"), PUIS decommente le bloc et essaie de COMPILER/
 * EXECUTER (mvn compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans les exercices precedents - il recapitule
 * les autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise13_MakingDecisionsCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un ecrit continue directement dans un switch,
    // sans AUCUNE boucle autour du tout - en pensant que continue,
    // comme break, "s'applique" naturellement a un switch.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA(int x) {
    //     switch (x) {
    //         case 1:
    //             continue;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un ecrit break dans un simple if, sans AUCUNE
    // boucle NI switch autour - en pensant que break "arrete le
    // bloc courant", quel qu'il soit.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB(boolean flag) {
    //     if (flag) {
    //         break;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : un switch statement SANS default, dont AUCUN case
    // ne correspond a x (x vaut 99, aucun case ne liste 99) - que se
    // passe-t-il ?
    //
    // Reponse : (celui-ci n'est pas un "compile ou pas" mais un "que
    // se passe-t-il ?" - ecris ta prediction avant de decommenter)
    // ------------------------------------------------------------------
    // static int useC() {
    //     int before = 5;
    //     switch (99) {
    //         case 1:
    //             before = 100;
    //             break;
    //     }
    //     return before;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : "outer:" etiquette une PREMIERE boucle (deja
    // terminee juste apres). Une SECONDE boucle, completement
    // separee, essaie quand meme d'utiliser "break outer;" - en
    // pensant qu'une etiquette "reste valable" pour tout le reste de
    // la methode, une fois declaree.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD() {
    //     outer:
    //     for (int i = 0; i < 3; i++) {
    //     }
    //     for (int j = 0; j < 3; j++) {
    //         break outer;
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. continue ne s'applique JAMAIS a un
     *   switch, uniquement a une boucle (voir Exercise12) - sans
     *   AUCUNE boucle du tout autour, il n'y a litteralement rien
     *   pour continue de "continuer". Erreur : "continue outside of
     *   loop".
     *
     * Bloc B : NE COMPILE PAS. break exige TOUJOURS une boucle OU un
     *   switch qui l'entoure - un simple if ne compte pas. Erreur :
     *   "break outside switch or loop".
     *
     * Bloc C : renvoie 5 (before, INCHANGE). Sans default et sans
     *   AUCUN case correspondant a 99, le switch ne fait
     *   litteralement RIEN du tout - ce n'est PAS une erreur, le
     *   programme continue simplement juste APRES le switch, comme
     *   si le switch entier n'avait jamais existe pour cette
     *   execution precise.
     *
     * Bloc D : NE COMPILE PAS. Une etiquette ne "vit" que pour LA
     *   boucle qu'elle precede IMMEDIATEMENT - une fois cette
     *   premiere boucle terminee (meme vide), "outer" n'existe plus
     *   du tout, meme si une AUTRE boucle, plus loin dans la meme
     *   methode, essaie de s'y referer. Erreur : "undefined label:
     *   outer".
     */
}
