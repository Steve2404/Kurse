# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier ; les tests de contrat dans [`ReadableContractTest.java`](ReadableContractTest.java) et [`WritableContractTest.java`](WritableContractTest.java), puis une classe de test par implémentation.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Voir le problème

La sortie de `Data` (vérifiée) :

```
copie vers la memoire : 1 cle(s)
copie vers l'archive : java.lang.UnsupportedOperationException: archive en lecture seule
```

**Question — la deuxième copie :** elle lance `UnsupportedOperationException`. L'erreur est découverte **pendant que le programme tourne**, au premier `write` : le compilateur a tout accepté, puisque `LegacyArchive` **est** un `LegacyStorage`. Dans une vraie application, c'est un utilisateur qui la découvre.

**Question — où est le défaut :** ni dans `copy` (qui utilise honnêtement ce que l'interface promet), ni vraiment dans le code de l'archive (une archive ne doit pas être modifiée). Le défaut est dans la **conception** : `LegacyArchive` signe une promesse (« je sais écrire et effacer ») qu'elle ne peut pas tenir. Elle viole Liskov : on ne peut pas la mettre partout où un `LegacyStorage` est attendu. Et la cause de cette violation, c'est une interface **trop grosse**, qui mélange deux capacités.

**Question — la copie à moitié faite :** ici, non : l'archive refuse dès la **première** clé, donc rien n'est copié. Mais rien ne le garantit : un stockage qui refuserait seulement certaines clés laisserait une copie **à moitié faite**, ni l'ancien état ni le nouveau, souvent pire qu'une erreur franche au début. Une erreur trouvée par le **compilateur**, elle, ne laisse jamais de données à moitié modifiées.

---

## Étape 2 — Séparer les interfaces

Le code : [`Keys.java`](Keys.java), [`ReadableStorage.java`](ReadableStorage.java), [`WritableStorage.java`](WritableStorage.java), avec leur contrat en Javadoc.

**Question — `extends` :** parce qu'un stockage modifiable **est aussi** un stockage lisible : on écrit dedans **pour** relire ensuite. Avec deux interfaces séparées, une méthode qui doit lire **et** écrire (relire après une copie, effacer seulement si la clé existe) devrait recevoir le même objet deux fois, sous deux types. Et grâce à `extends`, un `WritableStorage` se passe **partout** où un `ReadableStorage` est attendu : c'est une substitution correcte, puisque le contrat de lecture est entièrement tenu.

---

## Étape 3 — Le test de contrat, et la mémoire

Le code : [`MemoryStorage.java`](MemoryStorage.java), [`MemoryStorageTest.java`](MemoryStorageTest.java).

**Expérience — le nombre de tests** (vérifié) : `MemoryStorageTest` lance **18** tests : les 9 du contrat de lecture (5 `@Test` et 4 exécutions du test paramétré), les 8 du contrat d'écriture (5 et 3), et son propre test. Dans l'arbre des résultats d'IntelliJ, les tests hérités sont rangés **sous `MemoryStorageTest`**, comme s'ils y étaient écrits.

**Question — le message hors du contrat :** parce que **toutes** les implémentations ne peuvent pas le garantir mot pour mot. La vue préfixée de l'étape 5 transmet la valeur `null` au stockage du dessous avec **sa** clé : le message devient `valeur absente pour users/a`, pas `valeur absente pour a`. Le contrat dit ce que **toutes** promettent (une `IllegalArgumentException`) ; un détail propre à une implémentation se teste dans **sa** classe de test. Un contrat trop précis interdirait des implémentations parfaitement correctes.

---

## Étape 4 — L'archive

Le code : [`Archive.java`](Archive.java), [`ArchiveTest.java`](ArchiveTest.java). L'archive lance **11** tests : les 9 du contrat de lecture et ses 2 tests propres.

**Expérience — l'ordre de `Map.copyOf`** (vérifié, trois lancements du même programme) :

```
Map.copyOf : [m/1, zeta, d, c, b, alpha]
Map.copyOf : [alpha, b, c, d, zeta, m/1]
Map.copyOf : [m/1, zeta, d, c, b, alpha]
```

L'ordre **change d'un lancement à l'autre** : les `Map` non modifiables de Java mélangent leurs clés avec un nombre tiré au démarrage de la JVM, exprès, pour que personne ne compte sur un ordre. **Vue contre copie** (vérifié) : après `source.put("b", "deux")`, la vue montre `[a, b]`, la copie `[a]`.

**Question — trier :** le contrat promet des clés triées, et `Map.copyOf` ne garantit **aucun** ordre. Un test qui ne vérifierait pas l'ordre passerait tantôt, échouerait tantôt : un **test instable** (*flaky*), le pire de tous, parce qu'on finit par l'ignorer. Avec quatre clés, un ordre faux tombe par hasard sur l'ordre trié une fois sur 24.

**Question — l'archive comme destination :** le programme **ne compile pas** (vérifié) : `incompatible types: Archive cannot be converted to WritableStorage`. L'erreur de l'étape 1, découverte à l'exécution par un utilisateur, est maintenant découverte **par le compilateur**, avant même de lancer quoi que ce soit. C'est tout l'intérêt de bons types : rendre l'erreur **impossible à écrire**.

---

## Étape 5 — La vue préfixée et l'enveloppe journalisée

Le code : [`PrefixedStorage.java`](PrefixedStorage.java), [`AuditedStorage.java`](AuditedStorage.java). Les tests : [`PrefixedStorageTest.java`](PrefixedStorageTest.java) (20 tests), [`AuditedStorageTest.java`](AuditedStorageTest.java) (19 tests).

**Question — le `/` final :** sans lui, la vue `"users"` verrait aussi `"users2/x"` (et `"users"` lui-même), c'est-à-dire les données d'un **autre** utilisateur. Avec le `/`, `"users/"` ne correspond qu'aux clés **rangées dans** ce dossier. C'est le mutant 8.

**Question — l'enveloppe optimisée :** non. Un code qui fait `if (storage.delete(k)) compteur--;` deviendrait faux avec elle : elle ne **remplace** pas correctement un `WritableStorage`. Le test de contrat `deleteMissingKeyReturnsFalse` la démasque, **sans** que tu aies écrit de test spécial pour l'enveloppe : c'est le mutant 12. Voilà la force du test de contrat : chaque nouvelle implémentation hérite de **toute** la vérification.

---

## Étape 6 — Des clients qui demandent le minimum

Le code : [`Backup.java`](Backup.java), [`StorageStats.java`](StorageStats.java). Les tests : [`BackupTest.java`](BackupTest.java).

**Question — `ReadableStorage` pour les statistiques :** parce qu'elles ne font que **lire**. En demandant `WritableStorage`, elles refuseraient les archives (qui n'ont rien de différent à lire), et le lecteur de leur signature croirait qu'elles **modifient** peut-être le stockage. Une signature étroite dit la vérité sur ce que fait la méthode, et accepte le plus d'implémentations possible. C'est la séparation des interfaces vue du côté du **client**.

---

## Étape 7 — Les mutants

Les 15 mutants sont tués par les tests de référence ; 10 d'entre eux le sont par des tests de **contrat** hérités (vérifié dans la sortie de `Check solution` : `keysAreSorted`, `keysListIsUnmodifiable`, `nullValueIsRefused`, `deleteMissingKeyReturnsFalse`, `missingKeyIsEmpty`, `deleteExistingKey`, `invalidKeyIsRefused`), sans test écrit pour l'implémentation fautive.

**Expérience — `List.of("a").add("b")`** (vérifié) : `java.lang.UnsupportedOperationException`. La bibliothèque de Java elle-même **viole** Liskov : `List` promet `add`, et les listes non modifiables lancent une exception. Ses concepteurs l'ont choisi pour ne pas multiplier les interfaces (`List`, `ModifiableList`, `ResizableList`…) ; la Javadoc appelle `add` une « opération optionnelle ». C'est un compromis connu, et la raison pour laquelle on ne sait jamais, en lisant `List<String>`, si l'on a le droit d'ajouter. Dans **ton** code, tu as le choix : fais des types qui disent la vérité.
