package collections.exercises;

import collections.ExerciseChecker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EXERCICE 9 (CAPSTONE) - Graphe generique : Map + Set + Deque + Generiques
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * C'est l'exercice le plus complet du package : il combine TOUT ce qui a
 * ete vu (List, Set, Map, Deque, generiques) pour implementer un graphe
 * oriente generique et ses parcours.
 *
 * -- L'image a garder en tete pour tout l'exercice --
 *
 * Imagine un camp de vacances avec plusieurs maisons (A, B, C, D, E...)
 * reliees entre elles par des sentiers a SENS UNIQUE (comme des rues a
 * sens unique en ville). Un sentier de A vers B veut dire "tu peux
 * marcher de A a B, mais pas forcement de B a A". Tout cet exercice
 * consiste a se promener intelligemment dans ce camp.
 *
 * Structure interne imposee : Map<T, Set<T>> ou la cle est une maison
 * et la valeur est l'ensemble des maisons directement accessibles
 * depuis elle (son "carnet de sentiers sortants"). On utilise
 * LinkedHashMap/LinkedHashSet pour un ORDRE D'ITERATION PREDICTIBLE
 * (important pour que les tests ci-dessous soient deterministes).
 *
 *
 * ==================================================================
 * TODO 1 : addEdge(from, to)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ajouter un sentier, c'est ecrire dans le carnet de la maison 'from'
 * : "depuis ici, je peux aussi aller a 'to'". Mais attention a un
 * piege : la maison 'to' doit, elle aussi, exister quelque part dans
 * le grand carnet du camp - meme si PERSONNE n'a encore trace de
 * sentier qui PART de 'to'. Sinon, une maison "cul-de-sac" (ou on
 * arrive, mais d'ou aucun sentier ne repart) n'apparaitrait jamais
 * dans le plan du camp, et on l'oublierait completement en explorant.
 *
 * -- Essayons a la main --
 *
 * On ajoute, dans cet ordre : A->B, A->C, B->D, C->D, D->E. Dessine
 * ca sur une feuille avec des points et des fleches. Carnet apres
 * toutes les additions :
 *   A : {B, C}
 *   B : {D}
 *   C : {D}
 *   D : {E}
 *   E : {}   (personne ne part de E, mais E existe bien dans le carnet)
 *
 * Si tu oublies de creer l'entree de E quand tu ajoutes D->E, E
 * n'apparaitra JAMAIS dans le carnet, meme si tout le monde peut y
 * arriver.
 *
 * -- Le plan --
 *
 *   1. S'assurer que 'from' a bien une entree dans le carnet (creer un
 *      ensemble vide si c'est la premiere fois qu'on le voit).
 *   2. Ajouter 'to' a l'ensemble des sentiers sortants de 'from'.
 *   3. S'assurer que 'to' a AUSSI une entree dans le carnet (meme
 *      vide), pour ne pas l'oublier s'il n'a pas encore de sentier
 *      sortant.
 *
 *
 * ==================================================================
 * TODO 2 : bfs(start) - parcours en largeur
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine une rumeur qui se propage dans le camp, de bouche a
 * oreille, en commencant par toi. D'ABORD, tu la racontes a tous tes
 * voisins DIRECTS (une seule maison de distance). ENSUITE seulement,
 * chacun de ces voisins la raconte a SES propres voisins (deux
 * maisons de distance). On avance par cercles concentriques, jamais
 * en sautant un cercle.
 *
 * -- Essayons a la main --
 *
 * Meme camp qu'avant (A->B, A->C, B->D, C->D, D->E), depart = A.
 *
 *   File d'attente = [A], deja racontee a = {A}
 *   Je sors A de la file, je le note dans le resultat : [A]
 *     Je raconte a ses voisins B et C (pas encore au courant) : je
 *     les ajoute a la file ET a "deja racontee a" tout de suite (pour
 *     ne pas les re-ajouter deux fois si un autre chemin y mene).
 *     File = [B, C]
 *   Je sors B, je le note : [A, B]. Voisin de B : D (nouveau) -> file = [C, D]
 *   Je sors C, je le note : [A, B, C]. Voisin de C : D (deja au
 *     courant, on l'ignore) -> file = [D]
 *   Je sors D, je le note : [A, B, C, D]. Voisin de D : E (nouveau)
 *     -> file = [E]
 *   Je sors E, je le note : [A, B, C, D, E]. Pas de voisin. File vide,
 *     fini.
 *
 * Resultat : [A, B, C, D, E].
 *
 * -- Ce qu'on remarque --
 *
 * On a besoin d'une file (premier arrive, premier sorti) pour
 * respecter l'ordre "cercle par cercle", et d'un ensemble "deja
 * racontee a" pour ne jamais reraconter la meme rumeur deux fois a la
 * meme maison (sinon, avec un cycle dans le camp, la rumeur tournerait
 * en boucle pour toujours).
 *
 * -- Le plan --
 *
 *   1. Mettre 'start' dans la file, et le marquer "deja visite".
 *   2. Tant que la file n'est pas vide : en sortir la maison de
 *      devant, l'ajouter au resultat.
 *   3. Pour chaque voisin de cette maison, s'il n'est pas deja
 *      visite : le marquer visite ET l'ajouter a la file (les deux en
 *      meme temps, pour ne pas l'ajouter deux fois via deux chemins
 *      differents).
 *
 *
 * ==================================================================
 * TODO 3 : dfs(start) - parcours en profondeur, version iterative
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un explorateur dans un labyrinthe qui deroule un fil
 * derriere lui (le fil d'Ariane). Il choisit UN chemin et va TOUT
 * DROIT, aussi loin que possible, sans se soucier des autres chemins
 * qu'il aurait pu prendre. Ce n'est que lorsqu'il arrive dans une
 * impasse (plus aucun chemin nouveau) qu'il REMONTE le fil jusqu'au
 * dernier endroit ou il restait un chemin non essaye, et repart de la.
 *
 * -- Essayons a la main --
 *
 * Meme camp (A->B, A->C, B->D, C->D, D->E), depart = A. Avec une pile
 * (on empile/depile toujours par le dessus, comme une pile
 * d'assiettes) :
 *
 *   Pile = [A]
 *   Je depile A (pas encore visite) -> je le note, je le marque
 *     visite : resultat = [A]. J'empile ses voisins B et C. Piege :
 *     pour que B soit explore AVANT C (l'ordre du carnet), il faut
 *     empiler C d'abord, puis B (le dernier empile est le premier
 *     depile). Pile = [C, B]
 *   Je depile B (pas visite) -> resultat = [A, B]. J'empile son seul
 *     voisin D. Pile = [C, D]
 *   Je depile D (pas visite) -> resultat = [A, B, D]. J'empile son
 *     seul voisin E. Pile = [C, E]
 *   Je depile E (pas visite) -> resultat = [A, B, D, E]. Pas de
 *     voisin. Pile = [C]
 *   Je depile C (pas visite) -> resultat = [A, B, D, E, C]. Son voisin
 *     D est deja visite, on l'ignore (ou on l'empile puis on le jette
 *     au depilage suivant, les deux approches marchent). Pile vide,
 *     fini.
 *
 * Resultat : [A, B, D, E, C].
 *
 * -- Ce qu'on remarque --
 *
 * BFS (la rumeur) utilise une FILE (offer/poll) et avance cercle par
 * cercle. DFS (l'explorateur) utilise une PILE (push/pop) et fonce
 * jusqu'au bout d'un chemin avant de revenir en arriere. Meme camp,
 * meme depart, deux ordres de visite completement differents : c'est
 * la structure de donnees choisie (file vs pile) qui fait toute la
 * difference.
 *
 * -- Le plan --
 *
 *   1. Empiler 'start'.
 *   2. Tant que la pile n'est pas vide : depiler une maison.
 *   3. Si elle est deja visitee, l'ignorer et recommencer au point 2
 *      (elle a pu etre empilee deux fois par deux chemins differents).
 *   4. Sinon, la marquer visitee, l'ajouter au resultat, et empiler
 *      SES voisins en ordre INVERSE (pour que le premier voisin du
 *      carnet soit le prochain depile, puisque le dernier empile sort
 *      en premier).
 *
 *
 * ==================================================================
 * TODO 4 : hasCycle()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Reprends l'explorateur au fil d'Ariane. Un cycle, ce n'est PAS
 * "revenir dans une maison qu'on a deja visitee un jour" (ca, c'est
 * normal, plusieurs chemins peuvent mener a la meme maison). Un
 * cycle, c'est plus precis : "marcher, marcher, marcher... et
 * retomber sur une maison qui est ENCORE sur le fil que tu es en train
 * de derouler MAINTENANT" - autrement dit, tu es revenu sur tes
 * propres pas, en boucle, sans jamais avoir range ton fil.
 *
 * -- Essayons a la main --
 *
 * Camp 1 : X->Y, Y->Z, Z->X. Pars de X, marche vers Y (fil : X, Y),
 * marche vers Z (fil : X, Y, Z), puis Z mene vers... X, qui est
 * ENCORE sur ton fil en ce moment ! C'est un cycle.
 *
 * Camp 2 (celui du TODO 1-3) : A->B->D->E et A->C->D->E. Meme si D est
 * visite deux fois (une fois via B, une fois via C), ce n'est PAS un
 * cycle : quand tu arrives en D la deuxieme fois (via C), tu as deja
 * FINI d'explorer D et range ton fil pour cette branche-la (D n'est
 * plus "actuellement sur le fil"). C'est juste deux chemins differents
 * qui se rejoignent, pas une boucle.
 *
 * -- Ce qu'on remarque --
 *
 * Il faut distinguer TROIS etats pour chaque maison, pas juste deux
 * (visite / pas visite) :
 *   - BLANC : jamais encore visitee.
 *   - GRIS : "actuellement sur le fil" - on est en train de
 *     l'explorer, on n'a pas fini, le fil n'est pas range.
 *   - NOIR : completement exploree, le fil est range pour cette
 *     branche, plus aucun risque de cycle par ici.
 * Un cycle existe uniquement si, en explorant, on retombe sur une
 * maison GRISE (pas une maison noire, ce serait juste un chemin qui
 * se rejoint, comme le Camp 2 ci-dessus).
 *
 * -- Le plan --
 *
 *   1. Preparer un carnet d'etats, tout le monde commence BLANC.
 *   2. Pour CHAQUE maison du camp (le camp peut avoir plusieurs
 *      morceaux separes, pas juste un depart unique) : si elle est
 *      encore BLANCHE, explorer depuis elle.
 *   3. Explorer une maison = la marquer GRISE, regarder chacun de ses
 *      voisins : si un voisin est GRIS -> cycle trouve, on arrete
 *      tout. S'il est BLANC, l'explorer a son tour (meme recette,
 *      recursivement). Une fois tous les voisins traites sans
 *      probleme, marquer la maison NOIRE.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui, clairement : l'etape 3 a sa propre petite recette qui s'appelle
 * elle-meme (Q3, elle cache sa propre recette ; c'est aussi elle qui
 * revient pour chaque maison blanche du camp, Q2). D'ou le helper
 * prive suggere dans les indices : dfsHasCycle(node, state).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - adjacency.computeIfAbsent(from, k -> new LinkedHashSet<>()).add(to);
 *   - adjacency.computeIfAbsent(to, k -> new LinkedHashSet<>()); // s'assure que 'to' existe comme cle
 *
 * Indice TODO 2 :
 *   - Deque<T> queue = new ArrayDeque<>(); queue.offer(start);
 *   - Set<T> visited = new LinkedHashSet<>(); visited.add(start);
 *   - Tant que queue non vide : node = queue.poll(); ajouter au resultat ;
 *     pour chaque voisin non visite : visited.add(voisin), queue.offer(voisin).
 *
 * Indice TODO 3 :
 *   - Deque<T> stack = new ArrayDeque<>(); stack.push(start);
 *   - Set<T> visited = new LinkedHashSet<>();
 *   - Tant que stack non vide : node = stack.pop(); si deja visite, continue ;
 *     sinon le marquer visite, l'ajouter au resultat, empiler ses voisins
 *     (attention : empiler dans l'ordre pour que le PREMIER voisin de la
 *     liste d'adjacence soit explore en premier, il faut donc parcourir
 *     les voisins en ordre INVERSE avant de les push() un par un, car
 *     push() ajoute en tete).
 *
 * Indice TODO 4 :
 *   - Utiliser une Map<T, Integer> state avec 0=blanc, 1=gris, 2=noir,
 *     et une methode recursive privee dfsHasCycle(node, state). Gris
 *     retrouve pendant l'exploration => cycle.
 */
public class Exercise09_GenericGraph {

    static class Graph<T> {
        private final Map<T, Set<T>> adjacency = new LinkedHashMap<>();

        void addEdge(T from, T to) {
            throw new UnsupportedOperationException("TODO 1 : implementer addEdge()");
        }

        Set<T> neighborsOf(T node) {
            return adjacency.getOrDefault(node, Set.of());
        }

        List<T> bfs(T start) {
            throw new UnsupportedOperationException("TODO 2 : implementer bfs()");
        }

        List<T> dfs(T start) {
            throw new UnsupportedOperationException("TODO 3 : implementer dfs()");
        }

        boolean hasCycle() {
            throw new UnsupportedOperationException("TODO 4 : implementer hasCycle()");
        }
    }

    public static void main(String[] args) {
        Graph<String> g = new Graph<>();
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "D");
        g.addEdge("C", "D");
        g.addEdge("D", "E");

        ExerciseChecker.check("neighborsOf(A) == {B, C}", g.neighborsOf("A").equals(new LinkedHashSet<>(Arrays.asList("B", "C"))));

        List<String> bfsResult = g.bfs("A");
        ExerciseChecker.check("bfs(A) == [A, B, C, D, E]", bfsResult.equals(Arrays.asList("A", "B", "C", "D", "E")));

        List<String> dfsResult = g.dfs("A");
        ExerciseChecker.check("dfs(A) == [A, B, D, E, C]", dfsResult.equals(Arrays.asList("A", "B", "D", "E", "C")));

        ExerciseChecker.check("Graphe A->B->D->E, A->C->D est acyclique", !g.hasCycle());

        Graph<String> cyclic = new Graph<>();
        cyclic.addEdge("X", "Y");
        cyclic.addEdge("Y", "Z");
        cyclic.addEdge("Z", "X"); // ferme le cycle
        ExerciseChecker.check("Graphe X->Y->Z->X contient un cycle", cyclic.hasCycle());

        ExerciseChecker.summary();
    }
}