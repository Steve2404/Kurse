# Projet 3 — Ressources, réessais et disjoncteur

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 11) :**
- **try-with-resources :**
  - `AutoCloseable` contre `Closeable` ;
  - l'**ordre de fermeture** (inverse de l'ouverture), qui précède `catch` et `finally` ;
  - une ressource dont le **constructeur échoue** n'est jamais fermée ;
  - les **exceptions supprimées** (`getSuppressed`) ;
  - une exception de `close()` qui devient **principale** ;
  - la variable **effectivement finale** déclarée avant le `try` (Java 9) ;
  - le `close()` idempotent de `Closeable` ;
- redéfinir `close()` avec un `throws` **plus étroit** (ou aucun) ;
- **`addSuppressed`** à la main ;
- une exception **non vérifiée** pour un refus immédiat.

Côté algorithmes :
- les **réessais** : le dernier échec devient la cause, les précédents sont supprimés ;
- un **disjoncteur** (*circuit breaker*) : une machine à états CLOSED → OPEN → HALF_OPEN.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p03_resources` :
- `ResourceException`, `ServiceException`, `RetryExhaustedException` et `CircuitOpenException` ;
- `Channel`, `Journal`, l'interface `Attempt<T>` et `CircuitBreaker` ;
- **`Resources`** (le `main`).

**Règle du crescendo :** chapitres 1 à 11 (voir `PARCOURS.md`).

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch11-p03 -sourcepath src/main/java src/main/java/ch11_exceptions/projects/p03_resources/Resources.java
java "-Duser.language=fr" -cp build/ch11-p03 ch11_exceptions.projects.p03_resources.Resources
```

---

## Tableau de bord

### ☐ Étape 1 — Les ressources

**📖 La leçon : une ressource, quelque chose qu'il faut refermer.** Un fichier, une connexion, un robinet : on l'ouvre, on s'en sert, et il faut **toujours** le refermer, même en cas de problème. En Java, une ressource réalise l'interface `AutoCloseable` (ou sa fille `Closeable`), qui a une seule méthode, `close()`.

**👉 À toi :**

- **`ResourceException`** et **`ServiceException`** : `extends Exception`, avec un constructeur `(String message)`.
- **`class Channel implements AutoCloseable`**, construite avec `(String name, List<String> log)` :
  - le constructeur **déclare `throws ResourceException`**. Si le nom commence par `!`, il lève `ResourceException("ouverture ratee de <nom>")`. Sinon il note `ouvre <nom>` ;
  - `void send(String message)` note `<nom> <- <message>`. Sur un canal fermé, il lève `IllegalStateException("canal ferme : <nom>")` ;
  - `public void close() throws ResourceException` note `ferme <nom>`, puis, si le nom commence par `~`, lève `ResourceException("fermeture ratee de <nom>")`.
- **`class Journal implements Closeable`** :
  - `close()` **sans `throws`** : compte les fermetures demandées et les fermetures effectives (seulement la première) ;
  - `String stats()` rend `fermetures demandees N, effectives M`.
- **Question :** que déclarent `AutoCloseable.close()` et `Closeable.close()` ?

### ☐ Étape 2 — Les travaux

```
db cache ok : ouvre db, ouvre cache, db <- debut, cache <- fin, ferme cache, ferme db, finally
db ~cache fail : ouvre db, ouvre ~cache, db <- debut, ferme ~cache, ferme db, attrape IllegalStateException(travail rate) supprimees [fermeture ratee de ~cache], finally
db !cache ok : ouvre db, ferme db, attrape ResourceException(ouverture ratee de !cache), finally
```

**📖 La leçon : le try-with-resources.** Les ressources déclarées entre les parenthèses du `try` sont **fermées automatiquement** à la fin du bloc, dans l'**ordre inverse** de leur ouverture, avant le `catch` et le `finally` :

```java
class Robinet implements AutoCloseable {
    private final String nom;
    Robinet(String nom) { this.nom = nom; System.out.println("ouvre " + nom); }
    @Override
    public void close() { System.out.println("ferme " + nom); }
}

try (Robinet a = new Robinet("eau"); Robinet b = new Robinet("gaz")) {
    System.out.println("travail");
} finally {
    System.out.println("finally");
}
// ouvre eau, ouvre gaz, travail, ferme gaz, ferme eau, finally
```

**📖 La leçon : les exceptions supprimées.** Si le corps du `try` lance une exception **et** que `close()` en lance une aussi, Java garde celle du corps comme exception **principale**, et range celle de `close()` dans ses **supprimées** :

