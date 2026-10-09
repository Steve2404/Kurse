# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`FastReportTest.java`](FastReportTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18** (Temurin) et **JUnit 5.11.4**, le 9 octobre 2026, sur un portable ordinaire. Tes temps seront différents ; les **rapports** entre les temps, eux, se ressembleront.

---

## Étape 1 — Mesurer avant tout

**La sortie de `Data` (vérifiée) :** `250 lignes : 142 ms`, `500 lignes : 238 ms`, `1000 lignes : 1235 ms`, puis le rapport :

```
lignes : 1000 (invalides : 29, doublons : 25)
personnes :
  ada : 565 points, 132 taches
  farid : 555 points, 136 taches
  …
colonnes :
  A faire : 232
  Bloque : 231
  En cours : 253
  Fini : 230
jour le plus charge : 2026-10-12 (42 evenements)
```

**Question — la complexité :** les petites tailles sont faussées par la chauffe de la JVM (étape 6). Avec des tailles plus grandes (mesurées pendant la préparation du projet) : 1 000 lignes en 1,3 s, 2 000 en 12,7 s, 4 000 en **180 s**. Quand n double, le temps est multiplié par 10 puis par 14 : au moins du **n³**, et un peu plus (les listes plus longues sortent des mémoires caches du processeur). En n³, 10 000 lignes prendraient environ 1 000 fois 1,3 s, soit plus de 20 minutes ; un mois de 200 000 lignes, des **années**. Ce n'est pas une question de machine plus rapide : aucune machine ne sauvera un algorithme cubique.

---

## Étape 2 — Trouver : le profileur

