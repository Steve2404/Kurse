# Projet 3 — La simulation d'une agence bancaire

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

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

---

## Tableau de bord

### ☐ Étape 1 — La file d'événements

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
