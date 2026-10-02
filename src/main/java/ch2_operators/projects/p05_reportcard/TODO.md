# Projet 5 (CAPSTONE) — Le bulletin de notes

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 2 (avec le chapitre 1).
- la promotion `double`/`int` ;
- la **division entière** et la **portée d'un cast** ;
- l'arrondi par cast ;
- `+=` / `-=` ;
- les options en **bits** ;
- les **ternaires imbriqués** ;
- `&&` / `||` et leur priorité ;
- l'arithmétique de `char`.

**Ce qui est donné :** `Check.java`. Arguments : `14.5 9.5 17 3 2 1 4 101`.
- les notes de maths, de physique et de français ;
- leurs coefficients ;
- le nombre d'absences ;
- les options **écrites en binaire** :
  - bit 0 : délégué ;
  - bit 1 : sport ;
  - bit 2 : latin.

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p05_reportcard`. La classe du `main` s'appelle **`ReportCard`**.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle, pas de méthode de `String`, pas de `Math`.

**Indication :** peu d'indices ici. Recalcule **chaque** ligne de la sortie attendue à la main avant de coder.

---

## Le problème

Le bulletin d'un élève, calculé selon ces règles :
1. **La moyenne pondérée** des trois notes.
2. **Les absences :** au-delà de 3, chacune retire 0.25 point.
3. **Les bonus :** latin +0.5, délégué +0.2. Le sport ne rapporte rien, mais s'affiche.
4. **Le plafond :** la moyenne ne dépasse jamais 20.
5. **L'arrondi :** au **dixième le plus proche**, sans `Math`.
6. **La mention :**
   - Très bien à partir de 16 ;
   - Bien à partir de 14 ;
   - Assez bien à partir de 12 ;
   - Passable à partir de 10 ;
   - sinon Ajourné.
7. **L'écart** à la moyenne de classe (11.5), avec son signe.
8. **La lettre :** de `A` (20) à `E` (0), un rang tous les 4 points.
9. **Admis :** moyenne ≥ 10 **et** moins de 10 absences. **Félicitations :** moyenne ≥ 16, **ou** moyenne ≥ 14 **et** délégué.

---

## Tableau de bord

### ☐ Étape 1 — La moyenne et le piège du cast

```
points 79.5 / coefficients 6 = 13.25
piege : (int) points = 79 ; 79 / 6 = 13 ; (double) (79 / 6) = 13.0 ; (double) 79 / 6 = 13.166666666666666
```
- **La moyenne :** quel est le type de `maths * cMaths` ? Et celui de la somme des coefficients ?
- **La ligne « piège »** compare trois façons de diviser 79 par 6. Explique, en commentaire, **à quoi s'applique** le cast `(double)` dans chaque cas.

### ☐ Étape 2 — Pénalité, options et bonus

```
absences 4 -> penalite 0.25 ; options 101 : delegue latin
```
- **Les options :** lis `"101"` **en base 2**. Les masques sont des constantes construites par décalage.
- **Le texte des options :** trois ternaires, chacun avec un test de bit.
- **Les bonus :** des affectations composées, avec un ternaire à droite.

### ☐ Étape 3 — Arrondi, mention, écart, lettre

```
moyenne finale : 13.7 (Assez bien)
ecart a la classe : +2.2
lettre : B, admis : true, felicitations : false
```
- **L'arrondi au dixième :** décale de 10, ajoute 0.5, tronque **par un cast**, puis redivise.
  - **Question :** pourquoi diviser par `10.0` et pas par `10` ?
- **La mention :** un ternaire imbriqué à 5 issues.
- **L'écart :** le `+` ne s'affiche que pour un écart positif. Pense à l'arrondir, sinon tu obtiendras `2.1999999999999993`.
- **La lettre :** `'A'` plus un rang, puis un cast `(char)`.
- **Félicitations :** dans `a || b && c`, lequel de `&&` et `||` est évalué d'abord ? Ajoute des parenthèses si elles rendent l'intention plus claire.

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `Double.parseDouble` ; `parseInt(…, 2)` | 1, 2 | ☐ |
| `(int)`, `(char)` | 1, 3 | ☐ |
| `<<`, ` & ` (bits des options) | 2 | ☐ |
| `+=`, `-=` | 2 | ☐ |
| ternaires imbriqués | 3 | ☐ |
| `&&`, `||` | 3 | ☐ |
| `Integer.toBinaryString` | 2 | ☐ |

---

## Sortie attendue complète

```
points 79.5 / coefficients 6 = 13.25
piege : (int) points = 79 ; 79 / 6 = 13 ; (double) (79 / 6) = 13.0 ; (double) 79 / 6 = 13.166666666666666
absences 4 -> penalite 0.25 ; options 101 : delegue latin
moyenne finale : 13.7 (Assez bien)
ecart a la classe : +2.2
lettre : B, admis : true, felicitations : false
```
