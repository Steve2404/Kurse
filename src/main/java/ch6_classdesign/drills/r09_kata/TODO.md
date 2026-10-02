# Drill de rappel 9 — Kata mixte chronométré (tout le chapitre 6)

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall09.java`, paquet `ch6_classdesign.drills.r09_kata`, avec un `LOG` (`StringBuilder`). Les classes :

| Classe | Contenu |
|---|---|
| `Vehicle` (abstraite) | `static int built` ; `String kind = "vehicule"` ; `protected final String name` ; `protected Vehicle(String name)` (compte, puis note ` Vehicle(<nom>)`) ; `abstract int wheels()` ; `String describe()` rend `nom a N roues` ; `static String category()` rend `transport` ; `toString` = `NomDeClasse[nom]` ; `equals` (même nom et même nombre de roues) et `hashCode` |
| `Bike extends Vehicle` | `String kind = "velo"` ; `Bike(String name)` appelle `super(name)`, puis note ` Bike(String)` ; `Bike()` délègue avec `this("anonyme")`, puis note ` Bike()` ; `wheels()` rend 2 ; `describe()` rend `"[" + super.describe() + "]"` ; `static String category()` rend `deux-roues` |
| `Wheel` (`final`, immuable) | `private final int inches` ; constructeur `private` ; `static of(int)` ; `bigger(int)` rend une nouvelle roue ; `equals`, `hashCode` ; `toString` = `26"` |

## Défis

- ☐ **D01.** `Vehicle v = new Bike("BMX");`. Affiche le journal (sans les espaces de bord).
  → `D01 : Vehicle(BMX) Bike(String)`
- ☐ **D02.** `v.describe()`, `v.wheels()`, `v.kind`, `Vehicle.category()` et `Bike.category()`.
  → `D02 : [BMX a 2 roues] 2 vehicule transport deux-roues`
- ☐ **D03.** `Bike b = new Bike();`. Affiche `b.describe()`, puis `Vehicle.built`.
  → `D03 : [anonyme a 2 roues] 2`
- ☐ **D04.** `w = Wheel.of(26)`. Affiche w, `w.bigger(2)`, w, puis `w.equals(Wheel.of(26))`.
  → `D04 : 26" 28" 26" true`
- ☐ **D05.** v, `v instanceof Bike`, puis `v.equals(new Bike("BMX"))`.
  → `D05 : Bike[BMX] true true`

## Sortie attendue complète

```
D01 : Vehicle(BMX) Bike(String)
D02 : [BMX a 2 roues] 2 vehicule transport deux-roues
D03 : [anonyme a 2 roues] 2
D04 : 26" 28" 26" true
D05 : Bike[BMX] true true
```

## Après le kata

Pour chaque ✗, refais le drill thématique :

| Défi | Drill |
|---|---|
| D01, D03 | r02, r03 |
| D02 | r04, r05 |
| D04 | r07 |
| D05 | r08 |
