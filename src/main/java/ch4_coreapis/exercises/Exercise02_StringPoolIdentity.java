package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 2 - Le pool de String : PREDIT a la main quels == rendent true, puis verifie contre le VRAI comportement (niveau : difficile)
 * ==================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le "pool" de String, c'est un CASIER COMMUN ou Java range CHAQUE
 * litteral String ("hello" ecrit tel quel dans le code) - et ne
 * range JAMAIS 2 fois le MEME texte : "hello" ecrit 2 fois a 2
 * endroits DIFFERENTS du code pointe malgre tout vers le MEME casier.
 * == compare des ADRESSES (est-ce EXACTEMENT le meme objet ?),
 * equals() compare le CONTENU (les memes lettres, meme si ce sont 2
 * objets differents). new String("hello") FORCE la creation d'un
 * TOUT NOUVEL objet, EN DEHORS du casier commun - meme s'il contient
 * exactement "hello". .intern() va chercher (ou range, si absent) LE
 * casier correspondant, et en rend l'adresse.
 *
 * PIEGE CLASSIQUE : une concatenation de PLUSIEURS LITTERAUX ("hel" +
 * "lo") est calculee UNE FOIS POUR TOUTES par le compilateur
 * lui-meme (avant meme que le programme tourne) - le resultat est
 * alors traite comme un litteral de plus, et RANGE dans le pool.
 * MAIS des qu'UNE VARIABLE entre dans le calcul (meme "effectivement
 * final"), la concatenation doit attendre L'EXECUTION : elle cree
 * alors un TOUT NOUVEL objet, PAS automatiquement range dans le pool.
 *
 *
 * ==================================================================
 * TODO : buildExpectedComparisons()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. En lisant les 6 comparaisons de main() (deja ecrites, RIEN a
 *      completer la-dedans), predire a la main, dans l'ORDRE, si
 *      chacune vaut true ou false.
 *   2. Les renvoyer dans une List<Boolean>.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un exercice de PREDICTION, pas de calcul.
 *
 * Exemple a verifier : buildExpectedComparisons() doit correspondre
 * EXACTEMENT aux 6 VRAIS resultats (voir main()).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - La comparaison 5 utilise DEUX litteraux cote a cote ("hel" +
 *     "lo") : le compilateur les fusionne AVANT l'execution.
 *   - La comparaison 6 utilise une VARIABLE (a) dans le calcul : plus
 *     question de fusion a la compilation, meme si a ne change
 *     jamais.
 */
public class Exercise02_StringPoolIdentity {

    public static List<Boolean> buildExpectedComparisons() {
        throw new UnsupportedOperationException("TODO : implementer buildExpectedComparisons()");
    }

    public static void main(String[] args) {
        List<Boolean> predicted = buildExpectedComparisons();

        List<Boolean> real = new ArrayList<>();
        real.add("hello" == "hello");
        real.add(new String("hello") == "hello");
        real.add(new String("hello").equals("hello"));
        real.add(new String("hello").intern() == "hello");
        real.add(("hel" + "lo") == "hello");
        String a = "hel";
        String b = a + "lo";
        real.add(b == "hello");

        ExerciseChecker.check("les 6 predictions correspondent EXACTEMENT au vrai comportement -> " + real,
                predicted.equals(real));

        ExerciseChecker.summary();
    }
}
