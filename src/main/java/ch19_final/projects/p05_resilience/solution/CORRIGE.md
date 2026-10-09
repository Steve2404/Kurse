# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`RateLimiterTest.java`](RateLimiterTest.java), [`CircuitBreakerTest.java`](CircuitBreakerTest.java) et [`ResilientStockTest.java`](ResilientStockTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18** et **JUnit 5.11.4**, le 9 octobre 2026. Avec l'horloge manuelle, elles sont **identiques** à chaque lancement, sur toutes les machines.

---

## Étape 1 — L'horloge manuelle

Le code : [`ManualClock.java`](ManualClock.java).

**Question — dans le code du projet :** parce que la **démonstration** s'en sert aussi : elle rejoue 20 secondes de panne en un instant, toujours de la même façon. Plus largement, une horloge manuelle sert à **simuler** : rejouer une journée de trafic, vérifier une facturation au changement d'heure, tester un traitement de nuit à 15 h. Ce n'est pas qu'un outil de test.

---

## Étape 2 — Le seau à jetons

Le code : [`TokenBucket.java`](TokenBucket.java).

```java
private void refill() {
    Instant now = clock.instant();
    long elapsedMillis = Duration.between(last, now).toMillis();
    if (elapsedMillis > 0) {
        milliTokens = Math.min(capacity, milliTokens + elapsedMillis * perSecond);
        last = last.plusMillis(elapsedMillis);
    }
}
```

**Expérience — dix fois `0.1` (vérifié) :** `0.9999999999999999`, et `>= 1` est **faux**. `0.1` n'a pas d'écriture exacte en binaire (comme 1/3 en décimal), et les petites erreurs s'additionnent. Avec des `double`, le client attendrait une milliseconde de plus que prévu, ou serait refusé, selon les pas.

**Le reste de milliseconde :** avec `last = now` (le mutant 2), chaque pas de 1,5 ms ne compte que 1 ms : il faudrait 1 000 pas au lieu de **667** pour retrouver un jeton. Le test des pas de 1,5 ms tue bien ce mutant (vérifié). En gardant `last = last.plusMillis(…)`, la demi-milliseconde non comptée reste « en attente » pour le pas suivant : 667 pas font 1 000,5 ms, dont 1 000 comptées.

---

## Étape 3 — Un seau par client

Le code : [`RateLimiter.java`](RateLimiter.java).

**Question — qui appelle `evictFull` :** une tâche de **ménage** périodique, par exemple toutes les minutes, sur un `ScheduledExecutorService` (chapitre 13) : `scheduler.scheduleAtFixedRate(limiter::evictFull, 1, 1, TimeUnit.MINUTES)`. Pas à chaque requête : parcourir des milliers de seaux à chaque appel coûterait plus cher que la limitation elle-même. Un seau n'est plein qu'après `capacity / perSecond` secondes d'inactivité : le ménage n'oublie que les clients **partis**.

---

## Étape 4 — Le disjoncteur

Le code : [`CircuitOpenException.java`](CircuitOpenException.java), [`CircuitBreaker.java`](CircuitBreaker.java).

```java
public <T> T call(Supplier<T> action) {
    beforeCall();
    try {
        T result = action.get();
        onSuccess();
        return result;
    } catch (RuntimeException e) {
        if (isFailure.test(e)) {
            onFailure();
        } else {
            onSuccess();
        }
        throw e;
    }
}
```

**Question — l'arrondi au-dessus :** 300 ms avant la fin, l'arrondi en dessous dirait « réessayer dans 0 s » : l'utilisateur (ou un programme client) réessaierait aussitôt et serait **encore** refusé. Arrondi au-dessus, « 1 s » est une promesse tenue : à ce moment-là, l'essai sera permis. Même raison pour l'en-tête `Retry-After` d'un 429 ou d'un 503 : mieux vaut attendre un peu trop que pas assez.

---

## Étape 5 — Un seul essai à la fois

```java
private synchronized void beforeCall() {
    if (state == State.OPEN) {
        Instant reopen = openedAt.plus(openFor);
        if (clock.instant().isBefore(reopen)) {
            long seconds = (Duration.between(clock.instant(), reopen).toMillis() + 999) / 1000;
            throw new CircuitOpenException("circuit ouvert : reessayer dans " + seconds + " s");
        }
        state = State.HALF_OPEN;
    }
    if (state == State.HALF_OPEN) {
        if (trialRunning) {
            throw new CircuitOpenException("circuit ouvert : essai en cours");
        }
        trialRunning = true;
    }
}
```

C'est une **machine à états** (chapitre 18) en version compacte : une `enum` et des transitions écrites à un seul endroit. Avec trois états et peu de comportements différents, une `enum` suffit ; le patron État (une classe par état) devient utile quand chaque état a beaucoup de comportements propres.

---

## Étape 6 — Les percentiles

Le code : [`LatencyRecorder.java`](LatencyRecorder.java).

**La moyenne qui ment (vérifié) :** 99 mesures à 10 ms et une à 5 000 ms : moyenne **59,9 ms** ; `summary()` donne `n=100 p50=10 ms p95=10 ms p99=10 ms max=5000 ms`. Le p99 vaut encore 10 ms : il faut **plus d'une** mesure lente sur cent pour qu'il bouge. C'est pourquoi on regarde p99 **et** max.

