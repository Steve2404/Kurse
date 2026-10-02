package ch7_beyondclasses.projects.p07_sheet.solution;

/**
 * SOLUTION - l'arbre d'une formule : une interface scellee dont les implementations sont des records IMBRIQUES.
 * Un record imbrique est implicitement static ; permits peut alors etre omis (tout est dans le meme fichier).
 */
public sealed interface Expr {

    record Num(double value) implements Expr {
    }

    record Cell(Ref ref) implements Expr {
    }

    record Binary(Op op, Expr left, Expr right) implements Expr {
    }

    record Sum(Ref from, Ref to) implements Expr {
    }

    // Les references dont depend l'expression, ajoutees dans out (taille rendue).
    static int references(Expr e, Ref[] out, int n) {
        if (e instanceof Cell c) {
            out[n++] = c.ref();
        } else if (e instanceof Binary b) {
            n = references(b.left(), out, n);
            n = references(b.right(), out, n);
        } else if (e instanceof Sum s) {
            for (int col = s.from().col(); col <= s.to().col(); col++) {
                for (int row = s.from().row(); row <= s.to().row(); row++) {
                    out[n++] = new Ref(col, row);
                }
            }
        }
        return n;
    }
}
