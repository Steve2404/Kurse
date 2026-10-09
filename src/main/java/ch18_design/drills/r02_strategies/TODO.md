# Drill de rappel 2 — Des stratégies injectées (ouvert/fermé, inversion des dépendances)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p02 et p04.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch18_design.drills.r02_strategies`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Tous les montants sont des `long` en centimes.
- `Checkout` ne nomme **aucune** promotion concrète et lit la date avec son horloge (`LocalDate.now(clock)`, jamais `LocalDate.now()`). Ni `switch`, ni `instanceof`.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 4 à 7) et projet 4 (étapes 2 et 3).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée les fichiers dans l'ordre des défis, une valeur bidon pour un défi pas fini, `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@FunctionalInterface public interface Promotion` avec `long discount(List<Long> prices, LocalDate today)`, et `public final class ThreeForTwo implements Promotion` : les prix triés du plus cher au moins cher, le **troisième** de chaque tranche de trois est offert (la remise est la somme des prix offerts).
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `public final class HappyTuesday implements Promotion` : le **mardi**, 10 % du total, arrondis au centime le plus proche ; 0 les autres jours.
  → `d02 : 3 executions, 3 reussies`
- ☐ **D03.** `public final class Threshold implements Promotion`, construite avec `(long threshold, long amount)` : `amount` de remise dès que le total **atteint** le seuil (compris).
  → `d03 : 3 executions, 3 reussies`
- ☐ **D04.** `public final class Checkout`, construite avec `(List<Promotion> promotions, Clock clock)`, et `long total(List<Long> prices)` : la somme des prix moins **toutes** les remises, chacune calculée sur les prix **d'origine**, à la date de l'horloge.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Le total ne descend **jamais** sous 0 ; sans promotion, c'est la somme.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** Une promotion écrite en **lambda** par le test marche dans ta caisse.
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 3 executions, 3 reussies
d03 : 3 executions, 3 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **La stratégie** : une interface d'**une** méthode (`@FunctionalInterface`), une classe par façon de faire, et le code appelant qui ne connaît que l'interface. Une stratégie d'une ligne peut être une lambda.
- **Trois pour deux** : `prices.stream().sorted(Comparator.reverseOrder()).toList()`, puis les indices `i % 3 == 2` (`IntStream.range(0, n).filter(…).mapToLong(sorted::get).sum()`).
- **L'arrondi** : `(total * 10 + 50) / 100`. **Le mardi** : `today.getDayOfWeek() == DayOfWeek.TUESDAY`.
- **L'injection** : la caisse **reçoit** `List<Promotion>` et `Clock` dans son constructeur ; `LocalDate today = LocalDate.now(clock);`.
- **La somme des remises** : `promotions.stream().mapToLong(p -> p.discount(prices, today)).sum()`, puis `Math.max(0, somme - remises)`.
- **Le test** fixe la date : `Clock.fixed(Instant.parse("2026-10-13T12:00:00Z"), ZoneOffset.UTC)` (un mardi).

</details>
