package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * EXERCICE 8 - Consumer/BiConsumer et UnaryOperator/BinaryOperator (niveau : difficile)
 * ============================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Les 4 interfaces du jour, en une phrase chacune --
 *
 *   - Consumer<T>          : prend UN ingredient, ne rend RIEN (elle
 *                             fait juste un effet de bord, comme
 *                             imprimer ou ranger quelque part).
 *   - BiConsumer<T,U>      : pareil, mais avec DEUX ingredients.
 *   - UnaryOperator<T>     : un Function<T,T> special - l'ingredient
 *                             qui entre et le resultat qui sort sont
 *                             TOUJOURS du meme type.
 *   - BinaryOperator<T>    : un BiFunction<T,T,T> special - deux
 *                             ingredients du MEME type, un resultat de
 *                             ce MEME type.
 *
 * Consumer et BiConsumer ont chacun une methode default andThen(...),
 * qui fabrique un nouveau Consumer/BiConsumer executant D'ABORD celui
 * de gauche, PUIS celui de droite, sur le(s) MEME(S) ingredient(s).
 *
 *
 * ==================================================================
 * TODO 1 : buildNotifier(emailLog, smsLog)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un standardiste qui recoit UN message a annoncer, et qui
 * doit le crier a la fois dans le micro "email" ET dans le micro
 * "SMS" - jamais l'un sans l'autre.
 *
 * -- Essayons a la main --
 *
 * emailLog et smsLog sont deux listes vides au depart. On appelle le
 * Consumer<String> fabrique avec "Colis expedie" : le message doit se
 * retrouver AJOUTE dans emailLog ET dans smsLog, dans cet ordre.
 *
 * -- Le plan --
 *
 *   1. Fabriquer un Consumer<String> qui ajoute a emailLog (pense a
 *      une method reference sur la liste elle-meme).
 *   2. Fabriquer un Consumer<String> qui ajoute a smsLog, de la meme
 *      facon.
 *   3. Combiner les deux avec andThen(), pour n'en rendre qu'UN SEUL.
 *
 *
 * ==================================================================
 * TODO 2 : buildStockUpdater()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un magasinier qui, quand un article est vendu, fait TOUJOURS
 * deux gestes de suite sur SON registre (une Map<String,Integer> nom
 * -> quantite) et le NOM de l'article vendu : (a) il diminue le stock
 * de 1, (b) si le stock tombe a 0 ou moins, il retire carrement
 * l'article du registre (un article a "0 en stock" n'a plus besoin
 * d'etre liste).
 *
 * -- Essayons a la main --
 *
 * stock = {"stylo": 1, "cahier": 5}. On applique le BiConsumer combine
 * sur (stock, "stylo") : le stock de "stylo" descend a 0, donc "stylo"
 * disparait COMPLETEMENT du registre -> stock = {"cahier": 5}.
 * On applique ensuite sur (stock, "cahier") : descend a 4, ne
 * disparait pas (4 > 0) -> stock = {"cahier": 4}.
 *
 * -- Le plan --
 *
 *   1. Fabriquer un BiConsumer<Map<String,Integer>,String> qui
 *      diminue de 1 la quantite associee a la cle (indice : la
 *      methode Map.merge(cle, -1, Integer::sum) fait exactement ca en
 *      une ligne, meme si la cle n'existe pas encore).
 *   2. Fabriquer un second BiConsumer<Map<String,Integer>,String> qui
 *      retire la cle SI sa valeur actuelle est <= 0.
 *   3. Combiner les deux avec andThen(), dans cet ordre precis
 *      (diminuer D'ABORD, retirer ENSUITE).
 *
 *
 * ==================================================================
 * TODO 3 : combineDiscounts(discounts)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un prix passe par plusieurs "tampons a remise" poses les uns apres
 * les autres sur un tapis roulant, chacun grignotant un peu le prix
 * (UnaryOperator<Double> : un prix entre, un AUTRE prix, forcement
 * aussi un Double, ressort). combineDiscounts fabrique UN SEUL tampon
 * qui fait tout le travail des tampons de la liste, dans l'ordre.
 *
 * -- Essayons a la main --
 *
 * discounts = [prix -> prix - 5.0, prix -> prix * 0.9]. Prix de
 * depart : 100.0. Tampon 1 : 100.0 - 5.0 = 95.0. Tampon 2 : 95.0 * 0.9
 * = 85.5. Le tampon combine, applique a 100.0, doit rendre 85.5.
 *
 * -- Le plan --
 *
 *   1. Renvoyer un NOUVEAU UnaryOperator<Double> (un lambda), qui ne
 *      fait rien tant qu'on ne l'appelle pas.
 *   2. A l'INTERIEUR de ce lambda, quand on recoit enfin le prix de
 *      depart : parcourir la liste de tampons DANS L'ORDRE, et a
 *      chaque tour, remplacer le prix courant par
 *      discount.apply(prix courant).
 *   3. Renvoyer le prix final, une fois tous les tampons passes.
 *
 * -- Piege a eviter --
 *
 * UnaryOperator<T> HERITE bien de andThen(...) via Function<T,T>, mais
 * son type de retour reste Function<T,V> (pas UnaryOperator<T>) : le
 * combiner tampon par tampon avec andThen() vous ferait sortir du
 * type UnaryOperator en cours de route. Le plus simple et le plus
 * clair ici est la boucle a l'INTERIEUR d'un seul lambda, comme
 * decrit ci-dessus.
 *
 *
 * ==================================================================
 * TODO 4 : bestOffer(offers, betterOf)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un rouleau compresseur (comme dans Exercise06) avance sur une liste
 * de prix, en ne gardant a chaque fois QUE le "meilleur" des deux
 * (selon la regle betterOf, un BinaryOperator<Double> qui compare deux
 * prix et en RENVOIE un des deux).
 *
 * -- Essayons a la main --
 *
 * offers = [12.0, 7.5, 9.0]. betterOf = "le plus petit des deux"
 * (Math::min). Depart : 12.0. Ecrase avec 7.5 -> min(12.0,7.5)=7.5.
 * Ecrase avec 9.0 -> min(7.5,9.0)=7.5. Resultat : 7.5.
 *
 * -- Le plan --
 *
 *   1. Demarrer l'accumulateur avec le PREMIER element de la liste
 *      (offers n'est jamais vide dans cet exercice).
 *   2. Pour chaque element SUIVANT, remplacer l'accumulateur par
 *      betterOf.apply(accumulateur, element).
 *   3. Renvoyer l'accumulateur final.
 *
 * -- Ces 4 TODO ont-ils besoin d'une boite magique separee entre eux ? --
 *
 * Non : chacun est deja sa propre boite (une methode publique dediee),
 * et chacun tient en quelques lignes. Pas besoin de redecouper
 * davantage.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Consumer.andThen(autre) : Consumer<T> par defaut, execute this
 *     puis autre, sur la MEME valeur.
 *   - BiConsumer.andThen(autre) : meme principe, avec 2 arguments.
 *   - Map.merge(cle, -1, Integer::sum) : si la cle existe, applique
 *     Integer::sum(valeurActuelle, -1) ; sinon, pose juste -1.
 *   - UnaryOperator<Double> combineDiscounts(...) { return price -> {
 *         double result = price; for (... : discounts) { result =
 *         discount.apply(result); } return result; }; }
 *   - BinaryOperator<T> est juste un BiFunction<T,T,T> - .apply(a, b)
 *     s'utilise pareil.
 */
public class Exercise08_ConsumerOperators {

    public static Consumer<String> buildNotifier(List<String> emailLog, List<String> smsLog) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildNotifier()");
    }

    public static BiConsumer<Map<String, Integer>, String> buildStockUpdater() {
        throw new UnsupportedOperationException("TODO 2 : implementer buildStockUpdater()");
    }

    public static UnaryOperator<Double> combineDiscounts(List<UnaryOperator<Double>> discounts) {
        throw new UnsupportedOperationException("TODO 3 : implementer combineDiscounts()");
    }

    public static double bestOffer(List<Double> offers, BinaryOperator<Double> betterOf) {
        throw new UnsupportedOperationException("TODO 4 : implementer bestOffer()");
    }

    public static void main(String[] args) {
        List<String> emailLog = new ArrayList<>();
        List<String> smsLog = new ArrayList<>();
        Consumer<String> notifier = buildNotifier(emailLog, smsLog);
        notifier.accept("Colis expedie");
        ExerciseChecker.check("le message part dans les 2 canaux",
                emailLog.equals(List.of("Colis expedie")) && smsLog.equals(List.of("Colis expedie")));

        Map<String, Integer> stock = new HashMap<>();
        stock.put("stylo", 1);
        stock.put("cahier", 5);
        BiConsumer<Map<String, Integer>, String> updater = buildStockUpdater();
        updater.accept(stock, "stylo");
        updater.accept(stock, "cahier");
        ExerciseChecker.check("stock a 0 -> article retire du registre", !stock.containsKey("stylo"));
        ExerciseChecker.check("stock > 0 -> juste decremente", stock.get("cahier") == 4);

        List<UnaryOperator<Double>> discounts = List.of(prix -> prix - 5.0, prix -> prix * 0.9);
        UnaryOperator<Double> combined = combineDiscounts(discounts);
        ExerciseChecker.check("remises combinees : 100.0 -> 85.5", combined.apply(100.0) == 85.5);

        List<Double> offers = List.of(12.0, 7.5, 9.0);
        double cheapest = bestOffer(offers, Math::min);
        ExerciseChecker.check("meilleure offre (min) == 7.5", cheapest == 7.5);

        ExerciseChecker.summary();
    }
}