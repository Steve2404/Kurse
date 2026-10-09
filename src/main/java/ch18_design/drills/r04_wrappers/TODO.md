# Drill de rappel 4 — Adaptateur, proxy, décorateurs, composite

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p06.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch18_design.drills.r04_wrappers`.
- `Data.OldExchange` (fourni) est la bourse d'un fournisseur : `double quote(String lowerCaseTicker)` en **euros**, avec des symboles en **minuscules** ; ses premiers appels peuvent échouer (`bourse injoignable`).
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Ni `extends`, ni `instanceof`. Seul l'adaptateur nomme `OldExchange`.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 6 (étapes 1 à 5).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée les fichiers dans l'ordre des défis, `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@FunctionalInterface public interface PriceFeed` avec `long price(String symbol)` (symbole en majuscules, prix en centimes), et l'adaptateur `public final class ExchangeAdapter implements PriceFeed`, construit avec un `Data.OldExchange` : symbole en minuscules pour le fournisseur, euros **arrondis** en centimes (`Math.round`) ; les erreurs du fournisseur passent telles quelles.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Le proxy `public final class CachedFeed implements PriceFeed`, construit avec un `PriceFeed` : garde chaque prix par symbole (pour toujours), ne garde **pas** une erreur ; `int misses()` compte les appels transmis.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** Le décorateur `public final class RetryFeed implements PriceFeed`, construit avec `(PriceFeed inner, int attempts)` : au plus `attempts` appels ; après le dernier échec, relance la **dernière** exception.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** Le composite `public final class BestFeed implements PriceFeed`, construit avec une `List<PriceFeed>` : le prix le **plus bas** parmi les sources qui répondent ; aucune : `IllegalStateException("aucune source pour " + symbol)`.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Le décorateur `public final class LoggedFeed implements PriceFeed`, construit avec `(PriceFeed inner, List<String> log)` : note `"ACME = 1235"` ou `"ACME : erreur"`, puis rend ou relance **la même** exception.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** L'emboîtement `LoggedFeed(CachedFeed(RetryFeed(ExchangeAdapter, 2)))` : le test vérifie le nombre d'appels au fournisseur et le journal.
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Adaptateur** : implémente **notre** interface, traduit vers l'autre : `Math.round(exchange.quote(symbol.toLowerCase()) * 100)`.
- **Proxy** : même interface, contrôle l'accès ; `Map<String, Long>` ; on range **après** un appel réussi (une exception saute la ligne `put`).
- **Décorateur** : même interface **et** un champ `private final PriceFeed inner` ; ajoute son comportement, puis délègue. Tentatives : `try { return inner.price(s); } catch (RuntimeException e) { last = e; }` dans une boucle, puis `throw last;`.
- **Composite** : une liste de sources derrière la même interface. Le minimum sans exceptions dans le flux : une méthode privée qui rend −1 pour une source en panne, puis `filter(p -> p >= 0).min()` et `orElseThrow(…)`.
- **L'ordre compte** : le journal **dehors** voit chaque demande ; le cache **dedans** évite les rappels ; les tentatives **au fond** insistent sur le fournisseur.

</details>
