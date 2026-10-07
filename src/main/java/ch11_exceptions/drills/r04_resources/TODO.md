# Drill de rappel 4 — try-with-resources

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`** dans le paquet `ch11_exceptions.drills.r04_resources`, avec un champ `static final List<String> LOG`.
- Dans le même fichier, crée la classe package-private **`Door implements AutoCloseable`** :
  - construite avec `(String name, boolean failOnClose)` ; elle note `ouvre <nom>` dans `Recall04.LOG` ;
  - `public void close()` **sans `throws`** note `ferme <nom>`, puis lève `IllegalStateException("close <nom>")` si `failOnClose`.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_resources` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
