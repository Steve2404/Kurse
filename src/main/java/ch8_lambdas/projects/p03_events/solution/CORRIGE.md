# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Event`, `EventQueue`, `Bank` et `EventsApp`.
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — La file d'événements

**Le code :** [`Event.java`](Event.java) et [`EventQueue.java`](EventQueue.java).

**Un record qui contient un `Runnable` :** chaque événement transporte **ce qu'il faut faire** quand son heure arrive. La file ne connaît pas la banque : elle trie des actions.

**Le tas binaire :** chaque parent passe avant ses enfants. La racine est donc toujours le prochain événement. Le tableau est rempli **sans trou**, ce qui donne un arbre complet, et les indices `2i + 1`, `2i + 2` et `(i − 1) / 2` suffisent pour naviguer.

**Question — la complexité :** **O(log n)** pour `push` comme pour `pop`. Un élément monte ou descend au plus de la **hauteur** de l'arbre, qui vaut log₂ n. Une liste triée demanderait O(n) pour insérer, et une recherche du minimum à chaque `pop` aussi O(n).

**`seq` départage les égalités :** deux événements à la même minute sortent dans l'ordre où ils ont été **planifiés**. Sans `seq`, le tas ne garantirait aucun ordre entre eux, et la sortie ne serait pas reproductible.

---

## Étape 2 — L'agence

**Le code :** [`Bank.java`](Bank.java).

**Les interfaces fonctionnelles du constructeur :**

| Paramètre | Interface | Méthode | Rôle |
|---|---|---|---|
| `nextArrival`, `nextService` | `IntSupplier` | `int getAsInt()` | tirer un nombre (sans boxing) |
| `names` | `Supplier<String>` | `String get()` | fabriquer un nom |
| `listener` | `BiConsumer<Integer, String>` | `void accept(Integer, String)` | recevoir les notifications |

La banque **ne sait pas** d'où viennent les nombres ni ce que deviennent les messages. Le `main` décide, ce qui la rend facile à tester avec d'autres générateurs.

**`this::arrival`** est une référence de méthode **sur un objet précis** (`this`). Elle donne un `Runnable` qui appellera `arrival()` sur **cette** banque.

**La lambda de fin de service capture `name`**, une variable locale (effectively final). Elle s'exécutera **plus tard**, quand l'événement sortira de la file, mais elle se souviendra du bon client. Elle modifie `served` et `freeTellers`, ce qui est permis car ce sont des **champs**.

**Le tableau circulaire :** `tail % 64` réutilise les cases libérées. `head` et `tail` ne font qu'augmenter, et la file contient toujours `tail − head` clients.

---

## Étape 3 — Le programme

**Le code :** [`EventsApp.java`](EventsApp.java).

**`BiConsumer.andThen` :** `journal.andThen(count).andThen(alarm)` est **un seul** `BiConsumer`, qui appelle les trois dans l'ordre, avec les mêmes arguments. Contrairement à `Function.andThen`, il ne transmet pas de résultat : un `Consumer` n'en a pas.

**Les compteurs dans des `int[]`** (`lines`, `events`, `longWaits`) : les lambdas ne peuvent pas modifier une variable locale. On modifie donc la case d'un tableau, dont la **référence** ne change pas.

**`Supplier<Runnable>`** : un fournisseur qui fournit… une action. `deferred.get()` rend le `Runnable`, et `.run()` l'exécute.

**Question — pourquoi le rapport contient-il les chiffres finaux ?** `report()` ne calcule **rien** : il rend une **lambda**. Le texte n'est construit qu'au moment de `report.get()`, **après** `run()`. La lambda lit alors les champs de la banque (via `this`), qui contiennent l'état final. C'est l'**évaluation paresseuse**. Si `report()` construisait le `String` tout de suite, on lirait l'état **initial**. Vérifié avec les valeurs de départ : `0 clients servis, attente moyenne 0.0 min, max 0 min, file max 0, occupation 0 %, fermeture reelle 0`. Les deux `NaN` (0/0) sont arrondis à 0 par `Math.round`, ce qui cache même l'erreur.
