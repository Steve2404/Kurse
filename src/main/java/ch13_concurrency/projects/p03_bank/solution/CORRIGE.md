# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur la solution et sur des copies modifiées, lancées plusieurs fois.

---

## Étape 1 — Comptes, compteur, banque

**Le virement** (`Bank.transfer`) :

```java
Account first = accounts.get(Math.min(from, to));
Account second = accounts.get(Math.max(from, to));
first.lock().lock();
try {
    second.lock().lock();
    try {
        accounts.get(from).add(-amount - fee);
        accounts.get(to).add(amount);
    } finally {
        second.lock().unlock();
    }
} finally {
    first.lock().unlock();
}
```

**Question — pourquoi « le plus petit id d'abord » empêche-t-il l'interblocage ?** Un interblocage a besoin d'un **cycle** : A tient 1 et attend 2, pendant que B tient 2 et attend 1. Si **tous** les threads prennent les verrous dans le **même ordre** (le plus petit id d'abord), personne ne peut tenir 2 en attendant 1 : le cycle est impossible.

---

## Étape 2 — Les virements concurrents

**Expérience — sans les verrous de `transfer`** (vérifié, 3 lancements) : les soldes changent à chaque fois, et **l'argent n'est plus conservé** :

```
total 598267 = 600000 false
total 616026 = 600000 false
total 602706 = 600000 false
```

Deux threads lisent le même solde en même temps, chacun ajoute son montant, et l'une des deux écritures **écrase** l'autre : une mise à jour est perdue.

**En retirant aussi `synchronized` du compteur** (vérifié, 3 lancements) : le nombre de virements tombe sous 20 000 (`19587`, `19645`, `19397`). `transfers++` n'est **pas** atomique : c'est lire, ajouter 1, puis écrire, et deux threads peuvent lire la même valeur. Les frais (60000) restent justes : `addAndGet` est atomique.

---

## Étape 3 — L'interblocage évité

**Question — la barrière :** `await()` ne rend la main qu'aux deux tâches **ensemble**. Chacune a pris son 1er verrou **avant** `await()` : à la sortie de la barrière, A tient le compte 0 et B le compte 1, à coup sûr.

**Sans délai** (`lock()` au lieu de `tryLock`), A attendrait le compte 1 tenu par B, et B le compte 0 tenu par A : un **interblocage**. Les deux threads attendraient pour toujours, et le programme ne se terminerait jamais. Avec `tryLock(100 ms)`, A abandonne, relâche le compte 0, et B réussit.

---

## Étape 4 — Réentrance, `volatile`, atomiques

**La réentrance :** pris deux fois, `getHoldCount()` vaut 2 ; il faut deux `unlock()` pour le libérer (`isLocked()` vaut alors `false`). Un 3e `unlock()` lance une `IllegalMonitorStateException` : le thread ne le tient plus. `new ReentrantLock(true)` crée un verrou **équitable** : les threads l'obtiennent dans l'ordre d'arrivée.

**Question — sans `volatile`, la boucle pourrait-elle ne jamais s'arrêter ?** Oui. Chaque thread peut garder une **copie** d'un champ dans le cache de son processeur. Sans `volatile`, rien n'oblige le thread de la boucle à relire `running` en mémoire : il pourrait voir `true` pour toujours. `volatile` garantit que chaque lecture voit la dernière écriture.

**Question — `volatile` rend-il `running++` atomique ?** Non. `volatile` garantit la **visibilité**, pas l'**atomicité** : `x++` reste trois opérations (lire, ajouter, écrire). Pour un compteur partagé, il faut un `AtomicInteger` ou un verrou (voir l'expérience de l'étape 2).

**Les atomiques :** `getAndIncrement()` rend l'**ancienne** valeur (1000) puis l'augmente (1001) ; `compareAndSet(1001, 0)` réussit car la valeur vaut bien 1001 ; `updateAndGet(v -> v + 10)` rend la **nouvelle** valeur, 10.
