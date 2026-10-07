# Drill de rappel 3 — Les modificateurs d'accès

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire. Ce drill a **trois paquets** ; crée-les dans `ch5_methods/drills/r03_access/` :

| Fichier | Paquet | Contenu |
|---|---|---|
| `shop/Item.java` | `…r03_access.shop` | `public String name = "stylo"`, `protected int stock = 5`, `int code = 42`, `private int secret = 7`, `public int secret()`, `protected static String label()` (rend `"article"`), `void restock(int n)` |
| `shop/Clerk.java` | `…r03_access.shop` | `public static String report(Item item)` : appelle `restock(1)`, puis rend `code + " " + stock + " " + Item.label()` |
| `club/Special.java` | `…r03_access.club` | `public class Special extends Item` : `public int bonus(int n)` ajoute n à `stock` et le rend ; `public static int peek(Special s)` rend `s.stock` ; `public static String describe()` rend `label() + " special"` |
| `Recall03.java` | `…r03_access` | le `main` : il ne voit que le `public` |

- `extends` n'est permis ici que pour étudier `protected` (l'héritage est au chapitre 6).

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1 et 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_access` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall03`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Un nouvel `Item` : son `name`, puis `secret()`.
  → `D01 : stylo 7`
- ☐ **D02.** `Clerk.report(item)`.
  → `D02 : 42 6 article`
- ☐ **D03.** Un nouveau `Special` : `bonus(3)`, puis `Special.peek(special)`.
  → `D03 : 8 8`
- ☐ **D04.** `Special.describe()`.
  → `D04 : article special`
- ☐ **D05.** `item.name = "crayon"` ; `Item same = item;`. Affiche `same.name`, puis `Clerk.report(same)`.
  → `D05 : crayon 42 7 article`
- ☐ **D06.** `Item viaParent = special;`. Affiche son `name`, `secret()`, puis `Clerk.report(viaParent)`.
  → `D06 : stylo 7 42 9 article`

## Expériences (hors sortie attendue)

Une ligne à la fois, puis retire-la.
1. Dans `Recall03` : `item.stock`, `item.code`, `item.secret`. Quelles erreurs ?
2. Dans `Special` : `code` (hérité ?), `secret`.
3. Dans `Special` : `Item other = new Item(); other.stock++;`. Pourquoi refusé alors que `stock++` passe ?
4. Dans `Special.peek` : remplace le paramètre `Special s` par `Item s`. Que se passe-t-il ?
5. Dans `Clerk` : `new Special().stock` compile-t-il ? (Même paquet que `Item`.)
6. Mets `class Item` sans `public` : que devient `Recall03` ?

## Sortie attendue complète

```
D01 : stylo 7
D02 : 42 6 article
D03 : 8 8
D04 : article special
D05 : crayon 42 7 article
D06 : stylo 7 42 9 article
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Modificateur | Même classe | Même paquet | Sous-classe dans un autre paquet | Partout |
|---|---|---|---|---|
| `private` | oui | non | non | non |
| *(aucun)* package-private | oui | oui | non | non |
| `protected` | oui | oui | oui, **par héritage** | non |
| `public` | oui | oui | oui | oui |

**Le piège de `protected` :**
- depuis une sous-classe d'un **autre** paquet, on accède au membre seulement par `this` (implicite) ou par une référence du type de la **sous-classe** ;
- une référence de type parent (`Item other`) est refusée.

**Les classes :**
- une classe de premier niveau est `public` ou package-private, jamais `private` ni `protected` ;
- un fichier contient au plus une classe `public`, qui porte le nom du fichier.

**Pour bien choisir :** le niveau le plus faible qui suffit. Champs `private`, méthodes de service package-private, API `public`.

</details>
