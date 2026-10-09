# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`RetrierTest.java`](RetrierTest.java) et [`ReminderServiceTest.java`](ReminderServiceTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18** et **JUnit 5.11.4**, le 9 octobre 2026. Les tests de référence ont été lancés 20 fois de suite sans un échec : des tests de concurrence bien faits sont **stables**.

---

## Étape 1 — L'opérateur, derrière un port

Le code : [`Notification.java`](Notification.java), [`GatewayException.java`](GatewayException.java), [`SmsGateway.java`](SmsGateway.java), [`OperatorGateway.java`](OperatorGateway.java).

```java
@Override
public String send(String phone, String text) {
    try {
        return operator.deliver(phone, text);
    } catch (IllegalStateException e) {
        throw new GatewayException(e.getMessage(), true);
    } catch (IllegalArgumentException e) {
        throw new GatewayException(e.getMessage(), false);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new GatewayException("interrompu", false);
    }
}
```

**Question — le drapeau perdu :** `shutdownNow()` interrompt les fils pour leur dire « arrêtez-vous ». L'opérateur dormait, son `sleep` lance `InterruptedException` en **baissant** le drapeau. Si l'adaptateur avale l'exception sans relever le drapeau, le code au-dessus ne sait pas qu'on lui a demandé de s'arrêter : le `Retrier` attendrait puis réessaierait, le fil continuerait d'envoyer, et l'arrêt du programme traînerait (ou ne se ferait jamais, s'il y a beaucoup de travail en file). Ici, le `GatewayException` non réessayable arrête aussi le `Retrier` : les deux gestes se complètent.

---

## Étape 2 — Le recul exponentiel

Le code : [`Sleeper.java`](Sleeper.java), [`RetryPolicy.java`](RetryPolicy.java).

```java
public Duration delayAfter(int failures) {
    double millis = firstDelay.toMillis() * Math.pow(multiplier, failures - 1);
    return millis >= maxDelay.toMillis() ? maxDelay : Duration.ofMillis(Math.round(millis));
}
```

Avec un multiplicateur de 1,5 : 100, 150, 225, puis 337,5 arrondi à **338** (vérifié).

---

## Étape 3 — Réessayer

Le code : [`Retrier.java`](Retrier.java).

```java
public <T> T call(Supplier<T> action) {
    List<GatewayException> earlier = new ArrayList<>();
    for (int attempt = 1; ; attempt++) {
        try {
            return action.get();
        } catch (GatewayException e) {
            if (!e.retryable() || attempt == policy.maxAttempts()) {
                earlier.forEach(e::addSuppressed);
                throw e;
            }
            earlier.add(e);
        }
        pause(attempt, earlier);
    }
}
```

**Question — pas toutes les exceptions :** une `NullPointerException` est un **bug**, pas un incident passager : le même code avec les mêmes données échouera **pareil** au deuxième essai, et au dixième. Réessayer ne ferait que multiplier les attentes (ici 100 + 200 + 400 ms) avant de montrer la même erreur, et cacher le bug derrière des journaux pleins de nouveaux essais. On ne réessaie que ce qu'on **sait** passager.

---

## Étape 4 — Compter sans se bloquer

Le code : [`Metrics.java`](Metrics.java).

---

## Étape 5 — Le pool borné

Le code : [`SendResult.java`](SendResult.java), [`ReminderService.java`](ReminderService.java), méthodes `start`, `deliver`, `sendAll`.

```java
private CompletableFuture<SendResult> start(Notification n) {
    try {
        return CompletableFuture.supplyAsync(() -> deliver(n), executor)
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionally(e -> failure(n, e));
    } catch (RejectedExecutionException e) {
        metrics.rejected();
        return CompletableFuture.completedFuture(new SendResult(n.taskId(), "refuse", "file pleine"));
    }
}
```

**Question — attendre `started` :** le pool accepte un travail en le donnant à un **fil libre**, ou, si tous sont occupés, en le mettant **en file**. Juste après le premier `send`, le fil n'a peut-être pas encore **pris** le travail : il est encore dans la file. Les trois `send` suivants trouveraient alors une file avec une place de moins, et le **troisième** (pas le quatrième) serait refusé, une fois sur combien, selon la vitesse de la machine. `started.await()` garantit que le premier travail a quitté la file et occupe le fil : la suite est alors **certaine**. C'est toute la différence entre un test de concurrence stable et un test « qui passe en général ».

