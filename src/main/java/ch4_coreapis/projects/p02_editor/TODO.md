# Projet 2 — L'éditeur de texte à `StringBuilder`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :**
- **`StringBuilder`** : `append`, `insert`, `replace`, `delete`, `deleteCharAt`, `reverse`, `substring`, `indexOf`, `charAt`, `length`, `setLength`, `toString`, et la **mutabilité** ;
- un tableau utilisé comme **pile** ;
- `split` avec une **limite** ;
- le **pool de chaînes** : `==` / `equals`, `new String`, `intern`, les constantes de compilation ;
- l'**immutabilité** des `String`.

**Ce qui est donné :** `Data.java` (les commandes) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p02_editor`. La classe du `main` s'appelle **`Editor`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection : l'historique est un **tableau**.

---

## Le problème

Un mini-éditeur de ligne garde son texte dans **un seul** `StringBuilder` et exécute les commandes de `Data.COMMANDS`. Après chaque commande, il affiche `NOM -> [texte] (longueur)`.

| Commande | Effet |
|---|---|
| `APPEND texte` | ajoute à la fin |
| `INSERT pos texte` | insère à la position |
| `REPLACE début fin texte` | remplace `[début, fin)` |
| `DELETE début fin` | supprime `[début, fin)` |
| `DELCHAR pos` | supprime un caractère |
| `CUT début fin` | supprime `[début, fin)` et le garde dans un **presse-papiers** |
| `PASTE pos` | insère le presse-papiers. **Refus** si `pos` dépasse la longueur (au lieu de planter) |
| `REVERSE` | inverse le texte |
| `FIND texte` | position du texte, ou −1 |
| `UNDO` | annule la dernière modification |
| `LENGTH` | longueur et 1er caractère |
| autre | `commande inconnue : …` |

**L'annulation :**
- chaque commande qui **modifie** le texte en sauve d'abord une copie dans un historique de `Data.HISTORY` cases ;
- **historique plein :** on oublie la plus ancienne copie ;
- **historique vide :** `UNDO` répond `rien a annuler`.

---

## Tableau de bord

### ☐ Étape 1 — Analyser une commande

- **Le découpage :** `"INSERT 8 le grand "` se coupe en `INSERT` et `"8 le grand "` avec `split(" ", 2)`.
  - **Question :** quel est le rôle du 2e argument de `split` ? Que perdrait-on sans lui, à cause des espaces du texte ?
- **Le choix :** un `switch` sur le nom de la commande.

### ☐ Étape 2 — Les modifications

```
APPEND -> [Bonjour monde] (13)
INSERT -> [Bonjour le grand monde] (22)
REPLACE -> [Bonjour le GRAND monde] (22)
```
- **Les intervalles :** dans `replace`, `delete` et `substring`, l'intervalle est `[début, fin)`. La fin est **exclue**.
  - **À la main :** compte les positions de `"Bonjour le grand monde"`. Pourquoi `REPLACE 11 16` ?
- **`append` modifie le `StringBuilder` ET le rend.**
  - **Question :** pourquoi peut-on écrire `sb.append("a").append("b")` ? Que vaudrait `sb == sb.append("x")` ?

### ☐ Étape 3 — Couper, coller, inverser

```
CUT -> [e GRAND monde] (13), presse-papiers [Bonjourl]
PASTE -> refuse : position 99 hors limites (longueur 13)
```
- **Couper :** `substring` (qui rend un `String` **sans** modifier le buffer), puis `delete`.
- **Coller :** vérifie la position **avant** `insert`.
  - **Expérience :** retire la vérification. Quelle exception lève `insert(99, …)` ?

### ☐ Étape 4 — La pile d'annulation dans un tableau

```
UNDO -> annule [Bonjour le GRAND monde] (historique 3)
UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)
```
- **La structure :** un tableau de `String` et une taille. Le sommet est la case `taille - 1`.
- **Sauvegarder :** si le tableau est plein, **décale** tout d'une case vers la gauche (avec `System.arraycopy`) pour oublier la plus ancienne copie.
- **Restaurer :** vide le buffer **sans en créer un nouveau** (`setLength(0)`), puis recharge la copie.
- **Question :** à la fin, après 6 `UNDO`, le texte n'est pas revenu à vide. Pourquoi ? Combien de modifications ont été oubliées ?

### ☐ Étape 5 — Pool de chaînes, égalité, immutabilité

```
a == b true, a == c false, a.equals(c) true, a == c.intern() true
constante true, variable false, variable.intern() true
sb.equals false, contenus true, s1 == s3 true, s1 xy, capacite vide 0
immuable : abc, ABC, concat "abcd" et abc
```
**Reproduis chaque comparaison**, et explique-la en commentaire :
- **les littéraux :** deux littéraux `"java"` désignent **le même** objet du pool ;
- **`new String("java")`** crée un **nouvel** objet. `intern()` rend celui du pool ;
- **`final String half = "ja"; half + "va"`** est une **constante de compilation**, calculée par `javac`, donc dans le pool. Avec une variable **non `final`**, ce n'est plus le cas ;
- **`StringBuilder` n'a pas redéfini `equals`** : il compare les références ;
- **`new StringBuilder(50)`** : 50 est la **capacité**, pas la longueur ;
- **`immutable.toUpperCase();` seul ne change rien.** Pourquoi ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `split(" ", n)` | 1 | ☐ |
| `new StringBuilder(`, `append`, `insert`, `replace`, `delete`, `deleteCharAt`, `reverse` | 2, 3 | ☐ |
| `substring`, `indexOf`, `charAt`, `toString` | 3 | ☐ |
| tableau de `String`, `System.arraycopy`, `setLength` | 4 | ☐ |
| `new String(`, `intern()`, `final String x = "…"` | 5 | ☐ |

---

## Sortie attendue complète

```
APPEND -> [Bonjour monde] (13)
INSERT -> [Bonjour le grand monde] (22)
FIND -> "monde" en position 17
REPLACE -> [Bonjour le GRAND monde] (22)
DELETE -> [le GRAND monde] (14)
UNDO -> annule [Bonjour le GRAND monde] (historique 3)
DELCHAR -> [Bonjourle GRAND monde] (21)
CUT -> [e GRAND monde] (13), presse-papiers [Bonjourl]
PASTE -> refuse : position 99 hors limites (longueur 13)
PASTE -> [Bonjourle GRAND monde] (21)
REVERSE -> [ednom DNARG elruojnoB] (21)
REVERSE -> [Bonjourle GRAND monde] (21)
UNDO -> annule [ednom DNARG elruojnoB] (historique 4)
UNDO -> annule [Bonjourle GRAND monde] (historique 3)
UNDO -> annule [e GRAND monde] (historique 2)
commande inconnue : SHOUT
UNDO -> annule [Bonjourle GRAND monde] (historique 1)
UNDO -> annule [Bonjour le GRAND monde] (historique 0)
UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)
UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)
UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)
UNDO -> rien a annuler [Bonjour le GRAND monde] (historique 0)
LENGTH -> 22, caractere 0 : B
--- POOL ET EGALITE ---
a == b true, a == c false, a.equals(c) true, a == c.intern() true
constante true, variable false, variable.intern() true
sb.equals false, contenus true, s1 == s3 true, s1 xy, capacite vide 0
immuable : abc, ABC, concat "abcd" et abc
```
