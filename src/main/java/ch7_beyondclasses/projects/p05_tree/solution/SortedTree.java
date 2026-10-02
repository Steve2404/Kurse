package ch7_beyondclasses.projects.p05_tree.solution;

/**
 * SOLUTION - un arbre binaire de recherche d'entiers, qui montre les 4 sortes de classes imbriquees.
 */
public class SortedTree {

    // 1. Interface IMBRIQUEE (implicitement static) : ce qu'on fait de chaque valeur visitee.
    public interface Visitor {
        void visit(int value, int depth);
    }

    // 2. Classe imbriquee STATIC : elle n'a pas besoin d'un SortedTree pour exister.
    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    // Un builder STATIC imbrique : new SortedTree.Builder().add(...).build().
    public static class Builder {
        private final SortedTree tree = new SortedTree();

        public Builder add(int... values) {
            for (int v : values) {
                tree.insert(v);
            }
            return this;
        }

        public SortedTree build() {
            return tree;
        }
    }

    // 3. Classe INTERNE (non static) : chaque Cursor est lie a UN arbre, et lit ses champs (SortedTree.this.root).
    public class Cursor {
        private final Node[] stack = new Node[size + 1];
        private int top;

        Cursor() {
            pushLeft(SortedTree.this.root);
        }

        private void pushLeft(Node n) {
            while (n != null) {
                stack[top++] = n;
                n = n.left;
            }
        }

        public boolean hasNext() {
            return top > 0;
        }

        // Parcours infixe iteratif : on depile, puis on descend a gauche du sous-arbre droit.
        public int next() {
            Node n = stack[--top];
            pushLeft(n.right);
            return n.value;
        }
    }

    private Node root;
    private int size;

    public Cursor cursor() {
        return new Cursor();   // depuis une methode d'instance, this est implicite : this.new Cursor()
    }

    public int size() {
        return size;
    }

    public boolean insert(int v) {
        if (root == null) {
            root = new Node(v);
            size++;
            return true;
        }
        Node n = root;
        while (true) {
            if (v == n.value) {
                return false;                    // doublon ignore
            }
            if (v < n.value) {
                if (n.left == null) {
                    n.left = new Node(v);
                    size++;
                    return true;
                }
                n = n.left;
            } else {
                if (n.right == null) {
                    n.right = new Node(v);
                    size++;
                    return true;
                }
                n = n.right;
            }
        }
    }

    public boolean remove(int v) {
        int before = size;
        root = remove(root, v);
        return size < before;
    }

    // Trois cas : feuille, un seul enfant, deux enfants (remplacer par le successeur = le minimum a droite).
    private Node remove(Node n, int v) {
        if (n == null) {
            return null;
        }
        if (v < n.value) {
            n.left = remove(n.left, v);
        } else if (v > n.value) {
            n.right = remove(n.right, v);
        } else if (n.left == null || n.right == null) {
            size--;
            return n.left != null ? n.left : n.right;
        } else {
            Node succ = n.right;
            while (succ.left != null) {
                succ = succ.left;
            }
            n.value = succ.value;
            n.right = remove(n.right, succ.value);
        }
        return n;
    }

    public int height() {
        return height(root);
    }

    private int height(Node n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }

    // Parcours avec un Visitor : droite, noeud, gauche (pour dessiner l'arbre couche sur le cote).
    public void visitSideways(Visitor v) {
        sideways(root, 0, v);
    }

    private void sideways(Node n, int depth, Visitor v) {
        if (n == null) {
            return;
        }
        sideways(n.right, depth + 1, v);
        v.visit(n.value, depth);
        sideways(n.left, depth + 1, v);
    }

    // Parcours en largeur, avec une file faite d'un tableau.
    public String levels() {
        StringBuilder sb = new StringBuilder();
        Node[] queue = new Node[size];
        int[] depth = new int[size];
        int head = 0;
        int tail = 0;
        if (root != null) {
            queue[tail++] = root;
        }
        int current = -1;
        while (head < tail) {
            Node n = queue[head];
            int d = depth[head++];
            sb.append(d != current ? (current < 0 ? "" : " | ") : " ").append(n.value);
            current = d;
            if (n.left != null) {
                depth[tail] = d + 1;
                queue[tail++] = n.left;
            }
            if (n.right != null) {
                depth[tail] = d + 1;
                queue[tail++] = n.right;
            }
        }
        return sb.toString();
    }

    // 4. Classe LOCALE : declaree dans la methode, elle lit low et high (parametres effectively final).
    public int countBetween(int low, int high) {
        class RangeCounter {
            int count;

            void walk(Node n) {
                if (n == null) {
                    return;
                }
                if (n.value > low) {
                    walk(n.left);              // elagage : inutile d'aller a gauche si la valeur est <= low
                }
                if (n.value >= low && n.value <= high) {
                    count++;
                }
                if (n.value < high) {
                    walk(n.right);
                }
            }
        }
        RangeCounter counter = new RangeCounter();
        counter.walk(root);
        return counter.count;
    }

    // Plancher : la plus grande valeur <= v (ou Integer.MIN_VALUE s'il n'y en a pas).
    public int floor(int v) {
        int best = Integer.MIN_VALUE;
        for (Node n = root; n != null; n = v < n.value ? n.left : n.right) {
            if (n.value == v) {
                return v;
            }
            if (n.value < v) {
                best = n.value;
            }
        }
        return best;
    }

    public int ceiling(int v) {
        int best = Integer.MAX_VALUE;
        for (Node n = root; n != null; n = v < n.value ? n.left : n.right) {
            if (n.value == v) {
                return v;
            }
            if (n.value > v) {
                best = n.value;
            }
        }
        return best;
    }

    // Plus proche ancetre commun dans un ABR : on descend tant que a et b sont du meme cote.
    public int commonAncestor(int a, int b) {
        Node n = root;
        while (n != null) {
            if (a < n.value && b < n.value) {
                n = n.left;
            } else if (a > n.value && b > n.value) {
                n = n.right;
            } else {
                return n.value;
            }
        }
        return Integer.MIN_VALUE;
    }

    // Reconstruction equilibree : le milieu du tableau trie devient la racine, recursivement.
    public static SortedTree balanced(int[] sorted) {
        SortedTree t = new SortedTree();
        t.root = build(sorted, 0, sorted.length - 1);
        t.size = sorted.length;
        return t;
    }

    private static Node build(int[] a, int lo, int hi) {
        if (lo > hi) {
            return null;
        }
        int mid = (lo + hi) / 2;
        Node n = new Node(a[mid]);
        n.left = build(a, lo, mid - 1);
        n.right = build(a, mid + 1, hi);
        return n;
    }
}
