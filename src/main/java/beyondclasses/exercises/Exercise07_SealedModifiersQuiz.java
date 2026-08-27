package beyondclasses.exercises;

/**
 * EXERCICE 7 - Quiz "ca compile ou pas ?" sur les modificateurs des sealed classes (niveau : examen OCP)
 * =============================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * Exercise09_FunctionalInterfaceQuiz du package lambdas. Pour chaque
 * bloc, c'est TOI la boite magique : lis d'abord l'histoire imagee,
 * essaie de repondre sur une feuille (compile / ne compile pas +
 * pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- Les regles a garder en tete pour tout le quiz --
 *
 *   1. Un sealed class/interface DOIT lister ses sous-types permis,
 *      soit explicitement (permits A, B, C), soit implicitement (le
 *      clause permits peut etre OMISE UNIQUEMENT SI tous les
 *      sous-types directs sont declares dans le MEME FICHIER source
 *      que le type sealed).
 *   2. CHAQUE sous-type direct DOIT choisir un et un seul de ces 3
 *      modificateurs : final (ferme definitivement), sealed (rouvre
 *      une nouvelle liste fermee), ou non-sealed (rouvre en grand).
 *      Un sous-type qui n'en choisit AUCUN ne compile pas.
 *   3. Un record qui implemente un sealed interface n'a PAS besoin
 *      d'ecrire "final" explicitement : un record est TOUJOURS final
 *      par nature, meme sans l'ecrire.
 *   4. Une fois qu'un sous-type est non-sealed, la chaine "se
 *      referme" : SES PROPRES sous-types, eux, n'ont plus besoin
 *      d'aucun modificateur special - on revient aux regles normales
 *      de l'heritage Java.
 */
public class Exercise07_SealedModifiersQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : un professeur annonce a l'oral "seuls Circle et
    // Square ont le droit d'etre mes eleves", mais n'ecrit JAMAIS
    // cette liste noir sur blanc dans le "permits" - il compte sur le
    // fait que TOUT LE MONDE (Shape, Circle ET Square) est ecrit dans
    // le MEME cahier (le meme fichier .java) pour que ce soit quand
    // meme clair.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // sealed interface Shape {}
    // final class Circle implements Shape {}
    // final class Square implements Shape {}

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : la meme classe Shape, sealed, avec sa liste permits
    // ECRITE explicitement cette fois - mais un des 2 eleves (Square)
    // arrive en cours sans avoir choisi son "camp" (ni final, ni
    // sealed, ni non-sealed), comme s'il pensait que ce n'etait pas
    // obligatoire pour lui.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // sealed interface Shape permits Circle, Square {}
    // final class Circle implements Shape {}
    // class Square implements Shape {}

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : cette fois, Shape (sealed, SANS permits ecrit) et
    // Circle (qui l'implemente) sont dans 2 fichiers SEPARES - un peu
    // comme annoncer a l'oral une liste d'invites, mais sans jamais
    // l'ecrire, alors que l'invite en question habite dans une AUTRE
    // maison (un autre fichier) et n'a AUCUN moyen de savoir s'il est
    // vraiment sur la liste.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // // --- Fichier Shape.java ---
    // public sealed interface Shape {}
    // // --- Fichier Circle.java (fichier SEPARE) ---
    // public final class Circle implements Shape {}

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : le meme probleme que le Bloc C, mais cette fois le
    // professeur ECRIT explicitement la liste des invites autorises
    // dans le permits, meme si Circle habite ailleurs.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // // --- Fichier Shape.java ---
    // public sealed interface Shape permits Circle {}
    // // --- Fichier Circle.java (fichier SEPARE) ---
    // public final class Circle implements Shape {}

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : Mid choisit non-sealed - "a partir de moi, la porte
    // est GRANDE OUVERTE, n'importe qui peut devenir mon enfant sans
    // rien demander a personne". Grandchild en profite, sans choisir
    // lui-meme aucun modificateur special.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // sealed class Base permits Mid {}
    // non-sealed class Mid extends Base {}
    // class Grandchild extends Mid {}

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : Circle est un record (compact, immuable - voir
    // Exercise08) qui implemente le sealed interface Shape, sans
    // jamais ecrire "final" devant "record Circle" - comme si le
    // record pensait que ce mot-cle etait facultatif pour lui.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // sealed interface Shape permits Circle {}
    // record Circle(double radius) implements Shape {}

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. Circle et Square sont TOUS LES DEUX dans le
     *   MEME fichier que Shape (regle 1), et TOUS LES DEUX marques
     *   final (regle 2) : le permits peut etre omis, le compilateur
     *   deduit lui-meme la liste fermee a partir du fichier.
     *
     * Bloc B : NE COMPILE PAS. Square n'a AUCUN des 3 modificateurs
     *   requis (regle 2) - meme avec un permits explicite qui le
     *   nomme correctement, chaque sous-type DOIT quand meme choisir
     *   son propre avenir. Erreur : "sealed, non-sealed or final
     *   modifiers expected".
     *
     * Bloc C : NE COMPILE PAS. Circle est dans un fichier DIFFERENT
     *   de Shape, et Shape n'a AUCUN permits ecrit (regle 1 violee) :
     *   le compilateur exige une liste EXPLICITE des lors que les
     *   sous-types ne sont pas tous visibles dans le meme fichier.
     *   Erreur : "sealed class must have subclasses" (sur Shape) +
     *   "class is not allowed to extend sealed class" (sur Circle).
     *
     * Bloc D : COMPILE. Circle est dans un fichier different, MAIS
     *   cette fois Shape ECRIT explicitement "permits Circle" - la
     *   regle 1 n'exige le meme fichier QUE lorsque permits est omis,
     *   jamais quand il est present.
     *
     * Bloc E : COMPILE. Mid, en choisissant non-sealed, "referme la
     *   chaine sealed" : Grandchild n'est plus soumis a AUCUNE regle
     *   speciale (regle 4) - on retombe sur de l'heritage Java tout a
     *   fait classique.
     *
     * Bloc F : COMPILE. Un record est TOUJOURS final, meme sans
     *   l'ecrire (regle 3) - le compilateur le sait deja tout seul,
     *   inutile (et d'ailleurs deja implicite) de repeter "final"
     *   devant "record".
     */
}
