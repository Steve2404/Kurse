package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

/**
 * EXERCICE 12 - Un evaluateur d'expressions : interface sealed + records + pattern matching instanceof (niveau : avance)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une expression comme (1 + 2) * -3 est un ARBRE : une multiplication
 * dont la gauche est une addition et la droite une negation. On le
 * decrit avec une interface sealed Expr et exactement 4 sortes de
 * noeuds, des records :
 *
 *   Num(int value)            un nombre
 *   Add(Expr left, Expr right) une addition
 *   Mul(Expr left, Expr right) une multiplication
 *   Neg(Expr inner)           un moins devant
 *
 * Comme la liste est FERMEE (sealed), une chaine de instanceof qui
 * traite les 4 cas les traite TOUS. Chaque methode est recursive :
 * elle s'appelle sur les enfants.
 *
 *
 * ==================================================================
 * TODO 1 : eval(e)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (1 + 2) * -3 -> 3 * -3 = -9
 *
 * -- Le plan --
 *
 *   1. e instanceof Num n -> n.value() ; Add a -> eval(a.left()) + eval(a.right()) ;
 *      Mul m -> produit ; Neg g -> -eval(g.inner()).
 *   2. Dernier recours : throw new IllegalStateException() (jamais atteint grace a sealed).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : eval EST la boite, qui s'appelle elle-meme.
 *
 *
 * ==================================================================
 * TODO 2 : show(e)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (1 + 2) * -3 -> "((1 + 2) * -3)"      Num 5 -> "5"      Neg(Num 4) -> "-4"
 *
 * -- Le plan --
 *
 *   1. Num -> la valeur ; Add -> "(" + show(gauche) + " + " + show(droite) + ")" ; Mul pareil avec " * " ;
 *      Neg -> "-" + show(inner).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : simplify(e)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On simplifie d'abord les enfants, puis on applique 4 regles :
 *   x + 0 -> x ; 0 + x -> x ; x * 1 -> x ; 1 * x -> x ; -(-x) -> x.
 * Les records sont immuables : on fabrique de NOUVEAUX noeuds.
 *
 * -- Essayons a la main --
 *
 *   (x + 0) * 1 avec x = Num 7 -> Num 7       --(Num 2) -> Num 2
 *
 * -- Le plan --
 *
 *   1. Add a : l = simplify(a.left()), r = simplify(a.right()) ; si r est Num 0 -> l ; si l est Num 0 -> r ; sinon new Add(l, r).
 *   2. Mul : pareil avec 1.  3. Neg g : i = simplify(g.inner()) ; si i est un Neg -> son inner ; sinon new Neg(i).
 *   4. Num -> e.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isNum(e, value) (un Num de cette valeur ?), deja ecrite.
 *
 *
 * ==================================================================
 * TODO 4 : countNodes(e)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (1 + 2) * -3 -> Mul, Add, Num, Num, Neg, Num -> 6
 *
 * -- Le plan --
 *
 *   1. Num -> 1 ; Add/Mul -> 1 + gauche + droite ; Neg -> 1 + inner.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - if (e instanceof Add a) { ... a.left() ... } : le record donne ses accesseurs.
 *   - Deux records egaux (memes composants) sont equals : new Num(7).equals(new Num(7)) est vrai.
 */
public class Exercise12_ExpressionEvaluator {

    public sealed interface Expr permits Num, Add, Mul, Neg {
    }

    public record Num(int value) implements Expr {
    }

    public record Add(Expr left, Expr right) implements Expr {
    }

    public record Mul(Expr left, Expr right) implements Expr {
    }

    public record Neg(Expr inner) implements Expr {
    }

    public static int eval(Expr e) {
        throw new UnsupportedOperationException("TODO 1 : implementer eval()");
    }

    public static String show(Expr e) {
        throw new UnsupportedOperationException("TODO 2 : implementer show()");
    }

    public static Expr simplify(Expr e) {
        throw new UnsupportedOperationException("TODO 3 : implementer simplify()");
    }

    public static int countNodes(Expr e) {
        throw new UnsupportedOperationException("TODO 4 : implementer countNodes()");
    }

    static boolean isNum(Expr e, int value) {
        return e instanceof Num n && n.value() == value;
    }

    public static void main(String[] args) {
        Expr sample = new Mul(new Add(new Num(1), new Num(2)), new Neg(new Num(3)));
        ExerciseChecker.check("eval((1 + 2) * -3) == -9", eval(sample) == -9);
        ExerciseChecker.check("show : ((1 + 2) * -3), 5, -4",
                show(sample).equals("((1 + 2) * -3)") && show(new Num(5)).equals("5") && show(new Neg(new Num(4))).equals("-4"));
        ExerciseChecker.check("simplify((7 + 0) * 1) == Num 7", simplify(new Mul(new Add(new Num(7), new Num(0)), new Num(1))).equals(new Num(7)));
        ExerciseChecker.check("simplify(--2) == Num 2 et 0 + x", simplify(new Neg(new Neg(new Num(2)))).equals(new Num(2))
                && simplify(new Add(new Num(0), new Num(9))).equals(new Num(9)));
        ExerciseChecker.check("simplify ne change pas ce qui est deja simple", simplify(sample).equals(sample));
        ExerciseChecker.check("countNodes == 6", countNodes(sample) == 6);

        ExerciseChecker.summary();
    }
}
