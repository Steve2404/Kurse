# Projet 3 — Le stockage des notes (Liskov, des interfaces étroites, des tests de contrat)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- le principe de **substitution de Liskov** (le L de SOLID) : une sous-classe doit pouvoir **remplacer** sa classe mère **partout**, sans surprise ;
- le **contrat** d'une interface : ce que **toutes** ses implémentations promettent, au-delà des signatures ;
- le principe de **séparation des interfaces** (le I de SOLID) : plusieurs petites interfaces plutôt qu'une grosse ;
- le **test de contrat** : une classe de test **abstraite**, écrite une fois, que chaque implémentation hérite ;
- la **vue** et l'**enveloppe** (*wrapper*) qui respectent le contrat de ce qu'elles enveloppent ;
- la **copie défensive** : une copie n'est pas une vue.

**Ce qui est FOURNI :** `Data.java` contient l'ancienne couche de stockage de l'application de notes : **une seule** interface, `LegacyStorage` (lire, écrire, effacer, lister), que tout le monde doit implémenter en entier, même `LegacyArchive`, une archive en lecture seule… qui lance donc des exceptions. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p03_storage` : `Keys`, `ReadableStorage`, `WritableStorage`, `MemoryStorage`, `Archive`, `PrefixedStorage`, `AuditedStorage`, `Backup`, `StorageStats`, et tes tests : deux classes de contrat **abstraites** (`ReadableContractTest`, `WritableContractTest`) et une classe de test par implémentation.

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : ni `UnsupportedOperationException`, ni `instanceof`, et aucune méthode de plus de **10 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Voir le problème

**📖 La leçon : Liskov.** Le troisième principe SOLID (Barbara Liskov, 1987) : **si `B` est un sous-type de `A`, on doit pouvoir donner un `B` à tout code qui attend un `A`, sans que ce code se comporte mal.** Le compilateur vérifie les **signatures** ; Liskov parle du **comportement promis**.

L'exemple célèbre : un carré **est** un rectangle, en géométrie. Alors `class Carre extends Rectangle`, et `setLargeur` change aussi la hauteur (sinon ce n'est plus un carré). Mais un code écrit pour les rectangles fait `r.setLargeur(5); r.setHauteur(4);` et s'attend à une aire de 20. Avec un `Carre`, il obtient 16. Le carré **casse la promesse** du rectangle : « largeur et hauteur se règlent indépendamment ». Ce n'est pas un bon sous-type, même si la géométrie dit le contraire.

Le signe le plus courant d'une violation : une implémentation qui lance `UnsupportedOperationException`, « je fais semblant d'être un `A`, mais ne me demande pas ça ».

**👉 À toi :** lance `Data` et lis tout le fichier.

**❓ Questions :**
- Que se passe-t-il à la deuxième copie ? À quel moment l'erreur est-elle découverte : à la compilation, ou quand le programme tourne ?
- `LegacyBackup.copy` est-il faux ? `LegacyArchive` est-elle fausse ? Où est vraiment le défaut ?
- Quand `LegacyArchive.write` lance son exception, une partie des notes a-t-elle déjà été copiée ? Pourquoi est-ce pire qu'une erreur au début ?

### ☐ Étape 2 — Séparer les interfaces

**📖 La leçon : des interfaces étroites.** Le **principe de séparation des interfaces** (*Interface Segregation Principle*) : **aucun client ne doit dépendre de méthodes qu'il n'utilise pas.** Une grosse interface oblige chaque implémentation à **tout** fournir, même ce qu'elle ne sait pas faire, et chaque client à dépendre de **tout**, même de ce qu'il n'appelle jamais. On la coupe en petites interfaces, et l'on peut les **empiler** avec `extends`.

**Exemple sur un autre sujet :** `interface Imprimante { imprimer(); scanner(); faxer(); }` oblige une petite imprimante sans scanner à lancer des exceptions. Mieux : `Imprimeur`, `Scanneur`, `Faxeur`, et l'appareil tout-en-un les implémente toutes.

**👉 À toi :**
- `public final class Keys` (constructeur privé) avec `public static String check(String key)` : une clé est faite de minuscules, de chiffres et de `/ . _ -`, **au moins un** caractère (`key.matches("[a-z0-9/._-]+")`) ; sinon (ou si elle est `null`), `IllegalArgumentException("cle invalide : " + key)`. Elle **rend** la clé, pour s'écrire en une ligne ;
- `public interface ReadableStorage` : `Optional<String> read(String key)` et `List<String> keys()` ;
- `public interface WritableStorage extends ReadableStorage` : `void write(String key, String value)` et `boolean delete(String key)` (vrai si la clé existait).

Écris en Javadoc, au-dessus de chaque interface, son **contrat** :
- **lecture** : `read` d'une clé absente rend `Optional.empty()` ; une clé invalide est refusée (le message de `Keys`) ; `keys()` rend les clés **triées**, dans une liste **non modifiable** ;
- **écriture** : après `write(k, v)`, `read(k)` rend `v` (la dernière valeur écrite) ; une valeur `null` est refusée par une `IllegalArgumentException` ; `delete(k)` rend `true` si la clé existait, `false` sinon, et après, `read(k)` est vide ; une clé invalide est refusée par `write` **et** par `delete`.

**❓ Question :** pourquoi `WritableStorage` **étend** `ReadableStorage`, au lieu d'être une interface à part avec seulement `write` et `delete` ?

### ☐ Étape 3 — Le test de contrat, et la mémoire

**📖 La leçon : un contrat se teste une fois pour toutes.** Un contrat vaut pour **toutes** les implémentations. Plutôt que de recopier les mêmes tests pour chacune, on écrit une **classe de test abstraite** : ses tests n'utilisent que l'interface, et elle déclare une méthode **abstraite** qui fabrique le stockage. Chaque implémentation a sa **sous-classe** de test, qui dit seulement comment fabriquer **son** stockage : JUnit relance alors **tous** les tests hérités sur elle. Une classe de test abstraite n'est jamais lancée seule.

Et le contrat d'écriture **hérite** du contrat de lecture : un `WritableStorage` doit passer **les deux**. C'est Liskov, vérifié par des tests.

**👉 À toi :**
1. `abstract class ReadableContractTest` avec `protected abstract ReadableStorage storageWith(Map<String, String> content)`, et un test par point du contrat de lecture : lire ce qu'il contient (deux clés), une clé absente, des clés **triées** (`"zeta"`, `"alpha"`, `"m/1"`, `"b"`), un stockage vide, la liste non modifiable (`keys().add(…)` lance `UnsupportedOperationException`), et un `@ParameterizedTest` sur des clés invalides (`""`, `"A"`, `"a b"`, `"été"`) avec le message exact.
2. `abstract class WritableContractTest extends ReadableContractTest` avec `protected abstract WritableStorage emptyStorage()` (un stockage **neuf et vide** à chaque appel). Il **remplit** `storageWith` lui-même, avec des `write` : ses sous-classes n'auront qu'une méthode à écrire. Un test par point du contrat d'écriture : écraser une valeur, effacer une clé présente, une clé absente, des clés triées **quel que soit l'ordre des `write`**, la valeur `null` refusée (et rien n'est écrit), les clés invalides refusées par `write` et par `delete`.
3. `public final class MemoryStorage implements WritableStorage` : une `TreeMap` ; `keys()` rend `List.copyOf(…)` ; une valeur `null` est refusée par le message `"valeur absente pour " + key`.
4. `class MemoryStorageTest extends WritableContractTest`, plus un test pour le message de la valeur `null`.

**🧪 Expérience :** lance `MemoryStorageTest` seul (flèche verte à côté de la classe). Combien de tests JUnit lance-t-il, alors que la classe n'en contient qu'un ? Ouvre l'arbre des résultats : où sont rangés les tests hérités ?

**❓ Question :** pourquoi le message exact de la valeur `null` n'est-il **pas** dans le contrat, mais seulement dans `MemoryStorageTest` ? (Indice : regarde l'étape 5.)

### ☐ Étape 4 — L'archive ne promet que ce qu'elle tient

**📖 La leçon : la copie défensive.** Une archive est **figée** à sa création. Si elle garde la `Map` reçue (ou une **vue** dessus, comme `Collections.unmodifiableMap`), l'appelant peut encore modifier « l'archive » en modifiant **sa** `Map`. Il faut une **copie** (`Map.copyOf`) : non modifiable, **et** indépendante de l'original.

**👉 À toi :**
- `public final class Archive implements ReadableStorage` (et **seulement** celle-là) : le constructeur `Archive(Map<String, String> content)` vérifie chaque clé avec `Keys`, puis garde `Map.copyOf(content)` ; `keys()` trie à chaque appel ;
- `class ArchiveTest extends ReadableContractTest` (le contrat de **lecture** seulement), plus : l'archive est une **photographie** (modifie la `HashMap` source après la création : l'archive ne bouge pas), et une clé invalide dans le contenu est refusée dès la création.

**🧪 Expérience :** dans une petite classe à part, affiche `Map.copyOf(…).keySet()` d'une `Map` de six clés, et lance le programme **trois fois**. Puis compare une vue (`Collections.unmodifiableMap(source)`) et une copie (`Map.copyOf(source)`) après un `source.put(…)`.

**❓ Questions :**
- Vu l'expérience, pourquoi `keys()` doit-elle **trier**, et pourquoi un test qui oublierait de vérifier l'ordre pourrait-il passer « par chance » ?
- Essaie d'écrire `Backup.copy(new MemoryStorage(), new Archive(Map.of()))` une fois `Backup` écrit (étape 6). Que se passe-t-il, et à quel moment ? Compare avec l'étape 1.

### ☐ Étape 5 — La vue préfixée et l'enveloppe journalisée

**📖 La leçon : respecter le contrat de ce qu'on remplace.** Une **vue** présente une partie d'un autre objet comme un objet complet ; une **enveloppe** (*wrapper*) ajoute un comportement autour d'un autre objet. Toutes deux **remplacent** un `WritableStorage` : elles doivent donc passer **tout** son contrat. Le test de contrat le vérifie gratuitement : une sous-classe de test de plus, et c'est tout.

**👉 À toi :**
- `public final class PrefixedStorage implements WritableStorage` : le constructeur `PrefixedStorage(WritableStorage inner, String prefix)` refuse un préfixe qui ne finit pas par `/` (`"prefixe invalide : " + prefix`), puis le vérifie avec `Keys`. La vue ne voit **que** les clés de `inner` qui commencent par le préfixe, et les montre **sans** le préfixe ; `write`, `read` et `delete` ajoutent le préfixe (après avoir vérifié la clé reçue) ;
- `public final class AuditedStorage implements WritableStorage` : enveloppe un autre stockage et note chaque modification **réussie** dans un journal : `"write a"`, `"delete a"`, ou `"delete b (absente)"` si la clé n'existait pas. `List<String> log()` rend une copie non modifiable. Une écriture refusée (clé invalide) ne laisse **pas** de trace ;
- leurs tests : `PrefixedStorageTest extends WritableContractTest`, dont `emptyStorage()` fabrique un `MemoryStorage` **qui contient déjà des voisins** (`"users2/x"`, `"users"`, `"admin/a"`) et rend la vue `"users/"` dessus ; plus : les écritures arrivent sous le préfixe, les voisins restent invisibles et intacts, le `/` final est obligatoire. `AuditedStorageTest extends WritableContractTest`, plus le journal.

**❓ Questions :**
- Pourquoi le `/` final du préfixe est-il indispensable ? Donne une clé que la vue `"users"` verrait à tort.
- Une enveloppe « optimisée » dont `delete` rendrait toujours `true` serait-elle un bon `WritableStorage` ? Quel test de contrat la démasquerait ?

### ☐ Étape 6 — Des clients qui demandent le minimum

**👉 À toi :**
- `public final class Backup` avec `public static int copy(ReadableStorage from, WritableStorage to)` : copie toutes les clés, **écrase** celles qui existent déjà dans la destination, rend le nombre de clés copiées ;
- `public final class StorageStats` avec `public static String describe(ReadableStorage storage)` : `"2 cle(s), 7 caractere(s)"` (le nombre de clés, puis la somme des longueurs des **valeurs**) ;
- `BackupTest` : une copie d'archive vers une mémoire qui contient déjà des clés (l'une est écrasée, l'autre gardée), une copie vers une vue préfixée, et les statistiques d'une archive et d'une mémoire vide.

**❓ Question :** `StorageStats.describe` pourrait prendre un `WritableStorage` et marcher dans tous tes tests. Pourquoi demander un `ReadableStorage` ?

### ☐ Étape 7 — Les mutants

**👉 À toi :** lance `Check`. Chaque mutant casse **une** promesse du contrat dans **une** implémentation.

**🧪 Expérience :** dans un `main` à part, `List.of("a").add("b")`. Que se passe-t-il ? La bibliothèque de Java respecte-t-elle Liskov partout ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `interface ReadableStorage`, `interface WritableStorage extends ReadableStorage`, `final class MemoryStorage implements WritableStorage`, `final class Archive implements ReadableStorage`, `final class PrefixedStorage implements WritableStorage`, `final class AuditedStorage implements WritableStorage`, `final class Keys`, `final class Backup`, `final class StorageStats`, `Map.copyOf(`, `List.copyOf(` ; ni `UnsupportedOperationException`, ni `instanceof`, ni rien de `Legacy`.
- **La conception :** aucune méthode de plus de **10 lignes** ; pas de `write(` dans `Archive.java` ; `Backup.java` contient `copy(ReadableStorage` ; `StorageStats.java` ne mentionne pas `WritableStorage`.
- **Tes tests :** au moins **60** tests (les tests hérités comptent pour chaque sous-classe), `abstract class ReadableContractTest`, `abstract class WritableContractTest extends ReadableContractTest`, au moins **3** fois `extends WritableContractTest` et **2** fois `extends ReadableContractTest`, `protected abstract`, `@Override`, `assertThrows(` ; ni `System.out` ni `Thread.sleep`.
- **Les 15 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p03_storage ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 71 tests, 71 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 15/15 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
