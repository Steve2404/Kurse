package ch6_classdesign.exercises;

/**
 * EXERCICE 4 - Quiz "ca compile ou pas ?" sur les regles de constructeurs (niveau : examen OCP)
 * =====================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * les quiz du chapitre precedent (Exercise07/14 de beyondclasses).
 * Pour chaque bloc, c'est TOI la boite magique : lis d'abord
 * l'histoire imagee, essaie de repondre sur une feuille (compile / ne
 * compile pas + pourquoi), PUIS decommente le bloc et essaie de
 * COMPILER (mvn compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. Si un constructeur n'ecrit NI this(...) NI super(...) en
 *      premiere ligne, le compilateur insere TOUT SEUL un super()
 *      SANS ARGUMENT - mais UNIQUEMENT si le parent possede
 *      REELLEMENT un tel constructeur.
 *   2. this(...) et super(...) doivent TOUJOURS etre la toute
 *      PREMIERE instruction du constructeur, jamais une ligne plus
 *      loin.
 *   3. Une chaine de this(...) qui finit par se rappeler ELLE-MEME
 *      (un cycle) est detectee et REFUSEE des la COMPILATION, pas
 *      seulement a l'execution.
 */
public class Exercise04_ConstructorRulesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : Parent n'a QU'UNE facon de naitre : en fournissant un
    // entier (Parent(int x)). Child, lui, ne se donne meme pas la
    // peine d'ecrire un constructeur, en esperant que "ca marchera
    // tout seul comme d'habitude".
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class Parent {
    //     Parent(int x) {
    //     }
    // }
    // static class Child extends Parent {
    //     // aucun constructeur ecrit du tout
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le meme Parent, mais cette fois Child ECRIT
    // explicitement son propre constructeur, et lui fait fournir la
    // valeur que Parent exige.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     Parent(int x) {
    //     }
    // }
    // static class Child extends Parent {
    //     Child() {
    //         super(42);
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un veut d'abord afficher un message de debug
    // "avant" de deleguer a l'autre constructeur, en pensant que
    // l'ordre des lignes n'a pas d'importance tant que this(...) est
    // ecrit QUELQUE PART dans le constructeur.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     Foo() {
    //         System.out.println("avant");
    //         this(1);
    //     }
    //     Foo(int x) {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : Foo() delegue a Foo(int), qui elle-meme, par erreur
    // de copier-coller, redelegue a Foo() - une boucle sans fin, un
    // peu comme 2 personnes qui se renvoient eternellement la balle
    // sans que personne ne la garde jamais.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     Foo() {
    //         this(1);
    //     }
    //     Foo(int x) {
    //         this();
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : Foo() delegue bien a Foo(int), mais Foo(int), lui,
    // ne redelegue nulle part - la chaine s'arrete proprement, sans
    // jamais revenir sur ses pas.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     Foo() {
    //         this(0);
    //     }
    //     Foo(int x) {
    //         // ne rappelle PAS this() : pas de cycle
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Aucun constructeur ecrit dans Child :
     *   le compilateur essaie d'inserer un super() sans argument tout
     *   seul, mais Parent n'a AUCUN constructeur sans argument (juste
     *   Parent(int x)) - impossible de deviner quoi que ce soit.
     *   Erreur reelle du compilateur : "constructor Parent in class
     *   Parent cannot be applied to given types" (le super() implicite
     *   ne "matche" aucun constructeur reel de Parent).
     *
     * Bloc B : COMPILE. Child fournit maintenant explicitement
     *   super(42), qui correspond EXACTEMENT au seul constructeur que
     *   Parent possede.
     *
     * Bloc C : NE COMPILE PAS. this(1) N'EST PAS en premiere ligne
     *   (le println() passe avant) - peu importe qu'il soit "quelque
     *   part" dans le constructeur, il DOIT etre la toute premiere
     *   instruction. Erreur : "call to this must be first statement
     *   in constructor".
     *
     * Bloc D : NE COMPILE PAS. Foo() appelle this(1), qui appelle
     *   this(), qui rappelle this(1)... un CYCLE. Contrairement a une
     *   recursion infinie "normale" entre 2 methodes classiques (qui,
     *   elle, compile tres bien et ne plante qu'A L'EXECUTION avec un
     *   StackOverflowError), Java DETECTE ce cas PRECIS des la
     *   compilation et le refuse directement. Erreur : "recursive
     *   constructor invocation".
     *
     * Bloc E : COMPILE. Foo() delegue a Foo(int), qui NE redelegue
     *   nulle part : la chaine se termine proprement, aucun cycle.
     */
}
