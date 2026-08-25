package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 11 - reduce() a 3 arguments : identite, accumulateur, combinateur (niveau : difficile)
 * ==============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le reduce(identite, accumulateur) a 2 arguments (deja vu au chapitre
 * streams) marche tres bien tant que l'accumulateur PREND et REND le
 * MEME type (par exemple, additionner des int entre eux). Mais que
 * faire pour compter la LONGUEUR TOTALE d'une liste de mots ? L'entree
 * de chaque etape est un int (le total partiel) ET un String (le mot
 * courant) - 2 types DIFFERENTS ! Le reduce a 2 arguments ne sait pas
 * gerer ca.
 *
 * reduce(identite, accumulateur, combinateur) a 3 arguments resout ce
 * cas : l'accumulateur transforme (partiel:int, mot:String) -> nouveau
 * partiel:int. MAIS en parallele, PLUSIEURS threads calculent chacun
 * leur PROPRE petit total partiel, sur leur PROPRE morceau de la
 * liste - il faut ENSUITE les FUSIONNER tous ensemble en UN SEUL
 * total final. C'est exactement le role du combinateur (3e argument) :
 * "voici comment fusionner 2 totaux partiels (int, int) -> int".
 *
 *
 * ==================================================================
 * TODO : totalLength(words)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * words = ["ab", "cde", "f"]. Identite = 0. Accumulateur : (partiel,
 * mot) -> partiel + mot.length(). Sequentiellement : 0+2=2, 2+3=5,
 * 5+1=6. En parallele, un thread pourrait calculer 2 (pour "ab") et un
 * AUTRE calculer 3+1=4 (pour "cde" et "f") en meme temps, PUIS le
 * combinateur (Integer::sum) les fusionne : 2+4=6 - MEME resultat
 * final, peu importe le decoupage reel.
 *
 * -- Le plan --
 *
 *   1. words.parallelStream().reduce(0, (partiel, mot) -> partiel + mot.length(), Integer::sum).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule ligne suffit, c'est la COMPREHENSION du role de
 * chacun des 3 arguments qui est le vrai coeur de l'exercice.
 *
 * Exemple a verifier : sur 50 000 mots repartis en 100 valeurs
 * distinctes repetees, totalLength() donne EXACTEMENT le meme resultat
 * qu'un calcul sequentiel classique, peu importe le nombre de coeurs
 * disponibles sur la machine qui execute le code.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Signature complete : reduce(U identite, BiFunction<U,T,U> accumulateur,
 *     BinaryOperator<U> combinateur) - ici U = Integer, T = String.
 *   - Sur un stream SEQUENTIEL, le combinateur n'est en pratique
 *     JAMAIS appele (il n'y a qu'un seul "morceau"), mais reduce() a 3
 *     arguments l'exige quand meme dans la signature, car le MEME code
 *     doit pouvoir fonctionner aussi bien en sequentiel qu'en
 *     parallele.
 */
public class Exercise11_ParallelReduceWithCombiner {

    public static int totalLength(List<String> words) {
        throw new UnsupportedOperationException("TODO : implementer totalLength()");
    }

    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        for (int i = 0; i < 50_000; i++) {
            words.add("mot" + (i % 100));
        }
        int expected = words.stream().mapToInt(String::length).sum();

        ExerciseChecker.check("totalLength() (parallele) == calcul sequentiel de reference",
                totalLength(words) == expected);

        ExerciseChecker.summary();
    }
}
