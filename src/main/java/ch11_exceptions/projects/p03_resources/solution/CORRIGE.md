# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : les exceptions, `Channel`, `Journal`, `Attempt`, `CircuitBreaker` et `Resources`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18).

---

## Étape 1 — Les ressources

**Le code :** [`Channel.java`](Channel.java) et [`Journal.java`](Journal.java).

**Question — ce que déclarent les deux `close()` :**
- `AutoCloseable.close()` déclare **`throws Exception`** ;
- `Closeable.close()` (une sous-interface, dans `java.io`) déclare **`throws IOException`**, et Java demande qu'il soit **idempotent** : le rappeler ne doit avoir aucun effet.

Une implémentation peut déclarer **plus précis**, ou **rien** : `Channel` déclare `ResourceException`, `Journal` ne déclare rien.

**Un constructeur qui échoue :** si `new Channel("!cache", log)` lève une exception, l'objet **n'existe pas**. Le `try`-with-resources ne fermera donc pas cette ressource-là, mais il fermera celles déjà ouvertes.

---

## Étape 2 — Les travaux

**Le code :** `describe` et `runJob` de [`Resources.java`](Resources.java).

**L'ordre d'un `try`-with-resources :**
1. ouverture des ressources, de gauche à droite ;
2. le corps ;
3. fermeture, dans l'ordre **inverse** (cache, puis db) ;
4. le `catch`, s'il y a une exception ;
5. le `finally`.

Les fermetures ont lieu **avant** le `catch`, comme le montre chaque ligne.

**Les exceptions supprimées :** si le corps lève une exception, et qu'un `close()` en lève **une autre**, celle du corps reste **principale**. Celle de `close()` lui est **attachée** comme « supprimée » (`getSuppressed()`). Rien n'est perdu.

**Questions :**
- **`~db cache ok`, pourquoi l'exception de `close()` est principale ?** Le corps a **réussi**, sans aucune exception. La seule exception est celle de la fermeture de `~db` : il n'y a rien à quoi l'attacher, donc elle **devient** l'exception principale, attrapée par le `catch`.
- **`~db ~cache fail`, l'ordre des supprimées :** l'ordre des **fermetures**. `~cache` est fermé en premier (ordre inverse), puis `~db`. On obtient `[fermeture ratee de ~cache, fermeture ratee de ~db]`.

**`db !cache ok`** : `db` est ouvert, puis `!cache` échoue à l'ouverture. Seul `db` est fermé, et le corps ne s'exécute **jamais**.

---

## Étape 3 — Ressource existante et `Closeable`

**Le code :** les blocs « ressource existante » et « Closeable » du `main`.

**`try (shared)`** ferme `shared` à la fin du bloc. Utiliser la variable **après** lève l'`IllegalStateException` du canal fermé. La variable existe encore, mais pas la ressource.

**Expérience — réaffecter `shared` avant le `try (shared)`** (vérifié) :

```
error: variable shared used as a try-with-resources resource neither final nor effectively final
```

Le `try` doit fermer **exactement** l'objet désigné. Une variable qui peut changer rendrait l'objet ambigu.

**`Closeable` idempotent :** la fermeture manuelle compte 1 demande et 1 fermeture effective. La fermeture automatique du `try` compte une 2e demande, sans effet.

---

## Étape 4 — Réessayer

**Le code :** `retry` et `scripted`.

**La cause et les supprimées :** le dernier échec (`panne`) devient la **cause** de la `RetryExhaustedException`. Les échecs précédents (`timeout`, `refus`) y sont attachés avec `addSuppressed`. Toute l'histoire est conservée dans **une** exception.

**`addSuppressed` sert aussi en dehors du `try`-with-resources :** c'est un mécanisme général, que Java utilise automatiquement pour les ressources, et qu'on peut employer soi-même.

---

## Étape 5 — Le disjoncteur

**Le code :** [`CircuitBreaker.java`](CircuitBreaker.java) et la fin du `main`.

**Le patron « disjoncteur » :** après 3 échecs consécutifs, on **cesse d'appeler** le service malade pendant 2 appels : on refuse tout de suite, sans attendre un échec de plus. Ensuite, on fait un essai (HALF_OPEN) :
- s'il réussit, retour à la normale (CLOSED) ;
- s'il échoue, on repart en OPEN immédiatement.

**Le début de la trace :** `ok` (CLOSED), puis `ko` ×3, dont le 3e fait passer en OPEN. Les deux appels suivants sont **refusés** (le délai de 2). Le 3e passe en HALF_OPEN, réussit, et l'état revient à CLOSED.

**Question — pourquoi `CircuitOpenException` est non vérifiée ?** Un circuit ouvert n'est pas une erreur du **service** que l'appelant pourrait corriger. C'est une **protection** du programme lui-même. En faire une exception non vérifiée évite d'obliger chaque appelant à la déclarer. `ServiceException`, elle, représente une panne **prévisible** du service distant, que l'appelant **doit** prévoir.
