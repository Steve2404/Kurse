# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Voir le problème

<details><summary>Indice 1</summary>

Regarde le type des deux paramètres de `LegacyBackup.copy` : qu'est-ce que le compilateur accepte comme destination ? Et `LegacyArchive` implémente-t-elle cette interface ?

</details>

<details><summary>Indice 2</summary>

Le défaut est dans une **promesse** : quelle interface promet `write`, et quelle classe signe cette promesse sans pouvoir la tenir ?

</details>

---

## Étape 2 — Séparer les interfaces

<details><summary>Indice 1</summary>

`Keys.check` : `if (key == null || !key.matches("[a-z0-9/._-]+")) throw …; return key;`. Le `+` demande au moins un caractère.

</details>

<details><summary>Indice 2</summary>

Pense à `Backup` : sa source peut être n'importe quel stockage, sa destination doit savoir écrire. Si `WritableStorage` n'étendait pas `ReadableStorage`, pourrait-on **lire** une mémoire passée comme `WritableStorage` ?

</details>

---

## Étape 3 — Le test de contrat, et la mémoire

<details><summary>Indice 1</summary>

Dans `WritableContractTest`, `storageWith` s'écrit une fois : `WritableStorage storage = emptyStorage(); content.forEach(storage::write); return storage;`. Marque-le `@Override protected`.

</details>

<details><summary>Indice 2</summary>

`MemoryStorageTest` n'a qu'une méthode obligatoire : `@Override protected WritableStorage emptyStorage() { return new MemoryStorage(); }`. Toutes les méthodes `@Test` héritées sont lancées par JUnit sur cette sous-classe.

</details>

---

## Étape 4 — L'archive

<details><summary>Indice 1</summary>

`content.keySet().forEach(Keys::check);` puis `this.content = Map.copyOf(content);`. `keys()` : `content.keySet().stream().sorted().toList()` (une liste de `toList()` est non modifiable).

</details>

<details><summary>Indice 2</summary>

Le test de la photographie : une `new HashMap<>(Map.of("a", "un"))`, l'archive, puis `source.put("b", …)` et `source.put("a", "change")`. L'archive doit toujours avoir `[a]` et `"un"`.

</details>

---

## Étape 5 — La vue préfixée et l'enveloppe journalisée

<details><summary>Indice 1</summary>

`keys()` de la vue : `inner.keys().stream().filter(key -> key.startsWith(prefix)).map(key -> key.substring(prefix.length())).toList()`. Comme `inner.keys()` est déjà trié, le résultat l'est aussi.

</details>

<details><summary>Indice 2</summary>

Dans `AuditedStorage.write`, appelle d'abord `inner.write(…)`, **puis** ajoute la ligne au journal : si `inner` lance une exception, la ligne suivante n'est jamais exécutée. Dans `PrefixedStorageTest`, garde le `MemoryStorage` du dessous dans un champ, rempli à chaque `emptyStorage()`, pour pouvoir le regarder dans tes tests.

</details>

---

## Étape 6 — Des clients qui demandent le minimum

<details><summary>Indice 1</summary>

`copy` : garde `from.keys()` dans une variable, `forEach(key -> to.write(key, from.read(key).orElseThrow()))`, puis rends sa taille.

</details>

<details><summary>Indice 2</summary>

`describe` : `storage.keys().stream().mapToInt(key -> storage.read(key).orElseThrow().length()).sum()`.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Chaque mutant touche une seule implémentation. Si ton contrat est complet, la sous-classe de test de cette implémentation le tue sans test spécial.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. La clé vide `""` est acceptée.
2. La mémoire garde les clés dans l'ordre d'écriture, plus triées.
3. `keys()` de la mémoire rend une liste modifiable.
4. La mémoire accepte une valeur `null` (sauf pour la clé vide).
5. `delete` de la mémoire rend toujours `true`.
6. L'archive garde une **vue** de la `Map` reçue au lieu d'une copie.
7. Lire une clé absente dans l'archive plante (`NullPointerException`) au lieu de rendre un `Optional` vide.
8. La vue `"users/"` regarde aussi les clés qui commencent par `"users"` sans `/`.
9. La vue montre les clés **avec** le préfixe.
10. `delete` de la vue oublie le préfixe.
11. Un préfixe sans `/` final est accepté.
12. `delete` de l'enveloppe journalisée rend toujours `true`.
13. L'enveloppe note l'écriture **avant** de la faire : une écriture refusée laisse une trace.
14. La sauvegarde n'écrase plus les clés déjà présentes dans la destination.
15. Les statistiques comptent la longueur des **clés** au lieu de celle des valeurs.

</details>
