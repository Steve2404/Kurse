# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les six cas

<details><summary>Indice 1</summary>

Chaque record a son propre fichier, dans le même paquet que `Json`. Le constructeur **compact** d'un record (`public JsonArray { … }`) peut réaffecter son paramètre avant qu'il soit rangé : c'est là que se fait la copie.

</details>

<details><summary>Indice 2</summary>

Pour `JsonObject` : d'abord un `forEach` sur la map reçue qui appelle `Objects.requireNonNull` sur la clé et sur la valeur, puis `Collections.unmodifiableMap(new LinkedHashMap<>(members))`. La copie protège de l'appelant ; l'enveloppe protège de tout le monde.

</details>

---

## Étape 2 — La grammaire et les valeurs simples

<details><summary>Indice 1</summary>

Écris d'abord les petits outils du curseur : `skipSpaces()`, `skip(char c)` (avance et rend `true` si le caractère courant est `c`), `digits()` (avance sur des chiffres et rend `true` s'il y en avait au moins un). Les règles s'écrivent ensuite presque mot pour mot.

</details>

<details><summary>Indice 2</summary>

Un nombre : retiens `start = pos`, puis `skip('-')`, la partie entière (`0` seul, ou des chiffres), la fraction (si `skip('.')`, il faut `digits()`), l'exposant (si `e` ou `E`, un signe facultatif, puis `digits()`). À la fin, `text.substring(start, pos)` est le texte exact du nombre. Pour un échappement, un `switch` sur le caractère qui suit l'antislash rend le caractère décodé.

</details>

---

## Étape 3 — Tableaux et objets

<details><summary>Indice 1</summary>

Une boucle `do { … } while (separateur(…))`, où `separateur` lit le caractère après l'élément : `,` → `true` (on continue), le fermant → `false` (on s'arrête), autre chose → une erreur. Le cas du conteneur vide se traite **avant** la boucle.

</details>

<details><summary>Indice 2</summary>

Pour la clé en double, retiens la position du `"` de la clé **avant** de la lire : c'est là que l'erreur doit pointer. `members.containsKey(key)` sur la `LinkedHashMap` en cours de remplissage.

</details>

---

## Étape 4 — Les erreurs

<details><summary>Indice 1</summary>

Deux méthodes d'aide : `error(String reason)` (à la position courante) et `errorAt(int index, String reason)`. Elles **rendent** l'exception au lieu de la lancer : on écrit `throw error("…")`, et le compilateur sait que la ligne ne continue pas.

</details>

<details><summary>Indice 2</summary>

Une méthode `peekOrFail()` qui saute les espaces, lance `fin du texte inattendue` si le texte est fini, et rend le caractère courant, évite de répéter ce test partout. Pour la colonne : parcours le texte jusqu'à `index`, et retiens le numéro de ligne et l'indice du début de la ligne courante.

</details>

---

## Étape 5 — La profondeur

<details><summary>Indice 1</summary>

Un champ `depth`. Une méthode `enter()`, appelée sur chaque `[` et `{`, l'augmente et vérifie la limite **avant** d'avancer le curseur (pour que l'erreur pointe sur le crochet).

</details>

<details><summary>Indice 2</summary>

N'oublie pas de redescendre (`depth--`) à la fin de **chaque** tableau et de **chaque** objet : sans cela, un tableau de 1 000 tableaux vides dépasserait la limite alors qu'il n'a que deux niveaux.

</details>

---

## Étape 6 — L'écrivain

<details><summary>Indice 1</summary>

Une seule méthode récursive `write(Json json, StringBuilder out, boolean pretty, int level)`, et une aide `newline(out, pretty, level)` qui n'écrit rien en mode compact, et sinon `\n` puis `level` fois deux espaces (`"  ".repeat(level)`).

</details>

<details><summary>Indice 2</summary>

Dans un tableau non vide : `[`, puis pour chaque élément une virgule (sauf le premier), `newline(level + 1)` et l'élément au niveau `level + 1` ; enfin `newline(level)` et `]`. L'échappement : un `switch` sur le caractère, avec un `default` qui choisit entre `\u%04x` (code < `0x20`) et le caractère tel quel.

</details>

---

## Étape 7 — Le test de propriété

<details><summary>Indice 1</summary>

Le générateur est récursif, comme le parseur : `randomValue` tire un nombre de 0 à 5 et rappelle `randomValue(random, depth + 1)` pour les éléments d'un tableau ou d'un objet. `random.nextInt(depth >= 4 ? 4 : 6)` arrête la croissance.

</details>

<details><summary>Indice 2</summary>

Avec `@RepeatedTest(300)`, ajoute un paramètre `RepetitionInfo info` à la méthode : `info.getCurrentRepetition()` donne une graine différente à chaque répétition. Pour les clés d'un objet tiré au hasard, ajoute l'indice à la fin (`randomString(random) + i`) : deux clés tirées pareilles écraseraient la première.

</details>

---

## Étape 8 — Lire un objet métier, les mutants

<details><summary>Indice 1</summary>

`if (members.get(key) instanceof JsonString s) { return s.value(); }`, puis `throw` après le `if` : un champ absent donne `null`, et `null instanceof …` est faux. Un seul chemin pour « absent » et « mauvais type ».

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | la limite devient `>=` : 500 niveaux refusés | 500 niveaux passent |
| 2, 3 | la profondeur ne redescend pas après un tableau, après un objet | 1 000 tableaux (objets) voisins |
| 4 | la clé en double est acceptée | `{"a":1,"b":2,"a":3}` |
| 5 | seul `\n` est refusé dans une chaîne | une vraie tabulation dans une chaîne |
| 6 | `\/` n'est plus reconnu | `"a\/b"` |
| 7 | `\b` devient la lettre `b` | `"\b\f\n\r\t"` |
| 8 | `\u` lu en base 10 | `"é"` |
| 9 | `012` accepté | `012` refusé |
| 10 | un point sans chiffres après est permis | `1.` refusé, et `3.25` |
| 11 | le signe `-` de l'exposant n'est plus lu | `2E-2` |
| 12 | l'exposant géant n'est plus transformé en `JsonException` | `1e99999999999` |
| 13 | le texte en trop est ignoré | `1 2` |
| 14 | `\r` n'est plus un espace | un document avec `\r\n` |
| 15 | la colonne décalée d'un cran après un saut de ligne | une erreur sur la ligne 4 ou 5 |
| 16 | `getLong` tronque `1.5` en 1 | `getLong` sur `1.5` refusé |
| 17 | `Map.copyOf` : l'ordre des clés se perd | l'ordre d'un objet de 5 ou 8 clés |
| 18 | le builder accepte les doublons | le doublon refusé |
| 19 | le tableau n'est plus copié | modifier la liste d'origine |
| 20 | `\r` écrit `\n` | l'échappement de `\r` |
| 21 | `\u001F` en majuscules | l'échappement de `\u001f` (le message exact) |
| 22 | toujours `": "`, même en compact | le résultat compact exact |
| 23 | un tableau vide s'écrit sur deux lignes | `[]` en pretty |
| 24 | plus de virgule entre les membres | le résultat compact exact |

</details>
