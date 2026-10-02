# Drill de rappel 3 — Les services

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Tes modules vont dans `ch12_modules/drills/r03_services/src/`.
- Ton script `recall.sh` va dans ce dossier, avec `P=ch12_modules/drills/r03_services` et `OUT=build/ch12/r03_services`. Il compile avec `-m s.app,s.square,s.triangle`.

## Défis

- ☐ **D01. Les 4 rôles :**
  - **`s.api`** exporte `s.api`, qui contient l'interface `Shape` (`String name()`, `int area(int size)`) ;
  - **`s.locator`** lit `s.api` et le transmet ; il exporte `s.locator` et **utilise** `Shape`. Sa classe `Shapes` contient :
    - `all()` : les instances, triées par nom ;
    - `types()` : les noms simples de `Provider.type()`, triés ;
    - `any()` : `ServiceLoader.load(Shape.class).findFirst()` ;
  - **`s.square`** fournit `Shape` avec `s.square.Square` (`carre`, côté²) ;
  - **`s.triangle`** fournit `Shape` avec `s.triangle.TriangleFactory`, par une méthode `public static Shape provider()` (`triangle`, côté² / 2) ;
  - **`s.app`** requiert `s.locator` seulement. Son `main` affiche :
    `"formes (cote 6) :"` + (` aucune` si la liste est vide, sinon ` nom=aire` pour chaque forme) + `" ; types " + types()` + `" ; findFirst present " + any().isPresent()`.
- ☐ **D02.** `echo "--- D02 tous"`, puis le lancement normal.
  → `formes (cote 6) : carre=36 triangle=18 ; types [Shape, Square] ; findFirst present true`
- ☐ **D03.** `echo "--- D03 carre seul"`, puis le lancement avec `--limit-modules s.app,s.square`.
  → `formes (cote 6) : carre=36 ; types [Square] ; findFirst present true`
- ☐ **D04.** `echo "--- D04 aucun fournisseur"`, puis le lancement avec `--limit-modules s.app`.
  → `formes (cote 6) : aucune ; types [] ; findFirst present false`
- ☐ **D05.** `echo "--- D05"`, puis `--describe-module` de `s.triangle`, puis de `s.locator` (filtres : `sed 's/ file:.*//' | grep -v "java.base mandated" | sort`).
  → `provides s.api.Shape with s.triangle.TriangleFactory` … `uses s.api.Shape`

## Expériences (hors sortie attendue)

1. Rends le constructeur de `Square` privé : erreur de compilation, ou d'exécution ?
2. Dans D04, que lève `ServiceLoader.load(…).iterator().next()` ?
3. Pourquoi `types()` donne-t-il `Shape` pour le triangle ?
4. Mets `uses s.api.Shape;` dans `s.app` au lieu de `s.locator` : qu'est-ce qui casse ?

## Sortie attendue complète

```
--- D02 tous
formes (cote 6) : carre=36 triangle=18 ; types [Shape, Square] ; findFirst present true
--- D03 carre seul
formes (cote 6) : carre=36 ; types [Square] ; findFirst present true
--- D04 aucun fournisseur
formes (cote 6) : aucune ; types [] ; findFirst present false
--- D05
contains s.triangle
provides s.api.Shape with s.triangle.TriangleFactory
requires s.api
s.triangle
exports s.locator
requires s.api transitive
s.locator
uses s.api.Shape
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Rôle | Directive | Contient |
|---|---|---|
| Interface du service | `exports` | l'interface (ou la classe abstraite) |
| Localisateur | `uses I;` + `requires` (souvent `transitive`) | le code `ServiceLoader.load(I.class)` |
| Fournisseur | `requires` l'API + `provides I with C;` | l'implémentation (pas besoin d'exporter) |
| Consommateur | `requires` le localisateur | le code qui appelle le localisateur |

- **Un fournisseur valable :**
  - soit une classe publique, avec un constructeur **public sans argument**, qui implémente I ;
  - soit une classe qui a une méthode **`public static I provider()`**.
- **`ServiceLoader`** :
  - `load(I.class)` ;
  - `for (I x : loader)` ;
  - `stream()`, qui donne des `ServiceLoader.Provider<I>` (`get()` instancie, `type()` donne la classe sans instancier ; pour `provider()`, c'est le type de retour) ;
  - `findFirst()` rend un `Optional`.
- L'ordre des fournisseurs n'est **pas** garanti.
- **On peut ajouter ou retirer un fournisseur sans recompiler** le consommateur : seul le module path change.

</details>
