# Drill de rappel 9 — Kata mixte chronométré (tout le chapitre 8)

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, **sans carte**. C'est le test final de chaque cycle.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall09`** dans le paquet `ch8_lambdas.drills.r09_kata`, avec deux méthodes `static` :
  - `int countIf(int[] values, IntPredicate keep)` ;
  - `int applyTimes(IntUnaryOperator f, int times, int start)`.

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

- ☐ **D01.** Sur `{3, 8, 12, 5, 20}`, avec `int limit = 6` :
  - `countIf(values, v -> v > limit)` ;
  - `countIf` avec la **négation** de « pair ». Il faut le cast `((IntPredicate) v -> v % 2 == 0).negate()`.
  → `D01 : 3 2`
- ☐ **D02.** `applyTimes(x -> x * 2, 10, 1)`, puis `applyTimes(IntUnaryOperator.identity(), 5, 7)`.
  → `D02 : 1024 7`
- ☐ **D03.** Le **currying** : `Function<Integer, Function<Integer, Integer>> adder = a -> b -> a + b`. Affiche `adder.apply(10).apply(5)`, puis `adder.apply(1).apply(2)`.
  → `D03 : 15 3`
- ☐ **D04.** `Predicate.not(String::isBlank)` sur `"  "`. Puis `normalize = clean.andThen(String::toLowerCase)`, avec `clean = String::strip`, sur `"  JaVa "` (entre crochets).
  → `D04 : false [java]`
- ☐ **D05.** `BiFunction<String, Integer, String> cut = String::substring` et `Supplier<String> lazy = () -> cut.apply("bonjour", 3)`. Affiche `lazy.get()`, puis `cut.andThen(String::length).apply("abcdef", 2)`.
  → `D05 : jour 4`

## Sortie attendue complète

```
D01 : 3 2
D02 : 1024 7
D03 : 15 3
D04 : false [java]
D05 : jour 4
```

## Après le kata

Pour chaque ✗, refais le drill thématique :

| Défi | Drill |
|---|---|
| D01 | r05, r04 |
| D02 | r05 |
| D03 | r03, r01 |
| D04 | r04, r06 |
| D05 | r06, r08 |
