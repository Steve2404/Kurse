package ch7_beyondclasses.projects.p07_sheet;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 * "REF=contenu" : un nombre, un texte (commence par '), ou une formule (+ - * /, parentheses, references, SUM(X1:Y2)).
 * La feuille fait 4 colonnes (A a D) et 3 lignes (1 a 3).
 */
public final class Data {

    public static final String[] CELLS = {
            "A1=10", "A2=20", "A3=30",
            "B1=A1*2", "B2=SUM(A1:A3)", "B3=B2/A1+B1",
            "C1='Total", "C2=(B3-A3)*0.5", "C3=SUM(A1:B2)",
            "D1=D2+1", "D2=D1*2", "D3=E9+C2"};

    /** La modification appliquee ensuite, puis le recalcul. */
    public static final String CHANGE = "A1=5";

    private Data() {
    }
}
