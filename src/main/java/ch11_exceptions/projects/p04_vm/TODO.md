# Projet 4 — La machine à exceptions (et les règles de `finally`)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 11) :**
- une **hiérarchie abstraite** d'exceptions vérifiées, avec une méthode redéfinie (`code()`) ;
- **le multi-catch** d'une exception à toi et d'une du JDK (`VmException | ArithmeticException`) ;
- une exception **hors** de la famille, pour qu'elle **ne soit pas** rattrapée ;
- **chaîner** une exception non rattrapée (`UncaughtVmException` avec cause) ;
- **`finally`** :
  - un `return` dans `finally` **écrase** celui du `try` ;
  - la valeur rendue est **déjà calculée** quand `finally` s'exécute ;
  - l'ordre try → catch → finally ;
  - une exception lancée dans `finally` **efface** l'originale (ni cause, ni supprimée).

Côté algorithme : **simuler ce que fait la JVM.**
- `TRY` empile un gestionnaire ;
- une erreur **déroule** jusqu'au gestionnaire le plus récent ;
- la pile est restaurée à sa hauteur du `TRY` ;
- le code d'erreur est empilé ;
- l'exécution saute au gestionnaire.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p04_vm` :
- `VmException`, `StackUnderflowException`, `ThrownException`, `UncaughtVmException` et `StepLimitException` ;
- `Machine` ;
- **`VmLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 11 (voir `PARCOURS.md`).

**À quoi sert ce projet ?** Une petite **machine virtuelle** : elle exécute un programme d'instructions (`PUSH 2`, `ADD`, `PRINT`…) sur une pile. Ce programme a ses propres `TRY` et `THROW`, que tu implémentes avec les vraies exceptions de Java.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch11-p04 -sourcepath src/main/java src/main/java/ch11_exceptions/projects/p04_vm/VmLab.java
java "-Duser.language=fr" -cp build/ch11-p04 ch11_exceptions.projects.p04_vm.VmLab
```

---

## Tableau de bord

### ☐ Étape 1 — Les exceptions de la machine

**📖 Rappel :** une exception peut être `abstract`, avec une méthode abstraite que chaque fille écrit (chapitre 6). Pour rattraper **toute** la famille, on rattrape la mère.

**👉 À toi :**

- **`abstract class VmException extends Exception`** : un constructeur `protected (String message)`, et `public abstract int code()`.
- **`StackUnderflowException extends VmException`** : construite avec `(int index)`, message `pile vide (instruction <index>)`, `code()` = -1.
- **`ThrownException extends VmException`** : construite avec `(int code)`, message `code <code>`, et `code()` rend ce code.
- **`UncaughtVmException extends Exception`** : construite avec `(String message, Throwable cause)`.
- **`StepLimitException extends RuntimeException`** : construite avec `(int limit)`, message `plus de <limit> pas`.
- **Question :** pourquoi `StepLimitException` ne doit-elle **pas** étendre `VmException` ?

### ☐ Étape 2 — La machine

**📖 Conseil :** déroule le programme « calcul » à la main : une colonne « instruction », une colonne « pile après ». Pour `TRY`, note le gestionnaire empilé ; pour `THROW`, retrouve-le.

**👉 À toi :**

- **`Machine(String program)`** : découpe sur `;`. Les instructions qui finissent par `:` sont des **étiquettes**, mémorisées dans une `Map<String, Integer>` (nom → indice).
- **L'état :**
  - `Deque<Integer> stack` ;
  - `Deque<Handler> handlers`, avec `record Handler(int target, int depth)` imbriqué ;
  - `List<Integer> printed` ;
  - `int steps`.
- **`int step(int pc) throws VmException`** exécute une instruction et rend l'indice suivant (-1 pour `HALT`) :

  | Instruction | Effet |
  |---|---|
  | `PUSH n` | empile n |
  | `POP` | dépile (`StackUnderflowException(pc)` si vide, comme tout dépilement) |
  | `DUP` | dépile v, puis empile v deux fois |
  | `ADD` `SUB` `MUL` `DIV` | dépile b, puis a ; empile `a op b` (`DIV` par 0 : `ArithmeticException`) |
  | `PRINT` | ajoute le sommet (sans dépiler) à `printed` |
  | `TRY L` | empile `new Handler(indice de L, stack.size())` |
  | `ENDTRY` | dépile un gestionnaire |
  | `THROW n` | lève `ThrownException(n)` |
  | `JMP L` | saute à L |
  | `JZ L` | dépile ; saute à L si la valeur vaut 0 |
  | `HALT` | rend -1 |
  | étiquette | rien |
  | autre | `IllegalArgumentException("instruction inconnue : " + nom)` |

  Une étiquette inconnue lève `IllegalArgumentException("etiquette inconnue : " + nom)`.
- **`void run(int maxSteps) throws UncaughtVmException`** : tant que `pc` est valide :
  1. `++steps > maxSteps` → `StepLimitException` ;
  2. `try { pc = step(pc); }` ;
  3. dans `catch (VmException | ArithmeticException e)` :
     - s'il n'y a aucun gestionnaire → `throw new UncaughtVmException("erreur non rattrapee", e)` ;
     - sinon, dépile le gestionnaire, dépile la pile jusqu'à `depth`, puis empile le code : `e instanceof VmException vm ? vm.code() : -2` ;
     - saute à `target`.
