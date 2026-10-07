# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les modules

<details><summary>Indice 1</summary>

`open module config.legacy { … }` : le mot `open` se place **devant** `module`. Tout le module est alors ouvert à la réflexion ; à l'intérieur, tu écris seulement `exports config.legacy;`.

</details>

<details><summary>Indice 2</summary>

`config.binder` ne connaît **aucun** des types qu'il remplit : il les reçoit sous forme de `Class<?>`. C'est pourquoi il ne requiert pas `config.model`.

</details>

---

## Étape 2 — Le binder

<details><summary>Indice 1</summary>

`type.getDeclaredConstructor()` donne le constructeur sans argument. `setAccessible(true)`, puis `newInstance()`. Pour un champ : `type.getDeclaredField(nom)`, `setAccessible(true)`, puis `field.set(objet, valeur)`.

</details>

<details><summary>Indice 2</summary>

`convert` compare `type` à `int.class`, `long.class`, `boolean.class` et `List.class`. Pour la récursion du `dump` : `value.getClass().getModule()` donne le module du type, `isNamed()` et `getName()` permettent le test.

</details>

---

## Étape 3 — Le programme

<details><summary>Indice 1</summary>

`ServerConfig.class.getModule().isOpen("config.model", binderModule)` teste l'ouverture **vers** un module ; `isOpen(paquet)` (un seul argument) teste l'ouverture à tous.

</details>

<details><summary>Indice 2</summary>

Les deux refus sont des exceptions **différentes** : la réflexion refusée lève une `InaccessibleObjectException` (une `RuntimeException`), l'accès direct à une classe non exportée lève une `IllegalAccessError` (une `Error`).

</details>

---

## Étape 4 — Le script `build.sh`

<details><summary>Indice 1</summary>

La forme des options : `--add-exports module/paquet=cible` et `--add-opens module/paquet=cible`. Pas d'espace autour du `/` et du `=`.

</details>

<details><summary>Indice 2</summary>

Sans `--add-exports`, la compilation **échoue** à cause de `Main` qui utilise `Defaults`. Pour avoir quand même des modules à lancer, la 2e compilation, avec l'option, est obligatoire.

</details>
