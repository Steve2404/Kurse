package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

/**
 * EXERCICE 1 - Fabriquer sa propre interface fonctionnelle (niveau : moyen/difficile)
 * ============================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Ce qu'est une interface fonctionnelle, en une phrase --
 *
 * Une interface fonctionnelle, c'est une interface qui n'a qu'UNE
 * SEULE methode abstraite (SAM = Single Abstract Method). C'est cette
 * unique methode qu'un lambda vient remplir. Les methodes default,
 * static, private et private static NE COMPTENT PAS dans ce total -
 * une interface peut en avoir autant qu'elle veut, tant qu'il ne reste
 * qu'UNE SEULE methode abstraite.
 *
 *
 * ==================================================================
 * TODO 1a, 1b, 1c : Validator<T>.and(), or(), negate()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine le videur a l'entree d'une fete d'anniversaire. Il a une
 * liste de regles a verifier avant de laisser entrer un enfant : "est-
 * ce qu'il a son carton d'invitation ?", "est-ce qu'il porte un
 * deguisement ?". Le videur ne veut pas ecrire un immense pave de
 * "si... et si... et si..." pour chaque combinaison de regles. Il
 * prefere pouvoir dire : "cette regle-COMBINEE, c'est la regle A ET
 * la regle B" (les deux doivent etre vraies), ou "la regle A OU la
 * regle B" (au moins une suffit), ou encore "le CONTRAIRE de la regle
 * A" (ce qui etait refuse devient accepte, et vice-versa).
 *
 * Validator<T> est TA PROPRE interface fonctionnelle (regarde son
 * code plus bas) : une seule methode abstraite, test(T value), qui
 * dit vrai ou faux pour une valeur donnee. Le but de cet exercice est
 * d'ajouter des methodes default qui permettent de COMBINER plusieurs
 * Validator entre eux, exactement comme le videur combine ses regles.
 *
 * -- Essayons a la main --
 *
 * minLength = "le mot de passe fait au moins 8 caracteres"
 * hasDigit  = "le mot de passe contient au moins un chiffre"
 *
 * strongPassword = minLength.and(hasDigit)
 *
 * Teste "abc" : minLength dit FAUX (trop court) -> and() s'arrete
 * tout de suite et repond FAUX, sans meme regarder hasDigit (ca
 * n'aurait aucun sens de continuer a verifier une regle si on sait
 * deja que le resultat final sera FAUX).
 *
 * Teste "abcdefgh" : minLength dit VRAI (8 caracteres), donc and()
 * regarde ENSUITE hasDigit -> FAUX (pas de chiffre) -> resultat final
 * FAUX.
 *
 * Teste "abcdefg1" : minLength VRAI, hasDigit VRAI -> resultat final
 * VRAI.
 *
 * -- Ce qu'on remarque --
 *
 * and(), or() et negate() ne VERIFIENT rien elles-memes : elles
 * FABRIQUENT et RENVOIENT un nouveau Validator, qui, quand on
 * l'appellera plus tard avec test(value), ira lui-meme interroger les
 * validators d'origine (this et other) et combinera leurs reponses.
 * C'est exactement comme demander au videur "fabrique-moi une
 * nouvelle regle qui n'est vraie que si TES deux anciennes regles le
 * sont" - le videur ne verifie rien tout de suite, il note juste la
 * nouvelle regle combinee pour plus tard.
 *
 * -- Le plan pour and(other) --
 *
 *   1. Fabriquer et renvoyer un NOUVEAU Validator<T> (donc un nouveau
 *      lambda, puisque Validator est une interface fonctionnelle).
 *   2. A l'interieur de ce nouveau lambda, pour une valeur recue :
 *      renvoyer VRAI seulement si this.test(valeur) ET
 *      other.test(valeur) sont TOUS LES DEUX vrais.
 *
 * Le plan pour or(other) et negate() suit exactement la meme logique,
 * juste avec "ou" au lieu de "et", et juste l'inverse pour negate().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : chacune de ces 3 methodes est deja, elle-meme, une petite
 * boite magique complete (une seule ligne : return value -> ...).
 * C'est le principe meme d'une methode default sur une interface
 * fonctionnelle : elle EST la boite.
 *
 * Exemple a verifier : minLength.and(hasDigit).and(hasUpper) sur
 * "Abcdefg1" (8 caracteres, un chiffre, une majuscule) -> VRAI.
 * Sur "abcdefg1" (pas de majuscule) -> FAUX.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - default Validator<T> and(Validator<? super T> other) {
 *         return value -> this.test(value) && other.test(value);
 *     }
 *   - or() suit le meme patron avec ||.
 *   - negate() suit le meme patron avec !this.test(value).
 *   - Pourquoi "Validator<? super T>" et pas juste "Validator<T>" en
 *     parametre ? Meme raisonnement PECS que dans Exercise08 du
 *     package collections : un Validator qui sait valider un type plus
 *     general que T sait forcement aussi valider un T.
 */
public class Exercise01_CustomFunctionalInterface {

    @FunctionalInterface
    interface Validator<T> {
        boolean test(T value);

        default Validator<T> and(Validator<? super T> other) {
            throw new UnsupportedOperationException("TODO 1a : implementer and()");
        }

        default Validator<T> or(Validator<? super T> other) {
            throw new UnsupportedOperationException("TODO 1b : implementer or()");
        }

        default Validator<T> negate() {
            throw new UnsupportedOperationException("TODO 1c : implementer negate()");
        }
    }

    public static void main(String[] args) {
        Validator<String> minLength = s -> s.length() >= 8;
        Validator<String> hasDigit = s -> s.chars().anyMatch(Character::isDigit);
        Validator<String> hasUpper = s -> s.chars().anyMatch(Character::isUpperCase);

        Validator<String> strongPassword = minLength.and(hasDigit).and(hasUpper);

        ExerciseChecker.check("and() : 'Abcdefg1' respecte les 3 regles -> VRAI",
                strongPassword.test("Abcdefg1"));
        ExerciseChecker.check("and() : 'abcdefg1' sans majuscule -> FAUX",
                !strongPassword.test("abcdefg1"));
        ExerciseChecker.check("and() : 'Abc1' trop court -> FAUX",
                !strongPassword.test("Abc1"));

        Validator<String> hasDigitOrUpper = hasDigit.or(hasUpper);
        ExerciseChecker.check("or() : 'abcdefg1' a un chiffre (pas de majuscule) -> VRAI",
                hasDigitOrUpper.test("abcdefg1"));
        ExerciseChecker.check("or() : 'abcdefgh' n'a ni chiffre ni majuscule -> FAUX",
                !hasDigitOrUpper.test("abcdefgh"));

        Validator<String> isBlank = s -> s.trim().isEmpty();
        Validator<String> notBlank = isBlank.negate();
        ExerciseChecker.check("negate() : '   ' est blanc -> notBlank == FAUX",
                !notBlank.test("   "));
        ExerciseChecker.check("negate() : 'salut' n'est pas blanc -> notBlank == VRAI",
                notBlank.test("salut"));

        ExerciseChecker.summary();
    }
}
