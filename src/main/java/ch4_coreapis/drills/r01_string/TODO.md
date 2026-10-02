# Drill de rappel 1 — Les méthodes de base de `String`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall01`** dans le paquet `ch4_coreapis.drills.r01_string`.
- Chaque ligne commence par `Dxx : ` et les valeurs sont séparées par un espace.

## Défis

- ☐ **D01.** Sur `s = "animals"` : la longueur, le premier caractère, `charAt(6)`, puis le dernier caractère calculé avec `length()`.
  → `D01 : 7 a s s`
- ☐ **D02.** Les **quatre** formes de `indexOf`, toujours sur `s` :
  - `indexOf('a')` ;
  - `indexOf('a', 4)` ;
  - `indexOf("al")` ;
  - `indexOf("al", 5)`.
  
  Puis `lastIndexOf('a')`.
  → `D02 : 0 4 4 -1 4`
- ☐ **D03.** `substring(3)`, `substring(3, 4)`, `substring(3, 3)` entre crochets, puis `substring(0, s.length())`.
  → `D03 : mals m [] animals`
- ☐ **D04.** Sur `mixed = "Java Rocks"` : majuscules, minuscules, puis `mixed` lui-même.
  → `D04 : JAVA ROCKS java rocks Java Rocks`
- ☐ **D05.** Six tests booléens :
  - `"abc".equals("ABC")` ;
  - `"abc".equalsIgnoreCase("ABC")` ;
  - `mixed` commence par `"Ja"` ;
  - `mixed` commence par `"va"` **à partir de l'indice 2** ;
  - `mixed` finit par `"ks"` ;
  - `mixed` contient `"a R"`.
  → `D05 : false true true true true true`
- ☐ **D06.** Sur `"banana"` :
  - `replace` avec deux **char** (`a` → `o`) ;
  - `replace` avec deux **textes** (`an` → `AN`).
  
  Puis `"ab"` concaténé à `"cd"` avec `concat`, et `"-"` répété 3 fois.
  → `D06 : bonono bANANa abcd ---`
- ☐ **D07.** Sur `"  pad  "`, chacun entre crochets :
  - `strip` ;
  - `stripLeading` ;
  - `stripTrailing` ;
  - `trim`.
  
  Puis `"".isEmpty()`, `" ".isEmpty()` et `" ".isBlank()`.
  → `D07 : [pad] [pad  ] [  pad] [pad] true false true`
- ☐ **D08.** Trois résultats :
  - `String.join` de `a`, `b`, `c` avec `/` ;
  - le nombre de morceaux de `"x,y,,z".split(",")` ;
  - `"AbC".compareTo("Abd")`.
  → `D08 : a/b/c 4 -33`

## Expériences (hors sortie attendue)

1. `s.charAt(7)` et `s.substring(8)` : quelle exception, et à quel moment (compilation ou exécution) ?
2. `s.substring(4, 2)` : que se passe-t-il ?
3. `"a,b,,".split(",").length` : combien ? Où sont passés les vides de la fin ?
4. Pourquoi `"AbC".compareTo("Abd")` vaut-il `-33` ? Calcule `'C' - 'd'`.

## Sortie attendue complète

```
D01 : 7 a s s
D02 : 0 4 4 -1 4
D03 : mals m [] animals
D04 : JAVA ROCKS java rocks Java Rocks
D05 : false true true true true true
D06 : bonono bANANa abcd ---
D07 : [pad] [pad  ] [  pad] [pad] true false true
D08 : a/b/c 4 -33
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les indices :**
- ils commencent à 0 ;
- `substring(début, fin)` : la fin est **exclue**, et `substring(i, i)` est vide ;
- `fin == length()` est permis ; au-delà, c'est une `StringIndexOutOfBoundsException` à l'exécution.

**`indexOf` :**
- quatre formes : `(char)`, `(char, départ)`, `(String)`, `(String, départ)` ;
- elle rend `-1` si rien n'est trouvé, **jamais** d'exception ;
- `lastIndexOf` cherche depuis la fin.

**`replace` :**
- deux formes : `(char, char)` et `(CharSequence, CharSequence)` ;
- elle remplace **toutes** les occurrences.

**`strip` ou `trim` :**
- `strip` connaît les espaces Unicode, `trim` seulement les caractères ≤ `' '` ;
- `isEmpty` : longueur 0 ; `isBlank` : vide ou seulement des blancs.

**`compareTo` :**
- la différence au premier caractère différent ;
- sinon la différence des longueurs ;
- les majuscules passent avant les minuscules.

</details>
