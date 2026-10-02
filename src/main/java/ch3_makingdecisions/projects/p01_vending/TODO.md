# Projet 1 — Le distributeur automatique

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Notions visées (chapitre 3) :**
- `if` / `else if` / `else` ;
- le **`switch` instruction**, classique (`case …:` avec `break`, et le « fall-through ») et en flèche (`case … ->`) ;
- le **`switch` expression**, avec **`yield`** ;
- `case` à **plusieurs valeurs** ;
- le `switch` sur un **`String`** ;
- la boucle **`for` à indice** qui consomme des arguments ;
- la boucle **`while`**.

**Ce qui est donné :** `Check.java`. Les commandes arrivent par les arguments.

**Ce que TU crées :** tout le programme, dans le paquet `ch3_makingdecisions.projects.p01_vending`. La classe du `main` s'appelle **`Vending`**.

**Règle du crescendo :** chapitres 1 à 3. Pas de méthode de `String` (même pas `equals` : c'est le `switch` qui compare les textes), pas de tableau créé par toi, pas de collection.

---

## Le problème

Un distributeur reçoit une suite de commandes, données en arguments, **l'une après l'autre** :

| Commande | Effet |
|---|---|
| `PIECE <centimes>` | insère une pièce : 10, 20, 50, 100 ou 200 ; toute autre valeur est **refusée** |
| `CHOIX <code>` | achète un produit : `A1` Eau 1.20, `B2` Café 1.50, `C3` Chips 1.80 (**1.50 pendant la happy hour**) |
| `HAPPY` | active ou désactive la happy hour |
| `STOCK` | affiche les stocks (au départ : A1 = 2, B2 = 1, C3 = 1) |
| `RENDU` | rend le crédit avec **le moins de pièces possible** |
| autre | `Commande inconnue : …` |

**Le refus d'un achat :**
1. produit inconnu ;
2. sinon produit épuisé ;
3. sinon crédit insuffisant (avec le montant manquant).

Les arguments de `Check` enchaînent 33 commandes : lis-les en haut de `Check.java`.

---

## Tableau de bord

### ☐ Étape 1 — La boucle de commandes

- Parcours `args` avec une boucle `for` **à indice**.
- `PIECE` et `CHOIX` **consomment** aussi l'argument suivant.
  - **Question :** pourquoi un for-each ne convient-il pas ici ?
  - Écris l'accès au paramètre avec un **pré-incrément** de l'indice, dans l'appel lui-même.
- Choisis le traitement avec un `switch` **sur le texte de la commande**.

### ☐ Étape 2 — Les pièces

```
PIECE 2.00 -> credit 2.00
PIECE 30 refusee
```
- Un `case` à **plusieurs valeurs** accepte les 5 pièces valides ; `default` refuse le reste.
- **Question :** que dit `javac` si deux `case` portent la même valeur ? Et si une valeur n'est pas une **constante** ?

### ☐ Étape 3 — Le catalogue : des `switch` expressions

- **Le prix, le nom et le stock d'un code** : écris trois méthodes, chacune réduite à **un** `return switch (…) { … };`.
- **Le prix de `C3`** dépend de la happy hour : sa branche est un **bloc** `{ … }` qui rend sa valeur avec **`yield`**.
  - **Questions :**
    - pourquoi un `switch` expression **doit-il** avoir un `default` ici ?
    - que se passe-t-il si une branche ne rend rien ?
- **Retirer un produit du stock :**
  - **Contrainte :** écris-le avec le `switch` **classique** (`case "A1":` … `break;` … `default:`).
  - **Expérience :** retire un `break`. Achète un A1 et regarde les stocks. C'est le **fall-through**.

### ☐ Étape 4 — L'achat

```
CHOIX D9 -> produit inconnu
CHOIX A1 -> Eau servi (1.20), reste 0.80
CHOIX B2 -> credit insuffisant (manque 0.70)
CHOIX C3 -> Chips epuise
```
- Une chaîne `if` / `else if` / `else`, dans l'ordre des refus.

### ☐ Étape 5 — Le rendu de monnaie : l'algorithme glouton

```
RENDU -> 5 piece(s) : 200 100 50 20 10
RENDU -> rien a rendre
```
- Tant qu'il reste du crédit, rends la **plus grosse** pièce possible. Quand elle est trop grosse, passe à la pièce **inférieure**.
- **Contrainte :** pas de tableau de pièces. La « pièce inférieure » est donnée par un `switch` **expression** (200 → 100 → 50 → 20 → 10 → 0).
- **Calcule à la main** le rendu de 3.80. Pourquoi l'algorithme glouton est-il optimal avec ces pièces-là, mais pas avec n'importe quelles pièces ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `args[++i]` | 1 | ☐ |
| `switch` sur un `String` | 1 | ☐ |
| `case` à plusieurs valeurs | 2 | ☐ |
| `switch` expression affectée, `yield` | 3, 5 | ☐ |
| `switch` classique : `break;`, `default:` | 3 | ☐ |
| `else if` | 4 | ☐ |
| `while (` | 5 | ☐ |

---

## Sortie attendue complète

```
PIECE 2.00 -> credit 2.00
PIECE 30 refusee
CHOIX D9 -> produit inconnu
CHOIX A1 -> Eau servi (1.20), reste 0.80
CHOIX B2 -> credit insuffisant (manque 0.70)
PIECE 1.00 -> credit 1.80
CHOIX B2 -> Cafe servi (1.50), reste 0.30
HAPPY HOUR on
CHOIX C3 -> credit insuffisant (manque 1.20)
PIECE 0.50 -> credit 0.80
PIECE 0.50 -> credit 1.30
PIECE 0.20 -> credit 1.50
CHOIX C3 -> Chips servi (1.50), reste 0.00
CHOIX C3 -> Chips epuise
STOCK A1=1 B2=0 C3=0
RENDU -> rien a rendre
PIECE 2.00 -> credit 2.00
PIECE 1.00 -> credit 3.00
PIECE 0.50 -> credit 3.50
PIECE 0.20 -> credit 3.70
PIECE 0.10 -> credit 3.80
RENDU -> 5 piece(s) : 200 100 50 20 10
Commande inconnue : TICKET
```
