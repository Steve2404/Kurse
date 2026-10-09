package ch19_final.projects.p03_api.solution;

/** La tache a change depuis qu'on l'a lue (verrou optimiste, projet 2) : l'API repondra 409. */
public class ConflictException extends RuntimeException {

    public ConflictException(long id, int staleVersion) {
        super("tache " + id + " : version " + staleVersion + " perimee");
    }
}
