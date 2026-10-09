package ch19_final.projects.p08_atelier.solution;

/** Quelqu'un a modifie la tache entre notre lecture et notre ecriture : il faut relire, puis recommencer. */
public class ConflictException extends StoreException {

    public ConflictException(long id, int staleVersion) {
        super("tache " + id + " : version " + staleVersion + " perimee");
    }
}
