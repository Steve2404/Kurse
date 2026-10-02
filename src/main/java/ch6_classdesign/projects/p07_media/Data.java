package ch6_classdesign.projects.p07_media;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 * Une ligne par media, champs separes par '|' :
 * B|titre|annee|tags|auteur|pages|isbn        (livre)
 * M|titre|annee|tags|realisateur|minutes      (film)
 * A|titre|annee|tags|artiste|secondes,...     (album : duree de chaque piste)
 * P|titre|annee|tags|animateur|secondes,...   (podcast : duree de chaque episode)
 */
public final class Data {

    public static final String[] MEDIA = {
            "B|Le Petit Prince|1943|conte,philosophie,enfance|Saint-Exupery|96|9782070612758",
            "M|Inception|2010|science-fiction,reve,action|Nolan|148",
            "A|Kind of Blue|1959|jazz,modal,trompette|Miles Davis|562,577,326,693,565",
            "B|Dune|1965|science-fiction,desert,politique|Herbert|600|9782266320482",
            "M|Interstellar|2014|science-fiction,espace,famille|Nolan|169",
            "P|Code Story|2021|tech,entretien|Cerise|1800,2100,1500",
            "M|Inception|2010|science-fiction,reve,action|Nolan|148",
            "B|Fondation|1951|science-fiction,espace,politique|Asimov|255|9782070360536",
            "A|Random Access Memories|2013|electro,disco|Daft Punk|271,369,337,322,248"};

    /** Les recherches a lancer. */
    public static final String[] QUERIES = {"science", "Nolan", "Asimov", "jazz"};

    /** Le titre pour lequel on cherche des recommandations. */
    public static final String LIKED = "Dune";

    /** La duree maximale d'une playlist, en secondes. */
    public static final int PLAYLIST_LIMIT = 1800;

    private Data() {
    }
}
