package ch6_classdesign.exercises;

/**
 * EXERCICE 13 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les autres quiz du chapitre (Exercise04/06/08/11). Pour chaque
 * bloc, c'est TOI la boite magique : lis d'abord l'histoire imagee,
 * essaie de repondre sur une feuille (compile / ne compile pas +
 * pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans Exercise04/06/08/11 - il recapitule les
 * autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise13_ClassDesignCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : Parent.m() est static. Child essaie de la "cacher"
    // avec une version D'INSTANCE (sans static) - en pensant que
    // static ou pas, tant que le nom et les parametres correspondent,
    // ca doit forcement marcher pareil.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class Parent {
    //     static void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     void m() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le probleme INVERSE du Bloc A - Parent.m() est cette
    // fois une methode d'instance normale, et c'est Child qui essaie
    // de la rendre static.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     static void m() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : Parent.secret() est private - donc INVISIBLE et
    // NON-HERITE par Child (voir aussi Exercise01, ou seuls
    // public/protected/package sont vraiment "recus"). Child declare
    // sa PROPRE methode secret(), avec un type de retour totalement
    // DIFFERENT (String au lieu de void) - ce qui serait normalement
    // interdit pour une VRAIE redefinition (voir Exercise08).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     private void secret() {
    //     }
    // }
    // static class Child extends Parent {
    //     private String secret() {
    //         return "child-secret";
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : cette fois, Parent.m() ET Child.m() sont TOUTES LES
    // DEUX static - un vrai cas de "cachage" (hiding) valide, sans
    // aucune ambiguite sur le "static ou pas".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     static void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     static void m() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : Parent est marquee final - "personne n'a le droit
    // d'en heriter, un point final". Child essaie quand meme de
    // l'etendre.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static final class Parent {
    // }
    // static class Child extends Parent {
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : cette fois, ce n'est pas la CLASSE qui est final
    // (Bloc E), mais UNE METHODE static precise (m()) - "cachee" par
    // Child, comme au Bloc D, mais Parent.m() est ici marquee final :
    // "cette version-la ne changera plus jamais, meme via un
    // cachage".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     static final void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     static void m() {
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Pour le "cachage" (hiding) d'une
     *   methode static, l'usage de static doit etre IDENTIQUE entre
     *   parent et enfant - une version d'instance ne peut pas
     *   "cacher" une version static. Erreur : "overridden method is
     *   static".
     *
     * Bloc B : NE COMPILE PAS, pour la meme regle appliquee dans
     *   l'autre sens : une version static ne peut pas non plus
     *   redefinir/cacher une version d'instance. Erreur : "overriding
     *   method is static".
     *
     * Bloc C : COMPILE. Une methode private n'est JAMAIS heritee :
     *   Child.secret() n'est donc PAS une redefinition de
     *   Parent.secret() (qu'elle ne "voit" meme pas), mais une toute
     *   NOUVELLE methode independante ("redeclaree", dans le
     *   vocabulaire de l'Exam Essentials) - aucune des regles de
     *   redefinition (Exercise08) ne s'applique donc ici, y compris
     *   le type de retour, qui peut etre completement different.
     *
     * Bloc D : COMPILE. static des 2 cotes : un cachage parfaitement
     *   valide.
     *
     * Bloc E : NE COMPILE PAS. Une classe marquee final ne peut
     *   JAMAIS etre etendue, meme une seule fois. Erreur : "cannot
     *   inherit from final Parent".
     *
     * Bloc F : NE COMPILE PAS. "final peut empecher aussi bien une
     *   redefinition qu'un cachage" (Exam Essentials) : meme un
     *   simple cachage de methode static (normalement valide, voir
     *   Bloc D) devient interdit des que la version du parent est
     *   final. Erreur : "overridden method is static,final".
     */
}
