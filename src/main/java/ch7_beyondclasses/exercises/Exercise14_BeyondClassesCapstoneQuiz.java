package ch7_beyondclasses.exercises;

/**
 * EXERCICE 14 (CAPSTONE) - Quiz "ca compile ou pas ?" : recapitulatif des pieges du chapitre (niveau : examen OCP)
 * ========================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * Exercise07_SealedModifiersQuiz. Pour chaque bloc, c'est TOI la
 * boite magique : lis d'abord l'histoire imagee, essaie de repondre
 * sur une feuille (compile / ne compile pas + pourquoi), PUIS
 * decommente le bloc et essaie de COMPILER (mvn compile ou votre IDE)
 * pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * Ce quiz couvre volontairement des pieges DIFFERENTS de ceux deja
 * testes en profondeur dans Exercise07 (sealed) - il recapitule les
 * autres phrases-cles du Summary/Exam Essentials du chapitre.
 */
public class Exercise14_BeyondClassesCapstoneQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un declare un record Foo(int x), puis se dit
    // "et si j'ajoutais AUSSI un champ y a cote, comme dans une classe
    // normale ?" - comme si le record n'etait qu'une classe normale
    // avec des accesseurs gratuits en bonus.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // record Foo(int x) {
    //     private int y;
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le meme Foo, mais cette fois le champ ajoute est
    // STATIC (un compteur d'instances partage par TOUT le monde, pas
    // une donnee propre a CHAQUE Foo).
    //
    // Reponse :
    // ------------------------------------------------------------------
    // record Foo(int x) {
    //     static int counter = 0;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un declare une classe locale (a l'interieur
    // d'une methode), et essaie de la marquer "public", comme il en a
    // l'habitude pour les classes top-level - "pour bien montrer
    // qu'elle est importante", pense-t-il.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void m() {
    //     public class Local {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : 2 interfaces independantes, Runnable1 et Runnable2.
    // Quelqu'un veut une classe anonyme qui implemente LES DEUX EN
    // MEME TEMPS, en les separant simplement par une virgule apres
    // "new" - comme on le ferait pour "class Both implements
    // Runnable1, Runnable2".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Runnable1 {
    //     void run1();
    // }
    // interface Runnable2 {
    //     void run2();
    // }
    // static Runnable1 buildBoth() {
    //     return new Runnable1, Runnable2() {
    //         public void run1() {
    //         }
    //         public void run2() {
    //         }
    //     };
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : quelqu'un ecrit le constructeur d'un enum en le
    // marquant explicitement "public", en pensant "je veux que
    // n'importe qui puisse creer une nouvelle Status depuis
    // l'exterieur" - sans se rappeler qu'un enum a une liste de
    // valeurs FIGEE une fois pour toutes.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // enum Status {
    //     ACTIVE(1);
    //     private final int code;
    //     public Status(int code) {
    //         this.code = code;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : quelqu'un declare une methode PRIVATE dans une
    // interface, mais SANS lui donner de corps - comme s'il pensait
    // que "private", comme "abstract", suffisait a dire "chaque
    // implementation la remplira elle-meme". Sauf qu'une methode
    // private est invisible depuis l'exterieur de l'interface :
    // aucune classe qui l'implemente ne pourrait donc JAMAIS la
    // fournir.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // interface Helper {
    //     private int bonus(int n);
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. Un record ne peut PAS declarer de champ
     *   d'INSTANCE en dehors de ses composants (x est le seul champ
     *   d'instance autorise ici) - ca casserait l'immutabilite meme
     *   du record. Erreur : "field declaration must be static" (avec
     *   la suggestion "consider replacing field with record
     *   component").
     *
     * Bloc B : COMPILE. Un champ STATIC n'appartient a AUCUNE instance
     *   particuliere (il est partage par tous les Foo, un peu comme
     *   une constante d'interface) : il ne menace pas l'immutabilite
     *   d'UNE instance donnee, donc c'est autorise.
     *
     * Bloc C : NE COMPILE PAS. Les classes locales (et anonymes, voir
     *   l'Exam Essentials du chapitre) ne peuvent JAMAIS etre
     *   declarees avec un modificateur d'acces (public, private,
     *   protected) - elles ne sont deja visibles QUE dans la methode
     *   qui les entoure, un modificateur d'acces n'aurait aucun sens.
     *
     * Bloc D : NE COMPILE PAS. Il n'existe simplement AUCUNE syntaxe
     *   Java pour faire "new TypeA, TypeB() { ... }" - une classe
     *   anonyme peut implementer EXACTEMENT une interface, OU etendre
     *   EXACTEMENT une classe, jamais 2 types a la fois. Pour
     *   combiner Runnable1 ET Runnable2, il faudrait d'abord creer
     *   une VRAIE classe (locale, imbriquee ou top-level) nommee qui
     *   implemente les 2.
     *
     * Bloc E : NE COMPILE PAS. Le constructeur d'un enum est TOUJOURS
     *   implicitement private (meme sans l'ecrire) - la liste des
     *   valeurs possibles est figee dans le fichier de l'enum
     *   lui-meme, jamais extensible depuis l'exterieur : ecrire
     *   "public" (ou meme "protected") devant est explicitement
     *   interdit. Erreur : "modifier public not allowed here".
     *
     * Bloc F : NE COMPILE PAS. Une methode private d'interface DOIT
     *   avoir un corps (contrairement a une methode abstract) :
     *   comme elle est invisible depuis l'exterieur, seule
     *   l'interface elle-meme peut jamais l'appeler - une methode
     *   private SANS corps ne pourrait donc jamais etre executee par
     *   personne. Erreur : "missing method body, or declare
     *   abstract".
     */
}
