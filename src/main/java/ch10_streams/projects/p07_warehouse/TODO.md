# Projet 7 (CAPSTONE) — La préparation des commandes d'un entrepôt

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**API visée :** tout le chapitre 10 à la fois :
- `Optional` ;
- les sources et opérations intermédiaires ;
- les streams primitifs et leurs statistiques ;
- `reduce` ;
- les `Collectors` (`groupingBy` avec `EnumMap`, `partitioningBy`, `teeing`…).

On y ajoute un **algorithme d'allocation** à état, où il faut décider lucidement ce qui doit **rester une boucle**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p07_warehouse`. La classe du `main` s'appelle **`Warehouse`**. Ici, on t'impose aussi au moins **un `enum`**.

**Indication :** ici, pas d'étape « à la main » détaillée. C'est à toi de vérifier chaque ligne de la sortie attendue sur papier avant de coder l'algorithme.

---

## Le problème

Un entrepôt reçoit des commandes (`Data.ORDERS`) de clients (`Data.CUSTOMERS`) pour des produits en stock limité (`Data.PRODUCTS`). Le stock ne suffit pas pour tout le monde. Il faut décider **qui est servi en premier**, servir **partiellement** quand il en manque, prévenir les clients lésés, puis produire le rapport de la journée et un **contrôle comptable**.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle

- Il te faut des produits, des clients, des commandes et leurs lignes. Il faut aussi deux notions à concevoir toi-même :
  - **ce qui a été servi pour une ligne** : produit, quantité servie, quantité manquante ;
  - **le résultat d'une commande** : la commande, son client, ce qui a été servi et les articles inconnus.
- **Deux `enum` :**
  - le niveau client : `GOLD` passe avant `STANDARD` ;
  - le statut d'une commande : `COMPLETE`, `PARTIELLE` ou `REFUSEE`.
  - **Question :** l'ordre de déclaration des constantes d'un `enum` sert à deux choses dans ce projet. Lesquelles ?
- **L'email du client :**
  - **Piège :** `"K2;Hugo;Sud;STANDARD;".split(";")` donne 4 morceaux. Comment obtenir le 5e, vide ? Indice : `split` a une version à 2 arguments.
  - Une méthode rend l'email sous forme d'`Optional`. Elle écarte le champ manquant **et** le blanc (projet 1).
- **Les montants** sont en centimes (`long`), comme au projet 6.

### ☐ Étape 2 — Refus et file de priorité

