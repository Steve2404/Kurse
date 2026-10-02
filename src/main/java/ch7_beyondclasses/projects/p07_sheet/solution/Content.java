package ch7_beyondclasses.projects.p07_sheet.solution;

/**
 * SOLUTION - le contenu d'une cellule : scelle, trois formes possibles, chacune un record.
 */
public sealed interface Content permits Content.Number, Content.Text, Content.Formula {

    record Number(double value) implements Content {
    }

    record Text(String text) implements Content {
    }

    record Formula(String source, Expr expr) implements Content {
    }
}
