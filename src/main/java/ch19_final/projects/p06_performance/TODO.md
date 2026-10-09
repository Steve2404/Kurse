# Projet 6 — Le rapport trop lent : mesurer, trouver, corriger

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- la règle d'or de la performance : **mesurer d'abord**, ne jamais deviner ;
- relier des mesures à la **complexité** (chapitre 17) : quand n double, que fait le temps ?
- un **profileur** : Java Flight Recorder (JFR), fourni avec le JDK, pour trouver **où** le temps passe ;
- optimiser **sans changer le résultat** : le maître étalon d'abord (chapitre 18) ;
- les gestes qui comptent : un passage au lieu de dix, `HashSet` au lieu de `List.contains`, un motif compilé une fois, `StringBuilder`, trier des valeurs déjà calculées ;
- mesurer **correctement** en Java : la chauffe du JIT, la médiane, le résultat qu'il faut utiliser ;
- le **test de vitesse** et le mutant de performance.

**Ce qui est FOURNI :** `Data.java` contient `LegacyReport`, qui fabrique le rapport mensuel de l'atelier à partir du journal des tâches (une ligne par événement : `date;personne;colonne;points;titre`). Il est **juste**, et il devient inutilisable dès que le journal grossit. `Data.generate(n, graine)` fabrique des journaux réalistes, toujours les mêmes pour la même graine (environ 2 % de lignes fausses et 3 % de doublons). Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch19_final.projects.p06_performance` : `TaskEvent`, `FastReport`, `Bench`, `ReportDemo`, et tes tests (par exemple `FastReportTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Aucune méthode de plus de **18 lignes**. Ni `LinkedList`, ni `String.matches("…")`, ni `split(` dans ton code. Dans `FastReport.java` : aucun `.contains(`, aucun `= out +`, rien de `Legacy`. Dans tes tests : ni `System.out`, ni `Thread.sleep`.

---

## Tableau de bord

### ☐ Étape 1 — Mesurer avant tout

**📖 La leçon : « l'optimisation prématurée est la racine de tous les maux ».** Cette phrase de Donald Knuth ne dit pas « n'optimise jamais » : elle dit « n'optimise pas **à l'aveugle** ». Les programmeurs devinent très mal où leur programme perd son temps. Avant de toucher au code : le **mesurer**, sur des tailles qui grandissent, et regarder comment le temps grandit (chapitre 17) :

| Quand n double, le temps… | Complexité probable |
|---|---|
| reste à peu près le même | O(1), O(log n) |
| double | O(n) |
| est multiplié par 4 | O(n²) |
| est multiplié par 8 | O(n³) |

**👉 À toi :** lance `Data` (son `main` mesure le legacy sur 250, 500 et 1 000 lignes, et affiche le rapport de 1 000 lignes). Lis le rapport : comprends chaque ligne. Puis lis `LegacyReport` en entier.

**❓ Questions :**
- D'après tes mesures, quelle est la complexité du legacy ? Combien de temps prendrait un journal de 10 000 lignes ? D'un mois réel de 200 000 lignes ?
- Sans profileur, en lisant seulement le code : où penses-tu que le temps passe ? Note ta réponse : tu la compareras au profileur.

### ☐ Étape 2 — Trouver : le profileur

**📖 La leçon : un profileur échantillonne.** Java Flight Recorder (JFR) regarde, une centaine de fois par seconde, **quelle méthode** chaque fil est en train d'exécuter, et note ces « échantillons ». Une méthode qui apparaît dans 80 % des échantillons consomme environ 80 % du temps. JFR est fourni avec le JDK, et il coûte si peu qu'on peut le laisser tourner en production.

**👉 À toi :**
1. Dans IntelliJ, ajoute l'option de JVM `-XX:StartFlightRecording=filename=build/ch19/rapport.jfr` au lancement de `Data` (flèche verte → **Modify Run Configuration…** → **Modify options** → **Add VM options**), et lance. Le fichier `build/ch19/rapport.jfr` apparaît à la fin.
2. Trouve le dossier de ton JDK : **File → Project Structure → SDKs**, le chemin à droite (par exemple `C:\Program Files\Eclipse Adoptium\jdk-17…`).
3. Dans le terminal d'IntelliJ (PowerShell), les 5 méthodes les plus souvent « en train de tourner » (remplace le chemin du JDK par le tien) :

```
& "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin\jfr.exe" print --events jdk.ExecutionSample --stack-depth 1 build\ch19\rapport.jfr | Select-String 'line:' | ForEach-Object { $_.Line.Trim() } | Group-Object | Sort-Object Count -Descending | Select-Object -First 5 Count, Name
```

4. Recommence avec `--stack-depth 5`, en ne gardant que les lignes de `LegacyReport` (remplace `'line:'` par `'LegacyReport'`) : **qui** appelle la méthode la plus chaude ?

