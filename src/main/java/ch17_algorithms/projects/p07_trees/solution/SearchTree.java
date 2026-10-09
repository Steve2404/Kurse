package ch17_algorithms.projects.p07_trees.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Un arbre binaire de recherche : a gauche de chaque noeud, les cles plus petites ; a droite, les plus grandes.
 * Toutes les operations descendent UN chemin : O(hauteur), soit O(log n) si l'arbre est equilibre.
 */
public final class SearchTree<K extends Comparable<K>> {

    private static final class Node<K> {
        final K key;
        Node<K> left;
        Node<K> right;

        Node(K key) {
            this.key = key;
        }
    }

    private Node<K> root;
    private int size;

    public boolean add(K key) {
        if (root == null) {
            root = new Node<>(key);
            size++;
            return true;
        }
        Node<K> n = root;
        while (true) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) {
                return false;
            }
            if (cmp < 0) {
                if (n.left == null) {
                    n.left = new Node<>(key);
                    size++;
                    return true;
                }
                n = n.left;
            } else {
                if (n.right == null) {
                    n.right = new Node<>(key);
                    size++;
                    return true;
                }
                n = n.right;
            }
        }
    }

    public boolean contains(K key) {
        Node<K> n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) {
                return true;
            }
            n = cmp < 0 ? n.left : n.right;
        }
        return false;
    }

    public boolean remove(K key) {
        int before = size;
        root = remove(root, key);
        return size < before;
    }

    // Trois cas : une feuille disparait ; un noeud a un enfant est remplace par cet enfant ;
    // un noeud a DEUX enfants prend la cle de son successeur (le plus petit a droite), qu'on retire a droite.
    private Node<K> remove(Node<K> n, K key) {
        if (n == null) {
            return null;
        }
        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = remove(n.left, key);
            return n;
        }
        if (cmp > 0) {
            n.right = remove(n.right, key);
            return n;
        }
        if (n.left == null) {
            size--;
            return n.right;
        }
        if (n.right == null) {
            size--;
            return n.left;
        }
        Node<K> successor = n.right;
        while (successor.left != null) {
            successor = successor.left;
        }
        Node<K> replacement = new Node<>(successor.key);
        replacement.left = n.left;
        replacement.right = remove(n.right, successor.key);
        return replacement;
    }

    public int size() {
        return size;
    }

    // Piege : la hauteur d'un arbre vide est 0, celle d'un arbre a un noeud est 1.
    public int height() {
        return height(root);
    }

    private int height(Node<K> n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }

    public K min() {
        Node<K> n = nonEmptyRoot();
        while (n.left != null) {
            n = n.left;
        }
        return n.key;
    }

    public K max() {
        Node<K> n = nonEmptyRoot();
        while (n.right != null) {
            n = n.right;
        }
        return n.key;
    }

    private Node<K> nonEmptyRoot() {
        if (root == null) {
            throw new NoSuchElementException("arbre vide");
        }
        return root;
    }

    // La plus grande cle <= key, ou null. En descendant : a chaque fois qu'on va a droite, le noeud est un candidat.
    public K floor(K key) {
        K best = null;
        Node<K> n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) {
                return n.key;
            }
            if (cmp < 0) {
                n = n.left;
            } else {
                best = n.key;
                n = n.right;
            }
        }
        return best;
    }

    public K ceiling(K key) {
        K best = null;
        Node<K> n = root;
        while (n != null) {
            int cmp = key.compareTo(n.key);
            if (cmp == 0) {
                return n.key;
            }
            if (cmp > 0) {
                n = n.right;
            } else {
                best = n.key;
                n = n.left;
            }
        }
        return best;
    }

    // Infixe (gauche, noeud, droite) : les cles TRIEES.
    public List<K> inOrder() {
        List<K> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    private void inOrder(Node<K> n, List<K> out) {
        if (n != null) {
            inOrder(n.left, out);
            out.add(n.key);
            inOrder(n.right, out);
        }
    }

    // Prefixe (noeud, gauche, droite) : reinserer dans cet ordre redonne LE MEME arbre.
    public List<K> preOrder() {
        List<K> out = new ArrayList<>();
        preOrder(root, out);
        return out;
    }

    private void preOrder(Node<K> n, List<K> out) {
        if (n != null) {
            out.add(n.key);
            preOrder(n.left, out);
            preOrder(n.right, out);
        }
    }

    // Par niveaux (parcours en largeur) : une FILE, et non une pile.
    public List<K> levelOrder() {
        List<K> out = new ArrayList<>();
        Deque<Node<K>> queue = new ArrayDeque<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            Node<K> n = queue.poll();
            out.add(n.key);
            if (n.left != null) {
                queue.add(n.left);
            }
            if (n.right != null) {
                queue.add(n.right);
            }
        }
        return out;
    }

    // Combien de cles dans [lo, hi] : on ne descend que dans les sous-arbres qui peuvent en contenir.
    public int rangeCount(K lo, K hi) {
        return rangeCount(root, lo, hi);
    }

    private int rangeCount(Node<K> n, K lo, K hi) {
        if (n == null) {
            return 0;
        }
        if (n.key.compareTo(lo) < 0) {
            return rangeCount(n.right, lo, hi);
        }
        if (n.key.compareTo(hi) > 0) {
            return rangeCount(n.left, lo, hi);
        }
        return 1 + rangeCount(n.left, lo, hi) + rangeCount(n.right, lo, hi);
    }

    // Le plus proche ancetre commun : le premier noeud ou les chemins vers a et b se SEPARENT.
    public K lowestCommonAncestor(K a, K b) {
        if (!contains(a) || !contains(b)) {
            throw new NoSuchElementException("cle absente");
        }
        Node<K> n = root;
        while (true) {
            if (a.compareTo(n.key) < 0 && b.compareTo(n.key) < 0) {
                n = n.left;
            } else if (a.compareTo(n.key) > 0 && b.compareTo(n.key) > 0) {
                n = n.right;
            } else {
                return n.key;
            }
        }
    }
}
