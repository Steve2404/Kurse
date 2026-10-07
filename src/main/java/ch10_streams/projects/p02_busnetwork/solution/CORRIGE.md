# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`BusNetwork.java`](BusNetwork.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18), sur la solution ou sur une copie instrumentée.

---

## Étape 1 — Modéliser une ligne

**Le code :** les records `Stop`, `Trip` et `Line` (avec `parse`, `departures`, `stop`, `goesFromTo`, `nextTrip` et `duration`).

**Question — un stream pour la somme cumulée ?** **Non.** Chaque décalage dépend du **précédent**. Un stream traite des éléments **indépendants** : on pourrait le faire avec un tableau à une case modifié dans un `map`, mais ce serait un effet de bord caché (et faux en parallèle). Une boucle `for` avec une variable `cumulated` dit honnêtement ce qui se passe.

**Les deux `Stream.iterate` :**
- `iterate(seed, hasNext, next)` (Java 9) : la condition d'arrêt fait partie de la source, et le stream est **fini**.
- `iterate(seed, next)` : un stream **infini**. Il faudrait ajouter `takeWhile(t -> !t.isAfter(last))` pour l'arrêter.

**Le piège :** si l'on écrit `filter` au lieu de `takeWhile`, le stream ne s'arrête jamais. Pire, `LocalTime` **fait le tour** à minuit : 23:50 + 20 min donne `00:10` (vérifié). Après 21:00, les horaires repassent par 00:00, 00:20… qui sont « avant 21:00 » et **repassent le filtre**. Vérifié avec une limite de 8 : `[20:00, 20:20, 20:40, 21:00, 00:00, 00:20, 00:40, 01:00]`.

---

## Étape 2 — `ARRETS`, `PAGE <n> <taille>`

**Le code :** `allStops`, `stops` et `page`.

**`flatMap`** : chaque ligne devient le **stream de ses arrêts**, puis tous sont mis bout à bout. `distinct` et `sorted` s'appliquent sur ce flux aplati.

**`skip` puis `limit`, ou l'inverse ?** Ce n'est **pas** pareil. Vérifié sur la page 2 de taille 3 :
- `skip(3).limit(3)` saute 3 éléments, puis en garde 3 : `[Musee, Phare, Plage]` ;
- `limit(3).skip(3)` garde les 3 premiers, puis les saute tous : `[]`.

**Page 4 :** `skip(9)` sur 9 arrêts ne laisse rien, et le `joining` rend `""`, d'où `vide`.

---

## Étape 3 — `LIGNES`

**Le code :** `linesByComplexity`.

**Le piège `reversed()` :** il inverse **tout** le comparateur construit jusque-là. Vérifié : `comparingInt(n).thenComparingInt(d).thenComparing(id).reversed()` donne `[L2, L1, L3, L4]`. Les durées et les ids sont **eux aussi** inversés : L2 (20 min) passe avant L1 (15 min), alors qu'on voulait la plus courte d'abord.

**La correction :** `comparing(nbArrets).reversed()` **d'abord**, puis `.thenComparingInt(Line::duration).thenComparing(Line::id)`. Seul le 1er critère est inversé.

---

## Étape 4 — `CIRCUIT <ligne> <ligne>`

**Le code :** `circuit`.

**`Stream.ofNullable(x)`** (Java 9) donne un stream vide si x est `null`, sinon un stream d'un élément. Une ligne inconnue disparaît donc sans `if`.

**`Stream.concat`** garde l'ordre : les arrêts de L1, puis ceux de L3. `distinct` élimine le second `Port`.

---

## Étape 5 — `PROCHAIN <ligne> <arrêt> <heure>` : la paresse prouvée

**Le code :** `withLineAndStop` et `next`.

**Pourquoi exactement 7 ?** L1 part à 06:00, 06:20, 06:40… et passe au Musée 10 minutes plus tard. Le premier passage ≥ 07:52 est 08:10, soit le départ de **08:00**, le 7e (06:00, 06:20, 06:40, 07:00, 07:20, 07:40, 08:00). `findFirst` arrête la génération **dès** qu'il a sa réponse : les départs suivants ne sont jamais calculés.

**Pourquoi 17 et pas 16 quand il n'y a plus de bus ?** L3 part de 07:00 à 19:00, toutes les 45 minutes : 07:00 + 16 × 45 = 19:00, soit **17** départs. Quand aucun ne convient, ils sont **tous** générés. La réponse « 18:36 » était au **16e** (18:15 + 21 min).