---

## Étape 7 — Le stock protégé

Le code : [`StockClient.java`](StockClient.java), [`StockAnswer.java`](StockAnswer.java), [`StockUnavailableException.java`](StockUnavailableException.java), [`ResilientStock.java`](ResilientStock.java).

```java
public StockAnswer stock(String ref) {
    Instant start = clock.instant();
    try {
        int quantity = breaker.call(() -> supplier.stock(ref));
        lastKnown.put(ref, new Known(quantity, clock.instant()));
        return new StockAnswer(ref, quantity, "fournisseur");
    } catch (IllegalArgumentException e) {
        throw e;
    } catch (RuntimeException e) {
        return fallback(ref, e);
    } finally {
        latency.record(Duration.between(start, clock.instant()));
    }
}
```

**Expérience — avec et sans disjoncteur (vérifié), 10 demandes pendant une panne :**

| Seuil | Appels au fournisseur | Durée totale | Latences |
|---|---|---|---|
| 2 | 2 | 4 s | `n=11 p50=0 ms p95=2000 ms p99=2000 ms max=2000 ms` |
| jamais ouvert | 10 | 20 s | `n=11 p50=2000 ms p95=2000 ms p99=2000 ms max=2000 ms` |

Avec le disjoncteur, deux utilisateurs attendent 2 secondes ; les huit suivants ont une réponse (du cache) **instantanée**, et le fournisseur n'est pas dérangé pendant qu'il redémarre. Sans lui, **tout le monde** attend 2 secondes pour rien, et le fournisseur reçoit 10 appels de plus.

**Question — l'essai de 10:00:11 :** le disjoncteur s'est ouvert à 10:00:07 (3e échec), pour 4 s. À 10:00:11, le délai est passé : demi-ouvert, et l'essai part… mais la panne dure jusqu'à 10:00:12, donc il échoue. Un essai raté **rouvre** le circuit pour un nouveau délai **entier** de 4 s, jusqu'à 10:00:15, même si le fournisseur va mieux dès 10:00:12. C'est le prix de la prudence : quelques secondes de cache de trop, plutôt que de submerger un service qui redémarre. D'où la suite `FFFFCCCCCCCCCCFFFFFF` et 14 appels (4 + 3 échecs + 1 essai raté + 6).

---

## Étape 8 — La démonstration, et les mutants

Le code : [`ResilienceDemo.java`](ResilienceDemo.java).

**La sortie (vérifiée) :**

```
10:00:01.000 CHAIN-11 = 14 (fournisseur) [CLOSED]
10:00:02.030 CHAIN-11 = 14 (fournisseur) [CLOSED]
10:00:03.060 CHAIN-11 = 14 (fournisseur) [CLOSED]
10:00:04.090 CHAIN-11 = 14 (fournisseur) [CLOSED]
10:00:05.120 CHAIN-11 = 14 (cache (3 s)) [CLOSED]
10:00:08.120 CHAIN-11 = 14 (cache (6 s)) [CLOSED]
10:00:11.120 CHAIN-11 = 14 (cache (9 s)) [OPEN]
10:00:14.120 CHAIN-11 = 14 (cache (10 s)) [OPEN]
10:00:15.120 CHAIN-11 = 14 (cache (11 s)) [OPEN]
10:00:16.120 CHAIN-11 = 14 (cache (12 s)) [OPEN]
10:00:17.120 CHAIN-11 = 14 (fournisseur) [CLOSED]
10:00:18.150 CHAIN-11 = 14 (fournisseur) [CLOSED]
…
10:00:26.390 CHAIN-11 = 14 (fournisseur) [CLOSED]
appels au fournisseur : 17 ; n=20 p50=30 ms p95=2000 ms p99=2000 ms max=2000 ms
requete 1 : PT0S
…
requete 5 : PT0S
requete 6 : PT1S
requete 7 : PT1S
requete 8 : PT1S
```

**Expérience — les heures :** l'horloge est **partagée** : chaque réponse réussie la fait avancer de 30 ms (d'où .030, .060…), et chaque échec de **2 secondes**. La demande de 10:00:05.120 tombe en panne et dure 2 s : elle se termine à 10:00:07.120. La boucle avance alors d'une seconde : la demande suivante part à 10:00:08.120. C'est exactement ce que vit un utilisateur : pendant une panne lente, chaque demande lui coûte 2 s d'attente. À 10:00:11.120, le 3e échec ouvre le disjoncteur (il affiche `OPEN` après la réponse) ; ensuite, les demandes de 14, 15 et 16 sont servies par le cache **sans attente** (les heures ne sautent plus). À 10:00:17.120, l'essai réussit (la panne a fini à 10:00:12) : `CLOSED`. Le limiteur, lui, laisse passer la rafale de 5, puis demande d'attendre 1 s (`PT1S`, la notation ISO 8601 d'une `Duration`) : à cet instant de l'horloge, aucun jeton n'est revenu.

**Les mutants :** avec les tests de référence, les **20 sont tués** (vérifié). Le tableau de l'indice 2 dit ce que change chacun.
