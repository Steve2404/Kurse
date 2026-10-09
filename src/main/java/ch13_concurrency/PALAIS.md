# 🏠 Palais mental — chapitre 13 : la douche 2, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la douche 2 gardent le chapitre 12.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🧺 La serviette — les threads, `start` contre `run`

- **Image :** tu jettes une serviette à un **deuxième baigneur** : `start()` le fait **entrer dans sa propre douche**, en même temps que toi. Si tu cries `run()`, c'est **toi** qui fais son travail, dans **ta** douche : aucun nouveau baigneur. Le baigneur `Runnable` ne rend **rien** ; le baigneur `Callable` rend un **résultat** et a le droit de lancer une exception vérifiée. Un baigneur **démon** (`daemon`) part dès que les vrais baigneurs ont fini.
- **À retenir :**
  - `new Thread(runnable).start()` crée un thread ; `run()` exécute dans le thread **courant** ;
  - `Runnable.run()` : `void`, pas d'exception vérifiée ; `Callable<V>.call()` : rend `V`, peut lancer `Exception` ;
  - `start()` deux fois sur le même thread → `IllegalThreadStateException` ;
  - états : `NEW`, `RUNNABLE`, `BLOCKED`, `WAITING`, `TIMED_WAITING`, `TERMINATED` ; la JVM s'arrête quand il ne reste que des threads démons ;
  - l'ordre d'exécution entre threads n'est **pas garanti**.
- **Mon image :** …

### 8. 🛁 Le tapis de bain — `ExecutorService`

- **Image :** sur le tapis, un **chef d'équipe** (l'exécuteur) avec un nombre fixe de baigneurs. `submit` lui confie une tâche et te donne un **ticket `Future`** : `get()` sur le ticket te fait **attendre** le résultat. `execute` confie sans ticket. `shutdown()` : le chef **ne prend plus de nouvelles tâches** mais finit les siennes. `shutdownNow()` : il **tente d'arrêter** tout le monde. `awaitTermination` : tu attends qu'il ait fini.
- **À retenir :**
  - `Executors.newSingleThreadExecutor()`, `newFixedThreadPool(n)`, `newCachedThreadPool()`, `newScheduledThreadPool(n)` ;
  - `execute(Runnable)` → `void` ; `submit(Runnable | Callable)` → `Future` ; `invokeAll` (toutes) ; `invokeAny` (une seule réussie) ;
  - `Future.get()` bloque ; `get(délai, unité)` ; `isDone()`, `cancel()` ;
  - `shutdown()` puis `awaitTermination(…)` ; oublier `shutdown` = le programme ne s'arrête pas ; après `shutdown`, `submit` → `RejectedExecutionException`.
- **Mon image :** …

### 9. 🪝 Le porte-serviette — les courses et `synchronized`