- **`String state()`** rend `affiche <printed>, pile <pile du BAS vers le haut> (<steps> pas)`.

### ☐ Étape 3 — Les programmes

```
calcul : affiche [42], pile [42] (5 pas)
throw imbrique : affiche [7, 9], pile [9] (10 pas)
pile vide : erreur non rattrapee <- StackUnderflowException pile vide (instruction 1) ; affiche [], pile [] (2 pas)
boucle infinie : arret StepLimitException plus de 50 pas
```
- Pour chaque `nom|code` de `Data.PROGRAMS`, crée une `Machine`, puis appelle `run(Data.MAX_STEPS)` :
  - en cas de succès : `<nom> : <state()>` ;
  - `catch (UncaughtVmException e)` : `<nom> : <message> <- <nom simple de la cause> <message de la cause> ; <state()>` ;
  - `catch (StepLimitException | IllegalArgumentException e)` : `<nom> : arret <nom simple> <message>`.
- **Questions :**
  - Dans « pile vide », pourquoi la pile finale est-elle vide, alors qu'on avait empilé 1 ?
  - Déroule « throw imbrique » à la main.

### ☐ Étape 4 — Les règles de `finally`

```
finally : finallyWins 2, valueAlreadyComputed 1, retour du catch [try, catch, finally]
masquee : IllegalStateException (lancee dans finally), cause null, supprimees 0
```

**📖 La leçon : quand s'exécute `finally` ?** Toujours, après le `try` (et le `catch` s'il y en a un), **même** quand le `try` fait un `return` :

```java
static String essai() {
    try {
        System.out.println("dans try");
        return "valeur du try";
    } finally {
        System.out.println("dans finally");
    }
}
// dans try, dans finally, puis l'appelant reçoit "valeur du try"
```

L'étape te fait observer les cas plus étranges : un `return` **dans** le `finally`, ou une exception lancée **dans** le `finally`.

**👉 À toi :**

- **Quatre méthodes** (les deux qui sortent de `finally` portent `@SuppressWarnings("finally")`) :
  - `int finallyWins()` : `try { return 1; } finally { return 2; }` ;
  - `int valueAlreadyComputed()` : `int x = 1; try { return x; } finally { x = 99; }` ;
  - `String order(List<String> trace)` :
    - `try` ajoute `try`, puis lève `IllegalStateException("rate")` ;
    - le `catch` ajoute `catch` et rend `"retour du catch"` ;
    - le `finally` ajoute `finally` ;
  - `void masked(int zero)` : le `try` affiche `1 / zero` (division par 0) ; le `finally` lève `IllegalStateException("lancee dans finally")`.
- **Dans `main` :**
  1. appelle d'abord `order` sur une liste neuve ;
  2. affiche la ligne `finally :` ;
  3. appelle `masked(0)` ; le `catch (RuntimeException e)` affiche le nom simple, le message, `getCause()` et le nombre de supprimées.
- **Questions :**
  - Où est passée l'`ArithmeticException` ?
  - Compare avec un try-with-resources (projet 3).
- **Expérience :** `System.exit(0)` dans un `try` : le `finally` s'exécute-t-il ? (À tester dans une classe à part : `Check` interdit `System.exit`.)

---

## Checklist (vérifiée par `Check`)

- `Data.PROGRAMS`, `Data.MAX_STEPS` ;
- `abstract class VmException extends Exception`, `extends VmException`, `class StepLimitException extends RuntimeException` ;
- `record Handler(`, `Deque<Handler>`, `throws VmException` ;
- `catch (VmException | ArithmeticException`, `throw new UncaughtVmException(`, `instanceof VmException` ;
- `catch (StepLimitException | IllegalArgumentException`, `catch (UncaughtVmException` ;
- 2 fois `@SuppressWarnings("finally")`, 3 fois `finally` au moins ;
- `.getSuppressed()`, `.getCause()`.

---

## Sortie attendue complète

```
calcul : affiche [42], pile [42] (5 pas)
division rattrapee : affiche [-2], pile [-2] (7 pas)
throw imbrique : affiche [7, 9], pile [9] (10 pas)
compte a rebours : affiche [3, 2, 1], pile [0] (23 pas)
pile vide : erreur non rattrapee <- StackUnderflowException pile vide (instruction 1) ; affiche [], pile [] (2 pas)
throw perdu : erreur non rattrapee <- ThrownException code 4 ; affiche [], pile [2] (2 pas)
boucle infinie : arret StepLimitException plus de 50 pas
instruction inconnue : arret IllegalArgumentException instruction inconnue : SQUARE
pile vide rattrapee : affiche [-1], pile [-1] (7 pas)
finally : finallyWins 2, valueAlreadyComputed 1, retour du catch [try, catch, finally]
masquee : IllegalStateException (lancee dans finally), cause null, supprimees 0
```
