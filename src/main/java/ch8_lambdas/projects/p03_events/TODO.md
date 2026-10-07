# Projet 3 — La simulation d'une agence bancaire

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 8) :**
- **`Runnable`** (ni paramètre, ni résultat) : les actions planifiées ;
- **`IntSupplier`**, **`Supplier<String>`** et le `Supplier` **paresseux** (le calcul n'a lieu qu'à `get()`) ;
- **`BiConsumer<Integer, String>`** et `andThen` pour chaîner des écouteurs ;
- la **référence `this::arrival`** ;
- une **lambda qui capture** une variable locale (le nom du client) et s'exécute **plus tard** ;
- un **record** qui contient un `Runnable`.

Côté algorithmes :
- la **simulation à événements discrets** ;
- un **tas binaire** (file de priorité) écrit à la main ;
- une **file circulaire** ;
- un **générateur pseudo-aléatoire** déterministe.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p03_events` :
- `Event`, `EventQueue`, `Bank` ;
- **`EventsApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. Pas de `PriorityQueue` ni d'aucune collection (chapitre 9) : tu écris le tas toi-même.

**À quoi sert ce projet ?** Simuler une agence bancaire minute par minute. Des **événements** (un client arrive, un client repart…) sont rangés par heure dans une file. On prend toujours le prochain, et on exécute son action : une lambda `Runnable`.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch8-p03 -sourcepath src/main/java src/main/java/ch8_lambdas/projects/p03_events/EventsApp.java
java "-Duser.language=fr" -cp build/ch8-p03 ch8_lambdas.projects.p03_events.EventsApp
```

---

## Tableau de bord

### ☐ Étape 1 — La file d'événements

**📖 La leçon : `Runnable`, une action à faire plus tard.** `Runnable` est l'interface fonctionnelle la plus simple : rien en entrée, rien en sortie. Sa méthode est `run()`.

```java
Runnable bip = () -> System.out.println("bip");
bip.run();      // bip
bip.run();      // bip : on peut la relancer autant qu'on veut
```

**📖 Conseil :** un tas binaire se dessine comme un arbre rangé dans un tableau : la case `i` a pour enfants les cases `2i + 1` et `2i + 2`. Dessine-le, puis ajoute des valeurs à la main.

**👉 À toi :**

- **`record Event(int time, int seq, String label, Runnable action)`** :
  - `boolean before(Event other)` : le plus petit `time` d'abord, puis le plus petit `seq` (ordre de planification).
- **`EventQueue`**, un **tas binaire** dans un `Event[]` qui double de taille :
  - `push` ajoute à la fin, puis **remonte** tant que l'élément passe avant son parent `(i - 1) / 2` ;
  - `pop` rend la racine, met le dernier élément à sa place, puis le **redescend** vers le plus petit de ses enfants `2i + 1` et `2i + 2` ;
  - `isEmpty`.
  - **Question :** quelle est la complexité de `push` et de `pop` ?

### ☐ Étape 2 — L'agence

```
  0 | C1 arrive, file 1
  0 | C1 au guichet apres 0 min, pour 7 min
  5 | C2 arrive, file 1
