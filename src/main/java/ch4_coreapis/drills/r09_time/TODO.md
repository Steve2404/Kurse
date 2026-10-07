# Drill de rappel 9 — `Duration`, `Instant`, les fuseaux et le changement d'heure

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 18 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall09`** dans le paquet `ch4_coreapis.drills.r09_time`.
- Fuseaux utilisés : `Europe/Paris` et `Asia/Tokyo`.
- En 2026, Paris passe à l'heure d'été le **29 mars** (02:00 → 03:00) et revient à l'heure d'hiver le **25 octobre** (03:00 → 02:00).

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 3 à 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r09_time` → **New** → **Java Class** → `Recall09`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall09` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall09`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Cinq `Duration` :
  - `ofDays(1)` ;
  - `ofHours(36)` ;
  - `ofMinutes(75)` ;
  - `ofSeconds(61)` ;
  - `ofMillis(1500)`.
  → `D01 : PT24H PT36H PT1H15M PT1M1S PT1.5S`
- ☐ **D02.** `d` est la durée de 09:00 à 17:45 (`Duration.between` sur deux `LocalTime`). Affiche :
  - `d` ;
  - `toMinutes()` ;
  - `toHours()` ;
  - la durée de 17:00 à 09:00.
  → `D02 : PT8H45M 525 8 PT-8H`
- ☐ **D03.** `start` vaut le 04/05/2026 à 10:30:45. Affiche :
  - `ChronoUnit.HOURS.between` de `start` à `start` plus 150 minutes ;
  - la même chose en `MINUTES` ;
  - `start` tronqué à la minute ;
  - `start` tronqué au jour.
  → `D03 : 2 150 2026-05-04T10:30 2026-05-04T00:00`
- ☐ **D04.** `meeting` est une réunion le 01/06/2026 à 09:00 à Paris. Affiche :
  - `meeting` ;
  - le même **instant** à Tokyo ;
  - la même **heure locale** à Tokyo.
  → `D04 : 2026-06-01T09:00+02:00[Europe/Paris] 2026-06-01T16:00+09:00[Asia/Tokyo] 2026-06-01T09:00+09:00[Asia/Tokyo]`
- ☐ **D05.** Quatre résultats :
  - `meeting.toInstant()` ;
  - `Instant.ofEpochSecond(86_400)` ;
  - l'instant plus une `Duration` d'1 h ;
  - le nombre de jours entre `Instant.EPOCH` et l'instant.
  → `D05 : 2026-06-01T07:00:00Z 1970-01-02T00:00:00Z 2026-06-01T08:00:00Z 20605`
- ☐ **D06.** Le 29/03/2026 à 01:30 à Paris : affiche `plusHours(1)` puis `plusHours(2)`.
  → `D06 : 2026-03-29T03:30+02:00[Europe/Paris] 2026-03-29T04:30+02:00[Europe/Paris]`
- ☐ **D07.** Le 24/10/2026 à 12:00 à Paris. Affiche :
  - `plusDays(1)` ;
  - `plusHours(24)` ;
  - la `Duration` réelle entre la date de départ et `plusDays(1)`.
  → `D07 : 2026-10-25T12:00+01:00[Europe/Paris] 2026-10-25T11:00+01:00[Europe/Paris] PT25H`
- ☐ **D08.** Deux heures locales à Paris :
  - le 29/03 à 02:15, qui **n'existe pas** : affiche son `toLocalTime()` ;
  - le 25/10 à 02:15, qui **existe deux fois** : affiche son décalage par défaut, puis avec `withLaterOffsetAtOverlap()`, puis avec `withEarlierOffsetAtOverlap()`.
  → `D08 : 03:15 +02:00 +01:00 +02:00`

## Expériences (hors sortie attendue)

1. `Duration.between(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2))` : que se passe-t-il ? Pourquoi ?
2. `LocalDate.of(2026, 1, 1).plus(Duration.ofDays(1))` : même question.
3. `ChronoUnit.HOURS.between` de 10:30 à 12:29 : 1 ou 2 ?
4. `Instant.now()` existe, mais pourquoi ne l'utilise-t-on pas dans un drill ?

## Sortie attendue complète

```
D01 : PT24H PT36H PT1H15M PT1M1S PT1.5S
D02 : PT8H45M 525 8 PT-8H
D03 : 2 150 2026-05-04T10:30 2026-05-04T00:00
D04 : 2026-06-01T09:00+02:00[Europe/Paris] 2026-06-01T16:00+09:00[Asia/Tokyo] 2026-06-01T09:00+09:00[Asia/Tokyo]
D05 : 2026-06-01T07:00:00Z 1970-01-02T00:00:00Z 2026-06-01T08:00:00Z 20605
D06 : 2026-03-29T03:30+02:00[Europe/Paris] 2026-03-29T04:30+02:00[Europe/Paris]
D07 : 2026-10-25T12:00+01:00[Europe/Paris] 2026-10-25T11:00+01:00[Europe/Paris] PT25H
D08 : 03:15 +02:00 +01:00 +02:00
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`Duration` :**
- en heures, minutes et secondes ; `toString` donne `PT…` ;
- au-delà de 24 h, on reste en heures (`PT36H`, pas de jours) ;
- `toHours()` et `toMinutes()` **tronquent** ;
- elle s'utilise sur des types qui ont une heure : `Duration.between` sur deux `LocalDate` lève une exception.

**`ChronoUnit.X.between(a, b)` :**
- le nombre d'unités **complètes**, sous forme de `long` ;
- `truncatedTo` met à zéro les champs plus fins.

**Les fuseaux :**
- `withZoneSameInstant` garde l'instant et change l'heure affichée ;
- `withZoneSameLocal` garde l'heure et change l'instant ;
- `Instant` est toujours en UTC (`Z`).

**Le changement d'heure :**
- au printemps, l'heure 02:xx n'existe pas : elle est décalée d'une heure ;
- en automne, l'heure 02:xx existe deux fois : par défaut, c'est le **premier** décalage ;
- `plusDays` garde l'heure locale ; `plusHours(24)` ajoute 24 h réelles.

</details>
