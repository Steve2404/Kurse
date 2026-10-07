# Drill de rappel 8 — `LocalDate`, `LocalTime`, `LocalDateTime` et `Period`

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall08`** dans le paquet `ch4_coreapis.drills.r08_dates`.
- Dates fixes uniquement : **pas de `now()`**, sinon la sortie change chaque jour.

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r08_dates` → **New** → **Java Class** → `Recall08`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall08`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall08`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

`date` vaut le 20 janvier 2026, construit avec `Month.JANUARY`. `time` vaut 06:15. `both` combine les deux.

- ☐ **D01.** Affiche :
  - `date`, `time` et `both` ;
  - `LocalTime.of(6, 15, 30)` ;
  - `LocalDateTime.of(2026, 1, 20, 6, 15, 0)`.
  
  Que deviennent les secondes nulles à l'affichage ?
  → `D01 : 2026-01-20 06:15 2026-01-20T06:15 06:15:30 2026-01-20T06:15`
- ☐ **D02.** `d = date`, puis `d.plusDays(10)` **sans** affectation. Ensuite, `d = d.plusDays(2).plusWeeks(1).minusMonths(1).plusYears(1)`. Affiche `date` et `d`.
  → `D02 : 2026-01-20 2026-12-29`
- ☐ **D03.** Trois débordements :
  - le 29/02/2024 plus un an ;
  - le 31/03/2026 moins un mois ;
  - le 31/12/2026 plus un jour.
  → `D03 : 2025-02-28 2026-02-28 2027-01-01`
- ☐ **D04.** Sur `date`, six informations :
  - le jour de la semaine ;
  - le mois ;
  - le numéro du mois ;
  - le jour de l'année ;
  - l'année bissextile ou non ;
  - `getDayOfWeek() == DayOfWeek.TUESDAY`.
  → `D04 : TUESDAY JANUARY 1 20 false true`
- ☐ **D05.** Quatre calculs :
  - `time` plus 50 minutes ;
  - `time` moins 7 heures ;
  - `time.withHour(23)` plus 2 heures ;
  - la date de `both` plus 20 heures.
  → `D05 : 07:05 23:15 01:15 2026-01-21`
- ☐ **D06.** Cinq `Period` :
  - `Period.of(1, 2, 3)` ;
  - `ofWeeks(2)` ;
  - le piège `Period.ofDays(1).ofMonths(3)` ;
  - `ofMonths(18).normalized()` ;
  - `Period.ZERO`.
  → `D06 : P1Y2M3D P14D P3M P1Y6M P0D`
- ☐ **D07.** Trois calculs :
  - `date.plus(p)` ;
  - l'âge d'une personne née le 15/05/2000 au jour de `date` (`Period.between`) ;
  - la période de `date` au 25/12/2025 (en arrière).
  → `D07 : 2027-03-23 P25Y8M5D P-26D`
- ☐ **D08.** Cinq résultats :
  - `date.isBefore(d)` ;
  - `date.isAfter(d)` ;
  - `equals` avec un nouveau `LocalDate.of(2026, 1, 20)` ;
  - `withDayOfMonth(1)` ;
  - `withMonth(2)`.
  → `D08 : true false true 2026-01-01 2026-02-20`

## Expériences (hors sortie attendue)

1. `LocalDate.of(2026, 2, 30)` et `LocalTime.of(25, 0)` : quelle exception, et quand ?
2. `new LocalDate(2026, 1, 1)` : que dit `javac` ?
3. `time.plusDays(1)` et `date.plusHours(1)` : lesquels compilent ?
4. `LocalDate.of(2026, 1, 31).plus(Period.ofMonths(1)).plus(Period.ofMonths(1))` contre `plus(Period.ofMonths(2))`.

## Sortie attendue complète

```
D01 : 2026-01-20 06:15 2026-01-20T06:15 06:15:30 2026-01-20T06:15
D02 : 2026-01-20 2026-12-29
D03 : 2025-02-28 2026-02-28 2027-01-01
D04 : TUESDAY JANUARY 1 20 false true
D05 : 07:05 23:15 01:15 2026-01-21
D06 : P1Y2M3D P14D P3M P1Y6M P0D
D07 : 2027-03-23 P25Y8M5D P-26D
D08 : true false true 2026-01-01 2026-02-20
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**La création :**
- tout est **immuable** et se crée par des fabriques `of(…)` : pas de constructeur public ;
- un résultat ignoré est perdu ;
- une valeur invalide lève une `DateTimeException` à l'exécution.

**Le débordement de fin de mois :**
- `plusMonths` et `plusYears` ramènent au dernier jour valide (le 31/03 moins un mois donne le 28/02).

**Les méthodes autorisées :**
- `LocalDate` n'a pas de méthodes d'heure, `LocalTime` pas de méthodes de jour : sinon, ça ne compile pas ;
- `LocalTime` boucle sur 24 h.

**`Period` (années, mois, jours) :**
- `toString` donne `P1Y2M3D`, `P14D` pour 2 semaines et `P0D` pour zéro ;
- les fabriques sont **statiques** et ne se chaînent pas : seul le dernier `ofX` compte ;
- `normalized()` convertit 12 mois en 1 an (pas les jours) ;
- `Period.between(a, b)` peut être négative.

**La comparaison :**
- `isBefore`, `isAfter` et `equals` ;
- `withX(…)` remplace un champ.

</details>
