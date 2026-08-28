package methods.exercises;

/**
 * EXERCICE 7 - Quiz "ca compile ou pas ?" : les variables "effectivement final" (niveau : examen OCP)
 * =============================================================================================================
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
 *   Une variable locale est "effectivement final" si elle N'EST
 *   JAMAIS REASSIGNEE apres sa toute premiere affectation - MEME sans
 *   jamais ecrire le mot-cle final devant. Le test rapide donne par
 *   le livre : ajoute mentalement "final" devant la declaration ; si
 *   le code compile TOUJOURS, elle etait deja effectivement final.
 *   Une lambda (ou une classe anonyme/locale, voir le chapitre
 *   "Beyond Classes") ne peut JAMAIS capturer une variable locale qui
 *   N'EST PAS final ou effectivement final.
 */
public class Exercise07_EffectivelyFinalQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : x recoit sa valeur UNE FOIS, et personne n'y touche
    // plus jamais ensuite - la lambda peut s'en souvenir sans
    // probleme.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static java.util.function.Supplier<Integer> m() {
    //     int x = 5;
    //     return () -> x;
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : x recoit d'abord 5, PUIS quelqu'un change d'avis et
    // lui redonne 10 - la lambda, elle, ne saurait plus QUELLE valeur
    // memoriser pour toujours.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static java.util.function.Supplier<Integer> m() {
    //     int x = 5;
    //     x = 10;
    //     return () -> x;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : cette fois, la lambda est creee EN PREMIER (avec x =
    // 5), mais x se fait quand meme modifier JUSTE APRES, avec "x +=
    // 1" - meme un "+=" compte comme une reaffectation, meme si elle
    // arrive APRES la creation de la lambda plutot qu'avant.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static java.util.function.Supplier<Integer> m() {
    //     int x = 5;
    //     java.util.function.Supplier<Integer> s = () -> x;
    //     x += 1;
    //     return s;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : une boucle for-each ("for (int i : liste)") cree, a
    // CHAQUE tour, une variable i TOUTE NEUVE (jamais la MEME
    // variable reutilisee) - chaque lambda capture donc SA PROPRE
    // copie, jamais partagee avec les autres tours.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static java.util.List<java.util.function.Supplier<Integer>> m() {
    //     java.util.List<java.util.function.Supplier<Integer>> result = new java.util.ArrayList<>();
    //     for (int i : java.util.List.of(1, 2, 3)) {
    //         result.add(() -> i);
    //     }
    //     return result;
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : cette fois, une boucle for CLASSIQUE ("for (int i =
    // 0; i < 3; i++)") - i est ICI la MEME variable tout du long,
    // REASSIGNEE a chaque tour par "i++" (voir aussi le chapitre
    // "Beyond Classes", Exercise11, pour ce meme piege applique aux
    // classes locales).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static java.util.List<java.util.function.Supplier<Integer>> m() {
    //     java.util.List<java.util.function.Supplier<Integer>> result = new java.util.ArrayList<>();
    //     for (int i = 0; i < 3; i++) {
    //         result.add(() -> i);
    //     }
    //     return result;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. x n'est JAMAIS reassignee apres sa premiere
     *   (et unique) affectation : effectivement final, meme sans le
     *   mot-cle.
     *
     * Bloc B : NE COMPILE PAS. x est reassignee (5 PUIS 10) : elle
     *   n'est PLUS effectivement final, meme si la lambda, elle,
     *   n'utilise "que" la valeur finale. Erreur : "local variables
     *   referenced from a lambda expression must be final or
     *   effectively final".
     *
     * Bloc C : NE COMPILE PAS, pour la MEME raison que le Bloc B -
     *   peu importe que la reaffectation ("x += 1") arrive AVANT ou
     *   APRES la creation de la lambda dans le CODE : ce qui compte,
     *   c'est qu'elle existe DU TOUT quelque part dans la methode.
     *
     * Bloc D : COMPILE. Chaque tour de la boucle for-each cree une
     *   variable i TOUTE NEUVE (jamais reassignee ENSUITE, une seule
     *   affectation par tour) : chaque lambda capture SA PROPRE
     *   copie, effectivement final chacune independamment.
     *
     * Bloc E : NE COMPILE PAS. Ici, i est LA MEME variable tout du
     *   long de la boucle, et "i++" la reassigne a CHAQUE tour :
     *   jamais effectivement final. Meme erreur que les Blocs B/C.
     */
}
