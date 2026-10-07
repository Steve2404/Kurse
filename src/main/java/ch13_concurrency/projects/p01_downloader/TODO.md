# Projet 1 — Le téléchargeur en morceaux (threads « à la main »)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Ce que le chapitre 13 t'apprend :** la **concurrence**, c'est-à-dire faire travailler **plusieurs threads en même temps**. Un **thread** est un fil d'exécution : une suite d'instructions qui avance. Jusqu'ici, ton programme n'en avait qu'un, `main`. Avec plusieurs threads, des calculs avancent en parallèle, comme plusieurs cuisiniers dans une même cuisine.

**La difficulté :** l'ordre dans lequel les threads avancent **change à chaque lancement**. Tes programmes ne doivent donc afficher que des résultats qui n'en dépendent pas (voir le `PARCOURS.md`). `Check` lance ton programme plusieurs fois.

**Les imports :** `java.util.concurrent.*` (et ses sous-paquets `atomic` et `locks`).

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p01 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p01_downloader/Downloader.java
java "-Duser.language=fr" -cp build/ch13-p01 ch13_concurrency.projects.p01_downloader.Downloader
```

**Un programme qui ne s'arrête pas ?** Dans IntelliJ, clique sur le carré rouge ■ du panneau Run. Dans le terminal, appuie sur **Ctrl + C**.

---

## Tableau de bord

### ☐ Étape 1 — Le résumé d'un morceau, et sa fusion

**📖 Rappel :** résumer un morceau avec quelques nombres, puis fusionner deux résumés voisins sans relire les données (chapitre 10, projet 4, étape 7). Ici, ce découpage permet de confier chaque morceau à un thread différent.

**👉 À toi :**

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

**📖 La leçon : créer et lancer des threads.** Un thread exécute un `Runnable` (une interface fonctionnelle : `void run()`). Deux façons de l'écrire : passer un `Runnable` à `new Thread`, ou **étendre** la classe `Thread` et redéfinir `run()`.

```java
String[] fait = new String[2];
Thread t1 = new Thread(() -> fait[0] = "croissants par " + Thread.currentThread().getName(), "four-1");
Thread t2 = new Thread(() -> fait[1] = "pains par " + Thread.currentThread().getName(), "four-2");
t1.start();          // lance le thread : il travaille EN MÊME TEMPS que main
t2.start();
t1.join();           // main attend que t1 ait fini
t2.join();
// fait = [croissants par four-1, pains par four-2]
```

- `start()` démarre le thread, puis rend **tout de suite** la main ;
- `join()` attend la fin du thread ;
- `Thread.currentThread().getName()` donne le nom du thread qui exécute la ligne.

**👉 À toi :**

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

**📖 La leçon : `run()` n'est pas `start()`.** Appeler `run()` directement est un appel de méthode **ordinaire** : il s'exécute dans le thread courant, sans en créer de nouveau.

```java
Runnable r = () -> System.out.println(Thread.currentThread().getName());
r.run();                         // affiche main
new Thread(r, "four-3").start(); // affiche four-3
```

**📖 La leçon : les états d'un thread.** `t.getState()` rend une valeur de l'`enum` `Thread.State` :

| État | Le thread… |
|---|---|
| `NEW` | est créé, mais pas encore démarré |
| `RUNNABLE` | travaille, ou est prêt à travailler |
| `BLOCKED` | attend un verrou `synchronized` tenu par un autre |
| `WAITING` | attend sans limite (`join()`) |
| `TIMED_WAITING` | attend pendant une durée (`sleep(ms)`) |
| `TERMINATED` | a fini |

`synchronized (objet) { … }` : un seul thread à la fois peut entrer dans un bloc protégé par le même objet. Les autres sont `BLOCKED` à l'entrée. Le projet 3 te l'apprend en détail.

**👉 À toi :**

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

**📖 La leçon : interrompre, ce n'est pas tuer.** `t.interrupt()` lève un **drapeau** dans le thread `t`. C'est à `t` de le regarder et de s'arrêter proprement :
- s'il dort (`sleep`, `join`), il est réveillé par une `InterruptedException` (une exception vérifiée) ;
- sinon, il peut tester `Thread.currentThread().isInterrupted()`.

**📖 La leçon : les threads daemon.** Un thread **daemon** est un thread « de service » : la JVM s'arrête quand il ne reste plus que des daemons. `t.setDaemon(true)` se fait **avant** `start()`.

**👉 À toi :**

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
