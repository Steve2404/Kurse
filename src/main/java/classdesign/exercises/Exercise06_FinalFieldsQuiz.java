package classdesign.exercises;

/**
 * EXERCICE 6 - Quiz "ca compile ou pas ?" sur les champs final ("blank finals") (niveau : examen OCP)
 * ============================================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main, comme
 * Exercise04_ConstructorRulesQuiz. Pour chaque bloc, c'est TOI la
 * boite magique : lis d'abord l'histoire imagee, essaie de repondre
 * sur une feuille (compile / ne compile pas + pourquoi), PUIS
 * decommente le bloc et essaie de COMPILER (mvn compile ou votre IDE)
 * pour verifier.
 *
 * Remettez le bloc en commentaire avant de passer au suivant, sinon
 * les erreurs de compilation des blocs precedents empecheront de
 * tester les suivants.
 *
 * -- La regle a garder en tete pour tout le quiz --
 *
 *   Un champ d'instance final DOIT recevoir une valeur EXACTEMENT
 *   UNE FOIS avant la fin du constructeur - ni zero fois (le
 *   compilateur ne "devine" jamais une valeur par defaut pour un
 *   final), ni deux fois (meme une reaffectation "au cas ou"), et
 *   cette UNE fois doit etre GARANTIE sur TOUS les chemins possibles
 *   du code (une affectation cachee dans un simple "if" SANS "else"
 *   ne suffit pas : le compilateur ne peut pas prouver qu'elle aura
 *   lieu a TOUS les coups).
 */
public class Exercise06_FinalFieldsQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : x recoit sa valeur des sa declaration ("des la
    // naissance"), et le constructeur n'y touche plus jamais.
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x = 5;
    //     Foo() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : x est declare final, mais personne - ni
    // l'initialiseur, ni le constructeur - ne lui donne jamais de
    // valeur, comme un formulaire avec une case "obligatoire" laissee
    // vide.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x;
    //     Foo() {
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : Foo a 2 facons de naitre (2 constructeurs), et
    // CHACUNE des 2 donne bien sa PROPRE valeur a x, sans jamais
    // passer par l'autre.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x;
    //     Foo() {
    //         x = 1;
    //     }
    //     Foo(int v) {
    //         x = v;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : x recoit deja 5 des sa declaration, mais le
    // constructeur, "pour etre sur", lui redonne AUSSI 10 - comme
    // signer 2 fois le meme document officiel, en pensant que ca le
    // rend "encore plus valide".
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x = 5;
    //     Foo() {
    //         x = 10;
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : x n'est affecte QUE si un drapeau vaut true - sans
    // AUCUN "sinon" pour couvrir l'autre cas. Le programmeur pense
    // "de toute facon, j'appelle toujours ce constructeur avec
    // flag=true", mais le compilateur, lui, ne fait JAMAIS confiance
    // aux intentions, seulement au code REELLEMENT ecrit.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x;
    //     Foo(boolean flag) {
    //         if (flag) {
    //             x = 1;
    //         }
    //     }
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : x recoit sa valeur non pas dans le constructeur
    // lui-meme, mais dans un BLOC d'instance (voir aussi
    // Exercise05) - qui, lui, s'execute TOUJOURS avant le corps du
    // constructeur, donc tout aussi fiable qu'un initialiseur classique.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static class Foo {
    //     final int x;
    //     {
    //         x = 42;
    //     }
    //     Foo() {
    //     }
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : COMPILE. x recoit EXACTEMENT une valeur, via son
     *   initialiseur - le constructeur n'a rien de plus a faire.
     *
     * Bloc B : NE COMPILE PAS. x ne recoit JAMAIS de valeur, ni via
     *   un initialiseur, ni via le constructeur. Erreur : "variable x
     *   might not have been initialized".
     *
     * Bloc C : COMPILE. CHAQUE constructeur, pris independamment,
     *   garantit une affectation UNIQUE de x sur SON PROPRE chemin -
     *   le compilateur analyse chaque constructeur separement, pas
     *   besoin qu'ils s'affectent "de la meme facon".
     *
     * Bloc D : NE COMPILE PAS. x recoit DEJA sa valeur via
     *   l'initialiseur (5) ; la reaffectation a 10 dans le
     *   constructeur est une DEUXIEME affectation, interdite pour un
     *   final. Erreur : "cannot assign a value to final variable x".
     *
     * Bloc E : NE COMPILE PAS. Le "if" sans "else" ne GARANTIT pas
     *   que x sera affecte sur TOUS les chemins possibles (le cas
     *   flag == false ne l'affecte jamais) - meme si, dans la
     *   pratique, on n'appelle JAMAIS ce constructeur avec
     *   flag=false, le compilateur ne raisonne QUE sur le code, pas
     *   sur les intentions. Erreur : "variable x might not have been
     *   initialized".
     *
     * Bloc F : COMPILE. Un bloc d'instance s'execute TOUJOURS avant
     *   le corps du constructeur (voir l'ordre de l'Exercise05) :
     *   c'est une affectation tout aussi valable et unique qu'un
     *   initialiseur de champ classique.
     */
}
