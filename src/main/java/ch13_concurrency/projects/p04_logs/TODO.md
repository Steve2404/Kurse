# Projet 4 — L'analyse de journaux (producteurs, consommateurs, collections concurrentes)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 13) :**
- **`BlockingQueue`** (`LinkedBlockingQueue` bornée) :
  - `put` et `take` **bloquent** ;
  - `offer` et `poll` avec délai ;
  - `remainingCapacity` ;
- **le schéma producteurs / consommateurs**, et l'arrêt par « **pilule empoisonnée** » ;
- **`ConcurrentHashMap`** :
  - `merge`, `putIfAbsent`, `computeIfAbsent`, `compute` (atomiques) ;
  - le refus de `null` ;
  - `newKeySet()` ;
- **`ConcurrentSkipListMap`** et **`ConcurrentSkipListSet`** (triées), **`ConcurrentLinkedQueue`** ;
- **`ConcurrentModificationException`** (même avec un seul thread) contre **`CopyOnWriteArrayList`** ;
- **`Collections.synchronizedList`**, et le verrouillage manuel pour la parcourir.

Côté algorithme : un pipeline d'analyse. Les producteurs déposent les lignes dans une file, les consommateurs les agrègent : visites et durée moyenne par page, codes de statut, utilisateurs en erreur, requêtes lentes. Tout est affiché **trié**, pour ne pas dépendre de l'ordre de traitement.

