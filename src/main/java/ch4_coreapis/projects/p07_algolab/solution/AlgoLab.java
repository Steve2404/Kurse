package ch4_coreapis.projects.p07_algolab.solution;

import ch4_coreapis.projects.p07_algolab.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 7 - le laboratoire d'algorithmes sur tableaux.
 */
public class AlgoLab {

    public static void main(String[] args) {
        sorting();
        searching();
        sums();
        sieve();
        matrices();
        pascal();
        maze();
    }

    // ---------------------------------------------------------------- 1. tris
    static void sorting() {
        // On trie toujours une COPIE : Data.NUMBERS sert encore plus loin, dans l'ordre d'origine.
        int[] a = Arrays.copyOf(Data.NUMBERS, Data.NUMBERS.length);
        int shifts = 0;
        for (int i = 1; i < a.length; i++) {
            int key = a[i];
            int j = i - 1;
            // Piege : la condition a[j] > key (strict) garde le tri STABLE et n'echange pas les egaux.
            while (j >= 0 && a[j] > key) {
                a[j + 1] = a[j];
                j--;
                shifts++;
            }
            a[j + 1] = key;
        }
        System.out.println("insertion : " + Arrays.toString(a) + " decalages=" + shifts);

        int[] b = Data.NUMBERS.clone();
        int swaps = 0;
        for (int i = 0; i < b.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < b.length; j++) {
                if (b[j] < b[min]) {
                    min = j;
                }
            }
            if (min != i) {           // on ne compte que les vrais echanges
                int tmp = b[i];
                b[i] = b[min];
                b[min] = tmp;
                swaps++;
            }
        }
        int[] reference = Arrays.copyOf(Data.NUMBERS, Data.NUMBERS.length);
        Arrays.sort(reference);
        System.out.println("selection : echanges=" + swaps + " identique a Arrays.sort " + Arrays.equals(a, reference)
                + " " + (Arrays.mismatch(b, reference) == -1) + " original intact " + (Data.NUMBERS[0] == 29));

