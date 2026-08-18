package exceptions.exercises;

/**
 * EXERCICE 3 - Quiz "ca compile ou pas ?" sur l'ordre des catch et le mot-cle throws (niveau : examen OCP)
 * ======================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * collections.exercises.Exercise10_WildcardsQuiz et
 * lambdas.exercises.Exercise09_FunctionalInterfaceQuiz. Pour chaque
 * bloc, c'est TOI la boite magique : lis d'abord l'histoire imagee,
 * essaie de repondre sur une feuille (compile / ne compile pas +
 * pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire (ou corrigez-le) avant de passer au
 * suivant, sinon les erreurs de compilation des blocs precedents
 * empecheront de tester les suivants.
 *
 * -- Les 3 regles a garder en tete pour tout le quiz --
 *
 *   1. Dans une CHAINE de catch (blocs separes), aucun type ne doit
 *      apparaitre APRES un de ses SUPERTYPES deja catche plus haut -
 *      ce bloc plus specifique deviendrait INATTEIGNABLE (le
 *      supertype l'aurait deja tout attrape avant).
 *   2. Dans un MULTI-catch (catch (A | B e)), aucune des alternatives
 *      ne doit etre un sous-type d'une autre - ce serait redondant
 *      (la plus generale suffirait deja a tout attraper).
 *   3. En surchargeant (@Override) une methode qui declare une
 *      exception CHECKED, la methode enfant peut declarer LA MEME
 *      exception, une de ses SOUS-CLASSES, ou AUCUNE exception - mais
 *      jamais une exception PLUS GENERALE que celle du parent (ca
 *      romprait la promesse du parent envers ses appelants).
 */
public class Exercise03_CatchOrderAndOverridingQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un videur qui verifie d'abord "es-tu un etre humain
    // fatiguant en general ?" (Exception, tres large), et SEULEMENT
    // APRES, plus loin dans sa liste, "es-tu precisement un probleme
    // de fichier illisible ?" (IOException, un cas bien plus precis).
    // Le premier controle, tres large, a deja tout intercepte avant
    // que le second ne soit meme consulte.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // void blocA() {
    //     try {
    //         throw new java.io.IOException();
    //     } catch (Exception e) {
    //         System.out.println("generic");
    //     } catch (java.io.IOException e) {
    //         System.out.println("specific");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : un panneau a l'entree qui dit "acces refuse aux
    // clients SANS ticket, OU (de maniere totalement redondante) aux
    // clients avec un ticket ROSE (les tickets roses etant DEJA une
    // sorte de ticket)" - la deuxieme moitie de la regle ne sert
    // strictement a rien, elle est deja entierement couverte par la
    // premiere.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void blocB() {
    //     try {
    //         throw new NumberFormatException();
    //     } catch (NumberFormatException | RuntimeException e) {
    //         System.out.println("caught");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : le meme panneau, mais cette fois avec 2 regles
    // VRAIMENT independantes ("ticket perdu" OU "chaussures
    // interdites") - aucune des deux ne rend l'autre inutile.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void blocC() {
    //     try {
    //         throw new ArithmeticException();
    //     } catch (ArithmeticException | NullPointerException e) {
    //         System.out.println("caught");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : un parent promet a tous ses invites "je ne risque de
    // vous deranger QUE pour un probleme de fichier illisible
    // (IOException), rien de plus grave". L'enfant qui herite de cette
    // promesse et la surcharge essaie d'elargir cette promesse a
    // "n'importe quel probleme possible" (Exception, bien plus large)
    // - ce qui trahit la promesse originale faite par le parent a
    // TOUS ceux qui faisaient deja confiance a Parent.m() sans se
    // soucier d'Exception en general.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     void m() throws java.io.IOException {}
    // }
    // static class Child extends Parent {
    //     @Override
    //     void m() throws Exception {}
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : le meme parent, mais cette fois l'enfant RESTREINT sa
    // propre promesse a un cas ENCORE PLUS precis (FileNotFoundException
    // est un SOUS-TYPE de IOException) - ca ne trahit jamais personne,
    // au contraire, l'enfant promet MOINS de risques que son parent.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Parent {
    //     void m() throws java.io.IOException {}
    // }
    // static class Child extends Parent {
    //     @Override
    //     void m() throws java.io.FileNotFoundException {}
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : quelqu'un ecrit un bloc "essaie de faire ceci", mais
    // n'ajoute ni "si ca rate, fais cela" (catch), ni "dans tous les
    // cas, fais toujours ceci a la fin" (finally) - le bloc try se
    // retrouve seul, sans aucune des 2 suites qu'il exige normalement.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void blocF() {
    //     try {
    //         System.out.println("no catch no finally");
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. IOException a deja ete "consommee"
     *   par le premier catch (Exception e), qui est un SUPERTYPE
     *   d'IOException : le second catch (IOException e), plus
     *   specifique mais place APRES, est INATTEIGNABLE. Regle :
     *   toujours ordonner les catch du plus specifique au plus
     *   general.
     *
     * Bloc B : NE COMPILE PAS. NumberFormatException est une
     *   sous-classe de RuntimeException : les avoir toutes les 2 dans
     *   le MEME multi-catch est redondant et explicitement interdit
     *   par le compilateur ("Alternatives in a multi-catch statement
     *   cannot be related by subclassing").
     *
     * Bloc C : COMPILE. ArithmeticException et NullPointerException
     *   ne sont ni l'une ni l'autre un sous-type de l'autre (2
     *   branches independantes de RuntimeException) : multi-catch
     *   parfaitement valide.
     *
     * Bloc D : NE COMPILE PAS. Child.m() ELARGIT la promesse
     *   d'exception du parent (Exception est plus general
     *   qu'IOException) : interdit lors d'une surcharge - le
     *   compilateur protege tout code qui appelait deja Parent.m()
     *   en ne gerant qu'IOException.
     *
     * Bloc E : COMPILE. Child.m() RETRECIT la promesse a un sous-type
     *   plus precis (FileNotFoundException extends IOException) :
     *   toujours autorise, ca ne trahit jamais l'appelant.
     *
     * Bloc F : NE COMPILE PAS. Un bloc try DOIT etre suivi d'au moins
     *   un catch ou d'un finally (sauf s'il s'agit d'un try-with-
     *   resources, qui lui peut se suffire a lui-meme - voir
     *   Exercise04).
     */
}