**À tester — avec `.toList()` avant la recherche :** la liste entière est construite, et la paresse est perdue. Vérifié : **46** horaires calculés pour L1 (tous ses départs de la journée) au lieu de 7, et 17 pour L3.

**Le compteur :** `peek(generated::add)` remplit une liste dont la **référence** ne change pas. Un `int` local ne pourrait pas être incrémenté dans une lambda (chapitre 8).

---

## Étape 6 — `HORAIRES <ligne> <arrêt> <de> <à>`

**Le code :** `timetable`.

**`dropWhile` et `takeWhile`** (Java 9) travaillent sur un **préfixe**. `dropWhile` jette tant que la condition est vraie, puis laisse **tout** passer. `takeWhile` garde tant qu'elle est vraie, puis **s'arrête**. Sur des horaires triés, c'est exactement « à partir de » et « jusqu'à ».

**Question — sur un stream non trié ?** `takeWhile` s'arrête au **premier** élément qui échoue, même si des éléments valides suivent. Vérifié : `Stream.of(1, 5, 2, 8, 3).takeWhile(x -> x < 6)` donne `[1, 5, 2]`, et le `3` est perdu. `filter` donnerait `[1, 5, 2, 3]`.

---

## Étape 7 — `DIRECT <départ> <arrivée> <heure>`

**Le code :** `direct`.

**`flatMap(Optional::stream)`** : chaque ligne candidate donne un `Optional<Trip>`, vide s'il n'y a plus de bus. Les vides disparaissent (projet 1).

**Question — pourquoi `Stade -> Centre` donne `aucun` ?** L2 dessert Centre (offset 7), **puis** Stade (offset 20). Un bus ne roule **que dans un sens** : `goesFromTo("Stade", "Centre")` exige que Stade soit **avant** Centre, ce qui est faux.

---

## Étape 8 — `CORRESPONDANCE <départ> <arrivée> <heure>` : l'algorithme

**Le code :** le record `Connection` et la méthode `connection`.

**`Hopital -> Port` à la main :**
- L4 passe à Hopital à 08:06 (le départ de 08:00 à Stade, + 6 min) et arrive à Gare à 08:16.
- Avec 2 minutes de changement, il faut un L1 au départ de Gare à partir de 08:18 : celui de **08:20**, qui arrive à Port à **08:35**.
- **L2 depuis Hopital ne mène nulle part** : après Hopital, L2 ne dessert que Stade, et aucune ligne ne va de Stade à Port (L4 va de Stade à Gare, puis il faudrait un 2e changement).

**Question — combien de trajets candidats pour `Universite -> Port` ?** Vérifié en comptant les appels à `nextTrip` : **4**. Ce sont 3 premiers trajets sur L2 (vers Centre, Hopital et Stade), plus 1 second trajet sur L1, depuis Centre. Depuis Hopital et Stade, aucune ligne ne mène à Port, donc aucun trajet n'est même calculé.

**Le départage :** à arrivée égale, on garde le départ **le plus tard** (`reverseOrder()` sur le départ) : on attend moins.

---

## Étape 9 — `ACCESSIBLE <ligne>`

**Le code :** `accessible`.

**`noneMatch`** se lit comme la phrase « aucun arrêt n'est inaccessible ». Il **court-circuite** : il s'arrête au premier arrêt inaccessible (Musée pour L1).

---

## Étape 10 — `TICKETS <n>`

**Le code :** `tickets`.

**Le compteur dans un champ :** une variable locale **réinitialisée** à chaque appel ne pourrait pas continuer la numérotation. Et `++n` sur une locale ne compile pas dans une lambda (chapitre 8). Un champ survit entre les appels et peut être modifié par la lambda via `this`.

**Question — sans `limit` ?** `Stream.generate` est **infini**. `collect(joining(…))` essaierait de lire tous les éléments et ne terminerait jamais : le programme tourne jusqu'à manquer de mémoire.

---

## Étape 11 — `RESEAU`

**Le code :** `network`.

**Question piège — sur un stream vide** (vérifié) :

| Appel | Résultat | Logique |
|---|---|---|
| `allMatch` | `true` | aucun élément ne contredit la condition (« vrai par vacuité ») |
| `anyMatch` | `false` | aucun élément ne la vérifie |
| `noneMatch` | `true` | aucun élément ne la vérifie |

---

## Étape 12 — `main`

**Le code :** `execute` et `main`. `RETARD` tombe dans le `default`.
