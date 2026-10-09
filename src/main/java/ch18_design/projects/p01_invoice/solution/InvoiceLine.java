package ch18_design.projects.p01_invoice.solution;

/**
 * Une ligne de facture. Le legacy testait p[0].equals("PART") partout ; ici, chaque sorte de ligne
 * SAIT son libelle, son prix et sa famille : le calcul et l'impression ne demandent plus "qui es-tu ?".
 */
public sealed interface InvoiceLine permits Part, Labor, Fee {

    String label();

    Money price();

    Category category();

    // Seule la main-d'oeuvre a des minutes : une methode par defaut evite un instanceof dans le calcul.
    default int billedMinutes() {
        return 0;
    }
}
