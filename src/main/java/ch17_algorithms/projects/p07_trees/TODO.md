# Projet 7 — L'annuaire de l'entreprise (les arbres binaires de recherche)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **l'arbre binaire de recherche** : à gauche les plus petits, à droite les plus grands ;
- **descendre un seul chemin** : ajouter, chercher, minimum, maximum, plancher, plafond en O(hauteur) ;
- **la suppression** et ses trois cas (dont le **successeur** pour un nœud à deux enfants) ;
- **les parcours** : infixe (trié), préfixe, **par niveaux** (avec une file) ;
- **élaguer** dans un arbre : compter les clés d'un intervalle, le plus proche ancêtre commun ;
- **la hauteur décide de tout** : un arbre **équilibré** est en O(log n), un arbre **dégénéré** en O(n) ;
- **un type générique borné** : `K extends Comparable<K>` (chapitre 9).

**Ce que TU crées :** dans `ch17_algorithms.projects.p07_trees` :
- **`SearchTree<K extends Comparable<K>>`** (signatures imposées) ;
- **`SearchTreeTest`**, tes tests.

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Tu écris l'arbre : pas de `TreeMap`, de `TreeSet`, ni de tri dans ton code. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : le jeu des devinettes « plus petit ou plus grand », et un arbre généalogique.

> **🧰 Tes outils pour ce projet**
>
> - **Dessine l'arbre** sur papier avant chaque test : les cas de suppression et les parcours se comprennent en le regardant.
> - **Voir l'arbre au débogueur :** un point d'arrêt après la construction, puis déplie `root` → `left` → `right` dans le panneau *Variables*.

---

## Tableau de bord

### ☐ Étape 1 — Ajouter et chercher

**📖 La leçon : une dichotomie qui se range.** Au projet 1, la dichotomie demandait un **tableau trié**, cher à garder trié quand on ajoute (il faut décaler). Un **arbre binaire de recherche** garde la même idée, mais sous forme de **nœuds** : chaque nœud a une clé, un enfant **gauche** (toutes les clés plus **petites**) et un enfant **droit** (toutes les clés plus **grandes**). Chercher, c'est jouer à « plus petit ou plus grand ? » depuis la **racine** : une seule descente. Ajouter, c'est descendre jusqu'à une place vide.

```
          50                 chercher 45 : 45 < 50 → à gauche ; 45 > 30 → à droite ;
        /    \                              45 > 40 → à droite : trouvé.
      30      70             ajouter 66 : 66 > 50, 66 < 70, 66 > 60, 66 > 65 : à droite de 65.
     /  \    /  \
   20   40  60   80
       /  \   \
      35  45   65
```

Le coût est la **hauteur** de l'arbre (le nombre de nœuds sur le plus long chemin depuis la racine).

**👉 À toi :** **`public final class SearchTree<K extends Comparable<K>>`**, avec une classe interne `Node` (clé, gauche, droite), et :
- **`public boolean add(K key)`** : ajoute la clé ; rend `false` (sans rien changer) si elle y est déjà ;
- **`public boolean contains(K key)`** ;
- **`public int size()`** et **`public int height()`** (0 pour un arbre vide, 1 pour un seul nœud).

Les clés se comparent avec `compareTo`. Écris `add` et `contains` avec une **boucle** (pas de récursivité) : une descente n'a pas besoin de pile.

**Tes tests :** construis l'arbre du dessin (dans un `@BeforeEach`, en ajoutant 50, 30, 70, 20, 40, 60, 80, 35, 45, 65 dans cet ordre) : la taille, un doublon refusé, une clé présente, une absente, un ajout ; les hauteurs (l'arbre du dessin, vide, un nœud).

**❓ Question :** l'ordre d'ajout compte-t-il ? Dessine l'arbre obtenu en ajoutant les mêmes clés **dans l'ordre croissant**.

### ☐ Étape 2 — Minimum, maximum, plancher, plafond

**👉 À toi :**
- **`public K min()`** et **`public K max()`** : le plus à gauche, le plus à droite. Sur un arbre vide : `NoSuchElementException("arbre vide")` ;
- **`public K floor(K key)`** : la plus grande clé `<= key`, ou `null` ; **`public K ceiling(K key)`** : la plus petite clé `>= key`, ou `null`. En descendant vers `key`, chaque fois qu'on part **à droite** (pour `floor`), le nœud quitté est un **candidat** : il est plus petit que `key`, et c'est le plus grand vu jusqu'ici.

