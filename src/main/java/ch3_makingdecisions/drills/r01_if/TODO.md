# Drill de rappel 1 — `if`/`else` et pattern matching

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall01`**.

## Défis

- ☐ **D01.** Une méthode « note » écrite en chaîne `if` / `else if` / `else` :
  - `A` à partir de 90 ;
  - `B` à partir de 75 ;
  - `C` à partir de 50 ;
  - sinon `D`.
  
  Affiche les notes de 95, 75, 74 et 10, collées.
  → `D01 : ABCD`
- ☐ **D02.** La parité de 7, rangée dans une variable **déclarée sans valeur**, puis affectée dans les deux branches d'un `if` / `else`.
  → `D02 : impair`
- ☐ **D03.** Une méthode « décrire un `Object` » :
  - un `Integer` supérieur à 100 donne « grand entier » (pattern + `&&` dans **une** condition) ;
  - un autre `Integer` donne « entier » suivi de sa valeur + 1 ;
  - un `String` donne « texte » suivi de sa valeur entre guillemets ;
  - sinon « autre ».
  
  Teste avec 150, 5, "ok" et 2.5.
  → `D03 : grand entier 150 | entier 6 | texte "ok" | autre`
- ☐ **D04.** Une méthode qui double un `Object`. Elle commence par `if (!(o instanceof Integer n)) return -1;`, puis utilise `n` **après** le `if`. Teste avec 21 et "21".
  → `D04 : 42 -1`
- ☐ **D05.** Avec `Object value = 42` : `if (!(value instanceof Integer n) || n < 0)` affiche un message, **sinon** affiche `n + 8`. Pourquoi `n` est-il utilisable dans le `||` **et** dans le `else` ?
  → `D05 : 50`
- ☐ **D06.** Un `if` **sans accolades**, suivi de **deux** instructions indentées comme si elles étaient dans le `if`. Seule la seconde s'affiche.
  → `D06 : sans accolades, seule la 1re instruction est dans le if`
- ☐ **D07.** `null instanceof String s` dans un ternaire.
  → `D07 : null n'est jamais une instance`
- ☐ **D08.** Avec `temp = 18`, un ternaire imbriqué :
  - `gel` sous 0 ;
  - `froid` sous 15 ;
  - `doux` sous 25 ;
  - sinon `chaud`.
  → `D08 : doux`
- ☐ **D09.** Deux pièges, dans la même ligne :
  - avec `Object boxed = 41` : dans `if (boxed instanceof Integer n)`, **réaffecte** `n = n + 1`, puis décris `n` et `boxed` ;
  - avec `hits = 0`, écris **exactement** `if (hits > 5); { hits++; }`. Que vaut `hits` ?
  → `D09 : n vaut 42, boxed vaut toujours 41 ; hits = 1`

## Expériences (hors sortie attendue)

1. `if (x = 5) { … }` avec un `int x` : quelle erreur ? Et avec un `boolean b`, `if (b = true)` ?
2. `if (o instanceof Integer i || i > 0)` : pourquoi `javac` refuse-t-il `i` ?
3. `Integer v = 5; if (v instanceof Integer w) {}` : que dit `javac` 17 (« expression type is a subtype of pattern type ») ?
4. Utilise la variable de pattern **après** un `if` sans `return` dans la branche fausse. Lis l'erreur.

## Sortie attendue complète

```
D01 : ABCD
D02 : impair
D03 : grand entier 150 | entier 6 | texte "ok" | autre
D04 : 42 -1
D05 : 50
D06 : sans accolades, seule la 1re instruction est dans le if
D07 : null n'est jamais une instance
D08 : doux
D09 : n vaut 42, boxed vaut toujours 41 ; hits = 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`if` :**
- la condition **doit** être un `boolean` : pas d'entier comme en C ;
- sans accolades, **une seule** instruction appartient au `if` ;
- `else if` n'est qu'un `else` qui contient un `if`.

**Le pattern matching `x instanceof Type v` :**
- il teste **et** déclare `v`, déjà casté ;
- **la portée de flux :** `v` n'existe que là où le compilateur est **certain** que le test est vrai :
  - après `&&` (oui), après `||` (non) ;
  - dans le `else` d'un `if (!(x instanceof T v))` ;
  - après un `if (!(x instanceof T v)) return;`, jusqu'à la fin du bloc ;
- `null instanceof T v` vaut toujours `false` ;
- **erreur de compilation** si `Type` n'est pas plus précis que le type de `x` (Java 17), ou si les types sont incompatibles ;
- la variable de pattern n'est pas `final` : on **peut** la réaffecter (ça ne change pas l'objet testé), mais c'est déconseillé.

**Le piège `if (cond);` :** le `;` est une instruction vide. C'est **elle**, le corps du `if` ; le bloc `{ }` qui suit s'exécute toujours.

</details>
