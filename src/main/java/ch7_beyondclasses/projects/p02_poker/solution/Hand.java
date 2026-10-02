package ch7_beyondclasses.projects.p02_poker.solution;

/**
 * SOLUTION - une main evaluee de 5 cartes : un record avec un constructeur COMPACT qui calcule.
 * key = les valeurs de rangs ordonnees pour departager (groupes les plus gros d'abord, puis les plus hauts).
 */
public record Hand(Card[] cards, Category category, int[] key) {

    // Constructeur compact : pas de liste de parametres ; on peut lire et REMPLACER les parametres
    // avant l'affectation automatique des champs. Ici : copies defensives des tableaux.
    public Hand {
        cards = cards.clone();
        key = key.clone();
    }

    // Un constructeur supplementaire doit appeler le canonique avec this(...).
    public Hand(Card[] cards) {
        this(cards, evaluateCategory(cards), tieBreak(cards));
    }

    private static int[] counts(Card[] cards) {
        int[] counts = new int[15];
        for (Card c : cards) {
            counts[c.rank().value()]++;
        }
        return counts;
    }

    // Les valeurs triees : d'abord par nombre d'exemplaires decroissant, puis par valeur decroissante.
    static int[] tieBreak(Card[] cards) {
        int[] counts = counts(cards);
        int[] order = new int[cards.length];
        int n = 0;
        for (int size = 4; size >= 1; size--) {
            for (int v = 14; v >= 2; v--) {
                if (counts[v] == size) {
                    order[n++] = v;
                }
            }
        }
        int[] result = new int[n];
        System.arraycopy(order, 0, result, 0, n);
        if (isStraight(cards) && result[0] == 14 && result[1] == 5) {
            return new int[] {5, 4, 3, 2, 1};   // la roue A-2-3-4-5 : l'As compte 1
        }
        return result;
    }

    static boolean isStraight(Card[] cards) {
        int[] counts = counts(cards);
        if (counts[14] == 1 && counts[2] == 1 && counts[3] == 1 && counts[4] == 1 && counts[5] == 1) {
            return true;
        }
        for (int low = 2; low <= 10; low++) {
            boolean run = true;
            for (int v = low; v < low + 5; v++) {
                run &= counts[v] == 1;
            }
            if (run) {
                return true;
            }
        }
        return false;
    }

    static Category evaluateCategory(Card[] cards) {
        int[] counts = counts(cards);
        int[] groups = new int[5];      // les tailles de groupes, triees decroissantes
        int g = 0;
        for (int size = 4; size >= 1; size--) {
            for (int v = 2; v <= 14; v++) {
                if (counts[v] == size) {
                    groups[g++] = size;
                }
            }
        }
        boolean flush = true;
        for (Card c : cards) {
            flush &= c.suit() == cards[0].suit();   // == sur des enums : chaque constante est unique
        }
        return Category.of(groups, flush, isStraight(cards));
    }

    // Comparaison : la categorie d'abord (compareTo d'un enum = difference des ordinal), puis la cle.
    public int compareTo(Hand other) {
        int c = category.compareTo(other.category);
        if (c != 0) {
            return c;
        }
        for (int i = 0; i < key.length; i++) {
            if (key[i] != other.key[i]) {
                return key[i] - other.key[i];
            }
        }
        return 0;
    }

    // Les champs d'un record ne se modifient pas ; l'accesseur rend une copie pour proteger le tableau.
    @Override
    public Card[] cards() {
        return cards.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards) {
            sb.append(c).append(' ');
        }
        return sb.toString().strip() + " -> " + category.label();
    }
}
