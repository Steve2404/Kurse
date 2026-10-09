# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`SearchTree.java`](SearchTree.java), et les tests de référence dans [`SearchTreeTest.java`](SearchTreeTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Ajouter et chercher

**Question — l'ordre d'ajout :** oui, il compte énormément. Dans l'ordre croissant (20, 30, 35, 40…), chaque clé est plus grande que toutes les précédentes : elle va **toujours à droite**. L'arbre devient une ligne de 10 nœuds penchée à droite, de hauteur **10** (au lieu de 4) : une liste chaînée déguisée. Chercher 80 demande 10 comparaisons au lieu de 3.

---

## Étape 2 — Minimum, maximum, plancher, plafond

Avec l'arbre du dessin : `floor(42)` = 40 et `ceiling(42)` = 45 ; `floor(65)` = `ceiling(65)` = 65 ; `floor(10)` = `null`, `ceiling(10)` = 20 ; `floor(81)` = 80, `ceiling(81)` = `null` ; `floor(55)` = 50, `ceiling(55)` = 60 ; `floor(36)` = 35, `ceiling(36)` = 40.

---

## Étape 3 — Les parcours

- infixe : `[20, 30, 35, 40, 45, 50, 60, 65, 70, 80]` (trié) ;
- préfixe : `[50, 30, 20, 40, 35, 45, 70, 60, 65, 80]` ;
- par niveaux : `[50, 30, 70, 20, 40, 60, 80, 35, 45, 65]`.

---

## Étape 4 — Supprimer

Après le retrait de 20 puis de 60, le préfixe vaut `[50, 30, 40, 35, 45, 70, 65, 80]` ; après le retrait de 30 (deux enfants) dans l'arbre de départ, `[50, 35, 20, 40, 45, 70, 60, 65, 80]` ; après le retrait de la racine 50, `[60, 30, 20, 40, 35, 45, 70, 65, 80]` (vérifiés).

**Question — un seul enfant au plus :** le successeur est le nœud **le plus à gauche** du sous-arbre droit : par définition, il n'a **pas** d'enfant gauche (sinon celui-ci serait encore plus à gauche). Il a donc zéro ou un enfant (à droite) : son retrait est un cas 1 ou 2, simple.

---

## Étape 5 — Élaguer

`rangeCount(33, 66)` = 6 (35, 40, 45, 50, 60, 65) ; `rangeCount(41, 44)` = 0 ; `rangeCount(50, 50)` = 1. Ancêtres : (35, 45) → 40 ; (20, 45) → 30 ; (35, 65) → 50 ; (60, 65) → 60 (un nœud est son propre ancêtre) ; (80, 80) → 80.

---

## Étape 6 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié).

---

## Expériences de fin de projet

1. **L'arbre dégénéré** (vérifié) :

   | n | ajouts triés | hauteur | ajouts mélangés | hauteur |
   |---|---|---|---|---|
   | 10 000 | 77 ms | `StackOverflowError` | 4 ms | 29 |
   | 20 000 | 312 ms | 20 000 | 4 ms | 34 |
   | 40 000 | 1253 ms | `StackOverflowError` | 6 ms | 37 |

   Avec des clés triées, chaque ajout descend tout le chemin : 1 + 2 + … + n ≈ n²/2 pas, d'où le ×4 quand `n` double. La hauteur vaut `n`, et la méthode `height()` récursive descend `n` appels l'un dans l'autre : la pile d'appels déborde. Remarque honnête : à 20 000, la pile a tenu, mais pas à 10 000 ! La profondeur que tolère la pile dépend de l'état du compilateur JIT (un code déjà compilé utilise moins de pile qu'un code encore interprété). Sur un arbre dégénéré, une méthode récursive est une bombe à retardement. Les vrais arbres (`TreeMap`) se **rééquilibrent** tout seuls (arbres rouge-noir) pour garder une hauteur en O(log n).
2. Avec un million de clés mélangées (vérifié), la hauteur vaut **51** : environ 2,5 fois log₂(n) ≈ 20. Un arbre construit au hasard n'est pas parfaitement équilibré, mais sa hauteur reste **logarithmique** (en moyenne environ 3 log₂ n au plus) : un million de recherches restent instantanées.
