package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un niveau abstrait intermediaire : tout ce qui est fait de pistes (album, podcast).
 * Il implemente minutes() mais laisse kind() aux classes concretes.
 */
public abstract class AudioMedia extends Media {

    private final String artist;
    protected final int[] tracks;   // durees en secondes

    protected AudioMedia(String title, int year, String[] tags, String artist, int[] tracks) {
        super(title, year, tags);
        this.artist = artist;
        this.tracks = tracks.clone();
    }

    @Override
    public int minutes() {
        int total = 0;
        for (int t : tracks) {
            total += t;
        }
        return (total + 59) / 60;   // arrondi a la minute superieure
    }

    @Override
    public boolean matches(String query) {
        return super.matches(query) || artist.equalsIgnoreCase(query);
    }

    // Sac a dos 0/1 sur les durees : la selection de pistes la plus longue sans depasser la limite.
    // best[s] = vrai si une combinaison de pistes dure exactement s secondes ; from[s] = la derniere piste ajoutee.
    public final String bestPlaylist(int limit) {
        boolean[] reachable = new boolean[limit + 1];
        int[] lastTrack = new int[limit + 1];
        int[] previous = new int[limit + 1];
        reachable[0] = true;
        for (int t = 0; t < tracks.length; t++) {
            for (int s = limit; s >= tracks[t]; s--) {   // a l'envers : chaque piste au plus une fois
                if (!reachable[s] && reachable[s - tracks[t]]) {
                    reachable[s] = true;
                    lastTrack[s] = t;
                    previous[s] = s - tracks[t];
                }
            }
        }
        int best = limit;
        while (!reachable[best]) {
            best--;
        }
        StringBuilder chosen = new StringBuilder();
        for (int s = best; s > 0; s = previous[s]) {
            chosen.insert(0, " " + (lastTrack[s] + 1));
        }
        return "pistes" + chosen + " = " + best / 60 + " min " + best % 60 + " s";
    }
}
