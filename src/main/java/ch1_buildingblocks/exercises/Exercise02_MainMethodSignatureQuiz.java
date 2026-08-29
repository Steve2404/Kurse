package ch1_buildingblocks.exercises;

/**
 * EXERCICE 2 - Quiz "ca compile ou pas ?" - ET "ca se LANCE ou pas ?" - sur la signature de main() (niveau : examen OCP)
 * ==================================================================================================================================
 *
 * Pas de main() a lancer directement pour verifier ici (chaque bloc
 * EST son propre petit programme complet) : recopie chaque bloc dans
 * un fichier .java a part (avec le bon nom de classe), PUIS essaie
 * "javac Fichier.java" (ca compile ?) ET "java Fichier" (ca se lance
 * ET affiche quelque chose ?) - POUR CHAQUE bloc, note tes 2
 * predictions avant de tester.
 *
 * -- Le piege CENTRAL de ce quiz --
 *
 *   Java n'exige JAMAIS qu'une classe ait une methode main() pour
 *   COMPILER (une classe SANS AUCUN main() compile tres bien, elle
 *   ne pourra juste pas etre LANCEE directement). Consequence : une
 *   methode NOMMEE "main" mais avec une SIGNATURE INCORRECTE
 *   (non-static, mauvais type de retour, private...) COMPILE
 *   PARFAITEMENT ELLE AUSSI - Java la traite juste comme une methode
 *   ORDINAIRE, sans aucun rapport avec le point d'entree du
 *   programme. L'erreur, dans TOUS ces cas, n'apparait qu'AU
 *   LANCEMENT (java NomDeClasse), JAMAIS a la compilation - une
 *   distinction facile a rater sur l'examen.
 */
public class Exercise02_MainMethodSignatureQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : "String... args" (varargs, voir le chapitre
    // "Methods") au lieu du "String[] args" habituel.
    //
    // Reponse : (a completer : compile ? se lance ? pourquoi)
    // ------------------------------------------------------------------
    // public class A {
    //     public static void main(String... args) {
    //         System.out.println("ca marche");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : "String args[]" (la vieille syntaxe C/C++ de
    // tableau, crochets APRES le nom) au lieu de "String[] args".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // public class B {
    //     public static void main(String args[]) {
    //         System.out.println("ca marche aussi");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : quelqu'un oublie "static" - en pensant que, puisque
    // la classe elle-meme se lance, static n'est "pas vraiment"
    // necessaire.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // public class C {
    //     public void main(String[] args) {
    //         System.out.println("ne devrait pas s'afficher");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : quelqu'un declare main() private, en pensant que
    // "c'est MA classe, donc private suffit pour que MOI je puisse
    // la lancer".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // public class D {
    //     private static void main(String[] args) {
    //         System.out.println("ne devrait pas s'afficher non plus");
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : main() renvoie un int au lieu de void, en pensant
    // "renvoyer 0 en cas de succes, comme dans d'autres langages".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // public class E {
    //     public static int main(String[] args) {
    //         System.out.println("ne devrait pas s'afficher");
    //         return 0;
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE, ET SE LANCE. String... args est
     *   TECHNIQUEMENT un tableau (voir "Core APIs") : une signature
     *   de main() parfaitement valide, au meme titre que String[].
     *
     * Bloc B : COMPILE, ET SE LANCE. String args[] (crochets APRES
     *   le nom) est une syntaxe de tableau plus ancienne mais
     *   TOUJOURS valide en Java, y compris pour main().
     *
     * Bloc C : COMPILE (Java n'exige aucune methode main() valide
     *   pour compiler une classe - une methode nommee "main" sans la
     *   bonne signature est juste une methode ORDINAIRE). NE SE
     *   LANCE PAS : erreur au lancement, "main method is not
     *   static".
     *
     * Bloc D : COMPILE, pour la MEME raison que le Bloc C. NE SE
     *   LANCE PAS : erreur au lancement, "main method not found"
     *   (private n'est simplement pas visible pour le lanceur java).
     *
     * Bloc E : COMPILE, encore pour la MEME raison. NE SE LANCE PAS :
     *   erreur au lancement, "main method must return a value of
     *   type void".
     */
}