**❓ Questions :**
- Quelle méthode arrive en tête ? Est-ce une méthode du legacy ? Pourquoi est-elle si lente ici ?
- Qui l'appelle, et à quelles lignes de `Data.java` ? Ta réponse de l'étape 1 était-elle juste ?

### ☐ Étape 3 — Le filet d'abord : le maître étalon

**📖 La leçon : optimiser, c'est refactorer.** Le nouveau code doit donner **exactement** le même texte que l'ancien, à l'espace près, y compris ses **bizarreries** : le legacy accepte `2026-13-45` comme date (son motif ne vérifie que la forme), et un titre peut contenir des `;`. On les garde. Les corriger serait un autre travail, à faire **après**, avec ses propres tests (chapitre 18). Le filet : comparer les deux versions sur des centaines de journaux tirés au hasard, **avant** d'écrire la version rapide.

**👉 À toi :** crée `FastReport` avec `public static String report(List<String> lines)` qui, pour l'instant, **appelle le legacy** (une ligne). Puis écris le filet (`FastReportTest`), qui doit passer **avant** toute optimisation :
- `@RepeatedTest(150)` : `Data.generate(numéro % 120, numéro)` comparé au legacy (`assertEquals(legacy, rapide)`) ;
- `@RepeatedTest(150)` : ton propre générateur (`SplittableRandom`), avec **peu** de personnes (`a`, `b`, `c`), peu de jours, de petits points et des doublons : beaucoup d'**égalités**, le terrain des bugs de tri ;
- des cas choisis : le journal vide (le texte exact), un rapport complet écrit en text block, l'égalité de jours (le plus ancien gagne), 10 lignes fausses (`@ValueSource`), les bizarreries, une ligne fausse en double (elle compte comme fausse, pas comme doublon).

Les tests de « petits » journaux restent rapides avec le legacy : sa lenteur n'apparaît qu'au-delà de quelques centaines de lignes.

**❓ Question :** pourquoi un second générateur « plein d'égalités » ? Que pourrait laisser passer `Data.generate` seul ?

### ☐ Étape 4 — Lire chaque ligne une fois

**📖 La leçon : le coût caché de `String.matches` et `split`.** `line.matches("…")` **recompile** l'expression régulière à **chaque** appel. `split(";")` crée un tableau et des chaînes à chaque appel ; le legacy redécoupe chaque ligne des milliers de fois. On lit chaque ligne **une** fois, dans un objet, avec un motif compilé **une** fois (`static final Pattern`).

