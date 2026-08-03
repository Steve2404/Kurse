package collections.exercises;

/**
 * EXERCICE 10 - Quiz "ca compile ou pas ?" sur les wildcards (niveau : examen OCP)
 * ====================================================================================
 *
 * Pas de main() a lancer ici : cet exercice se fait a la main. Pour
 * chaque bloc, c'est TOI la boite magique : lis d'abord l'histoire
 * imagee, essaie de repondre sur une feuille (compile / ne compile
 * pas + pourquoi), PUIS decommente le bloc et essaie de COMPILER (mvn
 * compile ou votre IDE) pour verifier. C'est ca, "l'essai a la main"
 * de cet exercice : le compilateur Java devient ton juge de paix.
 *
 * Remettez le bloc en commentaire (ou corrigez-le) avant de passer au
 * suivant, sinon les erreurs de compilation des blocs precedents
 * empecheront de tester les suivants.
 *
 * -- L'image a garder en tete pour tout le quiz --
 *
 * <? extends X> c'est une boite qui ne fait que DONNER (tu peux
 * regarder/sortir ce qu'il y a dedans, mais jamais rien y remettre,
 * car tu ne sais pas exactement quel sous-type precis elle contient
 * vraiment). <? super X> c'est une boite qui ne fait que RECEVOIR (tu
 * peux y deposer un X en toute confiance, mais ce que tu en ressors
 * n'est garanti que d'etre "quelque chose de tres general", donc pas
 * exploitable directement sans verification). Voir Exercise08 pour la
 * version longue de cette image (les voitures et les jouets).
 *
 * Rappel de cours (resume du chapitre 9 de l'OCP 17) :
 *   <?>            wildcard non borne : n'importe quel type
 *   <? extends X>  borne haute : X ou un sous-type de X (lecture seule, "Producer")
 *   <? super X>    borne basse : X ou un supertype de X (ecriture, "Consumer")
 *   Une methode generique <T> se declare AVANT le type de retour.
 */
public class Exercise10_WildcardsQuiz {

    // ------------------------------------------------------------------
    // Bloc A
    //
    // Histoire : tu as une boite qui contient "des Number, mais on ne
    // te dit pas lesquels precisement" (pourrait etre des Integer, des
    // Double, un melange...). Tu essaies d'y DEPOSER un Integer bien
    // precis. Est-ce que la boite peut etre sure que 42 est du bon
    // type pour ELLE, alors qu'elle ne sait meme pas quel type exact
    // elle contient deja ?
    //
    // Reponse : (a completer : compile / ne compile pas + pourquoi)
    // ------------------------------------------------------------------
    // void blocA() {
    //     java.util.List<? extends Number> list = new java.util.ArrayList<Integer>();
    //     list.add(42);
    // }

