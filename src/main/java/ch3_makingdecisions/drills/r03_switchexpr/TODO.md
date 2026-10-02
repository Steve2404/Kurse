# Drill de rappel 3 — Le `switch` expression

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall03`**.

## Défis

- ☐ **D01.** Une méthode « jours dans le mois », réduite à `return switch (…) { … };` :
  - février : 28 ;
  - 4, 6, 9 et 11 : 30 ;
  - sinon 31.
  
  Teste avec 2, 4 et 12.
  → `D01 : 28 30 31`
- ☐ **D02.** Une méthode « saison » sur un `String` de mois abrégé. Trois `case` à 3 valeurs chacun. Le `default` est un **bloc** qui rend `automne` avec `yield`. Teste avec `jan`, `mai` et `oct`.
  → `D02 : hiver printemps automne`
- ☐ **D03.** Avec `score = 85`, un `switch` expression sur `score / 10` :
  - 10 et 9 donnent `A` ;
  - 8 est un **bloc** qui rend `B+` si l'unité est ≥ 5, sinon `B` ;
  - 7 donne `C` ;
  - sinon `D`.
  → `D03 : B+`
- ☐ **D04.** `var result = switch (1) { case 1 -> 10; default -> 2.5; };`. Que vaut `result`, et pourquoi ?
  → `D04 : 10.0`
- ☐ **D05.** Un `switch` expression écrit **directement dans le `println`**, sur le `char` `'*'`, qui calcule `6 op 3`.
  → `D05 : 18`
- ☐ **D06.** Un `switch` expression écrit avec la forme **`case 1:` + `yield`**, et non la flèche. Code 2.
  → `D06 : deux`
- ☐ **D07.** Avec `counter = 0` : `int got = switch (counter++) { case 0 -> counter * 100; default -> -1; };`. Affiche `got`, puis `counter`.
  → `D07 : 100 1`
- ☐ **D08.** Une boucle de 0 à 3 qui concatène le résultat d'un `switch` expression sur `i % 3` : `z`, `u` ou `d`.
  → `D08 : zudz`

## Expériences (hors sortie attendue)

1. Retire le `default` de D01 : quelle erreur (« does not cover all possible input values ») ?
2. Dans un bloc de branche, oublie le `yield` : quelle erreur ?
3. Écris `return` dans un bloc de `switch` expression, à la place de `yield` : que dit `javac` ?
4. `int r = switch (x) { case 1 -> "un"; default -> 2; };` : quelle erreur ?

## Sortie attendue complète

```
D01 : 28 30 31
D02 : hiver printemps automne
D03 : B+
D04 : 10.0
D05 : 18
D06 : deux
D07 : 100 1
D08 : zudz
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Le `switch` expression produit une valeur :**
- on peut l'affecter, le rendre ou l'utiliser dans une expression ;
- **forme flèche :** `case x -> valeur;` ou `case x -> { …; yield valeur; }` ;
- **forme deux-points :** `case x: yield valeur;` (pas de fall-through accidentel si chaque branche fait `yield`).

**L'exhaustivité :**
- il **doit** couvrir **toutes** les valeurs possibles ;
- avec `int` ou `String`, il faut donc un `default`. Avec un `enum`, toutes les constantes suffisent.

**Les branches :**
- **`yield`** sort une valeur d'un bloc. `return` et `break` sont interdits pour sortir du `switch` expression ;
- leurs types doivent être **compatibles** avec la cible. Sans cible typée (`var`), les numériques sont **promus** vers un type commun ;
- l'expression testée n'est évaluée **qu'une fois** (effets de bord compris).

</details>