- **Image :** dix baigneurs veulent **accrocher leur serviette au même crochet** en même temps : `compteur++` semble une seule action, mais c'est **lire, ajouter, écrire** ; deux baigneurs lisent la même valeur, une serviette **tombe** (course). Solution 1 : un **crochet magique `AtomicInteger`** qui fait les trois gestes d'un coup (`incrementAndGet`). Solution 2 : un **verrou `synchronized`** sur le porte-serviette : un seul baigneur à la fois.
- **À retenir :**
  - `x++` n'est **pas atomique** : course possible entre threads ;
  - `AtomicInteger` : `incrementAndGet()` (rend la nouvelle valeur), `getAndIncrement()` (l'ancienne), `addAndGet`, `get`, `set` ;
  - `synchronized` sur une méthode d'instance verrouille `this` ; sur une méthode `static`, la **classe** ; `synchronized (objet) { … }` un objet choisi ;
  - tous les threads doivent verrouiller **le même** objet.
- **Mon image :** …

### 10. 🪥 La brosse à dents — `Lock` et `CyclicBarrier`

- **Image :** une **brosse à dents partagée**, avec un **cadenas `ReentrantLock`**. Tu fais `lock()`, tu te brosses, et tu fais `unlock()` **dans `finally`**, sinon la brosse reste **bloquée pour toujours**. Tu peux essayer sans attendre : `tryLock()` dit **oui ou non tout de suite**. Rendre un cadenas que tu n'as **pas pris** : `IllegalMonitorStateException`. Au fond, une **barrière `CyclicBarrier(4)`** : personne ne sort de la salle de bain tant que **les 4** ne sont pas arrivés.
- **À retenir :**
  - `Lock l = new ReentrantLock(); l.lock(); try { … } finally { l.unlock(); }` ;
  - `tryLock()` rend un `boolean` immédiatement ; `tryLock(délai, unité)` attend un peu ;
  - autant de `unlock()` que de `lock()` (réentrant) ; `unlock()` sans le verrou → `IllegalMonitorStateException` ;
  - `CyclicBarrier(n, action)` : `await()` bloque jusqu'à ce que `n` threads l'appellent, puis l'action s'exécute une fois.
- **Mon image :** …

### 11. 🗄️ L'étagère — les collections concurrentes

- **Image :** sur l'étagère, des flacons partagés. Un **flacon ordinaire** (`ArrayList`) qu'on modifie **pendant** qu'un autre le parcourt **explose** (`ConcurrentModificationException`). Le **flacon `ConcurrentHashMap`** supporte plusieurs mains à la fois. Le **flacon `CopyOnWriteArrayList`** fait une **photocopie** à chaque écriture : celui qui le parcourt lit **l'ancienne photo**, sans exploser. `Collections.synchronizedList` met un **cadenas** sur un flacon ordinaire.
- **À retenir :**
  - `ConcurrentHashMap`, `ConcurrentLinkedQueue`, `ConcurrentSkipListMap/Set` (triés), `CopyOnWriteArrayList/Set`, `LinkedBlockingQueue` ;
  - `CopyOnWrite…` : copie à chaque écriture ; un itérateur voit l'**instantané** de départ ;
  - `Collections.synchronizedList(…)` : chaque méthode est synchronisée, mais **l'itération** doit être synchronisée à la main ;
  - modifier une collection ordinaire pendant son parcours → `ConcurrentModificationException` (même dans un seul thread).
- **Mon image :** …

### 12. 🗑️ La poubelle — les problèmes de vivacité et les tâches planifiées

- **Image :** trois scènes dans la poubelle. **Interblocage** : deux baigneurs tiennent chacun **une serviette** et attendent **celle de l'autre**, pour toujours. **Famine** : un petit baigneur n'obtient **jamais** la douche, les autres passent toujours avant. **Livelock** : deux baigneurs polis se **décalent** sans fin dans le couloir, actifs mais bloqués. Sur le couvercle, un **minuteur** : `schedule` (une fois, plus tard), `scheduleAtFixedRate` (toutes les N secondes **depuis le début** de la tâche précédente), `scheduleWithFixedDelay` (N secondes **après la fin** de la précédente).
- **À retenir :**
  - interblocage (deadlock), famine (starvation), livelock : threads bloqués ou improductifs ; prendre les verrous **toujours dans le même ordre** évite l'interblocage ;
  - `ScheduledExecutorService` : `schedule(tâche, délai, unité)`, `scheduleAtFixedRate(…)`, `scheduleWithFixedDelay(…)` ;
  - streams parallèles : `parallelStream()` ; `forEach` sans ordre, `forEachOrdered` dans l'ordre, `findAny` imprévisible ; `reduce` exige un accumulateur **associatif**.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : que se passe-t-il si on appelle `run()` au lieu de `start()` ?
2. Station 8 : que fait `shutdown()`, et que ne fait-il pas ?
3. Station 9 : pourquoi `compteur++` n'est-il pas sûr entre threads ?
4. Station 10 : où doit être `unlock()` ?
5. Station 11 : que voit un itérateur de `CopyOnWriteArrayList` modifiée pendant le parcours ?
6. Station 12 : quelle différence entre `scheduleAtFixedRate` et `scheduleWithFixedDelay` ?