**Le haut de la pile (vérifié, sur l'enregistrement de `Data`) :**

```
Count Name
----- ----
   25 java.util.LinkedList.node(int) line: 575
   11 java.util.LinkedList.node(int) line: 580
    6 java.lang.String.split(String, int) line: 3128
```

**Les lignes du legacy en dessous (vérifié, `--stack-depth 5`) :** `count(List, int, String) line: 94` (44 échantillons), `report(List) line: 71` (28) et `line: 72` (16).

**Question — la méthode en tête :** `LinkedList.node(int)`, une méthode du **JDK**, pas du legacy. C'est elle qui trouve le i-ième maillon d'une liste chaînée : elle part d'un bout et avance maillon par maillon. `get(i)` sur une `LinkedList` coûte donc O(n) : la boucle `for (int i = 0; i < seen.size(); i++) seen.get(i)` de `count` et de `points` est en O(n²) à elle seule.

**Question — qui l'appelle :** `count` (ligne 94 : `seen.get(i).split(";")[field]`), appelé **quatre fois par ligne** depuis la boucle du jour le plus chargé (lignes 71 et 72 : la condition du `if`). Une boucle sur les lignes (n), qui appelle `count` (n), qui appelle `get(i)` (n) : **n³**. Beaucoup de lecteurs devinent `String.matches` ou la concaténation des `String`, qui sont réellement lentes, mais qui pèsent peu ici face au n³ : c'est toute la leçon. Le profileur montre aussi `split` : il est appelé dans la même boucle.

---

## Étape 3 — Le filet d'abord

Le code : la première moitié de [`FastReportTest.java`](FastReportTest.java). Pendant cette étape, `report` se contente d'appeler `Data.LegacyReport.report(lines)` : le filet passe forcément, et il est prêt **avant** la première optimisation.

**Question — le générateur à égalités :** les bugs de tri ne se montrent qu'en cas d'**égalité** : deux personnes avec exactement le même total, deux jours avec le même nombre d'événements. Avec de gros journaux de 8 personnes, ces égalités deviennent rares, et un tri qui oublie le second critère (le nom), ou un jour le plus chargé qui garde le **dernier** à égalité, passerait presque toujours. Vérifié sur ce projet : `Data.generate` seul tue quand même les mutants 4 et 5, parce que beaucoup de ses journaux sont **petits** (de 0 à 119 lignes) et que les égalités y arrivent par hasard. C'est de la chance : il suffirait que quelqu'un agrandisse ces journaux pour que le filet se troue sans bruit. Le générateur à égalités (3 personnes, 3 jours, des points de 0 à 2) rend cette couverture **voulue** : il fabrique des égalités dans presque chaque journal, quelle que soit la taille. Et l'expérience montre aussi ce que le maître étalon ne voit **jamais** : avec lui seul, les mutants 1 (la lenteur) et 12 à 14 (le banc de mesure) survivent.

---

## Étape 4 — Lire chaque ligne une fois

Le code : [`TaskEvent.java`](TaskEvent.java).

```java
private static final Pattern LINE = Pattern.compile("\\d{4}-\\d{2}-\\d{2};[a-z]+;[^;]+;\\d{1,4};.+");

public static Optional<TaskEvent> parse(String line) {
    if (!LINE.matcher(line).matches()) {
        return Optional.empty();
    }
    int a = line.indexOf(';');
    int b = line.indexOf(';', a + 1);
    int c = line.indexOf(';', b + 1);
    int d = line.indexOf(';', c + 1);
    return Optional.of(new TaskEvent(line.substring(0, a), line.substring(a + 1, b), line.substring(b + 1, c),
            Integer.parseInt(line, c + 1, d, 10)));
}
```

---

## Étape 5 — Un passage au lieu de dix

Le code : [`FastReport.java`](FastReport.java).

```java
private static Journal read(List<String> lines) {
    Set<String> seen = new HashSet<>();
    List<TaskEvent> events = new ArrayList<>();
    int invalid = 0;
    int duplicates = 0;
    for (String line : lines) {
        Optional<TaskEvent> event = TaskEvent.parse(line);
        if (event.isEmpty()) {
            invalid++;
        } else if (!seen.add(line)) {
            duplicates++;
        } else {
            events.add(event.get());
        }
    }
    return new Journal(events, invalid, duplicates);
}
```

**Question — la complexité :** **O(n)** pour la lecture (une fois chaque ligne, un `HashSet.add` en O(1) en moyenne) et pour les totaux (une `Map` mise à jour par événement), plus O(p log p) pour trier les p personnes, et O(j log j) pour les j jours et les colonnes rangés dans des `TreeMap`. Avec 8 personnes et 31 jours, ces tris ne coûtent rien. Ce qui reste le plus cher, c'est la lecture : le motif sur chaque ligne et le hachage de chaque ligne dans le `HashSet` (l'expérience JFR de l'étape 7 le confirme).

---

## Étape 6 — Mesurer correctement

Le code : [`Bench.java`](Bench.java).

**Expérience — la chauffe (vérifiée, deux lancements) :** sur 10 000 lignes, le premier tour à froid prend **46 ms** (puis 52 ms au second lancement) ; après 50 tours de chauffe, la médiane tombe à **6 ms** (puis 5 ms), avec un minimum de 3 à 4 ms. Huit à dix fois plus rapide, **sans changer une ligne** : le JIT a compilé et optimisé le code entre-temps. Une mesure sans chauffe aurait conclu que la version rapide est huit fois plus lente qu'en réalité.

---

## Étape 7 — Le test de vitesse, la démonstration, les mutants

Le code : [`ReportDemo.java`](ReportDemo.java).

**Expérience — `ReportDemo` (vérifiée) :**

```
memes rapports : true
legacy, 1 000 lignes : mediane 1412 ms (min 1294, max 1474)
rapide, 1 000 lignes : mediane 1 ms (min 1, max 1)
rapide, 10000 lignes : mediane 4 ms (min 4, max 9)
rapide, 100000 lignes : mediane 55 ms (min 44, max 57)
rapide, 1000000 lignes : mediane 849 ms (min 645, max 885)
```

Sur 1 000 lignes, environ **1 400 fois** plus rapide (1 412 ms contre 1 ms). De 10 000 à 1 000 000 de lignes (× 100), le temps passe de 4 à 849 ms (× 200) : à peu près **linéaire**, un peu plus à cause de la mémoire (un million de lignes et leurs objets sollicitent le ramasse-miettes). Le legacy, lui, aurait mis des années sur le million de lignes.

**Expérience — JFR sur `ReportDemo` (vérifié) :** l'enregistrement mélange encore le legacy (qui tourne en tête de la démo) ; en ne gardant que les lignes de la version rapide, les plus chaudes sont `FastReport.read` ligne 53 (le `seen.add`, c'est-à-dire le hachage de chaque ligne dans `HashMap.putVal`) et ligne 50 (`TaskEvent.parse`, le motif : `Pattern$BmpCharProperty.match`). Plus aucune boucle imbriquée : il ne reste que le travail **indispensable**, lire et hacher chaque ligne une fois. Pour aller plus loin, il faudrait éviter le motif (un analyseur écrit à la main, comme au projet 1), au prix d'un code plus long : à faire seulement si une mesure montre que c'est nécessaire.

**Les mutants :** avec les tests de référence, les **14 sont tués** (vérifié) ; le mutant 1, par le seul test de vitesse. Le `Check` prend environ 20 secondes.
