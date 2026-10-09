package ch19_final.projects.p05_resilience.solution;

/** Une reponse de stock, et d'ou elle vient : "fournisseur", ou "cache (12 s)" quand on se replie sur la derniere valeur connue. */
public record StockAnswer(String ref, int quantity, String source) {
}
