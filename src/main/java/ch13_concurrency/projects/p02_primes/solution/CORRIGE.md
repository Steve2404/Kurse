# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Le crible segmenté

**Le code :** `SieveTask.java` et `Segment.java`. Une `Callable<Segment>` est une tâche qui **rend** un résultat (contrairement à un `Runnable`) et qui peut lancer une exception vérifiée.

**La fusion** (`merge`) : le compte s'additionne, le premier vient du 1er segment et le dernier du 2e. L'écart le plus grand est le maximum des deux écarts **et** de l'écart à la frontière, `next.first - last`, pour la même raison qu'au projet 1.

---

## Étape 2 — Soumettre et combiner

**L'ordre :** les segments sont **soumis** dans l'ordre, mais ils finissent dans n'importe quel ordre. Lire les `Future` dans l'ordre de la liste suffit à fusionner dans l'ordre : `get()` attend simplement le segment s'il n'est pas prêt.

**Question — `invokeAny` si toutes les tâches échouent ?** Il lance une **`ExecutionException`**, dont la cause est l'exception d'une des tâches. Vérifié avec deux tâches qui lancent chacune une `IllegalStateException` : `ExecutionException <- IllegalStateException`.

---

## Étape 3 — Délai, annulation, échec

- `get(50 ms)` lance une `TimeoutException`. La tâche, elle, continue ; `cancel(true)` l'interrompt (son `sleep` reçoit une `InterruptedException`). Ensuite `isCancelled()` et `isDone()` valent `true` : une tâche annulée est considérée comme **terminée** ;
- l'échec : la division par zéro arrive **dans le thread du pool**. `get()` la renvoie enveloppée : `ExecutionException <- ArithmeticException: / by zero` ;
- `submit(Runnable).get()` rend `null` : un `Runnable` ne rend rien.

---

## Étape 4 — Arrêter

**Après `shutdown()` :** `isShutdown()` vaut `true` tout de suite ; `isTerminated()` seulement quand toutes les tâches sont finies, d'où `awaitTermination` avant. Une nouvelle soumission est refusée : `RejectedExecutionException`.

**Question — pourquoi `t1`, `t2`, `t3` n'apparaissent-ils jamais ?** Le pool n'a **qu'un** thread, occupé par la tâche bloquante. Les trois autres attendent dans la file. `shutdownNow()` interrompt la bloquante (d'où `bloquante interrompue`) et **retire** les tâches en attente sans les lancer : elles sont dans la liste rendue (taille 3).

**Question — et si on oublie `shutdown()` ?** Les threads du pool ne sont **pas** des daemons : ils attendent de nouvelles tâches, et le programme **ne se termine jamais**. Vérifié avec un petit programme : il affiche ses lignes, puis reste bloqué jusqu'à ce qu'on l'arrête de force (au bout de 5 s ici). Dans IntelliJ, il faudrait cliquer sur le carré rouge ■. Et `Check` ne se terminerait pas non plus.
