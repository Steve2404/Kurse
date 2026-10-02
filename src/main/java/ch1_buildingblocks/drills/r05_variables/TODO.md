# Drill de rappel 5 — Variables, identificateurs, `var`, portée

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`**, avec un champ `static int counter` et un champ d'instance `int level = 1`.

## Défis

- ☐ **D01.** Quatre variables `int` aux noms **surprenants mais valides** :
  - une qui commence par `$` (vaut 5) ;
  - une qui commence par `_` (vaut 2) ;
  - une qui s'appelle **`Integer`** (vaut 3) ;
  - une qui mélange lettres, `$`, `_` et chiffres, et vaut `$… × _… + Integer`.
  → `D01 : 5 2 3 13`
- ☐ **D02.** Trois `int` déclarés sur **une seule ligne**, dont **seul le dernier** est initialisé (à 7). Affecte ensuite les deux autres (1, puis 1 + 7).
  → `D02 : 1 8 7`
- ☐ **D03.** Quatre `var` : un texte, un entier, un décimal, et un nombre de 5 milliards. Pourquoi ce dernier compile-t-il ?
  → `D03 : var 10 3.14 5000000000`
- ☐ **D04.** Une constante locale `final int`, puis un `final String` **déclaré sans valeur**, affecté **une fois** à la ligne suivante.
  → `D04 : 3 affecte une fois`
- ☐ **D05.** Le champ `static` (jamais initialisé), puis le champ d'instance, lu à travers un objet créé dans `main`.
  → `D05 : 0 1`
- ☐ **D06.** Une variable locale `level` (9) qui masque le champ. Affiche-la, puis le champ de l'objet. Ensuite, appelle une méthode d'instance qui déclare elle aussi un `level` local (5) et rend `local + this.level`.
  → `D06 : 9 1 6`
- ☐ **D07.** Dans un **bloc `{ }`** du `main`, une variable `inner` = `level + 1`.
  → `D07 : 10`
- ☐ **D08.** **Après** le bloc, déclare une **nouvelle** variable `inner` (100). Pourquoi est-ce permis ?
  → `D08 : 100`

## Expériences (hors sortie attendue)

1. **Ces identificateurs sont-ils valides ?** Écris-les, puis lis `javac` : `int 2nd;`, `int _;`, `int class;`, `int my-var;`, `int café;`, `int $;`, `int null;`.
2. **Quatre erreurs avec `var` :** `var x;`, `var y = null;`, `var a = 1, b = 2;`, et un champ `var z = 3;` (hors méthode).
3. Déclare `inner` **avant** le bloc, puis encore dans le bloc. Que dit `javac` ?
4. Lis une variable locale déclarée mais jamais initialisée.

## Sortie attendue complète

```
D01 : 5 2 3 13
D02 : 1 8 7
D03 : var 10 3.14 5000000000
D04 : 3 affecte une fois
D05 : 0 1
D06 : 9 1 6
D07 : 10
D08 : 100
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les identificateurs :**
- ils commencent par une lettre, `$` ou `_`, et peuvent ensuite contenir des chiffres ;
- ce ne peut être ni un mot réservé (`class`, `null`, `true`…), ni `_` **seul** ;
- les lettres Unicode (`é`) sont permises ;
- `Integer`, `String`, `var` **ne sont pas** des mots réservés : ce sont des identificateurs valides.

**Les déclarations :**
- `int a, b, c = 7;` n'initialise que `c` ;
- on ne peut pas écrire `int a, int b;`.

**`var` :**
- uniquement pour une **variable locale initialisée** dans sa déclaration ;
- pas `null`, pas plusieurs variables, ni champ, ni paramètre ;
- son type est fixé une fois pour toutes.

**`final` :**
- une seule affectation, éventuellement **après** la déclaration.

**La portée :**
- **locale** : de la déclaration à la fin du bloc ;
- **d'instance** : la vie de l'objet ;
- **de classe** (`static`) : la vie du programme ;
- une locale peut **masquer** un champ, et `this.` désigne alors le champ ;
- deux locales du **même nom** ne peuvent pas avoir des portées qui se chevauchent.

**Les valeurs par défaut :** pour les champs uniquement, jamais pour les variables locales.

</details>
