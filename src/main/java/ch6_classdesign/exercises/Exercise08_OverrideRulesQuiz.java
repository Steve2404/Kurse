package ch6_classdesign.exercises;

/**
 * EXERCICE 8 - Quiz "ca compile ou pas ?" sur les regles de redefinition (override) (niveau : examen OCP)
 * =================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * Exercise04_ConstructorRulesQuiz et Exercise06_FinalFieldsQuiz. Pour
 * chaque bloc, c'est TOI la boite magique : lis d'abord l'histoire
 * imagee, essaie de repondre sur une feuille (compile / ne compile
 * pas + pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les 4 regles a garder en tete pour tout le quiz --
 *
 *   1. La signature (nom + types de parametres) doit etre
 *      IDENTIQUE.
 *   2. L'acces ne peut JAMAIS etre RETRECI (public -> protected est
 *      interdit, mais protected -> public est autorise : elargir
 *      l'acces est permis, le retrecir non).
 *   3. Aucune exception CHECKED nouvelle ou plus large ne peut etre
 *      declaree (une exception checked PLUS PRECISE, sous-classe de
 *      celle du parent, reste autorisee) - les exceptions UNCHECKED,
 *      elles, echappent COMPLETEMENT a cette regle.
 *   4. Le type de retour doit etre le MEME, ou un type COVARIANT (un
 *      SOUS-TYPE du type de retour du parent).
 *
 * Une methode marquee final ne peut JAMAIS etre redefinie NI cachee.
 */
public class Exercise08_OverrideRulesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : Parent.m() est accessible a TOUT LE MONDE (public).
    // Child, en la redefinissant, decide de la rendre accessible
    // SEULEMENT au paquet et aux sous-classes (protected) - comme si
    // on retirait des droits que les appelants avaient DEJA.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class Parent {
    //     public void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     protected void m() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : Parent.m() declare pouvoir lancer une
    // IOException. Child, en la redefinissant, declare pouvoir lancer
    // la bien plus large Exception (qui couvre BIEN PLUS de cas que
    // IOException).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // import java.io.IOException;
    // static class Parent {
    //     void m() throws IOException {
    //     }
    // }
    // static class Child extends Parent {
    //     void m() throws Exception {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le meme Parent.m() (throws IOException), mais cette
    // fois Child declare FileNotFoundException - qui, elle, EST DEJA
    // une IOException (une sous-classe plus precise), rien de "plus
    // large" n'est ajoute.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // import java.io.IOException;
    // import java.io.FileNotFoundException;
    // static class Parent {
    //     void m() throws IOException {
    //     }
    // }
    // static class Child extends Parent {
    //     void m() throws FileNotFoundException {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : Parent.m() ne declare AUCUNE exception. Child, en la
    // redefinissant, declare pouvoir lancer RuntimeException - une
    // exception UNCHECKED, jamais soumise a la regle des exceptions
    // checked.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     void m() throws RuntimeException {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : Parent.reproduce() rend un Animal generique. Child,
    // en la redefinissant, rend un Dog - PLUS precis qu'Animal, mais
    // un Dog EST TOUJOURS un Animal (covariance : "au moins aussi
    // specifique", jamais "moins specifique").
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Animal {
    // }
    // static class Dog extends Animal {
    // }
    // static class Parent {
    //     Animal reproduce() {
    //         return new Animal();
    //     }
    // }
    // static class Child extends Parent {
    //     Dog reproduce() {
    //         return new Dog();
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : Parent.describe() rend un String. Child, en la
    // redefinissant, rend un int - un type totalement SANS RAPPORT
    // avec String (ni covariant, ni compatible d'aucune facon).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     String describe() {
    //         return "parent";
    //     }
    // }
    // static class Child extends Parent {
    //     int describe() {
    //         return 0;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc G
    //
    // Histoire : Parent.m() est marquee final - une facon de dire
    // "cette version-la ne changera JAMAIS, quoi qu'il arrive dans
    // les sous-classes". Child essaie quand meme de la redefinir.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     final void m() {
    //     }
    // }
    // static class Child extends Parent {
    //     void m() {
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. protected est MOINS accessible que
     *   public : retrecir l'acces d'une methode redefinie est
     *   interdit. Erreur : "attempting to assign weaker access
     *   privileges; was public".
     *
     * Bloc B : NE COMPILE PAS. Exception est PLUS LARGE que
     *   IOException (elle couvre bien plus de cas que ce que le
     *   parent promettait) : une methode redefinie ne peut jamais
     *   promettre MOINS de securite que l'originale. Erreur :
     *   "overridden method does not throw Exception".
     *
     * Bloc C : COMPILE. FileNotFoundException est une SOUS-CLASSE de
     *   IOException : c'est une promesse PLUS PRECISE (donc plus
     *   sure), jamais plus large - parfaitement autorise.
     *
     * Bloc D : COMPILE. RuntimeException est UNCHECKED : la regle des
     *   exceptions "plus larges interdites" ne s'applique QU'AUX
     *   exceptions checked, jamais aux unchecked.
     *
     * Bloc E : COMPILE. Dog est un SOUS-TYPE d'Animal (covariance) :
     *   le type de retour peut devenir PLUS specifique dans la
     *   redefinition, jamais moins.
     *
     * Bloc F : NE COMPILE PAS. int n'a AUCUN rapport de sous-typage
     *   avec String (ni covariant, ni identique) : le type de retour
     *   d'une redefinition doit rester COMPATIBLE. Erreur : "return
     *   type int is not compatible with String".
     *
     * Bloc G : NE COMPILE PAS. Une methode final ne peut JAMAIS etre
     *   redefinie (ni cachee, si elle etait static) : c'est
     *   precisement le but du mot-cle final ici. Erreur : "overridden
     *   method is final".
     */
}
