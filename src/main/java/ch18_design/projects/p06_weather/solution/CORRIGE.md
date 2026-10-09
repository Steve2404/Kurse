# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`WeatherTest.java`](WeatherTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — L'adaptateur

La sortie de `Data` (vérifiée) :

```
appel 1 : station injoignable
appel 2 : station injoignable
appel 3 : PAR = 64.4 F
appels recus : 3
```

Le code : [`Forecast.java`](Forecast.java), [`WeatherService.java`](WeatherService.java), [`MeteoAdapter.java`](MeteoAdapter.java). Les valeurs (vérifiées) : Paris **18**, Lyon **22**, Brest **13**, Nice **−12**.

**Question — `(int)` au lieu de `Math.round` :** Lyon : (71 − 32) × 5 / 9 = 21,67 ; `(int)` donne **21**, `Math.round` donne **22**. Nice : −11,67 ; `(int)` donne **−11**, `Math.round` donne **−12**. Nice est à part parce que la conversion `(int)` **tronque vers zéro** : pour un nombre négatif, elle arrondit donc **vers le haut**. Un test qui n'aurait que des températures positives ne verrait pas ce cas.

---

## Étape 2 — Le proxy

Le code : [`CachingWeather.java`](CachingWeather.java). Les tests : `cacheKeepsAForecastStrictlyLessThanItsLifetime`, `cacheIsPerCityAndNeverKeepsAnError`, avec les doublures `ScriptedWeather` et `MutableClock`.

**Question — la même interface :** le code appelant ne voit **aucune** différence entre le service et son proxy : il reçoit un `WeatherService`. On peut donc ajouter ou retirer le cache **sans toucher** à une seule ligne de l'appelant (seulement là où l'on assemble, dans `Weather`). Si le cache avait une autre interface (`getCached(city)`), chaque appelant devrait savoir qu'il existe et changer le jour où on l'enlève.

---

## Étape 3 — Les décorateurs

Le code : [`RetryingWeather.java`](RetryingWeather.java), [`LoggingWeather.java`](LoggingWeather.java). Les tests : `retrySucceedsWithinItsAttempts`, `retryGivesUpWithTheLastError`, `loggingNotesAndPassesThrough`.

**Question — plus d'information :** les exceptions **précédentes** : la deuxième panne cache la première, qui était peut-être différente (« délai dépassé » puis « station inconnue »). On pourrait les garder comme exceptions supprimées, exactement comme `FallbackWeather` à l'étape 4 : `last.addSuppressed(…)` pour chaque échec d'avant. Et le **nombre** de tentatives faites, dans le message.

---

## Étape 4 — Le secours et la moyenne

Le code : [`FallbackWeather.java`](FallbackWeather.java), [`AverageWeather.java`](AverageWeather.java). Les tests : `fallbackTriesInOrderAndStopsAtTheFirstAnswer`, `fallbackWithoutAnswerKeepsEveryCause`, `averageOfTheSourcesThatAnswer`, `compositesNest`.

**Question — le composite imbriqué :** le résultat est **23** (vérifié) : la moyenne intérieure vaut 15, puis (15 + 30) / 2 = 22,5, arrondi à 23. La moyenne de 10, 20 et 30 serait 20. Ce n'est pas un bug, c'est la **définition** du composite : chaque nœud se présente comme **un seul** service, donc le groupe intérieur pèse autant que la source 30 toute seule. Si l'on veut que chaque source compte pareil, il ne faut pas imbriquer, ou bien donner un **poids** à chaque réponse. Un composite simplifie l'usage, mais il faut savoir ce qu'il calcule.

---

## Étape 5 — Tout emboîter

Le code : [`Weather.java`](Weather.java). Les tests : `standardStackRetriesCachesAndLogsEveryRequest`, `standardStackFallsBackAfterThreeAttempts`.

**Question — les tentatives autour du secours** (vérifié, avec un fournisseur qui a 2 pannes) : `Forecast[city=Paris, celsius=20, source=secours]`, après **1 seul** appel au fournisseur. Au premier échec de l'adaptateur, le secours répond **tout de suite** : le service emboîté réussit, donc les tentatives n'ont jamais l'occasion de réessayer. On sert les prévisions du secours alors que le fournisseur aurait répondu au troisième essai. Dans le bon ordre, les tentatives insistent d'abord sur le fournisseur, et le secours ne sert qu'en dernier recours.

**Question — le journal dans le cache** (vérifié) : le journal n'a plus qu'**une** ligne, `[Paris -> 18 C (meteo)]` : la seconde demande est servie par le cache, qui ne va plus jusqu'au journal. Pour un journal des **demandes** (combien de fois les visiteurs demandent la météo), il faut le mettre **dehors** : c'est notre choix. Pour un journal des **coûts** (combien de fois on paie le fournisseur), il faudrait le mettre **dedans**, juste autour de l'adaptateur. Les deux sont justes : l'ordre exprime **ce qu'on veut mesurer**.

---

## Étape 6 — Les mutants

Les 17 mutants sont tués par les 17 tests de référence. Les deux tests d'intégration en attrapent **8** les premiers (vérifié dans la sortie de `Check solution`) : `standardStackFallsBackAfterThreeAttempts` les mutants 6, 9, 10, 16 et 17, `standardStackRetriesCachesAndLogsEveryRequest` les mutants 1, 4 et 15. Un bon test d'assemblage vérifie à la fois les pièces **et** leur ordre ; les tests unitaires, eux, disent **quelle** pièce est fausse.
