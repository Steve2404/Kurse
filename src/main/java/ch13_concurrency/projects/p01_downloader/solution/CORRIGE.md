# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier : `ChunkStats`, `ChunkTask`, `ChunkThread` et `Downloader`.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur la solution et sur une copie modifiée.

---

## Étape 1 — Le résumé d'un morceau, et sa fusion

**Le code :** [`ChunkStats.java`](ChunkStats.java).

**Question — pourquoi `Math.max(best, next.best)` serait-il faux ?** Parce qu'une série peut **traverser la frontière** entre deux morceaux. Avec `[3 3 5 5]` et `[5 5 5 2]`, chaque morceau a une meilleure série de 2 et de 3, mais la vraie plus longue série est `5 5 5 5 5`, de longueur 5 : la queue du 1er (2) plus la tête du 2e (3). C'est pourquoi le résumé garde `prefix` et `suffix`, et pourquoi `best` vaut aussi au moins `suffix + next.prefix`.

C'est le même découpage que la fusion du chapitre 10 (projet 4, étape 7) : un résumé de quelques nombres suffit pour fusionner, sans relire les octets.

---

## Étape 2 — Les threads du découpage

**Le code :** [`ChunkTask.java`](ChunkTask.java), [`ChunkThread.java`](ChunkThread.java) et le `main` de [`Downloader.java`](Downloader.java).

**Question — pourquoi pas de verrou ?** Chaque thread écrit **sa propre** case (`results[i]`, `workers[i]`) : deux threads n'écrivent jamais au même endroit. Et `main` ne lit les cases **qu'après** `join()`. `join()` garantit que tout ce que le thread a écrit est visible par celui qui attend (une relation *happens-before*).

**Expérience — sans les `join()` :** vérifié sur une copie de la solution, `main` lit les cases **avant** que les threads aient fini, et le programme s'arrête :

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "…ChunkStats.from()" because "<local7>" is null
```

La case est encore `null` : le thread n'a pas eu le temps de l'écrire. C'est **imprévisible**, car l'ordre et la vitesse des threads dépendent de la machine : un autre jour, une case pourrait être remplie et une autre non. Sur un petit exemple vérifié (4 threads qui calculent), `main` sans `join()` lit `[0, 0, 0, 0]`.

---

## Étape 3 — `run()` contre `start()`, et les états

**`run()` contre `start()` :** `who.run()` est un appel de méthode **ordinaire** : il s'exécute dans le thread **courant**, donc `main`. `start()` crée un **nouveau** thread, qui appelle `run()` à son tour : d'où `dl-run`.

**Les états :**
- `NEW` : créé, pas encore démarré ;
- `TIMED_WAITING` : en attente **avec** une durée (`sleep(60_000)`) ;
- `WAITING` : en attente **sans** durée (`join()` sur le dormeur) ;
- `BLOCKED` : bloqué à l'entrée d'un `synchronized`, dont `main` tient le verrou ;
- `TERMINATED` : fini, après `join()`.

---

## Étape 4 — Interruption, daemon, erreurs

**Question — pourquoi `false` chez le dormeur, et `true` dans la boucle ?** `interrupt()` lève un **drapeau**. Si le thread dort (`sleep`, `join`, `wait`), il est réveillé par une `InterruptedException`, et le drapeau est **remis à `false`** au moment où l'exception est lancée. La boucle, elle, ne dort jamais : elle teste le drapeau avec `isInterrupted()`, qui **ne l'efface pas**, d'où `true`.

**Question — pourquoi le daemon n'empêche-t-il pas la fin ?** La JVM s'arrête quand il ne reste **que** des threads daemon. Un daemon est un thread « de service » (comme le ramasse-miettes) : il ne retient pas le programme.

**Les deux erreurs :** `setDaemon(false)` après `start()` et un 2e `start()` lancent toutes deux une `IllegalThreadStateException`. Un thread ne peut démarrer qu'une fois, et son statut de daemon se fixe avant.

**La priorité :** 5 par défaut (`NORM_PRIORITY`), entre 1 et 10. Ce n'est qu'une **indication** pour le système : elle ne garantit aucun ordre.
