package ch19_final.projects.p03_api;

import java.util.List;

/** Les donnees FOURNIES du projet 3 (ne pas modifier) : les taches de depart de l'atelier (titre, colonne, points). */
public final class Data {

    private Data() {
    }

    public static final List<String[]> SEED = List.of(
            new String[]{"Changer la chaine", "Fini", "2"},
            new String[]{"Regler les freins", "En cours", "3"},
            new String[]{"Commander 12 chambres a air", "A faire", "1"},
            new String[]{"Facture Dupont", "A faire", "1"},
            new String[]{"Remise 50% sur les antivols", "A faire", "2"},
            new String[]{"Remise 500 euros velo cargo", "En cours", "5"},
            new String[]{"Graisser le pedalier", "Fini", "1"});
}
