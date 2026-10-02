package ch6_classdesign.projects.p07_media.solution;

import ch6_classdesign.projects.p07_media.Data;

/**
 * SOLUTION du projet 7 (capstone) - la mediatheque.
 */
public class MediaApp {

    static Media create(String line) {
        String[] p = line.split("\\|");
        String title = p[1];
        int year = Integer.parseInt(p[2]);
        String[] tags = p[3].split(",");
        return switch (p[0]) {
            case "B" -> new Book(title, year, tags, p[4], Integer.parseInt(p[5]), p[6]);
            case "M" -> new Movie(title, year, tags, p[4], Integer.parseInt(p[5]));
            case "A" -> new Album(title, year, tags, p[4], seconds(p[5]));
            default -> new Podcast(title, year, tags, p[4], seconds(p[5]));
        };
    }

    static int[] seconds(String csv) {
        String[] parts = csv.split(",");
        int[] s = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            s[i] = Integer.parseInt(parts[i]);
        }
        return s;
    }

    public static void main(String[] args) {
        Media[] catalog = new Media[Data.MEDIA.length];
        int size = 0;
        StringBuilder duplicates = new StringBuilder();
        for (String line : Data.MEDIA) {
            Media m = create(line);
            boolean known = false;
            for (int i = 0; i < size; i++) {
                known |= catalog[i].equals(m);   // equals redefini : meme type, titre et annee
            }
            if (known) {
                duplicates.append(' ').append(m.getTitle()).append(" #").append(m.getId());
            } else {
                catalog[size++] = m;
            }
        }
        for (int i = 0; i < size; i++) {
            System.out.println("#" + catalog[i].getId() + " " + catalog[i]);
        }
        System.out.println("doublons ignores :" + duplicates + " ; objets crees " + Media.created() + ", catalogue " + size);

        // Tri : annee decroissante, puis titre.
        Media[] sorted = new Media[size];
        System.arraycopy(catalog, 0, sorted, 0, size);
        for (int i = 1; i < size; i++) {
            Media key = sorted[i];
            int j = i - 1;
            while (j >= 0 && (sorted[j].getYear() < key.getYear()
                    || sorted[j].getYear() == key.getYear() && sorted[j].getTitle().compareTo(key.getTitle()) > 0)) {
                sorted[j + 1] = sorted[j];
                j--;
            }
            sorted[j + 1] = key;
        }
        StringBuilder byYear = new StringBuilder("du plus recent :");
        for (Media m : sorted) {
            byYear.append(' ').append(m.getYear());
        }
        System.out.println(byYear);

        for (String q : Data.QUERIES) {
            StringBuilder found = new StringBuilder("recherche " + q + " :");
            for (int i = 0; i < size; i++) {
                if (catalog[i].matches(q)) {    // chaque objet applique SA version de matches
                    found.append(" [").append(catalog[i].getTitle()).append(']');
                }
            }
            System.out.println(found);
        }

        StringBuilder isbns = new StringBuilder("isbn :");
        int[] minutesByKind = new int[4];
        String[] kinds = {"livre", "film", "album", "podcast"};
        for (int i = 0; i < size; i++) {
            Media m = catalog[i];
            if (m instanceof Book b) {
                isbns.append(' ').append(b.getIsbn()).append(b.getIsbn().isValid() ? " ok" : " FAUX (cle attendue " + b.getIsbn().expectedCheck() + ")");
            }
            for (int k = 0; k < kinds.length; k++) {
                if (kinds[k].equals(m.kind())) {
                    minutesByKind[k] += m.minutes();
                }
            }
        }
        System.out.println(isbns);
        StringBuilder totals = new StringBuilder("minutes :");
        for (int k = 0; k < kinds.length; k++) {
            totals.append(' ').append(kinds[k]).append('=').append(minutesByKind[k]);
        }
        System.out.println(totals);

        // Recommandations : les 3 medias les plus proches de LIKED (similarite decroissante, puis titre).
        Media liked = null;
        for (int i = 0; i < size; i++) {
            if (catalog[i].getTitle().equals(Data.LIKED)) {
                liked = catalog[i];
            }
        }
        Media[] others = new Media[size - 1];
        int n = 0;
        for (int i = 0; i < size; i++) {
            if (catalog[i] != liked) {
                others[n++] = catalog[i];
            }
        }
        for (int i = 1; i < n; i++) {
            Media key = others[i];
            int j = i - 1;
            while (j >= 0 && (liked.similarity(others[j]) < liked.similarity(key)
                    || liked.similarity(others[j]) == liked.similarity(key) && others[j].getTitle().compareTo(key.getTitle()) > 0)) {
                others[j + 1] = others[j];
                j--;
            }
            others[j + 1] = key;
        }
        StringBuilder recos = new StringBuilder("si vous aimez " + Data.LIKED + " :");
        for (int i = 0; i < 3; i++) {
            recos.append(' ').append(others[i].getTitle()).append(" (").append(liked.similarity(others[i])).append("%)");
        }
        System.out.println(recos);

        for (int i = 0; i < size; i++) {
            if (catalog[i] instanceof AudioMedia a) {
                System.out.println("playlist " + a.getTitle() + " <= " + Data.PLAYLIST_LIMIT / 60 + " min : " + a.bestPlaylist(Data.PLAYLIST_LIMIT));
            }
        }
    }
}
