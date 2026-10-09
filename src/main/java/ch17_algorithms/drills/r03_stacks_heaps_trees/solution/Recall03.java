package ch17_algorithms.drills.r03_stacks_heaps_trees.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

/** Le corrige du drill 3 : piles, tas, arbres de recherche. */
public final class Recall03 {

    private Recall03() {
    }

    // D01
    public static boolean balanced(String s) {
        Deque<Character> open = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                open.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                char expected = c == ')' ? '(' : c == ']' ? '[' : '{';
                if (open.isEmpty() || open.pop() != expected) {
                    return false;
                }
            }
        }
        return open.isEmpty();
    }

    // D02
    public static long evalRpn(String expr) {
        Deque<Long> stack = new ArrayDeque<>();
        for (String t : expr.trim().split("\\s+")) {
            switch (t) {
                case "+", "-", "*", "/" -> {
                    long right = stack.pop();
                    long left = stack.pop();
                    stack.push(switch (t) {
                        case "+" -> left + right;
                        case "-" -> left - right;
                        case "*" -> left * right;
                        default -> left / right;
                    });
                }
                default -> stack.push(Long.parseLong(t));
            }
        }
        return stack.pop();
    }

    // D03
    public static int[] daysUntilWarmer(int[] temps) {
        int[] wait = new int[temps.length];
        Deque<Integer> waiting = new ArrayDeque<>();
        for (int day = 0; day < temps.length; day++) {
            while (!waiting.isEmpty() && temps[waiting.peek()] < temps[day]) {
                int earlier = waiting.pop();
                wait[earlier] = day - earlier;
            }
            waiting.push(day);
        }
        return wait;
    }

    // D04 : le tri par tas SUR PLACE : un tas MAX dans le tableau, puis on echange la racine avec la fin.
    public static void heapSort(int[] a) {
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            siftDown(a, i, a.length);
        }
        for (int end = a.length - 1; end > 0; end--) {
            int t = a[0];
            a[0] = a[end];
            a[end] = t;
            siftDown(a, 0, end);
        }
    }

    private static void siftDown(int[] a, int i, int size) {
        while (true) {
            int left = 2 * i + 1;
            int right = left + 1;
            int largest = i;
            if (left < size && a[left] > a[largest]) {
                largest = left;
            }
            if (right < size && a[right] > a[largest]) {
                largest = right;
            }
            if (largest == i) {
                return;
            }
            int t = a[i];
            a[i] = a[largest];
            a[largest] = t;
            i = largest;
        }
    }

    // D05
    public static int[] topK(int[] a, int k) {
        PriorityQueue<Integer> best = new PriorityQueue<>();
        for (int v : a) {
            best.add(v);
            if (best.size() > k) {
                best.poll();
            }
        }
        int[] out = new int[best.size()];
        for (int i = out.length - 1; i >= 0; i--) {
            out[i] = best.poll();
        }
        return out;
    }

    private static final class Node {
        final int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }

    private static Node build(int[] keys) {
        Node root = null;
        for (int k : keys) {
            if (root == null) {
                root = new Node(k);
                continue;
            }
            Node n = root;
            while (true) {
                if (k == n.key) {
                    break;
                }
                if (k < n.key) {
                    if (n.left == null) {
                        n.left = new Node(k);
                        break;
                    }
                    n = n.left;
                } else {
                    if (n.right == null) {
                        n.right = new Node(k);
                        break;
                    }
                    n = n.right;
                }
            }
        }
        return root;
    }

    // D06 : l'arbre obtenu en inserant les cles dans cet ordre, parcouru en prefixe.
    public static List<Integer> preOrder(int[] insertOrder) {
        List<Integer> out = new ArrayList<>();
        preOrder(build(insertOrder), out);
        return out;
    }

    private static void preOrder(Node n, List<Integer> out) {
        if (n != null) {
            out.add(n.key);
            preOrder(n.left, out);
            preOrder(n.right, out);
        }
    }

    // D07 : le meme arbre, parcouru par niveaux.
    public static List<Integer> levelOrder(int[] insertOrder) {
        List<Integer> out = new ArrayList<>();
        Node root = build(insertOrder);
        Deque<Node> queue = new ArrayDeque<>();
        if (root != null) {
            queue.add(root);
        }
        while (!queue.isEmpty()) {
            Node n = queue.poll();
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
}