**Tes tests :** min et max, et sur un arbre vide ; plancher et plafond de 42, de 65 (présente), de 10 et 81 (hors de l'arbre : `null`), de 55 et 36. Un `@CsvSource` accepte une colonne **vide** pour `null` (projet 3 du chapitre 16).

### ☐ Étape 3 — Les parcours

**📖 La leçon : trois façons de visiter.** Dans un arbre généalogique, on peut citer les gens de trois façons classiques :
- **préfixe** : moi, puis ma branche gauche, puis ma branche droite. Réinsérer les clés dans cet ordre **redonne le même arbre** ;
- **infixe** : ma branche gauche, moi, ma branche droite. Pour un arbre de recherche, cela donne les clés **triées** ;
- **par niveaux** : la racine, puis tous ses enfants, puis tous ses petits-enfants… Ici, la récursivité ne convient pas : on utilise une **file** (premier entré, premier sorti, `ArrayDeque` avec `add` et `poll`). On sort un nœud, on ajoute ses enfants **à la fin** de la file.

**👉 À toi :** **`public List<K> inOrder()`**, **`public List<K> preOrder()`** et **`public List<K> levelOrder()`**.

**Tes tests :** les trois parcours de l'arbre du dessin (écris-les à la main d'abord), et le parcours par niveaux d'un arbre vide.

### ☐ Étape 4 — Supprimer

**📖 La leçon : les trois cas.** Pour retirer un nœud :
1. **une feuille** (aucun enfant) : elle disparaît ;
2. **un seul enfant** : l'enfant prend sa place ;
3. **deux enfants** : on ne peut pas raccrocher deux sous-arbres à un seul parent. On remplace sa clé par celle de son **successeur**, la plus petite clé de son sous-arbre **droit** (on descend une fois à droite, puis tout à gauche), puis on retire ce successeur, qui a au plus un enfant.

Une méthode récursive `Node<K> remove(Node<K> n, K key)` qui **rend le nouveau sous-arbre** s'écrit simplement : chaque parent fait `n.left = remove(n.left, key)`.

**👉 À toi :** **`public boolean remove(K key)`** : rend `true` si la clé était là. La taille diminue de 1.

**Tes tests :** retirer une feuille (20) puis un nœud à un enfant (60) ; une clé absente ; un nœud à deux enfants (30, remplacé par 35) ; la racine (50, remplacée par 60). Vérifie avec le parcours **préfixe**, qui montre la forme de l'arbre.

**❓ Question :** pourquoi le successeur a-t-il **au plus un** enfant ?

### ☐ Étape 5 — Élaguer dans un arbre

**👉 À toi :**
- **`public int rangeCount(K lo, K hi)`** : le nombre de clés dans `[lo, hi]`. Un nœud plus petit que `lo` : seules ses clés de **droite** peuvent compter ; plus grand que `hi` : seulement celles de **gauche** ; sinon, lui et les deux côtés ;
- **`public K lowestCommonAncestor(K a, K b)`** : le plus proche ancêtre commun de deux clés présentes (un nœud est son propre ancêtre). En partant de la racine : si `a` et `b` sont toutes deux plus petites, à gauche ; toutes deux plus grandes, à droite ; sinon, les chemins se **séparent** ici : c'est l'ancêtre. Si une des deux clés est absente : `NoSuchElementException("cle absente")`.

**Tes tests :** cinq intervalles (dont un sans clé, et des bornes égales à des clés) ; cinq ancêtres (dont une clé avec elle-même, et un ancêtre qui est l'une des deux clés) ; une clé absente ; un arbre de `String` (l'ordre alphabétique) ; un million de clés **mélangées** (`Collections.shuffle`) en moins de 4 secondes, et une hauteur inférieure à 60.

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. **L'arbre dégénéré**, dans `Mesure` : ajoute 10 000, 20 000 puis 40 000 clés **dans l'ordre croissant** dans ton arbre, en chronométrant, puis affiche `height()`. Fais la même chose avec les clés **mélangées**. Qu'observes-tu ?
2. Avec un million de clés mélangées, quelle hauteur obtiens-tu ? Compare à log₂(1 000 000) ≈ 20.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class SearchTree<K extends Comparable<K>>` et ses quatorze signatures exactes, `.compareTo(` ; jamais `TreeMap`, `TreeSet`, `Collections.sort`, `.sort(`.
- **Tes tests :** au moins **25** tests, `@BeforeEach`, `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p07_trees ===
[PASS] tes tests sur TON code : 26 tests, 26 reussis
[PASS] tes tests sur le code de REFERENCE : 26 tests, 26 reussis
[PASS] les tests de REFERENCE sur TON code : 26 tests, 26 reussis
   mutant 1 : tue (par addContainsSize)
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
