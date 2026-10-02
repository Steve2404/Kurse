package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - la racine abstraite de la mediatheque.
 */
public abstract class Media {

    private static int created;

    private final int id;
    private final String title;
    private final int year;
    private final String[] tags;

    protected Media(String title, int year, String[] tags) {
        this.id = ++created;
        this.title = title;
        this.year = year;
        this.tags = tags.clone();   // copie defensive : le tableau de l'appelant peut changer ensuite
    }

    public abstract String kind();

    public abstract int minutes();

    // Recherche par defaut : titre ou tags. Les sous-classes l'etendent (auteur, realisateur...).
    public boolean matches(String query) {
        String q = query.toLowerCase();
        if (title.toLowerCase().contains(q)) {
            return true;
        }
        for (String t : tags) {
            if (t.contains(q)) {
                return true;
            }
        }
        return false;
    }

    // Similarite de Jaccard : |intersection| / |union| des tags, en pourcentage entier.
    public final int similarity(Media other) {
        int common = 0;
        for (String a : tags) {
            for (String b : other.tags) {
                if (a.equals(b)) {
                    common++;
                }
            }
        }
        int union = tags.length + other.tags.length - common;
        return union == 0 ? 0 : 100 * common / union;
    }

    public final int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public static int created() {
        return created;
    }

    // Deux medias sont egaux s'ils ont le meme type, le meme titre et la meme annee (l'id ne compte pas).
    @Override
    public boolean equals(Object o) {
        return o instanceof Media m && m.kind().equals(kind()) && m.title.equals(title) && m.year == year;
    }

    @Override
    public int hashCode() {
        return (kind().hashCode() * 31 + title.hashCode()) * 31 + year;
    }

    @Override
    public String toString() {
        return "[" + kind() + "] " + title + " (" + year + ") " + minutes() + " min";
    }
}