    // ------------------------------------------------------------------
    // Bloc B
    //
    // Histoire : tu as une boite qui accepte "au moins des Integer, ou
    // n'importe quoi de plus general" (elle pourrait etre en vrai une
    // boite a Number). Tu y deposes un Integer (facile, elle promet de
    // savoir le recevoir). Puis tu essaies de RESSORTIR un element et
    // de le ranger directement dans un tiroir etiquette "Integer" :
    // est-ce que la boite te garantit que ce qui en ressort est
    // FORCEMENT un Integer ?
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void blocB() {
    //     java.util.List<? super Integer> list = new java.util.ArrayList<Number>();
    //     list.add(42);
    //     Integer x = list.get(0);
    // }

    // ------------------------------------------------------------------
    // Bloc C
    //
    // Histoire : une boite totalement mysterieuse, "on ne sait rien du
    // tout sur ce qu'elle contient" (<?>). Tu ressors un element et tu
    // le ranges dans un tiroir etiquette juste "Objet quelconque",
    // sans rien exiger de plus precis. Est-ce risque ?
    //
    // Reponse :
    // ------------------------------------------------------------------
    // void blocC() {
    //     java.util.List<?> list = new java.util.ArrayList<String>();
    //     Object o = list.get(0);
    // }

    // ------------------------------------------------------------------
    // Bloc D
    //
    // Histoire : un juge de concours qui sait comparer deux
    // concurrents entre eux (Comparable) regarde une file de
    // concurrents et designe le meilleur. Rien de special ici, c'est
    // le patron classique de la methode "max" generique bornee.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static <T extends Comparable<T>> T max(java.util.List<T> list) {
    //     T best = list.get(0);
    //     for (T item : list) {
    //         if (item.compareTo(best) > 0) {
    //             best = item;
    //         }
    //     }
    //     return best;
    // }
    // void useBlocD() {
    //     java.util.List<String> words = java.util.Arrays.asList("pomme", "kiwi", "banane");
    //     String m = max(words);
    // }

    // ------------------------------------------------------------------
    // Bloc E
    //
    // Histoire : une caisse etiquetee "n'importe quel type d'objets"
    // (List<Object>). Tu essaies d'y donner ta caisse a toi, etiquetee
    // precisement "des mots" (List<String>). Une caisse de mots est-
    // elle automatiquement une caisse "n'importe quel objet" pour le
    // compilateur, meme si intuitivement un mot EST un objet ?
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static void printAll(java.util.List<Object> list) {
    //     for (Object o : list) {
    //         System.out.println(o);
    //     }
    // }
    // void useBlocE() {
    //     java.util.List<String> strings = java.util.Arrays.asList("a", "b");
    //     printAll(strings); // attention : List<String> n'est PAS un List<Object> !
    // }

    // ------------------------------------------------------------------
    // Bloc F
    //
    // Histoire : la balance de cuisine de l'exercice 8 (TODO 3), qui
    // pese n'importe quel melange de "trucs qui sont des Number", sans
    // jamais rien ajouter dans la liste pesee.
    //
    // Reponse :
    // ------------------------------------------------------------------
    // static double sum(java.util.List<? extends Number> list) {
    //     double total = 0;
    //     for (Number n : list) {
    //         total += n.doubleValue();
    //     }
    //     return total;
    // }
    // void useBlocF() {
    //     java.util.List<Integer> ints = java.util.Arrays.asList(1, 2, 3);
    //     double s = sum(ints);
    // }

    /*
     * Reponses officielles (ne regardez qu'apres avoir repondu vous-meme) :
     *
     * Bloc A : NE COMPILE PAS sur list.add(42). Avec "? extends Number",
     *   le compilateur sait seulement que la liste contient "quelque
     *   chose qui est un Number", mais pas QUEL type precis -- il pourrait
     *   s'agir d'une List<Double> par exemple, donc ajouter un Integer
     *   serait dangereux : interdit (sauf ajouter null).
     *
     * Bloc B : COMPILE entierement. "? super Integer" garantit que la
     *   liste accepte au moins des Integer en ecriture. En lecture, on
     *   ne peut recuperer que des Object (pas garanti Integer), MAIS ici
     *   list.get(0) est assigne a Integer x : en realite le type retourne
     *   par get() sur List<? super Integer> est Object, donc CETTE ligne
     *   precise (Integer x = list.get(0);) NE COMPILE PAS sans cast.
     *   (Piege classique : "super" protege l'ecriture, pas la lecture.)
     *
     * Bloc C : COMPILE. Object o = list.get(0) fonctionne toujours quel
     *   que soit le wildcard, car TOUT est un Object.
     *
     * Bloc D : COMPILE. C'est le pattern generique classique pour une
     *   methode "max" bornee par Comparable.
     *
     * Bloc E : NE COMPILE PAS. List<String> n'est PAS un sous-type de
     *   List<Object> (les generiques ne sont pas covariants sur les
     *   types concrets, seulement via wildcards). Il faudrait
     *   List<?> ou List<? extends Object> comme parametre.
     *
     * Bloc F : COMPILE. C'est exactement le cas d'usage de "? extends
     *   Number" : on lit des Number, on ne modifie pas la liste.
     */
}