...
```

**📖 La leçon : le code d'une lambda attend son appel.** Écrire une lambda ne l'exécute **pas**. Son code ne tourne qu'au moment où l'on appelle sa méthode (`get()`, `run()`, `accept()`…), et il lit alors les valeurs **de ce moment-là** :

```java
int[] compteur = {0};
Supplier<String> rapport = () -> "compteur = " + compteur[0];   // rien n'est calculé ici
compteur[0] = 5;
rapport.get()                                                    // "compteur = 5"
```

**📖 La leçon : les fournisseurs et les consommateurs de primitifs.** `IntSupplier` rend un `int` avec `getAsInt()`. `Consumer` et `BiConsumer` **consomment** des valeurs sans rien rendre : parfait pour un écouteur qui affiche ou compte.

**📖 Rappel :** `this::arrival` est une référence de méthode sur l'objet courant (projet 1, étape 1). Un tableau circulaire : l'indice suivant de `i` est `(i + 1) % taille`.

**👉 À toi :**

- **`Bank(IntSupplier nextArrival, IntSupplier nextService, Supplier<String> names, BiConsumer<Integer, String> listener)`**.
- **L'état :**
  - l'horloge, et un compteur `seq` ;
  - la file des clients, un tableau **circulaire** de 64 cases (indices `% 64`) qui retient l'heure d'arrivée et le nom ;
  - les guichets libres (`Data.TELLERS`) ;
  - les statistiques : servis, attente totale, attente max, file max, minutes occupées.
- `schedule(int at, String label, Runnable action)` pousse un `Event`.
- **`run()`** planifie `this::arrival` à 0, puis : tant que la file n'est pas vide, `pop`, avance l'horloge, et `e.action().run()`.
- **`arrival()`** :
  1. tire un nom (`names.get()`) et l'enfile ;
  2. met à jour la file max ;
  3. notifie `nom arrive, file N` ;
  4. lance `startServices()` ;
  5. planifie la prochaine arrivée à `clock + nextArrival.getAsInt()`, **si** elle tombe avant `Data.CLOSING`.
- **`startServices()`** : tant qu'un guichet est libre et que la file n'est pas vide :
  1. défile et calcule l'attente ;
  2. tire la durée ;
  3. notifie `nom au guichet apres W min, pour D min` ;
  4. planifie la **fin de service** à `clock + D`, avec une lambda qui capture `name` : servis + 1, guichet libéré, notification `nom repart`, puis `startServices()`.
- **`Supplier<String> report()`** rend une lambda. Le texte n'est construit qu'à l'appel de `get()` : `N clients servis, attente moyenne X min, max M min, file max F, occupation P %, fermeture reelle T`.
  - La moyenne est arrondie à 0,1.
  - L'occupation vaut `100 * minutes occupées / (guichets * horloge finale)`, arrondie.

### ☐ Étape 3 — Le programme

```
...
57 evenements, 3 attentes de 10 min ou plus
19 clients servis, attente moyenne 4.5 min, max 12 min, file max 4, occupation 94 %, fermeture reelle 78
runnable : fermeture de l'agence
```

**📖 La leçon : enchaîner des `Consumer`.** `a.andThen(b)` donne un `Consumer` qui appelle `a`, **puis** `b`, avec la même valeur :

```java
Consumer<String> un = s -> System.out.print("[" + s + "]");
Consumer<String> deux = s -> System.out.println("(" + s + ")");
un.andThen(deux).accept("x");     // [x](x)
```

**👉 À toi :**

- **Le générateur :** `seed = Data.SEED`, puis `draw(lo, hi)` :
  - `seed = (seed * 1103515245 + 12345) & 0x7fffffffL;` ;
  - `return lo + (int) (seed % (hi - lo + 1));`.
- **Les fournisseurs :**
  - `IntSupplier arrivals = () -> draw(ARRIVAL_MIN, ARRIVAL_MAX)` ;
  - `services` de même ;
  - `Supplier<String> names = () -> "C" + ++counter[0]` (un `int[] counter`).
- **Trois écouteurs**, combinés par `journal.andThen(count).andThen(alarm)` :
  - `journal` affiche `String.format("%3d", t) + " | " + msg` pour les `Data.LOG_LINES` premiers messages ;
  - `count` compte tous les messages ;
  - `alarm` compte les messages de prise en charge dont l'attente (le nombre entre `apres ` et ` min`) est **≥ 10**.
- **L'ordre** : récupère `report` **avant** `run()`, puis affiche `...`, la ligne des compteurs, et `report.get()`.
- **La fin :**
  - `Runnable hello` affiche `runnable : fermeture de l'agence` ;
  - `Supplier<Runnable> deferred = () -> hello;` ;
  - puis `deferred.get().run()`.
- **Question :** le rapport est demandé avant `run()`. Pourquoi contient-il pourtant les chiffres finaux ?

---

## Checklist (vérifiée par `Check`)

- `Data.SEED` et `Data.TELLERS` ;
- `Runnable`, `IntSupplier`, `Supplier<String>`, `BiConsumer<Integer, String>` ;
- `andThen`, `this::arrival`, `getAsInt()`, `accept(`, `run()` ;
- `record Event(` et `class EventQueue`.

---

## Sortie attendue complète

```
  0 | C1 arrive, file 1
  0 | C1 au guichet apres 0 min, pour 7 min
  5 | C2 arrive, file 1
  5 | C2 au guichet apres 0 min, pour 5 min
  7 | C1 repart
  7 | C3 arrive, file 1
  7 | C3 au guichet apres 0 min, pour 11 min
 10 | C2 repart
 10 | C4 arrive, file 1
 10 | C4 au guichet apres 0 min, pour 9 min
 12 | C5 arrive, file 1
 17 | C6 arrive, file 2
 18 | C3 repart
 18 | C5 au guichet apres 6 min, pour 5 min
...
57 evenements, 3 attentes de 10 min ou plus
19 clients servis, attente moyenne 4.5 min, max 12 min, file max 4, occupation 94 %, fermeture reelle 78
runnable : fermeture de l'agence
```
