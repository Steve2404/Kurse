# Drill de rappel 3 — Casts, débordements, affectations composées

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall03`**.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 2 et 4) et projet 5 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_casts` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall03` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Les casts `(byte) 130`, `(byte) -129`, `(byte) 256`, `(short) 65_535`.
  → `D01 : -126 127 0 -1`
- ☐ **D02.** `(int) 3.99`, `(int) -3.99`, `(long) 1e19`, `(int) 'Z'`.
  → `D02 : 3 -3 9223372036854775807 90`
- ☐ **D03.** `(char) 66`, `(char) ('a' + 25)`, `(float) 0.1`, `(double) 0.1f`.
  → `D03 : B z 0.1 0.10000000149011612`
- ☐ **D04.** Un `byte` 100 auquel tu fais `+= 100`. Un `short` 10 multiplié deux fois avec `*=` (par 1000, puis par 10).
  → `D04 : -56 -31072`
- ☐ **D05.** Un `int` 7 : `/= 2`, puis `-= -3`, puis `%= 4`.
  → `D05 : 2`
- ☐ **D06.** Déclare `x` sans valeur, puis `y = (x = 3) + 1`. Affiche `x` et `y`.
  → `D06 : 3 4`
- ☐ **D07.** Un `long` 10 casté en `int` + 5. Un `double` qui reçoit l'entier 9. Puis `(int) (dd / 2)` et `(int) dd / 2`.
  → `D07 : 15 9.0 4 4`
- ☐ **D08.** Un compteur à 0, puis exactement `counter += counter++ + ++counter;`.
  → `D08 : 2`

## Expériences (hors sortie attendue)

1. `short s = 10; s = s * 2;` : l'erreur. Puis `s *= 2;` : pourquoi ça compile ?
2. `int i = 3.0;` puis `int j = (int) 3.0;`.
3. `byte b = 127;` compile, mais `byte c = 128;` non. Et `final int k = 100; byte d = k;` ?

## Sortie attendue complète

```
D01 : -126 127 0 -1
D02 : 3 -3 9223372036854775807 90
D03 : B z 0.1 0.10000000149011612
D04 : -56 -31072
D05 : 2
D06 : 3 4
D07 : 15 9.0 4 4
D08 : 2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Élargissement et rétrécissement :**
- **élargissement** (`byte` → `short` → `int` → `long` → `float` → `double`, et `char` → `int`) : automatique ;
- **rétrécissement** : il faut un **cast explicite** `(type)`. Une **constante** qui tient dans le type cible est permise sans cast (`byte b = 10;`).

**Ce que fait un cast :**
- **entier vers entier plus petit :** on garde les bits de poids faible, ce qui donne le « tour » (`(byte) 130 = 130 - 256`) ;
- **flottant vers entier :** troncature vers zéro, puis **saturation** aux bornes (`(long) 1e19 = Long.MAX_VALUE`) ;
- **la portée du cast :** il s'applique à ce qui le **suit immédiatement**. `(int) dd / 2` vaut `((int) dd) / 2`.

**Les affectations composées :**
- `+=`, `-=`, `*=`, `/=`, `%=` contiennent un **cast implicite** : `b += 100` vaut `b = (byte) (b + 100)` ;
- la valeur de gauche est lue **avant** l'évaluation de la droite.

**L'affectation est une expression :** `(x = 3)` vaut 3.

</details>
