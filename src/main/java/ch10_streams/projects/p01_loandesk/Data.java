package ch10_streams.projects.p01_loandesk;

import java.util.List;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier). Ce sont des lignes de
 * texte brutes, comme lues dans un fichier : a toi de les transformer.
 */
public final class Data {

    /** isbn;titre;auteur;exemplaires disponibles */
    public static final List<String> BOOKS = List.of(
            "B1;Dune;Herbert;3",
            "B2;Fondation;Asimov;2",
            "B4;Le Petit Prince;Saint-Exupery;1",
            "B8;Le Hobbit;Tolkien;0");

    /** id;nom;email (l'email peut etre vide ou ne contenir que des espaces : pas d'email) */
    public static final List<String> MEMBERS = List.of(
            "M1;Lea;lea@mail.fr",
            "M2;Hugo;",
            "M3;Ines;ines@biblio.org",
            "M4;Tom;  ");

    /** Les commandes du guichet, dans l'ordre d'arrivee. */
    public static final List<String> COMMANDS = List.of(
            "EMPRUNT M1 B1",
            "EMPRUNT M1 B1",
            "EMPRUNT M9 B1",
            "EMPRUNT M2 B7",
            "EMPRUNT M2 B4",
            "EMPRUNT M3 B4",
            "EMPRUNT M4 B4",
            "RETOUR M2 B4 6",
            "RETOUR M1 B1 0",
            "RETOUR M1 B1 0",
            "CONTACT M2",
            "CONTACT M4",
            "CONTACT M1",
            "CONTACT M7",
            "INFO Fondation",
            "INFO B4",
            "INFO Silmarillion",
            "RETOUR M3 B4 20",
            "RETOUR M4 B4 40",
            "RENOUVELER M1 B1",
            "BILAN");

    private Data() {
    }
}