        // Dedoublonnage EN PLACE d'un tableau trie : k = nombre de valeurs uniques deja ecrites.
        int k = 1;
        for (int i = 1; i < a.length; i++) {
            if (a[i] != a[k - 1]) {
                a[k++] = a[i];
            }
        }
        System.out.println("uniques : " + Arrays.toString(Arrays.copyOf(a, k)) + " (" + k + " sur " + a.length + ")");
    }

    // ---------------------------------------------------------------- 2. recherche
    static void searching() {
        int[] sorted = Arrays.copyOf(Data.NUMBERS, Data.NUMBERS.length);
        Arrays.sort(sorted);
        // Deux pointeurs sur un tableau trie : O(n) au lieu de O(n^2) avec deux boucles.
        StringBuilder pairs = new StringBuilder();
        int left = 0;
        int right = sorted.length - 1;
        int steps = 0;
        while (left < right) {
            steps++;
            int sum = sorted[left] + sorted[right];
            if (sum == Data.TARGET) {
                pairs.append(sorted[left]).append('+').append(sorted[right]).append(' ');
                int l = sorted[left];
                int r = sorted[right];
                while (left < right && sorted[left] == l) {   // saute les doublons a gauche
                    left++;
                }
                while (left < right && sorted[right] == r) {  // et a droite
                    right--;
                }
            } else if (sum < Data.TARGET) {
                left++;
            } else {
                right--;
            }
        }
        System.out.println("paires de somme " + Data.TARGET + " : " + pairs.toString().strip() + " en " + steps + " etapes");

        // La borne inferieure : le premier indice dont la valeur est >= cible (marche aussi avec des doublons).
        int[] queries = {8, 9, 0, 50};
        StringBuilder out = new StringBuilder("borne inferieure :");
        for (int q : queries) {
            int low = 0;
            int high = sorted.length;          // intervalle [low, high[
            while (low < high) {
                int mid = (low + high) >>> 1;  // >>> 1 : pas de debordement de low + high
                if (sorted[mid] < q) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            out.append(' ').append(q).append("->").append(low);
        }
        System.out.println(out);
    }

    // ---------------------------------------------------------------- 3. sommes
    static void sums() {
        int[] n = Data.NUMBERS;
        // prefix[i] = somme des i premiers elements ; la somme de [a, b] vaut prefix[b + 1] - prefix[a].
        int[] prefix = new int[n.length + 1];
        for (int i = 0; i < n.length; i++) {
            prefix[i + 1] = prefix[i] + n[i];
        }
        StringBuilder out = new StringBuilder("sommes d'intervalle :");
        for (int[] range : Data.RANGES) {
            out.append(" [").append(range[0]).append(',').append(range[1]).append("]=").append(prefix[range[1] + 1] - prefix[range[0]]);
        }
        System.out.println(out);

        // Fenetre glissante : on ajoute l'entrant et on retire le sortant, sans tout re-sommer.
        int window = 0;
        for (int i = 0; i < Data.WINDOW; i++) {
            window += n[i];
        }
        int best = window;
        int bestStart = 0;
        for (int i = Data.WINDOW; i < n.length; i++) {
            window += n[i] - n[i - Data.WINDOW];
            if (window > best) {
                best = window;
                bestStart = i - Data.WINDOW + 1;
            }
        }
        System.out.println("fenetre de " + Data.WINDOW + " : max=" + best + " a partir de l'indice " + bestStart + " "
                + Arrays.toString(Arrays.copyOfRange(n, bestStart, bestStart + Data.WINDOW)));

        // Kadane : la meilleure somme qui FINIT ici est soit l'element seul, soit l'element + la meilleure d'avant.
        int[] p = Data.PROFITS;
        int current = p[0];
        int max = p[0];
        int start = 0;
        int from = 0;
        int to = 0;
        for (int i = 1; i < p.length; i++) {
            if (current + p[i] < p[i]) {
                current = p[i];
                start = i;
            } else {
                current += p[i];
            }
            if (current > max) {
                max = current;
                from = start;
                to = i;
            }
        }
        System.out.println("kadane : max=" + max + " jours " + from + " a " + to + " " + Arrays.toString(Arrays.copyOfRange(p, from, to + 1)));
    }

    // ---------------------------------------------------------------- 4. crible
    static void sieve() {
        // composite[i] vaut true si i n'est pas premier ; un boolean[] neuf est rempli de false.
        boolean[] composite = new boolean[Data.SIEVE_LIMIT + 1];
        composite[0] = true;
        composite[1] = true;
        for (int i = 2; i * i <= Data.SIEVE_LIMIT; i++) {
            if (!composite[i]) {
                for (int m = i * i; m <= Data.SIEVE_LIMIT; m += i) {  // on part de i*i : les plus petits sont deja barres
                    composite[m] = true;
                }
            }
        }
        StringBuilder primes = new StringBuilder();
        int count = 0;
        int twins = 0;
        for (int i = 2; i <= Data.SIEVE_LIMIT; i++) {
            if (!composite[i]) {
                primes.append(count == 0 ? "" : ",").append(i);
                count++;
                if (i + 2 <= Data.SIEVE_LIMIT && !composite[i + 2]) {
                    twins++;
                }
            }
        }
        System.out.println("premiers <= " + Data.SIEVE_LIMIT + " (" + count + ") : " + primes);
        System.out.println("jumeaux : " + twins + " paires");
    }

    // ---------------------------------------------------------------- 5. matrices
    static void matrices() {
        int[][] m = Data.MATRIX;
        int rows = m.length;
        int cols = m[0].length;
        int[][] transposed = new int[cols][rows];
        int[][] rotated = new int[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                transposed[c][r] = m[r][c];
                rotated[c][rows - 1 - r] = m[r][c];   // rotation horaire = transposee puis lignes inversees
            }
        }
        System.out.println("transposee : " + Arrays.deepToString(transposed));
        System.out.println("rotation : " + Arrays.deepToString(rotated));

        // Spirale : quatre bornes qui se resserrent ; les deux tests evitent de repasser sur une ligne ou colonne seule.
        StringBuilder spiral = new StringBuilder();
        int top = 0;
        int bottom = rows - 1;
        int left = 0;
        int right = cols - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) {
                spiral.append(m[top][c]).append(' ');
            }
            top++;
            for (int r = top; r <= bottom; r++) {
                spiral.append(m[r][right]).append(' ');
            }
            right--;
            if (top <= bottom) {
                for (int c = right; c >= left; c--) {
                    spiral.append(m[bottom][c]).append(' ');
                }
                bottom--;
            }
            if (left <= right) {
                for (int r = bottom; r >= top; r--) {
                    spiral.append(m[r][left]).append(' ');
                }
                left++;
            }
        }
        System.out.println("spirale : " + spiral.toString().strip());

        int diagonal = 0;
        for (int i = 0; i < Math.min(rows, cols); i++) {
            diagonal += m[i][i];
        }
        System.out.println("diagonale : " + diagonal + " transposee de la transposee identique " + Arrays.deepEquals(m, transpose(transposed)));
    }

    static int[][] transpose(int[][] m) {
        int[][] t = new int[m[0].length][m.length];
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[0].length; c++) {
                t[c][r] = m[r][c];
            }
        }
        return t;
    }

    // ---------------------------------------------------------------- 6. Pascal
    static void pascal() {
        // Tableau IRREGULIER : la ligne r a r + 1 cases ; chaque case interieure = somme des deux du dessus.
        int[][] tri = new int[Data.PASCAL_ROWS][];
        for (int r = 0; r < tri.length; r++) {
            tri[r] = new int[r + 1];
            tri[r][0] = 1;
            tri[r][r] = 1;
            for (int c = 1; c < r; c++) {
                tri[r][c] = tri[r - 1][c - 1] + tri[r - 1][c];
            }
        }
        int width = Arrays.toString(tri[tri.length - 1]).length();
        for (int[] row : tri) {
            String text = Arrays.toString(row);
            // Centrage : (largeur - longueur) / 2 espaces devant.
            System.out.println(" ".repeat((width - text.length()) / 2) + text);
        }
        int sum = 0;
        for (int v : tri[tri.length - 1]) {
            sum += v;
        }
        System.out.println("somme de la derniere ligne : " + sum + " = 2^" + (tri.length - 1) + " " + (sum == (int) Math.pow(2, tri.length - 1)));
    }

    // ---------------------------------------------------------------- 7. labyrinthe (parcours en largeur)
    static void maze() {
        int rows = Data.MAZE.length;
        int cols = Data.MAZE[0].length();
        char[][] grid = new char[rows][];
        int start = -1;
        int end = -1;
        for (int r = 0; r < rows; r++) {
            grid[r] = Data.MAZE[r].toCharArray();   // une copie modifiable de chaque ligne
            if (Data.MAZE[r].indexOf('S') >= 0) {
                start = r * cols + Data.MAZE[r].indexOf('S');
            }
            if (Data.MAZE[r].indexOf('E') >= 0) {
                end = r * cols + Data.MAZE[r].indexOf('E');
            }
        }
        // Une case est codee par un seul int : r * cols + c. La file est un simple tableau avec deux indices.
        int[] queue = new int[rows * cols];
        int[] previous = new int[rows * cols];
        int[] distance = new int[rows * cols];
        Arrays.fill(distance, -1);              // -1 : pas encore visitee
        int head = 0;
        int tail = 0;
        queue[tail++] = start;
        distance[start] = 0;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        int visited = 0;
        while (head < tail) {
            int cell = queue[head++];
            visited++;
            if (cell == end) {
                break;
            }
            int r = cell / cols;
            int c = cell % cols;
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] == '#') {
                    continue;
                }
                int next = nr * cols + nc;
                if (distance[next] == -1) {
                    distance[next] = distance[cell] + 1;
                    previous[next] = cell;
                    queue[tail++] = next;
                }
            }
        }
        System.out.println("labyrinthe : plus court chemin = " + distance[end] + " pas, " + visited + " cases explorees");
        // On remonte le chemin depuis la sortie grace a previous[].
        for (int cell = previous[end]; cell != start; cell = previous[cell]) {
            grid[cell / cols][cell % cols] = '*';
        }
        for (char[] line : grid) {
            System.out.println("  " + new String(line));
        }
    }
}