```java
class Fragile implements AutoCloseable {
    @Override
    public void close() { throw new IllegalStateException("fermeture ratee"); }
}
try (Fragile f = new Fragile()) {
    throw new IllegalArgumentException("travail rate");
} catch (RuntimeException e) {
    e.getMessage()                        // "travail rate"
    e.getSuppressed()[0].getMessage()     // "fermeture ratee"
}
```

On peut aussi ajouter une supprimée à la main : `principale.addSuppressed(autre)`.

**👉 À toi :**

- **`static String describe(Throwable e)`** rend `NomSimple(message)`. Si `getSuppressed()` n'est pas vide, ajoute ` supprimees [messages]` (le message de chacune).
- **`static void runJob(String job, List<String> log)`**, où `job` vaut `res1 res2 issue` :
  - `try (Channel first = new Channel(p[0], log); Channel second = new Channel(p[1], log))` ;
  - le corps : `first.send("debut")`. Puis, si l'issue est `fail`, il lève `IllegalStateException("travail rate")`. Sinon `second.send("fin")` ;
  - `catch (ResourceException | IllegalStateException e)` note `attrape ` + `describe(e)` ;
  - `finally` note `finally`.
- **Dans le `main`**, pour chaque travail de `Data.JOBS`, une liste neuve, puis affiche `<travail> : <log joint par ", ">`.
- **Questions :**
  - Dans `~db cache ok`, pourquoi l'exception de `close()` est-elle **principale** ?
  - Dans `~db ~cache fail`, dans quel ordre sont les supprimées ?

### ☐ Étape 3 — Ressource existante et `Closeable`

```
ressource existante : ouvre partage, partage <- un, ferme partage, attrape IllegalStateException(canal ferme : partage)
Closeable idempotent : fermetures demandees 2, effectives 1
```

**📖 La leçon : une ressource déjà créée.** Depuis Java 9, on peut écrire `try (variable)` avec une variable **déjà** déclarée, à condition qu'elle ne change plus (« effectivement `final` », chapitre 7).

**👉 À toi :**

- **La ressource existante :**
  1. dans un `try` extérieur, `Channel shared = new Channel("partage", log)` ;
  2. puis `try (shared) { shared.send("un"); }` ;
  3. puis `shared.send("deux")` **après** le `try`.
  
  Le `catch (IllegalStateException | ResourceException e)` extérieur note `attrape ` + `describe(e)`.
- **Le `Closeable` :** `Journal journal = new Journal();`, puis `try (journal) { journal.close(); }`, puis affiche `stats()`.
- **Expérience :** réaffecte `shared` avant le `try (shared)`. Quelle erreur ?

### ☐ Étape 4 — Réessayer

```
reessais [timeout, timeout, ok:42] -> 42
reessais [timeout, refus, panne] -> abandon apres 3 essais <- panne (essai 3) ; RetryExhaustedException(abandon apres 3 essais) supprimees [timeout (essai 1), refus (essai 2)]
```

**📖 Rappel :** `addSuppressed` (étape 2). Une interface fonctionnelle générique qui annonce `throws` (projet 2, étape 1). Une exception construite avec une **cause** (projet 2, étape 2).

**👉 À toi :**

- **`RetryExhaustedException extends Exception`** : construite avec `(String message, Throwable cause)`.
- **`@FunctionalInterface interface Attempt<T>`** : `T run(int number) throws ServiceException`.
- **`static <T> T retry(int max, Attempt<T> attempt) throws RetryExhaustedException`** :
  - essaie les numéros 1 à `max`, et rend le premier succès ;
  - à chaque échec, l'échec **précédent** passe dans une liste ;
  - à la fin, crée `new RetryExhaustedException("abandon apres " + max + " essais", dernierEchec)`, lui ajoute les précédents avec `addSuppressed`, puis le lance.
- **`static Attempt<Integer> scripted(String[] answers)`** : l'essai `n` lit `answers[n - 1]`.
  - `ok:42` rend 42 ;
  - toute autre réponse lève `ServiceException(<réponse> + " (essai " + n + ")")`.
- **Pour `Data.FLAKY` puis `Data.DOWN`** (avec `Data.MAX_ATTEMPTS`), affiche `reessais <Arrays.toString> -> <valeur>`. En cas d'échec, affiche `-> <message> <- <message de la cause> ; <describe(e)>`.

### ☐ Étape 5 — Le disjoncteur

