# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Mesurer

<details><summary>Indice 1</summary>

Divise le temps de 1 000 lignes par celui de 500, et celui de 500 par celui de 250. Les petites tailles sont bruitées (la JVM n'est pas encore chaude) : fie-toi surtout au dernier rapport.

</details>

<details><summary>Indice 2</summary>

Pour estimer 10 000 lignes : 10 000 = 1 000 × 10. Si le temps est en n³, il est multiplié par 10³ = 1 000. Compte les boucles imbriquées dans le calcul du jour le plus chargé, en te souvenant que `LinkedList.get(i)` est lui-même une boucle.

</details>

---

## Étape 2 — Le profileur

<details><summary>Indice 1</summary>

Si la commande dit que `jfr.exe` est introuvable, le chemin du JDK est faux : copie-le exactement depuis **Project Structure → SDKs**, et garde le `&` et les guillemets au début.

</details>

<details><summary>Indice 2</summary>

`--stack-depth 1` ne garde que la méthode **en cours** de chaque échantillon (le haut de la pile). Avec `--stack-depth 5`, chaque échantillon garde aussi ses appelants : en ne gardant que les lignes de `LegacyReport`, tu vois quelles lignes du legacy étaient « en dessous » au moment de l'échantillon.

</details>

---

## Étape 3 — Le maître étalon

<details><summary>Indice 1</summary>

`@RepeatedTest(150)` avec un paramètre `RepetitionInfo info` : `info.getCurrentRepetition()` sert à la fois de taille et de graine. Une taille de 0 (le journal vide) arrive aussi : tant mieux.

</details>

<details><summary>Indice 2</summary>

Pour le générateur à égalités : des lignes `"2026-10-0" + (1 + random.nextInt(3)) + ";" + "abc".charAt(random.nextInt(3)) + ";" + …`, des points de 0 à 2, et une chance sur dix de recopier une ligne déjà tirée (un doublon).

</details>

---

## Étape 4 — Lire chaque ligne une fois

<details><summary>Indice 1</summary>

`private static final Pattern LINE = Pattern.compile("…")`, puis `LINE.matcher(line).matches()`. Le motif est **exactement** celui du legacy : copie-le depuis `Data.java`.

</details>

<details><summary>Indice 2</summary>

Quatre `indexOf` : `a = line.indexOf(';')`, `b = line.indexOf(';', a + 1)`, `c`, `d`. Le titre (après `d`) ne sert pas au rapport : peu importe qu'il contienne d'autres `;`.

</details>

---

## Étape 5 — Un passage au lieu de dix

<details><summary>Indice 1</summary>

Une méthode `read(lines)` qui rend un petit record privé `Journal(List<TaskEvent> events, int invalid, int duplicates)`, puis une méthode par partie du rapport (`appendPeople`, `appendColumns`, `appendBusiestDay`) qui reçoit les événements et le `StringBuilder`.

</details>

<details><summary>Indice 2</summary>

Pour les personnes : `Map<String, int[]>` (points, tâches) avec `computeIfAbsent(nom, k -> new int[2])`, puis une liste d'un petit record `Person(name, points, tasks)` triée avec `Comparator.comparingInt(Person::points).reversed().thenComparing(Person::name)`. Pour le jour : en parcourant la `TreeMap` dans l'ordre, ne remplace le meilleur que si le nouveau est **strictement** plus grand.

</details>

---

## Étape 6 — Mesurer correctement

<details><summary>Indice 1</summary>

Deux boucles dans `measure` : la chauffe (sans chronomètre), puis les tours mesurés, chacun entre deux `System.nanoTime()`. Trie le tableau à la fin.

</details>

<details><summary>Indice 2</summary>

`Supplier<?>` accepte n'importe quel calcul qui rend quelque chose : `() -> FastReport.report(lines)`, ou `calls::incrementAndGet` dans le test.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Le mutant 1 donne le bon rapport, mais parcourt la liste des événements à chaque ligne : sur 200 000 lignes, des dizaines de milliards d'opérations. Seul un test qui **chronomètre** un gros journal peut le voir.

</details>

<details><summary>Indice 2 — ce que change chaque mutant</summary>

| Mutant | Ce qui change | Le test qui le tue |
|---|---|---|
| 1 | un parcours de toute la liste à chaque ligne : O(n²), même résultat | le test de vitesse |
| 2 | les doublons ne sont plus repérés | le maître étalon |
| 3 | les points triés par ordre croissant | le maître étalon |
| 4 | plus de tri par nom en cas d'égalité | les égalités de points (les deux maîtres étalons) |
| 5 | à égalité, le jour le plus récent gagne | l'égalité de jours (et les maîtres étalons) |
| 6 | les lignes fausses ne sont plus comptées | le maître étalon |
| 7 | les doublons ne sont plus comptés | le maître étalon |
| 8 | « lignes » compte les événements gardés, pas les lignes lues | le maître étalon |
| 9 | chaque colonne compte 1 | le maître étalon |
| 10 | des points à 5 chiffres acceptés | le maître étalon (`12345`) |
| 11 | la colonne commence par `;` | le maître étalon |
| 12 | plus de chauffe dans le banc | le compteur de tours |
| 13 | la médiane d'un nombre pair de valeurs prend la mauvaise | la médiane de 4 valeurs |
| 14 | 0 tour mesuré accepté | les refus du banc |

</details>
