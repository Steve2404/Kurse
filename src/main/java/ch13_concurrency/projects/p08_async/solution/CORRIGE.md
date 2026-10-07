# Projet 8 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Les devis asynchrones

**Le code :** `TravelAgency.java`. Le vol et l'hôtel sont demandés **en même temps** ; `thenCombine` additionne quand les deux sont prêts.

**Question — pourquoi `thenCompose` et pas `thenApply` ?** `toChf` rend une `CompletableFuture<Integer>`. Avec `thenApply`, on obtiendrait une `CompletableFuture<CompletableFuture<Integer>>` : une future **dans** une future. Vérifié : `javac` refuse de ranger ce résultat dans une `CompletableFuture<Integer>` (`incompatible types: inference variable U#1 has incompatible bounds`). `thenCompose` « aplatit », comme `flatMap` pour un `Optional` (chapitre 10).

**`exceptionally`** rattrape l'erreur de **toute** la chaîne : `Atlantis indisponible (aucun vol pour Atlantis)`. L'exception reçue est enveloppée, d'où `e.getCause().getMessage()`.

---

## Étape 2 — Erreurs, délais, enchaînements

- `handle((prix, e) -> …)` reçoit **soit** le résultat, **soit** l'erreur : il s'exécute dans les deux cas ;
- `join()` : `CompletionException <- IllegalStateException` ; `get()` : `ExecutionException`, la version vérifiée ;
- `completeOnTimeout` donne `valeur par defaut` au bout de 20 ms ; `orTimeout` termine en erreur, avec une `TimeoutException` en cause ;
- `anyOf` rend le résultat de la **première** future terminée : `cache`, déjà prête ;
- `thenAccept` consomme la valeur sans rien rendre, et `thenRun` exécute une action sans la valeur.

---

## Étape 3 — `ThreadLocal`, `Semaphore`, `CountDownLatch`

**`ThreadLocal`** : chaque tâche incrémente **sa** valeur à elle. Dans `main`, la valeur vaut 42 après `set(42)`, puis 0 après `remove()` (la valeur initiale de `withInitial`). Vérifié sur un petit exemple : un thread qui met 5 laisse la valeur de `main` à 0.

**Question — pourquoi ne pas afficher `maxInside` ?** Il vaut 1 ou 2 selon la **vitesse** des threads : s'ils arrivent les uns après les autres, il n'y en a jamais deux à la fois. Le sémaphore garantit seulement « **au plus** 2 », d'où l'affichage de `maxInside <= 2`, qui est toujours vrai.

**`tryAcquire(3)`** rend `false` tout de suite : il n'y a que 2 permis.

---

## Étape 4 — Fork/Join

**Le principe :** couper le tableau en deux, récursivement, jusqu'à des morceaux assez petits pour être calculés directement, puis fusionner les résumés. La fusion est celle du projet 1 et du chapitre 10 (projet 4).

**Question — pourquoi `left.fork(); right.compute(); left.join()` ?** Le thread courant a du travail à faire **pendant** que la gauche est calculée ailleurs : il calcule la droite lui-même. Avec `right.fork()` puis deux `join()`, le thread courant enverrait les deux moitiés à d'autres, puis **attendrait sans rien faire** : un thread gaspillé à chaque niveau de découpe.

Vérifié sur un petit exemple : `ForkJoinPool(4).invoke(new Somme(0, 1_000_000))` donne `499999500000`, la même somme que la formule n(n−1)/2.
