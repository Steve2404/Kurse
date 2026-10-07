# Drill de rappel 6 — `break`, `continue`, étiquettes, `return`

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`**.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1 et 5) et projet 3 (étape 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_jumps` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall06` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** De 0 à 9 : arrête-toi à 6 (`break`), saute les pairs (`continue`), et concatène le reste.
  → `D01 : 135`
- ☐ **D02.** Deux boucles imbriquées de 1 à 3, avec l'étiquette `outer:`. Quand `j == 2`, fais `continue outer`. Concatène `i` et `j` suivis d'une espace.
  → `D02 : 11 21 31`
- ☐ **D03.** Cherche le **premier** couple (i, j) de 1 à 5 tel que i × j = 12, et sors des deux boucles avec `break search`.
  → `D03 : 3x4`
- ☐ **D04.** Une boucle `while (true)` qui incrémente `n` et s'arrête dès que n² > 50.
  → `D04 : 8`
- ☐ **D05.** Une méthode qui rend le premier multiple de `of` **strictement supérieur** à `above`. Écris-la avec un `for` **sans condition**, et sors avec `return`. Teste (7, 30) et (5, 5).
  → `D05 : 35 10`
- ☐ **D06.** Un `do/while` de 1 à 10 qui compte les multiples de 3. Pour les autres nombres, fais `continue`. Où saute ce `continue` ?
  → `D06 : 3 10`
- ☐ **D07.** Une boucle (étiquette `loop:`) de 0 à 2 contenant un `switch` classique :
  - 1 ajoute `b` puis `break` ;
  - 2 ajoute `c` puis **`break loop`** ;
  - le `default` ajoute `a`.
  
  Après le `switch`, ajoute `.`.
  → `D07 : a.b.c`
- ☐ **D08.** Une boucle `for` sur r (de 0 à 3, étiquette `rows:`) contenant un `while` sur c :
  - incrémente c ;
  - si c > r, fais `continue rows` ;
  - sinon ajoute c au total.
  → `D08 : 10`

## Expériences (hors sortie attendue)

1. Une instruction juste après un `break;` dans le même bloc : quelle erreur ?
2. `continue` dans un `switch` qui n'est **pas** dans une boucle : que dit `javac` ?
3. Une étiquette sur une instruction qui n'est pas une boucle (`etiq: { … break etiq; }`) : est-ce légal ?

## Sortie attendue complète

```
D01 : 135
D02 : 11 21 31
D03 : 3x4
D04 : 8
D05 : 35 10
D06 : 3 10
D07 : a.b.c
D08 : 10
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Instruction | Effet |
|---|---|
| `break;` | sort de la boucle **ou du `switch`** le plus proche |
| `continue;` | passe au tour suivant de la boucle la plus proche (dans un `do/while` : saute à la **condition** ; dans un `for` : à la **mise à jour**) |
| `break etiq;` | sort de l'instruction étiquetée |
| `continue etiq;` | tour suivant de la **boucle** étiquetée |
| `return;` / `return v;` | sort de la **méthode** entière |

**Les règles :**
- **une étiquette** (`nom:`) se place devant une instruction : le plus souvent une boucle, mais un bloc `{ }` est permis avec `break`. `continue etiq` exige une **boucle** ;
- **dans un `switch` à l'intérieur d'une boucle :** `break` sort du `switch` seulement. Pour sortir de la boucle, il faut une étiquette ;
- **le code inaccessible :** une instruction placée juste après `break`, `continue` ou `return` dans le même bloc est une **erreur de compilation**.

</details>