```
disjoncteur : ok/CLOSED echec/CLOSED echec/CLOSED echec/OPEN refus/OPEN refus/OPEN ok/CLOSED ...
servis 4, echecs 8, refus 5, etat final OPEN
```

**📖 Rappel :** un `enum` imbriqué (chapitre 7). Une exception non vérifiée signale plutôt une **erreur d'utilisation** ; une vérifiée, un problème **prévisible** que l'appelant doit traiter.

**👉 À toi :**

- **`CircuitOpenException extends RuntimeException`**.
- **`CircuitBreaker`** :
  - construit avec `(int threshold, int pause)` ;
  - un `enum State { CLOSED, OPEN, HALF_OPEN }` imbriqué, et `State state()`.
- **`String call(String outcome) throws ServiceException`** (`outcome` = la réponse du service distant) :
  1. si l'état est **OPEN** : tant que le délai (`pause` appels) n'est pas écoulé, décrémente-le et lève `CircuitOpenException("circuit ouvert")` ; ensuite, passe en **HALF_OPEN** ;
  2. si `outcome` vaut `ko` : un échec de plus. Si l'état est HALF_OPEN, ou si les échecs consécutifs atteignent `threshold`, l'état passe à **OPEN** et le délai repart à `pause`. Puis lève `ServiceException("ko")` ;
  3. sinon : remet les échecs à 0, passe en **CLOSED**, et rend `outcome`.
- **Le `main`** appelle `call` pour chaque mot de `Data.CALLS` (avec `Data.THRESHOLD` et `Data.PAUSE`) :
  - il note ` <marque>/<état après l'appel>`, où la marque vaut le résultat, `refus` (`CircuitOpenException`) ou `echec` (`ServiceException`) ;
  - il compte servis, échecs et refus.
- **Question :** pourquoi `CircuitOpenException` est-elle non vérifiée, alors que `ServiceException` est vérifiée ?

---

## Checklist (vérifiée par `Check`)

- `Data.JOBS`, `Data.FLAKY`, `Data.DOWN`, `Data.MAX_ATTEMPTS`, `Data.CALLS`, `Data.THRESHOLD`, `Data.PAUSE` ;
- `implements AutoCloseable`, `implements Closeable`, `public void close() throws ResourceException` ;
- `try (Channel first = new Channel(`, `try (shared)`, `try (journal)` ;
- `.getSuppressed()`, `addSuppressed` ;
- `catch (ResourceException | IllegalStateException`, `finally` ;
- `enum State`, `interface Attempt<T>`, `throws ServiceException`, `throw new CircuitOpenException(` ;
- `catch (CircuitOpenException`, `catch (ServiceException`, `catch (RetryExhaustedException`.

---

## Sortie attendue complète

```
db cache ok : ouvre db, ouvre cache, db <- debut, cache <- fin, ferme cache, ferme db, finally
db cache fail : ouvre db, ouvre cache, db <- debut, ferme cache, ferme db, attrape IllegalStateException(travail rate), finally
db ~cache fail : ouvre db, ouvre ~cache, db <- debut, ferme ~cache, ferme db, attrape IllegalStateException(travail rate) supprimees [fermeture ratee de ~cache], finally
~db cache ok : ouvre ~db, ouvre cache, ~db <- debut, cache <- fin, ferme cache, ferme ~db, attrape ResourceException(fermeture ratee de ~db), finally
db !cache ok : ouvre db, ferme db, attrape ResourceException(ouverture ratee de !cache), finally
~db ~cache fail : ouvre ~db, ouvre ~cache, ~db <- debut, ferme ~cache, ferme ~db, attrape IllegalStateException(travail rate) supprimees [fermeture ratee de ~cache, fermeture ratee de ~db], finally
ressource existante : ouvre partage, partage <- un, ferme partage, attrape IllegalStateException(canal ferme : partage)
Closeable idempotent : fermetures demandees 2, effectives 1
reessais [timeout, timeout, ok:42] -> 42
reessais [timeout, refus, panne] -> abandon apres 3 essais <- panne (essai 3) ; RetryExhaustedException(abandon apres 3 essais) supprimees [timeout (essai 1), refus (essai 2)]
disjoncteur : ok/CLOSED echec/CLOSED echec/CLOSED echec/OPEN refus/OPEN refus/OPEN ok/CLOSED echec/CLOSED ok/CLOSED ok/CLOSED echec/CLOSED echec/CLOSED echec/OPEN refus/OPEN refus/OPEN echec/OPEN refus/OPEN
servis 4, echecs 8, refus 5, etat final OPEN
```
