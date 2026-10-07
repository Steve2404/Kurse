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

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r09_kata` → **New** → **Java Class** → `Recall09`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall09`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall09`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
