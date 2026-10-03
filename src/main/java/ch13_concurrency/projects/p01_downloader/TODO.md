# Projet 1 — Le téléchargeur en morceaux (threads « à la main »)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 13) :**
- **créer un thread**, des deux façons : un `Runnable` donné à `new Thread(…)`, ou une sous-classe de `Thread` ;
- **`start()` contre `run()`** ; **`join()`**, qui attend la fin **et** rend visibles les écritures du thread ;
- **les états** (`getState()`) : `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED` ;
- **`sleep`**, **`interrupt()`**, `isInterrupted()`, et `InterruptedException`, qui **efface** le drapeau ;
- **les daemons** (`setDaemon`), **les priorités**, et `IllegalThreadStateException`.

Côté algorithme : un calcul **découpé en morceaux**, chacun traité par un thread, puis **fusionné**. Chaque morceau rend sa somme et sa plus longue série d'octets identiques. La fusion de deux morceaux voisins doit tenir compte d'une série qui **traverse la frontière**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch13_concurrency.projects.p01_downloader` :
- le record `ChunkStats`, et les classes `ChunkTask` et `ChunkThread` ;
- **`Downloader`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13. Pas de fichiers, de JDBC, `System.exit` ni `printStackTrace`.

---

## Tableau de bord

### ☐ Étape 1 — Le résumé d'un morceau, et sa fusion

- **`record ChunkStats(int from, int to, long sum, int first, int last, int prefix, int suffix, int best)`** :
  - `sum` : la somme des octets de `[from, to)` ;
  - `first` et `last` : le premier et le dernier octet ;
  - `prefix` : la longueur de la série de tête ;
  - `suffix` : celle de la série de queue ;
  - `best` : la plus longue série du morceau.
- **`static ChunkStats of(int from, int to)`** calcule tout cela en un seul passage (`Data.at(i)`).
- **`ChunkStats merge(ChunkStats next)`** fusionne avec le morceau **suivant**. Si `last == next.first`, la série se prolonge :
  - le `prefix` s'allonge si le morceau entier n'est qu'une série ;
  - le `suffix` de même, depuis la fin ;
  - `best` vaut aussi au moins `suffix + next.prefix`.
- **Question :** pourquoi un simple `Math.max(best, next.best)` serait-il faux ?

### ☐ Étape 2 — Les threads du découpage

```
decoupage : 4 morceaux de 250000 octets
morceau 0 [0,250000) somme 417545, serie max 18, tete 2, queue 1, par dl-0 (Runnable)
morceau 1 [250000,500000) somme 416243, serie max 16, tete 1, queue 1, par dl-1 (Thread)
total : somme 1666775, plus longue serie 18 ; identique au calcul sequentiel true
```
- **`ChunkTask implements Runnable`** et **`ChunkThread extends Thread`** :
  - ils reçoivent l'indice, `from`, `to`, et deux tableaux partagés `ChunkStats[] results` et `String[] workers` ;
  - chacun écrit **sa** case : `results[i] = ChunkStats.of(...)` ;
  - `workers[i]` reçoit `<nom du thread> (Runnable)` ou `<nom> (Thread)`.
- **Dans `main`** :
  1. découpe `Data.SIZE` en `Data.CHUNKS` morceaux égaux (le dernier va jusqu'à la fin) ;
  2. pour les indices pairs, `new Thread(new ChunkTask(...), "dl-" + i)` ; pour les impairs, `new ChunkThread("dl-" + i, ...)` ;
  3. `start()` tous les threads, **puis** `join()` tous ;
  4. affiche une ligne par morceau, la fusion dans l'ordre, puis la comparaison (`equals`) avec `ChunkStats.of(0, Data.SIZE)`.
- **Question :** pourquoi les tableaux n'ont-ils pas besoin de verrou ici ?

### ☐ Étape 3 — `run()` contre `start()`, et les états

```
run() direct execute par main ; start() execute par dl-run
etats : avant start NEW ; dormeur TIMED_WAITING, attente WAITING, bloque BLOCKED ; apres join TERMINATED
```
- **`run()` contre `start()`** : un `Runnable who` écrit `Thread.currentThread().getName()` dans une case.
  1. Appelle d'abord `who.run()` ;
  2. puis lance `new Thread(who, "dl-run")` avec `start()` et `join()`.
- **`static void waitFor(Thread t, Thread.State s)`** : boucle `while (t.getState() != s) Thread.sleep(1);`.
- **Trois threads** :
  - `dormeur` fait `Thread.sleep(60_000)`. En cas d'interruption, il ajoute au journal `dormeur : InterruptedException, drapeau apres catch <isInterrupted()>` ;
  - `attente` fait `dormeur.join()` ;
  - `bloque` fait `synchronized (lock) { … }` et ajoute `bloque : verrou obtenu`.
- **Dans un `synchronized (lock)` tenu par `main`** :
  1. démarre les trois threads ;
  2. attends `TIMED_WAITING`, `WAITING` et `BLOCKED` ;
  3. note les trois états.
- **Hors du bloc, dans cet ordre :** `bloque.join()`, `dormeur.interrupt()`, `dormeur.join()`, `attente.join()`.
- **Le journal** est une `Collections.synchronizedList`.

### ☐ Étape 4 — Interruption, daemon, erreurs

```
journal : [bloque : verrou obtenu, dormeur : InterruptedException, drapeau apres catch false, boucle : arretee, drapeau true]
daemon true, setDaemon apres start IllegalThreadStateException, start deux fois IllegalThreadStateException, priorite par defaut 5 (min 1, max 10)
```
- **Un thread qui calcule** : `while (!Thread.currentThread().isInterrupted()) n++;`, puis il ajoute `boucle : arretee, drapeau <isInterrupted()>`. `main` fait `start()`, `interrupt()`, puis `join()`, et affiche le journal.
- **Un daemon qui dort 60 s** : `setDaemon(true)` **avant** `start()`. Ensuite :
  - `setDaemon(false)` lève une exception, à attraper ;
  - relance `start()` sur le thread `dl-run`, déjà terminé : exception aussi ;
  - affiche `getPriority()`, `Thread.MIN_PRIORITY` et `Thread.MAX_PRIORITY`.
- **Questions :**
  - Pourquoi le drapeau vaut-il `false` chez le dormeur, et `true` dans la boucle ?
  - Pourquoi un daemon qui dort encore n'empêche-t-il pas le programme de se terminer ?
- **Expérience :** remplace les `join()` de l'étape 2 par rien. Que deviennent les résultats ? Pourquoi est-ce imprévisible ?

---

## Checklist (vérifiée par `Check`)

- `Data.SIZE`, `Data.CHUNKS`, `Data.at(` ;
- `implements Runnable`, `extends Thread`, `record ChunkStats(`, `.merge(` ;
- `new Thread(`, `.start()`, `.join()`, `.run()`, `Thread.currentThread().getName()` ;
- `Thread.sleep(`, `.getState()`, `Thread.State.TIMED_WAITING`, `WAITING`, `BLOCKED`, `synchronized (` ;
- `.interrupt()`, `.isInterrupted()`, `catch (InterruptedException` ;
- `.setDaemon(true)`, `.isDaemon()`, `catch (IllegalThreadStateException`, `.getPriority()`, `Thread.MAX_PRIORITY` ;
- `Collections.synchronizedList(`.

---

## Sortie attendue complète

```
decoupage : 4 morceaux de 250000 octets
morceau 0 [0,250000) somme 417545, serie max 18, tete 2, queue 1, par dl-0 (Runnable)
morceau 1 [250000,500000) somme 416243, serie max 16, tete 1, queue 1, par dl-1 (Thread)
morceau 2 [500000,750000) somme 416714, serie max 17, tete 1, queue 2, par dl-2 (Runnable)
morceau 3 [750000,1000000) somme 416273, serie max 16, tete 1, queue 1, par dl-3 (Thread)
total : somme 1666775, plus longue serie 18 ; identique au calcul sequentiel true
run() direct execute par main ; start() execute par dl-run
etats : avant start NEW ; dormeur TIMED_WAITING, attente WAITING, bloque BLOCKED ; apres join TERMINATED
journal : [bloque : verrou obtenu, dormeur : InterruptedException, drapeau apres catch false, boucle : arretee, drapeau true]
daemon true, setDaemon apres start IllegalThreadStateException, start deux fois IllegalThreadStateException, priorite par defaut 5 (min 1, max 10)
```