```
REFUS O5 : client inconnu K9
TRAITEMENT : O8, O2, O4, O1, O3, O6, O7, O9
```
- **Les refus :** une commande dont le client est inconnu est refusée d'emblée. Utilise la recherche de client (`Optional`) et son **absence**.
- **La file :** les commandes restantes sont triées par niveau client (`GOLD` d'abord), puis par date, puis par id.
- **Contrainte :** une seule chaîne de stream associe chaque commande à son client **et** élimine les clients inconnus, sans `filter` + `get`. Quelle chaîne `Optional` → `Stream` connais-tu (projet 1, étape BILAN) ?

### ☐ Étape 3 — L'allocation : l'algorithme

```
O8 Tom (GOLD) : COMPLETE 109.70
O4 Tom (GOLD) : PARTIELLE 349.00 | manque B2x2
O7 Hugo (STANDARD) : PARTIELLE 29.90 | manque B1x1 | inconnu Z9
O9 Ines (STANDARD) : REFUSEE 0.00 | manque B2x1
```
- **Pour chaque commande, dans l'ordre de la file, et pour chaque ligne :**
  - on sert `min(demandé, stock)` et on **retire** ce qui est servi du stock ;
  - le reste devient un **manque** ;
  - un article inconnu est noté, puis ignoré.
- **Le statut :**
  - `REFUSEE` si rien n'a été servi ;
  - `PARTIELLE` s'il y a un manque ou un article inconnu ;
  - sinon `COMPLETE`.
- **La question de conception du projet :** l'allocation **modifie** le stock, et son résultat **dépend de l'ordre de passage**. Est-ce une bonne idée de l'écrire avec `stream().map(...)` ?
  - Que dit la Javadoc de `java.util.stream` sur les lambdas avec effets de bord (« side-effects », « stateless ») ?
  - Choisis, et justifie en commentaire.

### ☐ Étape 4 — Avis et bons de livraison

```
AVIS par courrier a Tom : O4 PARTIELLE
AVIS ines@mail.fr : O3 PARTIELLE
BONS : BL-001=O8 BL-002=O2 ... BL-007=O7
```
- **Les avis :** chaque commande non complète déclenche un avis, par email si le client en a un, sinon par courrier. Construis le texte de repli avec `orElseGet`.
- **Les bons :** les commandes **expédiées** (non refusées) reçoivent un numéro `BL-001`, `BL-002`… dans l'ordre de traitement.
  - Les numéros viennent d'un `IntStream`, et non d'un compteur mutable.
  - **Question :** pourquoi un compteur `int` incrémenté dans une lambda serait-il refusé par le compilateur (chapitre 8) ?

### ☐ Étape 5 — Les regroupements

```
STATUTS : {COMPLETE=[O2, O8], PARTIELLE=[O1, O3, O4, O6, O7], REFUSEE=[O9]}
A RECOMMANDER : B2x3, A1x1, A3x1, B1x1, C1x1
CA PAR REGION : {Est=458.70, Nord=557.20, Sud=199.20}
CA GOLD : 797.00, STANDARD : 418.10
UNITES PAR CATEGORIE : {Info=9, Maison=4, Sport=9}
```
- **STATUTS :**
  - les clés suivent l'ordre de l'`enum`, et les ids sont triés ;
  - la `Map` est une `EnumMap`. `groupingBy` prend une fabrique : `EnumMap` n'a pas de constructeur sans argument, alors quelle lambda écrire ?
- **A RECOMMANDER :**
  - c'est le total des manques par article, toutes commandes confondues ;
  - il faut passer des commandes à leurs lignes servies (`flatMap`) ;
  - tri par manque décroissant, puis par sku.
- **CA PAR REGION :**
  - c'est le montant **formaté**, directement dans la `Map` ;
  - quel collecteur applique `money(...)` au résultat d'un `summingLong` ?
- **CA GOLD :** une partition.
- **UNITES PAR CATEGORIE :** ce sont les unités **servies**, par catégorie.

### ☐ Étape 6 — Statistiques

```
PANIERS : 7 expedies, min 29.70, max 349.00, total 1215.10
PANIER MOYEN : 173.59, plus gros : O4 349.00
TOP CLIENTS : Tom 458.70, Lea 338.30
```
- **PANIERS :** un seul objet de statistiques sur un `LongStream`.
- **PANIER MOYEN :** un `teeing` (moyenne + max), en un seul passage. La moyenne en centimes est arrondie avec `Math.round`.
- **TOP CLIENTS :** le montant par client, puis les 2 premiers (à égalité, par nom).

### ☐ Étape 7 — Fin de journée et contrôle comptable

```
RUPTURES : A1, A3, B1, B2, C1
STOCK RESTANT : A2=8, C2=12
CONTROLE : 1493.10 = 1215.10 expedies + 278.00 en stock : oui
```
- **RUPTURES et STOCK RESTANT :** les skus sont triés. Quelle `Map` choisir pour le stock ?
- **CONTROLE :**
  - la valeur initiale du stock (recalculée depuis `Data.PRODUCTS`) doit être égale à la valeur expédiée plus la valeur restante ;
  - le total expédié se calcule avec un `reduce`, et non `sum` ;
  - si tu vois `non`, ton allocation perd ou crée du stock.

### ☐ Étape 8 — `main`

- Il imprime tout dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `enum` | 1 | ☐ |
| `Optional.ofNullable` | 1, 2 | ☐ |
| `flatMap` | 2, 5 | ☐ |
| `sorted` + `thenComparing` | 2 | ☐ |
| `orElseGet` | 4 | ☐ |
| `IntStream.rangeClosed` + `mapToObj` | 4 | ☐ |
| `groupingBy` + `EnumMap` + `mapping` | 5 | ☐ |
| `summingInt`, `summingLong`, `collectingAndThen` | 5 | ☐ |
| `partitioningBy` | 5 | ☐ |
| `mapToLong` + `LongSummaryStatistics` | 6 | ☐ |
| `teeing` + `maxBy` | 6 | ☐ |
| `limit` | 6 | ☐ |
| `reduce` | 7 | ☐ |
| `joining` | partout | ☐ |
| ~~`Optional.get()`~~ | **interdit** | — |

---

## Sortie attendue complète

```
REFUS O5 : client inconnu K9
TRAITEMENT : O8, O2, O4, O1, O3, O6, O7, O9
O8 Tom (GOLD) : COMPLETE 109.70
O2 Lea (GOLD) : COMPLETE 338.30
O4 Tom (GOLD) : PARTIELLE 349.00 | manque B2x2
O1 Hugo (STANDARD) : PARTIELLE 139.60 | manque A1x1
O3 Ines (STANDARD) : PARTIELLE 218.90 | manque A3x1
O6 Zoe (STANDARD) : PARTIELLE 29.70 | manque C1x1
O7 Hugo (STANDARD) : PARTIELLE 29.90 | manque B1x1 | inconnu Z9
O9 Ines (STANDARD) : REFUSEE 0.00 | manque B2x1
AVIS par courrier a Tom : O4 PARTIELLE
AVIS par courrier a Hugo : O1 PARTIELLE
AVIS ines@mail.fr : O3 PARTIELLE
AVIS zoe@mail.fr : O6 PARTIELLE
AVIS par courrier a Hugo : O7 PARTIELLE
AVIS ines@mail.fr : O9 REFUSEE
BONS : BL-001=O8 BL-002=O2 BL-003=O4 BL-004=O1 BL-005=O3 BL-006=O6 BL-007=O7
STATUTS : {COMPLETE=[O2, O8], PARTIELLE=[O1, O3, O4, O6, O7], REFUSEE=[O9]}
A RECOMMANDER : B2x3, A1x1, A3x1, B1x1, C1x1
CA PAR REGION : {Est=458.70, Nord=557.20, Sud=199.20}
CA GOLD : 797.00, STANDARD : 418.10
UNITES PAR CATEGORIE : {Info=9, Maison=4, Sport=9}
PANIERS : 7 expedies, min 29.70, max 349.00, total 1215.10
PANIER MOYEN : 173.59, plus gros : O4 349.00
TOP CLIENTS : Tom 458.70, Lea 338.30
RUPTURES : A1, A3, B1, B2, C1
STOCK RESTANT : A2=8, C2=12
CONTROLE : 1493.10 = 1215.10 expedies + 278.00 en stock : oui
```
