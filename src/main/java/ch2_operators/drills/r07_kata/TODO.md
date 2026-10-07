# Drill de rappel 7 — Kata mixte chronométré (tout le chapitre 2)

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`**, avec un compteur `static` et une méthode `test(int)` qui l'incrémente et rend `valeur > 0`.
- `Check` lance ton `main` avec `29 5 1011`, donc `n` = 29, `m` = 5, et un masque écrit en binaire.

**Les notions de ce drill ont été apprises dans :** projets 1 à 5 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_kata` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall07` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
   Ce drill reçoit des **arguments** : lance `Recall07` une fois, puis Run → Edit Configurations… → **Recall07** → Program arguments : `29 5 1011`.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `n / m`, `n % m`, le modulo **toujours positif** de `n` par `m`, puis `n / m` en `double` (un seul cast).
  → `D01 : 5 4 4 5.8`
- ☐ **D02.** Le plus grand de `n` et `m` (ternaire), `pair` ou `impair` (avec `%`), puis « `n` est pair » testé **avec `&`**.
  → `D02 : 29 impair false`
- ☐ **D03.** `n` dans un `byte` (cast), auquel tu fais `+= m`. Puis `n * 1000` casté en `short`.
  → `D03 : 34 29000`
- ☐ **D04.** Avec `i = n` : `j = i++ + i-- - --i`, puis affiche `j` et `i`.
  → `D04 : 31 28`
- ☐ **D05.** `test(n) && test(m) || test(-1)` : le résultat, puis le nombre d'appels.
  → `D05 : true 2`
- ☐ **D06.** Avec le masque lu en base 2 :
  - `masque & 0b0110` ;
  - `masque | 1 << 4` ;
  - `masque >> 1` ;
  - `~masque & 0b1111` en binaire.
  → `D06 : 2 27 5 100`
- ☐ **D07.** Le `char` `'a' + n % 26` (cast). Puis sa majuscule, par une soustraction de 32 et un cast. Puis `fin` si la lettre est après `'m'`, sinon `debut`.
  → `D07 : d D debut`
- ☐ **D08.** Un `long` qui vaut `Integer.MAX_VALUE`, auquel tu fais `+= n`. Puis la même chose avec un `int`.
  → `D08 : 2147483676 -2147483620`

## Sortie attendue complète

```
D01 : 5 4 4 5.8
D02 : 29 impair false
D03 : 34 29000
D04 : 31 28
D05 : true 2
D06 : 2 27 5 100
D07 : d D debut
D08 : 2147483676 -2147483620
```

## Après le kata

- **Pour chaque défi raté ou trop lent :** refais le drill de son thème le lendemain.
  - r01 : unaires ;
  - r02 : arithmétique ;
  - r03 : casts ;
  - r04 : logique ;
  - r05 : bits ;
  - r06 : ternaire et priorité.
- **Note ton temps** dans `drills/README.md`.
