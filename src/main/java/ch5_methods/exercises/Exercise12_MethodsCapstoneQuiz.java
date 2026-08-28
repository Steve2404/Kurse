package ch5_methods.exercises;

/**
 * EXERCICE 12 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz du chapitre. Pour chaque bloc, c'est TOI la boite
 * magique : lis d'abord l'histoire imagee, essaie de repondre sur une
 * feuille (compile / ne compile pas + pourquoi), PUIS decommente le
 * bloc et essaie de COMPILER (mvn compile ou votre IDE) pour
 * verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans Exercise01/02/04/07 - il recapitule les
 * autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise12_MethodsCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un pense pouvoir "surcharger" m(int) juste en
    // changeant le type de RETOUR (int -> long), sans toucher au nom
    // ni a la liste de parametres - en oubliant que la "signature"
    // d'une methode, au sens strict, ne compte JAMAIS le type de
    // retour.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class A {
    //     int m(int x) {
    //         return x;
    //     }
    //     long m(int x) {
    //         return x;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : ayant appris (Exercise01, Bloc A) que l'ordre ENTRE
    // les modificateurs (public/static) est libre, quelqu'un en
    // deduit, a tort, que le TYPE DE RETOUR peut LUI AUSSI se
    // deplacer librement parmi eux.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class B {
    //     void public m() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : cette fois, 2 methodes add() portent bien des types
    // de PARAMETRES differents (int vs double) - une VRAIE difference
    // de signature, pas juste le type de retour comme au Bloc A.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class C {
    //     static int add(int a, int b) {
    //         return a + b;
    //     }
    //     static double add(double a, double b) {
    //         return a + b;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : sum(int... nums) est appelee SANS AUCUN argument du
    // tout - comme demander "additionne-moi ces nombres" en ne
    // montrant AUCUN nombre : varargs accepte ce cas comme un tableau
    // VIDE, pas comme une erreur.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class D {
    //     static int sum(int... nums) {
    //         int total = 0;
    //         for (int n : nums) {
    //             total += n;
    //         }
    //         return total;
    //     }
    //     static void useD() {
    //         int result = sum();
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. La "signature" d'une methode, c'est
     *   SON NOM + SA LISTE DE PARAMETRES - jamais le type de retour.
     *   int m(int) et long m(int) ont EXACTEMENT la meme signature :
     *   pour le compilateur, c'est une REDECLARATION de la MEME
     *   methode, pas une surcharge. Erreur : "method m(int) is
     *   already defined in class A".
     *
     * Bloc B : NE COMPILE PAS. Contrairement aux modificateurs entre
     *   eux (public/static, Exercise01 Bloc A), le TYPE DE RETOUR
     *   doit TOUJOURS rester juste avant le nom de la methode, jamais
     *   avant un modificateur - "void public" n'est tout simplement
     *   pas une syntaxe reconnue.
     *
     * Bloc C : COMPILE. int et double sont des types de PARAMETRES
     *   differents : une VRAIE difference de signature, donc une
     *   surcharge parfaitement valide.
     *
     * Bloc D : COMPILE. Un parametre varargs accepte ZERO argument
     *   aussi bien qu'un ou plusieurs - sum() recoit alors un tableau
     *   vide (nums.length == 0), et la boucle ne fait simplement rien
     *   tourner du tout.
     */
}
