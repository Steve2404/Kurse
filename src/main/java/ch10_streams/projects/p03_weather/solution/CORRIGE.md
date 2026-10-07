# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`WeatherStation.java`](WeatherStation.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Lire et valider les journées

**Le code :** le record `Day` et le constructeur de `WeatherStation`.

**`mapToInt(Integer::parseInt).toArray()`** passe d'un `Stream<String>` à un `IntStream`, puis à un `int[]`, sans jamais créer d'`Integer`.

**Question — `int[]` dans un record :** l'`equals` généré compare les composants avec `equals`, et pour un tableau, c'est l'`equals` d'`Object` : les **références**. Vérifié : deux records contenant chacun `{1}` ne sont pas égaux (`false`). Ici, ce n'est **pas un problème** : on ne compare jamais deux journées, et on ne les met dans aucun `Set`.

**Ne pas exposer le tableau :** l'accesseur généré `values()` rendrait le tableau lui-même, que l'appelant pourrait modifier. `temps()` rend un `IntStream.of(values)` : le reste du programme **lit** les valeurs, sans pouvoir les changer.

---

## Étape 2 — Le code de contrôle de la station

**Le code :** `checksum`.

**Question — pourquoi `IntStream` et pas `Stream<Character>` ?** `chars()` rend directement les **codes** (`int`). Un `Stream<Character>` emballerait chaque caractère dans un objet, et il faudrait le déballer pour additionner. Avec un `IntStream`, on filtre et on fait `sum()` sans aucun boxing.

---

## Étape 3 — Le bilan de chaque journée

**Le code :** `peak` et `daily`.

**`summaryStatistics()`** calcule min, max, somme, nombre et moyenne en **un seul** parcours. Un flux ne se consomme qu'une fois : appeler `min()`, puis `max()`, demanderait de recréer le flux à chaque fois.

**La fenêtre glissante et les égalités :** `max(Comparator)` garde le **premier** des éléments maximaux, comme le précise la Javadoc de `BinaryOperator.maxBy`. Vérifié : sur `a1, b1, c0`, comparés par le 2e caractère, `max` rend `a1`. Les heures de départ étant parcourues dans l'ordre, 10h-12h (somme 92) gagne contre 11h-13h (92 aussi).

**`getAsDouble()` est sûr ici** : le flux `range(start, start + 3)` a **toujours** 3 éléments, donc `average()` n'est jamais vide.

**`boxed()` indispensable :** `IntStream` n'a **pas** de méthode `toList()` (vérifié : `cannot find symbol` `method toList()` `location: interface IntStream`). Une `List` ne contient que des objets : il faut d'abord emballer chaque `int` en `Integer`.

---

## Étape 4 — Médiane et moyennes de toute la période

**Le code :** `all`, `median`, et la ligne `MOYENNE` de `report`.

**`flatMapToInt(Day::temps)`** aplatit les journées en un seul `IntStream`, sans boxing.

**La médiane sans tableau :** 120 valeurs, un nombre pair. On trie, on saute les 59 premières (`n / 2 - 1`), on garde 2, puis on fait la moyenne.

**Le piège Fahrenheit :** sur un `IntStream`, `c * 9 / 5` est une division **entière**. Vérifié sur 21 et 22 °C : la moyenne en `int` donne **70.0**, alors qu'en `double` (`asDoubleStream()`) elle donne **70.7**. `asDoubleStream()` convertit tout le flux en `double` avant le calcul.

**Question — un `IntStream` ne se consomme qu'une fois :** le réutiliser lève une exception (vérifiée) :

```
java.lang.IllegalStateException: stream has already been operated upon or closed
```

`all()` est une **méthode** qui crée un flux **neuf** à chaque appel, à partir de la liste des journées. La liste, elle, se relit autant qu'on veut.

---

## Étape 5 — L'histogramme

**Le code :** `histogram`.

**`IntStream.rangeClosed(min / 5, max / 5)`** produit les numéros de classe, et `mapToObj` transforme chaque numéro en ligne de texte.

**Question — pourquoi 120 et pas 144 ?** 6 journées × 24 heures = 144 relevés. Mais la journée du 19 juillet est **rejetée** (23 relevés), et elle n'entre dans **aucun** calcul : il reste 5 × 24 = **120**.

---

## Étape 6 — Les trois détections : l'algorithme

**Le code :** `heatWave`, `longestRise`, et le bloc ORAGE de `report`.

**MONTÉE — le 15 contre le 16 :** les deux journées ont une hausse de **9** heures, de 3h à 12h (vérifié). À égalité, la plus **tôt** gagne : le 15. La comparaison `h - 1 - start > best.length()` est **stricte**, donc une série égale ne remplace pas la meilleure.

**ORAGE en une chaîne :** `flatMap` sur les journées, et pour chacune un `IntStream.range(1, 24)` des heures. `mapToObj` passe de l'`IntStream` au `Stream<String>` attendu par `flatMap`. Le 17 juillet, de 11h à 12h, la température passe de 31 à 24 : une chute de **7**.

**Question de conception — un état dans un stream ?** CANICULE et MONTÉE ont besoin d'un **état** qui avance d'élément en élément (« la série en cours, la meilleure »). Un stream traite chaque élément **indépendamment**. Le forcer demanderait des tableaux modifiés dans des lambdas, ce qui est illisible et faux en parallèle. Une boucle est le bon outil. Les streams excellent pour les transformations **sans état**, comme ORAGE.

---

## Étape 7 — L'énergie

**Le code :** les lignes CLIMATISATION et DEGRES-JOURS de `report`.

**`mapToLong`** passe d'un `IntStream` à un `LongStream`. **`mapToDouble`** passe d'un `Stream<Day>` à un `DoubleStream`.

**Question — pourquoi `int` serait dangereux ?** Un `int` plafonne à environ 2,1 milliards. Sur un an et 1000 stations, il y a 8,76 millions de relevés horaires. Rien qu'à 10 degrés au-dessus du seuil, × 350 Wh, on dépasse 30 milliards. Vérifié avec ces chiffres : un cumul en `int` donne **595228928**, un nombre faux sans aucune erreur. Un `long` va jusqu'à 9,2 × 10¹⁸.

---

## Étape 8 — `main`

**Le code :** `report` et `main`. Le rapport s'imprime dans l'ordre de la sortie attendue : la station, les rejets, les journées, puis les statistiques globales.
