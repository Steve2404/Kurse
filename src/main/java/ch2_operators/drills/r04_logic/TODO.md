# Drill de rappel 4 — Comparaisons, logique, court-circuit, `instanceof`

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall04`**, avec un compteur `static` et une méthode `check(boolean)` qui l'incrémente puis rend son argument.

## Défis

- ☐ **D01.** `5 > 3`, `5 >= 5`, `5 != 5`, `'a' < 'b'`, `10 == 10.0`.
  → `D01 : true true false true true`
- ☐ **D02.** Avec `t = true` et `f = false` : `t & f`, `t | f`, `t ^ t`, `f ^ t`, `!t || f`.
  → `D02 : false true false true false`
- ☐ **D03.** `check(false) && check(true)` : le résultat, puis le compteur. Puis, compteur remis à 0, la même chose avec `&`.
  → `D03 : false 1 false 2`
- ☐ **D04.** Compteur à 0, puis `check(true) || check(false) || check(true)` : le résultat, puis le compteur.
  → `D04 : true 1`
- ☐ **D05.** Avec `n = 5`, trois expressions :
  - `n > 10 && n++ > 0` ;
  - `n > 1 || n++ > 0` ;
  - `n > 1 | n++ > 0`.
  
  Affiche les trois résultats, puis `n`.
  → `D05 : false true true 6`
- ☐ **D06.** `true || false && false`, puis `(true || false) && false`.
  → `D06 : true false`
- ☐ **D07.** Un `Object` qui contient un `Double` 2.5, et un `Object` `null`. Teste : `instanceof Double`, `instanceof Number`, `instanceof Integer`, puis `null instanceof Double`.
  → `D07 : true true false false`
- ☐ **D08.** Deux `new Recall04()` et une 3e variable qui désigne la 1re. Affiche `a == b`, `a == c`, `a != b`.
  → `D08 : false true true`

## Expériences (hors sortie attendue)

1. `boolean b = 5 == "5";` : quelle erreur ?
2. `int x = 3; boolean ok = x = 3;` : quelle erreur ? Et `boolean flag; boolean ok2 = flag = true;` ?
3. `String s = "a"; boolean bad = s instanceof Integer;` : pourquoi `javac` refuse-t-il ?

## Sortie attendue complète

```
D01 : true true false true true
D02 : false true false true false
D03 : false 1 false 2
D04 : true 1
D05 : false true true 6
D06 : true false
D07 : true true false false
D08 : false true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les comparaisons :**
- `< > <= >=` : sur les types numériques (dont `char`), avec promotion. `10 == 10.0` vaut donc `true` ;
- `==` et `!=` : sur des nombres, des `boolean` ou des **références**. Sur des références, ils comparent l'**identité** (le même objet), pas le contenu. On ne peut pas comparer un nombre à une référence d'un autre type.

**Les opérateurs logiques :**

| Opérateur | Court-circuit ? | Note |
|---|---|---|
| `&&` | oui : s'arrête au premier `false` | |
| `\|\|` | oui : s'arrête au premier `true` | |
| `&` / `\|` | **non** : évalue toujours les deux côtés | même résultat que `&&` / `\|\|`, mais tous les effets de bord ont lieu |
| `^` | — | vrai si les deux côtés **diffèrent** |

**La priorité :** `!` passe avant `&`, qui passe avant `^`, avant `|`, avant `&&`, avant `||`.

**`instanceof` :**
- `null instanceof X` vaut toujours `false` ;
- **erreur de compilation** si le type de l'expression ne peut **jamais** être un `X` (types sans rapport).

</details>
