# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Essayer de tester le legacy

<details><summary>Indice 1</summary>

Cherche dans `placeOrder` chaque `new`, chaque appel `static` (`X.INSTANCE`, `LocalDateTime.now()`), et chaque chose que la méthode lit ou écrit sans l'avoir reçue en paramètre.

</details>

<details><summary>Indice 2</summary>

Pour ton test « dimanche » : comment ferais-tu croire à `LocalDateTime.now()` qu'on est dimanche ? Et comment vérifierais-tu qu'aucun mail n'est parti ?

</details>

---

## Étape 2 — Les ports

<details><summary>Indice 1</summary>

Les ports parlent la langue du **métier** : une commande, un client, un article. Aucun mot technique (`SMTP`, `SQL`, `HTTP`) dans leurs noms.

</details>

<details><summary>Indice 2</summary>

`public Order { items = List.copyOf(items); }` : dans un constructeur compact, on peut réaffecter le paramètre avant qu'il soit rangé dans le champ.

</details>

---

## Étape 3 — Le service

<details><summary>Indice 1</summary>

Range chaque partie dans une petite méthode privée : `requireOpen(now)`, `priceOf(item)` (avec `orElseThrow(() -> new IllegalArgumentException(…))`), `notifySafely(order)`. `place` n'a plus qu'à les enchaîner.

</details>

<details><summary>Indice 2</summary>

Pour faire avancer le temps : deux `OrderService` qui partagent **le même** dépôt, l'un avec une horloge à 10 h 15 (pour commander), l'autre à 10 h 45 (pour annuler). Une méthode d'aide `serviceAt(Clock clock)` dans le test évite de répéter le constructeur.

</details>

---

## Étape 4 — Les pannes, avec Mockito

<details><summary>Indice 1</summary>

`Notifier broken = mock(Notifier.class); doThrow(new IllegalStateException("…")).when(broken).orderConfirmed(any());`. `any()` vient de `org.mockito.ArgumentMatchers`.

</details>

<details><summary>Indice 2</summary>

Pour le dimanche : `Clock.fixed(Instant.parse("2026-10-11T10:00:00Z"), ZoneOffset.UTC)`. Le 11 octobre 2026 est un dimanche.

</details>

---

## Étape 5 — Les adaptateurs et la racine de composition

<details><summary>Indice 1</summary>

Pour lire ce qu'écrit `ConsoleNotifier` : `ByteArrayOutputStream bytes = new ByteArrayOutputStream();`, puis `new PrintStream(bytes, true, StandardCharsets.UTF_8)`, puis `bytes.toString(StandardCharsets.UTF_8).lines().toList()`.

</details>

<details><summary>Indice 2</summary>

Le catalogue de `BakeryApp` : `Catalog catalog = item -> Optional.ofNullable(PRICES.get(item));`, avec `PRICES = Map.of("baguette", 120L, "croissant", 110L, "tarte", 1850L)`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque limite (7 h, 19 h, 30 minutes), un test **sur** la limite et un **juste à côté**. Pour la panne du mail, un test où le `Notifier` lance une exception.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. La boutique ouvre à 6 h au lieu de 7 h.
2. La boutique ferme à 20 h au lieu de 19 h (19 h pile est accepté).
3. La boutique est ouverte le dimanche.
4. Une commande vide est acceptée.
5. Un article commandé deux fois n'est compté qu'une fois.
6. Un article inconnu coûte 0 au lieu d'être refusé.
7. La commande n'est plus enregistrée.
8. La date de la commande est faussée (midi au lieu de l'heure lue).
9. Une panne du mail fait échouer la commande.
10. L'annulation à 30 minutes pile est refusée.
11. L'annulation n'efface plus la commande.
12. L'annulation ne prévient plus le client.
13. Le premier numéro est `CMD-0`.
14. L'historique d'un client montre les commandes de tout le monde.
15. Le dépôt ne garde plus l'ordre d'enregistrement.
16. La commande garde une **vue** de la liste reçue au lieu d'une copie.
17. Le message d'annulation a changé.

</details>