---

## Étape 6 — Le délai et les erreurs emballées

**L'emballage (vérifié) :** dans `exceptionally`, une erreur lancée par le travail arrive en `CompletionException`, avec la `GatewayException` pour cause ; un délai dépassé arrive directement en `TimeoutException` (cause `null`). D'où le déballage :

```java
Throwable cause = e instanceof CompletionException && e.getCause() != null ? e.getCause() : e;
```

**Question — redemander après un délai dépassé :** le premier envoi peut encore aboutir **après** le délai. Si l'appelant redemande, le client risque de recevoir **deux** SMS. L'idempotence (étape 7) y répond en partie : tant que le service tourne, la même tâche redemandée rend **le même futur**, et rien ne repart. Mais elle ne vit qu'en mémoire : après un redémarrage, ou avec deux serveurs, il faudrait une clé d'idempotence **chez l'opérateur** (beaucoup d'API de paiement et de messagerie acceptent un en-tête `Idempotency-Key` pour cela), ou une table en base.

---

## Étape 7 — L'idempotence

```java
public CompletableFuture<SendResult> send(Notification n) {
    CompletableFuture<SendResult> result = accepted.computeIfAbsent(n.taskId(), id -> start(n));
    if (result.isDone() && result.join().status().equals("refuse")) {
        accepted.remove(n.taskId(), result);
    }
    return result;
}
```

Le mutant 10 (sans `computeIfAbsent`) est tué par le test des deux demandes (vérifié) : les deux futurs ne sont plus le même objet, et chacun envoie son SMS.

---

## Étape 8 — L'arrêt propre, la démonstration, et les mutants

```java
public int shutdown(Duration grace) throws InterruptedException {
    executor.shutdown();
    if (executor.awaitTermination(grace.toMillis(), TimeUnit.MILLISECONDS)) {
        return 0;
    }
    return executor.shutdownNow().size();
}
```

Les deux rappels jamais commencés ne sont pas « perdus en silence » : leurs futurs se terminent par le **délai** (`delai depasse`). Sans `orTimeout`, ils ne se termineraient jamais, et un `join()` attendrait pour toujours.

**La sortie de `ReminderDemo` (vérifiée) :**

```
tache 1 : envoye
tache 2 : envoye
tache 3 : envoye
tache 4 : envoye
tache 5 : echec (numero inconnu : 0512345674)
tache 6 : envoye
tache 7 : echec (operateur occupe)
tache 8 : envoye
tache 9 : envoye
tache 10 : envoye
tache 11 : envoye
tache 12 : envoye
duree : 1224 ms
Snapshot[sent=10, retries=9, failed=2, rejected=0, timedOut=0]
jamais commences : 0
```

La durée varie un peu d'un lancement à l'autre (1 202 ms au second) ; tout le reste est identique.

**Expérience — le nombre de fils (vérifié) :**

| Fils | Durée |
|---|---|
| 1 | 2 869 ms |
| 3 | 1 192 ms |
| 12 | 963 ms |

Avec 12 fils, chaque rappel a son fil, et la durée est celle du **plus lent** : la tâche 7 (« 06…7 », toujours occupée) fait 4 essais de 50 ms et attend 100 + 200 + 400 ms entre eux, soit environ 900 ms. Ajouter des fils ne raccourcit pas le travail le plus long : c'est la **loi d'Amdahl** en petit. Et contre un vrai opérateur, 12 fils voudraient dire 12 connexions simultanées : il faut aussi respecter **ses** limites.

**Expérience — sans attente (vérifié) :** 500 ms au lieu de 1 192, **les mêmes résultats** (les mêmes 9 nouveaux essais). Ici, l'opérateur imité redevient disponible à l'essai suivant quoi qu'il arrive. Un vrai service saturé, lui, ne se remet que si on le laisse **respirer** : des nouveaux essais immédiats, multipliés par tous les clients, l'empêchent de se rétablir. Le recul exponentiel paie sa lenteur en **stabilité** du système entier.

**Les mutants :** avec les tests de référence, les **20 sont tués** (vérifié). Le tableau de l'indice 2 dit ce que change chacun.
