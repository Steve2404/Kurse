package ch2_operators.exercises;

/**
 * EXERCICE 2 - Quiz "ca compile ou pas ?" : chaque operateur unaire a SON type prefer (niveau : examen OCP)
 * ===================================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main. Pour
 * chaque bloc, c'est TOI la boite magique : lis d'abord l'histoire
 * imagee, essaie de repondre sur une feuille (compile / ne compile
 * pas + pourquoi, ou "quel resultat ?"), PUIS decommente le bloc et
 * essaie de COMPILER/EXECUTER (mvn compile ou votre IDE) pour
 * verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   ! (negation logique) n'accepte QUE un boolean. ~ (complement
 *   binaire, bit par bit) et le - unaire n'acceptent QUE des types
 *   numeriques ENTIERS (byte/short/char/int/long - PAS float/double
 *   pour ~). Chaque operateur unaire est donc "specialise", jamais
 *   interchangeable avec un autre.
 */
public class Exercise02_UnaryOperatorTypesQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : quelqu'un applique ! (negation logique) directement
    // a un int, en pensant que "0 c'est comme false, et le reste
    // comme true" (comme dans d'autres langages) - Java, lui, ne
    // fait JAMAIS cette conversion automatique.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static void useA() {
    //     int x = 5;
    //     boolean b = !x;
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : le probleme INVERSE - quelqu'un applique ~
    // (complement binaire) a un boolean, en esperant "inverser" true
    // en false comme le ferait !.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useB() {
    //     boolean flag = true;
    //     int x = ~flag;
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un applique le - unaire (rendre negatif) a un
    // String contenant un chiffre ("5"), en esperant que Java le
    // convertisse tout seul en nombre avant de l'inverser.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void useC() {
    //     String s = "5";
    //     int x = -s;
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : ~ (complement binaire) applique correctement, cette
    // fois, a un VRAI int - quel resultat pour ~5 ?
    //
    // Reponse : (celui-ci n'est pas un "compile ou pas" mais un "quel
    // resultat ?" - ecris ta prediction avant de decommenter)
    // ------------------------------------------------------------------
    // static int useD() {
    //     return ~5;
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS. ! n'accepte QUE un boolean - jamais de
     *   conversion automatique depuis un int, contrairement a
     *   d'autres langages. Erreur : "bad operand type int for unary
     *   operator '!'".
     *
     * Bloc B : NE COMPILE PAS. ~ n'accepte QUE un type numerique
     *   entier - jamais un boolean, qui n'a d'ailleurs AUCUNE
     *   representation binaire "de nombre" en Java. Erreur : "bad
     *   operand type boolean for unary operator '~'".
     *
     * Bloc C : NE COMPILE PAS. Aucun operateur arithmetique
     *   n'accepte un String, meme un String "qui ressemble" a un
     *   nombre - il faudrait explicitement le convertir D'ABORD
     *   (Integer.parseInt(s)). Erreur : "bad operand type String for
     *   unary operator '-'".
     *
     * Bloc D : renvoie -6. La formule de ~ est TOUJOURS -(x + 1) :
     *   ~5 = -(5 + 1) = -6. C'est le complement a UN bit pres du
     *   complement a 2 utilise pour representer les nombres negatifs
     *   en binaire - inverser TOUS les bits d'un nombre revient
     *   exactement a calculer -(x + 1).
     */
}
