# Drill de rappel 5 — Commandes, annuler/refaire, observateurs

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p07.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch18_design.drills.r05_commands`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Ni `switch`, ni `instanceof`. `Undo` ne nomme aucune action concrète.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 7 (étapes 2 à 4).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée les fichiers dans l'ordre des défis, `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@FunctionalInterface public interface CounterListener` avec `void changed(int oldValue, int newValue)`, et `public final class Counter` : `int value()` (0 au départ), `void set(int newValue)` qui prévient chaque abonné, `addListener`, `removeListener`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Un abonné qui se désabonne **pendant** qu'on le prévient : pas d'exception, et les autres abonnés sont prévenus.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `public interface Action` (`void apply()`, `void revert()`), `public final class Add implements Action` construite avec `(Counter counter, int amount)`, et `public final class Undo` : `void perform(Action)`, `boolean undo()`, `boolean redo()` (`false` quand il n'y a rien à faire).
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public final class Reset implements Action`, construite avec un `Counter` : remet à 0 et sait revenir à la valeur d'avant.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Une nouvelle action après une annulation rend « refaire » impossible.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `public final class Batch implements Action`, construite avec une `List<Action>` : appliquée dans l'ordre, défaite dans l'ordre **inverse**.
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

- **Observateur** : le sujet garde une `List<CounterListener>` et prévient sur une **copie** : `List.copyOf(listeners).forEach(l -> l.changed(old, newValue));` (sinon `ConcurrentModificationException` quand un abonné se désabonne).
- **Commande** : un objet qui sait `apply` et `revert`. `Add` défait par l'opération inverse ; `Reset` retient la valeur d'avant **à chaque** `apply` (un petit memento).
- **Historique** : deux `Deque<Action>` (`ArrayDeque`). `perform` : appliquer, `push` sur « fait », **vider** « défait ». `undo` : `pop`, `revert`, `push` sur « défait ». `redo` : l'inverse.
- **Macro** : défaire avec une boucle qui **descend** : `for (int i = size - 1; i >= 0; i--)`.

</details>
