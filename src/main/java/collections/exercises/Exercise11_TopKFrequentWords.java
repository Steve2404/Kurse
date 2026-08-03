package collections.exercises;

import collections.ExerciseChecker;

import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 11 - Top K mots les plus frequents (niveau : moyen/difficile)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO : topKFrequent(words, k)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une election dans la cour de recreation. Chaque enfant crie
 * le nom de son jouet prefere, un par un. Toi, tu tiens un carnet ou
 * tu notes, pour chaque jouet different qu'on te crie, un petit
 * batonnet a chaque fois qu'on le crie a nouveau (comme un pointage :
 * | | | pour 3 votes). A la fin, la maitresse te demande : "donne-moi
 * les K jouets qui ont recu le PLUS de votes". Et si deux jouets ont
 * recu EXACTEMENT le meme nombre de votes, on les depart age par ordre
 * alphabetique (pour que tout le monde retombe toujours sur le meme
 * classement, sans ambiguite).
 *
 * -- Essayons a la main --
 *
 * Votes crie dans cet ordre : "ecran", "clavier", "souris", "ecran",
 * "clavier", "ecran". On tient le carnet de pointage :
 *
 *   ecran   : | | |   (3 votes)
 *   clavier : | |     (2 votes)
 *   souris  : |       (1 vote)
 *
 * On veut les K=2 jouets les plus votes. On classe par nombre de votes
 * decroissant : ecran (3), clavier (2), souris (1). On garde les 2
 * premiers : ["ecran", "clavier"].
 *
 * Refais l'exercice a la main avec ["a","b","c","a","b","c"] et K=2.
 * Chaque mot recoit exactement 2 votes : personne n'est devant
 * personne par le nombre de votes ! C'est la que la regle de secours
 * (l'ordre alphabetique) doit trancher : le resultat attendu est
 * ["a","b"].
 *
 * -- Ce qu'on remarque --
 *
 * Il y a deux travaux bien distincts, l'un apres l'autre : d'abord
 * COMPTER (un carnet de pointage - une Map ou la cle est le mot et la
 * valeur est son nombre de votes), puis CLASSER (mettre les mots
 * differents dans l'ordre voulu, en utilisant le carnet de pointage
 * pour savoir qui a le plus de votes).
 *
 * -- Le plan --
 *
 *   1. Parcourir 'words' une seule fois, et remplir un carnet de
 *      pointage (Map<String, Integer>) : pour chaque mot, augmenter
 *      son compteur de 1 (ou le demarrer a 1 si c'est la premiere fois
 *      qu'on l'entend).
 *   2. Recuperer la liste des mots DIFFERENTS (les cles du carnet).
 *   3. Trier cette liste de mots differents avec une regle a 2 etages :
 *      d'abord par nombre de votes decroissant (en regardant dans le
 *      carnet), et en cas d'egalite parfaite des votes, par ordre
 *      alphabetique croissant.
 *   4. Garder seulement les K premiers de cette liste triee (ou moins,
 *      s'il y a moins de K mots differents en tout).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * L'etape 1 (compter) se raconte toute seule (Q1) et cache sa propre
 * petite regle ("deja vu ou pas encore vu ce mot", Q3) : bon candidat
 * pour rester quand meme une simple boucle ici, car le JDK offre deja
 * l'outil qui fait ce travail en une ligne (voir l'indice plus bas) -
 * pas besoin de reinventer une boite pour ca. L'etape 3 (la regle de
 * tri a 2 etages) merite en revanche sa propre petite regle nommee (un
 * Comparator), car elle cache une vraie decision a deux niveaux.
 *
 * Exemple a verifier : topKFrequent(["ecran","clavier","souris",
 * "ecran","clavier","ecran"], 2) == ["ecran","clavier"]
 * topKFrequent(["a","b","c","a","b","c"], 2) == ["a","b"] (egalite ->
 * ordre alphabetique)
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Pour compter en une ligne : counts.merge(word, 1, Integer::sum);
 *     (cree l'entree a 1 si absente, sinon ajoute 1 a la valeur existante).
 *   - Pour recuperer les mots differents : new ArrayList<>(counts.keySet())
 *   - Pour trier avec une regle a 2 etages (frequence desc, puis
 *     alphabetique asc) :
 *       list.sort((a, b) -> {
 *           int cmp = counts.get(b) - counts.get(a); // desc
 *           if (cmp != 0) return cmp;
 *           return a.compareTo(b); // asc
 *       });
 *   - Pour garder les K premiers sans deborder si la liste est plus
 *     courte que K : list.subList(0, Math.min(k, list.size()))
 */
public class Exercise11_TopKFrequentWords {

    public static List<String> topKFrequent(List<String> words, int k) {
        throw new UnsupportedOperationException("TODO : implementer topKFrequent()");
    }

    public static void main(String[] args) {
        List<String> votes = Arrays.asList("ecran", "clavier", "souris", "ecran", "clavier", "ecran");
        List<String> top2 = topKFrequent(votes, 2);
        ExerciseChecker.check("topKFrequent(votes, 2) == [ecran, clavier]",
                top2.equals(Arrays.asList("ecran", "clavier")));

        List<String> tie = Arrays.asList("a", "b", "c", "a", "b", "c");
        List<String> topTie = topKFrequent(tie, 2);
        ExerciseChecker.check("egalite de frequence departagee par ordre alphabetique -> [a, b]",
                topTie.equals(Arrays.asList("a", "b")));

        List<String> single = Arrays.asList("x");
        ExerciseChecker.check("topKFrequent avec K plus grand que le nombre de mots distincts renvoie tout",
                topKFrequent(single, 5).equals(Arrays.asList("x")));

        ExerciseChecker.summary();
    }
}