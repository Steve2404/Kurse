# Projet 3 — La banque concurrente (`synchronized`, atomiques, verrous)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 13) :**
- **la course (*race condition*)** : `x++` n'est pas atomique ;
- **`synchronized`** : méthode, ou bloc `synchronized (objet)` (même verrou que les méthodes de l'objet) ;
- **les classes atomiques** : `AtomicInteger` et `AtomicLong`, avec `incrementAndGet`, `getAndIncrement`, `addAndGet`, `compareAndSet`, `updateAndGet`, `accumulateAndGet` ;
- **`ReentrantLock`** :
  - `lock()`, puis `unlock()` dans un `finally` ;
  - `tryLock(délai)` ;
  - la réentrance : `getHoldCount()`, `isHeldByCurrentThread()`, `isLocked()` ;
  - l'équité : `new ReentrantLock(true)` ;
  - `IllegalMonitorStateException` ;
- **l'interblocage (*deadlock*)**, évité de deux façons : un **ordre global** des verrous, ou **`tryLock` avec délai** ;
- **`volatile`** ;
- **`CyclicBarrier`**, pour synchroniser deux threads au bon moment.

Côté algorithme : 20 000 virements exécutés par 8 threads. Les soldes finaux doivent être **identiques** à l'application séquentielle, et la **somme totale conservée**. Les montants ne sont jamais refusés (découvert permis) : le résultat ne dépend donc pas de l'ordre.

**Ce que TU crées :** dans `ch13_concurrency.projects.p03_bank` :
- les classes `Account`, `OperationCounter` et `Bank` ;
- **`BankLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

---

## Tableau de bord

### ☐ Étape 1 — Comptes, compteur, banque

- **`Account`** : `id`, un `ReentrantLock` (champ `lock`, et la méthode `Lock lock()`), et `long balance`.
  - `void add(long)` est appelée **verrou tenu** ;
  - `long balance()` prend le verrou pour lire.
- **`OperationCounter`** :
  - `public synchronized void transfer()` fait `transfers++` ;
  - `String summary()` utilise un **bloc** `synchronized (this)` et rend `<n> virements`.
- **`Bank(int n, long initial)`** :
  - **`transfer(int from, int to, long amount, long fee)`** :
    1. verrouille **d'abord** le compte de plus petit id, **puis** l'autre ;
    2. la source perd `amount + fee`, la cible gagne `amount` ;
    3. déverrouille dans des `finally` imbriqués ;
    4. puis `counter.transfer()`, `fees.addAndGet(fee)` et `biggest.accumulateAndGet(amount, Math::max)` (des `AtomicLong`) ;
  - `boolean tryTransfer(Account a, Account b, long amount, long waitMs)` : `lock` de a, puis `tryLock(waitMs, MILLISECONDS)` de b. Si l'essai rate, rend `false` ;
  - `total()` : la somme des soldes **plus** les frais ; `balances()` ;
  - `stats()` rend `<summary>, frais <f>, plus gros virement <m>`.
- **Question :** pourquoi l'ordre « plus petit id d'abord » rend-il l'interblocage impossible ?

### ☐ Étape 2 — Les virements concurrents

```
soldes [102097, 355497, -169497, 349086, -177365, 80182] ; identiques au sequentiel true
conservation : total 600000 = 600000 true ; 20000 virements, frais 60000, plus gros virement 999
```
- `Data.ACCOUNTS` comptes à `Data.INITIAL`, et un pool de `Data.THREADS` threads.
- Soumets `Data.TRANSFERS` tâches, la i-ème faisant `bank.transfer` avec `Data.transfer(i)` (`{source, cible, montant}`) et `Data.FEE`. Attends tous les `Future`, puis `shutdown`.
- **Le séquentiel :** recalcule les soldes attendus dans un `long[]`, sans thread. Compare avec `Arrays.stream(…).boxed().toList()`.
- **Expérience :** enlève les verrous de `transfer`. Lance plusieurs fois : que deviennent les soldes, le total, et le nombre de virements si tu remplaces aussi `synchronized` par rien ?

### ☐ Étape 3 — L'interblocage évité

```
interblocage evite : A abandonne, B reussit ; virement 2 -> 3 par tryTransfer true
```
- Une `CyclicBarrier(2)`, et un pool de 2 threads :
  - la tâche **A** verrouille le compte 0, attend la barrière, puis `tryLock(100 ms)` du compte 1 ;
  - la tâche **B** verrouille le compte 1, attend la barrière, puis `tryLock(5 s)` du compte 0.
  - Chacune rend `A reussit` ou `A abandonne` (`B …`), et relâche tout dans des `finally`.
- Puis `bank.tryTransfer(compte 2, compte 3, 500, 50)`.
- **Question :** pourquoi la barrière garantit-elle que chacun tient **déjà** son premier verrou ? Sans délai, que se passerait-il ?

### ☐ Étape 4 — Réentrance, `volatile`, atomiques

```
reentrance : getHoldCount 2, isHeldByCurrentThread true, isLocked apres 2 unlock false, unlock de trop IllegalMonitorStateException, equitable true
volatile : boucle arretee apres 1000 tours ; getAndIncrement 1000 puis 1001, compareAndSet(1001, 0) true -> 0, updateAndGet 10
```
- **La réentrance :**
  1. un `ReentrantLock` verrouillé deux fois ;
  2. note `getHoldCount()` et `isHeldByCurrentThread()` ;
  3. deux `unlock()`, puis `isLocked()` ;
  4. un troisième `unlock()` dans un `try` ;
  5. enfin `new ReentrantLock(true).isFair()`.
- **`volatile`** : un champ `static volatile boolean running = true`. Un thread boucle tant que `running`. Il incrémente un `AtomicInteger laps`, et met `running = false` à 1 000.
  - Après `join()`, affiche `laps.get()`, `getAndIncrement()`, `get()`, `compareAndSet(1001, 0)`, `get()`, puis `updateAndGet(v -> v + 10)`.
- **Questions :**
  - Sans `volatile`, la boucle pourrait-elle ne jamais s'arrêter ?
  - `volatile` rend-il `running++` atomique ?

---

## Checklist (vérifiée par `Check`)

- `Data.ACCOUNTS`, `Data.TRANSFERS`, `Data.transfer(`, `Data.FEE` ;
- `new ReentrantLock()`, `.lock()`, `.unlock()`, `public synchronized void`, `synchronized (this)` ;
- `AtomicLong`, `.addAndGet(`, `.accumulateAndGet(`, `Math.min(` ;
- `.tryLock(`, `TimeUnit.MILLISECONDS`, `CyclicBarrier` ;
- `.getHoldCount()`, `.isHeldByCurrentThread()`, `.isLocked()`, `catch (IllegalMonitorStateException`, `new ReentrantLock(true)`, `.isFair()` ;
- `static volatile boolean`, `AtomicInteger`, `.incrementAndGet()`, `.getAndIncrement()`, `.compareAndSet(`, `.updateAndGet(` ;
- au moins 3 `finally`.

---

## Sortie attendue complète

```
soldes [102097, 355497, -169497, 349086, -177365, 80182] ; identiques au sequentiel true
conservation : total 600000 = 600000 true ; 20000 virements, frais 60000, plus gros virement 999
interblocage evite : A abandonne, B reussit ; virement 2 -> 3 par tryTransfer true
reentrance : getHoldCount 2, isHeldByCurrentThread true, isLocked apres 2 unlock false, unlock de trop IllegalMonitorStateException, equitable true
volatile : boucle arretee apres 1000 tours ; getAndIncrement 1000 puis 1001, compareAndSet(1001, 0) true -> 0, updateAndGet 10
```
