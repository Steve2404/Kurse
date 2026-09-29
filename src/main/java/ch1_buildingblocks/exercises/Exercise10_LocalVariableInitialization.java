package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * EXERCICE 10 - Une variable locale n'a pas de valeur par defaut : l'initialiser sur TOUS les chemins (niveau : moyen)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- La regle --
 *
 * Un CHAMP (variable de classe ou d'instance) recoit une valeur par
 * defaut automatique (0, false, null - voir Exercise05). Une variable
 * LOCALE (dans une methode) n'en recoit JAMAIS. Le compilateur verifie
 * que, sur CHAQUE chemin possible du code, elle a recu une valeur
 * avant d'etre lue ("definite assignment"). Sinon, ca ne compile pas.
 *
 * Versions fausses, messages REELS de javac 17 :
 *
 *   int x; System.out.println(x);
 *   -> error: variable x might not have been initialized
 *
 *   int x; if (flag) { x = 1; } System.out.println(x);   (pas de else)
 *   -> error: variable x might not have been initialized
 *
 *   int x; if (flag) { x = 1; } else { x = 2; } System.out.println(x);
 *   -> COMPILE : les 2 chemins donnent une valeur a x.
 *
 *   int a = 1, b = 2;   -> COMPILE : plusieurs variables du MEME type en une instruction.
 *
 * Dans cet exercice, tu dois DECLARER les variables SANS valeur de
 * depart (quand le plan le demande) et prouver au compilateur que
 * chaque chemin les initialise.
 *
 *
 * ==================================================================
 * TODO 1 : grade(score)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque eleve repart avec UNE lettre. Si on oublie un cas (par
 * exemple les notes sous 50), un eleve repartirait sans lettre : le
 * compilateur refuse. Avec un "else" final, personne n'est oublie.
 *
 * -- Essayons a la main --
 *
 *   95 -> "A" ; 75 -> "B" ; 50 -> "C" ; 12 -> "D"
 *
 * -- Le plan --
 *
 *   1. Declarer String result; (SANS valeur).
 *   2. >= 90 -> "A" ; sinon >= 70 -> "B" ; sinon >= 50 -> "C" ; sinon "D".
 *   3. Rendre result.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : sumOfPair()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On peut declarer plusieurs variables du MEME type d'un coup, separees
 * par des virgules, chacune avec sa valeur.
 *
 * -- Le plan --
 *
 *   1. Declarer a = 3 et b = 4 en UNE seule instruction.
 *   2. Rendre a + b (7).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : discountedPriceCents(priceCents, vip)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La remise depend du client : 10 % pour un VIP (division entiere),
 * rien sinon. La variable discount est declaree sans valeur, et CHAQUE
 * branche doit lui en donner une.
 *
 * -- Essayons a la main --
 *
 *   (468, true)  -> remise 46 -> 422
 *   (468, false) -> remise 0  -> 468
 *
 * -- Le plan --
 *
 *   1. Declarer int discount; (SANS valeur).
 *   2. Si vip : discount = priceCents / 10 ; sinon discount = 0.
 *   3. Rendre priceCents - discount.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : firstIndexOf(values, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On cherche la position d'un nombre dans un tableau. Si on ne le
 * trouve pas, on doit quand meme rendre QUELQUE CHOSE : -1. Le plus
 * simple est de donner a la variable sa valeur "pas trouve" des le
 * depart, puis de la remplacer si on trouve.
 *
 * -- Essayons a la main --
 *
 *   ([4, 7, 7], 7) -> 1 (le premier) ; ([4, 7], 9) -> -1 ; ([], 1) -> -1
 *
 * -- Le plan --
 *
 *   1. found = -1.
 *   2. Parcourir les positions ; a la 1re egalite, found = position et arreter.
 *   3. Rendre found.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - String result;  puis  if (...) { result = "A"; } else if (...) { ... } else { result = "D"; }
 *   - int a = 3, b = 4;
 *   - for (int i = 0; i < values.length; i++) { if (values[i] == target) { found = i; break; } }
 */
public class Exercise10_LocalVariableInitialization {

    public static String grade(int score) {
        throw new UnsupportedOperationException("TODO 1 : implementer grade()");
    }

    public static int sumOfPair() {
        throw new UnsupportedOperationException("TODO 2 : implementer sumOfPair()");
    }

    public static int discountedPriceCents(int priceCents, boolean vip) {
        throw new UnsupportedOperationException("TODO 3 : implementer discountedPriceCents()");
    }

    public static int firstIndexOf(int[] values, int target) {
        throw new UnsupportedOperationException("TODO 4 : implementer firstIndexOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 grade : 95 -> A, 75 -> B, 50 -> C, 12 -> D",
                grade(95).equals("A") && grade(75).equals("B") && grade(50).equals("C") && grade(12).equals("D"));
        ExerciseChecker.check("2 sumOfPair() == 7", sumOfPair() == 7);
        ExerciseChecker.check("3 discountedPriceCents(468, true) == 422, (468, false) == 468",
                discountedPriceCents(468, true) == 422 && discountedPriceCents(468, false) == 468);
        ExerciseChecker.check("4 firstIndexOf([4, 7, 7], 7) == 1, ([4, 7], 9) == -1, ([], 1) == -1",
                firstIndexOf(new int[]{4, 7, 7}, 7) == 1 && firstIndexOf(new int[]{4, 7}, 9) == -1
                        && firstIndexOf(new int[0], 1) == -1);

        ExerciseChecker.summary();
    }
}