**Ce que TU crées :** dans `ch13_concurrency.projects.p04_logs` : `Stats` et **`LogPipeline`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p04 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p04_logs/LogPipeline.java
java "-Duser.language=fr" -cp build/ch13-p04 ch13_concurrency.projects.p04_logs.LogPipeline
```

---

## Tableau de bord

### ☐ Étape 1 — Les agrégats concurrents

**📖 La leçon : les collections concurrentes.** Les collections du chapitre 9 ne sont **pas** faites pour être modifiées par plusieurs threads à la fois. Leurs cousines de `java.util.concurrent` le sont :

| Au lieu de… | utilise… |
|---|---|
| `HashMap` | `ConcurrentHashMap` |
| `TreeMap` / `TreeSet` | `ConcurrentSkipListMap` / `ConcurrentSkipListSet` |
| `LinkedList` (comme file) | `ConcurrentLinkedQueue` |
| `ArrayList` (souvent lue, rarement modifiée) | `CopyOnWriteArrayList` |

Sur une `ConcurrentHashMap`, utilise les méthodes qui font tout **en une fois** : `merge`, `compute`, `putIfAbsent`, `computeIfAbsent` (chapitre 9, projet 2).

**👉 À toi :**

- **`Stats`**, avec 5 champs :
  - `Map<String, Integer> hits` et `Map<String, Long> totalMs`, deux `ConcurrentHashMap` ;
  - `NavigableMap<Integer, Integer> statuses`, une `ConcurrentSkipListMap` ;
  - `NavigableSet<String> errorUsers`, un `ConcurrentSkipListSet` ;
  - `Queue<String> slow`, une `ConcurrentLinkedQueue`.
- **`void record(String line)`** découpe `user=… page=… ms=… status=…`. Puis :
  - `hits.merge(page, 1, Integer::sum)` et `totalMs.merge(page, ms, Long::sum)` ;
  - `statuses.merge(status, 1, Integer::sum)` ;
  - si le statut vaut 500, ajoute l'utilisateur à `errorUsers` ;
  - si `ms > 950`, `slow.offer(ms + " ms " + user + " " + page)`.
- **Question :** pourquoi `hits.put(page, hits.get(page) + 1)` serait-il faux, même sur une `ConcurrentHashMap` ?

### ☐ Étape 2 — Le pipeline

```
lignes traitees 6000 / 6000, file vide true
visites {/=1244, /compte=1207, ...} ; duree moyenne (ms) /=516 /compte=506 ...
page la plus vue /=1244 ; statuts {200=5353, 404=632, 500=15} ; erreurs 500 pour [...]
requetes lentes 369, les 3 plus lentes [1009 ms bob /, 1009 ms bob /produit, 1009 ms chloe /compte]
```

**📖 La leçon : producteurs et consommateurs.** Une `BlockingQueue` est une file où `put` **attend** s'il n'y a plus de place, et `take` **attend** s'il n'y a rien. Des producteurs déposent, des consommateurs prennent. Pour arrêter un consommateur, on lui envoie une valeur spéciale, la **pilule empoisonnée** :

```java
BlockingQueue<String> tapis = new LinkedBlockingQueue<>(2);     // capacité 2
Thread patissier = new Thread(() -> {
    try {
        for (String g : new String[] {"eclair", "tarte", "FIN"}) tapis.put(g);
    } catch (InterruptedException e) { }
});
patissier.start();
String g;
while (!(g = tapis.take()).equals("FIN")) {
    System.out.print(g + " ");                                   // eclair tarte
}
```

**👉 À toi :**

- **La file :** `BlockingQueue<String> queue = new LinkedBlockingQueue<>(Data.CAPACITY)`. Les compteurs : un `Stats`, et un `AtomicInteger consumed`.
- **`Data.CONSUMERS` consommateurs**, sur leur propre pool. Chacun fait :
  - `take()` en boucle ;
  - s'il reçoit `Data.POISON`, il s'arrête ;
  - sinon `stats.record(line)`, puis `consumed.incrementAndGet()`.
- **`Data.PRODUCERS` producteurs**, sur un autre pool : le producteur p fait `put(Data.line(i))` pour sa part de `Data.LINES`.
- **L'ordre de fin :**
  1. `shutdown` et `awaitTermination` des producteurs ;
  2. **une pilule par consommateur** ;
  3. `shutdown` et `awaitTermination` des consommateurs.
- **L'affichage**, ligne par ligne :
  1. le nombre traité et `isEmpty()` ;
  2. les visites (une `TreeMap`), puis la durée moyenne entière par page (`totalMs / hits`) ;
  3. la page la plus vue (par visites décroissantes, puis par nom), les statuts, puis les utilisateurs en erreur ;
  4. les requêtes lentes : le nombre, puis les 3 plus lentes (triées par durée décroissante, puis par texte).
- **Question :** pourquoi les producteurs ne peuvent-ils pas envoyer les pilules eux-mêmes, à la fin de leur boucle ?

### ☐ Étape 3 — Les règles des collections

```
file de 1 : offer true puis false, poll 1 puis null, remainingCapacity 1
ArrayList modifiee en boucle : ConcurrentModificationException ; CopyOnWriteArrayList : 3 tours, taille finale 6 [a, b, c, a!, b!, c!]
ConcurrentHashMap.put(null) NullPointerException, HashMap.put(null) 1 ; x=100 y=10 ; synchronizedList [1, 2, 3] ; newKeySet true
```

**📖 Rappel :** les itérateurs et la modification pendant un parcours (chapitre 9, projet 1, étape 3). Une `Collections.synchronizedList` protège chaque méthode, mais un parcours ou un tri en fait **plusieurs** : il faut alors un `synchronized (liste) { … }` autour.

**👉 À toi :**

- **Une file de capacité 1 :** `offer(1, 10 ms)`, `offer(2, 10 ms)`, `poll(10 ms)` deux fois, puis `remainingCapacity()`.
- **La modification pendant le parcours :**
  - un for-each sur `ArrayList` `[a, b, c]` qui ajoute `s + "!"` → attrape l'exception ;
  - le même sur une `CopyOnWriteArrayList`, en comptant les tours.
- **Une `ConcurrentHashMap`** :
  1. `put(null, 1)` → attrape l'exception ; puis une `HashMap` qui accepte `null` ;
  2. `putIfAbsent("x", 1)`, puis `putIfAbsent("x", 2)` ;
  3. `computeIfAbsent("y", k -> 10)` et `compute("x", (k, v) -> v * 100)`.
- **Une `Collections.synchronizedList`** de `[3, 1, 2]`, triée **dans** un `synchronized (synced)`. Enfin `ConcurrentHashMap.newKeySet().add("k")`.
- **Question :** pourquoi la `CopyOnWriteArrayList` ne fait-elle que 3 tours alors qu'elle contient 6 éléments à la fin ?

---

## Checklist (vérifiée par `Check`)

- `Data.LINES`, `Data.PRODUCERS`, `Data.CONSUMERS`, `Data.CAPACITY`, `Data.POISON`, `Data.line(` ;
- `BlockingQueue<String>`, `new LinkedBlockingQueue<>(`, `.take()`, `.put(` ;
- `new ConcurrentHashMap<>()`, `.merge(`, `new ConcurrentSkipListMap<>()`, `new ConcurrentSkipListSet<>()`, `new ConcurrentLinkedQueue<>()` ;
- `.offer(`, `.poll(10, TimeUnit.MILLISECONDS)`, `.remainingCapacity()` ;
- `catch (ConcurrentModificationException`, `new CopyOnWriteArrayList<>(`, `catch (NullPointerException` ;
- `.putIfAbsent(`, `.computeIfAbsent(`, `.compute(`, `Collections.synchronizedList(`, `synchronized (synced)`, `ConcurrentHashMap.newKeySet()`.

---

## Sortie attendue complète

```
lignes traitees 6000 / 6000, file vide true
visites {/=1244, /compte=1207, /panier=1208, /produit=1182, /recherche=1159} ; duree moyenne (ms) /=516 /compte=506 /panier=510 /produit=522 /recherche=520
page la plus vue /=1244 ; statuts {200=5353, 404=632, 500=15} ; erreurs 500 pour [ana, bob, chloe, dan, eve, fred, gina]
requetes lentes 369, les 3 plus lentes [1009 ms bob /, 1009 ms bob /produit, 1009 ms chloe /compte]
file de 1 : offer true puis false, poll 1 puis null, remainingCapacity 1
ArrayList modifiee en boucle : ConcurrentModificationException ; CopyOnWriteArrayList : 3 tours, taille finale 6 [a, b, c, a!, b!, c!]
ConcurrentHashMap.put(null) NullPointerException, HashMap.put(null) 1 ; x=100 y=10 ; synchronizedList [1, 2, 3] ; newKeySet true
```