**👉 À toi :** `public record TaskEvent(String day, String who, String column, int points)` et `public static Optional<TaskEvent> parse(String line)` :
- le motif du legacy, compilé une fois : `\d{4}-\d{2}-\d{2};[a-z]+;[^;]+;\d{1,4};.+` (dans une chaîne Java, chaque `\` s'écrit `\\`) ;
- une ligne qui ne correspond pas : `Optional.empty()` ;
- sinon, les champs avec `indexOf(';', depuis)` et `substring` (pas de `split`), et les points avec `Integer.parseInt(line, début, fin, 10)`, qui lit un morceau de la chaîne sans la copier.

**🧪 Les tests :** les bizarreries (`2026-13-45;ada;Fini;0012;x` donne la date `2026-13-45` et 12 points ; un titre avec deux `;`).

### ☐ Étape 5 — Un passage au lieu de dix

**📖 La leçon : chaque structure a son coût (chapitre 17).**

| Geste du legacy | Coût | Remplacement | Coût |
|---|---|---|---|
| `LinkedList.get(i)` dans une boucle | O(n) par appel : O(n²) la boucle | une boucle `for (x : liste)` sur une `ArrayList` | O(n) |
| `liste.contains(x)` pour les doublons | O(n) par ligne | `HashSet.add(x)` (rend `false` si déjà vu) | O(1) |
| recompter toutes les lignes pour **chaque** total | O(n) par total | une `Map` mise à jour en passant | O(1) par ligne |
| recalculer les points **dans** le comparateur | O(n) par comparaison | trier des totaux **déjà** calculés | O(p log p) |
| `out = out + …` | recopie tout le texte | `StringBuilder` (projet 1) | O(longueur) |

**👉 À toi :** remplace l'appel au legacy par la vraie version :
- un passage sur les lignes : `TaskEvent.parse`, les fausses comptées, les doublons repérés par un `HashSet<String>` des lignes déjà vues ;
- les personnes : points et nombre de tâches dans une `Map`, puis un tri par points **décroissants**, puis par nom (`Comparator.comparingInt(…).reversed().thenComparing(…)`) ;
- les colonnes : une `TreeMap` (triée) de compteurs (`merge(colonne, 1, Integer::sum)`) ;
- le jour le plus chargé : une `TreeMap` jour → nombre ; parcourue dans l'ordre, elle donne le plus ancien en cas d'égalité ;
- tout le texte dans un `StringBuilder`.

Ton filet de l'étape 3 doit rester vert à chaque petit pas.

**❓ Question :** le legacy est en O(n³). Quelle est la complexité de ta version ? Où est l'étape la plus coûteuse qui reste ?

### ☐ Étape 6 — Mesurer correctement

**📖 La leçon : les trois pièges de la micro-mesure en Java.**
1. **La chauffe.** La JVM commence par **interpréter** le code ; après quelques milliers d'appels, le compilateur **JIT** le compile en code machine optimisé. Les premiers tours sont donc beaucoup plus lents : on les fait, et on les **jette**.
2. **Le bruit.** Un tour peut tomber sur le ramasse-miettes ou un autre programme : on répète, et l'on prend la **médiane** (pas la moyenne, projet 5).
3. **Le code mort.** Si personne n'utilise le résultat, le JIT a le droit de **supprimer** le calcul : on mesurerait du vide. On range le résultat quelque part que le JIT ne peut pas ignorer (un champ `volatile`, le « trou noir »).

Pour des mesures sérieuses, l'outil de référence est **JMH** (Java Microbenchmark Harness). Ton banc suffit pour comparer deux versions.

**👉 À toi :** `public final class Bench` (constructeur privé), avec le record imbriqué `public record Result(long[] nanos)` (les temps triés, en nanosecondes) et ses méthodes `median()` (l'élément du milieu, `nanos[length / 2]`), `min()`, `max()` et `toString()` (`mediane 2 ms (min 1, max 9)`), et `public static Result measure(Supplier<?> task, int warmups, int runs)` : `warmups` tours jetés, puis `runs` tours mesurés avec `System.nanoTime()` ; chaque résultat ajouté au trou noir (`sink += task.get().hashCode()`, avec `private static volatile int sink`). `warmups < 0` ou `runs < 1` → `IllegalArgumentException`.

**🧪 Les tests :** un compteur (`calls::incrementAndGet`) prouve que 7 + 5 tours ont eu lieu ; les temps sont triés ; la médiane de 3 et de 4 valeurs ; le `toString` ; les refus.

**🧪 Expérience :** dans une méthode `main` jetable, mesure `FastReport.report` sur un journal de 10 000 lignes avec **0** tour de chauffe et **1** tour, puis avec **50** tours de chauffe et 21 tours. Compare.

### ☐ Étape 7 — Le test de vitesse, la démonstration, les mutants

**📖 La leçon : un test de vitesse.** Le maître étalon prouve que le résultat est juste ; il ne dit rien du **temps**. Quelqu'un, dans six mois, remplacera ton `HashSet` par une liste « plus simple », et tous les tests resteront verts. Un test de vitesse attrape ce recul : 200 000 lignes en moins de **2 secondes** (`assertTimeoutPreemptively`). La limite est **large** (ta version met environ 0,1 s) : un test de vitesse ne doit pas échouer parce que la machine est un peu chargée, mais parce que l'algorithme a changé d'ordre de grandeur.

**👉 À toi :**
- le test : `Data.generate(200_000, 7)`, en moins de 2 s, et le rapport commence par `lignes : 200000 (invalides : ` ;
- `public final class ReportDemo` avec `main` : `memes rapports : true` (les deux versions sur `Data.generate(1000, 42)`), le legacy sur ces 1 000 lignes (1 tour de chauffe, 3 tours), la version rapide sur les mêmes (20 et 21 tours), puis la version rapide seule sur 10 000, 100 000 et 1 000 000 de lignes (3 et 5 tours), chaque fois avec le `toString` du `Result`.

**🧪 Expériences :**
- lance `ReportDemo`. Combien de fois la version rapide est-elle plus rapide sur 1 000 lignes ? Le temps suit-il n de 10 000 à 1 000 000 ?
- refais l'étape 2 (JFR) sur `ReportDemo` : quelles méthodes sont en tête maintenant ?

**👉 Puis :** lance `Check`. Les 14 mutants touchent la lecture (les doublons, les fausses lignes, le motif, les champs), les tris, le jour le plus chargé, les compteurs, le banc de mesure… et le **mutant 1**, qui donne le **bon** résultat, mais en O(n²) : seul ton test de vitesse peut le tuer.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record TaskEvent(`, `static final Pattern`, `Pattern.compile(`, `final class FastReport`, `HashSet`, `StringBuilder`, `final class Bench`, `record Result(`, `System.nanoTime()`, `volatile`, `Arrays.sort(`, `final class ReportDemo`, `Data.LegacyReport`, `Data.generate(` ; ni `LinkedList`, ni `.matches("`, ni `split(`.
- **La conception :** aucune méthode de plus de **18 lignes** ; `FastReport.java` ne contient ni `.contains(`, ni `= out +`, ni `Legacy`.
- **Tes tests :** au moins **300** tests (chaque répétition compte), `Data.LegacyReport.report(`, `@RepeatedTest`, `SplittableRandom`, `assertTimeoutPreemptively(`, `Data.generate(`, `@ParameterizedTest` ; ni `System.out`, ni `Thread.sleep`.
- **Les 14 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p06_performance ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 319 tests, 319 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 14/14 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
