package ch3_makingdecisions.exercises;

/**
 * EXERCICE 3 - Quiz "ca compile ou pas ?" sur la portee (flow scoping) du pattern variable (niveau : examen OCP)
 * =======================================================================================================================
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
 *   Le pattern variable (s dans "instanceof String s") n'est
 *   utilisable QUE la ou le compilateur peut PROUVER, par la LOGIQUE
 *   du code (pas juste par les accolades), que le test a
 *   necessairement reussi a cet endroit precis.
 */
public class Exercise03_FlowScopingQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un utilise s JUSTE APRES un if classique
    // (sans else, sans return anticipe) - en pensant que, puisque le
    // if a "deja verifie" le type plus haut dans le fichier, s reste
    // valable pour le reste de la methode.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA(Object obj) {
    //     if (obj instanceof String s) {
    //     }
    //     System.out.println(s);
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : quelqu'un remplace le && de l'Exercise02 (TODO 2)
    // par un || (OU), en pensant que la difference n'est qu'une
    // question de LOGIQUE metier, pas de compilation.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB(Object obj) {
    //     if (obj instanceof String s || s.length() > 3) {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un utilise s dans la branche ELSE d'un if
    // NON negatif ("if (obj instanceof String s) ... else ...") - en
    // pensant que s "existe" partout des qu'il a ete declare une
    // fois, meme dans la branche ou le test a justement ECHOUE.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC(Object obj) {
    //     if (obj instanceof String s) {
    //     } else {
    //         System.out.println(s);
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : cette fois, le if est NEGATIF ("if (!(obj instanceof
    // String s))"), et s est utilise dans SA branche ELSE - c'est
    // exactement l'inverse du Bloc C : ici, la branche else
    // correspond au cas ou la negation est FAUSSE, c'est-a-dire ou le
    // test ORIGINAL (sans le !) a REUSSI.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useD(Object obj) {
    //     if (!(obj instanceof String s)) {
    //     } else {
    //         System.out.println(s);
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : comme au TODO 1 de l'Exercise02, mais SANS
    // accolades du tout autour du return - quelqu'un se demande si
    // les accolades etaient "la vraie raison" pour laquelle ca
    // marchait.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static String useE(Object obj) {
    //     if (!(obj instanceof String s)) return "no";
    //     return "yes: " + s;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Apres un if CLASSIQUE (sans else, sans
     *   sortie anticipee dans la branche negative), le compilateur ne
     *   peut PAS prouver que obj etait VRAIMENT une String a cet
     *   endroit du code (le if aurait tres bien pu ne PAS s'executer)
     *   - s sort donc de portee des la fin des accolades du if.
     *   Erreur : "cannot find symbol : variable s".
     *
     * Bloc B : NE COMPILE PAS. Avec ||, si le cote GAUCHE est faux,
     *   Java evalue quand meme le cote DROIT (contrairement a && qui,
     *   lui, arrete tout des que le cote gauche est faux) - le
     *   compilateur ne peut donc PAS garantir que s est deja affecte
     *   au moment d'atteindre s.length().
     *
     * Bloc C : NE COMPILE PAS. La branche else d'un if NON negatif
     *   correspond exactement au cas ou le test a ECHOUE (obj n'est
     *   PAS une String) : s n'y a jamais existe.
     *
     * Bloc D : COMPILE. La branche else d'un if NEGATIF correspond au
     *   cas ou la negation est fausse, c'est-a-dire ou "obj
     *   instanceof String s" (sans le !) etait VRAI - s y est donc
     *   parfaitement valide.
     *
     * Bloc E : COMPILE. Les accolades n'ont jamais ete "la vraie
     *   raison" : ce qui compte, c'est que la branche prise quand le
     *   test echoue QUITTE definitivement la methode (via return,
     *   avec ou sans accolades) - la ligne suivante n'est donc
     *   atteignable QUE si le test original a reussi.
     */
}
