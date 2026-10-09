# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`ReviewTest.java`](ReviewTest.java), et une revue écrite modèle dans [`REVIEW.md`](REVIEW.md).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18** et **JUnit 5.11.4**, le 9 octobre 2026.

---

## Étape 1 — Relire comme un senior

**La sortie de `Data` (vérifiée) :**

```
total depense : 0.30000000000000004 euros
meme client ? true ; dans un HashSet : 2 client(s)
code tape par le client : 50 points
code ecrit dans le code : 100 points
import : 1 achat(s) lus sur 3 lignes
Customer[C2, Bob, bob.martin@example.org]
une autre instance du service voit-elle C1 ? true
palier avec 1000 points : SILVER (le cahier des charges dit GOLD des 1000)
```

**Question — combien de défauts :** la revue de référence en relève **17**, plus deux suggestions : voir [`REVIEW.md`](REVIEW.md). Les plus discrets sont ceux que la démo ne montre pas : les points bonus jamais crédités (`record` les rend, `balance` les ignore), la double inscription qui efface l'historique, `Math.round` qui donne 10 points pour 9,99 €, les achats datés du futur.

**Question — pourquoi ça passe une démonstration :** une démo suit **un** chemin, avec des valeurs **choisies**, dans **un** fil, une fois. Les littéraux `"DOUBLE"` sont le même objet (`==` marche) ; les montants ronds ne montrent pas l'arrondi ; personne ne teste 1 000 points pile ni un 29 février ; le fichier de démo est bien formé ; un seul fil ne perd rien ; une seule instance ne voit pas le partage `static` ; et le jour de la démo, aucun point n'a encore expiré. Chaque défaut a besoin d'une **condition** que la démo ne réunit pas : c'est exactement ce que les tests doivent fabriquer.

---

## Étape 2 — L'égalité, l'argent, les paliers

Le code : [`Customer.java`](Customer.java), [`Purchase.java`](Purchase.java), [`Tier.java`](Tier.java).

```java
public static Tier of(int points) {
    if (points >= 1000) {
        return GOLD;
    }
    return points >= 300 ? SILVER : BRONZE;
}
```

---

## Étape 3 — Le service

Le code : [`LoyaltyService.java`](LoyaltyService.java).

```java
public int record(Purchase purchase) {
    String id = purchase.customerId();
    requireKnown(id);
    if (purchase.date().isAfter(LocalDate.now(clock))) {
        throw new IllegalArgumentException("achat dans le futur : " + purchase.date());
    }
    int points = (int) (purchase.cents() / 100);
    if (tier(id) == Tier.GOLD) {
        points *= 2;
    }
    if ("DOUBLE".equals(purchase.promoCode())) {
        points *= 2;
    }
    purchases.get(id).add(purchase);
    batches.get(id).add(new Batch(points, purchase.date()));
    return points;
}
```

`tier(id) == Tier.GOLD` est juste : une valeur d'`enum` n'existe qu'en **un** exemplaire, `==` est même la façon recommandée de les comparer.

**Question — l'horloge en 2025 :** pour qu'un `LocalDate.now()` oublié **se voie**. Si l'horloge des tests était réglée sur aujourd'hui, un code qui lit la vraie date donnerait le même résultat que le code juste, et le test passerait : il ne prouverait rien. Avec une horloge en janvier 2025, la vraie date (octobre 2026) a fait expirer tous les points de 2024 : le mutant 7 échoue aussitôt (vérifié). Règle générale : une valeur de test **différente** de la valeur par défaut.

---

## Étape 4 — L'import

Le code : [`PurchaseImporter.java`](PurchaseImporter.java).

```java
private static long cents(String text, int number) {
    try {
        long cents = new BigDecimal(text).movePointRight(2).longValueExact();
        if (cents < 0) {
            throw new ArithmeticException();
        }
        return cents;
    } catch (NumberFormatException | ArithmeticException e) {
        throw new IllegalArgumentException("ligne " + number + " : montant invalide : " + text);
    }
}
```

**Expérience — `double` contre `BigDecimal` (vérifié) :** `(long) (Double.parseDouble("19.99") * 100)` donne **1998**, et `(long) (Double.parseDouble("0.29") * 100)` donne **28** : un centime perdu à chaque fois. `19.99` n'a pas d'écriture exacte en binaire ; multiplié par 100, il donne `1998.9999999999998`, que la conversion en `long` **tronque**. Avec `BigDecimal`, qui lit le texte en décimal : 1999 et 29.

---

## Étape 5 — Plusieurs fils

**Expérience — une `ArrayList` partagée (vérifiée) :** sur **30** lancements du test des 8 fils, **30 échecs**. Les échecs changent d'un lancement à l'autre : le plus souvent des achats **perdus** (`expected: <4000> but was: <3878>`, `<3970>`…), parfois une `ArrayIndexOutOfBoundsException` ou une `NullPointerException`. Deux ajouts simultanés écrivent dans la même case du tableau interne, ou l'un écrit pendant que l'autre remplace le tableau par un plus grand. Ici, l'erreur se montre à chaque fois parce que 4 000 ajouts se bousculent ; avec 10 ajouts, elle pourrait ne se montrer qu'une fois sur mille (projet 3).

**Question — pourquoi pas le solde :** le palier GOLD dépend du solde **au moment** de chaque achat. Avec 8 fils, l'ordre des achats change à chaque lancement : selon le moment où le solde franchit 1 000 points, un nombre différent d'achats est doublé. Le solde final n'est donc pas **déterminé**, même avec un code parfait : un test sur lui échouerait au hasard. Le nombre d'achats et le total dépensé, eux, ne dépendent pas de l'ordre. Choisir ce qu'on vérifie dans un test de concurrence, c'est choisir une valeur qui ne dépend pas de l'ordre.

---

## Étape 6 — La démonstration, et les mutants

Le code : [`ReviewDemo.java`](ReviewDemo.java).

**La sortie de `ReviewDemo` (vérifiée) :**

```
total depense : 30 centimes
dans un HashSet : 1 client(s)
code tape par le client : 100 points
import refuse : ligne 2 : montant invalide : dix
Customer[C2, Bob, b***@example.org]
palier avec 1000 points : GOLD
```

**Les mutants :** avec les tests de référence, les **16 sont tués** (vérifié). Chacun remet un défaut de la demande de fusion ; le tableau de l'indice 2 dit lequel. Une revue « prouvée par les tests » a un grand avantage sur une revue en commentaires : quand le collègue corrige, il sait **exactement** quand il a fini (tous les tests passent), et si le défaut revient un jour, un test le dira.
