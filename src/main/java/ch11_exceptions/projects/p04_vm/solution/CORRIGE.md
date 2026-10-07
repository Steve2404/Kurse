# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : les exceptions, `Machine` et `VmLab`.
>
> Le comportement de `System.exit` a été vérifié en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Les exceptions de la machine

**Le code :** les 5 classes d'exceptions.

**Une exception abstraite :** `VmException` regroupe les erreurs **du programme exécuté** par la machine. Chacune sait donner son code (`code()`), et c'est ce code que le gestionnaire `TRY` empile.

**Question — pourquoi `StepLimitException` ne doit pas étendre `VmException` ?** Les `VmException` sont **rattrapées par les `TRY`** du programme. Si la limite de pas en était une, un programme en boucle infinie protégée par un `TRY` attraperait sa **propre** limite et continuerait à boucler. Le garde-fou serait inutile. La limite est une protection de la **machine**, extérieure au programme : elle doit traverser tous les `TRY`. C'est une `RuntimeException`, non attrapée par le `catch (VmException | ArithmeticException e)`.

---

## Étape 2 — La machine

**Le code :** [`Machine.java`](Machine.java).

**Le mécanisme `TRY`, c'est celui de Java en miniature :** un `TRY` empile un **gestionnaire**, qui retient où sauter et la taille de la pile à ce moment. Une erreur fait :
1. dépiler le gestionnaire le **plus récent** (le plus intérieur) ;
2. **dérouler** la pile de valeurs jusqu'à la profondeur du `TRY` ;
3. empiler le code ;
4. sauter.

C'est exactement ce que fait la JVM avec les cadres d'appel quand une exception remonte.

**Le multi-catch avec deux familles :** `VmException` (vérifiée) et `ArithmeticException` (non vérifiée) n'ont pas de lien d'héritage, ce qui est permis. La division par 0 donne le code −2.

---

## Étape 3 — Les programmes

**Le code :** la boucle du `main` de [`VmLab.java`](VmLab.java).

**Question — « pile vide », pourquoi la pile finale est vide ?** `PUSH 1` empile 1. `ADD` dépile `b` (le 1), puis essaie de dépiler `a` : la pile est vide, d'où `StackUnderflowException`. Le 1 a **déjà** été dépilé avant l'exception, et il est perdu. Une exception interrompt une opération **au milieu**, sans rien annuler.

**« throw imbriqué », à la main :**

| Pas | Instruction | Pile | Gestionnaires | Affiché |
|---|---|---|---|---|
| 1 | `TRY outer` | [] | outer (profondeur 0) | |
| 2 | `TRY inner` | [] | outer, inner (0) | |
| 3 | `PUSH 5` | [5] | | |
| 4 | `THROW 7` : on dépile **inner**, on déroule jusqu'à 0, on empile 7, on saute à inner | [7] | outer | |
| 5 | `inner:` | [7] | | |
| 6 | `PRINT` | [7] | | [7] |
| 7 | `THROW 9` : on dépile **outer**, on déroule jusqu'à 0, on empile 9, on saute à outer | [9] | — | |
| 8 | `outer:` | [9] | | |
| 9 | `PRINT` | [9] | | [7, 9] |
| 10 | `HALT` | | | |

Le `ENDTRY` n'est **jamais** exécuté : l'exception saute par-dessus.

**« boucle infinie »** : `StepLimitException` traverse tout et arrête la machine après 50 pas.

---

## Étape 4 — Les règles de `finally`

**Le code :** les 4 méthodes statiques de `VmLab`.

**Les résultats :**
- **`finallyWins()` rend 2.** Le `return 1` du `try` est calculé, **puis** le `finally` s'exécute, et son `return 2` **remplace** le premier. (`javac -Xlint` signale « finally clause cannot complete normally », d'où le `@SuppressWarnings`.)
- **`valueAlreadyComputed()` rend 1.** La valeur de retour (`x`, qui vaut 1) est **copiée** au moment du `return`. Modifier `x` dans le `finally` ne change plus rien.
- **`order`** : on obtient `[try, catch, finally]`, et la méthode rend `retour du catch`. Le `finally` s'exécute **après** le `return` du `catch`, mais **avant** que la méthode rende la main.

**Question — où est passée l'`ArithmeticException` ?** Elle est **perdue**. Une exception levée **dans** le `finally` **remplace** celle qui était en cours. Elle n'est ni la cause (`cause null`), ni une supprimée (`supprimees 0`). C'est pour ça qu'un `finally` ne doit jamais lever d'exception ni faire de `return`.

**Comparaison avec le `try`-with-resources (projet 3) :** là, une exception de `close()` pendant une exception du corps est **attachée** comme supprimée, et rien n'est perdu. Le `try`-with-resources a été conçu justement pour corriger ce défaut du `finally` écrit à la main.

**Expérience — `System.exit(0)` dans un `try`** (vérifié dans une classe à part) : seul `try` s'affiche, et **le `finally` ne s'exécute pas**. `System.exit` arrête la JVM immédiatement. C'est l'une des rares façons d'empêcher un `finally`.
