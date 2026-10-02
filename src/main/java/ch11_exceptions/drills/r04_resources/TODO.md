# Drill de rappel 4 — try-with-resources

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`** dans le paquet `ch11_exceptions.drills.r04_resources`, avec un champ `static final List<String> LOG`.
- Dans le même fichier, crée la classe package-private **`Door implements AutoCloseable`** :
  - construite avec `(String name, boolean failOnClose)` ; elle note `ouvre <nom>` dans `Recall04.LOG` ;
  - `public void close()` **sans `throws`** note `ferme <nom>`, puis lève `IllegalStateException("close <nom>")` si `failOnClose`.

## Défis

Vide `LOG` entre deux défis.

- ☐ **D01.** `try (Door a = new Door("a", false); Door b = new Door("b", false))` ; le corps note `corps`.
  → `D01 : [ouvre a, ouvre b, corps, ferme b, ferme a]`
- ☐ **D02.** Les deux portes échouent à la fermeture, et le corps lève `IllegalStateException("corps")`.
  - `catch` : note `catch <message> <messages des supprimées en liste>` ;
  - `finally` : note `finally`.
  → `D02 : [ouvre a, ouvre b, ferme b, ferme a, catch corps [close b, close a], finally]`
- ☐ **D03.** Une seule porte qui échoue ; le corps note `corps`. Le `catch` note `catch <message> <nombre de supprimées>`.
  → `D03 : [ouvre a, corps, ferme a, catch close a 0]`
- ☐ **D04.** `Door shared = new Door("partagee", false);`, puis `try (shared; Door nothing = null)`. Le corps note `corps ` + `(nothing == null)`.
  → `D04 : [ouvre partagee, corps true, ferme partagee]`
- ☐ **D05.** `try (AutoCloseable lambda = () -> LOG.add("ferme lambda"))` ; le corps note `corps`. Ajoute le `catch (Exception e)` obligatoire.
  → `D05 : [corps, ferme lambda]`
- ☐ **D06.** `primary = new IllegalStateException("principale")`, puis `primary.addSuppressed(new IllegalArgumentException("a la main"))`. Affiche le nombre de supprimées, puis le message de la première.
  → `D06 : 1 a la main`
- ☐ **D07.** `try (var door = new Door("v", false))` ; le corps note `type ` + le nom simple de la classe de `door`.
  → `D07 : [ouvre v, type Door, ferme v]`

## Expériences (hors sortie attendue)

1. Utilise `a` dans le `catch` ou le `finally` de D01 : quelle erreur ?
2. `try (String s = "x") { }` : quelle erreur ?
3. Pourquoi D05 exige-t-il `catch (Exception e)`, et pas D01 ?
4. Déclare `Door shared` sans `final`, puis réaffecte-la avant `try (shared)` : quelle erreur ?
5. Dans D07, réaffecte `door = null;` dans le corps : quelle erreur ? (Une ressource est implicitement `final`.)

## Sortie attendue complète

```
D01 : [ouvre a, ouvre b, corps, ferme b, ferme a]
D02 : [ouvre a, ouvre b, ferme b, ferme a, catch corps [close b, close a], finally]
D03 : [ouvre a, corps, ferme a, catch close a 0]
D04 : [ouvre partagee, corps true, ferme partagee]
D05 : [corps, ferme lambda]
D06 : 1 a la main
D07 : [ouvre v, type Door, ferme v]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les ressources** implémentent `AutoCloseable` (`void close() throws Exception`). `Closeable` (java.io) l'étend : `close() throws IOException`, et doit être **idempotent**.
- **L'ordre :** ouverture de gauche à droite ; fermeture dans l'ordre **inverse**, **avant** `catch` et `finally`.
- **Portée :** la variable n'existe que dans le bloc `try`. Elle est implicitement `final`, et peut être déclarée avec `var`.
- **Les exceptions de `close()`** :
  - s'il y a déjà une exception, elles lui sont ajoutées comme **supprimées** (`getSuppressed()`) ;
  - sinon, la première devient la principale.
- **Une ressource `null`** n'est pas fermée.
- **Java 9 :** `try (variable)` avec une variable effectivement finale déclarée avant.
- **Un try-with-resources** peut n'avoir ni `catch` ni `finally`, sauf si `close()` lève une exception vérifiée non déclarée.

</details>
