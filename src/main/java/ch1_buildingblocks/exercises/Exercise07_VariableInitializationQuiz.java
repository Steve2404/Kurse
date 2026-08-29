package ch1_buildingblocks.exercises;

/**
 * EXERCICE 7 - Quiz "ca compile ou pas ?" : une variable LOCALE, contrairement a un champ, DOIT etre initialisee A LA MAIN (niveau : examen OCP)
 * ============================================================================================================================================================
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
 *   Contrairement a un CHAMP D'INSTANCE (voir Exercise04, qui recoit
 *   TOUJOURS une valeur par defaut automatique), une variable LOCALE
 *   DOIT etre EXPLICITEMENT initialisee AVANT sa toute PREMIERE
 *   utilisation - et cette garantie doit etre VALABLE SUR TOUS LES
 *   CHEMINS POSSIBLES du code, pas juste "en pratique".
 */
public class Exercise07_VariableInitializationQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : une variable locale declaree, JAMAIS initialisee,
    // puis lue directement - en esperant que Java lui donne, comme
    // pour un champ, une valeur par defaut automatique.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     int x;
    //     System.out.println(x);
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : 2 variables locales, du MEME type, declarees ET
    // initialisees EN UNE SEULE instruction, separees par une
    // virgule.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     int a = 1, b = 2;
    //     System.out.println(a + b);
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : x n'est affecte QUE si flag vaut true - SANS AUCUN
    // "else" pour couvrir l'autre cas - avant d'etre lu juste apres,
    // sans jamais verifier si le if s'est vraiment execute.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC(boolean flag) {
    //     int x;
    //     if (flag) {
    //         x = 1;
    //     }
    //     System.out.println(x);
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Une variable locale n'a JAMAIS de
     *   valeur par defaut automatique - contrairement a un champ
     *   d'instance, elle DOIT etre initialisee explicitement avant
     *   sa premiere lecture. Erreur : "variable x might not have
     *   been initialized".
     *
     * Bloc B : COMPILE. Declarer et initialiser PLUSIEURS variables
     *   du MEME type dans UNE SEULE instruction (separees par une
     *   virgule) est parfaitement autorise.
     *
     * Bloc C : NE COMPILE PAS. Le if SANS else ne garantit PAS que x
     *   soit affecte sur TOUS les chemins possibles (le cas flag ==
     *   false ne l'affecte jamais) - exactement la meme analyse de
     *   "definite assignment" que pour les champs final (voir le
     *   chapitre "Class Design"), mais ici appliquee a une variable
     *   locale ORDINAIRE (meme pas besoin d'etre final pour que la
     *   regle s'applique). Erreur : "variable x might not have been
     *   initialized".
     */
}
