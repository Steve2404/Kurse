# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`PathLab.java`](PathLab.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), **sous Windows**.

---

## Étape 1 — Décomposer, comparer

**Les pièges de la ligne :**
- `getRoot()` vaut `null` : un chemin **relatif** n'a pas de racine ;
- `getNameCount()` vaut 4, et les noms sont numérotés à partir de 0 : `getName(1)` est `api` ;
- `subpath(1, 3)` prend les noms 1 et 2 (la fin est exclue, comme `substring`) ;
- `startsWith("doc")` vaut `false` : on compare des **noms entiers** (`docs`), pas des débuts de texte ;
- `toAbsolutePath()` ajoute le répertoire courant devant : le chemin devient absolu.

---

## Étape 2 — Normaliser, relativiser

`normalize()` retire les `.` et résout les `..` **sur le texte**, sans regarder le disque. `relativize(cible)` répond à « comment aller de ce dossier à la cible ? » : `docs/guide` vers `docs/api/index.html` donne `../api/index.html`. L'aller-retour `source.getParent().resolve(href).normalize()` redonne la cible : c'est la preuve que le lien est juste.

---

## Étape 3 — Le mini-shell et l'arborescence

**Question — pourquoi `cd docs//api` marche-t-il ?** Quand Java analyse un chemin, il **fusionne** les séparateurs répétés. Vérifié : `Path.of("docs//api")` a **2** noms (`docs` et `api`), exactement comme `docs/api`.

---

## Étape 4 — `resolve`, `File`, erreurs

**Les trois erreurs** (vérifiées) :
- `guide.subpath(1, 5)` : `IllegalArgumentException`, car le chemin n'a que 2 noms ;
- `guide.relativize(guide.toAbsolutePath())` : `IllegalArgumentException: 'other' is different type of Path`. On ne peut pas relativiser un chemin relatif par rapport à un absolu ;
- `Path.of("a\u0000b")` : `InvalidPathException`, le caractère de code 0 est interdit dans un chemin.

**Expérience 1 — `Path.of("/a/b").isAbsolute()` :** vérifié sous **Windows** : `false`. Sous Windows, un chemin absolu a besoin d'une **lettre de lecteur** : `Path.of("C:/a/b").isAbsolute()` vaut `true`. Sous Linux et macOS, `/` est la racine du système : `/a/b` y est absolu (d'après la Javadoc de `Path.isAbsolute()` ; non lancé ici, la machine de test étant sous Windows). C'est pourquoi le projet affiche des chemins **relatifs**.

**Expérience 2 — `Path.of("a/b").getRoot()` :** `null` (vérifié), comme tout chemin relatif.

**Expérience 3 — `Path.of("").getNameCount()` :** `1` (vérifié). Le chemin vide a **un** nom : le nom vide. Il désigne le répertoire courant.
