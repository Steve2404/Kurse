# Drill de rappel 6 — Ternaire et priorité des opérateurs

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`**.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 3) et projet 2 (étape 1, tableau des priorités). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_ternary` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall06` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Avec `age = 17` : `majeur` ou `mineur`, puis `enfant` (moins de 13), `ado` (moins de 18) ou `adulte`, avec un ternaire **imbriqué**.
  → `D01 : mineur ado`
- ☐ **D02.** `(true ? 1 : 2.0)`, `(false ? 'a' : 98)`, `(true ? 'a' : 0)`.
  → `D02 : 1.0 b a`
- ☐ **D03.** Avec `x = 5` : `y = x > 3 ? x++ : x--`, puis affiche `y` et `x`.
  → `D03 : 5 6`
- ☐ **D04.** Quatre concaténations :
  - `"D04 : " + "1" + 2 + 3` (le `"1" + 2 + 3` directement dans le `println`) ;
  - `(1 + 2 + "3")` ;
  - `("" + 1 + 2)` ;
  - `(1 + '2')`.
  → `D04 : 123 33 12 51`
- ☐ **D05.** `2 + 3 * 4`, `(2 + 3) * 4`, `-2 * 3 + 4`, `20 / 4 * 2`.
  → `D05 : 14 20 -2 10`
- ☐ **D06.** Avec `a = 2` : `b = a++ + a * 2`, puis affiche `b` et `a`.
  → `D06 : 8 3`
- ☐ **D07.** `3 + 4 > 6 && 2 * 2 == 4 || false`, puis `5 & 3 | 8`, puis `5 & (3 | 8)`.
  → `D07 : true 9 1`
- ☐ **D08.** Avec `z = 10` : `z += z++ + z--;`. Puis un ternaire **imbriqué** :
  - `petit` si `z` ≤ 30 ;
  - sinon `trois` si `z % 7 == 3` ;
  - sinon `autre`.
  → `D08 : 31 trois`

## Expériences (hors sortie attendue)

1. `int r = true ? 1 : "un";` : quelle erreur ?
2. `5 > 3 ? System.out.println("a") : System.out.println("b");` comme instruction seule : pourquoi est-ce refusé ?
3. `System.out.println("total : " + 2 + 3);` contre `System.out.println(2 + 3 + " total");` : prédis.

## Sortie attendue complète

```
D01 : mineur ado
D02 : 1.0 b a
D03 : 5 6
D04 : 123 33 12 51
D05 : 14 20 -2 10
D06 : 8 3
D07 : true 9 1
D08 : 31 trois
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Le ternaire `cond ? a : b` :**
- seule la branche choisie est **évaluée** (effets de bord compris) ;
- c'est une **expression** : on ne peut pas l'utiliser seul comme instruction ;
- les deux branches sont **promues** vers un type commun (`int` et `double` donnent `double`). Une constante `int` qui tient dans un `char` combinée à un `char` donne un `char` ;
- les ternaires imbriqués se lisent `a ? x : (b ? y : z)`.

**La priorité, du plus fort au plus faible :**
1. suffixes `x++` `x--` ;
2. préfixes `++x` `--x`, unaires `+ - ! ~` et cast ;
3. `* / %` ;
4. `+ -` ;
5. `<< >> >>>` ;
6. `< > <= >= instanceof` ;
7. `== !=` ;
8. `&` ;
9. `^` ;
10. `|` ;
11. `&&` ;
12. `||` ;
13. `? :` ;
14. affectations `= += -= …`, évaluées de **droite à gauche**.

**La concaténation :** de gauche à droite. Dès qu'un opérande est un `String`, `+` concatène. Mais `1 + 2 + "3"` additionne d'abord : `"33"`.

</details>
